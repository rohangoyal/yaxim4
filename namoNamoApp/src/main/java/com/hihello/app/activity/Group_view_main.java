package com.hihello.app.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.androidquery.AQuery;
import com.google.android.gms.maps.GoogleMap;
import com.google.firebase.iid.FirebaseInstanceId;
import com.hihello.app.R;
import com.hihello.app.adapter.Commentadapter;
import com.hihello.app.adapter.Joingroupadapter;
import com.hihello.app.constant.HiHelloConstant;
import com.hihello.app.constant.NetworkConnection;
import com.hihello.app.db.DatabaseHandler;
import com.hihello.app.db.HiHelloContact;
import com.hihello.app.db.hiHelloContactDBService;
import com.hihello.app.getset.get_comment;
import com.hihello.app.getset.get_join;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by delaine on 6/30/2016.
 */
public class Group_view_main extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener  {
    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR  = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS     = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION              = 200;
    private static final int IO_BUFFER_SIZE = 300;
    SharedPreferences pref_name;
    private boolean mIsTheTitleVisible          = false;
    private boolean mIsTheTitleContainerVisible = true;
    String contact,wallimage,profileimage,username;
    ArrayList<get_comment> data1;
    ArrayList<get_join> data2;
    LinearLayout location_layout;
    private LinearLayout mTitleContainer;
    private TextView mTitle,mcontact,mothercontact,musername,mstatus,show_last_seen;
    private AppBarLayout mAppBarLayout;
    private ImageView mImageparallax;
    private FrameLayout mFrameParallax;
    private Toolbar mToolbar;
    private CircleImageView profile;
    AQuery aquery;
    String url,address;
    DatabaseHandler db;
    private HiHelloContact contacts;
    double lat,lng;
    GoogleMap map;
    Bitmap photo;
    ImageView mapPreview;
    TextView footer,group_name,aboutid,categoryid,subcateid,contactid,alternateid,locationid,emailid,websiteid,follower;
    String admin_pich;
    private String jid;
    LinearLayout web_layout;
    WebView mWebView;
    ImageView close;
    int width,height;
    ListView commentlist;
    int dataa=0,datab=0;
    String group_pich,admin,groupname,about,cate,subcate,altno,location,email,weburl,city,locstate,date,group_pic,admin_pic,name,follow,group_id,profile_name;
    Commentadapter commadapter;
    //    TextView commentbtn,followbtn;
    CardView aboutcard,categorycard,contactcard,webcard,emailidd,titlecard,datecard,listcard;
    public  static String str= "false";
    LinearLayout list_layout;
    Joingroupadapter joinadapter;
    NetworkConnection nw;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    private DisplayImageOptions options;
    LinearLayout aboutlayout,followlayout,commentlayout;
    ImageView aboutimg,commentimg,followimg;
    TextView abouttxt,comment,titleid,activedateid;
    String contactmob,title_id,name_pich,mob_no="";
    SharedPreferences pref;
    public static String pic_url,comfollow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Display display = getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();
        Log.e("height", height + "hty");
        if(height<900) {
            setContentView(R.layout.group_view);
        }
        else if(height>=900 & height<1400)
        {
            setContentView(R.layout.group_viewbig);
        }
        else
        {
            setContentView(R.layout.group_viewlarge);
        }

        db = new DatabaseHandler(this);
        pref =getSharedPreferences("MyPref", 1);
        contactmob=pref.getString("contact","");
        mob_no=pref.getString("contact","");
        mob_no="91"+mob_no;
        nw=new NetworkConnection(Group_view_main.this);
        userpicvolley();
        pref_name=getSharedPreferences("pref_name", 1);
        name=pref_name.getString("username", "");
        com.hihello.app.common.Log.e("usernameee", name);

//        adminname=(TextView)findViewById(R.id.adminnametxt);
        group_name=(TextView)findViewById(R.id.groupnametxt);
        aboutid=(TextView)findViewById(R.id.aboutid);
        categoryid=(TextView)findViewById(R.id.categoryid);
        subcateid=(TextView)findViewById(R.id.subcateid);
        contactid=(TextView)findViewById(R.id.contactid);
        alternateid=(TextView)findViewById(R.id.alternateid);
        locationid=(TextView)findViewById(R.id.locationid);
        emailid=(TextView)findViewById(R.id.emailid);
        websiteid=(TextView)findViewById(R.id.websiteid);
        comment=(TextView)findViewById(R.id.commenttxt);
        abouttxt=(TextView)findViewById(R.id.abouttxt);
        mapPreview = (ImageView) findViewById(R.id.mapPreview);
        follower=(TextView)findViewById(R.id.followtxt);
        titleid=(TextView)findViewById(R.id.titleid);
        activedateid=(TextView)findViewById(R.id.dateid);
        location_layout=(LinearLayout)findViewById(R.id.locationlayout);

        aboutlayout=(LinearLayout)findViewById(R.id.about_layout);
        commentlayout=(LinearLayout)findViewById(R.id.commentlayout);
        followlayout=(LinearLayout)findViewById(R.id.followlayout);

        aboutimg=(ImageView)findViewById(R.id.aboutimage);
        commentimg=(ImageView)findViewById(R.id.commentimage);
        followimg=(ImageView)findViewById(R.id.followimage);
        footer=(TextView)findViewById(R.id.footerid);


        commentlist=(ListView)findViewById(R.id.listView4);
//        commentbtn=(TextView)findViewById(R.id.textView34);
        aboutcard=(CardView)findViewById(R.id.aboutcard);
        categorycard=(CardView)findViewById(R.id.categorycard);
        contactcard=(CardView)findViewById(R.id.contactcard);
        webcard=(CardView)findViewById(R.id.webcard);
        emailidd=(CardView)findViewById(R.id.emailidd);
        titlecard=(CardView)findViewById(R.id.titlecard);
        datecard=(CardView)findViewById(R.id.datecard);
        listcard=(CardView)findViewById(R.id.listcard);
        list_layout=(LinearLayout)findViewById(R.id.list_layout);
//        followbtn=(TextView)findViewById(R.id.textView33);


        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.listheader, commentlist,
                false);
        TextView viewall=(TextView)header.findViewById(R.id.textView34);
        commentlist.addHeaderView(header, null, false);


//        ViewGroup.LayoutParams params = commentlist.getLayoutParams();
//        params.height = height-200;
//        commentlist.setLayoutParams(params);
//        commentlist.requestLayout();

        Intent in=getIntent();
        admin=in.getStringExtra("admin");
        groupname=in.getStringExtra("group");
        about=in.getStringExtra("about");
        cate=in.getStringExtra("cate");
        subcate=in.getStringExtra("subcate");
        altno=in.getStringExtra("alt_mob");
        location=in.getStringExtra("address");
        email=in.getStringExtra("email");
        weburl=in.getStringExtra("weburl");
        Log.e("weburlll",weburl);
        city=in.getStringExtra("city");
        locstate=in.getStringExtra("state");
        date=in.getStringExtra("date");
        group_pic=in.getStringExtra("group_pic");
        Log.e("group_pic",group_pic+"///");
        admin_pic=in.getStringExtra("admin_pic");
        Log.e("admin_pic",admin_pic+"///");
        admin_pich=in.getStringExtra("admin_pich");
        Log.e("admin_pich",admin_pich+"///");
        group_pich=in.getStringExtra("group_pich");
        Log.e("group_pich",group_pich+"///");
        follow=in.getStringExtra("follow");
        group_id=in.getStringExtra("group_id");
        profile_name=in.getStringExtra("profile_name");
//        Log.e("profile_name", profile_name);
        title_id=in.getStringExtra("title");
        name_pich=in.getStringExtra("name_pich");
        Log.e("name_pich", name_pich);
        web_layout=(LinearLayout)findViewById(R.id.linearLayout6);
        canMakeSmores();


        bindActivity();

        mTitle.setText(admin);
//        adminname.setText(admin);
        group_name.setText(groupname);
        aboutid.setText(about);
        categoryid.setText(cate);
        subcateid.setText(subcate);
        contactid.setText(altno);
        alternateid.setText(altno);
        titleid.setText(title_id);
        activedateid.setText(date);
        emailid.setText(email);
        websiteid.setText(weburl);
//        activedate.setText("Active: "+date);

//        String[] separated = location.split(",");
//        separated[0]= separated[0].trim();
//        separated[1]= separated[1].trim();
//        separated[2]= separated[2].trim();
//        address=separated[0]+","+city+","+locstate+","+separated[2];
//        locationid.setText(address);
//        BitmapDrawable drawableBitmap=new BitmapDrawable(loadBitmap("http://maps.google.com/maps/api/staticmap?center=" + address + "&zoom=16&size=200x200&sensor=false"));
//        Log.e("bitmapsss",drawableBitmap+"");

//        location_layout.setBackgroundDrawable(drawableBitmap);
        //address=address.replace(" ", "%20");


        aquery=new AQuery(this,mapPreview);
        aquery.id(mapPreview).image("http://maps.google.com/maps/api/staticmap?center=" + address + "&zoom=16&size=300x70&sensor=false",true, true, 300,R.drawable.backheaderimage);
        location_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uri = "http://maps.google.com/maps/api/staticmap?center=" + address + "&zoom=16&size=600x100&sensor=false";
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                startActivity(intent);
            }
        });



        viewall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(comfollow.equals("comment")) {
                    Intent in = new Intent(getApplicationContext(), Group_comments.class);
                    in.putExtra("user_name", name);
                    in.putExtra("group_name", groupname);
                    startActivity(in);
                }
                else if(comfollow.equals("follower"))
                {
                    Intent in = new Intent(getApplicationContext(), Show_joiner.class);
                    in.putExtra("user_name", name);
                    in.putExtra("group_name", groupname);
                    startActivity(in);
                }
            }
        });


        emailidd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", email, null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                startActivity(Intent.createChooser(emailIntent, null));
            }
        });


        aboutlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abouttxt.setTextColor(Color.parseColor("#1E90FF"));
                aboutimg.setImageResource(R.drawable.aboutblue);
                aboutlayout.setBackgroundResource(R.drawable.change_back);

                comment.setText("Comment");
                comment.setTextColor(Color.parseColor("#FFFFFF"));
                commentimg.setImageResource(R.drawable.commentwhite);
                commentlayout.setBackgroundResource(R.drawable.date_back);

                follower.setText("Follower");
                follower.setTextColor(Color.parseColor("#FFFFFF"));
                followimg.setImageResource(R.drawable.followwhite);
                followlayout.setBackgroundResource(R.drawable.date_back);

                listcard.setVisibility(View.GONE);
                titlecard.setVisibility(View.VISIBLE);
                datecard.setVisibility(View.VISIBLE);
                aboutcard.setVisibility(View.VISIBLE);
                categorycard.setVisibility(View.VISIBLE);
                contactcard.setVisibility(View.VISIBLE);
                list_layout.setVisibility(View.GONE);
                webcard.setVisibility(View.VISIBLE);
                footer.setVisibility(View.VISIBLE);
            }
        });


        commentlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentvolley();

                comfollow="comment";

                abouttxt.setTextColor(Color.parseColor("#FFFFFF"));
                aboutimg.setImageResource(R.drawable.aboutwhite);
                aboutlayout.setBackgroundResource(R.drawable.date_back);

                comment.setTextColor(Color.parseColor("#1E90FF"));
                commentimg.setImageResource(R.drawable.commentblue);
                commentlayout.setBackgroundResource(R.drawable.change_back);

                follower.setText("Follower");
                follower.setTextColor(Color.parseColor("#FFFFFF"));
                followimg.setImageResource(R.drawable.followwhite);
                followlayout.setBackgroundResource(R.drawable.date_back);

                listcard.setVisibility(View.VISIBLE);
                list_layout.setVisibility(View.VISIBLE);
                titlecard.setVisibility(View.GONE);
                datecard.setVisibility(View.GONE);
                aboutcard.setVisibility(View.GONE);
                categorycard.setVisibility(View.GONE);
                contactcard.setVisibility(View.GONE);
                webcard.setVisibility(View.GONE);
                footer.setVisibility(View.GONE);

            }
        });

        followlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joingroupvolley();

                comfollow="follower";
                abouttxt.setTextColor(Color.parseColor("#FFFFFF"));
                aboutimg.setImageResource(R.drawable.aboutwhite);
                aboutlayout.setBackgroundResource(R.drawable.date_back);

                comment.setText("Comment");
                comment.setTextColor(Color.parseColor("#FFFFFF"));
                commentimg.setImageResource(R.drawable.commentwhite);
                commentlayout.setBackgroundResource(R.drawable.date_back);

                follower.setTextColor(Color.parseColor("#1E90FF"));
                followimg.setImageResource(R.drawable.followblue);
                followlayout.setBackgroundResource(R.drawable.change_back);

                listcard.setVisibility(View.VISIBLE);
                list_layout.setVisibility(View.VISIBLE);
                titlecard.setVisibility(View.GONE);
                datecard.setVisibility(View.GONE);
                aboutcard.setVisibility(View.GONE);
                categorycard.setVisibility(View.GONE);
                contactcard.setVisibility(View.GONE);
                webcard.setVisibility(View.GONE);
                footer.setVisibility(View.GONE);


            }
        });

        web_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(Group_view_main.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.websitedialog);

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                Window window = dialog.getWindow();
                lp.copyFrom(window.getAttributes());

                //This makes the dialog take up the full width
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                window.setAttributes(lp);

                close=(ImageView)dialog.findViewById(R.id.close);
                mWebView = (WebView)dialog.findViewById(R.id.webView);
                mWebView.setWebViewClient(new myWebClient());
                mWebView.getSettings().setJavaScriptEnabled(true);
                mWebView.loadUrl("http://"+weburl);



                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });


        mToolbar.setTitle("");
        mAppBarLayout.addOnOffsetChangedListener(this);

        setSupportActionBar(mToolbar);
        startAlphaAnimation(mTitle, 0, View.INVISIBLE);
        initParallaxValues();

        mstatus=(TextView)findViewById(R.id.textView4);
        mcontact=(TextView)findViewById(R.id.textView);
        musername=(TextView)findViewById(R.id.adminname1);
        mothercontact=(TextView)findViewById(R.id.contactnumber);
        profile= (CircleImageView) findViewById(R.id.profile);
        show_last_seen=(TextView)findViewById(R.id.show_last_seen);
        profile.setDrawingCacheEnabled(true);
        mImageparallax.setDrawingCacheEnabled(true);
        if(getIntent().hasExtra("last"))
            show_last_seen.setText(getIntent().getStringExtra("last"));

        contacts = hiHelloContactDBService.fetchContactByJID(
                getApplicationContext(), jid);

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.loading)
                .showImageForEmptyUri(R.drawable.backheaderimage)
                .showImageOnFail(R.drawable.backheaderimage)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(Group_view_main.this));
        imageLoader.getInstance().displayImage(HiHelloConstant.url+"img/"+group_pic, profile, options, animateFirstListener);
        imageLoader.getInstance().displayImage(HiHelloConstant.url+"img/"+ admin_pic, mImageparallax, options, animateFirstListener);


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(Group_view_main.this, Zoomimage.class);
                in.putExtra("pic", admin_pic);
                in.putExtra("pich",admin_pich);
                startActivity(in);
            }
        });
    }
    private boolean canMakeSmores(){

        return(Build.VERSION.SDK_INT> Build.VERSION_CODES.LOLLIPOP_MR1);

    }


    private void bindActivity() {
        mToolbar        = (Toolbar) findViewById(R.id.main_toolbar);
        mTitle          = (TextView) findViewById(R.id.main_textview_title);
        mTitleContainer = (LinearLayout) findViewById(R.id.main_linearlayout_title);
        mAppBarLayout   = (AppBarLayout) findViewById(R.id.main_appbar);
        mImageparallax  = (ImageView) findViewById(R.id.main_imageview_placeholder);
        mFrameParallax  = (FrameLayout) findViewById(R.id.main_framelayout_title);
    }

    private void initParallaxValues() {
        CollapsingToolbarLayout.LayoutParams petDetailsLp =
                (CollapsingToolbarLayout.LayoutParams) mImageparallax.getLayoutParams();

        CollapsingToolbarLayout.LayoutParams petBackgroundLp =
                (CollapsingToolbarLayout.LayoutParams) mFrameParallax.getLayoutParams();

        petDetailsLp.setParallaxMultiplier(0.9f);
        petBackgroundLp.setParallaxMultiplier(0.3f);

        mImageparallax.setLayoutParams(petDetailsLp);
        mFrameParallax.setLayoutParams(petBackgroundLp);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.groupprofile_menu, menu);
        Log.e("nameeeee",profile_name);
        if(name.equals(profile_name))
        {
            MenuItem item = (MenuItem) menu.findItem(R.id.join);
            item.setVisible(false);

        }
        return true;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;

        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);
    }

    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if(!mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if(mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }

        } else {

            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }

    public static void startAlphaAnimation (View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }

    @Override
    public void onBackPressed() {
        Intent in=new Intent(Group_view_main.this,HomeMain_activity.class);
        startActivity(in);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.share:
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                share.putExtra(Intent.EXTRA_SUBJECT, "Hi Hello");
                share.putExtra(Intent.EXTRA_TEXT,
                        "Hi I am Join " + groupname + " Group and I am Using Hi Hello App.Download Hi Hello App And Get Free Recharge Every Time" + " " + "play.google.com/store/apps/details?id=com.hihello.app");
                startActivity(Intent.createChooser(share, "Share link!"));
                break;
            case R.id.edit:

                final Dialog dialog = new Dialog(Group_view_main.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.websitedialog);

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                Window window = dialog.getWindow();
                lp.copyFrom(window.getAttributes());

                //This makes the dialog take up the full width
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                window.setAttributes(lp);

                close=(ImageView)dialog.findViewById(R.id.close);
                mWebView = (WebView)dialog.findViewById(R.id.webView);
                mWebView.setWebViewClient(new myWebClient());
                mWebView.getSettings().setJavaScriptEnabled(true);
                mWebView.loadUrl("https://www.google.co.in/webhp?sourceid=chrome-instant&ion=1&espv=2&ie=UTF-8#q="+groupname);



                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

                break;




//                Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
//                intent.setData(Uri.parse("https://www.google.co.in/webhp?sourceid=chrome-instant&ion=1&espv=2&ie=UTF-8#q="+groupname));
//                startActivity(intent);
//            break;
            case R.id.join:

                joinvolley();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

//    private class DataLongOperationAsynchTask extends AsyncTask<String, Void, String[]> {
//        ProgressDialog dialog = new ProgressDialog(Group_view.this);
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            dialog.setMessage("Please wait...");
//            dialog.setCanceledOnTouchOutside(false);
//            dialog.show();
//        }
//
//        public String getLatLongByURL(String requestURL) {
//            URL url;
//            String response = "";
//            try {
//                url = new URL(requestURL);
//
//                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                conn.setReadTimeout(15000);
//                conn.setConnectTimeout(15000);
//                conn.setRequestMethod("GET");
//                conn.setDoInput(true);
//                conn.setRequestProperty("Content-Type",
//                        "application/x-www-form-urlencoded");
//                conn.setDoOutput(true);
//                int responseCode = conn.getResponseCode();
//
//                if (responseCode == HttpsURLConnection.HTTP_OK) {
//                    String line;
//                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//                    while ((line = br.readLine()) != null) {
//                        response += line;
//                    }
//                } else {
//                    response = "";
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return response;
//        }
//
//        protected String[] doInBackground(String... params) {
//            String response;
//            try {
//
//                params[0]=params[0].replace(" ","%20");
//
//                Log.e("url","http://maps.google.com/maps/api/geocode/json?address="+params[0]+"&sensor=false");
//                response = getLatLongByURL("http://maps.google.com/maps/api/geocode/json?address="+params[0]+"&sensor=false");
//                Log.e("response",""+response);
//                return new String[]{response};
//            } catch (Exception e) {
//                return new String[]{"error"};
//            }
//        }
//
//        @Override
//        protected void onPostExecute(String... result) {
//            try {
//                JSONObject jsonObject = new JSONObject(result[0]);
//
//                lng = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
//                        .getJSONObject("geometry").getJSONObject("location")
//                        .getDouble("lng");
//
//                lat = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
//                        .getJSONObject("geometry").getJSONObject("location")
//                        .getDouble("lat");
//
//                Log.e("latitude", "" + lat);
//                Log.e("longitude", "" + lng);
//
//
//            } catch (JSONException e) {
//
//                Log.e("Exception","e",e);
//                e.printStackTrace();
//            }
//            if (dialog.isShowing()) {
//                dialog.dismiss();
//            }
//        }
//    }

    public static Bitmap loadBitmap(String url) {
        Bitmap bitmap = null;
        InputStream in = null;
        BufferedOutputStream out = null;

        try {
            in = new BufferedInputStream(new URL(url).openStream(), IO_BUFFER_SIZE);

            final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
            out = new BufferedOutputStream(dataStream, IO_BUFFER_SIZE);

            out.flush();

            final byte[] data = dataStream.toByteArray();
            BitmapFactory.Options options = new BitmapFactory.Options();
            //options.inSampleSize = 1;

            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length,options);
        } catch (IOException e) {
            Log.e("bitmapss", "Could not load Bitmap from: " + url);
        }


        return bitmap;
    }


    public void joinvolley() {
        final ProgressDialog pd = new ProgressDialog(Group_view_main.this);
        pd.setCancelable(true);
        pd.setMessage("Loading Please Wait");
        pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);

        pd.show();
        String tokan= FirebaseInstanceId.getInstance().getToken();

        RequestQueue queue = Volley.newRequestQueue(Group_view_main.this);
        String url;

        url = HiHelloConstant.url + "join.php?group_name="+groupname+"&profile_name="+name+"&group_id="+group_id+"&mobile="+contactmob+"&fcmid="+tokan+"&image="+pic_url;
        url = url.replace(" ", "%20");
        Log.e("name", url);
        JsonObjectRequest request = new JsonObjectRequest( url, null, new Response.Listener<JSONObject>() {
            //        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(JSONObject res) {
                //dataobj = new ArrayList<get_setr>();
//                dialog.dismiss();
                try {
                    JSONArray jr = res.getJSONArray("name");
                    pd.dismiss();
                    JSONObject jq=new JSONObject();
                    jq=jr.getJSONObject(0);

                    if(jq.getString("scalar").equals("Allready Joined"))
                    {
                        Toast.makeText(getApplicationContext(),"Allready Joined",Toast.LENGTH_SHORT).show();
                    }
                    else if(jq.getString("scalar").equals("Inserted"))
                    {
                        db.insertjoingroup(Integer.parseInt(group_id), admin,groupname,about,altno, cate,subcate,email,weburl,locstate,city,location, date,admin_pich,group_pich,title_id, "",follow, profile_name,admin_pich,group_pich);
                        int si=db.getAllJoinGroup().size();
                        Toast.makeText(Group_view_main.this,si+"",Toast.LENGTH_LONG).show();
                        Log.e("teg", "InstanceID token: " + FirebaseInstanceId.getInstance().getToken());
                        Intent in=new Intent(Group_view_main.this, HomeMain_activity.class);
                        startActivity(in);
                        finish();

                    }


//                FirebaseInstanceId.getInstance().getToken();

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    android.util.Log.e("catch exp= ", e.toString());
                    e.printStackTrace();
//                    dialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pd.dismiss();
            }
        });

        int socketTimeout = 50000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        queue.add(request);
    }


    public class myWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub

            view.loadUrl(url);
            return true;

        }
    }

    public void commentvolley()
    {
        commentlist=(ListView)findViewById(R.id.listView4);
        data1=new ArrayList<get_comment>();

        final ProgressDialog dialog = new ProgressDialog(getApplicationContext());
        dialog.setMessage("Loading..Please wait.");
//	     dialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        dialog.setCanceledOnTouchOutside(false);
//        dialog.show();

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        android.util.Log.e("url= ", "jjava.lang.String[]iii");
        groupname=groupname.replace(" ","%20");
        String url = HiHelloConstant.url+"fetch_groupcomment.php?group_name="+groupname;
        android.util.Log.e("url= ", url);

        JsonObjectRequest request = new JsonObjectRequest( url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject res) {
                //dataobj = new ArrayList<get_setr>();
//                dialog.dismiss();
                try {
                    JSONArray jr = res.getJSONArray("name");

//                    Log.e("response=", res.getJSONArray("data").toString());



                    int len = jr.length();

//                    Log.e("length=", String.valueOf(len));

                    for (int i = 0; i < jr.length(); i++) {
                        JSONObject jsonobj = jr.getJSONObject(i);           //String nom, String prenom, String email, String age, String deport, String actuel

//                        Log.e("aarray=", jr.toString());
                        data1.add(new get_comment(jsonobj.getInt("id"), jsonobj.getString("group_name"), jsonobj.getString("username"), jsonobj.getString("comment"),jsonobj.getString("pic_url"),jsonobj.getString("date")));
//                        Toast.makeText(getActivity(), jsonobj.getString("subcat") + "", Toast.LENGTH_LONG).show();
                    }


                    if(data1.size()>0) {
                        list_layout.setVisibility(View.VISIBLE);
                        commadapter = new Commentadapter(getApplicationContext(),data1);
                        commentlist.setAdapter(commadapter);
                        commadapter.notifyDataSetChanged();
                        dataa=data1.size();
                        comment.setText(dataa+"");
                    }
                    else
                    {
                        Toast.makeText(Group_view_main.this,"Comments are not here.Please add comments.",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    android.util.Log.e("catch exp= ", e.toString());
                    e.printStackTrace();
//                    dialog.dismiss();
                }

            }

        }
                , new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {

//                Log.e("error", arg0.toString());
//                dialog.dismiss();
            }
        });
        int socketTimeout = 50000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        queue.add(request);
    }


    public void joingroupvolley()
    {
        commentlist=(ListView)findViewById(R.id.listView4);
        data2=new ArrayList<get_join>();

        final ProgressDialog dialog = new ProgressDialog(getApplicationContext());
        dialog.setMessage("Loading..Please wait.");
//	     dialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        dialog.setCanceledOnTouchOutside(false);
//        dialog.show();

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        android.util.Log.e("url= ", "jjava.lang.String[]iii");
        groupname=groupname.replace(" ","%20");
        String url = HiHelloConstant.url+"fetch_groupjoined.php?group_name="+groupname;
        android.util.Log.e("url= ", url);

        JsonObjectRequest request = new JsonObjectRequest( url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject res) {
                //dataobj = new ArrayList<get_setr>();
//                dialog.dismiss();
                try {
                    JSONArray jr = res.getJSONArray("name");

//                    Log.e("response=", res.getJSONArray("data").toString());



                    int len = jr.length();

//                    Log.e("length=", String.valueOf(len));

                    for (int i = 0; i < jr.length(); i++) {
                        JSONObject jsonobj = jr.getJSONObject(i);           //String nom, String prenom, String email, String age, String deport, String actuel

//                        Log.e("aarray=", jr.toString());
                        data2.add(new get_join(jsonobj.getInt("id"), jsonobj.getString("group_name"), jsonobj.getString("profile_name"), jsonobj.getString("group_id"), jsonobj.getString("image"), jsonobj.getString("date")));
//                        Toast.makeText(getActivity(), jsonobj.getString("subcat") + "", Toast.LENGTH_LONG).show();
                    }

                    if(data2.size()>0) {
                        list_layout.setVisibility(View.VISIBLE);
                        joinadapter = new Joingroupadapter(data2);
                        commentlist.setAdapter(joinadapter);
                        joinadapter.notifyDataSetChanged();
                        datab=data2.size();
                        follower.setText(datab+"");
                    }
                    else
                    {
                        Toast.makeText(Group_view_main.this,"No one join this group.",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    android.util.Log.e("catch exp= ", e.toString());
                    e.printStackTrace();
//                    dialog.dismiss();
                }

            }

        }
                , new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {

//                Log.e("error", arg0.toString());
//                dialog.dismiss();
            }
        });
        int socketTimeout = 50000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        queue.add(request);
    }
    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }

    public void userpicvolley()
    {

        final ProgressDialog dialog = new ProgressDialog(getApplicationContext());
        dialog.setMessage("Loading..Please wait.");
//	     dialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        dialog.setCanceledOnTouchOutside(false);
//        dialog.show();

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        android.util.Log.e("url= ", "jjava.lang.String[]iii");
        contactmob="91"+contactmob;
        String url = HiHelloConstant.url+"profile_pic.php?mobile="+contactmob;
        android.util.Log.e("url= ", url);

        JsonObjectRequest request = new JsonObjectRequest( url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject res) {
                //dataobj = new ArrayList<get_setr>();
//                dialog.dismiss();
                try {
                    JSONArray jr = res.getJSONArray("name");

//                    Log.e("response=", res.getJSONArray("data").toString());



                    int len = jr.length();

//                    Log.e("length=", String.valueOf(len));

                    for (int i = 0; i < jr.length(); i++) {
                        JSONObject jsonobj = jr.getJSONObject(i);           //String nom, String prenom, String email, String age, String deport, String actuel

//                        Log.e("aarray=", jr.toString());
//                        data1.add(new get_comment(jsonobj.getInt("id"), jsonobj.getString("group_name"), jsonobj.getString("username"), jsonobj.getString("comment")));
//                        Toast.makeText(getActivity(), jsonobj.getString("subcat") + "", Toast.LENGTH_LONG).show();
                        pic_url=jsonobj.getString("pic_url");
                    }


                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    android.util.Log.e("catch exp= ", e.toString());
                    e.printStackTrace();
//                    dialog.dismiss();
                }

            }

        }
                , new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {

//                Log.e("error", arg0.toString());
//                dialog.dismiss();
            }
        });
        int socketTimeout = 50000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        queue.add(request);
    }

    public void exitvolley()
    {
        final ProgressDialog dialog = new ProgressDialog(Group_view_main.this);
        dialog.setMessage("Loading..Please wait.");
        dialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        RequestQueue queue= Volley.newRequestQueue(Group_view_main.this);
        groupname=groupname.replace(" ","%20");
        String url1= HiHelloConstant.url+"exit_group.php?group="+groupname+"&mobile="+mob_no;
        android.util.Log.e("url= ", url1);

        JsonObjectRequest request=new JsonObjectRequest(url1, null,new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject res)
            {
                dialog.dismiss();
                try {
                    if(res.getString("scalar").equals("success"))
                    {
                        db.deleteGroup(groupname);
                        db.close();
                        Intent in=new Intent(Group_view_main.this,HomeMain_activity.class);
                        startActivity(in);
                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    dialog.dismiss();
                    android.util.Log.e("catch exp= ", e.toString());
                    e.printStackTrace();
                }

            }

        }
                ,new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                dialog.dismiss();
                android.util.Log.e("error", arg0.toString());
            }
        });

        queue.add(request);
    }

}
