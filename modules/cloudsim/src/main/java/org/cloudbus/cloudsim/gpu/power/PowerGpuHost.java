package org.cloudbus.cloudsim.gpu.power;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.VmScheduler;
import org.cloudbus.cloudsim.gpu.GpuVm;
import org.cloudbus.cloudsim.gpu.VideoCard;
import org.cloudbus.cloudsim.gpu.allocation.VideoCardAllocationPolicy;
import org.cloudbus.cloudsim.gpu.performance.PerformanceGpuHost;
import org.cloudbus.cloudsim.gpu.power.models.GpuHostPowerModelNull;
import org.cloudbus.cloudsim.power.models.PowerModel;
import org.cloudbus.cloudsim.provisioners.BwProvisioner;
import org.cloudbus.cloudsim.provisioners.RamProvisioner;

/**
 * {@link PowerGpuHost} extends {@link PerformanceGpuHost} to represent a
 * power-aware host.
 * 
 * @author Ahmad Siavashi
 * 
 */
public class PowerGpuHost extends PerformanceGpuHost {

	/** The power model associated with this host (video cards excluded) */
	private PowerModel powerModel;

	/**
	 * 
	 * @see PerformanceGpuHost#PerformanceGpuHost(int, int, RamProvisioner,
	 *      BwProvisioner, long, List, VmScheduler, VideoCardAllocationPolicy)
	 *      erformanceGpuHost(int, int, RamProvisioner, BwProvisioner, long, List,
	 *      VmScheduler, VideoCardAllocationPolicy)
	 * @param powerModel
	 *            the power model associated with the host (video cards have their
	 *            own power models)
	 */
	public PowerGpuHost(int id, String type, RamProvisioner ramProvisioner, BwProvisioner bwProvisioner, long storage,
			List<? extends Pe> peList, VmScheduler vmScheduler, VideoCardAllocationPolicy videoCardAllocationPolicy,
			PowerModel powerModel) {
		super(id, type, ramProvisioner, bwProvisioner, storage, peList, vmScheduler, videoCardAllocationPolicy);
		setPowerModel(powerModel);
	}
	
	/**
	 * 
	 * @see PerformanceGpuHost#PerformanceGpuHost(int, int, RamProvisioner,
	 *      BwProvisioner, long, List, VmScheduler, VideoCardAllocationPolicy)
	 *      erformanceGpuHost(int, int, RamProvisioner, BwProvisioner, long, List,
	 *      VmScheduler, VideoCardAllocationPolicy)
	 */
	public PowerGpuHost(int id, String type, RamProvisioner ramProvisioner, BwProvisioner bwProvisioner, long storage,
			List<? extends Pe> peList, VmScheduler vmScheduler, VideoCardAllocationPolicy videoCardAllocationPolicy) {
		super(id, type, ramProvisioner, bwProvisioner, storage, peList, vmScheduler, videoCardAllocationPolicy);
		setPowerModel(new GpuHostPowerModelNull());
	}

	/**
	 * 
	 * @see PerformanceGpuHost#PerformanceGpuHost(int, int, RamProvisioner,
	 *      BwProvisioner, long, List, VmScheduler) erformanceGpuHost(int, int,
	 *      RamProvisioner, BwProvisioner, long, List, VmScheduler)
	 */
	public PowerGpuHost(int id, String type, RamProvisioner ramProvisioner, BwProvisioner bwProvisioner, long storage,
			List<? extends Pe> peList, VmScheduler vmScheduler, PowerModel powerModel) {
		super(id, type, ramProvisioner, bwProvisioner, storage, peList, vmScheduler);
		setPowerModel(powerModel);
	}
	
	/**
	 * 
	 * @see PerformanceGpuHost#PerformanceGpuHost(int, int, RamProvisioner,
	 *      BwProvisioner, long, List, VmScheduler) erformanceGpuHost(int, int,
	 *      RamProvisioner, BwProvisioner, long, List, VmScheduler)
	 */
	public PowerGpuHost(int id, String type, RamProvisioner ramProvisioner, BwProvisioner bwProvisioner, long storage,
			List<? extends Pe> peList, VmScheduler vmScheduler) {
		super(id, type, ramProvisioner, bwProvisioner, storage, peList, vmScheduler);
		setPowerModel(new GpuHostPowerModelNull());
	}

	/**
	 * Returns the current total utilization of host's CPUs.
	 * 
	 * @return total utilization of host CPUs
	 **/
	@SuppressWarnings("unchecked")
	protected double getCurrentCpuUtilization() {
		double totalRequestedMips = 0.0;
		for (GpuVm vm : (List<GpuVm>) (List<?>) getVmList()) {
			totalRequestedMips += vm.getCurrentRequestedTotalMips();
		}
		return totalRequestedMips / getTotalMips();
	}

	/**
	 * Returns the current total power consumption of the host (CPUs + GPUs).
	 * 
	 * @return current total power consumption of the host
	 */
	public double getCurrentTotalPower() {
		double totalPower = 0;
		totalPower += getCurrentHostCpuPower();
		for (Double videoCardPower : getCurrentVideoCardsPower().values()) {
			totalPower += videoCardPower;
		}
		return totalPower;
	}

	/**
	 * Returns the current power consumption of host's CPUs
	 * 
	 * @return current power consumption of host's CPUs
	 */
	public double getCurrentHostCpuPower() {
		return getPowerModel().getPower(getCurrentCpuUtilization());
	}

	/**
	 * Returns the current power consumption of host's video cards
	 * 
	 * @return the current power consumption of host's video cards
	 */
	public Map<VideoCard, Double> getCurrentVideoCardsPower() {
		Map<VideoCard, Double> videoCardsPower = new HashMap<VideoCard, Double>();
		if (getVideoCardAllocationPolicy() != null) {
			for (VideoCard videoCard : getVideoCardAllocationPolicy().getVideoCards()) {
				PowerVideoCard powerVideoCard = (PowerVideoCard) videoCard;
				videoCardsPower.put(powerVideoCard, powerVideoCard.getPower());
			}
		}
		return videoCardsPower;
	}

	/**
	 * @return the powerModel
	 */
	public PowerModel getPowerModel() {
		return powerModel;
	}

	/**
	 * @param powerModel
	 *            the powerModel to set
	 */
	protected void setPowerModel(PowerModel powerModel) {
		this.powerModel = powerModel;
	}

}
