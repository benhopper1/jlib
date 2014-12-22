package hopper.cache.v004;



import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;



import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.StrictMode;
import android.util.LruCache;

@TargetApi(Build.VERSION_CODES.GINGERBREAD) @SuppressLint("NewApi") public class ImageCache {
	
/*	static final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);    
	static final int cacheSize = maxMemory / 8;
	@SuppressLint("NewApi") static private LruCache<Integer, Bitmap> mMemoryCache = new LruCache<Integer, Bitmap>(cacheSize);	
	@SuppressLint("NewApi") static StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();


	
	 //-------events----------------------------------------------------------------------------------------------------------------------
	private ArrayList<OnImageReadyListener> onImageReadyListenerArrayList = new ArrayList<OnImageReadyListener>();
	
	public interface OnImageReadyListener{
	    public void onImageReady(Object...objects);
	}
    public void setOnImageReadyListener(OnImageReadyListener listener) {
    	onImageReadyListenerArrayList.add(listener);
    }
    private void reportOnImageReady(Object...objects){
    	for(OnImageReadyListener listener : onImageReadyListenerArrayList){
    		listener.onImageReady(objects);
        }
    }
    //-----------------------------------------------------------------------------------------------------------------------------------



    @SuppressLint("NewApi") public static Bitmap getBitmapFromURL(String src) {
    	StrictMode.setThreadPolicy(policy); 
        try {
            URL url = new URL(src);
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




*/

}
