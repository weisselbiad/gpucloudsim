package org.cloudbus.cloudsim.gpu.power.models;

import java.util.Map;
import java.util.Map.Entry;

import org.cloudbus.cloudsim.gpu.Pgpu;

/**
 * Implements a power model where the power consumption is linear to resource
 * usage and frequency.
 * 
 * @author Ahmad Siavashi
 * 
 */
public class VideoCardPowerModelLinear implements VideoCardPowerModel {

	private boolean powerGate;
	private double a3, a2, a1, a0;
	private int frequency;

	/**
	 * A power model where the power consumption is linear to resource usage and
	 * frequency.
	 * 
	 * @param frequency
	 *            nominal frequency of the device in MHz
	 * @param a3
	 *            constant parameter of the model
	 * @param a2
	 *            constant parameter of the model
	 * 
	 * @param a1
	 *            constant parameter of the model
	 * 
	 * @param a0
	 *            constant parameter of the model
	 * 
	 * @param powerGate
	 *            power gates individual GPUs if they are idle
	 */
	public VideoCardPowerModelLinear(int frequency, double a3, double a2, double a1, double a0, boolean powerGate) {
		this.powerGate = powerGate;
		this.frequency = frequency;
		this.a3 = a3;
		this.a2 = a2;
		this.a1 = a1;
		this.a0 = a0;
	}

	@Override
	public double getPower(Map<Pgpu, Double> pgpuUtilization, Map<Pgpu, Double> gddramUtilization,
			double bwUtilization) {
		double totalVideoCardPower = 0.0;
		for (Entry<Pgpu, Double> entry : pgpuUtilization.entrySet()) {
			Double utilization = entry.getValue();
			double pgpuPower = 0.0;
			if (!this.powerGate || this.powerGate && utilization > 0.0) {
				pgpuPower = powerFunction(frequency, utilization);
			}
			totalVideoCardPower += pgpuPower;
		}
		return totalVideoCardPower;
	}

	/**
	 * 
	 * @param f
	 *            pgpu frequency
	 * @param u
	 *            pgpu utilization
	 * @return power consumption for the given f and u
	 */
	protected double powerFunction(double f, double u) {
		u *= 100;
		double power = a3 * f * u + a2 * f + a1 * u + a0;
		return power;
	}
}
