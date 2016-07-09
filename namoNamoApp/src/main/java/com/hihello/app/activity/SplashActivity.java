package com.hihello.app.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;



import com.hihello.androidclient.service.XMPPService;
import com.hihello.app.R;
import com.hihello.app.apicall.GetCMSApiCall;
import com.hihello.app.apicall.GetDailyMessageApiCall;
import com.hihello.app.constant.HiHelloSharedPrefrence;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends Activity {

	int  REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS=124;
	private Intent xmppServiceIntent;
	private int REQUEST_CODE_ASK_PERMISSIONS = 123;
	void checkForUpdate() {
		int versionCode = HiHelloSharedPrefrence
				.getVersionCode(getApplicationContext());
		boolean forceUpgrade = HiHelloSharedPrefrence
				.isForceUpgrade(getApplicationContext());
		int myVersionCode = 0;
		try {
			myVersionCode = getPackageManager().getPackageInfo(
					getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		if (forceUpgrade && versionCode > myVersionCode) {
			showAlert("There is a new version of "+getString(R.string.hihello)+" Application available in google play.");
		} else {
			startWorking();
		}
	}

	public void showAlert(String message) {
		AlertDialog alert = new AlertDialog.Builder(SplashActivity.this)
				.setTitle(getString(R.string.hihello)).setMessage(message).setCancelable(false)
				.create();
		alert.setButton("Go to google play store",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						String rateUsUrl = "https://play.google.com/store/apps/details?id=com.hihello.app";
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
		if (Build.VERSION.SDK_INT >= 23) {
			//do your check here
			getDummyContactWrapper();
		}
	}

	void startWorking() {
		xmppServiceIntent = new Intent(this, XMPPService.class);
		xmppServiceIntent.setAction("com.namonamo.androidclient.XMPPSERVICE");
		if (HiHelloSharedPrefrence.isRegistered(getApplicationContext())) {
			startService(xmppServiceIntent);
		}
		if (HiHelloSharedPrefrence.isRegistered(getApplicationContext())) {
			GetDailyMessageApiCall api = new GetDailyMessageApiCall(
					HiHelloSharedPrefrence
							.getDailyMessageLastTime(getApplicationContext()));
			api.runAsync(null);
			finish();
			openHiHelloRecentChat();
		} else {
			new AsyncTask<Void, Void, Void>() {

				@Override
				protected Void doInBackground(Void... params) {
					try {
						Thread.sleep(10000);
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

	public void openHiHelloRecentChat() {

		Intent intent = new Intent(this, HomeMain_activity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(intent);
	}

	public void openRegistrationForm() {
		Intent intent = new Intent(this, VerificationPhoneNo.class);
		startActivity(intent);
	}

	private void getDummyContactWrapper() {
		List<String> permissionsNeeded = new ArrayList<String>();

		final List<String> permissionsList = new ArrayList<String>();
		if (!addPermission(permissionsList,android.Manifest.permission.RECORD_AUDIO))
			permissionsNeeded.add("RECORD_AUDIO");
        if (!addPermission(permissionsList, android.Manifest.permission.READ_CONTACTS))
            permissionsNeeded.add("Read Contacts");
		if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
			permissionsNeeded.add("WRITE_EXTERNAL_STORAGE");
		if (!addPermission(permissionsList, Manifest.permission.INTERNET))
			permissionsNeeded.add("INTERNET");
        if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
            permissionsNeeded.add("Read External Storage");
        if (!addPermission(permissionsList, Manifest.permission.CALL_PHONE))
            permissionsNeeded.add("Call Phone");
		if (!addPermission(permissionsList, Manifest.permission.CAMERA))
			permissionsNeeded.add("CAMERA");
		if (!addPermission(permissionsList, Manifest.permission.WRITE_CONTACTS))
			permissionsNeeded.add("WRITE_CONTACTS");
		if (!addPermission(permissionsList, Manifest.permission.READ_SMS))
			permissionsNeeded.add("READ_SMS");
		if (!addPermission(permissionsList, Manifest.permission.RECEIVE_SMS))
			permissionsNeeded.add("RECEIVE_SMS");
		if (!addPermission(permissionsList, Manifest.permission.ACCESS_COARSE_LOCATION))
			permissionsNeeded.add("ACCESS_COARSE_LOCATION");
		if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
			permissionsNeeded.add("ACCESS_FINE_LOCATION");


		if (permissionsList.size() > 0) {
			if (permissionsNeeded.size() > 0) {
				// Need Rationale
				String message = "You need to grant access to " + permissionsNeeded.get(0);
				for (int i = 1; i < permissionsNeeded.size(); i++)
					message = message + ", " + permissionsNeeded.get(i);
				showMessageOKCancel(message,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
										REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
							}
						});
				return;
			}
			requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
					REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
			return;
		}
	}

	private boolean addPermission(List<String> permissionsList, String permission) {
		if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
			permissionsList.add(permission);
			// Check for Rationale Option
			if (!shouldShowRequestPermissionRationale(permission))
				return false;
		}
		return true;
	}
	private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
		new AlertDialog.Builder(SplashActivity.this)
				.setMessage(message)
				.setPositiveButton("OK", okListener)
				.setNegativeButton("Cancel", null)
				.create()
				.show();
	}







//	private void getDummyContactWrapper() {
//		int hasCameraPermission = checkSelfPermission(Manifest.permission.CAMERA);
//		if (hasCameraPermission != PackageManager.PERMISSION_GRANTED) {
//			requestPermissions(new String[] {Manifest.permission.CAMERA},
//					REQUEST_CODE_ASK_PERMISSIONS);
//			return;
//		}
//		int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.WRITE_CONTACTS);
//		if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
//			requestPermissions(new String[] {Manifest.permission.WRITE_CONTACTS},
//					REQUEST_CODE_ASK_PERMISSIONS);
//			return;
//		}
//		int hasReadContactsPermission = checkSelfPermission(Manifest.permission.READ_CONTACTS);
//		if (hasReadContactsPermission != PackageManager.PERMISSION_GRANTED) {
//			requestPermissions(new String[] {Manifest.permission.READ_CONTACTS},
//					REQUEST_CODE_ASK_PERMISSIONS);
//			return;
//		}
//		int hasReadSmsPermission = checkSelfPermission(Manifest.permission.READ_SMS);
//		if (hasReadSmsPermission != PackageManager.PERMISSION_GRANTED) {
//			requestPermissions(new String[] {Manifest.permission.READ_SMS},
//					REQUEST_CODE_ASK_PERMISSIONS);
//			return;
//		}
//		int hasReciveSmsPermission = checkSelfPermission(Manifest.permission.RECEIVE_SMS);
//		if (hasReciveSmsPermission != PackageManager.PERMISSION_GRANTED) {
//			requestPermissions(new String[] {Manifest.permission.RECEIVE_SMS},
//					REQUEST_CODE_ASK_PERMISSIONS);
//			return;
//		}
//
//		int hasReciveLocationPermission = checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
//		if (hasReciveLocationPermission != PackageManager.PERMISSION_GRANTED) {
//			requestPermissions(new String[] {Manifest.permission.ACCESS_COARSE_LOCATION},
//					REQUEST_CODE_ASK_PERMISSIONS);
//			return;
//		}
//		int hasLocationPermission = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
//		if (hasLocationPermission != PackageManager.PERMISSION_GRANTED) {
//			requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
//					REQUEST_CODE_ASK_PERMISSIONS);
//			return;
//		}
//		int hasRecordPermission = checkSelfPermission(Manifest.permission.RECORD_AUDIO);
//		if (hasRecordPermission != PackageManager.PERMISSION_GRANTED) {
//			requestPermissions(new String[] {Manifest.permission.RECORD_AUDIO},
//					REQUEST_CODE_ASK_PERMISSIONS);
//			return;
//		}
//		int hasWriteExternalPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//		if (hasWriteExternalPermission != PackageManager.PERMISSION_GRANTED) {
//			requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
//					REQUEST_CODE_ASK_PERMISSIONS);
//			return;
//		}
//		int hasReadExternalPermission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
//		if (hasReadExternalPermission != PackageManager.PERMISSION_GRANTED) {
//			requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
//					REQUEST_CODE_ASK_PERMISSIONS);
//			return;
//		}
//		int hasCallPhonePermission = checkSelfPermission(Manifest.permission.CALL_PHONE);
//		if (hasCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
//			requestPermissions(new String[] {Manifest.permission.CALL_PHONE},
//					REQUEST_CODE_ASK_PERMISSIONS);
//			return;
//		}
//	}
//	private void getReadSMS() {
//		int hasReadSmsPermission = checkSelfPermission(Manifest.permission.READ_SMS);
//		if (hasReadSmsPermission != PackageManager.PERMISSION_GRANTED) {
//			requestPermissions(new String[] {Manifest.permission.READ_SMS},
//					REQUEST_CODE_ASK_PERMISSIONS);
//			return;
//		}
//
//	}
//	private void getReciveSMS() {
//		int hasReciveSmsPermission = checkSelfPermission(Manifest.permission.RECEIVE_SMS);
//		if (hasReciveSmsPermission != PackageManager.PERMISSION_GRANTED) {
//			requestPermissions(new String[] {Manifest.permission.RECEIVE_SMS},
//					REQUEST_CODE_ASK_PERMISSIONS);
//			return;
//		}
//
//	}
}
