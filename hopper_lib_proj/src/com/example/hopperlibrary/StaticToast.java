package com.example.hopperlibrary;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

public class StaticToast extends Application{
	static StaticToast thisInstance;
	private static Context staticContext;
	private Context context; //= getApplicationContext();;
	private StaticToast(){
		context= getApplicationContext();
		staticContext = context;
		// TODO Auto-generated constructor stub
	}
	
	static StaticToast getSingleton(){
		if(thisInstance == null){
			return new StaticToast();
		}else{
			return thisInstance;
		}		
	}
	
	static public void message(String inString){
		if(thisInstance == null){getSingleton(); }
		//Context context = null;
		Toast toast = Toast.makeText(staticContext, inString, Toast.LENGTH_LONG);
		toast.show();
	}
	
	static public void message(Context inContext,String inString){
		//if(thisInstance == null){getSingleton(); }
		//Context context = null;
		Toast toast = Toast.makeText(inContext, inString, Toast.LENGTH_LONG);
		toast.show();
	}
	
	

}
