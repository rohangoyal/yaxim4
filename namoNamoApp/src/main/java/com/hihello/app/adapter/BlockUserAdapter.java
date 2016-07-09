package com.hihello.app.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hihello.app.R;
import com.hihello.app.activity.CircularImageView;
import com.hihello.app.activity.UIApplication;
import com.hihello.app.common.ContactUtil;
import com.hihello.app.db.BlockUser;
import com.nostra13.universalimageloader.core.ImageLoader;

public class BlockUserAdapter extends BaseAdapter {

	private ArrayList<BlockUser> blockedUser;
	private Context mContext;
	private LayoutInflater layoutInflater;

	public BlockUserAdapter(Context mContext, ArrayList<BlockUser> blockedUser) {
		this.blockedUser = blockedUser;
		this.mContext = mContext;
		layoutInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount() {
		return blockedUser.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = layoutInflater.inflate(R.layout.block_user_cell, null);
		BlockUser user = blockedUser.get(position);
		CircularImageView profileImage = (CircularImageView) convertView
				.findViewById(R.id.profileImage);
		TextView txt_name = (TextView) convertView.findViewById(R.id.txt_name);
		String name = ContactUtil.getContactName(mContext, user.getMobileNo());
		if (name == null || name.length() == 0) {
			name = user.getMobileNo();
		}
		txt_name.setText(name);
		ImageLoader.getInstance().displayImage(user.getProfilePic(),
				profileImage, UIApplication.diRoundOptions);
		return convertView;
	}

	public void setBlockUser(ArrayList<BlockUser> blockedUser2) {
		blockedUser = blockedUser2;		
	}
}