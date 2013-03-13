package com.example.repbase.classes;

import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.repbase.Common;
import com.example.repbase.DBInterface;

public class RepetitionWithJSONSkills extends Repetition {

	public RepetitionWithJSONSkills(int repId) throws JSONException,
			InterruptedException, ExecutionException, TimeoutException {
		this(DBInterface.getRepetitionByID(repId));
	}

	public RepetitionWithJSONSkills(JSONObject jo) throws JSONException{
		super(jo.getInt("ID"),
//				(Integer)checkForNull(jo, "GroupID"),
				(Integer)convJSONNullToNull(jo.get("GroupID")),
				jo.getInt("RepTimeID"),
				jo.getInt("Payed"),
				Common.convJSONStringToDate(jo.getString("PayedDate")),
				jo.getBoolean("Confirmed"),
				jo.getBoolean("Cancelled"),
				Common.convJSONArrToIntArrL(jo.getJSONArray("Services")));
	}
	
	private static Object checkForNull(JSONObject jo, String param)
			throws JSONException {
		return jo.isNull(param) ? null : jo.get(param);
//		if(jo.isNull(param))
//			return null;
//		else
//			return jo.get(param);
	}
	
	private static Object convJSONNullToNull(Object o) {
//		if (JSONObject.NULL.equals(o))
//			return null;
//		else
//			return o;
		
		return o.equals(JSONObject.NULL) ? null : o;
	}
	
	
}
