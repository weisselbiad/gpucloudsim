package org.cloudbus.cloudsim.gpu.allocation;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.cloudbus.cloudsim.gpu.Vgpu;
import org.cloudbus.cloudsim.gpu.VideoCard;

/**
 * Selects the video card with GPU with maximum available memory.
 * 
 * @author Ahmad Siavashi
 * 
 */
public class VideoCardAllocationPolicyLeastLoad extends VideoCardAllocationPolicySimple {
	/**
	 * Selects the video card with GPU with maximum available memory.
	 */
	public VideoCardAllocationPolicyLeastLoad(List<? extends VideoCard> videoCards) {
		super(videoCards);
	}

	@Override
	public boolean allocate(Vgpu vgpu, int PCIeBw) {
		sortVideoCards();
		return super.allocate(vgpu, PCIeBw);
	}

	protected void sortVideoCards() {
		// Find the one with the maximum available GPU memory
		Collections.sort(getVideoCards(), Collections.reverseOrder(new Comparator<VideoCard>() {
			@Override
			public int compare(VideoCard videoCard1, VideoCard videoCard2) {
				Integer videoCard1maxAvailableMemory = Collections
						.max(videoCard1.getVgpuScheduler().getPgpusAvailableMemory().values());
				Integer videoCard2maxAvailableMemory = Collections
						.max(videoCard2.getVgpuScheduler().getPgpusAvailableMemory().values());
				return Integer.compare(videoCard1maxAvailableMemory, videoCard2maxAvailableMemory);
			}
		}));
	}

}
