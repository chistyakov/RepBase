package com.example.repbase.dialogs;

import java.util.Date;
import java.util.List;

import com.example.repbase.Common;
import com.example.repbase.R;
import com.example.repbase.activities.AuthorizedActivity;
import com.example.repbase.classes.GroupWithJSONSkills;
import com.example.repbase.classes.RepetitionWithJSONSkills;
import com.example.repbase.classes.SessionState;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class ChoseGroupDialogFragment extends DialogFragment {
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Bundle b = getArguments();
		Log.d(Common.TEMP_TAG, b.toString());
		final long date = b.getLong("date");
		final int roomTimeId = b.getInt("roomTimeId");
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.pick_group);

		try {
			final List<GroupWithJSONSkills> lGroups = (SessionState.currentUser
					.getGroupsList());
			// insert null group for opportunity to reserve repetition for not
			// group but user
			// lGroups.add(0, null);
			String[] strLGrous = new String[lGroups.size() + 1];
			strLGrous[0] = getString(R.string.self_group);
			for (int i = 0; i < lGroups.size(); i++) {
				strLGrous[i + 1] = lGroups.get(i).getName();
			}
			builder.setItems(strLGrous, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					// The 'which' argument contains the index position
					// of the selected item
					try {
//						RepetitionWithJSONSkills rep;
						if (which == 0) {
							Log.d(Common.TEMP_TAG, "create groupless repetition");
//							rep = RepetitionWithJSONSkills.createNewRepetition(roomTimeId, null, new Date(date));
							RepetitionWithJSONSkills.createNewRepetition(
									roomTimeId, null, new Date(date));
						}
						else {
							GroupWithJSONSkills group = lGroups.get(--which);
//							rep = group.createRepetition(roomTimeId, new Date(date));
							group.createRepetition(roomTimeId, new Date(
									date));
							Log.d(Common.TEMP_TAG, lGroups.get(which)
									.toStringFullInfo());
						}
						// group.createRepetition(roomTimeId, new Date(date));
						
						Common.ShowMessageBox(getActivity(),
								getString(R.string.successCreateRep));
						Intent intent = new Intent(getActivity(), AuthorizedActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//						intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
						startActivity(intent);
					} catch (Exception e) {
						Log.d(Common.EXC_TAG, this.getClass().getSimpleName(),
								e);
						Common.ShowMessageBox(getActivity(), e.getMessage());
					}
				}
			});
		} catch (Exception e) {
			Log.d(Common.EXC_TAG, this.getClass().getSimpleName(), e);
			Common.ShowMessageBox(getActivity(), e.getMessage());
		}

		return builder.create();
	}
}
