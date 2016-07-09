package com.hihello.app.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author Deepak Saini
 * 
 */
public class HiHelloContactDbAdapter {

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
	private HiHelloDBHelper dbHelper;

	/**
	 * @param context
	 */
	public HiHelloContactDbAdapter(Context context) {
		this.context = context;
	}

	/**
	 * open data base
	 * 
	 * @return network adapter
	 * @throws SQLException
	 */
	public HiHelloContactDbAdapter open() throws SQLException {
		dbHelper = new HiHelloDBHelper(context);
		db = dbHelper.getWritableDatabase();
		return this;

	}

	/**
	 * closing data base
	 */
	public void close() {

		dbHelper.close();
	}

	public long createAccount(HiHelloContact item) {
		return db.insert(HiHelloContactTable.TABLENAME, null,
				item.toContentValues());
	}

	public long updateAccount(HiHelloContact contact) {
		return db.update(HiHelloContactTable.TABLENAME,
				contact.toContentValues(), HiHelloContactTable.COL_USER_JID
						+ "='" + contact.getJid() + "'", null);
	}

	public Cursor fetchAccountByJID(String JID) throws SQLException {
		Cursor mCursor = db.query(true, HiHelloContactTable.TABLENAME, null,
				HiHelloContactTable.COL_USER_JID + "=" + "'" + JID + "'",
				null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public Cursor fetchAccountByMobile(String mobile_no) throws SQLException {
		Cursor mCursor = db.query(true, HiHelloContactTable.TABLENAME, null,
				HiHelloContactTable.COL_MOBILE_NO + "=" + "'" + mobile_no
						+ "'", null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public Cursor fetchAllContact() throws SQLException {
		Cursor mCursor = db.query(true, HiHelloContactTable.TABLENAME, null,
				null, null, null, null, HiHelloContactTable.COL_DISPLAY_NAME,
				null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public int deleteEntryByJId(String JID) throws SQLException {
		return db
				.delete(HiHelloContactTable.TABLENAME,
						HiHelloContactTable.COL_USER_JID + "=" + "'" + JID
								+ "'", null);
	}

	public int deleteEntryByMobileNo(String mobile_no) throws SQLException {
		return db.delete(HiHelloContactTable.TABLENAME,
				HiHelloContactTable.COL_MOBILE_NO + "=" + "'" + mobile_no
						+ "'", null);
	}

	public Cursor fetchAccountByUserID(String user_id) {
		Cursor mCursor = db.query(true, HiHelloContactTable.TABLENAME, null,
				HiHelloContactTable.COL_USER_ID + "=" + user_id, null, null,
				null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

}
