package com.hihello.app.activity;

import java.io.File;
import java.io.FileInputStream;

import com.hihello.app.R;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;

import com.edmodo.cropper.CropImageView;

public class ProfilePicCropper extends BaseActivity {

	// Static final constants
	private static final int DEFAULT_ASPECT_RATIO_VALUES = 10;
	public static final String PROFILE_PIC_PATH = "PROFILE_PIC_PATH";
	public static final String OUT_PROFILE_PIC_PATH = "OUT_PROFILE_PIC_PATH";

	Bitmap croppedImage;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bnd = getIntent().getExtras();
		String profilePic = bnd.getString(PROFILE_PIC_PATH);



		setContentView(R.layout.activity_profile_pic_cropper);
		final CropImageView cropImageView = (CropImageView) findViewById(R.id.CropImageView);

		Toolbar tool=(Toolbar)findViewById(R.id.toolbar);


		setSupportActionBar(tool);
		String path = uriToPath(Uri.parse(profilePic));
		try {
			cropImageView.setImageBitmap(getBitmapFromUri(path));
		} catch (Exception x) {
		} catch (Error err) {
		}
		cropImageView.setFixedAspectRatio(true);
		cropImageView.setAspectRatio(DEFAULT_ASPECT_RATIO_VALUES,
				DEFAULT_ASPECT_RATIO_VALUES);

		findViewById(R.id.btn_crop).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						croppedImage = cropImageView.getCroppedImage();
						writeBitmapToPhoneMemory(croppedImage,
								"upload_profile_pic");
						Intent intent = new Intent();
						Bundle bnd = new Bundle();
						String upload_profile_pic = "/sdcard/HiHello/Profile/Me/upload_profile_pic.jpg";
						bnd.putString(OUT_PROFILE_PIC_PATH, upload_profile_pic);
						intent.putExtras(bnd);
						setResult(RESULT_OK, intent);
						finish();
					}
				});
		findViewById(R.id.btn_rotate).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						cropImageView.rotateImage(90);
					}
				});
	}

	public Bitmap getBitmapFromUri(String uri) throws Exception {
		DisplayMetrics outMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
		int maxSize = outMetrics.heightPixels;

		Bitmap bitmap = null;
		System.gc();
		File file = new File(uri);
		int scale = 1;
		if (file.exists()) {
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			System.gc();
			BitmapFactory.decodeStream(new FileInputStream(file), null, o);
			if (o.outHeight > maxSize || o.outWidth > maxSize)
				scale = (int) Math.pow(
						2,
						(int) Math.round(Math.log(maxSize
								/ (double) Math.max(o.outHeight, o.outWidth))
								/ Math.log(0.5)));

			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = (int) scale;
			System.gc();
			bitmap = BitmapFactory.decodeStream(new FileInputStream(file),
					null, o2);
		}
		return bitmap;
	}
}
