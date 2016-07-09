package com.namonamo.app.db;

import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;

public class DailyMessage implements Comparable<DailyMessage> {
	int id;
	String message;
	private long date;

	public ContentValues toContentValues() {
		ContentValues values = new ContentValues();
		values.put(DailyMessageTable.COL_ID, id);
		values.put(DailyMessageTable.COL_MESSAGE, message);
		values.put(DailyMessageTable.COL_DATE, getDate());
		return values;
	}

	public DailyMessage(JSONObject json) {
		try {
			id = json.getInt("id");
			message = json.getString("messages");
			setDate(json.getLong("date"));
		} catch (Exception x) {
		}
	}

	public DailyMessage(Cursor cursor) {
		id = cursor.getInt(DailyMessageTable.ROW_ID);
		message = cursor.getString(DailyMessageTable.ROW_MESSAGE);
		setDate(cursor.getLong(DailyMessageTable.ROW_DATE));
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public long getDate() {
		return date;
	}

	@Override
	public int compareTo(DailyMessage another) {
		return (int) (getDate() - another.getDate());
		
	}
}
