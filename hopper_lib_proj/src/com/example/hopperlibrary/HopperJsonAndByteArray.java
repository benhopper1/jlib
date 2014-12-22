package com.example.hopperlibrary;

import org.json.JSONObject;

import android.util.Log;
// wrap for referencial return and transport
public class HopperJsonAndByteArray {
	
	public JSONObject jsonObject;
	public byte[] byteArray;
	
	public HopperJsonAndByteArray(JSONObject inJson, byte[] inByteArray){
		this.jsonObject =	inJson;
		this.byteArray  =	inByteArray;
	}
	
	public JSONObject getJson(){
		return this.jsonObject;
	}
	
	public byte[] getByteArray(){
		return this.byteArray;
	}
	
	
	public void test(byte[] inByteArray){
		inByteArray[0]=(byte) 100;
		inByteArray[1]=(byte) 101;
		inByteArray[2]=(byte) 102;
		Log.v("MyActivity","-------"+inByteArray[3]);
		inByteArray[3]= (byte) (inByteArray[3]+(byte) 1);
		Log.v("MyActivity","-------"+inByteArray[3]);
		
	}
	
	
}
