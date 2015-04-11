package com.appMovil.tikitown;

import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

public class PlacesDetails extends AsyncTask<List<PlacesContainer>,Void,Void>{

	Context ctx;
	
	public PlacesDetails(Context ctx){
		super();
		this.ctx=ctx;
	}
	
	/**
	 * Function to set the details for a place
	 * @params
	 * places
	 * */
	@Override
	protected Void doInBackground(List<PlacesContainer>... places) {
		// TODO Auto-generated method stub
		
		SQLiteDatabase db=null;
		Database helper=new Database(ctx,Database.DB_NAME,null,Database.DB_VERSION);
		int i=0;
		
		for(i=0;i<places[0].size();i++){

			boolean isUpdateNeeded=helper.updateIfNeeded(db, places[0].get(i));
			
			if(isUpdateNeeded){
								
				String query="https://maps.googleapis.com/maps/api/place/details/json?reference="
						+places[0].get(i).getReference()+"&sensor=false&key="+Places.key;

				DetailsParser parser=new DetailsParser();

				DefaultHttpClient client= new DefaultHttpClient();

				String columns[]={"name","description","formatted_address","latitude","longitude","type","favorites"};

				String code[]={"latitude","longitude"};

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
							JSONObject json = response.getJSONObject("result");
							PlacesContainer place=parser.JSONParser(json);
							String values[]={place.getName(),place.getDescription(),place.getFormatted_address(),
									String.valueOf(place.getLatitude()),String.valueOf(place.getLongitude()),
									place.types[0],"false"};
							String value[]={String.valueOf(place.getLatitude()),String.valueOf(place.getLongitude())};
							helper.editRegister(db, columns, values, code, value);
						}
					}
				}catch(Exception e){
					System.out.println(e);
				}
			}
		}
		return null;
	}

}
