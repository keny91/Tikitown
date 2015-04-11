package com.appMovil.tikitown;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;


public class PreferenceActivity extends Activity {
	
	final int min_value = 500;
	final int max_value = 2000;
	public static final String PREFS_NAME = "CustomPrefs";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preference);
		
		SeekBar seek=(SeekBar) findViewById(R.id.seekBarPrefSeekBar);
		TextView text_right=(TextView) findViewById(R.id.seekBarPrefUnitsRight);
		TextView text_left=(TextView) findViewById(R.id.seekBarPrefUnitsLeft);
		TextView text_value=(TextView) findViewById(R.id.seekBarPrefValue);
		
		final SharedPreferences settings = getSharedPreferences(PREFS_NAME,0);
		int radius_value = settings.getInt("radius_value", 1000);
		seek.setProgress((radius_value-500)*100/1500);
		
		text_value.setText(String.valueOf(radius_value));		
		text_right.setText(String.valueOf(max_value));
		text_left.setText(String.valueOf(min_value));
		
		
		
		
		
		seek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

		    @Override
		    public void onStopTrackingTouch(SeekBar seekBar) {
		        // TODO Auto-generated method stub
		    }

		    @Override
		    public void onStartTrackingTouch(SeekBar seekBar) {
		        // TODO Auto-generated method stub
		    }
		    
		    /**
	    	 * When the bar is moved, the function returns the progress value.
	    	 * This method changes the preference value of "ratio_value" and changes the display on a 
	    	 * Textview to whatever value was set
	    	 * 
	    	 * @params
	    	 * progress 
	    	 * boolean fromUser = 1
	    	 * SeekBar  
	    	 * */

		    @Override
		    public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
		        // TODO Auto-generated method stub
		    	int radius_value;
		    	TextView text_value=(TextView) findViewById(R.id.seekBarPrefValue);

		        radius_value=(max_value-min_value)*progress/100+500;
		        text_value.setText(String.valueOf(radius_value));
		        
		        SharedPreferences.Editor editor = settings.edit();
				editor.putInt("radius_value", radius_value);	
				editor.commit(); 
		    }
		});	
	}
}