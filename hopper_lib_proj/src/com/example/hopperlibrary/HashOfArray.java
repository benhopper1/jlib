package com.example.hopperlibrary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class HashOfArray<T>{
	
	private HashMap<String, ArrayList<T>> hashMap = new HashMap<String, ArrayList<T>>();
	public HashOfArray(){}
	
	public void add(String inKey, T inObject){
		//key already exist, add to the array, else build array then add
		ArrayList<T> hashedArray = hashMap.get(inKey);
		if(hashedArray == null){
			hashedArray = new ArrayList<T>();
			hashMap.put(inKey, hashedArray);
		}
		hashedArray.add(inObject);
		
		
	}
	
	public boolean remove(String inKey, T inObject){
		boolean result = false;
		ArrayList<T> hashedArray = hashMap.get(inKey);
		if(hashedArray != null){
			result = hashedArray.remove(inObject);			
		}		
		return result;
	}
	
	public boolean remove(String inKey){
		boolean result = false;
		ArrayList<T> hashedArray = hashMap.get(inKey);
		if(hashedArray != null){
			hashMap.remove(inKey);
			result = true;
		}		
		return result;
	}
	
	public boolean remove(T inObject){
		boolean result = false;
		for(String theKey : hashMap.keySet()){
			ArrayList<T> theArrayList = hashMap.get(theKey);
			for(T theObject : theArrayList){
				if(inObject.equals(theObject)){
					console.log("removing match in hashOfArray");
					theArrayList.remove(inObject);
					result = true;
				}
			}
		}		
		return result;
	}
	
	public ArrayList<T> getArrayList(String inKey){
		return hashMap.get(inKey);
	}
	
	public void dump(){
		console.line("dump Of HashOfArray");		
		for(String theKey : hashMap.keySet()){
			ArrayList<T> theArrayList = hashMap.get(theKey);
			console.log("@@@@@@@@@@@@@:" + theKey);
			int arrayIndex = 0;
			for(T theObject : theArrayList){
				console.log("[" + (arrayIndex++) + "] " + theObject.toString());
			}
		}
	}
	
	public Set<String> getKeys(){
		return hashMap.keySet();
	}
	
	
}
