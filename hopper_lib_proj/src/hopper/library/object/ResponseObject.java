package hopper.library.object;

import java.util.HashMap;

import android.content.Context;

public class ResponseObject extends ConversationObject{
	static private HashMap<Integer, ResponseObject> hashMapOfInstance = new HashMap<Integer, ResponseObject>(); 

	private ResponseObject(Context inContext, int inId) {		
		super(inContext);
		this.setId(inId);
	}
	
	public static ResponseObject getOrMaybeCreate(Context inContext, int inId){
		ResponseObject tmpObject;
		tmpObject = hashMapOfInstance.get((Integer)inId);		
		if(tmpObject == null){			
			tmpObject = new ResponseObject(inContext, inId);
			hashMapOfInstance.put((Integer)inId, tmpObject);
		}else{
			tmpObject.setContext(inContext);
		}
	
		return tmpObject;		
	}
}
