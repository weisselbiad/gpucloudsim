/**
 * 
 */
package org.cloudbus.cloudsim.gpu.performance.models;

import java.util.List;

/**
 * GpuVms/Vgpus that share resources of a GpuHost/Pgpu may experience
 * performance degradation due to hardware conflicts or virtualization
 * overheads. Classes that implement this interface, model performance gains for
 * co-running GpuVms/Vgpus.
 * 
 * @author Ahmad Siavashi
 * 
 */
public interface PerformanceModel<T, K> {

	/**
	 * Returns the available mips for a GpuVm/Vgpu when running with other
	 * GpuVms/Vgpus on a GpuHost/Pgpu.
	 * 
	 * @param scheduler
	 *            the scheduler of the entities (i.e. VgpuScheduler or
	 *            GpuVmScheduler)
	 * @param entity
	 *            the entity (i.e. Vgpu or GpuVm) for which we wants to evaluate
	 *            available mips
	 * @param entities
	 *            list of entities (i.e Vgpus or GpuVms), excluding the <b>entity</b> itself
	 * @return the available mips for the entity considering possible performance
	 *         degredation
	 */
	public List<Double> getAvailableMips(T scheduler, K entity, List<K> entities);

}
