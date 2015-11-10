package com.example.s188884;

import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Window;


public class MainActivity extends Activity 
{
	public static final String PREFS_NAME = "MinePreferanser";

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        
    	SharedPreferences settings = getSharedPreferences(PREFS_NAME,0);
        String language = settings.getString("language", "en");
        
        if(language.equals("no"))
        {
        	Locale locale = new Locale("no"); 
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }
        else
        {
        	Locale locale = new Locale("en"); 
	        Locale.setDefault(locale);
	        Configuration config = new Configuration();
	        config.locale = locale;
	        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }
        
        //Lager en thread til splash screen
        Thread logoTimer = new Thread()
        {
        	public void run()
        	{
        		try
        		{
        			sleep(4000);
        			Intent menyIntent = new Intent("com.examples.s188884.MENY");
        			startActivity(menyIntent);
        		} 
        		catch (InterruptedException e) 
        		{
					e.printStackTrace();
				}

        		finally
        		{
        			finish();
        		}
        	}
        };
        logoTimer.start();
    }
}