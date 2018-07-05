/*
 * Title:        EdgeCloudSim - Sample Scenario Factory
 * 
 * Description:  Sample factory providing the default
 *               instances of required abstract classes 
 * 
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 * Copyright (c) 2017, Bogazici University, Istanbul, Turkey
 */

package edgesim.sample_application;

import java.util.List;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.VmAllocationPolicy;

import edgesim.core.ScenarioFactory;
import edgesim.core.SimSettings.APP_TYPES;
import edgesim.edge_client.CpuUtilizationModel_Custom;
import edgesim.edge_orchestrator.BasicEdgeOrchestrator;
import edgesim.edge_orchestrator.EdgeOrchestrator;
import edgesim.edge_server.VmAllocationPolicy_Custom;
import edgesim.mobility.MobilityModel;
import edgesim.mobility.NomadicMobility;
import edgesim.network.MM1Queue;
import edgesim.network.NetworkModel;
import edgesim.task_generator.IdleActiveLoadGenerator;
import edgesim.task_generator.LoadGeneratorModel;

public class SampleScenarioFactory implements ScenarioFactory {
	private int numOfMobileDevice;
	private double simulationTime;
	private String orchestratorPolicy;
	private String simScenario;
	
	SampleScenarioFactory(int _numOfMobileDevice,
			double _simulationTime,
			String _orchestratorPolicy,
			String _simScenario){
		orchestratorPolicy = _orchestratorPolicy;
		numOfMobileDevice = _numOfMobileDevice;
		simulationTime = _simulationTime;
		simScenario = _simScenario;
	}
	
	@Override
	public LoadGeneratorModel getLoadGeneratorModel() {
		return new IdleActiveLoadGenerator(numOfMobileDevice, simulationTime, simScenario);
	}

	@Override
	public EdgeOrchestrator getEdgeOrchestrator() {
		return new BasicEdgeOrchestrator(orchestratorPolicy, simScenario);
	}

	@Override
	public MobilityModel getMobilityModel() {
		return new NomadicMobility(numOfMobileDevice,simulationTime);
	}

	@Override
	public NetworkModel getNetworkModel() {
		return new MM1Queue(numOfMobileDevice);
	}

	@Override
	public VmAllocationPolicy getVmAllocationPolicy(List<? extends Host> hostList, int dataCenterIndex) {
		return new VmAllocationPolicy_Custom(hostList,dataCenterIndex);
	}

	@Override
	public UtilizationModel getCpuUtilizationModel(APP_TYPES _taskType) {
		return new CpuUtilizationModel_Custom(_taskType);
	}
}
