package com.namonamo.androidclient.chat.content;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.namonamo.androidclient.chat.ChatItem;
import com.namonamo.androidclient.data.ChatProvider.ChatConstants;
import com.namonamo.app.R;

public abstract class IWrapper {
	public abstract void setSelection(boolean selection);

	public View mRowView;
	private TextView mTimeView = null;
	private TextView mMessageView = null;
	private ImageView mIconView = null;
	protected boolean showDate;
	private TextView mDateView;
	protected boolean isSelected = false;

	public abstract ChatItem getChatItem();

	public abstract void populateFrom();

	public void setMessageStatus(ImageView iconView, ChatItem chatItem,
			boolean onMedia) {
		switch (chatItem.getDelivery_status()) {
		case ChatConstants.DS_NEW:
			iconView.setImageResource(R.drawable.message_queue);
			break;
		case ChatConstants.DS_SENT:
			if (onMedia)
				iconView.setImageResource(R.drawable.message_sent_on_media);
			else
				iconView.setImageResource(R.drawable.message_sent);
			break;
		case ChatConstants.DS_DELIVER:
			if (onMedia)
				iconView.setImageResource(R.drawable.message_deliver_on_media);
			else
				iconView.setImageResource(R.drawable.message_deliver);
			break;
		case ChatConstants.DS_SEEN:
			iconView.setImageResource(R.drawable.message_seen);
			break;
		case ChatConstants.DS_FAILED:
			iconView.setImageResource(R.drawable.message_failed);
			break;
		}

	}

	public View getRow() {
		return mRowView;
	}

	TextView getTimeView() {
		if (mTimeView == null) {
			mTimeView = (TextView) mRowView.findViewById(R.id.chat_time);
		}
		return mTimeView;
	}

	TextView getDateView() {
		if (mDateView == null) {
			mRowView.findViewById(R.id.layout_header).setVisibility(
					View.VISIBLE);
			mDateView = (TextView) mRowView.findViewById(R.id.txt_date);
		}
		return mDateView;
	}

	TextView getMessageView() {
		if (mMessageView == null) {
			mMessageView = (TextView) mRowView.findViewById(R.id.chat_message);
		}
		return mMessageView;
	}

	ImageView getIconView() {
		if (mIconView == null) {
			mIconView = (ImageView) mRowView.findViewById(R.id.iconView);
		}
		return mIconView;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public boolean isSelected() {
		return isSelected;
	}

}
