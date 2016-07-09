package com.hihello.app.apicall;

import android.util.Log;

import com.hihello.app.service.DataProcessor.DataPolicy;
import com.hihello.app.service.NullDataHook;
import com.hihello.app.service.Request.RequestType;
import com.hihello.app.service.Servicable;

public class GetUserRewardApiCall extends Servicable<NullDataHook> {

String response;

	public GetUserRewardApiCall(String userId) {
		setUrl("http://geminibusiness.in/admin/index.php/api/user/getusertotalrewards");
		setRequestType(RequestType.GET);
		setDataPolicy(DataPolicy.LiveOnly);
		addParams("userId", userId);
		addParams("user_id", userId);
	}

	@Override
	public void onPostRun() {
		super.onPostRun();
		try {
			/*
			 * { "auth_key": "C3729EB592CFF87602DA22D315E03725" }
			 */
			String str = getResponce().getData();

			setStringResponce(str);

		Log.e("str",str);

		} catch (Exception x) {
			x.printStackTrace();
		}
	}
	public void setStringResponce(String responce) {
		this.response = responce;
	}

	public String getStringResponce() {
		return response;
	}

}