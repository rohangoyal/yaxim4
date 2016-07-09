package com.hihello.app.activity;

import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.hihello.app.R;
import com.hihello.app.apicall.GetBannersApiCall;

import com.hihello.app.service.Servicable;
import com.hihello.group.PagerAdapter;
import com.viewpagerindicator.CirclePageIndicator;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class PhoneDirectory extends AppCompatActivity {

    ViewPager viewPager;
    PagerAdapter adapter;
    CirclePageIndicator indicator;
    int i=0;
    TimerTask timer;
    MaterialSearchView searchView ;
    private Toolbar tool;


    private Servicable.ServiceListener listener = new Servicable.ServiceListener() {

        @Override
        public void onComplete(Servicable<?> service) {


            String response= ((GetBannersApiCall) service).getStringResponce();




            try {
                JSONArray json=new JSONArray(response);

                String data[]=new String[json.length()];
                for(int i=0;i<json.length();i++)
                {

                    JSONObject obj= (JSONObject) json.get(i);

                    data[i]=obj.getString("image_url");


                }

                Log.e("size",data.length+"");

                viewPager = (ViewPager) findViewById(R.id.viewPager );
                adapter=new PagerAdapter(getSupportFragmentManager(),data);
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
            } catch (Exception e) {

                Log.e("ex","ex",e);
                e.printStackTrace();
            }




        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.directory);

        tool=(Toolbar)findViewById(R.id.toolbar);



        setSupportActionBar(tool);

        searchView  = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setVoiceSearch(true);
        tool.setTitle("Group");

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        GetBannersApiCall api = new GetBannersApiCall(
                "home");
        api.runAsync(listener);



        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });

    }







    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.group_menu,menu);

        MenuItem item = menu.findItem(R.id.search);
        searchView.setMenuItem(item);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
