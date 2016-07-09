package com.hihello.app.activity;

import com.hihello.app.R;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.hihello.androidclient.exceptions.YaximXMPPAdressMalformedException;
import com.hihello.androidclient.service.XMPPService;
import com.hihello.androidclient.util.PreferenceConstants;
import com.hihello.androidclient.util.XMPPHelper;
import com.hihello.app.apicall.RegistrationApiCall;
import com.hihello.app.constant.HiHelloSharedPrefrence;
import com.hihello.app.service.Servicable;
import com.hihello.app.service.Servicable.ServiceListener;

public class RegistrationActivity extends BaseActivity {
	protected String profile_pic_url = "";
	protected String contact_no;
	private OnClickListener submitClick = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			doRegistration();
		}
	};

	private ImageView imageView;
	private EditText txt_last;
	private EditText txt_first;
	private Uri mProfilePath;

	private void verifyAndSavePreferences(String jabberID, String password) {
		try {
			jabberID = XMPPHelper.verifyJabberID(jabberID);
		} catch (YaximXMPPAdressMalformedException e) {
			e.printStackTrace();
		}
		String resource = String.format("%s.%08X",
				"namonamo",
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
		HiHelloSharedPrefrence.setUserJID(getApplicationContext(), jabberID);
		HiHelloSharedPrefrence.setUserPassword(getApplicationContext(), password);
		
	}

	private ServiceListener registrationListener = new ServiceListener() {

		@Override
		public void onComplete(Servicable<?> service) {
			String jabberID = ((RegistrationApiCall) service).getJid();
			String password = ((RegistrationApiCall) service).getPassword();
			verifyAndSavePreferences(jabberID, password);

			dismissProgress();
			if (HiHelloSharedPrefrence.isRegistered(getApplicationContext())) {

				Intent xmppServiceIntent = new Intent(
						RegistrationActivity.this, XMPPService.class);
				xmppServiceIntent.putExtra("create_account", true);
				xmppServiceIntent
						.setAction("com.namonamo.androidclient.XMPPSERVICE");
				startService(xmppServiceIntent);

				HiHelloSharedPrefrence.saveMobileNo(getApplicationContext(),
						contact_no);
				finish();
				Intent intent = new Intent(RegistrationActivity.this,
						MyProfile.class);
				Bundle bnd = new Bundle();
				bnd.putString("CONTACT_NO", "+" + contact_no);
				intent.putExtras(bnd);
				startActivity(intent);
			}

		}
	};
	protected OnDateSetListener callBack = new OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			et_dob.setText(dayOfMonth + "/" + monthOfYear + "/" + year);
		}
	};
	private OnClickListener dobClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			DatePickerDialog picker = new DatePickerDialog(
					RegistrationActivity.this, callBack, 2014, 02, 15);
			picker.show();
		}
	};
	private EditText et_dob;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		setContentView(R.layout.registration);
		super.onCreate(savedInstanceState);
		contact_no = getIntent().getExtras().getString("CONTACT_NO");
		txt_first = (EditText) findViewById(R.id.txt_first_name);
		txt_last = (EditText) findViewById(R.id.txt_last_name);
		imageView = (ImageView) findViewById(R.id.imageView);
		((Button) findViewById(R.id.btn_submit))
				.setOnClickListener(submitClick);
		if (savedInstanceState != null) {
			if (savedInstanceState.containsKey(PHOTO_PATH)) {
				mProfilePath = Uri.parse(savedInstanceState
						.getString(PHOTO_PATH));
				displayImage(mProfilePath.toString(), imageView);
			}
		}
		et_dob = (EditText) findViewById(R.id.et_dob);
		et_dob.setOnClickListener(dobClick);
	}

	public void showMainScreen() {
		// TODO Auto-generated method stub

	}

	protected void doRegistration() {
		RegistrationApiCall apiCall = new RegistrationApiCall(
				getApplicationContext(), contact_no,
				contact_no.replace("+", ""));
		apiCall.runAsync(registrationListener);
		showProgress("Please Wait while registration...");
	}
}
