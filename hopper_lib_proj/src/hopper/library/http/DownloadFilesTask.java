package hopper.library.http;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.util.ByteArrayBuffer;

import com.example.hopperlibrary.HopperFileInfo;


import android.os.AsyncTask;

public class DownloadFilesTask extends AsyncTask<String, Integer, String> {
	
	// 0 - url source  // 1 - local new path and name ext
	
    protected String doInBackground(String... urlStrings) {
        int count = urlStrings.length;
        
        URL url;
		try {
			url = new URL(urlStrings[0]);
		
	        File file = new File(urlStrings[1]);
	        
	        URLConnection urlConnection = url.openConnection();
	        
	        InputStream inputStream = urlConnection.getInputStream();
	        BufferedInputStream bufferInputStream = new BufferedInputStream(inputStream);
	        
	        ByteArrayBuffer byteArrayBuffer = new ByteArrayBuffer(50);
	        int current = 0;
	        while ((current = bufferInputStream.read()) != -1) {
	        	byteArrayBuffer.append((byte) current);
	        }
	        
	        FileOutputStream fileOutputString = new FileOutputStream(file);
	        fileOutputString.write(byteArrayBuffer.toByteArray());
	        fileOutputString.close();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return urlStrings[1];
    }

    protected void onProgressUpdate(Integer... progress) {
       // setProgressPercent(progress[0]);
    }

    protected void onPostExecute(Long result) {
        //showDialog("Downloaded " + result + " bytes");
    }

}
