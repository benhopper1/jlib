/*
 *  -------------------- USE -------------------------------------
 *  	 LocalUserStore.setup(this, "testProject3", "data3.conf");
 *       LocalUserStore.putKeyAndValue("entryOne", "yesMan!!");
 *       LocalUserStore.dump();
 *       console.log(">-:" + LocalUserStore.getValueAsString("entryOne"));
 */



package hopper.library.local.store;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import org.json.JSONObject;

import com.example.hopperlibrary.HopperJsonStatic;
import com.example.hopperlibrary.console;

import android.app.Activity;
import android.os.Environment;
import android.text.format.Time;

public class LocalUserStore {
	private  LocalUserStore(){}
	static private boolean isSetup = false;
	static Activity activity;
	static private String projectName;
	static private String fileName;
	static private File currentFile;
	
	
	static public boolean exist(String inProjectName, String inFileName){
		File tmpFile = new File(Environment.getExternalStorageDirectory().getPath(), projectName + "/" + fileName);
		return tmpFile.exists();
		/*if){
			return true;
		}else{
			
		}*/
	}
	
	
	static public void setup(Activity inActivity, String inProjectName, String inFileName){
		isSetup = true;
		activity = inActivity;
		projectName = inProjectName;
		fileName = inFileName;
		
		if(openFile().exists()){
			console.log("LocalUserStore.setup LOCAL FILE ALREADY EXIST");
			return;
		}
		
		
		try {
			File directory = new File(Environment.getExternalStorageDirectory().getPath(), projectName);
			if(!(directory.exists())){
				directory.mkdir();
			}
			currentFile.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		console.log("LocalUserStore.setup CREATING LOCAL FILE");
		
		Time now = new Time();
		now.setToNow();
		writeString("{\"fileStart\":\"" + Long.toString(now.toMillis(false)) + "\"}");
		
	}	
	
	static private File openFile(){
		console.log("LocalUserStore.openFile" + projectName + "/" + fileName);
		currentFile = new File(Environment.getExternalStorageDirectory().getPath(), projectName + "/" + fileName);		
		console.log("LocalUserStore.openFile absPath:" + currentFile.getAbsolutePath());
		return currentFile;
	}
	
	static private void writeString(String inString){
		if(currentFile == null){return;}
		FileOutputStream out;
        try {
            out = new FileOutputStream(currentFile.getAbsolutePath());
             
	    	 PrintWriter pw = new PrintWriter(out);
	    	 pw.println(inString);
	    	 pw.flush();
	         pw.close();
	         out.close();
               

        } catch (FileNotFoundException e1) {
        	console.loge("FileNotFoundException in LocalUserStore.writeString() fileAbsPath:" + currentFile.getAbsolutePath());
            e1.printStackTrace();
        } catch (IOException e) {
            console.loge("IOException in LocalUserStore.writeString() fileAbsPath:" + currentFile.getAbsolutePath());
            e.printStackTrace();
        }
	}
	
	static private String readString(){		
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(currentFile.getAbsolutePath()));
		} catch (FileNotFoundException e) {
			console.log("FILE-NOT-FOUND, in LocalUserStore.readString" + currentFile.getAbsolutePath());			
			return "{}";			
		}
		String line = null;
		try {
			line = in.readLine();
			in.close();
		} catch (IOException e) {
			console.log("IOException, creating/write/close in LocalUserStore.readString");
			e.printStackTrace();
		}
		
        return line;		
	}
	
	static public void putKeyAndValue(String inKey, String inValue){
		JSONObject existing_json = HopperJsonStatic.createJsonObjectFromJsonString(readString());
		HopperJsonStatic.putKeyValueStringsForJsonObject(existing_json, inKey, inValue);
		writeString(existing_json.toString());
	}
	
	static public void putKeyAndValue(String inKey, int inValue){
		console.log("ONLY USING STRING TYPES, IMPLEMENT LATER!!!");
	}
	
	static public String getValueAsString(String inKey){
		JSONObject existing_json = HopperJsonStatic.createJsonObjectFromJsonString(readString());
		return HopperJsonStatic.getStringFromKeyForJsonObject(existing_json, inKey);
	}
	
	static public int getValueAsInt(){
		console.log("ONLY USING STRING TYPES, IMPLEMENT LATER!!!");
		return -1;
	}
	
	static public void dump(){
		JSONObject existing_json = HopperJsonStatic.createJsonObjectFromJsonString(readString());
		console.log("-------------------- > LocalUserStore D U M P < ---------------------------------------------------------");
		for(String theKey : HopperJsonStatic.getKeysFromJsonObject(existing_json)){
			console.log("KEY:" + theKey + "  VALUE:" + HopperJsonStatic.getStringFromKeyForJsonObject(existing_json, theKey));
		}
	}
	
	static public void delete(){
	    if (currentFile != null){
	    	writeString("{}");
			if(currentFile.delete()){
				console.log("file Deleted");
			}else{
				console.log("file NOT Deleted");
			}
	        return;
	    }
	}
}



