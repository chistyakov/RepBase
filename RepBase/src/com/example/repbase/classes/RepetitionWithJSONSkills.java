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
				(Integer)Common.convJSONNullToNull(jo.get("GroupID")),
				jo.getInt("RepTimeID"),
				Common.convJSONStringToDate(jo.getString("Begin")),
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
	
	public static RepetitionWithJSONSkills createNewRepetition(
			RoomTimeWithJSONSkills roomTime, GroupWithJSONSkills group,
			Date date) throws InterruptedException, ExecutionException,
			TimeoutException, JSONException {
		JSONObject jo;
		if (group!=null)
			jo = DBInterface.createRepetition(roomTime.getRoomId(),
					group.getId(), roomTime.getId(), date);
		else
			jo = DBInterface.createRepetition(roomTime.getRoomId(), null,
					roomTime.getId(), date);

		RepetitionWithJSONSkills rep = new RepetitionWithJSONSkills(jo);
		return rep;
	}
	
	public static RepetitionWithJSONSkills createNewRepetition(int roomTimeId,
			GroupWithJSONSkills group, Date date) throws InterruptedException,
			ExecutionException, TimeoutException, JSONException {
		RoomTimeWithJSONSkills room = new RoomTimeWithJSONSkills(roomTimeId);
		return createNewRepetition(room, group, date);
	}	
	
}
