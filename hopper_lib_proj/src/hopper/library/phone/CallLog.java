package hopper.library.phone;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONObject;

import com.example.hopperlibrary.HopperJsonStatic;
import com.example.hopperlibrary.console;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;

public class CallLog {
	private Context context;	
	public CallLog(Context inContext){
		context = inContext;
	}
	
	public ArrayList<JSONObject> getCallDetailsAsArrayListOfjson(){
		return getCallDetailsAsArrayListOfjson(0);
	}
	public ArrayList<JSONObject> getCallDetailsAsArrayListOfjson(int inLastId){		
		ArrayList<JSONObject> result_arrayList_json = new ArrayList<JSONObject>();
		Cursor managedCursor = ((Activity)context).managedQuery(android.provider.CallLog.Calls.CONTENT_URI, null, null, null, null);
		int number = managedCursor.getColumnIndex(android.provider.CallLog.Calls.NUMBER);
		int type = managedCursor.getColumnIndex(android.provider.CallLog.Calls.TYPE);
		int date = managedCursor.getColumnIndex(android.provider.CallLog.Calls.DATE);
		int duration = managedCursor.getColumnIndex(android.provider.CallLog.Calls.DURATION);
		int id_id = managedCursor.getColumnIndex(android.provider.CallLog.Calls._ID); 
		
		while (managedCursor.moveToNext()){
			String id = managedCursor.getString(id_id);
			console.log("id" + id);
			if(Integer.parseInt(id) > inLastId){
				JSONObject theJson_json = new JSONObject();
				String phoneNumber = managedCursor.getString(number); 
				String callType = managedCursor.getString(type); 
				String callDate = managedCursor.getString(date); 
				Date callDayTime = new Date(Long.valueOf(callDate)); 
				String callDuration = managedCursor.getString(duration); 
				String dir = null;
				
				int dircode = Integer.parseInt(callType); 
				switch (dircode) { 
					case android.provider.CallLog.Calls.OUTGOING_TYPE: 
						dir = "OUTGOING";
						break; 
					case android.provider.CallLog.Calls.INCOMING_TYPE: 
						dir = "INCOMING"; 
						break; 
					case android.provider.CallLog.Calls.MISSED_TYPE: 
						dir = "MISSED"; 
						break; 
				}
				
				HopperJsonStatic.putKeyValueStringsForJsonObject(theJson_json, "phoneNumber", phoneNumber);
				HopperJsonStatic.putKeyValueStringsForJsonObject(theJson_json, "callType", callType);
				HopperJsonStatic.putKeyValueStringsForJsonObject(theJson_json, "callDate", callDate);
				HopperJsonStatic.putKeyValueStringsForJsonObject(theJson_json, "callDayTime", callDayTime.toString());
				HopperJsonStatic.putKeyValueStringsForJsonObject(theJson_json, "callDuration", callDuration);
				HopperJsonStatic.putKeyValueStringsForJsonObject(theJson_json, "id", id);
				HopperJsonStatic.putKeyValueStringsForJsonObject(theJson_json, "status", dir);
				
				result_arrayList_json.add(theJson_json);
			}
						
			
		}
		//managedCursor.close();
		return result_arrayList_json;
	}
	
	public void dump(){
		ArrayList<JSONObject> result_arrayList_json = getCallDetailsAsArrayListOfjson();
		console.line("CallLog DUMP");
		console.log(result_arrayList_json.toString());
	}


	
}

