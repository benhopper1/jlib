package com.example.hopperlibrary;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import android.os.AsyncTask;
import android.util.Log;

public class HopperAsync extends AsyncTask<String, Void, String>  {
	public int progressValue;
	public enum EthreadType {
	    WaitOnBackgroundThread,
	    Fork
	}
	public EthreadType threadType;
	public HopperAsync(){
		this.threadType=threadType.Fork;
	}
	public HopperAsync(EthreadType inEthreadType){
		this.threadType=inEthreadType;
	}
	
	@Override
	protected void onProgressUpdate(Void... values) {
		console.log(progressValue);
		super.onProgressUpdate(values);
	}
	@Override
	protected String doInBackground(String... arg0) {		
		//Log.v("MyActivity","doInBackground ENTERED");
		//Log.v("MyActivity","doInBackground ThreadId "+Thread.currentThread().getId());
		//Log.v("MyActivity","doInBackground EXITING");
		
		String retString;
		retString=onNewThread(arg0[0]);
		return retString;
	}
	public String run(String inString){
		String retString=null;
		try {
			if(this.threadType==EthreadType.WaitOnBackgroundThread){
				retString =  this.execute(inString).get();
			}else{
				this.execute(inString);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return retString;
	}
	public String onNewThread(String ... inString){
		return "";
	}
	
	protected void onProgressUpdate(Integer[] progress) {
		// TODO Auto-generated method stub
		
	}
	
	
	

}
