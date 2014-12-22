package com.example.hopperlibrary;


import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;


final class HopperLocationListener implements LocationListener{
	private HopperHardware parent;
	public HopperLocationListener(HopperHardware inParent){
		parent=inParent;
	}
	@Override
	public void onLocationChanged(Location arg0) {
		Log.v("MyActivity","gps location listener on changed ENTERED");
		//Log.v("MyActivity","test logitutde:"+arg0.getLongitude());		
		
		double tmpLongitude =	arg0.getLongitude();
		double tmpLatitude =	arg0.getLatitude();
		double tmpAltitude =	arg0.getAltitude();
		float tmpSpeed =		arg0.getSpeed();		
		long tmpTime =			arg0.getTime();
		float tmpBearing =		arg0.getBearing();
		String tmpProvider =	arg0.getProvider();
		
		//upfront current differentialQuantum calculation
		double current_differentialQuantum_longitude =	Math.abs(this.parent.longitude-tmpLongitude	);
		double current_differentialQuantum_latitude =	Math.abs(this.parent.latitude-tmpLatitude	);
		double current_differentialQuantum_altitude =	Math.abs(this.parent.altitude-tmpAltitude	);
		float current_differentialQuantum_speed =		Math.abs(this.parent.speed-tmpSpeed		);			
		float current_differentialQuantum_bearing =		Math.abs(this.parent.bearing-tmpBearing	);
		
		
		
		if (
			(		current_differentialQuantum_longitude	>	this.parent.differentialQuantum_longitude	)	|
			(		current_differentialQuantum_latitude	>	this.parent.differentialQuantum_latitude	)	|
			(		current_differentialQuantum_altitude	>	this.parent.differentialQuantum_altitude	)	|
			(		current_differentialQuantum_speed		>	this.parent.differentialQuantum_speed		)	|
			(		current_differentialQuantum_bearing		>	this.parent.differentialQuantum_bearing		)	|
			(!this.parent.provider.equals(tmpProvider)) )
								{
								Log.v("MyActivity","care about group changed....");		
								Log.v("MyActivity","--------------------------------------------------");
								this.parent.on_change_GPS_before(arg0);
								
								if(current_differentialQuantum_longitude	>	this.parent.differentialQuantum_longitude){ //had change
									Log.v("MyActivity","DiffOfLong OLD:"+this.parent.longitude+" new:"+tmpLongitude+"  diff:"+Math.abs(this.parent.longitude-tmpLongitude));
									this.parent.longitude=tmpLongitude;
									this.parent.on_change_longitude(tmpLongitude);
								}
								if(current_differentialQuantum_latitude	>	this.parent.differentialQuantum_latitude){ //had change
									this.parent.latitude=tmpLatitude;
									this.parent.on_change_latitude(tmpLatitude);
								}
								if(current_differentialQuantum_altitude	>	this.parent.differentialQuantum_altitude){ //had change
									this.parent.altitude=tmpAltitude;
									this.parent.on_change_altitude(tmpAltitude);
								}
								if(current_differentialQuantum_speed		>	this.parent.differentialQuantum_speed){ //had change
									this.parent.speed=tmpSpeed;
									this.parent.on_change_speed(tmpSpeed);
								}
								if(current_differentialQuantum_bearing		>	this.parent.differentialQuantum_bearing){ //had change
									this.parent.bearing=tmpBearing;
									this.parent.on_change_bearing(tmpBearing);
								}
								if(!this.parent.provider.equals(tmpProvider)){ //had change
									Log.v("MyActivity","Provider !=  check: "+this.parent.provider+"  "+tmpProvider);
									this.parent.provider=tmpProvider;
									this.parent.on_change_provider(tmpProvider);
								}
								
								this.parent.on_change_GPS_after(arg0);
		}else{
			if(this.parent.time!=tmpTime){ //had change
				this.parent.time=tmpTime;
				this.parent.on_change_time(tmpTime);
			}
		}
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	
}








public class HopperHardware implements SensorEventListener {
	private Activity context;
	private SensorManager sensorManager;
	private LocationManager locationManager;// = (LocationManager) getSystemService(LOCATION_SERVICE);
    private Location location;// = locationManager.getLastKnownLocation(bestProvider);
	private Criteria criteria;
	private String bestProvider;
	private LocationListener locationListener;
	
	
	// deprecated private Sensor sensor_compass;
	private Sensor sensor_accelerometer;
	private Sensor sensor_magnetometer;
	private float azimuth;
	private float pitch;
	private float roll;
	private float heading;
	
	
	public double differentialQuantum_longitude;
	public double differentialQuantum_latitude;
	public double differentialQuantum_altitude;
	public float differentialQuantum_speed;	
	public float differentialQuantum_bearing;
	
	public double longitude;
	public double latitude;
	public double altitude;
	public float speed;
	public long time;
	public float bearing;
	public String provider;
	
	
	public static enum HardwareType{
		Type_Heading("Type_Heading",1),
		Type_Gps("Type_Gps",2),
		Type_Compass("Type_Compass",4); //new heading
	    private String stringValue;
	    private int intValue;
	    private HardwareType(String toString, int value) {
	        stringValue = toString;
	        intValue = value;
	    }
	    public int i(){
	    	return intValue;
	    }
	    @Override
	    public String toString() {
	        return stringValue;
	    }		
	}
	
	public HopperHardware(){}
	public HopperHardware(Activity inActivity){		
		this.provider="";
		differentialQuantum_longitude 	=0;
		differentialQuantum_latitude	=0;
		differentialQuantum_altitude	=0;
		differentialQuantum_speed		=0;	
		differentialQuantum_bearing		=0;
		
		
		this.context =inActivity;
		this.sensorManager = (SensorManager) this.context.getSystemService(Context.SENSOR_SERVICE);
		//deprecated this.sensor_compass = this.sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		
		this.sensor_accelerometer = this.sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		this.sensor_magnetometer = this.sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION); // TYPE_MAGNETIC_FIELD);		
		
		this.locationManager = (LocationManager) this.context.getSystemService(Context.LOCATION_SERVICE);
		this.criteria = new Criteria();
	    this.bestProvider = locationManager.getBestProvider(criteria, false);
	    this.location = locationManager.getLastKnownLocation(bestProvider);	
		 on_initialize();
		
		
	}
	public void on_initialize(){}
	public void start(int inHardwareTypeFlag){ // as flag ored
				
		if( (inHardwareTypeFlag & HardwareType.Type_Heading.intValue) !=0 ){
			this.sensorManager.registerListener(this, this.sensor_magnetometer, SensorManager.SENSOR_DELAY_UI);
			Log.v("MyActivity","Listener Enabled : "+HardwareType.Type_Heading.toString());
		}
		if( (inHardwareTypeFlag & HardwareType.Type_Gps.intValue) !=0 ){
			this.sensorManager.registerListener(this, this.sensor_accelerometer, SensorManager.SENSOR_DELAY_UI);
			Log.v("MyActivity","Listener Enabled : "+HardwareType.Type_Gps.toString());
			//Log.v("MyActivity","HOPPER TEST POINT_!   : ");
			//Log.v("MyActivity","GPS READ LONG.   : "+this.location.getLongitude());
			this.locationListener = new HopperLocationListener(this);
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 00, 0, locationListener);
			
			
		}
		if( (inHardwareTypeFlag & HardwareType.Type_Compass.intValue) !=0 ){
			this.sensorManager.registerListener(this, this.sensor_accelerometer, SensorManager.SENSOR_DELAY_UI);
			Log.v("MyActivity","Listener Enabled : "+HardwareType.Type_Compass.toString());
		}
		
		
		
		
		
		
		
		
	}
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
	float[] mGravity;
	float[] mGeomagnetic;
	@Override
	public void onSensorChanged(SensorEvent event) { //arg0 replaced with event
		this.on_change_forAll_before(event);
		if( event.sensor.getType() == Sensor.TYPE_ORIENTATION){//heading event
			float degree =event.values[0];
			this.heading = degree;			
			this.on_change_orientation_before(event); 
			this.on_change_heading(degree);  // raise event
			this.on_change_orientation_after(event);
		}		
		  
		//----new type oreintation ??--------------------------------------------------------
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
		      mGravity = event.values;
		}
		if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
		      mGeomagnetic = event.values;
		    //  float degree =event.values[0];
			//  this.heading = degree;		      
		}	
		if (mGravity != null && mGeomagnetic != null) {
		      float R[] = new float[9];
		      float I[] = new float[9];
		      boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
		      if (success) {
		    	  float orientation[] = new float[3];
		    	  SensorManager.getOrientation(R, orientation);
		    	  this.azimuth = orientation[0]; // orientation contains: azimut, pitch and roll
		    	  this.pitch = orientation[1];
		    	  this.roll = orientation[2];		    	 
		      }
		}
		
		
		
		this.on_change_forAll_after(event);
	}
	
	// sensor getters---------------
	public float getAzmuth(){
		return this.azimuth;
	}
	public float getHeading(){
		return this.heading;
	}
	public double getLongitude(){
		return this.location.getLongitude();
	}
	public double getLatitude(){
		return this.location.getLongitude();
	}
	public double getSpeed(){
		return this.location.getSpeed();
	}
	public Long getTime(){
		return this.location.getTime();
	}
	public float getBearing(){
		return this.location.getBearing();
	}
	public String getProvider(){
		return this.location.getProvider();
	}
	
	
	//---------------------------EVENTS
	public void on_change_forAll_before(SensorEvent inEvent){};
	
	public void on_change_orientation_before(SensorEvent inEvent){};
	public void on_change_heading(float inNewValue){}
	public void on_change_orientation_after(SensorEvent inEvent){};
	
	public void on_change_GPS_before(Location inLocation){Log.v("MyActivity","GPS Changed BEFORE");}
	public void on_change_longitude(double inNewValue){Log.v("MyActivity","GPS Changed longitude-");}
	public void on_change_latitude(double inNewValue){Log.v("MyActivity","GPS Changed latitude");}
	public void on_change_altitude(double inNewValue){Log.v("MyActivity","GPS Changed altitude");}
	public void on_change_speed(float inNewValue){Log.v("MyActivity","GPS Changed speed");}
	public void on_change_time(Long inNewValue){Log.v("MyActivity","GPS Changed time");}
	public void on_change_bearing(float inNewValue){Log.v("MyActivity","GPS Changed bearing");}
	public void on_change_provider(String inNewValue){Log.v("MyActivity","GPS Changed provider");}
	public void on_change_GPS_after(Location inLocation){Log.v("MyActivity","GPS Changed AFTER");}
	
	public void on_change_forAll_after(SensorEvent inEvent){};
	
	
	
	
	
	
	
}
