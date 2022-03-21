package org.cloudbus.cloudsim.gpu.provisioners;

import org.cloudbus.cloudsim.gpu.Vgpu;

/**
 * GpuBwProvisionerShared is an extension of {@link GpuBwProvisioner} which
 * shares GDDRAM bandwdidth among allocated Vgpu.
 * 
 * @author Ahmad Siavashi
 * 
 */
public class GpuBwProvisionerShared extends GpuBwProvisioner {

	/**
	 * Instantiates a new GPU bw provisioner shared.
	 * 
	 * @param bw
	 *            The total GDDRAM bw capacity from the Pgpu that the provisioner
	 *            can allocate to Vgpus.
	 */
	public GpuBwProvisionerShared(long bw) {
		super(bw);
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
		getVgpuRequestedBwMap().put(vgpu, bw);
		redistributeAllocatedBw();
		return true;
	}

	/**
	 * Redistributes the GDDRAM bandwidth among allocated Vgpus.
	 */
	protected void redistributeAllocatedBw() {
		long totalBw = getBw();
		long totalRequestedBw = 0;
		for (Long bw : getVgpuRequestedBwMap().values()) {
			totalRequestedBw += bw;
		}
		// calculating the scaling factor
		final double scaleFactor = ((double) totalBw) / totalRequestedBw;
		for (Vgpu vgpu : getVgpuRequestedBwMap().keySet()) {
			long scaledBw = (long) (getVgpuRequestedBwMap().get(vgpu).longValue() * scaleFactor);
			long requestedBw = getVgpuRequestedBwMap().get(vgpu);
			if (scaledBw > requestedBw) {
				getVgpuBwMap().put(vgpu, requestedBw);
				vgpu.setCurrentAllocatedBw(requestedBw);
			} else {
				getVgpuBwMap().put(vgpu, scaledBw);
				vgpu.setCurrentAllocatedBw(scaledBw);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudbus.cloudsim.gpu.provisioners.GpuBwProvisioner#isSuitableForVm
	 * (org.cloudbus.cloudsim.gpu.Vgpu, long)
	 */
	@Override
	public boolean isSuitableForVgpu(Vgpu vgpu, long bw) {
		return true;
	}

	@Override
	public void deallocateBwForVgpu(Vgpu vgpu) {
		super.deallocateBwForVgpu(vgpu);
		getVgpuRequestedBwMap().remove(vgpu);
		redistributeAllocatedBw();
	}

	@Override
	public void deallocateBwForAllVgpus() {
		super.deallocateBwForAllVgpus();
		getVgpuRequestedBwMap().clear();
	}

	@Override
	public long getAvailableBw() {
		long allocatedMips = 0;
		for (Long bw : getVgpuBwMap().values()) {
			allocatedMips += bw.longValue();
		}
		if (allocatedMips > getBw()) {
			return 0;
		}
		return getBw() - allocatedMips;
	}
}
