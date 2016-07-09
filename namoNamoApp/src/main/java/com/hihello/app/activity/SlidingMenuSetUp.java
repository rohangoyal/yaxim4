package com.hihello.app.activity;

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

import com.hihello.app.R;
import com.hihello.app.adapter.MenuAdapter;
import com.hihello.app.apicall.AddRewardApiCall;
import com.hihello.app.common.AppController;
import com.hihello.app.constant.HiHelloSharedPrefrence;
import com.hihello.app.content.HiHelloMenuItem;

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

		title = recentChat.getResources().getString(R.string.hihellochat);

		ArrayList<HiHelloMenuItem> items = setUpMenuItems();
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
							if (!HiHelloSharedPrefrence.isFBLike(recentChat)) {
								AddRewardApiCall api = new AddRewardApiCall(
										recentChat,
										HiHelloSharedPrefrence
												.getUserId(recentChat),
										HiHelloSharedPrefrence
												.getMobileNo(recentChat), 50,
										"FACEBOOK LIKE");
								api.runAsync(null);
							}
						} else if (position == AppController.ACTION_RATE_US) {
							if (!HiHelloSharedPrefrence.isRateUs(recentChat)) {
								AddRewardApiCall api = new AddRewardApiCall(
										recentChat,
										HiHelloSharedPrefrence
												.getUserId(recentChat),
										HiHelloSharedPrefrence
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
		txt_name.setText(HiHelloSharedPrefrence.getUserName(recentChat));
		ImageView img_user = (ImageView) menuHeader.findViewById(R.id.img_user);
		BaseActivity.displayImage(
				HiHelloSharedPrefrence.getProfileImageUrl(recentChat),
				img_user);
	}

	private static ArrayList<HiHelloMenuItem> setUpMenuItems() {
		ArrayList<HiHelloMenuItem> items = new ArrayList<HiHelloMenuItem>();
		items.add(new HiHelloMenuItem("New Chat", R.drawable.menu_new_chat));
		items.add(new HiHelloMenuItem("My Profile", R.drawable.contact1));
		items.add(new HiHelloMenuItem("Setting", R.drawable.setting));
		items.add(new HiHelloMenuItem("My Wallet", R.drawable.my_wallet));
		items.add(new HiHelloMenuItem("Special Offer",
				R.drawable.ic_action_all_friends_light));
		items.add(new HiHelloMenuItem("Earn Points", R.drawable.earn_points));
		items.add(new HiHelloMenuItem("Ranking in Friends", R.drawable.ranks));
		items.add(new HiHelloMenuItem("Invite Friends",
				R.drawable.invite_friends));
		items.add(new HiHelloMenuItem("Contacts", R.drawable.contacts));

		items.add(new HiHelloMenuItem("Rate Us", R.drawable.rate_us));
		items.add(new HiHelloMenuItem("Like Us", R.drawable.like_us));
		items.add(new HiHelloMenuItem("About HiHello",
				R.drawable.about_hihello));
		return items;

	}

	public void setmDrawerToggle(ActionBarDrawerToggle mDrawerToggle) {
		this.mDrawerToggle = mDrawerToggle;
	}

	public ActionBarDrawerToggle getmDrawerToggle() {
		return mDrawerToggle;
	}

}
