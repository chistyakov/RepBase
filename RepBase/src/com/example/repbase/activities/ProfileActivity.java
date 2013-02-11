package com.example.repbase.activities;

import org.json.JSONObject;

import com.example.repbase.Common;
import com.example.repbase.DBInterface;
import com.example.repbase.R;
import com.example.repbase.classes.Attribute;
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
	    
	    //обработчик кнопки Выйти
	    OnClickListener logoutButton_click = new OnClickListener()
	    {
	    	public void onClick(View v)
	    	{
	    		SessionState.AuthorizedUser = "";
	    		Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
	    		startActivity(intent);
	    	}
	    };
	    logoutButton.setOnClickListener(logoutButton_click);
	    
	    //обработчик кнопки Изменить информацию
	    OnClickListener changeUserInfoButton_click = new OnClickListener()
	    {
	    	public void onClick(View v)
	    	{
	    		Intent intent = new Intent(ProfileActivity.this, ChangeUserInfoActivity.class);
	    		startActivity(intent);
	    	}
	    };
	    changeUserInfoButton.setOnClickListener(changeUserInfoButton_click);
	    
	    //обработчик кнопки Удалить пользователя
	    OnClickListener deleteUserButton_click = new OnClickListener()
	    {
	    	public void onClick(View v)
	    	{
	    		AlertDialog.Builder ad = new AlertDialog.Builder(ProfileActivity.this);
				ad.setTitle("Удаление пользователя");
				ad.setMessage("Вы уверены что хотите удалить этого пользователя?");
				ad.setPositiveButton("Да", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int arg1)
					{
						try
						{
							if (DBInterface.DeleteUser(SessionState.AuthorizedUser))
							{
								AlertDialog.Builder ad2 = new AlertDialog.Builder(ProfileActivity.this);
								ad2.setTitle("Удаление пользователя");
								ad2.setMessage("Пользователь успешно удален");
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
							else
							{
								AlertDialog.Builder ad2 = new AlertDialog.Builder(ProfileActivity.this);
								ad2.setTitle("Удаление пользователя");
								ad2.setMessage("Не удалось удалить пользователя");
								ad2.setPositiveButton("OK", new DialogInterface.OnClickListener()
								{	
									public void onClick(DialogInterface dialog, int which) 
									{ }
								});
								ad2.show();
							}
						}
						catch(Exception e)
						{
							AlertDialog.Builder ad2 = new AlertDialog.Builder(ProfileActivity.this);
							ad2.setTitle("Удаление пользователя");
							ad2.setMessage("Не удалось удалить пользователя");
							ad2.setPositiveButton("OK", new DialogInterface.OnClickListener()
							{	
								public void onClick(DialogInterface dialog, int which) 
								{ }
							});
							ad2.show();
						}
					}
				});
				ad.setNegativeButton("Нет", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int arg1) { }
				});
				ad.show();
	    	}
	    };
	    deleteUserButton.setOnClickListener(deleteUserButton_click);
	    
	    try
        {
        	JSONObject user = DBInterface.GetUserByID(SessionState.AuthorizedUser);
        	nickText.setText(user.getString("Nick"));
    		phoneText.setText(DBInterface.DeEncryptPhone(user.getString("ID")));
        	for (Attribute a: Common.GetAttributesList(user))
        	{
        		if (a.Type.equals("Name"))
        			nameText.setText(a.Value);
        		else if (a.Type.equals("Surname"))
        			surnameText.setText(a.Value);
        		else if (a.Type.equals("E-mail"))
        			emailText.setText(a.Value);
        	}
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
