package com.hihello.androidclient.chat.content;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hihello.androidclient.chat.ChatItem;
import com.hihello.androidclient.chat.ChatWindow;
import com.hihello.androidclient.chat.ChatWindow2;
import com.hihello.androidclient.data.ChatProvider.ChatConstants;
import com.hihello.app.R;
import com.hihello.app.common.BitmapUtil;
import com.hihello.app.constant.HiHelloSharedPrefrence;
import com.hihello.app.service.DownloadServcie;
import com.hihello.app.service.UploadServcie;

public class ChatVideoWrapper extends IWrapper {
	private ChatWindow chatWindow;
	private ChatWindow2 chatWindow2;
	private ImageView mImageView;
	private ChatItem chatItem;
	private LinearLayout mUpDownView;
	private RelativeLayout mProgressBarView;
	private TextView mDurationView;

	public ChatVideoWrapper(ChatItem chatItem, ChatWindow2 chatWindow2, boolean isSelected, boolean showDate) {
		this.chatWindow2 = chatWindow2;
		this.isSelected = isSelected;
		this.showDate = showDate;
		this.setChatItem(chatItem);
		if (chatItem.isFrom_me())
			mRowView = chatWindow.getLayoutInflater().inflate(
					R.layout.from_me_video, null);
		else
			mRowView = chatWindow.getLayoutInflater().inflate(
					R.layout.from_you_video, null);
		mRowView.setTag(this);
		populateFrom();
	}

	@Override
	public void setSelection(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public ChatVideoWrapper(ChatItem chatItem, ChatWindow chatWindow,
			boolean isSelected, boolean showDate) {
		this.chatWindow = chatWindow;
		this.isSelected = isSelected;
		this.showDate = showDate;
		this.setChatItem(chatItem);
		if (chatItem.isFrom_me())
			mRowView = chatWindow.getLayoutInflater().inflate(
					R.layout.from_me_video, null);
		else
			mRowView = chatWindow.getLayoutInflater().inflate(
					R.layout.from_you_video, null);
		mRowView.setTag(this);
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
		// Log.i(TAG, "populateFrom(" + from_me + ", " + from + ", " +
		// message + ")");
		getTimeView().setText(getChatItem().getTime());

		if (chatItem.isFrom_me())
			setMessageStatus(getIconView(), chatItem, true);

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
		if (chatItem.isFrom_me()) {
			((TextView) mRowView.findViewById(R.id.txt_size)).setText("Retry");
			((ImageView) mRowView.findViewById(R.id.img_up_down_load))
					.setImageResource(R.drawable.upload);
			getImageView().setImageBitmap(
					BitmapUtil.getBitMapFromString(chatItem.getPhotoData()));

		} else {
			String localPath = HiHelloSharedPrefrence.getLocalPath(chatWindow,
					chatItem.getUrl());
			if (chatItem.getDelivery_status() == ChatConstants.DS_SENT) {
				getUpDownView().setVisibility(View.VISIBLE);
				getProgressBarView().setVisibility(View.GONE);
			}
			if (localPath != null && localPath.length() > 0) {
				getUpDownView().setVisibility(View.GONE);
				getProgressBarView().setVisibility(View.GONE);
			}
			((TextView) mRowView.findViewById(R.id.txt_size)).setText(chatItem
					.getFileSize());
			((ImageView) mRowView.findViewById(R.id.img_up_down_load))
					.setImageResource(R.drawable.download);
			getUpDownView().setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					startUpDownLoad();
				}
			});
			getImageView().setImageBitmap(
					BitmapUtil.getBitMapFromString(chatItem.getPhotoData()));

		}
		getTimeView().setTextSize(TypedValue.COMPLEX_UNIT_SP,
				chatWindow.mChatFontSize * 2 / 3);
		getDurationView().setText(chatItem.getDuration());
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

	private void startDownloadFile() {
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
				HiHelloSharedPrefrence.getUserId(chatWindow));
		bnd.putString(DownloadServcie.FILE_TYPE, chatItem.getChatTypeStr());
		bnd.putString(DownloadServcie.FILE_URL, chatItem.getUrl());
		service.putExtras(bnd);
		chatWindow.startService(service);
	}

	private void startUploadFile() {
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
				HiHelloSharedPrefrence.getUserId(chatWindow));
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

	TextView getDurationView() {
		if (mDurationView == null) {
			mDurationView = (TextView) mRowView.findViewById(R.id.duration);
		}
		return mDurationView;
	}

	ImageView getImageView() {
		if (mImageView == null) {
			mImageView = (ImageView) mRowView.findViewById(R.id.imageView);
		}
		return mImageView;
	}

	public void setChatItem(ChatItem chatItem) {
		this.chatItem = chatItem;
	}

	public ChatItem getChatItem() {
		return chatItem;
	}

}
