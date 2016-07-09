package com.hihello.app.apicall;

import com.hihello.app.constant.HiHelloConstant;
import com.hihello.app.service.DataProcessor.DataPolicy;
import com.hihello.app.service.NullDataHook;
import com.hihello.app.service.Request.RequestType;
import com.hihello.app.service.Servicable;

public class SendInvitationApiCall extends Servicable<NullDataHook> {

	private boolean success;

	public SendInvitationApiCall(String userId, String mobile_no) {
		setUrl(HiHelloConstant.SendInvitationUrl);
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