package com.example.repbase.activities;

import com.example.repbase.Common;
import com.example.repbase.R;
import com.example.repbase.classes.RoomWithJSONSkills;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class SingleRoomActivity extends Activity {

	private RoomWithJSONSkills room;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single_room);
		
		Intent intent = getIntent();
		final int roomId = intent.getIntExtra("roomId", 0);
		Log.d(Common.TEMP_TAG, String.valueOf(roomId));
		try {
			room = new RoomWithJSONSkills(roomId);
			Log.d(Common.TEMP_TAG, room.toStringFullInfo());
		} catch (Exception e) {
			Log.d(Common.EXC_TAG, this.getClass().getName(), e);
			Common.ShowMessageBox(this, e.getMessage());
		}
		
	}

}
