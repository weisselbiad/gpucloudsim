package org.cloudbus.cloudsim.gpu.hardware_assisted;

import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang3.ArrayUtils;
import org.cloudbus.cloudsim.gpu.BusTags;
import org.cloudbus.cloudsim.gpu.GpuTaskScheduler;
import org.cloudbus.cloudsim.gpu.Pgpu;
import org.cloudbus.cloudsim.gpu.Vgpu;

/**
 * 
 * Methods & constants that are related to {@link Vgpu virtual gpus} types and
 * configurations.
 * 
 * @author Ahmad Siavashi
 * 
 */

public class GridVgpuTags {

	public final static String DONT_CARE = null;

	/** NVIDIA GRID K1 Profiles */
	public final static int MAX_K120Q_VGPUS_PER_K1_PGPU = 8;
	public final static int MAX_K140Q_VGPUS_PER_K1_PGPU = 4;
	public final static int MAX_K160Q_VGPUS_PER_K1_PGPU = 2;
	public final static int MAX_K180Q_VGPUS_PER_K1_PGPU = 1;

	public final static String K1_K120Q = "NVIDIA K120Q";
	public final static String K1_K140Q = "NVIDIA K140Q";
	public final static String K1_K160Q = "NVIDIA K160Q";
	public final static String K1_K180Q = "NVIDIA K180Q";

	public final static String[] K1_VGPUS = { K1_K120Q, K1_K140Q, K1_K160Q, K1_K180Q };

	/** NVIDIA GRID K2 Profiles */
	public final static int MAX_K220Q_VGPUS_PER_K2_PGPU = 8;
	public final static int MAX_K240Q_VGPUS_PER_K2_PGPU = 4;
	public final static int MAX_K260Q_VGPUS_PER_K2_PGPU = 2;
	public final static int MAX_K280Q_VGPUS_PER_K2_PGPU = 1;

	public final static String K2_K220Q = "NVIDIA K220Q";
	public final static String K2_K240Q = "NVIDIA K240Q";
	public final static String K2_K260Q = "NVIDIA K260Q";
	public final static String K2_K280Q = "NVIDIA K280Q";

	public final static String[] K2_VGPUS = { K2_K220Q, K2_K240Q, K2_K260Q, K2_K280Q };

	/**
	 * K1 Board Pass through type 1/pGPU, 4/board
	 * 
	 * @return a GK107 physical GPU (pass through)
	 */
	public static Vgpu getK180Q(int vgpuId, GpuTaskScheduler scheduler) {
		final String type = K1_K180Q;
		// GPU Clock: 850 MHz
		final double mips = GridVideoCardTags.getGpuPeMipsFromFrequency(GridVideoCardTags.NVIDIA_K1_CARD, 850);
		// SMX count: 1
		final int numberOfPes = 1;
		// GDDRAM: 256 MB
		final int gddram = 4096;
		// Bandwidth: 28.5 GB/s
		final long bw = (long) 28.5 * 1024;
		Vgpu vgpu = new Vgpu(vgpuId, mips, numberOfPes, gddram, bw, type, DONT_CARE, scheduler, BusTags.PCI_E_3_X16_BW);
		return vgpu;
	}

	/**
	 * K1 Board K160Q vGPU type 2/pGPU, 8/board
	 * 
	 * @return a K140Q virtual GPU
	 */
	public static Vgpu getK160Q(int vgpuId, GpuTaskScheduler scheduler) {
		final String type = K1_K160Q;
		// GPU Clock: 850 MHz
		final double mips = GridVideoCardTags.getGpuPeMipsFromFrequency(GridVideoCardTags.NVIDIA_K1_CARD, 850);
		// SMX count: 1
		final int numberOfPes = 1;
		// GDDRAM: 2 GB
		final int gddram = 2048;
		// Bandwidth: 28.5 GB/s
		final long bw = (long) 28.5 * 1024;
		Vgpu vgpu = new Vgpu(vgpuId, mips, numberOfPes, gddram, bw, type, DONT_CARE, scheduler, BusTags.PCI_E_3_X16_BW);
		return vgpu;
	}

	/**
	 * K1 Board K140Q vGPU type 4/pGPU, 16/board
	 * 
	 * @return a K140Q virtual GPU
	 */
	public static Vgpu getK140Q(int vgpuId, GpuTaskScheduler scheduler) {
		final String type = K1_K140Q;
		// GPU Clock: 850 MHz
		final double mips = GridVideoCardTags.getGpuPeMipsFromFrequency(GridVideoCardTags.NVIDIA_K1_CARD, 850);
		// SMX count: 1
		final int numberOfPes = 1;
		// GDDRAM: 256 MB
		final int gddram = 1024;
		// Bandwidth: 28.5 GB/s
		final long bw = (long) 28.5 * 1024;
		Vgpu vgpu = new Vgpu(vgpuId, mips, numberOfPes, gddram, bw, type, DONT_CARE, scheduler, BusTags.PCI_E_3_X16_BW);
		return vgpu;
	}

	/**
	 * K1 Board K120Q vGPU type 8/pGPU, 32/board
	 * 
	 * @return a K120Q virtual GPU
	 */
	public static Vgpu getK120Q(int vgpuId, GpuTaskScheduler scheduler) {
		final String type = K1_K120Q;
		// GPU Clock: 850 MHz
		final double mips = GridVideoCardTags.getGpuPeMipsFromFrequency(GridVideoCardTags.NVIDIA_K1_CARD, 850);
		// SMX count: 1
		final int numberOfPes = 1;
		// GDDRAM: 512 MB
		final int gddram = 512;
		// Bandwidth: 28.5 GB/s
		final long bw = (long) 28.5 * 1024;
		Vgpu vgpu = new Vgpu(vgpuId, mips, numberOfPes, gddram, bw, type, DONT_CARE, scheduler, BusTags.PCI_E_3_X16_BW);
		return vgpu;
	}

	/**
	 * K2 Board K220Q vGPU type 8/pGPU, 16/board
	 * 
	 * @return a K220Q virtual GPU
	 */
	public static Vgpu getK220Q(int vgpuId, GpuTaskScheduler scheduler) {
		final String type = K2_K220Q;
		// GPU Clock: 745 MHz
		final double mips = GridVideoCardTags.getGpuPeMipsFromFrequency(GridVideoCardTags.NVIDIA_K2_CARD, 745);
		// SMX count: 8
		final int numberOfPes = 8;
		// GDDRAM: 512 MB
		final int gddram = 512;
		// Bandwidth: 160 GB/s
		final long bw = 160 * 1024;
		Vgpu vgpu = new Vgpu(vgpuId, mips, numberOfPes, gddram, bw, type, DONT_CARE, scheduler, BusTags.PCI_E_3_X16_BW);
		return vgpu;
	}

	/**
	 * K2 Board K240Q vGPU type 4/pGPU, 8/board
	 * 
	 * @return a K240Q virtual GPU
	 */
	public static Vgpu getK240Q(int vgpuId, GpuTaskScheduler scheduler) {
		final String type = K2_K240Q;
		// GPU Clock: 745 MHz
		final double mips = GridVideoCardTags.getGpuPeMipsFromFrequency(GridVideoCardTags.NVIDIA_K2_CARD, 745);
		// SMX count: 8
		final int numberOfPes = 8;
		// GDDRAM: 1 GB
		final int gddram = 1024;
		// Bandwidth: 160 GB/s
		final long bw = 160 * 1024;
		Vgpu vgpu = new Vgpu(vgpuId, mips, numberOfPes, gddram, bw, type, DONT_CARE, scheduler, BusTags.PCI_E_3_X16_BW);
		return vgpu;
	}

	/**
	 * K2 Board K260Q vGPU type 2/pGPU, 4/board
	 * 
	 * @return a K260Q virtual GPU
	 */
	public static Vgpu getK260Q(int vgpuId, GpuTaskScheduler scheduler) {
		final String type = K2_K260Q;
		// GPU Clock: 745 MHz
		final double mips = GridVideoCardTags.getGpuPeMipsFromFrequency(GridVideoCardTags.NVIDIA_K2_CARD, 745);
		// SMX count: 8
		final int numberOfPes = 8;
		// GDDRAM: 2 GB
		final int gddram = 2048;
		// Bandwidth: 160 GB/s
		final long bw = 160 * 1024;
		Vgpu vgpu = new Vgpu(vgpuId, mips, numberOfPes, gddram, bw, type, DONT_CARE, scheduler, BusTags.PCI_E_3_X16_BW);
		return vgpu;
	}

	/**
	 * K2 Board K280Q vGPU type 1/pGPU, 2/board
	 * 
	 * @return a K280Q virtual GPU
	 */
	public static Vgpu getK280Q(int vgpuId, GpuTaskScheduler scheduler) {
		final String type = K2_K280Q;
		// GPU Clock: 745 MHz
		final double mips = GridVideoCardTags.getGpuPeMipsFromFrequency(GridVideoCardTags.NVIDIA_K2_CARD, 745);
		// SMX count: 8
		final int numberOfPes = 8;
		// GDDRAM: 4 GB
		final int gddram = 4096;
		// Bandwidth: 160 GB/s
		final long bw = 160 * 1024;
		Vgpu vgpu = new Vgpu(vgpuId, mips, numberOfPes, gddram, bw, type, DONT_CARE, scheduler, BusTags.PCI_E_3_X16_BW);
		return vgpu;
	}
	
	/**
	 * Checks whether a videoCard type supports a given vgpu type or not.
	 * 
	 * @param videoCardType
	 *            type of the videoCard
	 * @param vgpuType
	 *            type of the vgpu
	 * @return $true if the videoCard supports the given vgpu type; $false
	 *         otherwise.
	 */
	public static boolean isVideoCardSuitable(String videoCardType, String vgpuType) {
		switch (videoCardType) {
		case GridVideoCardTags.NVIDIA_K1_CARD:
			return ArrayUtils.contains(GridVgpuTags.K1_VGPUS, vgpuType);
		case GridVideoCardTags.NVIDIA_K2_CARD:
			return ArrayUtils.contains(GridVgpuTags.K2_VGPUS, vgpuType);
		default:
			return true;
		}
	}

	/**
	 * Checks whether it is possible to allocate the given vgpu on any of the given
	 * pgpus or not.
	 * 
	 * @param currentResidents
	 *            videoCard's pgpus with their current resident vgpus
	 * @param newVgpu
	 *            the newly arrived vgpu
	 * @return $true if a suitable pgpu exists for <b>newVgpu</b>; $false otherwise
	 */
	public static boolean isPgpuSuitable(Entry<Pgpu, List<Vgpu>> currentResidents, Vgpu newVgpu) {
		Pgpu pgpu = currentResidents.getKey();
		List<Vgpu> vgpus = currentResidents.getValue();
		if (vgpus.isEmpty()) {
			return true;
		} else if (vgpus.get(0).getType() != newVgpu.getType()) {
			return false;
		}
		int currentNumberOfVgpus = vgpus.size();
		switch (newVgpu.getType()) {
		case GridVgpuTags.K1_K120Q:
			return currentNumberOfVgpus < GridVgpuTags.MAX_K120Q_VGPUS_PER_K1_PGPU;
		case GridVgpuTags.K1_K140Q:
			return currentNumberOfVgpus < GridVgpuTags.MAX_K140Q_VGPUS_PER_K1_PGPU;
		case GridVgpuTags.K1_K160Q:
			return currentNumberOfVgpus < GridVgpuTags.MAX_K160Q_VGPUS_PER_K1_PGPU;
		case GridVgpuTags.K1_K180Q:
			return currentNumberOfVgpus < GridVgpuTags.MAX_K180Q_VGPUS_PER_K1_PGPU;
		case GridVgpuTags.K2_K220Q:
			return currentNumberOfVgpus < GridVgpuTags.MAX_K220Q_VGPUS_PER_K2_PGPU;
		case GridVgpuTags.K2_K240Q:
			return currentNumberOfVgpus < GridVgpuTags.MAX_K240Q_VGPUS_PER_K2_PGPU;
		case GridVgpuTags.K2_K260Q:
			return currentNumberOfVgpus < GridVgpuTags.MAX_K260Q_VGPUS_PER_K2_PGPU;
		case GridVgpuTags.K2_K280Q:
			return currentNumberOfVgpus < GridVgpuTags.MAX_K280Q_VGPUS_PER_K2_PGPU;
		}
		if (pgpu.getGddramProvisioner().isSuitableForVgpu(newVgpu, newVgpu.getGddram())
				&& pgpu.getBwProvisioner().isSuitableForVgpu(newVgpu, newVgpu.getBw())) {
			return true;
		}
		return false;
	}

	/**
	 * Singleton class (cannot be instantiated)
	 */
	private GridVgpuTags() {
	}

}
