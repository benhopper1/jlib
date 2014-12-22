package hopper.library.time;

import android.util.Log;

import com.example.hopperlibrary.console;

public class MessureMe {
	public MessureMe(){}
	public long startTime;
	public long marktime;
	
	public void start(){
		startTime = System.nanoTime();
	}
	
	public void mark(String inName){
		marktime = System.nanoTime();
	}
	
	public void dump(){
		Log.v("MeasureMe", "------------------------MeasuerMe------------------------------------------");
		Log.v("MeasureMe",String.valueOf(marktime- startTime));
	}

}
