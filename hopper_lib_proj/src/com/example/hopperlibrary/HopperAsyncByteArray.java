package com.example.hopperlibrary;

import java.util.concurrent.ExecutionException;

import android.os.AsyncTask;
import android.util.Log;

public class HopperAsyncByteArray extends AsyncTask<byte[], Void, byte[]>  {
	public enum EthreadType {
	    WaitOnBackgroundThread,
	    Fork
	}
	public EthreadType threadType;
	public HopperAsyncByteArray(){
		this.threadType=EthreadType.Fork;
	}
	public HopperAsyncByteArray(EthreadType inEthreadType){
		this.threadType=inEthreadType;
	}
	
	@Override
	protected byte[] doInBackground(byte[]... arg0) {		
		//Log.v("MyActivity","doInBackground ENTERED");
		//Log.v("MyActivity","doInBackground ThreadId "+Thread.currentThread().getId());
		//Log.v("MyActivity","doInBackground EXITING");
		
		byte[] retString;
		retString=onNewThread(arg0[0]);
		return retString;
	}
	public byte[] run(byte[] inString){
		byte[] retString=null;
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
	public byte[] onNewThread(byte[] ... inString){
		return null;
	}
	
	
	

}
