package org.cloudbus.cloudsim.gpu.hardware_assisted.grid;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.cloudbus.cloudsim.gpu.Pgpu;
import org.cloudbus.cloudsim.gpu.power.models.VideoCardPowerModel;

/**
 * Implements a process variation-aware power model for NVIDIA GRID K1 video card where the power consumption is linear to resource
 * usage and frequency.
 * 
 * @author Ahmad Siavashi
 * 
 */
public class GridVideoCardPowerModelK1 implements VideoCardPowerModel {

	private Map<Pgpu, Double> pgpuScaleFactorMap;
	private boolean powerGate;

	/**
	 * A power model for NVIDIA GRID K1 video card where the power consumption is
	 * linear to resource usage and frequency.
	 * 
	 * @param powerGate
	 *            indicates whether VideoCard's Pgpus are power gated when they are
	 *            idle or not
	 */
	public GridVideoCardPowerModelK1(boolean powerGate) {
		this.pgpuScaleFactorMap = new HashMap<>();
		this.powerGate = powerGate;
	}

	@Override
	public double getPower(Map<Pgpu, Double> pgpuUtilization, Map<Pgpu, Double> gddramUtilization,
			double bwUtilization) {
		double totalVideoCardPower = 0.0;
		for (Entry<Pgpu, Double> entry : pgpuUtilization.entrySet()) {
			Pgpu pgpu = entry.getKey();
			if (!pgpuScaleFactorMap.containsKey(pgpu)) {
				double gridK1Frequency = 850;
				double pgpuFrequency = GridVideoCardTags.getGpuPeFrequencyFromMips(GridVideoCardTags.NVIDIA_K1_CARD,
						pgpu.getPeList().get(0).getMips());
				double scaleFactor = (pgpuFrequency - gridK1Frequency) / gridK1Frequency;
				scaleFactor = Math.exp(scaleFactor);
				pgpuScaleFactorMap.put(pgpu, scaleFactor);
			}
			Double utilization = entry.getValue();
			double pgpuPower = 0.0;
			if (!this.powerGate || this.powerGate && utilization > 0.0) {
				pgpuPower = powerFunction(GridVideoCardTags.getGpuPeFrequencyFromMips(GridVideoCardTags.NVIDIA_K1_CARD,
						pgpu.getPeList().get(0).getMips()), utilization);
			}
			pgpuPower *= pgpuScaleFactorMap.get(pgpu);
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
		double a0 = -0.8353;
		double a1 = 0.0873;
		double a2 = 0.0071;
		double a3 = 0.0002175;
		u *= 100;
		double power = a3 * f * u + a2 * f + a1 * u + a0;
		return power;
	}
}
