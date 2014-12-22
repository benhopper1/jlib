package com.example.hopperCommServicelibrary;

import java.util.ArrayList;
import java.util.TimeZone;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


//import hopper.arfcomm.rqdisplay.RemoteRecording_activity;

import com.example.hopperlibrary.HopperActivity;
import com.example.hopperlibrary.HopperAsync;
import com.example.hopperlibrary.HopperAudioMachine;
import com.example.hopperlibrary.HopperCommunicationInterface;
import com.example.hopperlibrary.HopperDataset;
import com.example.hopperlibrary.HopperInstanceHash;
import com.example.hopperlibrary.HopperJsonStatic;
import com.example.hopperlibrary.HopperLocalArfInfo;
import com.example.hopperlibrary.HopperLocalArfInfoStatic;
import com.example.hopperlibrary.HopperTimer;
import com.example.hopperlibrary.StaticToast;
import com.example.hopperlibrary.console;

public class HopperProcessMessages {	
	public CommService commService;
	public HopperCommunicationInterface hopperCommunicationInterface;
	public int deviceId;
	public int messageCheckInterval;
	public ArrayList<HopperFilterSet> localFilterSets;
	public HopperTimer messageCheck_Timer;
	public static Context context;
	public static TextView statusTextView;
	public static TextView captionTextView;
	public static HopperProcessMessages staticInstance;
	
	public HopperProcessMessages(Context inContext, CommService inCommService, HopperCommunicationInterface inHopperCommunicationInterface, int inMessageCheckInterval) {		
		HopperProcessMessages.staticInstance = this;
		HopperProcessMessages.context = inContext;
		this.localFilterSets = new ArrayList<HopperFilterSet>();
		this.commService = inCommService;
		this.hopperCommunicationInterface = inHopperCommunicationInterface;
		this.deviceId = this.hopperCommunicationInterface.deviceId;
		this.messageCheckInterval = inMessageCheckInterval;
		this.createLocalFilterSets();
		this.messageCheck_Timer = new HopperTimer(this.messageCheckInterval){			
			@Override
			public void onAlarm(){
				Log.v("Timer HopperProcessMessages","Started");
				HopperProcessMessages.this.processMessages();
				Log.v("Timer","Done");
			}
		};
	}
	public static HopperProcessMessages getInstance(){return staticInstance; }
	public int getMessageCheckInterval(){return messageCheckInterval;}
	public void stop(){
		this.messageCheck_Timer.stop();
	}
	public void restart(){
		this.messageCheck_Timer.stop();
		this.messageCheck_Timer.restart();
	}
	
	public void restart(int newInterval){
		this.messageCheck_Timer.stop();
		this.messageCheck_Timer.restart(newInterval);
		console.log("RESTART HopperProcessMessages INTERVAL",newInterval);
	}
	
	public String getMessages(){
		Log.v("HopperProcessMessages","getMessages ENTERED");
		return this.commService.receiveMessage(this.deviceId);
	}
	
	public void processMessages(){
		Log.v("HopperProcessMessages","processMessages ENTERED");
		Log.v("filter","processMessages ENTERED");
		ArrayList<HopperFilterSet> remoteFilterSets = this.commService.getArrayListOfMessages(this.commService.receiveMessageAsJson(this.deviceId));
		Log.v("filter","length of remoteFilterSets"+remoteFilterSets.size());
		for(HopperFilterSet remoteFilterSetItem : remoteFilterSets){
			Log.v("filter", "remote filiter Id(foreach)"+remoteFilterSetItem.getId());
			Log.v("filter", "size of localFilterset:"+this.localFilterSets.size());
			for(HopperFilterSet localFilterSetItem : this.localFilterSets){
				
				//Log.v("filter", "local"+this.loca thislFilterSets.size());
				localFilterSetItem.exec(remoteFilterSetItem);
			}
		}
	}
	
	private void createLocalFilterSets(){
		Log.v("filter","createLocalFilterSets ENTERED");
		
	/* setSyncRate
	 * ==========================================================================================================
	 */
		HopperFilterSet hFS_setSyncRateFilterSet = new HopperFilterSet(){
			@Override
			public void filterPassCallback(int inId,HopperFilterSet inReference,HopperFilterSet inRemoteFilterSet, String inKey, String inValue) {
				Log.v("filter","filterPassCallback:"+inId+inKey+inValue);
				int oldRate = HopperProcessMessages.this.messageCheckInterval;
				int newRate = Integer.parseInt(inRemoteFilterSet.getValueFromKey("rate"));
				HopperProcessMessages.this.messageCheck_Timer.changeInterval(newRate);
				
				JSONObject json_out = new JSONObject(); // = null;
				HopperJsonStatic.putKeyValueStringsForJsonObject(json_out, "CommReply", "changedSyncRate");
				HopperJsonStatic.putKeyValueStringsForJsonObject(json_out, "rateFrom", String.valueOf(oldRate));
				HopperJsonStatic.putKeyValueStringsForJsonObject(json_out, "rateTo", String.valueOf(newRate));			
				
				StaticToast.message(context,"setSyncRate (new):"+String.valueOf(newRate));
				HopperProcessMessages.this.commService.sendMessage(HopperProcessMessages.this.deviceId, Integer.parseInt(inRemoteFilterSet.getValueFromKey("fromDeviceId")), json_out);
				
				
			}
			
		};
		Log.v("filter","createLocalFilterSets adding filter");
		hFS_setSyncRateFilterSet.add("commMessage", "setSyncRate");
		Log.v("filter","createLocalFilterSets adding filterSet");
		this.localFilterSets.add(hFS_setSyncRateFilterSet);
		
	/* gotoRecordMode
	 * ==========================================================================================================
	 */
		HopperFilterSet hFS_gotoRecordMode = new HopperFilterSet(){
			@Override
			public void filterPassCallback(int inId,HopperFilterSet inReference,HopperFilterSet inRemoteFilterSet, String inKey, String inValue) {
				Log.v("filter","(hFS_gotoRecordMode)filterPassCallback:"+inId+inKey+inValue);
				Context mContext = null;
				Class mClass = null;
				try {
					mClass = Class.forName("com.example.hopperCommServicelibrary.RemoteRecording_activity");	//com.example.audiosenseui_2.act_remoteRecorder				
				} catch (ClassNotFoundException e) {					
					e.printStackTrace();
				}
				
				if(HopperInstanceHash.getInstance("RemoteRecording_activity") == null){
					console.log("FROM INTENT START ACTIVITY THINGY");					
					HopperActivity.loadActivity(HopperProcessMessages.context, mClass, "just testing the extra!!");
					//############################################################################
					//###Setup audio machine #############################################################
					if(((RemoteRecording_activity)HopperInstanceHash.getInstance("RemoteRecording_activity")) != null){((RemoteRecording_activity)HopperInstanceHash.getInstance("RemoteRecording_activity")).setState(1);}
					HopperAudioMachine HAM = new HopperAudioMachine("remotePlayer"){

						@Override
						public void onRecordStart() {
							console.log("ProcessMessage-- onRecordStart");
							if(((RemoteRecording_activity)HopperInstanceHash.getInstance("RemoteRecording_activity")) != null){((RemoteRecording_activity)HopperInstanceHash.getInstance("RemoteRecording_activity")).setState(3);}
						}

						@Override
						public void onAnalizeStart() {
							console.log("ProcessMessage-- onAnalizeStart");
							if(((RemoteRecording_activity)HopperInstanceHash.getInstance("RemoteRecording_activity")) != null){((RemoteRecording_activity)HopperInstanceHash.getInstance("RemoteRecording_activity")).setState(4);}
						}

						@Override
						public void onRecordEnd() {
							console.log("ProcessMessage-- onRecordEnd");
							if(((RemoteRecording_activity)HopperInstanceHash.getInstance("RemoteRecording_activity")) != null){((RemoteRecording_activity)HopperInstanceHash.getInstance("RemoteRecording_activity")).setState(4);}
							
							
							
						}

						@Override
						public void onAnalizeEnd() {
							console.log("ProcessMessage-- onAnalizeEnd");
							if(((RemoteRecording_activity)HopperInstanceHash.getInstance("RemoteRecording_activity")) != null){((RemoteRecording_activity)HopperInstanceHash.getInstance("RemoteRecording_activity")).setState(5);}
						}
						
						
						
					};
					
					
				}
				
				Activity HIH =   HopperInstanceHash.getInstance("RemoteRecording_activity");
				//if(HIH != null){((RemoteRecording_activity) HIH).setState(1);console.log("state--1");}
				
				String useId = inRemoteFilterSet.getValueFromKey("UID");				
				
				JSONObject json_out = new JSONObject(); 
				HopperJsonStatic.putKeyValueStringsForJsonObject(json_out, "commReply", "recordModeReady");
				HopperJsonStatic.putKeyValueStringsForJsonObject(json_out, "UID", useId);
				HopperProcessMessages.this.commService.sendMessage(HopperProcessMessages.this.deviceId, Integer.parseInt(inRemoteFilterSet.getValueFromKey("fromDeviceId")), json_out);
				
				
			}
			
		};
		Log.v("filter","createLocalFilterSets adding filter");
		hFS_gotoRecordMode.add("commMessage", "gotoRecordMode");
		Log.v("filter","createLocalFilterSets adding filterSet");
		this.localFilterSets.add(hFS_gotoRecordMode);	
		

		
	/* startRecording
	 * ==========================================================================================================
	 */
		HopperFilterSet hFS_startRecording = new HopperFilterSet(){
			@Override
			public void filterPassCallback(int inId,HopperFilterSet inReference,HopperFilterSet inRemoteFilterSet, String inKey, String inValue) {
				Log.v("filter","(hFS_startRecording)filterPassCallback:"+inId+inKey+inValue);				
				//Log.v("filter","OLD TEXT:"+HopperProcessMessages.statusTextView.getText().toString());				
				console.log("hFS_startRecording");
				
				final HopperAudioMachine HAM = HopperAudioMachine.getMaybeCreate("remotePlayer");
				HAM.clear();
				console.log("HAM created");							
				Activity HIH =   HopperInstanceHash.getInstance("RemoteRecording_activity");
				console.log("HIH attept at instance");
				if(HIH != null){((RemoteRecording_activity) HIH).setState(1);}
		    	HAM.record();
				console.log("HAM record   .record()");
				
				//#####################################____________________________________________________________
				int new_arf_buff_Id= HopperProcessMessages.this.hopperCommunicationInterface.storeNewByteArray(1, HAM.getBufferWithHeader(), "orphan","put caption here", "tmp");
				console.log("store byte array done arfBuffId:"+new_arf_buff_Id);
				
				HopperDataset dataSet = new HopperDataset(hopperCommunicationInterface);
				dataSet.executeSql("select filePath, buff_id from vw_basicArfData where arf_buffId = "+String.valueOf(new_arf_buff_Id));
				String fileServerPath = dataSet.getFieldAsString("filePath", 0);
				String buffIdString = dataSet.getFieldAsString("buff_id", 0);
				
				console.log("filePath:"+dataSet.getFieldAsString("filePath", 0));
				console.log("buff_id:"+dataSet.getFieldAsString("buff_id", 0));
				
				
				String useId = inRemoteFilterSet.getValueFromKey("UID");				
				console.log("retrieved data from sql");
				
				JSONObject json_out = new JSONObject(); 
				HopperJsonStatic.putKeyValueStringsForJsonObject(json_out, "commReply", "recordingDone");
				HopperJsonStatic.putKeyValueStringsForJsonObject(json_out, "UID", useId);
				HopperJsonStatic.putKeyValueStringsForJsonObject(json_out, "ArfBuffId", String.valueOf(new_arf_buff_Id));
				HopperJsonStatic.putKeyValueStringsForJsonObject(json_out, "buffId", buffIdString);
				HopperJsonStatic.putKeyValueStringsForJsonObject(json_out, "filePath", fileServerPath);				
				console.log("json built for out");
				
				HopperProcessMessages.this.commService.sendMessage(HopperProcessMessages.this.deviceId, Integer.parseInt(inRemoteFilterSet.getValueFromKey("fromDeviceId")), json_out);
				//HopperProcessMessages.statusTextView.setText("Recording ready for review!!!!");
				console.log("recording process done");
				//#####################################____________________________________________________________
			}
			
		};
		Log.v("filter","createLocalFilterSets adding filter");
		hFS_startRecording.add("commMessage", "recordStart");
		Log.v("filter","createLocalFilterSets adding filterSet");
		this.localFilterSets.add(hFS_startRecording);	
		
		
		
	//----testing area-----/----testing area-----/----testing area-----/----testing area-----/----testing area-----/----testing area-----
	//	HopperFilterSet hFS_startRecording2 = new HopperFilterSet();
	//	console.log("TESTofFilters----");
	//	hFS_startRecording.add("commMessage", "recordStart");
	//	this.localFilterSets.add(hFS_startRecording2);	
		
		
		

	//==============================================================

		
		
		
		
	
	/* hFS_captionChange
	 * ==========================================================================================================
	 */
		HopperFilterSet hFS_captionChange = new HopperFilterSet(){
			@Override
			public void filterPassCallback(int inId,HopperFilterSet inReference,HopperFilterSet inRemoteFilterSet, String inKey, String inValue) {
				Log.v("filter","filterPassCallback:"+inId+inKey+inValue);
				
				String newCaption = inRemoteFilterSet.getValueFromKey("newCaption");				
				//HopperProcessMessages.captionTextView.setText(newCaption);
				Activity remoteRecord = (Activity) HopperInstanceHash.getInstance("RemoteRecording_activity");
				if(remoteRecord != null){
					((RemoteRecording_activity) remoteRecord).setCaption(newCaption);
				}
				
				
				
				
				JSONObject json_out = new JSONObject(); // = null;
				HopperJsonStatic.putKeyValueStringsForJsonObject(json_out, "CommReply", "captionUpdated");				
				HopperProcessMessages.this.commService.sendMessage(HopperProcessMessages.this.deviceId, Integer.parseInt(inRemoteFilterSet.getValueFromKey("fromDeviceId")), json_out);
			}
			
		};
		Log.v("filter","createLocalFilterSets adding filter");
		hFS_captionChange.add("commMessage", "captionChange");
		Log.v("filter","createLocalFilterSets adding filterSet");
		this.localFilterSets.add(hFS_captionChange);

		
	
	/* hFS_closeRecordMode
	 * ==========================================================================================================
	 */
		HopperFilterSet hFS_closeRecordMode = new HopperFilterSet(){
			@Override
			public void filterPassCallback(int inId,HopperFilterSet inReference,HopperFilterSet inRemoteFilterSet, String inKey, String inValue) {
				Log.v("filter","(hFS_closeRecordMode)filterPassCallback:"+inId+inKey+inValue);
				Context mContext = null;
				Class mClass = null;
				try {
					mClass = Class.forName("com.example.audiosenseui_2.act_remoteRecorder");					
				} catch (ClassNotFoundException e) {					
					e.printStackTrace();
				}
				
				// call the close of ---- here--
				//mClass.closeNow();
				//HopperActivity.loadActivity(HopperProcessMessages.context, mClass, "just testing the extra!!");
				//String useId = inRemoteFilterSet.getValueFromKey("UID");				
				Activity remoteRecord = (Activity) HopperInstanceHash.getInstance("RemoteRecording_activity");
				if(remoteRecord != null){
					Log.v("filter","(hFS_closeRecordMode)filterPassCallback: getInstance == NULL ");
					remoteRecord.finish();
				}
				//remoteRecord.finish();
				JSONObject json_out = new JSONObject(); 
				HopperJsonStatic.putKeyValueStringsForJsonObject(json_out, "commReply", "recordModeClosed");
				//HopperJsonStatic.putKeyValueStringsForJsonObject(json_out, "UID", useId);
				HopperProcessMessages.this.commService.sendMessage(HopperProcessMessages.this.deviceId, Integer.parseInt(inRemoteFilterSet.getValueFromKey("fromDeviceId")), json_out);
				
				
			}
			
		};
		Log.v("filter","createLocalFilterSets adding filter");
		hFS_closeRecordMode.add("commMessage", "closeRecordMode");
		Log.v("filter","createLocalFilterSets adding filterSet");
		this.localFilterSets.add(hFS_closeRecordMode);	

		
		
		
		/* hFS_aliveStatusRequest
		 * ==========================================================================================================
		 */
			HopperFilterSet hFS_aliveStatusRequest = new HopperFilterSet(){
				@Override
				public void filterPassCallback(int inId,HopperFilterSet inReference,HopperFilterSet inRemoteFilterSet, String inKey, String inValue) {
					Log.v("filter","filterPassCallback:"+inId+inKey+inValue);
					
					long time = System.currentTimeMillis();					
					String timezone = java.util.TimeZone.getDefault().getID();
					String r_requestId = inRemoteFilterSet.getValueFromKey("requestId");
					String activeSection = "Not Implemented Yet!!";
					String r_senderDeviceId =  inRemoteFilterSet.getValueFromKey("senderDeviceId");
					String deviceId = String.valueOf(HopperCommunicationInterface.get("COMM").deviceId);
					
					//######################################################################
			        //-----Local Info ---------------------------------------------------  			        
					String deviceCaption = HopperLocalArfInfoStatic.getField("deviceCaption");			
					
					
					
					JSONObject json_out = new JSONObject(); // = null;
					HopperJsonStatic.putKeyValueStringsForJsonObject(json_out, "commReply", "aliveStatus");
					HopperJsonStatic.putKeyValueStringsForJsonObject(json_out, "type", "ak");
					
					HopperJsonStatic.putKeyValueStringsForJsonObject(json_out, "time", String.valueOf(time));
					HopperJsonStatic.putKeyValueStringsForJsonObject(json_out, "timeZone", timezone);
					HopperJsonStatic.putKeyValueStringsForJsonObject(json_out, "r_requestId", r_requestId);
					HopperJsonStatic.putKeyValueStringsForJsonObject(json_out, "activeSection", activeSection);
					HopperJsonStatic.putKeyValueStringsForJsonObject(json_out, "r_senderDeviceId", r_senderDeviceId);
					HopperJsonStatic.putKeyValueStringsForJsonObject(json_out, "deviceId", deviceId);
					HopperJsonStatic.putKeyValueStringsForJsonObject(json_out, "caption", deviceCaption);
					
					HopperProcessMessages.this.commService.sendMessage(HopperProcessMessages.this.deviceId, Integer.parseInt(r_senderDeviceId), json_out);
					
					console.log("CommReply:"+"aliveStatus");
					console.log("time:"+String.valueOf(time));
					console.log("timeZone:"+timezone);
					console.log("r_requestId:"+r_requestId);
					console.log("activeSection:"+activeSection);
					console.log("r_senderDeviceId:"+r_senderDeviceId);
					console.log("deviecId:"+deviceId);
					console.log("deviceCaption:"+deviceCaption);
					
					
					
				}
				
			};
			Log.v("filter","createLocalFilterSets adding filter");
			hFS_aliveStatusRequest.add("commMessage", "requestAliveStatus");
			Log.v("filter","createLocalFilterSets adding filterSet");
			this.localFilterSets.add(hFS_aliveStatusRequest);




	
	
	
	
		Log.v("filter","createLocalFilterSets DONE");	
		
	}
	
	public void addLocalFilterSet(HopperFilterSet inFilterSet){
		this.localFilterSets.add(inFilterSet);		
	}
	
	public void removeLocalFilterSet(HopperFilterSet inFilterSet){
		this.localFilterSets.remove(inFilterSet);		
	}
	
	

}
