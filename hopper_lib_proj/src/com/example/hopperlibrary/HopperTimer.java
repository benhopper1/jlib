package com.example.hopperlibrary;

import android.os.Handler;
import android.util.Log;

public class HopperTimer {
	public Long counter;	
	
	private int stop = 0;
	public int interval;
	public final Handler customHandler = new Handler();	
	public HopperTimer(int inInterval){
		interval=inInterval;
		customHandler.postDelayed(updateTimerThread, this.interval);
		Log.v("MyActivity","Timer CREATED");
		
	}
	public void changeInterval(int inValue){
		this.interval = inValue;
	}
	
	public void onAlarm(){
		
	}
	public void stop(){
		stop = 1;
	}
	public void restart(){
		if(stop == 1){
			stop = 0;
			customHandler.postDelayed(updateTimerThread, this.interval);
		}
	}
	public void restart(int inNewInterval){
		if(stop == 1){
			stop = 0;
			interval=inNewInterval;
			console.log("TIMER-timeer",interval);
			customHandler.postDelayed(updateTimerThread, this.interval);
		}
	}
	
	
	private Runnable updateTimerThread = new Runnable() {		
		public void run(){
			if(stop == 0){
				onAlarm();			
				customHandler.postDelayed(this, interval);
			}
		}			 
	};
}
