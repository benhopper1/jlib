package com.example.hopperlibrary;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.Environment;

public class HopperFileInfo {

	private HopperFileInfo() {
		// TODO Auto-generated constructor stub
	}
	
	static public String getBasename(String inString){
		return inString.substring( inString.lastIndexOf('/')+1, inString.length() );
	}
	static public String getBasenameWithExtension(String inString){
		return getBasename(inString);
	}
	static public String getBasenameWithoutExtension(String inString){
		if(getBasename(inString).lastIndexOf('.') == -1){return "";}
		return getBasename(inString).substring(0, getBasename(inString).lastIndexOf('.'));
	}
	
	static public String getExtension(String inString){
		String filenameArray[] = inString.split("\\.");
		if(filenameArray.length<1){return "";}
        return filenameArray[filenameArray.length-1];		
	}
	
	static public String getPath(String inString){
		return inString.replace(getBasename(inString), "");	

	}
	public static boolean fileUrlexists(String URLName){
	    try {
	      HttpURLConnection.setFollowRedirects(false);
	      // note : you may also need
	      //        HttpURLConnection.setInstanceFollowRedirects(false)
	      HttpURLConnection con =
	         (HttpURLConnection) new URL(URLName).openConnection();
	      con.setRequestMethod("HEAD");
	      return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
	    }
	    catch (Exception e) {
	       e.printStackTrace();
	       return false;
	    }
	  }
	static public File createTempFile(String inExtension, String inAppName){
		//console.loge("APP ____NAME:" + HopperFileInfo.class.getPackage().getName());
		String tmpFolder = Environment.getExternalStorageDirectory() + "/Android/data/" + inAppName + "/cache/";
		File directory = new File(tmpFolder);
		directory.mkdirs();
		File tmpFile = new File(tmpFolder + "tmpImgCovFile." + inExtension);
		
		return tmpFile;
	}
	
	
	
	
	
}
