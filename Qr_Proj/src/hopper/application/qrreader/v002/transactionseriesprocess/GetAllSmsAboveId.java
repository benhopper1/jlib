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

import android.util.Pair;

import com.example.hopperlibrary.HopperJsonStatic;
import com.example.hopperlibrary.console;

public class GetAllSmsAboveId   extends TransactionSeriesProcess{

	static{ 
		console.log("GetAllSmsAboveId LOADED");			
	}
	
	
	
	
	
	public int index = 0;
	
	
	public GetAllSmsAboveId(){
		console.log("GetAllSmsAboveId() instanated!!");
	}



	@Override
	public void processFirst(Object... objects){
		console.log("Test processFirst");
		TransportLayer transportLayer = (TransportLayer)objects[0];		
		toProcessArrayOfJson = SmsReceiver.getSmsSentAboveIdAsArrayListOfJson(MainActivity.activity, transportLayer.dataLayer.getField("lastId"));
		toProcessArrayOfJson.addAll(SmsReceiver.getSmsInboxAboveIdAsArrayListOfJson(MainActivity.activity, transportLayer.dataLayer.getField("lastId")));
		iterator = toProcessArrayOfJson.listIterator();
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
}