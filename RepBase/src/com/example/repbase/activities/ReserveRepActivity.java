package com.example.repbase.activities;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import com.example.repbase.Common;
import com.example.repbase.R;
import com.example.repbase.classes.RoomTimeWithJSONSkills;
import com.example.repbase.classes.RoomWithJSONSkills;
import com.exina.android.calendar.CalendarView;
import com.exina.android.calendar.Cell;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ReserveRepActivity extends Activity implements OnClickListener,
		CalendarView.OnCellTouchListener {

	private Button btnBack;
	private CalendarView cv = null;
	private Button btnPrevMonth;
	private Button btnNextMonth;
	private TextView tvMonth;
	private String[] monthes;
	private int roomId = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reserverep);

		try {

			btnBack = (Button) findViewById(R.id.btnBack);
			btnBack.setOnClickListener(this);

			btnPrevMonth = (Button) findViewById(R.id.btnPrev);
			btnPrevMonth.setOnClickListener(this);

			btnNextMonth = (Button) findViewById(R.id.btnNext);
			btnNextMonth.setOnClickListener(this);

			tvMonth = (TextView) findViewById(R.id.month);

			monthes = getResources().getStringArray(R.array.monthes);
			cv = (CalendarView) findViewById(R.id.calendar);
			cv.setOnCellTouchListener(this);
			refreshControls();

			Intent intent = getIntent();
			roomId = intent.getIntExtra("roomId", 0);
			SparseArray<List<RoomTimeWithJSONSkills>> saRoomTimePerDay = new SparseArray<List<RoomTimeWithJSONSkills>>(
					7);
			RoomWithJSONSkills room = new RoomWithJSONSkills(roomId);

			for (int i = 0; i < 7; i++)
				saRoomTimePerDay.append(i, room.getRoomTimesList(i + 1));

			cv.setDaysOfWeek(saRoomTimePerDay);

		} catch (Exception e) {
			Log.d(Common.EXC_TAG, this.getClass().getSimpleName(), e);
			Common.ShowMessageBox(this, e.getMessage());
		}
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBack:
			finish();
			break;
		case R.id.btnPrev:
			cv.previousMonth();
			refreshControls();
			break;
		case R.id.btnNext:
			cv.nextMonth();
			refreshControls();
			break;
		}
	}

	public void onTouch(Cell cell) {
		// int year = cv.getYear();
		// int month = cv.getMonth();
		// int day = cell.getDayOfMonth();

		if (cell instanceof CalendarView.GrayCell)
			return;
		Common.ShowMessageBox(
				this,
				cell.getDayOfMonth() + " " + monthes[cv.getMonth()] + " "
						+ cv.getYear());
		ChoseRoomTimeDialogFragment dialog = new ChoseRoomTimeDialogFragment();

		Bundle args = new Bundle();
		args.putInt("roomId", roomId);
		Calendar d = new GregorianCalendar(Common.TZONE, Common.LOC);
		d.setFirstDayOfWeek(Calendar.MONDAY);
		Log.d(Common.TEMP_TAG, this.getClass().getSimpleName() +": " + (new Date(cv.getYear(), cv.getMonth()+1, cell.getDayOfMonth())));
		d.set(cv.getYear(), cv.getMonth(), cell.getDayOfMonth(), 0, 0, 0);
//		d.set(Calendar.HOUR_OF_DAY, 0);
//		d.set(Calendar.MINUTE, 0);
//		d.set(Calendar.SECOND, 0);
//		d.set(Calendar.MILLISECOND, 0);
		args.putLong("dateInMills", d.getTimeInMillis());
		Log.d(Common.TEMP_TAG, this.getClass().getSimpleName() +": " + d.toString());
		dialog.setArguments(args);

		FragmentManager fm = getFragmentManager();
		dialog.show(fm, "ChoseRoomTimeDialogFragment");
	}

	private void refreshControls() {
		Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT+0400"),
				new Locale("RU"));

		tvMonth.setText(monthes[cv.getMonth()] + " "
				+ String.valueOf(cv.getYear()));
		if ((cv.getMonth() <= cal.get(Calendar.MONTH) & cv.getYear() == cal
				.get(Calendar.YEAR)) || cv.getYear() < cal.get(Calendar.YEAR)) {
			btnPrevMonth.setVisibility(android.view.View.INVISIBLE);
		} else
			btnPrevMonth.setVisibility(android.view.View.VISIBLE);
	}

}
