package com.namonamo.app.db;

import android.database.sqlite.SQLiteDatabase;
import com.namonamo.app.common.Log;

/**
 * @author Deepak Saini
 * 
 */
public class BlockUserTable {

	/**
	 * ContactTable
	 */
	public static final String TABLENAME = "block_list";

	public static final String COL_USER_JID = "jid";
	public static final String COL_USER_ID = "user_id";
	public static final String COL_MOBILE_NO = "mobile_no";
	public static final String COL_PROFILE_PIC = "profile_pic";
	

	public static final int ROW_USER_JID = 0;
	public static final int ROW_USER_ID = 1;
	public static final int ROW_MOBILE_NO = 2;
	public static final int ROW_PROFILE_PIC = 3;
	
	

	/**
	 * 
	 */
	private static final String DATABASE_CREATE = "create table if not exists "
			+ TABLENAME + "(" 
			+ COL_USER_JID + " text,"
			+ COL_USER_ID + " text,"
			+ COL_MOBILE_NO + " text,"
			+ COL_PROFILE_PIC + " text"
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
		Log.w(BlockUserTable.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS " + TABLENAME);
		onCreate(database);
	}
}