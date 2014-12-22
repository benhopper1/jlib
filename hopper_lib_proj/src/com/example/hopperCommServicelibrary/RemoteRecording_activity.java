package com.example.hopperCommServicelibrary;



//com.example.hopperCommServicelibrary.RemoteRecording_activity


import hopper.library.R;

import java.util.Timer;
import java.util.TimerTask;

import com.example.hopperlibrary.HopperAudioMachine;
import com.example.hopperlibrary.HopperInstanceHash;
import com.example.hopperlibrary.HopperLocalArfInfoStatic;
import com.example.hopperlibrary.HopperTimer;



import com.example.hopperlibrary.console;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class RemoteRecording_activity extends Activity {
	private int light_state_0 = 0;  //waiting on signal from server
	private int light_state_1 = 1;	//record audio anticipation
	private int light_state_2 = 2;	//recording audio now
	private int light_state_3 = 3;	//done/analizing
	private int light_state_4 = 4;	//transmiting
	private ImageView iv_light_0;
	private ImageView iv_light_1;
	private ImageView iv_light_2;
	private ImageView iv_light_3;
	private ImageView iv_light_4;
	
	private HopperTimer light_timer_0;
	private HopperTimer light_timer_1;
	private HopperTimer light_timer_2;
	private HopperTimer light_timer_3;
	private HopperTimer light_timer_4;
	
	private int oldTimerInterval = -1;
	
	
	
	// cool fix, if touch outside of dialog, then I trap here and do nothing!!! hahaha
	@Override
	  public boolean onTouchEvent(MotionEvent event) {
	    // If we've received a touch notification that the user has touched
	    // outside the app, finish the activity.
	    if (MotionEvent.ACTION_OUTSIDE == event.getAction()) {
	      //finish();
	      return true;
	    }	    
	    return true;
	  }
	@Override
	public void onStop () {
		HopperInstanceHash.remove("RemoteRecording_activity");
		super.onStop();
		}
	
	@Override
	 protected void onDestroy() {
		if(oldTimerInterval != -1){
			HopperProcessMessages.getInstance().restart(oldTimerInterval);			 
		}else{
			HopperProcessMessages.getInstance().restart(Integer.parseInt(HopperLocalArfInfoStatic.getField("processMessagesInterval")));
		}
	  
	  super.onDestroy();	  
	 }

	 
	 @Override
	 protected void onStart() {
	  if(oldTimerInterval != -1){
		  HopperProcessMessages.getInstance().restart(Integer.parseInt(HopperLocalArfInfoStatic.getField("processMessagesInterval_multiDevice_remoteRecording")));			 
	  }else{
		  oldTimerInterval = HopperProcessMessages.getInstance().getMessageCheckInterval();
		  HopperProcessMessages.getInstance().restart(Integer.parseInt(HopperLocalArfInfoStatic.getField("processMessagesInterval_multiDevice_remoteRecording")));
	  }
	  super.onStart();	  
	 }

	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);   
        HopperInstanceHash.add("RemoteRecording_activity", this);
        setContentView(R.layout.remoterecording_layout);
        int processMessagesInterval_multiDevice_remoteRecording = Integer.parseInt(HopperLocalArfInfoStatic.getField("processMessagesInterval_multiDevice_remoteRecording"));
   //     HopperProcessMessages.getInstance().restart(newInterval)
       
	   	console.log("CONSTRUCTOR RemoteRecording_activity");
		iv_light_0 = (ImageView) findViewById(R.id.iv_light_0);
		iv_light_1 = (ImageView) findViewById(R.id.iv_light_1);
		iv_light_2 = (ImageView) findViewById(R.id.iv_light_2);
		iv_light_3 = (ImageView) findViewById(R.id.iv_light_3);
		iv_light_4 = (ImageView) findViewById(R.id.iv_light_4);
       
  //     setLight(0,1);
  //     setLight(2,2);
  //     setLight(4,1);
  //     toggle(2,2,1); 
  //  	blink_start(0, 0, 1, 100);
 //   	blink_start(1, 0, 2, 100);
 //   	light_timer_0.stop();
 //   	light_timer_0.restart(1000);
		
		setState(1);
    	
       
    	//######################################################################
        //######BUTTON CLICK ###################################################
    	ImageButton ib_review_rr = (ImageButton) findViewById(R.id.ib_review_rr);
    	ib_review_rr.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0){  				
				HopperAudioMachine HAM = HopperAudioMachine.getMaybeCreate("remotePlayer");
				HAM.play();
			}});
        //######################################################################
       
	}
	
	public void setState(int inState){
		blink_stop(0);
		blink_stop(1);
		blink_stop(2);
		blink_stop(3);
		blink_stop(4);
		
		if(inState == 0){
			setLight(0,0);
			setLight(1,0);
			setLight(2,0);
			setLight(3,0);
			setLight(4,0);
		}
		
		if(inState == 1){//waiting on record signal
			blink_start(0, 0, 1, 500);
			setLight(1,0);
			setLight(2,0);
			setLight(3,0);
			setLight(4,0);
		}
		if(inState == 2){//record anticipate
			setLight(0,1);
			blink_start(1, 0, 1, 500);
			setLight(2,0);
			setLight(3,0);
			setLight(4,0);
		}
		if(inState == 3){//recording
			setLight(0,1);
			setLight(1,1);
			blink_start(2, 0, 2, 500);			
			setLight(3,0);
			setLight(4,0);
		}
		if(inState == 4){//done/analizing
			setLight(0,1);
			setLight(1,1);
			setLight(2,1);
			blink_start(3, 0, 1, 500);
			setLight(4,0);
		}
		if(inState == 5){//transmiting to server
			setLight(0,1);
			setLight(1,1);
			setLight(2,1);
			setLight(3,1);
			blink_start(4, 0, 1, 500);			
		}		
	}
	
	public void setCaption(String inText){
		EditText et_caption_rr = (EditText) findViewById(R.id.et_caption_rr);
		et_caption_rr.setText(inText);
	}
	
	

	public void toggle(int inIndex, int inVal0, int inVal1){
		if(getLight(inIndex) == inVal0){
			setLight(inIndex, inVal1);
		}else{
			setLight(inIndex, inVal0);
		}		
	}
	
	public int getLight(int inIndex){
		if(inIndex == 0){return light_state_0;}
		if(inIndex == 1){return light_state_1;}
		if(inIndex == 2){return light_state_2;}
		if(inIndex == 3){return light_state_3;}
		if(inIndex == 4){return light_state_4;}
		return -1;		
	}
	
	public void setLight(int inIndex, int inState){		
		if(inIndex == 0){
			if(inState == 0){iv_light_0.setImageResource(R.drawable.stat_light_null);light_state_0 = 0;}
			if(inState == 1){iv_light_0.setImageResource(R.drawable.stat_light_on);light_state_0 = 1;}
			if(inState == 2){iv_light_0.setImageResource(R.drawable.stat_light_done);light_state_0 = 2;}
			return;
		}
		
		if(inIndex == 1){
			if(inState == 0){iv_light_1.setImageResource(R.drawable.stat_light_null);light_state_1 = 0;}
			if(inState == 1){iv_light_1.setImageResource(R.drawable.stat_light_on);light_state_1 = 1;}
			if(inState == 2){iv_light_1.setImageResource(R.drawable.stat_light_done);light_state_1 = 2;}
			return;
		}
		
		if(inIndex == 2){
			if(inState == 0){iv_light_2.setImageResource(R.drawable.stat_light_null);light_state_2 = 0;}
			if(inState == 1){iv_light_2.setImageResource(R.drawable.stat_light_on);light_state_2 = 1;}
			if(inState == 2){iv_light_2.setImageResource(R.drawable.stat_light_done);light_state_2 = 2;}
			return;
		}
		
		if(inIndex == 3){
			if(inState == 0){iv_light_3.setImageResource(R.drawable.stat_light_null);light_state_3 = 0;}
			if(inState == 1){iv_light_3.setImageResource(R.drawable.stat_light_on);light_state_3 = 1;}
			if(inState == 2){iv_light_3.setImageResource(R.drawable.stat_light_done);light_state_3 = 2;}
			return;
		}
		
		if(inIndex == 4){
			if(inState == 0){iv_light_4.setImageResource(R.drawable.stat_light_null);light_state_4 = 0;}
			if(inState == 1){iv_light_4.setImageResource(R.drawable.stat_light_on);light_state_4 = 1;}
			if(inState == 2){iv_light_4.setImageResource(R.drawable.stat_light_done);light_state_4 = 2;}
			return;
		}		
	}
	
	public void blink_start(final int inIndex, final int inVal0, final int inVal1, int inTime){
		if(inIndex == 0){
			light_timer_0 = new HopperTimer(inTime){    		
	    		@Override
	    		public void onAlarm(){
	    			toggle(inIndex,inVal0,inVal1);
	    		}
	    	};
		}
		if(inIndex == 1){
			light_timer_1 = new HopperTimer(inTime){    		
	    		@Override
	    		public void onAlarm(){
	    			toggle(inIndex,inVal0,inVal1);
	    		}
	    	};
		}
		if(inIndex == 2){
			light_timer_2 = new HopperTimer(inTime){    		
	    		@Override
	    		public void onAlarm(){
	    			toggle(inIndex,inVal0,inVal1);
	    		}
	    	};
		}
		if(inIndex == 3){
			light_timer_3 = new HopperTimer(inTime){    		
	    		@Override
	    		public void onAlarm(){
	    			toggle(inIndex,inVal0,inVal1);
	    		}
	    	};
		}
		if(inIndex == 4){
			light_timer_4 = new HopperTimer(inTime){    		
	    		@Override
	    		public void onAlarm(){
	    			toggle(inIndex,inVal0,inVal1);
	    		}
	    	};
		}
		
	}
	
	
	public void blink_stop(final int inIndex){
		if(inIndex == 0){if(light_timer_0 != null ){light_timer_0.stop();}}
		if(inIndex == 1){if(light_timer_1 != null ){light_timer_1.stop();}}
		if(inIndex == 2){if(light_timer_2 != null ){light_timer_2.stop();}}
		if(inIndex == 3){if(light_timer_3 != null ){light_timer_3.stop();}}
		if(inIndex == 4){if(light_timer_4 != null ){light_timer_4.stop();}}
	}
	
	
	
	
}


