package com.appMovil.tikitown;

import android.graphics.Bitmap;

public class Photo{
	
	public Photo(){
		this.name=null;
		this.image=null;
		this.maxHeight=null;
		this.maxWidth=null;
		this.reference=null;
	}
	
	public String name;

	public String reference;

	public String maxWidth;

	public String maxHeight;

	public Bitmap image;
	
	/*Sets and gets to create a new Photo Object*/

	/**
	 * Function to get the name of the Object
	 * 
	 * @return
	 *  name
	 * */
	public String getName() {
		return name;
	}
	
	/**
	 * Function to set the name of the Object
	 * 
	 * @params
	 *  name
	 *  
	 * */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Function to get the reference from the Object
	 * 
	 * @return
	 *  reference
	 * */
	public String getReference() {
		return reference;
	}

	/**
	 * Function to set the reference to the Object
	 * 
	 * @params
	 *  reference
	 * */
	public void setReference(String reference) {
		this.reference = reference;
	}

	/**
	 * Function to get the maximum width
	 * 
	 * @return
	 *  maxWidth
	 * */
	public String getMaxWidth() {
		return maxWidth;
	}

	/**
	 * Function to set the maximum width
	 * 
	 * @params
	 *  maxWidth
	 * */
	public void setMaxWidth(String maxWidth) {
		this.maxWidth = maxWidth;
	}

	/**
	 * Function to get the maximum height
	 * 
	 * @return
	 *  maxHeight
	 * */
	public String getMaxHeight() {
		return maxHeight;
	}

	/**
	 * Function to set the maximum height
	 * 
	 * @params
	 * maxHeight
	 * */
	public void setMaxHeight(String maxHeight) {
		this.maxHeight = maxHeight;
	}

	/**
	 * Function to get the image
	 * 
	 * @return
	 * image
	 * */
	public Bitmap getImage() {
		return image;
	}

	/**
	 * Function to set the image
	 * 
	 * @params
	 * image
	 * */
	public void setImage(Bitmap image) {
		this.image = image;
	}

}