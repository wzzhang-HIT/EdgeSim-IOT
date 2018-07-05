package edgesim.network;

import edgesim.utils.Location;

public abstract class NetworkModel {
	protected int numberOfMobileDevices;

	public NetworkModel(int _numberOfMobileDevices){
		numberOfMobileDevices=_numberOfMobileDevices;
	};
	
	/**
	* initializes costom network model
	*/
	public abstract void initialize();
	
    /**
    * calculates the upload delay from source to destination device
    */
	public abstract double getUploadDelay(int sourceDeviceId, int destDeviceId, double dataSize);
	
    /**
    * calculates the download delay from source to destination device
    */
	public abstract double getDownloadDelay(int sourceDeviceId, int destDeviceId, double dataSize);
	
    /**
    * Mobile device manager should inform network manager about the network operation
    * This information may be important for some network delay models
    */
	public abstract void uploadStarted(Location accessPointLocation, int destDeviceId);
	public abstract void uploadFinished(Location accessPointLocation, int destDeviceId);
	public abstract void downloadStarted(Location accessPointLocation, int sourceDeviceId);
	public abstract void downloadFinished(Location accessPointLocation, int sourceDeviceId);
}
