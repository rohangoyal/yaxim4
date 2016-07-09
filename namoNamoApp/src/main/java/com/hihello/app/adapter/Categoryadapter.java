package com.hihello.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hihello.app.R;
import com.hihello.app.getset.get_category;

import java.util.ArrayList;

/**
 * Created by rohan on 5/23/2016.
 */
public class Categoryadapter extends BaseAdapter {
    View v;
    Context context;
    ArrayList<get_category> cate;
    int[] data1={R.drawable.edu, R.drawable.ent, R.drawable.fim, R.drawable.hea, R.drawable.hot, R.drawable.nig, R.drawable.tra, R.drawable.trn,R.drawable.ser, R.drawable.sho, R.drawable.res, R.drawable.goe};
    public Categoryadapter(ArrayList<get_category> cate)
    {
        this.cate=cate;
    }

    @Override
    public int getCount() {
        return cate.size();
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
        v=inflater.inflate(R.layout.categoryadapter, parent, false);
        TextView name=(TextView)v.findViewById(R.id.textView31);
        ImageView img=(ImageView)v.findViewById(R.id.imageView27);
        img.setImageResource(data1[position]);
        name.setText(cate.get(position).getCategory());
        return v;
    }
}
