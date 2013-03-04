package com.example.repbase;

import java.util.List;

import com.example.repbase.classes.GroupWithJSONSkills;
import com.example.repbase.classes.SessionState;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
// currently not used
public class AllGroupsArrayAdapter extends ArrayAdapter<GroupWithJSONSkills> {

	private List<GroupWithJSONSkills> lGroups;
	private Context tempC;
	public AllGroupsArrayAdapter(Context context, int textViewResourceId,
			List<GroupWithJSONSkills> groups) {
		super(context, textViewResourceId, groups);
		this.lGroups = groups;
		this.tempC = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View item = super.getView(position, convertView, parent);
		GroupWithJSONSkills group = lGroups.get(position);
		if (group.getUserIds()
				.contains(SessionState.currentUser.getId())) {
			item.setClickable(false);
			item.setFocusable(false);
			item.setBackgroundColor(tempC.getResources()
					.getColor(R.color.Green));
		}
		return item;
	}

}
