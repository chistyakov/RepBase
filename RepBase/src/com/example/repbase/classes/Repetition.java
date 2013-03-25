package com.example.repbase.classes;

import java.util.Date;
import java.util.List;

public class Repetition {
	private int id;
	private Integer groupId;
	private int roomTimeId;
	private Date date;
	private int payed;
	private Date payedDate;
	private boolean confirmed;
	private boolean cancelled;
	private List<Integer> servicesIds;
	
	public Repetition(){
		
	}
	
	public Repetition(int id, Integer groupId, int roomTimeId, Date date, int payed,
			Date payedDate, boolean confirmed, boolean cancelled,
			List<Integer> servicesIds) {
		this.id = id;
//		this.groupId = (groupId != null) ? groupId : -1;
//		this.groupId = new Integer(groupId);
		this.groupId = groupId;
		this.roomTimeId = roomTimeId;
		this.date = date;
		this.payed = payed;
		this.payedDate = payedDate;
		this.confirmed = confirmed;
		this.cancelled = cancelled;
		this.servicesIds = servicesIds;
	}
	
	public int getId() {
		return id;
	}
	protected void setId(int id) {
		this.id = id;
	}
	
	public Integer getGroupId() {
		return groupId;
	}
	protected void setGroupId(int groupId) {
		this.groupId = Integer.valueOf(groupId);
	}
	
	public int getRoomTimeId() {
		return roomTimeId;
	}
	protected void setRoomTimeId(int roomTimeId) {
		this.roomTimeId = roomTimeId;
	}
	
	public int getPayed() {
		return payed;
	}
	protected void setPayed(int payed) {
		this.payed = payed;
	}
	
	public Date getPayedDate() {
		return payedDate;
	}
	protected void setPayedDate(Date payedDate) {
		this.payedDate = payedDate;
	}
	
	public boolean isConfirmed() {
		return confirmed;
	}
	protected void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}
	
	public boolean isCancelled() {
		return cancelled;
	}
	protected void markAsCancelled() {
		this.cancelled = true;
	}
	protected void unMarkAsCancelled() {
		this.cancelled = false;
	}
	
	public List<Integer> getServicesIds() {
		return servicesIds;
	}
	public void setServicesIds(List<Integer> servicesIds) {
		this.servicesIds = servicesIds;
	} 
	
	
	public String toStringFullInfo() {
		String strCommonInfo = "id: " + this.id 
				+ ", groupId: " + this.groupId
				+ ", roomTimeId: " + this.roomTimeId
				+ ", date: " + this.date
				+ ", payed: " + this.payed
				+ ", payed date: " + this.payedDate
				+ ", confirmed: " + this.confirmed 
				+ ", cancelled: " + this.cancelled;
		StringBuilder sbServices = new StringBuilder();
		sbServices.append("services: [");
		for (Integer i : servicesIds)
		{
			sbServices.append(i);
			sbServices.append("; ");
		}
		sbServices.append("]");

		return strCommonInfo+", "+sbServices;
	}

	public Date getDate() {
		return date;
	}

	protected void setDate(Date date) {
		this.date = date;
	}

}
