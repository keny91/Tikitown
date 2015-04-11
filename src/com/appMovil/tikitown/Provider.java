package com.appMovil.tikitown;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;


public class Provider extends ContentProvider{
	
	private static final String URI="content://com.appMovil.tikitown/Sities";
	
	public static final Uri CONTENT_URI= Uri.parse(URI);
	
	private Database sitiesDB;
	
	private static final int SITIES=1;
	private static final int SITIES_ID=2;
	private static final UriMatcher uriMatch;
	
	static{
		uriMatch = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatch.addURI("com.appMovil.tikitown","Sities",SITIES);
		uriMatch.addURI("com.appMovil.tikitown","Sities/#",SITIES_ID);
	}
	

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		
		 int delete;
		 
		 String where = selection;
		 if(uriMatch.match(uri) == SITIES_ID)
			 where = "_ID=" + uri.getLastPathSegment();

		 SQLiteDatabase db = sitiesDB.getWritableDatabase();

		 delete = db.delete(Database.TB_NAME, where, selectionArgs);

		 return delete;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		int matcher=uriMatch.match(uri);
		
		switch(matcher){
			case SITIES:
				return "vnd.android.cursor.dir/vnd.appMovil.tikitown";
			case SITIES_ID:
				return "vnd.android.cursor.dir/vnd.appMovil.tikitown";
			default:
				return null;
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues content) {
		// TODO Auto-generated method stub
			 
		long regId;

		SQLiteDatabase db = sitiesDB.getWritableDatabase();

		regId = db.insert(Database.TB_NAME, null, content);

		Uri newUri = ContentUris.withAppendedId(CONTENT_URI, regId);

		return newUri;

	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		
		sitiesDB=new Database(getContext(),Database.DB_NAME,null,Database.DB_VERSION);
		
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
			String order) {
		// TODO Auto-generated method stub
		Cursor c=null;
		
		String where=selection;
		
		if(uriMatch.match(uri)==SITIES_ID)
			where="_ID"+uri.getLastPathSegment();
		
		SQLiteDatabase db= sitiesDB.getWritableDatabase();
		
		c=db.query(Database.TB_NAME, projection, where, selectionArgs, null, null, order);
		
		return c;
	}

	@Override
	public int update(Uri uri, ContentValues content, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		int update;
		 
	    String where = selection;
	    if(uriMatch.match(uri) == SITIES_ID)
	            where = "_ID=" + uri.getLastPathSegment();
	 
	    SQLiteDatabase db = sitiesDB.getWritableDatabase();
	 
	    update = db.update(Database.TB_NAME, content, where, selectionArgs);
	 
	    return update;
	}
	
	
	public static final class Columns implements BaseColumns{

		private void Sities(){}
		
		public static final String COL_NAME="name";
		public static final String COL_DES="description";
		public static final String COL_LOC="location";
		public static final String COL_TYPE="type";
		public static final String COL_IMG="image";
	}
	
}


