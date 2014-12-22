package hopper.library.model.v001;

import hopper.library.object.ResponseObject;

import android.content.Context;

import com.example.hopperlibrary.HopperCommunicationInterface;
import com.example.hopperlibrary.HopperDataset;
import com.example.hopperlibrary.HopperInstanceHash;

public class RecordResponseModel {
	
	private HopperCommunicationInterface HCI = HopperCommunicationInterface.get("COMM");
	
	public RecordResponseModel() {
		// TODO Auto-generated constructor stub
	}
	
	public ResponseObject getResponseObjectByArfBuffId(int inArfBuffId){
		Context context = HopperInstanceHash.getInstance("MainActivity").getApplicationContext();
		ResponseObject responseObject = ResponseObject.getOrMaybeCreate(context,0);
		HopperDataset dataset = new HopperDataset(HCI);		
		String sqlString = "CALL sp_getRspByArfBuffId_one("+ String.valueOf(HCI.userId) +", "+String.valueOf(inArfBuffId) +");";
		dataset.executeSql(sqlString);
		
		if(dataset.getRecordCount() > 0){
			responseObject.setUserId(dataset.getFieldAsInt("rsp_userId", 0));
			responseObject.setEntryTimeStamp(dataset.getFieldAsString("rsp_entryTimeStamp", 0));			
			responseObject.setScreenName((dataset.getFieldAsString("rsp_screenName", 0)));
			responseObject.setCaption((dataset.getFieldAsString("rsp_caption", 0)));
			responseObject.setImageId(dataset.getFieldAsInt("rsp_imageId", 0));
			responseObject.setImageFilePath(dataset.getFieldAsString("rsp_imageFilePath", 0));
			responseObject.setAudioFilePath(dataset.getFieldAsString("rsp_filePath", 0));
			responseObject.setArfBuffId(dataset.getFieldAsInt("rsp_id", 0));
			//must be added to sp---
			//responseObject.setHadDownload(Boolean.valueOf(dataset.getFieldAsInt("rsp_userId", 0)));
			//responseObject.setHadRead(dataset.getFieldAsString("rsp_userId", 0));
			responseObject.setPrivacyTypeId(dataset.getFieldAsInt("rsp_privacyTypeId", 0));
			responseObject.setParentId(dataset.getFieldAsInt("rq_id", 0));			
		}
		
		return responseObject;
		
	}
	

}
