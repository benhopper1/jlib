package com.example.hopperlibrary;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.app.Activity;
import android.content.Context;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

public class HopperMiscStatic {
	public static void closekeyBoard(Activity inActivity, TextView inObject){
		InputMethodManager imm = (InputMethodManager)inActivity.getSystemService( Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(inObject.getWindowToken(), 0);
	}
	
	public static String getMacAddress(){
		InetAddress ip;
		byte[] mac = null;
		try {
			ip = InetAddress.getLocalHost();		
		//ip.getHostAddress();
		NetworkInterface network = NetworkInterface.getByInetAddress(ip);
		mac = network.getHardwareAddress(); 
		System.out.print("Current MAC address : "); 
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < mac.length; i++) {
			sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));		
		}
		return sb.toString();
	
	}
		
	public static String getCurrentIpAddress(){
		InetAddress ip = null;
		try {
			ip = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ip.getHostAddress().toString();
	}
		
}
