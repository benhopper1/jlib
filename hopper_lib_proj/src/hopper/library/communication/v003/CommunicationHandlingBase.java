package hopper.library.communication.v003;

import java.util.HashMap;

public class CommunicationHandlingBase {
	public CommunicationHandlingBase(){}
	
	private HashMap<String, TransactionRequest> transactionRequestHashMap = new HashMap<String, TransactionRequest>();
	
	
	
	/*
	 * ---------------------------------------->-- T R A N S A C T I  O N   R E Q U E S T   S  E C T I O N --<----------------------------------------
	 */
	//used to store ref for cleanup later or cancel of use
	public void trackTransactionRequest(String inTrackingName, TransactionRequest inTransactionRequest){
		transactionRequestHashMap.put(inTrackingName, inTransactionRequest);
	}
	
	public void removeTrackedTransactionRequest(String inTrackingName){
		transactionRequestHashMap.remove(inTrackingName);
	}
	
	public void removeAllTrackedTransactionRequest(){
		transactionRequestHashMap.clear();
	}
	
	public HashMap<String, TransactionRequest> getTransactionRequestHashMap(){
		return transactionRequestHashMap;
	}
	
}
