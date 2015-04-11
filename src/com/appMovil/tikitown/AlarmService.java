package com.appMovil.tikitown;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
 

/**
 * Service that creates the notification of the meeting introduced
 * 
 * @variables
 *  mManager: object that manage the notification's creation and sending
 * */
 
public class AlarmService extends Service 
{
      
   private NotificationManager mManager;
 
    @Override
    public IBinder onBind(Intent arg0)
    {
       // TODO Auto-generated method stub
        return null;
    }
 
    @Override
    public void onCreate() 
    {
       // TODO Auto-generated method stub  
       super.onCreate();
    }
    
    
 
   @Override
   public void onStart(Intent intent, int start)
   {
       super.onStart(intent, start);

       mManager = (NotificationManager) this.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
       
       Bundle b=intent.getExtras();
       
       //When the event happens, we launch the next activity
       Intent intent1 = new Intent(this.getApplicationContext(),RouteTracer.class);
       intent1.putExtras(b);
     
       Notification notification = new Notification(R.drawable.ic_launcher,"Remember your meeting!", System.currentTimeMillis());
       intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP| Intent.FLAG_ACTIVITY_CLEAR_TOP);
 
       PendingIntent pendingNotificationIntent = PendingIntent.getActivity( this.getApplicationContext(),0, intent1,PendingIntent.FLAG_UPDATE_CURRENT);
       notification.flags |= Notification.FLAG_AUTO_CANCEL;
       notification.setLatestEventInfo(this.getApplicationContext(), "TikiTown", "Remember your meeting!", pendingNotificationIntent);
 
       mManager.notify(0, notification);
       
    }
 
    @Override
    public void onDestroy() 
    {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
 
}
