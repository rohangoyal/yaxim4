package com.namonamo.app.apicall;

import java.util.ArrayList;

import org.json.JSONArray;

import android.content.Context;

import com.namonamo.app.common.ContactUtil;
import com.namonamo.app.constant.NamoNamoConstant;
import com.namonamo.app.db.NamoNamoContact;
import com.namonamo.app.db.NamoNamoContactDBService;
import com.namonamo.app.service.DataProcessor.DataPolicy;
import com.namonamo.app.service.NullDataHook;
import com.namonamo.app.service.Request.RequestType;
import com.namonamo.app.service.Servicable;

public class DownloadRegisterUserApiCall extends Servicable<NullDataHook> {
	public static boolean isBusy = false;
	private Context mcontext;
	private int code;
	private ArrayList<NamoNamoContact> namonamoContacts;

	public DownloadRegisterUserApiCall(Context mcontext, String mobile_nos) {
		this.mcontext = mcontext;
		setUrl(NamoNamoConstant.GetUserDetail);
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
			JSONArray jsonArray = new JSONArray(str);
			int length = jsonArray.length();
			namonamoContacts = new ArrayList<NamoNamoContact>(length);
			for (int i = 0; i < length; i++) {
				NamoNamoContact contact = new NamoNamoContact(
						jsonArray.getJSONObject(i));
				namonamoContacts.add(contact);

				contact.setDisplayName(ContactUtil.getContactName(mcontext,
						contact.getMobile_number()));
				NamoNamoContactDBService.saveContact(mcontext, contact);
			}

		} catch (Exception x) {
			x.printStackTrace();
		}
	}

	public ArrayList<NamoNamoContact> getAllContact() {
		return namonamoContacts;
	}
}