package com.hihello.app.activity;


import com.hihello.app.R;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class Creategroup extends BaseActivity implements OnClickListener
{
	 private static final int SELECT_PICTURE = 1;

	private String selectedImagePath;
	EditText adminname,groupname;
	ImageView adminedit,groupedit,adminpan,wallimage;
	String admintxt,grouptxt,adminphotopath,wallimagepath;
	CircularImageView adminphoto;
	ImageView next;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.creategroup);
		Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//		getSupportActionBar().setHomeAsUpIndicator(R.drawable.leftback);
		getSupportActionBar().setTitle("Create Group");
//		next=(ImageView)findViewById(R.id.nextarrowid);
		adminedit=(ImageView)findViewById(R.id.adminpencil);
		groupedit=(ImageView)findViewById(R.id.grouppencil);
//		getActionBar().hide();
		adminname=(EditText)findViewById(R.id.admin);
		groupname=(EditText)findViewById(R.id.group);
		
		adminphoto = (CircularImageView)findViewById(R.id.imageView1);
        adminpan=(ImageView)findViewById(R.id.adminpan);
        wallimage=(ImageView)findViewById(R.id.wallimage);
        
        adminphoto.setOnClickListener(this);
        adminpan.setOnClickListener(this);
        wallimage.setOnClickListener(this);
		
		adminname.requestFocus();
		groupname.requestFocus();
//		next.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				admintxt=adminname.getText().toString();
//				grouptxt=groupname.getText().toString();
//				if(grouptxt.length()>0 && admintxt.length()>0)
//				{
//				Intent in=new Intent(getApplicationContext(),Groupform.class);
//				in.putExtra("adminname", admintxt);
//				in.putExtra("groupname", grouptxt);
//				in.putExtra("adminphotopath",adminphotopath);
//				in.putExtra("wallimagepath",wallimagepath);
//				startActivity(in);
//				}
//				else
//				{
//					Toast.makeText(getApplicationContext(), "Please Enter Admin Name And Group Name", Toast.LENGTH_LONG).show();
//				}
//			}
//		});
		
	}
	 @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		 if (resultCode == RESULT_OK) {
	            if (requestCode == SELECT_PICTURE) {
	                Uri selectedImageUri = data.getData();
	                selectedImagePath = getPath(selectedImageUri);
	                System.out.println("Image Path : " + selectedImagePath);
//	              Toast.makeText(getApplicationContext(),selectedImagePath, Toast.LENGTH_LONG).show();
	                adminphotopath=selectedImagePath;
	                adminphoto.setImageURI(selectedImageUri);
	            }
	            else
	            {
	            	Uri selectedImageUri = data.getData();
	                selectedImagePath = getPath(selectedImageUri);
	                System.out.println("Image Path : " + selectedImagePath);
//	                Toast.makeText(getApplicationContext(),selectedImagePath, Toast.LENGTH_LONG).show();
	                wallimagepath=selectedImagePath;
	               wallimage.setImageURI(selectedImageUri);
	            }
	        }
	}

	private String getPath(Uri uri) {
		// TODO Auto-generated method stub
		String[] projection = { MediaStore.Images.Media.DATA };
        @SuppressWarnings("deprecation")
		Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()==R.id.imageView1)
		{
		 Intent intent = new Intent();
      intent.setType("image/*");
      intent.setAction(Intent.ACTION_GET_CONTENT);
      startActivityForResult(Intent.createChooser(intent,"Select Picture"), SELECT_PICTURE);
		}
		else if(v.getId()==R.id.adminpan || v.getId()==R.id.wallimage )
		{

			Intent intent = new Intent();
             intent.setType("image/*");
             intent.setAction(Intent.ACTION_GET_CONTENT);
             startActivityForResult(Intent.createChooser(intent,"Select Picture"), 0);
//             button.setVisibility(v.GONE);

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.status_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		switch (menuItem.getItemId()) {
			case android.R.id.home:
				finish();
				break;
			case R.id.post:
				admintxt=adminname.getText().toString();
				grouptxt=groupname.getText().toString();
				if(grouptxt.length()>0 && admintxt.length()>0)
				{
					Intent in=new Intent(getApplicationContext(),Groupform.class);
					in.putExtra("adminname", admintxt);
					in.putExtra("groupname", grouptxt);
					in.putExtra("adminphotopath",adminphotopath);
					in.putExtra("wallimagepath",wallimagepath);
					startActivity(in);
				}
				else
				{
					Toast.makeText(getApplicationContext(), "Please Enter Admin Name And Group Name", Toast.LENGTH_LONG).show();
				}
				break;
		}
		return super.onOptionsItemSelected(menuItem);
	}
}
