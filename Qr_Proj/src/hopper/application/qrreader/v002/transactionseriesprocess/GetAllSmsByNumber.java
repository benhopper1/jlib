package hopper.application.qrreader.v002.transactionseriesprocess;



import hopper.application.qrreader.v002.MainActivity;
import hopper.library.communication.v003.DataLayer;
import hopper.library.communication.v003.TransportLayer;
import hopper.library.communication.v003.transactionseries.TransactionSeriesProcess;
import hopper.library.sms.SmsReceiver;

import java.util.ArrayList;
import java.util.ListIterator;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.hopperlibrary.HopperJsonStatic;
import com.example.hopperlibrary.console;



import android.app.Activity;
import android.util.Pair;


public class GetAllSmsByNumber  extends TransactionSeriesProcess{

	static{ 
		console.log("GetAllSmsByNumber LOADED");			
	}
	
	
	
	
	
	public int index = 0;
	
	
	public GetAllSmsByNumber(){
		console.log("GetAllSmsByNumber() instanated!!");
	}



	@Override
	public void processFirst(Object... objects){
		console.log("Test processFirst");
		TransportLayer transportLayer = (TransportLayer)objects[0];		
		toProcessArrayOfJson = SmsReceiver.getSmsSentAsArrayListOfJson(MainActivity.activity, transportLayer.dataLayer.getField("phoneNumber"));
		toProcessArrayOfJson.addAll(SmsReceiver.getSmsInboxAsArrayListOfJson(MainActivity.activity, transportLayer.dataLayer.getField("phoneNumber")));
		iterator = toProcessArrayOfJson.listIterator();
	}



	@Override
	public void processNext(Object... objects) {
		console.log("Test processNext");		
		//super.processNext(objects);
	}



	@Override
	public void processAll(Object... objects) {
		console.log("Test processAll");		
		Pair<DataLayer, Boolean> processResult = process();
		//boolean hasNext
		if(processResult.second){
			next(processResult.first);
		}else{
			last(processResult.first);
		}
		
	}
	
	
	private int capByteSize = 3600;
	private ArrayList<JSONObject> toProcessArrayOfJson;
	private ListIterator<JSONObject> iterator;
	/*
	 * ######################################################################
	 * ----------P R O C E S S-----------------------------------------------
	 * bool == hasNext
	*/
	public Pair<DataLayer, Boolean> process(){
		int currentSize = 0;
		JSONArray result_jsonArray = new JSONArray();
		DataLayer result_dataLayer = new DataLayer();		
		
		//iterator; = toProcessArrayOfJson.iterator();
		while(iterator.hasNext()){
			//testSize before actual iterate--
			int nextByteSize =  getByteSize(toProcessArrayOfJson.get(iterator.nextIndex()));
			if((currentSize + nextByteSize) < capByteSize){
				currentSize = currentSize + nextByteSize;
				HopperJsonStatic.putObjectIntoArray(result_jsonArray, iterator.next());
			}else{
				// has next left
				result_dataLayer.add("dataArray", result_jsonArray);
				Pair<DataLayer, Boolean> result_pair = new Pair<DataLayer, Boolean>(result_dataLayer, true);
				return result_pair;				
			}
		}
		
		//-- no next-----
		result_dataLayer.add("dataArray", result_jsonArray);
		Pair<DataLayer, Boolean> result_pair = new Pair<DataLayer, Boolean>(result_dataLayer, false);
		return result_pair;	
	}
	
	public int getByteSize(JSONObject inJson){
		return inJson.toString().length();
	}
	
	
	
	
	
	/*private DataLayer getDataIterate(int inIndex){		
		
		JSONObject base_json;
		JSONArray base_jsonArray = new JSONArray() ;
		for(int i = 0; i < 100; i++){
			base_json = new JSONObject();
			HopperJsonStatic.putKeyValueStringsForJsonObject(base_json, "row", String.valueOf(i));
			HopperJsonStatic.putObjectIntoArray(base_jsonArray, base_json);
		}	
		
		
		DataLayer dataLayer = new DataLayer();
		dataLayer
			.add("startPosition", "startPosition" + String.valueOf(inIndex))
			.add("endPosition","startPosition" + String.valueOf(inIndex))
			.add("hasNext", "startPosition"+ String.valueOf(inIndex))
			.add("byteSize", "startPosition"+ String.valueOf(inIndex))
			.add("dataArray", base_jsonArray)
		;
				
		return dataLayer;
				
	}*/
	
	
	

}
