package com.hihello.app.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hihello.androidclient.data.ChatProvider.ChatConstants;
import com.hihello.app.R;
import com.hihello.app.activity.CircularImageView;
import com.hihello.app.activity.UIApplication;
import com.hihello.app.constant.HiHelloSharedPrefrence;
import com.hihello.app.db.DailyMessage;
import com.hihello.app.db.DailyMessageDBService;
import com.hihello.app.db.HiHelloContact;
import com.hihello.app.db.hiHelloContactDBService;
import com.hihello.app.db.RecentChat;
import com.nostra13.universalimageloader.core.ImageLoader;

public class RecentChatAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private ArrayList<RecentChat> recentChats;
	private ArrayList<RecentChat> arraylist;
	private boolean showInfo;
	private boolean showDailyMessage = false;
	private Context ctx;

	public RecentChatAdapter(LayoutInflater inflater,
			ArrayList<RecentChat> recentChats, Context ctx, boolean showInfo) {
		this.showInfo = showInfo;
		this.inflater = inflater;

		this.ctx = ctx;
		this.recentChats = recentChats;
		arraylist=new ArrayList<RecentChat>();
		arraylist.addAll(recentChats);


	}

	public RecentChatAdapter(LayoutInflater inflater,
			ArrayList<RecentChat> recentChats, Context ctx, boolean showInfo,
			boolean showDailyMessage) {
		this.showDailyMessage = showDailyMessage;
		this.showInfo = showInfo;
		this.inflater = inflater;
		this.recentChats = recentChats;
		this.ctx = ctx;

		arraylist=new ArrayList<RecentChat>();
		arraylist.addAll(recentChats);

	}

	@Override
	public int getCount() {

//		Log.e("get", recentChats.size() + "");
		if (showDailyMessage)
			return recentChats.size() + 1;
		else
			return recentChats.size();


	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		if (showDailyMessage)
			if (arg0 > 0)
				return getRecentChatContactCell(arg0 - 1);
			else
				return getDailyMessageCell();

		else
			return getRecentChatContactCell(arg0);
	}

	private View getDailyMessageCell() {
		View view = inflater.inflate(R.layout.chat_cell, null);
		TextView txt_name = (TextView) view.findViewById(R.id.txt_name);
		ImageView img_message_status = (ImageView) view
				.findViewById(R.id.img_message_status);
		ImageView img_message_icon = (ImageView) view
				.findViewById(R.id.img_message_icon);
		CircularImageView profileImage = (CircularImageView) view
				.findViewById(R.id.profileImage);

		TextView txt_chat = (TextView) view.findViewById(R.id.txt_message);

		txt_name.setText("Hi Hello Daily");
		img_message_icon.setVisibility(View.GONE);
		img_message_status.setImageResource(R.drawable.msg_status_read);
		profileImage.setBackgroundDrawable(null);
//		profileImage.setImageResource(R.drawable.raj);

		long time = 0;
		DailyMessage message = null;
		ArrayList<DailyMessage> messages = DailyMessageDBService
				.getMessageAfterDate(ctx,
						HiHelloSharedPrefrence.getDailyMessageLastTime(ctx));
		ArrayList<DailyMessage> allMessages = DailyMessageDBService
				.getAllMessage(ctx);

		for (DailyMessage _message : allMessages) {
			if (_message.getDate() > time) {
				time = _message.getDate();
				message = _message;
			}
		}

		if (message != null) {
			txt_chat.setText(message.getMessage());
		} else {
			txt_chat.setText("No Message From Hi Hello");
		}

		if (messages != null && messages.size() > 0) {
			TextView txt_unread_count = (TextView) view
					.findViewById(R.id.txt_unread_count);
			txt_unread_count.setVisibility(View.VISIBLE);
			txt_unread_count.setText(messages.size() + "");
		}
		return view;
	}

	public View getRecentChatContactCell(int arg0) {
		View view = inflater.inflate(R.layout.chat_cell, null);
		RecentChat chat = recentChats.get(arg0);
		TextView txt_name = (TextView) view.findViewById(R.id.txt_name);
		ImageView img_message_status = (ImageView) view
				.findViewById(R.id.img_message_status);
		ImageView img_message_icon = (ImageView) view
				.findViewById(R.id.img_message_icon);
		CircularImageView profileImage = (CircularImageView) view
				.findViewById(R.id.profileImage);
		if (chat.getDisplayName() == null || chat.getDisplayName().length() < 1) {
			txt_name.setText(chat.getMobileNo());
		} else {
			txt_name.setText(chat.getDisplayName());
		}
		if (chat.getStatus() != null
				&& chat.getStatus().equalsIgnoreCase("online")) {
			txt_name.setTextColor(Color.parseColor("#008000"));
//			profileImage.setBackgroundResource(R.drawable.round_green);
			// view.findViewById(R.id.img_online_status).setVisibility(
			// View.VISIBLE);
		} else {
			profileImage.setBackgroundDrawable(null);
			// view.findViewById(R.id.img_online_status).setVisibility(View.GONE);
		}
		TextView txt_chat = (TextView) view.findViewById(R.id.txt_message);
		TextView txt_date = (TextView) view.findViewById(R.id.txt_date);
		Date chat_date = chat.getDate();
		Date today = new Date();
		String dateString = "";
		if (chat_date.getYear() == today.getYear()
				&& chat_date.getMonth() == today.getMonth()
				&& chat_date.getDate() == today.getDate()) {
			SimpleDateFormat readFormat = new SimpleDateFormat("hh:mm aa");
			dateString = readFormat.format(chat_date);
		} else if (chat_date.getYear() == today.getYear()
				&& chat_date.getMonth() == today.getMonth()
				&& chat_date.getDate() == today.getDate() - 1) {
			dateString = "Yesterday";
		} else {
			SimpleDateFormat readFormat = new SimpleDateFormat("dd/MM/yyyy");
			dateString = readFormat.format(chat_date);
		}
		if (showInfo)
			txt_date.setText(dateString);
		TextView txt_unread_count = (TextView) view
				.findViewById(R.id.txt_unread_count);
		img_message_status.setVisibility(View.VISIBLE);

		if (chat.getUnReadCount() > 0) {
			if (chat.isFromMe()) {
				setMessageStatus(img_message_status, chat.getDeliveryStatus());
			} else {
				if (chat.getMessage().length() < 1)
					img_message_status.setVisibility(View.GONE);
				else
					img_message_status
							.setImageResource(R.drawable.msg_status_unread);
			}
			txt_chat.setTypeface(null, Typeface.BOLD);
			if (showInfo) {
				txt_unread_count.setVisibility(View.VISIBLE);
				txt_unread_count.setText(chat.getUnReadCount() + "");
			}
		} else {
			if (chat.isFromMe()) {
				setMessageStatus(img_message_status, chat.getDeliveryStatus());
			} else {
				if (chat.getMessage().length() < 1)
					img_message_status.setVisibility(View.GONE);
				else

					img_message_status
							.setImageResource(R.drawable.msg_status_read);
			}
			txt_unread_count.setVisibility(View.GONE);
			txt_chat.setTypeface(null, Typeface.NORMAL);
		}

		try {
			switch (chat.getChatType()) {
			case RecentChat.CHAT_TYPE_AUDIO:
				txt_chat.setText("Audio");
				img_message_icon
						.setImageResource(R.drawable.message_audio_icon);
				break;
			case RecentChat.CHAT_TYPE_IMAGE:
				img_message_icon
						.setImageResource(R.drawable.message_camera_icon);
				txt_chat.setText("Image");
				break;
			case RecentChat.CHAT_TYPE_LOCATION:
				txt_chat.setText("Location");
				img_message_icon
						.setImageResource(R.drawable.message_location_icon);
				break;
			case RecentChat.CHAT_TYPE_CONTACT:
				txt_chat.setText("Contact");
				img_message_icon
						.setImageResource(R.drawable.message_contact_icon);
				break;
			case RecentChat.CHAT_TYPE_MEDIA:
				txt_chat.setText("Media");
				img_message_icon
						.setImageResource(R.drawable.message_camera_icon);
				break;
			case RecentChat.CHAT_TYPE_TEXT:
				txt_chat.setText(chat.getChatText());
				img_message_icon.setImageBitmap(null);
				break;
			case RecentChat.CHAT_TYPE_VIDEO:
				txt_chat.setText("Video");
				img_message_icon
						.setImageResource(R.drawable.message_video_icon);
				break;

			default:
				break;
			}
		} catch (Exception x) {
		}

		HiHelloContact contact = hiHelloContactDBService.fetchContactByJID(
				ctx, chat.getUserJID());
		if (contact == null || contact.getPic_url().toString().length() > 0) {
			ImageLoader.getInstance().displayImage(chat.getProfilePic(),
					profileImage, UIApplication.diRoundOptions);
		}
		else
		{
//			String secondLetter = String.valueOf(contact.getDisplayName().charAt(0));
//			ColorGenerator generator1 = ColorGenerator.MATERIAL; // or use DEFAULT
//			int color1 = generator1.getRandomColor();
//			TextDrawable drawable1 = TextDrawable.builder()
//					.buildRound(secondLetter, color1);
			profileImage.setImageResource(R.drawable.defaultuser);
		}
		return view;
	}


	public void filter(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());
		recentChats.clear();
		if (charText.length() == 0) {
			recentChats.addAll(arraylist);
		}
		else
		{
			for (RecentChat wp : arraylist)
			{
				if (wp.getDisplayName().toLowerCase(Locale.getDefault()).contains(charText))
				{
					recentChats.add(wp);
				}
			}
		}
		notifyDataSetChanged();
	}


	public void setMessageStatus(ImageView iconView, int deliveryStatus) {
		switch (deliveryStatus) {
		case ChatConstants.DS_NEW:
			iconView.setImageResource(R.drawable.message_queue);
			break;
		case ChatConstants.DS_SENT:
			iconView.setImageResource(R.drawable.message_sent);
			break;
		case ChatConstants.DS_DELIVER:
			iconView.setImageResource(R.drawable.message_deliver);
			break;
		case ChatConstants.DS_SEEN:
			iconView.setImageResource(R.drawable.message_seen);
			break;
		case ChatConstants.DS_FAILED:
			iconView.setImageResource(R.drawable.message_failed);
			break;
		default:
			iconView.setImageResource(R.drawable.message_queue);
		}
	}

	public void setRecentChats(ArrayList<RecentChat> recentChats) {
		this.recentChats = recentChats;
	}
}
