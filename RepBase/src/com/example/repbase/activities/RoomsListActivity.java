package com.example.repbase.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.json.JSONException;

import com.example.repbase.Common;
import com.example.repbase.R;
import com.example.repbase.WrongTimeRangeException;
import com.example.repbase.classes.BaseWithJSONSkills;
import com.example.repbase.classes.RoomWithJSONSkills;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class RoomsListActivity extends ListActivity implements OnClickListener {

	private BaseWithJSONSkills base;
	private Button btnBack;
	private TextView tvBaseName;
	private TextView tvBaseCity;
	private TextView tvBaseAddress;
	private TextView tvBaseDescription;

	private final String ATTRIBUTE_NAME_NAME = "name";
	private final String ATTRIBUTE_NAME_SQUARE = "square";
	private final String ATTRIBUTE_NAME_COSTS_RANGE = "costsRange";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_roomslist);

		btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);

		Intent intent = getIntent();
		final int baseId = intent.getIntExtra("baseId", 0);
		Log.d(Common.TEMP_TAG, String.valueOf(baseId));

		try {
			base = new BaseWithJSONSkills(baseId);
			Log.d(Common.TEMP_TAG, base.toStringFullInfo());

			tvBaseName = (TextView) findViewById(R.id.tvBaseName);
			tvBaseName.setText(base.getName());
			tvBaseCity = (TextView) findViewById(R.id.tvBaseCity);
			tvBaseCity.setText(getString(R.string.city) + base.getCity());
			tvBaseAddress = (TextView) findViewById(R.id.tvBaseAddress);
			tvBaseAddress.setText(getString(R.string.adderss) + base.getAddress());
			tvBaseDescription = (TextView) findViewById(R.id.tvBaseDescription);
			tvBaseDescription.setText(getString(R.string.description) + base.getDescription());
			if (base.getDescription().equals("null"))
				tvBaseDescription.setVisibility(android.view.View.INVISIBLE);

			if (base.getRoomIds().size() != 0) {
				// generate room's list for the base
				List<RoomWithJSONSkills> lRooms = new ArrayList<RoomWithJSONSkills>();
				for (int i : base.getRoomIds()) {
					RoomWithJSONSkills room = new RoomWithJSONSkills(i);
					Log.d(Common.TEMP_TAG, room.toStringFullInfo());
					if (!room.isDeleted())
						lRooms.add(room);
				}
				Log.d(Common.TEMP_TAG, "rooms list size: " + lRooms.size());
				if (lRooms.size() != 0) {
					// add rooms to display list
					ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>(
							lRooms.size());
					Map<String, Object> m;
					for (RoomWithJSONSkills room : lRooms) {
						m = new HashMap<String, Object>();
						m.put(ATTRIBUTE_NAME_NAME, room.getName());
						m.put(ATTRIBUTE_NAME_SQUARE, getString(R.string.square)
								+ String.valueOf(room.getSquare()));
						m.put(ATTRIBUTE_NAME_COSTS_RANGE,
								getString(R.string.repCost)
										+ convCostsRangeToString(room,
												getString(R.string.tvNoData)));
						data.add(m);
					}
					String[] from = { ATTRIBUTE_NAME_NAME,
							ATTRIBUTE_NAME_SQUARE, ATTRIBUTE_NAME_COSTS_RANGE };
					int[] to = { R.id.tvRoomName, R.id.tvRooomSquare,
							R.id.tvRoomCostsRange };

					SimpleAdapter sAdapter = new SimpleAdapter(this, data,
							R.layout.roomslist_item, from, to);
					setListAdapter(sAdapter);
				}

			}
		} catch (Exception e) {
			Log.d(Common.EXC_TAG, this.getClass().getName(), e);
		}
	}

	public void onClick(View v) {
		if (v.getId() == R.id.btnBack) {
			finish();
		}
	}

	private static String convCostsRangeToString(RoomWithJSONSkills room,
			String strNoData) throws InterruptedException, ExecutionException,
			TimeoutException, JSONException {
		try {
			Pair<Double, Double> p = room.getCostRange();
			double minCost = p.first;
			double maxCost = p.second;
			if (minCost == maxCost)
				return String.format(Common.LOC, "%1$.2f", minCost);
			else
				return String.format(Common.LOC, "%1$.2f", minCost) + " - "
						+ String.format(Common.LOC, "%1$.2f", maxCost);
		} catch (WrongTimeRangeException e) {
			return strNoData;
		}
	}

}
