package com.namonamo.app.apicall;

import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.namonamo.app.constant.NamoNamoConstant;
import com.namonamo.app.constant.NamoNamoSharedPrefrence;
import com.namonamo.app.service.DataProcessor.DataPolicy;
import com.namonamo.app.service.NullDataHook;
import com.namonamo.app.service.Request.RequestType;
import com.namonamo.app.service.Servicable;

public class RegistrationApiCall extends Servicable<NullDataHook> {

	private Context mcontext;
	private int code;
	private String jid;
	private String password;
	private String image;
	private String status;

	public RegistrationApiCall(Context mcontext, String mobile_no,
			String user_jid) {
		this.mcontext = mcontext;
		setUrl(NamoNamoConstant.RegistrationUrl);
		setRequestType(RequestType.POST);
		setDataPolicy(DataPolicy.LiveOnly);
		addParams("mobile_no", mobile_no.replace("+", ""));
		addParams("user_jid", user_jid);
	}

	@Override
	public void onPostRun() {
		super.onPostRun();
		try {
			String str = getResponce().getData();

			Log.e("fff", str);

			JSONObject json = new JSONObject(str);
			NamoNamoSharedPrefrence.setRegistered(mcontext, true);
			jid = (json.getString("jid"));
			password = (json.getString("password"));
			if (json.has("image")) {
				image = json.getString("image");
				NamoNamoSharedPrefrence.setProfileImageUrl(mcontext, image);
			}
			if (json.has("firstname")) {
				String value = json.getString("firstname");
				NamoNamoSharedPrefrence.setUserName(mcontext, value);
			}
			if (json.has("status")) {
				status = json.getString("status");
				NamoNamoSharedPrefrence.setStatus(mcontext, status);
			}
			if (json.has("mobile_no")) {
				String mobile_no = json.getString("mobile_no");
				NamoNamoSharedPrefrence.setMobileNo(mcontext, mobile_no);
			}
			//

			if (json.has("user_id")) {
				String value = json.getString("user_id");
				NamoNamoSharedPrefrence.setUserId(mcontext, value);
			}
			if (json.has("userId")) {
				String value = json.getString("userId");
				NamoNamoSharedPrefrence.setUserId(mcontext, value);
			}
			if (json.has("show_last_seen")) {
				String value = json.getString("show_last_seen");
				NamoNamoSharedPrefrence.setShowLastSeen(mcontext,
						value.equalsIgnoreCase("1"));
			}
			if (json.has("show_profile")) {
				String value = json.getString("show_profile");
				NamoNamoSharedPrefrence.setShowProfile(mcontext,
						value.equalsIgnoreCase("1"));
			}
			if (json.has("show_status")) {
				String value = json.getString("show_status");
				NamoNamoSharedPrefrence.setShowStatus(mcontext,
						value.equalsIgnoreCase("1"));
			}
		} catch (Exception x) {
			x.printStackTrace();
		}
	}

	public boolean isSuccess() {
		return code == 200;
	}

	public String getJid() {
		return jid;
	}

	public String getPassword() {
		return password;
	}
}