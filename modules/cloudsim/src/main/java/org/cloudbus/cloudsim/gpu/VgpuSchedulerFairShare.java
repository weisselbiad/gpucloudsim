package org.cloudbus.cloudsim.gpu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.gpu.selection.PgpuSelectionPolicy;
import org.cloudbus.cloudsim.lists.PeList;
import org.cloudbus.cloudsim.util.MathUtil;

/**
 * This is a Time-Shared {@link Vgpu} scheduler, which allows over-subscription.
 * In other words, the scheduler still allows the allocation of Vgpus that
 * require more GPU capacity than is available. OverSubscription results in
 * performance degradation. This scheduler can be considered as fair-share
 * scheduler which in turn is a time-sliced round-robin scheduler.
 * 
 * @author Ahmad Siavashi
 */
public class VgpuSchedulerFairShare extends VgpuSchedulerTimeShared {

	/**
	 * Requested Vgpu mips (which defers from mipsMap that holds actual scales mips
	 * values)
	 */
	private Map<Vgpu, List<Double>> requestedMipsMap;

	/**
	 * Instantiates a new fair-share vgpu scheduler.
	 * 
	 * @param pgpulist the list of gpu PEs of the video card where the VgpuScheduler
	 *                 is associated to.
	 */
	public VgpuSchedulerFairShare(String videoCardType, List<Pgpu> pgpuList, PgpuSelectionPolicy pgpuSelectionPolicy) {
		super(videoCardType, pgpuList, pgpuSelectionPolicy);
		setRequestedMipsMap(new HashMap<Vgpu, List<Double>>());
	}

	@Override
	public boolean allocatePgpuForVgpu(Pgpu pgpu, Vgpu vgpu, List<Double> mipsShare, int gddramShare, long bwShare) {
		if (!isSuitable(pgpu, vgpu)) {
			return false;
		}
		pgpu.getGddramProvisioner().allocateGddramForVgpu(vgpu, gddramShare);
		pgpu.getBwProvisioner().allocateBwForVgpu(vgpu, bwShare);
		getPgpuVgpuMap().get(pgpu).add(vgpu);
		getRequestedMipsMap().put(vgpu, mipsShare);
		getVgpuPeMap().put(vgpu, new ArrayList<Pe>());
		double mipsChange = MathUtil.sum(mipsShare);
		redistributeMipsDueToOverSubscription(pgpu, mipsChange);
		return true;
	}

	/**
	 * Rescales mips share of resident vgpus whenever a vgpu enters or leaves.
	 * 
	 * @param pgpu       the pgpu to redistribute the mips share of its resident
	 *                   vgpus
	 * @param mipsChange the amount of mips that has been changed in the pgpu;
	 *                   either added or removed.
	 */
	protected void redistributeMipsDueToOverSubscription(final Pgpu pgpu, double mipsChange) {
		// calculating the scaling factor
		final double totalPgpuMips = PeList.getTotalMips(pgpu.getPeList());
		double pgpuAvailableMips = 0.0;
		for (Pe pe : pgpu.getPeList()) {
			pgpuAvailableMips += pe.getPeProvisioner().getAvailableMips();
		}
		final double totalRequestedMipsFromPgpu = mipsChange + (totalPgpuMips - pgpuAvailableMips);
		final double scaleFactor = totalPgpuMips / totalRequestedMipsFromPgpu;
		// find vgpus running on the selected pgpu
		List<Vgpu> pgpuVgpus = getPgpuVgpuMap().get(pgpu);
		// deallocate
		for (Vgpu vgpu : pgpuVgpus) {
			for (Pe pe : getVgpuPeMap().get(vgpu)) {
				pe.getPeProvisioner().deallocateMipsForVm(vgpu.getVm());
			}
		}
		for (Vgpu vgpu : pgpuVgpus) {
			List<Double> scaledVmMips = new ArrayList<Double>();
			// scale
			for (double mips : getRequestedMipsMap().get(vgpu)) {
				scaledVmMips.add(Math.floor(mips * scaleFactor));
			}
			double totalScaledMipsForVm = MathUtil.sum(scaledVmMips);
			double totalRequestedMipsForVm = MathUtil.sum(getRequestedMipsMap().get(vgpu));
			if (totalScaledMipsForVm < totalRequestedMipsForVm) {
				getMipsMap().put(vgpu, scaledVmMips);
				vgpu.setCurrentAllocatedMips(scaledVmMips);
			} else {
				getMipsMap().put(vgpu, getRequestedMipsMap().get(vgpu));
				vgpu.setCurrentAllocatedMips(getRequestedMipsMap().get(vgpu));
			}
			// reallocate
			Collections.sort(pgpu.getPeList(), Collections.reverseOrder(new Comparator<Pe>() {
				public int compare(Pe pe1, Pe pe2) {
					return Double.compare(pe1.getPeProvisioner().getAvailableMips(),
							pe2.getPeProvisioner().getAvailableMips());
				}
			}));
			getVgpuPeMap().get(vgpu).clear();
			// No two Vgpu PEs are mapped to one Pgpu PE
			for (int i = 0; i < scaledVmMips.size(); i++) {
				Pe pe = pgpu.getPeList().get(i);
				pe.getPeProvisioner().allocateMipsForVm(vgpu.getVm(), scaledVmMips.get(i));
				getVgpuPeMap().get(vgpu).add(pe);
			}
		}
	}

	@Override
	public void deallocatePgpuForVgpu(Vgpu vgpu) {
		Pgpu pgpu = getPgpuForVgpu(vgpu);
		pgpu.getGddramProvisioner().deallocateGddramForVgpu(vgpu);
		pgpu.getBwProvisioner().deallocateBwForVgpu(vgpu);
		double totalMipsChange = 0.0;
		getPgpuVgpuMap().get(pgpu).remove(vgpu);
		for (Pe pe : getVgpuPeMap().get(vgpu)) {
			double allocatedMipsForVm = pe.getPeProvisioner().getTotalAllocatedMipsForVm(vgpu.getVm());
			pe.getPeProvisioner().deallocateMipsForVm(vgpu.getVm());
			totalMipsChange += allocatedMipsForVm;
		}
		getVgpuPeMap().remove(vgpu);
		getMipsMap().remove(vgpu);
		vgpu.setCurrentAllocatedMips(null);
		getRequestedMipsMap().remove(vgpu);
		redistributeMipsDueToOverSubscription(pgpu, totalMipsChange);
	}

	/**
	 * @return the requestedMipsMap
	 */
	public Map<Vgpu, List<Double>> getRequestedMipsMap() {
		return requestedMipsMap;
	}

	/**
	 * @param requestedMipsMap the requestedMipsMap to set
	 */
	protected void setRequestedMipsMap(Map<Vgpu, List<Double>> requestedMipsMap) {
		this.requestedMipsMap = requestedMipsMap;
	}
}
