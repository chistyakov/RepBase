package com.example.repbase.classes;

import java.util.ArrayList;
import java.util.List;

public class Base {
	private int id;
	private String name;
	private String city;
	private String address;
	private String description;
	private boolean deleted;
	private List<Integer> roomIds = new ArrayList<Integer>();
	private List<Integer> userIds = new ArrayList<Integer>();
	
	public Base(){
		
	}

	public Base(int id, String name, String city, String address,
			String description, boolean deleted, List<Integer> roomIds,
			List<Integer> userIds) {
		this.id = id;
		this.name = name;
		this.city = city;
		this.address = address;
		this.description = description;
		this.deleted = deleted;
		this.roomIds = roomIds;
		this.userIds = userIds;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Integer> getRoomIds() {
		return roomIds;
	}

	public void setRoomIds(List<Integer> rooms) {
		this.roomIds = rooms;
	}

	public List<Integer> getUserIds() {
		return userIds;
	}

	protected void setUserIds(List<Integer> userIds) {
		this.userIds = userIds;
	}

	public boolean isDeleted() {
		return deleted;
	}

	protected void markAsDeleted() {
		this.deleted = true;
	}
	
	protected void unMarkAsDeleted() {
		this.deleted = false;
	}

	public String toStringFullInfo() {
		String strCommonInfo = "id: " + this.id + ", name: " + this.name
				+ ", city: " + this.city + ", address: " + this.address
				+ ", description: " + this.description + ", deleted: "
				+ this.deleted;
		StringBuilder sbRooms = new StringBuilder();
		sbRooms.append("rooms: [");
		for (Integer i:roomIds)
		{
			sbRooms.append(i);
			sbRooms.append("; ");
		}
		sbRooms.append("]");

		StringBuilder sbUsers = new StringBuilder();
		sbUsers.append("users: [");
		for (Integer i:roomIds)
		{
			sbUsers.append(i);
			sbUsers.append("; ");
		}
		sbUsers.append("]");
		
		return strCommonInfo+", "+sbRooms+", "+sbUsers;
	}
	
}
