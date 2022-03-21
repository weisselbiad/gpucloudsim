package org.cloudbus.cloudsim.gpu.power.models;

import org.cloudbus.cloudsim.power.models.PowerModel;

/**
 * Implements a power model where the power consumption is zeroed out.
 * 
 * @author Ahmad Siavashi
 *
 */
public class GpuHostPowerModelNull implements PowerModel {

	/**
	 * The host will be zeroed out.
	 */
	public GpuHostPowerModelNull() {
	}

	@Override
	public double getPower(double utilization) throws IllegalArgumentException {
		return 0;
	}

}
