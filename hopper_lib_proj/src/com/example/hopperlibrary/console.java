package com.example.hopperlibrary;

import android.util.Log;

public class console {
	public static String tag = "arfComm";
	public console() {
		// TODO Auto-generated constructor stub
	}
	public static void log(String inString){
		Log.v(tag, inString);
	}
	public static void log(int inVal){
		Log.v(tag, String.valueOf(inVal));
	}
	public static void log(String inString, int inVal){
		Log.v(tag, inString+":["+String.valueOf(inVal)+"]");
	}
	
	public static void loge(String inString){
		Log.e(tag, "ERROR:"+inString);
	}
	
	public static void line(String head){
		Log.e(tag, "------------------------------------<--  " + head + "  -->-------------------------------------------------------");
	}
	public static void line(){
		Log.e(tag, "--------------------------------------------------------------------------------------------------------");
	}	
}
