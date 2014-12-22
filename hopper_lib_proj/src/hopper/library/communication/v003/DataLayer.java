package hopper.library.communication.v003;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.hopperlibrary.HopperJsonStatic;

public class DataLayer {
	static public DataLayer dataLayer(){
		return new DataLayer();
	} 
	private JSONObject dataLayer_json;
	
	public DataLayer(){
		dataLayer_json = new JSONObject();
	}
	
	public DataLayer(JSONObject inJson){
		dataLayer_json = inJson;
	}
	
	public void createDataLayer(JSONObject inJson){
		this.dataLayer_json = inJson;
	}
	
	public String getField(String inKey){
		return HopperJsonStatic.getStringFromKeyForJsonObject(dataLayer_json, inKey);
	}
	
	public String toString(){
		return dataLayer_json.toString();
	}	
	
	public JSONObject toJson(){
		return dataLayer_json;
	}
	
	public DataLayer add(String inKey, String inValue){
		HopperJsonStatic.putKeyValueStringsForJsonObject(dataLayer_json, inKey, inValue);
		return this;
	}
	
	public DataLayer add(String inKey, JSONObject inValue){
		HopperJsonStatic.putKeyValueStringObjectForJsonObject(dataLayer_json, inKey, inValue);
		return this;
	}
	
	public DataLayer add(String inKey, JSONArray inValue){
		HopperJsonStatic.putArrayIntoObjectWithKey(dataLayer_json, inValue, inKey);
		return this;
	}
	
	public DataLayer clean(){
		dataLayer_json = new JSONObject();
		return this;
	}
	
	
}
