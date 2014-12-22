package hopper.library.activityresource;

import java.util.ArrayList;

import com.example.hopperlibrary.HopperParcel;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class ProxyParcel implements Parcelable {
	/*
	 * This static is only used in the transport(startup), object shall(to be safe) get reference on activity.onCreate, may consider lock..extra safety 
	 */
	
	public static ProxyParcel thisInstance;
	
	public Intent startupIntent;
	public	int startupRequestCode;
	public Bundle startupOptions;
	
	
	
	public ProxyParcel() {
		thisInstance = this;
		startupIntent = new Intent();
	}
	
	
	
	

//--------callback-------------------------------------------------------------
	private ArrayList<OnCompleteListener> onCompleteListenerArrayList = new ArrayList<OnCompleteListener>();
	
	public interface OnCompleteListener{
	    public void onComplete(Object...objects);
	}
    public void setOnCompleteListener(OnCompleteListener listener) {
    	onCompleteListenerArrayList.add(listener);
    }
    public void reportOnComplete(Object...objects){
    	for(OnCompleteListener listener : onCompleteListenerArrayList){
    		listener.onComplete(objects);
        }
    }
//----------------------------------------------------------------------------
	
	
	
	
	

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub

	}
	
	public static final Parcelable.Creator<ProxyParcel> CREATOR = new Parcelable.Creator<ProxyParcel>() {
        public ProxyParcel createFromParcel(Parcel in) {
        	return thisInstance;            
        }

        public ProxyParcel[] newArray(int size) {
            return new ProxyParcel[size];
        }
    };

}
