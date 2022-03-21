package org.cloudbus.cloudsim.gpu.remote;

import org.cloudbus.cloudsim.gpu.Vgpu;

/**
 * 
 * The modes supported in remote GPU virtualization.
 * 
 * @author Ahmad Siavashi
 *
 */
public class RemoteVgpuTags {
	public static final String REMOTE_EXCLUSIVE = "RE";
	public static final String REMOTE_SHARED = "RS";
	public static final String LOCAL_EXCLUSIVE = "LE";
	public static final String LOCAL_SHARED = "LS";

	public static String getVgpuMode(String vgpuMode) {
		switch (vgpuMode) {
		case RemoteVgpuTags.REMOTE_EXCLUSIVE:
			return "RE";
		case RemoteVgpuTags.REMOTE_SHARED:
			return "RS";
		case RemoteVgpuTags.LOCAL_EXCLUSIVE:
			return "LE";
		case RemoteVgpuTags.LOCAL_SHARED:
			return "LS";
		default:
			return "Unknown";
		}
	}

	public static boolean isLocal(Vgpu vgpu) {
		String tenancy = vgpu.getTenancy();
		switch (tenancy) {
		case LOCAL_EXCLUSIVE:
		case LOCAL_SHARED:
			return true;
		default:
			return false;
		}
	}
	
	public static boolean isRemote(Vgpu vgpu) {
		return !isLocal(vgpu);
	}
	
	public static boolean isShared(Vgpu vgpu) {
		String tenancy = vgpu.getTenancy();
		switch (tenancy) {
		case LOCAL_SHARED:
		case REMOTE_SHARED:
			return true;
		default:
			return false;
		}
	}

	public static boolean isExclusive(Vgpu vgpu) {
		return !isShared(vgpu);
	}
	
}
