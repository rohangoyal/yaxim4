package com.hihello.app.db;

import android.database.sqlite.SQLiteDatabase;
import com.hihello.app.common.Log;


/**
 * @author Deepak Saini
 * 
 */
public class HiHelloContactTable {

	/**
	 * ContactTable
	 */
	public static final String TABLENAME = "hihello_contact";

	public static final String COL_USER_JID = "user_jid";
	public static final String COL_MOBILE_NO = "mobile_no";
	public static final String COL_PROFILE_PIC = "profile_pic";
	public static final String COL_STATUS = "status";
	public static final String COL_CONTACT_F_NAME = "f_name";
	public static final String COL_CONTACT_L_NAME = "l_name";
	
	public static final String COL_DISPLAY_NAME = "display_name";
	public static final String COL_EMAIL = "email";
	public static final String COL_DATE_OF_BIRTH = "dob";
	public static final String COL_LAST_STATUS_UPDATE = "last_status_update";
	public static final String COL_USER_ID = "user_id";
	public static final String COL_SHOW_LAST_SEEN = "show_last_seen";
	public static final String COL_SHOW_PROFILE = "show_profile";
	public static final String COL_SHOW_STATUS = "show_status";
	

	public static final int ROW_USER_JID = 0;
	public static final int ROW_MOBILE_NO = 1;
	public static final int ROW_PROFILE_PIC = 2;
	public static final int ROW_STATUS = 3;
	public static final int ROW_CONTACT_F_NAME = 4;
	public static final int ROW_CONTACT_L_NAME = 5;
	public static final int ROW_DISPLAY_NAME = 6;
	public static final int ROW_EMAIL = 7;
	public static final int ROW_DATE_OF_BIRTH = 8;
	public static final int ROW_LAST_STATUS_UPDATE = 9;
	public static final int ROW_USER_ID = 10;
	public static final int ROW_SHOW_PROFILE = 11;
	public static final int ROW_SHOW_STATUS = 12;
	public static final int ROW_SHOW_LAST_SEEN = 13;
	

	/**
	 * 
	 */
	private static final String DATABASE_CREATE = "create table if not exists "
			+ TABLENAME + "(" 
			+ COL_USER_JID + " text, " 
			+ COL_MOBILE_NO + " text, " 
			+ COL_PROFILE_PIC + " text, " 
			+ COL_STATUS + " text, " 
			+ COL_CONTACT_F_NAME + " text, " 
			+ COL_CONTACT_L_NAME + " text, " 
			+ COL_DISPLAY_NAME + " text, "
			+ COL_EMAIL + " text, " 
			+ COL_DATE_OF_BIRTH + " text, " 
			+ COL_LAST_STATUS_UPDATE + " text, "
			+ COL_USER_ID + " integer, "
			+ COL_SHOW_PROFILE + " integer, "
			+ COL_SHOW_STATUS + " integer, "
			+ COL_SHOW_LAST_SEEN + " integer"
			+ ");";

	

	/**
	 * @param database
	 * 
	 */
	public static final void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	/**
	 * @param database
	 * @param oldVersion
	 * @param newVersion
	 */
	public static final void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		Log.w(HiHelloContactTable.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS " + TABLENAME);
		onCreate(database);
	}
}