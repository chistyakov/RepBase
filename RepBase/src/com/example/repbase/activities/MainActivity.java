package com.example.repbase.activities;

import java.util.concurrent.TimeoutException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.example.repbase.Common;
import com.example.repbase.DBInterface;
//import com.example.repbase.DBInterface;
import com.example.repbase.R;
import com.example.repbase.classes.SessionState;
import com.example.repbase.classes.UserWithJSONSkills;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button submit = (Button) findViewById(R.id.submit);
		Button register = (Button) findViewById(R.id.register);
		final EditText nicknameBox = (EditText) findViewById(R.id.nicknameBox);
		final EditText passwdBox = (EditText) findViewById(R.id.passwdBox);

		// обработчик кнопки Войти
		OnClickListener submit_click = new OnClickListener() {
			public void onClick(View v) {
				try {
					if (!Common.CheckControl(MainActivity.this, nicknameBox,
							"Введите имя пользователя"))
						return;
					if (!Common.CheckControl(MainActivity.this, passwdBox,
							"Введите пароль"))
						return;

					SessionState.currentUser = new UserWithJSONSkills(
							nicknameBox.getText().toString(), passwdBox
									.getText().toString());
					// exception will be thrown in case credentials are wrong
					// it's not good style to use exception mechanism instead of
					// branching statements (if-then-else)
					// so UserWithJSONskills.actuality was created :(
					if (SessionState.currentUser.isActual()) {
						Log.d(Common.TEMP_TAG, "user was created. Without any exception.");
						if (!SessionState.currentUser.getDelStatus()) {
							// goto AuthorizedActivity
							Intent intent = new Intent(MainActivity.this,
									AuthorizedActivity.class);
							startActivity(intent);
						}
						else ShowMessageBox("User was deleted.");
					}
				} catch (JSONException e) {
					Log.d(Common.JSON_TAG, this.getClass().getName(), e);
					ShowMessageBox(Common.translateToRu(e.getMessage()));
				} catch (TimeoutException e) {
					Log.d(Common.TIMEOUT_TAG, this.getClass().getName(), e);
					ShowMessageBox(Common.TIMEOUTSTR);
				} catch (Exception e) {
					Log.d(Common.EXC_TAG, this.getClass().getName(), e);
					ShowMessageBox(e.toString());
				}
			}
		};

		// обработчик кнопки Регистрация
		OnClickListener register_click = new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
				startActivity(intent);
			}
		};

		submit.setOnClickListener(submit_click);
		register.setOnClickListener(register_click);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void ShowMessageBox(String msg) {
		Common.ShowMessageBox(MainActivity.this, msg);
	}
}
