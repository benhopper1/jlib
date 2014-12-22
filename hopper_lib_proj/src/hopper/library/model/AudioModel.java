package hopper.library.model;

import com.example.hopperlibrary.HopperCommunicationInterface;
import com.example.hopperlibrary.HopperDataset;

public class AudioModel {

	public AudioModel() {
		// TODO Auto-generated constructor stub
	}
	//                         1,        HAM.getBufferWithHeader(), getCaption() , "app-recordR, inRequestObject.getArfBuffId(), inRequestObject.getUserId(), privacyId)
	public int addResponse(int inArfId, byte[] inByteArray, String inCaption, String inLabel, int inRqId,int inRqUserId, int inPrivacyId){
		// use tmp for transaction failure-cleanup....
		//inPrivacyId = allow privacy responses
		String entryTypeString = "tmp";
		int rsp_arfBuffId = addByteArrayToDatabase(inArfId, inByteArray, inLabel, inCaption,  entryTypeString);
		
		final int entryTypeId = 2; // rsp		
		final int orderSource = 0; // na
		final int orderResponse = 0; // na
		final int orderResponse2 = 0; // na
		
		
		HopperDataset dataset = new HopperDataset(HopperCommunicationInterface.get("COMM"));
		String sqlString = "CALL sp_tb_arfBuff_update("+ String.valueOf(rsp_arfBuffId) +", "+ 
														String.valueOf(HopperCommunicationInterface.get("COMM").userId) +", "+ 
														String.valueOf(entryTypeId) +", "+ 
														String.valueOf(inRqId) +", "+ 
														String.valueOf(orderSource) +", "+ 
														String.valueOf(orderResponse) +","+ 
														String.valueOf(orderResponse2) +", '"+ 
														inLabel +"', '"+ 
														inCaption +"', "+
														String.valueOf(inPrivacyId) +", "+
														String.valueOf(inArfId) +");";
		dataset.executeSql(sqlString);
		return rsp_arfBuffId;
	}
	
	
	//-inRqId removed
	public int addRequest(int inArfId, byte[] inByteArray, String inCaption, String inLabel, int inPrivacyId){
		// use tmp for transaction failure-cleanup....
		String entryTypeString = "tmp";
		int rsp_arfBuffId = addByteArrayToDatabase(inArfId, inByteArray, inLabel, inCaption,  entryTypeString);
		
		final int entryTypeId = 1; 			// rq		
		final int orderSource = 0; 			// na
		final int orderResponse = 0; 		// na
		final int orderResponse2 = 0; 		// na
		final int inRqId = 0;				//parent allowed only
											// privacy = 9 = public responses to be created only / 7 public or private
		
		HopperDataset dataset = new HopperDataset(HopperCommunicationInterface.get("COMM"));
		String sqlString = "CALL sp_tb_arfBuff_update("+ String.valueOf(rsp_arfBuffId) +", "+ 
														String.valueOf(HopperCommunicationInterface.get("COMM").userId) +", "+ 
														String.valueOf(entryTypeId) +", "+ 
														String.valueOf(inRqId) +", "+ 
														String.valueOf(orderSource) +", "+ 
														String.valueOf(orderResponse) +","+ 
														String.valueOf(orderResponse2) +", '"+ 
														inLabel +"', '"+ 
														inCaption +"', "+
														String.valueOf(inPrivacyId) +", "+
														String.valueOf(inArfId) +");";
		dataset.executeSql(sqlString);
		return rsp_arfBuffId;
	}
	
	
	
	
	/*OLD UGLY METHOD OF TRANSFER, REPLACE WITH COMPLETE SOLUTION IN FUTURE
	 * USES PYTHON LOGIC ON SOCKET,,,, SOCKET TRANSFER----
	 */
	private int addByteArrayToDatabase(int inArfId, byte[] inByteArray, String inCaption, String inSearchString, String inEntryType){
		return HopperCommunicationInterface.get("COMM").storeNewByteArray(inArfId, inByteArray, inCaption, inSearchString, inEntryType);		
	}

}
