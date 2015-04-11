package com.appMovil.tikitown;

import java.util.List;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * Listener that manage the location's changes
 * 
 * @variables
 *  map: GoogleMap reference object
 *  ctx: context of the application
 *  lastLat, lastLng: last position registered
 *  mode: int to choose the listener's mode
 *  MODE_MAP: mode to update the displayed places
 *  MODE_ROUTE: mode to update the polyline that indicates the route to the place
 *   
 * */

public class LocListener implements LocationListener {

	GoogleMap map;
	Context ctx;
	double lastLat;
	double lastLng;
	
	public int mode;
	
	public static int MODE_MAP=1;
	
	public static int MODE_ROUTE=2;

	public LocListener(Context ctx, GoogleMap map,double lat, double lng){
		super();
		this.map=map;
		this.ctx=ctx;
		this.lastLat=lat;
		this.lastLng=lng;
	}
	
	@Override
	public void onLocationChanged(Location location) {
		double latitude = location.getLatitude(); 
		double longitude = location.getLongitude();

		LatLng latLng = new LatLng(latitude, longitude);
			
		double dfLat=Math.abs(latitude-lastLat);
		double dfLng=Math.abs(longitude-lastLng);
		
		if(dfLng>0.0009||dfLat>0.0009){
			if(mode==MODE_MAP){
				map.moveCamera(CameraUpdateFactory.newLatLng(latLng));		
				map.animateCamera(CameraUpdateFactory.zoomBy(0f));
				lastLat=latitude;
				lastLng=longitude;

				Places places=new Places();
				places.setMap(map, ctx);
				places.createQuery(latitude,longitude);
				places.mode=Places.MODE_SCAN;
				places.execute();
				
				SQLiteDatabase db=null;
				
				Database helper=new Database(ctx,Database.DB_NAME,null,Database.DB_VERSION);
				
				helper.deleteFarPlaces(db, lastLat, lastLng);
			}
			if(mode==MODE_ROUTE){
				routePoints(latitude,longitude);
				map.clear();
				map.addPolyline(RouteTracer.options);
			}
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
	
	/**
	 * Recalculate the polyline with the new position
	 * 
	 * @params
	 *  latitude,longitude: the coordinates of your current position
	 *  
	 * */
	
	public void routePoints(double latitude, double longitude){
		
		List<LatLng> points=RouteTracer.options.getPoints();
		
		LatLng current=new LatLng(latitude,longitude);
		
		LatLng first=points.get(0);
		LatLng second=points.get(1);
		
		LatLng dFS=new LatLng(Math.abs(second.latitude)-Math.abs(first.latitude)
				,Math.abs(second.longitude)-Math.abs(first.longitude));
		
		LatLng dCS=new LatLng(Math.abs(second.latitude)-Math.abs(current.latitude)
				,Math.abs(second.longitude)-Math.abs(current.longitude));
		
		
		if(dFS.latitude>dCS.latitude||dFS.longitude>dCS.longitude){
			points.remove(0);
			points.add(0, current);
		}else
			points.add(0,current);
		
		RouteTracer.options=new PolylineOptions()
			.addAll(points)
			.visible(true)
			.color(Color.BLUE)
			.width(5);
	}

}
