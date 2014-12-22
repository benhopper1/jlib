package hopper.cache;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.Buffer;
import java.util.Map;

import com.example.hopperlibrary.HopperAsync;
import com.example.hopperlibrary.HopperAsyncForObject;
import com.example.hopperlibrary.console;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.DropBoxManager.Entry;

import android.util.Log;
import android.util.LruCache;

public class ImageCache {	
	static final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);    
	static final int cacheSize = maxMemory / 8;
	@SuppressLint("NewApi") static private LruCache<Integer, Bitmap> mMemoryCache = new LruCache<Integer, Bitmap>(cacheSize);
	
	private ImageCache() {		
	}
	
	@SuppressLint("NewApi") static public void addBitmapToMemoryCache(int inId, Bitmap inBitmap){
		if(inId < 0 || inBitmap == null){return;}
		mMemoryCache.put(inId, inBitmap);
	}
	
	@SuppressLint("NewApi") static public void addRemoteImageToMemoryCache(int inId, String inUrlString){
		try {
			Log.v("arfComm","URL:"+inUrlString+" , "+String.valueOf(inId));
			Bitmap tmpBitmap = mMemoryCache.get(inId);		
			if(tmpBitmap == null){			
				tmpBitmap = getBitmapFromUrl(inId, inUrlString);
				addBitmapToMemoryCache(inId, tmpBitmap);
			}
		} catch (Exception e) {
			console.loge("ImageCache.addRemoteImageToMemoryCache");
			e.printStackTrace();
		}
	}
	@SuppressLint("NewApi") static public int addRemoteImageToMemoryCacheAutoHashId(String inUrlString){
		int autoHashedId = getHashKeyOf(inUrlString);
		Log.v("arfComm","URL:"+inUrlString+" , "+String.valueOf(autoHashedId));
		Bitmap tmpBitmap = mMemoryCache.get(autoHashedId);		
		if(tmpBitmap == null){			
			tmpBitmap = getBitmapFromUrl(autoHashedId, inUrlString);
			addBitmapToMemoryCache(autoHashedId, tmpBitmap);
		}
		return autoHashedId;
	}
	
	static public void addRemoteImageToMemoryCache_overWriteExist(int inId, String inUrlString){
		Log.v("arfComm","URL(addRemoteImageToMemoryCache_overWriteExist):"+inUrlString+" , "+String.valueOf(inId));
		Bitmap tmpBitmap = getBitmapFromUrl(inId, inUrlString);			
		addBitmapToMemoryCache(inId, tmpBitmap);
		
	}
	static public int addRemoteImageToMemoryCacheAutoHashId_overWriteExist(String inUrlString){
		int autoHashedId = getHashKeyOf(inUrlString);
		Log.v("arfComm","URL:"+inUrlString+" , "+String.valueOf(autoHashedId));
		Bitmap tmpBitmap = getBitmapFromUrl(autoHashedId, inUrlString);
		addBitmapToMemoryCache(autoHashedId, tmpBitmap);		
		return autoHashedId;
	}
	
	
	@SuppressLint("NewApi") static public boolean onCache(int inKey){
		if(mMemoryCache.get(inKey) != null){
			return true;
		}else{
			return false;
		}
	}
	
	@SuppressLint("NewApi") static public void remove(int inKey){
		mMemoryCache.remove(inKey);
	}
	//-- for non-id entries,,use there path etc...
	static public int getHashKeyOf(String inString){
		return inString.hashCode();		
	}
	
	
	@SuppressLint("NewApi") static public int getCacheCount(){
		return mMemoryCache.size();
	}
	
	@SuppressLint("NewApi") static public Map<Integer, Bitmap> getCacheAsMap(){
		return mMemoryCache.snapshot();
	}
	
	@SuppressLint("NewApi") static public Bitmap getBitmapFromCacheFirstThenUrl(int inId, String inUrl){
		console.log("getBitmapFromCacheFirstThenUrl");
		Bitmap tmpBitmap = getBitmapFromCache(inId);
		if(tmpBitmap == null){
			final int fId = inId;
			final String fUrl = inUrl;
			/*HopperAsync has = new HopperAsync(HopperAsync.EthreadType.WaitOnBackgroundThread){
	        	@Override
	        	public String onNewThread(String ... inString){	        	   
	        	*/	try {
						Bitmap tmpBitmapX = getBitmapFromUrl(fId, fUrl);
						addBitmapToMemoryCache(fId, tmpBitmapX);
						//return "";
					} catch (Exception e) {
						console.loge("ImageCache.getBitmapFromCacheFirstThenUrl");
						e.printStackTrace();
					}
	        		//return "";
	       /* 	}
	        };				
	        has.execute("");
			*/
			
			//tmpBitmap = getBitmapFromUrl(inId, inUrl);
			//addBitmapToMemoryCache(inId, tmpBitmap);
		}
		tmpBitmap = getBitmapFromCache(inId);
		return tmpBitmap;		
	}
	
	@SuppressLint("NewApi") static public Bitmap getBitmapFromCache(int inId){
		return mMemoryCache.get(inId);
	}
	
	public static byte[] readBytes(InputStream inputStream) throws IOException {
		  // this dynamically extends to take the bytes you read
		  ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

		  // this is storage overwritten on each iteration with bytes
		  int bufferSize = 1024;
		  byte[] buffer = new byte[bufferSize];

		  // we need to know how may bytes were read to write them to the byteBuffer
		  int len = 0;
		  while ((len = inputStream.read(buffer)) != -1) {
		    byteBuffer.write(buffer, 0, len);
		  }

		  // and then we can return your byte array.
		  return byteBuffer.toByteArray();
		}
	
	static public Bitmap getBitmapFromUrl(final int inId, final String inUrlString){
		Bitmap tmpBitmap;
		HopperAsyncForObject has = new HopperAsyncForObject(HopperAsyncForObject.EthreadType.WaitOnBackgroundThread){
			@Override
			public Object onNewThread(String ... inString){				
				Log.v("arfComm","URL(getBitmapFromUrl):"+inUrlString);
				
				URL url = null;		
				try {
					url = new URL(inUrlString);
				} catch (MalformedURLException e) {
					console.log("ERROR 2");
					e.printStackTrace();
				}
				try {
					url.openConnection().getInputStream();//.reset();
				} catch (IOException e1) {
					console.log("ERROR 3");
					e1.printStackTrace();
				}
				try {
					/*InputStream f = url.openConnection().getInputStream();
					byte[] mBytes =  readBytes(f);
					console.log("mbytes size:" + mBytes.length);*/
					
					
					return BitmapFactory.decodeStream(url.openConnection().getInputStream());
				} catch (IOException e) {
					console.log("ERROR 4");
					e.printStackTrace();
				}			
				
				return null;
			}
		};				
		Object x = has.run("");
		return (Bitmap)x;
	//			return null;
		
	}
	@SuppressLint("NewApi") static public void logDumpCache(){		
		Map<Integer, Bitmap> snapMap = mMemoryCache.snapshot();
		for (Integer id : snapMap.keySet()){
		    Log.v("arfComm","cache ids:"+String.valueOf(id));
		}
	}
	
	

}
