package com.hihello.app.apicall;

import android.util.Log;

import com.hihello.app.service.DataProcessor.DataPolicy;
import com.hihello.app.service.NullDataHook;
import com.hihello.app.service.Request.RequestType;
import com.hihello.app.service.Servicable;

public class GetBannersApiCall extends Servicable<NullDataHook> {

	private String responce;


	public GetBannersApiCall(String slider_type) {

		String url="http://geminibusiness.in/admin/index.php/api/other/getBanner";
		url=url.replace(" ","%20");
		setUrl(url);
		setRequestType(RequestType.POST);
		setDataPolicy(DataPolicy.LiveOnly);
		Log.e("url", url);
		addParams("slider_type",slider_type);


//		addParams("mobile", mobile_no);
//		addParams("message", message);
//		addParams("sender", "NMOAPP");
//		addParams("type", "3");

	}

	@Override
	public void onPostRun() {
		super.onPostRun();
		if (getResponce() != null)
			Log.e("banners response",getResponce().getData());
			setStringResponce(getResponce().getData());
	}

	public void setStringResponce(String responce) {
		this.responce = responce;
	}

	public String getStringResponce() {
		return responce;
	}
}