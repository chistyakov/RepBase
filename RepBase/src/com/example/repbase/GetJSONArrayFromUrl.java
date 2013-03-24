package com.example.repbase;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import android.os.AsyncTask;
import android.util.Log;

/**
 * copypasted from GetJSONFromURL class :(
 */
public class GetJSONArrayFromUrl extends AsyncTask<String, Void, JSONArray> {
	private final String magicExcStartStr = "The exception message is '";
	private final String magicExcEndStr = "'. See server";
	private final String fineServerError = "Invalid argument count for AddToLog function.";
	
	private JSONException jse;
	protected void onPreExecute(){
		jse = null;
	}

	protected JSONArray doInBackground(String... Urls) {
		try {
			HttpGet request = new HttpGet(Urls[0].replace(" ", "%20"));
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-type", "application/json");

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpResponse response = httpClient.execute(request);
			HttpEntity resp = response.getEntity();
			
			InputStream stream = resp.getContent();
			InputStreamReader reader = new InputStreamReader(stream, "utf-8");
			
			BufferedReader br = new BufferedReader(reader);
			
			String strBuffer = null;
			StringBuilder sb = new StringBuilder();
			while ((strBuffer = br.readLine()) != null) {
				sb.append(strBuffer);
			}
			strBuffer = sb.toString();
			
			stream.close();
			reader.close();
			br.close();

			JSONArray result = null;

			Log.d(Common.JSON_TAG, "respond: " + strBuffer);
			
			if(strBuffer.isEmpty()){
				Log.d(Common.JSON_TAG, "doInBackground finished; jse is null");
				return new JSONArray();
			}
			
			try {
				result = new JSONArray(strBuffer);
			} catch (JSONException ex) {
				Log.d(Common.JSON_TAG, "server throws error. Try to parse it");
				int startIndex = strBuffer.indexOf(magicExcStartStr);
				int endIndex = strBuffer.indexOf(magicExcEndStr);
				if (startIndex != -1 && endIndex != -1) {
					Log.d(Common.JSON_TAG, "server's error can be parsed");
					startIndex += magicExcStartStr.length();
					endIndex -= 1;
					
					String message = strBuffer.substring(startIndex, endIndex);
					Log.d(Common.JSON_TAG, "Parsing: startIndex: " + String.valueOf(startIndex)
					 + "; endIndex: " + String.valueOf(endIndex)+"; message " + message);
					if (message.contains(fineServerError)){
						Log.d(Common.JSON_TAG, "the cause of server error: "
								+ fineServerError
								+ " empty JSONArray will be return");
						return new JSONArray();
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
			Log.d(Common.JSON_TAG, "exception in GetJSONArrayFromUrl; ", e);
			return null;
		}
	}
	public JSONException getException(){
		return jse;
	}
}
