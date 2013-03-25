package com.example.repbase.activities;

import java.util.Iterator;
import java.util.List;

import com.example.repbase.Common;
import com.example.repbase.DBInterface;
import com.example.repbase.R;
import com.example.repbase.classes.GroupWithJSONSkills;
import com.example.repbase.classes.SessionState;
import com.example.repbase.dialogs.AccessCodeInputDialog;
import com.example.repbase.dialogs.AccessCodeInputDialog.AccessCodeInputDialogListener;

import android.app.FragmentManager;
//import android.R;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

public class AllGroupsListActivity extends ListActivity
		implements OnClickListener, AccessCodeInputDialogListener{
	private List<GroupWithJSONSkills> lAllGroups;

	private LinearLayout ll;
	private Button btnBack;
	private final int iBackId = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_custom_list);

			lAllGroups = DBInterface.getAllGroups();
			// blot out deleted groups and groups already contained current user
			Iterator<GroupWithJSONSkills> itr = lAllGroups.iterator();
			while(itr.hasNext()){
				GroupWithJSONSkills group = itr.next();
				if(group.isDeleted() || group.getUserIds().contains(SessionState.currentUser.getId()))
					itr.remove();
			}
			
			// add "back" button to the header
			ll = (LinearLayout) findViewById(R.id.llHeader);
			LayoutParams lpButton = new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT);
			btnBack = new Button(this);
			btnBack.setId(iBackId);
			btnBack.setText(getString(R.string.btnBackText));
			btnBack.setOnClickListener(this);
			ll.removeAllViews();
			ll.addView(btnBack, lpButton);

			// fill ListView with groups
			if (lAllGroups.size() != 0) {
				ArrayAdapter<GroupWithJSONSkills> adapter = new ArrayAdapter<GroupWithJSONSkills>(
						this, android.R.layout.simple_list_item_1, lAllGroups);
				//				AllGroupsArrayAdapter adapter = new AllGroupsArrayAdapter(this,
//						android.R.layout.simple_list_item_1, lAllGroups);
				setListAdapter(adapter);
			}

		} catch (Exception e) {
			Log.d(Common.EXC_TAG, this.getClass().toString(), e);
			Common.ShowMessageBox(this, e.getMessage());
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		try {
			GroupWithJSONSkills group = (GroupWithJSONSkills) l
					.getItemAtPosition(position);
			Log.d(Common.TEMP_TAG,
					"method onListItemClick(...) groupId="
							+ String.valueOf(group.getId()) + " groupName="
							+ group.getName());

			// call dialog
			// dialog'll register this Activity (parent for him) as Listener
			// hence, the method
			// AccessCodeInputDialogListener.onFinishedAccCodeDialog() should be
			// implemented
	        FragmentManager fm = getFragmentManager();
			AccessCodeInputDialog accCodeDialog = AccessCodeInputDialog
					.newInstance(group.getId(), group.getName());
	        accCodeDialog.show(fm, "dialog_acccode");
		} catch (Exception e) {
			Log.d(Common.EXC_TAG, this.getClass().getName(), e);
			Common.ShowMessageBox(this, e.getMessage());
		}
	}

	public void onClick(View v) {
		if (v.getId() == iBackId) {
			goBack();
		}
	}

	private void goBack() {
//		Intent intent = new Intent(this, GroupsActivity.class);
//		startActivity(intent);
		finish();
	}
	
//	@Override
	public void onFinishedAccCodeDialog(int groupId, String inputText) {
		Log.d(Common.TEMP_TAG,
				"method onFinishedAccCodeDialog() in main Activity. groupId="
						+ String.valueOf(groupId) + " inputText=" + inputText);
		try {
			// we have to get group from DB, because I don't wont to play around
			// with Parcel and Parcelable (int groupid is used instead of
			// GroupWithJSONSkills group)
			GroupWithJSONSkills group = new GroupWithJSONSkills(groupId);
			if (group.addUser(SessionState.currentUser, inputText)){
				Common.ShowMessageBox(this,
						getString(R.string.successEnterIntoGroup) + group.getName());
				goBack();
			}
			else
				Common.ShowMessageBox(this, getString(R.string.wrongAccCode));
		} catch (Exception e) {
			Common.ShowMessageBox(this, e.getMessage());
		}
	}
}
