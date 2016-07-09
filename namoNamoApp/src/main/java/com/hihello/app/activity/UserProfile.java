package com.hihello.app.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.PhoneLookup;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.hihello.app.R;
import com.hihello.app.adapter.OtherContactDetailAdapter;
import com.hihello.app.apicall.GetUserByJidApiCall;
import com.hihello.app.common.ContactUtil;
import com.hihello.app.db.HiHelloContact;
import com.hihello.app.db.hiHelloContactDBService;
import com.hihello.app.service.Servicable;
import com.hihello.app.service.Servicable.ServiceListener;
import com.nostra13.universalimageloader.core.ImageLoader;

public class UserProfile extends BaseActivity {
	private String jid;
	private HiHelloContact contact;
	private ImageView img_contact;
	private TextView txt_name;
	private TextView txt_status_date;
	private TextView txt_status;
	private TextView txt_type;
	private TextView txt_number;
	ArrayList<String> phoneArray = new ArrayList<String>();
	ArrayList<String> emailArray = new ArrayList<String>();
	private OnClickListener contactClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(UserProfile.this, ImageShow.class);
			Bundle bnd = new Bundle();
			bnd.putString("PATH", contact.getPic_url());
			bnd.putString("TITLE", contact.getDisplayName());

			intent.putExtras(bnd);
			startActivity(intent);
		}
	};
	private OnClickListener dialClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			otherContactListener.onPhoneClick(contact.getMobile_number());
		}
	};
	private OnClickListener messageClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			otherContactListener.onMessageClick(contact.getMobile_number());
		}
	};

	private IOtherContactListener otherContactListener = new IOtherContactListener() {

		@Override
		public void onEmailClick(String email) {
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/html");
			intent.putExtra(Intent.EXTRA_EMAIL, new String[] { email });
			intent.putExtra(Intent.EXTRA_SUBJECT, "");
			intent.putExtra(Intent.EXTRA_TEXT, "");
			startActivity(Intent.createChooser(intent, "Send Email"));
		}

		@Override
		public void onPhoneClick(String phone) {
			String uri = "tel:" + phone;
			Intent intent = new Intent(Intent.ACTION_DIAL);
			intent.setData(Uri.parse(uri));
			startActivity(intent);
		}

		@Override
		public void onMessageClick(String phone) {
			Uri uri = Uri.parse("smsto:" + phone);
			Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
			startActivity(intent);

		}

	};
	private Menu menu;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.profile_menu, menu);
		this.menu = menu;
		if (contact != null)
			menu.getItem(0).setVisible(false);
		return true;
	}

	private ServiceListener listener = new ServiceListener() {

		@Override
		public void onComplete(Servicable<?> service) {
			contact = ((GetUserByJidApiCall) service).getAllRewardItems();
			initializeControls();
			menu.getItem(0).setVisible(false);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_profile);
//		ActionBar actionBar = getActionBar();
//		actionBar.setDisplayHomeAsUpEnabled(true);
//		actionBar.setHomeButtonEnabled(true);
		Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		jid="";
//		jid = getIntent().getExtras().getString("JID");
		contact = hiHelloContactDBService.fetchContactByJID(
				getApplicationContext(), jid);
		if (contact != null) {
			initializeControls();
		} else {
			fetchContactFromServer();
		}
	}

	private void fetchContactFromServer() {
		GetUserByJidApiCall api = new GetUserByJidApiCall(
				getApplicationContext(), jid);
		api.runAsync(listener);
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

	private void initializeControls() {
		findViewById(R.id.layout_user_status).setVisibility(View.VISIBLE);
		findViewById(R.id.layout_user_phone).setVisibility(View.VISIBLE);

		img_contact = (ImageView) findViewById(R.id.img_contact);

		img_contact.setOnClickListener(contactClick);
		txt_name = (TextView) findViewById(R.id.txt_name);
		txt_status_date = (TextView) findViewById(R.id.txt_status_date);
		txt_status = (TextView) findViewById(R.id.txt_status);
		txt_type = (TextView) findViewById(R.id.txt_type);
		txt_number = (TextView) findViewById(R.id.txt_number);
		if(contact.getShow_profile())
			ImageLoader.getInstance().displayImage(contact.getPic_url(),
				img_contact, UIApplication.diRoundOptions);
//		getActionBar().setTitle(contact.getDisplayName() + "'s profile");
		txt_name.setText(contact.getDisplayName());
		if(contact.getShow_status())
			txt_status.setText(contact.getStatus());
		else
			txt_status.setText("No Status");
		txt_number.setText(contact.getMobile_number());
		findViewById(R.id.img_dial).setOnClickListener(dialClick);
		findViewById(R.id.img_message).setOnClickListener(messageClick);

		if (contact.getLast_status_update() > 0) {
			Date chat_date = new Date(contact.getLast_status_update() * 1000);
			Date today = new Date();
			if (chat_date.getYear() == today.getYear()
					&& chat_date.getMonth() == today.getMonth()
					&& chat_date.getDate() == today.getDate()) {
				SimpleDateFormat readFormat = new SimpleDateFormat("hh:mm aa");
				txt_status_date.setText(readFormat.format(chat_date));
			} else if (chat_date.getYear() == today.getYear()
					&& chat_date.getMonth() == today.getMonth()
					&& chat_date.getDate() == today.getDate() - 1) {
				txt_status_date.setText("Yesterday");
			} else {
				SimpleDateFormat readFormat = new SimpleDateFormat("dd/MM/yyyy");
				txt_status_date.setText(readFormat.format(chat_date));
			}
		}
		fetchOtherInformation();
		showOtherInformation();
	}

	public interface IOtherContactListener {
		void onEmailClick(String email);

		void onPhoneClick(String phone);

		void onMessageClick(String phone);

	}

	private void showOtherInformation() {
		findViewById(R.id.layout_other_contact).setVisibility(View.GONE);
		if (phoneArray.size() > 0 || emailArray.size() > 0) {
			findViewById(R.id.layout_other_contact).setVisibility(View.VISIBLE);
			LinearLayout listOtherContact = (LinearLayout) findViewById(R.id.list_other_contact);
			OtherContactDetailAdapter adapter = new OtherContactDetailAdapter(
					getLayoutInflater(), phoneArray, emailArray,
					getApplicationContext(), otherContactListener);
			int count = adapter.getCount();
			for (int index = 0; index < count; index++) {
				View view = adapter.getView(index, null, null);
				listOtherContact.addView(view);
			}
		}
	}

	private void fetchOtherInformation() {

		Cursor cursor = null;
		try {
			ContentResolver cr = getContentResolver();
			Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
					Uri.encode(contact.getMobile_number()));
			cursor = cr.query(uri, new String[] { PhoneLookup._ID }, null,
					null, null);
			if (cursor == null) {
				return;
			}
			if (cursor.moveToFirst()) {
				String id = cursor.getString(cursor
						.getColumnIndex(PhoneLookup._ID));
				phoneArray.addAll(ContactUtil.getAllPhoneNumber(id,
						getContentResolver()));
				emailArray.addAll(ContactUtil.getAllEmail(id,
						getContentResolver()));
			}
		} catch (Exception x) {

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}

		}
	}
}
