package org.cloudbus.cloudsim.gpu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicy;

/**
 * {@link GpuVmAllocationPolicy} extends {@link VmAllocationPolicy} to support
 * GPU-enabled VM placement.
 * 
 * @author Ahmad Siavashi
 *
 */
public abstract class GpuVmAllocationPolicy extends VmAllocationPolicy {

	/**
	 * The map between each VM and its allocated host. The map key is a VM UID and
	 * the value is the allocated host for that VM.
	 */
	private Map<String, Host> vmTable;

	/**
	 * GPU-equipped hosts
	 */
	private List<GpuHost> gpuHostList;

	/**
	 * Holds which GpuHost a vGPU is allocated on
	 */
	private Map<Vgpu, GpuHost> vgpuHosts;

	/**
	 * @param list all data center hosts
	 */
	public GpuVmAllocationPolicy(List<? extends Host> list) {
		super(list);
		setVmTable(new HashMap<String, Host>());
		setGpuHostList(getHostList());
		setVgpuHosts(new HashMap<Vgpu, GpuHost>());
	}

	@Override
	public List<Map<String, Object>> optimizeAllocation(List<? extends Vm> vmList) {
		return null;
	}

	@Override
	public void deallocateHostForVm(Vm vm) {
		getVmTable().remove(vm.getUid()).vmDestroy(vm);
	}

	protected void deallocateGpuForVgpu(Vgpu vgpu) {
		getVgpuHosts().remove(vgpu).vgpuDestroy(vgpu);
	}

	protected boolean allocateGpuForVgpu(Vgpu vgpu, GpuHost gpuHost) {
		if (!getVgpuHosts().containsKey(vgpu)) {
			boolean result = gpuHost.vgpuCreate(vgpu);
			if (result) {
				getVgpuHosts().put(vgpu, gpuHost);
				return true;
			}
		}
		return false;
	}

	protected boolean allocateGpuForVgpu(Vgpu vgpu, GpuHost gpuHost, Pgpu pgpu) {
		if (!getVgpuHosts().containsKey(vgpu)) {
			boolean result = gpuHost.vgpuCreate(vgpu, pgpu);
			if (result) {
				getVgpuHosts().put(vgpu, gpuHost);
				return true;
			}
		}
		return false;
	}

	protected boolean allocateGpuHostForVgpu(Vgpu vgpu, GpuHost gpuHost, Pgpu pgpu) {
		if (!getVgpuHosts().containsKey(vgpu)) {
			boolean result = gpuHost.vgpuCreate(vgpu, pgpu);
			if (result) {
				getVgpuHosts().put(vgpu, gpuHost);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean allocateHostForVm(Vm vm, Host host) {
		if (!getVmTable().containsKey(vm.getUid())) {
			boolean result = host.vmCreate(vm);
			if (result) {
				getVmTable().put(vm.getUid(), host);
				return true;
			}
		}
		return false;
	}

	/**
	 * Allocates Hosts for a set of {@link GpuVm}s.
	 * 
	 * @param set of VMs
	 * @return a list of vm-result pairs
	 */
	public Map<GpuVm, Boolean> allocateHostForVms(List<GpuVm> vms) {
		Map<GpuVm, Boolean> results = new HashMap<GpuVm, Boolean>();
		for (GpuVm vm : vms) {
			boolean result = allocateHostForVm(vm);
			results.put(vm, result);
		}
		return results;
	}

	@Override
	public Host getHost(Vm vm) {
		return getVmTable().get(vm.getUid());
	}

	@Override
	public Host getHost(int vmId, int userId) {
		return getVmTable().get(Vm.getUid(userId, vmId));
	}

	/**
	 * @return the vmTable
	 */
	protected Map<String, Host> getVmTable() {
		return vmTable;
	}

	/**
	 * @param vmTable the vmTable to set
	 */
	protected void setVmTable(Map<String, Host> vmTable) {
		this.vmTable = vmTable;
	}

	protected List<GpuHost> getGpuHostList() {
		return gpuHostList;
	}

	protected void setGpuHostList(List<GpuHost> gpuHostList) {
		this.gpuHostList = new ArrayList<GpuHost>();
		for (GpuHost host : gpuHostList) {
			if (host.isGpuEquipped()) {
				getGpuHostList().add(host);
			}
		}
	}

	public Map<Vgpu, GpuHost> getVgpuHosts() {
		return vgpuHosts;
	}

	protected void setVgpuHosts(Map<Vgpu, GpuHost> vgpuHosts) {
		this.vgpuHosts = vgpuHosts;
	}

}
