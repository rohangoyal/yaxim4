package com.hihello.app.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.hihello.app.R;
import com.hihello.app.adapter.firstlistadapter;
import com.hihello.app.common.Log;
import com.hihello.app.constant.HiHelloConstant;
import com.hihello.app.constant.NetworkConnection;
import com.hihello.app.db.DatabaseHandler;
import com.hihello.app.getset.get_set_groups;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by rohan on 6/20/2016.
 */
public class Group_common extends AppCompatActivity
{
    NetworkConnection nw;
    ListView list;
    firstlistadapter adp;
    ArrayList<get_set_groups> data;
    String name;
    DatabaseHandler db;
    SharedPreferences pref_name;
    private Toolbar tool;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_group);


        tool=(Toolbar)findViewById(R.id.toolbar);



        setSupportActionBar(tool);
        tool.setTitle("Group");

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nw=new NetworkConnection(getApplicationContext());
        pref_name=getSharedPreferences("pref_name", 1);
        name=pref_name.getString("username", "");
        Log.e("usernameee", name);

        list=(ListView)findViewById(R.id.listView1);

        volley();


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(name.equals(data.get(position).getProfile_name())) {
                    Intent in = new Intent(getApplicationContext(), Group_admin.class);
                    in.putExtra("group_id", data.get(position).getId() + "");
                    in.putExtra("admin", data.get(position).getAdmin_name());
                    in.putExtra("group", data.get(position).getGroup_name());
                    in.putExtra("about", data.get(position).getAbout());
                    in.putExtra("alt_mob", data.get(position).getAlt_mob());
                    in.putExtra("cate", data.get(position).getCate());
                    in.putExtra("subcate", data.get(position).getSubcate());
                    in.putExtra("email", data.get(position).getEmail());
                    in.putExtra("weburl", data.get(position).getWeb_url());
                    in.putExtra("state", data.get(position).getState());
                    in.putExtra("city", data.get(position).getCity());
                    in.putExtra("address", data.get(position).getAddress());
                    in.putExtra("admin_pic", data.get(position).getAdmin_pic());
                    in.putExtra("group_pic", data.get(position).getGroup_pic());
                    in.putExtra("date", data.get(position).getDate());
                    in.putExtra("follow", data.get(position).getFollow());
                    in.putExtra("title",data.get(position).getTitle());
                    in.putExtra("admin_pich",data.get(position).getGroup_pich());
//                    Toast.makeText(getActivity(),data.get(position).getTitle(),Toast.LENGTH_SHORT).show();
                    Log.e("group_pic", data.get(position).getGroup_pic());
                    Log.e("admin_pic", data.get(position).getAdmin_pic());
                    in.putExtra("profile_name", data.get(position).getProfile_name());
                    startActivity(in);
                    finish();
                }
                else
                {
                    Intent in = new Intent(getApplicationContext(), Group_admin.class);
                    in.putExtra("group_id", data.get(position).getId() + "");
                    in.putExtra("admin", data.get(position).getAdmin_name());
                    in.putExtra("group", data.get(position).getGroup_name());
                    in.putExtra("about", data.get(position).getAbout());
                    in.putExtra("alt_mob", data.get(position).getAlt_mob());
                    in.putExtra("cate", data.get(position).getCate());
                    in.putExtra("subcate", data.get(position).getSubcate());
                    in.putExtra("email", data.get(position).getEmail());
                    in.putExtra("weburl", data.get(position).getWeb_url());
                    in.putExtra("state", data.get(position).getState());
                    in.putExtra("city", data.get(position).getCity());
                    in.putExtra("address", data.get(position).getAddress());
                    in.putExtra("admin_pic", data.get(position).getAdmin_pic());
                    in.putExtra("group_pic", data.get(position).getGroup_pic());
                    in.putExtra("title",data.get(position).getTitle());
//                    Toast.makeText(getActivity(),data.get(position).getTitle(),Toast.LENGTH_SHORT).show();
                    Log.e("group_pic", data.get(position).getGroup_pic());
                    Log.e("admin_pic",data.get(position).getAdmin_pic());
                    in.putExtra("date", data.get(position).getDate());
                    in.putExtra("admin_pich",data.get(position).getGroup_pich());
                    in.putExtra("follow", data.get(position).getFollow());
                    in.putExtra("profile_name", data.get(position).getProfile_name());
                    startActivity(in);
                    finish();
                }
            }
        });
    }

    public void volley()
    {
        final ProgressDialog dialog = new ProgressDialog(getApplicationContext());
        dialog.setMessage("Loading..Please wait.");
        dialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        dialog.setCanceledOnTouchOutside(false);
//        dialog.show();


        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());

        String url1= HiHelloConstant.url+"fetch_joined.php?profile_name="+name;
        Log.e("url= ", url1);

        JsonObjectRequest request=new JsonObjectRequest(url1, null,new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject res)
            {
                data=new ArrayList<get_set_groups>();

                try {
                    JSONArray jr=res.getJSONArray("name");
//                    dialog.dismiss();
                    Log.e("response=",res.getJSONArray("name").toString());

                    int len=jr.length();

                    Log.e("length=",String.valueOf(len));

                    for(int i=0;i<jr.length();i++)
                    {
                        JSONObject jsonobj=jr.getJSONObject(i);

                        Log.e("aarray=",jr.toString());

                        data.add(new get_set_groups(jsonobj.getInt("id"), jsonobj.getString("admin_name"), jsonobj.getString("group_name"), jsonobj.getString("about"), jsonobj.getString("alt_mobile"), jsonobj.getString("category"), jsonobj.getString("subcategory"), jsonobj.getString("email"), jsonobj.getString("weburl"), jsonobj.getString("state"), jsonobj.getString("city"), jsonobj.getString("address"),jsonobj.getString("date"), jsonobj.getString("admin_thumb"), jsonobj.getString("group_thumb"), jsonobj.getString("title"), jsonobj.getString("comment"), jsonobj.getString("follow"), jsonobj.getString("profile_name"), jsonobj.getString("admin_pic"), jsonobj.getString("group_pic")));
                    }


                    adp=new firstlistadapter(getApplicationContext(),data,name);
                    list.setAdapter(adp);


                } catch (JSONException e) {
                    // TODO Auto-generated catch block
//                    dialog.dismiss();
                    Log.e("catch exp= ", e.toString());
                    e.printStackTrace();
                }

            }

        }
                ,new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
//                dialog.dismiss();
                Log.e("error", arg0.toString());
            }
        });

        queue.add(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sms_menu, menu);
        return true;
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
    }
}
