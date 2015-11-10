package com.example.s188884;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class DBAdapter
{
	Context context;
	static final String TAG = "DbHelper";
	static final String DB_NAVN = "navn.db";
	static final String TABELL = "Personer";
	static final String ID = BaseColumns._ID;
	static final String FORNAVN = "fornavn";
	static final String ETTERNAVN = "etternavn";
	static final String NUMMER = "nummer";
	static final String BURSDAG = "bursdag";
	static final int DB_VERSJON = 3;
	
	String[] cols = {FORNAVN,ETTERNAVN,NUMMER,BURSDAG};
	
	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;
	public static ArrayList<String> DBArray = new ArrayList<String>();
	
	public DBAdapter(Context ctx)
	{
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}
	
	public static class DatabaseHelper extends SQLiteOpenHelper
	{
		DatabaseHelper(Context context)
		{
			super(context, DB_NAVN, null, DB_VERSJON);
		}

		@Override
		public void onCreate(SQLiteDatabase db) 
		{
			String sql="create table " + TABELL + " ("
					+ ID + " integer primary key autoincrement, "
					+ FORNAVN + " text, "
					+ ETTERNAVN + " text, "
					+ NUMMER + " text, "
					+ BURSDAG + " text);";
					Log.d(TAG,"oncreated sql" + sql);
					db.execSQL(sql);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
		{
			db.execSQL("drop table if exists " + TABELL);
			Log.d(TAG,"updated");
			onCreate(db);
		}
	}//slutt DatabaseHelper
	
	public DBAdapter open() throws SQLException
	{
		db = DBHelper.getWritableDatabase();
		return this;
	}
	
	public void close()
	{
		
	}
	//Metode for å legge inn i Person-objektet og videre i database
	public void settInn(Person person)
	{
		ContentValues values = new ContentValues();
		values.put(FORNAVN, person.getFornavn());
		values.put(ETTERNAVN, person.getEtternavn());
		values.put(NUMMER, person.getNummer());
		values.put(BURSDAG, person.getBursdag());
		
		db.insert(TABELL, null, values);
		db.close();
	}
	//Metode for å slette en person med gitt ID fra databasen
	public void slettEn(int id)
	{
		db = DBHelper.getReadableDatabase();
		String whereClause = "_ID = " + id;
		db.delete(TABELL, whereClause, null);
	}
	//Metode for å slette alle fra databasen
	public void slettDBinnhold()
	{
		db = DBHelper.getWritableDatabase();
		String sql= "DELETE FROM ";
		db.execSQL(sql + TABELL);
	}
	//Metode for å oppdatere person i databasen etter redigering
	public void oppdater(int id, String fname, String ename, String number, String birthday)
	{
		db = DBHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		
		cv.put(FORNAVN, fname);
		cv.put(ETTERNAVN, ename);
		cv.put(NUMMER, number);
		cv.put(BURSDAG, birthday);
		
		String whereClause = "_ID = " + id;
		db.update(TABELL, cv, whereClause, null);
		db.close();
	}
	//Metode for å vise alle personer i listview
	public List<Person> visAlle()
	{
		List<Person> personListe = new ArrayList<Person>();
		String sql = "SELECT * FROM " + TABELL;
		db = DBHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery(sql, null);
		
		if(cursor.moveToFirst())
		{
			do
			{
				Person person = new Person();
				person.setID(Integer.parseInt(cursor.getString(0)));
				person.setFornavn(cursor.getString(1));
				person.setEtternavn(cursor.getString(2));
				person.setNummer(cursor.getString(3));
				person.setBursdag(cursor.getString(4));

				personListe.add(person);
				Collections.sort(personListe, new PersonNameComparator());
			}
			while(cursor.moveToNext());
		}
		db.close();
		return personListe;
	}
	//Metode for å finne en person
	public Cursor finnEn(Person person)
	{
		Cursor cur;
		cur = db.query(TABELL, cols, ID + "='" + person.getID() + "'", null, null, null, null);
		return cur;
	}
	//Metode for å hente ut antall personer i databasen
	public int getAntallPersonerDB() 
	{
		db = DBHelper.getReadableDatabase();
        String tellerQuery = "SELECT * FROM " + TABELL;
        
        Cursor cursor = db.rawQuery(tellerQuery, null);

        int count = cursor.getCount();
        db.close();
        return count;
    }
	//Metode som lager egen liste med personer som har bursdag når metoden er kalt
	public List<Person> getBirthdayPerson()
	{
		Calendar c = Calendar.getInstance();
		SimpleDateFormat f = new SimpleDateFormat("d/M");
		
		String fTid = f.format(c.getTime());
		
		db = DBHelper.getWritableDatabase();
		List<Person> bursdagsListe = new ArrayList<Person>();
		String sql = "SELECT * FROM " + TABELL + " WHERE bursdag = '" + fTid + "';";
		Cursor cursor = db.rawQuery(sql, null);
		
		if(cursor.moveToFirst())
		{
			do
			{
				Person bursdagsperson = new Person();
				bursdagsperson.setNummer(cursor.getString(3));	
				bursdagsListe.add(bursdagsperson);
			}
			while(cursor.moveToNext());
		}
		db.close();
		return bursdagsListe;
	}
	public class PersonNameComparator implements Comparator<Person> 
	{
	    public int compare(Person p1, Person p2) 
	    {
	        return p1.getFornavn().compareTo(p2.getFornavn());
	    }
	}
}
