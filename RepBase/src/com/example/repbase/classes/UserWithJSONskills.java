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
	private boolean actuality = false; // this artificial field shows that user
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
		this.setPassword(joUser.getString("Password"));
		this.setNick(joUser.getString("Nick"));
		this.setPhone(joUser.getString("Phone"));
		
		// new JSON request is sent in DBInterface.DeEncryptPhone(String ID)
		this.setPhone(DBInterface.DeEncryptPhone(joUser.getString("ID")));

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
		jo = DBInterface.GetUserByID(String.valueOf(getId()));
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
		if (name==getName()) return false;
		else{
			DBInterface.ChangeUserName(String.valueOf(getId()), name);
			// refresh function exchanges data with server
			// it will be better if DBInterface.ChangeUserName returns boolean
			refresh();
			return (name==getName());
		}
	}

}
