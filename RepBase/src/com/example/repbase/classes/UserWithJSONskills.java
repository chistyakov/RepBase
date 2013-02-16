package com.example.repbase.classes;

import java.util.concurrent.ExecutionException;

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
			throws ExecutionException, InterruptedException, JSONException {
		JSONObject jo = new JSONObject();
		jo = DBInterface
				.CreateUser(Nick, Password, Name, Surname, Phone, Email);

		if (jo.has("Exception"))
			throw new JSONException(jo.getString("Exception"));
		else {
			fillFields(jo);
			this.actuality = true;
			// this.URL = URL;
		}
	}

	public UserWithJSONskills(String Nick, String Password)
			throws ExecutionException, InterruptedException, JSONException {
		// DBInterface.CheckAuth() method returns JSONObject
		// contains name "CheckAuthorizationResult" in case nick was found
		// and name "Exception" otherwise
		// see GetJSONFromUrl.java
		JSONObject joCheckAuth = new JSONObject();
		joCheckAuth = DBInterface.CheckAuth(Nick, Password);
		if (joCheckAuth.has("Exception"))
			throw new JSONException(joCheckAuth.getString("Exception"));
		else {
			if (joCheckAuth.getBoolean("CheckAuthorizationResult")) {
				JSONObject jo = new JSONObject();
				jo = DBInterface.GetUserByNickname(Nick);
				fillFields(jo);
				this.actuality = true;
			} else
				throw new JSONException(
						"Authentication failed. The password is incorrect.");
		}
	}
	
	// private method fills all fields of user from JSON object
	private void fillFields(JSONObject joUser) throws JSONException, InterruptedException, ExecutionException{
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
	public void refresh() throws ExecutionException, InterruptedException, JSONException{
		JSONObject jo = new JSONObject();
		jo = DBInterface.GetUserByID(getId());
		fillFields(jo);
	}
	
	public boolean isActual() {
		return actuality;
	}
		
	public boolean checkPassword(String passw) throws ExecutionException, InterruptedException, JSONException{
		JSONObject joCheckAuth = DBInterface.CheckAuth(getNick(), passw);
		return joCheckAuth.getBoolean("CheckAuthorizationResult");
	}

	// returns true if the value was changed
	public boolean changeName(String name) throws InterruptedException, ExecutionException, JSONException {
		if (name.equals(getName()))
			return false;
		else {
			JSONObject joRespond=new JSONObject();
			joRespond=DBInterface.ChangeUserName(getId(), name);
			if (joRespond.length()!=0 && joRespond.has("Exception"))
				throw new JSONException(joRespond.getString("Exception"));
			// refresh function exchanges data with server
			// it will be better if DBInterface.ChangeUserName returns boolean
			refresh();
			return (name.equals(getName()));
		}
	}
	
	public boolean changeSurname(String surname) throws InterruptedException, ExecutionException, JSONException{
		if (surname.equals(getSurname()))
			return false;
		else {
			JSONObject joRespond=new JSONObject();
			joRespond=DBInterface.ChangeUserSurname(getId(), surname);
			if (joRespond.length()!=0 && joRespond.has("Exception"))
				throw new JSONException(joRespond.getString("Exception"));
			refresh();
			return (surname.equals(getSurname()));
		}
	}
	
	public boolean changeNick(String nick) throws InterruptedException, ExecutionException, JSONException{
		Log.d("changeexc", "changeNick method with parametr "+ nick+ " was called");
		if (nick.equals(getNick()))
			return false;
		else {
			JSONObject joRespond=new JSONObject();
			joRespond=DBInterface.ChangeUserNick(getId(), nick);
			if (joRespond.length()!=0 && joRespond.has("Exception"))
				throw new JSONException(joRespond.getString("Exception"));
			refresh();
			return (nick.equals(getNick()));
		}
	}
	
	public boolean changeEmail(String email) throws InterruptedException, ExecutionException, JSONException{
		if (email.equals(getEmail()))
			return false;
		else {
			JSONObject joRespond=new JSONObject();
			joRespond=DBInterface.ChangeUserEmail(getId(), email);
			if (joRespond.length()!=0 && joRespond.has("Exception"))
				throw new JSONException(joRespond.getString("Exception"));
			refresh();
			return (email.equals(getEmail()));
		}
	}
	
	public boolean changePhone(String phone) throws InterruptedException, ExecutionException, JSONException {
		if (phone.equals(getPhone()))
			return false;
		else {
			JSONObject joRespond=new JSONObject();
			joRespond=DBInterface.ChangeUserPhone(getId(), phone);
			if (joRespond.length()!=0 && joRespond.has("Exception"))
				throw new JSONException(joRespond.getString("Exception"));
			refresh();
			return (phone.equals(getPhone()));
		}
	}
	
	public boolean changePassword(String password) throws ExecutionException, InterruptedException, JSONException{
		Log.d("changeexc", "METHOD changePassword() was started");
		if (checkPassword(password))
			return false;
		else {
			JSONObject joRespond = new JSONObject();
			joRespond = DBInterface.ChangeUserPassword(getId(), password);
			if (joRespond.length() != 0 && joRespond.has("Exception"))
				throw new JSONException(joRespond.getString("Exception"));
			refresh();
			return checkPassword(password);
		}
	}
	
	public boolean delete() throws InterruptedException, ExecutionException, JSONException{
		if(DBInterface.DeleteUser(getId())){
			refresh();
			return (getDelStatus());
		} else 
			return false;
	}

}
