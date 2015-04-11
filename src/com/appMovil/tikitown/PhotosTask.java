package com.appMovil.tikitown;

import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

/**
* This AsyncTask request the photo of a place to the server
* 
* @variable
* head: a string with the header of the request
* query: a string with the complete url of the Places's server's service
*/

public class PhotosTask extends AsyncTask<Void,String,Bitmap>{
	
	public String head="https://maps.googleapis.com/maps/api/place/photo?";
	
	public String query;

	/**
	* This method generates the query according to the place's photo we need
	*
	* @params
	* name: the name of the place which we request the photo
	*
	* @return
	* true if create the query, false if not
	*/
		
	public boolean createQuery(String name){
		
		query=head;
		
		Photo photo=new Photo();
		
		photo=PhotoContainer.getPhoto(name);
		
		
		if(photo!=null){
			query+="photoreference="+photo.getReference();

			if(photo.getMaxWidth()!=null)
				query+="&maxwidth="+photo.getMaxWidth();
			else if(photo.getMaxHeight()!=null)
				query+="&maxheight="+photo.getMaxHeight();
			else
				query+="&maxwidth=720";

			query+="&key="+Places.key;

			return true;
		}else
			return false;
	}

	/**
	* Request the photo to the Places's server
	*
	* @return
	* A bitmap of the place's photo we've requested
	*/

	@Override
	protected Bitmap doInBackground(Void... params) {
		// TODO Auto-generated method stub
		
		DefaultHttpClient client=new DefaultHttpClient();
		
		try{
			HttpGet get=new HttpGet(query);
			HttpResponse response=client.execute(get);
			int status = response.getStatusLine().getStatusCode();
			if(status == 200){
				HttpEntity e = response.getEntity();
				InputStream stream=e.getContent();
				Bitmap image=BitmapFactory.decodeStream(stream);
				return image;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	* Put the image on the imageView in the details activity of that place
	*
	* @params
	*  result: a bitmap with the image of the requested place
	*/

	@Override
	protected void onPostExecute(Bitmap result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if(result!=null)
			DataShow.image.setImageBitmap(result);
	}

}
