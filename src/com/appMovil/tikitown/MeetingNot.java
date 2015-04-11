package com.appMovil.tikitown;

import java.util.Calendar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;

/**
 * Activity without IU that creates the alarm meeting and broadcast an intent to be processed by our 
 * Broadcast Receiver and show the notification
 * 
 * @variables
 *  pendingIntent: a pendingIntent with the notification's information
 *  
 * */

//This class set a date on the calendar and create the alarm for that date
public class MeetingNot extends Activity 
{
 
    private PendingIntent pendingIntent;
     
    @Override
    public void onCreate(Bundle savedInstanceState) 
     {
      int day,month,year,hour,min;
      
      super.onCreate(savedInstanceState);
      
      Intent gettingIntent = getIntent();
      
      Bundle b= gettingIntent.getExtras();
      day=b.getInt("Day", 0);
      month=b.getInt("Month", 0);
      year= b.getInt("Year", 0);
      
      hour=b.getInt("Hour", 0);
      min=b.getInt("Min", 0);
      
    
      Calendar calendar = Calendar.getInstance();
     
      calendar.set(Calendar.MONTH, month);
      calendar.set(Calendar.YEAR, year);
      calendar.set(Calendar.DAY_OF_MONTH, day);
 
      calendar.set(Calendar.HOUR_OF_DAY, hour);
      calendar.set(Calendar.MINUTE, min);
      calendar.set(Calendar.SECOND, 0);
      //calendar.set(Calendar.AM_PM,Calendar.PM);
     
      Intent myIntent = new Intent(MeetingNot.this,Receiver.class);
      
      myIntent.putExtras(b);
      myIntent.setAction("ALARM_ACTION");
      
      pendingIntent = PendingIntent.getBroadcast(MeetingNot.this, 0, myIntent,0);
     
      AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
      alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
      
      super.onBackPressed();
    
    } 
}