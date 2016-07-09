package com.hihello.app.activity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.hihello.app.R;
import com.hihello.app.adapter.MyAdapter1;
import com.hihello.app.adapter.PagerAdapter2;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by surbhi on 7/13/2015.
 */
public class Groupform extends ActionBarActivity

{

    SlidingTabLayout tabs;
    CharSequence Titles[] = {"Create", "About"};
    int tabicon[]={R.drawable.group,R.drawable.chat};
    int Numboftabs = 2;
    String mnumber,lline,eadd,wsite,stretadd,zadd,ccadd,adminname,groupname1,adminimagepath,wallpaperpath;
    int PROFILE = R.drawable.raj;
    private Toolbar mToolbar;
    String TITLES[] = {"Profile","Status","New Chat","My Wallet","Archive Chat","Earn Points","Group","Invite Friend","Blocked People","Ranking","Like Us","Settings","Help & Feedback"};
   // int ICONS[] = {R.drawable.signout, R.drawable.profile,R.drawable.counsellor,R.drawable.drawercourse,R.drawable.message};
   int ICONS[] = {R.drawable.profile,R.drawable.status,R.drawable.contact1,R.drawable.wallet,R.drawable.contact1,R.drawable.earnpoint,R.drawable.creategroup1,
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

    ActionBarDrawerToggle mDrawerToggle;

    ImageView contact;
    ViewPager vpager;
    PagerAdapter2 adapter;
    private ActionBar actionBar;

    //private android.support.v7.app.ActionBar actionBar;

    // String[] tabs={"National","State"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.groupform);
       // actionBar = getSupportActionBar();
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.drawer);

        Intent intent = getIntent();
        adminname  = intent.getStringExtra("adminname");
        groupname1 = intent.getStringExtra("groupname");
        adminimagepath = intent.getStringExtra("adminphotopath");
        wallpaperpath = intent.getStringExtra("wallimagepath");

        contact=(ImageView)findViewById(R.id.contact);
//        contact.setVisibility(View.GONE);
        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View

        //  RecyclerView recyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
        //  recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());
        mRecyclerView.setHasFixedSize(true);                            // Letting the system know that the list objects are of fixed size

        mAdapter = new MyAdapter1(TITLES,ICONS,NAME,EMAIL,PROFILE);       // Creating the Adapter of MyAdapter class(which we are going to see in a bit)
        // And passing the titles,icons,header view name, header view email,
        // and header view profile picture

        mRecyclerView.setAdapter(mAdapter);                              // Setting the adapter to RecyclerView
        final GestureDetector mGestureDetector=new GestureDetector(Groupform.this,new GestureDetector.OnGestureListener() {
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
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }

            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(),motionEvent.getY());



                if(child!=null && mGestureDetector.onTouchEvent(motionEvent)){
                    Drawer.closeDrawers();
                 //   Toast.makeText(Scholarship.this, "The Item Clicked is: " + recyclerView.getChildPosition(child), Toast.LENGTH_SHORT).show();
                    switch (recyclerView.getChildPosition(child))
                    {

                        case 1:
                            Intent con = new Intent(getApplicationContext(),UserProfile.class);
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
                            Intent in4= new Intent(getApplicationContext(),SettingPrefs.class);
                            startActivity(in4);
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
        adapter= new PagerAdapter2(getApplicationContext(), getSupportFragmentManager(),Titles,tabicon,Numboftabs);

        vpager.setAdapter(adapter);


        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1E90FF")));
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(vpager);



    }

    public void volley()
    {
//	     final ProgressDialog dialog = new ProgressDialog(getApplicationContext(),R.style.AppTheme);
//	    // dialog.setMessage("Loading..Please wait.");
//	     dialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
//	     dialog.setCanceledOnTouchOutside(false);
//	     dialog.show();
        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());


        final String username;
//	  mnumber,lline,eadd,wsite,stretadd,zadd,ccadd,adminname,groupname
//	  mobile,landline,email,website,streetadd,zipadd,cityadd
        String lastnm, num, password1, url, frstname, colg, ct;
        mnumber=groupcontect.mobile.getText().toString();
        lline=groupcontect.landline.getText().toString();
        eadd=groupcontect.email.getText().toString();
        wsite=groupcontect.website.getText().toString();
        stretadd=groupcontect.streetadd.getText().toString();

        ccadd=groupcontect.cityadd.getText().toString();
        Log.e("Mobile number", mnumber);

//	  pref=getSharedPreferences("abc",MODE_PRIVATE);
//    Editor edit=pref.edit();
//    edit.putString("username",username);
//    edit.putString("number","num");
//
//
//
//    edit.commit();



//    url="http://www.delainetech.com/mechanicalinsider/signup.php?firstname="+frstname+"&lastname="+lastnm+"&email="+username+"&password="+password1+"&number="+num+"&college="+colg+"&city="+ct+"&code="+rmn;
        url="http://delainetechnologies.com/gyanProject/webservices/info.php?wall_image="+wallpaperpath+"&profile_image="+adminimagepath+"&app_contact="+mnumber+"&admin_name="+adminname+"&group_name="+groupname1+"&email="+eadd+"&phone="+mnumber+"&landline="+lline+"&url="+wsite+"&address="+ccadd+"&state="+"haryana"+"&category="+"ok"+"&followers="+0+"&viewers="+0;
        url=url.replace(" ","%20");
        Log.e("name", url);
        JsonObjectRequest jsonreq=new JsonObjectRequest( url, null,new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject arg0) {
                // TODO Auto-generated method stub

//			dialog.dismiss();
                Log.e("res", arg0.toString());


                try{
                    if(arg0.getString("scalar").equals("Successfully inserted")==true)
                    {

                        AlertDialog.Builder alert1=new AlertDialog.Builder(getApplicationContext());
                        alert1.setTitle("Information");
                        alert1.setMessage("Sucessfully signed up");

                        AlertDialog dailog=alert1.create();
                        dailog.getWindow().setLayout(600, 400);
                        // dailog.show();

//		         dialog.dismiss();

                        Intent in=new Intent(getApplicationContext(),groupprofile.class);
                        Toast.makeText(getApplicationContext(), "data save successful", Toast.LENGTH_LONG).show();

                        startActivity(in);
                    }

                    else
                    {
                        AlertDialog.Builder alert1=new AlertDialog.Builder(getApplicationContext());
                        alert1.setTitle("Information");
                        alert1.setMessage(arg0.getString("scalar"));
                        AlertDialog dailog=alert1.create();
                        dailog.show();

                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Log.e("hi",e.getMessage());
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError e) {
                // TODO Auto-generated method stub
//			dialog.dismiss();
                Log.e("error", e.toString());
//			Toast.makeText(getApplicationContext(),String.valueOf(e), Toast.l).show();
            }
        });
        queue.add(jsonreq);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.form_menu, menu);
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


            case  R.id.action_settings:
                volley();
                break;
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

    @Override
    public void onBackPressed() {
        Intent i=new Intent(Groupform.this,HomeMain_activity.class);
        startActivity(i);
        finish();
    }
}



