package com.hihello.app.db;

import android.database.sqlite.SQLiteDatabase;
import com.hihello.app.common.Log;


/**
 * @author Deepak Saini
 * 
 */
public class DailyMessageTable {

	/**
	 * ContactTable
	 */
	public static final String TABLENAME = "hihello_daily_message";

	public static final String COL_ID = "id";
	public static final String COL_MESSAGE = "message";
	public static final String COL_DATE = "date";
		

	public static final int ROW_ID = 0;
	public static final int ROW_MESSAGE = 1;
	public static final int ROW_DATE = 2;
		

	/**
	 * 
	 */
	private static final String DATABASE_CREATE = "create table if not exists "
			+ TABLENAME + "(" 
			+ COL_ID + " integer, " 
			+ COL_MESSAGE + " text, " 
			+ COL_DATE + " long);";

	

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
		Log.w(DailyMessageTable.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS " + TABLENAME);
		onCreate(database);
	}
}