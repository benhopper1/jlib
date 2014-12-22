package com.example.hopperlibrary;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.widget.Switch;

public class HopperAudioMachine {
	private static Map<String,HopperAudioMachine > HopperAudioMachineMap;
	static{  HopperAudioMachineMap = new HashMap<String,HopperAudioMachine >();}
	
	private HopperAudioBytes hAudio;
	private byte[] currentBuffer;
	private String name;
	
	//--------callback-------------------------------------------------------------
		private ArrayList<OnCompleteListener> onCompleteListenerArrayList = new ArrayList<OnCompleteListener>();
		
		public interface OnCompleteListener{
		    public void onComplete(Object...objects);
		}
	    public void setOnCompleteListener(OnCompleteListener listener) {
	    	onCompleteListenerArrayList.add(listener);
	    }
	    private void reportOnComplete(Object...objects){
	    	for(OnCompleteListener listener : onCompleteListenerArrayList){
	    		listener.onComplete(objects);
	        }
	    }
	//-
	
	
	
	
	public HopperAudioMachine(final String inName){
		HopperAudioMachineMap.put(inName, this);
		name = inName;
		this.hAudio = new HopperAudioBytes(){
			
			@Override
			public void onRecordStart() {
				HopperAudioMachineMap.get(inName).onRecordStart();					
			}

			@Override
			public void onRecordEnd() {
				HopperAudioMachineMap.get(inName).onRecordEnd();
				//HopperAudioMachineMap.get(inName).reportOnComplete(HopperAudioMachineMap.get(inName));
				
			}

			@Override
			public void onAnalizeStart() {
				HopperAudioMachineMap.get(inName).onAnalizeStart();					
			}

			@Override
			public void onAnalizeEnd() {
				HopperAudioMachineMap.get(inName).onAnalizeEnd();
				
			}
			
		};
	}
	
	public static HopperAudioMachine getMaybeCreate(final String inName){
		HopperAudioMachine HAM=null;
		HAM = HopperAudioMachineMap.get(inName);
		if(HAM==null){
			Log.v("MyActivity", "HopperAudioMachineMap instance being created");
			HAM = new HopperAudioMachine(inName);
			HopperAudioMachineMap.put(inName, HAM);
			HAM.name = inName;
			//final HopperAudioMachine xHAM = HAM;
			HAM.hAudio = new HopperAudioBytes(){
				
				@Override
				public void onRecordStart() {
					HopperAudioMachineMap.get(inName).onRecordStart();					
				}

				@Override
				public void onRecordEnd() {
					//HopperAudioMachineMap.get(inName).reportOnComplete(HopperAudioMachineMap.get(inName));
					HopperAudioMachineMap.get(inName).onRecordEnd();
					
				}

				@Override
				public void onAnalizeStart() {
					HopperAudioMachineMap.get(inName).onAnalizeStart();					
				}

				@Override
				public void onAnalizeEnd() {
					HopperAudioMachineMap.get(inName).onAnalizeEnd();
					
				}
				
			};
			
			Log.v("MyActivity", "HopperAudioMachineMap instance created DONE");
		}
		return HAM;		
	}	

	private HopperAudioMachine(){
		
		//HopperAudioMachineMap.put(inString, this);
	}
	
	public void loadIntoBuffer(byte[] inByteArray){
		this.currentBuffer=inByteArray;
	}
	public void play(){
		if(this.currentBuffer==null){return;}
		if(this.currentBuffer.length>0){
			hAudio.playBuffer(this.currentBuffer);
		}
	}	
	public void record(){		
		byte[] unused;	    
	    byte[] mbytes=null;	
	    byte[] soundBuffer;
		soundBuffer = hAudio.getBuffer_waitForSound_recordUntilSilence(400f,20000,600f,2000);	   
	    Log.v("MyActivity", "ANALYZING--------------------------------------------------------------------------------");
	    unused=hAudio.trimBufferEnd(soundBuffer,600f);
	    Log.v("MyActivity", "ANALYZING-DONE@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
	    mbytes=hAudio.mergeArraysOfByte(mbytes,unused);
	    currentBuffer=mbytes;

	}
	
	public void clear(){
		currentBuffer=null;
	}
	
	public byte[]getBuffer(){
		return currentBuffer;
	}
	
	public byte[] getBufferWithHeader(){
		return hAudio.getBufferWithHeader(this.currentBuffer, 44100,16);
	}
	
	public void onRecordStart(){console.log("HOPPERaudioMachin: onRecordStart ");}
	public void onAnalizeStart(){console.log("HOPPERaudioMachin: onAnalizeStart ");}
	public void onRecordEnd(){console.log("HOPPERaudioMachin: onRecordEnd "); this.reportOnComplete(this, this.name);}
	public void onAnalizeEnd(){console.log("HOPPERaudioMachin: onAnalizeEnd ");}
	

}
