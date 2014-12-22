package hopper.library.communication.v003;

import hopper.library.communication.v003.WebSocketService.OnOpenedListener;

import java.util.ArrayList;

import com.example.hopperlibrary.HashOfArray;

public class TransactionRequest{
	
	//###########------------->--  S T A T I C   S T U F F --<------------------------#################################################
	//static private ArrayList<TransactionRequest> transactionRequestArrayList = new ArrayList<TransactionRequest>();
	
	static private HashOfArray<TransactionRequest> hashOfArray = new HashOfArray<TransactionRequest>();
	
	//destroy will remove from work-load and nullify passed in reference, should cause GC to free mem unless second ref was created
	static public void destroy(TransactionRequest inTransactionRequest){
		hashOfArray.remove(inTransactionRequest);		
		inTransactionRequest = null;
	}
	
	static public void process(String inCommand, Object ...inObjectsArray ){
		ArrayList<TransactionRequest> arrayList = hashOfArray.getArrayList(inCommand);
		if(arrayList != null){
			for(TransactionRequest transactionRequest : arrayList){
				transactionRequest.reportOnMatch(inObjectsArray);
			}
		}
	}	
	
	
	
	
	//###########------------->--  I N S T A N C E   S  T U F F  --<------------------------#################################################
	private String command;
	public TransactionRequest(String inCommand, OnMatchListener inListener){
		this.command = inCommand;
		hashOfArray.add(inCommand, this);
		onMatchListenerArrayList.add(inListener);
		hashOfArray.dump();
	}
	
	//############ E V E N T S ##############################################################################################
	//----------------------------------------------------------------------------------------------------
	private ArrayList<OnMatchListener> onMatchListenerArrayList = new ArrayList<OnMatchListener>();
	
 	/**
	 * @param objects[0] TransportLayer instance	 				 * 
	 */
	public interface OnMatchListener{
	    public void onMatch(Object...objects);
	}
	
    public void setOnMatchListener(OnMatchListener listener) {
    	onMatchListenerArrayList.add(listener);
    }
    
    private void reportOnMatch(Object...objects){
    	for(OnMatchListener listener : onMatchListenerArrayList){
    		listener.onMatch(objects);
        }
    }
    
	
	public String getCommand(){
		return command;
	}
	
	/*public TransportLayer returnify(TransportLayer inTransportLayer){
		inTransportLayer.routingLayer().addField("stage", "targetOut");
		return inTransportLayer;
	}*/
}
