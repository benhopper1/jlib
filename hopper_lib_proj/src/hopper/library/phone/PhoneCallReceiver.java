//--@manifest-------------
/*<receiver android:name="hopper.library.phone.PhoneCallReceiver">
	  <intent-filter>
	    	<action android:name="android.intent.action.PHONE_STATE" />
	  </intent-filter>
</receiver>*/


package hopper.library.phone;

import hopper.library.communication.v003.HopperFilter.OnFilterPassListener;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;

import com.example.hopperlibrary.HopperTimerOnce;
import com.example.hopperlibrary.console;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;


public class PhoneCallReceiver extends BroadcastReceiver {
	 Context context = null;
	 private static final String TAG = "Phone call";
	 private static boolean autoAnswer = false;
	 
	 
	 
	 
	//-------event onIncomingCallListenerArrayList---------------------------------------------------------------------------------------------------------
	private static ArrayList<OnIncomingCallListener> onIncomingCallListenerArrayList = new ArrayList<OnIncomingCallListener>();
	
	public static  interface OnIncomingCallListener{
	    public void onIncomingCall(Object...objects);
	}
    public static  void setOnIncomingCallListener(OnIncomingCallListener listener) {
    	onIncomingCallListenerArrayList.add(listener);
    }
	 /*
	  * @param0 String phoneNumber
	  * @param1	Intent received Intent
	  */
    private static  void reportOnIncomingCall(Object...objects){
    	for(OnIncomingCallListener listener : onIncomingCallListenerArrayList){
    		listener.onIncomingCall(objects);
        }
    }
    //----------------------------------------------------------------------------------------------------------------------------------- 
	 
    //-------event onOutgoingCallListenerArrayList---------------------------------------------------------------------------------------------------------
	private static  ArrayList<OnOutgoingCallListener> onOutgoingCallListenerArrayList = new ArrayList<OnOutgoingCallListener>();
	
	public static  interface OnOutgoingCallListener{
	    public void onOutgoingCall(Object...objects);
	}
	public static  void setOnOutgoingCallListener(OnOutgoingCallListener listener) {
	  	onOutgoingCallListenerArrayList.add(listener);
	}
	 /*
	  * @param0 String phoneNumber
	  * @param1	Intent ?? Intent
	  */
	private static  void reportOnOutgoingCall(Object...objects){
	  	for(OnOutgoingCallListener listener : onOutgoingCallListenerArrayList){
	  		listener.onOutgoingCall(objects);
	      }
	}
	//----------------------------------------------------------------------------------------------------------------------------------- 	 
	
    //-------event onEndCallListenerArrayList---------------------------------------------------------------------------------------------------------
	private static  ArrayList<OnEndCallListener> onEndCallListenerArrayList = new ArrayList<OnEndCallListener>();
	
	public static  interface OnEndCallListener{
	    public void onEndCall(Object...objects);
	}
	public static  void setOnEndCallListener(OnEndCallListener listener) {
	  	onEndCallListenerArrayList.add(listener);
	}
	 /*
	  * nothing passed!!
	  */
	private static  void reportOnEndCall(Object...objects){
	  	for(OnEndCallListener listener : onEndCallListenerArrayList){
	  		listener.onEndCall(objects);
	      }
	}
	//----------------------------------------------------------------------------------------------------------------------------------- 	 
	 
    //-------event onAutoAnswerCallListenerArrayList---------------------------------------------------------------------------------------------------------
	private static  ArrayList<OnAutoAnswerCallListener> onAutoAnswerCallListenerArrayList = new ArrayList<OnAutoAnswerCallListener>();
	
	public static  interface OnAutoAnswerCallListener{
	    public void onAutoAnswerCall(Object...objects);
	}
	public static  void setOnAutoAnswerCallListener(OnAutoAnswerCallListener listener) {
	  	onAutoAnswerCallListenerArrayList.add(listener);
	}
	 /*
	  * @param0 String phoneNumber
	  * @param1	Intent received Intent
	  */
	private static  void reportOnAutoAnswerCall(Object...objects){
	  	for(OnAutoAnswerCallListener listener : onAutoAnswerCallListenerArrayList){
	  		listener.onAutoAnswerCall(objects);
	      }
	}
	//----------------------------------------------------------------------------------------------------------------------------------- 	 	 
	
	
    //-------event onAnswerCallListenerArrayList---------------------------------------------------------------------------------------------------------
	private static  ArrayList<OnAnswerCallListener> onAnswerCallListenerArrayList = new ArrayList<OnAnswerCallListener>();
	
	public static  interface OnAnswerCallListener{
	    public void onAnswerCall(Object...objects);
	}
	public static  void setOnAnswerCallListener(OnAnswerCallListener listener) {
	  	onAnswerCallListenerArrayList.add(listener);
	}
	 /*
	  * @param0 String phoneNumber
	  * @param1	Intent ??? Intent
	  */
	private static  void reportOnAnswerCall(Object...objects){
	  	for(OnAnswerCallListener listener : onAnswerCallListenerArrayList){
	  		listener.onAnswerCall(objects);
	      }
	}
	//----------------------------------------------------------------------------------------------------------------------------------- 	 
	 
	 @Override
	 public void onReceive(Context context, Intent intent) {
		 console.log("onReceive");
		 
	     if (!intent.getAction().equals("android.intent.action.PHONE_STATE")){
	         return;
	     }else{
	         String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
	         console.log("onReceive STATE:" + state);

	         if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {	        	 
                 if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                	 String number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                	 console.log("Incoming call from: " + number);
                	 
                	 //-----R E P O R T I N G ---->
                	 reportOnIncomingCall(number,intent);
                	 
                	 if(!(autoAnswer)){
                		 console.log("autoAnswer == false, not answering call, must manually"); 
                		 return;
            		 }
                	
                	//-----R E P O R T I N G ---->
                	 reportOnAutoAnswerCall(number,intent);
                	 
			         
			         Intent buttonUp = new Intent(Intent.ACTION_MEDIA_BUTTON);
			         buttonUp.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(
	                 KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK));
			         try {
			             context.sendOrderedBroadcast(buttonUp, "android.permission.CALL_PRIVILEGED");
			             Log.d(TAG, "ACTION_MEDIA_BUTTON broadcasted...");
			             // startRecording();
			         } catch (Exception e) {
			             Log.d(TAG, "Catch block of ACTION_MEDIA_BUTTON broadcast !");
			         }
		
			             return;
                 } else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {

                	 Log.d(TAG, "CALL ANSWERED NOW !!");
                	 return;
                 } else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                	 Log.d(TAG, "ALL DONE IN ELSE IF...... !!");

                 } else {
                	 Log.d(TAG, "ALL DONE IN ELSE ...... !!");

                 }
	         }
	     }
	 }

	 public static void answerPhoneHeadsethook(Context context) {
		 // Simulate a press of the headset button to pick up the call		 
         Intent buttonDown = new Intent(Intent.ACTION_MEDIA_BUTTON);             
         buttonDown.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_HEADSETHOOK));
         context.sendOrderedBroadcast(buttonDown, "android.permission.CALL_PRIVILEGED");
         
         String number = buttonDown.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
         //-----R E P O R T I N G ---->
         reportOnAnswerCall(number,buttonDown);

         // froyo and beyond trigger on buttonUp instead of buttonDown
         Intent buttonUp = new Intent(Intent.ACTION_MEDIA_BUTTON);               
         buttonUp.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK));
         context.sendOrderedBroadcast(buttonUp, "android.permission.CALL_PRIVILEGED");
	 }

	 public static void disconnectPhoneItelephony(Context context) {		 
		 try {

		        String serviceManagerName = "android.os.ServiceManager";
		        String serviceManagerNativeName = "android.os.ServiceManagerNative";
		        String telephonyName = "com.android.internal.telephony.ITelephony";

		        Class telephonyClass;
		        Class telephonyStubClass;
		        Class serviceManagerClass;
		        Class serviceManagerStubClass;
		        Class serviceManagerNativeClass;
		        Class serviceManagerNativeStubClass;

		        Method telephonyCall;
		        Method telephonyEndCall;
		        Method telephonyAnswerCall;
		        Method getDefault;

		        Method[] temps;
		        Constructor[] serviceManagerConstructor;

		        // Method getService;
		        Object telephonyObject;
		        Object serviceManagerObject;

		        telephonyClass = Class.forName(telephonyName);
		        telephonyStubClass = telephonyClass.getClasses()[0];
		        serviceManagerClass = Class.forName(serviceManagerName);
		        serviceManagerNativeClass = Class.forName(serviceManagerNativeName);

		        Method getService = serviceManagerClass.getMethod("getService", String.class);

		        Method tempInterfaceMethod = serviceManagerNativeClass.getMethod("asInterface", IBinder.class);

		        Binder tmpBinder = new Binder();
		        tmpBinder.attachInterface(null, "fake");

		        serviceManagerObject = tempInterfaceMethod.invoke(null, tmpBinder);
		        IBinder retbinder = (IBinder) getService.invoke(serviceManagerObject, "phone");
		        Method serviceMethod = telephonyStubClass.getMethod("asInterface", IBinder.class);

		        telephonyObject = serviceMethod.invoke(null, retbinder);
		        //telephonyCall = telephonyClass.getMethod("call", String.class);
		        telephonyEndCall = telephonyClass.getMethod("endCall");
		        //telephonyAnswerCall = telephonyClass.getMethod("answerRingingCall");
		        
			      //-----R E P O R T I N G ---->
	           	 reportOnEndCall();

		        telephonyEndCall.invoke(telephonyObject);

		    } catch (Exception e) {
		        e.printStackTrace();
		        Log.e("xx",
		                "FATAL ERROR: could not connect to telephony subsystem");
		        
		}
	 }
	 
	 
	 public static void togglePhoneMic(Context context) {	
		 Intent i = new Intent(Intent.ACTION_MEDIA_BUTTON);
		 i.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK));
		 context.sendOrderedBroadcast(i, null);
	 }
	 
	 public static void phoneToSpeaker(Context context, boolean inSpeakerOn){
		 AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE); 
	        audioManager.setMode(AudioManager.MODE_IN_CALL);
	        audioManager.setSpeakerphoneOn(inSpeakerOn);
	 }
	 
	 public static void dialPhone(Activity inActivity, String inPhoneNumber){
		    	 
		 Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + inPhoneNumber));
		 
		//-----R E P O R T I N G ---->
		 reportOnOutgoingCall(inPhoneNumber,intent);
    	 
		 inActivity.startActivity(intent);	
	 }
	 
	 public static void setAutoAnswer(boolean inAutoAnswerYes){
		 autoAnswer = inAutoAnswerYes;
	 }
	 
	 
	 
	 
	 
	 /*public void setStates(){
		 if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)){
			 
		 }
		 
		 
		 
		 TelephonyManager.EXTRA_STATE_RINGING
		 int y = TelephonyManager.CALL_STATE_IDLE;
		 
	 }*/
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
}