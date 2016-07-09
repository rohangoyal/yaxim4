package com.hihello.app.service;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.hihello.androidclient.data.ChatProvider;
import com.hihello.androidclient.data.ChatProvider.ChatConstants;
import com.hihello.androidclient.service.XMPPService;
import com.hihello.app.apicall.UploadImageHttp;

public class UploadServcie extends IntentService {

	public static final String FILE_PATH = "FILE_PATH";
	public static final String CHAT_ID = "CHAT_ID";
	public static final String USER_ID = "USER_ID";
	public static final String FILE_TYPE = "FILE_TYPE";
	private String filePath;
	private String fileType;
	private String chatId;
	private String userId;

	public UploadServcie() {
		super("");
		// TODO Auto-generated constructor stub
	}

	public void updateChat(String chatId, ContentValues values) {
		Uri rowuri = Uri.parse("content://" + ChatProvider.AUTHORITY + "/"
				+ ChatProvider.TABLE_NAME + "/" + chatId);
		getContentResolver().update(rowuri, values, null, null);
	}

	public String getChatMessage(String chatId) {
		Uri rowuri = Uri.parse("content://" + ChatProvider.AUTHORITY + "/"
				+ ChatProvider.TABLE_NAME + "/" + chatId);

		Cursor cursor = getContentResolver().query(rowuri, null, null, null,
				null);
		String message = null;
		if (cursor.moveToFirst()) {
			message = cursor.getString(cursor
					.getColumnIndex(ChatConstants.MESSAGE));
		}
		cursor.close();
		return message;
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle bnd = intent.getExtras();
		filePath = bnd.getString(FILE_PATH);
		fileType = bnd.getString(FILE_TYPE);

		chatId = bnd.getString(CHAT_ID);
		userId = bnd.getString(USER_ID);
		try {
			String urls = UploadImageHttp.uploadMedia(filePath, fileType,
					userId);
			JSONArray jsonArr = new JSONArray(urls);
			String url = jsonArr.getJSONObject(0).getString("url");

			ContentValues values = new ContentValues();
			values.put(ChatConstants.DELIVERY_STATUS, ChatConstants.DS_NEW);
			String message = getChatMessage(chatId);

			JSONObject json = new JSONObject(message);
			json.getJSONObject("message").put("url", url);
			message = json.toString();
			values.put(ChatConstants.MESSAGE, message);
			updateChat(chatId, values);

			Intent xmppServiceIntent = new Intent(getApplicationContext(),
					XMPPService.class);
			xmppServiceIntent.setAction("sync");
			startService(xmppServiceIntent);

		} catch (Exception e) {
			ContentValues values = new ContentValues();
			values.put(ChatConstants.DELIVERY_STATUS, ChatConstants.DS_RETRY);
			updateChat(chatId, values);
		}
	}

}
