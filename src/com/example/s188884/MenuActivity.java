package com.example.s188884;

import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MenuActivity extends Activity
{
	public static final String PREFS_NAME = "MinePreferanser";
	
	String locale = Locale.getDefault().getDisplayName();

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		addButton();
		showAllButton();
		settingsButton();
		exitButton();
		
		SharedPreferences settings = getSharedPreferences(PREFS_NAME,0);
		
		EditText birthdaymessage = (EditText) findViewById(R.id.birthdaymessage);
		birthdaymessage.setText(settings.getString("birthdaymessage", ""));
		birthdaymessage.addTextChangedListener(watcher);
	}
	//Endrer i sharedpreferences når det blir gjort endringer i applikasjonen
	private TextWatcher watcher = new TextWatcher()
	{
		@Override
		 public void afterTextChanged(Editable s) {}
		 @Override
		 public void beforeTextChanged(CharSequence s, int start, int count, int after){}
		
		 @Override
		 public void onTextChanged(CharSequence s, int start, int before, int count) 
		 {
				SharedPreferences settings = getSharedPreferences(PREFS_NAME,0);
				Editor editor = settings.edit();
				editor.putString("birthdaymessage", s.toString()).commit();
		 }
	};
	
	// Legg inn bursdag-knappen
	public void addButton()
	{
		Button btnAdd = (Button)findViewById(R.id.add);
		btnAdd.setOnClickListener(new OnClickListener()
		{
			public void onClick(View arg)
			{
				Intent add = new Intent(MenuActivity.this,AddActivity.class);
				startActivity(add);
				overridePendingTransition(0,0);
			}
		});
	}
	
	// Vis alle-knappen
	public void showAllButton()
	{
		Button btnshowAll = (Button)findViewById(R.id.showall);
		btnshowAll.setOnClickListener(new OnClickListener()
		{
			public void onClick(View arg)
			{
				Intent showAll = new Intent(MenuActivity.this,ShowAllActivity.class);
				startActivity(showAll);
				overridePendingTransition(0,0);
			}
		});
	}
				
	// Innstillinger-knapp
	public void settingsButton()
	{
		Button btnSettings = (Button)findViewById(R.id.settings);
		btnSettings.setOnClickListener(new OnClickListener()
		{
			public void onClick(View arg)
			{  
				Intent settings = new Intent(MenuActivity.this,SettingsActivity.class);
				startActivity(settings);
				overridePendingTransition(0,0);
				finish();
			}
		});
	}
			
	// Avsluttknappen
		public void exitButton()
		{
			Button btnExit = (Button)findViewById(R.id.exit);
			btnExit.setOnClickListener(new OnClickListener()
			{
				public void onClick(View arg)
				{
					finish();
					System.exit(0);
				}
			});
		}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
    	switch (item.getItemId())
        {
        case R.id.action_settings:
        	Intent settings = new Intent(this,SettingsActivity.class);
			startActivity(settings);
			overridePendingTransition(0,0);
        case R.id.action_exit:
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			startActivity(intent);
        	finish();
			System.exit(0);
        }
    return super.onOptionsItemSelected(item);
    }
}
