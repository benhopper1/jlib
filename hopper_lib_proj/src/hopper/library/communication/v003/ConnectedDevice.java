package hopper.library.communication.v003;
/*
 * This is for specific case ONLY: DEMO!!!,,, connected device is set only to last browser with a backstack history...
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Stack;

import org.json.JSONArray;
import org.json.JSONObject;

import hopper.library.communication.v003.HopperFilter.OnFilterPassListener;
import hopper.library.communication.v003.WebSocketService.OnTransactionListener;

import com.example.hopperlibrary.HopperJsonStatic;
import com.example.hopperlibrary.console;

public class ConnectedDevice {
	static public String primaryDeviceTokenId = null;
	static public Stack<String> backstack = new Stack<String>();
	
   //-------events----------------------------------------------------------------------------------------------------------------------
	static int keyCount = 0;
	static private HashMap<String,OnChangeListener> onChangeListenerHashMap = new HashMap<String,OnChangeListener>();
	
	static public interface OnChangeListener{
	    public void onChange(Object...objects);
	}
	static public String setOnChangeListener(OnChangeListener listener) {
    	onChangeListenerHashMap.put(String.valueOf(keyCount), listener);
    	int resultKey = keyCount;
    	keyCount++;
    	return String.valueOf(resultKey);
    }
	static public void removeOnChangeListener(String inKey) {
		onChangeListenerHashMap.remove(inKey);
    }
	
	static private void reportOnChange(Object...objects){
		//ArrayList al = onChangeListenerHashMap.v
    	for(OnChangeListener listener : onChangeListenerHashMap.values()){
    		listener.onChange(objects);
        }
    }
    //-----------------------------------------------------

	public ConnectedDevice(){
		//creadintials
		TransportLayer transactionTransportLayer = TransportLayer.createTransportLayer();		
		transactionTransportLayer.routingLayer()
				.type(RoutingLayer.TypesEnum.transactionToServer)
				.add("command", "returnAllCredentialsForUser")
		;

		console.log("ConnectedDevice Credential request,,,sending:" + transactionTransportLayer.toString());
		WebSocketService.getInstance("mobileService").sendAsTransaction(transactionTransportLayer, new OnTransactionListener() {
			@Override
			public void onTransaction(Object... objects) {
				console.log("Credentials Transaction type done and back");
				console.log(":" + (String) objects[1]);
				TransportLayer transFromServer = TransportLayer.createTransportLayer((String) objects[1]);
				JSONObject dataLayer_json = transFromServer.getDataLayerAsDataLayer().toJson();
				JSONArray credentials_jsonArray = HopperJsonStatic.getJSONArray(dataLayer_json, "credentialsPackage");
				for(int i = 0; i < credentials_jsonArray.length(); i++){
					JSONObject credential_json = HopperJsonStatic.getJsonObjectFromJsonArrayByIndex(credentials_jsonArray, i);
					if(HopperJsonStatic.getStringFromKeyForJsonObject(credential_json, "deviceType").equalsIgnoreCase("desktopBrowser")){						
						primaryDeviceTokenId = HopperJsonStatic.getStringFromKeyForJsonObject(credential_json, "deviceTokenId");
						ConnectedDevice.reportOnChange();
						console.log("setting connected device to:" + primaryDeviceTokenId);
						if(!(backstack.contains(primaryDeviceTokenId))){
							console.log("inserting into stack:" + primaryDeviceTokenId);
							backstack.push(primaryDeviceTokenId);
						}else{
							console.log("already in stack:" + primaryDeviceTokenId);
						}						
					}					
				}
			}

		});		
		
		//-------------------------------------------------------------------------------------------------------------------------
		FilterSetProcessor.getMaybeCreate("filterProcessor_0", WebSocketService.getInstance("mobileService"))       
	    	.add("ConnectedDevice", "filter", "advise", new OnFilterPassListener(){
				@Override
				public void onFilterPass(Object... objects){					
					console.log("------>onFilterPass advise FROM ConnectedDevice()" );
					TransportLayer transFromServer = TransportLayer.createTransportLayer((String) objects[1]);
					String action = HopperJsonStatic.getStringFromKeyForJsonObject(transFromServer.getDataLayerAsDataLayer().toJson(),"action");
					if(action.equalsIgnoreCase("login")){
						console.log("----->ConnectedDevice login");
						String deviceType = HopperJsonStatic.getStringFromKeyForJsonObject(transFromServer.getDataLayerAsDataLayer().toJson(),"deviceType");
						if(deviceType.equalsIgnoreCase("desktopBrowser")){							
							primaryDeviceTokenId = HopperJsonStatic.getStringFromKeyForJsonObject(transFromServer.getRoutingLayer(),"fromDeviceTokenId");
							ConnectedDevice.reportOnChange();
							console.log("setting connected device to:" + primaryDeviceTokenId);
							if(!(backstack.contains(primaryDeviceTokenId))){
								console.log("inserting into stack:" + primaryDeviceTokenId);
								backstack.push(primaryDeviceTokenId);
							}else{
								console.log("already in stack:" + primaryDeviceTokenId);
							}						
						}
					}
					
					if(action.equalsIgnoreCase("logout")){
						String deviceType = HopperJsonStatic.getStringFromKeyForJsonObject(transFromServer.getDataLayerAsDataLayer().toJson(),"deviceType");
						if(deviceType.equalsIgnoreCase("desktopBrowser")){
							String deviceTokenId = HopperJsonStatic.getStringFromKeyForJsonObject(transFromServer.getRoutingLayer(),"fromDeviceTokenId");
							Iterator<String> iter = backstack.iterator();
							while (iter.hasNext()){
							    if(iter.next().equalsIgnoreCase(deviceTokenId)){                
							        iter.remove();
							        break;
							    }
							}
												
							if(backstack.empty()){
								primaryDeviceTokenId = null;
								console.log("primaryDeviceTokenId set to NULL, backstack is empty:" + primaryDeviceTokenId);
							}else{
								primaryDeviceTokenId = backstack.peek();
								console.log("primaryDeviceTokenId set to backstack:" + primaryDeviceTokenId);
							}
							
							ConnectedDevice.reportOnChange();
						}
					}
				}
	    	});
	}
	
	
	
}


























