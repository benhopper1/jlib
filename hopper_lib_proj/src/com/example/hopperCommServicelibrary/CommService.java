package com.example.hopperCommServicelibrary;

import hopper.global.tools.GlobalMessage;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.hopperlibrary.HopperAsync;
import com.example.hopperlibrary.HopperCommunicationInterface;
import com.example.hopperlibrary.HopperJsonStatic;
import com.example.hopperlibrary.HopperLocalArfInfo;
import com.example.hopperlibrary.HopperLocalArfInfoStatic;
import com.example.hopperlibrary.HopperTimer;
import com.example.hopperlibrary.HopperUniversalClientSocket;
import com.example.hopperlibrary.console;

import android.app.Activity;
import android.os.SystemClock;
import android.util.Log;





public class CommService {
	private HopperUniversalClientSocket HUCS;	
	private HopperTimer ht;
	private int pulseInterval;
	
	private HopperTimer ht2;
	private int pulseInterval2;
	
	private HopperTimer mailNotificationTimer;
	private int mailNotificationTimerInterval;
	
	
	
	//--------callback-------------------------------------------------------------
	private ArrayList<MailNotificationListener> mailNotificationListenerArrayList = new ArrayList<MailNotificationListener>();
	
	public interface MailNotificationListener{
	    public void onNotification();
	}
    public void setMailNotificationListener(MailNotificationListener listener) {
    	mailNotificationListenerArrayList.add(listener);
    }
    private void reportNotification(){
    	for(MailNotificationListener listener : mailNotificationListenerArrayList){
    		listener.onNotification();
        }
    }
    //----------------------------------------------------------------------------
    
	
	static private HashMap<String, CommService> instanceHash = new HashMap<String, CommService>(); 
	
	static public CommService get(String inName){
		return instanceHash.get(inName);
	}
	static public CommService getmaybeCreate(String inName, String inHost, int inPort, int inPulseInterval, int inPulseInterval2){
		CommService tmpInstance = instanceHash.get(inName);
		if(tmpInstance == null){
			tmpInstance = new CommService(inHost, inPort, inPulseInterval, inPulseInterval2);
			instanceHash.put(inName, tmpInstance);
		}
		return tmpInstance;
	}
	static public CommService getmaybeCreate(String inName, String inHost, int inPort, int inPulseInterval){
		CommService tmpInstance = instanceHash.get(inName);
		if(tmpInstance == null){
			tmpInstance = new CommService(inHost, inPort, inPulseInterval);
			instanceHash.put(inName, tmpInstance);
		}
		return tmpInstance;
	}
	
	
	private CommService(String inHost, int inPort, int inPulseInterval){
		hopper.global.tools.GlobalMessage h;
		GlobalMessage.putObject("toFindUserDialog_execObject", this);
		console.log("COMM SERVICE STARTED");
		this.pulseInterval = inPulseInterval;
		this.HUCS = new HopperUniversalClientSocket(inHost, inPort);
		Log.v("socket", "CommService::constructor IP:"+inHost+"   port:"+inPort);
		
		if(CommService.this.pulseInterval > 0){
			//SystemClock.sleep(1000);
			this.pulse();
			this.ht = new HopperTimer(CommService.this.pulseInterval){			
				@Override
				public void onAlarm(){				
					CommService.this.pulse();
				}
			};
		}
	}
	
	private CommService(String inHost, int inPort, int inPulseInterval, int inPulseInterval2) {
		console.log("COMM SERVICE STARTED");
		this.pulseInterval = inPulseInterval;
		this.pulseInterval2 = inPulseInterval2;		
		this.HUCS = new HopperUniversalClientSocket(inHost, inPort);
		Log.v("socket", "CommService::constructor IP:"+inHost+"   port:"+inPort);
		
		if(CommService.this.pulseInterval > 0){
			//SystemClock.sleep(1000);
			this.pulse();		
			this.ht = new HopperTimer(CommService.this.pulseInterval){			
				@Override
				public void onAlarm(){				
					CommService.this.pulse();
				}
			};
		}
		if(CommService.this.pulseInterval2 > 0){			
			this.ht2 = new HopperTimer(CommService.this.pulseInterval2){			
				@Override
				public void onAlarm(){				
					CommService.this.deviceChangeBroadcast("pulse");
				}
			};
		}
	}	
	
	public void mailNotificationTimer_start(int inVal){		
		if(this.mailNotificationTimer == null){
			mailNotificationTimerInterval = inVal; //miliseconds		
			this.mailNotificationTimer = new HopperTimer(CommService.this.mailNotificationTimerInterval){			
				@Override
				public void onAlarm(){				
					getNotification();
				}
			};
		}
	}
	
	public void mailNotificationTimer_setInterval(int inVal){
		mailNotificationTimerInterval = inVal; //miliseconds		
	}
	
	public void mailNotificationTimer_stop(){
		mailNotificationTimer.stop(); 		
	}
	public void mailNotificationTimer_restart(int inVal){
		mailNotificationTimer.stop();
		mailNotificationTimer.restart(inVal);		
	}	
	
	public String test(String inString){
		this.HUCS.sendUnsecureHeader(1000, inString.length());
		this.HUCS.sendString_2(inString);
		this.HUCS.receiveUnsecureHeader();
		String retString = this.HUCS.receiveString();
		return retString;
	}
	
	
	/*	exec sends with a receive
	 * 	send, does not implement a receive
	 * 	receive waits for reception
	 */
	public String exec(String inString){
		this.HUCS.sendUnsecureHeader(1000, inString.length());
		this.HUCS.sendString_2(inString);
		this.HUCS.receiveUnsecureHeader();
		String retString = this.HUCS.receiveString();
		return retString;		
	}
	
	public String receive(){		
		this.HUCS.receiveUnsecureHeader();
		String retString = this.HUCS.receiveString();		
		return retString;		
	}
	
	public void send(String inString){
		this.HUCS.sendUnsecureHeader(1000, inString.length());
		this.HUCS.sendString_2(inString);
	}
	
	public void sendMessage(int inFromDeviceId, int inToDeviceId, String inStringData){
		JSONObject jobj_payload = new JSONObject();
		HopperJsonStatic.putKeyValueStringsForJsonObject(jobj_payload, "genericString", inStringData);
		
		JSONObject jobj = new JSONObject();
		HopperJsonStatic.putKeyValueStringsForJsonObject(jobj,"command","dev2dev_addMessage");
		HopperJsonStatic.putKeyValueStringsForJsonObject(jobj,"fromDevId",String.valueOf(inFromDeviceId));
		HopperJsonStatic.putKeyValueStringsForJsonObject(jobj,"toDevId",String.valueOf(inToDeviceId));
		HopperJsonStatic.putKeyValueStringObjectForJsonObject(jobj, "data", jobj_payload);
		this.send(jobj.toString());
		this.receive();//garbage return
	}
	
	public void sendMessage(int inFromDeviceId, int inToDeviceId, JSONObject inJson){
		JSONObject jobj = new JSONObject();
		HopperJsonStatic.putKeyValueStringsForJsonObject(jobj,"command","dev2dev_addMessage");
		HopperJsonStatic.putKeyValueStringsForJsonObject(jobj,"fromDevId",String.valueOf(inFromDeviceId));
		HopperJsonStatic.putKeyValueStringsForJsonObject(jobj,"toDevId",String.valueOf(inToDeviceId));		
		HopperJsonStatic.putKeyValueStringObjectForJsonObject(jobj,"data",inJson);
		this.send(jobj.toString());
		this.receive();//garbage return
	}
	
	public void sendMessage_broadcast(int inFromDeviceId, int inUserId, JSONObject inJson){
		console.log("sendMessage_broadcast");
		JSONObject jobj = new JSONObject();
		HopperJsonStatic.putKeyValueStringsForJsonObject(jobj,"command","broadcast_addMessage");
		HopperJsonStatic.putKeyValueStringsForJsonObject(jobj,"fromDevId",String.valueOf(inFromDeviceId));
		HopperJsonStatic.putKeyValueStringsForJsonObject(jobj,"userId",String.valueOf(inUserId));		
		HopperJsonStatic.putKeyValueStringObjectForJsonObject(jobj,"data",inJson);
		this.send(jobj.toString());
		this.receive();//garbage return
	}	
	
	
	
	public JSONArray receiveMessageAsJson(int inDeviceId){
		JSONObject jobj = new JSONObject();
		HopperJsonStatic.putKeyValueStringsForJsonObject(jobj,"command","getAllMsgboxMessages");
		HopperJsonStatic.putKeyValueStringsForJsonObject(jobj,"devId",String.valueOf(inDeviceId));
		this.send(jobj.toString());		
		String retString = this.receive();
		JSONObject jobj_return = HopperJsonStatic.getJsonObjectFromString(retString);		
		return HopperJsonStatic.getJSONArray(jobj_return,"message"); 
	}
	
	public String receiveMessage(int inDeviceId){		
		JSONObject jobj = new JSONObject();		
		HopperJsonStatic.putKeyValueStringsForJsonObject(jobj,"command","getAllMsgboxMessages");		
		HopperJsonStatic.putKeyValueStringsForJsonObject(jobj,"devId",String.valueOf(inDeviceId));		
		this.send(jobj.toString());		
		String retString = this.receive();		
		JSONObject jobj_return = HopperJsonStatic.getJsonObjectFromString(retString);		
		return HopperJsonStatic.getJSONArray(jobj_return,"message").toString();		
	}
	
	public ArrayList<HopperFilterSet> getArrayListOfMessages(JSONArray inJArray){
		ArrayList<HopperFilterSet> FS_arrayList = new ArrayList<HopperFilterSet>();
		for(int i = 0; i < inJArray.length();i++){
        	JSONObject jo = null;
			try {
				jo = inJArray.getJSONObject(i);
			} catch (JSONException e) {
				Log.e("commService","Error in getArrayListOfMessages");
				e.printStackTrace();
			}			
			JSONObject jobj_data = HopperJsonStatic.getJsonObjectFromKey(jo, "data");
			String[] keys = HopperJsonStatic.getKeysFromJsonObject(jobj_data);
			HopperFilterSet FS = new HopperFilterSet();
			for(int j = 0; j < keys.length; j++){				
				FS.add(keys[j], HopperJsonStatic.getStringFromKeyForJsonObject(jobj_data, keys[j]));				
			}
			FS.setJsonObject(jobj_data);
			FS_arrayList.add(FS);
        }
		return FS_arrayList;
	}
	
	public void pulse(){
		JSONObject jobj = new JSONObject();
		HopperJsonStatic.putKeyValueStringsForJsonObject(jobj, "command", "pulse");
		HopperJsonStatic.putKeyValueStringsForJsonObject(jobj, "deviceId", String.valueOf(HopperCommunicationInterface.get("COMM").deviceId));
		HopperJsonStatic.putKeyValueStringsForJsonObject(jobj, "userId", String.valueOf(HopperCommunicationInterface.get("COMM").userId));
		this.send(jobj.toString());
		String garbage = this.receive();		
	}
	
	public boolean getNotification(){		
		JSONObject jobj = new JSONObject();		
		HopperJsonStatic.putKeyValueStringsForJsonObject(jobj, "command", "getNotification");		
		HopperJsonStatic.putKeyValueStringsForJsonObject(jobj, "userId", HopperLocalArfInfoStatic.getField("userId"));		
		this.send(jobj.toString());		
		String returnedJson = this.receive();
		JSONObject jobj_notify = HopperJsonStatic.createJsonObjectFromJsonString(returnedJson);				
		if(HopperJsonStatic.getIntFromKeyForJsonObject(jobj_notify, "haveMail") == 1){
			reportNotification();
			console.log("haveMail: TRUE");
			return true;
		}else{
			console.log("haveMail: FALSE");
			return false;
		}
	}
	
	public void deviceChangeBroadcast(String inChangeTypeString){
		
		int userId = (int) HopperCommunicationInterface.get("COMM").userId;
		int deviceId = (int) HopperCommunicationInterface.get("COMM").deviceId ;       
             
		JSONObject jobj = new JSONObject();
		HopperJsonStatic.putKeyValueStringsForJsonObject(jobj, "command", "deviceChangeBroadcast");
		HopperJsonStatic.putKeyValueStringsForJsonObject(jobj, "changeType", inChangeTypeString );
		HopperJsonStatic.putKeyValueStringsForJsonObject(jobj, "fromDeviceId", String.valueOf(deviceId));
		sendMessage_broadcast(deviceId, userId, jobj);			
				
	}
	
	public void loginDeviceToCommService(){
		int userId = (int) HopperCommunicationInterface.get("COMM").userId;
		int deviceId = (int) HopperCommunicationInterface.get("COMM").deviceId ;
		String deviceString = HopperLocalArfInfoStatic.getField("deviceCaption");
		JSONObject jobj = new JSONObject();
		HopperJsonStatic.putKeyValueStringsForJsonObject(jobj, "command", "logInOut");
		HopperJsonStatic.putKeyValueStringsForJsonObject(jobj, "type", "login");
		HopperJsonStatic.putKeyValueStringsForJsonObject(jobj, "userId", String.valueOf(userId));
		HopperJsonStatic.putKeyValueStringsForJsonObject(jobj, "deviceId", String.valueOf(deviceId));
		HopperJsonStatic.putKeyValueStringsForJsonObject(jobj, "caption", deviceString);
		this.send(jobj.toString());
		console.log("login"+jobj.toString());
		this.receive();//garbage return
		
	}
	

}
