package com.hihello.app.apicall;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import com.hihello.app.constant.HiHelloConstant;

public class UploadImageHttp {
	public static String updateProfilePic(String url, String imagePath,
			String contact_no, String user_id) throws IOException, ClientProtocolException {

		HttpResponse responseBody;
		HttpClient client = new DefaultHttpClient();
		HttpPost request = new HttpPost(url);
		request.setHeader("X-APPKEY", HiHelloConstant.X_APPKEY);
		request.setHeader("X-DEVICE-ID", HiHelloConstant.DEVICE_ID);
		// request.setHeader("X-AUTHKEY",
		// FifthTechSharedServices.getUser().getAuth_key());
		MultipartEntity entity = new MultipartEntity();
		File file = new File(imagePath);
		ContentBody encFile = new FileBody(file);
		entity.addPart("photo", encFile);
		entity.addPart("mobile_no", new StringBody(contact_no));
		entity.addPart("user_id", new StringBody(user_id));
		
		
		request.setEntity(entity);
		responseBody = client.execute(request);

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				responseBody.getEntity().getContent(), "UTF-8"));
		String sResponse;
		StringBuilder s = new StringBuilder();
		while ((sResponse = reader.readLine()) != null) {
			s = s.append(sResponse);
		}
		return s.toString();
	}

	public static String uploadMedia(String imagePath,
			String file_type, String userId) throws IOException,
			ClientProtocolException {
		HttpResponse responseBody;
		HttpClient client = new DefaultHttpClient();
		HttpPost request = new HttpPost(HiHelloConstant.UploadMediaUrl);
		request.setHeader("X-APPKEY", HiHelloConstant.X_APPKEY);
		request.setHeader("X-DEVICE-ID", HiHelloConstant.DEVICE_ID);
		// request.setHeader("X-AUTHKEY",
		// FifthTechSharedServices.getUser().getAuth_key());
		MultipartEntity entity = new MultipartEntity();
		File file = new File(imagePath);
		ContentBody encFile = new FileBody(file);
		entity.addPart("file", encFile);

		entity.addPart("userId", new StringBody(userId));
		entity.addPart("user_id", new StringBody(userId));

		entity.addPart("mediaType", new StringBody(file_type));

		request.setEntity(entity);
		responseBody = client.execute(request);

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				responseBody.getEntity().getContent(), "UTF-8"));
		String sResponse;
		StringBuilder s = new StringBuilder();
		while ((sResponse = reader.readLine()) != null) {
			s = s.append(sResponse);
		}
		return s.toString();
	}
}