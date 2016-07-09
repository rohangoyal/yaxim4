package com.namonamo.app.apicall;

import java.util.ArrayList;

import org.json.JSONArray;

import com.namonamo.app.constant.NamoNamoConstant;
import com.namonamo.app.content.RewardItem;
import com.namonamo.app.service.DataProcessor.DataPolicy;
import com.namonamo.app.service.NullDataHook;
import com.namonamo.app.service.Request.RequestType;
import com.namonamo.app.service.Servicable;

public class GetUserRewardApiCall extends Servicable<NullDataHook> {

	private ArrayList<RewardItem> rewardItems;

	public GetUserRewardApiCall(String userId) {
		setUrl(NamoNamoConstant.GetUserRewardUrl);
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
			JSONArray jsonArray = new JSONArray(str);
			int length = jsonArray.length();
			rewardItems = new ArrayList<RewardItem>(length);
			for (int i = 0; i < length; i++) {
				RewardItem item = new RewardItem(jsonArray.getJSONObject(i));
				rewardItems.add(item);
			}

		} catch (Exception x) {
			x.printStackTrace();
		}
	}

	public ArrayList<RewardItem> getAllRewardItems() {
		return rewardItems;
	}
}