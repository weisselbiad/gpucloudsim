package org.cloudbus.cloudsim.gpu.hardware_assisted.grid;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.gpu.Pgpu;
import org.cloudbus.cloudsim.gpu.allocation.VideoCardAllocationPolicy;
import org.cloudbus.cloudsim.gpu.allocation.VideoCardAllocationPolicyNull;
import org.cloudbus.cloudsim.gpu.selection.PgpuSelectionPolicy;
import org.cloudbus.cloudsim.gpu.selection.PgpuSelectionPolicyNull;

/**
 * This class extends {@link GridGpuVmAllocationPolicyBreadthFirst} to implement
 * depth-first policy and enforce homogeneous vGPU allocation restriction of
 * NVIDIA GRID technology according to NVIDIA documents.
 * 
 * <b>Note</b>: This class performs a global placement, so classes required for
 * hierarchical placement that extend or implement
 * {@link VideoCardAllocationPolicy} and {@link PgpuSelectionPolicy} can be set
 * to {@link VideoCardAllocationPolicyNull} and {@link PgpuSelectionPolicyNull}
 * respectively. Otherwise, they are ignored.
 * 
 * @author Ahmad Siavashi
 *
 */
public class GridGpuVmAllocationPolicyDepthFirst extends GridGpuVmAllocationPolicyBreadthFirst {

	public GridGpuVmAllocationPolicyDepthFirst(List<? extends Host> list) {
		super(list);
	}

	@Override
	protected void sortPgpusList(List<Pair<Pgpu, Integer>> pgpuList) {
		Collections.sort(pgpuList, Collections.reverseOrder(new Comparator<Pair<Pgpu, Integer>>() {
			public int compare(Pair<Pgpu, Integer> p1, Pair<Pgpu, Integer> p2) {
				return Integer.compare(p1.getValue(), p2.getValue());
			};
		}));
	}

}
