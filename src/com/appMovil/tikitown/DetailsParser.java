package com.appMovil.tikitown;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class responsible of places's details's response's query's parsing
 * */

public class DetailsParser {
	
	/**
	 * Parses the JSON Object that receives from the Places's response
	 * 
	 * @params
	 *  o: JSONObject that is returned by Places's server
	 *  
	 * @return
	 *  a PlacesContainer object with the information obtains from the JSON Object 
	 * */

	public PlacesContainer JSONParser(JSONObject o){

		PlacesContainer place=new PlacesContainer();

		if(o!=null){
			try {

				if(o.has("id"))
					place.setId(o.getString("id"));

				if(o.has("name")){
					String name=o.getString("name");
					name=name.replaceAll("'", "");
					place.setName(name);
					}

				if(o.has("formatted_address")){
					String address=o.getString("formatted_address");
					address=address.replace("'", "");
					place.setFormatted_address(address);
				}		
				
				if(o.has("reference"))
					place.setReference(o.getString("reference"));

				if(o.has("reviews")){
					JSONArray reviews=o.getJSONArray("reviews");
					String des="";
					for(int j=0;j<reviews.length();j++){
						JSONObject rev=reviews.getJSONObject(j);
						des+="Author: "+rev.getString("author_name")+"\n";
						des+=rev.getString("text")+"\n";
					}
					place.setDescription(des);
				}
				
				//place.setIcon(icon);

				if(o.has("types")){
					JSONArray types=o.getJSONArray("types");
					place.types=new String[types.length()];

					for(int j=0;j<types.length();j++){
						place.types[j]=types.getString(j);
					}
				}

				
				//Check if there're photos information to save them and request it later
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
				return place;

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
