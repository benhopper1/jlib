package hopper.library.communication;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.example.hopperCommServicelibrary.HopperFilterSet;
import com.example.hopperlibrary.HopperAsync;
import com.example.hopperlibrary.HopperCommunicationInterface;
import com.example.hopperlibrary.HopperJsonStatic;
import com.example.hopperlibrary.console;

import de.roderick.weberknecht.WebSocket;
import de.roderick.weberknecht.WebSocketEventHandler;
import de.roderick.weberknecht.WebSocketException;
import de.roderick.weberknecht.WebSocketMessage;

public class WebSocketCommService {
	static private HashMap<String, WebSocketCommService> instanceHashMap = new  HashMap<String, WebSocketCommService>();
	
	private boolean isConnected = false;
	private String uriString;
	private WebSocket websocket;
	
	static public WebSocketCommService getMaybeCreate(String inName, String inHost, int inPort, String inStub){
		WebSocketCommService tmpInstance = null;
		tmpInstance = instanceHashMap.get(inName);
		if(tmpInstance == null){
			tmpInstance = new WebSocketCommService(inHost, inPort, inStub);
			instanceHashMap.put(inName, tmpInstance);
		}		
		return tmpInstance;
	}
	
	static public WebSocketCommService getInstance(String inName){
		 return instanceHashMap.get(inName);
	}
	
	static public void destroy(String inName){				
		instanceHashMap.remove(inName);	
	}

	private  WebSocketCommService(String inHost, int inPort, String inStub){
		this.uriString = "ws://" + inHost + ":" + String.valueOf(inPort) + "/" + inStub;		
	}
	
	private void startup(){
		
	}
	
	
	/*-------------------------------------------------------------------------------------------------------------
	 * 
	 * EVENTS SECTION
	 * 
	 * -------------------------------------------------------------------------------------------------------------
	 */
	
	
	//--------OnOpened callback-------------------------------------------------------------
	private ArrayList<OnOpenedListener> onOpenedListenerArrayList = new ArrayList<OnOpenedListener>();
	
	public interface OnOpenedListener{
	    public void onOpened(Object...objects);
	}
    public void setOnOpenedListener(OnOpenedListener listener) {
    	onOpenedListenerArrayList.add(listener);
    }
    private void reportOnOpened(Object...objects){
    	for(OnOpenedListener listener : onOpenedListenerArrayList){
    		listener.onOpened(objects);
        }
    }
  //-
  //--------OnClosed callback-------------------------------------------------------------
    private ArrayList<OnClosedListener> onClosedListenerArrayList = new ArrayList<OnClosedListener>();
  	
  	public interface OnClosedListener{
  	    public void onClosed(Object...objects);
  	}
	public void setOnClosedListener(OnClosedListener listener) {
		onClosedListenerArrayList.add(listener);
    }
	private void reportOnClosed(Object...objects){
	 	for(OnClosedListener listener : onClosedListenerArrayList){
	  		listener.onClosed(objects);
	    }
	}
 //-
	//--------OnMessaged callback-------------------------------------------------------------
    private ArrayList<OnMessagedListener> onMessagedListenerArrayList = new ArrayList<OnMessagedListener>();
  	
  	public interface OnMessagedListener{
  	    public void onMessaged(Object...objects);
  	}
	public void setOnMessagedListener(OnMessagedListener listener) {
		onMessagedListenerArrayList.add(listener);
    }
	private void reportOnMessaged(Object...objects){
	 	for(OnMessagedListener listener : onMessagedListenerArrayList){
	  		listener.onMessaged(objects);
	    }
	}
 //-
	
	//--------OnErrored callback-------------------------------------------------------------
    private ArrayList<OnErroredListener> onErroredListenerArrayList = new ArrayList<OnErroredListener>();
  	
  	public interface OnErroredListener{
  	    public void onErrored(Object...objects);
  	}
	public void setOnErroredListener(OnErroredListener listener) {
		onErroredListenerArrayList.add(listener);
    }
	private void reportOnErrored(Object...objects){
	 	for(OnErroredListener listener : onErroredListenerArrayList){
	  		listener.onErrored(objects);
	    }
	}
 
	public boolean connect(){
		boolean retVal = false;
		try {
		    URI url = new URI(uriString);
		    websocket = new WebSocket(url);

		    // Register Event Handlers
		    websocket.setEventHandler(new WebSocketEventHandler() {
		            public void onOpen()
		            {
		                console.log("--open");
		            }

		            
		            public void onClose()
		            {
		            	reportOnClosed(WebSocketCommService.this);
		            }

		            public void onPing() {}
		            public void onPong() {}

					@Override
					public void onMessage(WebSocketMessage inMessage) {
						console.log("onMessage:"+inMessage.getText());
						if(isConnected){
							console.log("received Message and am connected---------------");
							reportOnMessaged(WebSocketCommService.this, inMessage.getText());
						}else{
							
							//--handle server request/response only to get connected
							JSONObject fromServer_json = HopperJsonStatic.createJsonObjectFromJsonString(inMessage.getText());
							
							if(HopperJsonStatic.getStringFromKeyForJsonObject(fromServer_json, "command" ).equalsIgnoreCase("serverRequest") ){
								console.log("is a server rq");
									handleServerRequest(fromServer_json);	
							}
							
							if(HopperJsonStatic.getStringFromKeyForJsonObject(fromServer_json, "command" ).equalsIgnoreCase("serverResponse")){
								console.log("is a server rsp");
								handleServerResponse(fromServer_json);	
							}
							
													
						}
						
					}

					@Override
					public void onError(IOException inException) {
						reportOnErrored(WebSocketCommService.this, inException);
						
					}
		        });

		    // Establish WebSocket Connection
		    HopperAsync has = new HopperAsync(HopperAsync.EthreadType.WaitOnBackgroundThread){
		    	@Override
		    	public String onNewThread(String ... inString){				
		    		websocket.connect();		    		
		    		return "";
		    	}
		    };				
		    String x = has.run("");
		    

		    
		}
		catch (WebSocketException wse) {
		    wse.printStackTrace();
		}
		catch (URISyntaxException use) {
		    use.printStackTrace();
		}
		

		
		
		return retVal;
	}
	
	
	
	
	protected void handleServerRequest(JSONObject inJsonMessage){
		console.log("handleServerRequest");
		if(HopperJsonStatic.getStringFromKeyForJsonObject(inJsonMessage, "type" ).equalsIgnoreCase("identifyRequest")){
			HopperAsync has = new HopperAsync(HopperAsync.EthreadType.WaitOnBackgroundThread){
				@Override
				public String onNewThread(String ... inString){
					console.log("sending getCredentialPacket");
					websocket.send(getCredentialPacket());
					console.log("Sending -- handleServerRequest :"+getCredentialPacket());					
					return "";
				}
			};				
			String x = has.run("");
			
			
		}		
	}
	
	protected void handleServerResponse(JSONObject inJsonMessage){
		console.log("handleServerResponse");
		if(HopperJsonStatic.getStringFromKeyForJsonObject(inJsonMessage, "type" ).equalsIgnoreCase("error")){
			console.log("ERROR -- handleServerResponse");
		}
		
		if(HopperJsonStatic.getStringFromKeyForJsonObject(inJsonMessage, "type" ).equalsIgnoreCase("welcomePackage")){
			console.log("your in -- handleServerResponse");
			//-- change to connected state,
			this.isConnected = true;
			this.reportOnOpened(WebSocketCommService.this, inJsonMessage.toString());
		}
		
	}
	
	
	protected String getCredentialPacket(){
		//-- HEADER -------------------------------
		JSONObject header_json = new JSONObject();		
		HopperJsonStatic.putKeyValueStringsForJsonObject(header_json, "command", "clientResponse");
		HopperJsonStatic.putKeyValueStringsForJsonObject(header_json, "type", "identifyResponsePackage");		
		//-- INNER DATA ---------------------------
		JSONObject inner_json = new JSONObject();
		HopperJsonStatic.putKeyValueStringsForJsonObject(inner_json, "userId", String.valueOf(HopperCommunicationInterface.get("COMM").userId));
		HopperJsonStatic.putKeyValueStringsForJsonObject(inner_json, "deviceId", String.valueOf(HopperCommunicationInterface.get("COMM").deviceId));
		HopperJsonStatic.putKeyValueStringsForJsonObject(inner_json, "deviceCaption", "Android App Device");
		//----COMBINE SECTIONS
		HopperJsonStatic.putKeyValueStringObjectForJsonObject(header_json, "data", inner_json);
		
		return header_json.toString();
	}
	
	public void send(final String inPayloadString){
		console.log("WebSocketCommService.send"+inPayloadString);		
		HopperAsync has = new HopperAsync(HopperAsync.EthreadType.WaitOnBackgroundThread){
			@Override
			public String onNewThread(String ... inString){				
				websocket.send(inPayloadString);									
				return "";
			}
		};				
		String x = has.run("");	
			
	}						
	
	
//	var message = 
//		{
//			'command'   	:'clientRequest',
//			'type'			:'dev2dev_addMessage',
//			'fromDeviceId'	:'128',
//			'toDeviceId'		:'166',
//			'data'			:
//				{
//					'commMessage':'sendMeYourStatus',
//					'fromDeviceId':'128'
//				}
//
//		};
	
	public void sendMessage(final String toDeviceId, final String inMessagePayloadString){
		console.log("WebSocketCommService.send"+inMessagePayloadString);		
		HopperAsync has = new HopperAsync(HopperAsync.EthreadType.WaitOnBackgroundThread){
			@Override
			public String onNewThread(String ... inString){
				
				//-- HEADER -------------------------------
				JSONObject header_json = new JSONObject();		
				HopperJsonStatic.putKeyValueStringsForJsonObject(header_json, "command", "clientRequest");
				HopperJsonStatic.putKeyValueStringsForJsonObject(header_json, "type", "dev2dev_addMessage");
				HopperJsonStatic.putKeyValueStringsForJsonObject(header_json, "fromDeviceId", String.valueOf(HopperCommunicationInterface.get("COMM").deviceId));	
				HopperJsonStatic.putKeyValueStringsForJsonObject(header_json, "toDeviceId", toDeviceId);	
				
				//-- INNER DATA ---------------------------
				JSONObject inner_json = HopperJsonStatic.createJsonObjectFromJsonString(inMessagePayloadString);				
				HopperJsonStatic.putKeyValueStringsForJsonObject(inner_json, "fromDeviceId", String.valueOf(HopperCommunicationInterface.get("COMM").deviceId));				
				HopperJsonStatic.putKeyValueStringObjectForJsonObject(header_json, "data", inner_json);						
				websocket.send(header_json.toString());									
				return "";
			}
		};				
		String x = has.run("");	
		
	}
	
	
	
	
	
	
	
	

}
