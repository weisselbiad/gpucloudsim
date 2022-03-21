package org.cloudbus.cloudsim.gpu.allocation;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.cloudbus.cloudsim.gpu.Vgpu;
import org.cloudbus.cloudsim.gpu.VideoCard;

/**
 * Selects the video card with GPU with minimum available memory.
 * 
 * @author Ahmad Siavashi
 * 
 */
public class VideoCardAllocationPolicyBestFit extends VideoCardAllocationPolicySimple {

	/**
	 * Selects the video card with GPU with minimum available memory.
	 */
	public VideoCardAllocationPolicyBestFit(List<? extends VideoCard> videoCards) {
		super(videoCards);
	}

	@Override
	public boolean allocate(Vgpu vgpu, int PCIeBw) {
		sortVideoCards();
		return super.allocate(vgpu, PCIeBw);
	}

	protected void sortVideoCards() {
		Collections.sort(getVideoCards(), new Comparator<VideoCard>() {
			@Override
			public int compare(VideoCard videoCard1, VideoCard videoCard2) {
				Integer videoCard1maxAvailableMemory = Collections
						.min(videoCard1.getVgpuScheduler().getPgpusAvailableMemory().values());
				Integer videoCard2maxAvailableMemory = Collections
						.min(videoCard2.getVgpuScheduler().getPgpusAvailableMemory().values());
				return Integer.compare(videoCard1maxAvailableMemory, videoCard2maxAvailableMemory);
			}
		});
	}

}
