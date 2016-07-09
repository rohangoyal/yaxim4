package com.namonamo.group;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class PagerAdapter extends FragmentPagerAdapter {

	public PagerAdapter(FragmentManager fm) {
		super(fm);
	}
	
	
	@Override
	public Fragment getItem(int arg0) {

		int ii=arg0%2;

		switch (ii) {
		case 0:
			return new FirstView();
		case 1:
			return new SecondView();
//		case 2:
//			return new ThirdView();
//		case 3:
//			return new FourthView();
//
		default:
			break;
		}
		return null;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 10;
	}

}
