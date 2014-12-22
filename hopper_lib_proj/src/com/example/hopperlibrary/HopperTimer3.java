package com.example.hopperlibrary;

import java.util.Timer;
import java.util.TimerTask;

public class HopperTimer3 {
	private long interval;
	private Timer myTimer;
	private MyTimerTask myTask;
	public HopperTimer3(long inInterval) {
		interval = inInterval;
		myTask = new MyTimerTask();
        myTimer = new Timer(); 
        
//      Parameters
//      task  the task to schedule. 
//      delay  amount of time in milliseconds before first execution. 
//      period  amount of time in milliseconds between subsequent executions. 
        myTimer.schedule(myTask, 1, interval);        
 
	}
	public void stop(){myTimer.cancel(); myTask.cancel();}
	public void start(){myTimer.schedule(new MyTimerTask(), 1, interval);   }
	public void startDifferent(long inInterval){
		interval = inInterval;
		start();
	}
	
	protected void sniplet(){console.log("Please Override Snpilet to use---timer...");}
	
	
	
	class MyTimerTask extends TimerTask {
		  public void run() {			 	 
		    console.log("MyTimerTask--------------------------");
		    sniplet();
		  }
		}

}
