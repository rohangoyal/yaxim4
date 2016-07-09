package com.namonamo.app.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author Deepak Saini
 * 
 */
public class DailyMessageDbAdapter {

	/**
	 * 
	 */
	private Context context;
	/**
	 * 
	 */
	private SQLiteDatabase db;
	/**
	 * 
	 */
	private NamoNamoDBHelper dbHelper;

	/**
	 * @param context
	 */
	public DailyMessageDbAdapter(Context context) {
		this.context = context;
	}

	/**
	 * open data base
	 * 
	 * @return network adapter
	 * @throws SQLException
	 */
	public DailyMessageDbAdapter open() throws SQLException {
		dbHelper = new NamoNamoDBHelper(context);
		db = dbHelper.getWritableDatabase();
		return this;

	}

	/**
	 * closing data base
	 */
	public void close() {

		dbHelper.close();
	}

	public long createAccount(DailyMessage item) {
		return db.insert(DailyMessageTable.TABLENAME, null,
				item.toContentValues());
	}

	public Cursor fetchAllMessage() throws SQLException {
		Cursor mCursor = db.query(true, DailyMessageTable.TABLENAME, null,
				null, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public Cursor fetchAllMessage(long date) {
		Cursor mCursor = db.query(true, DailyMessageTable.TABLENAME, null,
				DailyMessageTable.COL_DATE + ">" + date, null, null, null,
				null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}
}
