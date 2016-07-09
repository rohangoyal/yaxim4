package com.hihello.app.activity;


import com.hihello.app.R;
import com.hihello.app.R.color;
import com.hihello.app.adapter.PagerAdapter;
import com.hihello.app.adapter.nadapter;

import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Home extends FragmentActivity implements TabListener
{
	DrawerLayout dlayout;
	 FrameLayout frame;
	 ListView dList;
	 nadapter n1;
	ImageView search,contact,setting,notification,drawer;
	ViewPager viewpager;
	TextView hihello;
	EditText searchtext;
	PagerAdapter swipe;
	String[] s={"Groups","Chat"};
    ActionBar bar;
    Boolean b=false;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.home);
		bar=getActionBar();
		bar.setDisplayShowHomeEnabled(false);
		  bar.setDisplayShowTitleEnabled(false);
	LayoutInflater inflater=LayoutInflater.from(this);
	  View mCustomView = inflater.inflate(R.layout.homeheader, null);
//	  search=(ImageView)mCustomView.findViewById(R.id.imageView1);
	  contact=(ImageView)mCustomView.findViewById(R.id.imageView2);
	  setting=(ImageView)mCustomView.findViewById(R.id.imageView3);
	  
//	  notification=(ImageView)mCustomView.findViewById(R.id.imageView4);
//	  searchtext=(EditText)mCustomView.findViewById(R.id.editText1);
	  hihello=(TextView)mCustomView.findViewById(R.id.registerid);
//	  searchtext.setVisibility(View.GONE);
	  bar.setCustomView(mCustomView);
	  bar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#00ADEF")));
	  bar.setDisplayShowCustomEnabled(true);
		viewpager = (ViewPager)findViewById(R.id.pager);
		viewpager.setBackgroundColor(color.orange);
	 	swipe=new PagerAdapter(getSupportFragmentManager());
	 	viewpager.setAdapter(swipe);
	 	 drawer=(ImageView)mCustomView.findViewById(R.id.backarrowid);
	       drawer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Toast.makeText(getApplicationContext(), "drawerrrrrrrrr", Toast.LENGTH_LONG).show();
				if (dlayout.isDrawerOpen(Gravity.LEFT)) {
				      dlayout  .closeDrawer(Gravity.LEFT);
				    } else {
				        dlayout.openDrawer(Gravity.LEFT);
				    }
			}
		});
	 	contact.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent in=new Intent(getApplicationContext(),AllContacts.class);
				startActivity(in);			}
		});
//	 	search.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				if(b==false)
//				{
////				searchtext.setVisibility(View.VISIBLE);
//				notification.setVisibility(View.GONE);
//				hihello.setVisibility(View.GONE);
//				b=true;
//				}
//				else
//				{
////					searchtext.setVisibility(View.GONE);
//					notification.setVisibility(View.VISIBLE);
//					hihello.setVisibility(View.VISIBLE);
//					b=false;
//				}
//				
//			}
//		});
	 	
	 	
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
        
        n1=new nadapter();
    	
   	 dList = (ListView)findViewById(R.id.left_drawer);
   	 dList.setAdapter(n1);
   	 dList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				dlayout.closeDrawers();
		         switch (arg2) {
		  
		        
		    case 0:
		    	Intent intent = new Intent(getApplicationContext(),MyProfile.class);
	             startActivity(intent);
			
		    break;
		    case 1:
		    	Intent con = new Intent(getApplicationContext(),MyProfile.class);
	             startActivity(con);
			
		    break;
		    case 2:
		    	Intent usef = new Intent(getApplicationContext(),AllContacts.class);
		    	Log.e("xbjbx","sxjsbjx");
	             startActivity(usef);
		    	
		    	break;
		    	
		    case 3:
		    	Intent lgn= new Intent(getApplicationContext(),MyWallet.class);
		    	
		    	
		        
		    	break;
		    case 4:
		    	Intent in= new Intent(getApplicationContext(),Search.class);
		    	startActivity(in);
		    	break;
		    case 5:
		    	Intent in1= new Intent(getApplicationContext(),Creategroup.class);
		    	startActivity(in1);
		    	break;
		    	
		    case 6:
		    	Intent in2= new Intent(getApplicationContext(),Creategroup.class);
		    	startActivity(in2);
		    	break;
		    case 7:
		    	Intent in3= new Intent(getApplicationContext(),SettingPrefs.class);
		    	startActivity(in3);
		    	break;
		    case 8:
		    	Intent in4= new Intent(getApplicationContext(),Creategroup.class);
		    	startActivity(in4);
		    	break;
		         }
		         }
		});	
	}
	
	
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
	
	
//		
		public void setContentView(final int id)
	 {				
				
		 dlayout=(DrawerLayout)getLayoutInflater().inflate(R.layout.barxmml,null);
		 frame = (FrameLayout)dlayout.findViewById(R.id.content_frame);
			getLayoutInflater().inflate(id, frame,true);
			super.setContentView(dlayout);
		 
		  bar =getActionBar();
	      bar.setDisplayShowCustomEnabled(true);
	      bar.setHomeButtonEnabled(false);
	      bar.setDisplayShowHomeEnabled(false);
	      bar.setDisplayShowTitleEnabled(false);
	      bar.setIcon(null);
	      LayoutInflater inflator = (LayoutInflater) this .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	       View v = inflator.inflate(R.layout.homeheader, null);
	      
//	      groupname=(TextView)v.findViewById(R.id.registerid);
//	       save=(TextView)v.findViewById(R.id.textView1);
//	       back=(ImageView)v.findViewById(R.id.backarrowid);   
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
Intent in=new Intent(getApplicationContext(),jobs.class);
startActivity(in);
		super.onBackPressed();
	}

}
