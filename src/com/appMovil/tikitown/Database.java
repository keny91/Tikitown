package com.appMovil.tikitown;


import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * A class with methods to work and connect the application to his database
 * 
 * @variables
 *  TB_NAME: String with the table's name
 *  DB_NAME. String with the database' name
 *  DB_VERSION: int that indicate the database's version
 *  ctx: context of the application
 *  sql: String with the create SQL sentence
 *  size: int that indicate the current size of the database
 * 
 * */

public class Database extends SQLiteOpenHelper {
	
	public static final String TB_NAME="Sities";
	
	public static final String DB_NAME="SitiesDB";

	public static final int DB_VERSION=1;
	
	Context ctx;
	
	public String sql="CREATE TABLE Sities (_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
			"name TEXT, description TEXT, formatted_address TEXT, latitude DOUBLE, longitude DOUBLE,"+ 
			" type TEXT, image TEXT, favorites TEXT)";
	
	public int size=0;
	
	public Database(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
		this.ctx=context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}
	
	public void insertRegister(SQLiteDatabase db, PlacesContainer data, boolean fav){
		
		db=getWritableDatabase();
		
		db.execSQL("INSERT INTO Sities (name,description,formatted_address,latitude,longitude,type,image, favorites)"
				+ " VALUES ('"+data.getName()+"','"+data.getDescription()+"','"+data.getFormatted_address()+"','"
				+data.getLatitude()+"','"+data.getLongitude()+"','"+data.types[0]+"','"+data.getImage_path()+"','"
				+fav+"')");
		
		size++;
		
		db.close();
	}
	
	/**
	 * Function to insert a new register in the database if it is not exist
	 * 
	 * @params
	 *  db: SQL database object
	 *  data: object to insert
	 *  fav: set the object like favourite or not
	 *  
	 * */
	
	public void insertIfNotExist(SQLiteDatabase db, PlacesContainer data, boolean fav){
		
		db=getWritableDatabase();
		
		Cursor cursorDB=null;
		
		cursorDB=db.rawQuery("SELECT * FROM Sities WHERE latitude LIKE '"+data.getLatitude()
				+"' AND longitude LIKE '"+data.getLongitude()+"'",null);
				
		boolean isFull=cursorDB.moveToFirst();
		
		if(!isFull){
			db.close();
			insertRegister(db,data,fav);
		}
	}
	
	/**
	 * Return the registers that match with the introduced description
	 * 
	 * @params
	 *  db: SQL database object
	 *  code: String with the match column
	 *  value: String with the value of the register to find
	 *   
	 * @return 
	 *  A list of places that match with the value of the match column
	 * */
	
	public List<PlacesContainer> readDB(SQLiteDatabase db,String code, String value){
		
		List<PlacesContainer> result=new LinkedList<PlacesContainer>();
		
		db=getReadableDatabase();
		Cursor cursorDB=null;
		
		if(code==null)
			cursorDB=db.rawQuery("SELECT * FROM Sities ORDER BY name ASC",null);
		else
			cursorDB=db.rawQuery("SELECT * FROM Sities WHERE "+code+" LIKE '"+value+"' ORDER BY name ASC",null);
		
		
		boolean isFull=cursorDB.moveToFirst();
		
		if(isFull){

			do{
				PlacesContainer aux=new PlacesContainer();
				
				aux.setId(cursorDB.getString(0));
				aux.setName(cursorDB.getString(1));
				aux.setDescription(cursorDB.getString(2));
				aux.setFormatted_address(cursorDB.getString(3));
				aux.setLocation(cursorDB.getDouble(4),cursorDB.getDouble(5));
				aux.setTypes(cursorDB.getString(6));
				aux.setImage_path(cursorDB.getString(7));
				aux.setFav(Boolean.valueOf(cursorDB.getString(8)));

				result.add(aux);

			}while(cursorDB.moveToNext());

			db.close();
		}
		
		return result;
	}
	
	/**
	 * Delete the whole database
	 * 
	 * @params
	 *  db: SQL database object
	 * */
	
	public void deleteDB(SQLiteDatabase db){
		
		db=getWritableDatabase();
		db.execSQL("DROP TABLE Sities");
		db.execSQL(sql);
		
		size=0;
		
		db.close();
	}
	
	/**
	 * Delete a register that match with the description
	 * 
	 * @params
	 *  db: SQL Database object
	 *  code: String with the match column
	 *  value: String with the value of the register to find
	 *  
	 * */
	
	public void deleteRegister(SQLiteDatabase db, String code, String value){
		
		db=getWritableDatabase();
		db.execSQL("DELETE FROM Sities WHERE "+code+" LIKE '"+value+"'");
		size--;
		db.close();
		
	}
	
	/**
	 * Edit the register that match with the description
	 * 
	 * @params
	 *  db: SQL Database object
	 *  columns: String array with the columns to update
	 *  values: String array with the values of the columns to update
	 *  code: String array with the columns to look
	 *  value: String array with the values of the columns to look
	 * */
	
	public void editRegister(SQLiteDatabase db,String[] columns, String[] values, String[] code, String[] value){
		
		db=getWritableDatabase();
		
		String statement="UPDATE Sities SET ";
		
		for(int i=0;i<columns.length;i++){
			statement+=""+columns[i]+"='"+values[i]+"'";
			if(i<columns.length-1)
				statement+=",";
		}
		
		statement+=" WHERE ";
		
		for(int i=0;i<code.length;i++){
			statement+=""+code[i]+"='"+value[i]+"'";
			if(i<code.length-1)
				statement+=" AND ";
		}
		
		db.execSQL(statement);
		
		db.close();
		
	}
	
	/**
	 * Add a list of registers to the database if any of them already exist
	 * 
	 * @params
	 *  db: SQL Database object
	 *  list: list of registers to add
	 * */
	
	public void addAll(SQLiteDatabase db,List<PlacesContainer> list){
		
		for(int i=0;i<list.size();i++){
			insertIfNotExist(db,list.get(i),false);
		}
	}
	
	/**
	 * Look for the places that are close to the position given
	 * 
	 * @params
	 *  db:SQL Database Object
	 *  lat, lng: doubles with the latitude and longitude respectively of the position
	 *  
	 * @return list of places that are close sorted alphabetically by name
	 * */
	
	public List<PlacesContainer> getNearPlaces(SQLiteDatabase db, double lat, double lng){
		
		List<PlacesContainer> result=new LinkedList<PlacesContainer>();
		
		db=getReadableDatabase();
		
		final SharedPreferences settings = ctx.getSharedPreferences(PreferenceActivity.PREFS_NAME,0);
		
		int radius=settings.getInt("radius_value", 1000);
		
		Cursor cursorDB=null;
		
		double radius_degrees=(radius*360)/(2*Math.PI*6371000);
		
		double lat_range[]=new double[2];
		double lng_range[]=new double[2];

		lat_range[0]=lat-radius_degrees;
		lat_range[1]=lat+radius_degrees;

		lng_range[0]=lng-radius_degrees;
		lng_range[1]=lng+radius_degrees;
		
		String query="SELECT * FROM Sities WHERE "
				+ "(latitude BETWEEN "+String.valueOf(lat_range[0])+" AND "+String.valueOf(lat_range[1])+") AND"
				+ "(longitude BETWEEN '"+String.valueOf(lng_range[0])+"' AND '"+String.valueOf(lng_range[1])+"')"
				+ " AND name <> 'null' "
				+ "ORDER BY name ASC";

		
		cursorDB=db.rawQuery(query,null);
		
		boolean isFull=cursorDB.moveToFirst();
		
		if(isFull){

			do{
				PlacesContainer aux=new PlacesContainer();
				
				aux.setId(cursorDB.getString(0));
				aux.setName(cursorDB.getString(1));
				aux.setDescription(cursorDB.getString(2));
				aux.setFormatted_address(cursorDB.getString(3));
				aux.setLocation(cursorDB.getDouble(4),cursorDB.getDouble(5));
				aux.setTypes(cursorDB.getString(6));
				aux.setImage_path(cursorDB.getString(7));
				aux.setFav(Boolean.valueOf(cursorDB.getString(8)));

				result.add(aux);

			}while(cursorDB.moveToNext());

			db.close();
		}
		
		return result;
	}
	
	/**
	 * Check if a register need to be updated
	 * 
	 * @params
	 *  db: SQL Database object
	 *  data: object with the details of the register to update
	 *  
	 * @return
	 *  true: if it needs to be updated
	 *  false: if it doesn't need to be update
	 *   
	 * */
	
	public boolean updateIfNeeded(SQLiteDatabase db, PlacesContainer data){

		db=getReadableDatabase();

		Cursor cursorDB=null;

		cursorDB=db.rawQuery("SELECT * FROM Sities WHERE latitude LIKE '"+data.getLatitude()
				+"' AND longitude LIKE '"+data.getLongitude()+"'",null);
		
		boolean isFull=cursorDB.moveToFirst();

		if(isFull){

			PlacesContainer aux=new PlacesContainer();

			aux.setName(cursorDB.getString(1));
			if(aux.getName().equals("null")){
				db.close();
				return true;
			}else{
				db.close();
				return false;
			}
		}else{
			db.close();
			return true;
		}
	}
	
	/**
	 * Delete the not favorite places that are away from the position given
	 * 
	 * @params
	 *  db: SQL Database object
	 *  lat, lng: doubles with the latitude and longitude respectively of the position
	 *  
	 * */
	
	public void deleteFarPlaces(SQLiteDatabase db, double lat, double lng){
		
		db=getReadableDatabase();

		final SharedPreferences settings = ctx.getSharedPreferences(PreferenceActivity.PREFS_NAME,0);

		int radius=settings.getInt("radius_value", 1000);

		double radius_degrees=(radius*360)/(2*Math.PI*6371000);

		double lat_range[]=new double[2];
		double lng_range[]=new double[2];

		lat_range[0]=lat-radius_degrees;
		lat_range[1]=lat+radius_degrees;

		lng_range[0]=lng-radius_degrees;
		lng_range[1]=lng+radius_degrees;


		String query="DELETE FROM Sities WHERE "
				+ "(latitude < '"+String.valueOf(lat_range[0])+"' OR latitude > '"+String.valueOf(lat_range[1])+"') AND"
				+ "(longitude < '"+String.valueOf(lng_range[0])+"' OR longitude > '"+String.valueOf(lng_range[1])+"') AND"
				+ "favorites <> 'true'";

		db.execSQL(query);
	}
}
