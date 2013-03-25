package com.example.repbase.activities;

import java.util.List;

import com.example.repbase.Common;
import com.example.repbase.R;
import com.example.repbase.classes.GroupWithJSONSkills;
import com.example.repbase.classes.SessionState;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class GroupsActivity extends ListActivity implements OnClickListener{
//TODO: try to add buttons directly to llHeader (currently view element is used)

	private Button btnEnterIntoGroup;
	private Button btnCreateGroup;
	private TextView tvGreeting;
	private LinearLayout llHeader;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_custom_list);
		refresh();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		refresh();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		GroupWithJSONSkills g = (GroupWithJSONSkills) l.getItemAtPosition(position);
		Intent intent = new Intent(this, SingleGroupActivity.class);
		intent.putExtra("groupId", g.getId());
		startActivity(intent);
	}
	
//	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
			case  R.id.btnEnterIntoGroup:
				intent = new Intent(this, AllGroupsListActivity.class);
				break;
			case R.id.btnCreateGroup:
				intent = new Intent(this, CreateNewGroupActivity.class);
				break;
		}
		startActivity(intent);
	}
	
	private void refresh() {
		try {

			// create header for the list from xml
			// inflate EnterIntoGroup button
			View vHeader = getLayoutInflater().inflate(
					R.layout.activity_groups_header, null);
			btnEnterIntoGroup = (Button) vHeader
					.findViewById(R.id.btnEnterIntoGroup);
			btnEnterIntoGroup
					.setText(getString(R.string.btnEnterIntoGroupText));
			btnEnterIntoGroup.setOnClickListener(this);

			// inflate CreateGroup button
			btnCreateGroup = (Button) vHeader.findViewById(R.id.btnCreateGroup);
			btnCreateGroup.setText(getString(R.string.btnCreateNewGroupText));
			btnCreateGroup.setOnClickListener(this);

			// refresh user's info from server (groups list can be modified by
			// other user)
			SessionState.currentUser.refreshFromServer();
			// list with user's groups
			List<GroupWithJSONSkills> lGroups = SessionState.currentUser
					.getGroupsList();
			String greating;
			// check SessionState.currentUser's groupslist first to do not
			// call to server if user doesn't contain in groups
			if (!SessionState.currentUser.getGroupsIDList().isEmpty()
					&& !lGroups.isEmpty()) {
				ArrayAdapter<GroupWithJSONSkills> adapter = new ArrayAdapter<GroupWithJSONSkills>(
						this, android.R.layout.simple_list_item_1, lGroups);
				setListAdapter(adapter);
				// set greeting text: "You exist in those groups:"
				greating = getString(R.string.tvGroupsGreetingText);
			} else {
				setListAdapter(null);
				// set text for case when user doesn't have groups
				greating = getString(R.string.tvGroupsGreetingTextFail);
			}

			tvGreeting = (TextView) vHeader.findViewById(R.id.tvGroupsGreeting);
			tvGreeting.setText(greating);

			llHeader = (LinearLayout) findViewById(R.id.llHeader);
			llHeader.removeAllViews();
			llHeader.addView(vHeader);
		} catch (Exception e) {
			Log.d(Common.EXC_TAG, this.getClass().getName(), e);
			Common.ShowMessageBox(this, e.getMessage());
		}
	}
}
