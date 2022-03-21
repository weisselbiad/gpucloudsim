package org.cloudbus.cloudsim.gpu;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link GpuTaskScheduler} is an abstract class that represents the policy of
 * scheduling performed by a {@link Vgpu} to run its {@link GpuTask GpuTasks}.
 * So, extending classes must execute GpuTasks. Also, the interface for
 * gpuTask management is also implemented in this class. Each Vgpu has to have
 * its own instance of a GpuTaskScheduler.
 * 
 * 
 * @author Ahmad Siavashi
 *
 */
public abstract class GpuTaskScheduler {

	/** The previous time. */
	private double previousTime;

	/**
	 * The list of current mips share available for the {@link Vgpu} using the
	 * scheduler.
	 */
	private List<Double> currentMipsShare;

	/** The list of {@link GpuTask} waiting to be executed on the {@link Vgpu}. */
	protected List<? extends ResGpuTask> taskWaitingList;

	/** The list of tasks being executed on the {@link Vgpu}. */
	protected List<? extends ResGpuTask> taskExecList;

	/** The list of paused {@link Vgpu} tasks. */
	protected List<? extends ResGpuTask> taskPausedList;

	/** The list of finished {@link Vgpu} gpu tasks. */
	protected List<? extends ResGpuTask> taskFinishedList;

	/** The list of failed {@link Vgpu} tasks. */
	protected List<? extends ResGpuTask> taskFailedList;

	/**
	 * Creates a new GpuTaskScheduler object. A GpuTaskScheduler must be created
	 * before starting the actual simulation.
	 * 
	 * @pre $none
	 * @post $none
	 */
	public GpuTaskScheduler() {
		setPreviousTime(0.0);
		taskWaitingList = new ArrayList<ResGpuTask>();
		taskExecList = new ArrayList<ResGpuTask>();
		taskPausedList = new ArrayList<ResGpuTask>();
		taskFinishedList = new ArrayList<ResGpuTask>();
		taskFailedList = new ArrayList<ResGpuTask>();
	}

	/**
	 * Updates the processing of {@link GpuTask}s running under management of this
	 * scheduler.
	 * 
	 * @param currentTime current simulation time
	 * @param mipsShare   list with MIPS share of each Pe available to the scheduler
	 * @return the predicted completion time of the earliest finishing gpu task, or
	 *         {@link Double#MAX_VALUE} if there is no next events
	 * @pre currentTime >= 0
	 * @post $none
	 */
	public abstract double updateGpuTaskProcessing(double currentTime, List<Double> mipsShare);

	/**
	 * Receives a {@link GpuTask} to be executed in the {@link Vgpu} managed by this
	 * scheduler.
	 * 
	 * @param gl the submited gpu task
	 * @return expected finish time of this gpu task, or 0 if it is in a waiting
	 *         queue
	 * @pre gl != null
	 * @post $none
	 */
	public abstract double taskSubmit(GpuTask gl);

	/**
	 * Cancels execution of a {@link GpuTask}.
	 * 
	 * @param clId ID of the gpu task being canceled
	 * @return the canceled gpu task, $null if not found
	 * @pre $none
	 * @post $none
	 */
	public abstract GpuTask taskCancel(int clId);

	/**
	 * Pauses execution of a {@link GpuTask}.
	 * 
	 * @param clId ID of the gpu task being paused
	 * @return $true if gpu task paused, $false otherwise
	 * @pre $none
	 * @post $none
	 */
	public abstract boolean taskPause(int clId);

	/**
	 * Resumes execution of a paused {@link GpuTask}.
	 * 
	 * @param clId ID of the gpu task being resumed
	 * @return expected finish time of the gpu task, 0.0 if queued
	 * @pre $none
	 * @post $none
	 */
	public abstract double taskResume(int clId);

	/**
	 * Processes a finished {@link GpuTask}.
	 * 
	 * @param rcl finished gpu task
	 * @pre rgl != $null
	 * @post $none
	 */
	public abstract void taskFinish(ResGpuTask rcl);

	/**
	 * Gets the status of a {@link GpuTask}.
	 * 
	 * @param taskId ID of the gpu task
	 * @return status of the gpu task, -1 if gpu task not found
	 */
	public int getTaskStatus(int taskId) {
		for (ResGpuTask rcl : getTaskExecList()) {
			if (rcl.getTaskId() == taskId) {
				return rcl.getTaskStatus();
			}
		}

		for (ResGpuTask rcl : getTaskPausedList()) {
			if (rcl.getTaskId() == taskId) {
				return rcl.getTaskStatus();
			}
		}

		for (ResGpuTask rcl : getTaskWaitingList()) {
			if (rcl.getTaskId() == taskId) {
				return rcl.getTaskStatus();
			}
		}

		return -1;
	}

	/**
	 * Informs if there is any {@link GpuTask} that finished to execute in the
	 * {@link Vgpu} managed by this scheduler.
	 * 
	 * @return $true if there is at least one finished gpu task; $false otherwise
	 * @pre $none
	 * @post $none
	 */
	public boolean hasFinishedTasks() {
		return !getTaskFinishedList().isEmpty();
	}

	/**
	 * Returns the next {@link GpuTask} in the finished list.
	 * 
	 * @return a finished gpu task or $null if the respective list is empty
	 * @pre $none
	 * @post $none
	 */
	public ResGpuTask getNextFinishedTask() {
		if (!getTaskFinishedList().isEmpty()) {
			return getTaskFinishedList().remove(0);
		}
		return null;
	}

	/**
	 * Returns the number of gpu tasks running in the virtual gpu.
	 * 
	 * @return number of gpu tasks running
	 * @pre $none
	 * @post $none
	 */
	public int runningTasks() {
		return getTaskExecList().size();
	}

	/**
	 * Returns one {@link GpuTask} to migrate to another {@link Vgpu}.
	 * 
	 * @return one running gpu Task
	 * @pre $none
	 * @post $none
	 */
	public abstract GpuTask migrateTask();

	/**
	 * Gets total {@link Vgpu} utilization percentage of all {@link GpuTask
	 * GpuTasks}, according to vgpu utilization model of each one.
	 * 
	 * @param time the time to get the current vgpu utilization
	 * @return total utilization
	 */
	public abstract double getTotalUtilizationOfGpu(double time);

	/**
	 * Gets the current requested mips.
	 * 
	 * @return the current mips
	 */
	public abstract List<Double> getCurrentRequestedMips();

	/**
	 * Gets the total current available mips for the {@link GpuTask}.
	 * 
	 * @param rcl       the rcl
	 * @param mipsShare the mips share
	 * @return the total current mips
	 */
	public abstract double getTotalCurrentAvailableMipsForTask(ResGpuTask rcl, List<Double> mipsShare);

	/**
	 * Gets the total current requested mips for a given {@link GpuTask}.
	 * 
	 * @param rcl  the rcl
	 * @param time the time
	 * @return the total current requested mips for the given gpuTask
	 */
	public abstract double getTotalCurrentRequestedMipsForTask(ResGpuTask rcl, double time);

	/**
	 * Gets the total current allocated mips for gpu task.
	 * 
	 * @param rcl  the rcl
	 * @param time the time
	 * @return the total current allocated mips for gpu task
	 */
	public abstract double getTotalCurrentAllocatedMipsForTask(ResGpuTask rcl, double time);

	/**
	 * Gets the current allocated mips for gpu task.
	 * 
	 * @param rcl  the rcl
	 * @param time the time
	 * @return the current allocated mips for gpu task
	 */
	public abstract List<Double> getCurrentAllocatedMipsForTask(ResGpuTask rcl, double time);

	/**
	 * Gets the current requested GDDRam.
	 * 
	 * @return the current requested GDDRam
	 */
	public abstract double getCurrentRequestedUtilizationOfGddram();

	/**
	 * Gets the current requested GDDRAM bw.
	 * 
	 * @return the current requested GDDRAM bw
	 */
	public abstract double getCurrentRequestedUtilizationOfBw();

	/**
	 * Gets the previous time.
	 * 
	 * @return the previous time
	 */
	public double getPreviousTime() {
		return previousTime;
	}

	/**
	 * Sets the previous time.
	 * 
	 * @param previousTime the new previous time
	 */
	protected void setPreviousTime(double previousTime) {
		this.previousTime = previousTime;
	}

	/**
	 * Sets the current mips share.
	 * 
	 * @param currentMipsShare the new current mips share
	 */
	protected void setCurrentMipsShare(List<Double> currentMipsShare) {
		this.currentMipsShare = currentMipsShare;
	}

	/**
	 * Gets the current mips share.
	 * 
	 * @return the current mips share
	 */
	public List<Double> getCurrentMipsShare() {
		return currentMipsShare;
	}

	/**
	 * Gets the gpu task waiting list.
	 * 
	 * @param <T> the generic type
	 * @return the gpu task waiting list
	 */
	@SuppressWarnings("unchecked")
	public <T extends ResGpuTask> List<T> getTaskWaitingList() {
		return (List<T>) taskWaitingList;
	}

	/**
	 * GpuTask waiting list.
	 * 
	 * @param <T>             the generic type
	 * @param taskWaitingList the gpu task waiting list
	 */
	protected <T extends ResGpuTask> void setTaskWaitingList(List<T> taskWaitingList) {
		this.taskWaitingList = taskWaitingList;
	}

	/**
	 * Gets the gpu task exec list.
	 * 
	 * @param <T> the generic type
	 * @return the gpu task exec list
	 */
	@SuppressWarnings("unchecked")
	public <T extends ResGpuTask> List<T> getTaskExecList() {
		return (List<T>) taskExecList;
	}

	/**
	 * Sets the gpu task exec list.
	 * 
	 * @param <T>          the generic type
	 * @param taskExecList the new gpu task exec list
	 */
	protected <T extends ResGpuTask> void setTaskExecList(List<T> taskExecList) {
		this.taskExecList = taskExecList;
	}

	/**
	 * Gets the gpu task paused list.
	 * 
	 * @param <T> the generic type
	 * @return the gpu task paused list
	 */
	@SuppressWarnings("unchecked")
	public <T extends ResGpuTask> List<T> getTaskPausedList() {
		return (List<T>) taskPausedList;
	}

	/**
	 * Sets the gpu task paused list.
	 * 
	 * @param <T>            the generic type
	 * @param taskPausedList the new gpu task paused list
	 */
	protected <T extends ResGpuTask> void setTaskPausedList(List<T> taskPausedList) {
		this.taskPausedList = taskPausedList;
	}

	/**
	 * Gets the gpu task finished list.
	 * 
	 * @param <T> the generic type
	 * @return the gpu task finished list
	 */
	@SuppressWarnings("unchecked")
	public <T extends ResGpuTask> List<T> getTaskFinishedList() {
		return (List<T>) taskFinishedList;
	}

	/**
	 * Sets the gpu task finished list.
	 * 
	 * @param <T>              the generic type
	 * @param taskFinishedList the new gpu task finished list
	 */
	protected <T extends ResGpuTask> void setTaskFinishedList(List<T> taskFinishedList) {
		this.taskFinishedList = taskFinishedList;
	}

	/**
	 * Gets the gpu task failed list.
	 * 
	 * @param <T> the generic type
	 * @return the gpu task failed list.
	 */
	@SuppressWarnings("unchecked")
	public <T extends ResGpuTask> List<T> getTaskFailedList() {
		return (List<T>) taskFailedList;
	}

	/**
	 * Sets the gpu task failed list.
	 * 
	 * @param <T>            the generic type
	 * @param taskFailedList the new gpu task failed list.
	 */
	protected <T extends ResGpuTask> void setTaskFailedList(List<T> taskFailedList) {
		this.taskFailedList = taskFailedList;
	}

}
