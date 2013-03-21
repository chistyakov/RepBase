package com.example.repbase.classes;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.repbase.Common;
import com.example.repbase.DBInterface;

public class RepTimeWithJSONSkills extends RepTime {
	public RepTimeWithJSONSkills(int id) throws JSONException,
			InterruptedException, ExecutionException, TimeoutException {
		this(DBInterface.getRepTimeByID(id));
	}
	
	public RepTimeWithJSONSkills(JSONObject jo) throws JSONException {
		super(jo.getInt("ID"),
				Common.convJSONStringToDate(jo.getString("Begin")),
				Common.convJSONStringToDate(jo.getString("End")),
				jo.getBoolean("Deleted"),
				jo.getInt("DayOfWeek"),
				jo.getInt("Cost"),
				jo.getInt("RoomID"));
//		Log.d(Common.TEMP_TAG+"1", "date: " + String.valueOf(Common.convJSONStringToDate(jo.getString("Begin"))));
//		Log.d(Common.TEMP_TAG+"1", "calendar: " + String.valueOf(Common.convJSONStringToCal(jo.getString("Begin"))));
	}
	
}
