package com.example.hopperlibrary;

import java.util.HashMap;

import android.app.Activity;
import android.util.Log;

public class HopperInstanceHash {
	
	private static HashMap<String, Activity> mHash = new HashMap<String, Activity>();
	
	
	private HopperInstanceHash() {
		// TODO Auto-generated constructor stub
	}
	
	public static void add(String inKey, Activity inInstance){
		mHash.put(inKey, inInstance);
		Log.v("HopperInstanceHash","put:"+inKey+" ,"+inInstance.toString());
	}
	
	public static Activity getInstance(String inKey){		
		Log.v("HopperInstanceHash","get:"+inKey);
		return mHash.get(inKey);
	}
	
	public static void remove(String inKey){
		mHash.remove(inKey);
		Log.v("HopperInstanceHash","remove:"+inKey);
	}
	
}
