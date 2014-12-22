package com.example.hopperlibrary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;



public class HopperAjax extends AsyncTask<String, Void, String>  {
	
	public String fileUrl;
	
	public HopperAjax(String inFileUrl){
		this.fileUrl=inFileUrl;
	}
	
	@Override
	protected String doInBackground(String... arg0) {		
		Log.v("MyActivity","doInBackground AJAX ENTERED");
		Log.v("MyActivity","doInBackground ThreadId "+Thread.currentThread().getId());
		try {
			Thread.sleep((long)2000);
		} catch (InterruptedException e) {			
			e.printStackTrace();
		}
		//Log.v("MyActivity","doInBackground AJAX EXITING");		
		String retString;
		retString=send(arg0[0]);
		//retString=onNewThread(arg0[0]);
		Log.v("MyActivity","Concluding background AJAX thread NOW------");		
		return retString;
	}
	
	public String run(String inString){
		String retString=null;
		try {
			retString =  this.execute(inString).get();  //100000L, TimeUnit.MILLISECONDS
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return retString;
	}
	
	public String onNewThread(String ... inString){
		return "";
	}
	
	
public String send(String inString){
		
		
		String fullUrlString= this.fileUrl+"?message="+encode(inString);
		InputStream inputStream = null;
		String result = "";
		try {

           // create HttpClient
           HttpClient httpclient = new DefaultHttpClient();

           // make GET request to the given URL          
           Log.v("MyActivity","encoded url "+fullUrlString);
           Log.v("MyActivity","what type of HTTP.get?"+HttpGet.METHOD_NAME);
           HttpGet httpget = new HttpGet(fullUrlString);
           HttpResponse httpResponse = httpclient.execute(httpget);
           
           inputStream = httpResponse.getEntity().getContent();           
           if(inputStream != null)
               result = convertInputStreamToStringB(inputStream);
           else
               result = "Did not work!";
       } catch (Exception e) {
           Log.v("MyActivity", e.getLocalizedMessage());
           Log.v("MyActivity", "EXCEPTION in GET in ajax");
       }       
       return result;
   }
   private String convertInputStreamToString(InputStream inputStream) throws IOException{
       BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
       String line = "";
       String result = "";
       while((line = bufferedReader.readLine()) != null)
           result += line;
       	Log.v("MyActivity","stringBuilder"+ line);
       inputStream.close();
       return result;

   }
   private String convertInputStreamToStringB(InputStream inputStream) throws IOException{
	   BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
	   StringBuilder total = new StringBuilder();
	   String line;
	   while ((line = r.readLine()) != null) {
	       total.append(line);
	       Log.v("MyActivity","stringBuilder:"+ line);
	   }
	   
       inputStream.close();
       return total.toString();

   }
   
   
   public String encode(String inString){
		
		try {
			inString = URLEncoder.encode(inString,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return inString;
				
		
	}
	public String decode(String inString){
		inString = inString.replace("%20"," ");
		inString = inString.replace( "%22",String.valueOf('"'));		
		return inString;
		
	}
}
	
	
	
	
	


