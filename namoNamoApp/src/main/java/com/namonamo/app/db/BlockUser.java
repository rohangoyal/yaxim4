package com.namonamo.app.db;

import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;

public class BlockUser {
	private String jid;
	private String userId;
	private String mobileNo;
	private String profilePic;

	public ContentValues toContentValues() {
		ContentValues values = new ContentValues();
		values.put(BlockUserTable.COL_USER_JID, getJid());
		values.put(BlockUserTable.COL_USER_ID, getUserId());
		values.put(BlockUserTable.COL_MOBILE_NO, getMobileNo());
		values.put(BlockUserTable.COL_PROFILE_PIC, getProfilePic());
		return values;
	}

	public BlockUser(JSONObject json) {
		try {
			setJid(json.getString("block_user_jid"));
			setUserId(json.getString("user_id"));
			setMobileNo(json.getString("mobile_number"));
			setProfilePic(json.getString("pic_url"));

		} catch (Exception x) {
		}
	}

	public BlockUser(Cursor cursor) {
		setJid(cursor.getString(BlockUserTable.ROW_USER_JID));
		setUserId(cursor.getString(BlockUserTable.ROW_USER_ID));
		setMobileNo(cursor.getString(BlockUserTable.ROW_MOBILE_NO));
		setProfilePic(cursor.getString(BlockUserTable.ROW_PROFILE_PIC));
	}

	public void setJid(String jid) {
		this.jid = jid;
	}

	public String getJid() {
		return jid;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}

	public String getProfilePic() {
		return profilePic;
	}
}
