package edgesim.utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Channel {
	
//	private static final int BACKGROUNDND_NOISE=-80;//dBm or 1.0e-11 mW
	private static final int CLOCK_FREQUENCY_ON_CLOUD=10;//GHz
	
	private int id;
	private double bandwidth;
	private double receivedPower;
	private List<Integer> mobileDevices;
	
	public Channel(int id, double bandwidth) {
		this.id=id;
		this.bandwidth=bandwidth;
		receivedPower=0;
		mobileDevices=new LinkedList<>();
	}

	public double getBandwidth() {
		return bandwidth;
	}

	public double getReceivedPower() {
		return receivedPower;
	}
	
	public int getId() {
		return id;
	}

	public void addMobileDevice(MobileDevice md) {
		mobileDevices.add(md.getId());
		//calculate the received power
		receivedPower+=md.getTransPower()*md.getChannelGain();
	}
	
	public void removeMobileDevice(MobileDevice md) {
		for(int i=0;i<mobileDevices.size();i++) {
			if(md.getId()==i) mobileDevices.remove(i);
		}
		//calculate the received power
		receivedPower-=md.getTransPower()*md.getChannelGain();
	}
	
	//test
	public static int mobileDeviceNum;
	public static int channelNum;
	public static List<MobileDevice> mobiles=new ArrayList<>();
	public static List<Channel> channels=new ArrayList<>();
	public static List<Double> rp=new ArrayList<>();
	
	public static void main(String[] args) {
		
		//executeOnCloud(50);
		
		int testTimes=1;
		int total=0;
		for(int k=0;k<testTimes;k++) {
			for(int i=15;i<=50;i+=5) {
				mobiles=new ArrayList<>();
				channels=new ArrayList<>();
				rp=new ArrayList<>();
				int iteration=iteration(i,5);
				total+=getBeneficialNum();
			}
		}
//		System.out.println((double)total/testTimes);
//		for(MobileDevice md:mobiles) {
//			for(Double d:md.overhead) {
//				System.out.print(d+",");
//			}
//			System.out.println();
//		}
	}
	
	private static void executeOnCloud(int mobileNum) {
		Random random=new Random();
		mobiles=MobileDevice.generateMobiles(mobileNum);
		
		for(int i=0;i<5;i++) {
			Channel channel=new Channel(i, 5);
			channels.add(channel);
		}
		
		for(int i=0;i<mobileNum;i++) {
			int cid=Math.abs(random.nextInt()%5);
			mobiles.get(i).setChannelId(cid);
			channels.get(cid).addMobileDevice(mobiles.get(i));
		}
		
		int count=0;
		
		for(MobileDevice md:mobiles) {
			double cloud=getOverhead2(md);
			double local=md.overheadLocal();
			if(cloud<local) count++;
		}
		System.out.println(count);
	}
	
	private static int iteration(int numOfMobile,int numOfChannel) {
		Random random=new Random();
		mobileDeviceNum=numOfMobile;
		
		channelNum=numOfChannel;
		
		mobiles=MobileDevice.generateMobiles(mobileDeviceNum);
		
		//Local Computing by All Users
		double totalLocalOverhead=0;
		for(MobileDevice md:mobiles) {
			totalLocalOverhead+=md.overheadLocal();
		}
		System.out.println("Local:"+totalLocalOverhead);
		
		//generate the channel
		for(int i=0;i<channelNum;i++) {
			Channel channel=new Channel(i, 5);
			channels.add(channel);
			rp.add((double) 0);
		}
		
		//begin the game
		int iteration=0;
		
		//select a random channel for each mobile device in the first iteration
		for(int i=0;i<mobileDeviceNum;i++) {
			MobileDevice md=mobiles.get(i);
			int selectedChannel=Math.abs(random.nextInt()%channelNum);
			md.setUpdate(selectedChannel);
		}
		
		//map stores the choices of mobile devices
		int[] map=new int[mobileDeviceNum];
		while(true) {
			iteration++;
			boolean updated=false;
			getBeneficialNum();
			//System.out.println(getBeneficialNum());
			//send pilot signal to the base station
			sendPilotSignalToBase();
			
			//receive the information of the received powers on all the channels from the base station
			int updates=0;
			for(int i=0;i<mobileDeviceNum;i++) {
				MobileDevice md=mobiles.get(i);
				int update=receiveInfoFromBase(md);
				md.setUpdate(update);
				if(update==-2) continue;
				map[i]=update;
				updates++;
			}
			
			//random select a mobile device from those whose update is not -2
			if(updates>0) {
				Random r=new Random();
				int count=Math.abs(r.nextInt()%updates+1);
				int id=0,j=0;
				for(int i=0;i<mobileDeviceNum;i++) {
					if(mobiles.get(i).getUpdate()==-2) continue;
					j++;
					if(j==count) {
						id=i;
						break;
					}
				}
				MobileDevice md=mobiles.get(id);
				//update channel
				int update=map[id];
				updated=true;
				if(update>-1) {
					channels.get(update).addMobileDevice(md);
				}
				if(md.getChannelId()>=0)
					channels.get(md.getChannelId()).removeMobileDevice(md);
				md.setChannelId(update);
				md.setUpdate(-2);
			}
			
			if(updated==false) break;
		}
		
		//Distributed Cloud Computing Offloading
		double totalOffloadOverload=0;
		for(MobileDevice md:mobiles) {
			totalOffloadOverload+=md.overhead.get(md.overhead.size()-1);
		}
		System.out.println("Distributed:"+totalOffloadOverload);
		
		//Cloud Computing by All Users
		channels=new ArrayList<>();
		for(int i=0;i<5;i++) {
			Channel channel=new Channel(i, 5);
			channels.add(channel);
		}
		
		for(int i=0;i<mobileDeviceNum;i++) {
			int cid=Math.abs(random.nextInt()%5);
			mobiles.get(i).setChannelId(cid);
			channels.get(cid).addMobileDevice(mobiles.get(i));
		}
		double totalCloudOverhead=0;
		for(MobileDevice md:mobiles) {
			totalCloudOverhead+=getOverhead2(md);
		}
		System.out.println("Cloud:"+totalCloudOverhead);
		
		return iteration;
	}
	
	//not useful for now
	private static void sendPilotSignalToBase() {
		//init the rp
		for(int i=0;i<channelNum;i++) {
			rp.set(i, channels.get(i).getReceivedPower());
		}
		for(int i=0;i<mobileDeviceNum;i++) {
			MobileDevice md=mobiles.get(i);
			double gainPower=md.getTransPower()*md.getChannelGain();
			if(md.getUpdate()>=0) {
				double tmp=rp.get(md.getUpdate());
				rp.set(md.getUpdate(), tmp+gainPower);
				if(md.getChannelId()>=0)
					rp.set(md.getChannelId(), rp.get(md.getChannelId())-gainPower);
			}
		}
	}
	
	private static int receiveInfoFromBase(MobileDevice md) {
		double overhead=Double.MAX_VALUE;
		int update=-2;
		for(int i=-1;i<channelNum;i++) {
			md.setUpdate(i);
			double cmp=getOverhead(md);
			if(cmp<overhead) {
				overhead=cmp;
				update=i;
			}
		}
		return update==md.getChannelId()?-2:update;
	}
	
	//calculate the overhead for this iteration using update channel
	private static double getOverhead(MobileDevice md) {
		if(md.getUpdate()==-1) return overheadLocal(md);
		else if(md.getUpdate()>=0) return overheadCloud(md);
		else {
			return 0;
		}
	}
	
	//calculate the truly overhead for mobile device
	private static double getOverhead2(MobileDevice md) {
		if(md.getChannelId()==-1) return overheadLocal(md);
		else return overheadCloud2(md);
	}
	
	//calculate the overhead on local
	private static double overheadLocal(MobileDevice md) {
		return md.overheadLocal();
	}
	
	//calculate the overhead on cloud
	private static double overheadCloud(MobileDevice md) {
		double offTime=md.getTask().inputFileSize*1024*8/calculateDataRate(md);
		double execTime=md.getTask().length/(CLOCK_FREQUENCY_ON_CLOUD*1000);
		double energy=offTime*md.getTransPower();
		double overhead=md.getLambdaTime()*(offTime+execTime)+md.getLambdaEnergy()*energy;
		return overhead;
	}
	
	//calculate the data rate of the channel(bps)
	public static double calculateDataRate(MobileDevice md) {
		double dr=0;
		if(md.getUpdate()>=0) {
			double S=md.getTransPower()*md.getChannelGain();
			double N=channels.get(md.getUpdate()).getReceivedPower();
//			double N=rp.get(md.getUpdate());
			if(md.getChannelId()==md.getUpdate()) {
				N-=S;
			}
			if(Double.doubleToLongBits(0.0)==Double.doubleToLongBits(N)
					|| Double.doubleToLongBits(-0.0)==Double.doubleToLongBits(N) || N<1) {
				S*=100000000;
				N=1;
			}
			dr=channels.get(md.getUpdate()).getBandwidth()*1000000*Math.log(1+S/N)/Math.log((double)2);
		}
		return dr;
	}
	
	private static double overheadCloud2(MobileDevice md) {
		double offTime=md.getTask().inputFileSize*1024*8/calculateDataRate2(md);
		double execTime=md.getTask().length/(CLOCK_FREQUENCY_ON_CLOUD*1000);
		double energy=offTime*md.getTransPower();
		double overhead=md.getLambdaTime()*(offTime+execTime)+md.getLambdaEnergy()*energy;
		return overhead;
	}
	private static double calculateDataRate2(MobileDevice md) {
		double dr=0;
		if(md.getChannelId()>=0) {
			double S=md.getTransPower()*md.getChannelGain();
			double N=channels.get(md.getChannelId()).getReceivedPower();
//			double N=rp.get(md.getUpdate());
			N-=S;
			if(Double.doubleToLongBits(0.0)==Double.doubleToLongBits(N)
					|| Double.doubleToLongBits(-0.0)==Double.doubleToLongBits(N) || N<1) {
				S*=100000000;
				N=1;
			}
			dr=channels.get(md.getChannelId()).getBandwidth()*1000000*Math.log(1+S/N)/Math.log((double)2);
		}
		return dr;
	}
	public static int getBeneficialNum() {
		int count=0;
		for(MobileDevice md:mobiles) {
			md.overhead.add(getOverhead2(md));
			if(md.getChannelId()>=0 && md.overheadLocal()>overheadCloud2(md)) count++;
		}
		return count;
	}
}
