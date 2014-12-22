//<uses-permission android:name="android.permission.READ_CONTACTS" />

package hopper.library.phone;

import hopper.library.http.UploadFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.hopperlibrary.HopperJsonStatic;
import com.example.hopperlibrary.console;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.PhoneLookup;
import android.provider.ContactsContract.RawContacts;
import android.util.Log;
import android.util.Pair;
import android.provider.ContactsContract;

public class ContactInformation {
	private Activity activity;
	private HashMap<String,Pair<String,String>> contactsHashMapOfPair = new HashMap<String,Pair<String,String>>();
	
	private HashMap<String,Contact> contactHashMapById = new HashMap<String,Contact>();
	
	public ContactInformation(Activity inActivity){
		activity = inActivity;
		
		ContentResolver cr = inActivity.getContentResolver(); //Activity/Application android.content.Context
	    Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
	    if(cursor.moveToFirst()){
	        do{
	            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
	            
	            //if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0){
	                Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID +" = ?",new String[]{ id }, null);
	                while (pCur.moveToNext()){
	                    String contactName = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
	                    String contactNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
	                    String contactId = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));  //.r._ID
	                    String rawContactId = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID));
	                    String emailAddress = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
	                    String image_uri = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
	                    
	                   	                   
	                    
	                    contactHashMapById.put(contactId, 
                    		new Contact()	                    	
		                    	.setId(contactId)
			                    .setName(contactName)
			                    .setPhoneNumber(contactNumber)
			                    .setRawId(rawContactId)
			                    .setImageUrl(image_uri)
			                    .setEmailAddress(emailAddress)
	                    );
	                    	                    
	                    console.log("co" + contactName);
	                    console.log("contactNumber" + contactNumber);
	                    console.log("contactId" + contactId);
	                    console.log("rawContactId" + rawContactId);
	                    console.log("image_uri" + image_uri);
	                    
	                    
	                    console.log("addedId:" + contactId + "-------" + contactName + "----- " +  rawContactId +"------------");// + image_uri);
	                    Pair<String, String> contactPair = new Pair<String, String>(contactId, contactNumber);
	                    this.contactsHashMapOfPair.put(contactName, contactPair);
	                    
	                    if(contactNumber != null){
	                    	//sendImageFileToTempFolderOnServer("http://192.168.0.16:35001/uploadAsTemp", contactNumber);
	                    }
	                    
	                    break;
	                }
	                pCur.close();
	           // }

	        } while (cursor.moveToNext()) ;
	    }		
	}
	
	public JSONArray getContactsAsJsonArray(){
		JSONArray result_jsonArray = new JSONArray();
		JSONObject current_json = null;// = new JSONObject();
		
		/*for(String theKey : contactsHashMapOfPair.keySet()){
			current_json = new JSONObject();
			Pair<String, String> thePair = contactsHashMapOfPair.get(theKey);
			HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "contactId", thePair.first);
			HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "contactName", theKey);
			HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "contactNumber", this.cleanPhoneNumeber(thePair.second));
			HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "contactEmailAddress", "notImplementedYet!!");
			
			HopperJsonStatic.putObjectIntoArray(result_jsonArray, current_json);
		}*/
		console.log("txchk_");
		for(Contact contact : contactHashMapById.values()){
			current_json = new JSONObject();
			console.log("txchk_0");console.log("contactId" + contact.getId());
			HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "contactId", contact.getId());
			console.log("txchk_1");
			HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "contactName", contact.getName());
			console.log("txchk_2");
			HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "contactNumber", this.cleanPhoneNumeber(contact.getPhoneNumber()));
			console.log("txchk_3");
			HopperJsonStatic.putKeyValueStringsForJsonObject(current_json, "contactEmailAddress", contact.getEmailAddress());
			console.log("txchk_4");
			
			HopperJsonStatic.putObjectIntoArray(result_jsonArray, current_json);
			console.log("txchk_5");
		}	
		
		
		return result_jsonArray;
	}
	
	public void dump(){
		console.log("Dump:" + this.getContactsAsJsonArray().toString());
	}
	
	public String cleanPhoneNumeber(String inPhoneNumeber){
		inPhoneNumeber = inPhoneNumeber.replace("-", " ");
		String[] phoneSplits = inPhoneNumeber.split(",");
		inPhoneNumeber = phoneSplits[0];
		String phoneSplitEnd = "";
		for(int i = 1; i < phoneSplits.length; i++){
			phoneSplitEnd = " , " + phoneSplits[i];
		}

		if(inPhoneNumeber.charAt(0) == '1'){
			inPhoneNumeber = inPhoneNumeber.substring(1);
		}

		inPhoneNumeber = inPhoneNumeber.replaceAll("[^0-9]/g", "");
		inPhoneNumeber = inPhoneNumeber.replaceAll("(\\d{3})(\\d{3})(\\d{4})", "($1) $2-$3");
		return inPhoneNumeber + phoneSplitEnd;		
	}
	
	//hashkey:id first:phone second name
	public HashMap<String,Pair<String,String>> getContactsPairById(){
		HashMap<String,Pair<String,String>> result = new HashMap<String,Pair<String,String>>();
		for(String oldKey : contactsHashMapOfPair.keySet()){
			Pair<String,String> oldPair = contactsHashMapOfPair.get(oldKey);
			Pair<String,String> newPair = new Pair<String,String>(oldKey,oldPair.second);
			result.put(oldPair.first, newPair);
		}
		return result;
			
	}
	
	public void addMember(String inName,String inPhoneNumber, String inEmailAddress, String inCompany, String inJobTitle, Bitmap inContactBitmap){
		
		ArrayList < ContentProviderOperation > ops = new ArrayList < ContentProviderOperation > ();

		 ops.add(ContentProviderOperation.newInsert(
		 ContactsContract.RawContacts.CONTENT_URI)
		     .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
		     .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
		     .build());

		 //------------------------------------------------------ Names
		 if (inName != null) {
		     ops.add(ContentProviderOperation.newInsert(
		     ContactsContract.Data.CONTENT_URI)
		         .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
		         .withValue(ContactsContract.Data.MIMETYPE,
		     ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
		         .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,inName).build());
		 }

		 //------------------------------------------------------ Mobile Number                     
		 if (inPhoneNumber != null) {
		     ops.add(ContentProviderOperation.
		     newInsert(ContactsContract.Data.CONTENT_URI)
		         .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
		         .withValue(ContactsContract.Data.MIMETYPE,
		     ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
		         .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, inPhoneNumber)
		         .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
		     ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
		         .build());
		 }

		 //------------------------------------------------------ Home Numbers
		 /*if (HomeNumber != null) {
		     ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
		         .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
		         .withValue(ContactsContract.Data.MIMETYPE,
		     ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
		         .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, HomeNumber)
		         .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
		     ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
		         .build());
		 }*/

		 //------------------------------------------------------ Work Numbers
		 /*if (WorkNumber != null) {
		     ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
		         .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
		         .withValue(ContactsContract.Data.MIMETYPE,
		     ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
		         .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, WorkNumber)
		         .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
		     ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
		         .build());
		 }*/

		 //------------------------------------------------------ Email
		 if (inEmailAddress != null) {
		     ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
		         .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
		         .withValue(ContactsContract.Data.MIMETYPE,
		     ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
		         .withValue(ContactsContract.CommonDataKinds.Email.DATA, inEmailAddress)
		         .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
		         .build());
		 }

		 //------------------------------------------------------ Organization
		 if (!inCompany.equals("") && !inJobTitle.equals("")) {
		     ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
		         .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
		         .withValue(ContactsContract.Data.MIMETYPE,
		     ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
		         .withValue(ContactsContract.CommonDataKinds.Organization.COMPANY, inCompany)
		         .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
		         .withValue(ContactsContract.CommonDataKinds.Organization.TITLE, inJobTitle)
		         .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
		         .build());
		 }
		 
		 //------------------------------------------------------ Image
		 if(inContactBitmap != null) {
			 byte[] scaledPhotoData   = bitmapToPNGByteArray(inContactBitmap);
			 ops.add(ContentProviderOperation.newInsert(Data.CONTENT_URI)
						/* 0 is referencing index 0 in this ContentProviderOperation ArrayList */
						.withValueBackReference(Data.RAW_CONTACT_ID, 0)
						.withValue(Data.MIMETYPE, Photo.CONTENT_ITEM_TYPE)
						.withValue(Photo.PHOTO, scaledPhotoData)
						//.withValue(ContactsContract.Data.IS_SUPER_PRIMARY, 1)
						.build());
			 
			 
		    /* ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
		         .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
		         .withValue(ContactsContract.Data.MIMETYPE,
		     ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
		         .withValue(ContactsContract.CommonDataKinds.Organization.COMPANY, inCompany)
		         .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
		         .withValue(ContactsContract.CommonDataKinds.Organization.TITLE, inJobTitle)
		         .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
		         .build());*/
		 }

		 // Asking the Contact provider to create a new contact                 
		 try {
		     activity.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
		 } catch (Exception e) {
		     e.printStackTrace();
		    // Toast.makeText(myContext, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
		 }
		
		
	}
	
	public static byte[] bitmapToPNGByteArray(Bitmap bitmap){
		final int size = bitmap.getWidth() * bitmap.getHeight() * 4;
		final ByteArrayOutputStream out = new ByteArrayOutputStream(size);
		try {
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
			out.flush();
			out.close();
			return out.toByteArray();
		}
		catch(IOException e){
			Log.w("error", "Unable to serialize photo: " + e.toString());
			return null;
		}
	}
	
	public boolean deleteContact(String phone, String name){
		    Uri contactUri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phone));
		    Cursor cur = activity.getContentResolver().query(contactUri, null, null, null, null);
		    try {
		        if (cur.moveToFirst()) {
		            do {
		                if (cur.getString(cur.getColumnIndex(PhoneLookup.DISPLAY_NAME)).equalsIgnoreCase(name)) {
		                    String lookupKey = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
		                    Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey);
		                    activity.getContentResolver().delete(uri, null, null);
		                    return true;
		                }

		            } while (cur.moveToNext());
		        }
		        
		    } catch (Exception e) {
		        System.out.println(e.getStackTrace());
		    }
		    return false;
	}
	
	public static Uri loadContactPhotoUri(Activity inActivity, String inPhoneNumber) {
		Uri phoneUri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(inPhoneNumber));		
	    Uri photoUri = null;
	    ContentResolver cr = inActivity.getContentResolver();
	    console.log("chk_11");
	    Cursor contact = cr.query(phoneUri, new String[] { ContactsContract.Contacts._ID }, null, null, null);
	    if(contact == null){
       		console.log("contact null");
       		return null;
       	}
	    console.log("chk_12");
	    if(contact.moveToFirst()){	    
	    	long userId = contact.getLong(contact.getColumnIndex(ContactsContract.Contacts._ID));        
	    	photoUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, userId);
	    	return photoUri;
	    }
	   
        return null;	    
	}
	
	public String phoneNumberToNumeric(String inString){
		String result = "";
		for(int i = 0; i < inString.length(); i++){
			if((int)inString.charAt(i) > 47 && (int)inString.charAt(i) < 58){
				result = result + inString.charAt(i);
			}
		}
		return result;
	}
	
	public JSONObject sendImageFileToTempFolderOnServer(String inServiceConnectString, String inPhoneNumber){		
        String phoneNumber = this.phoneNumberToNumeric(inPhoneNumber);
        
       	ContentResolver cr = activity.getContentResolver();
    	console.log("chk_1");
       	Uri photoUri = loadContactPhotoUri(activity, phoneNumber);
       	console.log("chk_2");
       	if(photoUri == null){
       		console.log("photoUri null");
       	}
       	console.log("chk_3");
       	InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(cr, photoUri);
       	console.log("chk_4");
       	if(input == null){
       		console.log("inpu null");
       		return null;
       	}
       	       	
        UploadFile uploadFile = new UploadFile(inServiceConnectString,input);
		Pair<String,String> pair = new Pair("forceName",phoneNumber + ".jpg");
		
		String resultString = uploadFile.uploadByStream(pair);		
		JSONObject result_json = HopperJsonStatic.createJsonObjectFromJsonString(resultString);
		HopperJsonStatic.putKeyValueStringsForJsonObject(result_json, "phoneNumber", inPhoneNumber);		
		return result_json;
	}
	
	/*private InputStream getContactImageByIdAsInputStream(String inContactId){
		
		ContentResolver cr = activity.getContentResolver(); //Activity/Application android.content.Context
	    Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
	    if(cursor.moveToFirst()){
	        do{
	            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

	            if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0){
	                Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID +" = ?",new String[]{ id }, null);
	                while (pCur.moveToNext()){	                	
	                	String contactId = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));
	                	
	                	long userId = pCur.getLong(pCur.getColumnIndex(ContactsContract.Contacts._ID));        
	        	    	photoUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, userId);
	        	    	return photoUri;
	                	
	                	
	                	if(contactId.equalsIgnoreCase(inContactId)){
	                		console.log("found id");		                    
	                		Uri photoUri = null;	                		  
		        	    	photoUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(inContactId));
		        	    	if(photoUri == null){
		        	       		console.log("photoUri null");
		        	       	}
		        	       	console.log("chk_3");
		        	       	InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(cr, photoUri);
		        	       	if(input == null){return null;}
	                		
	                		
		                    console.log("chk.000");
		                    if(b == null){return null;}
		                    InputStream is = new ByteArrayInputStream(b);
		                    console.log("chk.010");
		                    return input;
	                	} 
	                    break;
	                }
	                pCur.close();
	            }

	        } while (cursor.moveToNext()) ;
	    }		
		
		
		return null;
		
	//	Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(inContactId));
		
	//	return ContactsContract.Contacts.openContactPhotoInputStream(activity.getContentResolver(), uri);
		InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(activity.getContentResolver(),
                ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,inContactId));
		//return inputStream;
	}
	*/
	
	
	/*public InputStream getContactImageByIdAsInputStream2(String inContactId){
		console.log("chk.000");
		ContentResolver cr = activity.getContentResolver();
		console.log("chk.001");
		Uri photo = getPhotoUri(Long.parseLong(inContactId)); // ContentUris.withAppendedId( ContactsContract.Contacts.CONTENT_URI, Long.parseLong(inContactId));
		console.log("chk.002");
	    //photo = Uri.withAppendedPath(photo, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY );
		photo =  ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,Long.parseLong(inContactId));
	    console.log("chk.003");
	    if(photo == null){return null;}
	    console.log("chk.103");
	    InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(cr, photo);
	    console.log("chk.004");
       	if(input == null){return null;}
       	console.log("chk.005");
       	return input;
	}*/
	
	public InputStream getContactImageByIdAsInputStream3(String inContactId){
		
		InputStream inputStream = ContactsContract.Contacts
                .openContactPhotoInputStream(activity.getContentResolver(),
                        ContentUris.withAppendedId(
                                ContactsContract.Contacts.CONTENT_URI,
                                Long.parseLong(inContactId)));
		return inputStream;
	}
	
	public JSONArray sendImageFileSToTempFolderOnServerByIds(String inServiceConnectString){
		JSONArray result_jsonArray = new JSONArray();
		HashMap<String,Pair<String,String>> contactHashById = getContactsPairById();		
		
		for(String theKey : contactHashById.keySet()){
			console.log("contactId:" + theKey);
			Pair<String,String> thePair = contactHashById.get(theKey);
			
			InputStream contactImageInputStream = getContactImageByIdAsInputStream3(theKey);
			
			if(contactImageInputStream == null){
				console.log("contactImageInputStream is null:" + theKey);
			}else{
				console.log("contactcontactsHashMapOfPairImageInputStream is NOT null:" + theKey);
				UploadFile uploadFile = new UploadFile(inServiceConnectString, contactImageInputStream);
				Pair<String,String> pair = new Pair<String,String>("forceName", thePair.second + ".jpg");
				
				String resultString = uploadFile.uploadByStream(pair);		
				JSONObject result_json = HopperJsonStatic.createJsonObjectFromJsonString(resultString);
				HopperJsonStatic.putKeyValueStringsForJsonObject(result_json, "phoneNumber", thePair.first);
				HopperJsonStatic.putKeyValueStringsForJsonObject(result_json, "serviceString", inServiceConnectString);
				
				HopperJsonStatic.putObjectIntoArray(result_jsonArray, result_json);	
			}
		}
		
		return result_jsonArray;
	}
	
	
	
	/*public Uri getContactUriById(String inContactId){
		Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(inContactId));
		//InputStream stream = ContactsContract.Contacts.openContactPhotoInputStream(activity.getContentResolver(), uri);
		return uri;
	}
	*/
	/*public Uri getPhotoUri(long contactId) {
	    ContentResolver contentResolver = activity.getContentResolver();

	    try {
	        Cursor cursor = contentResolver
	                .query(ContactsContract.Data.CONTENT_URI,
	                        null,
	                        ContactsContract.Data.CONTACT_ID
	                                + "="
	                                + contactId
	                                + " AND "

	                                + ContactsContract.Data.MIMETYPE
	                                + "='"
	                                + ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE
	                                + "'", null, null);

	        if (cursor != null) {
	            if (!cursor.moveToFirst()) {
	                return null; // no photo
	            }
	        } else {
	            return null; // error in cursor process
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }

	    Uri person = ContentUris.withAppendedId(
	            ContactsContract.Contacts.CONTENT_URI, contactId);
	    return Uri.withAppendedPath(person,
	            ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
	}
	*/
	
	public void updateContactImage(String contactId, Bitmap inBitmap){
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		inBitmap.compress(Bitmap.CompressFormat.PNG , 75, stream);		
		ContentValues values = new ContentValues();
		values.put(ContactsContract.Data.RAW_CONTACT_ID, contactHashMapById.get(contactId).getRawId()); //original rawId
		values.put(ContactsContract.Data.IS_SUPER_PRIMARY, 1);
		values.put(ContactsContract.CommonDataKinds.Photo.PHOTO, stream.toByteArray());
		values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE );
		
		Uri dataUri = ContentUris.withAppendedId(Data.CONTENT_URI, Long.parseLong(contactId) );//contactHashMapById.get(contactId).getRawId()  //Long.parseLong(contactId)
		activity.getContentResolver().update(dataUri , values, null, null);
	}
	
	public String getContactIdByPhoneNumber(String inPhoneNumber){
		for(String theKey : contactHashMapById.keySet()){
			Contact contact = contactHashMapById.get(theKey);
			console.log("getContactIdByPhoneNumber CMPARE:" + phoneNumberToNumeric(contact.getPhoneNumber()) +"  " + phoneNumberToNumeric(inPhoneNumber));
			if(phoneNumberToNumeric(contact.getPhoneNumber()).equalsIgnoreCase(phoneNumberToNumeric(inPhoneNumber))){
				console.log("good compare");
				return contact.getId();
			}
		}		
		
		return null;
	}
	
}
