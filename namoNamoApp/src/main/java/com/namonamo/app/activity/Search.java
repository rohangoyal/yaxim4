package com.namonamo.app.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.namonamo.app.R;
import com.namonamo.app.common.Log;
import com.namonamo.app.getset.get_setter;

import android.provider.ContactsContract;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by user on 10/21/2015.
 */
public class Search extends BaseActivity
{
    ListView lv;
    Context context;
   ImageView add;
    SearchAdapter adapter;
    ViewFlipper view;
    ImageView add1,add2,add3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.search);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.leftback);
        getSupportActionBar().setTitle("Group Search");
//        lv=(ListView)findViewById(R.id.listView);
//        add=(ImageView)findViewById(R.id.imageView4);
        view=(ViewFlipper)findViewById(R.id.viewFlipper2);
        add1=(ImageView)findViewById(R.id.banner);
        add2=(ImageView)findViewById(R.id.banner1);
        add3=(ImageView)findViewById(R.id.banner2);
        view.setFlipInterval(2000);
        view.startFlipping();

//        share.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent in=new Intent(Search.this,ShareToActivity.class);
//                startActivity(in);
//            }
//        });
//        iconsearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                str=search1.getText().toString();
//                Toast.makeText(getApplicationContext(), str,Toast.LENGTH_SHORT).show();
//                volley1();
//            }
//        });
//        setting.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (b) {
//                    creategroup.setVisibility(View.VISIBLE);
//                    b = false;
//                } else {
//                    creategroup.setVisibility(View.GONE);
//                    b = true;
//
//                }
//            }
//        });
//        searchicon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                searchicon.setVisibility(View.GONE);
//                searchlayout.setVisibility(View.VISIBLE);
//            }
//        });
//        creategroup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                    creategroup.setVisibility(View.GONE);
//                Intent in= new Intent(getApplicationContext(),Creategroup.class);
//                startActivity(in);
//            }
//        });
//        context=this;
//        adapter=new SearchAdapter();
//        lv=(ListView)findViewById(R.id.listView);
//        volley();
//        lv.setAdapter(adapter);
//        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
//        if(c==false)
//        {
//        url1="http://delainetechnologies.com/gyanProject/webservices/fetch.php";
//        Log.e("url= ", url1);
//            c=true;
//        }
//        else
//        {
//          url1="http://www.delainetechnologies.com/gyanProject/webservices/searchdata.php?search="+str;
//            Log.e("url= ", url1);
//            c=false;
//        }
//        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET,url1, null,new Response.Listener<JSONObject>() {
//
//            @Override
//            public void onResponse(JSONObject res)
//            {
//                dataobj=new ArrayList<get_setter>();
//
//                try {
//                    JSONArray jr=res.getJSONArray("name");
//
//                    Log.e("response=",res.getJSONArray("name").toString());
//
//                    //Log.e("vacancy= ",jobarray.toString());
//                    //categoryarray=new ArrayList<String>();
//
//                    int len=jr.length();
//
//                    Log.e("length=",String.valueOf(len));
//
//                    for(int i=0;i<jr.length();i++)
//                    {
//                        JSONObject jsonobj=jr.getJSONObject(i);
//
//                        Log.e("aarray=",jr.toString());
//
//                        dataobj.add(new get_setter(jsonobj.getString("wall_image"),jsonobj.getString("profile_image"), jsonobj.getString("app_contact"),jsonobj.getString("admin_name"),jsonobj.getString("group_name"),jsonobj.getString("email"),jsonobj.getString("phone"),jsonobj.getString("landline"),jsonobj.getString("url"),jsonobj.getString("address"),jsonobj.getString("state"),jsonobj.getString("category"),jsonobj.getString("followers"),jsonobj.getString("viewers")));
//                    }
//
//
//                    adapter=new SearchAdapter(Search.this,dataobj);
//
//                    adapter.notifyDataSetChanged();
//                    lv.setAdapter(adapter);
//
////                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
////
////                        @Override
////                        public void onItemClick(AdapterView<?> arg0, View view,int position, long arg3) {
////                            // TODO Auto-generated method stub
////                            //Integer i=view.getId();
////
////                            //Toast.makeText(getActivity(), Integer.toString(i),200).show();
////
////                            Intent in =new Intent(getActivity(),groupprofile.class);
////                            in.putExtra("wallimage",dataobj.get(position).getWallimage());
////                            in.putExtra("profileimage",dataobj.get(position).getProfileimage());
////                            in.putExtra("contact",dataobj.get(position).getContact()); // form fees
////                            in.putExtra("adminname",dataobj.get(position).getAdminname());// website
////                            in.putExtra("groupimage",dataobj.get(position).getGroupname());
////                            in.putExtra("email",dataobj.get(position).getEmail());
////                            in.putExtra("phone",dataobj.get(position).getPhone());
////                            in.putExtra("landline",dataobj.get(position).getLandline());
////                            in.putExtra("website",dataobj.get(position).getWebsite()); // form fees
////                            in.putExtra("address",dataobj.get(position).getAddress());// website
////                            in.putExtra("state",dataobj.get(position).getState());
////                            in.putExtra("category",dataobj.get(position).getCategory());
////                            in.putExtra("followers",dataobj.get(position).getFollowers());
////                            in.putExtra("viewers",dataobj.get(position).getViewers());
////
//////							String g=dataobj.get(position).getComment();
////                            Log.e("hsgsgshgd", dataobj.get(position).getWallimage());
////
////                            startActivity(in);
////
////
////
////
////
////
////                        }
////                    });
//
//
//
//
//
//
//                } catch (JSONException e) {
//                    // TODO Auto-generated catch block
//                    Log.e("catch exp= ", e.toString());
//                    e.printStackTrace();
//                }
//
//            }
//
//        },new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError arg0) {
//
//                Log.e("error", arg0.toString());
//            }
//        });
//
//        queue.add(request);
    }
//    public void volley()
//    {
//        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
//
//            url1="http://delainetechnologies.com/gyanProject/webservices/fetch.php";
//            Log.e("url= ", url1);
//
//
//        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET,url1, null,new Response.Listener<JSONObject>() {
//
//            @Override
//            public void onResponse(JSONObject res)
//            {
//                dataobj=new ArrayList<get_setter>();
//
//                try {
//                    JSONArray jr=res.getJSONArray("name");
//
//                    Log.e("response=",res.getJSONArray("name").toString());
//
//                    //Log.e("vacancy= ",jobarray.toString());
//                    //categoryarray=new ArrayList<String>();
//
//                    int len=jr.length();
//
//                    Log.e("length=",String.valueOf(len));
//
//                    for(int i=0;i<jr.length();i++)
//                    {
//                        JSONObject jsonobj=jr.getJSONObject(i);
//
//                        Log.e("aarray=",jr.toString());
//
//                        dataobj.add(new get_setter(jsonobj.getString("wall_image"),jsonobj.getString("profile_image"), jsonobj.getString("app_contact"),jsonobj.getString("admin_name"),jsonobj.getString("group_name"),jsonobj.getString("email"),jsonobj.getString("phone"),jsonobj.getString("landline"),jsonobj.getString("url"),jsonobj.getString("address"),jsonobj.getString("state"),jsonobj.getString("category"),jsonobj.getString("followers"),jsonobj.getString("viewers")));
//                    }
//
//
//                    adapter=new SearchAdapter(Search.this,dataobj);
//
//                    adapter.notifyDataSetChanged();
//                    lv.setAdapter(adapter);

//                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//                        @Override
//                        public void onItemClick(AdapterView<?> arg0, View view,int position, long arg3) {
//                            // TODO Auto-generated method stub
//                            //Integer i=view.getId();
//
//                            //Toast.makeText(getActivity(), Integer.toString(i),200).show();
//
//                            Intent in =new Intent(getActivity(),groupprofile.class);
//                            in.putExtra("wallimage",dataobj.get(position).getWallimage());
//                            in.putExtra("profileimage",dataobj.get(position).getProfileimage());
//                            in.putExtra("contact",dataobj.get(position).getContact()); // form fees
//                            in.putExtra("adminname",dataobj.get(position).getAdminname());// website
//                            in.putExtra("groupimage",dataobj.get(position).getGroupname());
//                            in.putExtra("email",dataobj.get(position).getEmail());
//                            in.putExtra("phone",dataobj.get(position).getPhone());
//                            in.putExtra("landline",dataobj.get(position).getLandline());
//                            in.putExtra("website",dataobj.get(position).getWebsite()); // form fees
//                            in.putExtra("address",dataobj.get(position).getAddress());// website
//                            in.putExtra("state",dataobj.get(position).getState());
//                            in.putExtra("category",dataobj.get(position).getCategory());
//                            in.putExtra("followers",dataobj.get(position).getFollowers());
//                            in.putExtra("viewers",dataobj.get(position).getViewers());
//
////							String g=dataobj.get(position).getComment();
//                            Log.e("hsgsgshgd", dataobj.get(position).getWallimage());
//
//                            startActivity(in);
//
//
//
//
//
//
//                        }
//                    });






//                } catch (JSONException e) {
//                    // TODO Auto-generated catch block
//                    Log.e("catch exp= ", e.toString());
//                    e.printStackTrace();
//                }
//
//            }
//
//        },new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError arg0) {
//
//                Log.e("error", arg0.toString());
//            }
//        });
//
//        queue.add(request);
//    }
//    public void volley1()
//    {
//        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
//
//
//            url1="http://www.delainetechnologies.com/gyanProject/webservices/searchdata.php?search="+str;
//            Log.e("url= ", url1);
//        Toast.makeText(getApplicationContext(), str,Toast.LENGTH_SHORT).show();
//
//        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET,url1, null,new Response.Listener<JSONObject>() {
//
//            @Override
//            public void onResponse(JSONObject res)
//            {
//                dataobj=new ArrayList<get_setter>();
//
//                try {
//                    JSONArray jr=res.getJSONArray("name");
//
//                    Log.e("response=",res.getJSONArray("name").toString());
//
//                    //Log.e("vacancy= ",jobarray.toString());
//                    //categoryarray=new ArrayList<String>();
//
//                    int len=jr.length();
//
//                    Log.e("length=",String.valueOf(len));
//
//                    for(int i=0;i<jr.length();i++)
//                    {
//                        JSONObject jsonobj=jr.getJSONObject(i);
//
//                        Log.e("aarray=",jr.toString());
//
//                        dataobj.add(new get_setter(jsonobj.getString("wall_image"),jsonobj.getString("profile_image"), jsonobj.getString("app_contact"),jsonobj.getString("admin_name"),jsonobj.getString("group_name"),jsonobj.getString("email"),jsonobj.getString("phone"),jsonobj.getString("landline"),jsonobj.getString("url"),jsonobj.getString("address"),jsonobj.getString("state"),jsonobj.getString("category"),jsonobj.getString("followers"),jsonobj.getString("viewers")));
//                    }
//
//
//                    adapter=new SearchAdapter(Search.this,dataobj);
//
//                    adapter.notifyDataSetChanged();
//                    lv.setAdapter(adapter);

//                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//                        @Override
//                        public void onItemClick(AdapterView<?> arg0, View view,int position, long arg3) {
//                            // TODO Auto-generated method stub
//                            //Integer i=view.getId();
//
//                            //Toast.makeText(getActivity(), Integer.toString(i),200).show();
//
//                            Intent in =new Intent(getActivity(),groupprofile.class);
//                            in.putExtra("wallimage",dataobj.get(position).getWallimage());
//                            in.putExtra("profileimage",dataobj.get(position).getProfileimage());
//                            in.putExtra("contact",dataobj.get(position).getContact()); // form fees
//                            in.putExtra("adminname",dataobj.get(position).getAdminname());// website
//                            in.putExtra("groupimage",dataobj.get(position).getGroupname());
//                            in.putExtra("email",dataobj.get(position).getEmail());
//                            in.putExtra("phone",dataobj.get(position).getPhone());
//                            in.putExtra("landline",dataobj.get(position).getLandline());
//                            in.putExtra("website",dataobj.get(position).getWebsite()); // form fees
//                            in.putExtra("address",dataobj.get(position).getAddress());// website
//                            in.putExtra("state",dataobj.get(position).getState());
//                            in.putExtra("category",dataobj.get(position).getCategory());
//                            in.putExtra("followers",dataobj.get(position).getFollowers());
//                            in.putExtra("viewers",dataobj.get(position).getViewers());
//
////							String g=dataobj.get(position).getComment();
//                            Log.e("hsgsgshgd", dataobj.get(position).getWallimage());
//
//                            startActivity(in);
//
//
//
//
//
//
//                        }
//                    });






//                } catch (JSONException e) {
//                    // TODO Auto-generated catch block
//                    Log.e("catch exp= ", e.toString());
//                    e.printStackTrace();
//                }
//
//            }
//
//        },new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError arg0) {
//
//                Log.e("error", arg0.toString());
//            }
//        });
//
//        queue.add(request);
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.group_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                Intent in=new Intent(Search.this,HomeMain_activity.class);
                startActivity(in);
                break;
            case R.id.addgroup:
                Intent in1=new Intent(Search.this,Creategroup.class);
                startActivity(in1);
			break;

        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onBackPressed() {
        Intent in=new Intent(Search.this,HomeMain_activity.class);
        startActivity(in);
        finish();
    }
}
