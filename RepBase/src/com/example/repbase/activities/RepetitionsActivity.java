package com.example.repbase.activities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TextView;

import com.example.repbase.Common;
import com.example.repbase.DBInterface;
import com.example.repbase.R;
import com.example.repbase.classes.BaseWithJSONSkills;
import com.example.repbase.classes.Group;
import com.example.repbase.classes.GroupWithJSONSkills;
import com.example.repbase.classes.RoomTimeWithJSONSkills;
import com.example.repbase.classes.RepetitionWithJSONSkills;
import com.example.repbase.classes.RoomWithJSONSkills;
import com.example.repbase.classes.SessionState;

public class RepetitionsActivity extends Activity implements OnClickListener
{
	private Button btnReserveRep;
	private TextView tvNoDate;
	@Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repetitions);
        btnReserveRep = (Button) findViewById(R.id.addRepetitionButton);
        btnReserveRep.setOnClickListener(this);
        
        tvNoDate = (TextView) findViewById(R.id.tvNoData);
        
        try
        {
        	Refresh();
        }
        catch(Exception e)
        {
        	Log.d(Common.EXC_TAG, this.getClass().getName(), e);
        	ShowMessageBox("Exception occured: " + e);
        }
	}
	
	public void ShowMessageBox(String msg)
    {
    	Common.ShowMessageBox(RepetitionsActivity.this, msg);
    }
	
	private void Refresh()
			throws JSONException, InterruptedException, ExecutionException, TimeoutException
	{
		JSONArray reps = DBInterface.getActiveReptetitions(SessionState.currentUser.getId());
    	if (reps.length() == 0)
    		return;
    	else
    	{
    		LinearLayout layout = (LinearLayout)findViewById(R.id.repLinearLayout_list);
    		LayoutInflater inflater = getLayoutInflater();
    		
    		boolean color = true;
    		
    		layout.removeAllViews();
    		for (int i = 0; i < reps.length(); i++)
    		{
    			JSONObject jRep = reps.getJSONObject(i);
    			
    			final RepetitionWithJSONSkills rep = new RepetitionWithJSONSkills(jRep);
    			if (rep.isCancelled())
    				continue;
    			Log.d(Common.TEMP_TAG, "REPETITION: " + rep.toStringFullInfo());
    			final RoomTimeWithJSONSkills rTime = new RoomTimeWithJSONSkills(jRep.getJSONObject("Time"));
    			Log.d(Common.TEMP_TAG, "REPTIME: " + rTime.toStringFullInfo());
    			final RoomWithJSONSkills rRoom = new RoomWithJSONSkills(jRep.getJSONObject("RepRoom"));
    			Log.d(Common.TEMP_TAG, "ROOM: " + rRoom.toStringFullInfo());
    			final BaseWithJSONSkills rBase = new BaseWithJSONSkills(jRep.getJSONObject("RepBase"));
    			Log.d(Common.TEMP_TAG, "BASE: " + rBase.toStringFullInfo());

    			View item = inflater.inflate(R.layout.replist_item, layout, false);
    			
    			// display base name and room name
    			TextView basename = (TextView)item.findViewById(R.id.baseNameText_list);
    			basename.setText(rBase.getName());
    			TextView roomname = (TextView)item.findViewById(R.id.repRoomText_list);
    			roomname.setText(rRoom.getName());
    			
    			// display date
    			TextView from = (TextView)item.findViewById(R.id.repFromText_list);
    			TextView until = (TextView)item.findViewById(R.id.repUntilText_list);
    			SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm", Common.LOC);
    			sdfTime.setTimeZone(Common.TZONE);
    			from.setText(sdfTime.format(rTime.getStartTime()));
    			until.setText(sdfTime.format(rTime.getEndTime()));
    			
    			// display group
    			TextView group = (TextView)item.findViewById(R.id.groupNameText_list);
    			if (rep.getGroupId() == null)
    				group.setText("Нет группы");
    			else {
        			Group testGroup = new GroupWithJSONSkills(jRep.getJSONObject("RepGroup"));
        			Log.d(Common.TEMP_TAG, "GROUP: " + testGroup.toStringFullInfo());
        			group.setText(testGroup.getName());
    			}
    			
    			// display date
    			TextView date = (TextView)item.findViewById(R.id.repDateText_list);
    			SimpleDateFormat sdfDate = new SimpleDateFormat("dd.MM.yyyy", Common.LOC);
    			sdfDate.setTimeZone(Common.TZONE);
    			date.setText(sdfDate.format(rep.getDate()));
    			
    			// display money info
    			TextView conf = (TextView)item.findViewById(R.id.repConfirmedText_list);
    			if (rep.isConfirmed())
    			{
    				conf.setText("да");
    				TextView repcostlabel = (TextView)item.findViewById(R.id.repCostLabel_list);
    				repcostlabel.setVisibility(0);
    				TextView repcosttext = (TextView)item.findViewById(R.id.repCostText_list);
    				repcosttext.setVisibility(0);
    				repcosttext.setText(String.valueOf(rTime.getCost()));
 
    				TextView reppayedlabel = (TextView)item.findViewById(R.id.repPayedLabel_list);
    				reppayedlabel.setVisibility(0);
    				TextView reppayedtext = (TextView)item.findViewById(R.id.repPayedText_list);
    				reppayedtext.setVisibility(0);
    				reppayedtext.setText(String.valueOf(rep.getPayed()));
    			}
    			else
    				conf.setText("нет");
    			
    			
    			item.getLayoutParams().width = LayoutParams.MATCH_PARENT;
    			if (color)
    				item.setBackgroundColor(getResources().getColor(R.color.purple_item));
    			else
    				item.setBackgroundColor(getResources().getColor(R.color.blue_item));
    			color = !color;
    			
    			// hide cancel button
    			// Calendar object with date
    			Calendar calDate = new GregorianCalendar(Common.TZONE, Common.LOC);
    			calDate.setTime(rep.getDate());
    			// Calendar object with time
    			Calendar calTime = new GregorianCalendar(Common.TZONE, Common.LOC);
    			calTime.setTime(rTime.getStartTime());
    			// Calendar object with date and time
    			Calendar startMoment = new GregorianCalendar(Common.TZONE, Common.LOC);
    			startMoment.set(calDate.get(Calendar.YEAR),
						calDate.get(Calendar.MONTH),
						calDate.get(Calendar.DAY_OF_MONTH),
						calTime.get(Calendar.HOUR_OF_DAY),
						calTime.get(Calendar.MINUTE),
						calTime.get(Calendar.SECOND));
    			// hide button if rep started after current moment
    			if(startMoment.before(GregorianCalendar.getInstance(Common.TZONE, Common.LOC)))
    			{
    				Button canc = (Button)item.findViewById(R.id.cancelRepButton_list);
    				canc.setVisibility(android.view.View.INVISIBLE);
    			}
    			
    			Button cancelButton = (Button)item.findViewById(R.id.cancelRepButton_list);
    			OnClickListener cancelButton_click = new OnClickListener()
    			{
					public void onClick(View v)
					{
						try {
							rep.cancel();
							CancelRepDialogFragment dialog = new CancelRepDialogFragment();
							FragmentManager fm = getFragmentManager();
							dialog.show(fm, "tag");
							Refresh();
						} catch (Exception e) {
							Common.ShowMessageBox(
									getApplicationContext(),
									getString( R.string.errCancelRep) + ": "
											+ e.getMessage());
							Log.d(Common.EXC_TAG, this.getClass().getName(), e);
						}
						
					}
    			};
    			cancelButton.setOnClickListener(cancelButton_click);
    			
    			layout.addView(item);
    		}
    		if(layout.getChildCount()==0)
    			tvNoDate.setVisibility(View.VISIBLE);
    		else
    			tvNoDate.setVisibility(View.GONE);
    			
    	}
	}

	public void onClick(View v) {
		Log.d(Common.TEMP_TAG, "onClick(View v)");
		if(v.getId() == R.id.addRepetitionButton) {
			Log.d(Common.TEMP_TAG, "R.id.addRepetitionButton");
			Intent intent = new Intent(this, AllBasesListActivity.class);
			startActivity(intent);
		}
	}

	@Override
	protected void onRestart() {
		super.onRestart();
        try
        {
        	Refresh();
        }
        catch(Exception e)
        {
        	Log.d(Common.EXC_TAG, this.getClass().getName(), e);
        	ShowMessageBox("Exception occured: " + e);
        }
	}
}
