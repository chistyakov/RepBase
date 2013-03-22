package com.example.repbase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.example.repbase.classes.BaseWithJSONSkills;
import com.example.repbase.classes.GroupWithJSONSkills;
import com.example.repbase.classes.RoomTimeWithJSONSkills;
import com.example.repbase.classes.SessionState;

// TODO: spyke: mark class as static
// (all methods are static but GetAvailableTimes())

public class DBInterface
{
	public static String URL = "http://test-blabla.no-ip.org/DBService/DBService.svc/";
	private static final String wrongAccCode = "AccessCode";
	
	// generic methods is used
	// substitute they with overloaded method if it doesn't work
	private static <T> String wrapParameter(String ParameterName, T Parameter)
	{
		return "?" + ParameterName + "=" + String.valueOf(Parameter);
	}
	private static <T> String wrapParameter_(String ParameterName, T Parameter)
	{
		return ParameterName + "=" + String.valueOf(Parameter);
	}

	private static String wrapDateParameter(String ParameterName, Date Parameter)
	{
		return '?' + wrapDateParameter_(ParameterName, Parameter);
	}

	// TODO: substitute returned String with SimpleDateFormating
	private static String wrapDateParameter_(String ParameterName, Date Parameter)
	{
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy.MM.dd HH:mm", Common.LOC);
		sdfDate.setTimeZone(Common.TZONE);
		
		return ParameterName + '=' + sdfDate.format(Parameter);
	}

	
	public static JSONObject checkAuth(String Nick, String Password)
			throws ExecutionException, InterruptedException, TimeoutException, JSONException
	{
		String MethodURL = "CheckAuthorization/";
		return getObjectRespond(URL + MethodURL + 
				wrapParameter("Nick", Nick) + "&" + 
				wrapParameter("Password", Password));
	}
	
	public static JSONObject getUserByID(int ID)
			throws ExecutionException, InterruptedException, TimeoutException, JSONException
	{
		String MethodURL = "GetUserByID/";
		return getObjectRespond(URL + MethodURL + wrapParameter("ID", ID));
	}
	
	public static JSONObject getUserByNickname(String Nick)
			throws ExecutionException, InterruptedException, TimeoutException, JSONException
	{
		String MethodURL = "GetUserByNickname/";
		return getObjectRespond(URL + MethodURL + wrapParameter("Nick", Nick));
	}
	
	public static JSONObject createUser(String Nick, String Password, String Name, String Surname, String Phone, String Email)
			throws ExecutionException, InterruptedException, JSONException, TimeoutException
	{
		String MethodURL = "CreateUser/";
		return getObjectRespond(URL + MethodURL
				+ wrapParameter("Phone", Phone) + '&'
				+ wrapParameter_("Password", Password) + '&'
				+ wrapParameter_("Nick", Nick) + '&'
				+ wrapParameter_("Name", Name) + '&'
				+ wrapParameter_("Surname", Surname) + '&'
				+ wrapParameter_("E_mail", Email));
	}
	
	public static String deEncryptPhone(int UserID) 
			throws InterruptedException, ExecutionException, JSONException, TimeoutException
	{
		String MethodURL = "DeEncryptPhone/";
		JSONObject res = getObjectRespond(URL + MethodURL
				+ wrapParameter("UserID", UserID));		
		return res.getString("DeEncryptPhoneResult");
	}
	
	public static void deleteUser(int UserID) throws InterruptedException,
			ExecutionException, JSONException, TimeoutException {
		String MethodURL = "DeleteUser/";
		getObjectRespond(URL
				+ MethodURL
				+ wrapParameter("ActionPerformerID",
						SessionState.currentUser.getId()) + '&'
				+ wrapParameter_("ID", UserID));
	}
	
	public static JSONObject changeUserNick(int UserID, String Nick)
			throws InterruptedException, ExecutionException, JSONException, TimeoutException {
		String MethodURL = "User_ChangeNick/";
		return getObjectRespond(URL
				+ MethodURL
				+ wrapParameter("ActionPerformerID",
						SessionState.currentUser.getId()) + '&'
				+ wrapParameter_("UserID", UserID) + '&'
				+ wrapParameter_("Nick", Nick));
	}
	
	public static JSONObject changeUserPhone(int UserID, String Phone)
			throws InterruptedException, ExecutionException, JSONException, TimeoutException {
		String MethodURL = "User_ChangePhone/";
		return getObjectRespond(URL
				+ MethodURL
				+ wrapParameter("ActionPerformerID",
						SessionState.currentUser.getId()) + '&'
				+ wrapParameter_("UserID", UserID) + '&'
				+ wrapParameter_("Phone", Phone));
	}
	
	public static void changeUserName(int UserID, String Name)
			throws InterruptedException, ExecutionException, JSONException, TimeoutException {
		String MethodURL = "User_ChangeName/";
		getObjectRespond(URL
				+ MethodURL
				+ wrapParameter("ActionPerformerID",
						SessionState.currentUser.getId()) + '&'
				+ wrapParameter_("UserID", UserID) + '&'
				+ wrapParameter_("Name", Name));
	}
	
	public static JSONObject changeUserSurname(int UserID, String Surname)
			throws InterruptedException, ExecutionException, JSONException, TimeoutException {
		String MethodURL = "User_ChangeSurname/";
		return getObjectRespond(URL
				+ MethodURL
				+ wrapParameter("ActionPerformerID",
						SessionState.currentUser.getId()) + '&'
				+ wrapParameter_("UserID", UserID) + '&'
				+ wrapParameter_("Surname", Surname));
	}
	
	public static JSONObject changeUserEmail(int UserID, String Email)
			throws InterruptedException, ExecutionException, JSONException, TimeoutException {
		String MethodURL = "User_ChangeEmail/";
		return getObjectRespond(URL
				+ MethodURL
				+ wrapParameter("ActionPerformerID",
						SessionState.currentUser.getId()) + '&'
				+ wrapParameter_("UserID", UserID) + '&'
				+ wrapParameter_("Email", Email));
	}
	
	public static JSONObject ñhangeUserPassword(int UserID, String Password)
			throws InterruptedException, ExecutionException, JSONException, TimeoutException {
		String MethodURL = "User_ChangePassword/";
		return getObjectRespond(URL
				+ MethodURL
				+ wrapParameter("ActionPerformerID",
						SessionState.currentUser.getId()) + '&'
				+ wrapParameter_("UserID", UserID) + '&'
				+ wrapParameter_("Password", Password));
	}
	
	public static JSONArray getActiveReptetitions(int UserID)
			throws InterruptedException, ExecutionException, JSONException, TimeoutException {
		String MethodURL = "User_GetRepetitions/";
		JSONObject reps = getObjectRespond(URL + MethodURL
				+ wrapParameter("UserID", UserID));
		return reps.getJSONArray("User_GetRepetitionsResult");
	}
	
	public static JSONObject getRoomByID(int RoomID)
			throws InterruptedException, ExecutionException, JSONException, TimeoutException {
		String MethodURL = "GetRoomByID/";
		return getObjectRespond(URL + MethodURL + wrapParameter("RoomID", RoomID));
	}
	
	public static JSONObject getRoomTimeByID(int RepTimeID)
			throws InterruptedException, ExecutionException, JSONException, TimeoutException {
		String MethodURL = "GetRepTimeByID/";
		return getObjectRespond(URL + MethodURL
				+ wrapParameter("RepTimeID", RepTimeID));
	}

	
	public static JSONObject getRepetitionByID(int RepID)
			throws InterruptedException, ExecutionException, JSONException, TimeoutException {
		String MethodURL = "GetRepetitionByID/";
		return getObjectRespond(URL + MethodURL
				+ wrapParameter("RepetitionID", RepID));
	}
	
	public static void cancelRepetition(int RepID)
			throws InterruptedException, ExecutionException, JSONException, TimeoutException
	{
		String MethodURL = "CancelRepetition/";
		getObjectRespond(URL
				+ MethodURL
				+ wrapParameter("ActionPerformerID",
						SessionState.currentUser.getId()) + '&'
				+ wrapParameter("RepetitionID", RepID));
	}
	
	public JSONArray getAvailableTimes(int BaseID, Date Begin, Date End)
			throws InterruptedException, ExecutionException, JSONException, TimeoutException {
		String MethodURL = "GetAvailableTimes/";
		JSONObject res = getObjectRespond(URL + MethodURL
				+ wrapParameter("BaseID", BaseID) + '&'
				+ wrapDateParameter("Begin", Begin) + '&'
				+ wrapDateParameter("End", End));
		return res.getJSONArray("GetAvailableTimesResult");
	}
	
	public static List<GroupWithJSONSkills> getAllGroups() throws InterruptedException, ExecutionException, TimeoutException, JSONException{
		String MethodURL = "GetAllGroups";
		JSONArray resp = getArrayRespond(URL + MethodURL);
		Log.d(Common.TEMP_TAG, resp.toString());
		List<GroupWithJSONSkills> retList = new ArrayList<GroupWithJSONSkills>();
		for (int i = 0; i<resp.length(); i++){
			GroupWithJSONSkills group = new GroupWithJSONSkills(resp.getJSONObject(i));
			retList.add(group);
			Log.d(Common.TEMP_TAG, "DBInterface. List size: " +retList.size() + " "+ retList.get(i).getName());
		}
		return retList;
	}
	
	public static JSONObject getGroupByID(int GroupID)
			throws InterruptedException, ExecutionException, JSONException,
			TimeoutException {
		String MethodURL = "GetGroupByID/";
		return getObjectRespond(URL + MethodURL + wrapParameter("ID", GroupID));
	}
	
	public static JSONObject getGroupByName(String name)
			throws InterruptedException, ExecutionException, TimeoutException,
			JSONException {
		String MethodURL = "GetGroupByName/";
		return getObjectRespond(URL + MethodURL + wrapParameter("Name", name));
	}
	
	/** method doesn'r returns String with new code because server doesn't provide it
	 * 
	 * @param groupId to change AccessCode
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws TimeoutException
	 * @throws JSONException
	 */
	public static void generateNewAccessCode(int groupId) throws InterruptedException, ExecutionException, TimeoutException, JSONException{
		String MethodURL = "GenerateNewAccessCod/";
		getObjectRespond(URL + MethodURL
				+ wrapParameter("ActionPerformerID", SessionState.currentUser.getId()) + '&'
				+ wrapParameter_("GroupID", groupId));
	}
	
	public static boolean addUserToGroup(int userId, int groupId,
			String accessCode) throws InterruptedException, ExecutionException,
			TimeoutException, JSONException {
		String MethodURL = "AddUserToGroup/";
		try {
			getObjectRespond(URL
					+ MethodURL
					+ wrapParameter("ActionPerformerID", SessionState.currentUser.getId()) + '&'
					+ wrapParameter_("UserID", userId) + '&'
					+ wrapParameter_("GroupID", groupId) + '&'
					+ wrapParameter_("AccessCode", accessCode));
		} catch (JSONException e) {
			// return FALSE if server respond error about wrong AccessCode
			if (e.getMessage().contains(wrongAccCode))
				return false;
			else
				throw e;
		}
		return true;
	}

	public static void deleteUserFromGroup(int userId, int groupId) throws InterruptedException, ExecutionException, TimeoutException, JSONException{
		String MethodURL = "DeleteUserFromGroup/";
		getObjectRespond(URL + MethodURL
				+ wrapParameter("ActionPerformerID", SessionState.currentUser.getId()) + '&'
				+ wrapParameter_("UserID", userId) + '&'
				+ wrapParameter_("GroupID", groupId));
	}
	
	public static JSONObject createGroup(String name)
			throws InterruptedException, ExecutionException, TimeoutException,
			JSONException {
		String MethodURL = "CreateGroup/";
		return getObjectRespond(URL + MethodURL
				+ wrapParameter("ActionPerformerID", SessionState.currentUser.getId()) + '&'
				+ wrapParameter_("Name", name));
	}
	
	public static void deleteGroup(int groupId) throws InterruptedException,
			ExecutionException, TimeoutException, JSONException {
		String MethodURL = "DeleteGroup/";
		getObjectRespond(URL + MethodURL
				+ wrapParameter("ActionPerformerID", SessionState.currentUser.getId()) + '&'
				+ wrapParameter_("GroupID", groupId));
	}	
	
	public static JSONObject getBaseById(int id) throws InterruptedException,
			ExecutionException, TimeoutException, JSONException {
		String MethodURL = "GetBaseByID/";
		return getObjectRespond(URL + MethodURL
				+wrapParameter("ID", id));
	}
	
	public static JSONObject getBaseByName(String name)
			throws InterruptedException, ExecutionException, TimeoutException,
			JSONException {
		String MethodURL = "GetBaseByName/";
		return getObjectRespond(URL + MethodURL
				+ wrapParameter("Name", name)); 
	}
	
	public static List<BaseWithJSONSkills> getAllBases()
			throws InterruptedException, ExecutionException, TimeoutException,
			JSONException {
		String MethodURL = "GetAllBases";
		JSONArray respond = getArrayRespond(URL + MethodURL);
		List<BaseWithJSONSkills> retList = new ArrayList<BaseWithJSONSkills>();
		for (int i = 0; i<respond.length(); i++){
			BaseWithJSONSkills base = new BaseWithJSONSkills(respond.getJSONObject(i));
			retList.add(base);
		}
		return retList;
	}
	
	public static List<RoomTimeWithJSONSkills> getRoomTimesList(int roomId,
			int dayOfweek) throws InterruptedException, ExecutionException,
			TimeoutException, JSONException {
		String MethodURL = "GetRoomTimes/";
		List<RoomTimeWithJSONSkills> retList = new ArrayList<RoomTimeWithJSONSkills>();
		JSONArray respond = getArrayRespond(URL + MethodURL
				+ wrapParameter("RoomID", roomId) + '&'
				+ wrapParameter_("DayOfWeek", dayOfweek));
		for (int j = 0; j < respond.length(); j++) {
			RoomTimeWithJSONSkills repTime = new RoomTimeWithJSONSkills(
					respond.getJSONObject(j));
			if (!repTime.isDeleted())
				retList.add(repTime);
		}
		return retList;
	}
	
	public static List<RoomTimeWithJSONSkills> getRoomTimesList(int roomId)
			throws InterruptedException, ExecutionException, TimeoutException,
			JSONException {
		List<RoomTimeWithJSONSkills> retList = new ArrayList<RoomTimeWithJSONSkills>();
		for (int i = 1; i <= 7; i++) {
			retList.addAll(getRoomTimesList(roomId, i));
		}
		return retList;
	}	
	
	
	private static JSONObject getObjectRespond(String URL) throws InterruptedException, ExecutionException, TimeoutException, JSONException {
		Log.d(Common.JSON_TAG, URL);
		GetJSONFromUrl gjfu = new GetJSONFromUrl();
		JSONObject jo = new JSONObject();
		
		gjfu.execute(URL);
		jo = gjfu.get(30, TimeUnit.SECONDS);

		if (gjfu.getException() != null)
			throw gjfu.getException();
		if (jo == null)
			throw new NullPointerException("exception wasn't triggered, but JSON object is null");
		return jo;
	}
	
	// copypasted method :(
	private static JSONArray getArrayRespond(String URL) throws InterruptedException, ExecutionException, TimeoutException, JSONException {
		Log.d(Common.JSON_TAG, URL);
		GetJSONArrayFromUrl gjfu = new GetJSONArrayFromUrl();
		JSONArray ja = new JSONArray();
		
		gjfu.execute(URL);
		ja = gjfu.get(30, TimeUnit.SECONDS);
		
		if (gjfu.getException() != null)
			throw gjfu.getException();
		if (ja == null)
			throw new NullPointerException("exception wasn't triggered, but JSON array is null");
		return ja;
	}
}