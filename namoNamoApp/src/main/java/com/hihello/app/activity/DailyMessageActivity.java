package com.hihello.app.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;


import com.hihello.app.R;
import com.hihello.app.adapter.DailyMessageAdapter;
import com.hihello.app.constant.HiHelloSharedPrefrence;
import com.hihello.app.db.DailyMessage;
import com.hihello.app.db.DailyMessageDBService;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

public class DailyMessageActivity extends ActionBarActivity {

	private ListView listDailyMessage;
	private ArrayList<DailyMessage> allMessage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_daily_message);
		Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//		getSupportActionBar().setHomeAsUpIndicator(R.drawable.leftback);
//		ActionBar actionBar = getActionBar();
//		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setTitle("Hi Hello Notification");
		initializeControls();

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		switch (menuItem.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		}
		return (super.onOptionsItemSelected(menuItem));
	}

	private void initializeControls() {
		listDailyMessage = (ListView) findViewById(R.id.list_daily_message);
		allMessage = DailyMessageDBService
				.getAllMessage(getApplicationContext());
		Collections.sort(allMessage);
		long time = new Date().getTime()/1000;
		for (DailyMessage message : allMessage) {
			if (message.getDate() > time) {
				time = message.getDate();
			}
		}
		DailyMessageAdapter adapter = new DailyMessageAdapter(allMessage,
				getLayoutInflater());

		HiHelloSharedPrefrence.setDailyMessageLastTime(
				getApplicationContext(), time);
		listDailyMessage.setAdapter(adapter);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.sms_menu, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		Intent i=new Intent(DailyMessageActivity.this,HomeMain_activity.class);
		startActivity(i);
		finish();
	}
}
