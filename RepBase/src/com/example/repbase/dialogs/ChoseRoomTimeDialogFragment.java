package com.example.repbase.dialogs;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.example.repbase.Common;
import com.example.repbase.R;
import com.example.repbase.classes.RoomTimeWithJSONSkills;
import com.example.repbase.classes.RoomWithJSONSkills;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

public class ChoseRoomTimeDialogFragment extends DialogFragment {
	// private int roomId;
	// private Date date;
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Bundle b = getArguments();
		Log.d(Common.TEMP_TAG, b.toString());

		final int roomId = b.getInt("roomId");
		final Date d = new Date(b.getLong("dateInMills"));
		Calendar cal = new GregorianCalendar(Common.TZONE, Common.LOC);
		cal.setTime(d);
		// we have to set the first day of week to friday to send right number to server
//		cal.setFirstDayOfWeek(Calendar.SATURDAY);
		Log.d(Common.TEMP_TAG, this.getClass().getSimpleName() +": " + cal.toString());

		RoomWithJSONSkills room;
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.pick_room_time);

		try {
			room = new RoomWithJSONSkills(roomId);
			final List<RoomTimeWithJSONSkills> lTimes = room
					.getRoomTimesList(Common.convCalDayOfWeekToDBSpecific(cal
							.get(Calendar.DAY_OF_WEEK)));
			String[] strTimesList = new String[lTimes.size()];
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Common.LOC);
			sdf.setTimeZone(Common.TZONE);
			for (int i = 0; i < lTimes.size(); i++) {
				RoomTimeWithJSONSkills t = lTimes.get(i);
				strTimesList[i] = sdf.format(t.getStartTime()) + " - "
						+ sdf.format(t.getEndTime()) + " "
						+ String.valueOf(t.getCost()) + " " + getString(R.string.rub);
			}
			builder.setItems(strTimesList,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// The 'which' argument contains the index position
							// of the selected item
							Log.d(Common.TEMP_TAG, String.valueOf(which));
							Log.d(Common.TEMP_TAG, lTimes.get(which).toStringFullInfo());
							
							ChoseGroupDialogFragment df = new ChoseGroupDialogFragment();
							Bundle args = new Bundle();
							args.putLong("date", d.getTime());
							args.putInt("roomTimeId", lTimes.get(which).getId());
							df.setArguments(args);
							df.show(getFragmentManager(), "ChoseGroupDialogFragment");
						}
					});
		} catch (Exception e) {
			Log.d(Common.EXC_TAG, this.getClass().getSimpleName(), e);
			Common.ShowMessageBox(getActivity(), e.getMessage());
		}

		return builder.create();
	}
}
