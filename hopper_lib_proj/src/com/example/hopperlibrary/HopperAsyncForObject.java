//package com.example.hopperlibrary;

//public class HopperAsyncForObject {

//	public HopperAsyncForObject() {
//		// TODO Auto-generated constructor stub
//	}

//}



package com.example.hopperlibrary;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import android.os.AsyncTask;
import android.util.Log;

public class HopperAsyncForObject extends AsyncTask<String, Void, Object>  {
	public enum EthreadType {
	    WaitOnBackgroundThread,
	    Fork
	}
	public EthreadType threadType;
	public HopperAsyncForObject(){
		this.threadType=threadType.Fork;
	}
	public HopperAsyncForObject(EthreadType inEthreadType){
		this.threadType=inEthreadType;
	}
	
	@Override
	protected Object doInBackground(String... arg0) {		
		//Log.v("MyActivity","doInBackground ENTERED");
		//Log.v("MyActivity","doInBackground ThreadId "+Thread.currentThread().getId());
		//Log.v("MyActivity","doInBackground EXITING");
		
		Object retString;
		retString=onNewThread(arg0[0]);
		return retString;
	}
	public Object run(String inString){
		Object retString=null;
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
	public Object onNewThread(String ... inString){
		return null;
	}
	
	
	

}

