package com.appMovil.tikitown;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * This listener make the request textsearch when we are moving
 * 
 * @variables
 * lastLat, lastLng: double that indicates the last position
 * places: to make the request
 * ctx: context of the application
 * */

public class TextLocListener extends SearchByText implements LocationListener {

	double lastLat;
	double lastLng;
	static Places places;
	public Context ctx;
		
	
	public TextLocListener(double lat, double lng){
		super();
		this.lastLat=lat;
		this.lastLng=lng;
	
	}
	
	public TextLocListener(Context ctx){
		this.ctx=ctx;
	}
	
	public TextLocListener(){
		super();
	}
	
	/**
	 * Called when our location has changed, check how far we're from our last position and if it's far enough
	 * update our last position and make the request
	 * 
	 * @params 
	 *  location: the current location
	 * */
	
	@Override
	public void onLocationChanged(Location location) {
		
		double latitude = location.getLatitude(); 
		double longitude = location.getLongitude();

		double dfLat=Math.abs(latitude-lastLat);
		double dfLng=Math.abs(longitude-lastLng);

		if(dfLng>0.0009&&dfLat>0.0009){
			lastLat=latitude;
			lastLng=longitude;
			
			Places places=new Places(ctx);

			places.createSQuery(site,address,dist,latitude,longitude);
			places.mode=Places.MODE_TEXT;

			places.execute();	

		}
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
}
