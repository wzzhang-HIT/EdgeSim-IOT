package edgesim.task_generator;

import java.util.List;

import edgesim.utils.EdgeTask;

public abstract class LoadGeneratorModel {
	protected List<EdgeTask> taskList;
	protected int numberOfMobileDevices;
	protected double simulationTime;
	protected String simScenario;
	
	public LoadGeneratorModel(int _numberOfMobileDevices, double _simulationTime, String _simScenario){
		numberOfMobileDevices=_numberOfMobileDevices;
		simulationTime=_simulationTime;
		simScenario=_simScenario;
	};
	
	/*
	 * each task has a virtual start time
	 * it will be used while generating task
	 */
	public List<EdgeTask> getTaskList() {
		return taskList;
	}

	/*
	 * fill task list according to related task generation model
	 */
	public abstract void initializeModel();
}
