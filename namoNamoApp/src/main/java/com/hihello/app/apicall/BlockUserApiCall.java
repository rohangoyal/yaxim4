package com.hihello.app.apicall;

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

public class BlockUserApiCall extends Servicable<NullDataHook> {

	private Context mcontext;

	public BlockUserApiCall(Context mcontext, String block_user_jid) {
		this.mcontext = mcontext;
		setUrl(HiHelloConstant.BlockUserUrl);
		setRequestType(RequestType.POST);
		setDataPolicy(DataPolicy.LiveOnly);
		addParams("block_user_jid", block_user_jid);
		addParams("user_id", HiHelloSharedPrefrence.getUserId(mcontext));
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