package com.namonamo.app.apicall;

import org.json.JSONObject;

import android.content.Context;
import android.widget.Toast;

import com.namonamo.app.constant.NamoNamoConstant;
import com.namonamo.app.constant.NamoNamoSharedPrefrence;
import com.namonamo.app.service.DataProcessor.DataPolicy;
import com.namonamo.app.service.NullDataHook;
import com.namonamo.app.service.Request.RequestType;
import com.namonamo.app.service.Servicable;

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

		setUrl(NamoNamoConstant.UpdateProfileUrl);
		setRequestType(RequestType.POST);
		setDataPolicy(DataPolicy.LiveOnly);
		addParams("mobile_no", NamoNamoSharedPrefrence.getMobileNo(mcontext));
		addParams("user_id", NamoNamoSharedPrefrence.getUserId(mcontext));

		if (status.length() > 0)
			addParams("status", status);
		if (name.length() > 0)
			addParams("firstname", name);
		if(key.length()>0){
			addParams(key, value?"1":"0");
		}
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
					NamoNamoSharedPrefrence.setStatus(mcontext, status);
				if (name.length() > 0)
					NamoNamoSharedPrefrence.setUserName(mcontext, name);

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