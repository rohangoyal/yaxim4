package com.namonamo.app.db;

import java.util.ArrayList;
import java.util.Collections;

import android.content.Context;
import android.database.Cursor;

public class RecentChatDBService {

	public static final String RECENT_DB_CHANGE_ACTION = "com.namonamo.RECENT_DB_CHANGE_ACTION";

	public static ArrayList<RecentChat> getAllRecentChat(Context ctx) {
		ArrayList<RecentChat> items = new ArrayList<RecentChat>();
		RecentChatDbAdapter mDbHelper = new RecentChatDbAdapter(ctx);
		try {
			mDbHelper.open();
			Cursor cursor = mDbHelper.fetchAllRecentChat();
			if (cursor.getCount() > 0) {
				cursor.moveToFirst();
				do {
					RecentChat item = new RecentChat(cursor);
					items.add(item);
				} while (cursor.moveToNext());
				if (items != null && items.size() > 1) {
					Collections.sort(items);
				}
			}
			cursor.close();
		} catch (Exception x) {
		} finally {
			mDbHelper.close();
		}
		return items;
	}

	public static long saveRecentChat(Context ctx, RecentChat chatItem) {
		long rowId = 0;
		RecentChatDbAdapter mDbHelper = new RecentChatDbAdapter(ctx);
		try {
			mDbHelper.open();
			Cursor cursor = mDbHelper.fetchChatByJID(chatItem.getUserJID());
			if (cursor.getCount() > 0) {
				rowId = mDbHelper.updateRecentChat(chatItem);
			} else {
				rowId = mDbHelper.createAccount(chatItem);
			}
		} catch (Exception x) {
		} finally {
			mDbHelper.close();
		}
		return rowId;
	}

	public static RecentChat fetchRecentChat(Context context, String jid) {
		RecentChat item = null;
		RecentChatDbAdapter mDbHelper = new RecentChatDbAdapter(context);
		try {
			mDbHelper.open();
			Cursor cursor = mDbHelper.fetchChatByJID(jid);
			if (cursor.getCount() > 0) {
				cursor.moveToFirst();
				do {
					item = new RecentChat(cursor);
				} while (cursor.moveToNext());
			}
			cursor.close();
		} catch (Exception x) {
		} finally {
			mDbHelper.close();
		}
		return item;
	}

	public static void setReadMessage(Context applicationContext, String jid) {
		RecentChat recentchat = fetchRecentChat(applicationContext, jid);
		if (recentchat != null) {
			recentchat.setUnReadCount(0);
			saveRecentChat(applicationContext, recentchat);
		}
	}

	public static long removeRecentChat(Context ctx, String jid) {
		long rowId = 0;
		RecentChatDbAdapter mDbHelper = new RecentChatDbAdapter(ctx);
		try {
			mDbHelper.open();
			rowId = mDbHelper.deleteEntryByJId(jid);
		} catch (Exception x) {
			x.getMessage();
		} finally {
			mDbHelper.close();
		}
		return rowId;
	}

}
