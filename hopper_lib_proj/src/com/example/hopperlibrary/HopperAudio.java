package com.example.hopperlibrary;

import java.io.IOException;
import java.util.UUID;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.util.Log;

public class HopperAudio {
	private MediaRecorder mRecorder;
	private String mFileName;  // path and filename
	private Context context;
	private Activity activity;
	private MediaPlayer mPlayer;
	
	public HopperAudio(Activity inActivity){
		Log.v("MyActivity","HopperAudio CREATED ");
		this.activity=inActivity;
		this.context=inActivity;
		this.init();
	}
	public String createFileName(){
		String appDir=this.context.getFilesDir().toString()+"/";
		String newFileName =appDir+"fileRecording_"+UUID.randomUUID().toString();	
		return newFileName;
	}
	public void init(){
		Log.v("MyActivity","HopperAudio init ENTERED ");
		this.mRecorder = new MediaRecorder();
		Log.v("MyActivity","HopperAudio init ENTERED 2");
		this.mRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
		Log.v("MyActivity","HopperAudio init ENTERED 3");
		this.mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		Log.v("MyActivity","HopperAudio init ENTERED 4");
		Log.v("MyActivity","Path "+this.createFileName());		
		this.mRecorder.setOutputFile(this.createFileName());
		this.mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);	
	}
	
	public void startRecording() {    
	    try {
	        this.mRecorder.prepare();
	    } catch (IOException e) {
	        Log.e("MyActivity", "prepare() failed");
	    }
	
	    mRecorder.start();
	}
	public void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }
	public void startPlaying() {
        this.mPlayer = new MediaPlayer();
        try {
        	this.mPlayer.setDataSource(this.mFileName);
        	this.mPlayer.prepare();
        	this.mPlayer.start();
        } catch (IOException e) {
            Log.e("MyActivity", "prepare() failed");
        }
    }


		

}
