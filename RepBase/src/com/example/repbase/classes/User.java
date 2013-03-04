package com.example.repbase.classes;

// TODO: rename methods get...Status() to is...ed
// TODO: Lists encapsulation

import java.util.ArrayList;
import java.util.List;

import com.example.repbase.Common;

import android.util.Log;

public class User {

	private int id=0;
	private String nick="";
	private String password="";
	private String name = "";
	private String surname = "";
	private String phone="";
	private String email = "";
	private boolean deleted = false;
	private boolean banned = false;
	private boolean fullrights = false;
	private List<Integer> groupIDs = new ArrayList<Integer>();
	private List<Integer> baseIDs = new ArrayList<Integer>();
//	private List<Integer> repIDs = new ArrayList<Integer>();

	public User(){
		Log.d(Common.TEMP_TAG, "default constructor for User class was called.");
	}

	public User(int id, String nick, String password, String name,
			String surname, String phone, String email, boolean deleted,
			boolean banned, boolean fullrights, ArrayList<Integer> groupsIDs,
			ArrayList<Integer> baseIDs/*, ArrayList<Integer> repIDs*/) {
		this.id = id;
		this.nick = nick;
		this.password = password;
		this.name = name;
		this.surname = surname;
		this.phone = phone;
		this.email = email;
		this.deleted = deleted;
		this.banned = banned;
		this.fullrights = fullrights;
		this.groupIDs = groupsIDs;
		this.baseIDs = baseIDs;
//		this.repIDs = repIDs;
	}
	
	public User(User u) {
		this.id = u.id;
		this.nick = u.nick;
		this.password = u.password;
		this.name = u.name;
		this.surname = u.surname;
		this.phone = u.phone;
		this.email = u.email;
		this.banned = u.banned;
		this.deleted = u.deleted;
		this.fullrights = u.fullrights;
		this.groupIDs = u.groupIDs;
//		this.repIDs = u.repIDs;
		this.baseIDs = u.baseIDs;
	}

	// ID
	public int getId() {
		return id;
	}
	protected void setId(int id) {
		this.id=id;
	}

	// Nick
	public String getNick() {
		return nick;
	}
	protected void setNick(String nick) {
		this.nick=nick;
	}

	// Password
	public String getPassword() {
		return password;
	}

	protected void setPassword(String password) {
		this.password = password;
	}

	// Name
	public String getName() {
		return name;
	}
	
	protected void setName(String name) {
		this.name = name;
	}
	
	// Surname
	public String getSurname() {
		return surname;
	}
	
	protected void setSurname(String surname) {
		this.surname = surname;
	}

	// Phone
	public String getPhone() {
		return phone;
	}

	protected void setPhone(String phone) {
		this.phone = phone;
	}
	
	// Email
	public String getEmail() {
		return email;
	}
	
	protected void setEmail(String email) {
		this.email = email;
	}

	// deleted
	public boolean getDelStatus() {
		return deleted;
	}
	
	protected void markAsDeleted() {
		deleted = true;
	}
	
	protected void unMarkAsDeleted() {
		deleted = false;
	}
	
	// banned
	public boolean getBanStatus() {
		return banned;
	}
	
	protected void markAsBanned() {
		banned = true;
	}
	
	protected void unMarkAsBanned() {
		banned = false;
	}
	
	// fullrights
	public boolean getFullRightsStatus() {
		return fullrights;
	}
	protected void markAsAdmin() {
		fullrights=true;
	}
	protected void unMarkAsAdmin() {
		fullrights=false;
	}

	// GroupIDs
	public List<Integer> getGroupsIDList() {
		return groupIDs;
	}
	
	protected void setGroupsIDList(ArrayList<Integer> groupsIDs){
		this.groupIDs = groupsIDs;
	}

	// BaseIDs
	public List<Integer> getBaseIDList() {
		return baseIDs;
	}
	
	protected void setBaseIDList(ArrayList<Integer> baseIDs){
		this.baseIDs = baseIDs;
	}

//	// RepIDs
//	public List<Integer> getRepIDList() {
//		return repIDs;
//	}
//	
//	protected void setRepIDList(ArrayList<Integer> repIDs){
//		this.repIDs = repIDs;
//	}
}
