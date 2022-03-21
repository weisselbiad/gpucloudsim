package org.cloudbus.cloudsim.gpu.provisioners;

import org.cloudbus.cloudsim.gpu.Vgpu;

/**
 * GpuGddramProvisioner is an abstract class that represents the provisioning
 * policy used by a Pgpu to allocate gddram memory to Vgpus inside it.
 * 
 * @author Ahmad Siavashi
 * 
 */
public abstract class GpuGddramProvisioner {

	/**
	 * The total gddram capacity from the pgpu that the provisioner can allocate to
	 * vgpus.
	 */
	private int gddram;

	/** The available gddram. */
	private int availableGddram;

	/**
	 * Creates the new GddramProvisioner.
	 * 
	 * @param gddram
	 *            The total gddram capacity from the pgpu that the provisioner can
	 *            allocate to vgpus.
	 * 
	 * @pre gddram>=0
	 * @post $none
	 */
	public GpuGddramProvisioner(int gddram) {
		setGddram(gddram);
		setAvailableGddram(gddram);
	}

	/**
	 * Allocates GDDRAM for a given Vgpu.
	 * 
	 * @param vgpu
	 *            the virtual gpu for which the GDDRAM is being allocated
	 * @param gddram
	 *            the GDDRAM to be allocated to the Vgpu
	 * 
	 * @return $true if the GDDRAM could be allocated; $false otherwise
	 * 
	 * @pre $none
	 * @post $none
	 */
	public abstract boolean allocateGddramForVgpu(Vgpu vgpu, int gddram);

	/**
	 * Gets the allocated GDDRAM for a given Vgpu.
	 * 
	 * @param vgpu
	 *            the Vgpu
	 * 
	 * @return the allocated GDDRAM for the vgpu
	 */
	public abstract int getAllocatedGddramForVgpu(Vgpu vgpu);

	/**
	 * Releases GDDRAM used by a Vgpu.
	 * 
	 * @param vgpu
	 *            the vgpu
	 * 
	 * @pre $none
	 * @post none
	 */
	public abstract void deallocateGddramForVgpu(Vgpu vgpu);

	/**
	 * Releases GDDRAM used by all Vgpu.
	 * 
	 * @pre $none
	 * @post none
	 */
	public void deallocateGddramForAllVgpus() {
		setAvailableGddram(getGddram());
	}

	/**
	 * Checks if it is possible to change the current allocated GDDRAM for the Vgpu
	 * to a new amount, depending on the available GDDRAM.
	 * 
	 * @param vgpu
	 *            the vgpu to check if there is enough available GDDRAM on the host to
	 *            change the VM allocated GDDRAM
	 * @param gddram
	 *            the new total amount of GDDRAM for the Vgpu.
	 * 
	 * @return true, if is suitable for vgpu
	 */
	public abstract boolean isSuitableForVgpu(Vgpu vgpu, int gddram);

	/**
	 * @return the gddram
	 */
	public int getGddram() {
		return gddram;
	}

	/**
	 * @param gddram
	 *            the gddram to set
	 */
	public void setGddram(int gddram) {
		this.gddram = gddram;
	}

	/**
	 * @return the availableGddram
	 */
	public int getAvailableGddram() {
		return availableGddram;
	}

	/**
	 * @param availableGddram
	 *            the availableGddram to set
	 */
	protected void setAvailableGddram(int availableGddram) {
		this.availableGddram = availableGddram;
	}

}
