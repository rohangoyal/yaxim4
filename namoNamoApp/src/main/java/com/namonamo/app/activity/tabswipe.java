
package com.namonamo.app.activity;

import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class tabswipe extends FragmentPagerAdapter{

	

	public tabswipe(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int index) {
		
		
		switch(index)
		{
		case 0:
		 return new groupcontect();
		case 1:
		return new Groupabout();
		
			}
		return null;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 2;
	}

}
