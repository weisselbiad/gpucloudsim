package org.cloudbus.cloudsim.gpu;

import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerSpaceShared;
import org.cloudbus.cloudsim.ResCloudlet;
import org.cloudbus.cloudsim.core.CloudSim;

/**
 * {@link GpuCloudletSchedulerSpaceShared} extends
 * {@link CloudletSchedulerSpaceShared} to schedule {@link GpuCloudlet}s.
 * 
 * @author Ahmad Siavashi
 * 
 */
public class GpuCloudletSchedulerSpaceShared extends CloudletSchedulerSpaceShared implements GpuCloudletScheduler {

	private List<GpuTask> gpuTaskList;

	/**
	 * Space Shared scheduler for GpuCloudlets. Assumes all PEs have same MIPS
	 * capacity.
	 */
	public GpuCloudletSchedulerSpaceShared() {
		super();
		setGpuTaskList(new ArrayList<GpuTask>());
	}

	@Override
	public double cloudletSubmit(Cloudlet cloudlet, double fileTransferTime) {
		// it can go to the exec list
		if ((currentCpus - usedPes) >= cloudlet.getNumberOfPes()) {
			ResGpuCloudlet rgcl = new ResGpuCloudlet((GpuCloudlet) cloudlet);
			rgcl.setCloudletStatus(Cloudlet.INEXEC);
			for (int i = 0; i < cloudlet.getNumberOfPes(); i++) {
				rgcl.setMachineAndPeId(0, i);
			}
			getCloudletExecList().add(rgcl);
			usedPes += cloudlet.getNumberOfPes();
		} else {// no enough free PEs: go to the waiting queue
			ResGpuCloudlet rgcl = new ResGpuCloudlet((GpuCloudlet) cloudlet);
			rgcl.setCloudletStatus(Cloudlet.QUEUED);
			getCloudletWaitingList().add(rgcl);
			return 0.0;
		}

		// calculate the expected time for cloudlet completion
		double capacity = 0.0;
		int cpus = 0;
		for (Double mips : getCurrentMipsShare()) {
			capacity += mips;
			if (mips > 0) {
				cpus++;
			}
		}

		currentCpus = cpus;
		capacity /= cpus;

		// use the current capacity to estimate the extra amount of
		// time to file transferring. It must be added to the cloudlet length
		double extraSize = capacity * fileTransferTime;
		long length = cloudlet.getCloudletLength();
		length += extraSize;
		cloudlet.setCloudletLength(length);
		return cloudlet.getCloudletLength() / capacity;
	}

	@Override
	public void cloudletFinish(ResCloudlet rcl) {
		ResGpuCloudlet rgcl = (ResGpuCloudlet) rcl;
		if (!rgcl.hasGpuTask()) {
			super.cloudletFinish(rcl);
		} else {
			GpuTask gt = rgcl.getGpuTask();
			getGpuTaskList().add(gt);
			try {
				rgcl.setCloudletStatus(GpuCloudlet.PAUSED);
				getCloudletPausedList().add(rgcl);
			} catch (Exception e) {
				e.printStackTrace();
				CloudSim.abruptallyTerminate();
			}
			usedPes -= rgcl.getNumberOfPes();
		}
	}

	protected List<GpuTask> getGpuTaskList() {
		return gpuTaskList;
	}

	protected void setGpuTaskList(List<GpuTask> gpuTaskList) {
		this.gpuTaskList = gpuTaskList;
	}

	@Override
	public boolean hasGpuTask() {
		return !getGpuTaskList().isEmpty();
	}

	@Override
	public GpuTask getNextGpuTask() {
		if (hasGpuTask()) {
			return getGpuTaskList().remove(0);
		}
		return null;
	}

	@Override
	public boolean notifyGpuTaskCompletion(GpuTask gt) {
		for (ResCloudlet rcl : getCloudletPausedList()) {
			ResGpuCloudlet rgcl = (ResGpuCloudlet) rcl;
			if (rgcl.getGpuTask() == gt) {
				rgcl.setCloudletStatus(GpuCloudlet.SUCCESS);
				rgcl.finalizeCloudlet();
				return true;
			}
		}
		return false;
	}

}
