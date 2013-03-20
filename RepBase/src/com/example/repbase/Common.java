package com.example.repbase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.repbase.classes.Attribute;

// TODO: think about i18n of application
public class Common {
	public static final int MAX_EMAIL_LENGTH=20;
	public static final String JSON_TAG = "json";
	public static final String EXC_TAG = "exc";
	public static final String TIMEOUT_TAG = "exc";
	public static final String TEMP_TAG = "temp";
	
	public static final Locale LOC = new Locale("ru", "RU");
	public static final TimeZone TZONE = TimeZone.getTimeZone("GMT+0400");
	
	public static final String TIMEOUTSTR = "Не могу дождаться ответа от сервера. Пожалуйста, проверьте соединение.";
	
	public static void ShowMessageBox(Context context, String msg) {
		Toast.makeText(context, translateToRu(msg), Toast.LENGTH_SHORT).show();
	}

	public static boolean CheckControl(Context context, EditText control,
			String failureMessage) {
		if (control.getText().toString().equals("")) {
			Common.ShowMessageBox(context, failureMessage);
			return false;
		}
		if (control.getText().toString().contains(" ")){
			Common.ShowMessageBox(context, "Нельзя использовать пробелы");
			return false;
		}
		return true;
	}

	private static JSONArray GetAttributes(JSONObject jobject)
			throws JSONException {
		return jobject.getJSONObject("Attributes").getJSONArray("Attributes");
	}

	public static ArrayList<Attribute> GetAttributesList(JSONObject object)
			throws JSONException {
		ArrayList<Attribute> result = new ArrayList<Attribute>();

		JSONArray attrs = Common.GetAttributes(object);

		for (int i = 0; i < attrs.length(); i++) {
			result.add(new Attribute(attrs.getJSONObject(i).getInt("ID"), attrs
					.getJSONObject(i).getString("Value"), attrs
					.getJSONObject(i).getJSONObject("Type").getInt("ID"), attrs
					.getJSONObject(i).getJSONObject("Type").getString("Name")));
		}

		return result;
	}

	/**
	 * @return the value of attribute from the list with specified name
	 */
	public static String getSpecifiedAttribute(JSONObject object, String AttType)
			throws JSONException {
		ArrayList<Attribute> attList = new ArrayList<Attribute>();
		attList = Common.GetAttributesList(object);
		for (Attribute a : attList) {
			if (a.Type.equals(AttType))
				return a.Value;
		}
		throw new JSONException("can't find key " + AttType + " in JSON array of attributes");
	}
	
	/**
	 * @return the value of required attribute and fallback if attribute is not
	 *         exists
	 */
	public static String optSpecifiedAttribute(JSONObject object, String AttType, String fallback)
			throws JSONException {
		ArrayList<Attribute> attList = new ArrayList<Attribute>();
		attList = Common.GetAttributesList(object);
		for (Attribute a : attList) {
			if (a.Type.equals(AttType))
				return a.Value;
		}
		return fallback;
	}

	/**
	 * this function is not the best i18n approach :) IT'S DREADFUL !!! it
	 * should be renamed to terribleTranslateToRu()
	 */
	public static String translateToRu(String engStr) {
		/*Actually switch statement for Strings was added in jdk 7
		* @see http://stackoverflow.com/questions/338206/switch-statement-with-strings-in-java
		* we use ancient if-then-elseif approach because
		* afaik, Android does not currently support java7 */

		if (engStr
				.equals("Invalid argument. Invalid \"Nickname\" argument - length")) {
			return "Длина ника от 4 до 15 символов";
		} else if (engStr
				.equals("Invalid argument. Invalid \"Nickname\" argument - already exists")) {
			return "Пользователь с таким ником уже зарегистрирован";
		} else if (engStr
				.equals("Invalid argument. Invalid \"Password\" argument - length")) {
			return "Длина пароля от 5 до 25 символов";
		} else if (engStr
				.equals("Invalid argument. Invalid \"Name\" argument - length")) {
			return "Длина имени до 20 символов";
		} else if (engStr
				.equals("Invalid argument. Invalid \"Surname\" argument - length")) {
			return "Длина фамилии до 20 символов";
		} else if (engStr.equals("Invalid phone number format")) {
			// return "Неверный формат номера телефона"
			return "Неверная длина номера телефона (необходимо 11 цифр)";
		} else if (engStr
				.equals("Invalid argument. Invalid \"Phone\" argument - length")) {
			return "Неверная длина номера телефона (необходимо 11 цифр)";
		} else if (engStr
				.equals("Invalid argument. Invalid \"Phone\" argument - already exists")) {
			return "Пользователь с таким номером тел. уже зарегистрирован";
		} else if (engStr
				.equals("Invalid argument. Invalid \"E-mail\" argument - already exists")) {
			return "Пользователь с таким e-mail уже зарегистрирован";
		} else if (engStr.equals("There is no such user in database")) {
			return "Такого пользователя не сущуствует";
		} else if (engStr.equals("Authentication failed. The password is incorrect.")){
			return ("Проверьте правильность ввода пароля");
		}
		else
			return engStr;
	}
	
	public static ArrayList<Integer> convJSONArrToIntArrL(JSONArray ja)
			throws JSONException {
		ArrayList<Integer> al = new ArrayList<Integer>();
		if (ja != null){
			for (int i = 0; i<ja.length(); i++){
				al.add(ja.getInt(i));
			}
		}
		return al;
	}
	
	public static Date convJSONStringToDate(String jsonDateStr){
		Log.d(Common.TEMP_TAG, jsonDateStr);
		if(jsonDateStr.equals("null"))
			return null;
		// the example of date from server: \/Date(1363201200000+0400)\/
		String strTimeMills = jsonDateStr.split("\\(|\\+|\\-")[1];
		Date d = new Date(Long.parseLong(strTimeMills));
		return d;
	}
	
//	public static Calendar convJSONStringToCal(String jsonDateStr){
//		// the example of date from server: \/Date(1363201200000+0400)\/
//		String strTimeMills = jsonDateStr.split("[(+]")[1];
//		Calendar cal = new GregorianCalendar();
//		cal.setTimeInMillis(Long.parseLong(strTimeMills));
//		return cal;		
//	}
	
	public static String convJSONStringToString(String jsonDateStr){
		Log.d(Common.TEMP_TAG, jsonDateStr);
		if(jsonDateStr == null)
			return null;
		Date dt = convJSONStringToDate(jsonDateStr);
		return Integer.toString(dt.getHours() + 4)
				+ ':'
				+ (Integer.toString(dt.getMinutes()).equals("0") ? "00"
						: Integer.toString(dt.getMinutes()).length() == 1 ? "0"
								+ Integer.toString(dt.getMinutes())
								: Integer.toString(dt.getMinutes()));	
	}
}
