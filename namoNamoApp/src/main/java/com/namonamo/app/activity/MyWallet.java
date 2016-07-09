package com.namonamo.app.activity;

import java.util.ArrayList;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.namonamo.app.R;
import com.namonamo.app.apicall.GetUserRewardApiCall;
import com.namonamo.app.constant.Hihelloconstant;
import com.namonamo.app.constant.NamoNamoSharedPrefrence;
import com.namonamo.app.content.RewardItem;
import com.namonamo.app.service.Servicable;
import com.namonamo.app.service.Servicable.ServiceListener;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MyWallet extends BaseActivity {
	SharedPreferences pref_reward;
	SharedPreferences.Editor editor_reward ;
	TextView text_reward;
	private ProgressBar progressBar;
	ArrayList<RewardItem> rewards = new ArrayList<RewardItem>();
	private ServiceListener listener = new ServiceListener() {

		@Override
		public void onComplete(Servicable<?> service) {
			TextView txt_message = (TextView) findViewById(R.id.txt_message);
			rewards = ((GetUserRewardApiCall) service).getAllRewardItems();
			progressBar.setVisibility(View.GONE);
			if (rewards != null) {
				txt_message.setText("");
				MyRewardAdapter adapter = new MyRewardAdapter();
				ListView listView = (ListView) findViewById(R.id.listView);
				listView.setAdapter(adapter);
				int totalReward = 0;
				for (RewardItem item : rewards) {
					totalReward += item.getReward();
				}
//				Hihelloconstant.reward=totalReward;
//				TextView txt_reward = (TextView) findViewById(R.id.txt_rewards);
//				txt_reward.setText(totalReward + "");
				text_reward.setText("Rs."+totalReward);
				editor_reward = pref_reward.edit();
				editor_reward.putString("reward", text_reward.getText().toString());
				editor_reward.commit();

			} else {
				txt_message.setText("No Rewards Found");
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_wallet);
		Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//		getSupportActionBar().setHomeAsUpIndicator(R.drawable.leftback);
		getSupportActionBar().setTitle("My Wallet");
		text_reward=(TextView)toolbar.findViewById(R.id.txt_reward);
		pref_reward=getSharedPreferences("pref_reward", 1);
//		ActionBar actionBar = getActionBar();
//		getActionBar().hide();
//		actionBar.setDisplayHomeAsUpEnabled(true);
//		actionBar.setHomeButtonEnabled(true);
		fetchMyRewards();
//		CircularImageView img_contact = (CircularImageView) findViewById(R.id.img_contact);
//		ImageLoader
//				.getInstance()
//				.displayImage(
//						NamoNamoSharedPrefrence
//								.getProfileImageUrl(getApplicationContext()),
//						img_contact, UIApplication.diRoundOptions);
//		TextView txt_name = (TextView) findViewById(R.id.txt_name);
//		txt_name.setText(NamoNamoSharedPrefrence
//				.getUserName(getApplicationContext()));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		switch (menuItem.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		}
		return (super.onOptionsItemSelected(menuItem));
	}

	private void fetchMyRewards() {
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		GetUserRewardApiCall api = new GetUserRewardApiCall(
				NamoNamoSharedPrefrence.getUserId(getApplicationContext()));
		api.runAsync(listener);

	}

	class MyRewardAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return rewards.size();
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
			RewardItem reward = rewards.get(position);
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(R.layout.text_layout,
						null);
			}
			ImageView imageView=(ImageView)convertView.findViewById(R.id.imageView);
			TextView textView = (TextView) convertView
					.findViewById(R.id.textView);
			textView.setText("  "+"Points from " + reward.getType().toLowerCase()
					+ ": ");
			TextView text_point = (TextView) convertView
					.findViewById(R.id.text_point);
//			text_point.setText(reward.getReward() + "");
//			int imageId = getImageRewardType(reward.getType());
//			convertView.findViewById(R.id.layout_container)
//					.setBackgroundResource(imageId);

			String firstLetter = String.valueOf( reward.getType().charAt(0));

			ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT

			int color = generator.getRandomColor();

			TextDrawable drawable = TextDrawable.builder()
					.buildRound(firstLetter, color);
			imageView.setImageDrawable(drawable);



			String secondLetter = String.valueOf( reward.getReward());

			ColorGenerator generator1 = ColorGenerator.MATERIAL; // or use DEFAULT

			int color1 = generator1.getRandomColor();

			TextDrawable drawable1 = TextDrawable.builder()
					.buildRound(secondLetter, color1);
			text_point.setBackground(drawable1);

			return convertView;
		}
	}

//	public int getImageRewardType(String type) {
//		if (type.equalsIgnoreCase("location"))
//			return R.drawable.reward_location;
//		if (type.equalsIgnoreCase("text"))
//			return R.drawable.reward_text;
//		if (type.equalsIgnoreCase("image"))
//			return R.drawable.reward_image;
//		if (type.equalsIgnoreCase("audio"))
//			return R.drawable.reward_audio;
//		if (type.equalsIgnoreCase("video"))
//			return R.drawable.reward_video;
//		if (type.equalsIgnoreCase("invite"))
//			return R.drawable.reward_invite;
//		if (type.equalsIgnoreCase("refertofriend"))
//			return R.drawable.reward_reffer;
//		return R.drawable.reward_text;
//	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.wallet_menu, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		Intent i=new Intent(MyWallet.this,HomeMain_activity.class);
		startActivity(i);
		finish();
	}
}