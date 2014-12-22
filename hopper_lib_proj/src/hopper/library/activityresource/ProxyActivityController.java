package hopper.library.activityresource;

import com.example.hopperlibrary.HopperInstanceHash;
import com.example.hopperlibrary.console;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class ProxyActivityController {
	
	
	private ProxyParcel proxyParcel;

	public ProxyActivityController() {
		proxyParcel = new ProxyParcel();
	}
	
	/*
	 *	startActivityForResult: (2 signatures)
	 * 		1.	(Intent intent, int requestCode, Bundle options)
	 * 		2.	(Intent intent, int requestCode)
	 * 
	 * 	Set this up after instanation....
	 */
	public void startProxy(Intent inIntent, int inRequestCode, Bundle inOptions){
		proxyParcel.startupIntent = inIntent;
		proxyParcel.startupRequestCode = inRequestCode;
		proxyParcel.startupOptions = inOptions;
		
		startActivity();
	}
	
	public void startProxy(Intent inIntent, int inRequestCode){
		proxyParcel.startupIntent = inIntent;
		proxyParcel.startupRequestCode = inRequestCode;	
		startActivity();
	}
	
	public ProxyParcel getParcelInstance(){
		return proxyParcel;
	}
	
	public void putExtra(String inKey, String inValue){
		proxyParcel.startupIntent.putExtra(inKey, inValue);		
	}
	public void putExtra(String inKey, int inValue){
		proxyParcel.startupIntent.putExtra(inKey,String.valueOf(inValue));
	}
	
	/*
	 * -------EVENT----------------------------------------------------------------
	 */
	public void setOnComplete(ProxyParcel.OnCompleteListener inOnCompleteListener){
		proxyParcel.setOnCompleteListener(inOnCompleteListener);
	}
	
	
	
	private void startActivity(){
		console.log("ProxyActivityController.startActivity ENTERED");
		Intent intent = new Intent(HopperInstanceHash.getInstance("MainActivity"), ProxyActivity.class);
		//Intent intent = new Intent(whoStartingActivity, classToBeStarted);
		intent.putExtra("proxyParcel",proxyParcel);
		HopperInstanceHash.getInstance("MainActivity").startActivity(intent);
		
		
		//HopperInstanceHash.getInstance("MainActivity").startActivity(startupIntent);
	}
	
	
			

}
