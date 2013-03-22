package com.example.repbase.classes;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.util.Log;

import com.example.repbase.Common;
import com.example.repbase.WrongTimeRangeException;

public class RoomTime {
	private int id = 0;
	private Date startTime = new Date();
	private Date endTime = new Date();
	private boolean deleted = false;
	private int dayOfWeek = 1;
	private int cost = 0;
	private int roomId = 0;
	
	public RoomTime(){
		
	}
	
	public RoomTime(int id, Date startTime, Date endTime, boolean deleted,
			int dayOfWeek, int cost, int roomId) {
		this.id = id;
		this.startTime = startTime;
		this.endTime = endTime;
		this.deleted = deleted;
		this.dayOfWeek = dayOfWeek;
		this.cost = cost;
		this.roomId = roomId;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	protected void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the startTime
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime the startTime to set
	 */
	protected void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the endTime
	 */
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime the endTime to set
	 */
	protected void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	/**
	 * @return the deleted
	 */
	public boolean isDeleted() {
		return deleted;
	}

	protected void markAsDeleted() {
		this.deleted = true;
	}
	protected void unMarkAsDeleted() {
		this.deleted = false;
	}

	/**
	 * @return the dayOfWeek
	 */
	public int getDayOfWeek() {
		return dayOfWeek;
	}

	/**
	 * @param dayOfWeek the dayOfWeek to set
	 */
	protected void setDayOfWeek(int dayOfWeek) {
		if(dayOfWeek <=7 & dayOfWeek >=1)
			this.dayOfWeek = dayOfWeek;
		else
			throw new IndexOutOfBoundsException(
					"The day of week must be between 1 and 7");
	}

	/**
	 * @return the cost
	 */
	public int getCost() {
		return cost;
	}

	/**
	 * @param cost the cost to set
	 */
	protected void setCost(int cost) {
		this.cost = cost;
	}

	/**
	 * @return the roomId
	 */
	public int getRoomId() {
		return roomId;
	}

	/**
	 * @param roomId the roomId to set
	 */
	protected void setRoomId(int roomId) {
		this.roomId = roomId;
	}
	
	public String toStringFullInfo() {
		String strCommonInfo = "id: " + this.id
				+ ", begin: " + this.startTime
				+ ", end: " + this.endTime
				+ ", deleted: " + this.deleted
				+ ", dayOfWeek: " + this.dayOfWeek
				+ ", cost: " + this.cost
				+ ", roomId: " + this.roomId;
		return strCommonInfo;
	}
	
	/**
	 * get cost per hour for repetition
	 * @return double cost/(end-begin) 
	 * @throws WrongTimeRangeException if end<=begin
	 */
	public double getCostPerHour() throws WrongTimeRangeException {

			double diffHours = 0.0;
			Calendar calendarBegin = new GregorianCalendar(Common.TZONE, Common.LOC);
			calendarBegin.setTime(startTime);
			Calendar calendarEnd = new GregorianCalendar(Common.TZONE, Common.LOC);
			calendarEnd.setTime(endTime);
			
//			// repTime can't start on one day and finish on other
//			if (calendarEnd.get(Calendar.DAY_OF_YEAR) != calendarBegin.get(Calendar.DAY_OF_YEAR))
//				return -1.0;
			
			diffHours = calendarEnd.get(Calendar.HOUR_OF_DAY) - calendarBegin.get(Calendar.HOUR_OF_DAY);
			diffHours += ((calendarEnd.get(Calendar.MINUTE) - calendarBegin.get(Calendar.MINUTE))/60);
			diffHours += ((calendarEnd.get(Calendar.SECOND) - calendarBegin.get(Calendar.SECOND))/(60*60));
			
			Log.d(Common.TEMP_TAG, "diffHours: " + diffHours);
			
			if (diffHours <= 0.0)
				throw new WrongTimeRangeException();
			return cost / diffHours;
	}
	
}
