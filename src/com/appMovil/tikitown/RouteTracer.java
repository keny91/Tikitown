package com.appMovil.tikitown;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * Activity that trace the route to the place previously selected
 * 
 * @variables
 *  map: GoogleMap object reference
 *  coder: object to get an approximate address of your current position
 *  manager: a manager that manage the location obtaining
 *  listener: manage the location's changes
 *  options: options of the polyline of the route
 *  
 * */

public class RouteTracer extends FragmentActivity{

	GoogleMap map=null;
	Geocoder coder;
	
	LocationManager manager;
	LocListener listener;
	
	static PolylineOptions options;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.route);
		
		if(map==null)
			map = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
		
		Bundle b=getIntent().getExtras();
		
		String origin=getAddress();
		
		PlacesContainer place=new PlacesContainer();
		place.setFormatted_address(b.getString("address"));
		place.setLocation(b.getDouble("lat"),b.getDouble("lng"));
		
		moveCamera(place.getLatitude(),place.getLongitude());
		
		try {
			
			origin=URLEncoder.encode(origin,"utf-8");
			String destination=URLEncoder.encode(place.getFormatted_address(),"utf-8");
			String[] addressRoute={origin,destination};
			
			routes(addressRoute);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(manager!=null&&listener!=null)
			manager.removeUpdates(listener);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(manager!=null&&listener!=null)
			manager.removeUpdates(listener);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(manager!=null&&listener!=null){
			manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 35000, 100, listener);
			manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 35000, 100, listener);
		}
	}



	/**
	 * Move the camera to the given position
	 * 
	 * @params
	 *  lat, lng: doubles that indicates the current position
	 * */
	
	public void moveCamera(double lat, double lng){
		
		LatLng latLng = new LatLng(lat, lng);
		map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
		map.animateCamera(CameraUpdateFactory.zoomTo(20));
		map.addMarker(new MarkerOptions().position(latLng));
	}
	
	/**
	 * Create the query and execute the asyncTask to request the route's points
	 * 
	 * @params
	 * address: array with origin and destination of the route
	 * */
	
	public void routes(String[] address){
		String query="http://maps.googleapis.com/maps/api/directions/json?";
		
		query+="origin="+address[0];
		query+="&destination="+address[1];
		query+="&sensor=false";
		
		RouteTask tracer=new RouteTask();
		tracer.map=map;
		tracer.execute(query);
		
	}

	
	/**
	 * Use the Geocoder to get an  address of the current position and enable the 
	 * location listener to update your position and recreate the route
	 * 
	 * @return
	 *  A String with the address requested
	 * */
	
	public String getAddress(){
		
		coder=new Geocoder(getApplicationContext());
		
		String result="";
		
		map.setMyLocationEnabled(true);
		manager = (LocationManager) getSystemService(LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		String provider = manager.getBestProvider(criteria,true);
		Location myLocation = manager.getLastKnownLocation(provider);

		double latitude = myLocation.getLatitude();
		double longitude = myLocation.getLongitude();
		
		listener=new LocListener(getApplicationContext(),map,latitude,longitude);
		listener.mode=LocListener.MODE_ROUTE;
		
		manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 35000, 100, listener);
		manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 35000, 100, listener);
		
		try {
			List<Address> address=coder.getFromLocation(latitude, longitude, 5);
			for(int i=0;i<=address.get(0).getMaxAddressLineIndex();i++){
				result+=address.get(0).getAddressLine(i);
				if(i!=address.get(0).getMaxAddressLineIndex())
					result+=",";
				}
			return result;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
