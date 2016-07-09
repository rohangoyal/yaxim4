package com.namonamo.app.adapter;

import java.util.ArrayList;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.namonamo.app.R;

import android.content.Context;
import android.database.MergeCursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.namonamo.app.activity.CircularImageView;
import com.namonamo.app.activity.UIApplication;
import com.namonamo.app.constant.Hihelloconstant;
import com.namonamo.app.db.NamoNamoContact;
import com.nostra13.universalimageloader.core.ImageLoader;

public class NamoNamoContactAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private ArrayList<NamoNamoContact> allContacts;

	public NamoNamoContactAdapter(LayoutInflater inflater,
			ArrayList<NamoNamoContact> allContacts, Context ctx) {
		this.inflater = inflater;
		this.allContacts = allContacts;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return allContacts.size();
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
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		View view = arg1;
		if (view == null)
			view = inflater.inflate(R.layout.contact_cell, null);
		NamoNamoContact contact = allContacts.get(arg0);
		TextView txt_name = (TextView) view.findViewById(R.id.txt_name);
		String userName = contact.getDisplayName();
		if (userName == null || userName.length() < 1)
			userName = contact.getMobile_number();
		txt_name.setText(userName);
		TextView txt_chat = (TextView) view.findViewById(R.id.txt_status);
		if (contact.getStatus() == null || contact.getStatus().length() == 0
				|| contact.getShow_status() == false)
			txt_chat.setText("No Status");
		else
			txt_chat.setText(contact.getStatus());

		CircularImageView profileImage = (CircularImageView) view
				.findViewById(R.id.img_contact);
//		profileImage.setImageResource(R.drawable.contact);
		String pic=contact.getPic_url();
		if (pic.length()>1) {
			ImageLoader.getInstance().displayImage(contact.getPic_url(),
					profileImage, UIApplication.diRoundOptions);
		}
//		else
//		{
//			String secondLetter = String.valueOf(contact.getDisplayName().charAt(0));
//			ColorGenerator generator1 = ColorGenerator.MATERIAL; // or use DEFAULT
//			int color1 = generator1.getRandomColor();
//			TextDrawable drawable1 = TextDrawable.builder()
//					.buildRound(secondLetter, color1);
//			profileImage.setBackground(drawable1);
//		}
		return view;
	}

	public void setContacts(ArrayList<NamoNamoContact> allContact) {
		this.allContacts = allContact;

	}

}
