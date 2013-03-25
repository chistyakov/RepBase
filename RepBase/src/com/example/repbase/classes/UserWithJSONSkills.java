package com.example.repbase.classes;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.example.repbase.Common;
import com.example.repbase.DBInterface;

// TODO: spyke - remove parseJSONObjOnFields()
public class UserWithJSONSkills extends User {
	// private String URL;
	
	/**
	 * this artificial field shows that user is "alive" can be used, for
	 * example, to expire user's session
	 * 
	 * @see MainActivity.java
	 */
	private boolean actuality = false; 	

	public UserWithJSONSkills() {
		super();
		actuality = false;
		// this.URL = "http://test-blabla.no-ip.org/DBService/DBService.svc/";
	};
	
	public UserWithJSONSkills(JSONObject jo) throws JSONException, InterruptedException, ExecutionException, TimeoutException{
		super(jo.getInt("ID"),
				jo.getString("Nick"),
				jo.getString("Password"),
				Common.getSpecifiedAttribute(jo, "Name"),
				Common.getSpecifiedAttribute(jo, "Surname"),
				DBInterface.deEncryptPhone(jo.getInt("ID")),
				Common.getSpecifiedAttribute(jo, "E-mail"),
				Boolean.parseBoolean(Common.getSpecifiedAttribute(jo, "Deleted")),
				Boolean.parseBoolean(Common.getSpecifiedAttribute(jo, "Banned")),
				Boolean.parseBoolean(Common.optSpecifiedAttribute(jo, "Full Rights", "FALSE")),
				Common.convJSONArrToIntArrL(jo.getJSONArray("GroupIDs")),
				Common.convJSONArrToIntArrL(jo.getJSONArray("BaseIDs"))/*,
				Common.convJSONArrToIntArrL(jo.getJSONArray("RepIDs"))*/);
		actuality = true;
	}
	
	public UserWithJSONSkills(int id) throws JSONException,
			InterruptedException, ExecutionException, TimeoutException {
		this(DBInterface.getUserByID(id));
	}

	public UserWithJSONSkills(String Nick, String Password, String Name,
			String Surname, String Phone, String Email)
			throws ExecutionException, InterruptedException, JSONException,
			TimeoutException {
		this(DBInterface
				.createUser(Nick, Password, Name, Surname, Phone, Email));
	}

	public UserWithJSONSkills(String Nick, String Password)
			throws ExecutionException, InterruptedException, JSONException,
			TimeoutException {
		
		// DBInterface.CheckAuth() method returns JSONObject
		// contains name "CheckAuthorizationResult" in case nick was found
		if (DBInterface.checkAuth(Nick, Password).getBoolean("CheckAuthorizationResult")) {
			parseJSONObjOnFields(DBInterface.getUserByNickname(Nick));
			this.actuality = true;
		} else
			throw new JSONException(
					"Authentication failed. The password is incorrect.");
	}
	
	/**
	 * private method fills all fields of user from JSON object
	 */
	private void parseJSONObjOnFields(JSONObject joUser) throws JSONException,
			InterruptedException, ExecutionException, TimeoutException {
		this.setId(joUser.getInt("ID"));
		this.setPassword(joUser.getString("Password"));
		this.setNick(joUser.getString("Nick"));
		
		// new JSON request is sent in DBInterface.DeEncryptPhone(String ID)
		this.setPhone(DBInterface.deEncryptPhone(joUser.getInt("ID")));

		// we can use Common.GetAttributesList() method with loop.
		// Common.getSpecifiedAttribute() is more slowly
		// but more clearly in my eyes
		this.setName(Common.getSpecifiedAttribute(joUser, "Name"));
		this.setSurname(Common.getSpecifiedAttribute(joUser, "Surname"));
		this.setEmail(Common.getSpecifiedAttribute(joUser, "E-mail"));

		// Common.OPTspecifiedAttribute is used instead of Common.getSpecifiedAttribute
		// because "Full Rights" attribute can be missed
		if (Boolean.parseBoolean(Common.optSpecifiedAttribute(joUser,
				"Full Rights", "FALSE")))
			this.markAsAdmin(); 
		// Common.GETspecifiedAttribute is used 
		if (Boolean.parseBoolean(Common.getSpecifiedAttribute(joUser,
				"Deleted")))
			this.markAsDeleted();
		if (Boolean.parseBoolean(Common.getSpecifiedAttribute(joUser,
				"Banned")))
			this.markAsBanned();
		
		this.setGroupsIDList(Common.convJSONArrToIntArrL(joUser.getJSONArray("GroupIDs")));
		this.setBaseIDList(Common.convJSONArrToIntArrL(joUser.getJSONArray("BaseIDs")));
	}

	/**
	 *  refresh all values from server
	 * @throws ExecutionException
	 * @throws InterruptedException
	 * @throws JSONException
	 * @throws TimeoutException
	 */
	public void refreshFromServer() throws ExecutionException, InterruptedException,
			JSONException, TimeoutException {
		parseJSONObjOnFields(DBInterface.getUserByID(getId()));
	}
	
	public boolean isActual() {
		return actuality;
	}
		
	public boolean checkPassword(String passw) throws ExecutionException,
			InterruptedException, JSONException, TimeoutException {
		JSONObject joCheckAuth = DBInterface.checkAuth(getNick(), passw);
		return joCheckAuth.getBoolean("CheckAuthorizationResult");
	}

	/**
	 * @return true if the value was changed
	 * @param name
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws JSONException
	 * @throws TimeoutException
	 */
	public boolean changeName(String name) throws InterruptedException,
			ExecutionException, JSONException, TimeoutException {
		if (name.equals(getName()))
			return false;
		else {
			DBInterface.changeUserName(getId(), name);
			// refresh function exchanges data with server
			refreshFromServer();
			return (name.equals(getName()));
		}
	}
	
	public boolean changeSurname(String surname) throws InterruptedException,
			ExecutionException, JSONException, TimeoutException {
		if (surname.equals(getSurname()))
			return false;
		else {
			DBInterface.changeUserSurname(getId(), surname);
			refreshFromServer();
			return (surname.equals(getSurname()));
		}
	}
	
	public boolean changeNick(String nick) throws InterruptedException,
			ExecutionException, JSONException, TimeoutException {
		Log.d(Common.TEMP_TAG, "changeNick method with parametr "+ nick+ " was called");
		if (nick.equals(getNick()))
			return false;
		else {
			DBInterface.changeUserNick(getId(), nick);
			refreshFromServer();
			return (nick.equals(getNick()));
		}
	}
	
	public boolean changeEmail(String email) throws InterruptedException,
			ExecutionException, JSONException, TimeoutException {
		if (email.equals(getEmail()))
			return false;
		else {
			DBInterface.changeUserEmail(getId(), email);
			refreshFromServer();
			return (email.equals(getEmail()));
		}
	}
	
	public boolean changePhone(String phone) throws InterruptedException,
			ExecutionException, JSONException, TimeoutException {
		if (phone.equals(getPhone()))
			return false;
		else {
			DBInterface.changeUserPhone(getId(), phone);
			refreshFromServer();
			return (phone.equals(getPhone()));
		}
	}
	
	public boolean changePassword(String password) throws ExecutionException,
			InterruptedException, JSONException, TimeoutException {
		Log.d(Common.TEMP_TAG, "METHOD changePassword() was started");
		if (checkPassword(password))
			return false;
		else {
			DBInterface.ñhangeUserPassword(getId(), password);
			refreshFromServer();
			return checkPassword(password);
		}
	}
	
	public boolean changeProfileContent(String nick, String name, String surname,
			String email, String phone, String password)
			throws InterruptedException, ExecutionException, JSONException,
			TimeoutException {
		return (changeNick(nick) |
				changeName(name) |
				changeSurname(surname) |
				changeEmail(email) |
				changePhone(phone) |
				checkPassword(password));
	}
	
	public boolean changeProfileContent(String nick, String name, String surname,
			String email, String phone) throws InterruptedException,
			ExecutionException, JSONException, TimeoutException {
		return (changeNick(nick) |
				changeName(name) |
				changeSurname(surname) |
				changeEmail(email) |
				changePhone(phone));
	}
	
	/** 
	 * method doesn't change the password!!
	 * @param u User to set profile data
	 * @return true if something was changed
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws JSONException
	 * @throws TimeoutException
	 */
	public boolean changeProfileContent(User u) throws InterruptedException,
			ExecutionException, JSONException, TimeoutException {
		return (changeNick(u.getNick()) | 
				changeName(u.getName()) |
				changeSurname(u.getSurname()) |
				changeEmail(u.getEmail()) |
				changePhone(u.getPhone()));
		// we can't change password because User contains only encrypted password
		// but DBInterface.ChangePassword requires non-encrypted passw 
	}
	
	public boolean delete() throws InterruptedException, ExecutionException,
			JSONException, TimeoutException {
		DBInterface.deleteUser(getId());
		refreshFromServer();
		return (isDeleted());
	}
	
	
	public List<GroupWithJSONSkills> getGroupsList()
			throws InterruptedException, ExecutionException, JSONException,
			TimeoutException {
		List<GroupWithJSONSkills> lGrous = new ArrayList<GroupWithJSONSkills>();
		for (int groupId : getGroupsIDList()) {
			GroupWithJSONSkills group = new GroupWithJSONSkills(groupId);
			if(!group.isDeleted())
				lGrous.add(group);
		}
		return lGrous;
	}
}
