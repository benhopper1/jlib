package hopper.application.qrreader.v002;

import android.os.Bundle;
import hopper.application.qrreader.v002.StartupProcess.OnExpressLoginFailListener;
import hopper.application.qrreader.v002.StartupProcess.OnExpressLoginSuccessListener;
import hopper.application.qrreader.v002.fragments.StartupFragment;
import hopper.application.qrreader.v002.fragments.StartupFragment.OnExitPressListener;
import hopper.application.qrreader.v002.fragments.StartupFragment.OnScanPressListener;
import hopper.application.qrreader.v002.fragments.UserAddEdditFragment;
import hopper.application.qrreader.v002.fragments.UserAddEdditFragment.OnOkPressedListener;
import hopper.library.communication.v003.ConnectedDevice;
import hopper.library.communication.v003.DataLayer;
import hopper.library.communication.v003.FilterSetProcessor;
import hopper.library.communication.v003.HopperFilter.OnFilterPassListener;
import hopper.library.communication.v003.RoutingLayer;
import hopper.library.communication.v003.TransportLayer;
import hopper.library.communication.v003.WebSocketService;
import hopper.library.communication.v003.WebSocketService.OnOpenedListener;
import hopper.library.communication.v003.WebSocketService.OnTransactionListener;
import hopper.library.file.FileAndFolder;
import hopper.library.fragment.qrcode.QrReader_frag;
import hopper.library.fragment.qrcode.QrReader_frag.OnUpdateListener;
import hopper.library.fragment.stack.FragmentStack;
import hopper.library.http.HttpPostJson;
import hopper.library.http.UploadFile;
import hopper.library.http.Upload_frag;
import hopper.library.http.Upload_frag.OnUploadListener;
import hopper.library.local.store.LocalUserStore;
import hopper.library.phone.CallLog;
import hopper.library.phone.PhoneCallReceiver;
import hopper.library.phone.PhoneCallReceiver.OnAnswerCallListener;
import hopper.library.phone.PhoneCallReceiver.OnAutoAnswerCallListener;
import hopper.library.phone.PhoneCallReceiver.OnEndCallListener;
import hopper.library.phone.PhoneCallReceiver.OnIncomingCallListener;
import hopper.library.phone.PhoneCallReceiver.OnOutgoingCallListener;
import hopper.library.sms.SmsReceiver;
import hopper.library.sms.SmsReceiver.OnIncomingSmsListener;
import hopper.library.sms.SmsSentObserver.OnOutgoingSmsListener;
import hopper.library.web.javascript.WebAppInterface.LoadUploadFragmentApplicationScope;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.hopperlibrary.HopperJsonStatic;
import com.example.hopperlibrary.console;



import android.app.Activity;
import android.util.Pair;


public class MainActivity extends Activity implements LoadUploadFragmentApplicationScope {
	//private WebViewFragment webViewFragment = new WebViewFragment();
	
	
	static public FragmentStack fragmentStack;
	public QrReader_frag qrReader_frag;
	public ConnectedDevice connectedDevice;
	/*public String httpHostIp = "209.140.28.20";
	public String httpHostPort = "30000";
	
	public String WebSocketIp = "209.140.28.20";
	public String WebSocketPort = "30300";
	
	final public String defaultSecureDomain = "http://" + httpHostIp + ":" + httpHostPort;
	final public String defaultDomain = "http://" + httpHostIp + ":" + httpHostPort;
	final public String defaultloginRoute = "/user/mobileLogin";
	
	*/
	
	
	public String httpHostIp = "192.168.0.16";
	public String httpHostPort = "35001";
	
	public String WebSocketIp = "192.168.0.16";
	public String WebSocketPort = "30300";
	
	/*final public String defaultSecureDomain = "http://" + httpHostIp + ":" + httpHostPort;
	final public String defaultDomain = "http://" + httpHostIp + ":" + httpHostPort;
	final public String defaultInformationRoute = "http://" + httpHostIp + ":" + httpHostPort + "/information/default";
	
	final public String projectName = "arfSync";
	final public String configFileName = "arfSync.conf";*/
	
	
	
	
	
	static public Activity activity;
	static public StartupFragment startupFragment = new StartupFragment();
	
	
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	activity = this;
    	if(fragmentStack == null){
    		//fragmentStack = new FragmentStack(this, R.id.fragment1);
    		fragmentStack = FragmentStack.getMaybeCreate("mainFragmentStack", this, R.id.fragment1);
    	}
    	
    	console.log("+++++++++++++++++onCreate+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
    	console.log("Starting qr reeader");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);        
        //MainActivity.this.loadFragment(new Blank_frag());
       // fragmentStack.add(new Blank_frag());
        
        /*PhoneContacts phoneContacts = new PhoneContacts(this);
        phoneContacts.dump();
        *///phoneContacts.getNamesAndNumbersAsArrayListOfPair();
        
        
        
        /*Handset handset = new Handset();
        handset.call(this);*/
        
        
        
        CallLog callLog = new CallLog(this);
        callLog.dump();
        
        final StartupProcess startupProcess = new StartupProcess(this);
        startupProcess.loginExpress(new OnExpressLoginSuccessListener(){
			@Override
			public void onExpressLoginSuccess(Object... objects) {
				console.log("onExpressLoginSuccess call back ENTERED!!");
				//--> W E B S O C K E T   S T A R T U P --
				startupProcess.loginWebsocket();
				
				doMe();
				
				
				//=========================================================================
				//=========================================================
				//==========================================
				startupFragment.setOnScanPressListener(new OnScanPressListener(){
					@Override
					public void onScanPress(Object... objects) {
						qrReader_frag = new QrReader_frag();
				        qrReader_frag.setOnUpdateListener(new OnUpdateListener(){
							@Override
							public void onUpdate(Object... objects) {
								console.log("SCAN  ----- onUpdate");
								fragmentStack.back();
								JSONObject data_json = HopperJsonStatic.getJsonObjectFromString((String)objects[1]);
								String cmd = HopperJsonStatic.getStringFromKeyForJsonObject(data_json, "cmd");
								String cd = HopperJsonStatic.getStringFromKeyForJsonObject(data_json, "cd");		
														
								TransportLayer transactionTransportLayer = TransportLayer.createTransportLayer();
								transactionTransportLayer
										.addToDataLayer("cmd", cmd)
										.addToDataLayer("cd", cd)
										.addToRoutingLayer("type", RoutingLayer.TypesEnum.transactionToServer.toString())
										.addToRoutingLayer("command", "connectWaitingQr")
								;

								console.log("onOpened,,,sending:" + transactionTransportLayer.toString());
								WebSocketService.getInstance("mobileService").sendAsTransaction(transactionTransportLayer, new OnTransactionListener() {
									@Override
									public void onTransaction(Object... objects) {
										console.log("SCAN  ----- done and back");
										//fragmentStack.back();
										//new ConnectedDevice();
										//WHY, this is never reached
										
									}

								});
								
							}        	
				        });
						//MainActivity.this.loadFragment(qrReader_frag);
				        fragmentStack.add(qrReader_frag);
						
					}
				});
				startupFragment.setOnExitPressListener(new OnExitPressListener(){
					@Override
					public void onExitPress(Object... objects) {
						// USER EXITED HERE					
					}
					
				});
				
				//MainActivity.this.loadFragment(startupFragment);
				fragmentStack.add(startupFragment);
				
				
				
				
			}
		},new OnExpressLoginFailListener(){
			@Override
			public void onExpressLoginFail(Object... objects) {
				console.log("onExpressLoginFAIL call back ENTERED!!");	
				UserAddEdditFragment userAddEdditFragment = new UserAddEdditFragment(UserAddEdditFragment.FormType.SHORT_FORM){

					@Override
					public boolean onValidate(String fieldName, String fieldValue) {
						// userNameExistRoute
						//return super.onValidate(fieldName, fieldValue);
						
						if(fieldName == "userName"){
							String addUserDomainRoute = LocalUserStore.getValueAsString("domain") + LocalUserStore.getValueAsString("userNameExistRoute");
							HttpPostJson postJson = new HttpPostJson(addUserDomainRoute);
							JSONObject toService_json = new JSONObject();
							HopperJsonStatic.putKeyValueStringsForJsonObject(toService_json, "userName", fieldValue);
							String postJsonResult = postJson.sendJson(toService_json.toString());
							console.log("VALIDATE return json:" + postJsonResult);
							
							JSONObject fromService_json = HopperJsonStatic.createJsonObjectFromJsonString(postJsonResult);
							console.log(HopperJsonStatic.getStringFromKeyForJsonObject(fromService_json, "hadError"));
							if(HopperJsonStatic.getStringFromKeyForJsonObject(fromService_json, "hadError") == "false"){
								JSONObject result_json = HopperJsonStatic.getJsonObjectFromKey(fromService_json, "result");
								//String isActive = HopperJsonStatic.getStringFromKeyForJsonObject(result_json, "isActive");
								String exist = HopperJsonStatic.getStringFromKeyForJsonObject(result_json, "exist");
								if(Boolean.valueOf(exist)){
									this.highlightField(fieldName, "Already Exist");
									return false;
								}
								
							}else{
								this.highlightField(fieldName, "Unknown Error, Try something else!!");
								return false;
							}
							
						}
						
						return true;
					}
					
				};
				fragmentStack.add(userAddEdditFragment);
				
				userAddEdditFragment.setOnOkPressedListener(new OnOkPressedListener(){
					@Override
					public void onOkPressed(Object... objects) {
						UserAddEdditFragment.FormType formType = (UserAddEdditFragment.FormType) objects[0];
						JSONObject fromForm_json = (JSONObject) objects[1];
						console.line("onOkPressed call back!!");
						console.log("formType:" + formType);
						console.log("formJsonString:" + fromForm_json.toString());					
						
						String addUserDomainRoute = LocalUserStore.getValueAsString("domain") + LocalUserStore.getValueAsString("addUserRoute");
						HttpPostJson postJson = new HttpPostJson(addUserDomainRoute);						
						String postJsonResult = postJson.sendJson(fromForm_json.toString());
						console.log("return json:" + postJsonResult.toString());
						
						
					}
					
				});
				
			}
		});
        
        
        
    }
        
        
        //startupProcess.loginWebsocket();
        
        
        
   /*     
        
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
		String password = LocalUserStore.getValueAsString("password"); 
		String domainLoginRoute = LocalUserStore.getValueAsString("domain") + LocalUserStore.getValueAsString("loginRoute");
		
		console.log("userName:" + userName);
		console.log("password:" + password);
		console.log("domainLoginRoute:" + domainLoginRoute);
		
		if(!(userName.equalsIgnoreCase("")) && !(password.equalsIgnoreCase(""))){
			//have user and pass
			JSONObject toService_json = new JSONObject();
			HopperJsonStatic.putKeyValueStringsForJsonObject(toService_json, "userName", userName);
			HopperJsonStatic.putKeyValueStringsForJsonObject(toService_json, "password", password);
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
	        	String deviceId = HopperJsonStatic.getStringFromKeyForJsonObject(result_json, "useDeviceId");
	        	String userId = HopperJsonStatic.getStringFromKeyForJsonObject(result_json, "id");
	        	
	        	// store local user / pass /dev id
	        	LocalUserStore.putKeyAndValue("deviceId", deviceId);
	        	LocalUserStore.putKeyAndValue("userId", userId);
	        	LocalUserStore.putKeyAndValue("userName", userName);
	        	LocalUserStore.putKeyAndValue("password", password);
	        	LocalUserStore.dump();     	
	        }
	        
			
		}else{
			//NO user or/and pass
			//load webview login thingy
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
			        	
			        	LocalUserStore.putKeyAndValue("deviceId", deviceId);
			        	LocalUserStore.putKeyAndValue("userId", userId);
			        	LocalUserStore.putKeyAndValue("userName", userName);
			        	LocalUserStore.putKeyAndValue("password", password);
			        	LocalUserStore.dump();
			        }else{
			        	//error-----
			        	console.log("GOT ERROR");
			        	
			        }
				}
				
			});
			loadFragment(theWebViewFrag);
			
		}
       
        */
		//LocalUserStore.delete();
        
 //XX       final WebSocketService mobileService = new WebSocketService("mobileService");
 //XX       TransactionSeries transactionSeries = new TransactionSeries();
 //XX       SeriesProcessFactory.addProcessPackagePathByCommand("getAllSmsByNumber", "hopper.application.qrreader.transactionseriesprocess.GetAllSmsByNumber");
 //XX   	SeriesProcessFactory.addProcessPackagePathByCommand("getAllSmsAboveId", "hopper.application.qrreader.transactionseriesprocess.GetAllSmsAboveId");
        
        
        /*Intent intent = new Intent(this, WebViewActivity.class);
        startActivity(intent);*/
        
        
        
        
        //HttpPostJson postJson = new HttpPostJson("http://192.168.0.16:35001/user/mobileLogin");
        /*HttpPostJson postJson = new HttpPostJson("http://192.168.0.16:35001/information/default");
        JSONObject send_json = new JSONObject();
        HopperJsonStatic.putKeyValueStringsForJsonObject(send_json, "userName", "ben");
        HopperJsonStatic.putKeyValueStringsForJsonObject(send_json, "password", "");
        
        
        String postJsonResult = postJson.sendJson(send_json.toString());
        console.log("postJson return:" + postJsonResult);*/
        
        
       /* console.log(String.valueOf("exist:" + LocalUserStore.exist("testProject3", "data3.conf")));
        LocalUserStore.setup(this, "testProject3", "data3.conf");
        LocalUserStore.putKeyAndValue("entryOne", "yesMan!!");
        LocalUserStore.dump();
        console.log(String.valueOf("exist:" + LocalUserStore.exist("testProject3", "data3.conf")));
        console.log(">-:" + LocalUserStore.getValueAsString("entryOne"));*/
        //LocalUserStore.delete();
        
        
        
        
        
        
        
        
        
        /*LocalUserStore.setup(this, "testProject3", "data3.conf");
        LocalUserStore.putKeyAndValue("entryOne", "yesMan!!");
        LocalUserStore.dump();
        console.log(">-:" + LocalUserStore.getValueAsString("entryOne"));*/
        
        
        
    	//SmsReceiver.getSmsOutboxAsArrayListOfJson(this, "2566066202");       
      
        //MUST: adds a lookup to package, replace with directory reader someday....
    	
    	
    	
    	
    	
    	/*Handler handler = new Handler();
    	SmsSentObserver smsSentObserver = new SmsSentObserver(handler, this);*/
    	
    	
    	
    	
        //Contact.setup(this);        
        //ContactInformation contactInformation = new ContactInformation(this);
    	//console.log("XXz:" + SmsReceiver.getAllIdsForPhoneNumberAsJsonArray(this, "12564662496").toString());
       // console.log("XXz2:" + SmsReceiver.getSmsByIdAsJson(this, "241").toString());
        
        //have to setup or all will fail!!!!!!!-------------------------------------------------------------------------
//XX   	SmsReceiver.setup(this);
        
        
        
        
    @Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
    	fragmentStack.back();
		//super.onBackPressed();
	}
    
    public void back(){
    	fragmentStack.back();
    }


	private void doMe(){
        
        SmsReceiver.setOnIncomingSmsListener(new OnIncomingSmsListener(){
        	/*
        	 * (non-Javadoc)
        	 * @see hopper.library.sms.SmsReceiver.OnIncomingSmsListener#onIncomingSms(java.lang.Object[])
        	 * @param0	String	phone
        	 * @param0	String	name
        	 * @param0	String	msg
        	 */
			@Override
			public void onIncomingSms(Object... objects) {
				console.log("onIncomingSms call back");
				console.log("phone:" + ((String)objects[0]).replace("+", ""));
				console.log("name:" + objects[1]);
				console.log("msg:"  + objects[2]);			
					
				WebSocketService.getInstance("mobileService").sendCommand(ConnectedDevice.primaryDeviceTokenId, "notification", DataLayer.dataLayer()
						.add("action", "incomingSms")
						.add("phoneNumber", ((String)objects[0]).replace("+", ""))
						.add("name", (String)objects[1])
						.add("msg", (String) objects[2])
				);
			}
        	
        });
        
        SmsReceiver.setOnOutgoingSmsListener(new OnOutgoingSmsListener(){
			@Override
			public void onOutgoingSms(Object... objects) {
				console.log("OnOutgoingSmsListener call back");
				console.log("phone:" + ((String)objects[0]).replace("+", ""));
				console.log("name:" + objects[1]);
				console.log("msg:"  + objects[2]);			
					
				WebSocketService.getInstance("mobileService").sendCommand(ConnectedDevice.primaryDeviceTokenId, "notification", DataLayer.dataLayer()
						.add("action", "outgoingSms")
						.add("phoneNumber", ((String)objects[0]).replace("+", ""))
						.add("name", (String)objects[1])
						.add("msg", (String) objects[2])
				);
				
			}});
        
        
        
        
        
        
        
        
        
        
        
        
        /*Email email = new Email(this);
        email.send("hopperdevelopment@gmail.com", "any subject44", "hell, my first email message!!!babby");
        */
        //ImageCache b; v2 brian
        
        
       /* String urlString = "http://192.168.0.16/phone.png";
        Bitmap bm = ImageCache.getBitmapFromUrl(1, urlString);
            
        
        console.log("loading bmp");
        ImageView iv_1 = (ImageView) findViewById(R.id.iv_1);	        
        iv_1.setImageBitmap(bm);*/
	        
        
       // contactInformation.addMember("My Voice Mail", "12564662496", "hopperdevelopment@gmail.com", "Hopper Development", "CEO", bm);
        
        
       /* String lookedUpId = contactInformation.getContactIdByPhoneNumber("12566066202");
        console.log("lookedUpId" + lookedUpId);
        if(bm != null){
        	console.log("bm not null");
        	contactInformation.updateContactImage(lookedUpId, bm);
        }else{
        	console.log("bm null");
        }*/
        
       /* 
        
        JSONArray resultContactsUpload_jsonArray = contactInformation.sendImageFileSToTempFolderOnServerByIds("http://192.168.0.16:35001/uploadAsTemp");
        console.log("---------resultContactsUpload_jsonArray-------------------------------------------------------------");
        console.log(resultContactsUpload_jsonArray.toString());
       
        
        */
        
        
        
       
        //test sms out
        // SmsReceiver.sendSmsText(this, "12564662496","n use a real keyboard.  xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx-zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz-aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        
       // new ContactInformation(this).addMember("BigBen91", "12564662497", "hopperdevelopment@gmail.com", "The Company", "Cheif Finacial Officer");
        
        /*while(true){
        	contactInformation.deleteContact("12564662497", "BigBen91");
        	if(false){break;}
        }	*/
        
        
        
        
        /*console.log("chk_1");
        //hashkey:id first:phone second name
        ContactInformation contactInformation = new ContactInformation(this);
        console.log("chk_2");
        HashMap<String,Pair<String,String>> contactsHashOfPair = contactInformation.getContactsPairById();
        contactInformation.dump();
        console.log("chk_3");
        
        Cursor cursor = getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);
        cursor.moveToFirst();
        console.log("chk_4");
        
        do{
           String msgData = "";
           for(int idx=0;idx<cursor.getColumnCount();idx++){
               msgData += " " + cursor.getColumnName(idx) + ":" + cursor.getString(idx);
           }
           
           
           String phoneFromSms = cursor.getString(2);
           console.log("->NAME:" + getContactName(getApplicationContext(), phoneFromSms));
           
           
           String personId = cursor.getString(3);
           console.log("chk_5");
           Pair<String,String> pair = contactsHashOfPair.get(personId);
           String phoneNumber = "na";
           String name = "na";
           if(pair != null){
        	   phoneNumber = pair.first;
        	   name = pair.second;
           }          
           console.log("chk_6");
           
           console.log("chk_7");
           
           console.log("--------msg-------------------");
           console.log("phone:" + phoneNumber);
           console.log("name:" + name);
           
           console.log(msgData);
        }while(cursor.resultString photoresultString photomoveToNext());
        
        */
        
        
       
        

        
        /*ContactInformation contactInformation = new ContactInformation(this);
        contactInformation.dump();*/
       /* 
        DownloadFilesTask downloadFilesTask = new DownloadFilesTask();
        downloadFilesTask.execute("http://192.168.0.16:35001/welcome", Environment.getExternalStorageDirectory().getPath()+ "/test.htm");
        */
        
        
        
        MainActivityCommunicationHandling MainActivityCommunicationHandling = new MainActivityCommunicationHandling(this);
                    
        
        
        
        PhoneCallReceiver.setOnIncomingCallListener(new OnIncomingCallListener(){
			@Override
			public void onIncomingCall(Object... objects) {
				console.log("setOnIncomingCallListener callback!!!");
				console.log("PhoneNumber:" +(String)objects[0] );
						
					
				WebSocketService.getInstance("mobileService").sendCommand(ConnectedDevice.primaryDeviceTokenId, "notification", DataLayer.dataLayer()
						.add("action", "incomingPhoneCall")
						.add("phoneNumber", ((String)objects[0]).replace("+", ""))
						.add("name", "not implememted yet")						
				);
				
				
			}
		});
        
        
        PhoneCallReceiver.setOnOutgoingCallListener(new OnOutgoingCallListener(){
 			@Override
 			public void onOutgoingCall(Object... objects) {
 				console.log("setOnOutgoingCallListener callback!!!");
 				console.log("PhoneNumber:" +(String)objects[0] );				
 			}		
 		});
        PhoneCallReceiver.setOnEndCallListener(new OnEndCallListener(){
 			@Override
 			public void onEndCall(Object... objects) {
 				console.log("setOnEndCallListener callback!!!");
 				//console.log("PhoneNumber:" +(String)objects[0] );				
 			}
 		});
        PhoneCallReceiver.setOnAutoAnswerCallListener(new OnAutoAnswerCallListener(){
 			@Override
 			public void onAutoAnswerCall(Object... objects) {
 				console.log("setOnAutoAnswerCallListener callback!!!");
 				console.log("PhoneNumber:" +(String)objects[0] );				
 			}
 		});
        PhoneCallReceiver.setOnAnswerCallListener(new OnAnswerCallListener(){ 			
			@Override
			public void onAnswerCall(Object... objects) {
				console.log("setOnAnswerCallListener callback!!!");
 				console.log("PhoneNumber:" +(String)objects[0] );
				
			}
 		});
        
        
        
        
        
        
        /*Button bt_dataTest = (Button) findViewById(R.id.bt_dataTest);
        
        bt_dataTest.setOnClickListener(new OnClickListener(){			
			@Override
			public void onClick(View arg0) {
				console.log("WebViewFragment load click..");
				MainActivity.this.loadWebViewFragment();
				//SyncContactModel syncContactModel = new SyncContactModel(MainActivity.this);
				//syncContactModel.syncContactToServer();
				
				console.log("ConnectedDevice.primaryDeviceTokenIdXX:" + ConnectedDevice.primaryDeviceTokenId);
				
				OutgoingTransactionRequest outgoingTransactionRequest = new OutgoingTransactionRequest();
				outgoingTransactionRequest.setTargetTokenId(ConnectedDevice.primaryDeviceTokenId);
				outgoingTransactionRequest.setCommand("test1");
				outgoingTransactionRequest.dataLayer()
					.add("dataKey0", "dataValue0")
					.add("dataKey1", "dataValue1")
				;
				WebSocketService.getInstance("mobileService").sendTransactionToDeviceToken(outgoingTransactionRequest, new OnTransactionListener(){
					*//**
					 * @param objects[0] WebSocketService instance
					 * @param objects[1] String containing all transmitted message data 
					 * @param objects[2] TransportLayer that encapsulates all incoming data
					 * @see hopper.library.communication.v003.WebSocke)tService.OnTransactionListener#onTransaction(java.lang.Object[])					 * 
					 *//*
					@Override
					public void onTransaction(Object... objects) {
						console.log("outgoingTransactionRequest onTransaction callback ");
						console.log("returned data:" + ((TransportLayer) objects[2]).dataLayer.toString());
						
					}
				});
				
			}
        });	
        */
        
        
 
        
        
        /*Button bt_doScan = (Button) findViewById(R.id.bt_doScan);
        bt_doScan.setOnClickListener(new OnClickListener(){			
			@Override
			public void onClick(View arg0) {
				
				qrReader_frag = new QrReader_frag();
		        qrReader_frag.setOnUpdateListener(new OnUpdateListener(){
					@Override
					public void onUpdate(Object... objects) {						
						JSONObject data_json = HopperJsonStatic.getJsonObjectFromString((String)objects[1]);
						String cmd = HopperJsonStatic.getStringFromKeyForJsonObject(data_json, "cmd");
						String cd = HopperJsonStatic.getStringFromKeyForJsonObject(data_json, "cd");		
												
						TransportLayer transactionTransportLayer = TransportLayer.createTransportLayer();
						transactionTransportLayer
								.addToDataLayer("cmd", cmd)
								.addToDataLayer("cd", cd)
								.addToRoutingLayer("type", RoutingLayer.TypesEnum.transactionToServer.toString())
								.addToRoutingLayer("command", "connectWaitingQr")
						;

						console.log("onOpened,,,sending:" + transactionTransportLayer.toString());
						WebSocketService.getInstance("onUpdate of scan----------").sendAsTransaction(transactionTransportLayer, new OnTransactionListener() {
							@Override
							public void onTransaction(Object... objects) {
								console.log("Transaction type done and back");
								//new ConnectedDevice();
								fragmentStack.back();
							}

						});						
					}        	
		        });
				//MainActivity.this.loadFragment(qrReader_frag);
		        fragmentStack.add(qrReader_frag);
			}
		});
        */
        
  /*      
        
      //######################################################################################################################
      //------------W e b S o c k e t   S e t u p   a n d   Y o u  r   L O G I N ---------------------------------------------
      //######################################################################################################################
      		TransportLayer.setup("1", "1787", "$token_09");
      		TransportLayer setupTransportLayer = TransportLayer.createTransportLayer();
      		setupTransportLayer.routingLayer().type(
      				RoutingLayer.TypesEnum.setupToServer);

      		setupTransportLayer.dataLayer()
      				.add("userName", "mark")
      				.add("passimport hopper.application.qrreader.MainActivity;word", "")
      				.add("deviceNumber", "fakeingDevNum22")
      				.add("userAgent", "fakeingUserAgent22")
      				.add("deviceType", "androidApp");

      		setupTransportLayer.transportLayerFields()
      				//.deviceId("3000")
      				.add("test2key", "test2value");
      		
      		WebSocketService.getInstance("mobileService").connect(MainActivity.this.WebSocketIp, Integer.parseInt(MainActivity.this.WebSocketPort), "na", setupTransportLayer.toJson());
      		*/
      		
      	//######################################################################################################################
      	//------------Event When After Login and  ---------------------------------------------
      	//######################################################################################################################
        	WebSocketService.getInstance("mobileService").setOnOpenedListener(new OnOpenedListener() {
      				@Override
      				public void onOpened(Object... objects){
      					console.log("onOpened!!!");
      					new ConnectedDevice();
      					
      				    FilterSetProcessor.getMaybeCreate("filterProcessor_0", WebSocketService.getInstance("mobileService"))      						
      						.add("global_filter", "filter", "widget_appFileSystem", new OnFilterPassListener(){
      							@Override
      							public void onFilterPass(Object... objects) {
      								console.log("------>onFilterPass GLOBAL widget_appFileSystem" );						
      								
      								final TransportLayer transFromServer = TransportLayer.createTransportLayer((String) objects[1]);
      								String action = HopperJsonStatic.getStringFromKeyForJsonObject(transFromServer.getDataLayerAsDataLayer().toJson(),"action");
      								
      								
      								if(action.equalsIgnoreCase("getFolderContents")){
      									String lookingPath = HopperJsonStatic.getStringFromKeyForJsonObject(transFromServer.getDataLayerAsDataLayer().toJson(),"path");
      									console.log("-----------getFolderContents--------------------------------path:---" + lookingPath);
      									JSONArray folderContents_jsonArray = FileAndFolder.getContentsAsJsonArray(lookingPath);
      									JSONObject response_json = new JSONObject();
      									HopperJsonStatic.putArrayIntoObjectWithKey(response_json, folderContents_jsonArray, "contents");
      									HopperJsonStatic.putKeyValueStringsForJsonObject(response_json, "rqPath", lookingPath);
      									
      									WebSocketService.getInstance("mobileService").sendCommand(ConnectedDevice.primaryDeviceTokenId, "widget_appFileSystem", DataLayer.dataLayer()
      											.add("action", "responseFolderContents")
      											.add("data", response_json)
      									);
      								}
      								
      								
      								
      								
      								if(action.equalsIgnoreCase("getFile")){
      									String absPathToFile = HopperJsonStatic.getStringFromKeyForJsonObject(transFromServer.getDataLayerAsDataLayer().toJson(),"absPath");
      									console.log("-----------getFile--------------------------------path:---" + absPathToFile);
      									UploadFile uploadFile = new UploadFile("http://192.168.0.16:35001/uploadAsTemp",absPathToFile);
      									Pair<String,String> pair = new Pair("testKey55","testValue55");
      									
      									String resultString = uploadFile.upload(pair);
      									console.log("resultString:" + resultString);
      									
      									WebSocketService.getInstance("mobileService").sendCommand(ConnectedDevice.primaryDeviceTokenId, "widget_appFileSystem", DataLayer.dataLayer()
      											.add("action", "responseFile")
      											.add("data", HopperJsonStatic.createJsonObjectFromJsonString(resultString))
      									);   									
      									
      								}
      							}
      						})			
      						.processorStart();
      					;
      				}
      			});
        
        
        
    }// do ME
    
    
    
	//public void loadFragment(Fragment inFragmentInstance){
		//fragmentStack.add(inFragmentInstance);
		
		/*android.app.FragmentManager fm = getFragmentManager();
		FragmentTransaction fragmentTransaction = fm.beginTransaction();		
		fragmentTransaction.replace(R.id.fragment1, inFragmentInstance);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();*/
	//}
	
	
	public void fragmentShowTop(){
		console.log("Main fragmentShowTop()");
		fragmentStack.showTop();
		console.log("Main fragmentShowTop() DONE");
		
	}
	public void loadWebViewFragment(){
		//loadFragment(webViewFragment);
		hopper.library.web.WebViewFragment theWebViewFrag = new hopper.library.web.WebViewFragment("http://192.168.0.16:35001/user/widget_userForm");
		//loadFragment(theWebViewFrag);
		fragmentStack.add(theWebViewFrag);
	}
	/*public void popFragmentBackstack(){
		android.app.FragmentManager fm = getFragmentManager();
		if (fm.getBackStackEntryCount() > 0){
			fm.popBackStack();
	    }
	}*/
	
	public void loadUploadFragment(OnUploadListener inOnUploadListener){
		
		//loadFragment(false, R.id.blank_frag_2 ,new Upload_frag("http://" + ((MainActivity)attachedActivity).httpHostIp + ":" + ((MainActivity)attachedActivity).httpHostPort + "/upload", "imageStore", "normalUserImage","Select Image", "image/*", new OnUploadListener(){		
		//String inUploadServiceUrl, String inCommand, String inTheme, String inCaption, String inFileFilter, OnUploadListener inListener
		Upload_frag uploadFragment = new Upload_frag("http://192.168.0.16:35001/upload", "imageStore", "normalUserImage","Select Image", "image/*", inOnUploadListener);
		//loadFragment(uploadFragment);
		//fragmentStack.add(uploadFragment);
		fragmentStack.showThis(uploadFragment);
	}
	
/*	public void loadFromInformationService(){		
		HttpPostJson postJson = new HttpPostJson(defaultInformationRoute);
		String postJsonResult = postJson.sendJson("{}");
		JSONObject fromService_json = HopperJsonStatic.createJsonObjectFromJsonString(postJsonResult);
		String error = HopperJsonStatic.getStringFromKeyForJsonObject(fromService_json, "hadError");
		console.log("error:" + error);
		console.log("fromService_json:" + fromService_json.toString());
        JSONObject send_json = new JSONObject();
        HopperJsonStatic.putKeyValueStringsForJsonObject(send_json, "userName", "ben");
        HopperJsonStatic.putKeyValueStringsForJsonObject(send_json, "password", "");        
        if(error.equalsIgnoreCase("false")){
        	console.log("error<-> false");
        	JSONObject result_json = HopperJsonStatic.getJsonObjectFromKey(fromService_json, "result"); 
        	LocalUserStore.setup(this, projectName, configFileName);            
            for(String theKey : HopperJsonStatic.getKeysFromJsonObject(result_json)){
            	console.log(theKey + " " + HopperJsonStatic.getStringFromKeyForJsonObject(result_json, theKey));
            	LocalUserStore.putKeyAndValue(theKey, HopperJsonStatic.getStringFromKeyForJsonObject(result_json, theKey));
            }
            console.log("LocalUserStore:");
            LocalUserStore.dump();
            
            
        }else{
        	console.log("error<-> true");
        }
		
		
		
        
	}*/
	
	
	
	
	
	/*public void updateWsTmpFileInfo(){
		ContactInformation contactInformation = new ContactInformation(this);
		JSONObject result_json = contactInformation.sendImageFileToTempFolderOnServer("http://192.168.0.16:35001/uploadAsTemp","12566066202");
		console.log(result_json.toString());
	}*/
    
  
  
}
