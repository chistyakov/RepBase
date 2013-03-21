package com.example.repbase.classes;

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
				(Integer)Common.convJSONNullToNull(jo.get("GroupID")),
				jo.getInt("RepTimeID"),
				jo.getInt("Payed"),
				Common.convJSONStringToDate(jo.getString("PayedDate")),
				jo.getBoolean("Confirmed"),
				jo.getBoolean("Cancelled"),
				Common.convJSONArrToIntArrL(jo.getJSONArray("Services")));
	}
	
	public void cancel() throws InterruptedException, ExecutionException, JSONException, TimeoutException {
		try {
			DBInterface.cancelRepetition(this.getId());
		} catch(JSONException e) {
			throw (new JSONException(e.getMessage()));
		}
		this.markAsCancelled();			
	}
	
	
}
