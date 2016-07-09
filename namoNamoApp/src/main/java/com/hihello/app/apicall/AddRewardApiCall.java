package com.hihello.app.apicall;

import org.json.JSONObject;

import android.content.Context;

import com.hihello.app.constant.HiHelloSharedPrefrence;
import com.hihello.app.constant.HiHelloConstant;
import com.hihello.app.service.DataProcessor.DataPolicy;
import com.hihello.app.service.NullDataHook;
import com.hihello.app.service.Request.RequestType;
import com.hihello.app.service.Servicable;

public class AddRewardApiCall extends Servicable<NullDataHook> {

	private Context mcontext;
	private int reward;
	private String rewardType;

	public AddRewardApiCall(Context mcontext, String userId, String mobile_no,
			int reward, String rewardType) {
		this.mcontext = mcontext;
		setUrl(HiHelloConstant.AddRewardUrl);
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
					HiHelloSharedPrefrence.removeRewardPoint(mcontext,
							reward * 1000, rewardType);
				}
				if (rewardType.equalsIgnoreCase("IMAGE")) {
					HiHelloSharedPrefrence.removeRewardPoint(mcontext,
							reward * 2, rewardType);
				}
				if (rewardType.equalsIgnoreCase("FACEBOOK LIKE")) {
					HiHelloSharedPrefrence.setFacebookLike(mcontext, true);
				}
				if (rewardType.equalsIgnoreCase("RATING APP")) {
					HiHelloSharedPrefrence.setRateUs(mcontext, true);
				}
			} else
				HiHelloSharedPrefrence.removeRewardPoint(mcontext, reward,
						rewardType);

		} catch (Exception x) {
			x.printStackTrace();
		}
	}
}