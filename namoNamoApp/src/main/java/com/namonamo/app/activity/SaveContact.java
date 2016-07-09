package com.namonamo.app.activity;

import java.io.FileNotFoundException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.namonamo.app.R;
import com.namonamo.app.adapter.ContentAdapter;
import com.namonamo.app.common.BitmapUtil;
import com.namonamo.app.common.ContactUtil;

public class SaveContact extends BaseActivity {

	private JSONObject json;
	private ActionBar actionBar;
	private OnClickListener cancelClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			finish();
		}
	};
	private OnClickListener saveClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			try {
				ContactUtil.saveContact2(SaveContact.this, json);
				finish();
			} catch (Exception x) {
				x.getMessage();
			}
		}
	};
	private int phoneSize;
	private int emailSize;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_contact);
//		actionBar = getActionBar();
//		actionBar.setDisplayHomeAsUpEnabled(true);
//		actionBar.setHomeButtonEnabled(true);

		Bundle bnd = getIntent().getExtras();
		if (bnd != null) {
			try {
				json = new JSONObject(bnd.getString("DATA"));

				if (json.has("Phone"))
					phoneSize = json.getJSONArray("Phone").length();
				if (json.has("Email"))
					emailSize = json.getJSONArray("Email").length();

				initializeControls();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
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

	private OnItemClickListener cellClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int index,
				long arg3) {
			try {
				if (index < phoneSize) {
					JSONObject phone = json.getJSONArray("Phone")
							.getJSONObject(index);
					String number = phone.getString("Number");
					String uri = "tel:" + number.trim();
					Intent intent = new Intent(Intent.ACTION_DIAL);
					intent.setData(Uri.parse(uri));
					startActivity(intent);

				} else {
					JSONObject emailJson = json.getJSONArray("Email")
							.getJSONObject(index - phoneSize);
					String email = emailJson.getString("Email");
					Intent intent = new Intent(Intent.ACTION_SEND);
					intent.setType("text/html");
					intent.putExtra(Intent.EXTRA_EMAIL, new String[] { email });
					intent.putExtra(Intent.EXTRA_SUBJECT, "");
					intent.putExtra(Intent.EXTRA_TEXT, "");
					startActivity(Intent.createChooser(intent, "Send Email"));

				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	};
	private Bitmap bitmap;

	private void initializeControls() throws FileNotFoundException,
			JSONException {
		Button btn_cancel = (Button) findViewById(R.id.btn_cancel);
		btn_cancel.setOnClickListener(cancelClick);

		Button btn_ok = (Button) findViewById(R.id.btn_ok);
		btn_ok.setText("Save");
		btn_ok.setOnClickListener(saveClick);

		((TextView) findViewById(R.id.txt_name))
				.setText(json.optString("Name"));
		ImageView image_contact = (ImageView) findViewById(R.id.image_contact);

		ListView list_content = (ListView) findViewById(R.id.list_content);
		list_content.setAdapter(new ContentAdapter(getLayoutInflater(), json,
				this));
		list_content.setOnItemClickListener(cellClick);

		if (json.has("PhotoData")) {
			bitmap = BitmapUtil
					.getBitMapFromString(json.optString("PhotoData"));
			if (bitmap != null)
				image_contact.setImageBitmap(bitmap);
		}
	}
}
