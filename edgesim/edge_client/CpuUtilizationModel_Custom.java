package edgesim.edge_client;

import org.cloudbus.cloudsim.UtilizationModel;

import edgesim.core.SimSettings;

public class CpuUtilizationModel_Custom implements UtilizationModel {
	private SimSettings.APP_TYPES taskType;
	
	public CpuUtilizationModel_Custom(SimSettings.APP_TYPES _taskType){
		taskType=_taskType;
	}
	
	/*
	 * (non-Javadoc)
	 * @see cloudsim.power.UtilizationModel#getUtilization(double)
	 */
	@Override
	public double getUtilization(double time) {
		return SimSettings.getInstance().getTaskLookUpTable()[taskType.ordinal()][9];
	}
	
	public double predictUtilization(SimSettings.VM_TYPES _vmType){
		return SimSettings.getInstance().getTaskLookUpTable()[taskType.ordinal()][9];
	}
}
