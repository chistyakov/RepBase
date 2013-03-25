package com.example.repbase.activities;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;

import com.example.repbase.Common;
import com.example.repbase.R;


@SuppressWarnings("deprecation")
public class AuthorizedActivity extends TabActivity 
{
	@Override
    public void onCreate(Bundle savedInstanceState) {
		Log.d(Common.TEMP_TAG, this.getClass().getSimpleName() + " onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorized);
        
        TabHost tabHost = getTabHost();
        
        
        tabHost.setup();
        
        
        //Таб "Профиль"
        TabHost.TabSpec spec = tabHost.newTabSpec("tag1");
        spec.setContent(new Intent(AuthorizedActivity.this, ProfileActivity.class));
        spec.setIndicator("Профиль");
        tabHost.addTab(spec);

        //Tab "Groups"
        spec = tabHost.newTabSpec("tag0");
        spec.setContent(new Intent(AuthorizedActivity.this, GroupsActivity.class));
        spec.setIndicator(getString(R.string.groups_tab));
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

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d(Common.TEMP_TAG, this.getClass().getSimpleName() + ": onDestroy");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.d(Common.TEMP_TAG, this.getClass().getSimpleName() + ": onPause");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d(Common.TEMP_TAG, this.getClass().getSimpleName() + ": onResume");
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.d(Common.TEMP_TAG, this.getClass().getSimpleName() + ": onStop");
	}
}
