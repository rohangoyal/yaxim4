package com.hihello.app.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.hihello.app.R;
import com.hihello.app.adapter.Commentadapter;
import com.hihello.app.adapter.Joineradapter;
import com.hihello.app.common.Log;
import com.hihello.app.constant.NetworkConnection;
import com.hihello.app.db.DatabaseHandler;
import com.hihello.app.getset.get_comment;
import com.hihello.app.getset.getsetjoin;

import java.util.ArrayList;

import github.ankushsachdeva.emojicon.EmojiconsPopup;

/**
 * Created by delaine on 7/3/2016.
 */
public class joinerquery extends AppCompatActivity
{
    ArrayList<getsetjoin> data1;
    NetworkConnection nw;
    ListView lv;
    String user_name,group_name,comm;
    public static String pic_url;
    Joineradapter joineradapter;
    DatabaseHandler db;
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.joiner_query);
        lv=(ListView)findViewById(R.id.list);
        nw=new NetworkConnection(getApplicationContext());
        db = new DatabaseHandler(getApplicationContext());
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.leftback);
        getSupportActionBar().setTitle("Joiner Query");
        Intent in=getIntent();
        user_name=in.getStringExtra("user_name");
        group_name=in.getStringExtra("group_name");
        data1=new ArrayList<getsetjoin>();
        data1=db.getJoinerMessage(group_name);
        Log.e("data1 ka sizeee",data1.size()+" sizeee");
        Toast.makeText(joinerquery.this,data1.size()+"",Toast.LENGTH_LONG).show();
        joineradapter=new Joineradapter(data1);
        lv.setAdapter(joineradapter);

    }
}
