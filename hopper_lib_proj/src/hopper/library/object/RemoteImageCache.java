package hopper.library.object;

import com.example.hopperlibrary.HopperAsync;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;

public class RemoteImageCache {	
	static private LruCache<String, Bitmap> mMemoryCache;
	static private boolean hadInit =false;
	static final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
	    // Use 1/8th of the available memory for this memory cache.
	static final int cacheSize = maxMemory / 8;
	
		
	private RemoteImageCache() {
		// never used, causes error for implementation on instanation
	}
	
	static public int getCacheCount(){
		return mMemoryCache.size();
	}
	
	static public void init(){
		if(hadInit == true){return;}
		hadInit = true;
	    mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
	        @SuppressLint("NewApi")
			@Override
	        protected int sizeOf(String key, Bitmap bitmap) {
	            // The cache size will be measured in kilobytes rather than
	            // number of items.
	            return bitmap.getByteCount() / 1024;
	        }
	    };
	}
	
	static public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
	    if (getBitmapFromMemCache(key) == null) {
	        mMemoryCache.put(key, bitmap);
	    }
	}

	static public Bitmap getBitmapFromMemCache(String key) {
	    return mMemoryCache.get(key);
	}
	
	// loads bitmap only if non-exist....
	static public void preLoadBitmap(int resId, String inUrl) {
	    final String imageKey = String.valueOf(resId);

	    final Bitmap bitmap = getBitmapFromMemCache(imageKey);
	    if (bitmap != null) {
	    	//imageView.setImageBitmap(bitmap);
	    } else {
	    	//imageView.setImageResource(R.drawable.image_placeholder);
	    	BitmapLoaderTask task = new BitmapLoaderTask(inUrl);
	        task.execute(resId);
	    }
	}
	
	static public void loadImageView_aSync(int inId, String inUrl, ImageView inImageView){}
	static public void loadImageView_sync(final int inId, String inUrl, final ImageView inImageView){
		final String imageKey = String.valueOf(inId);
		final Bitmap bitmap = getBitmapFromMemCache(imageKey);
	    if (bitmap != null) {
	    	HopperAsync has = new HopperAsync(HopperAsync.EthreadType.WaitOnBackgroundThread){
				@Override
				public String onNewThread(String ... inString){						
						inImageView.setImageBitmap(bitmap);						
						Log.v("arfComm","used cache for:"+String.valueOf(inId));
						return "";
				}
			};				
			String x = has.run("");		
	    	//imageView.setImageBitmap(bitmap);
	    } else {
	    	inImageView.setImageBitmap(bitmap);	
	    	//overload workertask for view , async manner,,,,
	    	
	    	//imageView.setImageResource(R.drawable.image_placeholder);
	        //BitmapWorkerTask task = new BitmapWorkerTask(inUrl);
	       // task.execute(inId);
	    }
	}
	
	

}


