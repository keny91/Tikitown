package com.appMovil.tikitown;

import android.app.Service;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;

/**
 * Service that puts the listener to our location to make the text's search's request
 * 
 * @variables
 *  locationManager: object that manage the notification's creation and sending
 *  places: to make the request
 *  locationListener: object that manage the location's changes
 * */

public class LocLisServ extends Service {

	static Places places;
	LocationManager locationManager;
	TextLocListener locationListener;
	
	public int onStartCommand(Intent intent,int flags, int startId){
	     super.onStartCommand(intent, flags, startId);

		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		String provider = locationManager.getBestProvider(criteria,true);
		Location myLocation = locationManager.getLastKnownLocation(provider);
		
		double latitude = myLocation.getLatitude(); 
		double longitude = myLocation.getLongitude();
		
		locationListener = new TextLocListener(latitude,longitude);
		locationListener.ctx=this;
		
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3500, 0,locationListener);  
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3500, 0,locationListener);

		return START_STICKY;
	}

	public void onDestroy() {
	    // TODO Auto-generated method stub
	    super.onDestroy();
	    if(locationManager!=null&&locationListener!=null)
	    	locationManager.removeUpdates(locationListener);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}