package com.hihello.app.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.hihello.app.activity.AllRanking;
import com.hihello.app.activity.DailyRanking;
import com.hihello.app.activity.ContactRanking;

public class RankingPagerAdapter extends FragmentStatePagerAdapter {

	// Declare the number of ViewPager pages
	final int PAGE_COUNT = 3;
	CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
	int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created
	int tabimages[];
	Context cs;

	public RankingPagerAdapter(Context cs,FragmentManager supportFragmentManager, CharSequence[] titles, int[] tabimage, int numboftabs) {
		super(supportFragmentManager);
		this.Titles = titles;
		this.tabimages=tabimage;
		this.NumbOfTabs = numboftabs;
		this.cs=cs;
	}
	public RankingPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {

		if(position == 0) // if the position is 0 we are returning the First tab
		{
			ContactRanking frg = new ContactRanking();
			return frg;
		}
		else if(position == 1) // if the position is 1 we are returning the First tab
		{
			AllRanking frg = new AllRanking();
			return frg;
		}
		else             // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
		{
			DailyRanking frg = new DailyRanking();
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
		return 3;
	}


}
