package com.namonamo.app.db;

import java.util.ArrayList;
import java.util.Collections;

import android.content.Context;
import android.database.Cursor;

public class NamoNamoContactDBService {

	public static ArrayList<NamoNamoContact> getAllContact(Context ctx) {
		ArrayList<NamoNamoContact> items = new ArrayList<NamoNamoContact>();
		NamoNamoContactDbAdapter mDbHelper = new NamoNamoContactDbAdapter(ctx);
		try {
			mDbHelper.open();
			Cursor cursor = mDbHelper.fetchAllContact();
			if (cursor.getCount() > 0) {
				cursor.moveToFirst();
				do {
					NamoNamoContact item = new NamoNamoContact(cursor);
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

	public static long saveContact(Context ctx, NamoNamoContact contact) {
		long rowId = 0;
		NamoNamoContactDbAdapter mDbHelper = new NamoNamoContactDbAdapter(ctx);
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

	public static NamoNamoContact fetchContactByJID(Context context, String jid) {
		NamoNamoContact item = null;
		NamoNamoContactDbAdapter mDbHelper = new NamoNamoContactDbAdapter(
				context);
		try {
			mDbHelper.open();
			Cursor cursor = mDbHelper.fetchAccountByJID(jid);
			if (cursor.getCount() > 0) {
				cursor.moveToFirst();
				do {
					item = new NamoNamoContact(cursor);

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
		NamoNamoContactDbAdapter mDbHelper = new NamoNamoContactDbAdapter(ctx);
		try {
			mDbHelper.open();
			Cursor cursor = mDbHelper.fetchAllContact();
			if (cursor.getCount() > 0) {
				cursor.moveToFirst();
				do {
					int user_id = cursor.getInt(NamoNamoContactTable.ROW_USER_ID);
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

	public static NamoNamoContact fetchContactByUserID(Context mContext,
			String user_id) {
		NamoNamoContact item = null;
		NamoNamoContactDbAdapter mDbHelper = new NamoNamoContactDbAdapter(
				mContext);
		try {
			mDbHelper.open();
			Cursor cursor = mDbHelper.fetchAccountByUserID(user_id);
			if (cursor.getCount() > 0) {
				cursor.moveToFirst();
				do {
					item = new NamoNamoContact(cursor);

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
