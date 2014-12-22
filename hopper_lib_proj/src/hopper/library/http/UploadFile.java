package hopper.library.http;

import hopper.cache.v003.ImageCache;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;
import android.util.Pair;

import com.example.hopperlibrary.HopperAsync;
import com.example.hopperlibrary.HopperFileInfo;

public class UploadFile{
	private InputStream fileInputStream;
	private HttpURLConnection connection = null;
	private DataOutputStream outputStream = null;
	private DataInputStream inputStream = null;
	private String boundary =  "*****";
	private String lineEnd = "\r\n";
	private String twoHyphens = "--";
	
	private int bytesRead, bytesAvailable, bufferSize;
	private byte[] buffer;
	private int maxBufferSize = 1*1024*1024; //1MB   *10; // = 10MB
	
	private String urlServer;
	private String pathToOurFile;
	
	public UploadFile(String inUrlString, String inLocalFilePath){
		urlServer = inUrlString;
		pathToOurFile  = inLocalFilePath;
		
	}
	
	public String upload(final Pair<String, String> ...inPairStrings){
		HopperAsync has = new HopperAsync(HopperAsync.EthreadType.WaitOnBackgroundThread){
			@Override
			public String onNewThread(String ... inString){				
				Log.v("arfComm","----cookieTest ENTERED:");
				String dataFromService = null;
				try	{
					//File tmpFile = HopperFileInfo.createTempFile(HopperFileInfo.getExtension(pathToOurFile), "demo");
					//ImageCache.shrinkBitmap(pathToOurFile,tmpFile.getAbsolutePath() , 300, 200);//300, 200
				    FileInputStream fileInputStream = new FileInputStream(pathToOurFile);
				 
				    URL url = new URL(urlServer);
				    connection = (HttpURLConnection) url.openConnection();
				 
				    // Allow Inputs &amp; Outputs.
				    connection.setDoInput(true);
				    connection.setDoOutput(true);
				    connection.setUseCaches(false);
				 
				    // Set HTTP method to POST.
				    connection.setRequestMethod("POST");				 
				    connection.setRequestProperty("Connection", "Keep-Alive");
				    connection.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);				    
				    outputStream = new DataOutputStream( connection.getOutputStream() );
				    
				    for(Pair<String, String> thePair : inPairStrings){
				    	String key = thePair.first;
				    	String value = thePair.second;
				    	transmitFormParameter(key, value, outputStream);
				    	
				    }				    

				    outputStream.writeBytes(twoHyphens + boundary + lineEnd);
				    outputStream.writeBytes("Content-Disposition: form-data; name=\"uploadedFile\";filename=\"" + pathToOurFile +"\"" + lineEnd);
				    outputStream.writeBytes(lineEnd);
				 
				    bytesAvailable = fileInputStream.available();
				    bufferSize = Math.min(bytesAvailable, maxBufferSize);
				    buffer = new byte[bufferSize];
				 
				    // Read file
				    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
				 
				    while (bytesRead > 0)
				    {
				        outputStream.write(buffer, 0, bufferSize);
				        bytesAvailable = fileInputStream.available();
				        bufferSize = Math.min(bytesAvailable, maxBufferSize);
				        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
				    }
				 
				    outputStream.writeBytes(lineEnd);
				    outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
				    
				    //---BH added
				    InputStream in = connection.getInputStream();
				    InputStreamReader isw = new InputStreamReader(in);

			        
				    
				 
				    // Responses from the server (code and message)
				    int serverResponseCode = connection.getResponseCode();
				    String serverResponseMessage = connection.getResponseMessage();
				    Log.v("arfComm","----serverResponseCode:"+String.valueOf(serverResponseCode));
				    Log.v("arfComm","----serverResponseMessage:"+serverResponseMessage);
				    if(serverResponseCode == 200){
				    	StringBuilder sb= new StringBuilder();
				    	//connection.getE
				    	int data = isw.read();
				        while (data != -1) {
				            char current = (char) data;
				            data = isw.read();
				            System.out.print(current);
				            Log.v("arfComm","----serverResponseMessage:"+current);
				            sb.append(current);
				        }
				        Log.v("arfComm","----serverResponseMessage final:"+sb);
				        //JSONObject json_fromService = HopperJsonStatic.getJsonObjectFromString(sb.toString());        
				        //newFilePath = HopperJsonStatic.getStringFromKeyForJsonObject(json_fromService, "newFilePath");				        
				        dataFromService = sb.toString();			    	
				    	
				    }			    
				 
				    fileInputStream.close();
				    outputStream.flush();
				    outputStream.close();
				}
				catch (Exception ex){
					Log.v("arfComm","----cookieTest FAILED:", ex);
				    //Exception handling
				}

				return dataFromService;
			}
		};			
		//String x = has.run("");
		return has.run("");
	}
	private void transmitFormParameter(String inParamKey, String inParamValue, DataOutputStream inOutStream){
		try {
			inOutStream.writeBytes(twoHyphens + boundary + lineEnd);
			inOutStream.writeBytes("Content-Disposition: form-data; name=\"" + inParamKey + "\"" + lineEnd);
			inOutStream.writeBytes("Content-Type: text/plain; charset=US-ASCII" + lineEnd);
			inOutStream.writeBytes("Content-Transfer-Encoding: 8bit" + lineEnd);
			inOutStream.writeBytes(lineEnd);
			inOutStream.writeBytes(inParamValue + lineEnd);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	// by stream-----
	public UploadFile(String inUrlString, InputStream inputStream){
		urlServer = inUrlString;
		fileInputStream = inputStream;		
	}
	
	public String uploadByStream(final Pair<String, String> ...inPairStrings){
		HopperAsync has = new HopperAsync(HopperAsync.EthreadType.WaitOnBackgroundThread){
			@Override
			public String onNewThread(String ... inString){				
				Log.v("arfComm","----cookieTest ENTERED:");
				String dataFromService = null;
				try	{
					
				    InputStream fileInputStream = UploadFile.this.fileInputStream;
				 
				    URL url = new URL(urlServer);
				    connection = (HttpURLConnection) url.openConnection();
				 
				    // Allow Inputs &amp; Outputs.
				    connection.setDoInput(true);
				    connection.setDoOutput(true);
				    connection.setUseCaches(false);
				 
				    // Set HTTP method to POST.
				    connection.setRequestMethod("POST");				 
				    connection.setRequestProperty("Connection", "Keep-Alive");
				    connection.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);				    
				    outputStream = new DataOutputStream( connection.getOutputStream() );
				    
				    for(Pair<String, String> thePair : inPairStrings){
				    	String key = thePair.first;
				    	String value = thePair.second;
				    	transmitFormParameter(key, value, outputStream);
				    	
				    }				    

				    outputStream.writeBytes(twoHyphens + boundary + lineEnd);
				    outputStream.writeBytes("Content-Disposition: form-data; name=\"uploadedFile\";filename=\"" + pathToOurFile +"\"" + lineEnd);
				    outputStream.writeBytes(lineEnd);
				 
				    bytesAvailable = fileInputStream.available();
				    bufferSize = Math.min(bytesAvailable, maxBufferSize);
				    buffer = new byte[bufferSize];
				 
				    // Read file
				    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
				 
				    while (bytesRead > 0)
				    {
				        outputStream.write(buffer, 0, bufferSize);
				        bytesAvailable = fileInputStream.available();
				        bufferSize = Math.min(bytesAvailable, maxBufferSize);
				        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
				    }
				 
				    outputStream.writeBytes(lineEnd);
				    outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
				    
				    //---BH added
				    InputStream in = connection.getInputStream();
				    InputStreamReader isw = new InputStreamReader(in);

			        
				    
				 
				    // Responses from the server (code and message)
				    int serverResponseCode = connection.getResponseCode();
				    String serverResponseMessage = connection.getResponseMessage();
				    Log.v("arfComm","----serverResponseCode:"+String.valueOf(serverResponseCode));
				    Log.v("arfComm","----serverResponseMessage:"+serverResponseMessage);
				    if(serverResponseCode == 200){
				    	StringBuilder sb= new StringBuilder();
				    	//connection.getE
				    	int data = isw.read();
				        while (data != -1) {
				            char current = (char) data;
				            data = isw.read();
				            System.out.print(current);
				            Log.v("arfComm","----serverResponseMessage:"+current);
				            sb.append(current);
				        }
				        Log.v("arfComm","----serverResponseMessage final:"+sb);
				        //JSONObject json_fromService = HopperJsonStatic.getJsonObjectFromString(sb.toString());        
				        //newFilePath = HopperJsonStatic.getStringFromKeyForJsonObject(json_fromService, "newFilePath");				        
				        dataFromService = sb.toString();			    	
				    	
				    }			    
				 
				    fileInputStream.close();
				    outputStream.flush();
				    outputStream.close();
				}
				catch (Exception ex){
					Log.v("arfComm","----cookieTest FAILED:", ex);
				    //Exception handling
				}

				return dataFromService;
			}
		};			
		//String x = has.run("");
		return has.run("");
	}
	
	
	
	
	
	
	
	
}
