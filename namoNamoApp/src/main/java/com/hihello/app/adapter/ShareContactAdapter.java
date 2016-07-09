package com.hihello.app.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hihello.app.fragment.ContactFragment;
import com.hihello.app.fragment.RecentFragment;

public class ShareContactAdapter extends FragmentPagerAdapter {

	// Declare the number of ViewPager pages
	final int PAGE_COUNT = 2;
	CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
	int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created
	int tabimages[];
	Context cs;

	public ShareContactAdapter(Context cs,FragmentManager supportFragmentManager, CharSequence[] titles, int[] tabimage, int numboftabs) {
		super(supportFragmentManager);
		this.Titles = titles;
		this.tabimages=tabimage;
		this.NumbOfTabs = numboftabs;
		this.cs=cs;
	}

	public ShareContactAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		if(position == 0) // if the position is 0 we are returning the First tab
		{
			RecentFragment frg = new RecentFragment();
			return frg;
		}
		else  // if the position is 1 we are returning the First tab
		{
			ContactFragment frg = new ContactFragment();
			return frg;
		}
	}

	@Override
	public CharSequence getPageTitle(int position)

	{
		return Titles[position];
	}

	// This method return the Number of tabs for the tabs Strip

	@Override
	public int getCount()
	{
		return 2;
	}

}
