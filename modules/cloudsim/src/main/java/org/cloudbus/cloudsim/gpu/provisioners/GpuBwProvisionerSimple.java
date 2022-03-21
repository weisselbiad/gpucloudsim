package org.cloudbus.cloudsim.gpu.provisioners;

import org.cloudbus.cloudsim.gpu.Vgpu;

/**
 * GpuBwProvisionerSimple is an extension of {@link GpuBwProvisioner} which uses
 * a best-effort policy to allocate GDDRAM bandwidth (bw) to Vgpus: if there is
 * available GDDRAM bw on the Pgpu, it allocates; otherwise, it fails. Each Pgpu
 * has to have its own instance of a GpuBwProvisioner.
 * 
 * @author Ahmad Siavashi
 * 
 */
public class GpuBwProvisionerSimple extends GpuBwProvisioner {

	long availableBw;

	/**
	 * Instantiates a new GPU bw provisioner simple.
	 * 
	 * @param bw
	 *            The total GDDRAM bw capacity from the Pgpu that the provisioner
	 *            can allocate to Vgpus.
	 */
	public GpuBwProvisionerSimple(long bw) {
		super(bw);
		setAvailableBw(bw);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.cloudbus.cloudsim.gpu.provisioners.GpuBwProvisioner#allocateBwForVgpu
	 * (org.cloudbus.cloudsim.gpu.Vgpu, long)
	 */
	@Override
	public boolean allocateBwForVgpu(Vgpu vgpu, long bw) {
		if (!isSuitableForVgpu(vgpu, bw)) {
			return false;
		}
		setAvailableBw(getAvailableBw() - bw);
		getVgpuBwMap().put(vgpu, bw);
		getVgpuRequestedBwMap().put(vgpu, bw);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudbus.cloudsim.gpu.provisioners.GpuBwProvisioner#isSuitableForVm
	 * (org.cloudbus.cloudsim.gpu.Vgpu, long)
	 */
	@Override
	public boolean isSuitableForVgpu(Vgpu vgpu, long bw) {
		if (getAvailableBw() >= bw) {
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudbus.cloudsim.gpu.provisioners.GpuBwProvisioner#getAvailableBw()
	 */
	@Override
	public long getAvailableBw() {
		return availableBw;
	}

	/**
	 * @param availableBw
	 *            the availableBw to set
	 */
	protected void setAvailableBw(long availableBw) {
		this.availableBw = availableBw;
	}

}
