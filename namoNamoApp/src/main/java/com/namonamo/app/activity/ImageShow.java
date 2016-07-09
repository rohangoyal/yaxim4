package com.namonamo.app.activity;


import com.namonamo.app.R;
import com.squareup.picasso.Picasso;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

public class ImageShow extends BaseActivity {

	private ImageView imageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_show);
//		ActionBar actionBar = getActionBar();
//		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
//		actionBar.setDisplayHomeAsUpEnabled(true);
//		actionBar.setHomeButtonEnabled(true);
		String title = getIntent().getExtras().getString("TITLE");
		if (title == null)
			title = "";
//		actionBar.setTitle(title);
		imageView = (ImageView) findViewById(R.id.imageView);
		String url = getIntent().getExtras().getString("PATH");
		// String title = getIntent().getExtras().getString("TITLE");
//		Picasso.with(getApplicationContext())
//				.load(url)
//				.into(imageView);
		displayFullImage(url, imageView);
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
}
