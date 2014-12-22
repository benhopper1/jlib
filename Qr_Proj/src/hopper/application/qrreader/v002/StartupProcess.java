package hopper.application.qrreader.v002;

import java.util.ArrayList;

import hopper.library.communication.v003.RoutingLayer;
import hopper.library.communication.v003.TransportLayer;
import hopper.library.communication.v003.WebSocketService;
import hopper.library.communication.v003.transactionseries.SeriesProcessFactory;
import hopper.library.communication.v003.transactionseries.TransactionSeries;
import hopper.library.fragment.stack.FragmentStack;
import hopper.library.http.HttpPostJson;
import hopper.library.local.store.LocalUserStore;
import hopper.library.sms.SmsReceiver;
import hopper.library.web.WebViewFragment.OnCompleteListener;

import org.json.JSONObject;

import com.example.hopperlibrary.HopperJsonStatic;
import com.example.hopperlibrary.console;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
public class StartupProcess {
	public String httpHostIp = "192.168.0.16";
	public String httpHostPort = "35001";
	
	public String WebSocketIp = "192.168.0.16";
	public String WebSocketPort = "30300";
	
	final public String defaultSecureDomain = "http://" + httpHostIp + ":" + httpHostPort;
	final public String defaultDomain = "http://" + httpHostIp + ":" + httpHostPort;
	final public String defaultInformationRoute = "http://" + httpHostIp + ":" + httpHostPort + "/information/default";
	
	final public String projectName = "arfSync";
	final public String configFileName = "arfSync.conf";
	

	public Activity activity;
	
	public StartupProcess(Activity inActivity){
		activity = inActivity;
	}
	


//=========================================================================================================================
//	E X P R E S S 
//=========================================================================================================================
	
	
	//======== E V E N T ==================================================================================================================================
	//-------event onExpressLoginSuccessListenerArrayList---------------------------------------------------------------------------------------------------------
	private ArrayList<OnExpressLoginSuccessListener> onExpressLoginSuccessListenerArrayList = new ArrayList<OnExpressLoginSuccessListener>();
	
	public interface OnExpressLoginSuccessListener{
	    public void onExpressLoginSuccess(Object...objects);
	}
	public void setOnExpressLoginSuccessListener(OnExpressLoginSuccessListener listener) {
		onExpressLoginSuccessListenerArrayList.add(listener);
	}
	 /*
	  * @param0 String xxxxxxx
	  * @param1	Intent xxxxxxx
	  */
	public void reportOnExpressLoginSuccess(Object...objects){
		for(OnExpressLoginSuccessListener listener : onExpressLoginSuccessListenerArrayList){
			listener.onExpressLoginSuccess(objects);
	    }
	}
	//----------------------------------------------------------------------------------------------------------------------------------- 
	

	
	//======== E V E N T ==================================================================================================================================
	//-------event onExpressLoginFailListenerArrayList---------------------------------------------------------------------------------------------------------
	private ArrayList<OnExpressLoginFailListener> onExpressLoginFailListenerArrayList = new ArrayList<OnExpressLoginFailListener>();
	
	public interface OnExpressLoginFailListener{
	    public void onExpressLoginFail(Object...objects);
	}
	public void setOnExpressLoginFailListener(OnExpressLoginFailListener listener) {
		onExpressLoginFailListenerArrayList.add(listener);
	}
	 /*
	  * @param0 String 	xxxxxxx
	  * @param1	Intent  xxxxxxx
	  */
	public void reportOnExpressLoginFail(Object...objects){
		for(OnExpressLoginFailListener listener : onExpressLoginFailListenerArrayList){
			listener.onExpressLoginFail(objects);
	    }
	}
	//----------------------------------------------------------------------------------------------------------------------------------- 	
	
	
	public void loginExpress(OnExpressLoginSuccessListener inOnExpressLoginSuccessListener){
		this.setOnExpressLoginSuccessListener(inOnExpressLoginSuccessListener);
		this.loginExpress();
	}
	
	public void loginExpress(OnExpressLoginSuccessListener inOnExpressLoginSuccessListener,OnExpressLoginFailListener inOnExpressLoginFailListener ){
		this.setOnExpressLoginSuccessListener(inOnExpressLoginSuccessListener);
		this.setOnExpressLoginFailListener(inOnExpressLoginFailListener);
		this.loginExpress();
	}
	
	
	public void loginExpress(){
		
        //===========================================================
        //	First Time Loaded ?
        //===========================================================
        if(!(LocalUserStore.exist("arfSync", "arfSync.conf"))){
        	//--> Never Loaded Before
        	loadFromInformationService();
        }
        
        //===========================================================
        //	Have UserName and Password ?
        //===========================================================  
        String userName = LocalUserStore.getValueAsString("userName"); //"ben";//
		String password = LocalUserStore.getValueAsString("password"); //"";//
		String deviceId = LocalUserStore.getValueAsString("deviceId");
		String domainLoginRoute = LocalUserStore.getValueAsString("domain") + LocalUserStore.getValueAsString("loginRoute");
		
		console.log("userName:" + userName);
		console.log("password:" + password);
		console.log("domainLoginRoute:" + domainLoginRoute);
		
		if(!(userName.equalsIgnoreCase("")) && !(password.equalsIgnoreCase(""))){
			//have user and pass
			JSONObject toService_json = new JSONObject();
			HopperJsonStatic.putKeyValueStringsForJsonObject(toService_json, "userName", userName);
			HopperJsonStatic.putKeyValueStringsForJsonObject(toService_json, "password", password);
			HopperJsonStatic.putKeyValueStringsForJsonObject(toService_json, "deviceId", deviceId);
			HttpPostJson postJson = new HttpPostJson(domainLoginRoute);
			
			String postJsonResult = postJson.sendJson(toService_json.toString());
			console.log("return json:" + postJsonResult.toString());
			
			JSONObject fromService_json = HopperJsonStatic.createJsonObjectFromJsonString(postJsonResult);
			String error = HopperJsonStatic.getStringFromKeyForJsonObject(fromService_json, "hadError");
			console.log("error:" + error);
			console.log("fromService_json:" + fromService_json.toString());	          
	        if(error.equalsIgnoreCase("false")){
	        	// good login -----
	        	console.log("error<-> false");
	        	JSONObject result_json = HopperJsonStatic.getJsonObjectFromKey(fromService_json, "result");
	        	deviceId = HopperJsonStatic.getStringFromKeyForJsonObject(result_json, "useDeviceId");
	        	String userId = HopperJsonStatic.getStringFromKeyForJsonObject(result_json, "id");
	        	String userImageUrl = HopperJsonStatic.getStringFromKeyForJsonObject(result_json, "screenImage");
	        	
	        	// store local user / pass /dev id
	        	LocalUserStore.putKeyAndValue("deviceId", deviceId);
	        	LocalUserStore.putKeyAndValue("userId", userId);
	        	LocalUserStore.putKeyAndValue("userName", userName);
	        	LocalUserStore.putKeyAndValue("password", password);
	        	LocalUserStore.putKeyAndValue("userImageUrl", userImageUrl);
	        	LocalUserStore.dump();
	        	//event cause SUCCESS
	        	reportOnExpressLoginSuccess();
	        }else{
	        	//event cause FAIL
	        	reportOnExpressLoginFail();
	        }
	        
			
		}else{
			//NO user or/and pass
			//load webview login thingy
			console.log("stating webview frag------");
			hopper.library.web.WebViewFragment theWebViewFrag = new hopper.library.web.WebViewFragment(LocalUserStore.getValueAsString("domain") + LocalUserStore.getValueAsString("loginRoute"));
			theWebViewFrag.setOnCompleteListener(new OnCompleteListener(){
				@Override
				public void onComplete(Object... objects) {
					console.log("login complete in callback!!!:" + ((String)objects[0]));
					JSONObject fromService_json = HopperJsonStatic.createJsonObjectFromJsonString(((String)objects[0]));
					String error = HopperJsonStatic.getStringFromKeyForJsonObject(fromService_json, "hadError");
								        
			        if(error.equalsIgnoreCase("false")){
			        	//NO ERROR --
			        	console.log("NO ERROR");
						JSONObject result_json = HopperJsonStatic.getJsonObjectFromKey(fromService_json, "result");
			        	String deviceId = HopperJsonStatic.getStringFromKeyForJsonObject(result_json, "useDeviceId");
			        	String userId = HopperJsonStatic.getStringFromKeyForJsonObject(result_json, "id");
			        	String userName = HopperJsonStatic.getStringFromKeyForJsonObject(result_json, "userName"); 
			        	String password = HopperJsonStatic.getStringFromKeyForJsonObject(result_json, "password");
			        	String userImageUrl = HopperJsonStatic.getStringFromKeyForJsonObject(result_json, "screenImage");
			        	
			        	//must not trust client, use post direct from android check---
			        	HttpPostJson postJson = new HttpPostJson(LocalUserStore.getValueAsString("domain") + LocalUserStore.getValueAsString("loginRoute"));						
						String postJsonResult = postJson.sendJson(result_json.toString());
						JSONObject fromService_json2 = HopperJsonStatic.createJsonObjectFromJsonString(postJsonResult);
						String error2 = HopperJsonStatic.getStringFromKeyForJsonObject(fromService_json2, "hadError");
						console.log("error:" + error2);
						console.log("fromService_json2:" + fromService_json2.toString());	          
				        if(error2.equalsIgnoreCase("false")){
				        	LocalUserStore.putKeyAndValue("deviceId", deviceId);
				        	LocalUserStore.putKeyAndValue("userId", userId);
				        	LocalUserStore.putKeyAndValue("userName", userName);
				        	LocalUserStore.putKeyAndValue("password", password);
				        	LocalUserStore.putKeyAndValue("userImageUrl", userImageUrl);
				        	LocalUserStore.dump();
				        	//event cause SUCCESS
				        	reportOnExpressLoginSuccess();
				        }else{
				        	//client faked returned data, potential hacker
				        	console.loge("Hacker Potroctection HERE----");
				        }
						
						
			        	
			        	
			        }else{
			        	//error-----
			        	console.log("GOT ERROR");
			        	//event cause FAIL
			        	reportOnExpressLoginFail();
			        }
				}
				
			});
			loadFragment(theWebViewFrag);
			
		}
	}
	
	public void loadFromInformationService(){		
		HttpPostJson postJson = new HttpPostJson(defaultInformationRoute);
		String postJsonResult = postJson.sendJson("{}");
		JSONObject fromService_json = HopperJsonStatic.createJsonObjectFromJsonString(postJsonResult);
		String error = HopperJsonStatic.getStringFromKeyForJsonObject(fromService_json, "hadError");
		console.log("error:" + error);
		console.log("fromService_json:" + fromService_json.toString());
        /*JSONObject send_json = new JSONObject();
        HopperJsonStatic.putKeyValueStringsForJsonObject(send_json, "userName", "ben");
        HopperJsonStatic.putKeyValueStringsForJsonObject(send_json, "password", "");   */     
        if(error.equalsIgnoreCase("false")){
        	console.log("error<-> false");
        	JSONObject result_json = HopperJsonStatic.getJsonObjectFromKey(fromService_json, "result"); 
        	LocalUserStore.setup(activity, projectName, configFileName);            
            for(String theKey : HopperJsonStatic.getKeysFromJsonObject(result_json)){
            	console.log(theKey + " " + HopperJsonStatic.getStringFromKeyForJsonObject(result_json, theKey));
            	LocalUserStore.putKeyAndValue(theKey, HopperJsonStatic.getStringFromKeyForJsonObject(result_json, theKey));
            }
            console.log("LocalUserStore:");
            LocalUserStore.dump();
            
            
        }else{
        	console.log("error<-> true");
        }
		
		
		
        
	}
	
	public void loadFragment(Fragment inFragmentInstance){	
		/*android.app.FragmentManager fm = activity.getFragmentManager();
		FragmentTransaction fragmentTransaction = fm.beginTransaction();		
		fragmentTransaction.replace(R.id.fragment1, inFragmentInstance);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();*/
		FragmentStack fragmentStack = FragmentStack.getInstance("mainFragmentStack");
		if(fragmentStack == null){
			console.log("mainFragmentStack == NULL");
		}else{
			console.log("mainFragmentStack != NULL");
		}
		fragmentStack.add(inFragmentInstance);
	}
	
	
	
	
//=========================================================================================================================
//	W E B   S O C K E T
//=========================================================================================================================
	
	
	
	
	
	
	
	
	public void loginWebsocket(){
		
        if(!(LocalUserStore.exist("arfSync", "arfSync.conf"))){
        	//--> no config file, get out of here!!
        	console.log("StartupProcess.loginWebsocket no config file, get out of here!!");
        	return;
        }
        
        //---init stuff ------
        
        final WebSocketService mobileService = new WebSocketService("mobileService");
        TransactionSeries transactionSeries = new TransactionSeries();
        SeriesProcessFactory.addProcessPackagePathByCommand("getAllSmsByNumber", "hopper.application.qrreader.v002.transactionseriesprocess.GetAllSmsByNumber");
    	SeriesProcessFactory.addProcessPackagePathByCommand("getAllSmsAboveId", "hopper.application.qrreader.v002.transactionseriesprocess.GetAllSmsAboveId");
    	SeriesProcessFactory.addProcessPackagePathByCommand("getAllPhoneContacts", "hopper.application.qrreader.v002.transactionseriesprocess.GetAllPhoneContacts", activity);
    	SeriesProcessFactory.addProcessPackagePathByCommand("getAllPhoneLogs", "hopper.application.qrreader.v002.transactionseriesprocess.GetAllPhoneLogs", activity);    	
    	//have to setup or all will fail!!!!!!!-------------------------------------------------------------------------
    	SmsReceiver.setup(activity);
        
		
        String userName = LocalUserStore.getValueAsString("userName"); //"ben";//
		String password = LocalUserStore.getValueAsString("password"); //"";//
		String deviceId = LocalUserStore.getValueAsString("deviceId");
		String userId = LocalUserStore.getValueAsString("userId");		
		String security = "notImplementedYet";
		String websockIp = LocalUserStore.getValueAsString("websockIp");
		String websockPort = LocalUserStore.getValueAsString("websockPort");
		
		
		console.log("-----------W e b S o c k e t   S e t u p   a n d   Y o u  r   L O G I N -------------------------");
		console.log("" + userName);
		console.log("" + password);
		console.log("" + deviceId);
		console.log("" + userId);
		console.log("" + security);
		console.log("" + websockIp);
		console.log("" + websockPort);
		
		
		
		
	      //######################################################################################################################
	      //------------W e b S o c k e t   S e t u p   a n d   Y o u  r   L O G I N ---------------------------------------------
	      //######################################################################################################################
	      		TransportLayer.setup(userId, deviceId, security);
	      		TransportLayer setupTransportLayer = TransportLayer.createTransportLayer();
	      		setupTransportLayer.routingLayer().type(
	      				RoutingLayer.TypesEnum.setupToServer);

	      		setupTransportLayer.dataLayer()
	      				.add("userName", userName)
	      				.add("password", password)
	      				.add("deviceNumber", "deviceNumberNotImplemntedYet")
	      				.add("userAgent", "userAgentNotImplemntedYet")
	      				.add("deviceType", "androidApp");

	      		setupTransportLayer.transportLayerFields()
	      				//.deviceId("3000")
	      				.add("test2key", "test2value");
	      		
	      		WebSocketService.getInstance("mobileService").connect(websockIp, Integer.parseInt(websockPort), "na", setupTransportLayer.toJson());
	      		
	}
	
	
	
	
	
}
