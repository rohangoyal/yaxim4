package com.hihello.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hihello.app.R;

import java.util.ArrayList;

/**
 * Created by rohan on 6/17/2016.
 */
public class Messagereciveradapter extends BaseAdapter {
    View v;
    Context context;
    ArrayList<String> data;
    public Messagereciveradapter(ArrayList<String> data)
    {
        this.data=data;
    }

    @Override
    public int getCount() {
        return data.size();
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
        v=inflater.inflate(R.layout.messagereciveradapter, parent, false);
        TextView chat_text=(TextView)v.findViewById(R.id.chat_message);
        chat_text.setText(data.get(position));
        return v;
    }
}
