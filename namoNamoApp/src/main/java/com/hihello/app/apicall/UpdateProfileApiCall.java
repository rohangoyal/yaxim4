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

public class UpdateProfileApiCall extends Servicable<NullDataHook> {

	private Context mcontext;
	private int code;
	private String password;
	private String status;
	private String name;

	public UpdateProfileApiCall(Context mcontext, String name, String status,
			String key, boolean value) {
		this.mcontext = mcontext;
		this.status = status;
		this.name = name;

		setUrl(HiHelloConstant.UpdateProfileUrl);
		setRequestType(RequestType.POST);
		setDataPolicy(DataPolicy.LiveOnly);
		//addParams("mobile_no", HiHelloSharedPrefrence.getMobileNo(mcontext));
		addParams("user_id", HiHelloSharedPrefrence.getUserId(mcontext));

		if (status.length() > 0)
			Log.e("status", status);
			addParams("status", status);
		if (name.length() > 0)
			addParams("firstname", name);
		if(key.length()>0){
			addParams(key, value?"1":"0");
		}

			Log.e("data",getKey());
	}

	@Override
	public void onPostRun() {
		super.onPostRun();
		try {
			/*
			 * { "auth_key": "C3729EB592CFF87602DA22D315E03725" }
			 */
			String str = getResponce().getData();
			JSONObject json = new JSONObject(str);
			code = json.getInt("code");
			if (code == 200) {
				if (status.length() > 0)
					HiHelloSharedPrefrence.setStatus(mcontext, status);
				if (name.length() > 0)
					HiHelloSharedPrefrence.setUserName(mcontext, name);

			}
		} catch (Exception x) {
			x.printStackTrace();
		}
	}

	public boolean isSuccess() {
		return code == 200;
	}

	public String getStatus() {
		return status;
	}

	public String getPassword() {
		return password;
	}
}