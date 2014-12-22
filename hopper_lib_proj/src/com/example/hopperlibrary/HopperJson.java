package com.example.hopperlibrary;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class HopperJson {
	public String getStringFromKeyForJsonObject(JSONObject inObject,String inKey){
		String newString = null;
		try {
			newString = inObject.getString(inKey);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newString;
	}
	public void putKeyValueStringsForJsonObject(JSONObject inObject,String inKey,String inValue){
		try {
			inObject.put(inKey,inValue);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void putKeyValueStringObjectForJsonObject(JSONObject inObject,String inKey,JSONObject inValue){
		try {
			inObject.put(inKey,inValue);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	public JSONObject getJsonObjectFromString(String inString){
		JSONObject inJson = null;
		try {
			inJson= new JSONObject(inString);
		} catch (JSONException e) {
			Log.v("MyActivity","ERROR in  [JSONObject getJsonFromString(String inString)]");
			e.printStackTrace();
		}
		return inJson;
	}

}
