package com.example.repbase.activities;

//import java.net.PasswordAuthentication;

import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.example.repbase.Common;
import com.example.repbase.R;
import com.example.repbase.classes.SessionState;

public class ChangeUserInfoActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_changeuserinfo);

		Button decline = (Button) findViewById(R.id.declineUserChangesButton);
		Button save = (Button) findViewById(R.id.saveUserChangesButton);

		final EditText nickBox = (EditText) findViewById(R.id.nickBox_ch);
		final EditText nameBox = (EditText) findViewById(R.id.nameBox_ch);
		final EditText surnameBox = (EditText) findViewById(R.id.surnameBox_ch);
		final EditText phoneBox = (EditText) findViewById(R.id.phoneBox_ch);
		final EditText emailBox = (EditText) findViewById(R.id.emailBox_ch);
		final EditText oldPassBox = (EditText) findViewById(R.id.oldPassword_ch);
		final EditText newPass1Box = (EditText) findViewById(R.id.newPassword1_ch);
		final EditText newPass2Box = (EditText) findViewById(R.id.newPassword2_ch);

		Reset();

		OnClickListener decline_click = new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(ChangeUserInfoActivity.this, AuthorizedActivity.class);
				startActivity(intent);
			}
		};
		
		decline.setOnClickListener(decline_click);

		OnClickListener save_click = new OnClickListener() {
			public void onClick(View v) {
				try {
					// проверки на заполненность полей
					if (!Common.CheckControl(ChangeUserInfoActivity.this, nickBox, "Введите ник"))
						return;
					if (!Common.CheckControl(ChangeUserInfoActivity.this, nameBox, "Введите имя"))
						return;
					if (!Common.CheckControl(ChangeUserInfoActivity.this, surnameBox, "Введите фамилию"))
						return;
					if (!Common.CheckControl(ChangeUserInfoActivity.this, phoneBox, "Введите номер телефона"))
						return;
					if (!Common.CheckControl(ChangeUserInfoActivity.this, emailBox, "Введите e-mail"))
						return;
					if (!newPass1Box.getText().toString().equals("") || !newPass2Box.getText().toString().equals("")) {
						if (!Common.CheckControl(ChangeUserInfoActivity.this,oldPassBox,
										"Для изменения пароля необходимо указать старый пароль"))
							return;
						if (!SessionState.currentUser.checkPassword(oldPassBox.getText().toString())) {
							ShowMessageBox("Проверьте правильность ввода старого пароля");
							return;
						}
//						Log.d("change", "newPass1Box: " + newPass1Box.getText().toString());
//						Log.d("change", "newPass2Box: " + newPass2Box.getText().toString());
						if (!newPass1Box.getText().toString().equals(newPass2Box.getText().toString())) {
							ShowMessageBox("Введенные пароли не совпадают");
							return;
						}
					}
								
					String makingChangesRes;
					if((SessionState.currentUser.changeNick(nickBox.getText().toString())||
							SessionState.currentUser.changeName(nameBox.getText().toString())||
							SessionState.currentUser.changeSurname(surnameBox.getText().toString())||
							SessionState.currentUser.changeEmail(emailBox.getText().toString())||
							SessionState.currentUser.changePhone(phoneBox.getText().toString()))||
							SessionState.currentUser.changePassword(newPass1Box.getText().toString())) {
						makingChangesRes="Информация успешно изменена";
					}
					else makingChangesRes="Вы не внесли изменений";
					AlertDialog.Builder ad = new AlertDialog.Builder(ChangeUserInfoActivity.this);
					ad.setTitle("Изменение информации");
					ad.setMessage(makingChangesRes);
					ad.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									Intent intent = new Intent(
											ChangeUserInfoActivity.this,
											AuthorizedActivity.class);
									startActivity(intent);
								}
							});
					ad.show();
						
				} catch (JSONException e){
					ShowMessageBox(Common.translateToRu(e.getMessage()));
				}
				catch (Exception e) {
					ShowMessageBox("Exception occured: " + e.toString());
				}
			}
		};
		save.setOnClickListener(save_click);
	}

	private void Reset() {
		final EditText nickBox = (EditText) findViewById(R.id.nickBox_ch);
		final EditText nameBox = (EditText) findViewById(R.id.nameBox_ch);
		final EditText surnameBox = (EditText) findViewById(R.id.surnameBox_ch);
		final EditText phoneBox = (EditText) findViewById(R.id.phoneBox_ch);
		final EditText emailBox = (EditText) findViewById(R.id.emailBox_ch);
		final EditText oldPassBox = (EditText) findViewById(R.id.oldPassword_ch);
		final EditText newPass1Box = (EditText) findViewById(R.id.newPassword1_ch);
		final EditText newPass2Box = (EditText) findViewById(R.id.newPassword2_ch);

		oldPassBox.setText("");
		newPass1Box.setText("");
		newPass2Box.setText("");

		try {
			SessionState.currentUser.refresh();
			
			nickBox.setText(SessionState.currentUser.getNick());
			phoneBox.setText(SessionState.currentUser.getPhone());
			nameBox.setText(SessionState.currentUser.getName());
			surnameBox.setText(SessionState.currentUser.getSurname());
			emailBox.setText(SessionState.currentUser.getEmail());
		} catch (Exception e) {
			ShowMessageBox("Exception occured: " + e.getMessage());
		}
	}

	public void ShowMessageBox(String msg) {
		Common.ShowMessageBox(ChangeUserInfoActivity.this, msg);
	}
}
