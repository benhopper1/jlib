package hopper.library.communication.v003;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.hopperlibrary.HopperJsonStatic;
import com.example.hopperlibrary.console;

public class TransportLayer{
	static public Queue<TransportLayer> transportLayerQueue = new LinkedList<TransportLayer>();
	static public String deviceId = "";
	static public String userId = "";
	static public String securiryToken = "";
	static int objectCount = 0;
	
	public JSONObject transportLayer_json;
	
	public RoutingLayer routingLayer;
	public DataLayer dataLayer;
	
	
	TransportLayer(){
		transportLayer_json = new JSONObject();
		routingLayer = new RoutingLayer();
		dataLayer = new DataLayer();
		//console.log("Qsize:" + transportLayerQueue.size());
		//console.log("objectCount:"+objectCount);
	}
	
	static public void setup(String inUserId, String inDeviceId, String inSecurityToken){
		userId = inUserId;
		deviceId = inDeviceId;		
		securiryToken = inSecurityToken;
	}
	
	// after setup on client ->
	static public TransportLayer createTransportLayer(){
		TransportLayer transportLayerInstance = transportLayerQueue.poll();
		if(transportLayerInstance == null){
			transportLayerInstance = new TransportLayer();objectCount++;
		}
		//JSONObject build_json = new JSONObject();
		HopperJsonStatic.putKeyValueStringsForJsonObject(transportLayerInstance.transportLayer_json, "userId", userId);
		HopperJsonStatic.putKeyValueStringsForJsonObject(transportLayerInstance.transportLayer_json, "deviceId", deviceId);
		HopperJsonStatic.putKeyValueStringsForJsonObject(transportLayerInstance.transportLayer_json, "securiryToken", securiryToken);		
		
		return transportLayerInstance;
	}
	
	static public TransportLayer createTransportLayer(String fromServer_json){
		TransportLayer transportLayerInstance = transportLayerQueue.poll();
		if(transportLayerInstance == null){
			transportLayerInstance = new TransportLayer();
		}
		
		transportLayerInstance.transportLayer_json = HopperJsonStatic.createJsonObjectFromJsonString(fromServer_json);
		if(transportLayerInstance.routingLayer == null){
			transportLayerInstance.routingLayer = new RoutingLayer();			
		}
		transportLayerInstance.routingLayer.createRoutingLayer(HopperJsonStatic.getJsonObjectFromKey(transportLayerInstance.transportLayer_json, "routingLayer"));
		
		if(transportLayerInstance.dataLayer == null){
			transportLayerInstance.dataLayer = new DataLayer();			
		}
		transportLayerInstance.dataLayer.createDataLayer(HopperJsonStatic.getJsonObjectFromKey(transportLayerInstance.transportLayer_json, "dataLayer"));
		//build		
		return transportLayerInstance;
	}
	
	private void setDataLayer(JSONObject inDataLayer_json){
		HopperJsonStatic.putKeyValueStringObjectForJsonObject(this.transportLayer_json, "dataLayer", inDataLayer_json);
	}
	
	
	public JSONObject getDataLayer(){
		return HopperJsonStatic.getJsonObjectFromKey(this.transportLayer_json, "dataLayer");
	}
	
	public DataLayer getDataLayerAsDataLayer(){
		return this.dataLayer();
	}

	private void setRoutingLayer(JSONObject inDataLayer_json){
		HopperJsonStatic.putKeyValueStringObjectForJsonObject(this.transportLayer_json, "routingLayer", inDataLayer_json);
	}
	
	public JSONObject getRoutingLayer(){
		return HopperJsonStatic.getJsonObjectFromKey(this.transportLayer_json, "routingLayer");
	}
	
	public RoutingLayer getRoutingLayerAsRoutingLayer(){
		RoutingLayer result = new RoutingLayer(HopperJsonStatic.getJsonObjectFromKey(this.transportLayer_json, "routingLayer"));
		return result;
	}	
	
	
	public String getTransactionId(){
		return HopperJsonStatic.getStringFromKeyForJsonObject(this.transportLayer_json, "transactionId");
	}
	
	public void setTransactionId(String inTransactionId){
		HopperJsonStatic.putKeyValueStringsForJsonObject(this.transportLayer_json, "transactionId", inTransactionId);
	}
	
	public String toString(){
		return this.toJson().toString();
	}
	
	public JSONObject toJson(){
		//JSONObject result_json = new
		//HopperJsonStatic.putKeyValueStringObjectForJsonObject(transportLayer_json, "", inValue)
		this.setRoutingLayer(this.routingLayer.toJson());
		this.setDataLayer(this.dataLayer.toJson());
		return transportLayer_json;
	}	
	
	public void reuse(){
		transportLayerQueue.add(this);
	}
	
	public TransportLayer transportLayerFields(){
		return this;
	}
	
	public TransportLayer add(String inKeyField, String inValueField){
		HopperJsonStatic.putKeyValueStringsForJsonObject(transportLayer_json, inKeyField, inValueField);
		return this;
	}
	
	public TransportLayer addToRoutingLayer(String inKeyField, String inValueField){
		HopperJsonStatic.putKeyValueStringsForJsonObject(routingLayer.toJson(), inKeyField, inValueField);
		return this;
	}
	
	public TransportLayer addToDataLayer(String inKeyField, String inValueField){
		HopperJsonStatic.putKeyValueStringsForJsonObject(dataLayer.toJson(), inKeyField, inValueField);
		return this;
	}
	public TransportLayer addToDataLayer(String inKeyField, JSONArray inJsonArray){
		dataLayer.add(inKeyField, inJsonArray);		
		return this;
	}
	public TransportLayer addToDataLayer(String inKeyField, JSONObject inJson){
		dataLayer.add(inKeyField, inJson);		
		return this;
	}
	
	public TransportLayer transportLayer(){
		return this;
	}
	
	public TransportLayer deviceId(String inValue){
		HopperJsonStatic.putKeyValueStringsForJsonObject(transportLayer_json, "deviceId", inValue);
		return this;
	}
	
	public TransportLayer userId(String inValue){
		HopperJsonStatic.putKeyValueStringsForJsonObject(transportLayer_json, "userId", inValue);
		return this;
	}
	
	public TransportLayer securityToken(String inValue){
		HopperJsonStatic.putKeyValueStringsForJsonObject(transportLayer_json, "securityToken", inValue);
		return this;
	}
	
	public TransportLayer transactionId(String inValue){
		HopperJsonStatic.putKeyValueStringsForJsonObject(transportLayer_json, "transactionId", inValue);
		return this;
	}
	
	public TransportLayer setDefault(){
		userId = HopperJsonStatic.getStringFromKeyForJsonObject(transportLayer_json, "userId");
		deviceId = HopperJsonStatic.getStringFromKeyForJsonObject(transportLayer_json, "deviceId");
		securiryToken = HopperJsonStatic.getStringFromKeyForJsonObject(transportLayer_json, "securiryToken");
		return this;
	}
	
	public DataLayer dataLayer(){
		return dataLayer;
	}
	
	public DataLayer dataLayer(DataLayer inDataLayer){
		dataLayer = inDataLayer;
		return dataLayer;
	}
	
	
	public RoutingLayer routingLayer(){
		return routingLayer;
	}
}

























