package com.example.hopperlibrary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;



public class HopperActivity {

	public HopperActivity() {
		// TODO Auto-generated constructor stub
	}
	
	public static Intent loadActivity(Context inContext, Class inClass, String inExtra){
		Log.v("CLASS","CLASS NAME:"+inClass.getName());
		Intent i = new Intent(inContext, inClass);
	    i.putExtra("extra", inExtra);
	    inContext.startActivity(i);
	    return i;
	}
	
	
	public static void closeActivity(Intent inIntent){
		//Activity mActivity = (Activity) inIntent; 
		//closeNow()
	}
}
