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
		Log.d("reg", jo.toString());

		// TODO: test this assumption
		// jo can contain "Exception" (see RegisterActivity.java)
		if (jo.has("Exception"))
			throw new JSONException(jo.getString("Exception"));
		else {

			// super(jo.getInt("ID"), Nick, jo.getString("Password"), Phone);
			// bitch!!!
			// "Constructor call must be the first statement in a constructor"
			// we have to set every field manually

			this.setId(jo.getInt("ID"));
			this.setPassword(jo.getString("Password"));
			this.setNick(jo.getString("Nick"));
			this.setPhone(jo.getString("Phone"));

			// we can use Common.GetAttributesList() method with loop.
			// Common.getSpecifiedAttribute() is more slowly
			// but more clearly in my eyes
			this.setName(Common.getSpecifiedAttribute(jo, "Name"));
			this.setSurname(Common.getSpecifiedAttribute(jo, "Surname"));
			this.setEmail(Common.getSpecifiedAttribute(jo, "E-mail"));

			if (Boolean.parseBoolean(Common.optSpecifiedAttribute(jo,
					"Full Rights", "FALSE")))
				this.markAsAdmin();
			if (Boolean.parseBoolean(Common
					.getSpecifiedAttribute(jo, "Deleted")))
				this.markAsDeleted();
			if (Boolean
					.parseBoolean(Common.getSpecifiedAttribute(jo, "Banned")))
				this.markAsBanned();
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
		Log.d("Auth", "i've come back to UserWithJSONskills constructor");
		JSONObject joCheckAuth = new JSONObject();
		Log.d("Auth",joCheckAuth.optString("Exception", joCheckAuth.toString()));
		joCheckAuth = DBInterface.CheckAuth(Nick, Password);
		if (joCheckAuth.has("Exception"))
			throw new JSONException(joCheckAuth.getString("Exception"));
		else {
			if (joCheckAuth.getBoolean("CheckAuthorizationResult")) {
				JSONObject jo = new JSONObject();
				jo = DBInterface.GetUserByNickname(Nick);
				this.setId(jo.getInt("ID"));
				this.setPassword(jo.getString("Password"));
				this.setNick(jo.getString("Nick"));
				this.setPhone(jo.getString("Phone"));
				
				this.setName(Common.getSpecifiedAttribute(jo, "Name"));
				this.setSurname(Common.getSpecifiedAttribute(jo, "Surname"));
				this.setEmail(Common.getSpecifiedAttribute(jo, "E-mail"));

				if (Boolean.parseBoolean(Common.optSpecifiedAttribute(jo,
						"Full Rights", "FALSE")))
					this.markAsAdmin(); // Common.optSpecifiedAttribute is used instead of Common.getSpecifiedAttribute
										// because "Full Rights" attribute can be missed
				if (Boolean.parseBoolean(Common.getSpecifiedAttribute(jo,
						"Deleted")))
					this.markAsDeleted();
				if (Boolean.parseBoolean(Common.getSpecifiedAttribute(jo,
						"Banned")))
					this.markAsBanned();

				this.actuality = true;
			} else
				throw new JSONException(
						"Authentication failed. The password is incorrect.");
			Log.d("Auth", "i've finished with UserWithJSONskills constructor");
		}
	}

	public boolean isActual() {
		return actuality;
	}
	
	public void changeName() {
		
	}
}
