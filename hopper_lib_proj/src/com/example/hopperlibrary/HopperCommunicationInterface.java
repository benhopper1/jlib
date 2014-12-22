package com.example.hopperlibrary;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.util.Log;



public class HopperCommunicationInterface {
	private HopperJson hJson;
	
	private static Map<String,HopperCommunicationInterface > HopperCommunicationInterfaceMap;
	static{  HopperCommunicationInterfaceMap = new HashMap<String,HopperCommunicationInterface >();}
	
	//public HopperClientSocketByteArray HCS_byteArray;
	//public HopperClientSocket HCS_string;
	public HopperUniversalClientSocket HUCS;
	public long userId; // same as security_0
	public long security_1;
	public long security_2;
	public long security_3;	
	public int deviceId;
	public String deviceNumber;
	public String firstName;
	public String lastName;
	public String emailAddress;
	public String userName;
	public int loginGood;
	
	
	
	
	public static HopperCommunicationInterface get(String inConnectionName){
		HopperCommunicationInterface ret= HopperCommunicationInterfaceMap.get(inConnectionName);
		if(ret==null){
			Log.v("MyActivity","ERROR HopperCommunicationInterfaceMap NULL, create a connection somewhere in your startup with '"+inConnectionName+"'");
		}
		return ret;
	}
	
	//-----thiiii
	public static HopperCommunicationInterface getAndMaybeCreate(String inHost, int inPort, String inUser, String inPassword, String inConnectionName){
		Log.v("MyActivity","HopperCommunicationInterface STATIC getAndMaybeCreate ENTERED");
		HopperCommunicationInterface HCI = HopperCommunicationInterfaceMap.get(inConnectionName);
		if(HCI==null){
			Log.v("MyActivity","HopperCommunicationInterface STATIC getAndMaybeCreate requested object not exist, will create");
			HCI = new HopperCommunicationInterface(inHost,inPort, inUser, inPassword);				
			HopperCommunicationInterfaceMap.put(inConnectionName, HCI);
		}
		return HCI;		
	}
	
	
	
	public String getDeviceId(){
		Activity this_context = null;
		HopperLocalArfInfo HLAI = new HopperLocalArfInfo(this_context);
		String tmp = HLAI.getField("deviceId").trim();
		Log.v("newStuff", tmp );
		if(tmp == null || tmp == "" || tmp.length()<1){
			return "-1";			
		}
        return tmp;
	}	
	
	public HopperCommunicationInterface(String inHost, int inPort, String inUser, String inPassword){		
		String deviceId = this.getDeviceId();   //remove for production
		Activity this_context = null;
		HopperLocalArfInfo HLAI = new HopperLocalArfInfo(this_context);
		
		
		
		String inDeviceNumber = HopperDevice.eId;
		this.deviceNumber=inDeviceNumber;
		String deviceTypeString = "MOBILE-APPLICATION";
		Log.v("MyActivity","HopperCommunicationInterface instance constructor hst,prt,user,pssw ENTERED");
		hJson = new HopperJson();
		//HCS_byteArray =new HopperClientSocketByteArray(inHost,inPort);
		//HCS_string=new HopperClientSocket(HCS_byteArray.sock);
				
		// login-----------------
        HUCS = new HopperUniversalClientSocket(inHost,inPort);   
        String sString="{\"command\":\"login\",  \"user\":\""+inUser+"\",  \"deviceId\":\""+deviceId+"\", \"password\":\""+inPassword+"\",  \"deviceTypeString\":\""+deviceTypeString+"\",  \"deviceNumber\":\""+inDeviceNumber+"\"}";
        HUCS.sendHeader(2000,1000L,2000L,3000L, 4000L,sString.getBytes().length);
        HUCS.sendString_2(sString);        
        
        //server response of AK--------------------------
        HUCS.receiveHeader();
        String newString = HUCS.receiveString();        
        Log.v("MyActivity","HopperCommunicationInterface constructor, AK server rsp :"+newString);
        
        //server response of security info --------------------------
        HUCS.receiveHeader();
        newString = HUCS.receiveString();        
        Log.v("MyActivity","HopperCommunicationInterface constructor, security info server rsp :"+newString);
        JSONObject jSon_fromServer = HopperJsonStatic.getJsonObjectFromString(newString);
        this.userName=HopperJsonStatic.getStringFromKeyForJsonObject(jSon_fromServer, "userName");
        this.firstName=HopperJsonStatic.getStringFromKeyForJsonObject(jSon_fromServer, "firstName");
        this.lastName=HopperJsonStatic.getStringFromKeyForJsonObject(jSon_fromServer, "lastName");
        this.emailAddress=HopperJsonStatic.getStringFromKeyForJsonObject(jSon_fromServer, "emailAddress");
        this.userId= Integer.parseInt(HopperJsonStatic.getStringFromKeyForJsonObject(jSon_fromServer, "userId"));        
        this.loginGood= HopperJsonStatic.getIntFromKeyForJsonObject(jSon_fromServer,"loginStatus");
        this.deviceId= HopperJsonStatic.getIntFromKeyForJsonObject(jSon_fromServer,"deviceId");
        
        HLAI.putField("deviceId", String.valueOf(this.deviceId)); // for manual, change back for normal use
      //  deviceId=HopperMiscStatic.getMacAddress();
        
        Log.v("MyActivity","deviceId:"+deviceId);        
		
	}
	
	public HopperCommunicationInterface(String inHost, int inPort, String inConnectionName){
		Log.v("MyActivity","HopperCommunicationInterface ENTERED");
		HUCS = new HopperUniversalClientSocket(inHost,inPort);
		Log.v("MyActivity","HopperCommunicationInterface ESTABLISHED CONNECTION TO SERVER");
		HopperCommunicationInterfaceMap.put(inConnectionName, this);
		Log.v("MyActivity","HopperCommunicationInterface STATICALLY STORED THIS INSTANCE TO CONNECTIONMAP KEY:"+inConnectionName);
}
	
	public void login(String inUser, String inPassword){
		String deviceTypeString = "nexis13488-ANDROIDtest";
		String inDeviceNumber = HopperDevice.eId;;
		Log.v("MyActivity","HopperCommunicationInterface.login ENTERED usr/pswrd:"+inUser+" ******");
        String sString="{\"command\":\"login\",  \"user\":\""+inUser+"\",\"password\":\""+inPassword+"\",\"deviceNumber\":\""+inDeviceNumber+"\",  \"deviceTypeString\":\""+deviceTypeString+"\"}";
        HUCS.sendHeader(2000,1000L,2000L,3000L, 4000L,sString.getBytes().length);
        HUCS.sendString_2(sString);        
        
        //server response of AK--------------------------
        HUCS.receiveHeader();
        String newString = HUCS.receiveString();        
        Log.v("MyActivity","HopperCommunicationInterface constructor, AK server rsp :"+newString);
        
        //server response of security info --------------------------
        HUCS.receiveHeader();
        newString = HUCS.receiveString(); 
        
        //-- get security login data here----------
        Log.v("MyActivity","HopperCommunicationInterface constructor, security info server rsp :"+newString);		
        JSONObject jSon_fromServer = HopperJsonStatic.getJsonObjectFromString(newString);
        this.userName=HopperJsonStatic.getStringFromKeyForJsonObject(jSon_fromServer, "userName");
        this.firstName=HopperJsonStatic.getStringFromKeyForJsonObject(jSon_fromServer, "firstName");
        this.lastName=HopperJsonStatic.getStringFromKeyForJsonObject(jSon_fromServer, "lastName");
        this.emailAddress=HopperJsonStatic.getStringFromKeyForJsonObject(jSon_fromServer, "emailAddress");
        this.userId= Integer.parseInt(HopperJsonStatic.getStringFromKeyForJsonObject(jSon_fromServer, "userId"));        
        this.loginGood= HopperJsonStatic.getIntFromKeyForJsonObject(jSon_fromServer,"loginStatus");
        
	}
	
	
	
	
	//RequestByteArray
	public String getExistingRequestByteArray(){return "";}
	//JSONObject inJson = getJsonObjectFromString( inString); create json from string incomeing!!
	//String sessionId = getStringFromKeyForJsonObject(inJson,"newSessionId");
	

	public String createNewArfAndReceiveNewFileInformation(String inCaption,long inTimeStamp,String inSearchString){ //retruns json with new arf id...
		JSONObject json_outToServer = new JSONObject(); //hJson.getJsonObjectFromString("Data");
		hJson.putKeyValueStringsForJsonObject(json_outToServer,"command","createNewArfAndReceiveNewFileInformation");
		hJson.putKeyValueStringsForJsonObject(json_outToServer,"caption",inCaption);
		hJson.putKeyValueStringsForJsonObject(json_outToServer,"timeStamp",String.valueOf(inTimeStamp));
		hJson.putKeyValueStringsForJsonObject(json_outToServer,"searchString",inSearchString);
		Log.v("MyActivity","createNewArfAndReceiveNewFileInformation SENDING HEADER");
		// sending request----------------------
		HUCS.sendHeader(2000,1000L,2000L,3000L, 4000L,json_outToServer.toString().getBytes().length);
		Log.v("MyActivity","createNewArfAndReceiveNewFileInformation SENDING json payload--:"+json_outToServer.toString());
		HUCS.sendString_2(json_outToServer.toString());
		
        //server response of AK-of request-------------------------
        HUCS.receiveHeader();
        String newString = HUCS.receiveString();        
        Log.v("MyActivity","HopperCommunicationInterface createNewArfAndReceiveNewFileInformation, AK server rsp :"+newString);
		
      //server response for requested data-------------------------
		HUCS.receiveHeader();
		String ARFstuff = HUCS.receiveString();
		Log.v("MyActivity","ARFstuff:"+ARFstuff);
		//in morning : receive header then the new id etc....,,,, build function to send bytearray for storage
		return ARFstuff;
	}
	
	public String storeArfBufferInDb_request(String inString,byte[] inByteArray){
	// phase 1--- sending request------JSON--------------
		Log.v("MyActivity","PHASE 1------------------------------------------------------------------");
		HUCS.sendHeader(2000,1000L,2000L,3000L, 4000L,inString.getBytes().length);
		Log.v("MyActivity","storeArfBufferInDb_request SENDING json payload--:"+inString);
		HUCS.sendString_2(inString);
        //server response of AK-of request-------------------------
        HUCS.receiveHeader();
        String newString = HUCS.receiveString();        
        Log.v("MyActivity","HopperCommunicationInterface storeArfBufferInDb_request, AK server rsp :"+newString);
    //phase2--send byte array-------------------------
        Log.v("MyActivity","PHASE 2------------------------------------------------------------------");
		HUCS.sendHeader(3000,1000L,2000L,3000L, 4000L,inByteArray.length);
		Log.v("MyActivity","storeArfBufferInDb_request SENDING byteArray--:");
		HUCS.sendByteArray(inByteArray);
        //server response of AK-of request-------------------------
        HUCS.receiveHeader();
        newString = HUCS.receiveString();        
        Log.v("MyActivity","HopperCommunicationInterface storeArfBufferInDb_request, AK server rsp :"+newString);
    //phase3--rec data back-------------------------	
        Log.v("MyActivity","PHASE 3------------------------------------------------------------------");
        HUCS.receiveHeader();
        newString = HUCS.receiveString();           
		return newString;
	}
	public byte[] retreiveArfBufferFromDb( String inString, String outString ){
// phase 1--- sending request------JSON--------------
		Log.v("MyActivity","PHASE 1------------------------------------------------------------------");
		HUCS.sendHeader(2000,1000L,2000L,3000L, 4000L,inString.getBytes().length);
		Log.v("MyActivity","retreiveArfBufferFromDb SENDING json payload--:"+inString);
		HUCS.sendString_2(inString);
        //server response of AK-of request-------------------------
        HUCS.receiveHeader();
        String newString = HUCS.receiveString();        
        Log.v("MyActivity","retreiveArfBufferFromDb , AK server rsp :"+newString);
//phase2--send byte array-------------------------	
        //server response JSON-------------------------
        HUCS.receiveHeader();
        outString = HUCS.receiveString();        
        Log.v("MyActivity","retreiveArfBufferFromDb , json rsp :"+outString);
        //server response BYTE ARRAY-------------------------
        HUCS.receiveHeader();
        byte[] outByteArray;
        outByteArray = HUCS.receiveByteArray();
        Log.v("MyActivity","retreiveArfBufferFromDb , BYTE ARRAY received, size: :"+outByteArray.length);
        Log.v("MyActivity","retreiveArfBufferFromDb , COMPLETE----------------------------------------:");
		return outByteArray;
	}


	public JSONArray listOfUsers(String inString){			
		JSONArray jSonArray_returnInfo = new JSONArray();
	// phase 1--- sending request------JSON--------------		
		HUCS.sendHeader(2000,1000L,2000L,3000L, 4000L,inString.getBytes().length);
		Log.v("MyActivity","listOfUsers SENDING json payload--:"+inString);
		HUCS.sendString_2(inString);
        //server response of AK-of request-------------------------
        HUCS.receiveHeader();
        String newString = HUCS.receiveString();        
        Log.v("MyActivity","listOfUsers , AK server rsp :"+newString);
   //phase2--send json-------------------------	
        //server response JSON-------------------------
        HUCS.receiveHeader();
        String outString = HUCS.receiveString();
        JSONObject jSon_fromServer = HopperJsonStatic.getJsonObjectFromString(outString);
        jSonArray_returnInfo= HopperJsonStatic.getJSONArray(jSon_fromServer,"arrayOfUsers")	;
		return jSonArray_returnInfo;
	}
	
	public JSONArray listOfRequestByUserId(String inString){			
		JSONArray jSonArray_returnInfo = new JSONArray();
	// phase 1--- sending request------JSON--------------
		Log.v("MyActivity","listOfRequestByUserId PHASE 1--------------");
		HUCS.sendHeader(2000,1000L,2000L,3000L, 4000L,inString.getBytes().length);
		Log.v("MyActivity","listOfUsers SENDING json payload--:"+inString);
		HUCS.sendString_2(inString);
        //server response of AK-of request-------------------------
        HUCS.receiveHeader();
        String newString = HUCS.receiveString();        
        Log.v("MyActivity","listOfUsers , AK server rsp :"+newString);
   //phase2--send byte array-------------------------
        Log.v("MyActivity","listOfRequestByUserId PHASE 2--------------");
        //server response JSON-------------------------
        HUCS.receiveHeader();
        String outString = HUCS.receiveString();
        JSONObject jSon_fromServer = HopperJsonStatic.getJsonObjectFromString(outString);
        jSonArray_returnInfo= HopperJsonStatic.getJSONArray(jSon_fromServer,"arrayOfRequest")	;
        Log.v("MyActivity","listOfRequestByUserId Complete returning jason array");
		return jSonArray_returnInfo;
	}
	
	
	public JSONObject executeSql(String inSqlString, String inTypeOfSql){
		//JSONArray jSonArray_returnInfo = new JSONArray();
		JSONObject json_toServer = new JSONObject();
		HopperJsonStatic.putKeyValueStringsForJsonObject(json_toServer, "command", "executeSql");
		HopperJsonStatic.putKeyValueStringsForJsonObject(json_toServer, "typeOfSql", inTypeOfSql);
		HopperJsonStatic.putKeyValueStringsForJsonObject(json_toServer, "sqlString", inSqlString);		
// phase 1--- sending request------JSON--------------
		Log.v("MyActivity","executeSql PHASE 1--------------");
		HUCS.sendHeader(2000,1000L,2000L,3000L, 4000L,json_toServer.toString().getBytes().length);
		Log.v("MyActivity","executeSql SENDING json payload--:"+inSqlString);
		HUCS.sendString_2(json_toServer.toString());
        //server response of AK-of request-------------------------
        HUCS.receiveHeader();
        String newString = HUCS.receiveString();        
        Log.v("MyActivity","executeSql , AK server rsp :"+newString);
 // phase 2--- receive  requested json------JSON--------------  
        Log.v("MyActivity","executeSql PHASE 2--------------");
        HUCS.receiveHeader();
        String outString = HUCS.receiveString();
        //---here build json to leave here with
        JSONObject jSon_fromServer = HopperJsonStatic.getJsonObjectFromString(outString);        
		return jSon_fromServer;
		

	}
	
	public JSONObject executeQueryCommand(String inQueryCommand, JSONObject inParametersAsJson){
		//JSONArray jSonArray_returnInfo = new JSONArray();
		JSONObject json_toServer = new JSONObject();
		HopperJsonStatic.putKeyValueStringsForJsonObject(json_toServer, "command", "executeQueryCommand");
		HopperJsonStatic.putKeyValueStringsForJsonObject(json_toServer, "queryCommand", inQueryCommand);
		HopperJsonStatic.putKeyValueStringObjectForJsonObject(json_toServer, "parameters", inParametersAsJson);		
// phase 1--- sending request------JSON--------------
		Log.v("MyActivity","executeSql PHASE 1--------------");
		HUCS.sendHeader(2000,1000L,2000L,3000L, 4000L,json_toServer.toString().getBytes().length);
		Log.v("MyActivity","executeSql SENDING json payload--:");
		HUCS.sendString_2(json_toServer.toString());
        //server response of AK-of request-------------------------
        HUCS.receiveHeader();
        String newString = HUCS.receiveString();        
        Log.v("MyActivity","executeSql , AK server rsp :"+newString);
 // phase 2--- receive  requested json------JSON--------------  
        Log.v("MyActivity","executeSql PHASE 2--------------");
        HUCS.receiveHeader();
        String outString = HUCS.receiveString();
        //---here build json to leave here with
        JSONObject jSon_fromServer = HopperJsonStatic.getJsonObjectFromString(outString);        
		return jSon_fromServer;
		

	}
	
	
	
	
	
	
	






	public void sendRequestByteArrayToStorage(){}
	public void sendResposeByteArrayToStorage(){}
	
	public byte[] receiveAudioBufferFromStorage(String inBuffIdAsString){
        JSONObject json_arf_001 = new JSONObject();
        HopperJsonStatic.putKeyValueStringsForJsonObject(json_arf_001,"command"			,"retreiveArfBufferFromDb");
        HopperJsonStatic.putKeyValueStringsForJsonObject(json_arf_001,"buffId"			,inBuffIdAsString);
        String outString="";
        return retreiveArfBufferFromDb( json_arf_001.toString(), outString );	
	}
	
	
	public String receiveString(){
		return HUCS.receiveString();
	}
	public byte[] receiveByteArray(){return null;}
	
	
	
	public int storeNewByteArray(int inArfId, byte[] inByteArray, String inCaption, String inSearchString, String inEntryType){//return the new newArf_BufId        
		HopperCommunicationInterface HCI = HopperCommunicationInterface.get("COMM");
		//------------send the byte array to be saved--------------------------------------------
        JSONObject json_arf_001 = new JSONObject();
        HopperJsonStatic.putKeyValueStringsForJsonObject(json_arf_001,"command"			,"storeArfBufferInDb");
        HopperJsonStatic.putKeyValueStringsForJsonObject(json_arf_001,"streamType"		,"000");
        HopperJsonStatic.putKeyValueStringsForJsonObject(json_arf_001,"sampleRateHz"	,"44100");
        HopperJsonStatic.putKeyValueStringsForJsonObject(json_arf_001,"channelConfig"	,"0001");
        HopperJsonStatic.putKeyValueStringsForJsonObject(json_arf_001,"audioFormat"		,"0002");
        HopperJsonStatic.putKeyValueStringsForJsonObject(json_arf_001,"mode"			,"0003");
        HopperJsonStatic.putKeyValueStringsForJsonObject(json_arf_001,"entryType"		,inEntryType);
        HopperJsonStatic.putKeyValueStringsForJsonObject(json_arf_001,"arfId"			,String.valueOf(inArfId));
        HopperJsonStatic.putKeyValueStringsForJsonObject(json_arf_001,"orderSource"		,"0");
        HopperJsonStatic.putKeyValueStringsForJsonObject(json_arf_001,"label"			,inCaption);
        HopperJsonStatic.putKeyValueStringsForJsonObject(json_arf_001,"searchString"	,inSearchString);       
        String retString001 = HCI.storeArfBufferInDb_request(json_arf_001.toString(),inByteArray);
        JSONObject json_part2 = HopperJsonStatic.getJsonObjectFromString(retString001);
        String new_arf_buff_Id = HopperJsonStatic.getStringFromKeyForJsonObject(json_part2,"newArf_BufId");
        Log.v("MyActivity", "Main Activity, from storeArfBufferInDb_request str:"+retString001);
        return Integer.parseInt(new_arf_buff_Id);    	
    }
	
	public void updateMailStatus (String inDeviceIdAsString,String inBuffIdAsString ,String inMailStatusType){
		HopperDataset dataset = new HopperDataset("COMM");
		String inArfBuffIdAsString = null;
		dataset.executeSql("select id from tb_arf_buff where BuffId = "+inArfBuffIdAsString);
		inArfBuffIdAsString=dataset.getFieldAsString("id", 0);
		
		dataset.executeSql("select id from tb_mailStatusType where label = '"+inMailStatusType+"' ;");
		String mailStatusType_idAsString = dataset.getFieldAsString("id", 0);		
		dataset.executeSql("UPDATE tb_userDeviceList_arf_buff SET mailStatusType_id= "+mailStatusType_idAsString+" WHERE userDeviceList_id = "+inDeviceIdAsString +" and arf_buff_id = "+inArfBuffIdAsString);
	
	}

}
