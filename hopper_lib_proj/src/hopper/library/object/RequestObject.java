package hopper.library.object;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;

public class RequestObject extends ConversationObject{

	static private HashMap<Integer, RequestObject> hashMapOfInstance = new HashMap<Integer, RequestObject>();	
	private ArrayList<ResponseObject> responsesArrayList = new ArrayList<ResponseObject>();
	
	
	private RequestObject(Context inContext, int inId) {		
		super(inContext);
		this.setId(inId);
	}
	
	public static RequestObject getOrMaybeCreate(Context inContext, int inId){
		RequestObject tmpObject;
		tmpObject = hashMapOfInstance.get((Integer)inId);		
		if(tmpObject == null){			
			tmpObject = new RequestObject(inContext, inId);			
			hashMapOfInstance.put((Integer)inId, tmpObject);
		}else{
			tmpObject.setContext(inContext);
		}	
		return tmpObject;		
	}
	
	public void addResponse(ResponseObject inResponseObject){
		responsesArrayList.add(inResponseObject);
	}
	
	public ArrayList<ResponseObject> getResponsesArrayList(){
		return responsesArrayList;
	}


}
