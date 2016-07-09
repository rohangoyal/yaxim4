package com.hihello.app.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hihello.app.R;
import com.hihello.app.getset.get_message;
import com.hihello.app.getset.get_messages;

import java.util.ArrayList;

/**
 * Created by rohan on 6/16/2016.
 */
public class Messageadapter extends BaseAdapter {
    View v;
    Context context;
    ArrayList<get_messages> data;
    String role;
    public Messageadapter(String role,ArrayList<get_messages> data)
    {
        this.role=role;
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

        Log.e("hello",data.get(position).getType());

        if(role.equals("joiner")) {

            if (data.get(position).getType().equals("Sender")) {
                v = inflater.inflate(R.layout.messagereciveradapter, parent, false);
            } else if (data.get(position).getType().equals("Receiver")) {
                v = inflater.inflate(R.layout.messageadapter, parent, false);
            }
        }
        else
        {
            if (data.get(position).getType().equals("Receiver")) {
                v = inflater.inflate(R.layout.messagereciveradapter, parent, false);
            } else if (data.get(position).getType().equals("Sender")) {
                v = inflater.inflate(R.layout.messageadapter, parent, false);
            }
        }
        TextView chat_text=(TextView)v.findViewById(R.id.chat_message);
        chat_text.setText(data.get(position).getMsg());
        return v;
    }
}
