package com.appMovil.tikitown;

import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

/**
 * Class responsible of ask Places's server the location of a place according to his address
 * 
 * @variables
 *  name: String with the name of the place
 *  MODE_UPDATE: mode to update the information of the place
 *  MODE_MARK: mode to mark the place on the map
 *  mode: int to choose the mode
 *  map: GoogleMap reference object
 *  ctx: context of the application
 *  db: SQL Database Object
 *  parser: object to parse the given information from Places's server
 *   
 * */

public class GEOCodeTask extends AsyncTask<String,String,JSONArray>{

	public String[] name=new String[1];
	
	public static final int MODE_UPDATE=1;
	
	public static final int MODE_MARK=2;
	
	public int mode;
	
	public GoogleMap map;
	
	public Context ctx;
	
	public SQLiteDatabase db;
	
	public PlacesParser places=new PlacesParser();
	
	/**
	 * Do the "get" http query to place's server
	 * 
	 * @params
	 *  query: contains the url of the Places's server's service and the name of the place if it's needed
	 * */
	
	@Override
	protected JSONArray doInBackground(String... query) {
		// TODO Auto-generated method stub

			DefaultHttpClient client=new DefaultHttpClient();
			
			if(query.length>1)
				name[0]=query[1];
			
			try{
				HttpGet get = new HttpGet(query[0]);
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
	 * Parse the JSONArray obtained from Places's server
	 * Depends on the mode, update a register with the information or mark the place location on the map 
	 * 
	 * @params
	 *  result: the JSONArray that the server sent
	 *  
	 * */
	
	@Override
	protected void onPostExecute(JSONArray result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		
		if(result!=null){
			
			List<PlacesContainer> list=places.JSONParser(result);

			if(mode==MODE_UPDATE){

				Database dbhelper=new Database(ctx,Database.DB_NAME,null,Database.DB_VERSION);
				String[] columns={"formatted_address","latitude","longitude"};
				String latitude=String.valueOf(list.get(0).getLatitude());
				String longitude=String.valueOf(list.get(0).getLongitude());
				String[] values={list.get(0).formatted_address,latitude,longitude};
				String[] code={"name"};
				dbhelper.editRegister(db,columns,values,code,name);

			}else if(mode==MODE_MARK){
				
				map.clear();

				Places task=new Places();
				task.setMap(map,ctx);
				task.createQuery(list.get(0).getLatitude(), list.get(0).getLongitude());            
				task.execute();

				MarkerOptions options=new MarkerOptions();
				LatLng pos=new LatLng(list.get(0).getLatitude(),list.get(0).getLongitude());
				options.position(pos)
				.title(list.get(0).getFormatted_address());
				map.addMarker(options);
				map.animateCamera(CameraUpdateFactory.newLatLng(pos));
			}
		}
	}
}
