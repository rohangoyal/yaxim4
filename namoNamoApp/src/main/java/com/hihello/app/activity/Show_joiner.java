package com.hihello.app.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.hihello.app.R;
import com.hihello.app.adapter.Joingroupadapter;
import com.hihello.app.constant.HiHelloConstant;
import com.hihello.app.getset.get_join;
import com.hihello.group.Group;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by rohan on 6/21/2016.
 */
public class Show_joiner extends AppCompatActivity
{
    ListView lv;
    ArrayList<get_join> data2;
    String groupname,username;
    Joingroupadapter joinadapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_joiner);

        lv=(ListView)findViewById(R.id.listView5);
        Intent in=getIntent();
        groupname=in.getStringExtra("group_name");
        username=in.getStringExtra("user_name");
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.leftback);
        getSupportActionBar().setTitle("Group Joiners");
        joingroupvolley();
    }

    public void joingroupvolley()
    {
        lv=(ListView)findViewById(R.id.listView5);
        data2=new ArrayList<get_join>();

        final ProgressDialog dialog = new ProgressDialog(getApplicationContext());
        dialog.setMessage("Loading..Please wait.");
//	     dialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        dialog.setCanceledOnTouchOutside(false);
//        dialog.show();

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        android.util.Log.e("url= ", "jjava.lang.String[]iii");
        groupname=groupname.replace(" ","%20");
        String url = HiHelloConstant.url+"fetch_groupjoined.php?group_name="+groupname;
        android.util.Log.e("url= ", url);

        JsonObjectRequest request = new JsonObjectRequest( url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject res) {
                //dataobj = new ArrayList<get_setr>();
//                dialog.dismiss();
                try {
                    JSONArray jr = res.getJSONArray("name");

//                    Log.e("response=", res.getJSONArray("data").toString());



                    int len = jr.length();

//                    Log.e("length=", String.valueOf(len));

                    for (int i = 0; i < jr.length(); i++) {
                        JSONObject jsonobj = jr.getJSONObject(i);           //String nom, String prenom, String email, String age, String deport, String actuel

//                        Log.e("aarray=", jr.toString());
                        data2.add(new get_join(jsonobj.getInt("id"), jsonobj.getString("group_name"), jsonobj.getString("profile_name"), jsonobj.getString("group_id"), jsonobj.getString("image"), jsonobj.getString("date")));
//                        Toast.makeText(getActivity(), jsonobj.getString("subcat") + "", Toast.LENGTH_LONG).show();
                    }

                    if(data2.size()>0) {
                        joinadapter = new Joingroupadapter(data2);
                        lv.setAdapter(joinadapter);
                        joinadapter.notifyDataSetChanged();

                    }
                    else
                    {
                        Toast.makeText(Show_joiner.this,"No one join this group.",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    android.util.Log.e("catch exp= ", e.toString());
                    e.printStackTrace();
//                    dialog.dismiss();
                }

            }

        }
                , new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {

//                Log.e("error", arg0.toString());
//                dialog.dismiss();
            }
        });
        int socketTimeout = 50000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        queue.add(request);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                Intent in1=new Intent(Show_joiner.this,Group.class);
                startActivity(in1);
                finish();
                break;
            case R.id.home:
                Intent in=new Intent(Show_joiner.this,Group.class);
                startActivity(in);
                finish();
                break;

        }

        return (super.onOptionsItemSelected(menuItem));
    }
}
