package edgesim.utils;

import org.apache.commons.math3.distribution.ExponentialDistribution;
//import org.apache.commons.math3.distribution.NormalDistribution;

import edgesim.core.SimSettings;
import edgesim.core.SimSettings.APP_TYPES;

public class EdgeTask {
    public APP_TYPES taskType;
    public double startTime;
    public long length, inputFileSize, outputFileSize;
    public int pesNumber;
    public int mobileDeviceId;
    
    public EdgeTask(int _mobileDeviceId, APP_TYPES _taskType, double _startTime, ExponentialDistribution[][] expRngList) {
    	mobileDeviceId=_mobileDeviceId;
    	startTime=_startTime;
    	taskType=_taskType;
    	
    	inputFileSize = (long)expRngList[_taskType.ordinal()][0].sample();
    	outputFileSize =(long)expRngList[_taskType.ordinal()][1].sample();
    	length = (long)expRngList[_taskType.ordinal()][2].sample();
    	
//		changed @ 180310
//    	NormalDistribution[] nd=new NormalDistribution[2];
//    	nd[0]=new NormalDistribution(expRngList[_taskType.ordinal()][0].getMean(), 1);
//    	nd[1]=new NormalDistribution(expRngList[_taskType.ordinal()][2].getMean(), 1);
//    	
//    	inputFileSize=(long) nd[0].sample();
//    	length=(long) nd[1].sample();
//    	if(length<500) length+=500;
    	
    	pesNumber = (int)SimSettings.getInstance().getTaskLookUpTable()[_taskType.ordinal()][8];
	}
}
