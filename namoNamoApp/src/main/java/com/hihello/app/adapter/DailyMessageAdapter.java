package com.hihello.app.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hihello.app.R;
import com.hihello.app.db.DailyMessage;

public class DailyMessageAdapter extends BaseAdapter {

	private ArrayList<DailyMessage> messages;
	private LayoutInflater layoutInflater;

	public DailyMessageAdapter(ArrayList<DailyMessage> message,
			LayoutInflater layoutInflater) {
		this.messages = message;
		this.layoutInflater = layoutInflater;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return messages.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return messages.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	private String getDateString(long milliSeconds) {
		SimpleDateFormat dateFormater = new SimpleDateFormat("dd MMM yy");
		Date date = new Date(milliSeconds);
		return dateFormater.format(date);
	}

	private String getTimeString(long milliSeconds) {
		SimpleDateFormat dateFormater = new SimpleDateFormat("hh:mm aa");
		Date date = new Date(milliSeconds);
		return dateFormater.format(date);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = layoutInflater.inflate(R.layout.from_you_chat, null);
		
		DailyMessage message = messages.get(position);
		boolean showDate = false;
		if (position == 0)
			showDate = true;
		else {
			DailyMessage pChatItem = (DailyMessage) getItem(position - 1);
			Date pDate = new Date(pChatItem.getDate()*1000);
			Date nDate = new Date(message.getDate()*1000);
			int pDay = pDate.getDate();
			int nDay = nDate.getDate();
			if (pDay != nDay)
				showDate = true;
		}

		
		TextView mTimeView = (TextView) convertView
				.findViewById(R.id.chat_time);
		mTimeView.setText(getTimeString(message.getDate()*1000));
		TextView mMessageView = (TextView) convertView
				.findViewById(R.id.chat_message);
		if(message.getMessage()!=null)
			mMessageView.setText(message.getMessage());
		if (showDate) {
			convertView.findViewById(R.id.layout_header).setVisibility(
					View.VISIBLE);
			TextView mDateView = (TextView) convertView
					.findViewById(R.id.txt_date);
			String dateStr = getDateString(message.getDate()*1000);
			mDateView.setText(dateStr);
		}
		return convertView;
	}

}
