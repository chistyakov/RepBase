package com.example.repbase.activities;

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
import com.example.repbase.classes.UserWithJSONskills;

public class MainActivity extends Activity {

	private final String tagAuth = "Auth"; // tag for logging Authentication

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
//				JSONObject auth = new JSONObject();

				try {
					if (!Common.CheckControl(MainActivity.this, nicknameBox,
							"Введите имя пользователя"))
						return;
					if (!Common.CheckControl(MainActivity.this, passwdBox,
							"Введите пароль"))
						return;

					// auth =
					// DBInterface.CheckAuth(nicknameBox.getText().toString(),
					// passwdBox.getText().toString());
					SessionState.currentUser = new UserWithJSONskills(
							nicknameBox.getText().toString(), passwdBox
									.getText().toString());
					// exception will be thrown in case credentials are wrong
					// it's not good style to use exception mechanism instead of
					// branching statements (if-then-else)
					// so UserWithJSONskills.actuality was created :(
					if (SessionState.currentUser.isActual()) {
						Log.d(tagAuth,"user was created. Without any exception.");

						// check created user
						// ctrl+shift+f always crashes this strings
//						Log.d(tagAuth,"id: " + SessionState.currentUser.getId());
//						Log.d(tagAuth,"Nick: " + SessionState.currentUser.getNick());
//						Log.d(tagAuth,"Name: " + SessionState.currentUser.getName());
//						Log.d(tagAuth,"Surname: "
//										+ SessionState.currentUser.getSurname());
//						Log.d(tagAuth,"Password: "
//										+ SessionState.currentUser.getPassword());
//						Log.d(tagAuth,"Phone: " + SessionState.currentUser.getPhone());
//						Log.d(tagAuth,"Email" + SessionState.currentUser.getEmail());
//						Log.d(tagAuth,"deleted: "
//										+ String.valueOf(SessionState.currentUser.getDelStatus()));
//						Log.d(tagAuth,"banned: "
//										+ String.valueOf(SessionState.currentUser.getBanStatus()));
//						Log.d(tagAuth,"admin: "
//										+ String.valueOf(SessionState.currentUser.getFullRightsStatus()));

						
						// deprecated strings						
       					JSONObject uobj = DBInterface.GetUserByNickname(nicknameBox.getText().toString());
       					SessionState.AuthorizedUser = Integer.toString(uobj.getInt("ID"));

						// goto AuthorizedActivity
						Intent intent = new Intent(MainActivity.this,AuthorizedActivity.class);
						startActivity(intent);
					}

					// if (auth.getBoolean("CheckAuthorizationResult"))
					// {
					// JSONObject uobj =
					// DBInterface.GetUserByNickname(nicknameBox.getText().toString());
					// SessionState.AuthorizedUser =
					// Integer.toString(uobj.getInt("ID"));
					// Intent intent = new Intent(MainActivity.this,
					// AuthorizedActivity.class);
					// startActivity(intent);
					// }
					// else
					// ShowMessageBox("Неверные данные");
				} catch (JSONException e) {
					ShowMessageBox(Common.translateToRu(e.getMessage()));
				} catch (Exception e) {
//					Log.d(tagAuth, e.toString());
//					StackTraceElement[] ste = e.getStackTrace();
//					for (int i = 0; i < ste.length; i++) {
//						Log.d(tagAuth, ste[i].toString());						
//					}
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
