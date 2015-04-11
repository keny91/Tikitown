package com.appMovil.tikitown;


import com.appMovil.tikitown.ShakeDetection.OnShakeListener;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;



import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;

/**
 * This class implements the Main Menu for the application
 * This is the Launcher application
 * */

public class MainMenu extends Activity {

	
	 private ShakeDetection mShakeDetector;
	 private SensorManager mSensorManager;
	 private Sensor mAccelerometer;


	    @Override
	    protected void onResume() {
	        super.onResume();
	        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
	    }

	    @Override
	    protected void onPause() {
	        mSensorManager.unregisterListener(mShakeDetector);
	        super.onPause();
	    }  

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
      
        
        // ShakeDetector initialization
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        
        mShakeDetector =  new ShakeDetection (new OnShakeListener() {
            @Override
            public void onShake() {
                // Do stuff!
            	Intent intent = new Intent(MainMenu.this, MapsActivity.class);
                startActivity(intent);
            	
            }
        });
        
                
        Button b_barrido = (Button) findViewById(R.id.button1); 
        Button b_busqueda = (Button) findViewById(R.id.button2); 
        Button b_settings = (Button) findViewById(R.id.button3); 
        Button b_fav = (Button) findViewById(R.id.button4); 
        
        /**
    	 * Button Listeners
    	 * OnClick a Intend is run to launch another activity
    	 *  
    	 * */
        
        //Boton central ---> BARRIDO

        b_barrido.setOnClickListener(new View.OnClickListener() 
        {
        	@Override 
        	public void onClick(View arg0) 
        	{ 
        		//

        		Intent intent = new Intent(MainMenu.this, MapsActivity.class);
        		startActivity(intent);


        	} 
        }); 

        //Boton Izquierdo ---> BUSQUEDA
        b_busqueda.setOnClickListener(new View.OnClickListener() 
        {
        	@Override 
        	public void onClick(View arg0) 
        	{ 

        		Intent intent = new Intent(MainMenu.this, SearchByText.class);

        		startActivity(intent);
        	} 
        }); 

        //Boton derecho ---> FAVORITOS
        b_fav.setOnClickListener(new View.OnClickListener() 
        {
        	@Override 
        	public void onClick(View arg0) 
        	{ 

        		Intent intent = new Intent(MainMenu.this, DataActivity.class);
        		Bundle b=new Bundle();
        		b.putInt("mode", DataActivity.MODE_FAV);
        		intent.putExtras(b);
        		startActivity(intent);
        	} 
        }); 

        //Boton arriba/Derecha ---> SETTINGS
        b_settings.setOnClickListener(new View.OnClickListener() 
        {
        	@Override 
        	public void onClick(View arg0) 
        	{ 
        		// TODO Auto-generated method stub
        		Intent intent = new Intent(MainMenu.this, PreferenceActivity.class);
        		startActivity(intent);
        	} 
        }); 
    }
}
