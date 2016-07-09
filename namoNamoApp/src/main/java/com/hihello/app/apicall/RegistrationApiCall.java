package com.hihello.app.apicall;

import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.hihello.app.constant.HiHelloSharedPrefrence;
import com.hihello.app.constant.HiHelloConstant;
import com.hihello.app.service.DataProcessor.DataPolicy;
import com.hihello.app.service.NullDataHook;
import com.hihello.app.service.Request.RequestType;
import com.hihello.app.service.Servicable;

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
		setUrl(HiHelloConstant.RegistrationUrl);
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
			HiHelloSharedPrefrence.setRegistered(mcontext, true);
			jid = (json.getString("jid"));
			password = (json.getString("password"));
			if (json.has("image")) {
				image = json.getString("image");
				HiHelloSharedPrefrence.setProfileImageUrl(mcontext, image);
			}
			if (json.has("firstname")) {
				String value = json.getString("firstname");
				HiHelloSharedPrefrence.setUserName(mcontext, value);
			}
			if (json.has("status")) {
				status = json.getString("status");
				HiHelloSharedPrefrence.setStatus(mcontext, status);
			}
			if (json.has("mobile_no")) {
				String mobile_no = json.getString("mobile_no");
				HiHelloSharedPrefrence.setMobileNo(mcontext, mobile_no);
			}
			//

			if (json.has("user_id")) {
				String value = json.getString("user_id");
				HiHelloSharedPrefrence.setUserId(mcontext, value);
			}
			if (json.has("userId")) {
				String value = json.getString("userId");
				HiHelloSharedPrefrence.setUserId(mcontext, value);
			}
			if (json.has("show_last_seen")) {
				String value = json.getString("show_last_seen");
				HiHelloSharedPrefrence.setShowLastSeen(mcontext,
						value.equalsIgnoreCase("1"));
			}
			if (json.has("show_profile")) {
				String value = json.getString("show_profile");
				HiHelloSharedPrefrence.setShowProfile(mcontext,
						value.equalsIgnoreCase("1"));
			}
			if (json.has("show_status")) {
				String value = json.getString("show_status");
				HiHelloSharedPrefrence.setShowStatus(mcontext,
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