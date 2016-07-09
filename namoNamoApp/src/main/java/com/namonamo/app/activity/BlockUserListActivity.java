package com.namonamo.app.activity;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.namonamo.app.R;
import com.namonamo.app.adapter.BlockUserAdapter;
import com.namonamo.app.apicall.DeleteBlockUserApiCall;
import com.namonamo.app.apicall.GetBlockUserApiCall;
import com.namonamo.app.common.Log;
import com.namonamo.app.db.BlockUser;
import com.namonamo.app.db.BlockUserDBService;
import com.namonamo.app.service.Servicable;
import com.namonamo.app.service.Servicable.ServiceListener;

public class BlockUserListActivity extends BaseActivity {
	SharedPreferences pref_block;
	SharedPreferences.Editor editor_block ;
	private ListView listView;
	private ArrayList<BlockUser> blockedUser;
	private ServiceListener listener = new ServiceListener() {

		@Override
		public void onComplete(Servicable<?> service) {
			if (isDestroyed)
				return;
			displayBlockUser();
		}
	};
	private OnItemClickListener blockClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1,
				final int index, long arg3) {
			ArrayAdapter<String> chatOptionMenu = new ArrayAdapter<String>(
					BlockUserListActivity.this,
					android.R.layout.select_dialog_item);
			chatOptionMenu.add("Unblock");
			AlertDialog.Builder builderSingle = new AlertDialog.Builder(
					BlockUserListActivity.this);
			builderSingle.setAdapter(chatOptionMenu,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							switch (which) {
							case 0:
								doUnBlock(blockedUser.get(index));
								break;
							}
						}

					});
			builderSingle.show();
		}
	};
	private ServiceListener unBlockListener = new ServiceListener() {
		@Override
		public void onComplete(Servicable<?> service) {
			displayBlockUser();
		}
	};
	private BlockUserAdapter adapter;

	private void doUnBlock(BlockUser blockUser) {
		DeleteBlockUserApiCall api = new DeleteBlockUserApiCall(
				getApplicationContext(), blockUser.getJid());
		api.runAsync(unBlockListener);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_block_user_list);
		pref_block=getSharedPreferences("pref_block", 1);
		initializeControls();
		displayBlockUser();
		GetBlockUserApiCall api = new GetBlockUserApiCall(
				getApplicationContext());
		api.runAsync(listener);
//		ActionBar actionBar = getActionBar();
//		actionBar.setHomeButtonEnabled(true);
//		actionBar.setDisplayHomeAsUpEnabled(true);
//		getActionBar().hide();
		Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//		getSupportActionBar().setHomeAsUpIndicator(R.drawable.leftback);
		getSupportActionBar().setTitle("Block People");
		showProgress("loading blocked user");

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
			case R.id.contact:
				Intent intent = new Intent(BlockUserListActivity.this,AllContacts.class);
				startActivity(intent);
				break;
		}
		return (super.onOptionsItemSelected(item));
	}

	private void displayBlockUser() {
		dismissProgress();
		findViewById(R.id.txt_message).setVisibility(View.GONE);
		blockedUser = BlockUserDBService
				.getAllBlockUser(getApplicationContext());
		if (blockedUser == null || blockedUser.size() == 0) {
			findViewById(R.id.txt_message).setVisibility(View.VISIBLE);
		}
		if(adapter==null) {
			adapter = new BlockUserAdapter(
					getApplicationContext(), blockedUser);
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(blockClickListener);
			editor_block = pref_block.edit();
			editor_block.putString("block", blockedUser.size()+"");
			Log.e("blockkkk",blockedUser.size()+"");
			editor_block.commit();
		}else {
			adapter.setBlockUser(blockedUser);
			adapter.notifyDataSetChanged();
			editor_block = pref_block.edit();
			editor_block.putString("block", blockedUser.size()+"");
			Log.e("blockkkk", blockedUser.size() + "");
			editor_block.commit();
		}
	}

	private void initializeControls() {
		listView = (ListView) findViewById(R.id.list_block_user);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.block_menu, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		Intent i=new Intent(BlockUserListActivity.this,HomeMain_activity.class);
		startActivity(i);
		finish();
	}
}
