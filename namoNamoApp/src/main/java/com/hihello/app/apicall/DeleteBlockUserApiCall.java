package com.hihello.app.apicall;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.hihello.app.constant.HiHelloSharedPrefrence;
import com.hihello.app.constant.HiHelloConstant;
import com.hihello.app.db.BlockUser;
import com.hihello.app.db.BlockUserDBService;
import com.hihello.app.service.DataProcessor.DataPolicy;
import com.hihello.app.service.NullDataHook;
import com.hihello.app.service.Request.RequestType;
import com.hihello.app.service.Servicable;

public class DeleteBlockUserApiCall extends Servicable<NullDataHook> {

	private Context mcontext;
	private ArrayList<BlockUser> blockUsers;

	public DeleteBlockUserApiCall(Context mcontext, String block_user_jid) {
		this.mcontext = mcontext;
		setUrl(HiHelloConstant.DeleteBlockUserUrl);
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
			blockUsers = new ArrayList<BlockUser>();
			BlockUserDBService.deleteAllBlockUser(mcontext);
			for (int i = 0; i < length; i++) {
				JSONObject json = jsonArray.getJSONObject(i);
				BlockUser user = new BlockUser(json);
				getBlockUsers().add(user);
				BlockUserDBService.saveBlockUser(mcontext, user);
			}
		} catch (Exception x) {
			x.printStackTrace();
		}
	}

	public ArrayList<BlockUser> getBlockUsers() {
		return blockUsers;
	}

}