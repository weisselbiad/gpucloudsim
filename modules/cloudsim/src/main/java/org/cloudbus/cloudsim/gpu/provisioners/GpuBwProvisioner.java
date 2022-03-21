package org.cloudbus.cloudsim.gpu.provisioners;

import java.util.HashMap;
import java.util.Map;

import org.cloudbus.cloudsim.gpu.Vgpu;

/**
 * GpuBwProvisioner is an abstract class that represents the provisioning policy
 * used by a Pgpu to allocate GDDRAM bandwidth (bw) to Vgpus inside it.
 * 
 * @author Ahmad Siavashi
 *
 */
public abstract class GpuBwProvisioner {

	/**
	 * The total bandwidth capacity from the pgpu that the provisioner can allocate
	 * to Vgpus.
	 */
	private long bw;

	/** vgpu-bw mapping */
	private Map<Vgpu, Long> vgpuBwMap;

	/** vgpu-requested bw mapping */
	private Map<Vgpu, Long> vgpuRequestedBwMap;

	/**
	 * Creates the new GpuBwProvisioner.
	 * 
	 * @param bw
	 *            The total bandwidth capacity from the pgpu that the provisioner
	 *            can allocate to vgpus.
	 * 
	 * @pre bw >= 0
	 * @post $none
	 */
	public GpuBwProvisioner(long bw) {
		setBw(bw);
		setVgpuBwMap(new HashMap<Vgpu, Long>());
		setVgpuRequestedBwMap(new HashMap<Vgpu, Long>());
	}

	/**
	 * Allocates GDDRAM BW for a given Vgpu.
	 * 
	 * @param vgpu
	 *            the vgpu for which the bw are being allocated
	 * @param bw
	 *            the bw to be allocated to the vgpu
	 * 
	 * @return $true if the bw could be allocated; $false otherwise
	 * 
	 * @pre $none
	 * @post $none
	 */
	public abstract boolean allocateBwForVgpu(Vgpu vgpu, long bw);

	/**
	 * Gets the allocated GDDRAM BW for Vgpu.
	 * 
	 * @param vgpu
	 *            the vgpu
	 * 
	 * @return the allocated GDDRAM BW for vgpu
	 */
	public long getAllocatedBwForVgpu(Vgpu vgpu) {
		return getVgpuBwMap().get(vgpu).longValue();
	}

	/**
	 * Releases GDDRAM BW used by a vgpu.
	 * 
	 * @param vgpu
	 *            the vgpu
	 * 
	 * @pre $none
	 * @post none
	 */
	public void deallocateBwForVgpu(Vgpu vgpu) {
		getVgpuBwMap().remove(vgpu);
		vgpu.setCurrentAllocatedBw(0);
	}

	/**
	 * Releases GDDRAM BW used by all Vgpus.
	 * 
	 * @pre $none
	 * @post none
	 */
	public void deallocateBwForAllVgpus() {
		getVgpuBwMap().clear();
	}

	/**
	 * Checks if it is possible to change the current allocated GDDRAM BW for the
	 * Vgpu to a new amount, depending on the available BW.
	 * 
	 * @param vgpu
	 *            the vgpu to check if there is enough available GDDRAM BW on the
	 *            Pgpu to change the Vgpu allocated GDDRAM BW
	 * @param bw
	 *            the new total amount of GDDRAM BW for the Vgpu.
	 * 
	 * @return true, if is suitable for vgpu
	 */
	public abstract boolean isSuitableForVgpu(Vgpu vgpu, long bw);

	/**
	 * Gets the GDDRAM bw capacity.
	 * 
	 * @return the bw capacity
	 */
	public long getBw() {
		return bw;
	}

	/**
	 * Sets the GDDRAM bw capacity.
	 * 
	 * @param bw
	 *            the new bw capacity
	 */
	protected void setBw(long bw) {
		this.bw = bw;
	}

	/**
	 * Gets the available GDDRAM BW in the pgpu.
	 * 
	 * @return available bw
	 * 
	 * @pre $none
	 * @post $none
	 */
	public abstract long getAvailableBw();

	/**
	 * @return the vgpuBwMap
	 */
	public Map<Vgpu, Long> getVgpuBwMap() {
		return vgpuBwMap;
	}

	/**
	 * @param vgpuBwMap
	 *            the vgpuBwMap to set
	 */
	protected void setVgpuBwMap(Map<Vgpu, Long> vgpuBwMap) {
		this.vgpuBwMap = vgpuBwMap;
	}

	/**
	 * @return the vgpuRequestedBwMap
	 */
	public Map<Vgpu, Long> getVgpuRequestedBwMap() {
		return vgpuRequestedBwMap;
	}

	/**
	 * @param vgpuRequestedBwMap
	 *            the vgpuRequestedBwMap to set
	 */
	protected void setVgpuRequestedBwMap(Map<Vgpu, Long> vgpuRequestedBwMap) {
		this.vgpuRequestedBwMap = vgpuRequestedBwMap;
	}

}
