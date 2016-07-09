package com.namonamo.app.activity;

import github.ankushsachdeva.emojicon.EmojiconGridView.OnEmojiconClickedListener;
import github.ankushsachdeva.emojicon.EmojiconsPopup;
import github.ankushsachdeva.emojicon.EmojiconsPopup.OnEmojiconBackspaceClickedListener;
import github.ankushsachdeva.emojicon.EmojiconsPopup.OnSoftKeyboardOpenCloseListener;
import github.ankushsachdeva.emojicon.emoji.Emojicon;

import java.util.ArrayList;
import java.util.List;

import com.namonamo.app.R;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow.OnDismissListener;


import com.namonamo.app.apicall.UpdateProfileApiCall;
import com.namonamo.app.constant.NamoNamoSharedPrefrence;
import com.namonamo.app.service.Servicable;
import com.namonamo.app.service.Servicable.ServiceListener;

public class EditStatus extends BaseActivity {
	private ActionBar actionBar;
	private ListView listStatus;
	ImageView post;
	List<String> statusList;
	private EditText txtStatus;
	private ImageView img_emoji;
	private EmojiconsPopup popup;
	private OnItemClickListener statusClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			updateStatus(statusList.get(arg2));
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_status);

		Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("Edit Status");
		initializeControl();

	}

	private void initializeControl() {
		statusList = new ArrayList<String>();
		statusList.add("At School");
		statusList.add("At the movies");
		statusList.add("At work");
		statusList.add("Can't talk only Hi Hello");
		statusList.add("Bettry about to die");
		statusList.add("At home");

		listStatus = (ListView) findViewById(R.id.list_status);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1,
				statusList);
		listStatus.setOnItemClickListener(statusClick);
		listStatus.setAdapter(adapter);
		txtStatus = (EditText) findViewById(R.id.txt_status);
		txtStatus.setText(NamoNamoSharedPrefrence
				.getStatus(getApplicationContext()));
		setUpEmojiKeyboard();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.status_menu, menu);
		return true;
	}

	private OnClickListener emojiClick = new OnClickListener() {

		@Override
		public void onClick(View v) {

			if (!popup.isShowing()) {

				// If keyboard is visible, simply show the emoji popup
				if (popup.isKeyBoardOpen()) {
					popup.showAtBottom();
					img_emoji.setImageResource(R.drawable.ic_action_keyboard);
				}

				// else, open the text keyboard first and immediately after that
				// show the emoji popup
				else {
					txtStatus.setFocusableInTouchMode(true);
					// mChatInput.requestFocus();
					popup.showAtBottomPending();
					final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					inputMethodManager.showSoftInput(txtStatus,
							InputMethodManager.SHOW_IMPLICIT);
					img_emoji.setImageResource(R.drawable.ic_action_keyboard);
				}
			}

			// If popup is showing, simply dismiss it to show the undelying text
			// keyboard
			else {
				popup.dismiss();
			}
		}
	};
	protected boolean shouldClose = false;
	private ServiceListener listener = new ServiceListener() {

		@Override
		public void onComplete(Servicable<?> service) {
			dismissProgress();
			if (((UpdateProfileApiCall) service).isSuccess()) {
				txtStatus.setText(((UpdateProfileApiCall) service).getStatus());
				if (shouldClose) {
					finish();
				}
			} else
				showAlert("Unable to post Status");
		}
	};

	private void setUpEmojiKeyboard() {
		popup = new EmojiconsPopup(findViewById(R.id.root_view), this);
		popup.setSizeForSoftKeyboard();
		popup.setOnEmojiconClickedListener(new OnEmojiconClickedListener() {

			@Override
			public void onEmojiconClicked(Emojicon emojicon) {
				txtStatus.append(emojicon.getEmoji());
			}
		});

		// Set on backspace click listener
		popup.setOnEmojiconBackspaceClickedListener(new OnEmojiconBackspaceClickedListener() {

			@Override
			public void onEmojiconBackspaceClicked(View v) {
				KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0,
						0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
				txtStatus.dispatchKeyEvent(event);
			}
		});
		// If the emoji popup is dismissed, change emojiButton to smiley icon
		popup.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				img_emoji.setImageResource(R.drawable.smiley);
			}
		});

		// If the text keyboard closes, also dismiss the emoji popup
		popup.setOnSoftKeyboardOpenCloseListener(new OnSoftKeyboardOpenCloseListener() {

			@Override
			public void onKeyboardOpen(int keyBoardHeight) {

			}

			@Override
			public void onKeyboardClose() {
				if (popup.isShowing())
					popup.dismiss();
			}
		});

		img_emoji = (ImageView) findViewById(R.id.img_emoji);
		img_emoji.setOnClickListener(emojiClick);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		switch (menuItem.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		case R.id.post:
			shouldClose = true;
			updateStatus(txtStatus.getText().toString());
			break;
		}
		return (super.onOptionsItemSelected(menuItem));
	}

	private void updateStatus(String status) {
		if(status.length()<1) {
			showAlert("Status should not be empty.");
			return;
		}
		showProgress("Updating Status...");
		UpdateProfileApiCall apiCall = new UpdateProfileApiCall(
				getApplicationContext(),"", status, "", false);
		apiCall.runAsync(listener);

	}

	@Override
	public void onBackPressed() {
		Intent i=new Intent(EditStatus.this,HomeMain_activity.class);
		startActivity(i);
		finish();
	}
}
