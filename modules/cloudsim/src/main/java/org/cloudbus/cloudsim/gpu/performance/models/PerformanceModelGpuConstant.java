/**
 * 
 */
package org.cloudbus.cloudsim.gpu.performance.models;

import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.gpu.Vgpu;
import org.cloudbus.cloudsim.gpu.VgpuScheduler;

/**
 * {@link PerformanceModelGpuConstant} imposes a constant performance
 * degradation on all Vgpus; whether they reside on the same Pgpu or not.
 * However, pass-through vgpus are able to get the full performance of the
 * underlying Pgpu.
 * 
 * @author Ahmad Siavashi
 * 
 */
public class PerformanceModelGpuConstant implements PerformanceModel<VgpuScheduler, Vgpu> {

	protected final double gain;

	/**
	 * This class imposes a constant performance degradation on non-pass through
	 * vgpus.
	 * 
	 * @param performanceLoss
	 *            should be a number in [0,1] interval.
	 */
	public PerformanceModelGpuConstant(double performanceLoss) {
		this.gain = 1 - performanceLoss;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudbus.cloudsim.gpu.interference.models.PerformanceModel#
	 * getAvailableMips(org.cloudbus.cloudsim.gpu.GpuHost, java.util.List)
	 */
	@Override
	public List<Double> getAvailableMips(VgpuScheduler scheduler, Vgpu vgpu, List<Vgpu> vgpus) {
		List<Double> allocatedMips = scheduler.getAllocatedMipsForVgpu(vgpu);
		List<Double> availableMips = new ArrayList<Double>(allocatedMips.size());
		for (Double mips : allocatedMips) {
			availableMips.add(this.gain * mips);
		}
		return availableMips;
	}
}
