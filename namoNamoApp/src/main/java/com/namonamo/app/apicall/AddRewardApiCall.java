package com.namonamo.app.apicall;

import org.json.JSONObject;

import android.content.Context;

import com.namonamo.app.constant.NamoNamoConstant;
import com.namonamo.app.constant.NamoNamoSharedPrefrence;
import com.namonamo.app.service.DataProcessor.DataPolicy;
import com.namonamo.app.service.NullDataHook;
import com.namonamo.app.service.Request.RequestType;
import com.namonamo.app.service.Servicable;

public class AddRewardApiCall extends Servicable<NullDataHook> {

	private Context mcontext;
	private int reward;
	private String rewardType;

	public AddRewardApiCall(Context mcontext, String userId, String mobile_no,
			int reward, String rewardType) {
		this.mcontext = mcontext;
		setUrl(NamoNamoConstant.AddRewardUrl);
		setRequestType(RequestType.POST);
		setDataPolicy(DataPolicy.LiveOnly);
		addParams("userId", userId);
		addParams("user_id", userId);
		addParams("mobile_no", mobile_no);
		addParams("reward", (this.reward = reward) + "");
		addParams("rewardType", this.rewardType = rewardType);

	}

	@Override
	public void onPostRun() {
		super.onPostRun();
		try {
			String str = getResponce().getData();
			JSONObject json = new JSONObject(str);
			if (json.getString("code").equalsIgnoreCase("200")) {
				if (rewardType.equalsIgnoreCase("TEXT")) {
					NamoNamoSharedPrefrence.removeRewardPoint(mcontext,
							reward * 1000, rewardType);
				}
				if (rewardType.equalsIgnoreCase("IMAGE")) {
					NamoNamoSharedPrefrence.removeRewardPoint(mcontext,
							reward * 2, rewardType);
				}
				if (rewardType.equalsIgnoreCase("FACEBOOK LIKE")) {
					NamoNamoSharedPrefrence.setFacebookLike(mcontext, true);
				}
				if (rewardType.equalsIgnoreCase("RATING APP")) {
					NamoNamoSharedPrefrence.setRateUs(mcontext, true);
				}
			} else
				NamoNamoSharedPrefrence.removeRewardPoint(mcontext, reward,
						rewardType);

		} catch (Exception x) {
			x.printStackTrace();
		}
	}
}