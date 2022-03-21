package org.cloudbus.cloudsim.gpu.power;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.cloudbus.cloudsim.gpu.Pgpu;
import org.cloudbus.cloudsim.gpu.Vgpu;
import org.cloudbus.cloudsim.gpu.VgpuScheduler;
import org.cloudbus.cloudsim.gpu.VideoCard;
import org.cloudbus.cloudsim.gpu.performance.PerformanceScheduler;
import org.cloudbus.cloudsim.gpu.power.models.VideoCardPowerModel;
import org.cloudbus.cloudsim.gpu.provisioners.VideoCardBwProvisioner;
import org.cloudbus.cloudsim.lists.PeList;

/**
 * {@link PowerVideoCard} extends {@link VideoCard} to enable the simulation of
 * power-aware {@link VideoCard VideoCards}.
 * 
 * @author Ahmad Siavashi
 * 
 */
public class PowerVideoCard extends VideoCard {

	/** The power model associated with this video card. */
	private VideoCardPowerModel powerModel;

	/**
	 * @see VideoCard#VideoCard(int, int, VgpuScheduler, VideoCardBwProvisioner)
	 *      VideoCard(int, int, VgpuScheduler, VideoCardBwProvisioner)
	 * @param powerModel the power model associated with the video card
	 */
	public <T extends VgpuScheduler & PerformanceScheduler<Vgpu>> PowerVideoCard(int id, String type, T vgpuScheduler,
			VideoCardBwProvisioner pcieBandwidthProvisioner, VideoCardPowerModel powerModel) {
		super(id, type, vgpuScheduler, pcieBandwidthProvisioner);
		setPowerModel(powerModel);
	}

	protected double getCurrentPCIeBwUtilization() {
		return ((double) getPCIeBandwidthProvisioner().getAvailableBw()) / getPCIeBandwidthProvisioner().getBw();
	}

	/**
	 * Returns GDDRAM utilization of each Pgpu in the VideoCard.
	 * 
	 * @return memory utilization for each available Pgpu in the VideoCard
	 */
	protected Map<Pgpu, Double> getCurrentGddramUtilization() {
		Map<Pgpu, Double> usages = new HashMap<Pgpu, Double>();
		for (final Entry<Pgpu, List<Vgpu>> entry : getVgpuScheduler().getPgpuVgpuMap().entrySet()) {
			final Pgpu pgpu = entry.getKey();
			Double usage = 1.0 - ((double) pgpu.getGddramProvisioner().getAvailableGddram())
					/ pgpu.getGddramProvisioner().getGddram();
			usages.put(pgpu, usage);
		}
		return usages;
	}

	/**
	 * Returns the current utilization of Pgpus in the VideoCard.
	 * 
	 * @return current utilization of Pgpus in the VideoCard
	 **/
	protected Map<Pgpu, Double> getCurrentMipsUtilization() {
		Map<Pgpu, Double> utilizations = new HashMap<Pgpu, Double>();
		for (final Entry<Pgpu, List<Vgpu>> entry : getVgpuScheduler().getPgpuVgpuMap().entrySet()) {
			final Pgpu pgpu = entry.getKey();
			final int pgpuTotalMips = PeList.getTotalMips(pgpu.getPeList());
			List<Vgpu> vgpus = entry.getValue();
			Double currentRequestedMips = 0.0;
			for (final Vgpu vgpu : vgpus) {
				currentRequestedMips += vgpu.getCurrentRequestedTotalMips();
			}
			Double utilization = currentRequestedMips / pgpuTotalMips;
			utilizations.put(pgpu, utilization);
		}
		return utilizations;
	}

	/**
	 * Returns the current power consumption of the VideoCard
	 * 
	 * @return current power consumption of the VideoCard
	 */
	public double getPower() {
		Map<Pgpu, Double> gpuUtilization = getCurrentMipsUtilization();
		Map<Pgpu, Double> gddramUtilization = getCurrentGddramUtilization();
		double pcieBwUtilization = getCurrentPCIeBwUtilization();
		return getPowerModel().getPower(gpuUtilization, gddramUtilization, pcieBwUtilization);
	}

	/**
	 * @return the powerModel
	 */
	public VideoCardPowerModel getPowerModel() {
		return powerModel;
	}

	/**
	 * @param powerModel the powerModel to set
	 */
	protected void setPowerModel(VideoCardPowerModel powerModel) {
		this.powerModel = powerModel;
	}

}
