package com.hihello.app.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;


import com.hihello.app.R;
import com.hihello.app.apicall.UpdateProfileApiCall;
import com.hihello.app.constant.HiHelloSharedPrefrence;
import com.hihello.app.service.Servicable;
import com.hihello.app.service.Servicable.ServiceListener;

public class EditUserName extends BaseActivity {

	private EditText edit_name;
	private ActionBar actionBar;
	ImageView post;
	private ServiceListener listener = new ServiceListener() {

		@Override
		public void onComplete(Servicable<?> service) {
			dismissProgress();
			if (((UpdateProfileApiCall) service).isSuccess()) {
				finish();
			} else
				showAlert("Unable to update name");
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_user_name);
		initializeControls();
		Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//		getSupportActionBar().setHomeAsUpIndicator(R.drawable.leftback);
		getSupportActionBar().setTitle("Edit User Name");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.status_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		switch (menuItem.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		case R.id.post:
			String name = edit_name.getText().toString();
			if (name.trim().length() == 0) {
				showAlert("Name should not be blank.");
				break;
			}
			updateStatus(name);
			break;
		}
		return (super.onOptionsItemSelected(menuItem));
	}

	private void updateStatus(String name) {
		if (name.length() < 1) {
			showAlert("User name should not be empty.");
			return;
		}
		showProgress("Updating Name...");
		UpdateProfileApiCall apiCall = new UpdateProfileApiCall(
				getApplicationContext(), name, "", "", false);
		apiCall.runAsync(listener);

	}

	private void initializeControls() {
		edit_name = (EditText) findViewById(R.id.edit_name);
		edit_name.setText(HiHelloSharedPrefrence
				.getUserName(getApplicationContext()));
	}

	@Override
	public void onBackPressed() {
		Intent i=new Intent(EditUserName.this,HomeMain_activity.class);
		startActivity(i);
		finish();
	}
}
