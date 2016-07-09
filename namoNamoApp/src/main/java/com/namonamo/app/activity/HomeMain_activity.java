package com.namonamo.app.activity;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.namonamo.app.R;
import com.namonamo.app.adapter.MyAdapter;
import com.namonamo.app.adapter.NamoNamoContactAdapter;
import com.namonamo.app.adapter.PagerAdapter;
import com.namonamo.app.db.NamoNamoContact;
import com.namonamo.app.db.NamoNamoContactDBService;
import com.namonamo.app.fragment.ContactFragment;

import java.util.ArrayList;

/**
 * Created by rohan on 3/7/2016.
 */
public class HomeMain_activity extends BaseActivity
{
    int defaultValue = 1;
    int pos;
    public static final String ARG_PAGE = "ARG_PAGE";
    SlidingTabLayout tabs;
    CharSequence Titles[] = {"Groups", "Chats","Contacts"};
    int tabicon[]={R.drawable.group,R.drawable.chat,R.drawable.chat};
    int Numboftabs = 3;

    int PROFILE = R.drawable.raj;
    private Toolbar mToolbar;
    String TITLES[] = {"Profile","Status","New Chat","Get Recharge","Archive Chat","Earn Points","Group","Invite Friend","Block Contact","Ranking","Like Us","Settings","Help & Feedback"};
    // int ICONS[] = {R.drawable.signout, R.drawable.profile,R.drawable.counsellor,R.drawable.drawercourse,R.drawable.message};
    int ICONS[] = {R.drawable.profile,R.drawable.status,R.drawable.contact1,R.drawable.wallet,R.drawable.offers,R.drawable.earnpoint,R.drawable.creategroup1,
          R.drawable.invitefrnd,R.drawable.blockfrnd,R.drawable.ranking,R.drawable.like,R.drawable.setting1,R.drawable.helpandfeed};

    //Similarly we Create a String Resource for the name and email in the header view
    //And we also create a int resource for profile picture in the header view
    String NAME = "Rohan";
    String EMAIL = "rgoyal617@gmail.com";
    //int PROFILE = R.drawable.userprofile;

    //DrawerFragment Frag;
    private Toolbar toolbar;                              // Declaring the Toolbar Object

    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    DrawerLayout Drawer;                                  // Declaring DrawerLayout
    public static ImageView contact;
    ActionBarDrawerToggle mDrawerToggle;


    ViewPager vpager;
    PagerAdapter adapter;
    private ActionBar actionBar;

    //private android.support.v7.app.ActionBar actionBar;

    // String[] tabs={"National","State"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scholarship);


        // actionBar = getSupportActionBar();
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.drawer);

        contact=(ImageView)findViewById(R.id.contact);
        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View

        //  RecyclerView recyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
        //  recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());
        mRecyclerView.setHasFixedSize(true);                            // Letting the system know that the list objects are of fixed size
        SharedPreferences info = getSharedPreferences("userinfo",
                Context.MODE_PRIVATE);
        final String url=info.getString("PROFILE_IMAGE", "");
        final String url1="https://www.facebook.com/";
        SharedPreferences info1 = getSharedPreferences("userinfo",
                Context.MODE_PRIVATE);
        String name=info1.getString("USER_NAME", "");

        mAdapter = new MyAdapter(getApplicationContext(),TITLES,ICONS,name,EMAIL,url);       // Creating the Adapter of MyAdapter class(which we are going to see in a bit)
        // And passing the titles,icons,header view name, header view email,
        // and header view profile picture

//        contact.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent ing=new Intent(getApplicationContext(),AllContacts.class);
//                startActivity(ing);
//            }
//        });
        mRecyclerView.setAdapter(mAdapter);                              // Setting the adapter to RecyclerView
        final GestureDetector mGestureDetector=new GestureDetector(HomeMain_activity.this,new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return false;
            }
        });



        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(),motionEvent.getY());



                if(child!=null && mGestureDetector.onTouchEvent(motionEvent)){
                    Drawer.closeDrawers();
                    //   Toast.makeText(Scholarship.this, "The Item Clicked is: " + recyclerView.getChildPosition(child), Toast.LENGTH_SHORT).show();
                    switch (recyclerView.getChildPosition(child))
                    {


//                        case 0:
//                            Intent intent = new Intent(getApplicationContext(),MyProfile.class);
//                            startActivity(intent);
//
//                            break;
                        case 1:
                            Intent con = new Intent(getApplicationContext(),MyProfile.class);
                            startActivity(con);

                            break;
                        case 2:
                            Intent usef = new Intent(getApplicationContext(),EditStatus.class);
                            Log.e("xbjbx", "sxjsbjx");
                            startActivity(usef);

                            break;

                        case 3:
                            Intent lgn= new Intent(getApplicationContext(),AllContacts.class);
                            startActivity(lgn);
                            break;
                        case 4:
                            Intent in= new Intent(getApplicationContext(),MyWallet.class);
                            startActivity(in);
                            break;
                        case 5:
                            Intent ins= new Intent(getApplicationContext(),MyWallet.class);
                            startActivity(ins);
                            break;
                        case 6:
                            Intent ine= new Intent(getApplicationContext(),EarnPoint.class);
                            startActivity(ine);
                            break;
                        case 7:
                            Intent in1= new Intent(getApplicationContext(),Search.class);
                            startActivity(in1);
                            break;
                        case 8:
                            Intent ini= new Intent(getApplicationContext(),AllContacts.class);
                            startActivity(ini);
//                            Intent intent = new Intent(Intent.ACTION_PICK,
//                                    ContactsContract.Contacts.CONTENT_URI);
//                            intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
//                            startActivityForResult(intent, REQUEST_CONTACT);
                            break;
                        case 9:
                            Intent in2= new Intent(getApplicationContext(),BlockUserListActivity.class);
                            startActivity(in2);
                            break;
                        case 10:
                            Intent in3= new Intent(getApplicationContext(),Ranking.class);
                            startActivity(in3);
                            break;
                        case 11:
                            Intent inl=new Intent(Intent.ACTION_VIEW);
                            inl.setData(Uri.parse(url1));
                            startActivity(inl);
                            break;

                        case 12:
                            Intent in5= new Intent(getApplicationContext(),SettingPrefs.class);
                            startActivity(in5);
                            break;
                        case 13:
                            Intent inh= new Intent(getApplicationContext(),Creategroup.class);
                            startActivity(inh);
                            break;

                    }
                    return true;

                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
        mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager

        mRecyclerView.setLayoutManager(mLayoutManager);                 // Setting the layout Manager


        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);        // Drawer object Assigned to the view
        mDrawerToggle = new ActionBarDrawerToggle(this, Drawer, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // code here will execute once the drawer is opened( As I dont want anything happened whe drawer is
                // open I am not going to put anything here)
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // Code here will execute once drawer is closed
            }


        }; // Drawer Toggle Object Made
        Drawer.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        mDrawerToggle.syncState();

        vpager = (ViewPager) findViewById(R.id.pager);
        adapter= new PagerAdapter(getApplicationContext(), getSupportFragmentManager(),Titles,tabicon,Numboftabs);

        vpager.setAdapter(adapter);


        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1E90FF")));
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                pos=position;
//                Log.e("position",pos+"");
//                if(position==0)
//                    contact.setImageResource(R.drawable.groupsign);
//                else if(position==1)
//                    contact.setImageResource(R.drawable.chatsign);
//                else if(position==2)
//                    contact.setImageResource(R.drawable.contactplus1);


                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(vpager);
        int page = getIntent().getIntExtra(ARG_PAGE, defaultValue);
        vpager.setCurrentItem(page);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.recentchat_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();
        switch ( item.getItemId())
        {



        }
        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
        // }
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//
//
//        Log.e("onPrepare",pos+"");
//        if(pos==0) {
//            menu.getItem(1).setIcon(R.drawable.chatsign);
//        }
//        else if(pos==1)
//        {
//            menu.getItem(1).setIcon(R.drawable.search_btn);
//        }
//        else
//        {
//            menu.getItem(1).setIcon(R.drawable.new_chat);
//        }
//        return super.onPrepareOptionsMenu(menu);
//    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
