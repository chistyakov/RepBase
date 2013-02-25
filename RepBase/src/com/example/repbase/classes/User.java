package com.example.repbase.classes;

import java.util.ArrayList;
import java.util.List;

import com.example.repbase.Common;

import android.util.Log;

public class User {

	// TODO: methods for GroupIDs, BaseIDs and RepIDs
	// see at the bottom
	
	// TODO: ask Igor about BaseIDs
	
	private int ID=0;
	private String Nick="";
	private String Password="";
	private String Phone="";
	private List<Integer> GroupIDs = new ArrayList<Integer>();
	private List<Integer> BaseIDs = new ArrayList<Integer>();
	private List<Integer> RepIDs = new ArrayList<Integer>();

	// private fields added by Sasha
	// I guess user~person
	// I mean "user" has got name, surname, email, etc.
	private String Name = "";
	private String Surname = "";
	private String Email = "";
	private boolean deleted = false;
	private boolean banned = false;
	private boolean fullrights = false;

//	public User(int id, String Nick, String Password, String Phone) {
//		this.ID = id;
//		this.Nick = Nick;
//		this.Password = Password;
//		this.Phone = Phone;
//	}
	
	public User(){
		Log.d(Common.TEMP_TAG, "default constructor for User class was called.");
	}

	public User(int id, String Nick, String Password, String Name,
			String Surname, String Phone, String Email, boolean deleted,
			boolean banned, boolean fullrights, ArrayList<Integer> GroupsIDs,
			ArrayList<Integer> BaseIDs, ArrayList<Integer> RepIDs) {
		this.ID = id;
		this.Nick = Nick;
		this.Password = Password;
		this.Name = Name;
		this.Surname = Surname;
		this.Email = Email;
		this.Phone = Phone;
		this.deleted = deleted;
		this.banned = banned;
		this.fullrights = fullrights;
		this.GroupIDs = GroupsIDs;
		this.BaseIDs = BaseIDs;
		this.RepIDs = RepIDs;
	}
	
	public User(User u) {
		this.ID = u.ID;
		this.Nick = u.Nick;
		this.Name = u.Name;
		this.Surname = u.Surname;
		this.Phone = u.Phone;
		this.Password = u.Password;
		this.Email = u.Email;
		this.banned = u.banned;
		this.deleted = u.deleted;
		this.fullrights = u.fullrights;
//		this.GroupIDs = new ArrayList<Integer>(u.GroupIDs);
//		this.BaseIDs = new ArrayList<Integer>(u.BaseIDs);
//		this.RepIDs = new ArrayList<Integer>(u.RepIDs);
		this.GroupIDs = u.GroupIDs;
		this.RepIDs = u.RepIDs;
		this.BaseIDs = u.BaseIDs;
	}

	// Name
	public String getName() {
		return Name;
	}

	protected void setName(String name) {
		this.Name = name;
	}

	// Surname
	public String getSurname() {
		return Surname;
	}

	protected void setSurname(String surname) {
		this.Surname = surname;
	}

	// Email
	public String getEmail() {
		return Email;
	}

	protected void setEmail(String email) {
		this.Email = email;
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


	// ID
	public int getId() {
		return ID;
	}
	protected void setId(int id) {
		this.ID=id;
	}

	// Nick
	public String getNick() {
		return Nick;
	}
	protected void setNick(String nick) {
		this.Nick=nick;
	}

	// Password
	public String getPassword() {
		return Password;
	}

	protected void setPassword(String password) {
		this.Password = password;
	}

	// Phone
	public String getPhone() {
		return Phone;
	}

	protected void setPhone(String phone) {
		this.Phone = phone;
	}

	// GroupIDs
	public List<Integer> getGroupsIDList() {
		return GroupIDs;
	}
	
	protected void setGroupsIDList(ArrayList<Integer> GroupsIDs){
		this.GroupIDs = GroupsIDs;
	}

	// BaseIDs
	public List<Integer> getBaseIDList() {
		return BaseIDs;
	}
	
	protected void setBaseIDList(ArrayList<Integer> BaseIDs){
		this.BaseIDs = BaseIDs;
	}

	// RepIDs
	public List<Integer> getRepIDList() {
		return RepIDs;
	}
	
	protected void setRepIDList(ArrayList<Integer> RepIDs){
		this.RepIDs = RepIDs;
	}
}
