package com.namonamo.app.db;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;

public class BlockUserDBService {

	public static ArrayList<BlockUser> getAllBlockUser(Context ctx) {
		ArrayList<BlockUser> items = new ArrayList<BlockUser>();
		BlockUserDbAdapter mDbHelper = new BlockUserDbAdapter(ctx);
		try {
			mDbHelper.open();
			Cursor cursor = mDbHelper.fetchAllBlockUser();
			if (cursor.getCount() > 0) {
				cursor.moveToFirst();
				do {
					BlockUser user = new BlockUser(cursor);
					items.add(user);
				} while (cursor.moveToNext());
			}
			cursor.close();
		} catch (Exception x) {
		} finally {
			mDbHelper.close();
		}
		return items;
	}
	
	public static BlockUser getBlockUser(Context ctx, String jid) {
		BlockUserDbAdapter mDbHelper = new BlockUserDbAdapter(ctx);
		BlockUser user = null;
		try {
			mDbHelper.open();
			Cursor cursor = mDbHelper.fetchBlockUser(jid);
			if (cursor.getCount() > 0) {
				cursor.moveToFirst();
				do {
					user = new BlockUser(cursor);
				} while (cursor.moveToNext());
			}
			cursor.close();
		} catch (Exception x) {
		} finally {
			mDbHelper.close();
		}
		return user;
	}

	public static long saveBlockUser(Context ctx, BlockUser user) {
		long rowId = 0;
		BlockUserDbAdapter mDbHelper = new BlockUserDbAdapter(ctx);
		try {
			mDbHelper.open();
			rowId = mDbHelper.createAccount(user);
		} catch (Exception x) {
			x.getMessage();
		} finally {
			mDbHelper.close();
		}
		return rowId;
	}

	public static long deleteBlockUserByJID(Context ctx, String jid) {
		long rowId = 0;
		BlockUserDbAdapter mDbHelper = new BlockUserDbAdapter(ctx);
		try {
			mDbHelper.open();
			rowId = mDbHelper.deleteAccountByJID(jid);
		} catch (Exception x) {
			x.getMessage();
		} finally {
			mDbHelper.close();
		}
		return rowId;
	}

	public static long deleteBlockUserByUserId(Context ctx, String user_id) {
		long rowId = 0;
		BlockUserDbAdapter mDbHelper = new BlockUserDbAdapter(ctx);
		try {
			mDbHelper.open();
			rowId = mDbHelper.deleteAccountByUserId(user_id);
		} catch (Exception x) {
			x.getMessage();
		} finally {
			mDbHelper.close();
		}
		return rowId;
	}

	public static long deleteAllBlockUser(Context mcontext) {
		long rowId = 0;
		BlockUserDbAdapter mDbHelper = new BlockUserDbAdapter(mcontext);
		try {
			mDbHelper.open();
			rowId = mDbHelper.deleteAllBlockUser();
		} catch (Exception x) {
			x.getMessage();
		} finally {
			mDbHelper.close();
		}
		return rowId;		
	}

}