package org.cloudbus.cloudsim.gpu.hardware_assisted.grid;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.gpu.GpuVm;
import org.cloudbus.cloudsim.gpu.allocation.VideoCardAllocationPolicy;
import org.cloudbus.cloudsim.gpu.allocation.VideoCardAllocationPolicyNull;
import org.cloudbus.cloudsim.gpu.selection.PgpuSelectionPolicy;
import org.cloudbus.cloudsim.gpu.selection.PgpuSelectionPolicyNull;

public class GridGpuVmAllocationPolicyViri extends GridGpuVmAllocationPolicyVird {

	/**
	 * /** The VIRI policy is implemented according to, 
	 * A. Garg, U. Kurkure, H. Sivaraman, L. Vu, Virtual machine placement solution 
	 * for VGPU enabled clouds, in: 2019 International Conference on High Performance 
	 * Computing & Simulation (HPCS), IEEE, 2019, pp. 897–903.
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

	public GridGpuVmAllocationPolicyViri(List<? extends Host> list) {
		super(list);
	}

	@Override
	protected void sortVms(List<GpuVm> vms) {
		Collections.sort(vms, new Comparator<GpuVm>() {
			@Override
			public int compare(GpuVm vm1, GpuVm vm2) {
				int vgpu1gddram = !vm1.hasVgpu() ? 0 : vm1.getVgpu().getGddram();
				int vgpu2gddram = !vm2.hasVgpu() ? 0 : vm2.getVgpu().getGddram();
				return Integer.compare(vgpu1gddram, vgpu2gddram);
			}
		});
	}

}
