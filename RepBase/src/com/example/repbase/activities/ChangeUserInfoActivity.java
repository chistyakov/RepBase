package com.example.repbase.activities;

//TODO: delete logging

import java.util.concurrent.TimeoutException;

import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.example.repbase.Common;
import com.example.repbase.R;
import com.example.repbase.classes.SessionState;
import com.example.repbase.classes.User;

public class ChangeUserInfoActivity extends Activity {
	
	private EditText nickBox;
	private EditText nameBox;
	private EditText surnameBox;
	private EditText phoneBox;
	private EditText emailBox;
	private EditText oldPassBox;
	private EditText newPass1Box;
	private EditText newPass2Box;
	
	User uBackup;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_changeuserinfo);

		Button decline = (Button) findViewById(R.id.declineUserChangesButton);
		Button save = (Button) findViewById(R.id.saveUserChangesButton);
		
		nickBox = (EditText) findViewById(R.id.nickBox_ch);
		nameBox = (EditText) findViewById(R.id.nameBox_ch);
		surnameBox = (EditText) findViewById(R.id.surnameBox_ch);
		phoneBox = (EditText) findViewById(R.id.phoneBox_ch);
		emailBox = (EditText) findViewById(R.id.emailBox_ch);
		oldPassBox = (EditText) findViewById(R.id.oldPassword_ch);
		newPass1Box = (EditText) findViewById(R.id.newPassword1_ch);
		newPass2Box = (EditText) findViewById(R.id.newPassword2_ch);
		
		uBackup = new User(SessionState.currentUser);
		
		Reset();

		OnClickListener decline_click = new OnClickListener() {
			public void onClick(View v) {
				// recover date from uBackup if "decline" button is clicked
				try {
					// password can't be recovered.
					SessionState.currentUser.refreshFromServer();
					SessionState.currentUser.changeProfileContent(uBackup);
				} catch (JSONException e) {
					ShowMessageBox("Произошла ошибка при попытке восстановить данные: "
							+ Common.translateToRu(e.getMessage()));
				} catch (Exception e) {
					ShowMessageBox("Произошла ошибка при попытке восстановить данные: "
							+ e.toString());					
				}
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
					if (emailBox.getText().toString().length()>Common.MAX_EMAIL_LENGTH){
						ShowMessageBox("Максимальная длина e-mail: " + Common.MAX_EMAIL_LENGTH);
						return;
					}
					boolean changePass; // flag is set in 1 when we should try to change password.
					if (changePass=(!newPass1Box.getText().toString().equals("") || !newPass2Box.getText().toString().equals(""))) {
						// newPass1Box is not empty or newPass2Box is not empty
						if (!Common.CheckControl(ChangeUserInfoActivity.this,oldPassBox,
										"Для изменения пароля необходимо указать старый пароль"))
							return;
						if (!SessionState.currentUser.checkPassword(oldPassBox.getText().toString())) {
							ShowMessageBox("Проверьте правильность ввода старого пароля");
							return;
						}
						if (!newPass1Box.getText().toString().equals(newPass2Box.getText().toString())) {
							ShowMessageBox("Введенные пароли не совпадают");
							return;
						}
						if(newPass1Box.getText().toString().contains(" ")){
							ShowMessageBox("Нелязя использовать пробел, введи нормальный пароль!");
							return;
						}
					}
								
					String makingChangesRes;
					if (SessionState.currentUser.changeProfileContent(nickBox.getText().toString(),
							nameBox.getText().toString(),
							surnameBox.getText().toString(),
							emailBox.getText().toString(),
							phoneBox.getText().toString()) |
							(changePass&&
									SessionState.currentUser.changePassword(newPass1Box.getText().toString())))
						makingChangesRes="Информация успешно изменена";
					else
						makingChangesRes = "Вы не внесли изменений";
						
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
					Log.d(Common.JSON_TAG, this.getClass().toString(), e);
					ShowMessageBox(Common.translateToRu(e.getMessage()));
				} catch (TimeoutException e) {
					Log.d(Common.TIMEOUT_TAG, this.getClass().toString(), e);
					ShowMessageBox(Common.TIMEOUTSTR);
				} catch (Exception e) {
					Log.d(Common.EXC_TAG, this.getClass().toString(), e);
					ShowMessageBox(e.toString());
				}
			}
		};
		save.setOnClickListener(save_click);
	}

	private void Reset() {
		
		oldPassBox.setText("");
		newPass1Box.setText("");
		newPass2Box.setText("");

		try {
			SessionState.currentUser.refreshFromServer();

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
