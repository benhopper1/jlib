package com.example.hopperlibrary;

import java.util.ArrayList;

public class HopperMediaJukebox {
	private ArrayList<String> mediaArrayList; 
	private HopperMediaPlayer hopperMediaPlayer;
	
	
	public HopperMediaJukebox(ArrayList<String> inArrayList) {
		mediaArrayList = inArrayList;		
	}
	
	public void start(){
		if(mediaArrayList == null){return;}
		final ArrayList<String> tmpMediaArrayList = new ArrayList<String>(mediaArrayList);
		
		if(tmpMediaArrayList.size() >0){
			String firstAudioString = tmpMediaArrayList.get(0);
			console.log("firstAudioString"+firstAudioString);
			tmpMediaArrayList.remove(0);
			if(firstAudioString != null){
				hopperMediaPlayer = new HopperMediaPlayer(0){
					@Override
					protected void onCompletion(){
						console.log("HopperMediaJukebox onCompletion");
						HopperMediaJukebox thisOne = new HopperMediaJukebox(tmpMediaArrayList);
						thisOne.start();						
					}

					@Override
					protected void onError() {
						HopperMediaJukebox thisOne = new HopperMediaJukebox(tmpMediaArrayList);
						thisOne.start();
						console.log("MEDIA-ERROR");
						super.onError();
					}
					
				};
				hopperMediaPlayer.loadUrl(firstAudioString);
				hopperMediaPlayer.play();
			}
		}
	}
	
	
	

}
