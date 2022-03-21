package org.cloudbus.cloudsim.gpu.remote;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.gpu.GpuHost;
import org.cloudbus.cloudsim.gpu.GpuVm;
import org.cloudbus.cloudsim.gpu.Pgpu;
import org.cloudbus.cloudsim.gpu.VideoCard;

/**
 * This class extends {@link RemoteGpuVmAllocationPolicySimple} and allocates
 * GPU-enabled VMs on GPU hosts with least loaded (i.t.o. resident vGPUs) pGPUs.
 * 
 * @author Ahmad Siavashi
 *
 */
public class RemoteGpuVmAllocationPolicyLeastLoadModified extends RemoteGpuVmAllocationPolicySimple {

	/**
	 * This class extends {@link RemoteGpuVmAllocationPolicySimple} and allocates
	 * GPU-enabled VMs on GPU hosts with least loaded (i.t.o. resident vGPUs)
	 * pGPUs.
	 * 
	 * @see {@link RemoteGpuVmAllocationPolicySimple}
	 */
	public RemoteGpuVmAllocationPolicyLeastLoadModified(List<? extends Host> list) {
		super(list);
	}

	@Override
	public boolean allocateHostForVm(Vm vm) {
		if (((GpuVm) vm).hasVgpu()) {
			sortGpuHosts();
		}
		return super.allocateHostForVm(vm);
	}

	/**
	 * Return available memory of each video card's least loaded pGPU.
	 */
	public Map<Pgpu, Integer> getPgpusVgpuCount(GpuHost gpuHost) {
		Map<Pgpu, Integer> pgpuVgpuCount = new HashMap<>();
		for (VideoCard videoCard : gpuHost.getVideoCardAllocationPolicy().getVideoCards()) {
			for (Pgpu pgpu : videoCard.getVgpuScheduler().getPgpuList()) {
				pgpuVgpuCount.put(pgpu, videoCard.getVgpuScheduler().getPgpuVgpuMap().get(pgpu).size());
			}
		}
		return pgpuVgpuCount;
	}

	protected void sortGpuHosts() {
		Collections.sort(getGpuHostList(), new Comparator<GpuHost>() {
			public int compare(GpuHost gpuHost1, GpuHost gpuHost2) {
				Integer host1MinVgpuCount = Collections.min(getPgpusVgpuCount(gpuHost1).values());
				Integer host2MinVgpuCount = Collections.min(getPgpusVgpuCount(gpuHost2).values());
				return Integer.compare(host1MinVgpuCount, host2MinVgpuCount);
			};
		});
	}

}
