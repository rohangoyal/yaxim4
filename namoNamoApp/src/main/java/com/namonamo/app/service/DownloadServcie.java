package com.namonamo.app.service;

import org.json.JSONObject;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.namonamo.androidclient.data.ChatProvider;
import com.namonamo.androidclient.data.ChatProvider.ChatConstants;
import com.namonamo.app.common.NamoNamoDownloadManager;

public class DownloadServcie extends IntentService {

	public static final String FILE_URL = "FILE_URL";

	public static final String CHAT_ID = "CHAT_ID";
	public static final String USER_ID = "USER_ID";
	public static final String FILE_TYPE = "FILE_TYPE";
	private String fileUrl;
	private String fileType;
	private String chatId;
	private String userId;

	public DownloadServcie() {
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
		fileUrl = bnd.getString(FILE_URL);
		fileType = bnd.getString(FILE_TYPE);

		chatId = bnd.getString(CHAT_ID);
		userId = bnd.getString(USER_ID);
		try {

			String localPath = "";
			if (fileType.equalsIgnoreCase("IMAGE"))
				localPath = NamoNamoDownloadManager.downloadImageFile(
						getApplicationContext(), fileUrl);
			else if (fileType.equalsIgnoreCase("VIDEO"))
				localPath = NamoNamoDownloadManager.downloadVideoFile(
						getApplicationContext(), fileUrl);
			else if(fileType.equalsIgnoreCase("AUDIO"))
				localPath = NamoNamoDownloadManager.downloadAudioFile(
						getApplicationContext(), fileUrl);
			ContentValues values = new ContentValues();
			values.put(ChatConstants.DELIVERY_STATUS, ChatConstants.DS_SENT);

			String message = getChatMessage(chatId);
			JSONObject json = new JSONObject(message);
			json.getJSONObject("message").put("LocalPath", localPath);
			message = json.toString();
			values.put(ChatConstants.MESSAGE, message);
			updateChat(chatId, values);
		} catch (Exception e) {
		}
	}

}
