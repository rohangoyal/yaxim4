package com.namonamo.androidclient.chat;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

import android.database.Cursor;

import com.namonamo.androidclient.data.ChatProvider;
import com.namonamo.androidclient.data.ChatProvider.ChatConstants;

public class ChatItem implements Serializable {

	public static int CHAT_TYPE_TEXT = 0;
	public static int CHAT_TYPE_VIDEO = 1;
	public static int CHAT_TYPE_CONTACT = 2;
	public static int CHAT_TYPE_AUDIO = 3;
	public static int CHAT_TYPE_LOCATION = 4;
	public static int CHAT_TYPE_IMAGE = 5;

	private int _id;
	private String time;
	private String message;
	private boolean from_me;
	private String jid;
	private int delivery_status;
	private long dateMilliseconds;
	private int chatType;
	private String chatTypeStr;
	private String headerDate;

	private String getTimeString(long milliSeconds) {
		SimpleDateFormat dateFormater = new SimpleDateFormat("hh:mm aa");
		Date date = new Date(milliSeconds);
		return dateFormater.format(date);
	}

	private String getDateString(long milliSeconds) {
		SimpleDateFormat dateFormater = new SimpleDateFormat("dd MMM yyyy");
		Date date = new Date(milliSeconds);
		return dateFormater.format(date);
	}

	private String getDurationString(String duration) {
		int _duration = Integer.parseInt(duration) / 1000;
		int hour = _duration / 3600;
		int minute = _duration / 60;
		int second = _duration % 60;
		DecimalFormat decim = new DecimalFormat("00");
		return decim.format(hour) + ":" + decim.format(minute) + ":"
				+ decim.format(second);
	}

	public ChatItem(Cursor cursor) {

		setDateMilliseconds(cursor.getLong(cursor
				.getColumnIndex(ChatProvider.ChatConstants.DATE)));
		setId(cursor.getInt(cursor
				.getColumnIndex(ChatProvider.ChatConstants._ID)));
		setTime(getTimeString(getDateMilliseconds()));
		setHeaderDate(getDateString(getDateMilliseconds()));
		setMessage(cursor.getString(cursor
				.getColumnIndex(ChatProvider.ChatConstants.MESSAGE)));
		setFrom_me((cursor.getInt(cursor
				.getColumnIndex(ChatProvider.ChatConstants.DIRECTION)) == ChatConstants.OUTGOING));
		setJid(cursor.getString(cursor
				.getColumnIndex(ChatProvider.ChatConstants.JID)));
		setDelivery_status(cursor.getInt(cursor
				.getColumnIndex(ChatProvider.ChatConstants.DELIVERY_STATUS)));

	}

	public ChatItem() {
		// TODO Auto-generated constructor stub
	}

	private void setFrom_me(boolean from_me) {
		this.from_me = from_me;
	}

	public boolean isFrom_me() {
		return from_me;
	}

	private void setTime(String time) {
		this.time = time;
	}

	public String getTime() {
		return time;
	}

	private void setHeaderDate(String date) {
		this.headerDate = date;
	}

	public String getHeaderDate() {
		return headerDate;
	}

	public void setMessage(String message) {
		this.message = message;
		try {
			JSONObject json = new JSONObject(getMessage());
			setChatTypeStr(json.getString("type"));
			if (getChatTypeStr().equalsIgnoreCase("TEXT"))
				chatType = CHAT_TYPE_TEXT;
			else if (getChatTypeStr().equalsIgnoreCase("AUDIO"))
				chatType = CHAT_TYPE_AUDIO;
			else if (getChatTypeStr().equalsIgnoreCase("VIDEO"))
				chatType = CHAT_TYPE_VIDEO;
			else if (getChatTypeStr().equalsIgnoreCase("CONTACT"))
				chatType = CHAT_TYPE_CONTACT;
			else if (getChatTypeStr().equalsIgnoreCase("LOCATION"))
				chatType = CHAT_TYPE_LOCATION;
			else if (getChatTypeStr().equalsIgnoreCase("IMAGE"))
				chatType = CHAT_TYPE_IMAGE;

		} catch (Exception x) {

		}
	}

	public String getMessage() {
		return message;
	}

	private void setJid(String jid) {
		this.jid = jid;
	}

	public String getJid() {
		return jid;
	}

	private void setDelivery_status(int delivery_status) {
		this.delivery_status = delivery_status;
	}

	public int getDelivery_status() {
		return delivery_status;
	}

	private void setDateMilliseconds(long dateMilliseconds) {
		this.dateMilliseconds = dateMilliseconds;
	}

	public long getDateMilliseconds() {
		return dateMilliseconds;
	}

	public int getChatType() {
		return chatType;
	}

	public String getChatText() {
		String chatText = "";
		try {
			JSONObject json = new JSONObject(message);
			chatText = json.optString("message");
		} catch (Exception x) {
		}
		return chatText;
	}

	public String getUrl() {
		String url = "";
		try {
			JSONObject json = new JSONObject(message);
			url = json.getJSONObject("message").optString("url");
		} catch (Exception x) {
		}
		return url;
	}

	public String getDuration() {
		String duration = "";
		try {
			JSONObject json = new JSONObject(message);
			duration = json.getJSONObject("message").optString("Duration");
			duration = getDurationString(duration);
		} catch (Exception x) {
		}
		return duration;
	}

	public String getFileSize() {
		String size = "";
		int MB = 1024 * 1204;
		int KB = 1204;

		try {
			JSONObject json = new JSONObject(message);
			long value = json.getJSONObject("message").optLong("FileSize");
			if (value > MB)
				size = value / MB + " MB";
			else
				size = value / KB + " KB";

		} catch (Exception x) {
		}
		return size;
	}

	public String getPhotoData() {
		String url = "";
		try {
			JSONObject json = new JSONObject(message);
			url = json.getJSONObject("message").optString("PhotoData");
		} catch (Exception x) {
		}
		return url;
	}

	public String getContactName() {
		String name = "";
		try {
			JSONObject json = new JSONObject(message);
			name = json.getJSONObject("message").getString("Name");
		} catch (Exception x) {
		}
		return name;
	}

	public double getLatitude() {
		double latitude = 0.0;
		try {
			JSONObject json = new JSONObject(message);
			latitude = json.getJSONObject("message").getDouble("lat");
		} catch (Exception x) {
		}
		return latitude;
	}

	public double getLongitude() {
		double longitude = 0.0;
		try {
			JSONObject json = new JSONObject(message);
			longitude = json.getJSONObject("message").getDouble("long");
		} catch (Exception x) {
		}
		return longitude;
	}

	public String getFullContact() {
		String fullContact = "";
		try {
			JSONObject json = new JSONObject(message);
			fullContact = json.optString("message");
		} catch (Exception x) {
		}
		return fullContact;
	}

	public void setId(int _id) {
		this._id = _id;
	}

	public int getId() {
		return _id;
	}

	public String getLocalUrl() {
		// TODO Auto-generated method stub
		String localUrl = "";
		try {
			JSONObject json = new JSONObject(message);
			localUrl = json.getJSONObject("message").optString("LocalPath");
		} catch (Exception x) {
		}
		return localUrl;
	}

	public void setChatTypeStr(String chatTypeStr) {
		this.chatTypeStr = chatTypeStr;
	}

	public String getChatTypeStr() {
		return chatTypeStr;
	}

	public String getMobileNo() {
		String mobileNo = "";
		try {
			JSONObject json = new JSONObject(message);
			mobileNo = json.getString("mobileNo");

		} catch (Exception x) {
		}
		return mobileNo;
	}
}
