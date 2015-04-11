package com.appMovil.tikitown;

import java.net.URLEncoder;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import android.app.FragmentManager;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Activity that shows the places that are requested to Places's server
 * 
 * @variables
 *  map: GoogleMap reference object
 *  places: asyncTask that makes the request to Places's server
 *  manager: manage the location obtaining
 *  details: asyncTask that makes the details request to Places's server
 * */

public class MapsActivity extends FragmentActivity {	

	GoogleMap map;
	Places places;
	
	LocationManager manager;
	
	LocListener locationListener;
	
	static PlacesDetails details;
	
	/**
	 * Setup the map fragment if it's not initialized
	 * */

	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the map.
		if (map == null){
			map = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();	
		}
		if(map!=null)
			setUpMap();
	}
	
	/**
	 * Set the map's options, get our current location, activate the location listener
	 * mark the places and set the Places's request to the server
	 * */

	private void setUpMap(){
		
		map.clear();
		
		map.setMyLocationEnabled(true);
		
		LatLng pos=getPosition(true);

		map.moveCamera(CameraUpdateFactory.newLatLng(pos));
		map.animateCamera(CameraUpdateFactory.zoomTo(15));
		
		places=new Places();

		places.setMap(map,getApplicationContext());
		places.createQuery(pos.latitude,pos.longitude);
		places.mode=Places.MODE_SCAN;
		
		SQLiteDatabase db=null;
		Database helper=new Database(this,Database.DB_NAME,null,Database.DB_VERSION);
		
		places.markPlaces(helper.getNearPlaces(db, pos.latitude, pos.longitude));
	 
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maps);
		
		setUpMapIfNeeded();

		searchPlace();
	}
	
	@Override
	public void onResume(){
		super.onResume();
		setUpMap();
		places.execute();
		
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(manager!=null&&locationListener!=null)
			manager.removeUpdates(locationListener);
	}

	/**
	 * Delete the registers of the database that correspond to the scanning
	 * and remove the location listener
	 * */
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		SQLiteDatabase db=null;
		Database helper=new Database(this,Database.DB_NAME,null,Database.DB_VERSION);
		
		helper.deleteRegister(db, "favorites", "false");
		
		if(manager!=null&&locationListener!=null)
			manager.removeUpdates(locationListener);
		
		super.onDestroy();
	}
	
	/**
	 * Manage the request to get the location of the place that you introduce in the search bar
	 * */

	public void searchPlace(){

		Button mBtnFind = (Button) findViewById(R.id.btn_show);

		mBtnFind.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				EditText etPlace = (EditText) findViewById(R.id.et_place);
				String location = etPlace.getText().toString();

				if(location==null || location.equals("")){
					Toast.makeText(getBaseContext(), "No Place is entered", Toast.LENGTH_SHORT).show();
					return;
				}

				String url = "https://maps.googleapis.com/maps/api/geocode/json?";

				try {
					location = URLEncoder.encode(location, "utf-8");
					url+="address="+location;
					url+="&sensor=false";
					GEOCodeTask Task = new GEOCodeTask();
					Task.ctx=getApplicationContext();
					Task.map=map;
					Task.mode=GEOCodeTask.MODE_MARK;

					Task.execute(url);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	

	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		
		switch(item.getItemId()){
		
		case R.id.settings:
			Intent i=new Intent(this,PreferenceActivity.class);
			startActivity(i);
			return true;
		case R.id.filter_settings:
			FragmentManager manager =getFragmentManager();
			FiltersDialog MyDialog = new FiltersDialog();
			MyDialog.show(manager,"myDialog");
			return true;
		case R.id.list_places:
			Intent i1=new Intent(this,DataActivity.class);
			LatLng pos=getPosition(false);
			Bundle b=new Bundle();
			b.putInt("mode", DataActivity.MODE_MAP);
			b.putDouble("lat",pos.latitude);
			b.putDouble("lng", pos.longitude);
			i1.putExtras(b);
			startActivity(i1);
			return true;
		default:
			return false;
		}
	}
	
	/**
	 * Get the current position and enabled the location listener according to the given param
	 * 
	 * @params
	 *  enabled: indicates if you activate the location listener
	 * 
	 * @return
	 *  a LatLng object with the coordinates of your position
	 * */
	
	public LatLng getPosition(boolean enabled){
		
		manager = (LocationManager) getSystemService(LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		String provider = manager.getBestProvider(criteria,true);
		Location myLocation = manager.getLastKnownLocation(provider);

		double latitude = myLocation.getLatitude(); 
		double longitude = myLocation.getLongitude();
		
		LatLng position=new LatLng(latitude,longitude);
		
		if(enabled){
			locationListener = new LocListener(getApplicationContext(),map,latitude,longitude);
			locationListener.mode=LocListener.MODE_MAP;

			manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 35000, 100, locationListener);
			manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 35000, 100, locationListener);
		}
		
		return position;
	}
}