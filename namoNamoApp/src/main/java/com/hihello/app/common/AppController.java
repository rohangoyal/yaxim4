package com.hihello.app.common;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;

import com.hihello.app.activity.AllContacts;
import com.hihello.app.activity.EarnPoint;
import com.hihello.app.activity.MyProfile;
import com.hihello.app.activity.MyWallet;
import com.hihello.app.activity.Ranking;
import com.hihello.app.activity.SettingPrefs;

public class AppController {

	public static final int ACTION_HIHELLO_CONTACT = 1;
	public static final int ACTION_MY_PROFILE = 2;
	public static final int ACTION_SETTING = 3;
	public static final int ACTION_MY_WALLET = 4;
	public static final int ACTION_SPECIAL_OFFER = 5;
	public static final int ACTION_EARN_POINT = 6;
	public static final int ACTION_RANKING = 7;
	public static final int ACTION_INVITE = 8;
	public static final int ACTION_CONTACTS = 9;
	public static final int ACTION_RATE_US = 10;
	public static final int ACTION_LIKE_US = 11;
	public static final int ACTION_ABOUT = 12;

	public static Intent getIntentForMenu(Context ctx, int position) {
		Intent intent = null;
		switch (position) {
		case ACTION_HIHELLO_CONTACT:
			intent = new Intent(ctx, AllContacts.class);
			break;
		case ACTION_MY_PROFILE:
			intent = new Intent(ctx, MyProfile.class);
			break;
		case ACTION_SETTING:
			intent = new Intent(ctx, SettingPrefs.class);
			break;
		case ACTION_MY_WALLET:
			intent = new Intent(ctx, MyWallet.class);
			break;
		case ACTION_SPECIAL_OFFER:
			intent = new Intent(ctx, MyWallet.class);
			break;
		case ACTION_EARN_POINT:
			intent = new Intent(ctx, EarnPoint.class);
			break;
		case ACTION_RANKING:
			intent = new Intent(ctx, Ranking.class);
			break;
		case ACTION_INVITE:
			// Invite friend
			intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/plain");
			String text = "Hey, I am using Hi Hello App! Download Link: https://play.google.com/store/apps/details?id=com.hihello.app";
			intent.putExtra(Intent.EXTRA_TEXT, text);//
			intent = Intent.createChooser(intent, "");
			break;
		case ACTION_CONTACTS:
			// Contacts
			intent = new Intent(Intent.ACTION_VIEW,
					ContactsContract.Contacts.CONTENT_URI);
			break;
		case ACTION_RATE_US:
			String rateUsUrl = "https://play.google.com/store/apps/details?id=com.hihello.app";
			intent = new Intent(Intent.ACTION_VIEW, Uri.parse(rateUsUrl));
			break;
		case ACTION_LIKE_US:
			// Like us
			String url = "https://www.facebook.com/pages/Namo-Namo-App/1542926385962303";
			intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			break;
		case ACTION_ABOUT:
			String aboutUrl = "http://www.hihelloapp.com";
			intent = new Intent(Intent.ACTION_VIEW, Uri.parse(aboutUrl));
			break;
		}
		return intent;

	}
	
}
