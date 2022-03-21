/**
 * 
 */
package org.cloudbus.cloudsim.gpu.interference.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cloudbus.cloudsim.gpu.ResGpuTask;

/**
 * This class represents a simple interference model in which simultaneous
 * execution of multiple tasks inside a Vgpu results in no slowdown.
 * 
 * @author Ahmad Siavashi
 * 
 */
public class InterferenceModelNull implements InterferenceModel<ResGpuTask> {

	/**
	 * A simple interference model in which simultaneous execution of multiple tasks
	 * inside a Vgpu results in no slowdown.
	 */
	public InterferenceModelNull() {
	}

	@Override
	public List<Double> getAvailableMips(ResGpuTask rcl, List<Double> mipsShare, List<ResGpuTask> execList) {
		List<Double> allocatedMips = new ArrayList<Double>(Collections.nCopies(mipsShare.size(), 0.0));
		for (Integer peId : rcl.getPeIdList()) {
			allocatedMips.set(peId, mipsShare.get(peId));
		}
		return allocatedMips;
	}
}
