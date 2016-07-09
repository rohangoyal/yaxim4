package com.hihello.app.apicall;

import org.json.JSONObject;

import android.content.Context;

import com.hihello.app.constant.HiHelloConstant;
import com.hihello.app.constant.HiHelloSharedPrefrence;
import com.hihello.app.service.DataProcessor.DataPolicy;
import com.hihello.app.service.NullDataHook;
import com.hihello.app.service.Request.RequestType;
import com.hihello.app.service.Servicable;

public class GetCMSApiCall extends Servicable<NullDataHook> {

	private Context ctx;

	public GetCMSApiCall(Context ctx) {
		this.ctx = ctx;
		setUrl(HiHelloConstant.GetCMSUrl);
		setRequestType(RequestType.GET);
		setDataPolicy(DataPolicy.LiveOnly);
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
			HiHelloSharedPrefrence.setVersionCode(ctx,
					json.getJSONObject("android").getInt("versionCode"));
			HiHelloSharedPrefrence.setForceUpgrade(ctx,
					json.getJSONObject("android").getString("forceUpgrade")
							.equalsIgnoreCase("Y"));

		} catch (Exception x) {
			x.printStackTrace();
		}
	}

}