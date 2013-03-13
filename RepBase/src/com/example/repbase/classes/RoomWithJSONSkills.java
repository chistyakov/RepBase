package com.example.repbase.classes;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.repbase.Common;
import com.example.repbase.DBInterface;

public class RoomWithJSONSkills extends Room {
	public RoomWithJSONSkills(int id) throws NumberFormatException,
			JSONException, InterruptedException, ExecutionException,
			TimeoutException {
		this(DBInterface.getRoomByID(id));
	}
	public RoomWithJSONSkills(JSONObject jo) throws NumberFormatException,
			JSONException {
		super(jo.getInt("ID"),
				Common.getSpecifiedAttribute(jo, "Name"),
				jo.getInt("BaseID"),
				Integer.valueOf(Common.getSpecifiedAttribute(jo, "Square")),
				Boolean.valueOf(Common.getSpecifiedAttribute(jo, "Deleted")),
				jo.getString("Description"),
				Common.convJSONArrToIntArrL(jo.getJSONArray("ConditionIDs")));
	}
}
