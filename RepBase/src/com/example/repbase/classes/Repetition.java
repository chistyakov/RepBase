package com.example.repbase.classes;

import java.util.Date;
import java.util.List;

public class Repetition {
	private int id;
	private int groupId;
	private int timeId;
	private int payed;
	private Date payedDate;
	private Date d_date;
	private boolean confirmed;
	private boolean cancelled;
	private List<Integer> servicesIds;
	
	public int getId() {
		return id;
	}
	protected void setId(int id) {
		this.id = id;
	}
	
	public int getGroupId() {
		return groupId;
	}
	protected void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	
	public int getTimeId() {
		return timeId;
	}
	protected void setTimeId(int timeId) {
		this.timeId = timeId;
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
	
	public Date getD_date() {
		return d_date;
	}
	protected void setD_date(Date d_date) {
		this.d_date = d_date;
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

}
