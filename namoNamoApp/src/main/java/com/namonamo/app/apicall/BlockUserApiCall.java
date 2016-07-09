package com.namonamo.app.apicall;

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

public class BlockUserApiCall extends Servicable<NullDataHook> {

	private Context mcontext;

	public BlockUserApiCall(Context mcontext, String block_user_jid) {
		this.mcontext = mcontext;
		setUrl(NamoNamoConstant.BlockUserUrl);
		setRequestType(RequestType.POST);
		setDataPolicy(DataPolicy.LiveOnly);
		addParams("block_user_jid", block_user_jid);
		addParams("user_id", NamoNamoSharedPrefrence.getUserId(mcontext));
	}

	@Override
	public void onPostRun() {
		super.onPostRun();
		try {
			String str = getResponce().getData();
			JSONArray jsonArray = new JSONArray(str);
			int length = jsonArray.length();
			BlockUserDBService.deleteAllBlockUser(mcontext);
			for (int i = 0; i < length; i++) {
				JSONObject json = jsonArray.getJSONObject(i);
				BlockUser user = new BlockUser(json);
				BlockUserDBService.saveBlockUser(mcontext, user);
			}

		} catch (Exception x) {
			x.printStackTrace();
		}
	}
}