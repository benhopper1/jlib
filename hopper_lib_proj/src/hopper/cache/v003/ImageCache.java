package hopper.cache.v003;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;
import android.util.LruCache;

import com.example.hopperlibrary.HopperAsyncForObject;
import com.example.hopperlibrary.HopperFileInfo;
import com.example.hopperlibrary.console;

@TargetApi(Build.VERSION_CODES.GINGERBREAD) public class ImageCache {
	
	static final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);    
	static final int cacheSize = maxMemory / 8;
	@SuppressLint("NewApi") static private LruCache<Integer, Bitmap> mMemoryCache = new LruCache<Integer, Bitmap>(cacheSize);	
	
	@SuppressLint("NewApi") static StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	
	private ImageCache(){}
	
	static public int getHashKeyOf(String inString){
		return inString.hashCode();		
	}
	
	@SuppressLint("NewApi") static public Bitmap getBitmapFromUrl(final String inUrlString){

	    	StrictMode.setThreadPolicy(policy); 
	        try {
	            URL url = new URL(inUrlString);
	            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	            connection.setDoInput(true);
	            connection.connect();
	            InputStream input = connection.getInputStream();
	            Bitmap myBitmap = BitmapFactory.decodeStream(input);
	            return myBitmap;
	        } catch (IOException e) {
	            e.printStackTrace();
	            return null;
	        }
	    }
	/*	HopperAsyncForObject has = new HopperAsyncForObject(HopperAsyncForObject.EthreadType.WaitOnBackgroundThread){
			@Override
			public Object onNewThread(String ... inString){				
				Log.v("arfComm","URL(getBitmapFromUrl):"+inUrlString);
				Bitmap resultBitmap = null;
				URL url = null;		
				try {
					url = new URL(inUrlString);
					url.openConnection().getInputStream();//.reset();
					resultBitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
					
				} catch (MalformedURLException e) {
					console.log("MalformedURLException");
					e.printStackTrace();
					
				}catch (IOException e) {
					console.log("IOException");
					e.printStackTrace();
				}			
				
				return resultBitmap;
			}
		};
		
		Object x = has.run("");
		return (Bitmap)x;		*/
	//}
	
	@SuppressLint("NewApi") static public Bitmap getBitmapFromCacheFirstThenUrl(final String inUrl){
		console.log("getBitmapFromCacheFirstThenUrl");
		final int hashId = ImageCache.getHashKeyOf(inUrl);
		Bitmap tmpBitmap = getBitmapFromCache(hashId);
		if(tmpBitmap == null){
			try {
				tmpBitmap = getBitmapFromUrl(inUrl);
				addBitmapToMemoryCache(hashId, tmpBitmap);
			} catch (Exception e) {
				console.loge("ERROR in ImageCache.getBitmapFromCacheFirstThenUrl");
				e.printStackTrace();
			}
			return tmpBitmap;
	        	
		}
		tmpBitmap = getBitmapFromCache(hashId);
		return tmpBitmap;		
	}
	
	@SuppressLint("NewApi") static public Bitmap getBitmapFromCache(int inId){
		return mMemoryCache.get(inId);
	}
	
	@SuppressLint("NewApi") static public void addBitmapToMemoryCache(int inId, Bitmap inBitmap){
		if(inId < 0 || inBitmap == null){return;}
		mMemoryCache.put(inId, inBitmap);
	}
	
	
	static public Bitmap shrinkBitmap(String inFile, String destFile, int width, int height){
		console.log("SHRINKING--------");
		 BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
		    bmpFactoryOptions.inJustDecodeBounds = true;
		    Bitmap bitmap = BitmapFactory.decodeFile(inFile, bmpFactoryOptions);

		    int heightRatio = (int)Math.ceil(bmpFactoryOptions.outHeight/(float)height);
		    int widthRatio = (int)Math.ceil(bmpFactoryOptions.outWidth/(float)width);

		    if (heightRatio > 1 || widthRatio > 1)
		    {
		     if (heightRatio > widthRatio)
		     {
		      bmpFactoryOptions.inSampleSize = heightRatio;
		     } else {
		      bmpFactoryOptions.inSampleSize = widthRatio; 
		     }
		    }

		    bmpFactoryOptions.inJustDecodeBounds = false;
		    bitmap = BitmapFactory.decodeFile(inFile, bmpFactoryOptions);
		    
		    File file = new File(destFile);
		    FileOutputStream fOut = null;
			try {
				fOut = new FileOutputStream(file);
				String fileExt = HopperFileInfo.getExtension(inFile);
				if(fileExt.equalsIgnoreCase("png")){
					console.log("compressing as PNG");
					bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut);
				}
				if(fileExt.equalsIgnoreCase("jpg") || fileExt.equalsIgnoreCase("jpeg")){
					console.log("compressing as JPEG");
					bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
				}
				console.log("Flushing---" + fileExt + file.length());
				
				fOut.flush();
				fOut.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		    
	
		    
		    
		    
		 return bitmap;
		}
	
	
	
}
