package org.cloudbus.cloudsim.gpu;

import org.cloudbus.cloudsim.gpu.hardware_assisted.GridVideoCardTags;

/**
 * 
 * Methods & constants that are related to {@link GpuHost GpuHosts} types and configurations.
 * 
 * @author Ahmad Siavashi
 *
 */
public class GpuHostTags {
	// Host Types
	public final static String HOST_CUSTOM = "Custom";
	public final static String DUAL_INTEL_XEON_E5_2620_V3 = "Dual Intel Xeon E5-2620 v3 (12 Cores, 2.40 GHz, 1 x NVIDIA GRID K1)";
	public final static String DUAL_INTEL_XEON_E5_2690_V4 = "Dual Intel Xeon E5-2690 v4 (28 Cores, 2.60 GHz, 1 x NVIDIA GRID K2)";

	// Instruction per Cycle (IPC)
	private final static int INTEL_XEON_E5_2600_V3_V4_IPC = 16;
	
	// 25 Gbit/s in MB/s
	public final static int NETWORK_BANDWIDTH_25_GBIT_PER_SEC = 3125;

	// Dual Intel Xeon E5-2620 v3
	/** 12 Cores */
	public final static int DUAL_INTEL_XEON_E5_2620_V3_NUM_PES = 12;
	/** Dual Intel Xeon E5-2620 v3 (2.4 GHz) */
	public final static double DUAL_INTEL_XEON_E5_2620_V3_PE_MIPS = 2400 * INTEL_XEON_E5_2600_V3_V4_IPC;
	/** 64GB RAM */
	public final static int DUAL_INTEL_XEON_E5_2620_V3_RAM = 64 * 1024;
	/** 1 x 1TB SATA Local Storage */
	public final static int DUAL_INTEL_XEON_E5_2620_V3_STORAGE = 1024 * 1024;
	/** 25 GB/s */
	public final static int DUAL_INTEL_XEON_E5_2620_V3_BW = NETWORK_BANDWIDTH_25_GBIT_PER_SEC;
	/** 1 Video Card Per GPU Host */
	public final static int DUAL_INTEL_XEON_E5_2620_V3_NUM_VIDEO_CARDS = 1;
	/** 1 NVIDIA Grid K1 Per Host */
	public final static String DUAL_INTEL_XEON_E5_2620_V3_VIDEO_CARD = GridVideoCardTags.NVIDIA_K1_CARD;

	// Dual Intel Xeon E5-2690 v4
	/** 28 Cores */
	public final static int DUAL_INTEL_XEON_E5_2690_V4_NUM_PES = 28;
	/** Dual Intel Xeon E5-2690 v4 (2.6 GHz) */
	public final static double DUAL_INTEL_XEON_E5_2690_V4_PE_MIPS = 2600 * INTEL_XEON_E5_2600_V3_V4_IPC;
	/** 128GB RAM */
	public final static int DUAL_INTEL_XEON_E5_2690_V4_RAM = 128 * 1024;
	/** 2 x 4TB SATA Local Storage */
	public final static int DUAL_INTEL_XEON_E5_2690_V4_STORAGE = 2 * 4096 * 1024;
	/** 25 GB/s */
	public final static int DUAL_INTEL_XEON_E5_2690_V4_BW = NETWORK_BANDWIDTH_25_GBIT_PER_SEC;
	/** 1 Video Card Per GPU Host */
	public final static int DUAL_INTEL_XEON_E5_2690_V4_NUM_VIDEO_CARDS = 1;
	/** 1 NVIDIA Grid K2 Per Host */
	public final static String DUAL_INTEL_XEON_E5_2690_V4_VIDEO_CARD = GridVideoCardTags.NVIDIA_K2_CARD;

}
