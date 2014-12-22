package hopper.library.model.v001;

import java.util.ArrayList;
import java.util.HashMap;


import android.annotation.SuppressLint;
import android.util.Log;
import android.widget.EditText;

import com.example.hopperlibrary.HopperCommunicationInterface;
import com.example.hopperlibrary.HopperDataset;
import com.example.hopperlibrary.HopperLocalArfInfo;
import com.example.hopperlibrary.console;

public class AccountModel {
	public boolean isNewAccount = false;
	private int userId = 0;
	
	private String firstName;
	private String lastName;
	private String emailAddress;
	private String screenName;
	private String password;
	private String city;
	private String state;
	private String country;
	private String imageUrl;
	private String localImagePath;
	public boolean isDirty;
	
	private HashMap<String,String> fieldsHashMap = new HashMap<String,String>();
	
	public AccountModel() {		
			userId = (int) HopperCommunicationInterface.get("COMM").userId;
			HopperDataset userDataset = new HopperDataset(HopperCommunicationInterface.get("COMM"));
			userDataset.executeSql("select * from vw_userData where id = "+String.valueOf(userId));
			firstName = userDataset.getFieldAsString("fName", 0);
			lastName = userDataset.getFieldAsString("lName", 0);
			emailAddress = userDataset.getFieldAsString("emailAddress", 0);
			screenName = userDataset.getFieldAsString("userName", 0);
			//password = userDataset.getFieldAsString("fName", 0);
			city = userDataset.getFieldAsString("city", 0);
			state = userDataset.getFieldAsString("state", 0);
			country = userDataset.getFieldAsString("country", 0);
			imageUrl = "http://www.walnutcracker.net/"+userDataset.getFieldAsString("file", 0);
			
			//ArrayList<String> fields = userDataset.getArrayOfFieldNames();
			
			
			
			
		
	}
	
	
	public void uiUpdate(){
		//EditText et_firstName = (EditText) findViewById(R.id.et_firstName);
	}
	
	
	
	public int updateAccount(){		
		HopperDataset newAccountDataset = new HopperDataset("COMM");
		console.log("CALL sp_account_new('" + this.getFirstName() + "', '" + this.getLastName() + "', '" + this.getEmailAddress() + "', '" + this.getScreenName() + "', '" + this.getPassword() + "', '" + this.getCity() + "', '" + this.getState() + "', '" + this.getCountry() + "');");
		
		//int newId = newAccountDataset.executeSqlWithReturnNewId("CALL sp_account_new('" + this.getFirstName() + "', '" + this.getLastName() + "', '" + this.getEmailAddress() + "', '" + this.getScreenName() + "', '" + this.getPassword() + "', '" + this.getCity() + "', '" + this.getState() + "', '" + this.getCountry() + "');");
		//newAccountDataset.executeSql("CALL sp_account_new('" + "TEDDY" + "', '" + "BROWN" + "', '" + "TEDDY@R.COM" + "', '" + "ted" + "', '" + "ted" + "', '" + "CITY" + "', '" + "STATE" + "', '" + "USA" + "');");

		newAccountDataset.executeSql("CALL sp_account_update('"+ this.getScreenName() + "', '" + this.getPassword() + "', '" + "" + "', '"
		 + "" + "', '"
		 + this.getFirstName() + "', '"
		 + this.getLastName() + "', '"
		 + this.getEmailAddress() + "', '"
		 + this.getCity() + "', '"
		 + this.getState() + "', '"
		 + this.getCountry() + "');");
		
		
		return newAccountDataset.getFieldAsInt("outPut", 0);
		//return Integer.parseInt(idAsStringResult);
	}
	
	
	@SuppressLint("NewApi")
	public String getLocalImagePath() {
		if(localImagePath == null || localImagePath.isEmpty() || localImagePath == "None" ){return "";}
		return localImagePath;
	}
	
	public void setLocalImagePath(String localImagePath) {
		this.localImagePath = localImagePath;
	}

	@SuppressLint("NewApi")
	public String getImageUrl() {
		if(imageUrl.isEmpty()){return "";}
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	
	
	public boolean userExistX(String inUserName){
		HopperDataset userDataset = new HopperDataset(HopperCommunicationInterface.get("COMM"));
		userDataset.executeSql("select * from vw_userData where userName = '" + inUserName + "'" );
		return userDataset.getRecordCount() > 0;
	}
	
	@SuppressLint("NewApi")
	public String getFirstName() {
		if(firstName.isEmpty()){return "";}
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@SuppressLint("NewApi")
	public String getLastName() {
		if(lastName.isEmpty()){return "";}
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@SuppressLint("NewApi")
	public String getEmailAddress() {
		if(emailAddress.isEmpty()){return "";}
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	@SuppressLint("NewApi")
	public String getScreenName() {
		if(screenName.isEmpty()){return "";}
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public String getPassword() {
		//-----Local Info ---------------------------------------------------  
        HopperLocalArfInfo localInfo = new HopperLocalArfInfo(null);       
		return localInfo.getField("password");
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@SuppressLint("NewApi")
	public String getCity() {
		if(city.isEmpty()){return "";}
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@SuppressLint("NewApi")
	public String getState() {
		if(state.isEmpty()){return "";}
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@SuppressLint("NewApi")
	public String getCountry() {
		if(country.isEmpty()){return "";}
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public boolean passwordChange(String oldPassword, String inPassword){
		return false;
	}
	
	//public void addData(String inFirstName, String inLastName,String inEmailAddress,String inUserName, String inPassword,String inCity,String inState,String inCountry ){
		
	//}
	
	public int getUserId(){
		return userId;
	}
	
	 
	
	

}
