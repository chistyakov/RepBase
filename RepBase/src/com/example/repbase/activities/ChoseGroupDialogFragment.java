package com.example.repbase.activities;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.json.JSONException;

import com.example.repbase.Common;
import com.example.repbase.R;
import com.example.repbase.classes.GroupWithJSONSkills;
import com.example.repbase.classes.SessionState;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
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
			final List<GroupWithJSONSkills> lGroups = SessionState.currentUser.getGroupsList();
			String[] strLGrous = new String[lGroups.size()];
			for(int i=0; i<lGroups.size(); i++) {
				strLGrous[i] = lGroups.get(i).getName();
			}
	           builder.setItems(strLGrous, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int which) {
	               // The 'which' argument contains the index position
	               // of the selected item
	            	   Log.d(Common.TEMP_TAG, lGroups.get(which).toStringFullInfo());
	            	   GroupWithJSONSkills group = lGroups.get(which);
	            	   try {
						group.createRepetition(roomTimeId, new Date(date));

					} catch (Exception e) {
						Log.d(Common.EXC_TAG, this.getClass().getSimpleName(), e);
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
