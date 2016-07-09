package com.namonamo.app.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author Deepak Saini
 * 
 */
public class NamoNamoContactDbAdapter {

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
	public NamoNamoContactDbAdapter(Context context) {
		this.context = context;
	}

	/**
	 * open data base
	 * 
	 * @return network adapter
	 * @throws SQLException
	 */
	public NamoNamoContactDbAdapter open() throws SQLException {
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

	public long createAccount(NamoNamoContact item) {
		return db.insert(NamoNamoContactTable.TABLENAME, null,
				item.toContentValues());
	}

	public long updateAccount(NamoNamoContact contact) {
		return db.update(NamoNamoContactTable.TABLENAME,
				contact.toContentValues(), NamoNamoContactTable.COL_USER_JID
						+ "='" + contact.getJid() + "'", null);
	}

	public Cursor fetchAccountByJID(String JID) throws SQLException {
		Cursor mCursor = db.query(true, NamoNamoContactTable.TABLENAME, null,
				NamoNamoContactTable.COL_USER_JID + "=" + "'" + JID + "'",
				null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public Cursor fetchAccountByMobile(String mobile_no) throws SQLException {
		Cursor mCursor = db.query(true, NamoNamoContactTable.TABLENAME, null,
				NamoNamoContactTable.COL_MOBILE_NO + "=" + "'" + mobile_no
						+ "'", null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public Cursor fetchAllContact() throws SQLException {
		Cursor mCursor = db.query(true, NamoNamoContactTable.TABLENAME, null,
				null, null, null, null, NamoNamoContactTable.COL_DISPLAY_NAME,
				null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public int deleteEntryByJId(String JID) throws SQLException {
		return db
				.delete(NamoNamoContactTable.TABLENAME,
						NamoNamoContactTable.COL_USER_JID + "=" + "'" + JID
								+ "'", null);
	}

	public int deleteEntryByMobileNo(String mobile_no) throws SQLException {
		return db.delete(NamoNamoContactTable.TABLENAME,
				NamoNamoContactTable.COL_MOBILE_NO + "=" + "'" + mobile_no
						+ "'", null);
	}

	public Cursor fetchAccountByUserID(String user_id) {
		Cursor mCursor = db.query(true, NamoNamoContactTable.TABLENAME, null,
				NamoNamoContactTable.COL_USER_ID + "=" + user_id, null, null,
				null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

}
