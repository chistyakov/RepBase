package com.example.repbase.classes;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.repbase.Common;
import com.example.repbase.DBInterface;

public class GroupWithJSONSkills extends Group {
	public GroupWithJSONSkills(JSONObject jo) throws JSONException {
		super(jo.getInt("ID"),
				Common.getSpecifiedAttribute(jo, "Name"),
				jo.getString("AccessCode"),
				Integer.parseInt(Common.getSpecifiedAttribute(jo, "AccessCode_Num")),
				Boolean.parseBoolean(Common.getSpecifiedAttribute(jo, "Deleted")),
				Common.convJSONArrToIntArrL(jo.getJSONArray("UserIDs")));
	}
	
	public GroupWithJSONSkills(int id) throws InterruptedException,
			ExecutionException, JSONException, TimeoutException {
		this(DBInterface.getGroupByID(id));
	}
	
	public GroupWithJSONSkills(String name) throws JSONException,
			InterruptedException, ExecutionException, TimeoutException {
		this(DBInterface.getGroupByName(name));
	}
	
	/**
	 * method is used by ArrayAdapter
	 * 
	 * @return group's name
	 */
	@Override
	public String toString() {
		return this.getName();
	}
	
	public String generateNewAccessCode() throws InterruptedException, ExecutionException, TimeoutException, JSONException{
		DBInterface.generateNewAccessCode(this.getId());
		JSONObject jo = DBInterface.getGroupByID(this.getId());
		this.setAccessCode(jo.getString("AccessCode"));
		return this.getAccessCode();
	}
	
	public void deleteUser(int userId) throws InterruptedException, ExecutionException, TimeoutException, JSONException{
		if(this.getUserIds().contains(userId)){
			DBInterface.deleteUserFromGroup(userId, this.getId());

			// Integer.valueOf(int i) is used to don't delete user with location
			// "userId" from list, but delete user with id == "userId"
			this.getUserIds().remove(Integer.valueOf(userId)); 
		}
		else throw new IndexOutOfBoundsException("The group doesn't contain required user");
	}
	public void deleteUser(UserWithJSONSkills u) throws InterruptedException, ExecutionException, TimeoutException, JSONException{
		deleteUser(u.getId());
//		u.refreshFromServer();
		u.getGroupsIDList().remove(Integer.valueOf(this.getId()));
	}
	
	/**
	 * Static factory method. Creates new group
	 * @param name
	 * @return created GroupWithJSONSkills object binded with DB
	 * @throws JSONException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws TimeoutException
	 */
	public static GroupWithJSONSkills createNewGroup(String name) throws JSONException, InterruptedException, ExecutionException, TimeoutException{
		return new GroupWithJSONSkills(DBInterface.createGroup(name));
	}

	/**
	 * Static factory method. Creates new group
	 * @param name
	 * @param userToUpdate new group will be added to user's groups list
	 * @return created GroupWithJSONSkills object binded with DB
	 * @throws JSONException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws TimeoutException
	 */
	public static GroupWithJSONSkills createNewGroup(String name,
			UserWithJSONSkills userToUpdate) throws JSONException,
			InterruptedException, ExecutionException, TimeoutException {
		GroupWithJSONSkills group = GroupWithJSONSkills.createNewGroup(name);
		userToUpdate.getGroupsIDList().add(group.getId());
		return group;
	}
	
	public void delete() throws InterruptedException, ExecutionException,
			TimeoutException, JSONException {
		DBInterface.deleteGroup(getId());
		this.markAsDeleted();
	}

	/**
	 * add user to group
	 * 
	 * @param userId
	 * @param accessCode
	 * @return true if user is successfully added, false if accessCode is wrong
	 * @throws Exception
	 *             if user already exists in group or error on server side
	 *             occurred
	 */
	public boolean addUser(int userId, String accessCode) throws Exception {
		if (this.getUserIds().contains(userId))
			throw new Exception("Duplicated users");
		// not sure that this.getAceesCode() should be checked
		// e.g. first user got a group from DB
		// then second user got the group from DB
		// then second user generated a new code
		// then first user tries to enter to the group with the new code
		// but group's object for first user contains the old code
		if (accessCode.equals(this.getAccessCode())
				&& DBInterface.addUserToGroup(userId, this.getId(), accessCode)) {
			return true;
		}
		return false;
	}
	public boolean addUser(UserWithJSONSkills u, String accessCode)
			throws Exception {
		if (this.addUser(u.getId(), accessCode)) {
//			u.refreshFromServer();
			u.getGroupsIDList().add(Integer.valueOf(this.getId()));
			return true;
		}
		return false;
	}
}
