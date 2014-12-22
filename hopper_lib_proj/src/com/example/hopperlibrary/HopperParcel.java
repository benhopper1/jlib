package com.example.hopperlibrary;

import java.util.ArrayList;



import android.os.Parcel;
import android.os.Parcelable;

public class HopperParcel implements Parcelable {
	public static HopperParcel thisInstance;
	public Object object; 
	public String mString;
	public HopperParcel() {
		thisInstance = this;
		// TODO Auto-generated constructor stub
	}
//--------callback-------------------------------------------------------------
	private ArrayList<OnCompleteListener> onCompleteListenerArrayList = new ArrayList<OnCompleteListener>();
	
	public interface OnCompleteListener{
	    public void onComplete(Object...objects);
	}
    public void setOnCompleteListener(OnCompleteListener listener) {
    	onCompleteListenerArrayList.add(listener);
    }
    public void raiseOnComplete(Object...objects){
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
	public void writeToParcel(Parcel arg0, int arg1) {
		// TODO Auto-generated method stub

	}
	
	// Just cut and paste this for now
    public static final Parcelable.Creator<HopperParcel> CREATOR = new Parcelable.Creator<HopperParcel>() {
        public HopperParcel createFromParcel(Parcel in) {
        	return thisInstance;
            //return new HopperParcel();
        }

        public HopperParcel[] newArray(int size) {
            return new HopperParcel[size];
        }
    };

}
