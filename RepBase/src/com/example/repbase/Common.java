package com.example.repbase;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import com.example.repbase.classes.Attribute;

public class Common 
{
	public static void ShowMessageBox(Context context, String msg)
	{
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}
	
	public static boolean CheckControl(Context context, EditText control, String failureMessage)
	{
		if (control.getText().toString().equals(""))
		{
			Common.ShowMessageBox(context, failureMessage);
			return false;
		}
		return true;
	}
	
	public static JSONArray GetAttributes(JSONObject object) 
			throws JSONException
	{
		return object.getJSONObject("Attributes").getJSONArray("Attributes");
	}
	
	public static ArrayList<Attribute> GetAttributesList(JSONObject object)
			throws JSONException
	{		
		ArrayList<Attribute> result = new ArrayList<Attribute>();
		
		JSONArray attrs = Common.GetAttributes(object);
		
		for (int i = 0; i < attrs.length(); i++)
		{
			result.add(new Attribute(
					attrs.getJSONObject(i).getInt("ID"), 
					attrs.getJSONObject(i).getString("Value"),
					attrs.getJSONObject(i).getJSONObject("Type").getInt("ID"),
					attrs.getJSONObject(i).getJSONObject("Type").getString("Name")));
		}
		
		return result;
	}
}
