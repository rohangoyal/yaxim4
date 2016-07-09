package com.namonamo.group;


import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;


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
        lay=(LinearLayout)view.findViewById(R.id.layoutid);

        return view;
    }


    public static Fragment newInstance(int position) {


            FirstView f1 = new FirstView();
            Bundle b = new Bundle();
            b.putInt("position", position);
            f1.setArguments(b);
            return f1;
        }

    }
