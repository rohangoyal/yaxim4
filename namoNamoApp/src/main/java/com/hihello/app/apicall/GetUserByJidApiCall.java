package com.hihello.app.apicall;

import org.json.JSONObject;

import android.content.Context;

import com.hihello.app.constant.HiHelloConstant;
import com.hihello.app.db.HiHelloContact;
import com.hihello.app.db.hiHelloContactDBService;
import com.hihello.app.service.DataProcessor.DataPolicy;
import com.hihello.app.service.NullDataHook;
import com.hihello.app.service.Request.RequestType;
import com.hihello.app.service.Servicable;

public class GetUserByJidApiCall extends Servicable<NullDataHook> {

	private Context mContext;
	private HiHelloContact contact;
	private String jid;

	public GetUserByJidApiCall(Context ctx, String jid) {
		this.mContext = ctx;
		this.jid = jid;
		setUrl(HiHelloConstant.GetUserByJidUrl);
		setRequestType(RequestType.GET);
		addParams("jid", jid);
		setDataPolicy(DataPolicy.LiveOnly);
	}

	@Override
	public void onPostRun() {
		super.onPostRun();
		try {
			/*
			 * { "auth_key": "C3729EB592CFF87602DA22D315E03725" }
			 */
			String str = getResponce().getData();
			JSONObject json = new JSONObject(str);
			json.put("jid", jid);
			contact = new HiHelloContact(json);
			hiHelloContactDBService.saveContact(mContext, contact);
		} catch (Exception x) {
			x.printStackTrace();
		}
	}

	public HiHelloContact getAllRewardItems() {
		return contact;
	}
}