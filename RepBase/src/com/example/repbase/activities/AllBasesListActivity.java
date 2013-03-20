package com.example.repbase.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.json.JSONException;

import com.example.repbase.Common;
import com.example.repbase.DBInterface;
import com.example.repbase.R;
import com.example.repbase.classes.Base;
import com.example.repbase.classes.BaseWithJSONSkills;
import com.example.repbase.classes.RepTimeWithJSONSkills;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class AllBasesListActivity extends ListActivity implements OnClickListener{
	
	private final String ATTRIBUTE_NAME_NAME = "name";
	private final String ATTRIBUTE_NAME_CITY = "city";
	private final String ATTRIBUTE_NAME_ROOMS = "rooms";
	private final String ATTRIBUTE_NAME_COST = "cost";
	private final String ATTRIBUTE_NAME_ID = "id";
	
	private List<BaseWithJSONSkills> lAllBases;
	private Button btnBack;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_bases_list);
		
		btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		
		try {
			lAllBases = DBInterface.getAllBases();
			
			ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>> (lAllBases.size());
			Map<String, Object> m;
			for (int i = 0; i <lAllBases.size(); i++) {
				BaseWithJSONSkills base = lAllBases.get(i);
				m = new HashMap<String, Object>();
				m.put(ATTRIBUTE_NAME_NAME, base.getName());
				m.put(ATTRIBUTE_NAME_CITY, base.getCity());
				m.put(ATTRIBUTE_NAME_ROOMS,
						getString(R.string.tvBaseRoomsCount)
								+ String.valueOf(base.getRoomIds()
										.size()));
				
				// getting information about max and min cost of repTimes for a
				// base is very heavy operation
				// TODO: transfer this logic to the server
//				m.put(ATTRIBUTE_NAME_COST,
//						getCostRangeForBase(base,
//								getString(R.string.tvBaseRepCost)));
				m.put(ATTRIBUTE_NAME_ID, base.getId());
				data.add(m);
			}
			
			String[] from = {ATTRIBUTE_NAME_NAME, ATTRIBUTE_NAME_CITY, ATTRIBUTE_NAME_ROOMS, ATTRIBUTE_NAME_COST};
			int[] to = {R.id.tvBaseName, R.id.tvBaseCity, R.id.tvBaseRooms, R.id.tvBaseCosts};
			
			SimpleAdapter sAdapter = new SimpleAdapter(this, data, R.layout.base_member_box_item, from, to);
			setListAdapter(sAdapter);
			
		} catch (Exception e) {
			Log.d(Common.EXC_TAG, this.getClass().getName(), e);
			Common.ShowMessageBox(getApplicationContext(), e.getMessage());
		}
		
	}
	public void onClick(View v) {
		if(v.getId() == R.id.btnBack) {
			finish();
		}
	}
	
	/**
	 * get min and max cost of repTime for base
	 * method loops for all repTime in all rooms of base
	 * method makes zillion skillion requests to the server 
	 * @param base to analyze
	 * @param strLabel - String value to head the output string
	 * @return String strLabel: minCost - maxCost
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws TimeoutException
	 * @throws JSONException
	 */
	private static String getCostRangeForBase(Base base, String strLabel) throws InterruptedException,
			ExecutionException, TimeoutException, JSONException {
		// search for max and min repTime's cost
		List<RepTimeWithJSONSkills> repTimeList = new ArrayList<RepTimeWithJSONSkills>();
		for (int roomId : base.getRoomIds())
			repTimeList.addAll(DBInterface.getRepTimesListByRoom(roomId));
		double maxCost = 0.0;
		double minCost = Double.MAX_VALUE;
		for (RepTimeWithJSONSkills repTime : repTimeList) {
			double cost = repTime.getCostPerHour();
			if (cost == -1.0)
				continue;
			if (cost > maxCost)
				maxCost = cost;
			if (cost < minCost)
				minCost = cost;
		}
		return strLabel + String.format("%1$.2f", minCost) + " - "
				+ String.format("%1$.2f", maxCost);
	}
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
    	Map<String, Object> m = (Map<String, Object>)l.getItemAtPosition(position);
    	Log.i(Common.TEMP_TAG, "onListItemClick: " + position + "; " + m.get(ATTRIBUTE_NAME_ID));
    	
    	Intent intent = new Intent(getApplicationContext(), ReserveRepTimeActivity.class);
    	intent.putExtra("baseId",  (Integer)m.get(ATTRIBUTE_NAME_ID));
    	startActivity(intent);

	}
	
}
