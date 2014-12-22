package hopper.library.communication;

import android.R.bool;

public class DeviceObject{
	private int deviceId;
	private String caption;
	
	public DeviceObject(int inDeviceId, String inCaption){
		deviceId = inDeviceId;
		caption = inCaption;
	}
	
	public int getDeviceId(){
		return deviceId;
	}
	public void setDeviceId(int inDeviceId){
		deviceId = inDeviceId;
	}
	public String getCaption() {
		return caption;
	}
	public void setCaption(String inCaption){
		caption = inCaption;
	}
	
	public boolean isBrowser(){
		if(getType().equalsIgnoreCase("browser")){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean isApp(){
		if(getType().equalsIgnoreCase("app")){
			return true;
		}else{
			return false;
		}
	}
	
	public String getType(){
		String stringBrowser = "browser";		
		if(caption.toLowerCase().contains(stringBrowser.toLowerCase())){
			return stringBrowser;
		}
		

		String stringApp = "app";		
		if(caption.toLowerCase().contains(stringApp.toLowerCase())){
			return stringApp;
		}		
		
		return null;
	}
}
