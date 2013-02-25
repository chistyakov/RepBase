package com.example.repbase;

// TODO: spyke: pass Exception not via JSONObject but via exception mechanism  
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
///import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class GetJSONFromUrl extends AsyncTask<String, Void, JSONObject> {
	private final String magicExcStartStr = "The exception message is '";
	private final String magicExcEndStr = "'. See server";
	private final String fineServerError = "Invalid argument count for AddToLog function.";
	
	private JSONException jse;
	protected void onPreExecute(){
		jse = null;
	}

	/**
	 * doInBackground method returns JSONObject with respond /* if respond is
	 * empty method returns empty JSONObject /* if respond contains error from
	 * server in html method tries to parse it /* and sets jse field in
	 * JSONException with either server's error text /* or with text
	 * "Error occurred. Can't parse error's text from server's respond"
	 */
	protected JSONObject doInBackground(String... Urls) {
		try {
			HttpGet request = new HttpGet(Urls[0]);
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-type", "application/json");

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpResponse response = httpClient.execute(request);
			HttpEntity resp = response.getEntity();
			
			InputStream stream = resp.getContent();
			InputStreamReader reader = new InputStreamReader(stream, "utf-8");
			
			// BufferedReader is used for big blocks of data
			BufferedReader br = new BufferedReader(reader);
			
			// we have to read respond per line
			// because reader.read(buffer) sometimes places corrupted data into the buffer
			// I didn't find the nature of such issue
			// and used code from here: http://habrahabr.ru/qa/11449/
			String strBuffer = null;
			StringBuilder sb = new StringBuilder();
			while ((strBuffer = br.readLine()) != null) {
				sb.append(strBuffer);
			}
			strBuffer = sb.toString();
			
			stream.close();
			reader.close();
			br.close();

			JSONObject result = null;

			Log.d(Common.JSON_TAG, "respond: " + strBuffer);
			
			// return empty JSONObject if the respond is empty
			if(strBuffer.isEmpty()){
				Log.d(Common.JSON_TAG, "doInBackground finished; jse is null");
				return new JSONObject();
			}
			
			try {
				result = new JSONObject(strBuffer);
			} catch (JSONException ex) {
				Log.d(Common.JSON_TAG, "server throws error. Try to parse it");
				int startIndex = strBuffer.indexOf(magicExcStartStr);
				int endIndex = strBuffer.indexOf(magicExcEndStr);
				if (startIndex != -1 && endIndex != -1) {
					Log.d(Common.JSON_TAG, "server's error can be parsed");
					startIndex += magicExcStartStr.length();
					endIndex -= 1;
					
					String message = strBuffer.substring(startIndex, endIndex);
//					message = message.replace("\"", "\\\"");
					Log.d(Common.JSON_TAG, "Parsing: startIndex: " + String.valueOf(startIndex)
					 + "; endIndex: " + String.valueOf(endIndex)+"; message " + message);
					
					// error with text "Invalid argument count for AddToLog function." is not critical
					// empty JSONObject will be returned
					if (message.contains(fineServerError)){
						Log.d(Common.JSON_TAG, "the cause of server error: "
								+ fineServerError
								+ " empty JSONObject will be return");
						return new JSONObject();
					}
					Log.d(Common.JSON_TAG, "server's error was parsed successfully: " + message);
					jse = new JSONException(message);
				} else {
					Log.d(Common.JSON_TAG, "server's error can't be parsed");			
					jse = new JSONException("Error occurred. Can't parse error's text from server's respond");
				}
			}
			String jseStr = (jse == null) ? "null" : jse.toString();
			String resultStr = (result == null) ? "null" : result.toString();
			Log.d(Common.JSON_TAG, "execution finished; Exception =" + jseStr
					+ "; result = " + resultStr);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			Log.d(Common.JSON_TAG, "exception in GetJSONFromUrl; ", e);
			return null;
		}
	}
	public JSONException getException(){
		return jse;
	}
}
