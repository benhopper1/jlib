package hopper.library.phone.v002;

import java.util.ArrayList;

import com.example.hopperlibrary.console;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

public class PhoneCallReceiver extends BroadcastReceiver{
	static public enum StateType {
		idle(0),
		ringing(1),
		offhook(2)
		
		;
		private int value;
	
		private StateType(int inValue){
			this.value = inValue;
		}
	
		public int getValue(){
			return this.value;
	    }
	}	
	
	static private StateType state;	
	
	
	
	
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
	    	console.log("--------------- reportOnIncomingCall --------------------------------------");
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
			console.log("--------------- reportOnOutgoingCall --------------------------------------");
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
			console.log("--------------- reportOnEndCall --------------------------------------");
		  	for(OnEndCallListener listener : onEndCallListenerArrayList){
		  		listener.onEndCall(objects);
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
			console.log("--------------- reportOnAnswerCall --------------------------------------");
		  	for(OnAnswerCallListener listener : onAnswerCallListenerArrayList){
		  		listener.onAnswerCall(objects);
		      }
		}
		//----------------------------------------------------------------------------------------------------------------------------------- 
	
		
		
		//-------event onMissedCallListenerArrayList---------------------------------------------------------------------------------------------------------
		private static  ArrayList<OnMissedCallListener> onMissedCallListenerArrayList = new ArrayList<OnMissedCallListener>();
		
		public static  interface OnMissedCallListener{
		    public void onMissedCall(Object...objects);
		}
		public static  void setOnMissedCallListener(OnMissedCallListener listener) {
		  	onMissedCallListenerArrayList.add(listener);
		}
		 /*
		  * @param0 String phoneNumber
		  * @param1	Intent ??? Intent
		  */
		private static  void reportOnMissedCall(Object...objects){
			console.log("--------------- reportOnMissedCall --------------------------------------");
		  	for(OnMissedCallListener listener : onMissedCallListenerArrayList){
		  		listener.onMissedCall(objects);
		      }
		}
		//----------------------------------------------------------------------------------------------------------------------------------- 
	
	
		//-------event onOffHookListenerArrayList---------------------------------------------------------------------------------------------------------
			private static  ArrayList<OnOffHookListener> onOffHookListenerArrayList = new ArrayList<OnOffHookListener>();
			
			public static  interface OnOffHookListener{
			    public void onOffHook(Object...objects);
			}
			public static  void setOnOffHookListener(OnOffHookListener listener) {
			  	onOffHookListenerArrayList.add(listener);
			}
			 /*
			  * @param0 String phoneNumber
			  * @param1	Intent ??? Intent
			  */
			private static  void reportOnOffHook(Object...objects){
				console.log("--------------- reportOnOffHook --------------------------------------");
			  	for(OnOffHookListener listener : onOffHookListenerArrayList){
			  		listener.onOffHook(objects);
			      }
			}
			//----------------------------------------------------------------------------------------------------------------------------------- 
			
			
	
	
	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		console.log("=---onReceive----------------------------");
		if (!intent.getAction().equals("android.intent.action.PHONE_STATE")){
	         return;
	     }else{
	         String realtimeState = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
	         console.log("onReceive STATE:" + realtimeState);       
	         
	         if(realtimeState.equals(TelephonyManager.EXTRA_STATE_IDLE)){notify_idle();}
	         if(realtimeState.equals(TelephonyManager.EXTRA_STATE_RINGING)){
	        	 String phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
	        	 notify_ringing(phoneNumber);
	         }
	         if(realtimeState.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){
	        	 notify_offhook();
	         }
	         
	         
	     }
		
	}
	
	
	//reportOnRinging
	//reportOnUniRinging
	//reportOnSecondaryRinging
	
	
	
	static private void notify_idle(){
		if(state == StateType.offhook){
			state = StateType.idle;
			reportOnEndCall();
		}else if(state == StateType.ringing){
			state = StateType.idle;
			reportOnMissedCall();		
		}				
	}
		
	static private void notify_offhook(){
		 if(state == StateType.ringing){
			 state = StateType.offhook;
			 reportOnAnswerCall();
		 }
		 state = StateType.offhook;
		 reportOnOffHook();
	}
	
	static private void notify_ringing(String inPhoneNumber){
		if(state == StateType.idle){
			//report uniring---
		}else if(state == StateType.offhook){
			//report seconadary ringing			
		}
		state = StateType.ringing;
		reportOnIncomingCall(inPhoneNumber);
	}
	
	
	
	
	
	
	
	
	
	
	
	

}
