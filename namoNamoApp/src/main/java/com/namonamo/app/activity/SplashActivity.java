package com.namonamo.app.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;


import com.namonamo.androidclient.service.XMPPService;
import com.namonamo.app.R;
import com.namonamo.app.apicall.GetCMSApiCall;
import com.namonamo.app.apicall.GetDailyMessageApiCall;
import com.namonamo.app.constant.NamoNamoSharedPrefrence;

public class SplashActivity extends Activity {

	private Intent xmppServiceIntent;

	void checkForUpdate() {
		int versionCode = NamoNamoSharedPrefrence
				.getVersionCode(getApplicationContext());
		boolean forceUpgrade = NamoNamoSharedPrefrence
				.isForceUpgrade(getApplicationContext());
		int myVersionCode = 0;
		try {
			myVersionCode = getPackageManager().getPackageInfo(
					getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		if (forceUpgrade && versionCode > myVersionCode) {
			showAlert("There is a new version of NamoNamo Application available in google play.");
		} else {
			startWorking();
		}
	}

	public void showAlert(String message) {
		AlertDialog alert = new AlertDialog.Builder(SplashActivity.this)
				.setTitle("NamoNamo").setMessage(message).setCancelable(false)
				.create();
		alert.setButton("Go to google play store",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						String rateUsUrl = "https://play.google.com/store/apps/details?id=com.namonamo.app";
						Intent intent = new Intent(Intent.ACTION_VIEW, Uri
								.parse(rateUsUrl));
						startActivity(intent);
						finish();

					}
				});
		alert.show();
	}

	private boolean isDestroy;

	protected void onDestroy() {
		isDestroy = true;
		super.onDestroy();

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.splash);
		super.onCreate(savedInstanceState);
		checkForUpdate();
		GetCMSApiCall api = new GetCMSApiCall(this);
		api.runAsync(null);
	}

	void startWorking() {
		xmppServiceIntent = new Intent(this, XMPPService.class);
		xmppServiceIntent.setAction("com.namonamo.androidclient.XMPPSERVICE");
		if (NamoNamoSharedPrefrence.isRegistered(getApplicationContext())) {
			startService(xmppServiceIntent);
		}
		if (NamoNamoSharedPrefrence.isRegistered(getApplicationContext())) {
			GetDailyMessageApiCall api = new GetDailyMessageApiCall(
					NamoNamoSharedPrefrence
							.getDailyMessageLastTime(getApplicationContext()));
			api.runAsync(null);
			finish();
			openNamoNamoRecentChat();
		} else {
			new AsyncTask<Void, Void, Void>() {

				@Override
				protected Void doInBackground(Void... params) {
					try {
						Thread.sleep(1500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					return null;
				}

				protected void onPostExecute(Void result) {
					finish();
					openRegistrationForm();
				};
			}.execute((Void) null);
		}
	}

	@Override
	public void onBackPressed() {
		// super.onBackPressed();
	}

	public void openNamoNamoRecentChat() {

		Intent intent = new Intent(this, HomeMain_activity.class);
		startActivity(intent);
	}

	public void openRegistrationForm() {
		Intent intent = new Intent(this, VerificationPhoneNo.class);
		startActivity(intent);
	}
}
