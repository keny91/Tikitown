package com.appMovil.tikitown;

import java.io.Serializable;

public class PlacesContainer implements Serializable{

	
	private static final long serialVersionUID = 1L;

	public String id;
	
	public String name;
	
	public String reference;
	
	public String icon;
	
	public Geometry geometry;

	public String formatted_address;
	
	public String formatted_phone;
	
	public String image_path;
	
	public String description;
	
	public boolean fav;
	
	public static class Geometry implements Serializable{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public Location location;
	}
	
	public static class Location implements Serializable{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public double lat;
		
		public double lng;
	}
	
	public String types[];

	/**
	 * Function to establish the features for a place
	 * 
	 * @params
	 * id, name, reference, icon, geometry, formatted_address, formatted_phone
	 * 
	 * 
	 * */
	public PlacesContainer(String id, String name, String reference,
			String icon, Geometry geometry,
			String formatted_address, String formatted_phone) {
		super();
		this.id = id;
		this.name = name;
		this.reference = reference;
		this.icon = icon;
		this.geometry = geometry;
		this.formatted_address = formatted_address;
		this.formatted_phone = formatted_phone;
	}
	
	
	/**
	 * Function to create an empty container for a place
	 * 
	 * 
	 * */
	public PlacesContainer(){
		this.id = null;
		this.name = null;
		this.reference = null;
		this.icon = null;
		this.geometry = new Geometry();
		this.geometry.location=new Location();
		this.formatted_address = null;
		this.formatted_phone = null;
		this.types=new String[1];
	}
	
	/*Gets and sets when a PlacesContainer object is created*/

	/**
	 * Function to get the Id
	 * 
	 * @return
	 * id
	 * 
	 * 
	 * */
	public String getId() {
		return id;
	}

	/**
	 * Function to set the Id
	 * 
	 * @params
	 * id
	 * 
	 * 
	 * */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Function to get the name
	 * 
	 * @return
	 * name
	 * */
	public String getName() {
		return name;
	}


	/**
	 * Function to set the name
	 * @params
	 * name
	 * */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Function to get the reference
	 * @return
	 * reference
	 * */
	public String getReference() {
		return reference;
	}

	/**
	 * Function to set the reference
	 * @params
	 * reference
	 * */
	public void setReference(String reference) {
		this.reference = reference;
	}

	/**
	 * Function to get the icon
	 * @return
	 * icon
	 * */
	public String getIcon() {
		return icon;
	}

	/**
	 * Function to set the icon
	 * @params
	 * icon
	 * */
	public void setIcon(String icon) {
		this.icon = icon;
	}

	/**
	 * Function to get the geometry
	 * @return
	 * geometry
	 * */
	public Geometry getGeometry() {
		return geometry;
	}


	/**
	 * Function to set the geometry
	 * @params
	 * geometry
	 * */
	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

	/**
	 * Function to get the address
	 * @return
	 * formatted_address
	 * */
	public String getFormatted_address() {
		return formatted_address;
	}

	/**
	 * Function to set the address
	 * @params
	 * formatted_address
	 * */
	public void setFormatted_address(String formatted_address) {
		this.formatted_address = formatted_address;
	}

	/**
	 * Function to get the phone
	 * @return
	 * formatted_phone
	 * */
	public String getFormatted_phone() {
		return formatted_phone;
	}

	/**
	 * Function to set the phone
	 * @params
	 * formatted_phone
	 * */
	public void setFormatted_phone(String formatted_phone) {
		this.formatted_phone = formatted_phone;
	}

	/**
	 * Function to get the image
	 * @return
	 * image_path
	 * */
	public String getImage_path() {
		return image_path;
	}


	/**
	 * Function to set the image
	 * @params
	 * image_path
	 * */
	public void setImage_path(String image_path) {
		this.image_path = image_path;
	}

	/**
	 * Function to get the description
	 * @return
	 * description
	 * */
	public String getDescription() {
		return description;
	}

	/**
	 * Function to set the description
	 * @params
	 * description
	 * */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Function to set the location
	 * @params
	 * lat, lng
	 * */
	public void setLocation(double lat,double lng){
		this.geometry.location.lat=lat;
		this.geometry.location.lng=lng;
	}
	
	/**
	 * Function to set the type
	 * @params
	 * type
	 * */
	public void setTypes(String type){
		this.types[0]=type;
	}
	
	/**
	 * Function to get the latitude
	 * @return
	 * geometry.location.lat
	 * */
	public double getLatitude(){
		return this.geometry.location.lat;
	}
	
	/**
	 * Function to get the longitude
	 * @return
	 * geometry.location.lng
	 * */
	public double getLongitude(){
		return this.geometry.location.lng;
	}

	/**
	 * Function to know if the place is inside the favorite list
	 * @return
	 * fav
	 * */
	public boolean isFav() {
		return fav;
	}


	/**
	 * Function to set as a favorite place
	 * @params
	 * fav
	 * */
	public void setFav(boolean fav) {
		this.fav = fav;
	}
}
