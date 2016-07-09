package com.hihello.app.apicall;

import org.json.JSONArray;

import android.content.Context;

import com.hihello.app.constant.HiHelloConstant;
import com.hihello.app.db.HiHelloContact;
import com.hihello.app.service.DataProcessor.DataPolicy;
import com.hihello.app.service.NullDataHook;
import com.hihello.app.service.Request.RequestType;
import com.hihello.app.service.Servicable;

public class UpdateUserApiCall extends Servicable<NullDataHook> {

	private Context mcontext;

	public UpdateUserApiCall(Context mcontext, String mobile_nos) {
		this.mcontext = mcontext;
		setUrl(HiHelloConstant.GetUserDetail);
		setRequestType(RequestType.GET);
		setDataPolicy(DataPolicy.LiveOnly);
		addParams("mobile_number", mobile_nos);
	}

	@Override
	public void onPostRun() {
		super.onPostRun();
		try {
			if (getResponce() == null)
				return;
			/*
			 * { "auth_key": "C3729EB592CFF87602DA22D315E03725" }
			 */
			String str = getResponce().getData();
			JSONArray jsonArray = new JSONArray(str);
			int length = jsonArray.length();
			for (int i = 0; i < length; i++) {
				HiHelloContact contact = new HiHelloContact(
						jsonArray.getJSONObject(i));
			}

		} catch (Exception x) {
			x.printStackTrace();
		}
	}
}