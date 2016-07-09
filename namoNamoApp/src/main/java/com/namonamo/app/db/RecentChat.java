package com.namonamo.app.db;

import java.util.Date;

import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;

public class RecentChat implements Comparable<RecentChat> {
	String userJID;
	String message;
	int unReadCount;
	boolean isFromMe;
	Date date;
	String mobileNo;
	private String profilePic;
	private boolean isContact;
	private String displayName;
	private int chatType;
	private int deliveryStatus;
	private String status;

	public final static int CHAT_TYPE_TEXT = 0;
	public final static int CHAT_TYPE_MEDIA = 1;
	public final static int CHAT_TYPE_VIDEO = 2;
	public final static int CHAT_TYPE_CONTACT = 3;
	public final static int CHAT_TYPE_AUDIO = 4;
	public final static int CHAT_TYPE_LOCATION = 5;
	public final static int CHAT_TYPE_IMAGE = 6;

	public ContentValues toContentValues() {
		ContentValues values = new ContentValues();
		values.put(RecentChatTable.COL_DATE, date.getTime());
		values.put(RecentChatTable.COL_IS_FROM_ME, isFromMe ? "1" : "0");
		values.put(RecentChatTable.COL_MESSAGE, message);
		values.put(RecentChatTable.COL_MOBILE_NO, mobileNo);
		values.put(RecentChatTable.COL_UNREAD_COUNT, unReadCount + "");
		values.put(RecentChatTable.COL_USER_JID, userJID);
		values.put(RecentChatTable.COL_PROFILE_PIC, getProfilePic());
		values.put(RecentChatTable.COL_IS_CONTACT, isContact ? "1" : "0");

		return values;
	}

	public RecentChat() {
	}

	public RecentChat(Cursor cursor) {
		long timeStamp = cursor.getLong(RecentChatTable.ROW_DATE);
		date = new Date(timeStamp);
		isFromMe = cursor.getString(RecentChatTable.ROW_IS_FROM_ME)
				.equalsIgnoreCase("1");
		message = cursor.getString(RecentChatTable.ROW_MESSAGE);
		mobileNo = cursor.getString(RecentChatTable.ROW_MOBILE_NO);
		unReadCount = cursor.getInt(RecentChatTable.ROW_UNREAD_COUNT);
		userJID = cursor.getString(RecentChatTable.ROW_USER_JID);
		setProfilePic(cursor.getString(RecentChatTable.ROW_PROFILE_PIC));
		setContact(cursor.getString(RecentChatTable.ROW_IS_CONTACT)
				.equalsIgnoreCase("1"));

	}

	void populateChatType() {
		try {
			JSONObject json = new JSONObject(getMessage());
			String type = json.getString("type");
			if (type.equalsIgnoreCase("TEXT"))
				setChatType(CHAT_TYPE_TEXT);
			else if (type.equalsIgnoreCase("AUDIO"))
				setChatType(CHAT_TYPE_AUDIO);
			else if (type.equalsIgnoreCase("CONTACT"))
				setChatType(CHAT_TYPE_CONTACT);
			else if (type.equalsIgnoreCase("MEDIA"))
				setChatType(CHAT_TYPE_MEDIA);
			else if (type.equalsIgnoreCase("IMAGE"))
				setChatType(CHAT_TYPE_IMAGE);
			else if (type.equalsIgnoreCase("VIDEO"))
				setChatType(CHAT_TYPE_VIDEO);
			else if (type.equalsIgnoreCase("LOCATION"))
				setChatType(CHAT_TYPE_LOCATION);

		} catch (Exception x) {

		}

	}

	public String getUserJID() {
		return userJID;
	}

	public void setUserJID(String userJID) {
		this.userJID = userJID;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
		populateChatType();
	}

	public int getUnReadCount() {
		return unReadCount;
	}

	public void setUnReadCount(int unReadCount) {
		this.unReadCount = unReadCount;
	}

	public boolean isFromMe() {
		return isFromMe;
	}

	public void setFromMe(boolean isFromMe) {
		this.isFromMe = isFromMe;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public void updateUnreadChat() {
		unReadCount++;

	}

	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}

	public String getProfilePic() {
		return profilePic;
	}

	public void setContact(boolean isContact) {
		this.isContact = isContact;
	}

	public boolean isContact() {
		return isContact;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return this.displayName;
	}

	@Override
	public int compareTo(RecentChat another) {
		return another.date.compareTo(date);
	}

	private void setChatType(int chatType) {
		this.chatType = chatType;
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

	public void setDeliveryStatus(int status) {
		this.deliveryStatus = status;
	}

	public int getDeliveryStatus() {
		return deliveryStatus;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
