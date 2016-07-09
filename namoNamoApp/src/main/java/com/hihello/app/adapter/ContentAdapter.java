package com.hihello.app.adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.hihello.app.R;
import com.hihello.app.common.ContactUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ContentAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private JSONArray arrayPhone;
	int phoneSize = 0;
	private JSONArray arrayEmail;
	int emailSize = 0;

	public ContentAdapter(LayoutInflater inflater, JSONObject json, Context ctx) {
		try {
			this.inflater = inflater;
			if (json.has("Phone")) {
				this.arrayPhone = json.getJSONArray("Phone");
				phoneSize = arrayPhone.length();
			}
			if (json.has("Email")) {
				this.arrayEmail = json.getJSONArray("Email");
				emailSize = arrayEmail.length();
			}

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
		
		try {
			if (index < phoneSize) {
				JSONObject json = arrayPhone.getJSONObject(index);
				String phoneNumber = json.getString("Number");
				int phType = json.getInt("Type");
				String phoneType = ContactUtil.getNumberType(phType);
				TextView txtNumber = (TextView) view
						.findViewById(R.id.txt_value);
				TextView txtType = (TextView) view.findViewById(R.id.txt_type);
				txtNumber.setText(phoneNumber);
				txtType.setText(phoneType);
				img_type.setImageResource(R.drawable.contact1);
			} else {
				JSONObject json = arrayEmail.getJSONObject(index-phoneSize);
				String email = json.getString("Email");
				int type = json.getInt("Type");
				String emailType = ContactUtil.getEmailType(type);
				TextView txt_value = (TextView) view
						.findViewById(R.id.txt_value);
				TextView txtType = (TextView) view.findViewById(R.id.txt_type);
				txt_value.setText(email);
				txtType.setText(emailType);
				img_type.setImageResource(R.drawable.contact1);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return view;
	}

}
