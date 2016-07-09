package com.namonamo.app.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.namonamo.app.R;
import com.namonamo.app.activity.UserProfile.IOtherContactListener;

public class OtherContactDetailAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private ArrayList<String> arrayPhone;
	int phoneSize = 0;
	private ArrayList<String> arrayEmail;
	int emailSize = 0;
	private IOtherContactListener listener;

	public OtherContactDetailAdapter(LayoutInflater inflater,
			ArrayList<String> phones, ArrayList<String> emails, Context ctx,
			IOtherContactListener listener) {
		try {
			this.inflater = inflater;
			this.arrayEmail = emails;
			this.arrayPhone = phones;
			this.listener = listener;
			phoneSize = arrayPhone.size();
			emailSize = arrayEmail.size();
		} catch (Exception x) {
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return phoneSize + emailSize;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int index, View arg1, ViewGroup arg2) {
		View view = inflater.inflate(R.layout.contact_centent_cell, null);
		ImageView img_type = (ImageView) view.findViewById(R.id.img_type);
		ImageView img_type2 = (ImageView) view.findViewById(R.id.img_type2);
		ImageView img_divider = (ImageView) view.findViewById(R.id.img_divider);
		ImageView img_divider2 = (ImageView) view
				.findViewById(R.id.img_divider2);
		if (index < phoneSize + emailSize - 1) {
			img_divider2.setVisibility(View.VISIBLE);
		}
		try {
			if (index < phoneSize) {
				final String phoneNumber = arrayPhone.get(index);
				TextView txtNumber = (TextView) view
						.findViewById(R.id.txt_value);
				txtNumber.setText(phoneNumber);
				view.findViewById(R.id.txt_type).setVisibility(View.GONE);
				img_type2.setVisibility(View.VISIBLE);
				img_divider.setVisibility(View.VISIBLE);
				img_type.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						listener.onPhoneClick(phoneNumber);
					}
				});
				img_type2.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						listener.onMessageClick(phoneNumber);
					}
				});

			} else {
				final String email = arrayEmail.get(index - phoneSize);
				TextView txt_value = (TextView) view
						.findViewById(R.id.txt_value);
				view.findViewById(R.id.txt_type).setVisibility(View.GONE);
				txt_value.setText(email);
				img_type.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						listener.onEmailClick(email);
					}
				});
				img_type.setImageResource(R.drawable.contact_email);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return view;
	}

}
