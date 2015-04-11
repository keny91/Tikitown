package com.appMovil.tikitown;

import java.io.File;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

/**
 * Activity that displays the details of the place previously clicked
 * 
 * @variables
 *  helper: a Database interaction helper
 *  database: SQL Database object
 *  image: ImageView that show the photo of the current place if there're any
 *  fav: boolean that indicate if the place is favorite or not
 * */

public class DataShow extends Activity {

	private Database helper;

	private SQLiteDatabase database;
	
	public static ImageView image;
	
	public boolean fav;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.site_data);

		Intent i=getIntent();

		Bundle b=i.getExtras();

		List<PlacesContainer> list=new LinkedList<PlacesContainer>();

		String nameData=b.getString("name");

		helper=new Database(this,Database.DB_NAME,null,Database.DB_VERSION);

		database=null;

		if(nameData!=null)
			list=helper.readDB(database, "name", nameData);

		image=(ImageView) findViewById(R.id.imageView);
		
		TextView name=(TextView) findViewById(R.id.name);
		TextView des=(TextView) findViewById(R.id.description);
		TextView type=(TextView) findViewById(R.id.type);
		TextView address=(TextView) findViewById(R.id.address);
		Button go_to=(Button) findViewById(R.id.b_goto);
		Button metting=(Button) findViewById(R.id.meeting);
		Button delete=(Button) findViewById(R.id.delete);

		
		if(list.get(0)!=null){
			
			if(!(list.get(0).getName()).equals("null"))
				name.setText(list.get(0).getName());
			if(list.get(0).types!=null&&!(list.get(0).types[0]).equals("null"))
				type.setText(list.get(0).types[0]);
			if(!(list.get(0).getDescription()).equals("null"))
				des.setText(list.get(0).getDescription());
			if(!(list.get(0).getFormatted_address()).equals("null"))
				address.setText(list.get(0).getFormatted_address());
			
			fav=list.get(0).isFav();
			
			if(!fav)
				delete.setText("Add Favorites");

			File imgFile = new File(list.get(0).getImage_path());

			
			//If there're no photo, try to get it from place's repository
			if(imgFile.exists()){

				Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

				image.setImageBitmap(myBitmap);

			}else{
				PhotosTask task=new PhotosTask();
				boolean doTask=task.createQuery(list.get(0).getName());
				if(doTask)
					task.execute();
			}

			
			//If the place is on favorites table, delete them; if not, let you add it
			delete.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					TextView name=(TextView) findViewById(R.id.name);
					
					if(fav){

						helper.deleteRegister(database, "name", (String) name.getText());

						String msg = "Register deleted"; 
						int duration = Toast.LENGTH_SHORT; 
						Toast toast = Toast.makeText(getApplicationContext(), msg, duration); 
						toast.show();
					}else{
						String[] columns=new String[1];
						String[] values=new String[1];
						String[] code=new String[1];
						String[] value=new String[1];
						
						columns[0]="favorites";
						values[0]="true";
						code[0]="name";
						value[0]=name.getText().toString();
						
						helper.editRegister(database, columns, values, code, value);
					}

					finish();
				}


			});

			metting.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					String name=(String) ((TextView)findViewById(R.id.name)).getText().toString();

					List<PlacesContainer> place=helper.readDB(database, "name", name);

					Intent i=new Intent(getApplicationContext(),AddingNewMeeting.class);

					Bundle b=new Bundle();

					b.putString("address", place.get(0).getFormatted_address());
					b.putDouble("lat", place.get(0).getLatitude());
					b.putDouble("lng", place.get(0).getLongitude());

					i.putExtras(b);
					startActivity(i);
				}


			});
			
			go_to.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					String name=(String) ((TextView)findViewById(R.id.name)).getText().toString();

					List<PlacesContainer> place=helper.readDB(database, "name", name);
					
					String address=place.get(0).getFormatted_address();
					
					if(address!=null&!address.equals("")&!address.equals("null")){

						Intent i=new Intent(getApplicationContext(),RouteTracer.class);

						Bundle b=new Bundle();

						b.putString("address", place.get(0).getFormatted_address());
						b.putDouble("lat", place.get(0).getLatitude());
						b.putDouble("lng", place.get(0).getLongitude());

						i.putExtras(b);
						startActivity(i);
						
					}else{
						
						String msg = "No place recorded"; 
						int duration = Toast.LENGTH_SHORT; 
						Toast toast = Toast.makeText(getApplicationContext(), msg, duration); 
						toast.show();
					}
				}
			});
		}
	}
}
