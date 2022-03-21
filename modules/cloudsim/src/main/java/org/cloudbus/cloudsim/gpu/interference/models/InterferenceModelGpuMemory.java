/**
 * 
 */
package org.cloudbus.cloudsim.gpu.interference.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.gpu.ResGpuTask;

/**
 * {@link InterferenceModelGpuMemory} implements the {@link InterferenceModel}
 * interface. The class considers the GDDRAM bandwidth as the only cause for
 * interference. For a Vgpu, the GDDRAM bandwidth of running GpuTasks are used
 * to evaluate the inter-process interference.
 * 
 * @author Ahmad Siavashi
 * 
 */
public class InterferenceModelGpuMemory implements InterferenceModel<ResGpuTask> {

	/**
	 * This class uses gddram bandwidth requests of vgpu's running gpuTasks to
	 * evaluate the inter-process interference. The available Mips for the execution
	 * of each gpuTask is calculated considering the possible slowdown resulting
	 * from the interference.
	 */
	public InterferenceModelGpuMemory() {
	}

	@Override
	public List<Double> getAvailableMips(ResGpuTask rcl, List<Double> mipsShare, List<ResGpuTask> execList) {
		List<Double> allocatedMips = new ArrayList<Double>(Collections.nCopies(mipsShare.size(), 0.0));
		for (Integer peId : rcl.getPeIdList()) {
			allocatedMips.set(peId, mipsShare.get(peId));
		}
		double totalMemoryUtilization = 0.0;
		for (ResGpuTask rgt : execList) {
			totalMemoryUtilization += rgt.getGpuTask().getUtilizationOfGddram(CloudSim.clock());
		}
		if (totalMemoryUtilization <= 1) {
			return allocatedMips;
		}
		double scaleFactor = 1.0 / totalMemoryUtilization;
		for (int i = 0; i < allocatedMips.size(); i++) {
			allocatedMips.set(i, allocatedMips.get(i) * scaleFactor);
		}
		return allocatedMips;
	}
}
