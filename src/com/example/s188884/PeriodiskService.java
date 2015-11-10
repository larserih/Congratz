package com.example.s188884;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

public class PeriodiskService extends Service {
	
	public static final String PREFS_NAME = "MinePreferanser";	
	
	@Override
	public int onStartCommand (Intent intent, int flags, int startId)
	{
		super.onStartCommand(intent, flags, startId);
		
		SharedPreferences settings = getSharedPreferences(PREFS_NAME,0);
		
		Calendar cal = Calendar.getInstance();	
		
		// Hente ut time og minutt satt i sharedpreferences
		int hour = settings.getInt("timeofdayhour", 0);
		int minute = settings.getInt("timeofdayminute", 0);
		
		//Sette cal sin time og minutt til det som er i sharedpreferences
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, 0);
		
		long triggerMillis = cal.getTimeInMillis();

		//Eventuelt for å ikke sende sms samme dag
	    /*if (cal.getTimeInMillis() < Calendar.getInstance().getTimeInMillis()) 
    	{
        	triggerMillis = cal.getTimeInMillis() + 1380000;
    	}*/
		
		
		PendingIntent p = PendingIntent.getService(this, 0, new Intent(this, SMSService.class), 0);
		
		AlarmManager alarm = (AlarmManager)getSystemService(ALARM_SERVICE);
		alarm.setRepeating(AlarmManager.RTC_WAKEUP, triggerMillis, 86400000, p); //eller 86400000 er ms i et døgn (24*60*60)*1000
		
		return Service.START_NOT_STICKY;
		
		
	}
	
	@Override
	 public void onDestroy() 
	 {
	  // Stoppe alarmen ved ondestroy
			super.onDestroy();
			AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
			PendingIntent p = PendingIntent.getService(this, 0, new Intent(this, SMSService.class), 0);
			alarm.cancel(p);
	
			stopSelf();
	 }

	@Override
	public IBinder onBind(Intent intent) 
	{
		// TODO Auto-generated method stub
		return null;
	}

}