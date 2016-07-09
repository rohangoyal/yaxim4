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
import com.hihello.app.activity.CirclePageIndicator;
import com.hihello.app.getset.get_comment;
import com.hihello.app.getset.getsetjoin;

import java.util.ArrayList;

/**
 * Created by delaine on 7/3/2016.
 */
public class Joineradapter extends BaseAdapter {
    ArrayList<getsetjoin> data1;
    View v;
    AQuery aquery;
    Context context;

    public Joineradapter(ArrayList<getsetjoin> data1)
    {
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
        View v=inflater.inflate(R.layout.joineradapter,parent,false);
        TextView name=(TextView)v.findViewById(R.id.textView36);
        TextView msg=(TextView)v.findViewById(R.id.textView35);
        CirclePageIndicator image=(CirclePageIndicator)v.findViewById(R.id.profileImage);

        msg.setText(data1.get(position).getMessage());
        name.setText(data1.get(position).getJoiner_name());
        aquery=new AQuery((Activity) context,image);
        aquery.id(image).image(data1.get(position).getJoiner_pic(), true, true, 300, R.drawable.backheaderimage);

        return v;
    }
}
