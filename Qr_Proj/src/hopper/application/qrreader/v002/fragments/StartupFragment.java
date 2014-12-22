package hopper.application.qrreader.v002.fragments;



import java.util.ArrayList;

import com.example.hopperlibrary.console;


import hopper.application.qrreader.v002.R;
import hopper.image.utility.Transform;
import hopper.library.local.store.LocalUserStore;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class StartupFragment extends Fragment{
	private View view;
	private Activity attachedActivity;
	
	//======== E V E N T ==================================================================================================================================
	//-------event onScanPressListenerArrayList---------------------------------------------------------------------------------------------------------
		private ArrayList<OnScanPressListener> onScanPressListenerArrayList = new ArrayList<OnScanPressListener>();
		
		public interface OnScanPressListener{
		    public void onScanPress(Object...objects);
		}
	    public void setOnScanPressListener(OnScanPressListener listener) {
	    	onScanPressListenerArrayList.add(listener);
	    }
		 /*
		  * @param0 
		  * @param1	
		  */
	    public void reportOnScanPress(Object...objects){
	    	for(OnScanPressListener listener : onScanPressListenerArrayList){
	    		listener.onScanPress(objects);
	        }
	    }
    //----------------------------------------------------------------------------------------------------------------------------------- 
	//======== E V E N T ==================================================================================================================================
	//-------event onExitPressListenerArrayList---------------------------------------------------------------------------------------------------------
		private ArrayList<OnExitPressListener> onExitPressListenerArrayList = new ArrayList<OnExitPressListener>();
		
		public interface OnExitPressListener{
		    public void onExitPress(Object...objects);
		}
	    public void setOnExitPressListener(OnExitPressListener listener) {
	    	onExitPressListenerArrayList.add(listener);
	    }
		 /*
		  * @param0
		  * @param1	
		  */
	    public void reportOnExitPress(Object...objects){
	    	for(OnExitPressListener listener : onExitPressListenerArrayList){
	    		listener.onExitPress(objects);
	        }
	    }
    //----------------------------------------------------------------------------------------------------------------------------------- 
	
	    
    public StartupFragment(){
    	
    }
    
    public void setImage(final String imageUrl){
    	
    	 view.post(new Runnable() {
			    @Override
			    public void run() {
			    	ImageView iv_userImage = (ImageView)view.findViewById(R.id.iv_userImage);
			    	Bitmap beforeTransform = hopper.cache.v003.ImageCache.getBitmapFromCacheFirstThenUrl(imageUrl);
			    	Bitmap afterTransform = Transform.roundCorners(beforeTransform, 50);
			    	iv_userImage.setImageBitmap(afterTransform);
		    		//member_imageView.setImageBitmap(hopper.cache.v003.ImageCache.getBitmapFromCacheFirstThenUrl("http://" + ((MainActivity)attachedActivity).httpHostIp + ":" + ((MainActivity)attachedActivity).httpHostPort + "/" + imagePath));
		    	}
			 });
    }
    
    
    
	 @Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
		 View v = inflater.inflate(R.layout.startup_layout, container, false);
		 this.view = v;
		 
		 
		 if(!(LocalUserStore.getValueAsString("domain") == null) && !(LocalUserStore.getValueAsString("userImageUrl") == null)){
			 console.log("imageUrl:" + LocalUserStore.getValueAsString("domain") + LocalUserStore.getValueAsString("userImageUrl"));
			 this.setImage(LocalUserStore.getValueAsString("domain") + LocalUserStore.getValueAsString("userImageUrl"));
		 }
		 Button bt_scan = (Button) v.findViewById(R.id.bt_scan);
		 bt_scan.setOnClickListener(new OnClickListener(){
			 @Override
			 public void onClick(View arg0) {
				 reportOnScanPress();
				
			 }
		 });
	        
        Button bt_exit = (Button) v.findViewById(R.id.bt_exit);
        bt_exit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				reportOnExitPress();
				
			}
        });
	        
	        
	        
		 
		 
		 return v;
		 
	 }
	 
	@Override
	public void onAttach(Activity activity) {
	    super.onAttach(activity);
	    attachedActivity = activity;
	}
	
	
	
	
}
