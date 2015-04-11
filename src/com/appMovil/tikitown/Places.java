package com.appMovil.tikitown;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.json.*;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;

/**
 * Make the request to Place's server
 * 
 * @variables
 * context: the context application
 * key: the key to get access to the server
 * mode: integer with the mode
 * MODE_SCAN: ask for new places
 * MODE_TEXT: ask for places according to a String we introduce
 * map: GoogleMap Object reference
 * head: header to radar query
 * headS: header to text query
 * query: the complete query
 * 
 * */

public class Places extends AsyncTask<Void,String,JSONArray>{
		
	Context context;
			
	final static String key="AIzaSyAElky2zvtOTMd4c8Hqi7QhK2nLoSiiWDE";
	
	public int mode;
	
	public static int MODE_SCAN=1;
	
	public static int MODE_TEXT=2;
	
	GoogleMap map;
	
	String head = "https://maps.googleapis.com/maps/api/place/radarsearch/json";
	
	String headS = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=";
	
	String query;
	
	public Places(Context ctx) {
		this.context=ctx;
		// TODO Auto-generated constructor stub
	}

	public Places() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Make the request to Places's server
	 * 
	 * @return
	 *  A JSONArray with the received information
	 * */
	
	public JSONArray query(){
		
		if(MapsActivity.details!=null)
			MapsActivity.details.cancel(true);
		
		HttpClient client=new DefaultHttpClient();
		
		try{
			HttpGet get = new HttpGet(query);
			HttpResponse httpResponse = client.execute(get);
			int status = httpResponse.getStatusLine().getStatusCode();
			if(status == 200){
				HttpEntity e = httpResponse.getEntity();
				String data = EntityUtils.toString(e);
				JSONObject response= new JSONObject(data);
				String response_status = response.get("status").toString();
				if(response_status.equals("OK")){
					JSONArray json = response.getJSONArray("results");
					return json;
				}else
					return null;
			}else
				return null;
		}catch(Exception e){
	    	System.out.println(e);
	    	return null;
	    }
	}
	
	/**
	 * Create the query to the text search
	 * 
	 * @params
	 * site: String with the according text we request
	 * address: location of the places we want
	 * dist: distance to the place
	 * lat, lng: coordinates of our position
	 * 
	 * @return
	 *  A string with the complete query
	 * */
	
	public String createSQuery(String site, String address, String dist, double lat, double lng){

		query=headS;

		if(site!=null&&address!=null){

			try{

				query+=URLEncoder.encode(site, "utf-8");
				query+="+"+URLEncoder.encode(address, "utf-8");

				query+="&sensor=true";	
				query+="&location="+lat+","+lng+"&radius="+URLEncoder.encode(dist, "utf-8");

				query+="&key="+key;

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return query;
	}
	
	/**
	 * Create the query to the radar search
	 * 
	 * @params
	 * lat, lng: coordinates of our position
	 * 
	 * @return
	 *  A string with the complete query
	 * */
	
	public String createQuery(double lat,double lng){
		
		query=head;
		
		final SharedPreferences settings = context.getSharedPreferences(PreferenceActivity.PREFS_NAME,0);
		
		double latitude=lat;
		double longitude=lng;
		
		String radius=String.valueOf(settings.getInt("radius_value", 1000));
				
		if(latitude!=0&&longitude!=0)
			query+="?location="+latitude+","+longitude+"";
		if(radius!=null)
			query+="&radius="+radius+"&sensor=false";
		if(setFilters(settings)!=null){
			try {
				String filters=URLEncoder.encode(setFilters(settings),"utf-8");
				query+="&types="+filters+"";
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		query+="&key="+key;
		
		return query;
	}

	@Override
	protected JSONArray doInBackground(Void... params) {
		// TODO Auto-generated method stub
		return query();
	}

	/**
	 * Depends on the mode this method parse,mark and save the places we're scanning 
	 * or calls and activity to show us where are the place we request on the text search
	 * 
	 * @params
	 *  result: a JSONArray with the response of the Places's server
	 * */
	
	@Override
	protected void onPostExecute(JSONArray result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if(result!=null){
			if(mode==MODE_SCAN){
				PlacesParser PLACES=new PlacesParser();
				List<PlacesContainer> list=PLACES.JSONParser(result);
				if(list!=null){
					markPlaces(list);

					Database helper=new Database(context,Database.DB_NAME,null,Database.DB_VERSION);
					SQLiteDatabase db=null;
					helper.addAll(db,list);

					MapsActivity.details=new PlacesDetails(context);

					MapsActivity.details.execute(list);
				}
			}
			if(mode==MODE_TEXT){
				PlacesParser PLACES=new PlacesParser();
				List<PlacesContainer> list=PLACES.JSONParser(result);
				if(list!=null){
					markRoute(list);
				}
			}
		}
	}

	public void setMap(GoogleMap map,Context ctx) {
		this.map = map;
		context=ctx;
	}
	
	/**
	 * Get the filters from the shared preferences and returns it to create the query to radar search
	 * 
	 * @params
	 *  settings: the shared preferences
	 *  
	 * @return
	 *  A string that corresponds to the filters part of the query
	 * */
	
	public String setFilters(SharedPreferences settings){
		
		final boolean [] check_filter = new boolean [9];
		
		String filters="";
		
		String filt[]={"restaurant","movie_theater","shopping_mall","spa","theater","night_club","cafe","park"};
		  
		  //READ PREFERENCES
		  // getBoolean("variable", defaultValue);
		  check_filter[0] = settings.getBoolean("filter_restaurant", true);
		  check_filter[1] = settings.getBoolean("filter_cinema", true);
		  check_filter[2] = settings.getBoolean("filter_shopping", true);
		  check_filter[3] = settings.getBoolean("filter_spa", true);
		  check_filter[4] = settings.getBoolean("filter_theater", true);
		  check_filter[5] = settings.getBoolean("filter_disco", true);
		  check_filter[6] = settings.getBoolean("filter_cafe", true);
		  check_filter[7] = settings.getBoolean("filter_park", true);
		  check_filter[8]=false;
		
		  for(int i=0;i<check_filter.length;i++){
			  if(check_filter[i]){
				  filters+=filt[i];
				  
				  if(i!=check_filter.length-1&&check_filter[i+1])
					  filters+="|";
			  }
		  }
		  
		  if(!filters.equals(""))
			  return filters;
		  else
			  return null;
	}

	@Override
	protected void onProgressUpdate(String... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}
	
	/**
	 * Mark the places on the map
	 * 
	 * @params
	 *  places: a list of places to be mark
	 * */
	
	public void markPlaces(List<PlacesContainer> places){
		
		if(!places.isEmpty()){
			double lat=0;
			double lng=0;
			
			for(int i=0;i<places.size();i++){
				
				MarkerOptions options=new MarkerOptions();
				lat=places.get(i).geometry.location.lat;
				lng=places.get(i).geometry.location.lng;
				
				options.position(new LatLng(lat,lng));
				options.title(places.get(i).getName());
				options.snippet(places.get(i).getFormatted_address());
				
				if(lat!=0&&lng!=0)
					map.addMarker(options);
			}
		}
	}
	
	/**
	 * Creates the intent to show us the place and route to the text search requested place
	 * 
	 * @params
	 *  places: the information of the places that match with the description
	 * */

	public void markRoute(List<PlacesContainer> places){

		if(!places.isEmpty()){
			double lat=0;
			double lng=0;


			lat=places.get(0).geometry.location.lat;
			lng=places.get(0).geometry.location.lng;
			String f_address=places.get(0).getFormatted_address();

			Bundle extras = new Bundle();
			extras.putDouble("lat",lat);
			extras.putDouble("lng",lng);
			extras.putString("address", f_address);

			Intent intent=new Intent(this.context, RouteTracer.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.stopService(new Intent(context,LocLisServ.class));
			intent.putExtras(extras);
			context.startActivity(intent);
		}
	}
}
