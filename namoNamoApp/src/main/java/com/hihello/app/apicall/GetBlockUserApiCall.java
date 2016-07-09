package com.hihello.app.apicall;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.hihello.app.constant.HiHelloConstant;
import com.hihello.app.constant.HiHelloSharedPrefrence;
import com.hihello.app.db.BlockUser;
import com.hihello.app.db.BlockUserDBService;
import com.hihello.app.service.DataProcessor.DataPolicy;
import com.hihello.app.service.NullDataHook;
import com.hihello.app.service.Request.RequestType;
import com.hihello.app.service.Servicable;

public class GetBlockUserApiCall extends Servicable<NullDataHook> {

	private ArrayList<BlockUser> blockUsers;
	private Context mContext;

	public GetBlockUserApiCall(Context ctx) {
		this.mContext = ctx;
		setUrl(HiHelloConstant.GetBlockUserUrl);
		setRequestType(RequestType.GET);
		setDataPolicy(DataPolicy.LiveOnly);
		addParams("user_id", HiHelloSharedPrefrence.getUserId(mContext));
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