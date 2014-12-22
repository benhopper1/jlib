package hopper.library.communication.v003;

import org.json.JSONObject;
import com.example.hopperlibrary.HopperJsonStatic;

public class RoutingLayer {
	public enum TypesEnum {
		   setupToServer(0),
		   setupToClient(1),
		   commandForClient(2),
		   commandForServer(3),
		   toFamilyDevice(4),
		   toForeignDevice(5),
		   useFilter(6),
		   requestClientCredentials(7),
		   tokenToTokenUseFilter(8),
		   transactionToServer(9),
		   transactionToClient(10),
		   transactionToToken(11),
		   
		   
		   deviceToDeviceUseFilter(100)
   ;

		   private int value;

		   private TypesEnum(int inValue) {
		      this.value = inValue;
		   }

		    public int getValue() {
		        return this.value;
		    }
	}	
	
	private JSONObject routingLayer_json;
	
	public void createRoutingLayer(JSONObject inRoutingLayer_json){
		routingLayer_json = inRoutingLayer_json;			
	}
	
	public RoutingLayer(JSONObject inRoutingLayer_json){
		routingLayer_json = inRoutingLayer_json;
	}
	
	public RoutingLayer(TypesEnum inRoutingType){
		routingLayer_json = new JSONObject();		
		HopperJsonStatic.putKeyValueStringsForJsonObject(routingLayer_json, "type", inRoutingType.toString());		
	}

	public RoutingLayer(){
		routingLayer_json = new JSONObject();		
	}
	
	public void addField(String inKey, String inValue){
		HopperJsonStatic.putKeyValueStringsForJsonObject(routingLayer_json, inKey, inValue);
	}
	
	public String getField(String inKey){
		return HopperJsonStatic.getStringFromKeyForJsonObject(routingLayer_json, inKey);
	}
	
	public String toString(){
		return routingLayer_json.toString();
	}
	
	public JSONObject toJson(){
		return routingLayer_json;
	}
	
	public RoutingLayer get(String inKey, String inValue){
		HopperJsonStatic.putKeyValueStringsForJsonObject(routingLayer_json, inKey, inValue);
		return this;
	}
	
	public RoutingLayer type(TypesEnum inValue){
		HopperJsonStatic.putKeyValueStringsForJsonObject(routingLayer_json, "type", inValue.toString());
		return this;
	}	
	
	public RoutingLayer type(String inValue){
		HopperJsonStatic.putKeyValueStringsForJsonObject(routingLayer_json, "type", inValue);
		return this;
	}
	public RoutingLayer command(String inValue){
		HopperJsonStatic.putKeyValueStringsForJsonObject(routingLayer_json, "command", inValue);
		return this;
	}
	public RoutingLayer subCommand(String inValue){
		HopperJsonStatic.putKeyValueStringsForJsonObject(routingLayer_json, "subCommand", inValue);
		return this;
	}
	public RoutingLayer toDeviceId(String inValue){
		HopperJsonStatic.putKeyValueStringsForJsonObject(routingLayer_json, "toDeviceId", inValue);
		return this;
	}
	
	public RoutingLayer add(String inKey, String inValue){
		HopperJsonStatic.putKeyValueStringsForJsonObject(routingLayer_json, inKey, inValue);
		return this;
	}
	
	public RoutingLayer reset(){
		routingLayer_json = new JSONObject();
		return this;
	}
	
	public RoutingLayer clean(){
		routingLayer_json = new JSONObject();
		return this;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
