package com.namonamo.androidclient.chat.content;

import org.json.JSONObject;

import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.TextView;

import com.namonamo.androidclient.chat.ChatItem;
import com.namonamo.androidclient.chat.ChatWindow;
import com.namonamo.app.R;
import com.namonamo.app.activity.UIApplication;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ChatLocationWrapper extends IWrapper {
	private ChatWindow chatWindow;
	private String place_map;
	private String content;
	private String place_name;
	private TextView txt_place_name;
	ImageView img_place;
	private ChatItem chatItem;

	@Override
	public void setSelection(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public ChatLocationWrapper(ChatItem chatItem, ChatWindow chatWindow,
			boolean isSelected, boolean showDate) {
		this.chatWindow = chatWindow;
		this.isSelected = isSelected;
		this.showDate = showDate;
		this.setChatItem(chatItem);
		if (chatItem.isFrom_me())
			mRowView = chatWindow.getLayoutInflater().inflate(
					R.layout.from_me_location, null);
		else
			mRowView = chatWindow.getLayoutInflater().inflate(
					R.layout.from_you_location, null);
		mRowView.setTag(this);
		try {
			content = chatItem.getMessage();
			JSONObject json = new JSONObject(content).getJSONObject("message");
			this.place_map = json.optString("place_map");
			this.place_name = json.optString("place_name");

		} catch (Exception x) {

		}

		populateFrom();
	}

	public void populateFrom() {
		// Log.i(TAG, "populateFrom(" + from_me + ", " + from + ", " +
		// message + ")");
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

		getTimeView().setTextSize(TypedValue.COMPLEX_UNIT_SP,
				chatWindow.mChatFontSize * 2 / 3);
		getPlaceNameText().setText(place_name);
		ImageLoader.getInstance().displayImage(place_map, getPlaceImage(),
				UIApplication.diOptions);
		if (showDate)
			getDateView().setText(chatItem.getHeaderDate());

	}

	TextView getPlaceNameText() {
		if (txt_place_name == null) {
			txt_place_name = (TextView) mRowView
					.findViewById(R.id.txt_place_name);
		}
		return txt_place_name;
	}


	ImageView getPlaceImage() {
		if (img_place == null) {
			img_place = (ImageView) mRowView.findViewById(R.id.img_place);
		}
		return img_place;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void setChatItem(ChatItem chatItem) {
		this.chatItem = chatItem;
	}

	public ChatItem getChatItem() {
		return chatItem;
	}

}
