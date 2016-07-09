package com.hihello.androidclient.chat.content;

import com.hihello.androidclient.chat.ChatItem;
import com.hihello.androidclient.chat.ChatWindow;
import com.hihello.androidclient.chat.ChatWindow2;
import com.hihello.app.R;

public class ChatTextWrapper extends IWrapper {
	private ChatWindow chatWindow;
	private ChatWindow2 chatWindow2;
	// private String content;
	private ChatItem chatItem;

	public ChatTextWrapper(ChatItem chatItem, ChatWindow2 chatWindow2, boolean isSelected, boolean showDate) {
		this.chatWindow2 = chatWindow2;
		this.setSelected(isSelected);
		this.showDate = showDate;
		this.setChatItem(chatItem);
		if (chatItem.isFrom_me())

			mRowView = chatWindow.getLayoutInflater().inflate(
					R.layout.from_me_chat, null);
		else
			mRowView = chatWindow.getLayoutInflater().inflate(
					R.layout.from_you_chat, null);
		mRowView.setTag(this);
		try {

		} catch (Exception x) {

		}
		populateFrom();
	}

	@Override
	public void setSelection(boolean isSelected) {
		this.setSelected(isSelected);
	}

	public void setChatItem(ChatItem chatItem) {
		this.chatItem = chatItem;
	}

	public ChatItem getChatItem() {
		return chatItem;
	}

	public ChatTextWrapper(ChatItem chatItem, ChatWindow chatWindow,
			boolean isSelected, boolean showDate) {
		this.chatWindow = chatWindow;
		this.setSelected(isSelected);
		this.showDate = showDate;
		this.setChatItem(chatItem);
		if (chatItem.isFrom_me())
			mRowView = chatWindow.getLayoutInflater().inflate(
					R.layout.from_me_chat, null);
		else
			mRowView = chatWindow.getLayoutInflater().inflate(
					R.layout.from_you_chat, null);
		mRowView.setTag(this);
		try {

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
		// Log.i(TAG, "populateFrom(" + from_me + ", " + from + ", " +
		// message + ")");
		getTimeView().setText(getChatItem().getTime());
		if (chatItem.isFrom_me())
			setMessageStatus(getIconView(), chatItem, false);
		getMessageView().setText(getChatItem().getChatText());
		if (showDate)
			getDateView().setText(chatItem.getHeaderDate());
	}

}
