package com.hihello.app.activity;

import java.util.ArrayList;
import java.util.Date;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnActionExpandListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;


import com.hihello.androidclient.XMPPRosterServiceAdapter;
import com.hihello.androidclient.chat.ChatWindow;
import com.hihello.androidclient.data.ChatProvider;
import com.hihello.androidclient.data.ChatProvider.ChatConstants;
import com.hihello.androidclient.data.RosterProvider;
import com.hihello.androidclient.service.IXMPPRosterService;
import com.hihello.androidclient.service.XMPPService;
import com.hihello.androidclient.util.PreferenceConstants;
import com.hihello.androidclient.util.StatusMode;
import com.hihello.app.R;
import com.hihello.app.adapter.RecentChatAdapter;
import com.hihello.app.apicall.BlockUserApiCall;
import com.hihello.app.apicall.DeleteBlockUserApiCall;
import com.hihello.app.common.ContactUtil;
import com.hihello.app.common.Log;
import com.hihello.app.constant.HiHelloSharedPrefrence;
import com.hihello.app.db.BlockUserDBService;
import com.hihello.app.db.HiHelloContact;
import com.hihello.app.db.hiHelloContactDBService;
import com.hihello.app.db.RecentChat;
import com.hihello.app.db.RecentChatDBService;
import com.hihello.app.service.Servicable;
import com.hihello.app.service.Servicable.ServiceListener;

public class RecentChats extends BaseActivity implements
		LoaderManager.LoaderCallbacks<Cursor> {

	private static final String TAG = "Recent Chats";

	private DrawerLayout mDrawerLayout;

	private ActionBar actionBar;
	private Intent xmppServiceIntent;
	ArrayAdapter<String> chatOptionMenu;
	private ServiceConnection xmppServiceConnection;
	private ContentObserver mContactObserver = new ContactObserver();
	private ArrayList<RecentChat> allRecentChats;
	private RecentChatAdapter recentChatAdapter;
	private ListView listView;
	private OnItemClickListener chatItemClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			if (arg2 == 0) {
				Intent intent = new Intent(RecentChats.this,
						DailyMessageActivity.class);
				startActivity(intent);
			} else {
				RecentChat contact = showContact.get(arg2 - 1);
				if (contact.getDisplayName() == null) {
					contact.setDisplayName(contact.getMobileNo());
				}
				startChatActivity(contact.getUserJID(),
						contact.getDisplayName(), null);
			}
		}
	};

	private ServiceListener userListener = new ServiceListener() {
		@Override
		public void onComplete(Servicable<?> service) {
			showRecentChats();
		}
	};
	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			showRecentChats();
		}
	};

	protected XMPPRosterServiceAdapter serviceAdapter;

	public void setAndSaveStatus(StatusMode statusMode, String message) {
		SharedPreferences.Editor prefedit = PreferenceManager
				.getDefaultSharedPreferences(this).edit();
		// do not save "offline" to prefs, or else!
		if (statusMode != StatusMode.offline)
			prefedit.putString(PreferenceConstants.STATUS_MODE,
					statusMode.name());
		prefedit.putString(PreferenceConstants.STATUS_MESSAGE, message);
		prefedit.commit();
		serviceAdapter.setStatusFromConfig();
	}

	private void registerXMPPService() {
		xmppServiceIntent = new Intent(this, XMPPService.class);
		xmppServiceIntent.setAction("org.yaxim.androidclient.XMPPSERVICE");

		xmppServiceConnection = new ServiceConnection() {
			public void onServiceConnected(ComponentName name, IBinder service) {

				serviceAdapter = new XMPPRosterServiceAdapter(
						IXMPPRosterService.Stub.asInterface(service));
				if (serviceAdapter != null && serviceAdapter.isAuthenticated())
					setAndSaveStatus(StatusMode.chat, "online");
			}

			public void onServiceDisconnected(ComponentName name) {
				if (serviceAdapter != null && serviceAdapter.isAuthenticated())
					setAndSaveStatus(StatusMode.away, "offline");

			}
		};

	}

	private void bindXMPPService() {
		bindService(xmppServiceIntent, xmppServiceConnection, BIND_AUTO_CREATE);
	}

	private void unbindXMPPService() {
		try {
			unbindService(xmppServiceConnection);
		} catch (IllegalArgumentException e) {
			Log.e(TAG, "Service wasn't bound!");
		}
	}

	private EditText txtSearch;
	private ImageView imgClear;
	protected String searchText;
	private TextWatcher watcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			searchText = s.toString();
			showRecentChats();
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
		}
	};
	private OnActionExpandListener listener = new OnActionExpandListener() {

		@Override
		public boolean onMenuItemActionExpand(MenuItem item) {
			return true;
		}

		@Override
		public boolean onMenuItemActionCollapse(MenuItem item) {
			searchText = "";
			((EditText) item.getActionView().findViewById(R.id.txt_search))
					.setText("");
			showRecentChats();
			return true;
		}
	};

	private ArrayList<RecentChat> showContact;

	private ActionBarDrawerToggle mDrawerToggle;

	private long last_notify;

	private Handler mNotifyHandler = new Handler();

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	private void startChatActivity(String user, String userName, String message) {
		Intent chatIntent = new Intent(this, ChatWindow.class);
		Uri userNameUri = Uri.parse(user);
		chatIntent.setData(userNameUri);
		chatIntent.putExtra(ChatWindow.INTENT_EXTRA_USERNAME, userName);
		if (message != null) {
			chatIntent.putExtra(ChatWindow.INTENT_EXTRA_MESSAGE, message);
		}
		startActivity(chatIntent);
	}

	void setUpActionView(View view) {
		txtSearch = (EditText) view.findViewById(R.id.txt_search);
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(txtSearch, InputMethodManager.SHOW_IMPLICIT);
		imgClear = (ImageView) view.findViewById(R.id.img_search_clear);
		imgClear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				txtSearch.setText("");
			}
		});
		txtSearch.addTextChangedListener(watcher);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.recentchat_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case android.R.id.home:
			mDrawerLayout.openDrawer(Gravity.LEFT);
			break;
		case R.id.search:
			if (item.getActionView() != null)
				setUpActionView(item.getActionView());
			item.setOnActionExpandListener(listener);
		default:
			break;
		}
		return true;
	}

	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		
		
		mDrawerToggle.syncState();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		HiHelloSharedPrefrence.setChatPageInit(getApplicationContext(), true);
		setContentView(R.layout.panel_navigation);

//		Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
//		setSupportActionBar(toolbar);
//		actionBar = getSupportActionBar();
//		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//		getSupportActionBar().setHomeButtonEnabled(true);

		SlidingMenuSetUp slider_menu = new SlidingMenuSetUp();
		mDrawerLayout = slider_menu.setUpDrawerMenu(this);
		mDrawerToggle = slider_menu.getmDrawerToggle();
		// refershContacts(userListener);
		registerXMPPService();
		bindXMPPService();

	}

	@Override
	protected void onResume() {
		try {
			if (serviceAdapter != null && serviceAdapter.isAuthenticated())
				setAndSaveStatus(StatusMode.chat, "online");

			IntentFilter filter = new IntentFilter(
					RecentChatDBService.RECENT_DB_CHANGE_ACTION);
			registerReceiver(receiver, filter);
			showChatList();
			getContentResolver().registerContentObserver(
					RosterProvider.CONTENT_URI, true, mContactObserver);
		} catch (Exception x) {

		}
		super.onResume();
	}

	private class ContactObserver extends ContentObserver {
		public ContactObserver() {
			super(new Handler());
		}

		@Override
		public void onChange(boolean selfChange, Uri uri) {
			int match = RosterProvider.URI_MATCHER.match(uri);
			switch (match) {
			case RosterProvider.CONTACTS:
				notifyStatusChange();
				break;
			case RosterProvider.CONTACT_ID:
				// String segment = uri.getPathSegments().get(1);
				notifyStatusChange();
				break;
			}
		}
	}

	private Runnable mNotifyChange = new Runnable() {

		@Override
		public void run() {
			updateContactStatus();
		}
	};

	private OnItemLongClickListener chatItemLongClick = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			if (arg2 > 0) {
				showChatOptionMenu(arg2 - 1);
			}
			return false;
		}
	};

	void notifyStatusChange() {
		long ts = System.currentTimeMillis();
		if (ts > last_notify + 500)
			mNotifyChange.run();
		else
			mNotifyHandler.postDelayed(mNotifyChange, 200);
		last_notify = ts;
	}

	void removeChatHistory(final String JID) {
		getContentResolver().delete(ChatProvider.CONTENT_URI,
				ChatProvider.ChatConstants.JID + " = ?", new String[] { JID });
	}

	protected void showChatOptionMenu(final int index) {
		final RecentChat contact = showContact.get(index);
		final boolean isBlocked = BlockUserDBService.getBlockUser(
				getApplicationContext(), contact.getUserJID()) != null;

		chatOptionMenu = new ArrayAdapter<String>(RecentChats.this,
				android.R.layout.simple_list_item_1);
		chatOptionMenu.add("View Contact");
		chatOptionMenu.add(isBlocked ? "Unblock contact" : "Block contact");

		chatOptionMenu.add("Archive conversation");
		chatOptionMenu.add("Delete conversation");

		AlertDialog.Builder builderSingle = new AlertDialog.Builder(
				RecentChats.this);
		builderSingle.setAdapter(chatOptionMenu,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							Intent intent = new Intent(RecentChats.this,
									UserProfile.class);
							intent.putExtra("JID", contact.getUserJID());
							startActivity(intent);
							break;
						case 1:
							if (isBlocked)
								doUnBlockContact(showContact.get(index));
							else
								doBlockContact(showContact.get(index));
							break;
						case 2:
							// removeChatHistory(contact.getUserJID());
							RecentChatDBService.removeRecentChat(
									getApplicationContext(),
									contact.getUserJID());
							showRecentChats();
							break;
						case 3:
							removeChatHistory(contact.getUserJID());
							RecentChatDBService.removeRecentChat(
									getApplicationContext(),
									contact.getUserJID());
							showRecentChats();
							break;
						}
					}
				});
		builderSingle.show();

	}

	protected void doUnBlockContact(RecentChat recentChat) {
		DeleteBlockUserApiCall api = new DeleteBlockUserApiCall(
				getApplicationContext(), recentChat.getUserJID());
		api.runAsync(null);
	}

	protected void doBlockContact(RecentChat recentChat) {
		BlockUserApiCall api = new BlockUserApiCall(getApplicationContext(),
				recentChat.getUserJID());
		api.runAsync(null);
	}

	@Override
	protected void onPause() {
		unregisterReceiver(receiver);
		getContentResolver().unregisterContentObserver(mContactObserver);
		super.onPause();
	}

	private static final String[] STATUS_QUERY = new String[] {
			RosterProvider.RosterConstants.ALIAS,
			RosterProvider.RosterConstants.STATUS_MODE,
			RosterProvider.RosterConstants.JID,
			RosterProvider.RosterConstants.STATUS_MESSAGE,

	};

	public void updateContactStatus() {
		setAllOffLine();
		if (serviceAdapter == null || !serviceAdapter.isAuthenticated())
			return;
		Cursor cursor = getContentResolver().query(RosterProvider.CONTENT_URI,
				STATUS_QUERY, null, null, null);
		// int ALIAS_IDX = cursor
		// .getColumnIndex(RosterProvider.RosterConstants.ALIAS);
		// int MODE_IDX = cursor
		// .getColumnIndex(RosterProvider.RosterConstants.STATUS_MODE);
		int JID_IDX = cursor.getColumnIndex(RosterProvider.RosterConstants.JID);
		int MSG_IDX = cursor
				.getColumnIndex(RosterProvider.RosterConstants.STATUS_MESSAGE);

		if (cursor.moveToFirst()) {
			do {
				// int status_mode = cursor.getInt(MODE_IDX);
				String status_message = cursor.getString(MSG_IDX);
				String jid = cursor.getString(JID_IDX);

				if (status_message != null
						&& status_message.equalsIgnoreCase("online"))
					for (RecentChat chat : allRecentChats)
						if (chat.getUserJID().equalsIgnoreCase(jid)) {
							chat.setStatus("online");
							break;
						}

			} while (cursor.moveToNext());
		}
		cursor.close();
		if (recentChatAdapter != null)
			recentChatAdapter.notifyDataSetChanged();
	}

	void setAllOffLine() {
		for (RecentChat chat : allRecentChats)
			chat.setStatus("");
	}

	@Override
	protected void onDestroy() {
		if (serviceAdapter != null && serviceAdapter.isAuthenticated())
			setAndSaveStatus(StatusMode.available, new Date().getTime() + "");
		unbindXMPPService();
		super.onDestroy();
	}

	private void showChatList() {
		listView = (ListView) findViewById(R.id.chat_list);
		listView.setOnItemClickListener(chatItemClick);
		listView.setOnItemLongClickListener(chatItemLongClick);

		showRecentChats();
	}

	void setLastChatDetail(RecentChat chat) {
		String selection = ChatConstants.JID + "='" + chat.getUserJID() + "'";
		Cursor _cur = getContentResolver().query(ChatProvider.CONTENT_URI,
				null, selection, null, null);
		if (_cur.moveToLast()) {
			chat.setMessage(_cur.getString(_cur
					.getColumnIndex(ChatProvider.ChatConstants.MESSAGE)));
			chat.setFromMe(_cur.getInt(_cur
					.getColumnIndex(ChatProvider.ChatConstants.DIRECTION)) == ChatConstants.OUTGOING);
			chat.setDeliveryStatus(_cur.getInt(_cur
					.getColumnIndex(ChatProvider.ChatConstants.DELIVERY_STATUS)));

		} else {
			chat.setMessage("");
			chat.setFromMe(false);
			chat.setDeliveryStatus(-1);
		}
		_cur.close();
	}

	private void showRecentChats() {

		allRecentChats = RecentChatDBService
				.getAllRecentChat(getApplicationContext());

		if (showContact != null)
			showContact.clear();
		else
			showContact = new ArrayList<RecentChat>();

		for (RecentChat chat : allRecentChats) {
			HiHelloContact contact = hiHelloContactDBService
					.fetchContactByJID(getApplicationContext(),
							chat.getUserJID());
			if (contact != null) {
				chat.setProfilePic(contact.getPic_url());
				chat.setDisplayName(contact.getDisplayName());
			}
			if (chat.getDisplayName() == null
					|| chat.getDisplayName().length() == 0) {
				String name = ContactUtil.getContactName(
						getApplicationContext(), chat.getMobileNo());
				if (name != null && name.length() > 0) {
					chat.setDisplayName(name);
				}
			}
			setLastChatDetail(chat);
			if (searchText != null
					&& chat.getDisplayName() != null
					&& chat.getDisplayName().toUpperCase()
							.contains(searchText.toUpperCase())) {
				showContact.add(chat);
			}
		}

		if (searchText == null || searchText.length() == 0) {
			showContact = allRecentChats;
		}
		updateContactStatus();

		if (recentChatAdapter == null) {
			recentChatAdapter = new RecentChatAdapter(getLayoutInflater(),
					showContact, getApplicationContext(), true, true);
			listView.setAdapter(recentChatAdapter);
		} else {
			recentChatAdapter.setRecentChats(showContact);
			recentChatAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub

	}

}