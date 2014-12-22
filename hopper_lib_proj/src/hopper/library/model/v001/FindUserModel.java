// USE-----
//	getMaybeCreate(Context inContext, false)
//	setCriteria(String inSearchString)
//	setLimitLowValue()
//	setLimitHighValue(int limitHighValue)
//  refresh()
// 	obj = getData()
//---------- easy way------
// 	getMaybeCreate(Context inContext, false)
// 	getFreshData()

package hopper.library.model.v001;


import hopper.cache.ImageCache;
import hopper.library.object.UserObject;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import com.example.hopperlibrary.HopperAsync;
import com.example.hopperlibrary.HopperDataset;
import com.example.hopperlibrary.HopperFileInfo;
import com.example.hopperlibrary.HopperJsonStatic;
import com.example.hopperlibrary.HopperLocalArfInfoStatic;
import com.example.hopperlibrary.console;

import android.content.Context;
import android.widget.ImageView;

public class FindUserModel extends RemoteModel{
	
	protected static FindUserModel instanceVar;// = new FindUserModel();
	private static Context context;
	private int limitLowValue = 0;
	private int limitHighValue = 0;
	private int countOfRecords = -1;
	
	
	public ArrayList<UserObject> getFreshData(String inSearchString, int inTopLow, int inTopHigh){
		setCriteria(inSearchString);
		setLimitLowValue(inTopLow);
		setLimitHighValue(inTopHigh);
		refresh();
		return (ArrayList<UserObject>) getData();		
	}
	
	private String criteriaSearchString;
	
	public int getCountOfRecords() {
		return countOfRecords;
	}
	private void setCountOfRecords(int countOfRecords) {
		this.countOfRecords = countOfRecords;
	}
	public int getLimitLowValue() {
		return limitLowValue;
	}
	public void setLimitLowValue(int limitLowValue) {
		this.limitLowValue = limitLowValue;
	}
	public int getLimitHighValue() {
		return limitHighValue;
	}
	public void setLimitHighValue(int limitHighValue) {
		this.limitHighValue = limitHighValue;
	}
	private FindUserModel() {
		
	}
	public static FindUserModel getMaybeCreate(Context inContext, boolean inAutoRefreshOnStartup){
		context = inContext;
		FindUserModel tmpObject = instanceVar;
		if(tmpObject != null){
			return tmpObject;
		}
		tmpObject = new FindUserModel();
		instanceVar = tmpObject;
		if(inAutoRefreshOnStartup){tmpObject.refresh();}
		return tmpObject;
	}
	
	public void setCriteria(String inSearchString){
		criteriaSearchString = inSearchString;		
	}
	public String getCriteria(){
		return criteriaSearchString;
	}
	
	@Override
	protected void onRefresh() {
        ArrayList<UserObject> userArrayList = new ArrayList<UserObject>();
        final HopperDataset tmpDataset = new HopperDataset("COMM");
        
        if(criteriaSearchString != null){
        	tmpDataset.executeSql("CALL sp_fuzzyFindUsers('" + criteriaSearchString + "', " + String.valueOf(limitLowValue) + " , " + String.valueOf(limitHighValue - limitLowValue) + " );");        
        	countOfRecords = tmpDataset.getFieldAsInt("countOf", 0);
        	for(int i = 0; i < tmpDataset.getRecordCount(); i++){
        		UserObject userObject = new UserObject();
        		userObject.setId(tmpDataset.getFieldAsInt("id", i));
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
        		
        		userArrayList.add(userObject);
        	}
        	
        	final ArrayList<UserObject> userArrayList2 = new ArrayList<UserObject>(userArrayList);
        			for(int i = 0; i< userArrayList2.size(); i++){
        				UserObject userObject = userArrayList2.get(i);
	        			//----prebuild image cach----
	                	if(userObject.getProperty("imageId") != "None"){
	                		int imageId = Integer.parseInt(userObject.getProperty("imageId"));
	        				if(imageId > 0){
	        					//String imagePath = HopperLocalArfInfoStatic.getField("requestResponseImagePath") + HopperFileInfo.getBasenameWithExtension(userObject.getProperty("imageFilePath"));
	        					ImageCache.addRemoteImageToMemoryCache(imageId, userObject.getProperty("imageFilePath"));    					    					
	        				}
	                	}
        			}
        	data = userArrayList;
        }
		
	}

}
