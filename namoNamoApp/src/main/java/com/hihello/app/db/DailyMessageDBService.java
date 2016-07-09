package com.hihello.app.db;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;

public class DailyMessageDBService {

	public static ArrayList<DailyMessage> getAllMessage(Context ctx) {
		ArrayList<DailyMessage> items = new ArrayList<DailyMessage>();
		DailyMessageDbAdapter mDbHelper = new DailyMessageDbAdapter(ctx);
		try {
			mDbHelper.open();
			Cursor cursor = mDbHelper.fetchAllMessage();
			if (cursor.getCount() > 0) {
				cursor.moveToFirst();
				do {
					DailyMessage item = new DailyMessage(cursor);
					items.add(item);
				} while (cursor.moveToNext());
				// Collections.sort(items);
			}
			cursor.close();
		} catch (Exception x) {
		} finally {
			mDbHelper.close();
		}
		return items;
	}

	public static ArrayList<DailyMessage> getMessageAfterDate(Context ctx,
			long date) {
		ArrayList<DailyMessage> items = new ArrayList<DailyMessage>();
		DailyMessageDbAdapter mDbHelper = new DailyMessageDbAdapter(ctx);
		try {
			mDbHelper.open();
			Cursor cursor = mDbHelper.fetchAllMessage(date);
			if (cursor.getCount() > 0) {
				cursor.moveToFirst();
				do {
					DailyMessage item = new DailyMessage(cursor);
					items.add(item);
				} while (cursor.moveToNext());
				// Collections.sort(items);
			}
			cursor.close();
		} catch (Exception x) {
		} finally {
			mDbHelper.close();
		}
		return items;
	}

	public static long saveMessage(Context ctx, DailyMessage message) {
		long rowId = 0;
		DailyMessageDbAdapter mDbHelper = new DailyMessageDbAdapter(ctx);
		try {
			mDbHelper.open();
			rowId = mDbHelper.createAccount(message);
		} catch (Exception x) {
			x.getMessage();
		} finally {
			mDbHelper.close();
		}
		return rowId;
	}
}