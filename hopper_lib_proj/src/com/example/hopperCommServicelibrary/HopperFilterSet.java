package com.example.hopperCommServicelibrary;

import java.util.ArrayList;

import org.json.JSONObject;

import com.example.hopperlibrary.console;

import android.util.Log;

public class HopperFilterSet {
	private int id;
	private static int countOf = 0;
	private JSONObject theJson;
	
	
	public ArrayList<HopperFilter> filters;
	//--------callback-------------------------------------------------------------
		private ArrayList<OnFilterPassListener> onFilterPassListenerArrayList = new ArrayList<OnFilterPassListener>();
		
		public interface OnFilterPassListener{
		    public void onFilterPass(Object...objects);
		}
	    public void setOnFilterPassListener(OnFilterPassListener listener) {
	    	onFilterPassListenerArrayList.add(listener);
	    }
	    private void reportOnFilterPass(Object...objects){
	    	for(OnFilterPassListener listener : onFilterPassListenerArrayList){
	    		listener.onFilterPass(objects);
	        }
	    }
	    
	    public JSONObject getJsonObject(){
	    	return theJson;
	    }
	    
	    public void setJsonObject(JSONObject inJson){
	    	theJson = inJson;
	    }
	    
	    
	    
	//-
	
	/*
	 * ====================================================================
	 */
	public HopperFilterSet() {
		this.filters = new ArrayList<HopperFilter>();
		this.id = HopperFilterSet.countOf;
		HopperFilterSet.countOf++;
		
		Log.v("filter","Id:"+this.id);	
	}
	
	//copy constructor, keeps same id---
	//public HopperFilterSet(HopperFilterSet inHopperFilterSet) {
	//	this.id = inHopperFilterSet.getId();
	//	for(HopperFilter item_filter : inHopperFilterSet.filters){
	//		this.add(item_filter);
	//	}		
	//}
	public int getId(){
		return this.id;
	}
	
	//check all keys for match and return value on match else null
	public String getValueFromKey(String inKey){
		Log.v("HopperFilterSet","getValueFromKey Started:"+inKey);
		Log.v("HopperFilterSet","size of filters:"+this.filters.size());
		for(HopperFilter HF : this.filters){
			Log.v("HopperFilterSet","getValueFromKey_1");	
			if(HF.getKey().trim().equalsIgnoreCase(inKey)){
				Log.v("HopperFilterSet","getValueFromKey_2");	
				return HF.getValue();
			}			
		}
		Log.v("HopperFilterSet","getValueFromKey_3");	
		return "-NA-";
	}
	
	public int getValueFromKeyAsInt(String inKey){
		Log.v("HopperFilterSet","getValueFromKey Started:"+inKey);
		Log.v("HopperFilterSet","size of filters:"+this.filters.size());
		for(HopperFilter HF : this.filters){
			Log.v("HopperFilterSet","getValueFromKey_1");	
			if(HF.getKey().trim().equalsIgnoreCase(inKey)){
				Log.v("HopperFilterSet","getValueFromKey_2");
				String result = HF.getValue();
				try {
					return Integer.parseInt(result);
				} catch (NumberFormatException e) {
					Log.e("ERROR", "PARSE ERROR IN HOPPER FILTER SET, STRING TO INT");
					e.printStackTrace();
					return 0;
				}
			}			
		}
		Log.v("HopperFilterSet","getValueFromKey_3");	
		return 0;
	}
	
	
	
	
	public void add(String inKey, String inValue){
		Log.v("FilterSet.add","attempting FilterSet.add:"+inKey+" ,"+inValue);
		if(inKey == null || inValue == null ){return;}
		HopperFilter tmpFilter = new HopperFilter(inKey, inValue);
		this.add(tmpFilter);
	}	
	
	public void add(HopperFilter inFilter){
		Log.v("HopperFilterSet","add HopperFilter(inType) Entered");	
		this.filters.add(inFilter);
		Log.v("HopperFilterSet","add HopperFilter(inType) Done");
	}
	
	public HopperFilter getFilterAtIndex(int inIndex){
		if(!(inIndex < this.filters.size())){return null;}
		return this.filters.get(inIndex);
	}
	
	public ArrayList<HopperFilter> getFilters(){
		return this.filters;
	}
	
	public int getSize(){
		return this.filters.size();
	}
	
	public void exec(HopperFilterSet inFilterSet){
		int score_pass = 0;
		int score_fail = 0;
		int score = 0;
		Log.v("filter","exec SIZof REMOTE:"+inFilterSet.filters.size());
		Log.v("filter","exec SIZof LOCAL:"+this.filters.size());
		for(HopperFilter filterLocal : this.filters){
			Log.v("filter","in local for----");
			for(HopperFilter filterRemote : inFilterSet.filters){
				Log.v("filter","in remote for----");
				Log.v("filter","Key Test(remote, local):"+filterRemote.getKey()+" , "+filterLocal.getKey());
				Log.v("filter","QUESTION:"+filterRemote.getKey().trim().equalsIgnoreCase(filterLocal.getKey().trim()));
				if(filterRemote.getKey().trim().equalsIgnoreCase(filterLocal.getKey().trim())){
					Log.v("filter","keyMatch:"+filterRemote.getKey());
					Log.v("filter","Value(remote, local) Test:"+filterRemote.getValue()+" , "+filterLocal.getValue());
					if(filterRemote.getValue().trim().equalsIgnoreCase(filterLocal.getValue().trim())){
						Log.v("filter","getValue:"+filterRemote.getValue());
						score_pass++;
						score++;
						this.filterPassCallback(this.id, this, inFilterSet, filterRemote.getKey(), filterRemote.getValue());
						reportOnFilterPass(this.id, this, inFilterSet, filterRemote.getKey(), filterRemote.getValue());
					}else{
						Log.v("filter","getValue(FAILED):"+filterRemote.getValue());
						score_fail++;
						score--;
						this.filterFailCallback(this.id, this, inFilterSet, filterRemote.getKey(), filterLocal.getValue(), filterRemote.getValue());
					}
				}				
			}			
		}
		if(score_pass > 0){
			this.logicOrCallback(this.id, this, inFilterSet, score_pass, score_fail, score);
		}
		if(score_fail < 1 && score_pass > 0){
			this.logicAndCallback(this.id, this, inFilterSet, score_pass, score_fail, score);
		}
		this.callback(this.id, this, inFilterSet, score_pass, score_fail, score);
		
	}
	
	public void callback(int inId, HopperFilterSet inReference, HopperFilterSet remoteFilterSet, int inScore_pass, int inScore_fail, int inScore){
		Log.v("filter","callback _ENTERED, id:"+this.id);
		Log.v("filter","callback id:"+inId+"  pass:"+inScore_pass+"  fail:"+inScore_fail+"  score:"+inScore);
	}
	
	public void logicAndCallback(int inId, HopperFilterSet inReference, HopperFilterSet remoteFilterSet, int inScore_pass, int inScore_fail, int inScore){
		Log.v("filter","logicAndCallback id:"+inId+"  pass:"+inScore_pass+"  fail:"+inScore_fail+"  score:"+inScore);
	}
	
	public void logicOrCallback(int inId, HopperFilterSet inReference, HopperFilterSet remoteFilterSet, int inScore_pass, int inScore_fail, int inScore){
		Log.v("filter","logicOrCallback id:"+inId+"  pass:"+inScore_pass+"  fail:"+inScore_fail+"  score:"+inScore);
	}
	public void filterPassCallback(int inId, HopperFilterSet inReference, HopperFilterSet remoteFilterSet, String inKey, String inValue){
		Log.v("filter","filterPassCallback id:"+inId+"  inKey:"+inKey+"  inValue:"+inValue);
	}
	public void filterFailCallback(int inId, HopperFilterSet inReference, HopperFilterSet remoteFilterSet, String inKey, String inLocalValue, String inRemoteValue){
		Log.v("filter","filterFailCallback id:"+inId+"  inKey:"+inKey+"  inLocalValue:"+inLocalValue+" inRemoteValue:"+inRemoteValue);		
	}
	
	
	
	
	
	
}
