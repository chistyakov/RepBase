package com.example.repbase.classes;

public class Attribute 
{
	public int ID;
	public String Value;
	
	public int TypeID;
	public String Type;
	
	public Attribute(int ID, String Value, int TypeID, String Type)
	{
		this.ID = ID;
		this.Value = Value;
		this.TypeID = TypeID;
		this.Type = Type;
	}
}
