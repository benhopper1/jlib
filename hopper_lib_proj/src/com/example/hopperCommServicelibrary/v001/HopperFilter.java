package com.example.hopperCommServicelibrary.v001;

import hopper.library.model.v001.SocialInterfaceModel;

import java.util.ArrayList;

import org.json.JSONObject;

import com.example.hopperlibrary.HopperInstanceHash;

public class HopperFilter {
	private String filterKey;
	
	public HopperFilter(String inFilterKey){
		filterKey = inFilterKey;
	}
	
	/*########################################################################################
	  --------EVENT--------FilterMatch--------------------------------------------------------	 
	*/	
	private ArrayList<OnFilterMatchListener> onFilterMatchListenerArrayList = new ArrayList<OnFilterMatchListener>();
	
	public interface OnFilterMatchListener{
	    public void onFilterMatch(Object...objects);
	}
    public void setOnFilterMatchListener(OnFilterMatchListener listener) {
    	onFilterMatchListenerArrayList.add(listener);
    }
    private void reportOnFilterMatch(final Object...objects){
    	HopperInstanceHash.getInstance("MainActivity").runOnUiThread(new Runnable() {
	        @Override
	        public void run() {  
		    	for(OnFilterMatchListener listener : onFilterMatchListenerArrayList){
		    		listener.onFilterMatch(objects);
		        }
	        }
   		});	
    }
	//-	EndOf FilterMatch--------
    
	/*########################################################################################
	  --------EVENT--------PreExecutionFilter--------------------------------------------------------	 
	*/	
	private ArrayList<OnPreExecutionFilterListener> onPreExecutionFilterListenerArrayList = new ArrayList<OnPreExecutionFilterListener>();
		
	public interface OnPreExecutionFilterListener{
	    public void onPreExecutionFilter(Object...objects);
	}
	public void setOnPreExecutionFilterListener(OnPreExecutionFilterListener listener) {
		onPreExecutionFilterListenerArrayList.add(listener);
	}
	private void reportOnPreExecutionFilter(final Object...objects){
		HopperInstanceHash.getInstance("MainActivity").runOnUiThread(new Runnable() {
	        @Override
	        public void run() {       	
				for(OnPreExecutionFilterListener listener : onPreExecutionFilterListenerArrayList){
			  		listener.onPreExecutionFilter(objects);
			    }
	        }
		});		
	}
	//-	EndOf PreExecutionFilter--------
	
	/*########################################################################################
	  --------EVENT--------Execute--------------------------------------------------------	 
	*/	
	private ArrayList<OnExecuteListener> onExecuteListenerArrayList = new ArrayList<OnExecuteListener>();
		
	public interface OnExecuteListener{
	    public void onExecute(Object...objects);
	}
	public void setOnExecuteListener(OnExecuteListener listener) {
		onExecuteListenerArrayList.add(listener);
	}
	private void reportOnExecute(final Object...objects){
		HopperInstanceHash.getInstance("MainActivity").runOnUiThread(new Runnable() {
	        @Override
	        public void run() {       	
	        
				for(OnExecuteListener listener : onExecuteListenerArrayList){
			  		listener.onExecute(objects);
			    }
				
				
	        }//public void run() {
		});		
	}
	//-	EndOf Execute--------
	
	public String getFilterKey(){
		return filterKey;
	}
	//---process should be called to process this filter-----
	public boolean process(String inRemoteFilterKey, JSONObject inMessage){
		boolean retBool = false;
		if(compare(inRemoteFilterKey)){
			boolean preExecutionFilter = preExecutionFilter(inRemoteFilterKey, inMessage);
			reportOnPreExecutionFilter(filterKey, inMessage, preExecutionFilter);
			if(preExecutionFilter){
				execute(filterKey,inMessage);
				reportOnExecute(filterKey, inMessage);
				retBool = true;
			}
		}		
		return retBool;
	}
    
    
	protected boolean compare(String inRemoteFilterKey){
		boolean retBool = false;
		if(filterKey.equalsIgnoreCase(inRemoteFilterKey)){
			retBool = true;
			reportOnFilterMatch(this, filterKey);
		}
		
		return retBool;
	}
	
	protected boolean preExecutionFilter(String inRemoteFilterKey, JSONObject inMessage){
		//--could override to add extra filter,, saves on parsing, return false to stop execution
		return true;
	}
	
	protected void execute(String inFilterKey, JSONObject inMessage){
		//should override me for a result----------
	}
	
}
