package org.cloudbus.cloudsim.gpu;

/**
 * 
 * @author Ahmad Siavashi
 *
 */

public interface GpuCloudletScheduler {
	public boolean hasGpuTask();
	public GpuTask getNextGpuTask();
	public boolean notifyGpuTaskCompletion(GpuTask gt);
}
