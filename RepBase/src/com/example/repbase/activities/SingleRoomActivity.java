package com.example.repbase.activities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.repbase.Common;
import com.example.repbase.R;
import com.example.repbase.classes.BaseWithJSONSkills;
import com.example.repbase.classes.RoomTimeWithJSONSkills;
import com.example.repbase.classes.RoomWithJSONSkills;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

public class SingleRoomActivity extends Activity implements OnClickListener {

	private RoomWithJSONSkills room;

	private Button btnBack;
	private Button btnReserve;
	private TextView tvBaseName;
	private TextView tvRoomName;
	// private TextView tvRoomSquare;
	private ExpandableListView elRepTimes;

	private List<Map<String, String>> lDayOfWeek;
	private List<Map<String, String>> childDataItem;
	private List<List<Map<String, String>>> childData;
	private Map<String, String> m;

	private final String ATTRIBUTE_NAME_DAY = "day";
	private final String ATTRIBUTE_NAME_TIME = "timeRange";
	private final String ATTRIBUTE_NAME_COST = "cost";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single_room);

		btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		btnReserve = (Button) findViewById(R.id.btnReserve);
		btnReserve.setOnClickListener(this);
		tvBaseName = (TextView) findViewById(R.id.tvBaseName);
		tvRoomName = (TextView) findViewById(R.id.tvRoomName);
		// tvRoomSquare = (TextView) findViewById(R.id.tvRoomSquare);

		Intent intent = getIntent();
		final int roomId = intent.getIntExtra("roomId", 0);
		Log.d(Common.TEMP_TAG, String.valueOf(roomId));
		try {
			room = new RoomWithJSONSkills(roomId);
			Log.d(Common.TEMP_TAG, room.toStringFullInfo());
			tvBaseName.setText(getString(R.string.base)
					+ (new BaseWithJSONSkills(room.getBaseId())).getName());
			tvRoomName.setText(getString(R.string.room) + room.getName() + " ("
					+ String.valueOf(room.getSquare()) + " "
					+ getString(R.string.meter) + ")");

			String[] days = getResources().getStringArray(
					R.array.day_of_week_array);

			lDayOfWeek = new ArrayList<Map<String, String>>();
			childData = new ArrayList<List<Map<String, String>>>();

			for (int i = 1; i <= 7; i++) {
				List<RoomTimeWithJSONSkills> lRoomTimes = room.getRoomTimesList(i);
				if (!lRoomTimes.isEmpty()) {
					m = new HashMap<String, String>();
					m.put(ATTRIBUTE_NAME_DAY, days[i - 1]);
					lDayOfWeek.add(m);

					childDataItem = new ArrayList<Map<String, String>>();
					for (RoomTimeWithJSONSkills roomTime : lRoomTimes) {
						m = new HashMap<String, String>();

						SimpleDateFormat sdfTime = new SimpleDateFormat(
								"HH:mm", Common.LOC);
						sdfTime.setTimeZone(Common.TZONE);
						m.put(ATTRIBUTE_NAME_TIME,
								sdfTime.format(roomTime.getStartTime()) + " - "
										+ sdfTime.format(roomTime.getEndTime()));
						m.put(ATTRIBUTE_NAME_COST,
								String.valueOf(roomTime.getCost()) + " "
										+ getString(R.string.rub));
						childDataItem.add(m);
					}
					childData.add(childDataItem);
				}
			}

			String groupFrom[] = new String[] { ATTRIBUTE_NAME_DAY };
			int groupTo[] = new int[] { android.R.id.text1 };

			String childFrom[] = new String[] { ATTRIBUTE_NAME_TIME,
					ATTRIBUTE_NAME_COST };
			int childTo[] = new int[] { R.id.tvRoomTimeRange, R.id.tvRoomTimeCost };

			SimpleExpandableListAdapter adapter = new SimpleExpandableListAdapter(
					this, lDayOfWeek, R.layout.item_roomtimeslist_expandable,
					groupFrom, groupTo, childData, R.layout.item_roomtimeslist,
					childFrom, childTo);

			elRepTimes = (ExpandableListView) findViewById(R.id.elRepTimes);
			elRepTimes.setAdapter(adapter);

		} catch (Exception e) {
			Log.d(Common.EXC_TAG, this.getClass().getName(), e);
			Common.ShowMessageBox(this, e.getMessage());
		}

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBack:
			finish();
			break;
		case R.id.btnReserve:
			Intent intent = new Intent(getApplicationContext(),
					ReserveRepActivity.class);
			intent.putExtra("roomId", room.getId());
			startActivity(intent);
			break;
		}

	}

}
