package com.hihello.app.apicall;

import org.json.JSONArray;

import com.hihello.app.activity.UIApplication;
import com.hihello.app.constant.HiHelloConstant;
import com.hihello.app.db.DailyMessage;
import com.hihello.app.db.DailyMessageDBService;
import com.hihello.app.service.DataProcessor.DataPolicy;
import com.hihello.app.service.NullDataHook;
import com.hihello.app.service.Request.RequestType;
import com.hihello.app.service.Servicable;

public class GetDailyMessageApiCall extends Servicable<NullDataHook> {

	public GetDailyMessageApiCall(long date) {
		setUrl(HiHelloConstant.GetDailyMessageUrl);
		setRequestType(RequestType.GET);
		setDataPolicy(DataPolicy.LiveOnly);
		date++;
		addParams("time", date + "");
	}

	@Override
	public void onPostRun() {
		super.onPostRun();
		try {
			/*
			 * { "auth_key": "C3729EB592CFF87602DA22D315E03725" }
			 */
			String str = getResponce().getData();
			JSONArray jsonArray = new JSONArray(str);
			int length = jsonArray.length();
			for (int i = 0; i < length; i++) {
				DailyMessage item = new DailyMessage(jsonArray.getJSONObject(i));
				DailyMessageDBService.saveMessage(UIApplication.context, item);
			}

		} catch (Exception x) {
			x.printStackTrace();
		}
	}

}