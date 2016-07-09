package com.hihello.app.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.hihello.app.common.Log;
import com.hihello.app.fragment.Recentchatfragment;
import com.hihello.app.fragment.Recentcontactfragment;
import com.hihello.app.fragment.firstlist;


/**
 * Created by surbhi on 7/22/2015.
 */
public class PagerAdapter extends FragmentStatePagerAdapter

{
    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created
    int tabimages[];
    Context cs;

    public PagerAdapter(Context cs,FragmentManager supportFragmentManager, CharSequence[] titles, int[] tabimage, int numboftabs) {
        super(supportFragmentManager);
        this.Titles = titles;
        this.tabimages=tabimage;
        this.NumbOfTabs = numboftabs;
        this.cs=cs;
    }
    public PagerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

    @Override
    public Fragment getItem(int position) {


        if(position == 0) // if the position is 0 we are returning the First tab
        {
            Log.e("firstlist","firstlisttt");
            firstlist frag = new firstlist();
            Log.e("firstlist","firstlisttt");
            return frag;
        }
       else if(position == 1) // if the position is 1 we are returning the First tab
        {
            Recentchatfragment frag1 = new Recentchatfragment();
            return frag1;
        }
        else             // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
        {
            Recentcontactfragment frag2 = new Recentcontactfragment();
           // Scholarship.contact.setImageResource(R.drawable.contactplus1);
            return frag2;


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
        return 3;
    }
}

