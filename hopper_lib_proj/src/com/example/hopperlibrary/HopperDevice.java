package com.example.hopperlibrary;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Log;

public class HopperDevice {
	public static String eId;
	public static int deviceId;
	
	public static String getEId(Context inContext){	
		////TelephonyManager tm = (TelephonyManager) inContext.getSystemService(Context.TELEPHONY_SERVICE);
		////String device_id = tm.getDeviceId();
		String device_id = Secure.getString(inContext.getContentResolver(), Secure.ANDROID_ID);
		eId = device_id;
		return device_id;
	}
	
	@SuppressLint("NewApi")
	public static int getDeviceId(Context inContext, int inUserId){
		//check local
		//######################################################################
        //-----Local Info ---------------------------------------------------  
        HopperLocalArfInfo localInfo = new HopperLocalArfInfo((Activity)inContext);        
        String deviceIdString = localInfo.getField("deviceId");        
        Log.v("arfComm","local diviceId:"+deviceIdString);
        // does match db???
    	HopperDataset datasetA = new HopperDataset(HopperCommunicationInterface.get("COMM"));
    	datasetA.executeSql("CALL sp_getMaybeCreateDeviceId( "+String.valueOf(inUserId)+", '"+ getEId( inContext)+"');");
    	String deviceIdStringFromDb = datasetA.getFieldAsString("outId",0);
        
      //######################################################################
       
        
        if(deviceIdString.isEmpty() || deviceIdString.equals(deviceIdStringFromDb) == false){        	
        		//######################################################################
                //-----Local Info ---------------------------------------------------                       
                localInfo.putField("deviceId", deviceIdStringFromDb);                
                Log.v("arfComm","local WRITE diviceId:"+deviceIdStringFromDb);
                console.log("readOfTextFile:"+localInfo.getField("deviceId"));
                //######################################################################        		
        		return -1;// -1 signals reboot
        }        
        return Integer.parseInt(deviceIdString);
	}      	
	

	private HopperDevice(HopperCommunicationInterface inHCI){
		
	}
	
	
	
	
	

}
