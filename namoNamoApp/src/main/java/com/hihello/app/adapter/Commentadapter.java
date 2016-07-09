package com.hihello.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.hihello.app.R;

import com.hihello.app.activity.CircularImageView;
import com.hihello.app.getset.get_comment;

import java.util.ArrayList;

/**
 * Created by rohan on 5/30/2016.
 */
public class Commentadapter extends BaseAdapter {
    ArrayList<get_comment> data1;
    View v;
    AQuery aquery;
    Context context;
    public Commentadapter(Context context,ArrayList<get_comment> data1) {
        this.context=context;
        this.data1=data1;
    }

    @Override
    public int getCount() {
        return data1.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        context=parent.getContext();
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        v=inflater.inflate(R.layout.commentshow_layout, parent, false);
        TextView comment=(TextView)v.findViewById(R.id.textView35);
        TextView username=(TextView)v.findViewById(R.id.textView36);
        TextView date=(TextView)v.findViewById(R.id.textView33);
        CircularImageView user_pic=(CircularImageView)v.findViewById(R.id.profileImage);
        comment.setText(data1.get(position).getComment());
        username.setText(data1.get(position).getUsername());
        date.setText(data1.get(position).getDate());
        aquery=new AQuery((Activity) context,user_pic);
        aquery.id(user_pic).image(data1.get(position).getPic_url(), true, true, 300, R.drawable.backheaderimage);
        return v;
    }
}
