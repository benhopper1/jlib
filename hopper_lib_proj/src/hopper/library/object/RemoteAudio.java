package hopper.library.object;

import java.io.IOException;

import com.example.hopperlibrary.HopperAsync;

import android.media.MediaPlayer;
import android.util.Log;

public class RemoteAudio {
	private String URL;
	private MediaPlayer mediaPlayer;
	public RemoteAudio() {
		mediaPlayer = new MediaPlayer();
	}
	
	public void setURL(String inURL){
		mediaPlayer.reset();
		URL = inURL;
		try {
			mediaPlayer.setDataSource(URL);
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
	}
	
	public String getURL(){return URL;}	
	public void play(){	
		try {
			mediaPlayer.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mediaPlayer.start();
	}
	public void pause(){}
	public void stop(){}
	
	

}
