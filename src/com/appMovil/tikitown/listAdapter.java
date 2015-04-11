package com.appMovil.tikitown;

import java.io.*;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.*;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

/**
 * Class that creates the dinamic list of places
 * 
 * @variables
 *  lista: object with the places of the list
 *  context: context of the application
 *  inflater: object that "inflate" the UI
 *  
 * */

public class listAdapter extends BaseAdapter implements ListAdapter{
	
	private List<PlacesContainer> lista;
	
	private static Context context;
	
	private LayoutInflater inflater;

	public listAdapter(List<PlacesContainer> lista, Context inflater) {
		this.lista = lista;
		this.inflater = LayoutInflater.from(inflater);
		context=inflater;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lista.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return lista.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		long id=1L;
		id=Long.parseLong(lista.get(arg0).getId());
		return id;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		listView list = null;
		 
	      if (arg1 == null){
	         arg1 = inflater.inflate(R.layout.database, null);
	 
	         list = new listView();
	         list.image = (ImageView) arg1.findViewById(R.id.imageView);
	         list.tittle = (TextView) arg1.findViewById(R.id.name);
	         list.des = (TextView) arg1.findViewById(R.id.description);
	        
	         arg1.setTag(list);
	         
	         
	      } else
	         list = (listView) arg1.getTag();
	 
	      PlacesContainer registro = (PlacesContainer) getItem(arg0);
	      
	      list.tittle.setText(registro.getName());
	      list.des.setText(registro.types[0]);
	      
	      File imgFile = new File(registro.getImage_path());
	      
	      if(imgFile.exists()){

	          Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

	          list.image.setImageBitmap(myBitmap);

	      }else{
	    	  int icon=setIcon(registro.types[0]);
	    	  list.image.setImageResource(icon);
	      }
	      
	      arg1.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					
					Intent i=new Intent(listAdapter.context,DataShow.class);
					
					Bundle b=new Bundle();
					TextView id=(TextView) v.findViewById(R.id.name);
					b.putString("name", (String) id.getText());
					i.putExtras(b);
					
					listAdapter.context.startActivity(i);
				}
	      });
	      
	      return arg1;
	}
	
	/**
	 * Class that contains the objects of the list's single item 
	 * */
	
	class listView{
		
		ImageView image;
		TextView tittle;
		TextView des;
	}
	
	/**
	 * Set the icon of the imageView in case of the place's got no photo
	 * 
	 * @params
	 *  type: String with the type of the place
	 *  
	 * @return
	 *  int which indicates the icon assigned
	 * */
	
	public int setIcon(String type){
		
		if(type.equals("night_club"))
			return R.drawable.disco;
		else if(type.equals("restaurant")||type.equals("food"))
			return R.drawable.restaurant;
		else if(type.equals("movie_theater"))
			return R.drawable.cinema;
		else if(type.equals("theater"))
			return R.drawable.theatre;
		else if(type.equals("shopping_mall")||type.equals("shop")||type.equals("store"))
			return R.drawable.shopping_mall;
		else if(type.equals("park"))
			return R.drawable.park;
		else if(type.equals("cafe")||type.equals("bar")||type.equals("bakery"))
			return R.drawable.cafe;
		else if(type.equals("spa"))
			return R.drawable.spa;
		else
			return 0;
	}
	

}
