package hopper.library.phone;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.hopperlibrary.HopperJsonStatic;
import com.example.hopperlibrary.console;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Pair;




//Contacts.DISPLAY_NAME
public class PhoneContacts {
	
	private Context context;
	
	public PhoneContacts(Context inContext){
		context = inContext;
	}
	
	public ArrayList<JSONObject> toArrayListOfJson(){
		console.log("toArrayListOfJson---");
		ArrayList<JSONObject> result_jsonArrayList = new ArrayList<JSONObject>();
		console.log("toArrayListOfJson---2");
		ArrayList<Pair<String, String>> contactsArrayList = getNamesAndNumbersAsArrayListOfPair();
		console.log("toArrayListOfJson---3");
		for(Pair<String, String> thePair : contactsArrayList){
			console.log("for:" + thePair.first);
			JSONObject theJson_json = new JSONObject();
			HopperJsonStatic.putKeyValueStringsForJsonObject(theJson_json, "name", thePair.first);
			HopperJsonStatic.putKeyValueStringsForJsonObject(theJson_json, "number", thePair.second);
			result_jsonArrayList.add(theJson_json);			
		}
		console.log("toArrayListOfJson---4 post for");
		return result_jsonArrayList;
	}
	
	public JSONArray getAsJsonArray(){
		JSONArray result_jsonArray = new JSONArray();
		ArrayList<Pair<String, String>> contactsArrayList = getNamesAndNumbersAsArrayListOfPair();
		for(Pair<String, String> thePair : contactsArrayList){
			JSONObject theJson_json = new JSONObject();
			HopperJsonStatic.putKeyValueStringsForJsonObject(theJson_json, "name", thePair.first);
			HopperJsonStatic.putKeyValueStringsForJsonObject(theJson_json, "number", thePair.second);
			HopperJsonStatic.putObjectIntoArray(result_jsonArray, theJson_json);
		}		
		return result_jsonArray;
	}
	
	public void dump(){
		console.log(getAsJsonArray().toString());
	}


	public ArrayList<Pair<String, String>> getNamesAndNumbersAsArrayListOfPair(){
		if(context == null){console.log("context == null");}
		 ContentResolver cr = context.getContentResolver();
	     Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
	     ArrayList<Pair<String, String>> resultArrayList = new ArrayList<Pair<String, String>>();
	     if (cur.getCount() > 0) {
	         while (cur.moveToNext()) {
	             String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
	             String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
	
	             if (("1").equals(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)))) {
	                 Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[] { id }, null);
	                 int i = 0;
	                 int pCount = pCur.getCount();
	                 String[] phoneNum = new String[pCount];
	                 String[] phoneType = new String[pCount];
	                 String[] phoneName = new String[pCount];
	                 while (pCur.moveToNext()) {
	                	 phoneType[i] = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
	                     phoneNum[i] = cleanPhoneNumeber(pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));	                     
	                     if(phoneType[i].equals("1")){
	                    	 phoneName[i] = name + " [HOME]";
	                     }
	                     if(phoneType[i].equals("2")){
	                    	 phoneName[i] = name + " [MOBILE]";
	                     }
	                     if(phoneType[i].equals("3")){
	                    	 phoneName[i] = name + " [WORK]";
	                     }
	                     if(phoneType[i].equals("4")){
	                    	 phoneName[i] = name + " [OTHER]";
	                     }
	                     //console.log("PHONE:" + phoneNum[i] + " TYPE:" + phoneType[i] + " NAME:" + phoneName[i]);
	                     Pair<String,String> thePair = new Pair<String,String>(phoneName[i],phoneNum[i]);
	                     resultArrayList.add(thePair);
	                     i++;
	                 } 
	             } 
	
	         }
	         
	         return resultArrayList;
	
	     }
	     
	     return null;	
	}
	
	public String cleanPhoneNumeber(String inPhoneNumber){
		
		String tmpNumber = "";
		for(int i = 0; i < inPhoneNumber.length(); i++){
			int val;
			try{
				val = Integer.parseInt(String.valueOf(inPhoneNumber.charAt(i)));
				tmpNumber = tmpNumber + String.valueOf(val);
			}catch(NumberFormatException e){
				
			}
		}
		inPhoneNumber = tmpNumber;
		if(inPhoneNumber.length() == 10){
			inPhoneNumber = "1" + inPhoneNumber;
		}		
		
		inPhoneNumber = inPhoneNumber.replaceAll("(\\d{3})(\\d{3})(\\d{4})", "$1$2$3");
		return inPhoneNumber;	
	}
}