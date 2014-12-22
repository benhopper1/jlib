package hopper.library.model.v001;

import com.example.hopperlibrary.HopperCommunicationInterface;
import com.example.hopperlibrary.HopperDataset;
import com.example.hopperlibrary.console;

public class MailNotificationModel {

	public MailNotificationModel() {
		// TODO Auto-generated constructor stub
	}
	
	
	
	
	
	
	public void sendMailNotification(int inSenderUserId, int inReceiverUserId, int inMailboxTypeId, int inArfBuffId){
		console.log("sendMailNotification ENTERED");
		HopperDataset dataset = new HopperDataset(HopperCommunicationInterface.get("COMM"));
		String sqlString = "call sp_addMailEntry(" + String.valueOf(inSenderUserId) +
												", " + String.valueOf(inReceiverUserId) +
												", 1" + 
												", " + String.valueOf(inArfBuffId) +
												"   );";
		dataset.executeSql(sqlString);
	}
	
	public int checkMailNotification(int inReceiverUserId, int inMailboxTypeId){
		int arfBuffId = 0;
		
		
		return arfBuffId;
	}

}
