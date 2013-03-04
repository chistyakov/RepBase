package com.example.repbase.classes;

import java.util.ArrayList;
import java.util.List;

public class Base {
	private int id;
	private String name;
	private String city;
	private String address;
	private String description;
	private List<Integer> rooms = new ArrayList<Integer>();
	
	public Base(){
		
	}
	public Base(int id, String name, String city, String address, String description, List<Integer> rooms){
		this.id = id;
		this.name = name;
		this.city = city;
		this.address = address;
		this.description = description;
		this.rooms = rooms;
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

	public List<Integer> getRooms() {
		return rooms;
	}

	public void setRooms(List<Integer> rooms) {
		this.rooms = rooms;
	}
	
}
