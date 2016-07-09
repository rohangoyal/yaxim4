package com.namonamo.app.activity;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;


import com.namonamo.app.R;
import com.namonamo.app.adapter.PagerAdapter;
import com.namonamo.app.adapter.RankingPagerAdapter;

public class Ranking extends BaseActivity {
	RankingPagerAdapter viewpageradapter;
	SlidingTabLayout tabs;
	CharSequence Titles[] = {"Contact", "All","Daily"};
	int tabicon[]={R.drawable.group,R.drawable.chat,R.drawable.chat};
	int Numboftabs = 3;
	private ViewPager mPager;
//	private OnClickListener allContactClick = new OnClickListener() {
//
//		@Override
//		public void onClick(View v) {
//			mPager.setCurrentItem(1);
//
//		}
//	};
//	private OnClickListener myContactClick = new OnClickListener() {
//
//		@Override
//		public void onClick(View v) {
//			mPager.setCurrentItem(0);
//		}
//	};
//	private ActionBar.TabListener tabListener = new ActionBar.TabListener() {
//
//
//		@Override
//		public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
//			mPager.setCurrentItem(tab.getPosition());
//		}
//
//		@Override
//		public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
//
//		}
//
//		@Override
//		public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
//
//		}
//
//	};

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.ranking);
		Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeAsUpIndicator(R.drawable.leftback);
		getSupportActionBar().setTitle("Ranking");

		mPager = (ViewPager) findViewById(R.id.pager);
		FragmentManager fm = getSupportFragmentManager();
		viewpageradapter= new RankingPagerAdapter(getApplicationContext(), getSupportFragmentManager(),Titles,tabicon,Numboftabs);
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

//	ViewPager.SimpleOnPageChangeListener viewPagerListener = new ViewPager.SimpleOnPageChangeListener() {
//		@Override
//		public void onPageSelected(int position) {
//			super.onPageSelected(position);
//			actionBar.setSelectedNavigationItem(position);
//		}
//	};

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		switch (menuItem.getItemId()) {
		case android.R.id.home:
			finish();
			break;
//			case R.id.post:
//				Intent in=new Intent(Ranking.this,HomeMain_activity.class);
//				startActivity(in);
		}
		return (super.onOptionsItemSelected(menuItem));
	}




	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.sms_menu, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		Intent i=new Intent(Ranking.this,HomeMain_activity.class);
		startActivity(i);
		finish();
	}
}