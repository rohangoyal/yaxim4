package com.namonamo.app.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.namonamo.app.R;
import com.namonamo.app.activity.UIApplication;
import com.namonamo.app.common.ContactUtil;
import com.namonamo.app.db.BlockUser;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class countrylistadapter extends BaseAdapter {

	
	Context c;
	ArrayList<String> clist;
	ArrayList<String> ccode;
	public countrylistadapter(Context c,ArrayList<String> clist,ArrayList<String> ccode) {
		// TODO Auto-generated constructor stub
	this.c=c;
	this.clist=clist;
	this.ccode=ccode;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return clist.size();
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
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final Context css=parent.getContext();
		LayoutInflater li=(LayoutInflater)css.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v=li.inflate(R.layout.countrylistitem,parent,false);
		TextView countryname=(TextView)v.findViewById(R.id.textView1);
		TextView countrycode=(TextView)v.findViewById(R.id.textView2);
		
		countryname.setText(clist.get(position));
		countrycode.setText(ccode.get(position));
		
		
		return v;
	
	}
	
	

	
	
}
