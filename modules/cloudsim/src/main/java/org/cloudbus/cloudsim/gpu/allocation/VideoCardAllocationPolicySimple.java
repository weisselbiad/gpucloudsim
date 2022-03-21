package org.cloudbus.cloudsim.gpu.allocation;

import java.util.List;

import org.cloudbus.cloudsim.gpu.Vgpu;
import org.cloudbus.cloudsim.gpu.VideoCard;

/**
 * Simply iterates over available video cards and finds the first that suits.
 * 
 * @author Ahmad Siavashi
 * 
 */
public class VideoCardAllocationPolicySimple extends VideoCardAllocationPolicy {

	/**
	 * Simply iterates over available video cards and finds the first that suits.
	 */
	public VideoCardAllocationPolicySimple(List<? extends VideoCard> videoCards) {
		super(videoCards);
	}

	public boolean allocate(Vgpu vgpu, int PCIeBw) {
		for (VideoCard videoCard : getVideoCards()) {
			if (videoCard.getVgpuScheduler().isSuitable(vgpu)) {
				videoCard.getVgpuScheduler().allocatePgpuForVgpu(vgpu, vgpu.getCurrentRequestedMips(),
						vgpu.getCurrentRequestedGddram(), vgpu.getCurrentRequestedBw());
				getVgpuVideoCardMap().put(vgpu, videoCard);
				vgpu.setVideoCard(videoCard);
				return true;
			}
		}
		return false;
	}
}
