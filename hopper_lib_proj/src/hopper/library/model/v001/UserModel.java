package hopper.library.model.v001;

import com.example.hopperlibrary.HopperDataset;
import com.example.hopperlibrary.console;

import hopper.library.object.UserObject;

public class UserModel {
	static final HopperDataset userDataset = new HopperDataset("COMM");
	
	
	private UserModel() {
		// TODO Auto-generated constructor stub
	}
	
	static public UserObject getUserObjectByUserId(int inId){
		UserObject tmpUserObject = new UserObject();
		
		userDataset.executeSql("select * from vw_userData where id =" + inId);
		if(userDataset.getRecordCount() > 0){
			tmpUserObject.setId(inId);
			int i =0;
			for(String fieldName : userDataset.getArrayOfFieldNames()){
				tmpUserObject.addProperty(fieldName, userDataset.getFieldAsString(fieldName, 0));
				console.log(fieldName+", "+userDataset.getFieldAsString(fieldName, 0),0);
			}			
			
		}else{
			return null;
		}
		return tmpUserObject;		
	}
}
