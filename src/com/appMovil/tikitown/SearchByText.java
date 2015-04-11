package com.appMovil.tikitown;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Activity to introduce the descriptions for the text search
 * */

public class SearchByText extends FragmentActivity {

	
	public static String site;
	public static String address;
	public static String dist;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.search_by_text);
    	Button but = (Button)findViewById(R.id.button);
    	Button but2 = (Button)findViewById(R.id.button2);

    	
		
    	but.setOnClickListener(new OnClickListener() 
    	{
    	    public void onClick(View v) 
    	    {
    	    	EditText siteField = (EditText) findViewById(R.id.title);
    	    	site = siteField.getText().toString();
		
    	    	EditText addressField = (EditText) findViewById(R.id.address);
    	    	address = addressField.getText().toString();
    	    	
    	    	EditText siteDist= (EditText) findViewById(R.id.radio);
    	    	dist = siteDist.getText().toString();
    	    	
    	    	Intent i=new Intent(SearchByText.this, LocLisServ.class);
    	    	
    	    	startService(i);
    		    
    		    onBackPressed();    		   
    		
    	    } });
		
	

	but2.setOnClickListener(new OnClickListener() 
	{
	    public void onClick(View v) 
	    {
	    	
	    stopService(new Intent(SearchByText.this, LocLisServ.class));
	    Toast.makeText(SearchByText.this, "Servicio destruido", Toast.LENGTH_SHORT).show();
	    
	    	
	    } });
	}
}
	
	
	


