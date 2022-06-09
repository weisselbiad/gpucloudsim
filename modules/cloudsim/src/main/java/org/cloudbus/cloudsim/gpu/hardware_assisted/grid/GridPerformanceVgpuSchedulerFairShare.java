package org.cloudbus.cloudsim.gpu.hardware_assisted.grid;

import java.util.List;

import org.cloudbus.cloudsim.gpu.Pgpu;
import org.cloudbus.cloudsim.gpu.Vgpu;
import org.cloudbus.cloudsim.gpu.VgpuScheduler;
import org.cloudbus.cloudsim.gpu.performance.PerformanceScheduler;
import org.cloudbus.cloudsim.gpu.performance.models.PerformanceModel;
import org.cloudbus.cloudsim.gpu.selection.PgpuSelectionPolicy;

/**
 * * {@link GridPerformanceVgpuSchedulerFairShare} extends
 * {@link org.cloudbus.cloudsim.gpu.VgpuSchedulerFairShare VgpuSchedulerFairShare}
 * to add support for
 * {@link org.cloudbus.cloudsim.gpu.performance.models.PerformanceModel
 * PerformanceModels}.
 * 
 * @author Ahmad Siavashi
 * 
 */
public class GridPerformanceVgpuSchedulerFairShare extends GridVgpuSchedulerFairShare implements PerformanceScheduler<Vgpu> {
	/** The performance model */
	private PerformanceModel<VgpuScheduler, Vgpu> performanceModel;

	/**
	 * @see org.cloudbus.cloudsim.gpu.VgpuSchedulerFairShare#VgpuSchedulerFairShare(int,
	 *      List, PgpuSelectionPolicy) VgpuSchedulerFairShare(int, List,
	 *      PgpuSelectionPolicy)
	 * 
	 * @param performanceModel
	 *            the performance model
	 */
	public GridPerformanceVgpuSchedulerFairShare(String videoCardType, List<Pgpu> pgpuList,
			PgpuSelectionPolicy pgpuSelectionPolicy, PerformanceModel<VgpuScheduler, Vgpu> performanceModel) {
		super(videoCardType, pgpuList, pgpuSelectionPolicy);
		this.performanceModel = performanceModel;
	}

	@Override
	public List<Double> getAvailableMips(Vgpu vgpu, List<Vgpu> vgpuList) {
		return this.performanceModel.getAvailableMips(this, vgpu, vgpuList);
	}
}
