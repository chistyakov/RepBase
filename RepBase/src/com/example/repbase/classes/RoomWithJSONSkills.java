package com.example.repbase.classes;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Pair;

import com.example.repbase.Common;
import com.example.repbase.DBInterface;
import com.example.repbase.WrongTimeRangeException;

public class RoomWithJSONSkills extends Room {
	public RoomWithJSONSkills(int id) throws NumberFormatException,
			JSONException, InterruptedException, ExecutionException,
			TimeoutException {
		this(DBInterface.getRoomByID(id));
	}

	public RoomWithJSONSkills(JSONObject jo) throws NumberFormatException,
			JSONException {
		super(jo.getInt("ID"), Common.getSpecifiedAttribute(jo, "Name"), jo
				.getInt("BaseID"), Integer.valueOf(Common
				.getSpecifiedAttribute(jo, "Square")), Boolean.valueOf(Common
				.getSpecifiedAttribute(jo, "Deleted")), jo
				.getString("Description"), Common.convJSONArrToIntArrL(jo
				.getJSONArray("ConditionIDs")));
	}

	/**
	 * 
	 * @return Pair<minCost, maxCost>
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws TimeoutException
	 * @throws JSONException
	 * @throws WrongTimeRangeException
	 *             if there isn't timeRep with correct time range for room
	 */
	public Pair<Double, Double> getCostRange() throws InterruptedException,
			ExecutionException, TimeoutException, JSONException,
			WrongTimeRangeException {
		List<RoomTimeWithJSONSkills> roomTimeList = this.getTimeList();
		if (roomTimeList.size() == 0)
			throw new WrongTimeRangeException();

		double maxCost = 0.0;
		double minCost = Double.MAX_VALUE;
		boolean flagCorrectCostExist = false;
		for (RoomTimeWithJSONSkills roomTime : roomTimeList) {
			try {
				double cost = roomTime.getCostPerHour();
				// flag is set true if there is repTime with correct time range
				// for the room
				flagCorrectCostExist = true;
				if (cost > maxCost)
					maxCost = cost;
				if (cost < minCost)
					minCost = cost;
			} catch (WrongTimeRangeException e) {
				continue;
			}
		}
		if (!flagCorrectCostExist)
			throw new WrongTimeRangeException();
		else
			return new Pair<Double, Double>(minCost, maxCost);
	}

	public List<RoomTimeWithJSONSkills> getTimeList()
			throws InterruptedException, ExecutionException, TimeoutException,
			JSONException {
		return DBInterface.getRoomTimesList(this.getId());
	}

	public List<RoomTimeWithJSONSkills> getRepsList(int dayOfWeek)
			throws InterruptedException, ExecutionException, TimeoutException,
			JSONException {
		return DBInterface.getRoomTimesList(this.getId(), dayOfWeek);
	}
}
