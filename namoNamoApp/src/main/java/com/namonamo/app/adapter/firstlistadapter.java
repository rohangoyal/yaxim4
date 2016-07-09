package com.namonamo.app.adapter;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.namonamo.app.R;
import com.namonamo.app.activity.CircularImageView;
import com.namonamo.app.getset.get_setter;

import java.util.ArrayList;


public class firstlistadapter extends BaseAdapter {
	Context c;
	AQuery aquery;
	ArrayList<get_setter> info;
	public CircularImageView admin;
	TextView username;
	String url;
	public firstlistadapter(Context c, ArrayList<get_setter> dataobj) {
		this.c=c;
		this.info=dataobj;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return info.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View v, ViewGroup parent) {
		// TODO Auto-generated method stub
		final Context css=parent.getContext();
		LayoutInflater li=(LayoutInflater)css.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		v=li.inflate(R.layout.firsrlistitem,parent,false);
		admin= (CircularImageView) v.findViewById(R.id.imageView2);
		username=(TextView)v.findViewById(R.id.textView1);
		username.setText(info.get(position).getAdminname());
		url="http://delainetechnologies.com/gyanProject/webservices/pimage/";
//		aquery=new AQuery((Activity)c,admin);
//		File imgFile = new File(info.get(position).getProfileimage());
//		if(imgFile.exists())
//		{
//
//			admin.setImageURI(Uri.fromFile(imgFile));
//
//		}
		aquery=new AQuery((Activity)c,admin);
		aquery.id(admin).image(url+info.get(position).getProfileimage(),true, true, 300,R.drawable.raj);
		return v;
	}

}
