
package hopper.library.communication.v003.transactionseries;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import com.example.hopperlibrary.console;

import hopper.library.communication.v003.TransportLayer;
import hopper.library.communication.v003.WebSocketService;
import hopper.library.communication.v003.WebSocketService.OnOpenedListener;
import hopper.library.communication.v003.WebSocketService.OnTransactionSeriesToTokenListener;




public class TransactionSeries {
	//static private HashMap<String, TransactionSeries> transactionSeriesHashMap;
	
	static private HashMap<String, TransactionSeriesProcess> transactionSeriesProcessHashById = new HashMap<String, TransactionSeriesProcess>();	
	//public String transactionSeriesId;
	static public void destroyProcess(String intransactionId){
		transactionSeriesProcessHashById.remove(intransactionId);
	}
	
	public TransactionSeries(){
		
		WebSocketService.getInstance("mobileService").setOnTransactionSeriesToTokenListener(new OnTransactionSeriesToTokenListener(){
			/* param0	TransportLayer		incoming
			 * para1	String				transactionSeriesId			
			*/
			@Override
			public void onTransactionSeriesToToken(Object... objects) {
				String transactionSeriesId = (String)objects[1];				
				console.log("OnTransactionSeriesToTokenListener MAIN<-->CALBACK!!!!" + transactionSeriesId);
				TransactionSeriesProcess process = transactionSeriesProcessHashById.get(transactionSeriesId);
				
				if(process != null){
					//already exist...
					TransactionSeries.reportOnMessage(objects);
					/*process.processNext(objects);
					process.processAll(objects);*/
					
				}else{
					console.log("no instance exist," + transactionSeriesId);
					// check frame, verify is 0
					if(((TransportLayer)objects[0]).routingLayer.getField("frame").equalsIgnoreCase("0")){
						//pass objects for inTransactionSeriesId/constructor of instance
						String command = ((TransportLayer)objects[0]).routingLayer.getField("command");
						process = SeriesProcessFactory.getNewInstance(command, transactionSeriesId, objects);
						
						transactionSeriesProcessHashById.put(transactionSeriesId, process);					
					}else{
						console.log("ERROR TransactionSeries onTransactionSeriesToToken call back, frame != 0 and needing instance of process, no can do!!");
					}
				}
				
				
				/*if(((String)objects[1]).equalsIgnoreCase("0")){
					
				}*/
				
				
			}
		});
	}			
				
				/*if(((String)objects[3]).equalsIgnoreCase("0")){					
					String transactionSeriesId = (String)objects[1];
					TransactionSeriesProcess transactionSeriesProcess = transactionSeriesProcessHashById.get(transactionSeriesId);
					if(transactionSeriesProcess == null){
						console.log("transactionSeriesProcess not in hashOfid");
						//create instance by factory						
						
						transactionSeriesProcess = TransactionSeriesProcess.getInstance((String)objects[1]);
						if(transactionSeriesProcess != null){
							transactionSeriesProcess.lease();
							console.log("transactionSeriesProcess puting in hshOfIds");
							transactionSeriesProcessHashById.put(transactionSeriesId, transactionSeriesProcess);
							console.log("transactionSeriesProcess setting up eventPaths");
							transactionSeriesProcess.listenNow(TransactionSeries.this);
						}else{							
							console.log("transactionSeriesProcess is missing no cmd or id");
							return;
						}
					}else{
						console.log("transactionSeriesProcess found existing in hashOfIds");
					}					
					
					
					
					
					//reportOnFirst(objects);
					//reportOnAll(objects);
				}else{
					reportOnNext(objects);
					reportOnAll(objects);
				}				
			}
			
		});	*/	

	
	/*public void next(){
		console.log("next() Runned!!!!");
	}*/
	
	//############ E V E N T S ##############################################################################################
	//--------OnMessage callback-------------------------------------------------------------
	static private ArrayList<OnMessageListener> onMessageListenerArrayList = new ArrayList<OnMessageListener>();
	
	static public interface OnMessageListener{
	    public void onMessage(Object...objects);
	}
	static public void setOnMessageListener(OnMessageListener listener) {
    	onMessageListenerArrayList.add(listener);
    }
	static private void reportOnMessage(Object...objects){
    	for(OnMessageListener listener : onMessageListenerArrayList){
    		listener.onMessage(objects);
        }
    }
    //-
 
    //############ E V E N T S ##############################################################################################
  	//--------OnAfterLastTransmit callback-------------------------------------------------------------
	static private ArrayList<OnAfterLastTransmitListener> onAfterLastTransmitListenerArrayList = new ArrayList<OnAfterLastTransmitListener>();
  	
	static public interface OnAfterLastTransmitListener{
  	    public void onAfterLastTransmit(Object...objects);
  	}
	static public void setOnAfterLastTransmitListener(OnAfterLastTransmitListener listener) {
      	onAfterLastTransmitListenerArrayList.add(listener);
      }
	static private void reportOnAfterLastTransmit(Object...objects){
      	for(OnAfterLastTransmitListener listener : onAfterLastTransmitListenerArrayList){
      		listener.onAfterLastTransmit(objects);
          }
      }
      //-
      
    //############ E V E N T S ##############################################################################################
  	//--------OnError callback-------------------------------------------------------------
	static private ArrayList<OnErrorListener> onErrorListenerArrayList = new ArrayList<OnErrorListener>();
  	
	static public interface OnErrorListener{
  	    public void onError(Object...objects);
  	}
	static public void setOnErrorListener(OnErrorListener listener) {
      	onErrorListenerArrayList.add(listener);
      }
	static private void reportOnError(Object...objects){
      	for(OnErrorListener listener : onErrorListenerArrayList){
      		listener.onError(objects);
          }
      }
      //-
      
	
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}

