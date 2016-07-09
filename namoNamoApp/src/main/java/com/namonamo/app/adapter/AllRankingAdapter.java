package com.namonamo.app.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.namonamo.app.R;
import com.namonamo.app.activity.CircularImageView;
import com.namonamo.app.activity.UIApplication;
import com.namonamo.app.constant.NamoNamoSharedPrefrence;
import com.namonamo.app.content.RewardItem;
import com.nostra13.universalimageloader.core.ImageLoader;

public class AllRankingAdapter extends BaseAdapter {

	private ArrayList<RewardItem> items;
	private Context mContext;
	private float radius;
	String rank;
	TextView txt_rank;

	public AllRankingAdapter(Context ctx, ArrayList<RewardItem> rewardItems) {
		this.mContext = ctx;
		this.items = rewardItems;
		DisplayMetrics outMetrics = new DisplayMetrics();
		((WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay().getMetrics(outMetrics);
		this.radius = outMetrics.density * 70;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}

	@Override
	public Object getItem(int arg0) {
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
		RewardItem item = items.get(position);
		if (convertView == null) {
			convertView = ((LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
					.inflate(R.layout.rank_cell, null);
		}
		((TextView) convertView.findViewById(R.id.txt_name)).setText(item
				.getFirstname());
//		.setText(""
//				+ (position + 1));
		txt_rank=(TextView) convertView.findViewById(R.id.txt_rank);
		rank=String.valueOf(position+1);
		((TextView) convertView.findViewById(R.id.txt_point)).setText(item
				.getReward() + "");
		CircularImageView img_contact = (CircularImageView) convertView
				.findViewById(R.id.img_contact);
		ImageLoader.getInstance().displayImage(
				item.getPic_url(),
				img_contact, UIApplication.getRoundedBO((int) radius));


		String secondLetter = String.valueOf(rank);
		ColorGenerator generator1 = ColorGenerator.MATERIAL; // or use DEFAULT
		int color1 = generator1.getRandomColor();
		TextDrawable drawable1 = TextDrawable.builder()
				.buildRound(secondLetter, color1);
		((TextView) convertView.findViewById(R.id.txt_rank)).setBackground(drawable1);

		return convertView;
	}

}
