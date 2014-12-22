package hopper.library.communication.v003;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.json.JSONObject;

import com.example.hopperlibrary.HopperAsync;
import com.example.hopperlibrary.HopperJsonStatic;
import com.example.hopperlibrary.console;

import de.roderick.weberknecht.WebSocket;
import de.roderick.weberknecht.WebSocketEventHandler;
import de.roderick.weberknecht.WebSocketException;
import de.roderick.weberknecht.WebSocketMessage;

/*
 * SINGLETON-----------------------------------------------------
 */
public class WebSocketMobileService {
	static private WebSocketMobileService theInstance = new WebSocketMobileService();
	private boolean isConnected = false;
	private String uriString;
	private WebSocket websocket;
	private JSONObject transportLayer_json;
	
	private WebSocketMobileService(){
		//--self medicate:
		this.setOnMessagedListener(new OnMessagedListener(){
			@Override
			public void onMessaged(Object... inObjects){
				console.log("onMessage callback --- self medicated!!" + inObjects[1].toString());				
				JSONObject fromServer_json = HopperJsonStatic.createJsonObjectFromJsonString((String) inObjects[1]);
				String transactionId = HopperJsonStatic.getStringFromKeyForJsonObject(fromServer_json, "transactionId");
				console.log("Trying to match:" + transactionId);
				OnTransactionListener listener = OnTransactionHashMap.get(transactionId);
				if(listener != null){
					console.log("MATCH on listener Hash");					
					
					JSONObject dataLayer_json = HopperJsonStatic.getJsonObjectFromKey(fromServer_json, "dataLayer");
					inObjects[2] = (Object) dataLayer_json;
					listener.onTransaction(inObjects);
				}
			}
		});
	}
	
	static public WebSocketMobileService getInstance(){
		return theInstance;
	}

	
	
	
	
	//############ E V E N T S ##############################################################################################
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
	//-
	
	//--------OnTransaction callback-------------------------------------------------------------
    //private ArrayList<OnTransactionListener> onTransactionListenerArrayList = new ArrayList<OnTransactionListener>();
    private HashMap<String, OnTransactionListener> OnTransactionHashMap = new HashMap<String, OnTransactionListener>();
  	
  	public interface OnTransactionListener{
  	    public void onTransaction(Object...objects);
  	}
	/*public void setOnTransactionListener(OnTransactionListener listener) {
		onTransactionListenerArrayList.add(listener);
    }*/
	public void sendAsTransaction(TransportLayer inTransportLayer, OnTransactionListener listener){
		String transactionId = UUID.randomUUID().toString();
		inTransportLayer.setTransactionId(transactionId);
		OnTransactionHashMap.put(transactionId, listener);
		theInstance.websocket.send(inTransportLayer.toString());
		
		
		/*console.log("sendAsTransaction");
		String transactionId = UUID.randomUUID().toString();
		OnTransactionHashMap.put(transactionId, listener);
		console.log("added to OnTransactionHashMap" + transactionId);
		
		JSONObject toServer_json = HopperJsonStatic.createJsonObjectFromJsonString(inDataString);
		
		JSONObject trans_json = this.loadTransportLayerWithPayload(toServer_json);
		
		HopperJsonStatic.putKeyValueStringsForJsonObject(trans_json, "transactionId", transactionId);	
		
		theInstance.websocket.send(trans_json.toString());*/
		//this.send(toServer_json.toString());
    }
	
	/*private void reportOnTransaction(Object...objects){
	 	for(OnTransactionListener listener : onTransactionListenerArrayList){
	  		listener.onTransaction(objects);
	    }
	}*/
	
	
	
	
	
	//-	
	
	
	
	static public void connect(String inHost, int inPort, String inStub,final JSONObject inConnectionData_json){
		theInstance.uriString = "ws://" + inHost + ":" + String.valueOf(inPort) + "/" + inStub;
		boolean retVal = false;
		try {
		    URI url = new URI(theInstance.uriString);
		    theInstance.websocket = new WebSocket(url);
		    

		    // Register Event Handlers
		    theInstance.websocket.setEventHandler(new WebSocketEventHandler() {
		            public void onOpen(){
		                console.log("--open");
		                theInstance.websocket.send(inConnectionData_json.toString());
		                //theInstance.send(inConnectionData_json.toString());
		                //theInstance.isConnected = true;
		                //theInstance.reportOnOpened(theInstance);
		            }

		            
		            public void onClose(){
		            	theInstance.isConnected = false;
		            	theInstance.reportOnClosed(theInstance);
		            }

		            public void onPing() {}
		            public void onPong() {}

					@Override
					public void onMessage(WebSocketMessage inMessage){
						console.log("onMessage:"+inMessage.getText());	
						
						if(theInstance.isConnected){
							console.log("received Message and am connected---------------");
							theInstance.reportOnMessaged(theInstance, inMessage.getText());
						}else{
							// not connected
							TransportLayer TL_fromServer = TransportLayer.createTransportLayer(inMessage.getText());
							RoutingLayer RL_fromServer = new RoutingLayer(TL_fromServer.getRoutingLayer());							
							console.log("not connected but received message from server:" + inMessage.getText());
							
							console.line("from routing stuff");
							console.log(RL_fromServer.getField("type"));
							console.log(RL_fromServer.getField("command"));
							console.line();
							//server connected the client------
							if(RL_fromServer.getField("type").equalsIgnoreCase(RoutingLayer.TypesEnum.commandForClient.toString()) && RL_fromServer.getField("command").equalsIgnoreCase("connectedToServer")){
								theInstance.isConnected = true;
								console.log("server connected");
								if(RL_fromServer.getField("subCommand").equalsIgnoreCase("changeDeviceId")){
									//TODO: update client deviceId HERE----------
								}
								theInstance.reportOnOpened(theInstance);
							}
							
							/*JSONObject fromServer_json = HopperJsonStatic.createJsonObjectFromJsonString(inMessage.getText());
							console.log("result of string bool:" + HopperJsonStatic.getStringFromKeyForJsonObject(fromServer_json, "isConnected" ));
							if(HopperJsonStatic.getStringFromKeyForJsonObject(fromServer_json, "isConnected" ).equalsIgnoreCase("true")){
								console.log("server connected");
								theInstance.isConnected = true;
								theInstance.updateTransportLayer(fromServer_json);
								theInstance.reportOnOpened(theInstance);
							}*/
																			
						}
						
					}

					@Override
					public void onError(IOException inException) {
						theInstance.reportOnErrored(theInstance, inException);
						
					}
		        });

		    // Establish WebSocket Connection
		    HopperAsync has = new HopperAsync(HopperAsync.EthreadType.WaitOnBackgroundThread){
		    	@Override
		    	public String onNewThread(String ... inString){				
		    		theInstance.websocket.connect();		    		
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
		
	}
    
	public String send(final String inJsonAsString){
		console.log("send..." + inJsonAsString);
		String result = null;
		
		theInstance.websocket.send(inJsonAsString);
		
		return null;
	}
	
	public JSONObject createTransportLayer(String inUserId, String inDeviceId, String inSecurityToken, JSONObject inDataLayer){
		JSONObject result_json = new JSONObject();
		HopperJsonStatic.putKeyValueStringsForJsonObject(result_json, "userId", inUserId);
		HopperJsonStatic.putKeyValueStringsForJsonObject(result_json, "deviceId", inDeviceId);
		HopperJsonStatic.putKeyValueStringsForJsonObject(result_json, "securityToken", inSecurityToken);
		//HopperJsonStatic.putKeyValueStringsForJsonObject(result_json, "transactionId", inTransactionId);	
		HopperJsonStatic.putKeyValueStringObjectForJsonObject(result_json, "dataLayer", inDataLayer) ;
		this.transportLayer_json = result_json;
		return result_json;
	}
	
	public JSONObject loadTransportLayerWithPayload(JSONObject inPayload){
		HopperJsonStatic.putKeyValueStringObjectForJsonObject(this.transportLayer_json, "dataLayer", inPayload);
		return this.transportLayer_json;
	}
	
	public void updateTransportLayer(JSONObject inTransportLayer){
		//map cross
		HopperJsonStatic.putKeyValueStringsForJsonObject(this.transportLayer_json, "userId", HopperJsonStatic.getStringFromKeyForJsonObject(inTransportLayer, "userId"));
		HopperJsonStatic.putKeyValueStringsForJsonObject(this.transportLayer_json, "deviceId", HopperJsonStatic.getStringFromKeyForJsonObject(inTransportLayer, "deviceId"));
		HopperJsonStatic.putKeyValueStringsForJsonObject(this.transportLayer_json, "securityToken", HopperJsonStatic.getStringFromKeyForJsonObject(inTransportLayer, "securityToken"));
		
		
	}
	
	
}
