package hopper.library.communication.v003;






import hopper.library.communication.v003.RoutingLayer.TypesEnum;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.json.JSONObject;

import android.annotation.TargetApi;
import android.os.Build;
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
public class WebSocketService {	
	private boolean isConnected = false;
	private String uriString;
	private WebSocket websocket;
	private JSONObject transportLayer_json;
	private String connectedDeviceTokenId;
	
	static private HashMap<String, WebSocketService> webSocketServiceHash = new HashMap<String, WebSocketService>();
	
	static public WebSocketService getInstance(String inName){
		return webSocketServiceHash.get(inName);
	}
	
	@TargetApi(Build.VERSION_CODES.GINGERBREAD) 
	public WebSocketService(String inName){		
		webSocketServiceHash.put(inName, this);
		//--self medicate:
		this.setOnMessagedListener(new OnMessagedListener(){
			@Override
			public void onMessaged(Object... inObjects){
				console.log("onMessage callback --- self medicated!!" + inObjects[1].toString());
				TransportLayer transportLayerFromServer = TransportLayer.createTransportLayer((String) inObjects[1]);
				//router type
				if(transportLayerFromServer.routingLayer().getField("type").equalsIgnoreCase("transactionToToken")){
					console.log("transactionToToken->->->");
					TransactionRequest.process(transportLayerFromServer.routingLayer().getField("command"), transportLayerFromServer);
				}
				//reportOnTransactionSeriesToToken
				if(transportLayerFromServer.routingLayer().getField("type").equalsIgnoreCase("transactionSeriesToToken")){
					console.log("transactionToToken->->->");
					//reportOnTransactionSeriesToToken(transportLayerFromServer, transportLayerFromServer.routingLayer().getField("command"), transportLayerFromServer.routingLayer().getField("stage"), transportLayerFromServer.routingLayer().getField("frame"));
					reportOnTransactionSeriesToToken(transportLayerFromServer, transportLayerFromServer.routingLayer().getField("transactionSeriesId"));
				}
				
				String transactionId = transportLayerFromServer.getTransactionId();
				
				console.log("Trying to match: transactionId" + transactionId);
				OnTransactionListener listener = OnTransactionHashMap.get(transactionId);
				if(transactionId.isEmpty()){console.log("isEmpty");}
				if(listener != null){
					OnTransactionHashMap.remove(transactionId);
					console.log("MATCH on listener Hash");
					Object[] newObjects = merge((Object) TransportLayer.createTransportLayer(((String)inObjects[1])), inObjects);					
					listener.onTransaction(newObjects);
				}
			}
		});
	}
	
	public Object[] merge(Object o, Object... arr) {
	    Object[] newArray = new Object[arr.length + 1];
	    newArray[arr.length] = o;
	    System.arraycopy(arr, 0, newArray, 0, arr.length);

	    return newArray;
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
	
	//--------OnTransactionSeriesToToken callback-------------------------------------------------------------
	/*
	 * param0	TransportLayer		incoming
	 * para1	String				transactionSeriesId
	 */
    private ArrayList<OnTransactionSeriesToTokenListener> onTransactionSeriesToTokenListenerArrayList = new ArrayList<OnTransactionSeriesToTokenListener>();
  	
  	public interface OnTransactionSeriesToTokenListener{
  	    public void onTransactionSeriesToToken(Object...objects);
  	}
	public void setOnTransactionSeriesToTokenListener(OnTransactionSeriesToTokenListener listener) {
		onTransactionSeriesToTokenListenerArrayList.add(listener);
    }
	private void reportOnTransactionSeriesToToken(Object...objects){
	 	for(OnTransactionSeriesToTokenListener listener : onTransactionSeriesToTokenListenerArrayList){
	 		console.log("listen onmes size:" + onTransactionSeriesToTokenListenerArrayList.size());
	  		listener.onTransactionSeriesToToken(objects);
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
	 		console.log("listen onmes size:" + onMessagedListenerArrayList.size());
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
    private HashMap<String, OnTransactionListener> OnTransactionHashMap = new HashMap<String, OnTransactionListener>();
  	
	/**
	 * @param objects[0] WebSocketService instance
	 * @param objects[1] String containing all transmitted message data 
	 * @param objects[2] TransportLayer that encapsulates all incoming data
	 * 
	 */
  	public interface OnTransactionListener{
  	    public void onTransaction(Object...objects);
  	}
  	
	public void sendAsTransaction(TransportLayer inTransportLayer, OnTransactionListener listener){
		String transactionId = UUID.randomUUID().toString();
		inTransportLayer.setTransactionId(transactionId);
		OnTransactionHashMap.put(transactionId, listener);
		console.log("sendAsTransaction:" + inTransportLayer.toString());
		this.websocket.send(inTransportLayer.toString());
		inTransportLayer.reuse();
		//inTransportLayer.;
    }
	
	
	public void connect(String inHost, int inPort, String inStub,final JSONObject inConnectionData_json){
		this.uriString = "ws://" + inHost + ":" + String.valueOf(inPort) + "/" + inStub;
		boolean retVal = false;
		try {
		    URI url = new URI(this.uriString);
		    this.websocket = new WebSocket(url);
		    

		    // Register Event Handlers
		    this.websocket.setEventHandler(new WebSocketEventHandler() {
		            public void onOpen(){
		                console.log("--open");
		                WebSocketService.this.websocket.send(inConnectionData_json.toString());		                
		            }

		            
		            public void onClose(){
		            	WebSocketService.this.isConnected = false;
		            	WebSocketService.this.reportOnClosed(this);
		            }

		            public void onPing() {}
		            public void onPong() {}

					@Override
					public void onMessage(WebSocketMessage inMessage){
						console.log("onMessage:"+inMessage.getText());	
						
						if(WebSocketService.this.isConnected){
							console.log("received Message and am connected---------------");
							WebSocketService.this.reportOnMessaged(WebSocketService.this, inMessage.getText());
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
								WebSocketService.this.isConnected = true;
								console.log("server connected");
								if(RL_fromServer.getField("subCommand").equalsIgnoreCase("changeDeviceId")){
									console.log("changeing devId from/to" + TransportLayer.deviceId + "/" + RL_fromServer.getField("newDeviceId"));
									TransportLayer.deviceId = RL_fromServer.getField("newDeviceId");
								}
								WebSocketService.this.reportOnOpened(WebSocketService.this);
							}
							
																		
						}
						
					}

					@Override
					public void onError(IOException inException) {
						WebSocketService.this.reportOnErrored(WebSocketService.this, inException);
						
					}
		        });

		    // Establish WebSocket Connection
		    HopperAsync has = new HopperAsync(HopperAsync.EthreadType.WaitOnBackgroundThread){
		    	@Override
		    	public String onNewThread(String ... inString){				
		    		WebSocketService.this.websocket.connect();		    		
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
		
		this.websocket.send(inJsonAsString);
		
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
	
	public String getConnectedDeviceTokenId(){
		return this.connectedDeviceTokenId;
	}
	
	public void setConnectedDeviceTokenId(String inId){
		this.connectedDeviceTokenId = inId;
	}
	
	public void sendCommand(String toDeviceTokenId, String inCommand, DataLayer inDataLayer){
		TransportLayer commandTransportLayer = TransportLayer.createTransportLayer();
		commandTransportLayer.routingLayer()
			.type("tokenToTokenUseFilter")
			.add("toDeviceTokenId", toDeviceTokenId)
			.add("filterKey", "filter")
			.add("filterValue", inCommand)			
		;
		commandTransportLayer.dataLayer(inDataLayer);
		this.send(commandTransportLayer.toString());
		commandTransportLayer.reuse();
	}
	
	public void returnTransaction(TransportLayer inOriginalTransportLayer, DataLayer inNewDataLayer){
		inOriginalTransportLayer.routingLayer()
			.reset()
			.add("type", "transactionToToken")
			.add("stage", "targetOut")
		;
		inOriginalTransportLayer.dataLayer(inNewDataLayer);
		this.send(inOriginalTransportLayer.toString());
		inOriginalTransportLayer.reuse();		
	}
	
	public void returnTransaction(TransportLayer inOriginalTransportLayer){
		DataLayer inNewDataLayer = new DataLayer();
		this.returnTransaction(inOriginalTransportLayer, inNewDataLayer);
	}
	
	public void returnTransaction(Object inObject){		
		this.returnTransaction((TransportLayer)inObject);
	}
	
	
	public void sendTransactionToDeviceToken(OutgoingTransactionRequest inOutgoingTransactionRequest, OnTransactionListener inListener){
		sendAsTransaction(inOutgoingTransactionRequest.toTransportLayer(), inListener);
	}
	
	
	
	
	
	
	
	
}
