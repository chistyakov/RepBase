package com.example.repbase;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
			throws ExecutionException, InterruptedException, TimeoutException, JSONException
	{
		String MethodURL = "CheckAuthorization/";
		return getRespond(URL + MethodURL + 
				WrapParameter("Nick", Nick) + "&" + 
				WrapParameter("Password", Password));
	}
	
	public static JSONObject GetUserByID(int ID)
			throws ExecutionException, InterruptedException, TimeoutException, JSONException
	{
		String MethodURL = "GetUserByID/";
		return getRespond(URL + MethodURL + WrapParameter("ID", ID));
	}
	
	public static JSONObject GetUserByNickname(String Nick)
			throws ExecutionException, InterruptedException, TimeoutException, JSONException
	{
		String MethodURL = "GetUserByNickname/";
		return getRespond(URL + MethodURL + WrapParameter("Nick", Nick));
	}
	
	public static JSONObject CreateUser(String Nick, String Password, String Name, String Surname, String Phone, String Email)
			throws ExecutionException, InterruptedException, JSONException, TimeoutException
	{
		String MethodURL = "CreateUser/";
		return getRespond(URL + MethodURL
				+ WrapParameter("Phone", Phone) + '&'
				+ WrapParameter_("Password", Password) + '&'
				+ WrapParameter_("Nick", Nick) + '&'
				+ WrapParameter_("Name", Name) + '&'
				+ WrapParameter_("Surname", Surname) + '&'
				+ WrapParameter_("E_mail", Email));
	}
	
	public static String DeEncryptPhone(int UserID) 
			throws InterruptedException, ExecutionException, JSONException, TimeoutException
	{
		String MethodURL = "DeEncryptPhone/";
		JSONObject res = getRespond(URL + MethodURL
				+ WrapParameter("UserID", UserID));		
		return res.getString("DeEncryptPhoneResult");
	}
	
	public static void DeleteUser(int UserID) throws InterruptedException,
			ExecutionException, JSONException, TimeoutException {
		String MethodURL = "DeleteUser/";
		getRespond(URL
				+ MethodURL
				+ WrapParameter("ActionPerformerID",
						SessionState.currentUser.getId()) + '&'
				+ WrapParameter_("ID", UserID));
	}
	
	public static JSONObject ChangeUserNick(int UserID, String Nick)
			throws InterruptedException, ExecutionException, JSONException, TimeoutException {
		String MethodURL = "User_ChangeNick/";
		return getRespond(URL
				+ MethodURL
				+ WrapParameter("ActionPerformerID",
						SessionState.currentUser.getId()) + '&'
				+ WrapParameter_("UserID", UserID) + '&'
				+ WrapParameter_("Nick", Nick));
	}
	
	public static JSONObject ChangeUserPhone(int UserID, String Phone)
			throws InterruptedException, ExecutionException, JSONException, TimeoutException {
		String MethodURL = "User_ChangePhone/";
		return getRespond(URL
				+ MethodURL
				+ WrapParameter("ActionPerformerID",
						SessionState.currentUser.getId()) + '&'
				+ WrapParameter_("UserID", UserID) + '&'
				+ WrapParameter_("Phone", Phone));
	}
	
	public static void ChangeUserName(int UserID, String Name)
			throws InterruptedException, ExecutionException, JSONException, TimeoutException {
		String MethodURL = "User_ChangeName/";
		getRespond(URL
				+ MethodURL
				+ WrapParameter("ActionPerformerID",
						SessionState.currentUser.getId()) + '&'
				+ WrapParameter_("UserID", UserID) + '&'
				+ WrapParameter_("Name", Name));
	}
	
	public static JSONObject ChangeUserSurname(int UserID, String Surname)
			throws InterruptedException, ExecutionException, JSONException, TimeoutException {
		String MethodURL = "User_ChangeSurname/";
		return getRespond(URL
				+ MethodURL
				+ WrapParameter("ActionPerformerID",
						SessionState.currentUser.getId()) + '&'
				+ WrapParameter_("UserID", UserID) + '&'
				+ WrapParameter_("Surname", Surname));
	}
	
	public static JSONObject ChangeUserEmail(int UserID, String Email)
			throws InterruptedException, ExecutionException, JSONException, TimeoutException {
		String MethodURL = "User_ChangeEmail/";
		return getRespond(URL
				+ MethodURL
				+ WrapParameter("ActionPerformerID",
						SessionState.currentUser.getId()) + '&'
				+ WrapParameter_("UserID", UserID) + '&'
				+ WrapParameter_("Email", Email));
	}
	
	public static JSONObject ChangeUserPassword(int UserID, String Password)
			throws InterruptedException, ExecutionException, JSONException, TimeoutException {
		String MethodURL = "User_ChangePassword/";
		return getRespond(URL
				+ MethodURL
				+ WrapParameter("ActionPerformerID",
						SessionState.currentUser.getId()) + '&'
				+ WrapParameter_("UserID", UserID) + '&'
				+ WrapParameter_("Password", Password));
	}
	
	public static JSONArray GetActiveReptetitions(int UserID)
			throws InterruptedException, ExecutionException, JSONException, TimeoutException {
		String MethodURL = "UserGetRepetitions/";
		JSONObject reps = getRespond(URL + MethodURL
				+ WrapParameter("UserID", UserID));
		return reps.getJSONArray("User_GetRepetitionsResult");
	}
	
	public static JSONObject GetRoomByID(int RoomID)
			throws InterruptedException, ExecutionException, JSONException, TimeoutException {
		String MethodURL = "GetRoomByID/";
		return getRespond(URL + MethodURL + WrapParameter("RoomID", RoomID));
	}
	
	public static JSONObject GetRepTimeByID(int RepTimeID)
			throws InterruptedException, ExecutionException, JSONException, TimeoutException {
		String MethodURL = "GetRepTimeByID/";
		return getRespond(URL + MethodURL
				+ WrapParameter("RepTimeID", RepTimeID));
	}

	public static JSONObject GetGroupByID(int GroupID)
			throws InterruptedException, ExecutionException, JSONException, TimeoutException {
		String MethodURL = "GetGroupByID/";
		return getRespond(URL + MethodURL + WrapParameter("ID", GroupID));
	}
	
	public static JSONObject GetRepetitionByID(int RepID)
			throws InterruptedException, ExecutionException, JSONException, TimeoutException {
		String MethodURL = "GetRepetitionByID/";
		return getRespond(URL + MethodURL
				+ WrapParameter("RepetitionID", RepID));
	}
	
	public static boolean CancelRepetition(int RepID)
			throws InterruptedException, ExecutionException, JSONException, TimeoutException
	{
		String MethodURL = "CancelRepetition/";
		getRespond(URL
				+ MethodURL
				+ WrapParameter("ActionPerformerID",
						SessionState.currentUser.getId()) + '&'
				+ WrapParameter("RepetitionID", RepID));

			return GetRepetitionByID(RepID).getBoolean("Cancelled");
	}
	
	public JSONArray GetAvailableTimes(int BaseID, Date Begin, Date End)
			throws InterruptedException, ExecutionException, JSONException, TimeoutException {
		String MethodURL = "GetAvailableTimes/";
		JSONObject res = getRespond(URL + MethodURL
				+ WrapParameter("BaseID", BaseID) + '&'
				+ WrapDateParameter("Begin", Begin) + '&'
				+ WrapDateParameter("End", End));
		return res.getJSONArray("GetAvailableTimesResult");
	}
	
	private static JSONObject getRespond(String URL) throws InterruptedException, ExecutionException, TimeoutException, JSONException {
		Log.d(Common.JSON_TAG, URL);
		GetJSONFromUrl gjfu = new GetJSONFromUrl();
		JSONObject jo = new JSONObject();
		
		gjfu.execute(URL);
		jo = gjfu.get(1, TimeUnit.MINUTES);

		if (gjfu.getException() != null)
			throw gjfu.getException();
		if (jo == null)
			throw new NullPointerException("exception wasn't triggered, but JSON object is null");
		return jo;
	}
	
}