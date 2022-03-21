package org.cloudbus.cloudsim.gpu.provisioners;

import java.util.HashMap;
import java.util.Map;

import org.cloudbus.cloudsim.gpu.Vgpu;

/**
 * GpuGddramProvisionerSimple is an extension of {@link GpuGddramProvisioner}
 * which uses a best-effort policy to allocate memory to Vgpus: if there is
 * available gddram on the Pgpu, it allocates; otherwise, it fails.
 * 
 * @author Ahmad Siavashi
 * 
 */
public class GpuGddramProvisionerSimple extends GpuGddramProvisioner {

	/**
	 * The GDDRAM map, where each key is a Vgpu and each value is the amount of
	 * GDDRAM allocated to that Vgpu.
	 */
	private Map<Vgpu, Integer> gddramTable;

	/**
	 * Instantiates a new gddram provisioner simple.
	 * 
	 * @param availableGddram
	 *            The total gddram capacity from the host that the provisioner can
	 *            allocate to Vgpus.
	 */
	public GpuGddramProvisionerSimple(int availableGddram) {
		super(availableGddram);
		setGddramTable(new HashMap<Vgpu, Integer>());
	}

	@Override
	public boolean allocateGddramForVgpu(Vgpu vgpu, int gddram) {
		int maxRam = vgpu.getGddram();
		/*
		 * If the requested amount of GDDRAM to be allocated to the Vgpu is greater than
		 * the amount of Vgpu is in fact requiring, allocate only the amount defined in
		 * the Vgpu requirements.
		 */
		if (gddram >= maxRam) {
			gddram = maxRam;
		}

		deallocateGddramForVgpu(vgpu);

		if (getAvailableGddram() >= gddram) {
			setAvailableGddram(getAvailableGddram() - gddram);
			getGddramTable().put(vgpu, gddram);
			vgpu.setCurrentAllocatedGddram(getAllocatedGddramForVgpu(vgpu));
			return true;
		}

		vgpu.setCurrentAllocatedGddram(getAllocatedGddramForVgpu(vgpu));

		return false;
	}

	@Override
	public int getAllocatedGddramForVgpu(Vgpu vgpu) {
		if (getGddramTable().containsKey(vgpu)) {
			return getGddramTable().get(vgpu);
		}
		return 0;
	}

	@Override
	public void deallocateGddramForVgpu(Vgpu vgpu) {
		if (getGddramTable().containsKey(vgpu)) {
			int amountFreed = getGddramTable().remove(vgpu);
			setAvailableGddram(getAvailableGddram() + amountFreed);
			vgpu.setCurrentAllocatedGddram(0);
		}
	}

	@Override
	public void deallocateGddramForAllVgpus() {
		super.deallocateGddramForAllVgpus();
		getGddramTable().clear();
	}

	@Override
	public boolean isSuitableForVgpu(Vgpu vgpu, int gddram) {
		int allocatedGddam = getAllocatedGddramForVgpu(vgpu);
		boolean result = allocateGddramForVgpu(vgpu, gddram);
		deallocateGddramForVgpu(vgpu);
		if (allocatedGddam > 0) {
			allocateGddramForVgpu(vgpu, allocatedGddam);
		}
		return result;
	}

	/**
	 * @see #getAllocatedGddramForVgpu(Vgpu)
	 * @return the gddramTable
	 */
	protected Map<Vgpu, Integer> getGddramTable() {
		return gddramTable;
	}

	/**
	 * @param gddramTable
	 *            the gddramTable to set
	 */
	protected void setGddramTable(Map<Vgpu, Integer> gddramTable) {
		this.gddramTable = gddramTable;
	}

}
