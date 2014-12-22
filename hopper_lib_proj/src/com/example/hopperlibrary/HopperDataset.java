package com.example.hopperlibrary;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import android.util.Log;

public class HopperDataset {
	private HopperCommunicationInterface HCI;
	private JSONArray dataset;
	
	private String encode(String inString){
		return inString.replace("'", "\"");
	}
	
	public HopperDataset(String inHCIName){
		this.HCI = HopperCommunicationInterface.get(inHCIName);		
	}
	
	public HopperDataset(HopperCommunicationInterface inHCI){
		this.HCI = inHCI;
	}
	
	public void executeSql(String inString){
		inString = encode(inString);
		JSONObject json_inFromServer = HCI.executeSql(inString, "select");
		JSONArray jsonArray_dataset = HopperJsonStatic.getJSONArray(json_inFromServer,"dataset")	;
		this.dataset = jsonArray_dataset;
		
	}
	
	public void executeQueryCommand(String inQueryCommand, JSONObject inParameters){
		JSONObject json_inFromServer = HCI.executeQueryCommand(inQueryCommand, inParameters);
		JSONArray jsonArray_dataset = HopperJsonStatic.getJSONArray(json_inFromServer,"dataset")	;
		this.dataset = jsonArray_dataset;
	}
	
	public int executeSqlWithReturnNewId(String inString){
		inString = encode(inString);
		JSONObject json_inFromServer = HCI.executeSql(inString, "insert");
		int newId = Integer.parseInt(HopperJsonStatic.getStringFromKeyForJsonObject(json_inFromServer, "newId"))	;
		this.dataset = null;
		return newId;		
	}
			
	public String getFieldAsString(String inFieldName, int inRowIndex){
		if (this.dataset ==null){Log.e("MyActivity","ERROR ->   No dataset!!!!!");return "";}
		if (inRowIndex >=dataset.length()){Log.e("MyActivity","ERROR ->  index too high!!"); return "";}
		
		JSONObject json_record = HopperJsonStatic.getJsonObjectFromJsonArrayByIndex(dataset, inRowIndex);
		String fieldValueAsString = HopperJsonStatic.getStringFromKeyForJsonObject(json_record,inFieldName);
		return fieldValueAsString;
	}
	public int getFieldAsInt(String inFieldName, int inRowIndex){
		String myFieldValueAsString = getFieldAsString(inFieldName, inRowIndex);
		int fieldAsInteger = 0;
		 try {
			fieldAsInteger = Integer.parseInt(myFieldValueAsString);
		} catch (NumberFormatException e) {
			Log.e("MyActivity","ERROR  HopperDataset.getFieldAsInt cannot be parsed to int type");
			e.printStackTrace();
		}		 
		 return fieldAsInteger;
	}
	public float getFieldAsFloat(String inFieldName, int inRowIndex){
		String myFieldValueAsString = getFieldAsString(inFieldName, inRowIndex);
		float fieldAsInteger = 0;
		 try {
			fieldAsInteger = Float.parseFloat(myFieldValueAsString);
		} catch (NumberFormatException e) {
			Log.e("MyActivity","ERROR  HopperDataset.getFieldAsFloat cannot be parsed to float type");
			e.printStackTrace();
		}		 
		 return fieldAsInteger;
	}
	public ArrayList<String> getColumnAsArrayListOfString(String inFieldName){
		ArrayList<String> stringArrayList = new ArrayList<String>();
		int recordCount = dataset.length();
		for(int i = 0; i<recordCount;i++){
			String fieldString = getFieldAsString(inFieldName, i);
			stringArrayList.add(fieldString);
		}
		return stringArrayList;
	}
	
	
	public int getRecordCount(){
		if(dataset !=null){
			return dataset.length();
		}else{
			return 0;
		}
	}
	
	public ArrayList<String> getArrayOfFieldNames(){
		ArrayList<String> retArray = new ArrayList<String>();
		JSONObject jsonRec = HopperJsonStatic.getJsonObjectFromJsonArrayByIndex(dataset,0);
		Iterator<String> iter = jsonRec.keys();
	    while (iter.hasNext()) {
	        String key = iter.next();
	        retArray.add(key);//jsonRec.getString(key));
			console.log("filed->:"+key);
	    }
	    return retArray;
	}
	
	
	
	
	
	private void testsql(){
		HopperCommunicationInterface HCI = HopperCommunicationInterface.get("COMM");
		JSONObject json_inFromServer = HCI.executeSql("select * from tb_user", "select");
		JSONArray jsonArray_dataset = HopperJsonStatic.getJSONArray(json_inFromServer,"dataset")	;
		
		for(int i =0;i<jsonArray_dataset.length();i++){
			JSONObject json_record = HopperJsonStatic.getJsonObjectFromJsonArrayByIndex(jsonArray_dataset, i);
			String fName = HopperJsonStatic.getStringFromKeyForJsonObject(json_record,"fName");
			String lName = HopperJsonStatic.getStringFromKeyForJsonObject(json_record,"lName");
			
			Log.v("MyActivity","fName:"+fName);
			Log.v("MyActivity","lName:"+lName);
			
		}
		
	}

}
