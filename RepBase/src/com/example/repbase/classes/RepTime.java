package com.example.repbase.classes;

import java.util.Date;

public class RepTime {
	private int id = 0;
	private Date begin = new Date();
	private Date end = new Date();
	private boolean deleted = false;
	private int dayOfWeek = 1;
	private int cost = 0;
	private int roomId = 0;
	
	public RepTime(){
		
	}
	
	public RepTime(int id, Date begin, Date end, boolean deleted,
			int dayOfWeek, int cost, int roomId) {
		this.id = id;
		this.begin = begin;
		this.end = end;
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
	 * @return the begin
	 */
	public Date getBegin() {
		return begin;
	}

	/**
	 * @param begin the begin to set
	 */
	protected void setBegin(Date begin) {
		this.begin = begin;
	}

	/**
	 * @return the end
	 */
	public Date getEnd() {
		return end;
	}

	/**
	 * @param end the end to set
	 */
	protected void setEnd(Date end) {
		this.end = end;
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
				+ ", begin: " + this.begin
				+ ", end: " + this.end
				+ ", deleted: " + this.deleted
				+ ", dayOfWeek: " + this.dayOfWeek
				+ ", cost: " + this.cost
				+ ", roomId: " + this.roomId;
		return strCommonInfo;
	}
	
}
