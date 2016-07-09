package com.hihello.app.apicall;

import java.util.ArrayList;

import org.json.JSONArray;

import android.content.Context;
import android.util.Log;

import com.hihello.app.common.ContactUtil;
import com.hihello.app.constant.HiHelloConstant;
import com.hihello.app.db.HiHelloContact;
import com.hihello.app.db.hiHelloContactDBService;
import com.hihello.app.service.DataProcessor.DataPolicy;
import com.hihello.app.service.NullDataHook;
import com.hihello.app.service.Request.RequestType;
import com.hihello.app.service.Servicable;

public class DownloadRegisterUserApiCall extends Servicable<NullDataHook> {
	public static boolean isBusy = false;
	private Context mcontext;
	private int code;
	private ArrayList<HiHelloContact> hihelloContacts;

	public DownloadRegisterUserApiCall(Context mcontext, String mobile_nos) {
		this.mcontext = mcontext;
		setUrl(HiHelloConstant.GetUserDetail);
		setRequestType(RequestType.GET);
		setDataPolicy(DataPolicy.LiveOnly);
		addParams("mobile_number", mobile_nos.replace("+", ""));
		isBusy = true;
	}

	@Override
	public void onPostRun() {
		super.onPostRun();
		try {
			isBusy = false;
			/*
			 * { "auth_key": "C3729EB592CFF87602DA22D315E03725" }
			 */
			String str = getResponce().getData();
			Log.e("str", str);
			JSONArray jsonArray = new JSONArray(str);
			int length = jsonArray.length();
			hihelloContacts = new ArrayList<HiHelloContact>(length);
			for (int i = 0; i < length; i++) {
				HiHelloContact contact = new HiHelloContact(
						jsonArray.getJSONObject(i));
				hihelloContacts.add(contact);

				contact.setDisplayName(ContactUtil.getContactName(mcontext,
						contact.getMobile_number()));
				hiHelloContactDBService.saveContact(mcontext, contact);
			}

		} catch (Exception x) {
			x.printStackTrace();
		}
	}

	public ArrayList<HiHelloContact> getAllContact() {
		return hihelloContacts;
	}
}