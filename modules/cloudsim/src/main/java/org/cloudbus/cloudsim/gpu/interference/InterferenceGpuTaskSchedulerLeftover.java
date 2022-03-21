/**
 * 
 */
package org.cloudbus.cloudsim.gpu.interference;

import java.util.List;

import org.cloudbus.cloudsim.gpu.GpuTaskSchedulerLeftover;
import org.cloudbus.cloudsim.gpu.ResGpuTask;
import org.cloudbus.cloudsim.gpu.interference.models.InterferenceModel;
import org.cloudbus.cloudsim.util.MathUtil;

/**
 * This class extends {@link org.cloudbus.cloudsim.gpu.GpuTaskSchedulerLeftover}
 * to simulate inter-process interference caused by hardware conflicts.
 * 
 * @author Ahmad Siavashi
 * 
 */
public class InterferenceGpuTaskSchedulerLeftover extends GpuTaskSchedulerLeftover {

	/** The interference model */
	private InterferenceModel<ResGpuTask> interferenceModel;

	/**
	 * This class extends {@link org.cloudbus.cloudsim.gpu.GpuTaskSchedulerLeftover}
	 * to take the inter-process interference caused by hardware conflicts into
	 * account.
	 * 
	 * @param interferenceModel
	 */
	public InterferenceGpuTaskSchedulerLeftover(InterferenceModel<ResGpuTask> interferenceModel) {
		super();
		setInterferenceModel(interferenceModel);
	}

	@Override
	public double getTotalCurrentAvailableMipsForTask(ResGpuTask rcl, List<Double> mipsShare) {
		List<Double> availableMips = getInterferenceModel().getAvailableMips(rcl, mipsShare, getTaskExecList());
		double totalMips = MathUtil.sum(availableMips);
		return totalMips;
	}

	/**
	 * @return the interferenceModel
	 */
	public InterferenceModel<ResGpuTask> getInterferenceModel() {
		return interferenceModel;
	}

	/**
	 * @param interferenceModel
	 *            the interferenceModel to set
	 */
	protected void setInterferenceModel(InterferenceModel<ResGpuTask> interferenceModel) {
		this.interferenceModel = interferenceModel;
	}

}
