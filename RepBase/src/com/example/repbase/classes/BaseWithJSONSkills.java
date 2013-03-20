package com.example.repbase.classes;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.repbase.Common;
import com.example.repbase.DBInterface;

public class BaseWithJSONSkills extends Base {
	public BaseWithJSONSkills(int id) throws JSONException,
			InterruptedException, ExecutionException, TimeoutException {
		this(DBInterface.getBaseById(id));
	}
	
	public BaseWithJSONSkills(String name) throws JSONException,
			InterruptedException, ExecutionException, TimeoutException {
		this(DBInterface.getBaseByName(name));
	}
	
	public BaseWithJSONSkills(JSONObject jo) throws JSONException{
		super(jo.getInt("ID"),
				Common.getSpecifiedAttribute(jo, "Name"),
				Common.getSpecifiedAttribute(jo, "City"),
				Common.getSpecifiedAttribute(jo, "Address"),
				jo.getString("Description"),
				Boolean.valueOf(Common.getSpecifiedAttribute(jo, "Deleted")),
				Common.convJSONArrToIntArrL(jo.getJSONArray("RoomIDs")),
				Common.convJSONArrToIntArrL(jo.getJSONArray("UserIDs")));
	}
	
	@Override
	public String toString() {
		return this.getName();
	}
}
