package com.example.s188884;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EditActivity extends MenuActivity
{
	final int PICK_CONTACT = 1;
	Button editBtn;
	TextView text1;
    DBAdapter db;
    Person person;
		@Override
		 protected void onCreate(Bundle savedInstanceState) 
		    {
		        super.onCreate(savedInstanceState);
		        setContentView(R.layout.activity_edit);
		        final DatePicker birthday = (DatePicker) findViewById(R.id.editbirthdayfield);
		        ImageView hentkontakt = (ImageView)findViewById(R.id.editnumberfromcontacts);
		        final EditText firstname = (EditText) findViewById(R.id.editfirstnamefield);
		        final EditText lastname = (EditText) findViewById(R.id.editlastnamefield);
		        final EditText number = (EditText) findViewById(R.id.editnumberfield);
		        final ImageView feilfornavn = (ImageView)findViewById(R.id.editwarningfirstname);
		        final ImageView feiletternavn = (ImageView)findViewById(R.id.editwarninglastname);
		        final ImageView feilnummer = (ImageView)findViewById(R.id.editwarningnumber);
		        
		        //Hente data som er overført via intent.extras
				Intent intent = getIntent();
				String thefornavn = intent.getStringExtra("fornavn");
				firstname.setText(thefornavn);
				String theetternavn = intent.getStringExtra("etternavn");
				lastname.setText(theetternavn);
				String thenummer = intent.getStringExtra("nummer");
				number.setText(thenummer);
				String thebirthday = intent.getStringExtra("bursdag");
				String[] paths = thebirthday.split("/");
				int day = Integer.parseInt(paths[0]);
				int month = Integer.parseInt(paths[1])-1;
				birthday.updateDate(2014, month, day);
				
				feilfornavn.setVisibility(View.INVISIBLE);
				feiletternavn.setVisibility(View.INVISIBLE);
				feilnummer.setVisibility(View.INVISIBLE);
				
		        firstname.addTextChangedListener(new TextWatcher()
		        {
		        	@Override
					public void beforeTextChanged(CharSequence s, int start, int count, int after) 
					{}
					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) 
					{}
					@Override
					public void afterTextChanged(Editable s) 
					{
						if(sjekkTom(firstname))
							feilfornavn.setVisibility(View.VISIBLE);
						else
							feilfornavn.setVisibility(View.INVISIBLE);
					}
		        });
		        
		        lastname.addTextChangedListener(new TextWatcher()
		        {
		        	public void beforeTextChanged(CharSequence s, int start, int count, int after) 
					{}
					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) 
					{}
					@Override
					public void afterTextChanged(Editable s) 
					{
						if(sjekkTom(lastname))
							feiletternavn.setVisibility(View.VISIBLE);
						else
							feiletternavn.setVisibility(View.INVISIBLE);
					}
		        });
		        
		        number.addTextChangedListener(new TextWatcher()
		        {
		        	@Override
					public void beforeTextChanged(CharSequence s, int start, int count, int after) 
					{}
					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) 
					{}
					@Override
					public void afterTextChanged(Editable s) 
					{
						if(sjekkTom(number))
							feilnummer.setVisibility(View.VISIBLE);
						else
							feilnummer.setVisibility(View.INVISIBLE);
					}
		        });
		        
		        try 
		        {
		            Field f[] = birthday.getClass().getDeclaredFields();
		            for (Field field : f) 
		            {
		                if (field.getName().equals("mYearPicker") || field.getName().equals("mYearSpinner")) 
		                {
		                    field.setAccessible(true);
		                    Object yearPicker = new Object();
		                    yearPicker = field.get(birthday);
		                    ((View) yearPicker).setVisibility(View.GONE);
		                }
		            }
		        } 
		        catch (IllegalAccessException e) 
		        {
		            Log.d("ERROR", e.getMessage());
		        }
		        
		        hentkontakt.setOnClickListener(new OnClickListener()
		        {
					@Override
					public void onClick(View v) 
					{
						Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
						startActivityForResult(intent, PICK_CONTACT);
					}
		        });
		        
		        editBtn = (Button)findViewById(R.id.edit);
		        editBtn.setOnClickListener(new View.OnClickListener() 
		        {	
					@Override
					public void onClick(View v) 
					{
						Intent intent = getIntent();
						int theid = intent.getIntExtra("id", 0);
						String fnavn = firstname.getText().toString();
						String enavn = lastname.getText().toString();
						String nummer = number.getText().toString();
						String bursdag = birthday.getDayOfMonth() + "/" + (birthday.getMonth()+1);
						try
						{
							if(sjekkNavn(fnavn) && sjekkNavn(enavn) && sjekkNummer(nummer) && !sjekkTom(firstname) && !sjekkTom(lastname) && !sjekkTom(number))
							{
								db = new DBAdapter(v.getContext());
								db.oppdater(theid, fnavn, enavn, nummer, bursdag);
								AlertDialog.Builder innleggingOK = new AlertDialog.Builder(EditActivity.this);
								innleggingOK
								.setTitle(R.string.success)
								.setMessage(fnavn + " " + enavn + " " + getResources().getText(R.string.edited))
								.setCancelable(false)
								.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() 
								{
									@Override
									public void onClick(DialogInterface dialog, int which) 
									{
										Intent i = new Intent(EditActivity.this,ShowAllActivity.class);
							            finish();
							            overridePendingTransition(0, 0);
							            startActivity(i);
									}
								})
								.show();
							}	
							else
							{		
								AlertDialog.Builder innleggingIkkeOK = new AlertDialog.Builder(EditActivity.this);
								innleggingIkkeOK
								.setTitle(R.string.error)
								.setMessage(R.string.errormessage)
								.setCancelable(false)
								.setPositiveButton("Ok", new DialogInterface.OnClickListener() 
								{
									@Override
									public void onClick(DialogInterface dialog, int which) 
									{
										dialog.cancel();
									}
								});
								innleggingIkkeOK.show();
							}
						}
						catch(SQLiteException e)
						{
							Toast toast = Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_SHORT);
							toast.show();
						}
					}
				});
		    }
		
		protected void onActivityResult(int reqCode, int resultCode, Intent data)
	    {
			super.onActivityResult(reqCode, resultCode, data);
				if(resultCode == Activity.RESULT_OK)
					switch(reqCode)
					{
					case(PICK_CONTACT):
					{
						final EditText phoneInput = (EditText)findViewById(R.id.editnumberfield);
						Cursor cursor = null;
						String phoneNumber = "";
						List<String> allNumbers = new ArrayList<String>();
						int phoneIdx = 0;
						try
						{
							Uri result = data.getData();
							String id = result.getLastPathSegment();
							cursor = getContentResolver().query(Phone.CONTENT_URI, null, Phone.CONTACT_ID + "=?", new String[]{ id }, null);
							phoneIdx = cursor.getColumnIndex(Phone.DATA);
							if(cursor.moveToFirst())
							{
								while(cursor.isAfterLast() == false)
								{
									phoneNumber = cursor.getString(phoneIdx);
									allNumbers.add(phoneNumber);
									cursor.moveToNext();
								}
							}
							else
							{}
						}
						catch(Exception e)
						{
							
						}
						finally
						{
							if(cursor != null)
								cursor.close();
						}
						
		               final CharSequence[] items = allNumbers.toArray(new String[allNumbers.size()]);
		                AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
		                builder.setTitle(R.string.choosenumber);
		                builder.setItems(items, new DialogInterface.OnClickListener() 
		                {
		                    public void onClick(DialogInterface dialog, int item) 
		                    {
		                        String selectedNumber = items[item].toString();
		                        selectedNumber = selectedNumber.replace("-", "");
		                        phoneInput.setText(selectedNumber);
		                    }
		                });
		                
		                AlertDialog alert = builder.create();
		                if(allNumbers.size() > 1) 
		                {
		                    alert.show();
		                } 
		                else 
		                {
		                    String selectedNumber = phoneNumber.toString();
		                    selectedNumber = selectedNumber.replace("-", "");
		                    phoneInput.setText(selectedNumber);
		                }
		                if (phoneNumber.length() == 0) 
		                {  
		                    Toast.makeText(getBaseContext(), R.string.nocontacts, Toast.LENGTH_LONG).show();  
		                }  
		            }  
		            break;  
		        }  
		    } 
		//Regex-metode for å sjekke navn
		public static boolean sjekkNavn(String navn) 
		{
			String navn_pattern = "^[æøåÆØÅa-zA-Z- ]*$";

			Pattern pattern = Pattern.compile(navn_pattern);
			Matcher matcher = pattern.matcher(navn);
			return matcher.matches();
		}
		
		//Regex-metode for å sjekke nummer
		public static boolean sjekkNummer(CharSequence nummer)
		{
			String nummer_pattern = "^[0-9+]{8,}$";
			
			Pattern pattern = Pattern.compile(nummer_pattern);
			Matcher matcher = pattern.matcher(nummer);
			return matcher.matches();
		}
		
		//Sjekk for å se om felt er tomt
		public static boolean sjekkTom(EditText text)
		{
			return text.getText().toString().trim().length() == 0;
		}
}
