package com.hihello.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.hihello.app.R;
import com.hihello.app.getset.get_join;

import java.util.ArrayList;

/**
 * Created by rohan on 5/30/2016.
 */
public class Joingroupadapter extends BaseAdapter
{
    AQuery aquery;
    Context context;
    ArrayList<get_join> data2;
    View v;
    public Joingroupadapter(ArrayList<get_join> data2) {
        this.data2=data2;
    }

    @Override
    public int getCount() {
        return data2.size();
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
        v=inflater.inflate(R.layout.joingroupadapter, parent, false);
        TextView join=(TextView)v.findViewById(R.id.textView36);
        ImageView user_pic=(ImageView)v.findViewById(R.id.profileImage);
        TextView date=(TextView)v.findViewById(R.id.textView33);
        join.setText(data2.get(position).getProfile_name());
        date.setText(data2.get(position).getDate());
        aquery=new AQuery((Activity) context,user_pic);
        aquery.id(user_pic).image(data2.get(position).getPic_url(), true, true, 300, R.drawable.backheaderimage);
        return v;
    }
}
