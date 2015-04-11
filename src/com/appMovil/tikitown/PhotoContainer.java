package com.appMovil.tikitown;

import java.util.LinkedList;
import java.util.List;

public class PhotoContainer {
	
	public static List<Photo> photos=new LinkedList<Photo>();
	/**
	 * Function to get photo by name
	 * 
	 * @params
	 * name
	 * @return
	 * null
	 * */
	public static Photo getPhoto(String name){
		
		//In order to get photo by name
		for(int i=0;i<photos.size();i++){
			if(photos.get(i).getName().equals(name))
				return photos.get(i);
		}
		return null;
	}

}
