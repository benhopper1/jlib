package com.example.hopperlibrary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class HopperLocalFile {
	private Activity activity;
	public HopperLocalFile(Activity inActivity) {
		// TODO Auto-generated constructor stub
	}
	
	//does not work!!!!
	public void deleteFile(String inFileName){
		String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath+"ArfInfo", "/"+inFileName);
        boolean deleted = file.delete();
        Log.v("newStuff", "FileDeleted:"+deleted);   
        
	}
	
	
	public void save(String inNewFullFileName, String inData){		
		Log.v("MyActivity", "Save File");    	
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath,"ArfInfo");
        if( !file.exists() ){
        	file.mkdirs();       	
        }
            

        String fn = inNewFullFileName;
        Log.v("MyActivity", filepath+"/"+fn); 
        FileOutputStream out;
        try {
            out = new FileOutputStream(filepath+"/"+fn);
             try {
            	 PrintWriter pw = new PrintWriter(out);
            	 pw.println(inData);
            	 pw.flush();
                 pw.close();
                 out.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        
    }
	public void fileNotExistCreate(String inFileName){
		String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath,"ArfInfo"+"/"+inFileName);		
		if(file.exists()){
			return ;
		}
		String sString = "{\"command\":\"test\"}";
		this.save(inFileName, sString);
		
	}	
		
	
	
	
	public String read(String inNewFullFileName){
		
		String filepath = Environment.getExternalStorageDirectory().getPath();
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(filepath+"/"+inNewFullFileName));
		} catch (FileNotFoundException e) {
			console.log("FILE-NOT-FOUND, creating");
			this.fileNotExistCreate(inNewFullFileName);
			try {
				in = new BufferedReader(new FileReader(filepath+"/"+inNewFullFileName));
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				console.log("PROBLEM, creating, contact arfComm...");
			}
			
		}
		String line = null;
		try {
			line = in.readLine();
		} catch (IOException e) {
			console.log("IOException, creating");
			e.printStackTrace();
		}
		
				
		
        return line;
	}
	
	
	
	
	
	
	
	

}
