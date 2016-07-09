//////////////////////////////////////////////////////////////////////////////
// 
//  Copyright (c) 2011 Aquevix Solutions Pvt Ltd. All rights reserved.
// 
//  Redistribution and use in source and binary forms, with or without
//  modification, are permitted provided that the following conditions are
//  met:
// 
//     * Redistributions of source code must retain the above copyright
//       notice, this list of conditions and the following disclaimer.
//     * Redistributions in binary form must reproduce the above
//       copyright notice, this list of conditions and the following disclaimer
//       in the documentation and/or other materials provided with the
//       distribution.
//     * Neither the name of Aquevix Solutions Pvt Ltd. nor the names of its
//       contributors may be used to endorse or promote products derived from
//       this software without specific prior written permission.
//     * Aquevix reserves the right to redistribute the source code, binaries,
//       derivative works, techniques, documents, designs and other techniques
//       used in this framework under a different license.
// 
//     For any questions you may contact us at:
// 
//       Attn:
//         Aquevix Solutions Pvt Ltd.
//         Suite 8-D, A-8 Bigjo's Tower,
//         Netaji Subhash Place,
//         New Delhi - 110034, INDIA
// 
//       Contact:
//         http://www.aquevix.com
//         info@aquevix.com
//         +91-11-45600412
// 
//  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
//  "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
//  LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
//  A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
//  OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
//  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
//  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
//  DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
//  THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
//  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
//  OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
//////////////////////////////////////////////////////////////////////////////
package com.hihello.app.service;

import org.apache.http.NameValuePair;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.hihello.app.service.DataProcessor.DataPolicy;
import com.hihello.app.service.Request.RequestType;


public abstract class Servicable<T extends DataHook> {
	public interface ServiceListener {
		public void onComplete(Servicable<?> service);
	}

	ServiceListener _callbackSelector;
	DataProcessor<T> _mprocessor;
	protected Request _mrequest;
	Response _mresponse;
	DataPolicy _mpolicy = DataPolicy.Optimized;
	boolean _cancel = false;

	public String getKey() {
		String params = _mrequest._url;
		for (NameValuePair pair : _mrequest._mparams)
			params = params + pair.getName() + "=" + pair.getValue() + "&";

		Log.e("params", params);
		return params;
	}

	private AsyncTask<Void, Void, Void> async;

	public Servicable() {
		_mrequest = new Request();
		_mprocessor = new DataProcessor<T>();
	}

	public void loadMoreNews(String timestamp) {
		addParams("after", timestamp);
		runAsync(_callbackSelector);
	}

	private View view2;

	public View getView2() {
		return this.view2;
	}

	public void setView2(View view2) {
		this.view2 = view2;
	}

	public void cancelProcess() {
		if (async != null)
			async.cancel(true);
		_cancel = true;
		// if(async!=null)
		// async.cancel(false);
	}

	public boolean isCancel() {
		return _cancel;
	}

	public void setRequest(Request request) {
		_mrequest = request;
	}

	public void addHeader(String key, String value) {
		_mrequest.addHeader(key, value);
	}

	public void setUrl(String url) {
		_mrequest.setUrl(url);
	}

	public void addParams(String key, String value) {
		_mrequest.addParams(key, value);
	}

	public void setDataPolicy(DataPolicy policy) {
		_mpolicy = policy;
	}

	public void setRequestType(RequestType requestType) {
		_mrequest.setRequestType(requestType);
	}

	public Response run() {
		return _mresponse = _mprocessor.execute(_mrequest, _mpolicy);
	}

	public Response getResponce() {
		if (_mresponse != null)
			return _mresponse;
		else
			return null;

	}

	public void onPostRun() {
	}

	public void onCacheRun(String data) {

	}

	class asyncExecute extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			if (isCancelled())
				return null;
			run();
			if (isCancelled())
				return null;
			onPostRun();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (isCancelled())
				return;
			if (_callbackSelector != null)
				_callbackSelector.onComplete(Servicable.this);
			super.onPostExecute(result);
		}
	}

	public void runAsync(ServiceListener listener) {
		_callbackSelector = listener;
		if (async != null)
			async.cancel(false);
		async = new asyncExecute().execute((Void) null);
	}
}
