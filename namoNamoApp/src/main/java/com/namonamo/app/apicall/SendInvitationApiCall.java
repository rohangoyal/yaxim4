package com.namonamo.app.apicall;

import com.namonamo.app.constant.NamoNamoConstant;
import com.namonamo.app.service.DataProcessor.DataPolicy;
import com.namonamo.app.service.NullDataHook;
import com.namonamo.app.service.Request.RequestType;
import com.namonamo.app.service.Servicable;

public class SendInvitationApiCall extends Servicable<NullDataHook> {

	private boolean success;

	public SendInvitationApiCall(String userId, String mobile_no) {
		setUrl(NamoNamoConstant.SendInvitationUrl);
		setRequestType(RequestType.POST);
		setDataPolicy(DataPolicy.LiveOnly);
		addParams("user_id", userId);
		addParams("mobile_no", mobile_no.replace("+", ""));
	}

	@Override
	public void onPostRun() {
		super.onPostRun();
		try {
			String str = getResponce().getData();
			if (getResponce().getHttpCode() == 200) {
				setSuccess(true);
			}
		} catch (Exception x) {
			x.printStackTrace();
		}
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public boolean isSuccess() {
		return success;
	}
}