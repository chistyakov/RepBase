package com.example.repbase.activities;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.example.repbase.Common;
import com.example.repbase.DBInterface;
import com.example.repbase.R;
import com.example.repbase.classes.SessionState;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Button submit = (Button)findViewById(R.id.submit);
        Button register = (Button)findViewById(R.id.register);
        final EditText nicknameBox = (EditText)findViewById(R.id.nicknameBox);
        final EditText passwdBox = (EditText)findViewById(R.id.passwdBox);
        
        //обработчик кнопки Войти
        OnClickListener submit_click = new OnClickListener()
        {
        	public void onClick(View v)
       		{
        		JSONObject auth = new JSONObject();
        		
       			try
     	        {
       				if (!Common.CheckControl(MainActivity.this, nicknameBox, "Введите имя пользователя"))
       					return;
       				if (!Common.CheckControl(MainActivity.this, passwdBox, "Введите пароль"))
       					return;
       				
       				auth = DBInterface.CheckAuth(nicknameBox.getText().toString(), passwdBox.getText().toString());

       				if (auth.getBoolean("CheckAuthorizationResult"))
       				{
       					JSONObject uobj = DBInterface.GetUserByNickname(nicknameBox.getText().toString());
       					SessionState.AuthorizedUser = Integer.toString(uobj.getInt("ID"));
       					Intent intent = new Intent(MainActivity.this, AuthorizedActivity.class);
       					startActivity(intent);
       				}
       				else
       					ShowMessageBox("Неверные данные");
      		    }
     		    catch (Exception e)
    		    {
     		    	try
     		    	{
     		    		String msg = auth.getString("Exception");
     		    		if (msg.equals("There is no such user in database"))
     		    			ShowMessageBox("Неверные данные");
     		    		else
     		    			ShowMessageBox(auth.getString("Exception"));
     		    	}
     		    	catch (Exception ex) {}
      		    }
       		}
        };
      	
        //обработчик кнопки Регистрация
        OnClickListener register_click = new OnClickListener()
        {
        	public void onClick(View v)
        	{
        		Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
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
    
    public void ShowMessageBox(String msg)
    {
    	Common.ShowMessageBox(MainActivity.this, msg);
    }
}
