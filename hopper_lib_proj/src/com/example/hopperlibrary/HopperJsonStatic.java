package com.example.hopperlibrary;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class HopperJsonStatic {
	public static String unescapeString(String inString){
		char backSlashChar =(char)92;
		String str = inString.replace(String.valueOf(backSlashChar), "");
		if(str.charAt(0)==(char)34){			
			str = str.substring(1);
		}
		if(str.charAt(str.length()-1)==(char)34){			
			str = str.substring(0,str.length()-1);
		}
		return str;
	}
	
	public static JSONObject createJsonObjectFromJsonString(String inJsonString){		
		JSONObject newJsonObj =null;
		try {
			newJsonObj = new JSONObject(inJsonString);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			console.log("JSON ERROR, HopperJsonStatic.createJsonObjectFromJsonString"+inJsonString);
		}
		return newJsonObj;		
	}
	
	public static JSONArray getJSONArray(JSONObject inObject,String inKey){
		JSONArray jSOnArray_return = null;
		try {
			jSOnArray_return= inObject.getJSONArray(inKey);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jSOnArray_return;
		
	}
	
	public static void putValueInJsonArray(JSONArray inJsonArray,String inValue){		
		//try {
			inJsonArray.put(inValue);
		//} catch (JSONException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
		//}
	}
	
	public static int getIntFromKeyForJsonObject(JSONObject inObject,String inKey){
		int retVal=0;
		try {
			retVal = inObject.getInt(inKey);
		} catch (JSONException e) {
			Log.v("MyActivity","ERROR in HopperJsonStatic.getIntFromKeyForJsonObject"+inKey);
			return 0;
			//e.printStackTrace();
		}
		return retVal;
	}
	public static String getStringFromKeyForJsonObject(JSONObject inObject,String inKey){
		String newString = null;
		try {
			newString = inObject.getString(inKey);
		} catch (JSONException e) {
			return "";
			//e.printStackTrace();
		}
		return newString;
	}
	public static void putKeyValueStringsForJsonObject(JSONObject inObject,String inKey,String inValue){
		try {
			inObject.put(inKey,inValue);
		} catch (JSONException e) {
			Log.v("JSON","putKeyValueStringsForJsonObject ERROR!!!!");
			e.printStackTrace();
		}
	}
	public static void putKeyValueStringObjectForJsonObject(JSONObject inObject,String inKey,JSONObject inValue){
		try {
			inObject.put(inKey,inValue);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	public static JSONObject getJsonObjectFromString(String inString){
		inString =unescapeString(inString);
		JSONObject inJson = null;
		try {
			inJson= new JSONObject(inString);
		} catch (JSONException e) {
			Log.v("MyActivity","ERROR in  [JSONObject getJsonFromString(String inString)]:"+inString);
			e.printStackTrace();
		}
		return inJson;
	}
	
	public static JSONObject getJsonObjectFromJsonArrayByIndex(JSONArray inJsonArray,int inIndex){
		JSONObject jSon_item = null;
		try {
			jSon_item = inJsonArray.getJSONObject(inIndex);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jSon_item;
		
	}
	public static JSONObject getJsonObjectFromKey(JSONObject inObject, String inKey){		
		JSONObject retObject = null;
		try {
			retObject = inObject.getJSONObject(inKey);
		} catch (JSONException e) {
			Log.v("MyActivity","ERROR in  [getJsonObjectFromKey]"+inKey);
			e.printStackTrace();
			//return createJsonObjectFromJsonString("{\"" + inKey + "\":{}}");
		}
		return retObject;
	}
	
	public static String[] getKeysFromJsonObject(JSONObject inObject){
		int keyCount = inObject.length();
		String[] retString = new String[keyCount];
		Iterator<?> keys = inObject.keys();
		for(int i = 0; i < keyCount; i++){ 
			retString[i] = (String)keys.next();
		}
		return retString;
	}
	
	public static void putObjectIntoArray(JSONArray inArray, JSONObject inObject){
		inArray.put(inObject);
	}
	
	public static void putArrayIntoObjectWithKey(JSONObject inObject, JSONArray inArray, String inKey){
		try {
			inObject.put(inKey,inArray);
		} catch (JSONException e) {
			Log.e("JSON","putArrayIntoObjectWithKey ERROR!!!!");
			e.printStackTrace();
		}
	}
	

}
