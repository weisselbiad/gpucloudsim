package org.cloudbus.cloudsim.gpu;

import java.util.Random;

import org.cloudbus.cloudsim.UtilizationModel;

public class UtilizationModelGaussian implements UtilizationModel{

	private Random random;
	private double mean;
	private double std;
	
	public UtilizationModelGaussian(double mean, double std) {
		setMean(mean);
		setStd(std);
		setRandom(new Random());
	}
	
	public UtilizationModelGaussian(double mean, double std, long seed) {
		setMean(mean);
		setStd(std);
		setRandom(new Random(seed));
	}
	
	public UtilizationModelGaussian(long seed) {
		this(0, 1, seed);
	}
	
	public UtilizationModelGaussian() {
		this(0, 1);
	}

	@Override
	public double getUtilization(double time) {
		return getRandom().nextGaussian() * getStd() + getMean();
	}

	public Random getRandom() {
		return random;
	}

	protected void setRandom(Random random) {
		this.random = random;
	}

	public double getMean() {
		return mean;
	}

	protected void setMean(double mean) {
		this.mean = mean;
	}

	public double getStd() {
		return std;
	}

	protected void setStd(double std) {
		this.std = std;
	}
	
}
