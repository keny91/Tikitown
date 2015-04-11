package com.appMovil.tikitown;

import java.util.LinkedList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.ListActivity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Activity where the places's list are displayed
 * 
 * @variables
 *  mode: int with the mode of the activity
 *  lat, lng: doubles that indicates the current position
 *  MODE_FAV: mode of the favourites list
 *  MODE_MAP: mode of the current nearby places
 *  
 * */

public class DataActivity extends ListActivity{
	
	private int mode;
	
	private double lat;
	
	private double lng;
	
	public static final int MODE_FAV=1;
	
	public static final int MODE_MAP=2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_data);
		
		Bundle b=getIntent().getExtras();
		
		mode=b.getInt("mode");
		
		lat=b.getDouble("lat");
		
		lng=b.getDouble("lng");
		
		
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.data, menu);
		return true;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		refreshList();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		
		switch(item.getItemId()){
		
		case R.id.add_setting:
			Intent i=new Intent(this,AddingNewPlace.class);
			startActivityForResult(i,Activity.RESULT_OK);
			return true;
		case R.id.filter_settings:
			FragmentManager manager =getFragmentManager();
			FiltersDialog MyDialog = new FiltersDialog();
			MyDialog.show(manager,"myDialog");
			return true;
		case R.id.delete_settings:
			delete();
			return true;
		case R.id.refresh:
			refreshList();
			return true;
		default:
			return false;
		}
	}
	
	/**
	 * Refresh the list of the places displayed depending of which mode is being used
	 * */
	
	public void refreshList(){
		
		List<PlacesContainer> lista=new LinkedList<PlacesContainer>();
				
		SQLiteDatabase db=null;

		Database dbHelper=new Database(this,Database.DB_NAME,null,Database.DB_VERSION);
		
		if(mode==MODE_FAV){

			lista=dbHelper.readDB(db, "favorites", "true");
		
		}
		if(mode==MODE_MAP){
			
			lista=dbHelper.getNearPlaces(db,lat,lng);
		}
		
		listAdapter adapter=new listAdapter(lista,this);

		setListAdapter(adapter);
		
	}
	
	/**
	 * Option to delete the whole information on the database
	 * */
	
	public void delete(){
		
		Database dbHelper=new Database(getApplicationContext(),Database.DB_NAME,null,Database.DB_VERSION);

		SQLiteDatabase db=null;

		dbHelper.deleteDB(db);
		
		String msg = "Database deleted"; 
    	int duration = Toast.LENGTH_SHORT; 
    	Toast toast = Toast.makeText(getApplicationContext(), msg, duration); 
    	toast.show();
		refreshList();
		
	}
}
