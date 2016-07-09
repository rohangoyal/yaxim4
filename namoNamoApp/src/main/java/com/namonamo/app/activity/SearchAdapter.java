package com.namonamo.app.activity;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;

import com.androidquery.AQuery;
import com.namonamo.app.R;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import  com.namonamo.app.activity.CircularImageView;
import com.namonamo.app.getset.get_setter;

import java.io.File;
import java.util.ArrayList;


/**
 * Created by user on 10/21/2015.
 */
public class SearchAdapter  extends BaseAdapter
{
    Context c;
    AQuery aquery;
    ArrayList<get_setter> info;
    public CircularImageView admin;
    TextView username,website,follower,view;
    ImageView profile;
    String url,url1;

    public SearchAdapter(Context c,ArrayList<get_setter> dataobj) {
        this.c = c;
        this.info=dataobj;
    }

    @Override
    public int getCount() {
        return info.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View v, ViewGroup parent) {
        final Context css=parent.getContext();
        LayoutInflater li=(LayoutInflater)css.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v=li.inflate(R.layout.searchadapter,parent,false);
        admin= (CircularImageView) v.findViewById(R.id.imageView1);
        username=(TextView)v.findViewById(R.id.textView6);
        website=(TextView)v.findViewById(R.id.textView10);
        follower=(TextView)v.findViewById(R.id.textView11);
        view=(TextView)v.findViewById(R.id.textView13);
        profile=(ImageView)v.findViewById(R.id.imageView11);
        username.setText(info.get(i).getAdminname());

        url="http://delainetechnologies.com/gyanProject/webservices/pimage/";
        url1="http://delainetechnologies.com/gyanProject/webservices/images/";
//		aquery=new AQuery((Activity)c,admin);
//        File imgFile = new File(info.get(i).getProfileimage());
//        if(imgFile.exists())
//        {
//
//            admin.setImageURI(Uri.fromFile(imgFile));
//
//        }
//        File imgFile1 = new File(info.get(i).getWallimage());
//        if(imgFile1.exists())
//        {
//
//            profile.setImageURI(Uri.fromFile(imgFile1));
//
//        }
        aquery=new AQuery((Activity)c,admin);
        aquery.id(admin).image(url+info.get(i).getProfileimage(),true, true, 300,R.drawable.raj);
        aquery=new AQuery((Activity)c,profile);
        aquery.id(profile).image(url1+info.get(i).getWallimage(),true, true, 300,R.drawable.raj);
        website.setText(info.get(i).getWebsite());
        follower.setText(info.get(i).getFollowers());
        view.setText(info.get(i).getViewers());
        return v;
    }
}
