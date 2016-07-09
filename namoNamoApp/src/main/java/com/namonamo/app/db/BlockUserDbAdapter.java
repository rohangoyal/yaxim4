package com.namonamo.app.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author Deepak Saini
 * 
 */
public class BlockUserDbAdapter {

	private Context context;

	private SQLiteDatabase db;

	private NamoNamoDBHelper dbHelper;

	public BlockUserDbAdapter(Context context) {
		this.context = context;
	}

	public BlockUserDbAdapter open() throws SQLException {
		dbHelper = new NamoNamoDBHelper(context);
		db = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		dbHelper.close();
	}

	public long createAccount(BlockUser user) {
		return db
				.insert(BlockUserTable.TABLENAME, null, user.toContentValues());
	}

	public long deleteAccountByJID(String jid) {
		return db.delete(BlockUserTable.TABLENAME, BlockUserTable.COL_USER_JID
				+ "='" + jid + "'", null);
	}

	public long deleteAccountByUserId(String user_id) {
		return db.delete(BlockUserTable.TABLENAME, BlockUserTable.COL_USER_ID
				+ "='" + user_id + "'", null);
	}

	public Cursor fetchAllBlockUser() throws SQLException {
		Cursor mCursor = db.query(BlockUserTable.TABLENAME, null, null, null,
				null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public Cursor fetchBlockUser(String jid) {
		Cursor mCursor = db.query(BlockUserTable.TABLENAME, null,
				BlockUserTable.COL_USER_JID + "='" + jid + "'", null, null,
				null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public long deleteAllBlockUser() {
		return db.delete(BlockUserTable.TABLENAME, null, null);
	}
}
