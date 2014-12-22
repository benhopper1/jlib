package hopper.library.object;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import com.example.hopperlibrary.HopperAsync;



import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;


public class RemoteImage {
	//(int) ((Runtime.getRuntime().maxMemory() / 1024)/8)
	static private LruCache<Integer, Bitmap> hashMapOfBitmap = new LruCache<Integer, Bitmap>(1024 * 1024 * 4);
	
	// uses cache
	public static void updateView(final ImageView inImageView, final String inUrl, final int inId){
		Log.v("arfComm","Size of bitmap cache :"+String.valueOf(hashMapOfBitmap.size()));
		HopperAsync has = new HopperAsync(HopperAsync.EthreadType.WaitOnBackgroundThread){
			@Override
			public String onNewThread(String ... inString){				
				Bitmap cacheBitmap = hashMapOfBitmap.get(inId);
				if(cacheBitmap == null){				
					inImageView.setImageBitmap(getImageAsBitmap(inUrl));
					cacheBitmap = inImageView.getDrawingCache();
					hashMapOfBitmap.put(inId, cacheBitmap);
					Log.v("arfComm","No cached bitmap, put in cache"+String.valueOf(inId));
				}else{
					inImageView.setImageBitmap(cacheBitmap);
					Log.v("arfComm","Loaded from cache"+String.valueOf(inId));
				}					
				return "";
			}
		};				
		String x = has.run("");		
	}
	
	
	//does not use cache
	public static void updateView(final ImageView inImageView, final String inUrl){
		HopperAsync has = new HopperAsync(HopperAsync.EthreadType.WaitOnBackgroundThread){
			@Override
			public String onNewThread(String ... inString){
				Log.v("MyActivity","Main ThreadId "+inString[0]);
				inImageView.setImageBitmap(getImageAsBitmap(inUrl));
				Log.v("arfComm","not using cache "+inUrl);
				return "not using cache";
			}
		};				
		String x = has.run("");		
	}
	
	public static Bitmap getImageAsBitmap(String inImageUrl){
		String[] mStr = new String[] { inImageUrl };
		Bitmap map = null;
		for (String url : mStr) {
			map = downloadImage(url);
		}
		return map;
	}
	
	private static Bitmap downloadImage(String url){
		Bitmap bitmap = null;
		InputStream stream = null;
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inSampleSize = 1;
		try {
			stream = getHttpConnection(url);
			bitmap = BitmapFactory.
					decodeStream(stream, null, bmOptions);
			stream.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return bitmap;
	}
	
	private static InputStream getHttpConnection(String urlString)
			throws IOException {
		InputStream stream = null;
		URL url = new URL(urlString);
		URLConnection connection = url.openConnection();

		try {
			HttpURLConnection httpConnection = (HttpURLConnection) connection;
			httpConnection.setRequestMethod("GET");
			httpConnection.connect();

			if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				stream = httpConnection.getInputStream();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return stream;
	}	

}
