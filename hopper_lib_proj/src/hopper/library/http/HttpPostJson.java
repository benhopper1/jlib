package hopper.library.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import com.example.hopperlibrary.HopperAsync;

public class HttpPostJson {
	
	private String url;
	public HttpPostJson(String inUrl){
		url = inUrl;
	}
	
	public String sendJson(final String inJsonString){		
		HopperAsync hopperAsync = new HopperAsync(HopperAsync.EthreadType.WaitOnBackgroundThread){
			@Override
			public String onNewThread(String ... inString){
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppostreq = new HttpPost(HttpPostJson.this.url);
				StringEntity se;
				java.io.InputStream inputstream = null;
				HttpResponse httpresponse = null;
				String resultstring = null;
				
				try{
					se = new StringEntity(inJsonString);	
					httppostreq.setEntity(se);
					httppostreq.setHeader("Accept", "application/json");
					httppostreq.setHeader("Content-type", "application/json");		 
					httpresponse = httpclient.execute(httppostreq);
					inputstream = httpresponse.getEntity().getContent();				
					resultstring = HttpPostJson.this.convertStreamToString(inputstream);
					inputstream.close();
					
				}catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}catch (IllegalStateException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				return resultstring;
			}
		};		
		return hopperAsync.run("");
	}
	
	public String convertStreamToString(java.io.InputStream inputstream) throws IOException {
        if (inputstream != null) {
                StringBuilder sb = new StringBuilder();
                String line;
                try{
                    BufferedReader reader = new BufferedReader(
                    		new InputStreamReader(inputstream, "UTF-8"));
                        	while ((line = reader.readLine()) != null) {
                        		sb.append(line).append("\n");
                        	}
                	}
                finally{
                    inputstream.close();
                }
                return sb.toString();
        }else{
                return "";
        }
	}
	
}