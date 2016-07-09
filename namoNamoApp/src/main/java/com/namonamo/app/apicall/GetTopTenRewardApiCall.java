package com.namonamo.app.apicall;

import java.util.ArrayList;
import java.util.Collections;

import org.json.JSONArray;

import android.content.Context;

import com.namonamo.app.constant.NamoNamoConstant;
import com.namonamo.app.constant.NamoNamoSharedPrefrence;
import com.namonamo.app.content.RewardItem;
import com.namonamo.app.db.NamoNamoContact;
import com.namonamo.app.db.NamoNamoContactDBService;
import com.namonamo.app.service.DataProcessor.DataPolicy;
import com.namonamo.app.service.NullDataHook;
import com.namonamo.app.service.Request.RequestType;
import com.namonamo.app.service.Servicable;

public class GetTopTenRewardApiCall extends Servicable<NullDataHook> {

	private ArrayList<RewardItem> rewardItems;
	private Context mContext;

	public GetTopTenRewardApiCall(Context ctx) {
		this.mContext = ctx;
		setUrl(NamoNamoConstant.GetTopTenReward);
		setRequestType(RequestType.GET);
		setDataPolicy(DataPolicy.LiveOnly);
		addParams("user_id", NamoNamoSharedPrefrence.getUserId(mContext));

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
				NamoNamoContact contact = NamoNamoContactDBService
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