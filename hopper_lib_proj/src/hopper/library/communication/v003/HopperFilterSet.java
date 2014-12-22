package hopper.library.communication.v003;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.hopperlibrary.console;

import android.util.Pair;

public class HopperFilterSet {
	//composite key---
	private HashMap<Pair<String,String>, HopperFilter> filtersHash;
	
	public HopperFilterSet(){
		filtersHash = new HashMap<Pair<String,String>, HopperFilter>();
	}
	
	public HopperFilterSet(HopperFilter inFilter){
		filtersHash = new HashMap<Pair<String,String>, HopperFilter>();
		this.addFilter(inFilter);
	}
	
	public void addFilter(HopperFilter inFilter){
		Pair<String, String> filtersHashKey = new Pair<String, String>(inFilter.getKey(), inFilter.getValue());
		this.filtersHash.put(filtersHashKey, inFilter);		
	}
	
	public void addAllFilters(ArrayList<HopperFilter> inFilterArrayList){		
		for(HopperFilter filter : inFilterArrayList){
			Pair<String, String> filtersHashKey = new Pair<String, String>(filter.getKey(), filter.getValue());
			this.filtersHash.put(filtersHashKey, filter);
		}
	}
	
	public HopperFilter getFilter(String inKey, String inValue){
		Pair<String, String> filtersHashKey = new Pair<String, String>(inKey, inValue);
		return filtersHash.get(filtersHashKey);
	}
	
	public void removeFilter(HopperFilter inFilter){
		Pair<String, String> filtersHashKey = new Pair<String, String>(inFilter.getKey(), inFilter.getValue());
		this.filtersHash.remove(filtersHashKey);		
	}

	public void removeAllFilters(ArrayList<HopperFilter> inFilterArrayList){
		for(HopperFilter filter : inFilterArrayList){
			Pair<String, String> filtersHashKey = new Pair<String, String>(filter.getKey(), filter.getValue());
			this.filtersHash.remove(filtersHashKey);
		}
	}
	
	public HashMap getHashMap(){
		return filtersHash;
	}
	public void dump(){
		for(HopperFilter filter : filtersHash.values()){
			console.line("filterSet Dump");
			console.log("filterkey:" + filter.getKey());
			console.log("filterValue:" + filter.getValue());
		}
	}
	
	
}
