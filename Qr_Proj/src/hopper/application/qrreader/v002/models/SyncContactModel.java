package hopper.application.qrreader.v002.models;

import hopper.library.communication.v003.RoutingLayer;
import hopper.library.communication.v003.TransportLayer;
import hopper.library.communication.v003.WebSocketService;
import hopper.library.communication.v003.WebSocketService.OnTransactionListener;
import hopper.library.phone.Contact;

import org.json.JSONObject;

import android.app.Activity;

import com.example.hopperlibrary.HopperJsonStatic;
import com.example.hopperlibrary.console;

public class SyncContactModel {
	private Activity activity;
	private final String transmitByteSizeCap = "3400";
	private final String[] properties = 
		{
			"id",
			"name",
			"phoneNumber",
			"rawId",
			"imageUrl",
			"emailAddress",
			"accountName",
			"accountType",
			"ringTone",
			"starred",
			"sourceId",
			"timesContacted",	
			"contactId",
			"phoneType ",
			"phoneLabel",
			"phoneThumbnailPhotoUri",
			"phonePhotoUri",
			"phoneNumber"
		};
	
	public SyncContactModel(Activity inActivity){
		activity = inActivity;
	}
	
	//, boolean hasNext
	// recursive async style
	private void iterateSyncData(String whereTo){
		console.log("iterateSyncData ENTERED WhereTo:" + whereTo);
		//final JSONObject prepared_json = Contact.toJson(whereTo, transmitByteSizeCap, properties);
		final JSONObject prepared_json = Contact.toJson(whereTo, transmitByteSizeCap, properties);
		console.log("prepared_json:" + prepared_json.toString());
		if(prepared_json == null){console.loge("syncContactToServer the prepared_json is NULL"); return;}
		console.log("iterateSyncData prepared_json built");
		JSONObject data_json = new JSONObject();
		HopperJsonStatic.putArrayIntoObjectWithKey(data_json, HopperJsonStatic.getJSONArray(prepared_json, "contactArray"),"data"); //, "data")(data_json, "data", HopperJsonStatic.getJSONArray(prepared_json, "contactArray")); //getJsonObjectFromKey(prepared_json, "contactArray"));
		TransportLayer transactionTransportLayer = TransportLayer.createTransportLayer();
		transactionTransportLayer
				.addToDataLayer("startPosition", HopperJsonStatic.getStringFromKeyForJsonObject(prepared_json, "startPosition"))
				.addToDataLayer("endPosition", HopperJsonStatic.getStringFromKeyForJsonObject(prepared_json, "endPosition"))
				.addToDataLayer("hasNext", HopperJsonStatic.getStringFromKeyForJsonObject(prepared_json, "hasNext"))
				.addToDataLayer("byteSize", HopperJsonStatic.getStringFromKeyForJsonObject(prepared_json, "byteSize"))
				.addToDataLayer("data", HopperJsonStatic.getJSONArray(prepared_json, "contactArray"))
				
				.addToRoutingLayer("type", RoutingLayer.TypesEnum.transactionToServer.toString())
				.addToRoutingLayer("command", "contactDataFromSourceForSync")
		;
		
		WebSocketService.getInstance("mobileService").sendAsTransaction(transactionTransportLayer, new OnTransactionListener() {
			@Override
			public void onTransaction(Object... objects) {
				console.log("Transaction type done and back");
				String whereTo = HopperJsonStatic.getStringFromKeyForJsonObject(prepared_json, "endPosition");				
				whereTo =  String.valueOf(Integer.parseInt(whereTo) + 1);
				boolean hasNext = (Integer.parseInt(HopperJsonStatic.getStringFromKeyForJsonObject(prepared_json, "hasNext")) != 0);
				if(hasNext){
					iterateSyncData(whereTo);
				}else{
					console.log("done With transmiting!!!!");
				}
			}

		});						
	}
	
	
	
	
	
	public void syncContactToServer(){
		Contact.setup(activity);
		//Contact.refresh();
		deleteContactsFromDb();//then send in promise
		//iterateSyncData("0");		
	}
	
	public void deleteContactsFromDb(){
		TransportLayer transactionTransportLayer = TransportLayer.createTransportLayer();
		transactionTransportLayer				
				.addToRoutingLayer("type", RoutingLayer.TypesEnum.transactionToServer.toString())
				.addToRoutingLayer("command", "deleteAllDeviceContactsFromDb")
		;
		WebSocketService.getInstance("mobileService").sendAsTransaction(transactionTransportLayer, new OnTransactionListener() {
			@Override
			public void onTransaction(Object... objects) {
				console.log("deleteAllDeviceContactsFromDb Transaction type done and back");
				iterateSyncData("0");
			}

		});			
	}
	
	
	

	
	
}
