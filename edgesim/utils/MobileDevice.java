package edgesim.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.math3.distribution.ExponentialDistribution;

import edgesim.core.SimSettings;
import edgesim.core.SimSettings.APP_TYPES;

public class MobileDevice {
	private int 			 id;//mobile ID
	private double 	 transPower;//transmission power(W)
	private double 	   cpuPower;//CPU power(W)
	private double 	computeCapa;//clock frequency(GHz)
	private double 	 lambdaTime;//weighting parameter of computational time
	private double lambdaEnergy;//weighting parameter of energy consumption
	private EdgeTask 	   task;//task to be executed(locally or on cloud)
	private int 	  channelId;//channel ID if chose to offload(-1 for executed locally)
	private double 	   distance;//distance to the base-station(km)
	private double 	channelGain;//channel gain
	private int 		 update;//used in offloading decision
	public List<Double> overhead;
	
	public MobileDevice(
			int id, 
			double transPower, 
			double computeCapa, 
			double lambdaTime, 
			double lambdaEnergy,
			EdgeTask task, 
			int channelId, 
			double distance) {
		super();
		this.id = id;
		this.transPower = transPower;
		this.computeCapa = computeCapa;
		this.lambdaTime = lambdaTime;
		this.lambdaEnergy = lambdaEnergy;
		this.task = task;
		this.channelId = channelId;
		this.distance = distance;
		this.update=-1;
		Random random=new Random();
		this.cpuPower=(Math.abs(random.nextInt()%3)+6)*1.0;
		this.channelGain=(Math.abs(random.nextInt()%21)+80)/(double)100;
		overhead=new ArrayList<>();
	}
	public int getUpdate() {
		return update;
	}
	public void setUpdate(int update) {
		this.update = update;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getTransPower() {
		return transPower;
	}
	public void setTransPower(double transPower) {
		this.transPower = transPower;
	}
	public double getComputeCapa() {
		return computeCapa;
	}
	public void setComputeCapa(double computeCapa) {
		this.computeCapa = computeCapa;
	}
	public double getLambdaTime() {
		return lambdaTime;
	}
	public void setLambdaTime(double lambdaTime) {
		this.lambdaTime = lambdaTime;
	}
	public double getLambdaEnergy() {
		return lambdaEnergy;
	}
	public void setLambdaEnergy(double lambdaEnergy) {
		this.lambdaEnergy = lambdaEnergy;
	}
	public EdgeTask getTask() {
		return task;
	}
	public void setTask(EdgeTask task) {
		this.task = task;
	}
	public int getChannelId() {
		return channelId;
	}
	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	public double getChannelGain() {
		return channelGain;
	}
	public double overheadLocal() {
		return this.getLambdaTime()*this.calculateTime()+this.getLambdaEnergy()*this.calculateEnergy();
	}
		
	public double getCpuPower() {
		return cpuPower;
	}
	public double calculateTime() {
		double time=this.task.length/(this.computeCapa*1000);
		return time;
	}
	
	public double calculateEnergy() {
		double energy=this.calculateTime()*this.cpuPower;
		return energy;
	}
	
	public static List<MobileDevice> generateMobiles(int numOfMobile) {
		List<MobileDevice> mobiles=new ArrayList<>();
		Random random=new Random();
		int[] bn={1000,500,2000,1500};//data size
		int[] dn={3000,1000,4000,2000};//task length
		int[] rn={25,20,250,2000};//result size
		double[] fm={0.5,0.8,1.0};//clock frequency
		double[] lambdas={0,0.2,0.5,0.8,1.0};//lambda for time(10%,20%,40%,20%,10%)
		
		//generate the mobiles
		for(int i=0;i<numOfMobile;i++) {
			SimSettings.APP_TYPES[] types={
					APP_TYPES.AUGMENTED_REALITY,
					APP_TYPES.HEALTH_APP,
					APP_TYPES.HEAVY_COMP_APP,
					APP_TYPES.INFOTAINMENT_APP};
			int type=Math.abs(random.nextInt()%4);
			int upload=bn[type];
			int result=rn[type];
			int length=dn[type];
			ExponentialDistribution[][] expRngList=new ExponentialDistribution[4][3];
			for(int j=0;j<APP_TYPES.values().length;j++) {
				expRngList[j][0]=new ExponentialDistribution(upload);
				expRngList[j][1]=new ExponentialDistribution(result);
				expRngList[j][2]=new ExponentialDistribution(length);
			}
			int lambda=Math.abs(random.nextInt()%100);
			double lambdaTime;
			if(lambda<10) lambdaTime=lambdas[0];
			else if(lambda<30) lambdaTime=lambdas[1];
			else if(lambda<70) lambdaTime=lambdas[2];
			else if(lambda<90) lambdaTime=lambdas[3];
			else lambdaTime=lambdas[4];
			EdgeTask task=new EdgeTask(i+1, types[type], 0, expRngList);
			int clockFreq=Math.abs(random.nextInt()%3);
			MobileDevice mobile=new MobileDevice(i+1, 2, fm[clockFreq], lambdaTime, 1-lambdaTime, task, -1, 1);
			mobiles.add(mobile);
		}
		return mobiles;
	}
	
	public static void main(String[] args) {
		int numOfMobile=30;
		
		List<MobileDevice> mobiles=generateMobiles(numOfMobile);
		int count=0;
		//display the mobiles
		for(int i=0;i<numOfMobile;i++) {
//			System.out.format("---------------Mobile #%02d---------------\n",mobiles.get(i).getId());
//			System.out.println("transmission power:"+mobiles.get(i).getTransPower());
//			System.out.println("capability:"+mobiles.get(i).getComputeCapa());
//			System.out.println("CPU power:"+mobiles.get(i).getCpuPower());
//			System.out.println("data size:"+mobiles.get(i).getTask().inputFileSize);
//			System.out.println("length:"+mobiles.get(i).getTask().length);
//			System.out.println("result:"+mobiles.get(i).getTask().outputFileSize);
//			System.out.println("lambda(t):"+mobiles.get(i).getLambdaTime());
//			System.out.println("lambda(e):"+mobiles.get(i).getLambdaEnergy());
//			System.out.println("overhead(t):"+mobiles.get(i).calculateTime());
//			System.out.println("overhead(e):"+mobiles.get(i).calculateEnergy());
//			System.out.println("overhead:"+mobiles.get(i).overheadLocal());
//			System.out.println("channel gain:"+mobiles.get(i).getChannelGain());
			double mec=mobiles.get(i).getTask().length/25000.0;
			double cc=((mobiles.get(i).getTask().inputFileSize
					+mobiles.get(i).getTask().outputFileSize)/(128*20.0)+mobiles.get(i).getTask().length/200000.0);
			if(mec>cc) count++;
		}
		System.out.println(count);
	}
}
