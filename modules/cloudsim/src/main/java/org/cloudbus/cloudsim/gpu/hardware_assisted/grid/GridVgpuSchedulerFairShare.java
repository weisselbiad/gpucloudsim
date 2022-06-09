package org.cloudbus.cloudsim.gpu.hardware_assisted.grid;

import java.util.List;
import java.util.Map.Entry;

import org.cloudbus.cloudsim.gpu.Pgpu;
import org.cloudbus.cloudsim.gpu.Vgpu;
import org.cloudbus.cloudsim.gpu.VgpuSchedulerFairShare;
import org.cloudbus.cloudsim.gpu.selection.PgpuSelectionPolicy;

/**
 * This is a Time-Shared vgpu scheduler, which allows over-subscription. In
 * other words, the scheduler still allows the allocation of Vgpus that require
 * more GPU capacity than is available. OverSubscription results in performance
 * degradation. This scheduler can be considered as fair-share scheduler which
 * in turn is a time-sliced round-robin scheduler.
 * 
 * @author Ahmad Siavashi
 */
public class GridVgpuSchedulerFairShare extends VgpuSchedulerFairShare {

	/**
	 * Instantiates a new fair-share vgpu scheduler.
	 * 
	 * @param pgpulist
	 *            the list of gpu PEs of the video card where the VgpuScheduler is
	 *            associated to.
	 */
	public GridVgpuSchedulerFairShare(String videoCardType, List<Pgpu> pgpuList, PgpuSelectionPolicy pgpuSelectionPolicy) {
		super(videoCardType, pgpuList, pgpuSelectionPolicy);
	}

	/**
	 * Checks whether the vgpu type is supported by this video card type or not.
	 * 
	 * @param vgpu
	 *            the vgpu
	 * @return $true if the video card supports the vgpu type.
	 */
	protected boolean isVideoCardSuitableForVgpu(Vgpu vgpu) {
		if (!GridVgpuTags.isVideoCardSuitable(getVideoCardType(), vgpu.getType())) {
			return false;
		}
		for (Entry<Pgpu, List<Vgpu>> entry : getPgpuVgpuMap().entrySet()) {
			if (GridVgpuTags.isPgpuSuitable(entry, vgpu)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isSuitable(final Vgpu vgpu) {
		if (!isVideoCardSuitableForVgpu(vgpu)) {
			return false;
		}
		return super.isSuitable(vgpu);
	}

	@Override
	public boolean allocatePgpuForVgpu(final Vgpu vgpu, final List<Double> mipsShare, final int gddramShare,
			final long bwShare) {
		if (!isVideoCardSuitableForVgpu(vgpu)) {
			return false;
		}
		return super.allocatePgpuForVgpu(vgpu, mipsShare, gddramShare, bwShare);
	}
}
