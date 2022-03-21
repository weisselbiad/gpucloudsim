package org.cloudbus.cloudsim.gpu.selection;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.cloudbus.cloudsim.gpu.Pgpu;
import org.cloudbus.cloudsim.gpu.Vgpu;
import org.cloudbus.cloudsim.gpu.VgpuScheduler;

/**
 * {@link PgpuSelectionPolicyDepthFirst} implements {@link PgpuSelectionPolicy}
 * and selects the Pgpu with the largest number of allocated Vgpus.
 * 
 * @author Ahmad Siavashi
 *
 */
public class PgpuSelectionPolicyDepthFirst implements PgpuSelectionPolicy {

	/**
	 * Selects the Pgpu with the largest number of allocated Vgpus.
	 */
	public PgpuSelectionPolicyDepthFirst() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudbus.cloudsim.gpu.selection.PgpuSelectionPolicy#selectPgpu(org.
	 * cloudbus.cloudsim.gpu.VgpuScheduler, java.util.List)
	 */
	@Override
	public Pgpu selectPgpu(Vgpu vgpu, VgpuScheduler scheduler, List<Pgpu> pgpuList) {
		if (pgpuList.isEmpty()) {
			return null;
		}
		return Collections.max(pgpuList, new Comparator<Pgpu>() {
			@Override
			public int compare(Pgpu pgpu1, Pgpu pgpu2) {
				Integer numPgpu1Vgpus = scheduler.getPgpuVgpuMap().get(pgpu1).size();
				Integer numPgpu2Vgpus = scheduler.getPgpuVgpuMap().get(pgpu2).size();
				return Integer.compare(numPgpu1Vgpus, numPgpu2Vgpus);
			}
		});
	}

}
