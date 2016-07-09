package com.hihello.app.db;

import android.database.sqlite.SQLiteDatabase;
import com.hihello.app.common.Log;


/**
 * @author Deepak Saini
 * 
 */
public class RecentChatTable {

	/**
	 * ContactTable
	 */
	public static final String TABLENAME = "recent_chat";

	public static final String COL_USER_JID = "user_jid";
	public static final String COL_MOBILE_NO = "mobile_no";
	public static final String COL_MESSAGE = "message";
	public static final String COL_UNREAD_COUNT = "unread_count";
	public static final String COL_DATE = "date";
	public static final String COL_IS_FROM_ME = "is_from_me";
	public static final String COL_PROFILE_PIC = "profile_pic";
	public static final String COL_IS_CONTACT = "is_contact";
	

	public static final int ROW_USER_JID = 0;
	public static final int ROW_MOBILE_NO = 1;
	public static final int ROW_MESSAGE = 2;
	public static final int ROW_UNREAD_COUNT = 3;
	public static final int ROW_DATE = 4;
	public static final int ROW_IS_FROM_ME = 5;
	public static final int ROW_PROFILE_PIC = 6;
	public static final int ROW_IS_CONTACT = 7;
	

	/**
	 * 
	 */
	private static final String DATABASE_CREATE = "create table if not exists "
			+ TABLENAME + "(" 
			+ COL_USER_JID + " text, " 
			+ COL_MOBILE_NO + " text, " 
			+ COL_MESSAGE + " text, " 
			+ COL_UNREAD_COUNT + " text, " 
			+ COL_DATE + " INTEGER, " 
			+ COL_IS_FROM_ME + " text, "
			+ COL_PROFILE_PIC + " text, "
			+ COL_IS_CONTACT + " text);";

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
		Log.w(RecentChatTable.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS " + TABLENAME);
		onCreate(database);
	}
}