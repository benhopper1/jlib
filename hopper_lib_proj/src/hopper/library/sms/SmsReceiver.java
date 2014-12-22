//----manifest entry------------------------------------------------------------------
/*<receiver android:name="hopper.library.sms.SmsReceiver" >
    <intent-filter >
        <action android:name="android.provider.Telephony.SMS_RECEIVED" />
    </intent-filter>
</receiver>*/
//----end of manifest entry-----------------------------------------------------------

package hopper.library.sms;


import hopper.library.phone.v002.PhoneCallReceiver.OnIncomingCallListener;
import hopper.library.sms.SmsSentObserver.OnOutgoingSmsListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.hopperlibrary.HopperJsonStatic;
import com.example.hopperlibrary.console;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract.PhoneLookup;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.format.DateFormat;
import android.util.Pair;
import android.widget.Toast;







public class SmsReceiver extends BroadcastReceiver{   
	private static Context context;
	
	static private Activity activity;
	
	
	//observer----------
	private static final Uri STATUS_URI = Uri.parse("content://sms");
	private static SmsSentObserver smsSentObserver = null;
	
	
	static public void setup(Activity inActivity){
		activity = inActivity;
		if(smsSentObserver == null){
		    smsSentObserver = new SmsSentObserver(new Handler(), activity);
		    activity.getContentResolver().registerContentObserver(STATUS_URI, true, smsSentObserver);
	    }
	}
	
	
	
	
	//######################### E V E N T #################################################################################################################
	//-----------------------------------------------------------------------------------------------------------------------------------------------------
	//-------event onIncomingSmsListenerArrayList---------------------------------------------------------------------------------------------------------
	private static ArrayList<OnIncomingSmsListener> onIncomingSmsListenerArrayList = new ArrayList<OnIncomingSmsListener>();
	
	public static  interface OnIncomingSmsListener{
	    public void onIncomingSms(Object...objects);
	}
    public static  void setOnIncomingSmsListener(OnIncomingSmsListener listener) {
    	onIncomingSmsListenerArrayList.add(listener);
    }
	 /*
	  * @param0 String phoneNumber
	  * @param1 String name
	  * @param2	String msg	 
	  */
    private static  void reportOnIncomingSms(Object...objects){
    	for(OnIncomingSmsListener listener : onIncomingSmsListenerArrayList){
    		listener.onIncomingSms(objects);
        }
    }
    
    
    

	//######################### E V E N T #################################################################################################################
	//-----------------------------------------------------------------------------------------------------------------------------------------------------
	//-------event onOutgoingSmsListenerArrayList---------------------------------------------------------------------------------------------------------
	
    public static  void setOnOutgoingSmsListener(OnOutgoingSmsListener listener) {
    	SmsSentObserver.setOnOutgoingSmsListener(listener);
    }	
    //----------------------------------------------------------------------------------------------------------------------------------- 
	
	
    @Override
    public void onReceive(Context inContext, Intent intent){
    	
    	
	    //---get the SMS message passed in---
    	context = inContext;
	    Bundle bundle = intent.getExtras();        
	    SmsMessage[] msgs = null;
	    //String str = "";
	    
	 /*  if(smsSentObserver == null){
		    smsSentObserver = new SmsSentObserver(new Handler(), context);
		    context.getContentResolver().registerContentObserver(STATUS_URI, true, smsSentObserver);
	    } */
	    
	    
	    
	    
	    
	    
	    if (bundle != null){
	        //---retrieve the SMS message received---
	        Object[] pdus = (Object[]) bundle.get("pdus");
	        msgs = new SmsMessage[pdus.length];
	        String concatMsgString = "";
	        String phoneNumber = "";
	        for (int i=0; i<msgs.length; i++){
	            msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);                
	            /*str += "Hop0009 -> SMS from " + msgs[i].getOriginatingAddress();                     
	            str += " :";
	            str += msgs[i].getMessageBody().toString();
	            str += "\n";*/
	            //---keep
	            concatMsgString = concatMsgString + msgs[i].getMessageBody().toString();
	            if(phoneNumber.equalsIgnoreCase("")){
	            	phoneNumber = msgs[i].getOriginatingAddress();
	            }
	            
	            console.log("PDU-RAW:" + msgs[i].getPdu().toString());
	           // msgs[i].
	            
	        }
	        //---display the new SMS message---
	        Toast.makeText(context, "PHONE:" + phoneNumber + " MSG:" + concatMsgString, Toast.LENGTH_SHORT).show();
	        console.log("HopXmSg:" + "PHONE:" + phoneNumber + " MSG:" + concatMsgString );
	        String name = getContactName(inContext, phoneNumber);
	        reportOnIncomingSms(phoneNumber,name, concatMsgString);
	        
	        console.log("RESULT CODE:" + getResultCode());
	        
	        console.log("SmsManager.RESULT_ERROR_GENERIC_FAILURE:" + SmsManager.RESULT_ERROR_GENERIC_FAILURE);
	        console.log("SmsManager.STATUS_ON_ICC_FREE:" + SmsManager.STATUS_ON_ICC_FREE); 
	        console.log("SmsManager.STATUS_ON_ICC_READ:" + SmsManager.STATUS_ON_ICC_READ); 
	        console.log("SmsManager.STATUS_ON_ICC_SENT:" + SmsManager.STATUS_ON_ICC_SENT); 
	        console.log("SmsManager.STATUS_ON_ICC_SENT:" + SmsManager.STATUS_ON_ICC_SENT); 
	        console.log("SmsManager.STATUS_ON_ICC_UNSENT:" + SmsManager.STATUS_ON_ICC_UNSENT); 
	        
	       	switch (getResultCode())
	        {
	          case Activity.RESULT_OK:
	            Toast.makeText(activity.getBaseContext(), "SMS sent",
	              Toast.LENGTH_SHORT).show();
	            //reportOnOutgoingSms(phoneNumber,name, concatMsgString);
	            //reportOnOutgoingSms(number,name, msgString);
	            break;
	          case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
	            Toast.makeText(activity.getBaseContext(), "Generic failure",
	              Toast.LENGTH_SHORT).show();
	            break;
	          case SmsManager.RESULT_ERROR_NO_SERVICE:
	            Toast.makeText(activity.getBaseContext(), "No service",
	              Toast.LENGTH_SHORT).show();
	            break;
	          case SmsManager.RESULT_ERROR_NULL_PDU:
	            Toast.makeText(activity.getBaseContext(), "Null PDU",
	              Toast.LENGTH_SHORT).show();
	            break;
	          case SmsManager.RESULT_ERROR_RADIO_OFF:
	            Toast.makeText(activity.getBaseContext(), "Radio off",
	              Toast.LENGTH_SHORT).show();
	            break;
	        }
	        
	        
	    }                   
	    Toast.makeText(context, "Hey", Toast.LENGTH_SHORT).show();
    }
    
    public static String getContactName(Context context, String phoneNumber) {
	    ContentResolver cr = context.getContentResolver();
	    Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
	            Uri.encode(phoneNumber));
	    Cursor cursor = cr.query(uri,
	            new String[] { PhoneLookup.DISPLAY_NAME }, null, null, null);
	    if (cursor == null) {
	        return null;
	    }
	    String contactName = null;
	    if (cursor.moveToFirst()) {
	        contactName = cursor.getString(cursor
	                .getColumnIndex(PhoneLookup.DISPLAY_NAME));
	    }
	    if (cursor != null && !cursor.isClosed()) {
	        cursor.close();
	    }
	    return contactName;
	}
    public static JSONArray getSmsByPhoneNumberAsJsonArray(Activity inActivity, String inPhoneNumber){
		JSONArray result_jsonArray = new JSONArray();
		JSONObject current_json;
		
		Cursor cursor = inActivity.getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);
        cursor.moveToFirst();
        console.log("chk_4");
        inPhoneNumber = "+" + inPhoneNumber;
        do{
        	console.log("testh:" + cursor.getString(2)+"/"+inPhoneNumber);
        	if(inPhoneNumber.equalsIgnoreCase(cursor.getString(2))){
        		console.log("had match:" + cursor.getString(2));
        		
        		for(int j = 0; j < 17; j++){
        			console.log(cursor.getColumnName(j)+":" + cursor.getString(j));
        		}
        		
	        	current_json = new JSONObject();
	        	HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "contactName", getContactName(inActivity.getApplicationContext(), cursor.getString(2)));
	        	HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "contactPhoneNumber",cursor.getString(2));
	        	HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "thread", cursor.getString(1));
	        	HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "read", cursor.getString(7));
	        	HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "date", cursor.getString(4));
	        	HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "dateSent", cursor.getString(5));
	        	HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "subject", cursor.getString(11));
	        	HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "body", cursor.getString(12));
	        	HopperJsonStatic.putObjectIntoArray(result_jsonArray, current_json);
	        	console.log("countUp" + (current_json.toString().length() + result_jsonArray.toString().length()));
        	}
        }while(cursor.moveToNext());		
		
		return result_jsonArray;
    }
    
    
   
    
    public static JSONArray getSmsByTreadIdAsJsonArray(Activity inActivity, String inThreadId){
		JSONArray result_jsonArray = new JSONArray();
		JSONObject current_json;
		
		Cursor cursor = inActivity.getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);
        cursor.moveToFirst();
        console.log("chk_4");
        do{
        	console.log("testh:" + cursor.getString(1)+"/"+inThreadId);
        	if(inThreadId.equalsIgnoreCase(cursor.getString(1))){
        		console.log("had match:" + cursor.getString(1));
	        	current_json = new JSONObject();
	        	HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "contactName", getContactName(inActivity.getApplicationContext(), cursor.getString(2)));
	        	HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "contactPhoneNumber",cursor.getString(2));
	        	HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "thread", cursor.getString(1));
	        	HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "read", cursor.getString(7));
	        	HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "date", cursor.getString(4));
	        	HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "dateSent", cursor.getString(5));
	        	HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "subject", cursor.getString(11));
	        	HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "body", cursor.getString(12));
	        	HopperJsonStatic.putObjectIntoArray(result_jsonArray, current_json);
	        	console.log("countUp" + (current_json.toString().length() + result_jsonArray.toString().length()));
        	}
        }while(cursor.moveToNext());		
		
		return result_jsonArray;
    }
    
    public static JSONObject getSmsByIdAsJson(Activity inActivity, String inId){		
		JSONObject current_json = new JSONObject();
		
		Cursor cursor = inActivity.getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);
        cursor.moveToFirst();
        console.log("chk_4");       
        do{
        	console.log("testh:" + cursor.getString(0)+"/"+inId);
        	if(inId.equalsIgnoreCase(cursor.getString(0))){
        		console.log("had match:" + cursor.getString(0));
        		HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "id",cursor.getString(0));
	        	HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "contactName", getContactName(inActivity.getApplicationContext(), cursor.getString(2)));
	        	HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "contactPhoneNumber",cursor.getString(2));
	        	HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "thread", cursor.getString(1));
	        	HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "read", cursor.getString(7));
	        	HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "date", cursor.getString(4));
	        	HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "dateSent", cursor.getString(5));
	        	HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "subject", cursor.getString(11));
	        	HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "body", cursor.getString(12));
	        	break;
        	}
        }while(cursor.moveToNext());		
		
		return current_json;
    }   
    
    
    public static void sendSmsText(Activity inActivity, String inPhoneNumber, String inText){    	        
        SmsManager sm = SmsManager.getDefault();        
        ArrayList<String> parts = sm.divideMessage(inText);
        
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";
        
        Intent iSent = new Intent(SENT);
        PendingIntent piSent = PendingIntent.getBroadcast(inActivity, 0, iSent, 0);
        Intent iDel = new Intent(DELIVERED);
        PendingIntent piDel = PendingIntent.getBroadcast(inActivity, 0, iDel, 0);

        if (parts.size() == 1){
            String msg = parts.get(0);
            sm.sendTextMessage(inPhoneNumber, null, msg, piSent, piDel);
        }else{
            ArrayList<PendingIntent> sentPis = new ArrayList<PendingIntent>();      
            ArrayList<PendingIntent> delPis = new ArrayList<PendingIntent>();       

            int ct = parts.size();
            for (int i = 0; i < ct; i++){
                sentPis.add(i, piSent);
                delPis.add(i, piDel);
            }

            sm.sendMultipartTextMessage(inPhoneNumber, null, parts, sentPis, delPis);
        }
        
        ContentValues values = new ContentValues();        
        values.put("address", inPhoneNumber);                  
        values.put("body", inText);                  
        inActivity.getContentResolver().insert(Uri.parse("content://sms/sent"), values);
    }
    
    public static JSONArray getAllIdsForTreadIdAsJsonArray(Activity inActivity, String inThreadId){
		JSONArray result_jsonArray = new JSONArray();	
		Cursor cursor = inActivity.getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);
        cursor.moveToFirst();
        console.log("chk_4");
        do{
        	console.log("testh:" + cursor.getString(1)+"/"+inThreadId);
        	if(inThreadId.equalsIgnoreCase(cursor.getString(1))){
        		console.log("had match:" + cursor.getString(1));
        		HopperJsonStatic.putValueInJsonArray(result_jsonArray, cursor.getString(0));	        	
        	}
        }while(cursor.moveToNext());		
		
		return result_jsonArray;
    }
    
    public static JSONArray getAllIdsForPhoneNumberAsJsonArray(Activity inActivity, String inPhoneNumber){
    	inPhoneNumber = "+" + inPhoneNumber;
 		JSONArray result_jsonArray = new JSONArray();	
 		Cursor cursor = inActivity.getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);
         cursor.moveToFirst();
         console.log("chk_4");
         do{
         	console.log("testh:" + cursor.getString(2)+"/"+inPhoneNumber);
         	if(inPhoneNumber.equalsIgnoreCase(cursor.getString(2))){
         		console.log("had match:" + cursor.getString(2));
         		HopperJsonStatic.putValueInJsonArray(result_jsonArray, cursor.getString(0));	        	
         	}
         }while(cursor.moveToNext());		
 		
 		return result_jsonArray;
     }
    
    public static JSONArray getAllNamesAndNumbers(Activity inActivity){
    	JSONArray result_jsonArray = new JSONArray();
 		Cursor cursor = inActivity.getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);
        cursor.moveToFirst();        
        HashSet<String> setOfNumbers = new HashSet<String>();
        do{
        	setOfNumbers.add(cursor.getString(2));
        }while(cursor.moveToNext());
        
        JSONObject current_json;
        for(String theNumber : setOfNumbers){
        	current_json = new JSONObject();
        	HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "phoneNumber", theNumber);
        	HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "contactName", getContactName(inActivity.getApplicationContext(),theNumber));
        	HopperJsonStatic.putObjectIntoArray(result_jsonArray, current_json);
        }        
		
		return result_jsonArray;
    }
    
    public static ArrayList<JSONObject> getSmsSentAsArrayListOfJson(Activity inActivity, String inPhoneNumber){		
		JSONObject current_json; // = new JSONObject();
		ArrayList<JSONObject> arrayList_json = new ArrayList<JSONObject>();
		
		Cursor cursor = inActivity.getContentResolver().query(Uri.parse("content://sms/sent"), null, null, null, null);
        cursor.moveToFirst();
        console.log("chk_4");       
        do{
        	if(PhoneNumberUtils.compare(cursor.getString(2),inPhoneNumber)){//inId.equalsIgnoreCase(cursor.getString(0))){
        		current_json = new JSONObject();
        		console.log("had match:" + cursor.getString(0));
        		HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "id",cursor.getString(0));
        		HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "smsContext","sent");
	        	HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "contactName", getContactName(inActivity.getApplicationContext(), cursor.getString(2)));
	        	HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "contactPhoneNumber",cursor.getString(2));
	        	HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "thread", cursor.getString(1));
	        	HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "read", cursor.getString(7));
	        	HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "date", getFormatedDate(cursor.getString(4)));
	        	HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "dateSent", getFormatedDate(cursor.getString(5)));
	        	HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "subject", cursor.getString(11));
	        	HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "body", cursor.getString(12));
	        	HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "cleanContactPhoneNumber", PhoneNumberUtils.extractNetworkPortion(cursor.getString(2)));
	        	console.log("OUTPUT:"+current_json.toString());//break;
	        	
	        	arrayList_json.add(current_json);
        	}
        }while(cursor.moveToNext());
		return arrayList_json;
    }
    
    public static ArrayList<JSONObject> getSmsInboxAsArrayListOfJson(Activity inActivity, String inPhoneNumber){		
  		JSONObject current_json; // = new JSONObject();
  		ArrayList<JSONObject> arrayList_json = new ArrayList<JSONObject>();
  		
  		Cursor cursor = inActivity.getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);
          cursor.moveToFirst();
          console.log("chk_4");       
          do{
          	if(PhoneNumberUtils.compare(cursor.getString(2),inPhoneNumber)){//inId.equalsIgnoreCase(cursor.getString(0))){
          		current_json = new JSONObject();
          		console.log("had match:" + cursor.getString(0));
          		HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "id",cursor.getString(0));
          		HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "smsContext","inBox");
  	        	HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "contactName", getContactName(inActivity.getApplicationContext(), cursor.getString(2)));
  	        	HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "contactPhoneNumber",cursor.getString(2));
  	        	HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "thread", cursor.getString(1));
  	        	HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "read", cursor.getString(7));
  	        	HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "date", getFormatedDate(cursor.getString(4)));
  	        	HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "dateSent", getFormatedDate(cursor.getString(5)));
  	        	HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "subject", cursor.getString(11));
  	        	HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "body", cursor.getString(12));
  	        	HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "cleanContactPhoneNumber", PhoneNumberUtils.extractNetworkPortion(cursor.getString(2)));
  	        	console.log("OUTPUT:"+current_json.toString());//break;
  	        	
  	        	arrayList_json.add(current_json);
          	}
          }while(cursor.moveToNext());
  		return arrayList_json;
      }
    
    
    /*#######################################################################################################################################
     *--------------------------- > ----	g e t S m s { ? ? ? } A b o v e I d A s A r r a y L i s t O f J s o n ----- < -------------------
     *#######################################################################################################################################
     */
    
    public static ArrayList<JSONObject> getSmsSentAboveIdAsArrayListOfJson(Activity inActivity, String inLastId){		
		Integer lastId = Integer.parseInt(inLastId);
    	
    	JSONObject current_json;
		ArrayList<JSONObject> arrayList_json = new ArrayList<JSONObject>();
		
		Cursor cursor = inActivity.getContentResolver().query(Uri.parse("content://sms/sent"), null, null, null, null);
        cursor.moveToFirst();
        console.log("chk_4");       
        do{
        	if(lastId.compareTo(cursor.getInt(0)) < 0){
        		current_json = new JSONObject();
        		console.log("had match:" + cursor.getString(0));
        		HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "id",cursor.getString(0));
        		HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "smsContext","sent");
	        	HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "contactName", getContactName(inActivity.getApplicationContext(), cursor.getString(2)));
	        	HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "contactPhoneNumber",cursor.getString(2));
	        	HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "thread", cursor.getString(1));
	        	HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "read", cursor.getString(7));
	        	HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "date", getFormatedDate(cursor.getString(4)));
	        	HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "dateSent", getFormatedDate(cursor.getString(5)));
	        	HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "subject", cursor.getString(11));
	        	HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "body", cursor.getString(12));
	        	HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "cleanContactPhoneNumber", PhoneNumberUtils.extractNetworkPortion(cursor.getString(2)));
	        	console.log("OUTPUT:"+current_json.toString());//break;
	        	
	        	arrayList_json.add(current_json);
        	}
        }while(cursor.moveToNext());
		return arrayList_json;
    }
    
        
    public static ArrayList<JSONObject> getSmsInboxAboveIdAsArrayListOfJson(Activity inActivity, String inLastId){
    	Integer lastId = Integer.parseInt(inLastId);
		JSONObject current_json; // = new JSONObject();
		ArrayList<JSONObject> arrayList_json = new ArrayList<JSONObject>();
		
		Cursor cursor = inActivity.getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);
        cursor.moveToFirst();
        console.log("chk_4");       
        do{
        	if(lastId.compareTo(cursor.getInt(0)) < 0){
        		current_json = new JSONObject();
        		console.log("had match:" + cursor.getString(0));
        		HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "id",cursor.getString(0));
        		HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "smsContext","inBox");
	        	HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "contactName", getContactName(inActivity.getApplicationContext(), cursor.getString(2)));
	        	HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "contactPhoneNumber",cursor.getString(2));
	        	HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "thread", cursor.getString(1));
	        	HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "read", cursor.getString(7));
	        	HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "date", getFormatedDate(cursor.getString(4)));
	        	HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "dateSent", getFormatedDate(cursor.getString(5)));
	        	HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "subject", cursor.getString(11));
	        	HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "body", cursor.getString(12));
	        	HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "cleanContactPhoneNumber", PhoneNumberUtils.extractNetworkPortion(cursor.getString(2)));
	        	console.log("OUTPUT:"+current_json.toString());//break;	        	
	        	arrayList_json.add(current_json);
        	}
        }while(cursor.moveToNext());
		return arrayList_json;
    }
    
    //--------------------------------------------------------------------------------------------------------------------------------------------
    
    
    
    
    
    
    static public String getFormatedDate(String inString){
    	SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
    	Calendar calendar = Calendar.getInstance();
    	//long now = 1293457709636L;
    	Long now = Long.parseLong(inString);
    	calendar.setTimeInMillis(now);
    	return formatter.format(calendar.getTime());
    } 
    
    
}