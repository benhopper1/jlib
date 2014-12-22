package com.example.hopperlibrary;

import java.util.Timer;
import java.util.TimerTask;

import android.util.Log;

public class HopperTimer_v2 {
	public Timer timer;
	private TimerTask task;
	public long interval;
	public HopperTimer_v2(String inName, long inInterval) {
		this.interval = inInterval;
		this.task = new TimerTask(){
			@Override
			public void run() {
				HopperTimer_v2.this.exec();
				
			}
			
		};
		this.timer = new Timer(inName, false);
		this.timer.scheduleAtFixedRate(this.task, 100L, inInterval); //TimerTask task, long delay, long period
	}
	public void exec(){
		Log.v("timer","exec method call, not (overloaded)implemented, OVERLOAD to use!!");
	}

}
