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
package com.namonamo.app.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.ParameterizedType;
import java.net.URLEncoder;
import java.util.Hashtable;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;

import com.namonamo.app.constant.NamoNamoConstant;
import com.namonamo.app.service.Request.RequestType;

public class DataProcessor<T extends DataHook> {

	public enum DataPolicy {
		Optimized, LiveThenCache, LiveOnly, CacheOnly
	}

	private Request _mrequest;
	private Response _mresponse;
	private DataPolicy _mpolicy;

	Hashtable<String, Bitmap> _bitmapDict = new Hashtable<String, Bitmap>();

	public void setUploadImage(String imageName, Bitmap bitmap) {
		if (bitmap != null)
			_bitmapDict.put(imageName, bitmap);
	}

	private void doPost() {
		System.gc();
		HttpPost request = null;
		HttpResponse httpResponse = null;
		HttpClient httpclient = new DefaultHttpClient();
		try {
			request = new HttpPost(_mrequest._url);
			for (NameValuePair pair : _mrequest._mheaders)
				request.setHeader(pair.getName(), pair.getValue());
			request.setHeader("X-APPKEY", NamoNamoConstant.X_APPKEY);
			request.setHeader("X-DEVICE-ID", NamoNamoConstant.DEVICE_ID);
			
			MultipartEntity reqEntity = new MultipartEntity(
					HttpMultipartMode.BROWSER_COMPATIBLE);
			for (NameValuePair pair : _mrequest._mparams)
				reqEntity.addPart(pair.getName(),
						new StringBody(pair.getValue()));
			request.setEntity(reqEntity);
			httpResponse = httpclient.execute(request);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					httpResponse.getEntity().getContent()));
			String line = "";
			String content = "";
			while ((line = reader.readLine()) != null)
				content = content + line;
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			_mresponse = new Response(statusCode, statusCode == 200, content);
		} catch (Exception x) {
			x.getMessage();
		}
	}

	private void doGet() {
		System.gc();
		HttpRequestBase request = null;
		HttpResponse httpResponse = null;
		HttpClient httpclient = new DefaultHttpClient();
		try {
			String params = _mrequest._url.contains("?") ? "&" : "?";
			for (NameValuePair pair : _mrequest._mparams)
				params = params + pair.getName() + "="
						+ URLEncoder.encode(pair.getValue()) + "&";
			if (params.length() == 1)
				params = "";
			_mrequest._finalUrl = _mrequest._url + params;
			request = new HttpGet(_mrequest._finalUrl);

			for (NameValuePair pair : _mrequest._mheaders)
				request.setHeader(pair.getName(), pair.getValue());
			
			request.setHeader("X-APPKEY", NamoNamoConstant.X_APPKEY);
			request.setHeader("X-DEVICE-ID", NamoNamoConstant.DEVICE_ID);
			

			httpResponse = httpclient.execute(request);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					httpResponse.getEntity().getContent()));
			String line = "";
			StringBuilder content = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				line = line.replace(":\"NULL\"", ":\"\"");
				line = line.replace(":\"null\"", ":\"\"");
				line = line.replace(":NULL", ":\"\"");
				line = line.replace(":null", ":\"\"");
				content.append(line);
			}
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			_mresponse = new Response(statusCode, statusCode == 200,
					content.toString());
		} catch (Exception x) {
			x.getMessage();
		}
	}

	public void onExecute() {
		if (_mrequest.getRequestType() == RequestType.GET)
			doGet();
		else
			doPost();
	}

	public boolean isConnected() {
		return true;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Response execute(Request req, DataPolicy policy) {
		_mrequest = req;
		_mpolicy = policy;
		T _hook = null;
		try {
			_hook = (T) ((Class) ((ParameterizedType) this.getClass()
					.getGenericSuperclass()).getActualTypeArguments()[0])
					.newInstance();
			_hook.onBeforeExecute(_mrequest);
		} catch (Exception e) {
		}

		String val;
		switch (_mpolicy) {
		case Optimized:
			// Here we need to know the cache expiration. For now, lets just
			// make this LiveThenCache
			break;
		case CacheOnly:
			val = _hook.load(req);
			if (val.length() > 0)
				_mresponse = new Response(200, true, val);
			else
				_mresponse = Response.NoDataResponse;
			break;
		case LiveOnly:
			onExecute();
			// _response; // TODO: We should not hold this, maybe
			break;
		case LiveThenCache:
		default:
			if (isConnected()) {
				onExecute();
			} else {
				val = _hook.load(req);
				if (val.length() > 0)
					_mresponse = new Response(200, true, val);
				else
					_mresponse = Response.NoDataResponse;
			}
			break;
		}
		// _hook.onAfterExecute(_mresponse);
		return _mresponse;
	}

}