package com.hihello.group;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hihello.app.R;

public class SecondView extends Fragment
{
    ImageView im;
    boolean b;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.secondview,container,false);
        im=(ImageView)view.findViewById(R.id.imageView7);



        return view;
    }

}
