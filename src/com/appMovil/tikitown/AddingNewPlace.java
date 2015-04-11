package com.appMovil.tikitown;

import java.io.*;
import java.net.URLEncoder;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

/**
 * Activity to add a new place to the database
 * 
 * @variables
 *  CAMERA_PIC_REQUEST: mode of the "start intent for result" to get the picture from the camera
 *  db: SQL Database Object
 *  site: PlacesContainer object where the information will be saved until it's inserted to the database
 *   
 * */

public class AddingNewPlace extends Activity
{
	 private static final int CAMERA_PIC_REQUEST = 2500;
	 
	 private static SQLiteDatabase db;
	 
	 private static PlacesContainer site;
	 
	 public void onCreate(Bundle savedInstanceState)
	 {
	    	super.onCreate(savedInstanceState);
	    	setContentView(R.layout.addingplace);
	    	
	    	site=new PlacesContainer();
	    	 	
	    	EditText editText1 = (EditText) findViewById(R.id.title);
	    	EditText editText2 = (EditText) findViewById(R.id.description);
	    	EditText editText3 = (EditText) findViewById(R.id.address);
	    	ImageView img = (ImageView) findViewById(R.id.ImgPic);
	    	Button but = (Button)findViewById(R.id.button);
	    	
	    	
	    	final Spinner tgs = (Spinner)findViewById(R.id.spinner);

	    	ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
	    	R.array.tags, android.R.layout.simple_spinner_item);

	    	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

	    	tgs.setAdapter(adapter);
	    	
	    	//Check if there're a previous state of activity
	    	if(savedInstanceState!=null){
	    		if(savedInstanceState.getString("name")!=null)
	    			editText1.setText(savedInstanceState.getString("name"));
	    		if(savedInstanceState.getString("des")!=null)
	    			editText2.setText(savedInstanceState.getString("des"));
	    		if(savedInstanceState.getString("address")!=null)
	    			editText3.setText(savedInstanceState.getString("address"));
	    		if(savedInstanceState.getString("path")!=null){
	    			File imgFile = new File(savedInstanceState.getString("path"));

	    			if(imgFile.exists()){

	    				Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

	    				img.setImageBitmap(myBitmap);

	    			}
	    		}
	    	}
	    		    	
	    	//Save the information of the place and ask Places's server for the location of the address introduced
	    	but.setOnClickListener(new OnClickListener() 
	    	{
	    	    public void onClick(View v) 
	    	    {
	    	    	
	    	    	String tag=(String) tgs.getSelectedItem();
	    	    	    		 		
    		 		EditText editText1 = (EditText) findViewById(R.id.title);
    		    	EditText editText2 = (EditText) findViewById(R.id.description);
    		    	EditText editText3 = (EditText) findViewById(R.id.address);
    		    	
    		    	String address="";
    		    	address=editText3.getText().toString();
    		    	
    		    	if(address!=null&!address.equals("")){
    		    		try {
    		    			String query="https://maps.googleapis.com/maps/api/geocode/json?";
    		    			query+="address="+URLEncoder.encode(address, "utf-8");
    		    			query+="&sensor=false";

    		    			GEOCodeTask geo=new GEOCodeTask();
    		    			geo.ctx=getApplicationContext();
    		    			geo.mode=GEOCodeTask.MODE_UPDATE;
    		    			geo.execute(query,editText1.getText().toString());
    		    		} catch (Exception e) {
    		    			// TODO Auto-generated catch block
    		    			e.printStackTrace();
    		    		}
    		    	}
    		    	
    		    	
    		    	site.setName(editText1.getText().toString());
    		    	site.setDescription(editText2.getText().toString());
    		    	site.setTypes(tag);
    		    	
    		    	Database dbhelper=new Database(getApplicationContext(),Database.DB_NAME,null,Database.DB_VERSION);
    		    	
    		    	dbhelper.insertRegister(db, site,true);
    		 		
    		 		Context context = getApplicationContext(); 
    		    	String msg = "Place added"; 
    		    	int duration = Toast.LENGTH_SHORT; 
    		    	Toast toast = Toast.makeText(context, msg, duration); 
    		    	toast.show();
    		    	
    		    	finish();
	    	    }    
	    	});
	    	

	    	editText1.setOnEditorActionListener(new OnEditorActionListener() 
	    	{
	    	    @Override
	    	    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) 
	    	    {
	    	    	if (actionId == EditorInfo.IME_ACTION_SEARCH ||
	                        actionId == EditorInfo.IME_ACTION_DONE ||
	                        event.getAction() == KeyEvent.ACTION_DOWN &&
	                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) 
	    	    	{
	     
	                            System.out.println("BUTTON PRESSED............");
	                            return true;
	    	    	
	    	    	}
	    	    	return true;
	    	    }
	    	 });
	    	
	    		    	
	    	editText2.setOnEditorActionListener(new OnEditorActionListener() 
	    	{
	    	    @Override
	    	    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) 
	    	    {
	    	    	if (actionId == EditorInfo.IME_ACTION_SEARCH ||
	                        actionId == EditorInfo.IME_ACTION_DONE ||
	                        event.getAction() == KeyEvent.ACTION_DOWN &&
	                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) 
	    	    	{
	     
	                            return true;
	    	    	
	    	    	}
	    	    	return true;
	    	    }
	    	 });
	    	
	    	editText3.setOnEditorActionListener(new OnEditorActionListener() 
	    	{
	    	    @Override
	    	    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) 
	    	    {
	    	    	if (actionId == EditorInfo.IME_ACTION_SEARCH ||
	                        actionId == EditorInfo.IME_ACTION_DONE ||
	                        event.getAction() == KeyEvent.ACTION_DOWN &&
	                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) 
	    	    	{
	     
	                            System.out.println("BUTTON PRESSED............");
	                            return true;
	    	    	
	    	    	}
	    	    	return true;
	    	    }
	    	    
	    	 });
	    	 
	    	//Request the photo taken by the camera
	    	img.setOnClickListener(new OnClickListener() 
	    	{
	    	    public void onClick(View v) 
	    	    {
	    	    	System.out.println("IMAGE PRESSED............");
	    	    	Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
	                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
	    	    }
	    	       
	    	    
	    	});
	    	
	    	         
	    }
	 
	 
	 /**
	  * Get the photo from camera, saved it on the hard memory and keep the path in site object
	  * 
	  * @params
	  *  requestCode: the code of the request
	  *  resultCode: the code of the result
	  *  data: intent with the information of taken picture
	  * */
	 
	 protected void  onActivityResult(int requestCode, int resultCode, Intent data) 
	    {
         if (requestCode == CAMERA_PIC_REQUEST && resultCode == RESULT_OK) 
         {
               Bitmap image = (Bitmap) data.getExtras().get("data");
               ImageView imageview = (ImageView) findViewById(R.id.ImgPic);
               imageview.setImageBitmap(image);
               
               EditText editText1 = (EditText) findViewById(R.id.title);
               
               String name = editText1.getText().toString();
                              
               if(name==null)
            	   name="site name";
               
               ContextWrapper context = new ContextWrapper(getApplicationContext());
               File dirImages = context.getDir(name, Context.MODE_PRIVATE);
               File path = new File(dirImages, name + ".jpg");
                
               FileOutputStream fos = null;
               try{
            	   
                   fos = new FileOutputStream(path);
                   image.compress(Bitmap.CompressFormat.JPEG, 80, fos);
                   fos.flush();
                   
               }catch (FileNotFoundException ex){
                   
            	   ex.printStackTrace();
               }catch (IOException ex){
                   
            	   ex.printStackTrace();
               }
               site.setImage_path(path.getAbsolutePath());
         }
     }

	 
	 /**
	  * Save the last state of the activity
	  * */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		
		EditText editText1 = (EditText) findViewById(R.id.title);
    	EditText editText2 = (EditText) findViewById(R.id.description);
    	EditText editText3 = (EditText) findViewById(R.id.address);

		outState.putString("name",editText1.getText().toString());
		outState.putString("des",editText2.getText().toString());
		outState.putString("address", editText3.getText().toString());
		if(site.getImage_path()!=null&&!site.getImage_path().equals(""))
			outState.putString("path", site.getImage_path());
		
		super.onSaveInstanceState(outState);
	}
	 
	 
	 
}
