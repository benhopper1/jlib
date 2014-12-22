package hopper.library.object;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

public class BitmapLoaderTask extends AsyncTask<Integer, Void, Bitmap> {	
	private URL  url = null;
	public BitmapLoaderTask(String inUrlAsString) {
		//imageView = inImageView;		
		try {
			url = new URL(inUrlAsString);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	// Decode image in background.
    @Override
    protected Bitmap doInBackground(Integer... params) {
    	Bitmap bitmap = null; 
		try {
			bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		RemoteImageCache.addBitmapToMemoryCache(String.valueOf(params[0]), bitmap);    	
        return bitmap;
    }

}
