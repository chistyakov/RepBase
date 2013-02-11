package com.example.repbase.activities;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

import com.example.repbase.Common;
import com.example.repbase.R;


@SuppressWarnings("deprecation")
public class AuthorizedActivity extends TabActivity 
{
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorized);
        
        TabHost tabHost = getTabHost();
        
        
        tabHost.setup();
        
        //Таб "Профиль"
        TabHost.TabSpec spec = tabHost.newTabSpec("tag1");
        spec.setContent(new Intent(AuthorizedActivity.this, ProfileActivity.class));
        spec.setIndicator("Профиль");
        tabHost.addTab(spec);
        
        //Таб "Репетиции"
        spec = tabHost.newTabSpec("tag2");
        spec.setContent(new Intent(AuthorizedActivity.this, RepetitionsActivity.class));
        spec.setIndicator("Репетиции");
        tabHost.addTab(spec);
        
        tabHost.setCurrentTab(0);
	}
	
	public void ShowMessageBox(String msg)
    {
    	Common.ShowMessageBox(AuthorizedActivity.this, msg);
    }
}
