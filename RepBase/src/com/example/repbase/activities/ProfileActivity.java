package com.example.repbase.activities;

import com.example.repbase.Common;
import com.example.repbase.R;
import com.example.repbase.classes.SessionState;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ProfileActivity extends Activity
{
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
		final TextView nickText = (TextView)findViewById(R.id.nickText);
	    final TextView nameText = (TextView)findViewById(R.id.nameText);
	    final TextView surnameText = (TextView)findViewById(R.id.surnameText);
	    final TextView phoneText = (TextView)findViewById(R.id.phoneText);
	    final TextView emailText = (TextView)findViewById(R.id.emailText);
	    
	    Button logoutButton = (Button)findViewById(R.id.logoutButton);
	    Button changeUserInfoButton = (Button)findViewById(R.id.changeUserInfoButton);
	    Button deleteUserButton = (Button)findViewById(R.id.deleteUserButton);
	    
	    //���������� ������ �����
	    OnClickListener logoutButton_click = new OnClickListener()
	    {
	    	public void onClick(View v)
	    	{
	    		SessionState.currentUser=null;
	    		Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
	    		startActivity(intent);
	    	}
	    };
	    logoutButton.setOnClickListener(logoutButton_click);
	    
	    //���������� ������ �������� ����������
	    OnClickListener changeUserInfoButton_click = new OnClickListener()
	    {
	    	public void onClick(View v)
	    	{
	    		Intent intent = new Intent(ProfileActivity.this, ChangeUserInfoActivity.class);
	    		startActivity(intent);
	    	}
	    };
	    changeUserInfoButton.setOnClickListener(changeUserInfoButton_click);
	    
	    //���������� ������ ������� ������������
	    OnClickListener deleteUserButton_click = new OnClickListener()
	    {
	    	public void onClick(View v)
	    	{
	    		AlertDialog.Builder ad = new AlertDialog.Builder(ProfileActivity.this);
				ad.setTitle("�������� ������������");
				ad.setMessage("�� ������� ��� ������ ������� ����� ������������?");
				ad.setPositiveButton("��", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int arg1)
					{
						try
						{
							if (!SessionState.currentUser.delete())
								throw new Exception();
							AlertDialog.Builder ad2 = new AlertDialog.Builder(ProfileActivity.this);
							ad2.setTitle("�������� ������������");
							ad2.setMessage("������������ ������� ������");
							ad2.setPositiveButton("OK", new DialogInterface.OnClickListener()
							{	
								public void onClick(DialogInterface dialog, int which) 
								{
									Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
									startActivity(intent);
								}
							});
							ad2.show();
						}
						catch(Exception e)
						{
							AlertDialog.Builder ad2 = new AlertDialog.Builder(ProfileActivity.this);
							ad2.setTitle("�������� ������������");
							ad2.setMessage("�� ������� ������� ������������");
							ad2.setPositiveButton("OK", new DialogInterface.OnClickListener()
							{	
								public void onClick(DialogInterface dialog, int which) 
								{ }
							});
							ad2.show();
						}
					}
				});
				ad.setNegativeButton("���", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int arg1) { }
				});
				ad.show();
	    	}
	    };
	    deleteUserButton.setOnClickListener(deleteUserButton_click);
	    
	    try
        {
	    	SessionState.currentUser.refreshFromServer();
	    	nickText.setText(SessionState.currentUser.getNick());
	    	phoneText.setText(SessionState.currentUser.getPhone());
	    	nameText.setText(SessionState.currentUser.getName());
	    	surnameText.setText(SessionState.currentUser.getSurname());
	    	emailText.setText(SessionState.currentUser.getEmail());
        }
        catch (Exception ex)
        {
        	ShowMessageBox(ex.getMessage());
        }
	}
	
	public void ShowMessageBox(String msg)
    {
    	Common.ShowMessageBox(ProfileActivity.this, msg);
    }
}
