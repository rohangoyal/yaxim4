package com.namonamo.app.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.namonamo.app.R;

public class nadapter extends BaseAdapter {
	
	int img[]={R.drawable.profile,R.drawable.status,R.drawable.contact1,R.drawable.wallet,R.drawable.offers,R.drawable.earnpoint,R.drawable.creategroup1,
			R.drawable.invitefrnd,R.drawable.blockfrnd,R.drawable.ranking,R.drawable.like,R.drawable.setting1,R.drawable.helpandfeed};
	String[] items={"Profile","Status","New Chat","My Wallet","Archive Chat","Earn Points","Group","Invite Friend","Blocked People","Ranking","Like Us","Settings","Help & Feedback"};

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.length;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int a, View b, ViewGroup c) {
		// TODO Auto-generated method stub
		
		Context cs=c.getContext();
		  LayoutInflater inflater=(LayoutInflater)cs.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		  b=inflater.inflate(R.layout.naviitem,c,false);
		  ImageView i=(ImageView)b.findViewById(R.id.naviitem1);
		  i.setBackgroundResource(img[a]);
		  TextView txt=(TextView)b.findViewById(R.id.naviitem2);
		  txt.setText(items[a]);
		  return b;
		
	}

}
