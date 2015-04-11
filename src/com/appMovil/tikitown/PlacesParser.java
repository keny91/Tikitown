package com.appMovil.tikitown;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class is responsible of parse the JSON array received from Places's server
 * */

public class PlacesParser{
	
	/**
	 * This method parse the JSONArray according to the JSON structure shown on Google API's website
	 * https://developers.google.com/places/documentation/search
	 * 
	 * @params
	 *  array: contains the information of the places
	 *  
	 * @return
	 *  A list of PlacesContainers with the parsed information
	 * */

	public List<PlacesContainer> JSONParser(JSONArray array){
		
		List<PlacesContainer> result=new LinkedList<PlacesContainer>();
		
		if(array!=null){
			try {
				for(int i=0;i<array.length();i++){
					JSONObject o=new JSONObject();
					PlacesContainer place=new PlacesContainer();
					o=array.getJSONObject(i);
					
					if(o.has("id"))
						place.setId(o.getString("id"));
					
					if(o.has("name")){
						String name=o.getString("name");
						name=name.replaceAll("'", "");
						place.setName(name);
						}
					
					if(o.has("formatted_address")){
						String address=o.getString("formatted_address");
						address=address.replaceAll("'", "");
						place.setFormatted_address(address);
					}	
					
					if(o.has("reference"))
						place.setReference(o.getString("reference"));

					//place.setIcon(icon);
					
					if(o.has("types")){
						JSONArray types=o.getJSONArray("types");
						place.types=new String[types.length()];

						for(int j=0;j<types.length();j++){
							place.types[j]=types.getString(j);
						}
					}
						
					if(o.has("photos")){
						
						JSONArray photos=o.getJSONArray("photos");
						JSONObject photo=photos.getJSONObject(0);
						
						Photo picture=new Photo();
						picture.setName(o.getString("name"));
						if(!photo.isNull("photo_reference"))
							picture.setReference(photos.getJSONObject(0).getString("photo_reference"));
						if(!photo.isNull("width"))
							picture.setMaxWidth(photos.getJSONObject(0).getString("width"));
						if(!photo.isNull("height"))
							picture.setMaxHeight(photos.getJSONObject(0).getString("height"));
						
						PhotoContainer.photos.add(picture);
					}
					
					place.geometry.location.lat=o.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
					place.geometry.location.lng=o.getJSONObject("geometry").getJSONObject("location").getDouble("lng");
					
					result.add(place);
				}
				return result;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}else{
			return null;
		}
	}

}
