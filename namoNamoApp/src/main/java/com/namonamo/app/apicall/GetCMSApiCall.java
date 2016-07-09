package com.namonamo.app.apicall;

import org.json.JSONObject;

import android.content.Context;

import com.namonamo.app.constant.NamoNamoConstant;
import com.namonamo.app.constant.NamoNamoSharedPrefrence;
import com.namonamo.app.service.DataProcessor.DataPolicy;
import com.namonamo.app.service.NullDataHook;
import com.namonamo.app.service.Request.RequestType;
import com.namonamo.app.service.Servicable;

public class GetCMSApiCall extends Servicable<NullDataHook> {

	private Context ctx;

	public GetCMSApiCall(Context ctx) {
		this.ctx = ctx;
		setUrl(NamoNamoConstant.GetCMSUrl);
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
			NamoNamoSharedPrefrence.setVersionCode(ctx,
					json.getJSONObject("android").getInt("versionCode"));
			NamoNamoSharedPrefrence.setForceUpgrade(ctx,
					json.getJSONObject("android").getString("forceUpgrade")
							.equalsIgnoreCase("Y"));

		} catch (Exception x) {
			x.printStackTrace();
		}
	}

}