package com.namonamo.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class ShareToActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
		intent = new Intent(this, SelectContactActivity.class);
		intent.putExtra(SelectContactActivity.EXTRA_SHARE_STEAM,
				imageUri.toString());
		startActivity(intent);
		finish();
	}
}
