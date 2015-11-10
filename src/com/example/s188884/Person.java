package com.example.s188884;


public class Person
{
	int _id;
	String _fnavn;
	String _enavn;
	String _nummer;
	String _bursdag;
	
	public Person()
	{}
	
	public Person(String fornavn, String etternavn, String nummer, String bursdag)
	{
		this._fnavn = fornavn;
		this._enavn = etternavn;
		this._nummer = nummer;
		this._bursdag = bursdag;
	}
	
	//Get og set-metoder for Person
	public int getID()
	{
		return this._id;
	}
	public void setID(int id)
	{
		this._id = id;
	}
	
	public String getFornavn()
	{
		return this._fnavn;
	}
	public void setFornavn(String fornavn)
	{
		this._fnavn = fornavn;
	}
	
	public String getEtternavn()
	{
		return this._enavn;
	}
	public void setEtternavn(String etternavn)
	{
		this._enavn = etternavn;
	}
	
	public String getNummer()
	{
		return this._nummer;
	}
	public void setNummer(String nummer)
	{
		this._nummer = nummer;
	}
	
	public String getBursdag()
	{
		return this._bursdag;
	}
	public void setBursdag(String bursdag)
	{
		this._bursdag = bursdag;
	}
}
