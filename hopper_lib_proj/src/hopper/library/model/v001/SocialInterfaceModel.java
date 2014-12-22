package hopper.library.model.v001;


import java.util.ArrayList;

import hopper.cache.ImageCache;
import hopper.library.communication.WebSocketCommService;
import hopper.library.object.SocialInterfaceNodeObject;
import hopper.library.object.UserObject;

import org.json.JSONObject;


import android.annotation.SuppressLint;
import android.app.Activity;

import com.example.hopperCommServicelibrary.v001.HopperFilter.OnExecuteListener;
import com.example.hopperCommServicelibrary.v001.HopperFilterSet;
import com.example.hopperCommServicelibrary.v001.HopperFilter;


import com.example.hopperlibrary.HopperAudioMachine;
import com.example.hopperlibrary.HopperCommunicationInterface;
import com.example.hopperlibrary.HopperDataset;
import com.example.hopperlibrary.HopperInstanceHash;
import com.example.hopperlibrary.HopperJsonStatic;
import com.example.hopperlibrary.HopperLocalArfInfoStatic;
import com.example.hopperlibrary.console;

public class SocialInterfaceModel {
	
	private String connectedDeviceId;
	final private Activity activity;
	final private WebSocketCommService webSocketCommService;
	
//--------callback-------------------------------------------------------------
	private ArrayList<OnNodeDataReceivedListener> onNodeDataReceivedListenerArrayList = new ArrayList<OnNodeDataReceivedListener>();
	
	public interface OnNodeDataReceivedListener{
	    public void onNodeDataReceived(Object...objects);
	}
    public void setOnNodeDataReceivedListener(OnNodeDataReceivedListener listener) {
    	onNodeDataReceivedListenerArrayList.add(listener);
    }
    private void reportOnNodeDataReceived(Object...objects){
    	for(OnNodeDataReceivedListener listener : onNodeDataReceivedListenerArrayList){
    		listener.onNodeDataReceived(objects);
        }
    }
//-
    
  //--------callback-------------------------------------------------------------
  	private ArrayList<OnNewRecordingInsertListener> onNewRecordingInsertListenerArrayList = new ArrayList<OnNewRecordingInsertListener>();
  	
  	public interface OnNewRecordingInsertListener{
  	    public void onNewRecordingInsert(Object...objects);
  	}
      public void setOnNewRecordingInsertListener(OnNewRecordingInsertListener listener) {
      	onNewRecordingInsertListenerArrayList.add(listener);
      }
      private void reportOnNewRecordingInsert(Object...objects){
      	for(OnNewRecordingInsertListener listener : onNewRecordingInsertListenerArrayList){
      		listener.onNewRecordingInsert(objects);
          }
      }
  //-

	
    public String getConnectedDeviceId(){
    	return connectedDeviceId;
    }
    
    public void setConnectedDeviceId(String inDeviceId){
    	connectedDeviceId = inDeviceId;
    }
    
    
	public SocialInterfaceModel(WebSocketCommService inWebSocketCommService, Activity inActivity){
		activity = inActivity;
		console.log("SocialInterfaceModel v001 CONSTRUCTOR ENTERED!!!");
		webSocketCommService= inWebSocketCommService;
		
		filterSetup();
	}

	public void sendUpOneNode(SocialInterfaceNodeObject currentNode){
		JSONObject browser_json = new JSONObject();
		HopperJsonStatic.putKeyValueStringsForJsonObject(browser_json, "filterKey", "upOneNode");
		
		//int toBrowserDeviceId = currentNode.getFromDeviceId();
		int toBrowserDeviceId = currentNode.getFromDeviceId();
		//commService.sendMessage((int) HopperCommunicationInterface.get("COMM").deviceId, toBrowserDeviceId, browser_json);
		webSocketCommService.sendMessage(String.valueOf(toBrowserDeviceId), browser_json.toString());
	}
	
	
	
	public void requestAnyNode(int inToDeviceId){
		JSONObject browser_json = new JSONObject();
		HopperJsonStatic.putKeyValueStringsForJsonObject(browser_json, "filterKey", "requestAnyNode");
		int toBrowserDeviceId = inToDeviceId;		
		webSocketCommService.sendMessage(String.valueOf(toBrowserDeviceId), browser_json.toString());
	}
	
	public void sendDownOneNode(SocialInterfaceNodeObject currentNode){
		console.log("SENDING sendDownOneNode");
		JSONObject browser_json = new JSONObject();
		HopperJsonStatic.putKeyValueStringsForJsonObject(browser_json, "filterKey", "downOneNode");
		
		int toBrowserDeviceId = currentNode.getFromDeviceId();
		//commService.sendMessage((int) HopperCommunicationInterface.get("COMM").deviceId, toBrowserDeviceId, browser_json);
		webSocketCommService.sendMessage(String.valueOf(toBrowserDeviceId), browser_json.toString());
	}
	
	public void appSocialTalkStateChange(String inType){
		console.log("SENDING appSocialTalkStateChange");
		JSONObject browser_json = new JSONObject();
		HopperJsonStatic.putKeyValueStringsForJsonObject(browser_json, "filterKey", "appSocialTalkStateChange");
		HopperJsonStatic.putKeyValueStringsForJsonObject(browser_json, "type", inType);
		
		//int toBrowserDeviceId = currentNode.getFromDeviceId();
		//commService.sendMessage((int) HopperCommunicationInterface.get("COMM").deviceId, toBrowserDeviceId, browser_json);
		//webSocketCommService.sendMessage(String.valueOf(toBrowserDeviceId), browser_json.toString());
	}
	
	
	
	
	/*
	 * CREATE Response NODE of SELF 
	 */
	public SocialInterfaceNodeObject createRspNodeObject(){
		SocialInterfaceNodeObject tmpSocialInterfaceNodeObject = new SocialInterfaceNodeObject();
		UserObject selfUserObject = UserModel.getUserObjectByUserId((int)HopperCommunicationInterface.get("COMM").userId);
		
		tmpSocialInterfaceNodeObject.setUserId(selfUserObject.getId());
		tmpSocialInterfaceNodeObject.setScreenName(selfUserObject.getProperty("userName"));
		tmpSocialInterfaceNodeObject.setScreenImageId(Integer.parseInt(selfUserObject.getProperty("imageId")));
		tmpSocialInterfaceNodeObject.setScreenImageUrl(HopperLocalArfInfoStatic.getField("arfWebSiteUrl")+selfUserObject.getProperty("file"));
		
		
		return tmpSocialInterfaceNodeObject;
	}
	
	
	/*
	 * SEND TO BROWSER-----
	 */
	public void sendRspNodeObject(SocialInterfaceNodeObject inRspNodeObject, boolean returnToQ){
		JSONObject browser_json = new JSONObject();
		
		HopperJsonStatic.putKeyValueStringsForJsonObject(browser_json, "filterKey", "nodeDataForBrowser");
		if(returnToQ){
			HopperJsonStatic.putKeyValueStringsForJsonObject(browser_json, "returnToQueue", "1");
		}else{
			HopperJsonStatic.putKeyValueStringsForJsonObject(browser_json, "returnToQueue", "0");
			
		}
		
		HopperJsonStatic.putKeyValueStringsForJsonObject(browser_json, "node_nodeId", "-1000");//  getIntFromKeyForJsonObject(filterSetAsJoson, "node_nodeId"));
		HopperJsonStatic.putKeyValueStringsForJsonObject(browser_json, "node_socialQueueId", String.valueOf(inRspNodeObject.getSocialQueueId()));//.getIntFromKeyForJsonObject(filterSetAsJoson, "node_socialQueueId"));
		
		HopperJsonStatic.putKeyValueStringsForJsonObject(browser_json, "node_userId", String.valueOf(inRspNodeObject.getUserId()));//.getIntFromKeyForJsonObject(filterSetAsJoson, "node_userId"));
		HopperJsonStatic.putKeyValueStringsForJsonObject(browser_json, "node_text", inRspNodeObject.getText());//.getIntFromKeyForJsonObject(filterSetAsJoson, "node_userId"));
		
		HopperJsonStatic.putKeyValueStringsForJsonObject(browser_json, "node_screenName", inRspNodeObject.getScreenName());//.getStringFromKeyForJsonObject(filterSetAsJoson, "node_screenName"));
		HopperJsonStatic.putKeyValueStringsForJsonObject(browser_json, "node_screenImageId", String.valueOf(inRspNodeObject.getScreenImageId()));//.getIntFromKeyForJsonObject(filterSetAsJoson, "node_screenImageId"));
		HopperJsonStatic.putKeyValueStringsForJsonObject(browser_json, "node_screenImageUrl", inRspNodeObject.getScreenImageUrl());//.getStringFromKeyForJsonObject(filterSetAsJoson, "node_screenImageUrl"));		
		
		
		HopperJsonStatic.putKeyValueStringsForJsonObject(browser_json, "node_timeStamp", inRspNodeObject.getTimeStamp());//.getStringFromKeyForJsonObject(filterSetAsJoson, "node_timeStamp"));
		HopperJsonStatic.putKeyValueStringsForJsonObject(browser_json, "node_audioUrl", inRspNodeObject.getAudioUrl());//.getStringFromKeyForJsonObject(filterSetAsJoson, "node_audioUrl"));
		HopperJsonStatic.putKeyValueStringsForJsonObject(browser_json, "node_audioId", String.valueOf(inRspNodeObject.getAudioId()));//.getIntFromKeyForJsonObject(filterSetAsJoson, "node_audioId"));
		
		HopperJsonStatic.putKeyValueStringsForJsonObject(browser_json, "node_rsponseType", inRspNodeObject.getResponseType());//.getStringFromKeyForJsonObject(filterSetAsJoson,  "node_rsponseType"));
		HopperJsonStatic.putKeyValueStringsForJsonObject(browser_json, "node_groupId", String.valueOf(inRspNodeObject.getGroupId()));//.getIntFromKeyForJsonObject(filterSetAsJoson, "node_groupId"));
		//HopperJsonStatic.putKeyValueStringsForJsonObject(browser_json, "node_action", inRspNodeObject.getAction());//.getStringFromKeyForJsonObject(filterSetAsJoson,  "node_action"));
		HopperJsonStatic.putKeyValueStringsForJsonObject(browser_json, "node_action", "nodeInsert");
		HopperJsonStatic.putKeyValueStringsForJsonObject(browser_json, "node_parentNodeId", String.valueOf(inRspNodeObject.getParentNodeId()));//.getIntFromKeyForJsonObject(filterSetAsJoson,  "node_parentNodeId"));
		//HopperJsonStatic.putKeyValueStringsForJsonObject(browser_json, "node_isRoot", String.valueOf(inRspNodeObject.getIsRoot()));//.getIntFromKeyForJsonObject(filterSetAsJoson, "node_isRoot"));		
		HopperJsonStatic.putKeyValueStringsForJsonObject(browser_json, "node_isRoot", String.valueOf(0));//app should not create root,,
		int fromDeviceId = inRspNodeObject.getParentNodeObject().getFromDeviceId();
		HopperJsonStatic.putKeyValueStringsForJsonObject(browser_json, "node_fromDeviceId", String.valueOf(fromDeviceId));
		
		//commService.sendMessage((int) HopperCommunicationInterface.get("COMM").deviceId, fromDeviceId, browser_json);
		console.log("dump of sendRspNodeObject :"+browser_json.toString());
		webSocketCommService.sendMessage(String.valueOf(fromDeviceId), browser_json.toString());
		//event of sent data here--
		
	
	}
	
	
	
	
	public void newRecordingInsert(HopperAudioMachine inHam, SocialInterfaceNodeObject inSinObject){
		
		HopperDataset socialAudioDataset = new HopperDataset("COMM");
		int userId = (int) HopperCommunicationInterface.get("COMM").userId;
		int newSocialAudioId = socialAudioDataset.executeSqlWithReturnNewId("INSERT INTO tb_social_audio(userId) VALUES(" + HopperCommunicationInterface.get("COMM").userId + ");");		
		String theName = newSocialAudioId + "_soc_" + HopperCommunicationInterface.get("COMM").userId + ".wav";		
		RemoteFileStorageModel remoteFileStorageModel = new RemoteFileStorageModel("http://www.walnutcracker.net/webservice/uploadRemoteFile",inHam.getBufferWithHeader(),"tmpAudio.wav");		
		String retString = remoteFileStorageModel.upload((int) HopperCommunicationInterface.get("COMM").userId, "socialAudioFileUpload", theName);		
		JSONObject json_retFromServer = HopperJsonStatic.createJsonObjectFromJsonString(retString);		
		String newFileName = HopperJsonStatic.getStringFromKeyForJsonObject(json_retFromServer, "fileName");		
		HopperDataset socialAudioDatasetPhase2 = new HopperDataset("COMM");		
		socialAudioDatasetPhase2.executeSql("UPDATE tb_social_audio SET filePath = '" + newFileName + "' WHERE id = " + newSocialAudioId);		
		
		String socialAudioPath = HopperLocalArfInfoStatic.getField("socialAudioPath");
		reportOnNewRecordingInsert(newSocialAudioId, HopperLocalArfInfoStatic.getField("arfWebSiteUrl") + socialAudioPath+theName, socialAudioPath+newFileName);
		
	}
	
	private void filterSetup(){
		console.log("filterSetup ENETERED");
		/*
		 * COMMUNICATION FILTER ------------(nodeDataForApp)---------------------------------------
		 * 
		 */
		
		HopperFilterSet filterSet = new HopperFilterSet(WebSocketCommService.getInstance("COMM"));
		
		HopperFilter filter_nodeDataForApp = new HopperFilter("nodeDataForApp");
		filter_nodeDataForApp.setOnExecuteListener(new OnExecuteListener(){
			/*
			 * @param filterKey filterKey that had this match
			 * @param inMessage json from other device
			 */
			
			@SuppressLint("NewApi")
			@Override
			public void onExecute(final Object... inObjects){
				
				
			        	
			        	
			       
						try {
							console.log("Social MODEL. callback for filter, onExecute");
							String matchingKey = (String)inObjects[0];
							JSONObject json_inMessage = (JSONObject)inObjects[1];
							
							SocialInterfaceNodeObject nodeObject = new SocialInterfaceNodeObject();
							
							nodeObject.setId(HopperJsonStatic.getIntFromKeyForJsonObject(json_inMessage, "node_nodeId"));
							nodeObject.setSocialQueueId(HopperJsonStatic.getIntFromKeyForJsonObject(json_inMessage, "node_socialQueueId"));
							nodeObject.setNodeId(HopperJsonStatic.getIntFromKeyForJsonObject(json_inMessage, "node_nodeId"));
							nodeObject.setUserId(HopperJsonStatic.getIntFromKeyForJsonObject(json_inMessage, "node_userId"));
							
							nodeObject.setText(HopperJsonStatic.getStringFromKeyForJsonObject(json_inMessage, "node_text"));
							nodeObject.setScreenName(HopperJsonStatic.getStringFromKeyForJsonObject(json_inMessage, "node_screenName"));
							nodeObject.setScreenImageId(HopperJsonStatic.getIntFromKeyForJsonObject(json_inMessage, "node_screenImageId"));
							nodeObject.setScreenImageUrl(HopperJsonStatic.getStringFromKeyForJsonObject(json_inMessage, "node_screenImageUrl"));
							
							if(nodeObject.getScreenImageUrl().isEmpty() == false ){
								ImageCache.addRemoteImageToMemoryCache(nodeObject.getImageHashId(), nodeObject.getScreenImageUrl()); 
							}
							
							nodeObject.setTimeStamp(HopperJsonStatic.getStringFromKeyForJsonObject(json_inMessage, "node_timeStamp"));
							nodeObject.setAudioUrl(HopperJsonStatic.getStringFromKeyForJsonObject(json_inMessage, "node_audioUrl"));
							console.log("audio URL:"+nodeObject.getAudioUrl());
							nodeObject.setAudioId(HopperJsonStatic.getIntFromKeyForJsonObject(json_inMessage, "node_audioId"));
							
							nodeObject.setResponseType(HopperJsonStatic.getStringFromKeyForJsonObject(json_inMessage,  "node_rsponseType"));
							nodeObject.setGroupId(HopperJsonStatic.getIntFromKeyForJsonObject(json_inMessage, "node_groupId"));
							nodeObject.setAction(HopperJsonStatic.getStringFromKeyForJsonObject(json_inMessage,  "node_action"));
							nodeObject.setParentNodeId(HopperJsonStatic.getIntFromKeyForJsonObject(json_inMessage,  "node_parentNodeId"));
							nodeObject.setIsRoot(HopperJsonStatic.getIntFromKeyForJsonObject(json_inMessage, "node_isRoot"));	
							nodeObject.setFromDeviceId(HopperJsonStatic.getIntFromKeyForJsonObject(json_inMessage, "node_fromDeviceId"));	
							
							
							reportOnNodeDataReceived(SocialInterfaceModel.this, matchingKey, nodeObject );
						} catch (Exception e) {
							console.log("ERROR(Model)---bad node data");
							e.printStackTrace();
						}
			      
				
				
			}
			
		});
		filterSet.addFilter(filter_nodeDataForApp);
		
		
		
		
		
		
		/*#####################################################################################
		 * browserToAppMarriage -------- F I L T E R---------
		 * 
		 *#####################################################################################
		 */
		
		HopperFilter filter_browserToAppMarriage = new HopperFilter("browserToAppMarriage");
		filter_browserToAppMarriage.setOnExecuteListener(new OnExecuteListener(){
			/*
			 * @param filterKey filterKey that had this match
			 * @param inMessage json from other device
			 */
			
			@SuppressLint("NewApi")
			@Override
			public void onExecute(final Object... inObjects){
				JSONObject inMessage_json = (JSONObject)inObjects[1];				
				SocialInterfaceModel.this.setConnectedDeviceId(HopperJsonStatic.getStringFromKeyForJsonObject((JSONObject)inObjects[1], "fromDeviceId"));
				console.log("Set Connected Device Id:"+SocialInterfaceModel.this.getConnectedDeviceId());
			}
			
		});
		filterSet.addFilter(filter_browserToAppMarriage);
		
		
		
		/*#####################################################################################
		 * advisee -------- F I L T E R---------
		 * 
		 *#####################################################################################
		 */
		
		HopperFilter filter_advise= new HopperFilter("advise");
		filter_advise.setOnExecuteListener(new OnExecuteListener(){
			/*
			 * @param filterKey filterKey that had this match
			 * @param inMessage json from other device
			 */
			
			@SuppressLint("NewApi")
			@Override
			public void onExecute(final Object... inObjects){
				//JSONObject inMessage_json = (JSONObject)inObjects[1];				
				//SocialInterfaceModel.this.setConnectedDeviceId(HopperJsonStatic.getStringFromKeyForJsonObject((JSONObject)inObjects[1], "fromDeviceId"));
				console.log("Advise exec...:");
			}
			
		});
		filterSet.addFilter(filter_advise);
				
		
		
	}

}








