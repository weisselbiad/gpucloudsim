package org.cloudbus.cloudsim.gpu.provisioners;

import java.util.HashMap;
import java.util.Map;

import org.cloudbus.cloudsim.gpu.Pgpu;

/**
 * VideoCardBwProvisioner is an abstract class that represents the provisioning
 * policy used by a VideoCard to allocate PCIe bandwidth (bw) to Pgpus inside
 * it.
 * 
 * @author Ahmad Siavashi
 *
 */
public abstract class VideoCardBwProvisioner {

	/**
	 * The total PCIe bandwidth capacity from the video card that the provisioner
	 * can allocate to Pgpus.
	 */
	private long bw;

	/** videoCard-bw mapping */
	private Map<Pgpu, Long> pgpuBwMap;

	/** videoCard-requested bw mapping */
	private Map<Pgpu, Long> pgpuRequestedBwMap;

	/**
	 * Creates the new VideoCardBwProvisioner.
	 * 
	 * @param bw
	 *            The total PCIe bandwidth capacity from the video card that the
	 *            provisioner can allocate to Pgpus.
	 * 
	 * @pre bw >= 0
	 * @post $none
	 */
	public VideoCardBwProvisioner(long bw) {
		setBw(bw);
		setPgpuBwMap(new HashMap<Pgpu, Long>());
		setPgpuRequestedBwMap(new HashMap<Pgpu, Long>());
	}

	/**
	 * Allocates PCIe BW for a given Pgpu.
	 * 
	 * @param pgpu
	 *            the pgpu for which the PCIe bw is being allocated
	 * @param bw
	 *            the PCIe bw to be allocated to the pgpu
	 * 
	 * @return $true if the PCIe bw could be allocated; $false otherwise
	 * 
	 * @pre $none
	 * @post $none
	 */
	public abstract boolean allocateBwForPgpu(Pgpu pgpu, long bw);

	/**
	 * Gets the allocated PCIe BW for Pgpu.
	 * 
	 * @param pgpu
	 *            the pgpu
	 * 
	 * @return the allocated PCIe BW for the pgpu
	 */
	public long getAllocatedBwForPgpu(Pgpu Pgpu) {
		return getPgpuBwMap().get(Pgpu).longValue();
	}

	/**
	 * Releases PCIe BW used by a pgpu
	 * 
	 * @param pgpu
	 *            the pgpu
	 * 
	 * @pre $none
	 * @post none
	 */
	public void deallocateBwForPgpu(Pgpu pgpu) {
		getPgpuBwMap().remove(pgpu);
	}

	/**
	 * Releases PCIe BW used by all Pgpus.
	 * 
	 * @pre $none
	 * @post none
	 */
	public void deallocateBwForAllPgpus() {
		getPgpuBwMap().clear();
	}

	/**
	 * Gets the PCIe bw capacity.
	 * 
	 * @return the PCIe bw capacity
	 */
	public long getBw() {
		return bw;
	}

	/**
	 * Sets the PCIe bw capacity.
	 * 
	 * @param bw
	 *            the new PCIe bw capacity
	 */
	protected void setBw(long bw) {
		this.bw = bw;
	}

	/**
	 * Gets the available PCIe BW in the Pgpu.
	 * 
	 * @return available PCIe bw
	 * 
	 * @pre $none
	 * @post $none
	 */
	public abstract long getAvailableBw();

	/**
	 * @return the pgpuBwMap
	 */
	public Map<Pgpu, Long> getPgpuBwMap() {
		return pgpuBwMap;
	}

	/**
	 * @param pgpuBwMap
	 *            the pgpuBwMap to set
	 */
	public void setPgpuBwMap(Map<Pgpu, Long> pgpuBwMap) {
		this.pgpuBwMap = pgpuBwMap;
	}

	/**
	 * @return the pgpuRequestedBwMap
	 */
	public Map<Pgpu, Long> getPgpuRequestedBwMap() {
		return pgpuRequestedBwMap;
	}

	/**
	 * @param pgpuRequestedBwMap
	 *            the pgpuRequestedBwMap to set
	 */
	public void setPgpuRequestedBwMap(Map<Pgpu, Long> pgpuRequestedBwMap) {
		this.pgpuRequestedBwMap = pgpuRequestedBwMap;
	}

}
