package com.namonamo.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author Deepak Saini
 * 
 */
public class NamoNamoDBHelper extends SQLiteOpenHelper {

	/**
	 * DATABASE_VERSION
	 */
	private static final int DATABASE_VERSION = 10;
	/**
	 * DATABASENAME
	 */
	private static final String DATABASENAME = "NamoNamoDB";

	/**
	 * @param context
	 */
	public NamoNamoDBHelper(Context context) {
		super(context, DATABASENAME, null, DATABASE_VERSION);
	}

	public void onCreate(SQLiteDatabase database) {
		RecentChatTable.onCreate(database);
		NamoNamoContactTable.onCreate(database);
		DailyMessageTable.onCreate(database);
		BlockUserTable.onCreate(database);
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		RecentChatTable.onUpgrade(database, oldVersion, newVersion);
		NamoNamoContactTable.onUpgrade(database, oldVersion, newVersion);
		DailyMessageTable.onUpgrade(database, oldVersion, newVersion);
		BlockUserTable.onUpgrade(database, oldVersion, newVersion);
	}
}