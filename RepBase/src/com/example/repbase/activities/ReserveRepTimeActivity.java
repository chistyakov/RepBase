package com.example.repbase.activities;

import com.example.repbase.Common;
import com.example.repbase.R;
import com.example.repbase.classes.BaseWithJSONSkills;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ReserveRepTimeActivity extends Activity implements OnClickListener{
	
	private BaseWithJSONSkills base;
	private Button btnBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reserve_rep);
		
		btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		
		Intent intent = getIntent();
		Log.d(Common.TEMP_TAG, String.valueOf(intent.getIntExtra("baseId", 0)));
		try {
			base = new BaseWithJSONSkills(intent.getIntExtra("baseId", 0));
			Log.d(Common.TEMP_TAG, base.toStringFullInfo());
		} catch (Exception e) {
			Log.d(Common.EXC_TAG, this.getClass().getName(), e);
		}
	}

	public void onClick(View v) {
		if(v.getId() == R.id.btnBack) {
			finish();
		}
	}

}
