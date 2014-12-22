package hopper.library.model.v001;

import hopper.cache.ImageCache;
import hopper.library.object.ProfileGroupObject;
import hopper.library.object.ProfileItemObject;
import hopper.library.object.ProfileObject;
import hopper.library.object.UserObject;

import java.util.ArrayList;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.util.Log;

import com.example.hopperCommServicelibrary.CommService.MailNotificationListener;
import com.example.hopperlibrary.HopperAjax;
import com.example.hopperlibrary.HopperAudioMachine;
import com.example.hopperlibrary.HopperCommunicationInterface;
import com.example.hopperlibrary.HopperDataset;
import com.example.hopperlibrary.HopperFileInfo;
import com.example.hopperlibrary.HopperJsonStatic;
import com.example.hopperlibrary.HopperLocalArfInfoStatic;
import com.example.hopperlibrary.console;

public class ProfileModel {
	//ArrayList<UserObject> userArrayList = new ArrayList<UserObject>();
	private String fullAudioProfileImagePath;
    final HopperDataset tmpDataset = new HopperDataset("COMM");
    

	public ProfileModel() {
		fullAudioProfileImagePath = HopperLocalArfInfoStatic.getField("arfWebSiteUrl")+HopperLocalArfInfoStatic.getField("audioProfileImagePath");
	}
	
	//--------callback-------------------------------------------------------------
	private ArrayList<CompleteListener> CompleteListenerArrayList = new ArrayList<CompleteListener>();
	
	public interface CompleteListener{
	    public void onComplete();
	}
    public void setCompleteListener(CompleteListener listener) {
    	CompleteListenerArrayList.add(listener);
    }
    private void reportNotification(){
    	for(CompleteListener listener : CompleteListenerArrayList){
    		listener.onComplete();
        }
    }
	    //----------------------------------------------------------------------------
	
	@SuppressLint("NewApi")
	public ProfileObject getProfileObject(int inUserId){
		ProfileObject profileObject = new ProfileObject();
		
		HopperDataset groupByUserId_dataset = new HopperDataset("COMM");
		int userId = 1;
		groupByUserId_dataset.executeSql("SELECT * FROM tb_audioProfile WHERE userId = " + userId + " ORDER BY sortOrder");
		int groupByUserId_dataset_count = groupByUserId_dataset.getRecordCount();
		for(int i = 0; i < groupByUserId_dataset_count;i++){
			int groupId = groupByUserId_dataset.getFieldAsInt("id", i);
			console.log("GROUP ID:",groupId);
			ProfileGroupObject profileGroupObject = new ProfileGroupObject();
			profileGroupObject.setId(groupByUserId_dataset.getFieldAsInt("id", i));
			profileGroupObject.setSubjectCaption(groupByUserId_dataset.getFieldAsString("subjectCaption", i));
			profileGroupObject.setSubjectFilePath(groupByUserId_dataset.getFieldAsString("subjectFilePath", i));
			profileGroupObject.setGroupCaption(groupByUserId_dataset.getFieldAsString("groupCaption", i));
			profileGroupObject.setGroupFilePath(groupByUserId_dataset.getFieldAsString("groupFilePath", i));
			profileGroupObject.setTopicImageFilePath(groupByUserId_dataset.getFieldAsString("topicImageFilePath", i));
			if(profileGroupObject.getTopicImageFilePath().isEmpty() == false ){
				ImageCache.addRemoteImageToMemoryCache(profileGroupObject.getTopicImageId(), fullAudioProfileImagePath + profileGroupObject.getTopicImageFilePath()); 
			}
			
			
			profileGroupObject.setSortOrder(groupByUserId_dataset.getFieldAsInt("sortOrder", i));
			HopperDataset itemsByGroup_dataset = new HopperDataset("COMM");
			//itemsByGroup_dataset.executeSql("SELECT tb_audioProfileItems.* from tb_audioProfile	LEFT JOIN tb_audioProfileItems ON tb_audioProfile.id = tb_audioProfileItems.groupId	WHERE groupId = " + groupId + " ORDER BY groupId ASC, sortOrder ASC");
			itemsByGroup_dataset.executeSql("SELECT * from tb_audioProfileItems	WHERE groupId = " + groupId + " ORDER BY groupId ASC, sortOrder ASC");
			
			int itemsByGroup_dataset_count = itemsByGroup_dataset.getRecordCount();
			for(int j = 0; j < itemsByGroup_dataset_count;j++){
				ProfileItemObject profileItemObject = new ProfileItemObject();
				profileItemObject.setId(itemsByGroup_dataset.getFieldAsInt("id", j));
				profileItemObject.setGroupId(itemsByGroup_dataset.getFieldAsInt("groupId", j));
				profileItemObject.setSortOrder(itemsByGroup_dataset.getFieldAsInt("sortOrder", j));
				profileItemObject.setFilePath(itemsByGroup_dataset.getFieldAsString("filePath", j));
				profileItemObject.setCaption(itemsByGroup_dataset.getFieldAsString("caption", j));				
				profileGroupObject.addItem(profileItemObject);
				
				console.log("Item Id:",profileItemObject.getId());
				
			}
			
			
			profileObject.addGroup(profileGroupObject);		
			
			
		}
		
		

		
		
		//int groupId = 0;
//		HopperDataset itemsByGroup_dataset = new HopperDataset("COMM");
	//	itemsByGroup_dataset.executeSql("SELECT tb_audioProfileItems.* from tb_audioProfile	LEFT JOIN tb_audioProfileItems ON tb_audioProfile.id = tb_audioProfileItems.groupId	WHERE groupId = " + groupId + "ORDER BY groupId ASC, sortOrder ASC");
		
		
		
		
		return profileObject;
		
	}
	public void updateGroupImage(String inLocalFilePath, int groupId, int inImageId){
		String newName = groupId +"_pro_img."+ HopperFileInfo.getExtension(inLocalFilePath);
		RemoteFileStorageModel remoteFileStorageModel = new RemoteFileStorageModel("http://www.walnutcracker.net/webservice/uploadRemoteFile",inLocalFilePath);
		String retString = remoteFileStorageModel.upload((int) HopperCommunicationInterface.get("COMM").userId, "profileImageFileUpload", newName);
		console.log("UPLOAD RETURN:"+retString);
		JSONObject json_retFromServer = HopperJsonStatic.createJsonObjectFromJsonString(retString);
		String newFileName = HopperJsonStatic.getStringFromKeyForJsonObject(json_retFromServer, "fileName");
		console.log("UPLOAD img(Hopper)RETURN fileName:"+newFileName);
		HopperDataset updateAudioDataset = new HopperDataset("COMM");
		updateAudioDataset.executeSql("update tb_audioProfile SET topicImageFilePath = '" + newFileName +"' WHERE id = " + groupId);
		
		ImageCache.addRemoteImageToMemoryCache_overWriteExist(inImageId, HopperLocalArfInfoStatic.getField("arfWebSiteUrl") + HopperLocalArfInfoStatic.getField("audioProfileImagePath")+ newFileName);
		reportNotification();
	}
	
	/*
	 * ------- inType -------------
	 * 0	-	group topic
	 * 1	-	group subject
	 * 2	-	item
	 */
	public void updateAudio(HopperAudioMachine inHam, int inType, int inId, int inItemIndex){
		String theName = null;
		if(inType == 0){
			theName = inId + "_pro_topic_" + ".wav";
			RemoteFileStorageModel remoteFileStorageModel = new RemoteFileStorageModel("http://www.walnutcracker.net/webservice/uploadRemoteFile",inHam.getBufferWithHeader(),"tmpAudio.wav");
			String retString = remoteFileStorageModel.upload((int) HopperCommunicationInterface.get("COMM").userId, "profileAudioFileUpload", theName);
			console.log("UPLOAD RETURN:"+retString);
			JSONObject json_retFromServer = HopperJsonStatic.createJsonObjectFromJsonString(retString);
			String newFileName = HopperJsonStatic.getStringFromKeyForJsonObject(json_retFromServer, "fileName");
			console.log("UPLOAD RETURN fileName:"+newFileName);
			HopperDataset updateAudioDataset = new HopperDataset("COMM");
			updateAudioDataset.executeSql("update tb_audioProfile SET subjectFilePath = '" + newFileName +"' WHERE id = " + inId);			
			reportNotification();
		}
		
		if(inType == 1){
			theName = inId + "_pro_subject" + ".wav";
			RemoteFileStorageModel remoteFileStorageModel = new RemoteFileStorageModel("http://www.walnutcracker.net/webservice/uploadRemoteFile",inHam.getBufferWithHeader(),"tmpAudio.wav");
			String retString = remoteFileStorageModel.upload((int) HopperCommunicationInterface.get("COMM").userId, "profileAudioFileUpload", theName);
			console.log("UPLOAD RETURN:"+retString);
			JSONObject json_retFromServer = HopperJsonStatic.createJsonObjectFromJsonString(retString);
			String newFileName = HopperJsonStatic.getStringFromKeyForJsonObject(json_retFromServer, "fileName");
			console.log("UPLOAD RETURN fileName:"+newFileName);
			HopperDataset updateAudioDataset = new HopperDataset("COMM");
			updateAudioDataset.executeSql("update tb_audioProfile SET groupFilePath = '" + newFileName +"' WHERE id = " + inId);
			reportNotification();
		}
		
		if(inType == 2){
			theName = inId + "_" + inItemIndex + "_pro_itm" + ".wav";
			RemoteFileStorageModel remoteFileStorageModel = new RemoteFileStorageModel("http://www.walnutcracker.net/webservice/uploadRemoteFile",inHam.getBufferWithHeader(),"tmpAudio.wav");
			String retString = remoteFileStorageModel.upload((int) HopperCommunicationInterface.get("COMM").userId, "profileAudioFileUpload", theName);
			console.log("UPLOAD RETURN:"+retString);
			JSONObject json_retFromServer = HopperJsonStatic.createJsonObjectFromJsonString(retString);
			String newFileName = HopperJsonStatic.getStringFromKeyForJsonObject(json_retFromServer, "fileName");
			console.log("UPLOAD RETURN fileName:"+newFileName);
			HopperDataset updateAudioDataset = new HopperDataset("COMM");			
			updateAudioDataset.executeSql("update tb_audioProfileItems SET filePath = '" + newFileName +"' WHERE id = " + inId);
			reportNotification();
		}
		
	}
	
	public void addDefaultGroup(){
		console.log("addDefaultGroup ENTERED", (int) HopperCommunicationInterface.get("COMM").userId);
		JSONObject json_toService = new JSONObject();
		HopperJsonStatic.putKeyValueStringsForJsonObject(json_toService,"command", "addDefaultGroup");
		HopperJsonStatic.putKeyValueStringsForJsonObject(json_toService,"userId", String.valueOf(HopperCommunicationInterface.get("COMM").userId));
		
		HopperAjax hAjax = new HopperAjax("http://www.walnutcracker.net/webservice/audioProfileService");
		String contents = hAjax.run(json_toService.toString());
		Log.v("arfComm", "WebserviceReturn:" + contents);
		JSONObject json_fromService = HopperJsonStatic.getJsonObjectFromString(contents);
		reportNotification();
		
		//String appServiceHost = HopperJsonStatic.getStringFromKeyForJsonObject(json_fromService, "appServiceHost");
		//int appServicePort = HopperJsonStatic.getIntFromKeyForJsonObject(json_fromService, "appServicePort");
		
		
	}
	
	public void deleteGroup(ProfileGroupObject inProfileGroupObject ){
		JSONObject json_toService = new JSONObject();
		HopperJsonStatic.putKeyValueStringsForJsonObject(json_toService,"command", "deleteGroup");
		HopperJsonStatic.putKeyValueStringsForJsonObject(json_toService,"userId", String.valueOf(HopperCommunicationInterface.get("COMM").userId));
		HopperJsonStatic.putKeyValueStringsForJsonObject(json_toService,"groupId", String.valueOf(inProfileGroupObject.getId()));
		HopperAjax hAjax = new HopperAjax("http://www.walnutcracker.net/webservice/audioProfileService");
		String contents = hAjax.run(json_toService.toString());
		Log.v("arfComm", "WebserviceReturn:" + contents);
		JSONObject json_fromService = HopperJsonStatic.getJsonObjectFromString(contents);
		reportNotification();		
	}
	
	public void addDefaultItem(ProfileGroupObject inProfileGroupObject){
		console.log("----groupId", inProfileGroupObject.getId());
		
		JSONObject json_toService = new JSONObject();
		HopperJsonStatic.putKeyValueStringsForJsonObject(json_toService,"command", "addDefaultItem");
		HopperJsonStatic.putKeyValueStringsForJsonObject(json_toService,"userId", String.valueOf(HopperCommunicationInterface.get("COMM").userId));
		HopperJsonStatic.putKeyValueStringsForJsonObject(json_toService,"groupId", String.valueOf(inProfileGroupObject.getId()));
		HopperAjax hAjax = new HopperAjax("http://www.walnutcracker.net/webservice/audioProfileService");
		String contents = hAjax.run(json_toService.toString());
		Log.v("arfComm", "WebserviceReturn:" + contents);
		JSONObject json_fromService = HopperJsonStatic.getJsonObjectFromString(contents);
		reportNotification();		
	}
	
	public void deleteItem(ProfileItemObject inProfileItemObject){
		console.log("----groupId", inProfileItemObject.getId());
		
		JSONObject json_toService = new JSONObject();
		HopperJsonStatic.putKeyValueStringsForJsonObject(json_toService,"command", "deleteItem");
		HopperJsonStatic.putKeyValueStringsForJsonObject(json_toService,"userId", String.valueOf(HopperCommunicationInterface.get("COMM").userId));
		HopperJsonStatic.putKeyValueStringsForJsonObject(json_toService,"itemId", String.valueOf(inProfileItemObject.getId()));
		HopperAjax hAjax = new HopperAjax("http://www.walnutcracker.net/webservice/audioProfileService");
		String contents = hAjax.run(json_toService.toString());
		Log.v("arfComm", "WebserviceReturn:" + contents);
		JSONObject json_fromService = HopperJsonStatic.getJsonObjectFromString(contents);
		reportNotification();		
	}
	
	public void groupUp(ProfileGroupObject inProfileGroupObject){		
		
		JSONObject json_toService = new JSONObject();
		HopperJsonStatic.putKeyValueStringsForJsonObject(json_toService,"command", "groupUp");
		HopperJsonStatic.putKeyValueStringsForJsonObject(json_toService,"userId", String.valueOf(HopperCommunicationInterface.get("COMM").userId));
		HopperJsonStatic.putKeyValueStringsForJsonObject(json_toService,"groupId", String.valueOf(inProfileGroupObject.getId()));
		HopperAjax hAjax = new HopperAjax("http://www.walnutcracker.net/webservice/audioProfileService");
		String contents = hAjax.run(json_toService.toString());
		Log.v("arfComm", "WebserviceReturn:" + contents);
		JSONObject json_fromService = HopperJsonStatic.getJsonObjectFromString(contents);
		reportNotification();		
	}
	
	public void groupDown(ProfileGroupObject inProfileGroupObject){		
		
		JSONObject json_toService = new JSONObject();
		HopperJsonStatic.putKeyValueStringsForJsonObject(json_toService,"command", "groupDown");
		HopperJsonStatic.putKeyValueStringsForJsonObject(json_toService,"userId", String.valueOf(HopperCommunicationInterface.get("COMM").userId));
		HopperJsonStatic.putKeyValueStringsForJsonObject(json_toService,"groupId", String.valueOf(inProfileGroupObject.getId()));
		HopperAjax hAjax = new HopperAjax("http://www.walnutcracker.net/webservice/audioProfileService");
		String contents = hAjax.run(json_toService.toString());
		Log.v("arfComm", "WebserviceReturn:" + contents);
		JSONObject json_fromService = HopperJsonStatic.getJsonObjectFromString(contents);
		reportNotification();		
	}
	
	
	

}

















