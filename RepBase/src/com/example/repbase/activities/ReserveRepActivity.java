package com.example.repbase.activities;

import com.example.repbase.Common;
import com.example.repbase.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class ReserveRepActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reserverep);

		Intent intent = getIntent();
		final int roomId = intent.getIntExtra("roomId", 0);
		Log.d(Common.TEMP_TAG, String.valueOf(roomId));
	}

}
