package com.hihello.app.activity;


import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.hihello.app.R;


import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;



public class jobs extends FragmentActivity implements TabListener
{


	DrawerLayout dlayout;
	String mnumber,lline,eadd,wsite,stretadd,zadd,ccadd,adminname,groupname1,adminimagepath,wallpaperpath;
   TextView groupname,save;
   ImageView back;
	ViewPager viewpager;
	tabswipe swipe;
	String[] s={"Contact","About"};
    ActionBar bar;
    
	//@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	//@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.jobpg);
		bar=getActionBar();
		bar.setDisplayShowHomeEnabled(false);
		  bar.setDisplayShowTitleEnabled(false);
	LayoutInflater inflater=LayoutInflater.from(this);
	  View mCustomView = inflater.inflate(R.layout.customactionbar, null);
	  bar.setCustomView(mCustomView);
	  bar.setDisplayShowCustomEnabled(true);
		viewpager = (ViewPager)findViewById(R.id.pager);
		bar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#00ADEF")));
		 Intent intent = getIntent();         
		 adminname  = intent.getStringExtra("adminname");
		  groupname1 = intent.getStringExtra("groupname");
		  adminimagepath = intent.getStringExtra("adminphotopath");
		  wallpaperpath = intent.getStringExtra("wallimagepath");
		save=(TextView)mCustomView.findViewById(R.id.textView2);
		save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				volley();
			}
		});
	 	swipe=new tabswipe(getSupportFragmentManager());
//         getActionBar().hide();
	 	
//	 	bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#f15c51")));
//	    bar.setTitle(Html.fromHtml("<font color='#ffffff'><b>Jobs</b></font>"));
	    
	 	viewpager.setAdapter(swipe);
	 	viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				 Log.e("selected","selected");
			     bar.setSelectedNavigationItem(arg0);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
	 	bar.setHomeButtonEnabled(false);
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        for(String tab:s)
        {
        bar.addTab(bar.newTab().setText(tab).setTabListener(this));
        }
	}


	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		viewpager.setCurrentItem(tab.getPosition());
	}

	
	
	
	
	
//	 @Override
//		
//		public void setContentView(final int id)
//	 {				
//				
//		 dlayout=(DrawerLayout)getLayoutInflater().inflate(R.layout.barxmml,null);
//		 frame = (FrameLayout)dlayout.findViewById(R.id.content_frame);
//			getLayoutInflater().inflate(id, frame,true);
//			super.setContentView(dlayout);
//		 
//		  bar =getActionBar();
//	      bar.setDisplayShowCustomEnabled(true);
//	      bar.setHomeButtonEnabled(false);
//	      bar.setDisplayShowHomeEnabled(false);
//	      bar.setDisplayShowTitleEnabled(false);
//	      bar.setIcon(null);
//	      LayoutInflater inflator = (LayoutInflater) this .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//	       View v = inflator.inflate(R.layout.actionbar, null);
//	      groupname=(TextView)v.findViewById(R.id.registerid);
//	       save=(TextView)v.findViewById(R.id.textView1);
//	       back=(ImageView)v.findViewById(R.id.backarrowid);   
//	}
	
	
	
	
	
	
	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft)
	{
		// TODO Auto-generated method stub

	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
Intent in=new Intent(getApplicationContext(),Creategroup.class);
startActivity(in);
		super.onBackPressed();
	}
	public void volley()
	 {
//	     final ProgressDialog dialog = new ProgressDialog(getApplicationContext(),R.style.AppTheme);
//	    // dialog.setMessage("Loading..Please wait.");
//	     dialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
//	     dialog.setCanceledOnTouchOutside(false);
//	     dialog.show();
	    RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
	    
	    
	  final String username;
//	  mnumber,lline,eadd,wsite,stretadd,zadd,ccadd,adminname,groupname
//	  mobile,landline,email,website,streetadd,zipadd,cityadd
	String lastnm, num, password1, url, frstname, colg, ct;
	  mnumber=groupcontect.mobile.getText().toString();
	  lline=groupcontect.landline.getText().toString();
	  eadd=groupcontect.email.getText().toString();
	  wsite=groupcontect.website.getText().toString();
	  stretadd=groupcontect.streetadd.getText().toString();  
	 
	  ccadd=groupcontect.cityadd.getText().toString();
	  Log.e("Mobile number",mnumber);
	 
//	  pref=getSharedPreferences("abc",MODE_PRIVATE);
//    Editor edit=pref.edit();
//    edit.putString("username",username);
//    edit.putString("number","num");
//   
//    
//    
//    edit.commit();
	      
    
    
//    url="http://www.delainetech.com/mechanicalinsider/signup.php?firstname="+frstname+"&lastname="+lastnm+"&email="+username+"&password="+password1+"&number="+num+"&college="+colg+"&city="+ct+"&code="+rmn;
   url="http://delainetechnologies.com/gyanProject/webservices/info.php?wall_image="+wallpaperpath+"&profile_image="+adminimagepath+"&app_contact="+mnumber+"&admin_name="+adminname+"&group_name="+groupname1+"&email="+eadd+"&phone="+mnumber+"&landline="+lline+"&url="+wsite+"&address="+ccadd+"&state="+"haryana"+"&category="+"ok"+"&followers="+0+"&viewers="+0;
    url=url.replace(" ","%20");
    Log.e("name",url); 
	  JsonObjectRequest jsonreq=new JsonObjectRequest( url, null,new Response.Listener<JSONObject>() {

		@Override
		public void onResponse(JSONObject arg0) {
			// TODO Auto-generated method stub
			
//			dialog.dismiss();
				Log.e("res", arg0.toString());
				
			
			try{
			  if(arg0.getString("scalar").equals("Successfully inserted")==true)
		        {
		         
		         AlertDialog.Builder alert1=new AlertDialog.Builder(getApplicationContext());
		         alert1.setTitle("Information");
		         alert1.setMessage("Sucessfully signed up");
		         
		         AlertDialog dailog=alert1.create();
		         dailog.getWindow().setLayout(600, 400);
		        // dailog.show(); 
		         
//		         dialog.dismiss();
		         
		         Intent in=new Intent(getApplicationContext(),HomeMain_activity.class);
		         Toast.makeText(getApplicationContext(), "data save successful", Toast.LENGTH_LONG).show();
		         
		         startActivity(in);
		        }
		        
		       else
		        {
		         AlertDialog.Builder alert1=new AlertDialog.Builder(getApplicationContext());
		         alert1.setTitle("Information");
		         alert1.setMessage(arg0.getString("scalar"));
		         AlertDialog dailog=alert1.create();
		         dailog.show(); 
		                  
		        }
		       } catch (JSONException e) {
		        // TODO Auto-generated catch block
		        Log.e("hi",e.getMessage());
		        e.printStackTrace();
		       } 
		}
	}, new Response.ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError e) {
			// TODO Auto-generated method stub
//			dialog.dismiss();
			Log.e("error", e.toString());
//			Toast.makeText(getApplicationContext(),String.valueOf(e), Toast.l).show();
		}
	});
	  queue.add(jsonreq);

			  
	 }
	
	
}
