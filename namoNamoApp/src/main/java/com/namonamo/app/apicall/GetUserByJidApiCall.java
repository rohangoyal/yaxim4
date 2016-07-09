package com.namonamo.app.apicall;

import org.json.JSONObject;

import android.content.Context;

import com.namonamo.app.constant.NamoNamoConstant;
import com.namonamo.app.db.NamoNamoContact;
import com.namonamo.app.db.NamoNamoContactDBService;
import com.namonamo.app.service.DataProcessor.DataPolicy;
import com.namonamo.app.service.NullDataHook;
import com.namonamo.app.service.Request.RequestType;
import com.namonamo.app.service.Servicable;

public class GetUserByJidApiCall extends Servicable<NullDataHook> {

	private Context mContext;
	private NamoNamoContact contact;
	private String jid;

	public GetUserByJidApiCall(Context ctx, String jid) {
		this.mContext = ctx;
		this.jid = jid;
		setUrl(NamoNamoConstant.GetUserByJidUrl);
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
			contact = new NamoNamoContact(json);
			NamoNamoContactDBService.saveContact(mContext, contact);
		} catch (Exception x) {
			x.printStackTrace();
		}
	}

	public NamoNamoContact getAllRewardItems() {
		return contact;
	}
}