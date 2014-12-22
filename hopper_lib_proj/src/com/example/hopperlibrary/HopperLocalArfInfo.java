package com.example.hopperlibrary;

import org.json.JSONObject;

import android.app.Activity;
import android.util.Log;

public class HopperLocalArfInfo {
	private Activity activity;
	public String arfFileName;
	public HopperLocalFile HLF;
	public HopperLocalArfInfo(Activity inActivity) {		
		this.activity = inActivity;
		this.arfFileName = "ArfInformation";
		this.HLF = new HopperLocalFile(this.activity);
	}
	
	public String getField(String inField){
		String string_fromFile = this.HLF.read(this.arfFileName);
		if(string_fromFile == null){return "";}
		JSONObject jsonObj_fromFile = HopperJsonStatic.getJsonObjectFromString(string_fromFile);
		String fieldValueAsString = HopperJsonStatic.getStringFromKeyForJsonObject(jsonObj_fromFile,inField);
		Log.v("newStuff",fieldValueAsString );
		return fieldValueAsString;
	}
	
	public void putField(String inField, String inValue){
		String string_fromFile = this.HLF.read(this.arfFileName);		
		JSONObject jsonObj_fromFile = HopperJsonStatic.getJsonObjectFromString(string_fromFile);		
		HopperJsonStatic.putKeyValueStringsForJsonObject(jsonObj_fromFile, inField,inValue);
		Log.v("newStuff","PUT:"+inValue+jsonObj_fromFile.toString() );
		this.HLF.save(this.arfFileName, jsonObj_fromFile.toString());		
	}	
	
	
	
	
}
