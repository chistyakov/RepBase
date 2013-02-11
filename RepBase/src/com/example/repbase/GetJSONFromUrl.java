package com.example.repbase;

import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.os.AsyncTask;

public class GetJSONFromUrl extends AsyncTask<String, Integer, JSONObject>
{
	protected JSONObject doInBackground(String... Urls)
	{
		try
		{
			HttpGet request = new HttpGet(Urls[0]);
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-type", "application/json");
			
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpResponse response = httpClient.execute(request);
			HttpEntity resp = response.getEntity();
			
			char[] buffer = new char[(int)resp.getContentLength()];
    	    InputStream stream = resp.getContent();
	        InputStreamReader reader = new InputStreamReader(stream);
	        reader.read(buffer);
	        stream.close();
	        
	        JSONObject result = new JSONObject();
	        
	        try
	        {
	        	result = new JSONObject(new String(buffer));
	        }
	        catch (Exception ex)
	        {
	        	int startIndex = new String(buffer).indexOf("The exception message is '") + 26;
	        	int endIndex = new String(buffer).indexOf("'. See server") - 1;
	        	result = new JSONObject("{\"Exception\":\"" + new String(buffer).subSequence(startIndex, endIndex) + "\"}");
	        }
	        return result;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
