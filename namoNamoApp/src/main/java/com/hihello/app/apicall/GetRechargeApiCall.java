package com.hihello.app.apicall;

import android.util.Log;

import com.hihello.app.service.DataProcessor.DataPolicy;
import com.hihello.app.service.NullDataHook;
import com.hihello.app.service.Request.RequestType;
import com.hihello.app.service.Servicable;

public class GetRechargeApiCall extends Servicable<NullDataHook> {

	private String responce;


	public GetRechargeApiCall(String mobile_no, String user_name,String operator_name,String amount,String user_id,String mobile_number_type,String circle) {

		String url="http://geminibusiness.in/admin/index.php/api/other/rechargeRequest";
		url=url.replace(" ","%20");
		setUrl(url);
		setRequestType(RequestType.POST);
		setDataPolicy(DataPolicy.LiveOnly);
		Log.e("url", url);
		addParams("mobile_number", mobile_no);
		addParams("user_name", user_name);
		addParams("operator_name",operator_name);
		addParams("amount",amount);
		addParams("user_id",user_id);
		addParams("mobile_number_type",mobile_number_type);
		addParams("circle",circle);

//		addParams("mobile", mobile_no);
//		addParams("message", message);
//		addParams("sender", "NMOAPP");
//		addParams("type", "3");

	}

	@Override
	public void onPostRun() {
		super.onPostRun();
		if (getResponce() != null)
			Log.e("Recharge response", getResponce().getData());
			setStringResponce(getResponce().getData());
	}

	public void setStringResponce(String responce) {
		this.responce = responce;
	}

	public String getStringResponce() {
		return responce;
	}
}