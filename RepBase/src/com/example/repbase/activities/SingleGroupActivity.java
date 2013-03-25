package com.example.repbase.activities;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.json.JSONException;

import com.example.repbase.Common;
import com.example.repbase.R;
import com.example.repbase.classes.GroupWithJSONSkills;
import com.example.repbase.classes.SessionState;
import com.example.repbase.classes.UserWithJSONSkills;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

// TODO: add clarification dialogs for leaving the group and deleting the group
public class SingleGroupActivity extends Activity implements OnClickListener{

	private TextView tvGroupName;
	private TextView tvAccCode;
	
	private TextView tvMemberGroupHeader;
	private LinearLayout llMemberGroup;
	
	private Button btnGenerateAccCode;
	private Button btnLeaveGroup;
	private Button btnDeleteGroup;
	private Button btnBack;
	
	GroupWithJSONSkills group;
	
	List<UserWithJSONSkills> lUsers = new ArrayList<UserWithJSONSkills>();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_single_group);

			Intent intent = getIntent();
			Log.d(Common.TEMP_TAG, String.valueOf(intent.getIntExtra("groupId", 0)));
			group = new GroupWithJSONSkills(intent.getIntExtra("groupId", 0));
			lUsers = group.getUsersList();
			
			tvGroupName = (TextView) findViewById(R.id.tvGroupName);
			tvAccCode = (TextView) findViewById(R.id.tvAccCode);
			
			// show group's member's
			tvMemberGroupHeader = (TextView) findViewById(R.id.tvGroupMemberHeader);
			llMemberGroup = (LinearLayout) findViewById(R.id.llGroupMemberBox);
			LayoutInflater li = getLayoutInflater();
			// lUser list contains all nondeleted users
			if(!lUsers.isEmpty()){
				tvMemberGroupHeader.setText(getString(R.string.tvGroupMemberHeder));
				// create textview from xml with user's name and surname for every member
				for (UserWithJSONSkills user : lUsers){
					View item = li.inflate(R.layout.item_groupmembers_list, llMemberGroup, false);
					TextView tvName = (TextView) item.findViewById(R.id.tvGroupMember);
					tvName.setText(user.getName() + " " + user.getSurname());
					// mark current user
					if (user.getId() == SessionState.currentUser.getId()) {
						tvName.setText(tvName.getText() + "(Вы)");
					}
					
					item.getLayoutParams().width = LayoutParams.MATCH_PARENT;
					llMemberGroup.addView(item);
				}
			} else
				tvMemberGroupHeader.setText(getString(R.string.tvGroupMemberEmptyHeder));
			
			btnGenerateAccCode = (Button) findViewById(R.id.btnGenerateAccCode);
			btnLeaveGroup = (Button) findViewById(R.id.btnLeaveGroup);
			btnDeleteGroup = (Button) findViewById(R.id.btnDeleteGroup);
			btnBack = (Button) findViewById(R.id.btnBack);
			btnGenerateAccCode.setOnClickListener(this);
			btnLeaveGroup.setOnClickListener(this);
			btnDeleteGroup.setOnClickListener(this);
			btnBack.setOnClickListener(this);

			tvGroupName.setText(group.getName());
			tvAccCode.setText(group.getAccessCode());

		} catch (Exception e){
			Log.d(Common.EXC_TAG, this.getClass().getName(), e);
			Common.ShowMessageBox(this, e.getMessage());			
		}
	}
	public void onClick(View v) {
		try {
			switch (v.getId()) {
				case R.id.btnGenerateAccCode :
					generateNewAccCode();
					break;
				case R.id.btnLeaveGroup :
					leaveGroup();
					break;
				case R.id.btnDeleteGroup :
					deleteGroup();
					break;
				case R.id.btnBack :
					finish();
					break;
			}
		} catch (Exception e) {
			Log.d(Common.EXC_TAG, this.getClass().getName(), e);
			Common.ShowMessageBox(this, e.getMessage());
		}
	}
	
	
	private void generateNewAccCode() throws InterruptedException,
			ExecutionException, TimeoutException, JSONException {
		tvAccCode.setText(group.generateNewAccessCode());
	}
	
	private void leaveGroup() throws InterruptedException, ExecutionException,
			TimeoutException, JSONException {
		group.deleteUser(SessionState.currentUser);
		Common.ShowMessageBox(this, getString(R.string.successLeaveGroup) + group.getName());
		finish();
	}
	
	private void deleteGroup() throws InterruptedException, ExecutionException,
			TimeoutException, JSONException {
		group.delete();
		Common.ShowMessageBox(this, getString(R.string.successDelGroup));
		finish();
	}
}
