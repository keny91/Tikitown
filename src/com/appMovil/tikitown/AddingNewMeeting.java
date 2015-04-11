package com.appMovil.tikitown;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

/**
 * Activity to create a meeting in the place selected on the previous activity
 * 
 * @variables
 *  btnCalendar, btnTimePicker, btnDone: buttons of the UI
 *  txtDate, txtTime, txtDescription: view with the information to be introduced
 *  mYear, mMonth,mDay,mHour,mMinute: integers that will pick the meeting information
 * */

public class AddingNewMeeting extends Activity implements OnClickListener 
{
 
    // Widget GUI
    Button btnCalendar, btnTimePicker, btnDone;
    TextView txtDate, txtTime;
    EditText txtDescription;
 
    // Variable for storing current date and time
    private int mYear, mMonth, mDay, mHour, mMinute;
    public static int savedY=0, savedM=0, savedD=0, savedH=0, savedMin=0;
    Bundle b;
 
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addingmeeting);
        
        Intent i=getIntent();
        
        b=i.getExtras();
 
        btnCalendar = (Button) findViewById(R.id.btnCalendar);
        btnTimePicker = (Button) findViewById(R.id.btnTimePicker);
        btnDone= (Button) findViewById(R.id.btnDone);
        
        txtDescription = (EditText) findViewById(R.id.meeting);
        txtDate = (TextView) findViewById(R.id.txtDate);
        txtTime = (TextView) findViewById(R.id.txtTime);
 
        btnCalendar.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);
        btnDone.setOnClickListener(this);    
    }
 
    /**
     * Method that manage all buttons clicks of UI, save the values introduced and send them to the next activity
     * 
     * @params
     * v: View clicked
     * */
    
    @Override
    public void onClick(View v) 
    {
 
    	//In order to know which botton was pressed
        if (v == btnCalendar) 
        {
 
            // Process to get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
 
            // Launch Date Picker Dialog
            DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() 
            {
 
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) 
                        {
                            // Display Selected date in textbox
                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            
                            //Save the changes done in the global variables
                            savedY= year;
                            savedM= monthOfYear;
                            savedD= dayOfMonth;
                          
                        }
                    
            }, mYear, mMonth, mDay);
            dpd.show();
        }
        if (v == btnTimePicker) 
        {
 
            // Process to get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);
 
            // Launch Time Picker Dialog
            TimePickerDialog tpd = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() 
            {
 
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) 
                        {
                            // Display Selected time in textbox
                            txtTime.setText(hourOfDay + ":" + minute);
                            
                            savedH= hourOfDay;
                            savedMin= minute;
                        }
            }, mHour, mMinute, false);
            tpd.show();
     
        }
        if (v == btnDone)
        {
        	
        	Context context = getApplicationContext(); 
        	String msg = "Meeting saved"; 
        	int duration = Toast.LENGTH_SHORT; 
        	Toast toast = Toast.makeText(context, msg, duration); 
        	toast.show();
        	
        	Intent myIntent = new Intent(AddingNewMeeting.this,MeetingNot.class);
        	b.putInt("Day", savedD);
        	b.putInt("Month", savedM);
        	b.putInt("Year", savedY);
        	b.putInt("Hour", savedH);
        	b.putInt("Min", savedMin);
        	
        	  
            myIntent.putExtras(b);
            
            startActivity(myIntent);
            
            finish();
        } 
    }
}
