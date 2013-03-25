package com.example.repbase.activities;

import java.util.ArrayList;
import java.util.List;

import com.example.repbase.Common;
import com.example.repbase.R;
import com.example.repbase.classes.GroupWithJSONSkills;
import com.example.repbase.classes.SessionState;
import com.example.repbase.classes.UserWithJSONSkills;
import com.example.repbase.dialogs.DeleteGroupDialogFragment;
import com.example.repbase.dialogs.DeleteGroupDialogFragment.DeleteGroupDialogFragmentListener;
import com.example.repbase.dialogs.LeaveGroupDialogFragment;
import com.example.repbase.dialogs.LeaveGroupDialogFragment.LeaveGroupDialogFragmentListener;

import android.app.Activity;
import android.app.DialogFragment;
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

public class SingleGroupActivity extends Activity implements OnClickListener,
		DeleteGroupDialogFragmentListener, LeaveGroupDialogFragmentListener {

	private String DEL_GROUP_DIALOG_TAG = "DeleteGroupDialogFragment";
	private String LEAVE_GROUP_DIALOG_TAG = "LeaveGroupDialogFragment";

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
			Log.d(Common.TEMP_TAG,
					String.valueOf(intent.getIntExtra("groupId", 0)));
			group = new GroupWithJSONSkills(intent.getIntExtra("groupId", 0));
			lUsers = group.getUsersList();

			tvGroupName = (TextView) findViewById(R.id.tvGroupName);
			tvAccCode = (TextView) findViewById(R.id.tvAccCode);

			// show group's member's
			tvMemberGroupHeader = (TextView) findViewById(R.id.tvGroupMemberHeader);
			llMemberGroup = (LinearLayout) findViewById(R.id.llGroupMemberBox);
			LayoutInflater li = getLayoutInflater();
			// lUser list contains all nondeleted users
			if (!lUsers.isEmpty()) {
				tvMemberGroupHeader
						.setText(getString(R.string.tvGroupMemberHeder));
				// create textview from xml with user's name and surname for
				// every member
				for (UserWithJSONSkills user : lUsers) {
					View item = li.inflate(R.layout.item_groupmembers_list,
							llMemberGroup, false);
					TextView tvName = (TextView) item
							.findViewById(R.id.tvGroupMember);
					tvName.setText(user.getName() + " " + user.getSurname());
					// mark current user
					if (user.getId() == SessionState.currentUser.getId()) {
						tvName.setText(tvName.getText() + "(Вы)");
					}

					item.getLayoutParams().width = LayoutParams.MATCH_PARENT;
					llMemberGroup.addView(item);
				}
			} else
				tvMemberGroupHeader
						.setText(getString(R.string.tvGroupMemberEmptyHeder));

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

		} catch (Exception e) {
			Log.d(Common.EXC_TAG, this.getClass().getName(), e);
			Common.ShowMessageBox(this, e.getMessage());
		}
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnGenerateAccCode:
			generateNewAccCode();
			break;
		case R.id.btnLeaveGroup:
			LeaveGroupDialogFragment leaveD = new LeaveGroupDialogFragment();
			leaveD.show(getFragmentManager(), LEAVE_GROUP_DIALOG_TAG);
			break;
		case R.id.btnDeleteGroup:
			DeleteGroupDialogFragment delD = new DeleteGroupDialogFragment();
			delD.show(getFragmentManager(), DEL_GROUP_DIALOG_TAG);
			break;
		case R.id.btnBack:
			Intent intent = new Intent(this, AuthorizedActivity.class);
			// resume AuthorizedActivity and delete all activities from activity
			// stack above AuthorizedActivity
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intent);
			break;
		}
	}

	private void generateNewAccCode() {
		try {
			tvAccCode.setText(group.generateNewAccessCode());
		} catch (Exception e) {
			Log.d(Common.EXC_TAG, this.getClass().getName(), e);
			Common.ShowMessageBox(this, e.getMessage());		
		}
	}

	public void onDialogPositiveClick(DialogFragment dialog) {
		if (dialog.getTag().equals(DEL_GROUP_DIALOG_TAG)) {
			try {
				group.delete();
				Common.ShowMessageBox(this, getString(R.string.successDelGroup));
				finish();
			} catch (Exception e) {
				Log.d(Common.EXC_TAG, this.getClass().getSimpleName(), e);
				Common.ShowMessageBox(getApplication(), e.getMessage());
			}
		} else if (dialog.getTag().equals(LEAVE_GROUP_DIALOG_TAG)) {
			try {
				group.deleteUser(SessionState.currentUser);
				Common.ShowMessageBox(this,
						getString(R.string.successLeaveGroup) + group.getName());
				finish();
			} catch (Exception e) {
				Log.d(Common.EXC_TAG, this.getClass().getSimpleName(), e);
				Common.ShowMessageBox(getApplication(), e.getMessage());
			}
		}

	}

	public void onDialogNegativeClick(DialogFragment dialog) {

	}
}
