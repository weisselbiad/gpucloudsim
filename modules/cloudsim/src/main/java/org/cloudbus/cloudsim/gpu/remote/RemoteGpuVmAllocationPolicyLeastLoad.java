package org.cloudbus.cloudsim.gpu.remote;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.gpu.GpuHost;
import org.cloudbus.cloudsim.gpu.GpuVm;
import org.cloudbus.cloudsim.gpu.Pgpu;
import org.cloudbus.cloudsim.gpu.VideoCard;

/**
 * This class extends {@link RemoteGpuVmAllocationPolicySimple} and allocates
 * GPU-enabled VMs on GPU hosts with least loaded (i.t.o. allocated memory)
 * pGPUs.
 * 
 * @see S. Iserte, R. Peña-Ortiz, J. Gutiérrez-Aguado, J. M. Claver and R. Mayo, "GSaaS: A Service to Cloudify and Schedule GPUs," in IEEE Access, vol. 6, pp. 39762-39774, 2018, doi: 10.1109/ACCESS.2018.2855261.
 * 
 * @author Ahmad Siavashi
 *
 */
public class RemoteGpuVmAllocationPolicyLeastLoad extends RemoteGpuVmAllocationPolicySimple {

	/**
	 * This class extends {@link RemoteGpuVmAllocationPolicySimple} and allocates
	 * GPU-enabled VMs on GPU hosts with least loaded (i.t.o. allocated memory)
	 * pGPUs.
	 * 
	 * @see {@link RemoteGpuVmAllocationPolicySimple}
	 */
	public RemoteGpuVmAllocationPolicyLeastLoad(List<? extends Host> list) {
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
	public Map<VideoCard, Map<Pgpu, Integer>> getVideoCardsAvailableMemory(GpuHost gpuHost) {
		Map<VideoCard, Map<Pgpu, Integer>> videoCardsAvailableMemory = new HashMap<>();
		for (VideoCard videoCard : gpuHost.getVideoCardAllocationPolicy().getVideoCards()) {
			videoCardsAvailableMemory.put(videoCard, videoCard.getVgpuScheduler().getPgpusAvailableMemory());
		}
		return videoCardsAvailableMemory;
	}

	protected void sortGpuHosts() {
		Collections.sort(getGpuHostList(), Collections.reverseOrder(new Comparator<GpuHost>() {
			public int compare(GpuHost gpuHost1, GpuHost gpuHost2) {
				Integer host1MaxAvailableGpuMemory = 0;
				Integer host2MaxAvailableGpuMemory = 0;
				for (Entry<VideoCard, Map<Pgpu, Integer>> item : getVideoCardsAvailableMemory(gpuHost1).entrySet()) {
					Integer videoCardMaxAvailableMemory = Collections.max(item.getValue().values());
					host1MaxAvailableGpuMemory = videoCardMaxAvailableMemory > host1MaxAvailableGpuMemory
							? videoCardMaxAvailableMemory
							: host1MaxAvailableGpuMemory;
				}
				for (Entry<VideoCard, Map<Pgpu, Integer>> item : getVideoCardsAvailableMemory(gpuHost2).entrySet()) {
					Integer videoCardMaxAvailableMemory = Collections.max(item.getValue().values());
					host2MaxAvailableGpuMemory = videoCardMaxAvailableMemory > host2MaxAvailableGpuMemory
							? videoCardMaxAvailableMemory
							: host2MaxAvailableGpuMemory;
				}
				return Integer.compare(host1MaxAvailableGpuMemory, host2MaxAvailableGpuMemory);
			};
		}));
	}

}
