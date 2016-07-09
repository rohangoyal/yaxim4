package com.hihello.app.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.Manifest;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.hihello.app.R;
import com.hihello.app.apicall.UploadImageHttp;
import com.hihello.app.common.Log;
import com.hihello.app.constant.HiHelloSharedPrefrence;
import com.hihello.app.constant.HiHelloConstant;
import com.hihello.app.constant.NetworkConnection;
import com.hihello.app.db.DatabaseHandler;
import com.hihello.app.getset.get_set_groups;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyProfile extends BaseActivity {private static final int REQUEST_IMAGE_CROPPER = 0;
	protected Uri mProfilePath;
	DatabaseHandler db;
	private static final String DATABASE_NAME = "hihello.db";
//	private ImageView profileImage;
	NetworkConnection nw;
	ArrayList<get_set_groups> data;
	TextView status;
	SharedPreferences pref_name,pref1;
	SharedPreferences.Editor name_edit;
	ImageView back,editstatus;
	CircleImageView cirimg;
	public  static String flag="false";
	private String contact_no,mob_no="";
	private int REQUEST_CODE_ASK_PERMISSIONS = 123;
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
		db = new DatabaseHandler(this);
		nw=new NetworkConnection(MyProfile.this);
		pref_name=getSharedPreferences("pref_name",1);
		Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	getSupportActionBar().setHomeAsUpIndicator(R.drawable.leftback);
		getSupportActionBar().setTitle("My Profile");
		pref1 =getSharedPreferences("MyPref", 1);
		mob_no=pref1.getString("contact", "");
		mob_no="91"+mob_no;
		contact_no = HiHelloSharedPrefrence
				.getMobileNo(getApplicationContext());
		user_id = HiHelloSharedPrefrence.getUserId(getApplicationContext());
		back=(ImageView)findViewById(R.id.backarrowid);
		status=(TextView)findViewById(R.id.textView4);
		status.setText(HiHelloSharedPrefrence
				.getStatus(getApplicationContext()));
		editstatus=(ImageView)findViewById(R.id.imageView25);
		if (Build.VERSION.SDK_INT >= 23) {
			//do your check here
			insertDummyContactWrapper();
		}


//		profileImage = (ImageView) findViewById(R.id.profileImage);
		txt_user_name = (TextView) findViewById(R.id.txt_user_name);
		cirimg = (CircleImageView) findViewById(R.id.imageView21);
		txt_user_name.setText(HiHelloSharedPrefrence
				.getUserName(getApplicationContext()));
		if(nw.isConnectingToInternet()==true) {
			updatevolley();
			joinvolley();
        }
        else
        {
//            Toast.makeText(getActivity(), "Network Problem", Toast.LENGTH_LONG).show();

        }


//		txt_status = (TextView) findViewById(R.id.txt_status);
//		txt_status.setText(HiHelloSharedPrefrence
//				.getStatus(getApplicationContext()));
//		back.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Intent in=new Intent(MyProfile.this,Scholarship.class);
//				startActivity(in);
//			}
//		});


		editstatus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent in=new Intent(MyProfile.this,EditStatus.class);
				startActivity(in);
				finish();
			}
		});

		displayImage(
				HiHelloSharedPrefrence
						.getProfileImageUrl(getApplicationContext()),
				cirimg);


//		displayImage(
//				HiHelloSharedPrefrence
//						.getProfileImageUrl(getApplicationContext()),
//				profileImage);
		findViewById(R.id.imageView21).setOnClickListener(imageClick);
//		findViewById(R.id.img_edit_picture).setOnClickListener(imageClick);

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
			Log.e("profilepic path",savedInstanceState.getString(PHOTO_PATH));
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		switch (menuItem.getItemId()) {
			case android.R.id.home:
				name_edit=pref_name.edit();
				name_edit.putString("username",txt_user_name.getText().toString());
				name_edit.commit();
				finish();
				break;
			case R.id.home:
				name_edit=pref_name.edit();
				name_edit.putString("username",txt_user_name.getText().toString());
				name_edit.commit();
				exportDB();
				Intent in=new Intent(MyProfile.this,HomeMain_activity.class);
				startActivity(in);
				finish();
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
						HiHelloConstant.UPLOAD_PIC_URL, filePath, contact_no,
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
//					HiHelloSharedPrefrence.setProfileImageUrl(
//							getApplicationContext(), url);
//					displayImage(url, profileImage);
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
		if (!HiHelloSharedPrefrence.getChatPageInit(getApplicationContext())) {
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
				txt_user_name.setText(HiHelloSharedPrefrence
						.getUserName(getApplicationContext()));
				break;
//		case REQUEST_STATUS:
//			txt_status.setText(HiHelloSharedPrefrence
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

		name_edit = pref_name.edit();
		name_edit.putString("username",txt_user_name.getText().toString());
		name_edit.commit();
		finish();
//		super.onBackPressed();
	}

	private void insertDummyContactWrapper() {
		int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
		if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
			requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
					REQUEST_CODE_ASK_PERMISSIONS);
			return;
		}

	}
	public void joinvolley()
	{
		final ProgressDialog dialog = new ProgressDialog(MyProfile.this);
		dialog.setMessage("Loading..Please wait.");
		dialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
		dialog.setCanceledOnTouchOutside(false);
//        dialog.show();


		RequestQueue queue= Volley.newRequestQueue(MyProfile.this);
		String username=txt_user_name.getText().toString();
		username=username.replace(" ","%20");
		String url1= HiHelloConstant.url+"fetch_joined.php?profile_name="+username;
		android.util.Log.e("url= ", url1);

		JsonObjectRequest request=new JsonObjectRequest(url1, null,new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject res)
			{
				data=new ArrayList<get_set_groups>();

				try {
					JSONArray jr=res.getJSONArray("name");
//                    dialog.dismiss();
					android.util.Log.e("response=",res.getJSONArray("name").toString());

					int len=jr.length();

					android.util.Log.e("length=",String.valueOf(len));

//                    Toast.makeText(getActivity(),"running",Toast.LENGTH_SHORT).show();
					for(int i=0;i<jr.length();i++)
					{
						JSONObject jsonobj=jr.getJSONObject(i);

						android.util.Log.e("aarray=",jr.toString());

						data.add(new get_set_groups(jsonobj.getInt("id"), jsonobj.getString("admin_name"), jsonobj.getString("group_name"), jsonobj.getString("about"), jsonobj.getString("alt_mobile"), jsonobj.getString("category"), jsonobj.getString("subcategory"), jsonobj.getString("email"), jsonobj.getString("weburl"), jsonobj.getString("state"), jsonobj.getString("city"), jsonobj.getString("address"),jsonobj.getString("date"), jsonobj.getString("admin_thumb"), jsonobj.getString("group_thumb"), jsonobj.getString("title"), jsonobj.getString("comment"), jsonobj.getString("follow"), jsonobj.getString("profile_name"), jsonobj.getString("admin_pic"), jsonobj.getString("group_pic")));
						db.insertjoingroup(jsonobj.getInt("id"), jsonobj.getString("admin_name"), jsonobj.getString("group_name"), jsonobj.getString("about"), jsonobj.getString("alt_mobile"), jsonobj.getString("category"), jsonobj.getString("subcategory"), jsonobj.getString("email"), jsonobj.getString("weburl"), jsonobj.getString("state"), jsonobj.getString("city"), jsonobj.getString("address"), jsonobj.getString("date"),jsonobj.getString("admin_pic"), jsonobj.getString("group_pic"), jsonobj.getString("title"), jsonobj.getString("comment"), jsonobj.getString("follow"), jsonobj.getString("profile_name"),jsonobj.getString("admin_thumb"), jsonobj.getString("group_thumb"));
//						id integer primary key,admin_name text ,group_name text,about text,alt_mobile text,category text,subcategory text,email text,weburl text,state text,city text,address text,date,text,admin_pic text,group_pic text,title text,comment text,follow text,profile_name text,admin_pich text,group_pich text
					}


					int si=db.getAllJoinGroup().size();
					android.util.Log.e("All Join Group Size", si + "  "+txt_user_name.getText().toString());
					db.close();

				} catch (JSONException e) {
					// TODO Auto-generated catch block
//                    dialog.dismiss();
					android.util.Log.e("catch exp= ", e.toString());
					e.printStackTrace();
				}

			}

		}
				,new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
//                dialog.dismiss();
				android.util.Log.e("error", arg0.toString());
			}
		});

		queue.add(request);
	}
	public void updatevolley()
	{
		String tokan= FirebaseInstanceId.getInstance().getToken();
		final ProgressDialog dialog = new ProgressDialog(MyProfile.this);
		dialog.setMessage("Loading..Please wait.");
		dialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
		dialog.setCanceledOnTouchOutside(false);
//        dialog.show();


		RequestQueue queue= Volley.newRequestQueue(MyProfile.this);
		String username=txt_user_name.getText().toString();
		username=username.replace(" ","%20");
		String url1= HiHelloConstant.url+"update_fcmid.php?contact="+mob_no+"&fcmid="+tokan;
		android.util.Log.e("url= ", url1);

		JsonObjectRequest request=new JsonObjectRequest(url1, null,new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject res)
			{
				try {
					if(res.getString("scalar").equals("update"))
					{

					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
//                    dialog.dismiss();
					android.util.Log.e("catch exp= ", e.toString());
					e.printStackTrace();
				}

			}

		}
				,new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
//                dialog.dismiss();
				android.util.Log.e("error", arg0.toString());
			}
		});

		queue.add(request);
	}

	public  void exportDB(){
		File sd = Environment.getExternalStorageDirectory();
		File data = Environment.getDataDirectory();
		FileChannel source=null;

		FileChannel destination=null;
		String currentDBPath = "/data/"+"com.hiihello.app"+"/databases/"+DATABASE_NAME;
		android.util.Log.e("pathhh","/data/"+"com.hiihello.app"+"/databases/"+DATABASE_NAME);
		String backupDBPath = DATABASE_NAME;
		File currentDB = new File(data, currentDBPath);
		File backupDB = new File(sd, backupDBPath);
		try {
			source = new FileInputStream(currentDB).getChannel();
			destination = new FileOutputStream(backupDB).getChannel();
			destination.transferFrom(source, 0, source.size());
			source.close();
			destination.close();

		} catch(IOException e) {


			android.util.Log.e("e", e.getMessage());
		}
	}

}



//Intent share = new Intent(android.content.Intent.ACTION_SEND);
//share.setType("text/plain");
//		share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
//		share.putExtra(Intent.EXTRA_SUBJECT, "GATE");
//		share.putExtra(Intent.EXTRA_TEXT,title+", "+" Vacancy "+vacacy+", Lastdate "+date+", Post "+postname+
//		", For more detail download Scholar app"+" "+"play.google.com/store/apps/details?id=com.scholar.engineering.banking.ssc");
//		startActivity(Intent.createChooser(share, "Share link!"));