package com.example.repbase;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.example.repbase.classes.Attribute;
import com.example.repbase.classes.SessionState;

// TODO: 
// TODO: spyke: mark class as static
// (all methods are static but GetAvailableTimes())

public class DBInterface
{
	public static String URL = "http://test-blabla.no-ip.org/DBService/DBService.svc/";
	
	// generic method is used
	// exchange its with overloaded method if it doesn't work
	public static <T> String WrapParameter(String ParameterName, T Parameter)
	{
		return "?" + ParameterName + "=" + String.valueOf(Parameter);
	}
	public static <T> String WrapParameter_(String ParameterName, T Parameter)
	{
		return ParameterName + "=" + String.valueOf(Parameter);
	}
	@SuppressWarnings("deprecation")
	public static String WrapDateParameter(String ParameterName, Date Parameter)
	{
		return '?' + ParameterName + '=' + 
				Integer.toString(Parameter.getYear()) + '.' + 
				Integer.toString(Parameter.getMonth()) + '.' + 
				Integer.toString(Parameter.getDate());
	}
	@SuppressWarnings("deprecation")
	public static String WrapDateParameter_(String ParameterName, Date Parameter)
	{
		return ParameterName + '=' + 
				Integer.toString(Parameter.getYear()) + '.' + 
				Integer.toString(Parameter.getMonth()) + '.' + 
				Integer.toString(Parameter.getDate());
	}

	
	
	public static JSONObject CheckAuth(String Nick, String Password)
			throws ExecutionException, InterruptedException
	{
		String MethodURL = "CheckAuthorization/";
		// why not WrapParameter_() but WrapParameter() should be used???
		// http://test-blabla.no-ip.org/DBService/DBService.svc/CheckAuthorization/?Nick=alexch&?Password=password1		
		return new GetJSONFromUrl().execute(URL + MethodURL + 
				DBInterface.WrapParameter("Nick", Nick) + "&" + 
				DBInterface.WrapParameter("Password", Password)
				).get();
	}
	
	public static JSONObject GetUserByID(int ID)
			throws ExecutionException, InterruptedException
	{
		String MethodURL = "GetUserByID/";
		return new GetJSONFromUrl().execute(URL + MethodURL +
				DBInterface.WrapParameter("ID", ID)
				).get();
	}
	
	public static JSONObject GetUserByNickname(String Nick)
			throws ExecutionException, InterruptedException
	{
		String MethodURL = "GetUserByNickname/";
		return new GetJSONFromUrl().execute(URL + MethodURL +
				DBInterface.WrapParameter("Nick", Nick)
				).get();
	}
	
	public static JSONObject CreateUser(String Nick, String Password, String Name, String Surname, String Phone, String Email)
			throws ExecutionException, InterruptedException, JSONException
	{
		String MethodURL = "CreateUser/";
		return new GetJSONFromUrl().execute(URL + MethodURL +
				DBInterface.WrapParameter("Phone", Phone) + '&' +
				DBInterface.WrapParameter_("Password", Password) + '&' +
				DBInterface.WrapParameter_("Nick", Nick) + '&' +
				DBInterface.WrapParameter_("Name", Name) + '&' +
				DBInterface.WrapParameter_("Surname", Surname) + '&' +
				DBInterface.WrapParameter_("E_mail", Email)
				).get();
	}
	
	public static String DeEncryptPhone(int UserID) 
			throws InterruptedException, ExecutionException, JSONException
	{
		String MethodURL = "DeEncryptPhone/";
		JSONObject res = new GetJSONFromUrl().execute(URL + MethodURL + 
				DBInterface.WrapParameter("UserID", UserID)
				).get();
		
		return res.getString("DeEncryptPhoneResult");
	}
	
	public static boolean DeleteUser(int UserID) throws InterruptedException,
			ExecutionException, JSONException {
		String MethodURL = "DeleteUser/";
		JSONObject res = new GetJSONFromUrl().execute(
				URL
						+ MethodURL
						+ DBInterface.WrapParameter("ActionPerformerID",
								SessionState.currentUser.getId()) + '&'
						+ DBInterface.WrapParameter_("ID", UserID)).get();
		return !(res.has("Exception")); // temporary decision (should be deleted
										// when GetJSONFromUrl will throw
										// exception
	}
	
	public static JSONObject ChangeUserNick(int UserID, String Nick)
			throws InterruptedException, ExecutionException, JSONException {
		String MethodURL = "User_ChangeNick/";
		return new GetJSONFromUrl().execute(
				URL
						+ MethodURL
						+ DBInterface.WrapParameter("ActionPerformerID", SessionState.currentUser.getId()) + '&'
						+ DBInterface.WrapParameter_("UserID",UserID) + '&'
						+ DBInterface.WrapParameter_("Nick", Nick)).get();
	}
	
	public static JSONObject ChangeUserPhone(int UserID, String Phone)
			throws InterruptedException, ExecutionException, JSONException {
		String MethodURL = "User_ChangePhone/";
		return new GetJSONFromUrl().execute(
				URL
						+ MethodURL
						+ DBInterface.WrapParameter("ActionPerformerID",
								SessionState.currentUser.getId()) + '&'
						+ DBInterface.WrapParameter_("UserID", UserID) + '&'
						+ DBInterface.WrapParameter_("Phone", Phone)).get();
	}
	
	public static JSONObject ChangeUserName(int UserID, String Name)
			throws InterruptedException, ExecutionException, JSONException {
		String MethodURL = "User_ChangeName/";
		JSONObject jo = new GetJSONFromUrl().execute(
				URL
						+ MethodURL
						+ DBInterface.WrapParameter("ActionPerformerID",
								SessionState.currentUser.getId()) + '&'
						+ DBInterface.WrapParameter_("UserID", UserID) + '&'
						+ DBInterface.WrapParameter_("Name", Name)).get();

		return jo;
	}
	
	public static JSONObject ChangeUserSurname(int UserID, String Surname)
			throws InterruptedException, ExecutionException, JSONException {
		String MethodURL = "User_ChangeSurname/";
		return new GetJSONFromUrl().execute(
				URL
						+ MethodURL
						+ DBInterface.WrapParameter("ActionPerformerID",
								SessionState.currentUser.getId()) + '&'
						+ DBInterface.WrapParameter_("UserID", UserID) + '&'
						+ DBInterface.WrapParameter_("Surname", Surname)).get();
	}
	
	public static JSONObject ChangeUserEmail(int UserID, String Email)
			throws InterruptedException, ExecutionException, JSONException {
		String MethodURL = "User_ChangeEmail/";
		return new GetJSONFromUrl().execute(
				URL
						+ MethodURL
						+ DBInterface.WrapParameter("ActionPerformerID",
								SessionState.currentUser.getId()) + '&'
						+ DBInterface.WrapParameter_("UserID", UserID) + '&'
						+ DBInterface.WrapParameter_("Email", Email)).get();
	}
	
	public static JSONObject ChangeUserPassword(int UserID, String Password)
			throws InterruptedException, ExecutionException, JSONException {
		String MethodURL = "User_ChangePassword/";
		return new GetJSONFromUrl().execute(
				URL
						+ MethodURL
						+ DBInterface.WrapParameter("ActionPerformerID",
								SessionState.currentUser.getId()) + '&'
						+ DBInterface.WrapParameter_("UserID", UserID) + '&'
						+ DBInterface.WrapParameter_("Password", Password))
				.get();
	}
	
	public static JSONArray GetActiveReptetitions(int UserID)
			throws InterruptedException, ExecutionException, JSONException {
		String MethodURL = "UserGetRepetitions/";
		JSONObject reps = new GetJSONFromUrl().execute(
				URL + MethodURL + DBInterface.WrapParameter("UserID", UserID))
				.get();
		return reps.getJSONArray("User_GetRepetitionsResult");
	}
	
	public static JSONObject GetRoomByID(int RoomID)
			throws InterruptedException, ExecutionException, JSONException {
		String MethodURL = "GetRoomByID/";
		return new GetJSONFromUrl().execute(
				URL + MethodURL + DBInterface.WrapParameter("RoomID", RoomID))
				.get();
	}
	
	public static JSONObject GetRepTimeByID(int RepTimeID)
			throws InterruptedException, ExecutionException, JSONException {
		String MethodURL = "GetRepTimeByID/";
		return new GetJSONFromUrl().execute(
				URL + MethodURL
						+ DBInterface.WrapParameter("RepTimeID", RepTimeID))
				.get();
	}

	public static JSONObject GetGroupByID(int GroupID)
			throws InterruptedException, ExecutionException, JSONException {
		String MethodURL = "GetGroupByID/";
		return new GetJSONFromUrl().execute(
				URL + MethodURL + DBInterface.WrapParameter("ID", GroupID))
				.get();
	}
	
	public static JSONObject GetRepetitionByID(int RepID)
			throws InterruptedException, ExecutionException, JSONException {
		String MethodURL = "GetRepetitionByID/";
		return new GetJSONFromUrl().execute(
				URL + MethodURL
						+ DBInterface.WrapParameter("RepetitionID", RepID))
				.get();
	}
	
	public static boolean CancelRepetition(int RepID)
			throws InterruptedException, ExecutionException, JSONException
	{
		String MethodURL = "CancelRepetition/";
		JSONObject res = new GetJSONFromUrl().execute(URL + MethodURL + 
				DBInterface.WrapParameter("ActionPerformerID", SessionState.AuthorizedUser) + '&' + 
				DBInterface.WrapParameter("RepetitionID", RepID)
				).get();
		if(res.has("Exception")) // temporary decision
			return false;
		else
			return GetRepetitionByID(RepID).getBoolean("Cancelled");
	}
	
	public JSONArray GetAvailableTimes(int BaseID, Date Begin, Date End)
			throws InterruptedException, ExecutionException, JSONException {
		String MethodURL = "GetAvailableTimes/";
		JSONObject res = new GetJSONFromUrl().execute(
				URL + MethodURL + DBInterface.WrapParameter("BaseID", BaseID)
						+ '&' + DBInterface.WrapDateParameter("Begin", Begin)
						+ '&' + DBInterface.WrapDateParameter("End", End))
				.get();
		return res.getJSONArray("GetAvailableTimesResult");
	}
	
}