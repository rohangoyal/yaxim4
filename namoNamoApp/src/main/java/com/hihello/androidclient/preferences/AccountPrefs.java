package com.hihello.androidclient.preferences;


import com.hihello.app.R;
import com.hihello.app.activity.SettingPrefs;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;


import com.hihello.androidclient.dialogs.ChangePasswordDialog;
import com.hihello.androidclient.exceptions.YaximXMPPAdressMalformedException;
import com.hihello.androidclient.util.PreferenceConstants;
import com.hihello.androidclient.util.XMPPHelper;

public class AccountPrefs extends PreferenceActivity {

	private SharedPreferences sharedPreference;

	private static int prioIntValue = 0;

	private EditTextPreference prefPrio;
	private EditTextPreference prefAccountID;

	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
//		addPreferencesFromResource(R.layout.accountprefs);

		ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);

		sharedPreference = PreferenceManager.getDefaultSharedPreferences(this);

		this.prefAccountID = (EditTextPreference) findPreference(PreferenceConstants.JID);
		this.prefAccountID.getEditText().addTextChangedListener(
				new TextWatcher() {
					public void afterTextChanged(Editable s) {
						// Nothing
					}

					public void beforeTextChanged(CharSequence s, int start,
							int count, int after) {
						// Nothing
					}

					public void onTextChanged(CharSequence s, int start,
							int before, int count) {

						try {
							XMPPHelper.verifyJabberID(s.toString());
							prefAccountID.getEditText().setError(null);
						} catch (YaximXMPPAdressMalformedException e) {
							prefAccountID.getEditText().setError(getString(R.string.Global_JID_malformed));
						}
					}
				});

		this.prefPrio = (EditTextPreference) findPreference(PreferenceConstants.PRIORITY);
		this.prefPrio
				.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						try {
							int prioIntValue = Integer.parseInt(newValue
									.toString());
							if (prioIntValue <= 127 && prioIntValue >= -128) {
								sharedPreference.edit().putInt(PreferenceConstants.PRIORITY,
										prioIntValue);
							} else {
								sharedPreference.edit().putInt(PreferenceConstants.PRIORITY, 0);
							}
							return true;

						} catch (NumberFormatException ex) {
							sharedPreference.edit().putInt(PreferenceConstants.PRIORITY, 0);
							return true;
						}

					}
				});

		this.prefPrio.getEditText().addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				try {
					prioIntValue = Integer.parseInt(s.toString());
					if (prioIntValue <= 127 && prioIntValue >= -128) {
						prefPrio.getEditText().setError(null);
						prefPrio.setPositiveButtonText(android.R.string.ok);
					} else {
						prefPrio.getEditText().setError(getString(R.string.account_prio_error));
					}
				} catch (NumberFormatException numF) {
					prioIntValue = 0;
					prefPrio.getEditText().setError(getString(R.string.account_prio_error));
				}

			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// Nothing
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

		});

		findPreference(PreferenceConstants.PASSWORD).setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				new ChangePasswordDialog(AccountPrefs.this).show();
				return true;
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent intent = new Intent(this, SettingPrefs.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
