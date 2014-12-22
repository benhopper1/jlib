package hopper.library.fragment.qrcode;

import com.example.hopperlibrary.MainActivity;
import com.example.hopperlibrary.console;

import hopper.library.qrcode.IntentIntegrator;
import hopper.library.qrcode.IntentResult;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class QrReader_frag extends Fragment{
	private int layoutId;
	private OnUpdateListener listener;
	private Activity attachedActivity;
	
	public QrReader_frag(){			
			
	}
	
	//-------events----------------------------------------------------------------------------------------------------------------------
	
	public interface OnUpdateListener{
	    public void onUpdate(Object...objects);
	}
    public void setOnUpdateListener(OnUpdateListener inListener) {
    	this.listener = inListener;
    }
    private void reportOnUpdate(Object...objects){    	
    		this.listener.onUpdate(objects);        
    }
    //------
    
	 @Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
		 //View v = inflater.inflate(R.layout.blank, container, false);
		 IntentIntegrator integrator = new IntentIntegrator(this);
		 integrator.initiateScan();		 
		 return null;
		 
	 }
	 
	@Override
	public void onAttach(Activity activity) {
	    super.onAttach(activity);
	    attachedActivity = activity;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {		
	  	  IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
	  	  if (scanResult != null) {
	  		  // handle scan result
	  		  reportOnUpdate(scanResult, scanResult.getContents());
	  	  }else{
	  	  
	  	  }
    }
}
