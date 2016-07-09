package com.hihello.app.activity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
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
import com.hihello.app.R;

import com.hihello.app.adapter.Commentadapter;
import com.hihello.app.adapter.firstlistadapter;
import com.hihello.app.constant.HiHelloConstant;
import com.hihello.app.constant.HiHelloNewConstant;
import com.hihello.app.constant.NetworkConnection;
import com.hihello.app.db.HiHelloContact;
import com.hihello.app.db.hiHelloContactDBService;
import com.hihello.app.getset.get_comment;
import com.hihello.app.getset.get_set_groups;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class groupprofile extends AppCompatActivity
    implements AppBarLayout.OnOffsetChangedListener {

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR  = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS     = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION              = 200;
    ArrayList<get_set_groups> data;
    firstlistadapter adp;
    private boolean mIsTheTitleVisible          = false;
    private boolean mIsTheTitleContainerVisible = true;
    String contact,wallimage,profileimage,username;
    private LinearLayout mTitleContainer;
    private TextView mTitle,mcontact,mothercontact,musername,mstatus,show_last_seen;
    private AppBarLayout mAppBarLayout;
    private ImageView mImageparallax;
    private FrameLayout mFrameParallax;
    private Toolbar mToolbar;
    private CircleImageView profile;
    AQuery aquery;
    String url,url1,contactName,contactId;
    CardView common_group;
    private HiHelloContact contacts;
    String picurl,status,mobnumber,profilename;
    private String jid,name;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    private DisplayImageOptions options;
    ListView lv;
    TextView viewtxt;
    SharedPreferences pref_name;
    NetworkConnection nw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        Log.e("height", height + "hty");
        nw=new NetworkConnection(getApplicationContext());
        if(height<900) {
            setContentView(R.layout.groupprofile);
        }
        else if(height>=900 & height<1400)
        {
            setContentView(R.layout.group_profile);
        }
        else
        {
            setContentView(R.layout.group_profilelarge);
        }
        canMakeSmores();
//            Intent in3=getIntent();
//            picurl=in3.getStringExtra("pic");
//            status=in3.getStringExtra("status");
//            mobnumber=in3.getStringExtra("mobileno");
//            profilename=in3.getStringExtra("name");
//            Log.e("piccc",picurl);
//            Log.e("status",status);
//            Log.e("mobnumber",mobnumber);
//            Log.e("profilename",profilename);

            bindActivity();

        pref_name=getSharedPreferences("pref_name", 1);
        name=pref_name.getString("username", "");
        com.hihello.app.common.Log.e("usernameee", name);


        if(nw.isConnectingToInternet()==true) {
            commonvolley();
        }
        else {
            Toast.makeText(getApplicationContext(), "Network Problem", Toast.LENGTH_LONG).show();
        }



            mToolbar.setTitle("");
            mAppBarLayout.addOnOffsetChangedListener(this);

            setSupportActionBar(mToolbar);
            startAlphaAnimation(mTitle, 0, View.INVISIBLE);
            initParallaxValues();
//            Intent intent = getIntent();
//           contact  = intent.getStringExtra("phone");
//            profileimage = intent.getStringExtra("profileimage");
//           wallimage = intent.getStringExtra("wallimage");
//           username = intent.getStringExtra("adminname");
            mstatus=(TextView)findViewById(R.id.textView4);
            mcontact=(TextView)findViewById(R.id.textView);
            musername=(TextView)findViewById(R.id.adminname1);
            mothercontact=(TextView)findViewById(R.id.contactnumber);
            profile= (CircleImageView) findViewById(R.id.profile);
            show_last_seen=(TextView)findViewById(R.id.show_last_seen);
            common_group=(CardView)findViewById(R.id.commongroupcard);
            viewtxt=(TextView)findViewById(R.id.viewtxt);
        lv=(ListView)findViewById(R.id.list);

        if(getIntent().hasExtra("last"))
            show_last_seen.setText(getIntent().getStringExtra("last"));
            mcontact.setText(HiHelloNewConstant.mobnumber);
            mothercontact.setText(HiHelloNewConstant.mobnumber);
            mTitle.setText(HiHelloNewConstant.username);
            musername.setText(HiHelloNewConstant.username);
            mstatus.setText(HiHelloNewConstant.status);
        if(getIntent().getExtras().getString("jid").toString()!=null) {

            jid = getIntent().getExtras().getString("jid");
            Log.e("jid", jid);
        }
        contacts = hiHelloContactDBService.fetchContactByJID(
                getApplicationContext(), jid);
        if (contacts != null) {
            mstatus.setText(contacts.getStatus());

        } else {

        }


        viewtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(groupprofile.this,Group_common.class);
                in.putExtra("classname","groupprofile");
                startActivity(in);
            }
        });


        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.loading)
                .showImageForEmptyUri(R.drawable.backheaderimage)
                .showImageOnFail(R.drawable.backheaderimage)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(groupprofile.this));
        imageLoader.getInstance().displayImage(HiHelloNewConstant.picurl, profile, options, animateFirstListener);
        imageLoader.getInstance().displayImage(HiHelloNewConstant.picurl, mImageparallax, options, animateFirstListener);



//            url="http://delainetechnologies.com/gyanProject/webservices/pimage/";
//        url1="http://delainetechnologies.com/gyanProject/webservices/images/";
//        aquery=new AQuery(this,profile);
//        aquery.id(profile).image(HiHelloNewConstant.picurl,true, true, 300,R.drawable.backheaderimage);
//
//        aquery=new AQuery(this,mImageparallax);
//        aquery.id(mImageparallax).image(HiHelloNewConstant.picurl,true, true, 300,R.drawable.backheaderimage);
//            File imgFile = new File(wallimage);
//            if(imgFile.exists())
//            {
//
//                mImageparallax.setImageURI(Uri.fromFile(imgFile));
//
//            }
//            File imgFile1 = new File(profileimage);
//            if(imgFile1.exists())
//            {
//
//                profile.setImageURI(Uri.fromFile(imgFile1));
//
//            }
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
        getMenuInflater().inflate(R.menu.mainprofile_menu, menu);
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
        Intent i=new Intent(groupprofile.this,HomeMain_activity.class);
        startActivity(i);
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
                        ""+HiHelloNewConstant.mobnumber);
                startActivity(Intent.createChooser(share, "Share link!"));
                break;
            case R.id.edit:

                getphonenoid();



//                Intent in=new Intent(groupprofile.this,EditContact.class);
//                startActivity(in);
                break;
        }
        return super.onOptionsItemSelected(item);
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

    public void getphonenoid()
    {
        ContentResolver contentResolver = getContentResolver();

        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(HiHelloNewConstant.mobnumber));

        String[] projection = new String[] {ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup._ID};

        Cursor cursor =
                contentResolver.query(
                        uri,
                        projection,
                        null,
                        null,
                        null);

        if(cursor!=null) {
            while(cursor.moveToNext()){
                contactName = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup.DISPLAY_NAME));
                contactId = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup._ID));
                Log.e("", "contactMatch name: " + contactName);
                Log.e("", "contactMatch id: " + contactId);
                Intent intent = new Intent(Intent.ACTION_EDIT);
                intent.setData(ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Integer.parseInt(contactId)));
                startActivity(intent);
            }
            cursor.close();
        }
    }



    public void commonvolley()
    {
        final ProgressDialog dialog = new ProgressDialog(groupprofile.this);
        dialog.setMessage("Loading..Please wait.");
        dialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        dialog.setCanceledOnTouchOutside(false);
//        dialog.show();


        RequestQueue queue= Volley.newRequestQueue(groupprofile.this);

        String url1= HiHelloConstant.url+"fetch_joined.php?profile_name="+name;
        com.hihello.app.common.Log.e("url= ", url1);

        JsonObjectRequest request=new JsonObjectRequest(url1, null,new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject res)
            {
                data=new ArrayList<get_set_groups>();

                try {
                    JSONArray jr=res.getJSONArray("name");
//                    dialog.dismiss();
                    Log.e("response=",res.getJSONArray("name").toString());

                    int len=jr.length();

                    com.hihello.app.common.Log.e("length=",String.valueOf(len));

                    for(int i=0;i<jr.length();i++)
                    {
                        JSONObject jsonobj=jr.getJSONObject(i);

                        com.hihello.app.common.Log.e("aarray=",jr.toString());

                        data.add(new get_set_groups(jsonobj.getInt("id"), jsonobj.getString("admin_name"), jsonobj.getString("group_name"), jsonobj.getString("about"), jsonobj.getString("alt_mobile"), jsonobj.getString("category"), jsonobj.getString("subcategory"), jsonobj.getString("email"), jsonobj.getString("weburl"), jsonobj.getString("state"), jsonobj.getString("city"), jsonobj.getString("address"),jsonobj.getString("date"), jsonobj.getString("admin_thumb"), jsonobj.getString("group_thumb"), jsonobj.getString("title"), jsonobj.getString("comment"), jsonobj.getString("follow"), jsonobj.getString("profile_name"), jsonobj.getString("admin_pic"), jsonobj.getString("group_pic")));
                    }
                    if(data.size()>0)
                    {
                        common_group.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        common_group.setVisibility(View.INVISIBLE);
                    }
                    adp=new firstlistadapter(getApplicationContext(),data,name);
                    lv.setAdapter(adp);


                } catch (JSONException e) {
                    // TODO Auto-generated catch block
//                    dialog.dismiss();
                    com.hihello.app.common.Log.e("catch exp= ", e.toString());
                    e.printStackTrace();
                }

            }

        }
                ,new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
//                dialog.dismiss();
                com.hihello.app.common.Log.e("error", arg0.toString());
            }
        });

        int socketTimeout = 50000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        queue.add(request);
    }


}
