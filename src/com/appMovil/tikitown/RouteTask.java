package com.appMovil.tikitown;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import android.graphics.Color;
import android.os.AsyncTask;

/**
 * AsyncTask that manage the route's request to Places's server
 * 
 * @variables
 *  polylines: set of points that indicates the route to follow
 *  map: GoogleMap Object reference
 *  
 * */

public class RouteTask extends AsyncTask<String,String,JSONArray>{

	List<LatLng> polylines;
	GoogleMap map;
	
	
	/**
	 * Request the route to Places's server through a HTTP get method
	 * 
	 * @params
	 *  query: contains the url of the Places's server's service
	 *  
	 * @return
	 *  a JSONarray with the points of the route to follow
	 *  
	 * */
	
	@Override
	protected JSONArray doInBackground(String... query) {

		DefaultHttpClient client=new DefaultHttpClient();
		
		try{
			HttpGet get = new HttpGet(query[0]);
			HttpResponse httpResponse = client.execute(get);
			int status = httpResponse.getStatusLine().getStatusCode();
			if(status == 200){
				HttpEntity e = httpResponse.getEntity();
				String data = EntityUtils.toString(e);
				JSONObject response= new JSONObject(data);
				String responseStatus = response.get("status").toString();
				if(responseStatus.equals("OK")){
					JSONArray json = response.getJSONArray("routes");
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
	
	@Override
	protected void onPostExecute(JSONArray result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if(result!=null){
			parser(result);
			tracer();
		}
	}
	
	
	/**
	 * Parse the JSONArray to polyline object
	 * 
	 * @params
	 *  routes: contains the points requested to Places's server
	 *  
	 * */
	
	public void parser(JSONArray routes){
		
		polylines=new ArrayList<LatLng>();
		
		try {
			JSONObject o= routes.getJSONObject(0);
			JSONArray legs=o.getJSONArray("legs");
			
			for(int i=0;i<legs.length();i++){
				JSONObject leg=legs.getJSONObject(i);
				JSONArray steps=leg.getJSONArray("steps");
				
				for(int j=0;j<steps.length();j++){
					String polyline=steps.getJSONObject(j).getJSONObject("polyline").getString("points");
					polylines.addAll(decoder(polyline));
				}
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Trace the route to the place we requested to Places's server
	 * */
	
	public void tracer(){
		
		RouteTracer.options=new PolylineOptions();

		RouteTracer.options.addAll(polylines)
				.visible(true)
				.color(Color.BLUE)
				.width(5);
		Polyline pop=map.addPolyline(RouteTracer.options);	
	}
	
	/**
	 * Method that decode the polyline's String that we receive from the response
	 * The algorithm follow the revert proccess that Google has published on his developer's web
	 * Link to the algorithm:
	 * https://developers.google.com/maps/documentation/utilities/polylinealgorithm?hl=es
	 * 
	 * @params
	 *  encode: String to decode
	 *  
	 * @return
	 * A list of LatLng with the points decoded from the polyline's String
	 * */
	
	public List<LatLng> decoder(String encode){
		List<LatLng> poly=new ArrayList<LatLng>();
		int i=0, lat=0, lng=0;
		
		while(i<encode.length()){
			int binary=0, des=0, result=0, aux=0;
			do{
				binary=encode.charAt(i) - 63; //Tomo un caracter y le resto 63
				aux = binary & 0x1f; //Operac—n de AND entre el caracter y 0001 1111
				aux <<= des; //Desplazo 'des' bits para obtener la potencia de 10 correspondiente al nœmero
				result |=aux; //Realizo un operaci—n OR entre los bits
				des+=5;//Sumo 5 al desplazamiento para obtener el siguiente orden de caracter
				i++;
			}while(binary>=0x20); //Repito el proceso hasta que el numero binario sea mayor o igual a 0010 0000
			int decenaLat=0;
			if((result & 1) != 0) //Compruebo si el nœmero necesita el complemento A2 o no
				decenaLat=~(result >> 1);
			else
				decenaLat=(result >> 1);
			lat+=decenaLat; //Le sumo a la latitud el numero en la potencia de base 10 correspondiente
			
			//Repito el proceso para obtener la longitud
			
			des=0;
			result=0;
			do{
				binary=encode.charAt(i) - 63; 
				aux = binary & 0x1f; 
				aux <<= des; 
				result |=aux; 
				des+=5;
				i++;
			}while(binary>=0x20); 
			int decenaLng=0;
			if((result & 1) != 0) 
				decenaLng=~(result >> 1);
			else
				decenaLng=(result >> 1);
			lng+=decenaLng;
			
			//Por œltimo, paso los nœmeros obtenidos a double y al orden de magnitud correspondiente
			
			double latitude=((double)lat/1E5);  
			double longitude=((double)lng/1E5);
			LatLng position= new LatLng(latitude,longitude);
			
			poly.add(position);
		}
		return poly;
	}
}
