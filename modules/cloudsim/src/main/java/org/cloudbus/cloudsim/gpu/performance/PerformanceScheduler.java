package org.cloudbus.cloudsim.gpu.performance;

import java.util.List;

/**
 * Schedulers implement this interface to add support for
 * {@link org.cloudbus.cloudsim.gpu.performance.models.PerformanceModel
 * PerformanceModel}.
 * 
 * @see org.cloudbus.cloudsim.gpu.performance.models.PerformanceModel
 *      PerformanceModel
 * @see PerformanceGpuHost
 * 
 * @author Ahmad Siavashi
 *
 * @param <T>
 *            generic variable for the entity that is being scheduled (e.g.
 *            GpuVm or Vgpu)
 */
public interface PerformanceScheduler<T> {
	/**
	 * Returns the available Mips for the given entity, considering runtime effects
	 * of other running entities
	 * 
	 * @param entity
	 *            the entity (e.g. Vgpu or GpuVm)
	 * @param entities
	 *            entities that their execution potentially interfere with the
	 *            execution of <B>entity</B>
	 */
	public List<Double> getAvailableMips(T entity, List<T> entities);
}
