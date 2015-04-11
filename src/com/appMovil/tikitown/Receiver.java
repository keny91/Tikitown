package com.appMovil.tikitown;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
 
/*This class is used when a pending intent is received in order to launch the alarm*/
public class Receiver extends BroadcastReceiver
{


	/**
	 * Function called when a pending intent is received in order
	 * to launch the alarm
	 * @params
	 * context, intent
	 * */
	@Override
	public void onReceive(Context context, Intent intent)
	{

		if(intent.getAction()!=null){
			if(intent.getAction().equals(Intent.ACTION_CALL)){
				Intent call = new Intent(Intent.ACTION_CALL);
				context.startService(call);
			}
			if(intent.getAction().equals("ALARM_ACTION")&&intent.hasExtra("address")){
				Intent i=new Intent(context,AlarmService.class);
				i.putExtras(intent.getExtras());
				context.startService(i);
			}
		}
	}   
}
