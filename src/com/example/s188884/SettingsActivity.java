package com.example.s188884;

import java.util.Locale;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;

public class SettingsActivity extends MenuActivity
{
	private Button serviceOn;
	private Button serviceOff;
	TextView status;
	public static final String PREFS_NAME = "MinePreferanser";
	PeriodiskService ps = new PeriodiskService();
	
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
	
		status = (TextView)findViewById(R.id.status);
		
		
		SharedPreferences settings = getSharedPreferences(PREFS_NAME,0);
		
		TimePicker timeofday = (TimePicker) findViewById(R.id.timepicker);
		timeofday.setCurrentHour(settings.getInt("timeofdayhour", 0));
		timeofday.setCurrentMinute(settings.getInt("timeofdayminute", 0));
		
		timeofday.setOnTimeChangedListener(watcher);

		timeofday.setIs24HourView(true);
		
		if(iServiceRunning(PeriodiskService.class))
		{
			status.setText(R.string.on);
		}
		else
			status.setText(R.string.off);
		
		noButton();
		enButton();
		
		serviceOn = (Button)findViewById(R.id.buttonOn);
		serviceOff = (Button)findViewById(R.id.buttonOff);
		
		//Skru på service knapp
		serviceOn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{	
				Intent service = new Intent(getApplicationContext(), PeriodiskService.class);
				startService(service);	
				Intent language = new Intent(SettingsActivity.this,SettingsActivity.class);
	            finish();
	            overridePendingTransition(0, 0);
	            startActivity(language);
				Toast.makeText(getBaseContext(), R.string.smsserviceon, Toast.LENGTH_SHORT).show();
			}
		});
		//Skru av service knapp
		serviceOff.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				Intent service = new Intent(getApplicationContext(), PeriodiskService.class);
				stopService(service);
				Intent language = new Intent(SettingsActivity.this,SettingsActivity.class);
	            finish();
	            overridePendingTransition(0, 0);
	            startActivity(language);
				Toast.makeText(getBaseContext(), R.string.smsserviceoff, Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	private boolean iServiceRunning(Class<?> serviceClass) 
	{
	    ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) 
	    {
	        if (serviceClass.getName().equals(service.service.getClassName())) 
	        {
	            return true;
	        }
	    }
	    return false;
	}
	
	private OnTimeChangedListener watcher = new OnTimeChangedListener()
	{
		@Override
		public void onTimeChanged(TimePicker view, int hourOfDay, int minute) 
		{
			SharedPreferences settings = getSharedPreferences(PREFS_NAME,0);
			Editor editor = settings.edit();
			editor.putInt("timeofdayhour", hourOfDay).commit();
			editor.putInt("timeofdayminute", minute).commit();
		}
	};
	
	public void noButton()
	{
		ImageButton btnNor = (ImageButton)findViewById(R.id.norwegian);
		btnNor.setOnClickListener(new OnClickListener()
		{
			public void onClick(View arg)
			{
				SharedPreferences settings = getSharedPreferences(PREFS_NAME,0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putString("language", "no");
				editor.commit();
				Locale locale = new Locale("no"); 
	            Locale.setDefault(locale);
	            Configuration config = new Configuration();
	            config.locale = locale;
	            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
	            Toast.makeText(SettingsActivity.this, "Du har valgt Norsk!", Toast.LENGTH_LONG).show();
	            Intent language = new Intent(SettingsActivity.this,SettingsActivity.class);
	            finish();
	            overridePendingTransition(0, 0);
	            startActivity(language);
			}
		});
	}
	public void enButton()
	{
		ImageButton btnEn = (ImageButton)findViewById(R.id.english);
		btnEn.setOnClickListener(new OnClickListener()
		{
			public void onClick(View arg)
			{
				SharedPreferences settings = getSharedPreferences(PREFS_NAME,0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putString("language", "en");
				editor.commit();
				Locale locale = new Locale("en"); 
	            Locale.setDefault(locale);
	            Configuration config = new Configuration();
	            config.locale = locale;
	            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
	            Toast.makeText(SettingsActivity.this, "You have chosen English!", Toast.LENGTH_LONG).show();
	            Intent language = new Intent(SettingsActivity.this,SettingsActivity.class);
	            finish();
	            overridePendingTransition(0, 0);
	            startActivity(language);
			}
		});	
	}
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch (item.getItemId()) {

		case android.R.id.home:
			Intent language = new Intent(SettingsActivity.this,MenuActivity.class);
            finish();
            overridePendingTransition(0, 0);
            startActivity(language);
		}

		return super.onOptionsItemSelected(item);
		}
}
