package com.hihello.group;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class PagerAdapter extends FragmentPagerAdapter {

	String data[];
	public PagerAdapter(FragmentManager fm,String [] data) {
		super(fm);
		this.data=data;
	}
	
	
	@Override
	public Fragment getItem(int arg0) {

		int ii=arg0%2;


		Bundle b=new Bundle();
		b.putString("url",data[arg0]);
		FirstView first=new FirstView();
		first.setArguments(b);
			return first;


	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.length;
	}

}
