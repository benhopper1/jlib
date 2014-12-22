package hopper.application.qrreader.v002;

import hopper.library.communication.v003.CommunicationHandlingBase;
import hopper.library.communication.v003.TransactionRequest;
import hopper.library.communication.v003.DataLayer;
import hopper.library.communication.v003.TransactionRequest;
import hopper.library.communication.v003.TransportLayer;
import hopper.library.communication.v003.WebSocketService;
import hopper.library.communication.v003.TransactionRequest.OnMatchListener;
import hopper.library.communication.v003.transactionseries.TransactionSeries;
import hopper.library.communication.v003.transactionseries.TransactionSeriesProcess;
import hopper.library.phone.AutoAnswerIntentService;
import hopper.library.phone.ContactInformation;
import hopper.library.phone.PhoneCallReceiver;
import hopper.library.phone.PhoneTones;
import hopper.library.sms.SmsReceiver;
import hopper.library.phone.PhoneTones;
import android.app.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;

import java.lang.reflect.Method;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.hopperlibrary.console;

public class MainActivityCommunicationHandling extends CommunicationHandlingBase {
	public MainActivityCommunicationHandling(final Activity inActivity){
		
		//####################################################################################################################################
		//------------------------------------------------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------------------------------------------------
		this.trackTransactionRequest("openPhone", 
				new TransactionRequest("openPhone", new OnMatchListener(){
		        	/**
					 * @param objects[0] TransportLayer instance			 				 * 
					 */
		 			@Override
		 			public void onMatch(Object... objects) {
		 				TransportLayer transportLayer = (TransportLayer) objects[0];
		 				String phoneNumber = transportLayer.dataLayer.getField("phoneNumber");
		 				console.log("TransactionRequest openPhone HANDLER!!!!" + phoneNumber);
		 		        //Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "2564662496"));
		 				Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
		 		        
		 		        AudioManager audioManager = (AudioManager)inActivity.getApplicationContext().getSystemService(Context.AUDIO_SERVICE); 
		 		        audioManager.setMode(AudioManager.MODE_IN_CALL);
		 		        audioManager.setSpeakerphoneOn(true);
		 		        
		 		       inActivity.startActivity(intent);			
		 			}
		         	
		         })
		);

		//####################################################################################################################################
		//------------------------------------------------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------------------------------------------------
		this.trackTransactionRequest("getContactInformation",	        
	        new TransactionRequest("getContactInformation", new OnMatchListener(){
				@Override
				public void onMatch(Object... objects) {
					console.log("TransactionRequest getContactInformation HANDLER!!!!");		        
					ContactInformation contactInformation = new ContactInformation(inActivity);
			        DataLayer dataLayer = new DataLayer();
					dataLayer
						.add("key0", "value0")
						.add("contactInformation", contactInformation.getContactsAsJsonArray())
				;
				WebSocketService.getInstance("mobileService").returnTransaction((TransportLayer)objects[0],dataLayer);
				}
	        	
	        })
		);
		
		//####################################################################################################################################
		//------------------------------------------------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------------------------------------------------
		//final AutoAnswerIntentService autoAnswerIntentService = new AutoAnswerIntentService();
		this.trackTransactionRequest("phoneAnswerPhone", 
	        new TransactionRequest("phoneAnswerPhone", new OnMatchListener(){
	 			@Override
	 			public void onMatch(Object... objects) {
	 				console.log("TransactionRequest autoAnswerIntentService HANDLER!!!!");        
	 				//autoAnswerIntentService.answerPhoneHeadsethook(inActivity);	 				
	 				PhoneCallReceiver.answerPhoneHeadsethook(inActivity.getApplicationContext());
	 				DataLayer dataLayer = new DataLayer();
					dataLayer
						.add("key0", "empty")		
					;
					WebSocketService.getInstance("mobileService").returnTransaction((TransportLayer)objects[0],dataLayer);
	 			}
	         	
	         })
		);
		
		//####################################################################################################################################
		//------------------------------------------------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------------------------------------------------ 
    
		this.trackTransactionRequest("phoneHangUp", 
	        new TransactionRequest("phoneHangUp", new OnMatchListener(){
	 			@Override
	 			public void onMatch(Object... objects) {
	 				console.log("TransactionRequest phoneHangUp HANDLER!!!!"); 
	 				PhoneCallReceiver.disconnectPhoneItelephony(inActivity);WebSocketService.getInstance("mobileService").returnTransaction(objects[0]);
	 				
	 				DataLayer dataLayer = new DataLayer();
					dataLayer
						.add("key0", "empty")		
					;
					WebSocketService.getInstance("mobileService").returnTransaction((TransportLayer)objects[0],dataLayer);
	 			}
	         	
	         })
		);
		
		//####################################################################################################################################
		//------------------------------------------------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------------------------------------------------ 
		final PhoneTones phoneTones = new PhoneTones();     
		this.trackTransactionRequest("makePhoneTone",	    
	        new TransactionRequest("makePhoneTone", new OnMatchListener(){
	   			@Override
	   			public void onMatch(Object... objects) {
	   				console.log("TransactionRequest makePhoneTone HANDLER!!!!");		        
	   				TransportLayer transportLayer = (TransportLayer) objects[0];
	 				String toneNumber = transportLayer.dataLayer.getField("toneNumber");
	 				String toneDuration = transportLayer.dataLayer.getField("toneDuration");
	 				phoneTones.produceAudioTone(toneNumber, toneDuration);
	   				
					
					WebSocketService.getInstance("mobileService").returnTransaction(objects[0]);
	   			}
	           	
	           })
		);
	        
		//####################################################################################################################################
		//------------------------------------------------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------------------------------------------------     
		this.trackTransactionRequest("phoneToSpeaker",
			new TransactionRequest("phoneToSpeaker", new OnMatchListener(){
				@Override
				public void onMatch(Object... objects) {
					console.log("TransactionRequest phoneToSpeaker HANDLER!!!!");
					PhoneCallReceiver.phoneToSpeaker(inActivity.getApplicationContext(), true);
				}
	        	
	        })
		);
	        
		//####################################################################################################################################
		//------------------------------------------------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------------------------------------------------     
		this.trackTransactionRequest("phoneToSpeakerOff",
	        new TransactionRequest("phoneToSpeakerOff", new OnMatchListener(){
				@Override
				public void onMatch(Object... objects) {
					console.log("TransactionRequest phoneToSpeakerOff HANDLER!!!!");
					PhoneCallReceiver.phoneToSpeaker(inActivity.getApplicationContext(), false);
				}
	        	
	        })
		);
		//####################################################################################################################################
		//------------------------------------------------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------------------------------------------------     
		this.trackTransactionRequest("dialPhone",        
	        new TransactionRequest("dialPhone", new OnMatchListener(){
				@Override
				public void onMatch(Object... objects) {
					TransportLayer transportLayer = (TransportLayer) objects[0];
	 				String phoneNumber = transportLayer.dataLayer.getField("phoneNumber");
					console.log("TransactionRequest openPhone HANDLER!!!!");
					PhoneCallReceiver.dialPhone(inActivity,phoneNumber);
				}
	        	
	        })
		);
	
		
		//####################################################################################################################################
		//------------------------------------------------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------------------------------------------------     
		this.trackTransactionRequest("getDeviceType",
			new TransactionRequest("getDeviceType", new OnMatchListener(){
				@Override
				public void onMatch(Object... objects) {
					console.log("TransactionRequest getDeviceType HANDLER!!!!");
					DataLayer dataLayer = new DataLayer();
					dataLayer
						.add("key0", "value0")
						.add("key1", "value1")
					;
					WebSocketService.getInstance("mobileService").returnTransaction((TransportLayer)objects[0],dataLayer);
				}
	        	
	        })
		);
		
		
		//####################################################################################################################################
		//------------------------------------------------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------------------------------------------------     
		this.trackTransactionRequest("smsGetAllIdsForPhoneNumber",
			new TransactionRequest("smsGetAllIdsForPhoneNumber", new OnMatchListener(){
				@Override
				public void onMatch(Object... objects) {
					console.log("TransactionRequest smsGetAllIdsForPhoneNumber HANDLER!!!!");
					TransportLayer transportLayer = (TransportLayer) objects[0];
	 				String top = transportLayer.dataLayer.getField("top");
	 				String phoneNumber = transportLayer.dataLayer.getField("phoneNumber");
	 				JSONArray ids_jsonArray = SmsReceiver.getAllIdsForPhoneNumberAsJsonArray(inActivity, phoneNumber);
	 				
	 				
					console.log("Top" + top);
					DataLayer dataLayer = new DataLayer();
					dataLayer
						.add("phoneNumber",phoneNumber)
						.add("smsIds", ids_jsonArray)						
					;
					WebSocketService.getInstance("mobileService").returnTransaction((TransportLayer)objects[0],dataLayer);
				}
	        	
	        })
		);
		
		//####################################################################################################################################
		//------------------------------------------------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------------------------------------------------     
		this.trackTransactionRequest("getSmsById",
			new TransactionRequest("getSmsById", new OnMatchListener(){
				@Override
				public void onMatch(Object... objects) {
					console.log("TransactionRequest getSmsByIdAsJson HANDLER!!!!");
					TransportLayer transportLayer = (TransportLayer) objects[0];
	 				String smsId = transportLayer.dataLayer.getField("smsId");
	 				String index = transportLayer.dataLayer.getField("index");
	 				
	 				JSONObject result_json = SmsReceiver.getSmsByIdAsJson(inActivity, smsId);
	 				
	 				
					console.log("id gotten:" + smsId);
					DataLayer dataLayer = new DataLayer();
					dataLayer
						.add("sms",result_json)
						.add("smsId", smsId)
						.add("index", index)	
					;
					WebSocketService.getInstance("mobileService").returnTransaction((TransportLayer)objects[0],dataLayer);
				}
	        	
	        })
		);
		
		//####################################################################################################################################
		//------------------------------------------------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------------------------------------------------     
		this.trackTransactionRequest("smsGetAllNamesAndNumbers",
			new TransactionRequest("smsGetAllNamesAndNumbers", new OnMatchListener(){
				@Override
				public void onMatch(Object... objects) {
					console.log("TransactionRequest smsGetAllNamesAndNumbers HANDLER!!!!");
						 				
					JSONArray result_jsonArray = SmsReceiver.getAllNamesAndNumbers(inActivity);
					
					DataLayer dataLayer = new DataLayer();
					dataLayer
						.add("smsMembers",result_jsonArray)						
					;
					WebSocketService.getInstance("mobileService").returnTransaction((TransportLayer)objects[0],dataLayer);
				}
	        	
	        })
		);
		
		//####################################################################################################################################
		//------------------------------------------------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------------------------------------------------     
		this.trackTransactionRequest("smsSendSmsMessage",
			new TransactionRequest("smsSendSmsMessage", new OnMatchListener(){
				@Override
				public void onMatch(Object... objects) {
					console.log("TransactionRequest smsSendSmsMessage HANDLER!!!!");
					
					TransportLayer transportLayer = (TransportLayer) objects[0];
					String phoneNumber = transportLayer.dataLayer.getField("phoneNumber");
					String smsMessage = transportLayer.dataLayer.getField("smsMessage");					
					
					SmsReceiver.sendSmsText(inActivity, phoneNumber, smsMessage);
					
					DataLayer dataLayer = new DataLayer();
					dataLayer
						.add("messageSent","true")						
					;
					WebSocketService.getInstance("mobileService").returnTransaction((TransportLayer)objects[0],dataLayer);
				}
	        	
	        })
		);
		
		
		
		
		
		
		
		
		
		
		/*//############################################TransactionSeriesProcess####################################################################
		//------------------------------------------------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------------------------------------------------	
		TransactionSeriesProcess testingTransactionSeriesProcess = new TransactionSeriesProcess("testing"){
			 param0	TransportLayer		incoming
			 * para1	String				command
			 * para2	String				stage
			 * para3	String				frame
			
			@Override
			public void processFirst(Object... objects) {
				console.log("testing callback---processFirst-------" + ((TransportLayer)objects[0]).routingLayer.getField("transactionSeriesId")+"<OK>");
				
				//this.next();				
			}

			@Override
			public void processNext(Object... objects) {
				console.log("testing callback---processNext-------");				
			}

			@Override
			public void processAll(Object... objects) {
				console.log("testing callback---processAll-------");
				this.next();
			}
			
			
		};*/
	}
	
	
}
