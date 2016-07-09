package com.hihello.app.adapter;

import java.util.ArrayList;
import java.util.Locale;

import com.androidquery.AQuery;
import com.hihello.app.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hihello.app.activity.CircularImageView;
import com.hihello.app.common.Log;
import com.hihello.app.db.HiHelloContact;

import de.hdodenhof.circleimageview.CircleImageView;
import github.ankushsachdeva.emojicon.EmojiconTextView;

public class HiHelloContactAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private ArrayList<HiHelloContact> allContacts;
	private ArrayList<HiHelloContact> arraylist;

	AQuery image;

	Context ctx;

	public HiHelloContactAdapter(LayoutInflater inflater,
								 ArrayList<HiHelloContact> allContacts, Context ctx) {
		this.inflater = inflater;
		this.allContacts = allContacts;

		this.ctx=ctx;
		this.arraylist = new ArrayList<HiHelloContact>();
		this.arraylist.addAll(allContacts);

	}

	@Override
	public int getCount() {

		Log.e("Result", "" + allContacts.size());
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

			view = inflater.inflate(R.layout.contact_cell, null);
		HiHelloContact contact = allContacts.get(arg0);
		TextView txt_name = (TextView) view.findViewById(R.id.txt_name);
		String userName = contact.getDisplayName();
		if (userName == null || userName.length() < 1)
			userName = contact.getMobile_number();
		txt_name.setText(userName);
		EmojiconTextView txt_chat = (EmojiconTextView) view.findViewById(R.id.txt_status);
		if (contact.getStatus() == null || contact.getStatus().length() == 0
				|| contact.getShow_status() == false)
			txt_chat.setText("No Status");
		else
			txt_chat.setText(contact.getStatus());
		CircleImageView profileImage = (CircleImageView) view
				.findViewById(R.id.img_contact);

//		CircularImageView profileImage = (CircularImageView) view
//				.findViewById(R.id.img_contact);
//		profileImage.setImageResource(R.drawable.contact);
		String pic=contact.getPic_url();
		image=new AQuery(this.ctx);

		if (pic.length()>1) {

			image.id(profileImage).image(contact.getPic_url(),true,true);
			//ImageLoader.getInstance().displayImage(contact.getPic_url(),
					//profileImage, UIApplication.diRoundOptions);
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



	// Filter Class
	public void filter(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());
		allContacts.clear();
		if (charText.length() == 0) {
			allContacts.addAll(arraylist);
		}
		else
		{
			for (HiHelloContact wp : arraylist)
			{
				if (wp.getDisplayName().toLowerCase(Locale.getDefault()).contains(charText))
				{
					allContacts.add(wp);
				}
			}
		}
		notifyDataSetChanged();
	}

	public void setContacts(ArrayList<HiHelloContact> allContact) {
		this.allContacts = allContact;

	}

}
