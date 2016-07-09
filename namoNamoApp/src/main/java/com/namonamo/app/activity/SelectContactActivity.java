package com.namonamo.app.activity;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


import com.namonamo.androidclient.chat.ChatWindow;
import com.namonamo.app.R;
import com.namonamo.app.adapter.ShareContactAdapter;

public class SelectContactActivity extends BaseActivity {
	ShareContactAdapter viewpageradapter;
	public static final String EXTRA_SHARE_DATA = "EXTRA_SHARE_DATA";
	public static final String EXTRA_SHARE_STEAM = "EXTRA_SHARE_STEAM";
	// Declare Variables
	ActionBar mActionBar;
	ViewPager mPager;
	Tab tab;
	private String dataToShare;
	private String streamToShare;
	SlidingTabLayout tabs;
	CharSequence Titles[] = {"Recent Chat", "Contact"};
	int tabicon[]={R.drawable.group,R.drawable.chat,R.drawable.chat};
	int Numboftabs = 2;

	public void startChatActivity(String user, String userName) {
		Intent chatIntent = new Intent(this,
				com.namonamo.androidclient.chat.ChatWindow.class);
		chatIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		Uri userNameUri = Uri.parse(user);
		chatIntent.setData(userNameUri);
		chatIntent.putExtra(ChatWindow.INTENT_EXTRA_USERNAME, userName);
		if (dataToShare != null) {
			chatIntent.putExtra(ChatWindow.INTENT_EXTRA_SHARE_MESSAGE,
					dataToShare);
		} else if (streamToShare != null) {
			chatIntent.putExtra(ChatWindow.INTENT_EXTRA_SHARE_STREAM,
					streamToShare);
		}
		startActivity(chatIntent);
		finish();
	}

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Get the view from activity_main.xml
		setContentView(R.layout.share_view_pager);
		dataToShare = getIntent().getExtras().getString(EXTRA_SHARE_DATA);
		streamToShare = getIntent().getExtras().getString(EXTRA_SHARE_STEAM);
		Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		mPager = (ViewPager) findViewById(R.id.pager);
		FragmentManager fm = getSupportFragmentManager();
		viewpageradapter= new ShareContactAdapter(getApplicationContext(), getSupportFragmentManager(),Titles,tabicon,Numboftabs);
		mPager.setAdapter(viewpageradapter);
		tabs = (SlidingTabLayout) findViewById(R.id.tabs);
		tabs.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1E90FF")));
		tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

		// Setting Custom Color for the Scroll bar indicator of the Tab View
		tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
			@Override
			public int getIndicatorColor(int position) {
				return getResources().getColor(R.color.tabsScrollColor);
			}
		});

		// Setting the ViewPager For the SlidingTabsLayout
		tabs.setViewPager(mPager);




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




	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.selectcontact_menu, menu);
		return true;
	}
	@Override
	public void onBackPressed() {
		Intent i=new Intent(SelectContactActivity.this,HomeMain_activity.class);
		startActivity(i);
		finish();
	}
}
