package com.namonamo.app.fragment;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;


import com.namonamo.androidclient.data.ChatProvider;
import com.namonamo.androidclient.data.ChatProvider.ChatConstants;
import com.namonamo.app.R;
import com.namonamo.app.activity.AllContacts;
import com.namonamo.app.activity.DailyMessageActivity;
import com.namonamo.app.activity.EditStatus;
import com.namonamo.app.activity.SelectContactActivity;
import com.namonamo.app.activity.SettingPrefs;
import com.namonamo.app.adapter.RecentChatAdapter;
import com.namonamo.app.common.ContactUtil;
import com.namonamo.app.db.NamoNamoContact;
import com.namonamo.app.db.NamoNamoContactDBService;
import com.namonamo.app.db.RecentChat;
import com.namonamo.app.db.RecentChatDBService;

public class RecentFragment extends Fragment {

	TextView dailymsg;
	private View view;
	private OnItemClickListener chatItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			RecentChat contact = recentChats.get(arg2);

			showConfirmation(contact);
		}

	};

	void showConfirmation(final RecentChat contact) {
		AlertDialog alert = new AlertDialog.Builder(getActivity())
				.setMessage("Send to " + contact.getDisplayName())
				.setCancelable(true).create();
		alert.setButton2("Cancel", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		alert.setButton("Send", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (contact.getDisplayName() == null) {
					contact.setDisplayName(contact.getMobileNo());
				}
				((SelectContactActivity) getActivity()).startChatActivity(
						contact.getUserJID(), contact.getDisplayName());
			}
		});
		alert.show();
	}

	private ArrayList<RecentChat> recentChats;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		// Get the view from fragmenttab3.xml
		view = inflater.inflate(R.layout.list_layout, container, false);
		showRecentList();
		return view;
	}
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
		inflater.inflate(R.menu.selectcontact_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);

	}
	void setLastChatDetail(RecentChat chat) {
		String selection = ChatConstants.JID + "='" + chat.getUserJID() + "'";
		Cursor _cur = getActivity().getContentResolver().query(
				ChatProvider.CONTENT_URI, null, selection, null, null);
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

	private void showRecentList() {
		ListView listView = (ListView) view.findViewById(R.id.listView);
		listView.setOnItemClickListener(chatItemClick);
		recentChats = RecentChatDBService.getAllRecentChat(getActivity());
		for (RecentChat chat : recentChats) {
			NamoNamoContact contact = NamoNamoContactDBService
					.fetchContactByJID(getActivity(), chat.getUserJID());
			if (contact != null) {
				chat.setProfilePic(contact.getPic_url());
				chat.setDisplayName(contact.getDisplayName());
			}
			if (chat.getDisplayName() == null
					|| chat.getDisplayName().length() == 0) {
				String name = ContactUtil.getContactName(getActivity(),
						chat.getMobileNo());
				if (name != null && name.length() > 0) {
					chat.setDisplayName(name);
				}
			}
			setLastChatDetail(chat);
		}
		RecentChatAdapter adapter = new RecentChatAdapter(getActivity()
				.getLayoutInflater(), recentChats, getActivity(), false);
		listView.setAdapter(adapter);

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		setUserVisibleHint(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		switch (menuItem.getItemId()) {
			case android.R.id.home:
				getActivity().finish();
				break;
			case R.id.search:
				break;
			case R.id.newChat:
				Intent in=new Intent(getActivity(), AllContacts.class);
				startActivity(in);
				break;
			case  R.id.action_status:
				Intent in2 =new Intent(getActivity(),EditStatus.class);
				startActivity(in2);
				break;
			case  R.id.action_settings:
				Intent in3 =new Intent(getActivity(),SettingPrefs.class);
				startActivity(in3);
				break;
		}
		return super.onOptionsItemSelected(menuItem);
	}
}