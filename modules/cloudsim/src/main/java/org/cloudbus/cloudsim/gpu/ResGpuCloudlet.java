package org.cloudbus.cloudsim.gpu;

import org.cloudbus.cloudsim.ResCloudlet;

public class ResGpuCloudlet extends ResCloudlet {

	private final GpuTask gpuTask;

	public ResGpuCloudlet(GpuCloudlet cloudlet) {
		super(cloudlet);
		this.gpuTask = cloudlet.getGpuTask();
	}

	public ResGpuCloudlet(GpuCloudlet cloudlet, long startTime, int duration, int reservID) {
		super(cloudlet, startTime, duration, reservID);
		this.gpuTask = cloudlet.getGpuTask();
	}
	
	public GpuCloudlet finishCloudlet() {
		setCloudletStatus(GpuCloudlet.SUCCESS);
		finalizeCloudlet();
		return (GpuCloudlet) getCloudlet();
	}

	public GpuTask getGpuTask() {
		return gpuTask;
	}

	public boolean hasGpuTask() {
		if (getGpuTask() != null) {
			return true;
		}
		return false;
	}

}
