package com.example.repbase.activities;

import org.json.JSONObject;

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
import com.example.repbase.DBInterface;
import com.example.repbase.R;
import com.example.repbase.classes.SessionState;

public class RegisterActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		Button submitData = (Button)findViewById(R.id.submitData);
		final EditText nicknameBox = (EditText)findViewById(R.id.nicknameBox_r);
		final EditText passwdBox1 = (EditText)findViewById(R.id.passwdBox1);
		final EditText passwdBox2 = (EditText)findViewById(R.id.passwdBox2);
		final EditText nameBox = (EditText)findViewById(R.id.nameBox);
		final EditText surnameBox = (EditText)findViewById(R.id.surnameBox);
		final EditText phoneBox = (EditText)findViewById(R.id.phoneBox);
		final EditText emailBox = (EditText)findViewById(R.id.emailBox);
		
		OnClickListener submitData_click = new OnClickListener()
        {
        	public void onClick(View v)
       		{
       			try
     	        {
       				if (!Common.CheckControl(RegisterActivity.this, nicknameBox, "Введите ник"))
       					return;
       				if (!Common.CheckControl(RegisterActivity.this, passwdBox1, "Введите пароль"))
       					return;
       				if (!passwdBox1.getText().toString().equals(passwdBox2.getText().toString()))
       				{
       					ShowMessageBox("Введенные пароли не совпадают");
       					return;
       				}
       				if (!Common.CheckControl(RegisterActivity.this, nameBox, "Введите имя"))
       					return;
       				if (!Common.CheckControl(RegisterActivity.this, surnameBox, "Введите фамилию"))
       					return;
       				if (!Common.CheckControl(RegisterActivity.this, phoneBox, "Введите номер телефона"))
       					return;
       				if (!Common.CheckControl(RegisterActivity.this, emailBox, "Введите адрес электронной почты"))
       					return;
       				
       				JSONObject newusr = DBInterface.CreateUser(
       						nicknameBox.getText().toString(), 
       						passwdBox1.getText().toString(), 
       						nameBox.getText().toString(), 
       						surnameBox.getText().toString(), 
       						phoneBox.getText().toString(), 
       						emailBox.getText().toString()
       						);
       				
       				try
       				{
       					newusr.getString("Password");
       					SessionState.AuthorizedUser = Integer.toString(newusr.getInt("ID"));
       					AlertDialog.Builder ad = new AlertDialog.Builder(RegisterActivity.this);
       					ad.setTitle("Пользователь создан");
       					ad.setMessage("Пользователь успешно создан");
       					ad.setPositiveButton("OK", new DialogInterface.OnClickListener()
       					{
       						public void onClick(DialogInterface dialog, int arg1)
       						{
       							Intent intent = new Intent(RegisterActivity.this, AuthorizedActivity.class);
       							startActivity(intent);
       						}
       					});
       					ad.show();
       				}
       				catch (Exception ex)
       				{
       					String exception = newusr.getString("Exception");
       					
       					if (exception.equals("Invalid argument. Invalid \"Nickname\" argument - length"))
       					{
       						ShowMessageBox("Длина ника от 4 до 15 символов");
       						return;
       					}
       					else if (exception.equals("Invalid argument. Invalid \"Nickname\" argument - already exists"))
       					{
       						ShowMessageBox("Пользователь с таким ником уже зарегистрирован");
       						return;
       					}
       					else if (exception.equals("Invalid argument. Invalid \"Password\" argument - length"))
       					{
       						ShowMessageBox("Длина пароля от 4 до 25 символов");
       						return;
       					}
       					else if (exception.equals("Invalid argument. Invalid \"Name\" argument - length"))
       					{
       						ShowMessageBox("Длина имени до 20 символов");
       						return;
       					}
       					else if (exception.equals("Invalid argument. Invalid \"Surname\" argument - length"))
       					{
       						ShowMessageBox("Длина фамилии до 20 символов");
       						return;
       					}
       					else if (exception.equals("Invalid phone number format"))
       					{
       						//ShowMessageBox("Неверный формат номера телефона");
       						ShowMessageBox("Неверная длина номера телефона (необходимо 11 цифр)");
       						return;
       					}
       					else if (exception.equals("Invalid argument. Invalid \"Phone\" argument - length"))
       					{
       						ShowMessageBox("Неверная длина номера телефона (необходимо 11 цифр)");
       						return;
       					}
       					else if (exception.equals("Invalid argument. Invalid \"Phone\" argument - already exists"))
       					{
       						ShowMessageBox("Пользователь с таким номером тел. уже зарегистрирован");
       						return;
       					}
       					else if (exception.equals("Invalid argument. Invalid \"E-mail\" argument - already exists"))
       					{
       						ShowMessageBox("Пользователь с таким e-mail уже зарегистрирован");
       						return;
       					}
       					else
       						ShowMessageBox("Someshit");
       				}
      		    }
     		    catch (Exception e)
    		    {
     		    	
     		    	//wtf?
     		    	ShowMessageBox("Неверная длина номера телефона (необходимо 11 цифр)");
      		    }
       		}
        };
        
        submitData.setOnClickListener(submitData_click);
	}
	
	public void ShowMessageBox(String msg)
    {
    	Common.ShowMessageBox(RegisterActivity.this, msg);
    }
}
