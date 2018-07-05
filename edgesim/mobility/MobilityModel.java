package edgesim.mobility;

import edgesim.utils.Location;

public abstract class MobilityModel {
	protected int numberOfMobileDevices;
	protected double simulationTime;
	
	public MobilityModel(int _numberOfMobileDevices, double _simulationTime){
		numberOfMobileDevices=_numberOfMobileDevices;
		simulationTime=_simulationTime;
	};
	
	/*
	 * calculate location of the devices according to related mobility model
	 */
	public abstract void initialize();
	
	/*
	 * returns location of a device at a certain time
	 */
	public abstract Location getLocation(int deviceId, double time);
}
