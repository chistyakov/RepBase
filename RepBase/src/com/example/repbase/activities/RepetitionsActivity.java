package com.example.repbase.activities;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
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
import com.example.repbase.classes.RepTimeWithJSONSkills;
import com.example.repbase.classes.RepetitionWithJSONSkills;
import com.example.repbase.classes.RoomWithJSONSkills;
import com.example.repbase.classes.SessionState;

public class RepetitionsActivity extends Activity
{
	@Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repetitions);
        
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
    		
    		for (int i = 0; i < reps.length(); i++)
    		{
    			JSONObject jRep = reps.getJSONObject(i);
    			
    			RepetitionWithJSONSkills testRep = new RepetitionWithJSONSkills(jRep);
    			Log.d(Common.TEMP_TAG, "REPETITION: " + testRep.toStringFullInfo());
    			RepTimeWithJSONSkills testTime = new RepTimeWithJSONSkills(jRep.getJSONObject("Time"));
    			Log.d(Common.TEMP_TAG, "REPTIME: " + testTime.toStringFullInfo());
    			RoomWithJSONSkills testRoom = new RoomWithJSONSkills(jRep.getJSONObject("RepRoom"));
    			Log.d(Common.TEMP_TAG, "ROOM: " + testRoom.toStringFullInfo());
    			BaseWithJSONSkills testBase = new BaseWithJSONSkills(jRep.getJSONObject("RepBase"));
    			Log.d(Common.TEMP_TAG, "BASE: " + testBase.toStringFullInfo());

    			View item = inflater.inflate(R.layout.replist_item, layout, false);
    			
    			// display base name and room name
    			TextView basename = (TextView)item.findViewById(R.id.baseNameText_list);
    			basename.setText(testBase.getName());
    			TextView roomname = (TextView)item.findViewById(R.id.repRoomText_list);
    			roomname.setText(testRoom.getName());
    			
    			// display date
    			TextView from = (TextView)item.findViewById(R.id.repFromText_list);
    			TextView until = (TextView)item.findViewById(R.id.repUntilText_list);
    			SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm", Common.loc);
    			sdfTime.setTimeZone(Common.tZone);
    			from.setText(sdfTime.format(testTime.getBegin()));
    			until.setText(sdfTime.format(testTime.getEnd()));
    			
    			// display group
    			TextView group = (TextView)item.findViewById(R.id.groupNameText_list);
    			if (testRep.getGroupId() == null)
    				group.setText("Нет группы");
    			else {
        			Group testGroup = new GroupWithJSONSkills(jRep.getJSONObject("RepGroup"));
        			Log.d(Common.TEMP_TAG, "GROUP: " + testGroup.toStringFullInfo());
        			group.setText(testGroup.getName());
    			}
    			
    			// display date
    			TextView date = (TextView)item.findViewById(R.id.repDateText_list);
    			SimpleDateFormat sdfDate = new SimpleDateFormat("dd.MM.yyyy", Common.loc);
    			sdfDate.setTimeZone(Common.tZone);
    			date.setText(sdfDate.format(testTime.getBegin()));
    			
    			// display money info
    			TextView conf = (TextView)item.findViewById(R.id.repConfirmedText_list);
    			if (testRep.isConfirmed())
    			{
    				conf.setText("да");
    				TextView repcostlabel = (TextView)item.findViewById(R.id.repCostLabel_list);
    				repcostlabel.setVisibility(0);
    				TextView repcosttext = (TextView)item.findViewById(R.id.repCostText_list);
    				repcosttext.setVisibility(0);
    				repcosttext.setText(String.valueOf(testTime.getCost()));
 
    				TextView reppayedlabel = (TextView)item.findViewById(R.id.repPayedLabel_list);
    				reppayedlabel.setVisibility(0);
    				TextView reppayedtext = (TextView)item.findViewById(R.id.repPayedText_list);
    				reppayedtext.setVisibility(0);
    				reppayedtext.setText(String.valueOf(testRep.getPayed()));
    			}
    			else
    				conf.setText("нет");
    			
    			
    			item.getLayoutParams().width = LayoutParams.MATCH_PARENT;
    			if (color)
    				item.setBackgroundColor(getResources().getColor(R.color.purple_item));
    			else
    				item.setBackgroundColor(getResources().getColor(R.color.blue_item));
    			color = !color;
    			
    			// display cancel button
    			if(testTime.getBegin().before(new Date()))
    			{
    				Button canc = (Button)item.findViewById(R.id.cancelRepButton_list);
    				canc.setVisibility(4);
    			}
    			
    			final int id = testRep.getId();
    			
    			Button cancelButton = (Button)item.findViewById(R.id.cancelRepButton_list);
    			OnClickListener cancelButton_click = new OnClickListener()
    			{
					public void onClick(View v)
					{
						AlertDialog.Builder ad = new AlertDialog.Builder(RepetitionsActivity.this);
						ad.setTitle("Отмена репетиции");
						ad.setMessage("Вы действительно хотите отменить репетицию?");
						ad.setPositiveButton("Да", new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog, int which)
							{
								try
								{
									if (DBInterface.cancelRepetition(id))
									{
										AlertDialog.Builder ad2 = new AlertDialog.Builder(RepetitionsActivity.this);
										ad2.setTitle("Отмена репетиции");
										ad2.setMessage("Репетиция успешно отменена");
										ad2.setPositiveButton("OK", new DialogInterface.OnClickListener()
										{	
											public void onClick(DialogInterface dialog, int which) 
											{
												try
												{
													Refresh();
												} 
												catch (JSONException e)
												{
													e.printStackTrace();
												} 
												catch (InterruptedException e)
												{
													e.printStackTrace();
												} 
												catch (ExecutionException e)
												{
													e.printStackTrace();
												} catch (TimeoutException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}
											}
										});
										ad2.show();
									}
									else
									{
										AlertDialog.Builder ad2 = new AlertDialog.Builder(RepetitionsActivity.this);
										ad2.setTitle("Отмена репетиции");
										ad2.setMessage("Не удалось отменить репетицию");
										ad2.setPositiveButton("OK", new DialogInterface.OnClickListener()
										{ public void onClick(DialogInterface dialog, int which){ } });
										ad2.show();
									}
								}
								catch (Exception e)
								{
									
								}
							}
						});
						ad.setNegativeButton("Нет", new DialogInterface.OnClickListener()
						{ public void onClick(DialogInterface dialog, int which){} });
						ad.show();
					}
    			};
    			cancelButton.setOnClickListener(cancelButton_click);
    			
    			layout.addView(item);
    		}
    	}
	}
}
