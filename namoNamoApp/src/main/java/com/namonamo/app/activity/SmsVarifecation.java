package com.namonamo.app.activity;


import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.namonamo.androidclient.exceptions.YaximXMPPAdressMalformedException;
import com.namonamo.androidclient.service.XMPPService;
import com.namonamo.androidclient.util.PreferenceConstants;
import com.namonamo.androidclient.util.XMPPHelper;
import com.namonamo.app.R;
import com.namonamo.app.apicall.RegistrationApiCall;
import com.namonamo.app.apicall.SendMessageApiCall;
import com.namonamo.app.common.Log;
import com.namonamo.app.constant.NamoNamoSharedPrefrence;
import com.namonamo.app.service.Servicable;
import com.namonamo.app.service.Servicable.ServiceListener;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsMessage;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

public class SmsVarifecation extends BaseActivity
{
	String finalotp="";
	EditText code1,code2,code3,code4;

	int code = 0;
	protected String country_code;
	protected int remainingSec=20,remain=20;
	protected String contact_number,phone1,ccode;
	static int ct=0;
	ArrayList<Integer> dotimage;
	ImageView dotimageview;
	TextView timertext,phonenumber,ok;
	protected ServiceListener listener = new ServiceListener() {
		@Override
		public void onComplete(Servicable<?> service) {
			String responce = ((SendMessageApiCall) service)
					.getStringResponce();
			//Log.e("responcee",responce);
			//Log.e("res","res");
			if (responce == null) {
				dismissProgress();
				showAlert("Some error occur. Please try again.");
			}

		}

	};
	private OnClickListener ok_Click = new OnClickListener() {

		@Override
		public void onClick(View v) {
			contact_number = phone1.trim();

			contact_number = contact_number.replace(" ", "");
			contact_number = contact_number.replaceAll("[\\-\\+\\.\\^:,]", "");
			contact_number = contact_number.replaceAll("[^\\w\\s]", "");
			contact_number = contact_number.trim().replace(" ", "");

			if (contact_number.length() < 1) {
				showAlert("Please Provide contact no");
				return;
			}
			country_code = "+91";
			code = new Random(new Date().getTime()).nextInt();
			code = code % 10000;
			if (code < 0) {
				code = -code;
			}
			SendMessageApiCall api = new SendMessageApiCall(contact_number,
					code+"");
			api.runAsync(listener);
			remainingSec = 60;

			showProgress("Confirmation Code: Wait for 60 Sec");

			timer = new Timer();
			TimerTask task = new TimerTask() {

				@Override
				public void run() {

					Runnable action = new Runnable() {

						@Override
						public void run() {
							if (remainingSec == 0) {
								timer.cancel();
								dismissProgress();
								showAlert("Unable to recive confirmation code, please try again.");
								return;
							}
							setProgressMessage("Confirmation Code: Wait for "
									+ remainingSec + " Sec");
							remainingSec--;
						}
					};
					runOnUiThread(action);
				}
			};
			showProgress("Confirmation Code: Wait for " + remainingSec + " Sec");
			timer.schedule(task, 1000, 1000);
		}
	};
	Timer timer;

	public void sendMessage(String phone1)
	{
		contact_number = phone1.trim();

		contact_number = contact_number.replace(" ", "");
		contact_number = contact_number.replaceAll("[\\-\\+\\.\\^:,]", "");
		contact_number = contact_number.replaceAll("[^\\w\\s]", "");
		contact_number = contact_number.trim().replace(" ", "");

		if (contact_number.length() < 1) {
			showAlert("Please Provide contact no");
			return;
		}
		country_code = "+91";
		code = new Random(new Date().getTime()).nextInt();
		code = code % 10000;
		if (code < 0) {
			code = -code;
		}
		SendMessageApiCall api = new SendMessageApiCall(contact_number,
				 code+"");
		api.runAsync(listener);
		remainingSec =60;

		showProgress("Confirmation Code: Wait for 60 Sec");

		timer = new Timer();
		TimerTask task = new TimerTask() {

			@Override
			public void run() {

				Runnable action = new Runnable() {

					@Override
					public void run() {
						if (remainingSec == 0) {
							timer.cancel();
							dismissProgress();
							showAlert("Unable to recive confirmation code, please try again.");
							return;
						}
						setProgressMessage("Confirmation Code: Wait for "
								+ remainingSec + " Sec");
						remainingSec--;
					}
				};
				runOnUiThread(action);
			}
		};
//		showProgress("Confirmation Code: Wait for " + remainingSec + " Sec");
		timer.schedule(task, 1000, 1000);




	}



	private void verifyAndSavePreferences(String jabberID, String password) {
		try {
			jabberID = XMPPHelper.verifyJabberID(jabberID);
		} catch (YaximXMPPAdressMalformedException e) {
			e.printStackTrace();
		}
		String resource = String.format("%s.%08X",
				getResources().getString(R.string.app_name),
				new java.util.Random().nextInt());

		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		Editor editor = sharedPreferences.edit();

		editor.putString(PreferenceConstants.JID, jabberID);
		editor.putString(PreferenceConstants.PASSWORD, password);
		editor.putString(PreferenceConstants.RESSOURCE, resource);
		editor.putString(PreferenceConstants.PORT,
				PreferenceConstants.DEFAULT_PORT);
		editor.commit();
	}

	private ServiceListener registrationListener = new ServiceListener() {

		@Override
		public void onComplete(Servicable<?> service) {
			String jabberID = ((RegistrationApiCall) service).getJid();
			String password = ((RegistrationApiCall) service).getPassword();
			verifyAndSavePreferences(jabberID, password);

			dismissProgress();
			if (NamoNamoSharedPrefrence.isRegistered(getApplicationContext())) {

				Intent xmppServiceIntent = new Intent(SmsVarifecation.this,
						XMPPService.class);
				xmppServiceIntent.putExtra("create_account", true);
				xmppServiceIntent
						.setAction("com.namonamo.androidclient.XMPPSERVICE");
				startService(xmppServiceIntent);

				NamoNamoSharedPrefrence.saveMobileNo(getApplicationContext(),
						country_code + contact_number);
				NamoNamoSharedPrefrence.saveCountryCode(getApplicationContext(), country_code);
				finish();

			}

		}
	};

	protected void doRegistration() {
		RegistrationApiCall apiCall = new RegistrationApiCall(
				getApplicationContext(), country_code + contact_number,
				country_code.replace("+", "") + contact_number);
		apiCall.runAsync(registrationListener);
		showProgress("Please Wait while registration...");

		Intent intent = new Intent(SmsVarifecation.this,
				MyProfile.class);
		Bundle bnd = new Bundle();
		bnd.putString("CONTACT_NO", country_code + contact_number);
		intent.putExtras(bnd);
		startActivity(intent);
//		dialog1();
	}

	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {


			dismissProgress();
			// Retrieves a map of extended data from the intent.
			final Bundle bundle = intent.getExtras();
			try {
				if (bundle != null) {
					final Object[] pdusObj = (Object[]) bundle.get("pdus");
					for (int i = 0; i < pdusObj.length; i++) {
						SmsMessage currentMessage = SmsMessage
								.createFromPdu((byte[]) pdusObj[i]);
						String phoneNumber = currentMessage
								.getDisplayOriginatingAddress();

						String senderNum = phoneNumber;
						String message = currentMessage.getDisplayMessageBody();
						message=message.replace(".","");
						Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
						message = message.replaceAll("[^\\d.]", "");
						if (message.equalsIgnoreCase(code + "")) {

							Log.e("if","if");
							timer.cancel();
							dismissProgress();
							doRegistration();
						}
					} // end for loop
				} // bundle is null

			} catch (Exception e) {
				Log.e("SmsReceiver", "Exception smsReceiver" + e);

			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.smstimer);
		Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		phonenumber=(TextView)findViewById(R.id.phonnumber);

//		ok=(TextView)findViewById(R.id.textView21);

		code1=(EditText)findViewById(R.id.editText1);
		code2=(EditText)findViewById(R.id.editText2);
		code3=(EditText)findViewById(R.id.editText3);
		code4=(EditText)findViewById(R.id.editText4);

		 Intent intent = getIntent();         
		 phone1 = intent.getStringExtra("phone1");
		 ccode=intent.getStringExtra("ccode");

		Log.e("contact number", phone1);
		Log.e("country code", ccode);

		phonenumber.setText(ccode+" "+phone1);

		sendMessage(phone1);
		
		 registerForSMS();
		 
		 
		 
		 
//		 ok.setOnClickListener(new OnClickListener() {
//			 @Override
//			 public void onClick(View v) {
//				 if(code1.getText().toString().length()>0)
//				 {
//					 finalotp=finalotp+code1.getText();
//				 }
//				 if(code2.getText().toString().length()>0)
//				 {
//					 finalotp=finalotp+code2.getText();
//				 }
//				 if(code3.getText().toString().length()>0)
//				 {
//					 finalotp=finalotp+code3.getText();
//				 }
//				 if(code4.getText().toString().length()>0)
//				 {
//					 finalotp=finalotp+code4.getText();
//				 }
//
//				 String verifycode=String.valueOf(code);
//				 if(finalotp.equals(verifycode))
//				 {
//					 Intent in=new Intent(SmsVarifecation.this,MyProfile.class);
//					 startActivity(in);
//				 }
//				 else
//				 {
//					 Toast.makeText(SmsVarifecation.this,finalotp,Toast.LENGTH_LONG).show();
//					 Log.e("code=",code+"");
//				 }
//			 }
//		 });
		 
		 
	}

	protected void onDestroy() {
		super.onDestroy();
		try {
			unregisterReceiver(receiver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void registerForSMS() {
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.provider.Telephony.SMS_RECEIVED");
		registerReceiver(receiver, filter);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.sms_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		switch (menuItem.getItemId()) {
			case android.R.id.home:
				Intent in=new Intent(SmsVarifecation.this,VerificationPhoneNo.class);
				startActivity(in);
				break;
			case R.id.home:
				Intent in1=new Intent(SmsVarifecation.this,VerificationPhoneNo.class);
				startActivity(in1);
				break;
		}

		return (super.onOptionsItemSelected(menuItem));
	}
}
