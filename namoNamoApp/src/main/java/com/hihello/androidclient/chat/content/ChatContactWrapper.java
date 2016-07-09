package com.hihello.androidclient.chat.content;

import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.TextView;

import com.hihello.androidclient.chat.ChatItem;
import com.hihello.androidclient.chat.ChatWindow;
import com.hihello.androidclient.chat.ChatWindow2;
import com.hihello.app.R;
import com.hihello.app.common.BitmapUtil;

public class ChatContactWrapper extends IWrapper {
	private TextView mMessageView = null;
	private ChatWindow chatWindow;
	private ChatWindow2 chatWindow2;
	private ChatItem chatItem;
	private ImageView img_contact;

	public ChatContactWrapper(ChatItem chatItem, ChatWindow2 chatWindow2, boolean isSelected, boolean showDate) {
		this.chatWindow2 = chatWindow2;
		this.isSelected = isSelected;
		this.showDate = showDate;
		this.setChatItem(chatItem);
		if (chatItem.isFrom_me())
			mRowView = chatWindow.getLayoutInflater().inflate(
					R.layout.from_me_contact, null);
		else
			mRowView = chatWindow.getLayoutInflater().inflate(
					R.layout.from_you_contact, null);
		mRowView.setTag(this);
		populateFrom();
	}

	@Override
	public void setSelection(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public ChatContactWrapper(ChatItem chatItem, ChatWindow chatWindow,
			boolean isSelected, boolean showDate) {
		this.chatWindow = chatWindow;
		this.isSelected = isSelected;
		this.showDate = showDate;
		this.setChatItem(chatItem);
		if (chatItem.isFrom_me())
			mRowView = chatWindow.getLayoutInflater().inflate(
					R.layout.from_me_contact, null);
		else
			mRowView = chatWindow.getLayoutInflater().inflate(
					R.layout.from_you_contact, null);
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
		getTimeView().setText(getChatItem().getTime());
		if (chatItem.isFrom_me())
			setMessageStatus(getIconView(), chatItem, false);
		getMessageView().setText(chatItem.getContactName());
		if (showDate)
			getDateView().setText(chatItem.getHeaderDate());
		Bitmap bitmap = BitmapUtil.getBitMapFromString(chatItem.getPhotoData());
		if (bitmap != null)
			getImageView().setImageBitmap(bitmap);
	}

	TextView getMessageView() {
		if (mMessageView == null) {
			mMessageView = (TextView) mRowView.findViewById(R.id.txt_name);
		}
		return mMessageView;
	}

	public void setChatItem(ChatItem chatItem) {
		this.chatItem = chatItem;
	}

	public ChatItem getChatItem() {
		return chatItem;
	}

	ImageView getImageView() {
		if (img_contact == null) {
			img_contact = (ImageView) mRowView.findViewById(R.id.img_contact);
		}
		return img_contact;
	}

}
