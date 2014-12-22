package com.example.hopperCommServicelibrary.v001;

import hopper.library.communication.WebSocketCommService;
import hopper.library.communication.WebSocketCommService.OnMessagedListener;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import com.example.hopperlibrary.HopperJsonStatic;
import com.example.hopperlibrary.console;

import android.util.Log;

public class HopperFilterSet {
	private HashMap<String, HopperFilter> filterHash = new HashMap<String, HopperFilter>();
	private WebSocketCommService webSocketCommService;
	
	
	public HopperFilterSet(WebSocketCommService inWebSocketCommService){
		console.log("HopperFilterSet Constructor Entered");
		webSocketCommService = inWebSocketCommService;
		webSocketCommService.setOnMessagedListener(new OnMessagedListener(){
			/*
			 * (non-Javadoc)
			 * @see hopper.library.communication.WebSocketCommService.OnMessagedListener#onMessaged(java.lang.Object[])
			 * @WebSocketCommService.this 
			 * @message as text
			 */
			@Override
			public void onMessaged(Object... inObjects){
				// TODO Auto-generated method stub
				//process forward from here---
				HopperFilterSet.this.processMessage((String)inObjects[1]);
				
			}			
		});
		
	}
	
	public void addFilter(HopperFilter inLocalFilter){
		console.log("HopperFilterSet addFilter Entered");
		filterHash.put(inLocalFilter.getFilterKey(), inLocalFilter);	
	}
	
	public void dump(){
		console.log("----------F I L T E R S E T----D U M P------------------------");
		for (HopperFilter filter : filterHash.values()) {			
			console.log("filter:"+filter.getFilterKey());
		}
		console.log("--------------------------------------------------------------");
	}
	
	public boolean processMessage(String inMessage){
		console.log("HopperFilterSet processMessage Entered");
		JSONObject json_inMessage = HopperJsonStatic.createJsonObjectFromJsonString(inMessage);
		String remoteFilterKey = HopperJsonStatic.getStringFromKeyForJsonObject(json_inMessage, "filterKey");
		HopperFilter localFilter;
		if(remoteFilterKey.equals("")){
			console.log("HopperFilterSet.processMessage no remoteFilterKey.equals('')");
			return false;			
		}
		localFilter = filterHash.get(remoteFilterKey);
		if(localFilter == null){
			return false;
		}
		
		return localFilter.process(remoteFilterKey, json_inMessage);					
	}
	
}
