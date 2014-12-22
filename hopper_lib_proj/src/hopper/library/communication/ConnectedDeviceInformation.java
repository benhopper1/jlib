package hopper.library.communication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.example.hopperlibrary.console;

public class ConnectedDeviceInformation{
	static private LinkedHashMap<String, DeviceObject> deviceHash = new LinkedHashMap<String, DeviceObject>();
	
	
	static public void addDevice(int inDeviceId, String inCaption){
		console.log("ConnectedDeviceInformation addDevice check "+inDeviceId+inCaption);
		DeviceObject tmpObject = deviceHash.get(String.valueOf(inDeviceId));
		if(tmpObject == null){
			tmpObject = new DeviceObject(inDeviceId, inCaption);
			deviceHash.put(String.valueOf(inDeviceId), tmpObject);
		}
		console.log("addDevice finished");
	}
	
	static public void removeDevice(int inDeviceId){
		deviceHash.remove(String.valueOf(inDeviceId));
	}
	
	static public ArrayList<DeviceObject> getDevices(){
		return (ArrayList<DeviceObject>) deviceHash.values();		
	}
	
	static public DeviceObject getDeviceObject(int inDeviceId){
		return deviceHash.get(String.valueOf(inDeviceId));		
	}
	
	static public DeviceObject getLastBrowserConnected(){
		ArrayList<DeviceObject> arrayOfDevice = convertToArrayList();
		for(int i = arrayOfDevice.size()-1; i > -1; i--){
			console.log("test:"+arrayOfDevice.get(i).getCaption(),i);
			if(arrayOfDevice.get(i).isBrowser()){				
				return arrayOfDevice.get(i);
			}			
		}
		
		return null;		
	}
	
	static private ArrayList<DeviceObject> convertToArrayList(){
		ArrayList<DeviceObject> tmpList = new ArrayList<DeviceObject>();
		for(DeviceObject deviceObject : deviceHash.values()){
			tmpList.add(deviceObject);
		}
		return tmpList;
	}
	
	static public DeviceObject getLastAppConnected(){
		ArrayList<DeviceObject> arrayOfDevice = convertToArrayList();
		for(int i = arrayOfDevice.size()-1; i > 0; i--){
			if(arrayOfDevice.get(i).isApp()){
				return arrayOfDevice.get(i);
			}
		}
		
		return null;		
	}
	
	
}
