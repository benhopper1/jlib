package com.example.hopperlibrary;

import java.io.IOException;
import java.util.HashMap;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;


public class HopperMediaPlayer {
	static HashMap<Integer, HopperMediaPlayer> mediaHash = new HashMap<Integer, HopperMediaPlayer>();
	
	private int id;
	private String urlPath = null;
	private MediaPlayer mediaPlayer;
	
	public boolean hasUrl(){
		if(urlPath == null){return false;}
		return true;
	}
	public HopperMediaPlayer(int inId) {
		id = inId;
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setOnCompletionListener(new OnCompletionListener(){
			@Override
			public void onCompletion(MediaPlayer arg0) {
				HopperMediaPlayer.this.onCompletion();				
			}
			
		});
		
		mediaPlayer.setOnErrorListener(new OnErrorListener(){
			@Override
			public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
				HopperMediaPlayer.this.onError();	
				return false;
			}
			
		});
		
		
		
	}
	
	static public HopperMediaPlayer getInstance(int inId){
		return mediaHash.get(inId);
	}
	
	static public HopperMediaPlayer getMaybeCreate(int inId){
		HopperMediaPlayer retObject = getInstance(inId);
		if(retObject == null){			
			retObject = new HopperMediaPlayer(inId);
		}		
		return retObject;		
	}
	private String lookForMp3(String inFileUrl){
		String filePath = HopperFileInfo.getPath(inFileUrl);
		String fileName = HopperFileInfo.getBasenameWithoutExtension(inFileUrl);		
		//String fileExtension = HopperFileInfo.getExtension(inFileUrl);
		String testFilePath = filePath+fileName+".mp3";
		if(HopperFileInfo.fileUrlexists(testFilePath)){
			console.log("FOUND MP3 format!!");
			return testFilePath; 
		}
		console.log("NO FOUND MP3 version found:"+testFilePath);
		return inFileUrl;
	}
	
	public void loadUrl(String inURL){
		urlPath = lookForMp3(inURL);
	}
	
	public void play(){
		if(urlPath == null){return;}
		
		try {
			mediaPlayer.setDataSource(urlPath);
			mediaPlayer.prepare();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		onPlay();
		mediaPlayer.start();
		
	}
	public void pause(){mediaPlayer.pause();onPause();}
	public void stop(){mediaPlayer.stop();onStop();}
	
	protected void onError(){}
	protected void onCompletion(){}
	protected void onPlay(){}
	protected void onPause(){}
	protected void onStop(){}
	
	

}
