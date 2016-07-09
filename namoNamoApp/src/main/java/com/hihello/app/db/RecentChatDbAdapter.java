package com.hihello.app.db;


import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author Deepak Saini
 * 
 */
public class RecentChatDbAdapter {

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
	public RecentChatDbAdapter(Context context) {
		this.context = context;
	}

	/**
	 * open data base
	 * 
	 * @return network adapter
	 * @throws SQLException
	 */
	public RecentChatDbAdapter open() throws SQLException {
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

	public long createAccount(RecentChat item) {
		return db.insert(RecentChatTable.TABLENAME, null,
				item.toContentValues());
	}

	public long updateRecentChat(RecentChat chatItem) {
		return db.update(RecentChatTable.TABLENAME, chatItem.toContentValues(),
				RecentChatTable.COL_USER_JID + "='" + chatItem.getUserJID()
						+ "'", null);
	}

	public Cursor fetchChatByJID(String JID) throws SQLException {
		Cursor mCursor = db.query(true, RecentChatTable.TABLENAME, null,
				RecentChatTable.COL_USER_JID + "=" + "'" + JID + "'", null,
				null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public Cursor fetchAllRecentChat() throws SQLException {
		Cursor mCursor = db.query(true, RecentChatTable.TABLENAME, null, null,
				null, null, null, null, null);
//		Cursor mCursor = db.rawQuery("SELECT * FROM " + RecentChatTable.TABLENAME +
//                         " ORDER BY "+RecentChatTable.COL_DATE + " DESC", null);
		
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public int deleteEntryByJId(String JID) throws SQLException {
		return db.delete(RecentChatTable.TABLENAME,
				RecentChatTable.COL_USER_JID + "=" + "'" + JID + "'", null);
	}
}
