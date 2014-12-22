package com.example.hopperlibrary;

import java.util.Timer;
import java.util.TimerTask;

import android.util.Log;

public class HopperTimerOnce {
	public Timer timer;
	private TimerTask task;
	public long interval;
	private boolean cancel = false;
	public void cancel(){
		this.cancel = true;
	}
	public HopperTimerOnce(String inName, long inInterval) {
		this.interval = inInterval;
		this.task = new TimerTask(){
			@Override
			public void run(){
				if(HopperTimerOnce.this.cancel == false){
					HopperTimerOnce.this.exec();
				}
				
			}
			
		};
		this.timer = new Timer(inName, false);
		this.timer.schedule(this.task, this.interval); //TimerTask task, long delay, long period
	}
	public void exec(){
		Log.v("timer","exec method call, not (overloaded)implemented, OVERLOAD to use!!");
	}
}
