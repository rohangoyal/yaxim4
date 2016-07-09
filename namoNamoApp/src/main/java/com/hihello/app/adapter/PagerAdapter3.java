package com.hihello.app.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hihello.group.FirstView;
import com.hihello.group.FourthView;
import com.hihello.group.SecondView;
import com.hihello.group.ThirdView;


public class PagerAdapter3 extends FragmentPagerAdapter {

	public PagerAdapter3(FragmentManager fm) {
		super(fm);
	}
	
	
	@Override
	public Fragment getItem(int arg0) {
		
		switch (arg0) {
		case 0:
			return new FirstView();
		case 1:
			return new SecondView();
		case 2:
			return new ThirdView();
		case 3:
			return new FourthView();
//
		default:
			break;
		}
		return null;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 4;
	}

}
