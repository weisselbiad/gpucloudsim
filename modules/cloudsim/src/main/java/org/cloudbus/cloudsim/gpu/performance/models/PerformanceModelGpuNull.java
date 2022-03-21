/**
 * 
 */
package org.cloudbus.cloudsim.gpu.performance.models;

import java.util.List;

import org.cloudbus.cloudsim.gpu.Vgpu;
import org.cloudbus.cloudsim.gpu.VgpuScheduler;

/**
 * {@link PerformanceModelGpuNull} does not impose any performance
 * degradation on Vgpus sharing a Pgpu.
 * 
 * @author Ahmad Siavashi
 * 
 */
public class PerformanceModelGpuNull implements PerformanceModel<VgpuScheduler, Vgpu> {

	/**
	 * This class does not impose any performance degradation on Vgpus sharing a Pgpu.
	 */
	public PerformanceModelGpuNull() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudbus.cloudsim.gpu.interference.models.PerformanceModel#
	 * getAvailableMips(org.cloudbus.cloudsim.gpu.GpuHost, java.util.List)
	 */
	@Override
	public List<Double> getAvailableMips(VgpuScheduler scheduler, Vgpu vgpu, List<Vgpu> vgpus) {
		return scheduler.getAllocatedMipsForVgpu(vgpu);
	}

}
