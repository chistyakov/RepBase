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

// TODO: spyke: mark class as static
// (all methods are static but GetAvailableTimes())

public class DBInterface
{
	public static String URL = "http://test-blabla.no-ip.org/DBService/DBService.svc/";
	public static String WrapParameter(String ParameterName, String Parameter)
	{
		return "?" + ParameterName + "=" + Parameter;
	}
	public static String WrapParameter_(String ParameterName, String Parameter)
	{
		return ParameterName + "=" + Parameter;
	}
	@SuppressWarnings("deprecation")
	public static String WrapParameter(String ParameterName, Date Parameter)
	{
		return '?' + ParameterName + '=' + 
				Integer.toString(Parameter.getYear()) + '.' + 
				Integer.toString(Parameter.getMonth()) + '.' + 
				Integer.toString(Parameter.getDate());
	}
	@SuppressWarnings("deprecation")
	public static String WrapParameter_(String ParameterName, Date Parameter)
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
		Log.d("Auth",URL + MethodURL + 
				DBInterface.WrapParameter("Nick", Nick) + "&" + 
				DBInterface.WrapParameter("Password", Password)); // why not WrapParameter_() but WrapParameter() should be used???
		// http://test-blabla.no-ip.org/DBService/DBService.svc/CheckAuthorization/?Nick=alexch&?Password=password1
		
		return new GetJSONFromUrl().execute(URL + MethodURL + 
				DBInterface.WrapParameter("Nick", Nick) + "&" + 
				DBInterface.WrapParameter("Password", Password)
				).get();
	}
	
	public static JSONObject GetUserByID(String ID)
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
		Log.d("reg", URL + MethodURL +
				DBInterface.WrapParameter("Phone", Phone) + '&' +
				DBInterface.WrapParameter_("Password", Password) + '&' +
				DBInterface.WrapParameter_("Nick", Nick) + '&' +
				DBInterface.WrapParameter_("Name", Name) + '&' +
				DBInterface.WrapParameter_("Surname", Surname) + '&' +
				DBInterface.WrapParameter_("E_mail", Email));
		
		return new GetJSONFromUrl().execute(URL + MethodURL +
				DBInterface.WrapParameter("Phone", Phone) + '&' +
				DBInterface.WrapParameter_("Password", Password) + '&' +
				DBInterface.WrapParameter_("Nick", Nick) + '&' +
				DBInterface.WrapParameter_("Name", Name) + '&' +
				DBInterface.WrapParameter_("Surname", Surname) + '&' +
				DBInterface.WrapParameter_("E_mail", Email)
				).get();
	}
	
	public static String DeEncryptPhone(String UserID) 
			throws InterruptedException, ExecutionException, JSONException
	{
		String MethodURL = "DeEncryptPhone/";
		JSONObject res = new GetJSONFromUrl().execute(URL + MethodURL + 
				DBInterface.WrapParameter("UserID", UserID)
				).get();
		
		return res.getString("DeEncryptPhoneResult");
	}
	
	public static boolean DeleteUser(String UserID)
			throws InterruptedException, ExecutionException, JSONException
	{
		String MethodURL = "DeleteUser/";
		new GetJSONFromUrl().execute(URL + MethodURL +
				DBInterface.WrapParameter("ActionPerformerID", SessionState.AuthorizedUser) + '&' +
				DBInterface.WrapParameter_("ID", UserID)
				);
		
		ArrayList<Attribute> list = Common.GetAttributesList(GetUserByID(UserID));
		for (Attribute atr: list)
			if (atr.Type.equals("Deleted") && atr.Value.equals("TRUE"))
				return true;
		return false;
	}
	
	public static JSONObject ChangeUserNick(String UserID, String Nick)
			throws InterruptedException, ExecutionException, JSONException
	{
		String MethodURL = "User_ChangeNick/";
		return new GetJSONFromUrl().execute(URL + MethodURL +
				DBInterface.WrapParameter("ActionPerformerID", SessionState.AuthorizedUser) + '&' +
				DBInterface.WrapParameter_("UserID", SessionState.AuthorizedUser) + '&' + 
				DBInterface.WrapParameter_("Nick", Nick)).get();
	}
	
	public static JSONObject ChangeUserPhone(String UserID, String Phone)
			throws InterruptedException, ExecutionException, JSONException
	{
		String MethodURL = "User_ChangePhone/";
		Log.d("change", "phone: "+URL + MethodURL +
				DBInterface.WrapParameter("ActionPerformerID", SessionState.AuthorizedUser) + '&' +
				DBInterface.WrapParameter_("UserID", SessionState.AuthorizedUser) + '&' + 
				DBInterface.WrapParameter_("Phone", Phone));
		return new GetJSONFromUrl().execute(URL + MethodURL +
				DBInterface.WrapParameter("ActionPerformerID", SessionState.AuthorizedUser) + '&' +
				DBInterface.WrapParameter_("UserID", SessionState.AuthorizedUser) + '&' + 
				DBInterface.WrapParameter_("Phone", Phone)).get();
	}
	
	public static JSONObject ChangeUserName(String UserID, String Name)
			throws InterruptedException, ExecutionException, JSONException
	{
		String MethodURL = "User_ChangeName/";
		Log.d("myLogs","ChangeUserName method was called with :"+Name);
		Log.d("myLogs",URL + MethodURL +
				DBInterface.WrapParameter("ActionPerformerID", SessionState.AuthorizedUser) + '&' +
				DBInterface.WrapParameter_("UserID", SessionState.AuthorizedUser) + '&' + 
				DBInterface.WrapParameter_("Name", Name));
		JSONObject jo=new GetJSONFromUrl().execute(URL + MethodURL +
				DBInterface.WrapParameter("ActionPerformerID", SessionState.AuthorizedUser) + '&' +
				DBInterface.WrapParameter_("UserID", SessionState.AuthorizedUser) + '&' + 
				DBInterface.WrapParameter_("Name", Name)).get();
		
		return jo;
	}
	
	public static JSONObject ChangeUserSurname(String UserID, String Surname)
			throws InterruptedException, ExecutionException, JSONException
	{
		String MethodURL = "User_ChangeSurname/";
		return new GetJSONFromUrl().execute(URL + MethodURL +
				DBInterface.WrapParameter("ActionPerformerID", SessionState.AuthorizedUser) + '&' +
				DBInterface.WrapParameter_("UserID", SessionState.AuthorizedUser) + '&' + 
				DBInterface.WrapParameter_("Surname", Surname)).get();
	}
	
	public static JSONObject ChangeUserEmail(String UserID, String Email)
			throws InterruptedException, ExecutionException, JSONException
	{
		String MethodURL = "User_ChangeEmail/";
		return new GetJSONFromUrl().execute(URL + MethodURL +
				DBInterface.WrapParameter("ActionPerformerID", SessionState.AuthorizedUser) + '&' +
				DBInterface.WrapParameter_("UserID", SessionState.AuthorizedUser) + '&' + 
				DBInterface.WrapParameter_("Email", Email)).get();
	}
	
	public static JSONObject ChangeUserPassword(String UserID, String Password)
			throws InterruptedException, ExecutionException, JSONException
	{
		String MethodURL = "User_ChangePassword/";
		Log.d("change", "passw: "+URL + MethodURL +
				DBInterface.WrapParameter("ActionPerformerID", SessionState.AuthorizedUser) + '&' +
				DBInterface.WrapParameter_("UserID", SessionState.AuthorizedUser) + '&' + 
				DBInterface.WrapParameter_("Password", Password));
		return new GetJSONFromUrl().execute(URL + MethodURL +
				DBInterface.WrapParameter("ActionPerformerID", SessionState.AuthorizedUser) + '&' +
				DBInterface.WrapParameter_("UserID", SessionState.AuthorizedUser) + '&' + 
				DBInterface.WrapParameter_("Password", Password)).get();
	}
	
	public static JSONArray GetActiveReptetitions(String UserID)
			throws InterruptedException, ExecutionException, JSONException
	{
		String MethodURL = "UserGetRepetitions/";
		JSONObject reps = new GetJSONFromUrl().execute(URL + MethodURL +
				DBInterface.WrapParameter("UserID", UserID)
				).get();
		return reps.getJSONArray("User_GetRepetitionsResult");
	}
	
	public static JSONObject GetRoomByID(String RoomID)
			throws InterruptedException, ExecutionException, JSONException
	{
		String MethodURL = "GetRoomByID/";
		return new GetJSONFromUrl().execute(URL + MethodURL +
				DBInterface.WrapParameter("RoomID", RoomID)
				).get();
	}
	
	public static JSONObject GetRepTimeByID(String RepTimeID)
			throws InterruptedException, ExecutionException, JSONException
	{
		String MethodURL = "GetRepTimeByID/";
		return new GetJSONFromUrl().execute(URL + MethodURL +
				DBInterface.WrapParameter("RepTimeID", RepTimeID)
				).get();
	}
	
	public static JSONObject GetGroupByID(String GroupID)
			throws InterruptedException, ExecutionException, JSONException
	{
		String MethodURL = "GetGroupByID/";
		return new GetJSONFromUrl().execute(URL + MethodURL +
				DBInterface.WrapParameter("ID", GroupID)
				).get();
	}
	
	public static JSONObject GetRepetitionByID(String RepID)
			throws InterruptedException, ExecutionException, JSONException
	{
		String MethodURL = "GetRepetitionByID/";
		return new GetJSONFromUrl().execute(URL + MethodURL +
				DBInterface.WrapParameter("RepetitionID", RepID)
				).get();
	}
	
	@SuppressWarnings("finally")
	public static boolean CancelRepetition(String RepID)
			throws InterruptedException, ExecutionException, JSONException
	{
		String MethodURL = "CancelRepetition/";
		JSONObject res = new GetJSONFromUrl().execute(URL + MethodURL + 
				DBInterface.WrapParameter("ActionPerformerID", SessionState.AuthorizedUser) + '&' + 
				DBInterface.WrapParameter("RepetitionID", RepID)
				).get();
		try
		{
			res.getString("Exception");
			return false;
		}
		catch (Exception e)
		{
			if (!GetRepetitionByID(RepID).getBoolean("Cancelled"))
				return false;
		}
		finally
		{
			return true;
		}
	}
	
	public JSONArray GetAvailableTimes(String BaseID, Date Begin, Date End)
			throws InterruptedException, ExecutionException, JSONException
	{
		String MethodURL = "GetAvailableTimes/";
		JSONObject res = new GetJSONFromUrl().execute(URL + MethodURL +
				DBInterface.WrapParameter("BaseID", BaseID) + '&' +
				DBInterface.WrapParameter("Begin", Begin) + '&' + 
				DBInterface.WrapParameter("End", End)).get();
		return res.getJSONArray("GetAvailableTimesResult");
	}
	
}