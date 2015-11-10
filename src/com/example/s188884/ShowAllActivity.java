package com.example.s188884;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ShowAllActivity extends ListActivity
{
	private ListView listView; 
	TextView antall;
	DBAdapter db;
	Person person;
	Button slettAlleBtn;
	Button leggTilBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_showall);
		final DBAdapter db = new DBAdapter(this);
		
		ArrayList<HashMap<String, String>> Items = new ArrayList<HashMap<String,String>>();
		List<Person> data = db.visAlle();
		
		for(Person val : data)
		{
			HashMap<String,String> map = new HashMap<String,String>();
			map.put("_ID", Integer.toString(val.getID()));
			map.put("fornavn", val.getFornavn());
			map.put("etternavn", val.getEtternavn());
			map.put("nummer", val.getNummer());
			map.put("bursdag", val.getBursdag());
			
			Items.add(map);
		}
		
		
		
		ListAdapter adapter = new SimpleAdapter(this, Items, R.layout.mainlist_item, new String[]{"_ID","fornavn","etternavn","nummer","bursdag"}, 
				new int[] {R.id.itemId,R.id.itemFirstname, R.id.itemLastname, R.id.itemNumber, R.id.itemBirthday });
		setListAdapter(adapter);
		
		
		
		antall = (TextView)findViewById(R.id.antall);
		String count = Integer.toString(db.getAntallPersonerDB());
		antall.setText(getResources().getText(R.string.count) + " " + count);
		listView = getListView();
				
		listView.setOnItemLongClickListener(new OnItemLongClickListener()
		{

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, final View view,int position, long id) 
			{
				AlertDialog.Builder alert = new AlertDialog.Builder(ShowAllActivity.this);
				alert
				 	  .setTitle(R.string.editordeletetitle)
                     .setMessage(R.string.editordelete)
                     .setCancelable(true)
                     .setNegativeButton(R.string.edit, new DialogInterface.OnClickListener() 
                     {

						@Override
						public void onClick(DialogInterface dialog, int which) 
						{
							int DBid = Integer.parseInt(((TextView) view.findViewById(R.id.itemId)).getText().toString());
							String fnavn = ((TextView)view.findViewById(R.id.itemFirstname)).getText().toString();
							String enavn = ((TextView)view.findViewById(R.id.itemLastname)).getText().toString();
							String number = ((TextView)view.findViewById(R.id.itemNumber)).getText().toString();
							String bursdag = ((TextView)view.findViewById(R.id.itemBirthday)).getText().toString();
							
							//Legge variabler i intentens extras for å hente ut i redigeractivity
							Intent i = new Intent(getApplicationContext(), EditActivity.class);
							i.putExtra("id", DBid);
							i.putExtra("fornavn", fnavn);
							i.putExtra("etternavn", enavn);
							i.putExtra("nummer", number);
							i.putExtra("bursdag", bursdag);
			                startActivity(i);
			                overridePendingTransition(0,0);
						} 
                     })
                     .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() 
                     {
                    	int DBid = Integer.parseInt(((TextView) view.findViewById(R.id.itemId)).getText().toString());
						@Override
						public void onClick(DialogInterface dialog, int which) 
						{
							db.slettEn(DBid);
                            Intent i = new Intent(getApplicationContext(), ShowAllActivity.class);
                            startActivity(i);
                            overridePendingTransition(0,0);
						}
					});
				AlertDialog alertDialog = alert.create();
                alertDialog.show();
				return true;
			}
		});
		
		leggTilBtn = (Button)findViewById(R.id.leggTil);
		leggTilBtn.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v) 
			{
				Intent i = new Intent(getApplicationContext(), AddActivity.class);
                startActivity(i);
                overridePendingTransition(0,0);
			}
			
		});
		
		slettAlleBtn = (Button)findViewById(R.id.slettAlle);
        slettAlleBtn.setOnClickListener(new View.OnClickListener() 
        {
			
			@Override
			public void onClick(View v) 
			{
				// Sjekker om Person-tabellen allerede er tom
				if (db.getAntallPersonerDB() == 0)
				{
					AlertDialog.Builder TomTabell = new AlertDialog.Builder(ShowAllActivity.this);
					TomTabell
					.setTitle(R.string.ops)
					.setMessage(R.string.emptylist)
					.setCancelable(false)
					.setPositiveButton("Ok", new DialogInterface.OnClickListener() 
					{
						@Override
						public void onClick(DialogInterface dialog, int which) 
						{
							dialog.cancel();
						}
					});
					TomTabell.show();
				}
				else
				{
					AlertDialog.Builder bekreftSlettAlle = new AlertDialog.Builder(ShowAllActivity.this);
					bekreftSlettAlle
					.setTitle(R.string.suretitle)
					.setMessage(R.string.sure)
					.setCancelable(false)
					.setNegativeButton(R.string.yes, new DialogInterface.OnClickListener() 
					{
						public void onClick(DialogInterface dialog, int which) 
						{
							db.slettDBinnhold();
				            
				          //Sjekke om tabellen er tømt ordentlig, AlertDialog 
				            if (db.getAntallPersonerDB() == 0)
							{
								Toast.makeText(getBaseContext(), R.string.deletedall, Toast.LENGTH_SHORT).show();
							}
							//Om tabellen fortsatt inneholder elementer, AlertDialog
							else
							{
								Toast.makeText(getBaseContext(), R.string.notdeletedall, Toast.LENGTH_SHORT).show();
							}
							dialog.cancel();
				            Intent language = new Intent(ShowAllActivity.this,ShowAllActivity.class);
				            finish();
				            overridePendingTransition(0, 0);
				            startActivity(language);
						}
					})
					.setPositiveButton(R.string.no, new DialogInterface.OnClickListener() 
					{
						@Override
						public void onClick(DialogInterface dialog, int which) 
						{
							dialog.cancel();
						}
					});
					bekreftSlettAlle.show();
				}
				
				
			}
		});
	}

	@Override
	 public void onResume()
	 {
		 super.onResume();
	 }
	 
	 @Override
	 public void onPause()
	 {
		 super.onPause();
	 }
	 
	 
	 //Må for å å få actionbars overflow siden denne klassen implements ListActivity
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
