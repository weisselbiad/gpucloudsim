package org.cloudbus.cloudsim.gpu.provisioners;

import org.cloudbus.cloudsim.gpu.Pgpu;

/**
 * VideoCardBwProvisionerShared is an extension of
 * {@link VideoCardBwProvisioner} which shares PCIe bandwdidth among allocated
 * Pgpus of a VideoCard.
 * 
 * @author Ahmad Siavashi
 *
 */
public class VideoCardBwProvisionerShared extends VideoCardBwProvisioner {

	/**
	 * Instantiates a new VideoCard PCIe bw provisioner shared.
	 * 
	 * @param bw
	 *            The total PCIe bw capacity from the VideoCard that the provisioner
	 *            can allocate to Pgpus.
	 */
	public VideoCardBwProvisionerShared(long bw) {
		super(bw);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudbus.cloudsim.gpu.provisioners.VideoCardBwProvisioner#
	 * allocateBwForPgpu(org.cloudbus.cloudsim.gpu.Pgpu, long)
	 */
	@Override
	public boolean allocateBwForPgpu(Pgpu pgpu, long bw) {
		getPgpuRequestedBwMap().put(pgpu, bw);
		redistributeAllocatedBw();
		return true;
	}

	protected void redistributeAllocatedBw() {
		// calculating the scaling factor
		long totalBw = getBw();
		long totalRequestedBw = 0;
		for (Long bw : getPgpuRequestedBwMap().values()) {
			totalRequestedBw += bw;
		}
		final double scaleFactor = ((double) totalBw) / totalRequestedBw;
		for (Pgpu pgpu : getPgpuRequestedBwMap().keySet()) {
			long scaledBw = (long) (getPgpuRequestedBwMap().get(pgpu).longValue() * scaleFactor);
			long requestedBw = getPgpuRequestedBwMap().get(pgpu);
			if (scaledBw > requestedBw) {
				getPgpuBwMap().put(pgpu, requestedBw);
			} else {
				getPgpuBwMap().put(pgpu, scaledBw);
			}
		}
	}

	@Override
	public void deallocateBwForPgpu(Pgpu pgpu) {
		super.deallocateBwForPgpu(pgpu);
		getPgpuRequestedBwMap().remove(pgpu);
		redistributeAllocatedBw();
	}

	@Override
	public void deallocateBwForAllPgpus() {
		super.deallocateBwForAllPgpus();
		getPgpuRequestedBwMap().clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.cloudbus.cloudsim.gpu.provisioners.VideoCardBwProvisioner#getAvailableBw(
	 * )
	 */
	@Override
	public long getAvailableBw() {
		long allocatedMips = 0;
		for (Long bw : getPgpuBwMap().values()) {
			allocatedMips += bw.longValue();
		}
		if (allocatedMips > getBw()) {
			return 0;
		}
		return getBw() - allocatedMips;
	}

}
