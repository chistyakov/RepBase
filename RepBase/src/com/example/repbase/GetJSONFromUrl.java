package com.example.repbase;

import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
///import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class GetJSONFromUrl extends AsyncTask<String, Integer, JSONObject> {
	private final String magicExcStartStr = "The exception message is '";
	private final String magicExcEndStr = "'. See server";

	protected JSONObject doInBackground(String... Urls) {
		try {
			HttpGet request = new HttpGet(Urls[0]);
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-type", "application/json");

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpResponse response = httpClient.execute(request);
			HttpEntity resp = response.getEntity();

			char[] buffer = new char[(int) resp.getContentLength()];
			InputStream stream = resp.getContent();
			InputStreamReader reader = new InputStreamReader(stream);
			reader.read(buffer);
			stream.close();

			JSONObject result;
			String strBuffer = String.valueOf(buffer);

			Log.d("Auth", "server's respond: " + String.valueOf(buffer));
			Log.d("Auth", "server's respond (the value of strBuffer): "
					+ strBuffer);
			// buffer sometimes is corrupted when error occurrences
			// see log below:
			// <style>BODY { color: #000000; background-color: white;
			// font-family: Verdana; margin-left: 0px; margin-top: 0px; }
			// #content { margin-left: 30px; font-size: .70em; padding-bottom:
			// 2em; } A:link { color: #336699; font-weight: bold;
			// text-decoration: underline; } A:visited { color: #6699cc;
			// font-weight: bold; text-decoration: underline; } A:active {
			// color: #336699; font-weight: bold; text-decoration: underline; }
			// .heading1 { background-color: #003366; border-bottom: #336699 6px
			// solid; color: #ffffff; font-family: Tahoma; font-size: 26px;
			// font-weight: normal;margin: 0em 0em 10px -20px; padding-bottom:
			// 8px; padding-left: 30px;padding-top: 16px;} pre {
			// font-size:small; background-color: #e5e5cc; padding: 5px;
			// font-family: Courier New; margin-top: 0px; border: 1px #f0f0e0
			// solid; white-space: pre-wrap; white-space: -pre-wrap; word-wrap:
			// break-word; } table { border-collapse: collapse; border-spacing:
			// 0px; font-family: Verdana;} table th { border-right: 2px white
			// solid; border-bottom: 2px white solid; font-weight: bold;
			// backgr... THERE ARE BAD SIMBOLS FUTHER IN CP1251 ENCODING (just
			// see log)
			//
			// so String.indexOf("The exception message is '") returns -1
			try {
				result = new JSONObject(strBuffer);
			} catch (Exception ex) {
				Log.d("Auth", "exception during creating JSONObject occurred.");
				int startIndex = strBuffer.indexOf(magicExcStartStr);
				int endIndex = strBuffer.indexOf(magicExcEndStr);
				if (startIndex != -1 && endIndex != -1) {
					startIndex += magicExcStartStr.length();
					endIndex -= 1;
					// Log.d("Auth", "startIndex: " + String.valueOf(startIndex)
					// + "; endIndex: " + String.valueOf(endIndex));
					result = new JSONObject("{\"Exception\":\""
							+ strBuffer.subSequence(startIndex, endIndex)
							+ "\"}");
					Log.d("Auth", result.toString());
				} else {
					result = new JSONObject(
							"{\"Exception\":\"Error occurred. Can't parse error's text from server's respond :(\"}");
				}
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
