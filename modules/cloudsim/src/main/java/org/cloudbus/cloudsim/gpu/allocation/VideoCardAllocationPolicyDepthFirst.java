package org.cloudbus.cloudsim.gpu.allocation;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.cloudbus.cloudsim.gpu.Vgpu;
import org.cloudbus.cloudsim.gpu.VideoCard;

/**
 * {@link VideoCardAllocationPolicyDepththFirst} extends
 * {@link VideoCardAllocationPolicy} to provision video cards to vgpus on a
 * host. Host video cards are sorted in descending order from the most loaded
 * video card to the least loaded one. Then, they are traversed one by one until
 * the newly arrived vgpu is allocated.
 * 
 * @author Ahmad Siavashi
 *
 */
public class VideoCardAllocationPolicyDepthFirst extends VideoCardAllocationPolicySimple {

	/**
	 * @param videoCards
	 */
	public VideoCardAllocationPolicyDepthFirst(List<? extends VideoCard> videoCards) {
		super(videoCards);
	}

	@Override
	public boolean allocate(Vgpu vgpu, int PCIeBw) {
		sortVideoCards();
		return super.allocate(vgpu, PCIeBw);
	}

	protected void sortVideoCards() {
		Collections.sort(getVideoCards(), Collections.reverseOrder(new Comparator<VideoCard>() {
			@Override
			public int compare(VideoCard videoCard1, VideoCard videoCard2) {
				Integer numVideoCard1Vgpus = videoCard1.getVgpuScheduler().getVgpuPeMap().keySet().size();
				Integer numVideoCard2Vgpus = videoCard2.getVgpuScheduler().getVgpuPeMap().keySet().size();
				return Integer.compare(numVideoCard1Vgpus, numVideoCard2Vgpus);
			}
		}));
	}

}
