package com.namonamo.androidclient.dialogs;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.namonamo.androidclient.XMPPRosterServiceAdapter;
import com.namonamo.androidclient.service.IXMPPRosterService;
import com.namonamo.androidclient.service.XMPPService;
import com.namonamo.androidclient.util.ConnectionState;
import com.namonamo.androidclient.util.PreferenceConstants;
import com.namonamo.app.R;

public class ChangePasswordDialog extends AlertDialog implements
		DialogInterface.OnClickListener, TextWatcher {

	private Context mContext;
	private ServiceConnection mXmppServiceConnection;
	private XMPPRosterServiceAdapter mServiceAdapter;

	private CheckBox mChangeOnServer;
	private EditText mOldPassword;
	private EditText mEditPassword;
	private EditText mRepeatPassword;
	private View mPasswordWarning;
	private Button mOkButton;
	
	private ProgressDialog mProgressDialog;

	public ChangePasswordDialog(Context context) {
		super(context);
		mContext = context;

		setTitle(R.string.account_jabberPW_title);

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View group = inflater.inflate(R.layout.password_view, null, false);
		setView(group);

		mChangeOnServer = (CheckBox) group.findViewById(R.id.password_change_on_server);
		mChangeOnServer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
					updateDialog();
				}
			});
		mOldPassword = (EditText) group.findViewById(R.id.password_old);
		mEditPassword = (EditText) group.findViewById(R.id.password_new);
		mRepeatPassword = (EditText) group.findViewById(R.id.password_new_repeat);
		mPasswordWarning = group.findViewById(R.id.password_warning);

		setButton(BUTTON_POSITIVE, context.getString(android.R.string.ok), this);
		setButton(BUTTON_NEGATIVE, context.getString(android.R.string.cancel),
				(DialogInterface.OnClickListener)null);

	}

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		mOkButton = getButton(BUTTON_POSITIVE);

		mOldPassword.addTextChangedListener(this);
		mEditPassword.addTextChangedListener(this);
		mRepeatPassword.addTextChangedListener(this);

		mChangeOnServer.setEnabled(false);
		mOkButton.setEnabled(false);
		
		Intent serviceIntent = new Intent(mContext, XMPPService.class);
		serviceIntent.setAction("com.namonamo.androidclient.XMPPSERVICE");

		mXmppServiceConnection = new ServiceConnection() {

			public void onServiceConnected(ComponentName name, IBinder service) {
				mServiceAdapter = new XMPPRosterServiceAdapter(
						IXMPPRosterService.Stub.asInterface(service));
				//if (mServiceAdapter.getConnectionState() == ConnectionState.ONLINE)
					mChangeOnServer.setChecked(true);
				updateDialog();
			}

			public void onServiceDisconnected(ComponentName name) {
			}
		};
		mContext.bindService(serviceIntent, mXmppServiceConnection, 0);
	}

	@Override
	public void dismiss() {
		mContext.unbindService(mXmppServiceConnection);
		super.dismiss();
	}

	public void onClick(DialogInterface dialog, int which) {
		String password = mEditPassword.getText().toString();
		if (mChangeOnServer.isChecked()) {
			mProgressDialog = ProgressDialog.show(mContext,
					mContext.getString(R.string.account_jabberPW_title),
					mContext.getString(R.string.account_jabberPW_progress), true);
			new PasswordChangeTask().execute(password);
		} else
			updateLocalPassword(password);
	}

	private void updateDialog() {
		boolean is_logged_in = (mServiceAdapter != null && mServiceAdapter.getConnectionState() == ConnectionState.ONLINE);

		// set checkbox / warning
		mChangeOnServer.setEnabled(true);
		if (!is_logged_in)
			mChangeOnServer.setChecked(false);
		mChangeOnServer.setEnabled(is_logged_in);

		mPasswordWarning.setVisibility(mChangeOnServer.isChecked() ? View.GONE : View.VISIBLE);

		boolean is_ok = true;
		// TODO: check old password length and match
		if (mEditPassword.length() == 0)
			is_ok = false;

		boolean passwords_match = mEditPassword.getText().toString().equals(
				mRepeatPassword.getText().toString());
		is_ok = is_ok && passwords_match;
		mRepeatPassword.setError((passwords_match || mRepeatPassword.length() == 0) ?
				null : mContext.getString(R.string.StartupDialog_error_password));

		mOkButton.setEnabled(is_ok);
	}

	public void afterTextChanged(Editable s) {
		updateDialog();
	}

	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {}
	public void onTextChanged(CharSequence s, int start, int before, int count) {}

	private void updateLocalPassword(String password) {
		PreferenceManager.getDefaultSharedPreferences(mContext).edit()
				.putString(PreferenceConstants.PASSWORD, password).commit();
		//YaximApplication.getConfig(mMainWindow).reconnect_required = false;

	}

	private class PasswordChangeTask extends AsyncTask<String, Void, String> {
		String mPassword;
		
		@Override
		protected String doInBackground(String... params) {
			mPassword = params[0];
			// blocking method on the server
			return mServiceAdapter.changePassword(mPassword);
		}

		@Override
		protected void onPostExecute(String result) {
			mProgressDialog.dismiss();
			int icon_id = android.R.drawable.ic_dialog_alert;
			if ("OK".equals(result)) {
				updateLocalPassword(mPassword);
				result = mContext.getString(R.string.account_jabberPW_finished);
				icon_id = android.R.drawable.ic_dialog_info;
			} else {
				result = mContext.getString(R.string.account_jabberPW_error, result);
			}
			new AlertDialog.Builder(mContext)
				.setTitle(R.string.account_jabberPW_title)
				.setIcon(icon_id)
				.setMessage(result)
				.setPositiveButton(mContext.getString(android.R.string.ok), null)
				.create().show();
		}
		
	}
}
