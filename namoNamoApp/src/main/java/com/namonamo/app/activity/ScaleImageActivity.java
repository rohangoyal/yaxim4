package com.namonamo.app.activity;

import android.app.Activity;
import android.os.Bundle;

import com.namonamo.app.R;
import com.namonamo.app.common.ScaleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ScaleImageActivity extends Activity {
	public static final String IMAGE_URL = "IMAGE_URL";
	private ScaleImageView scaleImageView;
	private String imageUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scale_layout);
		imageUrl = getIntent().getExtras().getString(IMAGE_URL);
		scaleImageView = (ScaleImageView) findViewById(R.id.scaleImageView);
		ImageLoader.getInstance().displayImage(imageUrl, scaleImageView,
				UIApplication.diOptions);
	}
}
