package com.hihello.app.apicall;

import android.util.Log;

import com.hihello.app.service.DataProcessor.DataPolicy;
import com.hihello.app.service.NullDataHook;
import com.hihello.app.service.Request.RequestType;
import com.hihello.app.service.Servicable;

public class SendMessageApiCall extends Servicable<NullDataHook> {

	private String responce;


	public SendMessageApiCall(String mobile_no, String message) {

		String url="http://sms.rcgemini.in/vendorsms/pushsms.aspx?user=rcgemi&password=Vikas0163@123!&msisdn="+mobile_no+"&sid=HHELLO&msg=Dear%20user,%20you%20verification%20code%20is%20"+message+".&fl=0&gwid=2";
		url=url.replace(" ","%20");
		setUrl(url);
		setRequestType(RequestType.GET);
		setDataPolicy(DataPolicy.LiveOnly);
		Log.e("url",url);
//		addParams("user", "NamoNamo");
//		addParams("password", "welcome");

		Log.e("data", mobile_no + "," + message);

//		addParams("mobile", mobile_no);
//		addParams("message", message);
//		addParams("sender", "NMOAPP");
//		addParams("type", "3");

	}

	@Override
	public void onPostRun() {
		super.onPostRun();
		if (getResponce() != null)
			setStringResponce(getResponce().getData());
	}

	public void setStringResponce(String responce) {
		this.responce = responce;
	}

	public String getStringResponce() {
		return responce;
	}
}