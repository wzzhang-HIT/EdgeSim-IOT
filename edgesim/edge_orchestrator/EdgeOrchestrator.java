package edgesim.edge_orchestrator;

import edgesim.edge_client.Task;
import edgesim.edge_server.EdgeVM;

public abstract class EdgeOrchestrator {
	protected String policy;
	protected String simScenario;
	
	public EdgeOrchestrator(String _policy, String _simScenario){
		policy = _policy;
		simScenario = _simScenario;
	}
	
	/*
	 * initialize edge orchestrator if needed
	 */
	public abstract void initialize();
	
	/*
	 * decides where to offload
	 */
	public abstract int getDeviceToOffload(Task task);
	
	/*
	 * returns proper VM from the related edge orchestrator point of view
	 */
	public abstract EdgeVM getVmToOffload(Task task);
}
