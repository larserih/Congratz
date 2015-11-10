package com.example.s188884;

import java.util.List;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;

public class SMSService extends IntentService 
{
	public static final String PREFS_NAME = "MinePreferanser";

	public SMSService()
	{
		super("SMSService");
	}
	
	@Override
	protected void onHandleIntent(Intent intent)
	{
		DBAdapter db = new DBAdapter(this);
		List<Person> birthdayPerson = db.getBirthdayPerson();
		
		
		//Sjekke om ingen har bursdag -> ikke nødvendig å kjøre resten av koden
		if(birthdayPerson == null)
		{
			return;
		}
		
		SharedPreferences settings = getSharedPreferences(PREFS_NAME,0);
		SmsManager sms = SmsManager.getDefault();
		//Toast.makeText(getBaseContext(), "Sender sms", Toast.LENGTH_SHORT).show();
        
        //Sende ut SMS til personer i bursdagslista
        for (Person p : birthdayPerson)
        {
        	sms.sendTextMessage(p.getNummer(), null, settings.getString("birthdaymessage", ""), null, null);
        	
        	//Sett notification om bursdagssms er sendt
        	NotificationManager nManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        	Notification n = new Notification.Builder(this)
        	.setContentTitle(this.getString(R.string.notititle))
        	.setContentText(this.getString(R.string.notimessage))
        	.setSmallIcon(R.drawable.icon)
        	.build();
        	
        	n.flags |= Notification.FLAG_AUTO_CANCEL;
        	nManager.notify(0, n);
        }
        
	}
	
}