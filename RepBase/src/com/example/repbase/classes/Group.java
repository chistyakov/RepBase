package com.example.repbase.classes;

import java.util.ArrayList;
import java.util.List;

public class Group {
	private int id;
	private String name;
	private String accessCode;
	private int accessCode_Num = 1;
	private boolean deleted = false;
	private List<Integer> userIds = new ArrayList<Integer>();
	
	public Group(){
		
	}
	
	public Group(int id, String name, String accessCode, int accessCode_Num){
		this.id = id;
		this.name = name;
		this.accessCode = accessCode;
		this.accessCode_Num = accessCode_Num;
	}

	public Group(int id, String name, String accessCode, int accessCode_Num, boolean deleted, List<Integer> userIds){
		this.id = id;
		this.name = name;
		this.accessCode = accessCode;
		this.accessCode_Num = accessCode_Num;
		this.deleted = deleted;
		this.userIds = userIds;
	}
	
	public int getId(){
		return id;
	}
	protected void setId(int id){
		this.id = id;
	}
	
	public String getName(){
		return name;
	}
	protected void setName(String name) {
		this.name = name;
	}
	
	public String getAccessCode(){
		return accessCode;
	}
	protected void setAccessCode(String accessCode){
		this.accessCode = accessCode;
		++this.accessCode_Num;
	}
	public int getAccessCodeNum(){
		return accessCode_Num;
	}
	
	public boolean isDeleted(){
		return deleted;
	}
	protected void markAsDeleted(){
		this.deleted = true;
	}
	protected void unMarkAsDeleted(){
		this.deleted = false;
	}

	public List<Integer> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<Integer> userIds) {
		this.userIds = userIds;
	}
	
	public String toStringFullInfo() {
		String strCommonInfo = "id: " + this.id 
				+ ", name: " + this.name
				+ ", accessCode: " + this.accessCode
				+ ", accessCode num: " + this.accessCode_Num
				+ ", deleted: " + this.deleted;
		StringBuilder sbUsers = new StringBuilder();
		sbUsers.append("rooms: [");
		for (Integer i : userIds)
		{
			sbUsers.append(i);
			sbUsers.append("; ");
		}
		sbUsers.append("]");

		return strCommonInfo+", "+sbUsers;
	}
}
