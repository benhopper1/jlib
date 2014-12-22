package hopper.application.qrreader.v002.fragments;

import java.util.ArrayList;

import org.json.JSONObject;

import com.example.hopperlibrary.HopperJsonStatic;
import com.example.hopperlibrary.console;



import hopper.application.qrreader.v002.R;
import hopper.image.utility.Transform;
import hopper.library.fragment.stack.FragmentStack;
import hopper.library.http.Upload_frag;
import hopper.library.http.Upload_frag.OnUploadListener;
import hopper.library.local.store.LocalUserStore;
import hopper.library.web.javascript.WebAppInterface;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class UserAddEdditFragment extends Fragment{
	static private View theView = null;
	static private Activity attachedActivity;
	private FormType formType;
	
	String imageUrl;
	
	//==================================================================
    // I N T E R F A C E
    //==================================================================
    public interface LoadUploadFragmentApplicationScope {
 	   public void loadUploadFragment(OnUploadListener inOnUploadListener);
 	   public void back();
 	  public void fragmentShowTop();
    }
    
    public UserAddEdditFragment(FormType inFormType){	
    	formType = inFormType;
    }
    
    //default----
    public UserAddEdditFragment(){	
    	formType = FormType.LONG_FORM;
    }
    
    public enum FormType{
    	SHORT_FORM, 
    	LONG_FORM
    }
    
    
    //======== E V E N T ==================================================================================================================================
	//-------event onOkPressedListenerArrayList---------------------------------------------------------------------------------------------------------
	private ArrayList<OnOkPressedListener> onOkPressedListenerArrayList = new ArrayList<OnOkPressedListener>();
	
	public interface OnOkPressedListener{
	    public void onOkPressed(Object...objects);
	}
    public void setOnOkPressedListener(OnOkPressedListener listener) {
    	onOkPressedListenerArrayList.add(listener);
    }
	 /*
	  * @param0	FormType 	
	  * @param1 JSONObject	form data serialized and ready....
	  */
    public void reportOnOkPressed(Object...objects){
    	for(OnOkPressedListener listener : onOkPressedListenerArrayList){
    		listener.onOkPressed(objects);
        }
    }
    //----------------------------------------------------------------------------------------------------------------------------------- 
    
    
    
    //======== E V E N T ==================================================================================================================================
	//-------event onCancelPressedListenerArrayList---------------------------------------------------------------------------------------------------------
	private ArrayList<OnCancelPressedListener> onCancelPressedListenerArrayList = new ArrayList<OnCancelPressedListener>();
	
	public interface OnCancelPressedListener{
	    public void onCancelPressed(Object...objects);
	}
    public void setOnCancelPressedListener(OnCancelPressedListener listener) {
    	onCancelPressedListenerArrayList.add(listener);
    }
	 /*
	  * @param0	FormType 	
	  * @param1 JSONObject	form data serialized and ready....
	  */
    public void reportOnCancelPressed(Object...objects){
    	for(OnCancelPressedListener listener : onCancelPressedListenerArrayList){
    		listener.onCancelPressed(objects);
        }
    }
    //----------------------------------------------------------------------------------------------------------------------------------- 

    
    

	 @Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
		 
		 if(theView == null){
			 View v = null;
			 if(formType == FormType.LONG_FORM){
				 v = inflater.inflate(R.layout.user_layout, container, false);
			 }
			 if(formType == FormType.SHORT_FORM){
				 v = inflater.inflate(R.layout.user_short_layout, container, false);
			 }
			 
			 theView = v;
			 
			 String defaultUserImageUrl = LocalUserStore.getValueAsString("domain") + LocalUserStore.getValueAsString("defaultUserImageUrl");
			 imageUrl = LocalUserStore.getValueAsString("defaultUserImageUrl"); 	 
			 final ImageView iv_screenImage = (ImageView)theView.findViewById(R.id.iv_screenImage);
			 
			 final String imagePath = defaultUserImageUrl;				       	
			 theView.post(new Runnable() {
			    @Override
			    public void run() {
			    	
			    	Bitmap beforeTransform = hopper.cache.v003.ImageCache.getBitmapFromCacheFirstThenUrl(imagePath);
			    	Bitmap afterTransform = Transform.roundCorners(beforeTransform, 50);
			    	iv_screenImage.setImageBitmap(afterTransform);
		    		//member_imageView.setImageBitmap(hopper.cache.v003.ImageCache.getBitmapFromCacheFirstThenUrl("http://" + ((MainActivity)attachedActivity).httpHostIp + ":" + ((MainActivity)attachedActivity).httpHostPort + "/" + imagePath));
		    	}
			 });
			 
			 iv_screenImage.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					console.log("click--- ");
					console.log("LoadUploadFragmentApplicationScope calling");
					
					Upload_frag uploadFragment = new Upload_frag("http://192.168.0.16:35001/upload", "imageStore", "normalUserImage","Select Image", "image/*", new OnUploadListener(){
						@Override
						public void onUpload(final Object... objects) {
							console.log("----fileChooser -----  upload callback!!!!!:" + (String) objects[0]);
							//((LoadUploadFragmentApplicationScope)mContext).back();
							//String newFile = "/" + (String) objects[0];
							
							//final imagePath2
							 theView.post(new Runnable() {
								    @Override
								    public void run() {
								    	imageUrl = "/" + (String) objects[0];
								    	console.log("Attempting to load :" + LocalUserStore.getValueAsString("domain") + "/" + (String) objects[0]);
								    	Bitmap beforeTransform = hopper.cache.v003.ImageCache.getBitmapFromCacheFirstThenUrl(LocalUserStore.getValueAsString("domain") + "/" + (String) objects[0]);
								    	Bitmap afterTransform = Transform.roundCorners(beforeTransform, 50);
								    	iv_screenImage.setImageBitmap(afterTransform);
								    	FragmentStack.getInstance("mainFragmentStack").showTop();
								    	
						    		}
							 });
							
							
							
							console.log("updateImageSource back from");
						}
			    	});
					
					FragmentStack.getInstance("mainFragmentStack").showThis(uploadFragment);
					
				}
				 
			 });
			 
			 Button bt_user_ok = (Button)theView.findViewById(R.id.bt_user_ok);
			 bt_user_ok.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					reportOnOkPressed(UserAddEdditFragment.this.formType, UserAddEdditFragment.this.toJson());					
				}
				 
			 });
			 
			 Button bt_user_cancel = (Button)theView.findViewById(R.id.bt_user_cancel);
			 bt_user_cancel.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0){
					UserAddEdditFragment.this.validateAll();
					reportOnCancelPressed(UserAddEdditFragment.this.formType, UserAddEdditFragment.this.toJson());
					//UserAddEdditFragment.this.highlightField("userName");
				}
				 
			 });			 
				
		 }	
	        
	        
	        
		 
		 
		 return theView;
		 
	 }
	 
	 public JSONObject toJson(){
		 
		 JSONObject result_json = new JSONObject();
		 String firstName = null;
		 String lastName = null;
		 String emailAddress = null;
		 String userName = null;
		 String password = null;
		 String address = null;
		 String city = null;
		 String state = null;
		 String zipcode = null;
		 String country = null;
		 
		 
		 if(formType == FormType.LONG_FORM){
			 firstName = ((EditText)theView.findViewById(R.id.et_user_firstName)).getText().toString();
			 lastName = ((EditText)theView.findViewById(R.id.et_user_lastName)).getText().toString();
			 emailAddress = ((EditText)theView.findViewById(R.id.et_user_email)).getText().toString();
			 userName = ((EditText)theView.findViewById(R.id.et_user_userName)).getText().toString();
			 password = ((EditText)theView.findViewById(R.id.et_user_password)).getText().toString();
			 address = ((EditText)theView.findViewById(R.id.et_user_address)).getText().toString();
			 city = ((EditText)theView.findViewById(R.id.et_user_city)).getText().toString();
			 state = ((EditText)theView.findViewById(R.id.et_user_state)).getText().toString();
			 zipcode = ((EditText)theView.findViewById(R.id.et_user_zipcode)).getText().toString();
			 country = ((EditText)theView.findViewById(R.id.et_user_country)).getText().toString();
		 }
		 
		 if(formType == FormType.SHORT_FORM){
			 firstName = ((EditText)theView.findViewById(R.id.et_user_firstName)).getText().toString();
			 lastName = ((EditText)theView.findViewById(R.id.et_user_lastName)).getText().toString();
			 emailAddress = ((EditText)theView.findViewById(R.id.et_user_email)).getText().toString();
			 userName = ((EditText)theView.findViewById(R.id.et_user_userName)).getText().toString();
			 password = ((EditText)theView.findViewById(R.id.et_user_password)).getText().toString();
			 
			 address = "";
			 city = "";
			 state = "";
			 zipcode = "";
			 country = "";
		 }
		 
		 
		 
		 //String screenImage = imageUrl;
		 console.log("imageUrl:" + imageUrl);
		 
		 HopperJsonStatic.putKeyValueStringsForJsonObject(result_json, "firstName", firstName);
		 HopperJsonStatic.putKeyValueStringsForJsonObject(result_json, "lastName", lastName);
		 HopperJsonStatic.putKeyValueStringsForJsonObject(result_json, "emailAddress", emailAddress);
		 HopperJsonStatic.putKeyValueStringsForJsonObject(result_json, "userName", userName);
		 HopperJsonStatic.putKeyValueStringsForJsonObject(result_json, "password", password);
		 HopperJsonStatic.putKeyValueStringsForJsonObject(result_json, "address", address);
		 HopperJsonStatic.putKeyValueStringsForJsonObject(result_json, "city", city);
		 HopperJsonStatic.putKeyValueStringsForJsonObject(result_json, "state", state);
		 HopperJsonStatic.putKeyValueStringsForJsonObject(result_json, "zipcode", zipcode);
		 HopperJsonStatic.putKeyValueStringsForJsonObject(result_json, "country", country);
		 HopperJsonStatic.putKeyValueStringsForJsonObject(result_json, "screenImage", imageUrl); 
		 
		 return result_json; 
			
	 }
	 
	 public void validateAll(){
		 if(formType == FormType.LONG_FORM){
			 if(onValidate("firstName", ((EditText)theView.findViewById(R.id.et_user_firstName)).getText().toString()) == false){
				 onValidateFail("firstName", ((EditText)theView.findViewById(R.id.et_user_firstName)).getText().toString());
			 }
			 if(onValidate("lastName", ((EditText)theView.findViewById(R.id.et_user_lastName)).getText().toString()) == false){
				 onValidateFail("lastName", ((EditText)theView.findViewById(R.id.et_user_lastName)).getText().toString());
			 }
			 if(onValidate("emailAddress", ((EditText)theView.findViewById(R.id.et_user_email)).getText().toString()) == false){
				 onValidateFail("emailAddress", ((EditText)theView.findViewById(R.id.et_user_email)).getText().toString());
			 }
			 if(onValidate("userName", ((EditText)theView.findViewById(R.id.et_user_userName)).getText().toString()) == false){
				 onValidateFail("userName", ((EditText)theView.findViewById(R.id.et_user_userName)).getText().toString());
			 }
			 if(onValidate("password", ((EditText)theView.findViewById(R.id.et_user_password)).getText().toString()) == false){
				 onValidateFail("password",  ((EditText)theView.findViewById(R.id.et_user_password)).getText().toString());
			 }
			 if(onValidate("address", ((EditText)theView.findViewById(R.id.et_user_address)).getText().toString()) == false){
				 onValidateFail("address", ((EditText)theView.findViewById(R.id.et_user_address)).getText().toString());
			 }
			 if(onValidate("city", ((EditText)theView.findViewById(R.id.et_user_city)).getText().toString()) == false){
				 onValidateFail("city", ((EditText)theView.findViewById(R.id.et_user_city)).getText().toString());
			 }
			 if( onValidate("state", ((EditText)theView.findViewById(R.id.et_user_state)).getText().toString()) == false){
				 onValidateFail("state", ((EditText)theView.findViewById(R.id.et_user_state)).getText().toString());
			 }
			 if(onValidate("zipcode",((EditText)theView.findViewById(R.id.et_user_zipcode)).getText().toString()) == false){
				 onValidateFail("zipcode", ((EditText)theView.findViewById(R.id.et_user_zipcode)).getText().toString());
			 }
			 if(onValidate("country", ((EditText)theView.findViewById(R.id.et_user_country)).getText().toString()) == false){
				 onValidateFail("country", ((EditText)theView.findViewById(R.id.et_user_country)).getText().toString());
			 }
		 }
		 
		 if(formType == FormType.SHORT_FORM){
			 if(onValidate("firstName",((EditText)theView.findViewById(R.id.et_user_firstName)).getText().toString()) == false){
				 onValidateFail("firstName", ((EditText)theView.findViewById(R.id.et_user_firstName)).getText().toString());
			 }
			 if( onValidate("lastName",((EditText)theView.findViewById(R.id.et_user_lastName)).getText().toString()) == false){
				 onValidateFail("lastName", ((EditText)theView.findViewById(R.id.et_user_lastName)).getText().toString());
			 }
			 if(onValidate("emailAddress",((EditText)theView.findViewById(R.id.et_user_email)).getText().toString()) == false){
				 onValidateFail("emailAddress", ((EditText)theView.findViewById(R.id.et_user_email)).getText().toString());
			 }
			 if(onValidate("userName", ((EditText)theView.findViewById(R.id.et_user_userName)).getText().toString()) == false){
				 onValidateFail("userName", ((EditText)theView.findViewById(R.id.et_user_userName)).getText().toString());
			 }
			 if( onValidate("password", ((EditText)theView.findViewById(R.id.et_user_password)).getText().toString()) == false){
				 onValidateFail("password", ((EditText)theView.findViewById(R.id.et_user_password)).getText().toString());
			 }		 
			
		 }
	 }
	 
	 
	 public void highlightField(String inFieldName, String inMessage){
		 Toast.makeText(attachedActivity.getApplicationContext(), "INVALID " + inFieldName + " " + inMessage, Toast.LENGTH_LONG).show();
		 highlightField(inFieldName);
	 }
	public void highlightField(String inFieldName){
		
		console.log("highlightField ENTERED");
		
		EditText toHighlightView = null;
		if(inFieldName.equalsIgnoreCase("firstName")){
			toHighlightView = (EditText)theView.findViewById(R.id.et_user_firstName);
		}
		if(inFieldName.equalsIgnoreCase("lastName")){
			toHighlightView = (EditText)theView.findViewById(R.id.et_user_lastName);
		}
		if(inFieldName.equalsIgnoreCase("emailAddress")){
			toHighlightView = (EditText)theView.findViewById(R.id.et_user_email);
		}
		if(inFieldName.equalsIgnoreCase("userName")){
			toHighlightView = (EditText)theView.findViewById(R.id.et_user_userName);
		}
		if(inFieldName.equalsIgnoreCase("password")){
			toHighlightView = (EditText)theView.findViewById(R.id.et_user_password);
		}
		if(inFieldName.equalsIgnoreCase("address")){
			toHighlightView = (EditText)theView.findViewById(R.id.et_user_address);
		}
		if(inFieldName.equalsIgnoreCase("city")){
			toHighlightView = (EditText)theView.findViewById(R.id.et_user_city);
		}
		if(inFieldName.equalsIgnoreCase("state")){
			toHighlightView = (EditText)theView.findViewById(R.id.et_user_state);
		}
		if(inFieldName.equalsIgnoreCase("zipcode")){
			toHighlightView = (EditText)theView.findViewById(R.id.et_user_zipcode);
		}
		if(inFieldName.equalsIgnoreCase("country")){
			toHighlightView = (EditText)theView.findViewById(R.id.et_user_country);
		}
		
			 
		toHighlightView.setTextColor(0xFFF06D2F);
		toHighlightView.setSelectAllOnFocus(true);
		toHighlightView.requestFocus();
	}
	 
	 /*{
			firstName:"",
			lastName:"",
			emailAddress:"",
			userName:"",
			password:"",
			address:"",
			city:"",
			state:"",
			zipcode:"",
			country:"",
			userGroup:"",
			screenImage:"",
			activateCode:"",
		}*/
	 
	 public boolean onValidate(String fieldName, String fieldValue){
		 console.log("Validate: " + fieldName + "==" + fieldValue);
		 boolean resultBool = true;		 
		 return resultBool;
	 }
	 
	 public void onValidateFail(String fieldName, String fieldValue){
		 console.log("FAILED Validate: " + fieldName + "==" + fieldValue + " FAILED");
	 }
	 



	@Override
	public void onAttach(Activity activity) {
	    super.onAttach(activity);
	    attachedActivity = activity;
	}
}
