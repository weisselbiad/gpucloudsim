package org.cloudbus.cloudsim.gpu.interference.models;

import java.util.List;

/**
 * The InterferenceModel interface needs to be implemented in order to provide a
 * model for inter-process interference. GpuTasks/Cloudlets running on a
 * Vgpu/GpuVm, may experience performance degradation, depending on utilization
 * of resources, when accessing shared resources.
 * 
 * @author Ahmad Siavashi
 * 
 */
public interface InterferenceModel<T> {

	/**
	 * Returns the MIPS available for the Cloudlet/GpuTask when running with other
	 * Cloudlets/GpuTasks.
	 * 
	 * @param rcl
	 *            the submitted cloudlet/task for which we evaluate the slowdown.
	 * @param mipsShare
	 *            availableMips to the scheduler
	 * @param execList
	 *            list of running cloudlets/tasks, including <B>rcl</B>
	 * @return available mips for the cloudlet/task, considering the possible
	 *         slowdown.
	 */
	public List<Double> getAvailableMips(T rcl, List<Double> mipsShare, List<T> execList);
}
