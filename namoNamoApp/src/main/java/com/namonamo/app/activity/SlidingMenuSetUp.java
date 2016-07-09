package com.namonamo.app.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.namonamo.app.R;
import com.namonamo.app.adapter.MenuAdapter;
import com.namonamo.app.apicall.AddRewardApiCall;
import com.namonamo.app.common.AppController;
import com.namonamo.app.constant.NamoNamoSharedPrefrence;
import com.namonamo.app.content.NamoNamoMenuItem;

public class SlidingMenuSetUp {

	private RecentChats recentChat;
	private String title;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;

	DrawerLayout setUpDrawerMenu(RecentChats _recentChat) {
		this.recentChat = _recentChat;
		mDrawerLayout = (DrawerLayout) _recentChat
				.findViewById(R.id.drawer_layout);
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);

		title = recentChat.getResources().getString(R.string.namonamochat);

		ArrayList<NamoNamoMenuItem> items = setUpMenuItems();
		final View menuHeader = recentChat.getLayoutInflater().inflate(
				R.layout.menu_header, null);
		ListView mDrawerList = (ListView) recentChat
				.findViewById(R.id.left_list);

		mDrawerList.addHeaderView(menuHeader);
		MenuAdapter adapter = new MenuAdapter(recentChat.getLayoutInflater(),
				items);
		mDrawerList.setAdapter(adapter);
		mDrawerList
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						mDrawerLayout.closeDrawers();
						Intent intent = AppController.getIntentForMenu(
								recentChat, position);
						if (intent != null)
							recentChat.startActivity(intent);
						if (position == AppController.ACTION_LIKE_US) {
							if (!NamoNamoSharedPrefrence.isFBLike(recentChat)) {
								AddRewardApiCall api = new AddRewardApiCall(
										recentChat,
										NamoNamoSharedPrefrence
												.getUserId(recentChat),
										NamoNamoSharedPrefrence
												.getMobileNo(recentChat), 50,
										"FACEBOOK LIKE");
								api.runAsync(null);
							}
						} else if (position == AppController.ACTION_RATE_US) {
							if (!NamoNamoSharedPrefrence.isRateUs(recentChat)) {
								AddRewardApiCall api = new AddRewardApiCall(
										recentChat,
										NamoNamoSharedPrefrence
												.getUserId(recentChat),
										NamoNamoSharedPrefrence
												.getMobileNo(recentChat), 50,
										"RATING APP");
								api.runAsync(null);
							}
						}
					}
				});

		setmDrawerToggle(new ActionBarDrawerToggle(recentChat, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer icon to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description */
		R.string.drawer_close /* "close drawer" description */) {

			/** Called when a drawer has settled in a completely closed state. */

			public void onDrawerClosed(View view) {
				recentChat.getSupportActionBar().setTitle(title);

				super.onDrawerClosed(view);
			}

			/** Called when a drawer has settled in a completely open state. */

			public void onDrawerOpened(View drawerView) {
//				recentChat.getSupportActionBar().setTitle("Menu");
				updateProfile(menuHeader);
				super.onDrawerOpened(drawerView);
			}
		});

		mDrawerLayout.setDrawerListener(getmDrawerToggle());
		updateProfile(menuHeader);
		return mDrawerLayout;

	}

	void updateProfile(View menuHeader) {
		TextView txt_name = (TextView) menuHeader.findViewById(R.id.txt_name);
		txt_name.setText(NamoNamoSharedPrefrence.getUserName(recentChat));
		ImageView img_user = (ImageView) menuHeader.findViewById(R.id.img_user);
		BaseActivity.displayImage(
				NamoNamoSharedPrefrence.getProfileImageUrl(recentChat),
				img_user);
	}

	private static ArrayList<NamoNamoMenuItem> setUpMenuItems() {
		ArrayList<NamoNamoMenuItem> items = new ArrayList<NamoNamoMenuItem>();
		items.add(new NamoNamoMenuItem("New Chat", R.drawable.menu_new_chat));
		items.add(new NamoNamoMenuItem("My Profile", R.drawable.my_profile));
		items.add(new NamoNamoMenuItem("Setting", R.drawable.setting));
		items.add(new NamoNamoMenuItem("My Wallet", R.drawable.my_wallet));
		items.add(new NamoNamoMenuItem("Special Offer",
				R.drawable.ic_action_all_friends_light));
		items.add(new NamoNamoMenuItem("Earn Points", R.drawable.earn_points));
		items.add(new NamoNamoMenuItem("Ranking in Friends", R.drawable.ranks));
		items.add(new NamoNamoMenuItem("Invite Friends",
				R.drawable.invite_friends));
		items.add(new NamoNamoMenuItem("Contacts", R.drawable.contacts));

		items.add(new NamoNamoMenuItem("Rate Us", R.drawable.rate_us));
		items.add(new NamoNamoMenuItem("Like Us", R.drawable.like_us));
		items.add(new NamoNamoMenuItem("About NamoNamo",
				R.drawable.about_namonamo));
		return items;

	}

	public void setmDrawerToggle(ActionBarDrawerToggle mDrawerToggle) {
		this.mDrawerToggle = mDrawerToggle;
	}

	public ActionBarDrawerToggle getmDrawerToggle() {
		return mDrawerToggle;
	}

}
