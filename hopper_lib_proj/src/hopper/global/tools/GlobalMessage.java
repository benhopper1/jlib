package hopper.global.tools;

import java.util.HashMap;

public class GlobalMessage {
	static public HashMap<String, Object> objectHash = new HashMap<String, Object>();
	private GlobalMessage() {
		// TODO Auto-generated constructor stub
	}
	static public void putObject(String inKey, Object inObject){
		objectHash.put(inKey, inObject);
	}
	
	static public Object getObject(String inKey){
		return objectHash.get(inKey);
	}
	
	static public Object getAndRemoveObject(String inKey){
		Object tmpObject = objectHash.get(inKey);
		objectHash.remove(inKey);
		return tmpObject;
	}
	static public void removeObject(String inKey){		
		objectHash.remove(inKey);		
	}
	
	
	

}
