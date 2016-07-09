package com.namonamo.androidclient.chat.content;

import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.namonamo.androidclient.chat.ChatItem;
import com.namonamo.androidclient.chat.ChatWindow;
import com.namonamo.androidclient.data.ChatProvider.ChatConstants;
import com.namonamo.app.R;
import com.namonamo.app.constant.NamoNamoSharedPrefrence;
import com.namonamo.app.service.DownloadServcie;
import com.namonamo.app.service.UploadServcie;

public class ChatAudioWrapper extends IWrapper {
	private ChatWindow chatWindow;
	private String content;
	private String fileName;
	private TextView mFileNameView;
	private TextView mDownloadView;
	private String localPath;
	private ChatItem chatItem;
	private LinearLayout mUpDownView;
	private RelativeLayout mProgressBarView;

	@Override
	public void setSelection(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public ChatAudioWrapper(ChatItem chatItem, ChatWindow chatWindow,
			boolean isSelected, boolean showDate) {
		this.chatWindow = chatWindow;
		this.isSelected = isSelected;
		this.showDate = showDate;
		this.setChatItem(chatItem);
		if (chatItem.isFrom_me())
			mRowView = chatWindow.getLayoutInflater().inflate(
					R.layout.from_me_audio, null);
		else
			mRowView = chatWindow.getLayoutInflater().inflate(
					R.layout.from_you_audio, null);
		mRowView.setTag(this);
		try {
			content = chatItem.getMessage();
			JSONObject json = new JSONObject(content).getJSONObject("message");
			this.fileName = json.optString("FileName");
			if (chatItem.isFrom_me()) {
				this.localPath = json.optString("LocalPath");
			}

		} catch (Exception x) {

		}

		populateFrom();
	}

	public void populateFrom() {
		if (isSelected()) {
			mRowView.findViewById(R.id.layout_root).setBackgroundResource(
					R.drawable.list_item_selection);
		} else {
			mRowView.findViewById(R.id.layout_root).setBackgroundColor(
					0x00000000);
		}
		getTimeView().setText(getChatItem().getTime());
		getDownloadText().setText("Play");
		if (!chatItem.isFrom_me()) {
			String localPath = NamoNamoSharedPrefrence.getLocalPath(chatWindow,
					chatItem.getUrl());
			if (localPath == null || localPath.length() < 1) {
				getDownloadText().setText("Download");
				getUpDownView().setVisibility(View.VISIBLE);
				((ImageView) mRowView.findViewById(R.id.img_up_down_load))
						.setImageResource(R.drawable.download);
				getUpDownView().setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						startUpDownLoad();
					}
				});
			}
		}

		if (chatItem.isFrom_me()) {
			setMessageStatus(getIconView(), chatItem, false);
		}

		switch (getChatItem().getDelivery_status()) {
		case ChatConstants.DS_RETRY:
			getUpDownView().setVisibility(View.VISIBLE);
			getUpDownView().setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					startUpDownLoad();
				}
			});
			getProgressBarView().setVisibility(View.GONE);
			break;
		case ChatConstants.DS_PENDING:
			startUpDownLoad();
			break;
		case ChatConstants.DS_UPDOWNLOADING:
			getProgressBarView().setVisibility(View.VISIBLE);
			getProgressBarView().setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					ContentValues values = new ContentValues();
					values.put(ChatConstants.DELIVERY_STATUS,
							ChatConstants.DS_RETRY);
					chatWindow.updateChat(chatItem.getId(), values);
				}
			});
			getUpDownView().setVisibility(View.GONE);
			break;

		}
		getFileNameText().setText(fileName);
		if (showDate)
			getDateView().setText(chatItem.getHeaderDate());
	}

	protected void startUpDownLoad() {
		if (getChatItem().isFrom_me()) {
			startUploadFile();
		} else {
			startDownloadFile();
		}
	}

	public void startDownloadFile() {
		getProgressBarView().setVisibility(View.VISIBLE);
		getUpDownView().setVisibility(View.GONE);
		ContentValues values = new ContentValues();
		values.put(ChatConstants.DELIVERY_STATUS,
				ChatConstants.DS_UPDOWNLOADING);
		chatWindow.updateChat(chatItem.getId(), values);

		Intent service = new Intent(chatWindow, DownloadServcie.class);
		Bundle bnd = new Bundle();
		bnd.putString(DownloadServcie.CHAT_ID, chatItem.getId() + "");
		bnd.putString(DownloadServcie.USER_ID,
				NamoNamoSharedPrefrence.getUserId(chatWindow));
		bnd.putString(DownloadServcie.FILE_TYPE, chatItem.getChatTypeStr());
		bnd.putString(DownloadServcie.FILE_URL, chatItem.getUrl());
		service.putExtras(bnd);
		chatWindow.startService(service);
	}

	public void startUploadFile() {
		getProgressBarView().setVisibility(View.VISIBLE);
		getUpDownView().setVisibility(View.GONE);
		ContentValues values = new ContentValues();
		values.put(ChatConstants.DELIVERY_STATUS,
				ChatConstants.DS_UPDOWNLOADING);
		chatWindow.updateChat(chatItem.getId(), values);

		Intent service = new Intent(chatWindow, UploadServcie.class);
		Bundle bnd = new Bundle();
		bnd.putString(UploadServcie.CHAT_ID, chatItem.getId() + "");
		bnd.putString(UploadServcie.USER_ID,
				NamoNamoSharedPrefrence.getUserId(chatWindow));
		bnd.putString(UploadServcie.FILE_TYPE, chatItem.getChatTypeStr());
		bnd.putString(UploadServcie.FILE_PATH, chatItem.getLocalUrl());
		service.putExtras(bnd);
		chatWindow.startService(service);
	}

	private LinearLayout getUpDownView() {
		if (mUpDownView == null) {
			mUpDownView = (LinearLayout) mRowView
					.findViewById(R.id.up_down_view);
		}
		return mUpDownView;
	}

	private RelativeLayout getProgressBarView() {
		if (mProgressBarView == null) {
			mProgressBarView = (RelativeLayout) mRowView
					.findViewById(R.id.progressBarView);
		}
		return mProgressBarView;
	}

	TextView getFileNameText() {
		if (mFileNameView == null) {
			mFileNameView = (TextView) mRowView
					.findViewById(R.id.txt_file_name);
		}
		return mFileNameView;
	}

	TextView getDownloadText() {
		if (mDownloadView == null) {
			mDownloadView = (TextView) mRowView.findViewById(R.id.txt_name);
		}
		return mDownloadView;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}

	public String getLocalPath() {
		return localPath;
	}

	public void setChatItem(ChatItem chatItem) {
		this.chatItem = chatItem;
	}

	public ChatItem getChatItem() {
		return chatItem;
	}

}
