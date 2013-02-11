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
import com.example.repbase.classes.Attribute;
import com.example.repbase.classes.SessionState;

public class ChangeUserInfoActivity extends Activity
{
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changeuserinfo);
        
        Button decline = (Button)findViewById(R.id.declineUserChangesButton);
        Button save = (Button)findViewById(R.id.saveUserChangesButton);
        
        final EditText nickBox = (EditText)findViewById(R.id.nickBox_ch);
        final EditText nameBox = (EditText)findViewById(R.id.nameBox_ch);
        final EditText surnameBox = (EditText)findViewById(R.id.surnameBox_ch);
        final EditText phoneBox = (EditText)findViewById(R.id.phoneBox_ch);
        final EditText emailBox = (EditText)findViewById(R.id.emailBox_ch);
        final EditText oldPassBox = (EditText)findViewById(R.id.oldPassword_ch);
        final EditText newPass1Box = (EditText)findViewById(R.id.newPassword1_ch);
        final EditText newPass2Box = (EditText)findViewById(R.id.newPassword2_ch);
        
        Reset();
        
        OnClickListener decline_click = new OnClickListener()
        {
			public void onClick(View v)
			{
				Intent intent = new Intent(ChangeUserInfoActivity.this, AuthorizedActivity.class);
				startActivity(intent);
			}
		};
		decline.setOnClickListener(decline_click);
		
		OnClickListener save_click = new OnClickListener()
		{
			public void onClick(View v)
			{
				//�������� �� ������������� �����
				if (!Common.CheckControl(ChangeUserInfoActivity.this, nickBox, "������� ���"))
					return;
				if (!Common.CheckControl(ChangeUserInfoActivity.this, nameBox, "������� ���"))
					return;
				if (!Common.CheckControl(ChangeUserInfoActivity.this, surnameBox, "������� �������"))
					return;
				if (!Common.CheckControl(ChangeUserInfoActivity.this, phoneBox, "������� ����� ��������"))
					return;
				if (!Common.CheckControl(ChangeUserInfoActivity.this, emailBox, "������� e-mail"))
					return;
				if (!newPass1Box.getText().toString().equals("") || !newPass2Box.getText().toString().equals(""))
				{
					if (!Common.CheckControl(ChangeUserInfoActivity.this, emailBox, "��� ��������� ������ ���������� ������� ������ ������"))
						return;
					if (!newPass1Box.getText().toString().equals(newPass2Box.getText()))
					{
						ShowMessageBox("��������� ������ �� ���������");
						return;
					}
				}
				
				try
				{
					JSONObject user = DBInterface.GetUserByID(SessionState.AuthorizedUser);
					//��������� ������
					if (!(nickBox.getText().toString().equals(user.getString("Nick"))))
					{
						JSONObject res = DBInterface.ChangeUserNick(SessionState.AuthorizedUser, nickBox.getText().toString());
						if (!Check(res)) return;
					}
					for (Attribute atr: Common.GetAttributesList(user))
					{
						if (atr.Type.equals("Name"))
						{
							if (!nameBox.getText().toString().equals(atr.Value))
							{
								JSONObject res = DBInterface.ChangeUserName(SessionState.AuthorizedUser, nameBox.getText().toString());
								if (!Check(res)) return;
							} 
						}
						else if (atr.Type.equals("Surname"))
						{
							if (!surnameBox.getText().toString().equals(atr.Value))
							{
								JSONObject res = DBInterface.ChangeUserSurname(SessionState.AuthorizedUser, surnameBox.getText().toString());
								if (!Check(res)) return;
							} 
						}
					}
					if (!(phoneBox.getText().toString().equals(user.getString("Phone"))))
					{
						JSONObject res = DBInterface.ChangeUserPhone(SessionState.AuthorizedUser, phoneBox.getText().toString());
						if (!Check(res)) return;
					}
					for (Attribute atr: Common.GetAttributesList(user))
					{
						if (atr.Type.equals("E-mail"))
						{
							if (!emailBox.getText().toString().equals(atr.Value))
							{
								JSONObject res = DBInterface.ChangeUserEmail(SessionState.AuthorizedUser, emailBox.getText().toString());
								if (!Check(res)) return;
								break;
							} 
						}
					}
					if (!newPass1Box.getText().toString().equals(""))
					{
						if (DBInterface.CheckAuth(DBInterface.GetUserByID(SessionState.AuthorizedUser).getString("Nick"), oldPassBox.getText().toString()).getBoolean("CheckAuthorizationResult"))
						{
							JSONObject res = DBInterface.ChangeUserPassword(SessionState.AuthorizedUser, newPass1Box.getText().toString());
							if (!Check(res)) return;
						}
						else return;
					}
					
					AlertDialog.Builder ad = new AlertDialog.Builder(ChangeUserInfoActivity.this);
					ad.setTitle("��������� ����������");
					ad.setMessage("���������� ������� ��������");
					ad.setPositiveButton("OK", new DialogInterface.OnClickListener()
					{	
						public void onClick(DialogInterface dialog, int which)
						{
							Intent intent = new Intent(ChangeUserInfoActivity.this, AuthorizedActivity.class);
							startActivity(intent);
						}
					});
					ad.show();
				}
				catch (Exception e)
				{
					ShowMessageBox("Exception occured: " + e.getMessage());
				}
			}
		};
		save.setOnClickListener(save_click);
	}
	
	private boolean Check(JSONObject res)
	{
		try 
		{
			if (res == null)
				return true;
			String exception = res.getString("Exception");
				
			if (exception.equals("Invalid argument. Invalid \"Nickname\" argument - length"))
			{
				ShowMessageBox("����� ���� �� 4 �� 15 ��������");
				return false;
			}
			else if (exception.equals("Invalid argument. Invalid \"Nickname\" argument - already exists"))
			{
				ShowMessageBox("������������ � ����� ����� ��� ���������������");
				return false;
			}
			else if (exception.equals("Invalid argument. Invalid \"Password\" argument - length"))
			{
				ShowMessageBox("����� ������ �� 4 �� 25 ��������");
				return false;
			}
			else if (exception.equals("Invalid argument. Invalid \"Name\" argument - length"))
			{
				ShowMessageBox("����� ����� �� 20 ��������");
				return false;
			}
			else if (exception.equals("Invalid argument. Invalid \"Surname\" argument - length"))
			{
				ShowMessageBox("����� ������� �� 20 ��������");
				return false;
			}
			else if (exception.equals("Invalid phone number format"))
			{
				ShowMessageBox("�������� ����� ������ �������� (���������� 11 ����)");
				return false;
			}
			else if (exception.equals("Invalid argument. Invalid \"Phone\" argument - length"))
			{
				ShowMessageBox("�������� ����� ������ �������� (���������� 11 ����)");
				return false;
			}
			else if (exception.equals("Invalid argument. Invalid \"Phone\" argument - already exists"))
			{
				ShowMessageBox("������������ � ����� ������� ���. ��� ���������������");
				return false;
			}
			else if (exception.equals("Invalid argument. Invalid \"E-mail\" argument - already exists"))
			{
				ShowMessageBox("������������ � ����� e-mail ��� ���������������");
				return false;
			}
			else
				return true;
		}
		catch (Exception e) { ShowMessageBox("Someshit"); return false; }
	}
	
	private void Reset()
	{
		final EditText nickBox = (EditText)findViewById(R.id.nickBox_ch);
        final EditText nameBox = (EditText)findViewById(R.id.nameBox_ch);
        final EditText surnameBox = (EditText)findViewById(R.id.surnameBox_ch);
        final EditText phoneBox = (EditText)findViewById(R.id.phoneBox_ch);
        final EditText emailBox = (EditText)findViewById(R.id.emailBox_ch);
        final EditText oldPassBox = (EditText)findViewById(R.id.oldPassword_ch);
        final EditText newPass1Box = (EditText)findViewById(R.id.newPassword1_ch);
        final EditText newPass2Box = (EditText)findViewById(R.id.newPassword2_ch);
		
        oldPassBox.setText("");
        newPass1Box.setText("");
        newPass2Box.setText("");
        
        try
        {
        	JSONObject user = DBInterface.GetUserByID(SessionState.AuthorizedUser);
        	nickBox.setText(user.getString("Nick"));
        	phoneBox.setText(DBInterface.DeEncryptPhone(SessionState.AuthorizedUser));
        	for (Attribute atr: Common.GetAttributesList(user))
        	{
        		if (atr.Type.equals("Name"))
        			nameBox.setText(atr.Value);
        		else if (atr.Type.equals("Surname"))
        			surnameBox.setText(atr.Value);
        		else if (atr.Type.equals("E-mail"))
        			emailBox.setText(atr.Value);
        	}
        }
        catch (Exception e)
        {
        	ShowMessageBox("Exception occured: " + e.getMessage());
        }
	}
	
	public void ShowMessageBox(String msg)
    {
    	Common.ShowMessageBox(ChangeUserInfoActivity.this, msg);
    }
}
