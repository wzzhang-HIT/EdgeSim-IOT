package edgesim.core;

import java.util.List;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.VmAllocationPolicy;

import edgesim.edge_orchestrator.EdgeOrchestrator;
import edgesim.mobility.MobilityModel;
import edgesim.network.NetworkModel;
import edgesim.task_generator.LoadGeneratorModel;

public interface ScenarioFactory {
	/**
	 * provides abstract Load Generator Model
	 */
	public LoadGeneratorModel getLoadGeneratorModel();

	/**
	 * provides abstract Edge Orchestrator
	 */
	public EdgeOrchestrator getEdgeOrchestrator();

	/**
	 * provides abstract Mobility Model
	 */
	public MobilityModel getMobilityModel();

	/**
	 * provides abstract Network Model
	 */
	public NetworkModel getNetworkModel();

	/**
	 * provides abstract CPU Utilization Model
	 */
	public UtilizationModel getCpuUtilizationModel(SimSettings.APP_TYPES _taskType);

	/**
	 * provides abstract Vm Allocation Policy
	 */
	public VmAllocationPolicy getVmAllocationPolicy(List<? extends Host> list, int dataCenterIndex);
}
