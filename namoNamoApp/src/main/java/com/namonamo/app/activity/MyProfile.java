package com.namonamo.app.activity;

import java.io.File;

import org.json.JSONObject;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;


import com.namonamo.app.R;
import com.namonamo.app.apicall.UploadImageHttp;
import com.namonamo.app.constant.NamoNamoConstant;
import com.namonamo.app.constant.NamoNamoSharedPrefrence;

public class MyProfile extends BaseActivity {private static final int REQUEST_IMAGE_CROPPER = 0;
	protected Uri mProfilePath;
	private ImageView profileImage;
	ImageView back;
	CircularImageView cirimg;
	private String contact_no;
	private OnClickListener doneClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			finish();
			Intent intent = new Intent(MyProfile.this, RecentChats.class);
			startActivity(intent);
		}
	};
	private OnClickListener name_edit_click = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(MyProfile.this, EditUserName.class);
			startActivityForResult(intent, REQUEST_USER_NAME);
		}
	};
	private TextView txt_user_name;
	//	private TextView txt_status;
	private OnClickListener status_edit_click = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(MyProfile.this, EditStatus.class);
			startActivityForResult(intent, REQUEST_STATUS);
		}
	};
	private ActionBar actionBar;
	private String user_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.profileupdate);

		Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//		getSupportActionBar().setHomeAsUpIndicator(R.drawable.leftback);
		getSupportActionBar().setTitle("My Profile");
		contact_no = NamoNamoSharedPrefrence
				.getMobileNo(getApplicationContext());
		user_id = NamoNamoSharedPrefrence.getUserId(getApplicationContext());
		back=(ImageView)findViewById(R.id.backarrowid);


		profileImage = (ImageView) findViewById(R.id.profileImage);
		txt_user_name = (TextView) findViewById(R.id.txt_user_name);
		cirimg = (CircularImageView)findViewById(R.id.imageView21);
		txt_user_name.setText(NamoNamoSharedPrefrence
				.getUserName(getApplicationContext()));
//		txt_status = (TextView) findViewById(R.id.txt_status);
//		txt_status.setText(NamoNamoSharedPrefrence
//				.getStatus(getApplicationContext()));
//		back.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Intent in=new Intent(MyProfile.this,Scholarship.class);
//				startActivity(in);
//			}
//		});

		displayImage(
				NamoNamoSharedPrefrence
						.getProfileImageUrl(getApplicationContext()),
				cirimg);
		displayImage(
				NamoNamoSharedPrefrence
						.getProfileImageUrl(getApplicationContext()),
				profileImage);
		findViewById(R.id.imageView21).setOnClickListener(imageClick);
		findViewById(R.id.img_edit_picture).setOnClickListener(imageClick);

		findViewById(R.id.btn_name_edit).setOnClickListener(name_edit_click);
//		findViewById(R.id.btn_status_edit)
//				.setOnClickListener(status_edit_click);
//		actionBar = getSupportActionBar();
//		actionBar.setDisplayHomeAsUpEnabled(true);
//		actionBar.setHomeButtonEnabled(true);
		// findViewById(R.id.finish).setOnClickListener(finishClick);
		if (savedInstanceState != null
				&& savedInstanceState.containsKey(PHOTO_PATH)) {
			mProfilePath = Uri.parse(savedInstanceState.getString(PHOTO_PATH));
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		switch (menuItem.getItemId()) {
			case android.R.id.home:
				Intent in=new Intent(MyProfile.this,HomeMain_activity.class);
				startActivity(in);
				break;
			case R.id.home:
				Intent in1=new Intent(MyProfile.this,HomeMain_activity.class);
				startActivity(in1);
				break;
		}

		return (super.onOptionsItemSelected(menuItem));
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mProfilePath != null && outState != null)
			outState.putString(PHOTO_PATH, mProfilePath.toString());

	}

	protected void uploadImage(String filePath) {
		if (filePath != null) {
			asyncUploadImage async = new asyncUploadImage();
			async.filePath = filePath;
			async.execute((Void) null);
			showProgress("Uploading Images...");
		} else {
			showAlert("Please select profile Image");
		}

	}

	class asyncUploadImage extends AsyncTask<Void, Void, Void> {
		String filePath;
		private String jsonString = null;

		@Override
		protected Void doInBackground(Void... params) {
			try {
				jsonString = UploadImageHttp.updateProfilePic(
						NamoNamoConstant.UPLOAD_PIC_URL, filePath, contact_no,
						user_id);
			} catch (Exception x) {
				x.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (filePath != null) {
				File file = new File(filePath);
				file.delete();
				try {
					if (jsonString == null) {
						showAlert("Unable to update profile.Try Again");
						return;
					}
					JSONObject json = new JSONObject(jsonString);
					String url = json.getString("image");
					NamoNamoSharedPrefrence.setProfileImageUrl(
							getApplicationContext(), url);
					displayImage(url, profileImage);
				} catch (Exception x) {

				}
			}
			dismissProgress();

		}
	}

	private void startTakingPicture() {
		AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
		myAlertDialog.setTitle("Upload Pictures Option");
		myAlertDialog.setMessage("How do you want to set your picture?");

		myAlertDialog.setPositiveButton("Gallery",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						Intent intent = new Intent(Intent.ACTION_GET_CONTENT,
								null);
						intent.setType("image/*");
						intent.putExtra("return-data", true);
						startActivityForResult(intent, REQUEST_GALLERY);
					}
				});

		myAlertDialog.setNegativeButton("Camera",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						mProfilePath = captureImage(REQUEST_CAMERA);
					}
				});
		myAlertDialog.show();
	}

	@Override
	protected void onDestroy() {
		if (!NamoNamoSharedPrefrence.getChatPageInit(getApplicationContext())) {
			Intent intent = new Intent(this, HomeMain_activity.class);
			startActivity(intent);
		}
		super.onDestroy();
	}

	private OnClickListener imageClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			startTakingPicture();
		}
	};
	private String profilePicPath;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
			case REQUEST_GALLERY:
				if (resultCode == RESULT_OK) {
					mProfilePath = data.getData();
					showProfileCropper(mProfilePath.toString());
				}

				break;
			case REQUEST_CAMERA:
				if (resultCode == RESULT_OK) {
					showProfileCropper(mProfilePath.toString());
				}
				break;
			case REQUEST_IMAGE_CROPPER:
				if (resultCode == RESULT_OK) {
					profilePicPath = data.getExtras().getString(
							ProfilePicCropper.OUT_PROFILE_PIC_PATH);
					uploadImage(profilePicPath);
					// profileImage.setImageDrawable(BitmapDrawable
					// .createFromPath(profilePicPath));

				}
				break;
			case REQUEST_USER_NAME:
				txt_user_name.setText(NamoNamoSharedPrefrence
						.getUserName(getApplicationContext()));
				break;
//		case REQUEST_STATUS:
//			txt_status.setText(NamoNamoSharedPrefrence
//					.getStatus(getApplicationContext()));
//			break;

		}

	}

	void showProfileCropper(String profilePicPath) {
		Intent intent = new Intent(MyProfile.this, ProfilePicCropper.class);
		Bundle bnd = new Bundle();
		bnd.putString(ProfilePicCropper.PROFILE_PIC_PATH, profilePicPath);
		intent.putExtras(bnd);
		startActivityForResult(intent, REQUEST_IMAGE_CROPPER);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.profile_menu, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		Intent in=new Intent(MyProfile.this,HomeMain_activity.class);
		startActivity(in);
		finish();
//		super.onBackPressed();
	}
}
