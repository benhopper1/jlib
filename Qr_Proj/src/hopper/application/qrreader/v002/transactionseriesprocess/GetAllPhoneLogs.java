package hopper.application.qrreader.v002.transactionseriesprocess;

import hopper.library.communication.v003.DataLayer;
import hopper.library.communication.v003.TransportLayer;
import hopper.library.communication.v003.transactionseries.TransactionSeriesProcess;
import hopper.library.phone.CallLog;
import hopper.library.phone.PhoneContacts;

import java.util.ArrayList;
import java.util.ListIterator;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Pair;

import com.example.hopperlibrary.HopperJsonStatic;
import com.example.hopperlibrary.console;

public class GetAllPhoneLogs extends TransactionSeriesProcess{

	static{ 
		console.log("GetAllPhoneLogs LOADED");			
	}
	
	
	
	
	
	public int index = 0;
	
	
	public GetAllPhoneLogs(){
		console.log("GetAllPhoneLogs() instanated!!");
	}



	@Override
	public void processFirst(Object... objects){
		console.log("GetAllPhoneContacts processFirst");
		TransportLayer transportLayer = (TransportLayer)objects[0];
		//PhoneContacts phoneContacts = new PhoneContacts(context);
		CallLog callLog = new CallLog(context);
        String theLastId = transportLayer.dataLayer.getField("lastId");
        if(!(theLastId.equals(""))){
        	toProcessArrayOfJson = callLog.getCallDetailsAsArrayListOfjson(Integer.parseInt(theLastId));
        }else{
        	toProcessArrayOfJson = callLog.getCallDetailsAsArrayListOfjson();
        }
		
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