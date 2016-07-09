package com.namonamo.app.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;


import com.namonamo.app.R;
import com.namonamo.app.apicall.DownloadRegisterUserApiCall;
import com.namonamo.app.common.ContactUtil;
import com.namonamo.app.service.Servicable.ServiceListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class BaseActivity extends ActionBarActivity {
	private ProgressDialog progress;
	public static final String PHOTO_PATH = "PHOTO_PATH";
	public static final String VIDEO_PATH = "VIDEO_PATH";

	boolean isDestroyed = false;
	static final int REQUEST_CAMERA = 101;
	static final int REQUEST_GALLERY = 102;
	static final int REQUEST_IMAGE_CROPPER = 103;
	static final int REQUEST_USER_NAME = 104;
	static final int REQUEST_STATUS = 105;
	static final int REQUEST_CONTACT = 106;

	public void showAlert(String message) {
		showAlert(message, this);
	}

	@Override
	protected void onDestroy() {
		isDestroyed = true;
		super.onDestroy();
	}

	public Uri captureImage(int requestCode) {
		String fileName = "profile" + (System.currentTimeMillis() / 10000)
				+ ".jpg";
		// create parameters for Intent with filename
		ContentValues values = new ContentValues();
		values.put(MediaStore.Images.Media.TITLE, fileName);
		values.put(MediaStore.Images.Media.DESCRIPTION,
				"Image capture by camera");
		// imageUri is the current activity attribute, define and save it for
		// later usage (also in onSaveInstanceState)
		Uri uri = getContentResolver().insert(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
		// create new Intent
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		startActivityForResult(intent, requestCode);
		return uri;
	}

	@SuppressWarnings("deprecation")
	public static void showAlert(String message, Activity activity) {
		AlertDialog alert = new AlertDialog.Builder(activity)
				.setTitle("Error!!").setMessage(message).setCancelable(true)
				.create();
		alert.setButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		alert.show();
	}

	public void showProgress(String message) {
		 dismissProgress();
		if (progress != null && progress.isShowing()) {
			progress.setMessage(message);
		} else {
			dismissProgress();
			progress = ProgressDialog.show(this, "", message, true);
		}
	}

	public void setProgressMessage(String message) {
		if (progress != null)
			progress.setMessage(message);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// Thread.setDefaultUncaughtExceptionHandler(new
		// UnCaughtException(this));
//		getActionBar().setBackgroundDrawable(
//				getResources().getDrawable(
//						R.drawable.ic_online));

	}

	public void dismissProgress() {
		if (progress != null) {
			progress.dismiss();
			progress = null;
		}
	}

	public static void displayImage(String uri, ImageView imageView) {
		ImageLoader.getInstance().displayImage(uri, imageView);
	}

	void displayFullImage(String uri, ImageView imageView) {
		ImageLoader.getInstance().displayImage(uri, imageView,
				UIApplication.diOptions);
	}

	void displayImage(String uri, ImageView imageView,
			ImageLoadingListener imageListener) {
		ImageLoader.getInstance().displayImage(uri, imageView, imageListener);

	}

	public String uriToPath(Uri uri) {
		String[] data = { MediaStore.Images.Media.DATA };
		Cursor cursor = getContentResolver().query(uri, data, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		String file_path = cursor.getString(column_index);
		cursor.close();
		return file_path;
	}

	public String writeBitmapToPhoneMemory(Bitmap resizedBitmap, String fileName) {
		String root = Environment.getExternalStorageDirectory().toString();
		File myDir = new File(root + "/NamoNamo/Profile/Me");
		myDir.mkdirs();
		String fname = fileName + ".jpg";
		File file = new File(myDir, fname);
		String path = "";
		if (file.exists())
			file.delete();
		try {
			FileOutputStream out = new FileOutputStream(file);
			resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
			out.flush();
			out.close();
			path = file.getAbsolutePath();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return path;
	}

	public void refershContacts(final ServiceListener listener) {
		new AsyncTask<Void, Void, Void>() {
			private ArrayList<String> mobile_numbers;

			@Override
			protected Void doInBackground(Void... params) {
				mobile_numbers = ContactUtil
						.getPhoneContact(getApplicationContext());
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				for (String mobiles : mobile_numbers) {
					DownloadRegisterUserApiCall apiCall = new DownloadRegisterUserApiCall(
							getApplicationContext(), mobiles);
					apiCall.runAsync(listener);
				}
			}
		}.execute((Void) null);

	}

}
