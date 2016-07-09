package com.hihello.app.fragment;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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


import com.hihello.app.R;
import com.hihello.app.activity.EditStatus;
import com.hihello.app.activity.SaveContact;
import com.hihello.app.activity.SelectContactActivity;
import com.hihello.app.activity.SettingPrefs;
import com.hihello.app.adapter.HiHelloContactAdapter;
import com.hihello.app.constant.HiHelloNewConstant;
import com.hihello.app.db.HiHelloContact;
import com.hihello.app.db.hiHelloContactDBService;

public class ContactFragment extends Fragment {

	private View view;

public static 	ListView listView;
	private OnItemClickListener chatItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			HiHelloContact contact = allContact.get(arg2);
			showConfirmation(contact);

		}
	};
	void showConfirmation(final HiHelloContact contact) {

//		Intent in3 =new Intent(getActivity(),groupprofile.class);
//		in3.putExtra("name",contact.getDisplayName());
//		in3.putExtra("status",contact.getStatus());
//		in3.putExtra("mobileno",contact.getMobile_number());
//		in3.putExtra("pic",contact.getPic_url());
		HiHelloNewConstant.picurl=contact.getPic_url();
		HiHelloNewConstant.username=contact.getDisplayName();
		HiHelloNewConstant.mobnumber=contact.getMobile_number();
		HiHelloNewConstant.status=contact.getStatus();
//		Toast.makeText(getActivity(),contact.getShow_last_seen()+" hello", Toast.LENGTH_SHORT).show();
//		startActivity(in3);

//		Toast.makeText(getActivity(),contact.getJid()+" hello", Toast.LENGTH_SHORT).show();
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
					contact.setDisplayName(contact.getMobile_number());
				}
				((SelectContactActivity) getActivity()).startChatActivity(
						contact.getJid(), contact.getDisplayName());
			}
		});
		alert.show();
	}
	private ArrayList<HiHelloContact> allContact;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Get the view from fragmenttab3.xml
		setHasOptionsMenu(true);
		view = inflater.inflate(R.layout.contactfragment, container, false);
		showAllContact();
		return view;
	}

	private void showAllContact() {
	 listView = (ListView) view.findViewById(R.id.listView);
		listView.setOnItemClickListener(chatItemClick);
		allContact = hiHelloContactDBService.getAllContact(getActivity());

		HiHelloContactAdapter adapter = new HiHelloContactAdapter(getActivity()
				.getLayoutInflater(), allContact, getActivity());
		listView.setAdapter(adapter);

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		setUserVisibleHint(true);
	}


	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getActivity().getMenuInflater();
		inflater.inflate(R.menu.selectcontact_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		switch (menuItem.getItemId()) {
			case android.R.id.home:
				getActivity().finish();
				break;
			case R.id.search:

				break;
			case R.id.contact:
				Intent in=new Intent(getActivity(),SaveContact.class);
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
		return (super.onOptionsItemSelected(menuItem));
	}
}