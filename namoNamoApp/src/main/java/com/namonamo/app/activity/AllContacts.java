package com.namonamo.app.activity;

import java.util.ArrayList;

import android.app.ActionBar;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnActionExpandListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;


import com.namonamo.androidclient.XMPPRosterServiceAdapter;
import com.namonamo.androidclient.chat.ChatWindow;
import com.namonamo.androidclient.service.IXMPPRosterService;
import com.namonamo.androidclient.service.XMPPService;
import com.namonamo.app.R;
import com.namonamo.app.adapter.InviteContactAdapter;
import com.namonamo.app.adapter.MergeAdapter;
import com.namonamo.app.adapter.NamoNamoContactAdapter;
import com.namonamo.app.apicall.DownloadRegisterUserApiCall;
import com.namonamo.app.apicall.SendInvitationApiCall;
import com.namonamo.app.common.ContactUtil;
import com.namonamo.app.common.Log;
import com.namonamo.app.constant.Hihelloconstant;
import com.namonamo.app.constant.NamoNamoConstant;
import com.namonamo.app.constant.NamoNamoSharedPrefrence;
import com.namonamo.app.content.InviteContact;
import com.namonamo.app.db.NamoNamoContact;
import com.namonamo.app.db.NamoNamoContactDBService;
import com.namonamo.app.service.Servicable;
import com.namonamo.app.service.Servicable.ServiceListener;

public class AllContacts extends BaseActivity {
	protected static final String TAG = "ALL CONTACT";
	private Intent xmppServiceIntent;
	private ServiceConnection xmppServiceConnection;
	private ActionBar actionBar;
	protected ArrayList<NamoNamoContact> allContact;
	private ServiceListener userListener = new ServiceListener() {

		@Override
		public void onComplete(Servicable<?> service) {

			dismissProgress();
			if (service instanceof DownloadRegisterUserApiCall
					&& serviceAdapter != null
					&& serviceAdapter.isAuthenticated()) {

				final ArrayList<NamoNamoContact> newContacts = ((DownloadRegisterUserApiCall) service)
						.getAllContact();
				if (newContacts != null && newContacts.size() > 0) {
					new AsyncTask<Void, Void, Void>() {
						@Override
						protected Void doInBackground(Void... params) {
							for (NamoNamoContact contact : newContacts) {
								serviceAdapter.sendPresenceRequest(
										contact.getJid(), "subscribed");

								if (!serviceAdapter.hasRosterItem(contact
										.getJid()))

									serviceAdapter.addRosterItem(
											contact.getJid(),
											contact.getDisplayName(),
											NamoNamoConstant.GENRAL_GROUP);
							}
							return null;
						}

						protected void onPostExecute(Void result) {

							allContact = NamoNamoContactDBService
									.getAllContact(getApplicationContext());
							showContactList();
							checkForContact();
						};
					}.execute((Void) null);

				}
			}
//			Toast.makeText(AllContacts.this,"Your contact list has been updated",Toast.LENGTH_SHORT).show();
		}
	};
	private ImageView imgClear;
	private EditText txtSearch;
	protected String searchText;
	private TextWatcher watcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			searchText = s.toString();
			showContactList();

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub

		}
	};
	private OnActionExpandListener listener = new OnActionExpandListener() {

		@Override
		public boolean onMenuItemActionExpand(MenuItem item) {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public boolean onMenuItemActionCollapse(MenuItem item) {
			searchText = "";
			((EditText) item.getActionView().findViewById(R.id.txt_search))
					.setText("");
			showContactList();
			return true;
		}
	};

	void setUpActionView(View view) {
		txtSearch = (EditText) view.findViewById(R.id.txt_search);
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(txtSearch, 0);
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
	protected void onResume() {
		bindXMPPService();
		super.onResume();
	}

	private void bindXMPPService() {
		bindService(xmppServiceIntent, xmppServiceConnection, BIND_AUTO_CREATE);
	}

	@Override
	protected void onPause() {
		unbindXMPPService();
		super.onPause();
	}

	private void unbindXMPPService() {
		try {
			unbindService(xmppServiceConnection);
		} catch (IllegalArgumentException e) {
			Log.e(TAG, "Service wasn't bound!");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.allcontact_menu, menu);
		return true;
	}

	private OnItemClickListener chatItemClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			if (arg2 < showContact.size()) {
				NamoNamoContact contact = showContact.get(arg2);
				String userName = contact.getDisplayName();
				if (userName == null || userName.length() < 1)
					userName = contact.getMobile_number();
				Hihelloconstant.username=contact.getDisplayName();
				Hihelloconstant.picurl=contact.getPic_url();
				Hihelloconstant.status=contact.getStatus();
				Hihelloconstant.mobnumber=contact.getMobile_number();
				startChatActivity(contact.getJid(), userName, null);
				finish();
			} else {
				int index = arg2 - showContact.size();
				SendInvitationApiCall api = new SendInvitationApiCall(
						NamoNamoSharedPrefrence
								.getUserId(getApplicationContext()),
						inviteContacts.get(index).getPhoneNo().replace("+", ""));
				api.runAsync(invitationListener);
			}
		}
	};
	private NamoNamoContactAdapter contactAdapter;
	private ArrayList<NamoNamoContact> showContact;
	protected XMPPRosterServiceAdapter serviceAdapter;
	private OnClickListener refershClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			refershContacts(userListener);
			findViewById(R.id.layout_no_contact).setVisibility(View.GONE);
			findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
		}
	};
	private OnClickListener inviteFriend = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(Intent.ACTION_PICK,
					ContactsContract.Contacts.CONTENT_URI);
			intent.setType(Phone.CONTENT_TYPE);
			startActivityForResult(intent, REQUEST_CONTACT);
		}
	};
	private ServiceListener invitationListener = new ServiceListener() {

		@Override
		public void onComplete(Servicable<?> service) {
			if (((SendInvitationApiCall) service).isSuccess()) {
				Toast.makeText(getApplicationContext(),
						"Invitation sending successfull.", Toast.LENGTH_SHORT)
						.show();
			}
		}
	};
	private MergeAdapter mergeAdapter;
	private ListView listView;
	protected InviteContactAdapter inviteAdapter;
	protected ArrayList<InviteContact> inviteContacts;

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQUEST_CONTACT:
			if (resultCode == RESULT_OK) {
				Uri contactUri = data.getData();
				if (null == contactUri)
					return;
				Cursor cursor = getContentResolver()
						.query(contactUri,
								new String[] { Phone.NUMBER },
								null, null, null);
				if (null == cursor)
					return;
				try {
					while (cursor.moveToNext()) {
						String number = cursor.getString(0);
						number = ContactUtil.prepareContactNumber(number);
						SendInvitationApiCall api = new SendInvitationApiCall(
								NamoNamoSharedPrefrence
										.getUserId(getApplicationContext()),
								number.replace("+", ""));
						api.runAsync(invitationListener);
					}
				} finally {
					cursor.close();
				}
			}
			break;
		}
	};

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.all_contact);
		Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//		getSupportActionBar().setHomeAsUpIndicator(R.drawable.leftback);
		getSupportActionBar().setTitle("Select Contacts");
//		actionBar = getActionBar();
//		actionBar.setDisplayHomeAsUpEnabled(true);
//		actionBar.setHomeButtonEnabled(true);
		allContact = NamoNamoContactDBService
				.getAllContact(getApplicationContext());

		listView = (ListView) findViewById(R.id.contactList);
//		listView.addFooterView(getInviteView());
		listView.setOnItemClickListener(chatItemClick);
		inviteAdapter = new InviteContactAdapter(getLayoutInflater(), null,
				getApplicationContext());
		showContactList();
//		fetchInviteContactList();

		registerXMPPService();
		checkForContact();
	}

	private void fetchInviteContactList() {
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				inviteContacts = ContactUtil
						.getAllInviteContact(getApplicationContext());
//				Collections.sort(inviteContacts);
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				if (inviteAdapter == null)
					inviteAdapter = new InviteContactAdapter(
							getLayoutInflater(), inviteContacts,
							getApplicationContext());
				else
					inviteAdapter.setContacts(inviteContacts);
				checkForContact();
				notifyAdapter();
			}
		}.execute((Void) null);
	}

	private void checkForContact() {
		if ((allContact == null || allContact.size() == 0) && (inviteContacts==null || inviteContacts.size()==0)) {
			if (DownloadRegisterUserApiCall.isBusy) {
				findViewById(R.id.layout_no_contact).setVisibility(View.GONE);
				findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
			} else {
				findViewById(R.id.progressBar).setVisibility(View.GONE);
				findViewById(R.id.layout_no_contact)
						.setVisibility(View.VISIBLE);
				findViewById(R.id.img_refersh).setOnClickListener(refershClick);
			}
		} else {
			findViewById(R.id.progressBar).setVisibility(View.GONE);
			findViewById(R.id.layout_no_contact).setVisibility(View.GONE);
		}
	}

	private void registerXMPPService() {
		xmppServiceIntent = new Intent(this, XMPPService.class);
		xmppServiceIntent.setAction("org.yaxim.androidclient.XMPPSERVICE");

		xmppServiceConnection = new ServiceConnection() {

			public void onServiceConnected(ComponentName name, IBinder service) {
				serviceAdapter = new XMPPRosterServiceAdapter(
						IXMPPRosterService.Stub.asInterface(service));

			}

			public void onServiceDisconnected(ComponentName name) {
				Log.i(TAG, "called onServiceDisconnected()");
			}
		};

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		switch (menuItem.getItemId()) {
			case android.R.id.home:
				Intent in=new Intent(AllContacts.this,HomeMain_activity.class);
				startActivity(in);
				break;
			case R.id.search:
//			 searchView = setUpActionView(listView);
//			 menuItem.setActionView(searchView );
//			menuItem.setOnActionExpandListener(listener);
//			if (menuItem.getActionView() != null)
//				setUpActionView(menuItem.getActionView());
//			 menuItem.expandActionView();
//			break;
			case R.id.refersh:
				refershContacts(userListener);
				findViewById(R.id.layout_no_contact).setVisibility(View.GONE);
				findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
				break;
			case R.id.contact:
				Intent intent = new Intent(Intent.ACTION_INSERT);
				intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
				int PICK_CONTACT = 100;
				startActivityForResult(intent, PICK_CONTACT);
				break;
		}
		return (super.onOptionsItemSelected(menuItem));
	}

	private void showContactList() {
		if (searchText == null || searchText.length() == 0) {
			showContact = allContact;
		} else {
			showContact = new ArrayList<NamoNamoContact>();
			for (NamoNamoContact contact : allContact) {
				if (contact.getDisplayName().toUpperCase()
						.contains(searchText.toUpperCase()))
					showContact.add(contact);
			}
		}
		if (contactAdapter == null)
			contactAdapter = new NamoNamoContactAdapter(getLayoutInflater(),
					showContact, getApplicationContext());
		else
			contactAdapter.setContacts(showContact);

		notifyAdapter();

//		getActionBar().setSubtitle(allContact.size() + " Contacts");
	}

	void notifyAdapter() {
		if (mergeAdapter == null) {
			mergeAdapter = new MergeAdapter();
			mergeAdapter.addAdapter(contactAdapter);
			mergeAdapter.addAdapter(inviteAdapter);
			listView.setAdapter(mergeAdapter);
		}
		mergeAdapter.notifyDataSetChanged();
	}

	private View getInviteView() {
		View view = getLayoutInflater().inflate(R.layout.cell_invite_friend,
				null);
		view.setOnClickListener(inviteFriend);
		return view;
	}

}