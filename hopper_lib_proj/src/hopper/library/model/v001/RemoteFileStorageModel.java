package hopper.library.model.v001;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import org.json.JSONObject;

import com.example.hopperlibrary.HopperAsync;
import com.example.hopperlibrary.HopperJsonStatic;
import com.example.hopperlibrary.console;

import android.os.Environment;
import android.util.Log;

public class RemoteFileStorageModel {
	private HttpURLConnection connection = null;
	private DataOutputStream outputStream = null;
	private DataInputStream inputStream = null;
	private String pathToOurFile = ""; //"/data/file_to_send.mp3";
	private String urlServer = ""; //"http://192.168.1.1/handle_upload.php";
	private String lineEnd = "\r\n";
	private String twoHyphens = "--";
	private String boundary =  "*****";
	private String boundary2 =  "RQdzAAihJq7Xp1kjraqf"; 
	
	private int bytesRead, bytesAvailable, bufferSize;
	private byte[] buffer;
	private int maxBufferSize = 1*1024*1024*10; // = 10MB

	
	public RemoteFileStorageModel(String inHostStub, byte[] inByteArray, String inFileName){
		
		urlServer = inHostStub;
		try {
			pathToOurFile  = writeFile(inByteArray, inFileName);
		} catch (IOException e) {
			console.log("ERROR in RemoteFileStorageModel.writeFile" );
			e.printStackTrace();
		}
		
		console.log("newPath:"+pathToOurFile);
		// TODO Auto-generated constructor stub
	}
	
	public RemoteFileStorageModel(String inHostStub, String inLocalFilePath){
		urlServer = inHostStub;
		pathToOurFile  = inLocalFilePath;
		
		// TODO Auto-generated constructor stub
	}
	private String writeFile(byte[] data, String fileName) throws IOException{
		String filepath = Environment.getExternalStorageDirectory().getPath()+"/"+fileName;
		console.log("tmp File and Path"+filepath);
		File file = new File(filepath);
		if (!file.exists()) {
		  file.createNewFile();
		}		
		FileOutputStream out = new FileOutputStream(filepath);
		console.log("tmp File and Path"+filepath);
		out.write(data);
		out.close();
		return filepath;
	}
	
	public void setMaxBufferSize(int inByteSize){
		maxBufferSize  = inByteSize;
	}
	public int getMaxBufferSize(){
		return maxBufferSize;
	}
	
	public HashMap<String, String> storeFile(){
		return null;
	}
	
	public String upload(final int inUserId, final String inProcessType, final String inFileNameHint){
		HopperAsync has = new HopperAsync(HopperAsync.EthreadType.WaitOnBackgroundThread){
			@Override
			public String onNewThread(String ... inString){				
				Log.v("arfComm","----cookieTest ENTERED:");
				String dataFromService = null;
				try	{					
				    FileInputStream fileInputStream = new FileInputStream(new File(pathToOurFile) );
				 
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
				 
				    transmitFormParameter("command", inProcessType, outputStream);
				    transmitFormParameter("userId", String.valueOf(inUserId), outputStream);
				    transmitFormParameter("fileNameHint", inFileNameHint, outputStream);
				    

				    outputStream.writeBytes(twoHyphens + boundary + lineEnd);
				    outputStream.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + pathToOurFile +"\"" + lineEnd);
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
				catch (Exception ex)
				{
					Log.v("arfComm","----cookieTest FAILED:", ex);
				    //Exception handling
				}

				return dataFromService;
			}
		};			
		//String x = has.run("");
		return has.run("");
	}
	//TODO: fix this area more generic, unspecific
	// returns json data...ONLY FOR SPECIAL USER IMAGE UPLOAD ONLY--------
	public String upload(final int inUserId, final String inProcessType){
		
		HopperAsync has = new HopperAsync(HopperAsync.EthreadType.WaitOnBackgroundThread){
			@Override
			public String onNewThread(String ... inString){				
				Log.v("arfComm","----cookieTest ENTERED:");
				String dataFromService = null;
				try	{					
				    FileInputStream fileInputStream = new FileInputStream(new File(pathToOurFile) );
				 
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
				 
				    transmitFormParameter("command", inProcessType, outputStream);
				    transmitFormParameter("userId", String.valueOf(inUserId), outputStream);
				    

				    outputStream.writeBytes(twoHyphens + boundary + lineEnd);
				    outputStream.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + pathToOurFile +"\"" + lineEnd);
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
				catch (Exception ex)
				{
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

}
