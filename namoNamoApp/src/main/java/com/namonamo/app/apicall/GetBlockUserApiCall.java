package com.namonamo.app.apicall;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.namonamo.app.constant.NamoNamoConstant;
import com.namonamo.app.constant.NamoNamoSharedPrefrence;
import com.namonamo.app.db.BlockUser;
import com.namonamo.app.db.BlockUserDBService;
import com.namonamo.app.service.DataProcessor.DataPolicy;
import com.namonamo.app.service.NullDataHook;
import com.namonamo.app.service.Request.RequestType;
import com.namonamo.app.service.Servicable;

public class GetBlockUserApiCall extends Servicable<NullDataHook> {

	private ArrayList<BlockUser> blockUsers;
	private Context mContext;

	public GetBlockUserApiCall(Context ctx) {
		this.mContext = ctx;
		setUrl(NamoNamoConstant.GetBlockUserUrl);
		setRequestType(RequestType.GET);
		setDataPolicy(DataPolicy.LiveOnly);
		addParams("user_id", NamoNamoSharedPrefrence.getUserId(mContext));
	}

	@Override
	public void onPostRun() {
		super.onPostRun();
		try {
			String str = getResponce().getData();
			JSONArray jsonArray = new JSONArray(str);
			int length = jsonArray.length();
			blockUsers = new ArrayList<BlockUser>();
			BlockUserDBService.deleteAllBlockUser(mContext);
			for (int i = 0; i < length; i++) {
				JSONObject json = jsonArray.getJSONObject(i);
				BlockUser user = new BlockUser(json);
				blockUsers.add(user);
				BlockUserDBService.saveBlockUser(mContext, user);
			}

		} catch (Exception x) {
			x.printStackTrace();
		}
	}

	public ArrayList<BlockUser> getAllBlockedUsers() {
		return blockUsers;
	}
}