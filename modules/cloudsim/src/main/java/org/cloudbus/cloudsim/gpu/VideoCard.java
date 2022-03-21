package org.cloudbus.cloudsim.gpu;

import org.cloudbus.cloudsim.gpu.provisioners.VideoCardBwProvisioner;

/**
 * Represents a physical video card attached to a host
 * 
 * @author Ahmad Siavashi
 * 
 */
public class VideoCard {

	/** identifier */
	private int id;

	/** Video Card Type */
	private String type;

	/** Vgpu Scheduler */
	private VgpuScheduler vgpuScheduler;

	/** PCIe BW Provisioner */
	private VideoCardBwProvisioner pcieBandwidthProvisioner;

	public VideoCard(int id, String type, VgpuScheduler vgpuScheduler,
			VideoCardBwProvisioner pcieBandwidthProvisioner) {
		setId(id);
		setType(type);
		setVgpuScheduler(vgpuScheduler);
		setPCIeBandwidthProvisioner(pcieBandwidthProvisioner);
	}

	public int getId() {
		return id;
	}

	protected void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	protected void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the vgpuScheduler
	 */
	public VgpuScheduler getVgpuScheduler() {
		return vgpuScheduler;
	}

	/**
	 * @param vgpuScheduler the vgpuScheduler to set
	 */
	protected void setVgpuScheduler(VgpuScheduler vgpuScheduler) {
		this.vgpuScheduler = vgpuScheduler;
	}

	/**
	 * @return the pcieBandwidth
	 */
	public VideoCardBwProvisioner getPCIeBandwidthProvisioner() {
		return pcieBandwidthProvisioner;
	}

	/**
	 * @param pcieBandwidth the pcieBandwidth to set
	 */
	protected void setPCIeBandwidthProvisioner(VideoCardBwProvisioner pcieBandwidthProvisioner) {
		this.pcieBandwidthProvisioner = pcieBandwidthProvisioner;
	}

}
