package com.appMovil.tikitown;

import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * Fragment of the dialog that shows the filters and let you select the filters of the places's search
 * */


public class FiltersDialog extends DialogFragment {
	
	/**
	 * Changes the values of the preferences
	 * 
	 * @params
	 *  checkbox: 
	 *  check_filter[]: String catch form preferences
	 *  value: boolean value
	 *   
	 * @commit
	 *  New values for preferences
	 * */
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		
		setCancelable(false);
		View view = inflater.inflate(R.layout.my_dialog, null);
		getDialog().setTitle(R.string.dialog_name);
		
		//CHECKBOXES
	  	CheckBox check1 = ( CheckBox ) view.findViewById(R.id.checkBox1);
		CheckBox check2 = ( CheckBox ) view.findViewById(R.id.checkBox2);
		CheckBox check3 = ( CheckBox ) view.findViewById(R.id.checkBox3);
		CheckBox check4 = ( CheckBox ) view.findViewById(R.id.checkBox4);
		CheckBox check5 = ( CheckBox ) view.findViewById(R.id.checkBox5);
		CheckBox check6 = ( CheckBox ) view.findViewById(R.id.checkBox6);
		CheckBox check7 = ( CheckBox ) view.findViewById(R.id.checkBox7);
		CheckBox check8 = ( CheckBox ) view.findViewById(R.id.checkBox8);
		
		
		Button B_ok  = (Button)view.findViewById(R.id.ok);
		Button B_cancel  = (Button)view.findViewById(R.id.cancel);
		
		/*	CHECKBOXES
		 * 
		 * 		1_Restaurants
		 * 		2_Cinemas
		 * 		3_Shopping Malls
		 * 		4_Spa
		 * 		5_Theater
		 * 		6_Disco
		 * 		7_Cafe
		 * 		8_Park
		 * */
		
		  final boolean [] check_filter = new boolean [9];
	
		  final SharedPreferences settings = getActivity().getSharedPreferences(PreferenceActivity.PREFS_NAME,0);
		  
		  //Read preferences to check if exists a previous state
		  check_filter[0] = settings.getBoolean("filter_restaurant", false);
		  check_filter[1] = settings.getBoolean("filter_cinema", false);
		  check_filter[2] = settings.getBoolean("filter_shopping", false);
		  check_filter[3] = settings.getBoolean("filter_spa", false);
		  check_filter[4] = settings.getBoolean("filter_theater", false);
		  check_filter[5] = settings.getBoolean("filter_disco", false);
		  check_filter[6] = settings.getBoolean("filter_cafe", false);
		  check_filter[7] = settings.getBoolean("filter_park", false);
		  
		  
		  //Assign initial state to array
			  check1.setChecked(check_filter[0]);
			  check2.setChecked(check_filter[1]);
			  check3.setChecked(check_filter[2]);
			  check4.setChecked(check_filter[3]);
			  check5.setChecked(check_filter[4]);
			  check6.setChecked(check_filter[5]);
			  check7.setChecked(check_filter[6]);
			  check8.setChecked(check_filter[7]);
			  	
		//Checkbox's listeners
		check1.setOnCheckedChangeListener(new OnCheckedChangeListener() 
		{
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
		    {
		    	 if ( isChecked )
			        {
			        	check_filter [0] = true; 	
			        }
			        
			        else {			        	
			        	check_filter [0] = false;
			        }
		    }
		});
				
		check2.setOnCheckedChangeListener(new OnCheckedChangeListener() // CHECKBOX_LISTENER_2
				{
				    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
				    {
				        if ( isChecked )
				        {
				            // perform logic
				        	check_filter [1] = true;
				        	
				        }
				        
				        else {
				        	
				        	check_filter [1] = false;
				        }

				    }
				});
						
		check3.setOnCheckedChangeListener(new OnCheckedChangeListener()	// CHECKBOX_LISTENER_3
				{
				    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
				    {
				    	if ( isChecked )
				        {
				        	check_filter [2] = true; 	
				        }				        
				        else {			        	
				        	check_filter [2] = false;
				        }

				    }
				});
						
		check4.setOnCheckedChangeListener(new OnCheckedChangeListener() // CHECKBOX_LISTENER_4
				{
				    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
				    {
				    	if ( isChecked )
				        {
				        	check_filter [3] = true; 	
				        }				        
				        else {			        	
				        	check_filter [3] = false;
				        }

				    }
				});
		
		check5.setOnCheckedChangeListener(new OnCheckedChangeListener() // CHECKBOX_LISTENER_5
		{
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
		    {
		    	if ( isChecked )
		        {
		        	check_filter [4] = true; 	
		        }				        
		        else {			        	
		        	check_filter [4] = false;
		        }

		    }
		});
		
		check6.setOnCheckedChangeListener(new OnCheckedChangeListener() // CHECKBOX_LISTENER_6
		{
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
		    {
		    	if ( isChecked )
		        {
		        	check_filter [5] = true; 	
		        }				        
		        else {			        	
		        	check_filter [5] = false;
		        }

		    }
		});
		
		check7.setOnCheckedChangeListener(new OnCheckedChangeListener() // CHECKBOX_LISTENER_7
		{
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
		    {
		    	if ( isChecked )
		        {
		        	check_filter [6] = true; 	
		        }				        
		        else {			        	
		        	check_filter [6] = false;
		        }

		    }
		});

		check8.setOnCheckedChangeListener(new OnCheckedChangeListener() // CHECKBOX_LISTENER_8
		{
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
		    {
		    	if ( isChecked )
		        {
		        	check_filter [7] = true; 	
		        }				        
		        else {			        	
		        	check_filter [7] = false;
		        }

		    }
		});
		
		
		// BUTTON LISTENERS
		
		// BUTTON_OK
		B_ok.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				//PREFERENCES ALTERATION
				
				SharedPreferences.Editor editor = settings.edit();
				editor.putBoolean("filter_restaurant", check_filter[0]);	// #1
				editor.putBoolean("filter_cinema", check_filter[1]); 		// #2
				editor.putBoolean("filter_shopping", check_filter[2]); 		// #3
				editor.putBoolean("filter_spa", check_filter[3]); 			// #4
				editor.putBoolean("filter_theater", check_filter[4]); 		// #5
				editor.putBoolean("filter_disco", check_filter[5]); 		// #6
				editor.putBoolean("filter_cafe", check_filter[6]); 			// #7
				editor.putBoolean("filter_park", check_filter[7]); 			// #8
				editor.commit();
				dismiss();
			}
		});
		
		// Cancel's button's listener
		B_cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
		
		return view;
	}
	
	
}