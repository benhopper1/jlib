package hopper.library.communication.v003.transactionseries;

import java.util.HashMap;

import android.content.Context;

import com.example.hopperlibrary.console;

import hopper.library.communication.v003.DataLayer;
import hopper.library.communication.v003.TransportLayer;
import hopper.library.communication.v003.WebSocketService;
import hopper.library.communication.v003.transactionseries.TransactionSeries.OnAfterLastTransmitListener;
import hopper.library.communication.v003.transactionseries.TransactionSeries.OnErrorListener;
import hopper.library.communication.v003.transactionseries.TransactionSeries.OnMessageListener;


public class TransactionSeriesProcess{
	static { console.log("TransactionSeriesProcess LOADED"); }
	/*static private HashMap<String, TransactionSeriesProcess> transactionSeriesProcessHashMap = new HashMap<String, TransactionSeriesProcess>();
	static public TransactionSeriesProcess getInstance(String inCommand){
		return transactionSeriesProcessHashMap.get(inCommand);
	}*/
	
	TransportLayer transportLayer;
	private String command;
	private String transactionSeriesId;
	
	public Context context;
	
	public void setContext(Context inContext){
		context = inContext;
	}
	
	public TransactionSeriesProcess(){
		console.log("TransactionSeriesProcess() instanated!! NULL CONSTRUCTOR??");
	}
	
	public void postConstructor(String inTransactionSeriesId, Object... inFirstTransmitedObjects){
		
		transportLayer = (TransportLayer)inFirstTransmitedObjects[0];
		transactionSeriesId = inTransactionSeriesId;
		
		processFirst(inFirstTransmitedObjects);
		processAll(inFirstTransmitedObjects);
		
		
		//#############################################################################################################
		//-------------------------- > E V E N T   S U B S C R I P T I O N < ------------------------------------------
		//#############################################################################################################
		TransactionSeries.setOnMessageListener(new OnMessageListener(){
			/*
			 * param0	TransportLayer		incoming
			 * para1	String				transactionSeriesId
			 */
			@Override
			public void onMessage(Object... objects){				
				if(((String)objects[1]).equalsIgnoreCase(TransactionSeriesProcess.this.transactionSeriesId)){
					console.log("TransactionSeriesProcess onMessage callback");
					TransactionSeriesProcess.this.transportLayer = (TransportLayer)objects[0];
					String frame = transportLayer.routingLayer.getField("frame");
					if(frame.equalsIgnoreCase("0")){
						//first ignored, should have got handled in constructor....
						console.log("ERROR, onMessage TransactionSeriesProcess frame == 0, should not been subscribed yet!!!");
					}else{
						processNext(objects);
						processAll(objects);						
					}
				}
				//TransportLayer transportLayer = ()objects[]
				
			}
		});
		
		TransactionSeries.setOnAfterLastTransmitListener(new OnAfterLastTransmitListener(){
			/*
			 * param0	TransportLayer		incoming
			 * para1	String				transactionSeriesId
			 */
			@Override
			public void onAfterLastTransmit(Object... objects){
				if(((String)objects[1]).equalsIgnoreCase(TransactionSeriesProcess.this.transactionSeriesId)){
					console.log("TransactionSeriesProcess onAfterLastTransmit callback");
				}
				
			}
		});
		
		TransactionSeries.setOnErrorListener(new OnErrorListener(){
			/*
			 * param0	TransportLayer		incoming
			 * para1	String				transactionSeriesId
			 */
			@Override
			public void onError(Object... objects) {
				if(((String)objects[1]).equalsIgnoreCase(TransactionSeriesProcess.this.transactionSeriesId)){
					console.log("TransactionSeriesProcess onError callback");
				}
				
			}
			
		});			
		
	}
	//#############################################################################################################
	//-------------------------- > N e x t  < ------------------------------------------
	public void next(){
		console.log("next() entered");
		next(null);
	}
	
	public void next(DataLayer inDataLayer){
		console.log("next(DataLayer) entered");
		transportLayer	
			.routingLayer.add("stage", "targetOut")
			.add("hasNext", "1")
		;
		if(inDataLayer != null){
			transportLayer.dataLayer(inDataLayer);
		}
		//targetOut
		console.log("sending out");
		WebSocketService.getInstance("mobileService").send(transportLayer.toString());
		console.log("sending out done");
		
	}
	
	//#############################################################################################################
	//-------------------------- > L a s t   < ------------------------------------------
	public void last(){
		last(null);
	}
	
	public void last(DataLayer inDataLayer){
		transportLayer	
			.routingLayer.add("stage", "targetFinal")
			.add("hasNext", "0")
		;
		if(inDataLayer != null){
			transportLayer.dataLayer(inDataLayer);
		}
		//targetOut
		WebSocketService.getInstance("mobileService").send(transportLayer.toString());
		TransactionSeries.destroyProcess(transactionSeriesId);
	}
	
	public void xxx(){
		
	}
	
	
	
		
	public void processFirst(Object... objects){}
	public void processNext(Object... objects){}
	public void processAll(Object... objects){}
	//public void processLast(Object... objects){}
	
	
	
	
	
	
	
	
	
	
	
	
}
