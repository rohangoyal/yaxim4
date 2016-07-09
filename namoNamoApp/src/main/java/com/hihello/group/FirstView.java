package com.hihello.group;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.androidquery.AQuery;
import com.hihello.app.R;


public class FirstView extends Fragment
{
    public static ImageView im;
    LinearLayout lay;
    boolean b;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.firstview,container,false);
        im=(ImageView)view.findViewById(R.id.imageView7);

        AQuery pic=new AQuery(getActivity());
        pic.id(im).image(getArguments().getString("url"));
        lay=(LinearLayout)view.findViewById(R.id.layoutid);

        return view;
    }




    }
