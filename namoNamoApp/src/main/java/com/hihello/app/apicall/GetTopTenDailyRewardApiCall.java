package com.hihello.app.apicall;

import java.util.ArrayList;
import java.util.Collections;

import org.json.JSONArray;

import android.content.Context;

import com.hihello.app.constant.HiHelloConstant;
import com.hihello.app.constant.HiHelloSharedPrefrence;
import com.hihello.app.content.RewardItem;
import com.hihello.app.db.HiHelloContact;
import com.hihello.app.db.hiHelloContactDBService;
import com.hihello.app.service.DataProcessor.DataPolicy;
import com.hihello.app.service.NullDataHook;
import com.hihello.app.service.Request.RequestType;
import com.hihello.app.service.Servicable;

public class GetTopTenDailyRewardApiCall extends Servicable<NullDataHook> {

	private ArrayList<RewardItem> rewardItems;
	private Context mContext;

	public GetTopTenDailyRewardApiCall(Context ctx) {
		this.mContext = ctx;
		setUrl(HiHelloConstant.GetTopTenDailyReward);
		setRequestType(RequestType.GET);
		setDataPolicy(DataPolicy.LiveOnly);
		addParams("user_id", HiHelloSharedPrefrence.getUserId(mContext));
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
				HiHelloContact contact = hiHelloContactDBService
						.fetchContactByUserID(mContext, item.getUsers_id());
				if (contact != null) {
					item.setFirstname(contact.getDisplayName());
				}
				if (item.getFirstname() == null
						|| item.getFirstname().length() < 1) {
					item.setFirstname("No Name");
				}

				rewardItems.add(item);
			}
			Collections.sort(rewardItems);

		} catch (Exception x) {
			x.printStackTrace();
		}
	}

	public ArrayList<RewardItem> getAllRewardItems() {
		return rewardItems;
	}
}