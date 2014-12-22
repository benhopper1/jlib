package hopper.library.phone;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.hopperlibrary.HashOfArray;
import com.example.hopperlibrary.HopperJsonStatic;
import com.example.hopperlibrary.console;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.util.Log;

public class Contact {
	
	
	//##### -------- S T A T I C ---------#############################
	static Activity activity;
	static private HashOfArray<Contact> contactHashOfArray;// = new HashOfArray<Contact>();
	static public void setup(Activity inActivity){
		activity = inActivity;
		refresh();
	}
	
	static public void add(Contact inContact){
		//--contactId
		contactHashOfArray.add(inContact.getContactId(), inContact);
	}
	static public ArrayList<Contact> getArrayList(String inContactId){
		return contactHashOfArray.getArrayList(inContactId)	;	
	}
	
	static public ArrayList<Contact> getArrayList(){
		ArrayList<Contact> resultArrayList = new ArrayList<Contact>();
		for(String searchContactId : contactHashOfArray.getKeys()){
			ArrayList<Contact> contacts = contactHashOfArray.getArrayList(searchContactId);
			for(Contact theContact : contacts){				
					resultArrayList.add(theContact);				
			}
		}
		console.log("getArrayList DONE");
		return resultArrayList;
	}
	
	static public ArrayList<Contact> getArrayListByPhoneNumber(String inPhoneNumber){
		ArrayList<Contact> resultArrayList = new ArrayList<Contact>();
		for(String searchContactId : contactHashOfArray.getKeys()){
			ArrayList<Contact> contacts = contactHashOfArray.getArrayList(searchContactId);
			for(Contact theContact : contacts){
				if(getPhoneNumberOnlyNumeric(theContact.getPhoneNumber()).equalsIgnoreCase(getPhoneNumberOnlyNumeric(inPhoneNumber))){
					resultArrayList.add(theContact);
				}
			}
		}
		
		return resultArrayList;
	}
	
	static public Contact getFirstByPhoneNumber(String inPhoneNumber){		
		for(String searchContactId : contactHashOfArray.getKeys()){
			ArrayList<Contact> contacts = contactHashOfArray.getArrayList(searchContactId);
			for(Contact theContact : contacts){
				console.log("-->:" + theContact.getPhoneNumber());
				if(theContact.getPhoneNumber() == null){break;}
				if(getPhoneNumberOnlyNumeric(theContact.getPhoneNumber()).equalsIgnoreCase(getPhoneNumberOnlyNumeric(inPhoneNumber))){
					return theContact;
				}
			}
		}		
		return null;
	}
	
	static public void dump(){
		contactHashOfArray.dump();
	}
	static public String getPhoneNumberOnlyNumeric(String inPhoneNumber){
		if(inPhoneNumber == null){return null;}
		String result = "";
		for(int i = 0; i < inPhoneNumber.length(); i++){
			if((int)inPhoneNumber.charAt(i) > 47 && (int)inPhoneNumber.charAt(i) < 58){
				result = result + inPhoneNumber.charAt(i);
			}
		}
		if(result.length() < 11){
			result = "1" + result;
		}
		return result;
	}
	
	/*
	 * @param0 String what index to start appending to the return;
	 * @param1 count of to return;	 * 
	 */
	static public JSONArray toJsonArray(String inStartIndex, String inLimitCount){
		JSONArray result_jsonArray = new JSONArray();
		int position = 0;
		int stratPos = Integer.parseInt(inStartIndex);
		int limitCount = Integer.parseInt(inLimitCount);
		for(Contact contact : Contact.getArrayList()){
			if(position >= stratPos){
				JSONObject theContactAsJson = contact.toJson();					
				if(theContactAsJson != null){					
						HopperJsonStatic.putObjectIntoArray(result_jsonArray, theContactAsJson);
						if(result_jsonArray.length() >= limitCount){
							break;
						}				
				}
			}			
			position++;
		}
		return result_jsonArray;
	}
	
	
	/*
	 * overLoading for param layout
	 */
	static public JSONObject toJson(String inStartIndex, String inLimitByteSize){
		return toJson(inStartIndex, inLimitByteSize, null);
	}
	
	
   static public String getEmailByRawId(String inRawId){    
	    String emailAddress = null;    
	    Cursor emails = activity.getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,null,ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + inRawId, null, null); 
		while (emails.moveToNext()){         
			emailAddress = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
		}        
		emails.close();        
	    //console.log("emailAddress" + emailAddress);
		return emailAddress;
    }
	

	/*
	 * @param0 String what index to start appending to the return;
	 * @param1 count of to return;	 * 
	 */
	static public JSONObject toJson(String inStartIndex, String inLimitByteSize, String[] inWantProperties){
		JSONObject result_json = new JSONObject();
		JSONArray result_jsonArray = new JSONArray();
		
		int position = 0;
		int stratPos = Integer.parseInt(inStartIndex);
		int limitSize = Integer.parseInt(inLimitByteSize);
		int hasNext = 0;
		for(Contact contact : Contact.getArrayList()){			
			if(contact != null){
				JSONObject theContactAsJson;
				if(inWantProperties != null){
					theContactAsJson = contact.toJson(inWantProperties);
				}else{
					theContactAsJson = contact.toJson();
				}									
				if(theContactAsJson != null){
					if(position >= stratPos){					
						if((result_jsonArray.toString().length() + theContactAsJson.toString().length()) > limitSize){
							hasNext = 1;
							break;
						}
						if((result_jsonArray.toString().length() + theContactAsJson.toString().length()) == limitSize){
							hasNext = 0;
							break;
						}						
						HopperJsonStatic.putObjectIntoArray(result_jsonArray, theContactAsJson);
					}
				}else{
					console.log("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
					console.log("theContactAsJson is NULL!!!!!!!!!");
				}
			}
			position++;
		}
		
		HopperJsonStatic.putArrayIntoObjectWithKey(result_json, result_jsonArray, "contactArray");
		HopperJsonStatic.putKeyValueStringsForJsonObject(result_json, "byteSize", String.valueOf(result_jsonArray.toString().length()));
		HopperJsonStatic.putKeyValueStringsForJsonObject(result_json, "startPosition", inStartIndex);
		HopperJsonStatic.putKeyValueStringsForJsonObject(result_json, "endPosition", String.valueOf(position));
		HopperJsonStatic.putKeyValueStringsForJsonObject(result_json, "hasNext", String.valueOf(hasNext));
		
		return result_json;
	}
	
	
	static public ArrayList<JSONObject> toArrayListOfJson(String[] inWantProperties){
		JSONObject result_json = new JSONObject();
		ArrayList<JSONObject> arrayList = new ArrayList<JSONObject>();		
		for(Contact contact : Contact.getArrayList()){			
			if(contact != null){
				JSONObject theContactAsJson;
				if(inWantProperties != null){
					theContactAsJson = contact.toJson(inWantProperties);
				}else{
					theContactAsJson = contact.toJson();
				}									
				if(theContactAsJson != null){					
					arrayList.add(theContactAsJson);				
				}else{
					console.log("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
					console.log("theContactAsJson is NULL!!!!!!!!!");
				}
			}			
		}
		
		return arrayList;		
	}
	
	
	
	
	
	
	
	
	
	static private Cursor getAllRawContacts(){		
		if(activity == null){console.log("Activity NULL!!!"); return null;}
		Cursor c = activity.getContentResolver().query(RawContacts.CONTENT_URI,
		new String[]{RawContacts._ID, RawContacts.CONTACT_ID, RawContacts._ID, RawContacts.ACCOUNT_NAME, 
				RawContacts.DISPLAY_NAME_PRIMARY,RawContacts.ACCOUNT_TYPE, RawContacts.CUSTOM_RINGTONE,
				RawContacts.STARRED, RawContacts.SOURCE_ID, RawContacts.TIMES_CONTACTED },
		null,
		null, null);
		return c;
	}
	
	static private Cursor getPhoneCursorForContactId(String inContactId){
		if(activity == null){console.log("Activity NULL!!!"); return null;}
		Log.v("tag","Entered getPhoneCursorForContactId");
		Cursor c = activity.getContentResolver().query(Data.CONTENT_URI,
		          new String[] {Data._ID, Phone.NUMBER, Phone.TYPE, Phone.LABEL, Phone.PHOTO_THUMBNAIL_URI, Phone.PHOTO_URI},
		          Data.CONTACT_ID + "=?" + " AND "
		                  + Data.MIMETYPE + "='" + Phone.CONTENT_ITEM_TYPE + "'",
		          new String[] {String.valueOf(inContactId)}, null);
		
		Log.v("tag","Done getPhoneCursorForContactId");
		return c;
		
	}
	
	
		
	static public void refresh(){
		contactHashOfArray = new HashOfArray<Contact>();
		HashOfArray<String> hashOfArray = new HashOfArray<String>();
		Log.v("tag","Starting");
		
		Cursor cursor = getAllRawContacts();
		Log.v("tag","got cursor");
		if(cursor.getCount() > 0){
			Log.v("tagg", "cursor.getCount()" + cursor.getCount());
			while(cursor.moveToNext()){
				//String rawContactId = cursor.getString(cursor.getColumnIndex(ContactsContract.RawContactsEntity.RAW_CONTACT_ID));
				String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.RawContactsEntity.CONTACT_ID));
				String id = cursor.getString(cursor.getColumnIndex(RawContacts._ID));
				String accountName = cursor.getString(cursor.getColumnIndex(RawContacts.ACCOUNT_NAME));
				String displayName = cursor.getString(cursor.getColumnIndex(RawContacts.DISPLAY_NAME_PRIMARY));
				String accountType = cursor.getString(cursor.getColumnIndex(RawContacts.ACCOUNT_TYPE));
				String ringTone = cursor.getString(cursor.getColumnIndex(RawContacts.CUSTOM_RINGTONE));
				String starred = cursor.getString(cursor.getColumnIndex(RawContacts.STARRED));
				String sourceId = cursor.getString(cursor.getColumnIndex(RawContacts.SOURCE_ID));
				String timesContacted = cursor.getString(cursor.getColumnIndex(RawContacts.TIMES_CONTACTED));
				//String emailAddress = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
				Contact contact = new Contact();
				
				contact
					.setContactId(cursor.getString(cursor.getColumnIndex(ContactsContract.RawContactsEntity.CONTACT_ID)))
					.setId(cursor.getString(cursor.getColumnIndex(ContactsContract.RawContactsEntity._ID)))
					.setAccountName(cursor.getString(cursor.getColumnIndex(RawContacts.ACCOUNT_NAME)))
					.setAccountType(cursor.getString(cursor.getColumnIndex(RawContacts.ACCOUNT_TYPE)))
					.setName(cursor.getString(cursor.getColumnIndex(RawContacts.DISPLAY_NAME_PRIMARY)))
					.setRingTone(ringTone)
					.setStarred(starred)
					.setSourceId(sourceId).setTimesContacted(timesContacted)
					.setEmailAddress(getEmailByRawId(id));
					//.setRawId(rawContactId);
				;
				
				hashOfArray.add(contactId, accountName + "  " + accountType);
				
				Log.v("tag","------------------------------------------");
				
				//Log.v("tag","rawContactId" + rawContactId);
				Log.v("tag","contactId" + contactId);
				Log.v("tag","id" + id);
				
				Log.v("tag","ACCOUNT_NAME" + accountName);
				Log.v("tag","DISPLAY_NAME_PRIMARY" + displayName);
				Log.v("tag","ACCOUNT_TYPE" + accountType);
				Log.v("tag","CUSTOM_RINGTONE" + ringTone);
				Log.v("tag","starred" + starred);
				Log.v("tag","sourceId" + sourceId);
				Log.v("tag","timesContacted" + timesContacted);
				
				/*Cursor cursorEmail = getEmailCursor("hopper");
				if(cursorEmail.getCount() > 0){
				   cursorEmail.moveToFirst();
				   String email = cursorEmail.getString(cursorEmail.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
				   contact.setEmailAddress(email);
				}else{
					 contact.setEmailAddress("noEmail:" + Phone.RAW_CONTACT_ID + " count:" + cursorEmail.getCount());
				}*/
				
				
				
				
				Cursor cursorPhone = getPhoneCursorForContactId(contactId);
				if(cursorPhone.getCount() > 0){
					cursorPhone.moveToFirst();
					//while(cursorPhone.moveToNext()){
					//only get first occurance---------					
						Log.v("tag","Entered Phone Loop");						
						String phoneNumber = getPhoneNumberOnlyNumeric(cursorPhone.getString(cursorPhone.getColumnIndex(Phone.NUMBER)));
						String phoneType = cursorPhone.getString(cursorPhone.getColumnIndex(Phone.TYPE));
						String phoneLabel = cursorPhone.getString(cursorPhone.getColumnIndex(Phone.LABEL));
						String phoneThumbnailPhotoUri = cursorPhone.getString(cursorPhone.getColumnIndex(Phone.PHOTO_THUMBNAIL_URI));
						String phonePhotoUri = cursorPhone.getString(cursorPhone.getColumnIndex(Phone.PHOTO_URI));
						//String emailAddress = cursor.getString(ContactsContract.CommonDataKinds.Email.DATA);
						//Phone.rContactsContract.CommonDataKinds.Email.DATA
						contact
							.setPhoneNumber(phoneNumber)
							.setPhoneType(phoneType)
							.setPhoneLabel(phoneLabel)
							.setPhoneThumbnailPhotoUri(phoneThumbnailPhotoUri)
							.setPhonePhotoUri(phonePhotoUri)
						;
						//String emailId = cursorPhone.getString(cursorPhone.getColumnIndex(Phone.RAW_CONTACT_ID));//cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.Contacts._ID));
						//Cursor cursorEmail = getEmailCursor(Phone.RAW_CONTACT_ID);
					//	if(cursorEmail.getCount() > 0){
					//		cursorEmail.moveToFirst();
					//		String email = cursorEmail.getString(cursorEmail.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
							//contact.setEmailAddress("myEmail");
				//		}
						
						
						
						
						Log.v("tag","phoneNumber" + phoneNumber);
						Log.v("tag","phoneType" + phoneType);
						Log.v("tag","phoneLabel" + phoneLabel);
						Log.v("tag","phoneThumbnailPhotoUri" + phoneThumbnailPhotoUri);
						Log.v("tag","phonePhotoUri" + phonePhotoUri);
					//}
					
				}
				
				Contact.add(contact);		           
			}
		}
	}
	
	
	
	
	
	//##### -------- I N S T A N C E ---------#############################
	private String id;
	private String name;
	private String phoneNumber;
	private String rawId;
	private String imageUrl;
	private String emailAddress;
	private String accountName;
	private String accountType;
	private String ringTone;
	private String starred;
	private String sourceId;
	private String timesContacted;	
	private String contactId;
	private String phoneType ;
	private String phoneLabel;
	private String phoneThumbnailPhotoUri;
	private String phonePhotoUri;
	
	
	
	
	
	
	public Contact(){
		
	}
	
	
	
	public String getAccountName() {
		return accountName;
	}



	public Contact setAccountName(String accountName) {
		this.accountName = accountName;
		return this;
	}



	public String getAccountType() {
		return accountType;
	}



	public Contact setAccountType(String accountType) {
		this.accountType = accountType;
		return this;
	}



	public String getRingTone() {
		return ringTone;
	}



	public Contact setRingTone(String ringTone) {
		this.ringTone = ringTone;
		return this;
	}



	public String getStarred() {
		return starred;
	}



	public Contact setStarred(String starred) {
		this.starred = starred;
		return this;
	}



	public String getSourceId() {
		return sourceId;
	}



	public Contact setSourceId(String sourceId) {
		this.sourceId = sourceId;
		return this;
	}



	public String getTimesContacted() {
		return timesContacted;
	}



	public Contact setTimesContacted(String timesContacted) {
		this.timesContacted = timesContacted;
		return this;
	}



	public String getContactId() {
		return contactId;
	}



	public Contact setContactId(String contactId) {
		this.contactId = contactId;
		return this;
	}



	public String getPhoneType() {
		return phoneType;
	}



	public Contact setPhoneType(String phoneType) {
		this.phoneType = phoneType;
		return this;
	}



	public String getPhoneLabel() {
		return phoneLabel;
	}



	public Contact setPhoneLabel(String phoneLabel) {
		this.phoneLabel = phoneLabel;
		return this;
	}



	public String getPhoneThumbnailPhotoUri() {
		return phoneThumbnailPhotoUri;
	}



	public Contact setPhoneThumbnailPhotoUri(String phoneThumbnailPhotoUri) {
		this.phoneThumbnailPhotoUri = phoneThumbnailPhotoUri;
		return this;
	}



	public String getPhonePhotoUri() {
		return phonePhotoUri;
	}



	public Contact setPhonePhotoUri(String phonePhotoUri) {
		this.phonePhotoUri = phonePhotoUri;
		return this;
	}



	public String getEmailAddress() {
		return emailAddress;
	}

	public Contact setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
		return this;
	}
	
	
	public String getId() {
		return id;
	}

	public Contact setId(String id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public Contact setName(String name) {
		this.name = name;
		return this;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public Contact setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
		return this;
	}

	public String getRawId() {
		return rawId;
	}

	public Contact setRawId(String rawId) {
		this.rawId = rawId;
		return this;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public Contact setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
		return this;
	}
	
	/*
	 * default
	 */
	public JSONObject toJson(){
		JSONObject current_json = new JSONObject();
		if(this.getId() != null){
			HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "contactId",this.getId());
		}
		if(this.getName() != null){
			HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "contactName",this.getName());
		}
		if(this.getPhoneNumber() != null){
			HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "contactNumber",Contact.getPhoneNumberOnlyNumeric(this.getPhoneNumber()));
		}
		if(this.getEmailAddress() != null){
			HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "contactEmailAddress",this.getEmailAddress());
		}		
		return current_json;
	}
	
	
	public JSONObject toJson(String[] inWantProperties){
		JSONObject current_json = new JSONObject();
		
		for(int i = 0; i < inWantProperties.length; i++){
			if(inWantProperties[i].equalsIgnoreCase("id")){
				HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "id",id);
			}
			if(inWantProperties[i].equalsIgnoreCase("contactId")){
				HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "contactId",contactId);
			}	
			if(inWantProperties[i].equalsIgnoreCase("name")){
				HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "contactName",name);
			}
			if(inWantProperties[i].equalsIgnoreCase("phoneNumber")){
				HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "contactNumber",phoneNumber);
			}
			if(inWantProperties[i].equalsIgnoreCase("rawId")){
				HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "rawId",rawId);
			}
			if(inWantProperties[i].equalsIgnoreCase("imageUrl")){
				HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "imageUrl",imageUrl);
			}
			if(inWantProperties[i].equalsIgnoreCase("emailAddress")){
				HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "contactEmailAddress",emailAddress);
			}
			if(inWantProperties[i].equalsIgnoreCase("accountName")){
				HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "accountName",accountName);
			}
			if(inWantProperties[i].equalsIgnoreCase("accountType")){
				HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "accountType",accountType);
			}
			if(inWantProperties[i].equalsIgnoreCase("ringTone")){
				HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "ringTone",ringTone);
			}
			if(inWantProperties[i].equalsIgnoreCase("starred")){
				HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "starred",starred);
			}
			if(inWantProperties[i].equalsIgnoreCase("sourceId")){
				HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "sourceId",sourceId);
			}
			if(inWantProperties[i].equalsIgnoreCase("timesContacted")){
				HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "timesContacted",timesContacted);
			}			
			if(inWantProperties[i].equalsIgnoreCase("phoneType ")){
				HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "phoneType",phoneType);
			}
			if(inWantProperties[i].equalsIgnoreCase("phoneLabel")){
				HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "phoneLabel",phoneLabel);
			}
			if(inWantProperties[i].equalsIgnoreCase("phoneThumbnailPhotoUri")){
				HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "phoneThumbnailPhotoUri",phoneThumbnailPhotoUri);
			}
			if(inWantProperties[i].equalsIgnoreCase("phonePhotoUri")){
				HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "phonePhotoUri",phonePhotoUri);
			}
			if(inWantProperties[i].equalsIgnoreCase("phoneNumber")){
				HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "phoneNumber",phoneNumber);
			}
		}
		
		
		return current_json;
	}
	
	
	
	
	
	
}
