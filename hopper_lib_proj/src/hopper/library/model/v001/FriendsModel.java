package hopper.library.model.v001;

import hopper.cache.ImageCache;
import hopper.library.object.UserObject;

import java.util.ArrayList;

import android.content.Context;

import com.example.hopperlibrary.HopperCommunicationInterface;
import com.example.hopperlibrary.HopperDataset;
import com.example.hopperlibrary.HopperFileInfo;
import com.example.hopperlibrary.HopperLocalArfInfoStatic;

public class FriendsModel extends RemoteModel{
	protected static FriendsModel instanceVar;// = new FindUserModel();
	private static Context context;
	
	private FriendsModel(){
		// TODO Auto-generated constructor stub
	}
	
	
	
	@Override
	protected void onRefresh() {		
        ArrayList<UserObject> userArrayList = new ArrayList<UserObject>();
        final HopperDataset tmpDataset = new HopperDataset("COMM");
        tmpDataset.executeSql("CALL sp_getFriends( " + (int)HopperCommunicationInterface.get("COMM").userId + " );");        
    	int countOfRecords = tmpDataset.getRecordCount();
    	for(int i = 0; i < countOfRecords; i++){
    		UserObject userObject = new UserObject();
    		
    		userObject.setId(tmpDataset.getFieldAsInt("id", i));
    		userObject.addProperty("id", tmpDataset.getFieldAsString("id", i));
    		userObject.addProperty("fName", tmpDataset.getFieldAsString("fName", i));
    		userObject.addProperty("lName", tmpDataset.getFieldAsString("lName", i));
    		userObject.addProperty("emailAddress", tmpDataset.getFieldAsString("emailAddress", i));
    		userObject.addProperty("screenName", tmpDataset.getFieldAsString("userName", i));
    		userObject.addProperty("status", tmpDataset.getFieldAsString("status", i));
    		userObject.addProperty("city", tmpDataset.getFieldAsString("city", i));
    		userObject.addProperty("state", tmpDataset.getFieldAsString("state", i));
    		userObject.addProperty("country", tmpDataset.getFieldAsString("country", i));
    		String imagePath = HopperLocalArfInfoStatic.getField("requestResponseImagePath") + HopperFileInfo.getBasenameWithExtension(tmpDataset.getFieldAsString("file", i));
    		userObject.addProperty("imageFilePath", imagePath);        		
    		userObject.addProperty("imageId", tmpDataset.getFieldAsString("imageId", i));  
    		
    		
    		// preload image cach
    		if(userObject.getProperty("imageId") != "None"){
        		int imageId = Integer.parseInt(userObject.getProperty("imageId"));
				if(imageId > 0){					
					ImageCache.addRemoteImageToMemoryCache(imageId, imagePath);    					    					
				}
        	}
    		
    		userArrayList.add(userObject);
    	}
        
    	data = userArrayList;
        
	}
        
        
        
    
    public static FriendsModel getMaybeCreate(Context inContext, boolean inAutoRefreshOnStartup){
		context = inContext;
		FriendsModel tmpObject = instanceVar;
		if(tmpObject != null){
			return tmpObject;
		}
		tmpObject = new FriendsModel();
		instanceVar = tmpObject;
		if(inAutoRefreshOnStartup){tmpObject.refresh();}
		return tmpObject;
	}

}
