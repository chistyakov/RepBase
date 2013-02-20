package com.example.repbase.classes;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.example.repbase.Common;
import com.example.repbase.DBInterface;

// TODO: check JSONObject, returned by DBInterface.CreateUser
public class UserWithJSONskills extends User {
	// private String URL;
	private boolean actuality = false; 	// this artificial field shows that user
										// is "alive"
										// can be used, for example, to expire
										// user's session
										// also is used in MainActivity.java

	public UserWithJSONskills() {
		super();
		actuality = false;
		// this.URL = "http://test-blabla.no-ip.org/DBService/DBService.svc/";
	};

	public UserWithJSONskills(String Nick, String Password, String Phone,
			String Name, String Surname, String Email)
			throws ExecutionException, InterruptedException, JSONException,
			TimeoutException {
		JSONObject jo = new JSONObject();
		jo = DBInterface
				.CreateUser(Nick, Password, Name, Surname, Phone, Email);
			fillFields(jo);
			this.actuality = true;
			// this.URL = URL;
	}

	public UserWithJSONskills(String Nick, String Password)
			throws ExecutionException, InterruptedException, JSONException,
			TimeoutException {
		// DBInterface.CheckAuth() method returns JSONObject
		// contains name "CheckAuthorizationResult" in case nick was found
		// and name "Exception" otherwise
		// see GetJSONFromUrl.java
		JSONObject joCheckAuth = new JSONObject();
		joCheckAuth = DBInterface.CheckAuth(Nick, Password);
		if (joCheckAuth.getBoolean("CheckAuthorizationResult")) {
			JSONObject jo = new JSONObject();
			jo = DBInterface.GetUserByNickname(Nick);
			fillFields(jo);
			this.actuality = true;
		} else
			throw new JSONException(
					"Authentication failed. The password is incorrect.");
	}
	
	// private method fills all fields of user from JSON object
	private void fillFields(JSONObject joUser) throws JSONException,
			InterruptedException, ExecutionException, TimeoutException {
		this.setId(joUser.getInt("ID"));
		this.setPassword(joUser.getString("Password"));
		this.setNick(joUser.getString("Nick"));
		this.setPhone(joUser.getString("Phone"));
		
		// new JSON request is sent in DBInterface.DeEncryptPhone(String ID)
		this.setPhone(DBInterface.DeEncryptPhone(joUser.getInt("ID")));

		// we can use Common.GetAttributesList() method with loop.
		// Common.getSpecifiedAttribute() is more slowly
		// but more clearly in my eyes
		this.setName(Common.getSpecifiedAttribute(joUser, "Name"));
		this.setSurname(Common.getSpecifiedAttribute(joUser, "Surname"));
		this.setEmail(Common.getSpecifiedAttribute(joUser, "E-mail"));

		if (Boolean.parseBoolean(Common.optSpecifiedAttribute(joUser,
				"Full Rights", "FALSE")))
			this.markAsAdmin(); // Common.OPTspecifiedAttribute is used instead of Common.getSpecifiedAttribute
								// because "Full Rights" attribute can be missed
		if (Boolean.parseBoolean(Common.getSpecifiedAttribute(joUser,
				"Deleted")))
			this.markAsDeleted();
		if (Boolean.parseBoolean(Common.getSpecifiedAttribute(joUser,
				"Banned")))
			this.markAsBanned();	
	}

	// refresh all values from server
	public void refresh() throws ExecutionException, InterruptedException,
			JSONException, TimeoutException {
		JSONObject jo = new JSONObject();
		jo = DBInterface.GetUserByID(getId());
		fillFields(jo);
	}
	
	public boolean isActual() {
		return actuality;
	}
		
	public boolean checkPassword(String passw) throws ExecutionException,
			InterruptedException, JSONException, TimeoutException {
		JSONObject joCheckAuth = DBInterface.CheckAuth(getNick(), passw);
		return joCheckAuth.getBoolean("CheckAuthorizationResult");
	}

	// returns true if the value was changed
	public boolean changeName(String name) throws InterruptedException,
			ExecutionException, JSONException, TimeoutException {
		if (name.equals(getName()))
			return false;
		else {
			DBInterface.ChangeUserName(getId(), name);
			// refresh function exchanges data with server
			refresh();
			return (name.equals(getName()));
		}
	}
	
	public boolean changeSurname(String surname) throws InterruptedException,
			ExecutionException, JSONException, TimeoutException {
		if (surname.equals(getSurname()))
			return false;
		else {
			DBInterface.ChangeUserSurname(getId(), surname);
			refresh();
			return (surname.equals(getSurname()));
		}
	}
	
	public boolean changeNick(String nick) throws InterruptedException,
			ExecutionException, JSONException, TimeoutException {
		Log.d(Common.TEMP_TAG, "changeNick method with parametr "+ nick+ " was called");
		if (nick.equals(getNick()))
			return false;
		else {
			DBInterface.ChangeUserNick(getId(), nick);
			refresh();
			return (nick.equals(getNick()));
		}
	}
	
	public boolean changeEmail(String email) throws InterruptedException,
			ExecutionException, JSONException, TimeoutException {
		if (email.equals(getEmail()))
			return false;
		else {
			DBInterface.ChangeUserEmail(getId(), email);
			refresh();
			return (email.equals(getEmail()));
		}
	}
	
	public boolean changePhone(String phone) throws InterruptedException,
			ExecutionException, JSONException, TimeoutException {
		if (phone.equals(getPhone()))
			return false;
		else {
			DBInterface.ChangeUserPhone(getId(), phone);
			refresh();
			return (phone.equals(getPhone()));
		}
	}
	
	public boolean changePassword(String password) throws ExecutionException,
			InterruptedException, JSONException, TimeoutException {
		Log.d(Common.TEMP_TAG, "METHOD changePassword() was started");
		if (checkPassword(password))
			return false;
		else {
			DBInterface.ChangeUserPassword(getId(), password);
			refresh();
			return checkPassword(password);
		}
	}
	
	public boolean multiChanges(String nick, String name, String surname,
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
	
	public boolean multiChanges(String nick, String name, String surname,
			String email, String phone) throws InterruptedException,
			ExecutionException, JSONException, TimeoutException {
		return (changeNick(nick) |
				changeName(name) |
				changeSurname(surname) |
				changeEmail(email) |
				changePhone(phone));
	}
	
	public boolean multiChanges(User u) throws InterruptedException,
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
		DBInterface.DeleteUser(getId());
		refresh();
		return (getDelStatus());
	}

}
