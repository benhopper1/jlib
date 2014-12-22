package com.example.hopperCommServicelibrary;

public class HopperFilter {
	public String key;
	public String Value;
	public HopperFilter(String inKey, String inValue) {
		if(inKey != null){
			this.key = inKey;
		}else {
			this.key = "";
		}
		if(inValue != null){
			this.Value = inValue;
		}else{
			this.Value = "";
		}
		
		
		
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return Value;
	}
	public void setValue(String value) {
		Value = value;
	}

}
