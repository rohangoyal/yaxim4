package com.namonamo.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.namonamo.app.R;
import com.namonamo.app.common.Log;
import com.namonamo.app.constant.Hihelloconstant;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class groupprofile extends AppCompatActivity
    implements AppBarLayout.OnOffsetChangedListener {

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR  = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS     = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION              = 200;

    private boolean mIsTheTitleVisible          = false;
    private boolean mIsTheTitleContainerVisible = true;
    String contact,wallimage,profileimage,username;
    private LinearLayout mTitleContainer;
    private TextView mTitle,mcontact,mothercontact,musername,mstatus;
    private AppBarLayout mAppBarLayout;
    private ImageView mImageparallax;
    private FrameLayout mFrameParallax;
    private Toolbar mToolbar;
    private CircleImageView profile;
    AQuery aquery;
    String url,url1;
    String picurl,status,mobnumber,profilename;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.groupprofile);

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

            mcontact.setText(Hihelloconstant.mobnumber);
            mothercontact.setText(Hihelloconstant.mobnumber);
            mTitle.setText(Hihelloconstant.username);
            musername.setText(Hihelloconstant.username);
            mstatus.setText(Hihelloconstant.status);

//            url="http://delainetechnologies.com/gyanProject/webservices/pimage/";
//        url1="http://delainetechnologies.com/gyanProject/webservices/images/";
        aquery=new AQuery(this,profile);
        aquery.id(profile).image(Hihelloconstant.picurl,true, true, 300,R.drawable.defaultuser);

        aquery=new AQuery(this,mImageparallax);
        aquery.id(mImageparallax).image(Hihelloconstant.picurl,true, true, 300,R.drawable.defaultuser);
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
                break;
            case R.id.edit:
                Intent in=new Intent(groupprofile.this,EditContact.class);
                startActivity(in);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
