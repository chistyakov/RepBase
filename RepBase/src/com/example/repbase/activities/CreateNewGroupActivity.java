package com.example.repbase.activities;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.json.JSONException;

import com.example.repbase.Common;
import com.example.repbase.R;
import com.example.repbase.classes.GroupWithJSONSkills;
import com.example.repbase.classes.SessionState;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class CreateNewGroupActivity extends Activity implements OnClickListener{
	private EditText etName;
	private Button btnOk;
	private Button btnBack;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_new_group);
		
		etName = (EditText) findViewById(R.id.etGroupName);
		btnOk = (Button) findViewById(R.id.btnOk);
		btnBack = (Button) findViewById(R.id.btnBack);
		btnOk.setOnClickListener(this);
		btnBack.setOnClickListener(this);
	}
	public void onClick(View v) {
		try{
		switch (v.getId()) {
			case R.id.btnOk :
				createGroup(etName.getText().toString());
				break;
			case R.id.btnBack:
				goBack();
				break;
		}}catch (Exception e){
			Log.d(Common.EXC_TAG, this.getClass().getName(), e);
			Common.ShowMessageBox(this, e.getMessage());
		}
		
	}
	private void createGroup(String name) throws JSONException, InterruptedException, ExecutionException, TimeoutException{
		GroupWithJSONSkills group = GroupWithJSONSkills.createNewGroup(name,
				SessionState.currentUser);
		Common.ShowMessageBox(this, getString(R.string.successCreateGroup));
		Intent intent = new Intent(this, SingleGroupActivity.class);
		intent.putExtra("groupId", group.getId());
		startActivity(intent);
	}
	
	private void goBack(){
//		Intent intent = new Intent(this, GroupsActivity.class);
//		startActivity(intent);
		finish();
	}

}
