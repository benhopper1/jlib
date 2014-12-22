package com.example.hopperlibrary;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;

public class HopperLocalArfInfoStatic {
	
	static private HopperLocalArfInfoStatic instance;
	
	static private Activity activity;
	static public String arfFileName = "ArfInformation";
	static public HopperLocalFile HLF;

	private HopperLocalArfInfoStatic(Activity inActivity) {	
		
	}
	
	public static void setup(Activity inActivity){
		activity = inActivity;		
		HLF = new HopperLocalFile(activity);
	}
	
	static public String getField(String inField){
		String fieldValueAsString = "";
		String string_fromFile = HLF.read(arfFileName);
		if(string_fromFile == null){return "";}
		JSONObject jsonObj_fromFile = HopperJsonStatic.getJsonObjectFromString(string_fromFile);
		fieldValueAsString = HopperJsonStatic.getStringFromKeyForJsonObject(jsonObj_fromFile,inField);
		Log.v("newStuff",fieldValueAsString );
		return fieldValueAsString;
	}
	
	//static public int getFieldAsInt(String inField){
//		String fieldValueAsString = "";
	//	String string_fromFile = HLF.read(arfFileName);
//		if(string_fromFile == null){return -1;}
//		JSONObject jsonObj_fromFile = HopperJsonStatic.getJsonObjectFromString(string_fromFile);
//		fieldValueAsString = HopperJsonStatic.getStringFromKeyForJsonObject(jsonObj_fromFile,inField);
//		Log.v("newStuff",fieldValueAsString );
		
//		if(fieldValueAsString == ""){
//			return -1;
//		}else{
//			return Integer.parseInt(fieldValueAsString);
//		}
//	}
	
	
	public void putField(String inField, String inValue){
		String string_fromFile = HLF.read(arfFileName);		
		JSONObject jsonObj_fromFile = HopperJsonStatic.getJsonObjectFromString(string_fromFile);		
		HopperJsonStatic.putKeyValueStringsForJsonObject(jsonObj_fromFile, inField,inValue);
		Log.v("newStuff","PUT:"+inValue+jsonObj_fromFile.toString() );
		HLF.save(arfFileName, jsonObj_fromFile.toString());		
	}
	
	static public String getField(Activity inActivity, String inField){
		HopperLocalFile tmpHLF = new HopperLocalFile(inActivity);
		String string_fromFile = tmpHLF.read(arfFileName);
		if(string_fromFile == null){return "";}		
		JSONObject jsonObj_fromFile = HopperJsonStatic.getJsonObjectFromString(string_fromFile);
		String fieldValueAsString = HopperJsonStatic.getStringFromKeyForJsonObject(jsonObj_fromFile,inField);		
		return fieldValueAsString;
	}
	@SuppressLint("NewApi")
	static public int getFieldAsInt(String inField){		
		String string_fromFile = HLF.read(arfFileName);
		if(string_fromFile == null){return 0;}		
		JSONObject jsonObj_fromFile = HopperJsonStatic.getJsonObjectFromString(string_fromFile);
		String fieldValueAsString = HopperJsonStatic.getStringFromKeyForJsonObject(jsonObj_fromFile,inField);
		if(fieldValueAsString == "None" || fieldValueAsString == "" || fieldValueAsString.isEmpty()){
			return -1;
		} else{
			return Integer.parseInt(fieldValueAsString);
		}	
	}
	
	static public void putField(Activity inActivity, String inField, String inValue){
		HopperLocalFile tmpHLF = new HopperLocalFile(inActivity);
		String string_fromFile = tmpHLF.read(arfFileName);		
		JSONObject jsonObj_fromFile = HopperJsonStatic.getJsonObjectFromString(string_fromFile);		
		HopperJsonStatic.putKeyValueStringsForJsonObject(jsonObj_fromFile, inField,inValue);		
		tmpHLF.save(arfFileName, jsonObj_fromFile.toString());		
	}
	
	static public void putFieldStatic(String inField, String inValue){
		String string_fromFile = HLF.read(arfFileName);		
		JSONObject jsonObj_fromFile = HopperJsonStatic.getJsonObjectFromString(string_fromFile);		
		HopperJsonStatic.putKeyValueStringsForJsonObject(jsonObj_fromFile, inField,inValue);
		Log.v("newStuff","PUT:"+inValue+jsonObj_fromFile.toString() );
		HLF.save(arfFileName, jsonObj_fromFile.toString());		
	}
		
		
		
		
	

}
