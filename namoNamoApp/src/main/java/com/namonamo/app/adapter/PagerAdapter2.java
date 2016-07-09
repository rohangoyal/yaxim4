package com.namonamo.app.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.namonamo.app.activity.Groupabout;
import com.namonamo.app.activity.groupcontect;


/**
 * Created by surbhi on 7/22/2015.
 */
public class PagerAdapter2 extends FragmentStatePagerAdapter

{
    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created
    int tabimages[];
    Context cs;

    public PagerAdapter2(Context cs,FragmentManager supportFragmentManager, CharSequence[] titles, int[] tabimage, int numboftabs) {
        super(supportFragmentManager);
        this.Titles = titles;
        this.tabimages=tabimage;
        this.NumbOfTabs = numboftabs;
        this.cs=cs;
    }
    public PagerAdapter2(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

    @Override
    public Fragment getItem(int position) {


        if(position == 0) // if the position is 0 we are returning the First tab
        {
            groupcontect frag = new groupcontect();
            return frag;
        }
        else             // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
        {

            Groupabout frag = new Groupabout();
            return frag;


        }


    }

    // This method return the titles for the Tabs in the Tab Strip

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

