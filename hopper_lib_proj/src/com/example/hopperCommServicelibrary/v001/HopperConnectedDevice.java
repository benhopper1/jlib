package com.example.hopperCommServicelibrary.v001;

public class HopperConnectedDevice {
	public static HopperConnectedDevice instance;
	public String thisDeviceId;
	public String connectedDeviceId;
	public long lastTransmit;
	
	
	private HopperConnectedDevice() {
		
	}
	
	public static HopperConnectedDevice getInstance(){
		if(instance == null){
			HopperConnectedDevice.instance = new HopperConnectedDevice();			
		}
		return HopperConnectedDevice.instance;		
	}
	
	public long getElapsed(){
		return System.currentTimeMillis() - this.lastTransmit;		
	}
	
	public void pulseMe(){
		this.lastTransmit = System.currentTimeMillis();
	}
}
