package com.hihello.app.db;

import java.util.ArrayList;
import java.util.Collections;

import android.content.Context;
import android.database.Cursor;

public class hiHelloContactDBService {

	public static ArrayList<HiHelloContact> getAllContact(Context ctx) {
		ArrayList<HiHelloContact> items = new ArrayList<HiHelloContact>();
		HiHelloContactDbAdapter mDbHelper = new HiHelloContactDbAdapter(ctx);
		try {
			mDbHelper.open();
			Cursor cursor = mDbHelper.fetchAllContact();
			if (cursor.getCount() > 0) {
				cursor.moveToFirst();
				do {
					HiHelloContact item = new HiHelloContact(cursor);
					items.add(item);
				} while (cursor.moveToNext());
				Collections.sort(items);
			}
			cursor.close();
		} catch (Exception x) {
		} finally {
			mDbHelper.close();
		}
		return items;
	}

	public static long saveContact(Context ctx, HiHelloContact contact) {
		long rowId = 0;
		HiHelloContactDbAdapter mDbHelper = new HiHelloContactDbAdapter(ctx);
		try {
			mDbHelper.open();
			Cursor cursor = mDbHelper.fetchAccountByJID(contact.getJid());
			if (cursor.getCount() > 0) {
				rowId = mDbHelper.updateAccount(contact);
			} else {
				rowId = mDbHelper.createAccount(contact);
			}
		} catch (Exception x) {
			x.getMessage();
		} finally {
			mDbHelper.close();
		}
		return rowId;
	}

	public static HiHelloContact fetchContactByJID(Context context, String jid) {
		HiHelloContact item = null;
		HiHelloContactDbAdapter mDbHelper = new HiHelloContactDbAdapter(
				context);
		try {
			mDbHelper.open();
			Cursor cursor = mDbHelper.fetchAccountByJID(jid);
			if (cursor.getCount() > 0) {
				cursor.moveToFirst();
				do {
					item = new HiHelloContact(cursor);

				} while (cursor.moveToNext());
			}
			cursor.close();
		} catch (Exception x) {
		} finally {
			mDbHelper.close();
		}
		return item;
	}

	public static ArrayList<String> getAllUsersId(Context ctx) {
		ArrayList<String> items = new ArrayList<String>();
		HiHelloContactDbAdapter mDbHelper = new HiHelloContactDbAdapter(ctx);
		try {
			mDbHelper.open();
			Cursor cursor = mDbHelper.fetchAllContact();
			if (cursor.getCount() > 0) {
				cursor.moveToFirst();
				do {
					int user_id = cursor.getInt(HiHelloContactTable.ROW_USER_ID);
					items.add(user_id+"");
				} while (cursor.moveToNext());
			}
			cursor.close();
		} catch (Exception x) {
		} finally {
			mDbHelper.close();
		}
		return items;
	}

	public static HiHelloContact fetchContactByUserID(Context mContext,
			String user_id) {
		HiHelloContact item = null;
		HiHelloContactDbAdapter mDbHelper = new HiHelloContactDbAdapter(
				mContext);
		try {
			mDbHelper.open();
			Cursor cursor = mDbHelper.fetchAccountByUserID(user_id);
			if (cursor.getCount() > 0) {
				cursor.moveToFirst();
				do {
					item = new HiHelloContact(cursor);

				} while (cursor.moveToNext());
			}
			cursor.close();
		} catch (Exception x) {
		} finally {
			mDbHelper.close();
		}
		return item;
	}

}
