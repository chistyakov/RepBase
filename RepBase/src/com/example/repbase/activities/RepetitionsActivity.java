package com.example.repbase.activities;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
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
import com.example.repbase.classes.Attribute;
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
        	ShowMessageBox("Exception occured");
        }
	}
	
	public void ShowMessageBox(String msg)
    {
    	Common.ShowMessageBox(RepetitionsActivity.this, msg);
    }
	
	@SuppressWarnings("deprecation")
	private void Refresh()
			throws JSONException, InterruptedException, ExecutionException
	{
		JSONArray reps = DBInterface.GetActiveReptetitions(SessionState.currentUser.getId());
    	if (reps.length() == 0)
    		return;
    	else
    	{
    		LinearLayout layout = (LinearLayout)findViewById(R.id.repLinearLayout_list);
    		LayoutInflater inflater = getLayoutInflater();
    		
    		boolean color = true;
    		
    		for (int i = 0; i < reps.length(); i++)
    		{
    			View item = inflater.inflate(R.layout.replist_item, layout, false);
    			JSONObject rep = reps.getJSONObject(i);
    			TextView basename = (TextView)item.findViewById(R.id.baseNameText_list);
    			ArrayList<Attribute> baseattrs = Common.GetAttributesList(rep.getJSONObject("RepBase"));
    			for (Attribute attr: baseattrs)
    			{
    				if (attr.Type.equals("Name"))
    				{
    					basename.setText(attr.Value);
    					break;
    				}
    			}
    			TextView roomname = (TextView)item.findViewById(R.id.repRoomText_list);
    			ArrayList<Attribute> room = Common.GetAttributesList(rep.getJSONObject("RepRoom"));
    			for (Attribute atr: room)
    			{
    				if (atr.Type.equals("Name"))
    				{
    					roomname.setText(atr.Value);
    					break;
    				}
    			}
    			TextView from = (TextView)item.findViewById(R.id.repFromText_list);
    			TextView until = (TextView)item.findViewById(R.id.repUntilText_list);
    			JSONObject reptime = rep.getJSONObject("Time"); 
    			String From = reptime.getString("Begin");
    			From = From.replaceAll("[^0-9]", "").substring(0, 8);
    			Date dt = new Date(Long.parseLong(From));
    			From = Integer.toString(dt.getHours() + 4) + ':' + (Integer.toString(dt.getMinutes()).equals("0") ? "00" : Integer.toString(dt.getMinutes()).length()==1 ? "0" + Integer.toString(dt.getMinutes()) : Integer.toString(dt.getMinutes()));
    			from.setText(From);
    			String Until = reptime.getString("End");
    			Until = Until.replaceAll("[^0-9]", "").substring(0, 8);
    			dt = new Date(Long.parseLong(Until));
    			Until = Integer.toString(dt.getHours() + 4) + ':' + (Integer.toString(dt.getMinutes()).equals("0") ? "00" : Integer.toString(dt.getMinutes()).length()==1 ? "0" + Integer.toString(dt.getMinutes()) : Integer.toString(dt.getMinutes()));
    			until.setText(Until);
    			TextView group = (TextView)item.findViewById(R.id.groupNameText_list);
    			if (rep.getString("GroupID") == null || rep.getString("GroupID").equals("null"))
    				group.setText("Нет группы");
    			else
    			{
    				for (Attribute atr: Common.GetAttributesList(rep.getJSONObject("RepGroup")))
    					if (atr.Type.equals("Name"))
    					{
    						group.setText(atr.Value);
    						break;
    					}
    			}
    			TextView date = (TextView)item.findViewById(R.id.repDateText_list);
    			String repdate = rep.getString("Begin").replaceAll("[^0-9]", "").substring(0, 13);
    			dt = new Date(Long.parseLong(repdate));
    			date.setText(Integer.toString(dt.getDate() + 1) + '.' + Integer.toString(dt.getMonth() + 1) + '.' + Integer.toString(dt.getYear() + 1900));
    			TextView conf = (TextView)item.findViewById(R.id.repConfirmedText_list);
    			if (rep.getBoolean("Confirmed"))
    			{
    				conf.setText("да");
    				TextView repcostlabel = (TextView)item.findViewById(R.id.repCostLabel_list);
    				repcostlabel.setVisibility(0);
    				TextView repcosttext = (TextView)item.findViewById(R.id.repCostText_list);
    				repcosttext.setVisibility(0);
    				repcosttext.setText(rep.getString("Cost"));
    				TextView reppayedlabel = (TextView)item.findViewById(R.id.repPayedLabel_list);
    				reppayedlabel.setVisibility(0);
    				TextView reppayedtext = (TextView)item.findViewById(R.id.repPayedText_list);
    				reppayedtext.setVisibility(0);
    				reppayedtext.setText(rep.getString("Payed"));
    			}
    			else
    				conf.setText("нет");
    			item.getLayoutParams().width = LayoutParams.MATCH_PARENT;
    			if (color)
    				item.setBackgroundColor(Color.parseColor("#559966CC"));
    			else
    				item.setBackgroundColor(Color.parseColor("#55336699"));
    			color = !color;
    			
    			if (Long.parseLong(rep.getString("Begin").replaceAll("[^0-9]", "").substring(0, 8)) <= Long.parseLong(Long.toString(new Date().getTime()).substring(0, 8)))
    			{
    				Button canc = (Button)item.findViewById(R.id.cancelRepButton_list);
    				canc.setVisibility(4);
    			}
    			
    			final int id = rep.getInt("ID");
    			
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
									if (DBInterface.CancelRepetition(id))
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
