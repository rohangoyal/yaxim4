package com.namonamo.app.apicall;

import org.json.JSONArray;

import com.namonamo.app.activity.UIApplication;
import com.namonamo.app.constant.NamoNamoConstant;
import com.namonamo.app.db.DailyMessage;
import com.namonamo.app.db.DailyMessageDBService;
import com.namonamo.app.service.DataProcessor.DataPolicy;
import com.namonamo.app.service.NullDataHook;
import com.namonamo.app.service.Request.RequestType;
import com.namonamo.app.service.Servicable;

public class GetDailyMessageApiCall extends Servicable<NullDataHook> {

	public GetDailyMessageApiCall(long date) {
		setUrl(NamoNamoConstant.GetDailyMessageUrl);
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