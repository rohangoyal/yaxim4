package com.hihello.app.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.hihello.app.R;

import java.util.ArrayList;

public class NewAdapter extends BaseExpandableListAdapter
{
	public ArrayList<String> groupItem, tempChild;
	public ArrayList<Object> Childtem = new ArrayList<Object>();
	public LayoutInflater minflater;
	public Activity activity;
	String slug;


	public NewAdapter(ArrayList<String> grList, ArrayList<Object> childItem) {
		groupItem = grList;
		this.Childtem = childItem;
	}

	public void setInflater(LayoutInflater mInflater, Activity act) {
		this.minflater = mInflater;
		activity = act;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return null;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
							 boolean isLastChild, View convertView, ViewGroup parent) {

		tempChild = (ArrayList<String>) Childtem.get(groupPosition);
		TextView text = null;
		LinearLayout l_layout;
		if (convertView == null) {
			convertView = minflater.inflate(R.layout.childrow, null);
		}
		text = (TextView) convertView.findViewById(R.id.textView1);
		l_layout=(LinearLayout)convertView.findViewById(R.id.linearLayout);
		text.setText(tempChild.get(childPosition));
		text.setTextColor(Color.parseColor("#ffffff"));
		int i=groupPosition%4;
		switch (i)
		{
			case 0:
				l_layout.setBackgroundColor(Color.parseColor("#86C745"));
				break;
			case 1:
				l_layout.setBackgroundColor(Color.parseColor("#F2612A"));
				break;
			case 2:
				l_layout.setBackgroundColor(Color.parseColor("#A436A5"));
				break;
			case 3:
				l_layout.setBackgroundColor(Color.parseColor("#008DDE"));
				break;

		}
//		convertView.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
////				Toast.makeText(activity, tempChild.get(childPosition),
////						Toast.LENGTH_SHORT).show();
//				for(int i=0;i< Newscatfragment.data.size();i++)
//				{
//					if(tempChild.get(childPosition).equals(Newscatfragment.data.get(i).getTitle()))
//					{
//						slug=Newscatfragment.data.get(i).getSlug();
//						Log.e("sluggggg",slug);
//					}
//				}
////				Bundle bundle=new Bundle();
////				bundle.putString("slug", slug);
////				Fragment newslist = new Newslistfragment();
////				newslist.setArguments(bundle);
////				Newscatfragment.fm1.beginTransaction().replace(R.id.frame_layout,newslist).commit();
//			}
//		});
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {

		return ((ArrayList<String>) Childtem.get(groupPosition)).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return null;
	}

	@Override
	public int getGroupCount() {
		return groupItem.size();
	}

	@Override
	public void onGroupCollapsed(int groupPosition) {
		super.onGroupCollapsed(groupPosition);
	}

	@Override
	public void onGroupExpanded(int groupPosition) {
		super.onGroupExpanded(groupPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
							 View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = minflater.inflate(R.layout.grouprow, null);
		}
		((CheckedTextView) convertView).setText(groupItem.get(groupPosition));
		((CheckedTextView) convertView).setChecked(isExpanded);
		((CheckedTextView) convertView).setTextColor(Color.parseColor("#ffffff"));
		int i=groupPosition%4;
		switch (i)
		{
			case 0:
				((CheckedTextView) convertView).setBackgroundColor(Color.parseColor("#86C745"));
				break;
			case 1:
				((CheckedTextView) convertView).setBackgroundColor(Color.parseColor("#F2612A"));
				break;
			case 2:
				((CheckedTextView) convertView).setBackgroundColor(Color.parseColor("#A436A5"));
				break;
			case 3:
				((CheckedTextView) convertView).setBackgroundColor(Color.parseColor("#008DDE"));
				break;

		}
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}


}


