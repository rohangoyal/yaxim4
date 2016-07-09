package com.namonamo.group;

import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.namonamo.app.R;

import java.util.Timer;
import java.util.TimerTask;

public class Group extends AppCompatActivity {

    ViewPager viewPager;
    PagerAdapter adapter;
    CirclePageIndicator indicator;
    int i=0;
    TimerTask timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group);

        viewPager = (ViewPager) findViewById(R.id.viewPager );
        adapter=new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        indicator = (CirclePageIndicator)findViewById(R.id.indicator);


        indicator.setViewPager(viewPager);

        final Handler handler= new Handler() ;

        timer=new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        viewPager.setCurrentItem(i % 10);

                        i++;
                    }
                });
            }
        };
        Timer time=new Timer();
        time.schedule(timer, 0, 5000);

    }
}
