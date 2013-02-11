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
       				if (!Common.CheckControl(RegisterActivity.this, nicknameBox, "������� ���"))
       					return;
       				if (!Common.CheckControl(RegisterActivity.this, passwdBox1, "������� ������"))
       					return;
       				if (!passwdBox1.getText().toString().equals(passwdBox2.getText().toString()))
       				{
       					ShowMessageBox("��������� ������ �� ���������");
       					return;
       				}
       				if (!Common.CheckControl(RegisterActivity.this, nameBox, "������� ���"))
       					return;
       				if (!Common.CheckControl(RegisterActivity.this, surnameBox, "������� �������"))
       					return;
       				if (!Common.CheckControl(RegisterActivity.this, phoneBox, "������� ����� ��������"))
       					return;
       				if (!Common.CheckControl(RegisterActivity.this, emailBox, "������� ����� ����������� �����"))
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
       					ad.setTitle("������������ ������");
       					ad.setMessage("������������ ������� ������");
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
       						ShowMessageBox("����� ���� �� 4 �� 15 ��������");
       						return;
       					}
       					else if (exception.equals("Invalid argument. Invalid \"Nickname\" argument - already exists"))
       					{
       						ShowMessageBox("������������ � ����� ����� ��� ���������������");
       						return;
       					}
       					else if (exception.equals("Invalid argument. Invalid \"Password\" argument - length"))
       					{
       						ShowMessageBox("����� ������ �� 4 �� 25 ��������");
       						return;
       					}
       					else if (exception.equals("Invalid argument. Invalid \"Name\" argument - length"))
       					{
       						ShowMessageBox("����� ����� �� 20 ��������");
       						return;
       					}
       					else if (exception.equals("Invalid argument. Invalid \"Surname\" argument - length"))
       					{
       						ShowMessageBox("����� ������� �� 20 ��������");
       						return;
       					}
       					else if (exception.equals("Invalid phone number format"))
       					{
       						//ShowMessageBox("�������� ������ ������ ��������");
       						ShowMessageBox("�������� ����� ������ �������� (���������� 11 ����)");
       						return;
       					}
       					else if (exception.equals("Invalid argument. Invalid \"Phone\" argument - length"))
       					{
       						ShowMessageBox("�������� ����� ������ �������� (���������� 11 ����)");
       						return;
       					}
       					else if (exception.equals("Invalid argument. Invalid \"Phone\" argument - already exists"))
       					{
       						ShowMessageBox("������������ � ����� ������� ���. ��� ���������������");
       						return;
       					}
       					else if (exception.equals("Invalid argument. Invalid \"E-mail\" argument - already exists"))
       					{
       						ShowMessageBox("������������ � ����� e-mail ��� ���������������");
       						return;
       					}
       					else
       						ShowMessageBox("Someshit");
       				}
      		    }
     		    catch (Exception e)
    		    {
     		    	
     		    	//wtf?
     		    	ShowMessageBox("�������� ����� ������ �������� (���������� 11 ����)");
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
