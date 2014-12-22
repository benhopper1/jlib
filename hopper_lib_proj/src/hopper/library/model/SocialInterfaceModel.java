package hopper.library.model;


import java.util.ArrayList;

import hopper.cache.ImageCache;
import hopper.library.object.SocialInterfaceNodeObject;
import hopper.library.object.UserObject;

import org.json.JSONObject;


import android.annotation.SuppressLint;
import com.example.hopperCommServicelibrary.CommService;
import com.example.hopperCommServicelibrary.HopperFilterSet;
import com.example.hopperCommServicelibrary.HopperProcessMessages;
import com.example.hopperCommServicelibrary.HopperFilterSet.OnFilterPassListener;
import com.example.hopperlibrary.HopperAudioMachine;
import com.example.hopperlibrary.HopperCommunicationInterface;
import com.example.hopperlibrary.HopperDataset;
import com.example.hopperlibrary.HopperJsonStatic;
import com.example.hopperlibrary.HopperLocalArfInfoStatic;
import com.example.hopperlibrary.console;

public class SocialInterfaceModel {
	
	
	final private HopperProcessMessages hopperProcessMessages;
	final private CommService commService;
	
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

	
	public SocialInterfaceModel(CommService inCommService, HopperProcessMessages inHopperProcessMessages) {
		hopperProcessMessages = inHopperProcessMessages;
		commService= inCommService;
		
		filterSetup();
	}
	
	public void sendUpOneNode(SocialInterfaceNodeObject currentNode){
		JSONObject browser_json = new JSONObject();
		HopperJsonStatic.putKeyValueStringsForJsonObject(browser_json, "commMessage", "upOneNode");
		
		int toBrowserDeviceId = currentNode.getFromDeviceId();
		commService.sendMessage((int) HopperCommunicationInterface.get("COMM").deviceId, toBrowserDeviceId, browser_json);
	}
	
	public void sendDownOneNode(SocialInterfaceNodeObject currentNode){
		JSONObject browser_json = new JSONObject();
		HopperJsonStatic.putKeyValueStringsForJsonObject(browser_json, "commMessage", "downOneNode");
		
		int toBrowserDeviceId = currentNode.getFromDeviceId();
		commService.sendMessage((int) HopperCommunicationInterface.get("COMM").deviceId, toBrowserDeviceId, browser_json);
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
		
		HopperJsonStatic.putKeyValueStringsForJsonObject(browser_json, "commMessage", "nodeDataForBrowser");
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
		HopperJsonStatic.putKeyValueStringsForJsonObject(browser_json, "node_action", inRspNodeObject.getAction());//.getStringFromKeyForJsonObject(filterSetAsJoson,  "node_action"));
		HopperJsonStatic.putKeyValueStringsForJsonObject(browser_json, "node_parentNodeId", String.valueOf(inRspNodeObject.getParentNodeId()));//.getIntFromKeyForJsonObject(filterSetAsJoson,  "node_parentNodeId"));
		HopperJsonStatic.putKeyValueStringsForJsonObject(browser_json, "node_isRoot", String.valueOf(inRspNodeObject.getIsRoot()));//.getIntFromKeyForJsonObject(filterSetAsJoson, "node_isRoot"));		
				
		int fromDeviceId = inRspNodeObject.getParentNodeObject().getFromDeviceId();
		HopperJsonStatic.putKeyValueStringsForJsonObject(browser_json, "node_fromDeviceId", String.valueOf(fromDeviceId));
		
		commService.sendMessage((int) HopperCommunicationInterface.get("COMM").deviceId, fromDeviceId, browser_json);
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
		/*
		 * COMMUNICATION FILTER ------------(nodeDataForApp)---------------------------------------
		 * 
		 */
		HopperFilterSet filter_nodeDataForApp = new HopperFilterSet();		
		filter_nodeDataForApp.add("commMessage", "nodeDataForApp");
		filter_nodeDataForApp.setOnFilterPassListener(new OnFilterPassListener(){			
			@SuppressLint("NewApi")
			@Override
			public void onFilterPass(Object... objects){
				/*
				 * 0	-inId
				 * 1	-HopperFilterSet local
				 * 2	-HopperFilterSet inRemoteFilterSet
				 * 3	-inKey
				 * 4	-inValue
				 */
				
				console.log("-x-x-x-x-x-x- filter_nodeDataForApp -z-z-z-z-z-z-z-z-z-z-");
				//--create new node, then populate...
				SocialInterfaceNodeObject nodeObject = new SocialInterfaceNodeObject();
				HopperFilterSet filterSet = (HopperFilterSet) objects[2];
				
				console.log("_+_+_+_+_+_+_+JSON of FS:"+filterSet.getJsonObject().toString());
				
				String subCommand = filterSet.getValueFromKey("subCommand");
				
				JSONObject filterSetAsJoson = filterSet.getJsonObject();
				
				/*
				 * -	Populate nodeObject----
				 */
				nodeObject.setId(HopperJsonStatic.getIntFromKeyForJsonObject(filterSetAsJoson, "node_nodeId"));
				nodeObject.setSocialQueueId(HopperJsonStatic.getIntFromKeyForJsonObject(filterSetAsJoson, "node_socialQueueId"));
				nodeObject.setNodeId(HopperJsonStatic.getIntFromKeyForJsonObject(filterSetAsJoson, "node_nodeId"));
				nodeObject.setUserId(HopperJsonStatic.getIntFromKeyForJsonObject(filterSetAsJoson, "node_userId"));
				
				nodeObject.setText(filterSet.getValueFromKey("node_text"));
				nodeObject.setScreenName(HopperJsonStatic.getStringFromKeyForJsonObject(filterSetAsJoson, "node_screenName"));
				nodeObject.setScreenImageId(HopperJsonStatic.getIntFromKeyForJsonObject(filterSetAsJoson, "node_screenImageId"));
				nodeObject.setScreenImageUrl(HopperJsonStatic.getStringFromKeyForJsonObject(filterSetAsJoson, "node_screenImageUrl"));
				
				if(nodeObject.getScreenImageUrl().isEmpty() == false ){
					ImageCache.addRemoteImageToMemoryCache(nodeObject.getImageHashId(), nodeObject.getScreenImageUrl()); 
				}
				
				nodeObject.setTimeStamp(HopperJsonStatic.getStringFromKeyForJsonObject(filterSetAsJoson, "node_timeStamp"));
				nodeObject.setAudioUrl(HopperJsonStatic.getStringFromKeyForJsonObject(filterSetAsJoson, "node_audioUrl"));
				console.log("audio URL:"+nodeObject.getAudioUrl());
				nodeObject.setAudioId(HopperJsonStatic.getIntFromKeyForJsonObject(filterSetAsJoson, "node_audioId"));
				
				nodeObject.setResponseType(HopperJsonStatic.getStringFromKeyForJsonObject(filterSetAsJoson,  "node_rsponseType"));
				nodeObject.setGroupId(HopperJsonStatic.getIntFromKeyForJsonObject(filterSetAsJoson, "node_groupId"));
				nodeObject.setAction(HopperJsonStatic.getStringFromKeyForJsonObject(filterSetAsJoson,  "node_action"));
				nodeObject.setParentNodeId(HopperJsonStatic.getIntFromKeyForJsonObject(filterSetAsJoson,  "node_parentNodeId"));
				nodeObject.setIsRoot(HopperJsonStatic.getIntFromKeyForJsonObject(filterSetAsJoson, "node_isRoot"));	
				nodeObject.setFromDeviceId(HopperJsonStatic.getIntFromKeyForJsonObject(filterSetAsJoson, "node_fromDeviceId"));	
				
				
				reportOnNodeDataReceived(SocialInterfaceModel.this, subCommand, nodeObject );
				
			}
			
		});		
		hopperProcessMessages.addLocalFilterSet(filter_nodeDataForApp);		
	}

}








