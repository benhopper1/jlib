package hopper.library.model;

import hopper.cache.ImageCache;
import hopper.library.object.RemoteImageCache;
import hopper.library.object.RequestObject;
import hopper.library.object.ResponseObject;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.example.hopperlibrary.HopperAsync;
import com.example.hopperlibrary.HopperCommunicationInterface;
import com.example.hopperlibrary.HopperDataset;
import com.example.hopperlibrary.HopperJsonStatic;
import com.example.hopperlibrary.console;



public class RequestModel extends RemoteModel{
	protected static HashMap<String, RequestModel> instanceHash = new HashMap<String, RequestModel>();
	private static Context context;
	
	private RequestModel() {
		
	}
	public static RequestModel getMaybeCreate(Context inContext, String inName, boolean inAutoRefreshOnStartup){
		context = inContext;
		RequestModel tmpObject = instanceHash.get(inName);
		if(tmpObject != null){
			return tmpObject;
		}
		tmpObject = new RequestModel();
		instanceHash.put(inName,tmpObject);
		if(inAutoRefreshOnStartup){tmpObject.refresh();}
		return tmpObject;
	}
	
	@Override
	protected void onRefresh(){
		
      
        
        final HopperDataset rqDataset = new HopperDataset("COMM");        
        
        rqDataset.executeSql("call sp_getMail_small( "+String.valueOf(HopperCommunicationInterface.get("COMM").getDeviceId())+",0, 5)");
        
        //test, trying to unlock table----and this worked, without the prceding sp locks everything....
        final HopperDataset ulockDataset = new HopperDataset("COMM");
        ulockDataset.executeSql("select * from tb_buff limit 1");
        
        HashMap<Integer, RequestObject> hashMapOfRqObject = new HashMap<Integer, RequestObject>();
        ArrayList<RequestObject> requestArrayList = new ArrayList<RequestObject>();
	    int recordCount = rqDataset.getRecordCount();
        for(int i = 0; i<recordCount;i++){
        	String rqUrl = rqDataset.getFieldAsString("rq_imageFilePath", i);
        	String rspUrl = rqDataset.getFieldAsString("rsp_imageFilePath", i);
        	
        	if(rqUrl != null || rqUrl !="None" || rqUrl !=""){
        		Log.v("arfComm", "1preLoadBitmap -> rq_imageFilePath:"+rqUrl);
        		ImageCache.addRemoteImageToMemoryCache(rqDataset.getFieldAsInt("rq_imageId", i), "http://www.walnutcracker.net/"+rqUrl);
        	}
        	if(rspUrl != null || rqUrl !="None" || rqUrl !=""){
        		Log.v("arfComm", "2preLoadBitmap -> rq_imageFilePath:"+rqUrl);
        		ImageCache.addRemoteImageToMemoryCache(rqDataset.getFieldAsInt("rsp_imageId", i), "http://www.walnutcracker.net/"+rspUrl);
        	}
        	Log.v("arfComm", "preLoadBitmap -> rq_imageFilePath:");
        }
        Log.v("arfComm", "preLoadBitmap DONE");
        
        //ImageCache.logDumpCache();	
   
        
        Log.v("arfComm", "STARTING SECTION 2");
        for(int m = 0; m<rqDataset.getRecordCount();m++){
        	RequestObject requestObject;
        	requestObject = hashMapOfRqObject.get(rqDataset.getFieldAsInt("rq_arfBuffId", m));        	
        	if(requestObject == null){        		
        		requestObject = RequestObject.getOrMaybeCreate( context,rqDataset.getFieldAsInt("rq_arfBuffId", m));
        		hashMapOfRqObject.put(rqDataset.getFieldAsInt("rq_arfBuffId", m), requestObject);
        		requestArrayList.add(requestObject);
        		Log.v("arfComm", "["+String.valueOf(m)+"] RQ adding, id:"+String.valueOf(rqDataset.getFieldAsString("rq_arfBuffId", m)));
        	} 
        	Log.v("arfComm", "check rq id:"+String.valueOf(requestObject.getId()));
	        
	        requestObject.setCaption(rqDataset.getFieldAsString("rq_caption", m));
	        requestObject.setImageFilePath(rqDataset.getFieldAsString("rq_imageFilePath", m));
	        requestObject.setImageId(rqDataset.getFieldAsInt("rq_imageId", m));
	        requestObject.setEntryTimeStamp(rqDataset.getFieldAsString("rq_entryTimeStamp", m));
	        requestObject.setScreenName(rqDataset.getFieldAsString("rq_screenName", m));
	        requestObject.setAudioFilePath(rqDataset.getFieldAsString("rq_filePath", m));
	        requestObject.setArfBuffId(rqDataset.getFieldAsInt("rq_arfBuffId", m));
	        requestObject.setUserId(rqDataset.getFieldAsInt("rq_userId", m));
	        console.log("SET USER ID:",rqDataset.getFieldAsInt("rq_userId", m));
	        
	        int rsp_arfBuffId = rqDataset.getFieldAsInt("rsp_arfBuffId", m);
	        if(rsp_arfBuffId>0){
	        	ResponseObject responseObject = ResponseObject.getOrMaybeCreate( context, rqDataset.getFieldAsInt("rsp_arfBuffId", m));	        	
	        	responseObject.setEntryTimeStamp(rqDataset.getFieldAsString("rsp_entryTimeStamp",  m));
	        	responseObject.setImageFilePath(rqDataset.getFieldAsString("rsp_imageFilePath",  m));
	        	responseObject.setImageId(rqDataset.getFieldAsInt("rsp_imageId",  m));
	        	responseObject.setCaption(rqDataset.getFieldAsString("rsp_caption", m));
	        	responseObject.setEntryTimeStamp(rqDataset.getFieldAsString("rsp_entryTimeStamp", m));
	        	responseObject.setScreenName(rqDataset.getFieldAsString("rsp_screenName", m));
	        	responseObject.setAudioFilePath(rqDataset.getFieldAsString("rsp_filePath", m));
	        	responseObject.setUserId(rqDataset.getFieldAsInt("rsp_userId", m));
	        	Log.v("arfComm", "setScreenName"+rqDataset.getFieldAsString("rsp_screenName", m));
	        	requestObject.addResponse(responseObject);
	        }        
	                	
        }
        
        data = requestArrayList;		
	}	
		

}
