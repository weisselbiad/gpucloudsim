package org.cloudbus.cloudsim.gpu.remote;

import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.gpu.GpuTask;

public class RemoteGpuTask extends GpuTask {

	/**
	 * The communication overhead when executed on a remote GPU.
	 */
	private float communicationOverhead;

	public RemoteGpuTask(int taskId, long blockLength, int numberOfBlocks, long inputSize, long outputSize,
			long requestedGddramSize, float communicationOverhead, UtilizationModel utilizationModelGpu,
			UtilizationModel utilizationModelGddram, UtilizationModel utilizationModelBw) {
		super(taskId, blockLength, numberOfBlocks, inputSize, outputSize, requestedGddramSize, utilizationModelGpu,
				utilizationModelGddram, utilizationModelBw);
		this.communicationOverhead = communicationOverhead;
	}

	public RemoteGpuTask(int taskId, long blockLength, int numberOfBlocks, long inputSize, long outputSize,
			long requestedGddramSize, float communicationOverhead, UtilizationModel utilizationModelGpu,
			UtilizationModel utilizationModelGddram, UtilizationModel utilizationModelBw, boolean record) {
		super(taskId, blockLength, numberOfBlocks, inputSize, outputSize, requestedGddramSize, utilizationModelGpu,
				utilizationModelGddram, utilizationModelBw, record);
		this.communicationOverhead = communicationOverhead;
	}

	public float getCommunicationOverhead() {
		return communicationOverhead;
	}

}
