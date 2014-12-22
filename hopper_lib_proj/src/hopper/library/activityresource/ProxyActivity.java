package hopper.library.activityresource;



import hopper.library.activityresource.ProxyParcel;
import hopper.library.model.ProfileModel;


import com.example.hopperlibrary.console;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class ProxyActivity extends Activity{
	private Intent thisIntent;
	private ProxyParcel proxyParcel;
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		
		thisIntent = getIntent();		
		proxyParcel = getIntent().getParcelableExtra("proxyParcel");		
        startActivityForResult(proxyParcel.startupIntent,  proxyParcel.startupRequestCode);		
		
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){		
		super.onActivityResult(requestCode, resultCode, data);
	    if(resultCode == RESULT_OK){	    	
            if(requestCode == proxyParcel.startupRequestCode){
            	proxyParcel.reportOnComplete(this, data);
            	finish();               
            }
        }
	}

}
