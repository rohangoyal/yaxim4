package com.namonamo.app.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.namonamo.app.R;
import com.namonamo.app.activity.CircularImageView;
import com.namonamo.app.activity.UIApplication;
import com.namonamo.app.constant.Hihelloconstant;
import com.namonamo.app.content.InviteContact;
import com.nostra13.universalimageloader.core.ImageLoader;

public class InviteContactAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private ArrayList<InviteContact> allContacts = new ArrayList<InviteContact>();

	public InviteContactAdapter(LayoutInflater inflater,
			ArrayList<InviteContact> allContacts, Context ctx) {
		this.inflater = inflater;
		this.allContacts = allContacts;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return allContacts == null ? 0 : allContacts.size();
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
			view = inflater.inflate(R.layout.invite_contact_cell, null);
		InviteContact contact = allContacts.get(arg0);
		TextView txt_name = (TextView) view.findViewById(R.id.txt_name);
		String userName = contact.getName();
		if (userName == null || userName.length() < 1)
			userName = contact.getPhoneNo();
		txt_name.setText(userName);
		TextView txt_phone = (TextView) view.findViewById(R.id.txt_phone_no);
		txt_phone.setText(contact.getPhoneNo());
		CircularImageView profileImage = (CircularImageView) view
				.findViewById(R.id.img_contact);
		if (contact.getPhotoUri() != null && contact.getPhotoUri().length() > 0) {
			ImageLoader.getInstance().displayImage(contact.getPhotoUri(),
					profileImage, UIApplication.diRoundOptions);
		}
//		else
//		{
//			String secondLetter = String.valueOf(contact.getName().charAt(0));
//			ColorGenerator generator1 = ColorGenerator.MATERIAL; // or use DEFAULT
//			int color1 = generator1.getRandomColor();
//			TextDrawable drawable1 = TextDrawable.builder()
//					.buildRound(secondLetter, color1);
//			profileImage.setBackground(drawable1);
//		}
		return view;
	}

	public void setContacts(ArrayList<InviteContact> allContact) {
		this.allContacts = allContact;

	}

}
