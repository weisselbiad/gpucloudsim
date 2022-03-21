package org.cloudbus.cloudsim.gpu.allocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.cloudbus.cloudsim.gpu.Pgpu;
import org.cloudbus.cloudsim.gpu.Vgpu;
import org.cloudbus.cloudsim.gpu.VideoCard;

/**
 * {@link VideoCardAllocationPolicy} is an abstract class that represents the
 * provisioning of video cards to vgpus in a host.
 * 
 * @author Ahmad Siavashi
 * 
 */
public abstract class VideoCardAllocationPolicy {

	/** vgpu to video card mapping */
	private Map<Vgpu, VideoCard> vgpuVideoCardMap;

	/** video cards attached to the host */
	private List<? extends VideoCard> videoCards = new ArrayList<VideoCard>();

	public VideoCardAllocationPolicy(List<? extends VideoCard> videoCards) {
		setVgpuVideoCardMap(new HashMap<Vgpu, VideoCard>());
		setVideoCards(videoCards);
	}

	/**
	 * Allocate pGPU and PCIe bandwidth to the vgpu
	 * 
	 * @param vgpu   the vgpu
	 * @param PCIeBw amount of requested PCIe bandwidth
	 * @return $true is the request is accepted; $false otherwise.
	 */
	public abstract boolean allocate(Vgpu vgpu, int PCIeBw);

	/**
	 * Allocate pGPU and PCIe bandwidth to the vgpu
	 * 
	 * @param videoCardId id of the video card to allocate on
	 * @param vgpu        the vgpu
	 * @param PCIeBw      amount of requested PCIe bandwidth
	 * @return $true is the request is accepted; $false otherwise.
	 */
	public boolean allocate(VideoCard videoCard, Vgpu vgpu, int PCIeBw) {
		if (!hasVideoCard(videoCard)) {
			throw new NoSuchElementException("videoCardId=" + videoCard.getId());
		}
		if (videoCard.getVgpuScheduler().isSuitable(vgpu)) {
			videoCard.getVgpuScheduler().allocatePgpuForVgpu(vgpu, vgpu.getCurrentRequestedMips(),
					vgpu.getCurrentRequestedGddram(), vgpu.getCurrentRequestedBw());
			getVgpuVideoCardMap().put(vgpu, videoCard);
			vgpu.setVideoCard(videoCard);
			return true;
		}
		return false;
	}

	/**
	 * Allocate pgpu to the vgpu
	 * 
	 * @param pgpuId      id of the pgpu on the video card to allocate on
	 * @param vgpu        the vgpu
	 * @param PCIeBw      amount of requested PCIe bandwidth
	 * @return $true is the request is accepted; $false otherwise.
	 */
	public boolean allocate(Pgpu pgpu, Vgpu vgpu, int PCIeBw) {
		if (!hasPgpu(pgpu)) {
			throw new NoSuchElementException("pgpuId=" + pgpu.getId());
		}
		VideoCard videoCard = getVideoCard(pgpu);
		if (videoCard.getVgpuScheduler().isSuitable(pgpu, vgpu)) {
			videoCard.getVgpuScheduler().allocatePgpuForVgpu(pgpu, vgpu, vgpu.getCurrentRequestedMips(),
					vgpu.getCurrentRequestedGddram(), vgpu.getCurrentRequestedBw());
			getVgpuVideoCardMap().put(vgpu, videoCard);
			vgpu.setVideoCard(videoCard);
			return true;
		}
		return false;
	};

	public boolean allocate(int pgpuId, Vgpu vgpu, int PCIeBw) {
		Pgpu pgpu = getPgpu(pgpuId);
		return allocate(pgpu, vgpu, PCIeBw);
	}

	/**
	 * @return the vgpuVideoCardMap
	 */
	public Map<Vgpu, VideoCard> getVgpuVideoCardMap() {
		return vgpuVideoCardMap;
	}

	/**
	 * @param vgpuVideoCardMap the vgpuVideoCardMap to set
	 */
	protected void setVgpuVideoCardMap(Map<Vgpu, VideoCard> vgpuVideoCardMap) {
		this.vgpuVideoCardMap = vgpuVideoCardMap;
	}

	protected void setVideoCards(List<? extends VideoCard> videoCards) {
		this.videoCards = videoCards;
	}

	public List<? extends VideoCard> getVideoCards() {
		return videoCards;
	}

	public List<? extends Pgpu> getPgpus() {
		List<Pgpu> pgpus = new ArrayList<>();
		for (VideoCard videoCard : getVideoCards()) {
			for (Pgpu pgpu : videoCard.getVgpuScheduler().getPgpuList()) {
				pgpus.add(pgpu);
			}
		}
		return pgpus;
	}

	/**
	 * Check if the vgpu can reside on the host associated to this video card
	 * allocation policy.
	 * 
	 * @param vgpu the vgpu
	 * @return $true if the vgpu can be allocated in the host associated with this
	 *         video card allocation policy; $false otherwise.
	 */
	public boolean isSuitable(Vgpu vgpu) {
		for (VideoCard videoCard : getVideoCards()) {
			boolean result = videoCard.getVgpuScheduler().isSuitable(vgpu);
			if (!result) {
				continue;
			}
			return true;
		}
		return false;
	}

	/**
	 * Deallocates resources allocated to a vgpu.
	 * 
	 * @param vgpu the vgpu
	 * @return $true if for a successful deallocation; $false otherwise.
	 */
	public void deallocate(Vgpu vgpu) {
		VideoCard videoCard = getVgpuVideoCardMap().remove(vgpu);
		if (videoCard != null) {
			videoCard.getVgpuScheduler().deallocatePgpuForVgpu(vgpu);
		}
	}

	protected VideoCard getVideoCard(Pgpu pgpu) {
		for (VideoCard vc : getVideoCards()) {
			for (Pgpu p : vc.getVgpuScheduler().getPgpuList()) {
				if (pgpu == p) {
					return vc;
				}
			}
		}
		return null;
	}

	public VideoCard getVideoCard(int videoCardId) {
		for (VideoCard vc : getVideoCards()) {
			if (videoCardId == vc.getId()) {
				return vc;
			}
		}
		return null;
	}

	protected boolean hasVideoCard(VideoCard videoCard) {
		return getVideoCard(videoCard.getId()) == null ? false : true;
	}

	protected boolean hasVideoCard(int videoCardId) {
		return getVideoCard(videoCardId) == null ? false : true;
	}

	public Pgpu getPgpu(int pgpuId) {
		for (VideoCard vc : getVideoCards()) {
			for (Pgpu p : vc.getVgpuScheduler().getPgpuList()) {
				if (pgpuId == p.getId()) {
					return p;
				}
			}
		}
		return null;
	}

	protected boolean hasPgpu(Pgpu pgpu) {
		return getPgpu(pgpu.getId()) == null ? false : true;
	}

	protected boolean hasPgpu(int pgpuId) {
		return getPgpu(pgpuId) == null ? false : true;
	}

}
