package hopper.library.web.javascript;

import hopper.image.utility.Transform;
import hopper.library.communication.v003.ConnectedDevice;
import hopper.library.communication.v003.DataLayer;
import hopper.library.communication.v003.WebSocketService;
import hopper.library.http.Upload_frag;
import hopper.library.http.Upload_frag.OnUploadListener;
import hopper.library.web.WebViewFragment;

import com.example.hopperlibrary.MainActivity;
import com.example.hopperlibrary.console;


import hopper.library.R;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.Html;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;





public class WebAppInterface {
    Context mContext;
    WebView webView;
    String domain;
    WebViewFragment webViewFragment;
    
    //==================================================================
    // I N T E R F A C E
    //==================================================================
    public interface LoadUploadFragmentApplicationScope {
 	   public void loadUploadFragment(OnUploadListener inOnUploadListener);
 	   public void back();
 	  public void fragmentShowTop();
    }
    
    
    
    
    /** Instantiate the interface and set the context */
    public WebAppInterface(Context c, WebView inWebView, String inDomain, WebViewFragment inWebViewFragment) {
        mContext = c;
        webView = inWebView;
        domain = inDomain;
        webViewFragment = inWebViewFragment;
    }
    
    //cause event to occure
    @JavascriptInterface
    public void reportOnComplete(String inJsonStructAsString){        
        console.log("reportOnComplete");
        if(webViewFragment != null){
        	webViewFragment.reportOnComplete(inJsonStructAsString);
        }
    }

    /** Show a toast from the web page */
    @JavascriptInterface
    public void showToast(String toast) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
        console.log("function called.." + toast);
    }
    
    @JavascriptInterface
    public void loadReadEmailIntent(String toast) {
        //Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
        console.log("loadReadEmailIntent called.." + toast);
        //Intent intent = mContext.getPackageManager().getLaunchIntentForPackage("com.android.email");
        //Intent testIntent = new Intent(Intent.ACTION_VIEW);  
        /*Uri data = Uri.parse("mailto:?subject=" + "blah blah subject" + "&body=" + "blah blah body" + "&to=" + "sendme@me.com");  
        testIntent.setData(data);
        mContext.startActivity(testIntent);this*/
        Intent sharingIntent = new Intent(Intent.CATEGORY_APP_EMAIL);
        //sharingIntent.setType("text/html");
        //sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml("<p>This is the text that will be shared.</p>"));
        mContext.startActivity(Intent.createChooser(sharingIntent,"Share using"));
    }
    
    @JavascriptInterface
    public void surfTo_SameDomain(final String inUrl){
    	//webView.loadUrl("http://192.168.0.16:35001/user/mobilelogin");
    	/*webView.post(new Runnable() {
		    @Override
		    public void run() {							    	
		    	webView.loadUrl(domain + "/user/mobilelogin");
	    	}
		});		*/					
    	//webView.loadUrl(domain + "/user/mobilelogin");
    	//Activity y;
    	((Activity)mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
            	webView.loadUrl(domain + inUrl);
            }
        });
    	
    }
    
    
    //String inUploadServiceUrl, String inCommand, String inTheme, String inCaption, String inFileFilter, OnUploadListener inListener
    @JavascriptInterface
    public void fileChooser(){
    	console.log("fileChooser ENTERED!!!");
    	
    	((LoadUploadFragmentApplicationScope)mContext).loadUploadFragment(new OnUploadListener(){
			@Override
			public void onUpload(Object... objects) {
				console.log("----fileChooser -----  upload callback!!!!!:" + (String) objects[0]);
				//((LoadUploadFragmentApplicationScope)mContext).back();
				String newFile = "/" + (String) objects[0];
				WebAppInterface.this.updateImageSource(newFile);
				console.log("updateImageSource back from");
			}
    	});
    	
    	
    	
    	
    	/*loadFragment(false, R.layout.blank ,new Upload_frag("http://192.168.0.16:35001/upload", "imageStore", "normalUserImage","Select Image", "image/*", new OnUploadListener(){
			@Override
			public void onUpload(Object... objects) {
				console.log("----fileChooser -----  upload callback!!!!!:" + (String) objects[0]);
				String newFile = (String) objects[0];										
			}
			
		}));*/
    }
    
    
    //============================================================================================================
    //  CALL TO JAVASCRIPT METHODS
    //============================================================================================================
    
    public void updateImageSource(final String inSrc){
    	console.log("updateImageSource ENTERED!!!:" + inSrc);
    	if(webView == null){
    		console.log("webView == null");
    	}else{
    		console.log("webView != null");
    	}
    	console.log("test A0 ");
    	
    	/*((Activity)mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
        	   WebAppInterface.this.webView.loadUrl("javascrifragmentShowToppt:console.log('HELLO BEN TTT$$');");
  	    	   WebAppInterface.this.webView.loadUrl("javascript:Widget_userFormScript.updateImageSource('" + inSrc + "')");
            }
        });*/
    	
    	
    	
    	//((LoadUploadFragmentApplicationScope)mContext).fragmentShowTop();
    	//((LoadUploadFragmentApplicationScope)mContext).
    	webView.loadUrl("javascript:console.log('HELLO BEN TTT$$');");
    	console.log("test 0 ");
    	webView.loadUrl("javascript:Widget_userFormScript.updateImageSource('" + inSrc + "')");
    	console.log("test 1 ");
    }
    
    
	/*@SuppressLint("NewApi") private void loadFragment(boolean useBackstack, int inLayoutId, Fragment inFragmentInstance){
		android.app.FragmentManager fm = ((Activity)mContext).getFragmentManager();
		FragmentTransaction fragmentTransaction = fm.beginTransaction();		
		fragmentTransaction.replace(inLayoutId, inFragmentInstance);
		
		if(useBackstack){
			fragmentTransaction.addToBackStack(null);
		}
		
		fragmentTransaction.commit();
	}*/
    
    
    
    //Intent intent = getPackageManager().getLaunchIntentForPackage("com.android.email");
    //startActivity(intent);
}