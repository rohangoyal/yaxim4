package com.namonamo.app.activity;

import java.io.FileNotFoundException;

import org.json.JSONException;
import org.json.JSONObject;
import com.namonamo.app.R;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.namonamo.app.adapter.ContentAdapter;
import com.namonamo.app.common.BitmapUtil;
import com.namonamo.app.common.ContactUtil;

public class EditContact extends BaseActivity {

	private JSONObject json;
	private ActionBar actionBar;
	private OnClickListener cancelClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			finish();
		}
	};
	private OnClickListener sendClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent data = new Intent();
			Bundle bnd = new Bundle();
			bnd.putString("DATA", json.toString());
			data.putExtras(bnd);
			setResult(RESULT_OK, data);
			finish();
		}
	};
	
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

	private void initializeControls() throws FileNotFoundException,
			JSONException {		
		findViewById(R.id.btn_cancel).setOnClickListener(cancelClick);
		findViewById(R.id.btn_ok).setOnClickListener(sendClick);

		((TextView) findViewById(R.id.txt_name))
				.setText(json.optString("Name"));
		CircularImageView image_contact = (CircularImageView) findViewById(R.id.image_contact);

		ListView list_content = (ListView) findViewById(R.id.list_content);
		list_content.setAdapter(new ContentAdapter(getLayoutInflater(), json,
				this));
		if (json.has("Image")) {
			String imageUri = json.optString("Image");
			Bitmap bitmap = ContactUtil.loadContactPhoto(
					getContentResolver(), Uri.parse(imageUri));
			json.put("PhotoData", BitmapUtil.BitMapToString(bitmap));
			image_contact.setImageBitmap(bitmap);
		}
	}

	@Override
	public void onBackPressed() {
		Intent i=new Intent(EditContact.this,HomeMain_activity.class);
		startActivity(i);
		finish();
	}
}
