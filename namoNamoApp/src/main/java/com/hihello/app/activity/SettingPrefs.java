package com.hihello.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;


import com.hihello.app.R;
import com.hihello.app.apicall.UpdateProfileApiCall;

public class SettingPrefs extends PreferenceActivity {
	private OnPreferenceChangeListener preferenceChangeListener = new OnPreferenceChangeListener() {

		@Override
		public boolean onPreferenceChange(Preference preference, Object newValue) {

			UpdateProfileApiCall api = new UpdateProfileApiCall(
					getApplicationContext(), "", "", preference.getKey(),
					(Boolean) newValue);
			api.runAsync(null);
			preference.setSummary((Boolean) newValue ? getResources()
					.getString(R.string.everyone) : getResources().getString(
					R.string.nobody));
			return true;
		}
	};

	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.mainprefs);
//		ActionBar actionBar = getActionBar();
//		actionBar.setHomeButtonEnabled(true);
//		actionBar.setDisplayHomeAsUpEnabled(true);
		initializeControls();
	}

	private void initializeControls() {
		getPreferenceManager().findPreference("show_status")
				.setOnPreferenceChangeListener(preferenceChangeListener);
		getPreferenceManager().findPreference("show_last_seen")
				.setOnPreferenceChangeListener(preferenceChangeListener);
		getPreferenceManager().findPreference("show_profile")
				.setOnPreferenceChangeListener(preferenceChangeListener);

	}

	@Override
	public boolean onOptionsItemSelected(
			MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				Intent i = new Intent(SettingPrefs.this, HomeMain_activity.class);
				startActivity(i);
				finish();
				break;
			case R.id.home:
				Intent in = new Intent(SettingPrefs.this, HomeMain_activity.class);
				startActivity(in);
				finish();
				break;
		}
				return super.onOptionsItemSelected(item);

	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		MenuInflater inflater = getMenuInflater();
//		inflater.inflate(R.menu.sms_menu, menu);
//		return true;
//	}

	@Override
	public void onBackPressed() {
		Intent i=new Intent(SettingPrefs.this,HomeMain_activity.class);
		startActivity(i);
		finish();
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		LinearLayout root = (LinearLayout)findViewById(android.R.id.list).getParent().getParent().getParent();
		Toolbar bar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.settings_toolbar, root, false);
		root.addView(bar, 0); // insert at top
		bar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
