package com.hihello.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hihello.app.R;
import com.hihello.app.getset.get_city;
import com.hihello.app.getset.get_state;

import java.util.ArrayList;

/**
 * Created by rohan on 5/24/2016.
 */
public class Cityadapter extends BaseAdapter {
    View v;
    Context context;
    ArrayList<get_city> citydata;
    public Cityadapter(ArrayList<get_city> citydata)
    {
        this.citydata=citydata;
    }

    @Override
    public int getCount() {
        return citydata.size();
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
        v=inflater.inflate(R.layout.stateadapter, parent, false);
        TextView name=(TextView)v.findViewById(R.id.textView31);
        name.setText(citydata.get(position).getCity());
        return v;
    }
}
