package com.namonamo.app.db;

import java.util.Date;

import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;
import android.widget.Toast;

public class NamoNamoContact implements Comparable<NamoNamoContact> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long last_status_update;
	private int show_profile;
	private int show_status;
	private int show_last_seen;
	private int user_id;

	public NamoNamoContact(JSONObject json) {
		try {
			this.jid = json.optString("jid");
			this.mobile_number = json.optString("mobile_number");
			if (!mobile_number.startsWith("+"))
				mobile_number = "+" + mobile_number;
			
			this.status = json.optString("status");
			this.firstname = json.optString("firstname");
			this.profile_pic = json.optString("pic_url");
			try {
				int timeStamp = json.optInt("date_of_birth");
				date_of_birth = new Date(timeStamp);
			} catch (Exception x) {
			}
			this.lastname = json.optString("lastname");
			this.email = json.optString("email");
			setLast_status_update(json.optLong("last_status_update"));
			setShow_profile(json.optInt("show_profile"));
			setShow_status(json.optInt("show_status"));
			setShow_last_seen(json.optInt("show_last_seen"));
			setUser_id(json.optInt("user_id"));

		} catch (Exception x) {

		}
	}

	String jid;

	public String getJid() {
		return jid;
	}

	public void setJid(String jid) {
		this.jid = jid;
	}

	public String getMobile_number() {
		if (!mobile_number.contains("+")) {
			return "+" + mobile_number;
		} else {
			return mobile_number;
		}
	}

	public void setMobile_number(String mobile_number) {
		this.mobile_number = mobile_number;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getPic_url() {
		return profile_pic;
	}

	public void setPic_url(String pic_url) {
		this.profile_pic = pic_url;
	}

	public Date getDate_of_birth() {
		return date_of_birth;
	}

	public void setDate_of_birth(Date date_of_birth) {
		this.date_of_birth = date_of_birth;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	String mobile_number;
	String status;
	String firstname;
	String profile_pic;
	Date date_of_birth;
	String lastname;
	String email;
	private String displayName;

	public ContentValues toContentValues() {
		ContentValues values = new ContentValues();
		values.put(NamoNamoContactTable.COL_CONTACT_F_NAME, firstname);
		values.put(NamoNamoContactTable.COL_CONTACT_L_NAME, lastname);
		values.put(NamoNamoContactTable.COL_DATE_OF_BIRTH,
				date_of_birth.getTime());
		values.put(NamoNamoContactTable.COL_DISPLAY_NAME, getDisplayName());
		values.put(NamoNamoContactTable.COL_EMAIL, email);
		values.put(NamoNamoContactTable.COL_MOBILE_NO, mobile_number);
		values.put(NamoNamoContactTable.COL_PROFILE_PIC, profile_pic);
		values.put(NamoNamoContactTable.COL_STATUS, status);
		values.put(NamoNamoContactTable.COL_USER_JID, jid);
		values.put(NamoNamoContactTable.COL_LAST_STATUS_UPDATE,
				getLast_status_update());
		values.put(NamoNamoContactTable.COL_USER_ID, user_id);
		values.put(NamoNamoContactTable.COL_SHOW_PROFILE, show_profile);
		values.put(NamoNamoContactTable.COL_SHOW_LAST_SEEN, show_last_seen);
		values.put(NamoNamoContactTable.COL_SHOW_STATUS, show_status);

		return values;
	}

	public NamoNamoContact(Cursor cursor) {
		try {
			firstname = cursor
					.getString(NamoNamoContactTable.ROW_CONTACT_F_NAME);
			lastname = cursor
					.getString(NamoNamoContactTable.ROW_CONTACT_L_NAME);
			long timeStamp = cursor
					.getLong(NamoNamoContactTable.ROW_DATE_OF_BIRTH);
			date_of_birth = new Date(timeStamp);
			setDisplayName(cursor
					.getString(NamoNamoContactTable.ROW_DISPLAY_NAME));
			email = cursor.getString(NamoNamoContactTable.ROW_EMAIL);
			mobile_number = cursor
					.getString(NamoNamoContactTable.ROW_MOBILE_NO);
			profile_pic = cursor
					.getString(NamoNamoContactTable.ROW_PROFILE_PIC);


			status = cursor.getString(NamoNamoContactTable.ROW_STATUS);
			jid = cursor.getString(NamoNamoContactTable.ROW_USER_JID);
			setLast_status_update(cursor
					.getLong(NamoNamoContactTable.ROW_LAST_STATUS_UPDATE));
			user_id = cursor.getInt(NamoNamoContactTable.ROW_USER_ID);
			show_status = cursor.getInt(NamoNamoContactTable.ROW_SHOW_STATUS);
			show_last_seen = cursor
					.getInt(NamoNamoContactTable.ROW_SHOW_LAST_SEEN);
			show_profile = cursor.getInt(NamoNamoContactTable.ROW_SHOW_PROFILE);

		} catch (Exception x) {

		}
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		if (displayName == null)
			return getMobile_number();
		return displayName;
	}

	@Override
	public int compareTo(NamoNamoContact another) {
		// TODO Auto-generated method stub
		return displayName.compareTo(another.getDisplayName());
	}

	public void setLast_status_update(long last_status_update) {
		this.last_status_update = last_status_update;
	}

	public long getLast_status_update() {
		return last_status_update;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setShow_last_seen(int show_last_seen) {
		this.show_last_seen = show_last_seen;
	}

	public boolean getShow_last_seen() {
		return show_last_seen == 1;
	}

	public void setShow_status(int show_status) {
		this.show_status = show_status;
	}

	public boolean getShow_status() {
		return show_status == 1;
	}

	public void setShow_profile(int show_profile) {
		this.show_profile = show_profile;
	}

	public boolean getShow_profile() {
		return show_profile == 1;
	}

}
