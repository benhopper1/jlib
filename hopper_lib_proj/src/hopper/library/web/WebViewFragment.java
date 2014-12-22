package hopper.library.web;


import java.util.ArrayList;

import hopper.library.phone.PhoneCallReceiver.OnIncomingCallListener;
import hopper.library.web.javascript.WebAppInterface;

import com.example.hopperlibrary.console;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewFragment extends Fragment {
	
	//======== E V E N T ==================================================================================================================================
	//-------event onCompleteListenerArrayList---------------------------------------------------------------------------------------------------------
		private ArrayList<OnCompleteListener> onCompleteListenerArrayList = new ArrayList<OnCompleteListener>();
		
		public interface OnCompleteListener{
		    public void onComplete(Object...objects);
		}
	    public void setOnCompleteListener(OnCompleteListener listener) {
	    	onCompleteListenerArrayList.add(listener);
	    }
		 /*
		  * @param0 String phoneNumber
		  * @param1	Intent received Intent
		  */
	    public void reportOnComplete(Object...objects){
	    	for(OnCompleteListener listener : onCompleteListenerArrayList){
	    		listener.onComplete(objects);
	        }
	    }
    //----------------------------------------------------------------------------------------------------------------------------------- 
	
	
	
	
	 
    private WebView mWebView;
	private boolean mIsWebViewAvailable;
	private String mUrl = null;
	private Activity attachedActivity;
	//private Context context;
	
	/**
	 * Creates a new fragment which loads the supplied url as soon as it can
	 * @param url the url to load once initialised
	 */
	public WebViewFragment(String url) {
	    super();
	    mUrl = url;
	    //context = inContext;
	    
	}
	
	/**
	 * Called to instantiate the view. Creates and returns the WebView.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
		
		if(mWebView != null){
			console.log("mWebView not null");
		}else{
			console.log("mWebView is null");
			mWebView = new WebView(getActivity());
		    
		    mWebView.getSettings().setJavaScriptEnabled(true);
		    mWebView.setWebChromeClient(new WebChromeClient() {
	            @Override
	            public boolean onConsoleMessage(ConsoleMessage cm) {
	                console.log(cm.message() + " -- From line " +
	                + cm.lineNumber() + " of "
	                + cm.sourceId() );
	                return true;
	            }
	        });
	    	mWebView.setWebViewClient(new SwAWebClient());
	        //wv_extra.loadUrl("http://192.168.0.16:35001/user/widget_userForm");
	        
	    	mWebView.addJavascriptInterface(new WebAppInterface(attachedActivity, mWebView, "http://192.168.0.16:35001", WebViewFragment.this), "Android");
		    mWebView.loadUrl(mUrl);
		    mIsWebViewAvailable = true;
		}
		
	    /*if (mWebView != null) {
	        mWebView.destroy();
	    }*/
	    /*mWebView = new WebView(getActivity());
	    
	    mWebView.getSettings().setJavaScriptEnabled(true);
	    mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage cm) {
                console.log(cm.message() + " -- From line " +
                + cm.lineNumber() + " of "
                + cm.sourceId() );
                return true;
            }
        });
    	mWebView.setWebViewClient(new SwAWebClient());
        //wv_extra.loadUrl("http://192.168.0.16:35001/user/widget_userForm");
        
    	mWebView.addJavascriptInterface(new WebAppInterface(attachedActivity, mWebView, "http://192.168.0.16:35001"), "Android");
	    mWebView.loadUrl(mUrl);
	    mIsWebViewAvailable = true;*/
	    return mWebView;
	}
	
	/**
	 * Convenience method for loading a url. Will fail if {@link View} is not initialised (but won't throw an {@link Exception})
	 * @param url
	 */
	public void loadUrl(String url) {
	    if(mIsWebViewAvailable){
	    	WebView wv_extra = getWebView();
	    	
	    	
	    	wv_extra.getSettings().setJavaScriptEnabled(true);
	    	wv_extra.setWebChromeClient(new WebChromeClient() {
	            @Override
	            public boolean onConsoleMessage(ConsoleMessage cm) {
	                console.log(cm.message() + " -- From line " +
	                + cm.lineNumber() + " of "
	                + cm.sourceId() );
	                return true;
	            }
	        });
	        wv_extra.setWebViewClient(new SwAWebClient());
	        //wv_extra.loadUrl("http://192.168.0.16:35001/user/widget_userForm");
	        
			wv_extra.addJavascriptInterface(new WebAppInterface(attachedActivity, wv_extra, "http://192.168.0.16:35001", WebViewFragment.this), "Android");
			wv_extra.loadUrl(mUrl = url);
    	}else{
    		console.log("WebView cannot be found. Check the view and fragment have been loaded.");
    	}
	}
	
	private class SwAWebClient extends WebViewClient {
		   
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }
         
    }
	
	/**
	 * Called when the fragment is visible to the user and actively running. Resumes the WebView.
	 */
	@Override
	public void onPause() {
	    super.onPause();
	    mWebView.onPause();
	}
	
	/**
	 * Called when the fragment is no longer resumed. Pauses the WebView.
	 */
	@Override
	public void onResume() {
	    mWebView.onResume();
	    super.onResume();
	}
	
	/**
	 * Called when the WebView has been detached from the fragment.
	 * The WebView is no longer available after this time.
	 */
	@Override
	public void onDestroyView() {
	    mIsWebViewAvailable = false;
	    super.onDestroyView();
	}
	
	/**
	 * Called when the fragment is no longer in use. Destroys the internal state of the WebView.
	 */
	@Override
	public void onDestroy() {
	    if (mWebView != null) {
	        mWebView.destroy();
	        mWebView = null;
	    }
	    super.onDestroy();
	}
	
	/**
	 * Gets the WebView.
	 */
	public WebView getWebView() {
	    return mIsWebViewAvailable ? mWebView : null;
	}
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        attachedActivity = activity;
    }
	   
} 
	
