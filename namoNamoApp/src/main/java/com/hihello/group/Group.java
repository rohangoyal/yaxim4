package com.hihello.group;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.hihello.app.activity.AllContacts;
import com.hihello.app.activity.Create_group;
import com.hihello.app.activity.Group_view;
import com.hihello.app.activity.HomeMain_activity;
import com.hihello.app.adapter.Categoryadapter;
import com.hihello.app.adapter.Cityadapter;
import com.hihello.app.adapter.Localgroupadapter;
import com.hihello.app.adapter.PagerAdapter3;
import com.hihello.app.adapter.Stateadapter;
import com.hihello.app.adapter.Subcategoryadapter;
import com.hihello.app.adapter.firstlistadapter;
import com.hihello.app.adapter.groupadapter;
import com.hihello.app.constant.HiHelloConstant;
import com.hihello.app.constant.NetworkConnection;
import com.hihello.app.db.DatabaseHandler;
import com.hihello.app.getset.get_allgroup;
import com.hihello.app.getset.get_category;
import com.hihello.app.getset.get_city;
import com.hihello.app.getset.get_comment;
import com.hihello.app.getset.get_set_groups;
import com.hihello.app.getset.get_state;
import com.hihello.app.getset.get_subcate;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.hihello.app.R;
import com.hihello.app.apicall.GetBannersApiCall;

import com.hihello.app.service.Servicable;
import com.viewpagerindicator.CirclePageIndicator;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Group extends AppCompatActivity {
    DatabaseHandler db;
    ArrayList<get_set_groups> groupdata;
    ArrayList<get_category> cate;
    ArrayList<get_subcate> subcate;
    ArrayList<get_state> statedata;
    ArrayList<get_city> citydata;
    Stateadapter stateadapter;
    public static String statename="Haryana",cityname="";
    Cityadapter cityadapter;
    NetworkConnection nw;
    ArrayList<get_comment> data1;
    ViewPager viewPager;
    PagerAdapter adapter;
    ListView lv;
    groupadapter adapter1;
    Localgroupadapter adapter2;
    CirclePageIndicator indicator;
    FloatingActionButton category,state,topten;
    int i=0;
    int width;
    TimerTask timer;
    FloatingActionMenu float_action;
    FrameLayout frame;
    MaterialSearchView searchView ;
    private Toolbar tool;
    ArrayList<get_allgroup> data;
    ArrayList<get_allgroup> data2;
    ArrayList<get_allgroup> data3;
    ArrayList<get_allgroup> data4;
    LinearLayout back_layout;
    ArrayList<ArrayList<String>> listoflist;
    Categoryadapter cateadapter;
    Subcategoryadapter subadapter;
    ListView lview;
    ImageView close;
    SharedPreferences pref, pref_cate;
    SharedPreferences.Editor pref_edit, edit_cate;
    int idd = 0;
    TextView heading;
    String ccategory="",subcategory="";

    int total_list_items=0;
    int number_of_items_per_pages=0;
    private int noOfBtns;
    private Button[] btns;


//    private Servicable.ServiceListener listener = new Servicable.ServiceListener() {
//
//        @Override
//        public void onComplete(Servicable<?> service) {
//
//
//            String response= ((GetBannersApiCall) service).getStringResponce();




//            try {
//                JSONArray json=new JSONArray(response);
//
//                String data[]=new String[json.length()];
//                for(int i=0;i<json.length();i++)
//                {
//
//                  JSONObject obj= (JSONObject) json.get(i);
//
//                    data[i]=obj.getString("image_url");
//
//
//                }


//            } catch (Exception e) {
//
//                Log.e("ex","ex",e);
//                e.printStackTrace();
//            }
//        }
//    };



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
                adapter= new PagerAdapter(getSupportFragmentManager(),data);
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
//                                viewPager.setCurrentItem(i % 10);

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
        setContentView(R.layout.group);
        nw=new NetworkConnection(Group.this);
        db = new DatabaseHandler(this);
        db = new DatabaseHandler(getApplicationContext());
        frame=(FrameLayout)findViewById(R.id.Frame_layout);
        float_action=(FloatingActionMenu)findViewById(R.id.menu2);
        cate = db.getAllCategory();
        statedata=db.getAllState();
        pref_cate = getSharedPreferences("pref_cate", 1);
        Log.e("cateeee size", cate.size() + "");
        if(nw.isConnectingToInternet()==true) {
            commentvolley();
        }
//        else
//            Toast.makeText(getApplicationContext(), "Network Problem", Toast.LENGTH_LONG).show();

        Display display = getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        int height = display.getHeight();
//        Toast.makeText(Group.this, width + " " + height, Toast.LENGTH_LONG).show();
        lv=(ListView)findViewById(R.id.listView2);
        category=(FloatingActionButton)findViewById(R.id.fab11);
        state=(FloatingActionButton)findViewById(R.id.fab12);
        topten=(FloatingActionButton)findViewById(R.id.fab13);
        LayoutInflater inflater = getLayoutInflater();

        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.list_header, lv,
                false);
        lv.addHeaderView(header, null, false);

         tool=(Toolbar)findViewById(R.id.toolbar);



        setSupportActionBar(tool);
        if(nw.isConnectingToInternet()==true) {
            groupvolley();
        }
//        else
//        {
////            Toast.makeText(getApplicationContext(), "Network Problem", Toast.LENGTH_LONG).show();
//            if(db.getAllGroups().size()>0) {
//                groupdata = new ArrayList<get_set_groups>();
//                groupdata = db.getAllGroups();
//                adapter2 = new Localgroupadapter(groupdata,width);
//                lv.setAdapter(adapter2);
//            }
//        }

//            getgroup();



        topten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float_action.close(true);
                topgroupvolley();
            }
        });


        state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float_action.close(true);
                final Dialog dialog = new Dialog(Group.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.selectcategory);

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                Window window = dialog.getWindow();
                lp.copyFrom(window.getAttributes());

                //This makes the dialog take up the full width
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                window.setAttributes(lp);

                heading=(TextView)dialog.findViewById(R.id.textView32);
                heading.setText("Select your state");
                lv=(ListView)dialog.findViewById(R.id.listView3);
                stateadapter=new Stateadapter(statedata);
                close=(ImageView)dialog.findViewById(R.id.close);
                lv.setAdapter(stateadapter);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        statename = statedata.get(position).getState();
                        idd=statedata.get(position).getId();
                        citydata=db.getSelectedAllCity(String.valueOf(idd));
                        Log.e("citydata size",citydata.size()+"");
//                        state.setText(statename);
                        cityfun();
                        dialog.dismiss();
                    }
                });
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });


        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              float_action.close(true);

                final Dialog dialog = new Dialog(Group.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.selectcategory);

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                Window window = dialog.getWindow();
                lp.copyFrom(window.getAttributes());

                //This makes the dialog take up the full width
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                window.setAttributes(lp);


                lview = (ListView) dialog.findViewById(R.id.listView3);
                cateadapter = new Categoryadapter(cate);
                close = (ImageView) dialog.findViewById(R.id.close);
                lview.setAdapter(cateadapter);
                lview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        edit_cate = pref_cate.edit();
                        edit_cate.putString("category", cate.get(position).getCategory());
                        edit_cate.commit();

                        HiHelloConstant.catename = pref_cate.getString("category", "");

                        idd = cate.get(position).getId();
                        ccategory=HiHelloConstant.catename;
                        subcate = db.getSelectedAllSubCategory(String.valueOf(idd));
                        Log.e("subcateeee size", subcate.size() + "");
                        subcategoryfun();
                        dialog.dismiss();
                    }
                });
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        searchView  = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setVoiceSearch(true);
        tool.setTitle("Group");

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(nw.isConnectingToInternet()==true) {
            GetBannersApiCall api = new GetBannersApiCall(
                    "home");
            api.runAsync(listener);
        }
//        else
//            Toast.makeText(getApplicationContext(), "Network Problem", Toast.LENGTH_LONG).show();




        viewPager = (ViewPager) findViewById(R.id.viewPager );



        indicator = (CirclePageIndicator)findViewById(R.id.indicator);


        final Handler handler= new Handler() ;

        timer=new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {

                    @Override
                    public void run() {
//                        viewPager.setCurrentItem(i % 10);

                        i++;
                    }
                });
            }
        };
        Timer time=new Timer();
        time.schedule(timer, 0, 5000);

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
                Intent in1= new Intent(Group.this,HomeMain_activity.class);
                startActivity(in1);
                finish();
                break;
            case R.id.addgroup:
                Intent in= new Intent(Group.this, Create_group.class);
                startActivity(in);
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent in= new Intent(Group.this, HomeMain_activity.class);
        startActivity(in);
        finish();
    }

//    public void groupvolley()
//    {
//        lv=(ListView)findViewById(R.id.listView2);
//        data=new ArrayList<get_allgroup>();
//        listoflist=new ArrayList<ArrayList<String>>();
//
//        final ProgressDialog dialog = new ProgressDialog(Group.this);
//        dialog.setMessage("Loading..Please wait.");
//        dialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.show();
//
//        RequestQueue queue = Volley.newRequestQueue(Group.this);
//        Log.e("url= ", "jjava.lang.String[]iii");
//
//        String url = HiHelloConstant.url+"fetch.php?";
//        Log.e("url= ", url);
//
//        JsonObjectRequest request = new JsonObjectRequest( url, null, new Response.Listener<JSONObject>() {
//
//            @Override
//            public void onResponse(JSONObject res) {
//                dialog.dismiss();
//                try {
//                    JSONArray jr = res.getJSONArray("name");
//
////                    Log.e("response=", res.getJSONArray("data").toString());
//                    int len = jr.length();
//
////                    Log.e("length=", String.valueOf(len));
//
//                    for (int i = 0; i < jr.length(); i++) {
//                        JSONObject jsonobj = jr.getJSONObject(i);           //String nom, String prenom, String email, String age, String deport, String actuel
//                        data.add(new get_allgroup(jsonobj.getInt("id"), jsonobj.getString("admin_name"), jsonobj.getString("group_name"), jsonobj.getString("about"), jsonobj.getString("alt_mobile"), jsonobj.getString("category"), jsonobj.getString("subcategory"), jsonobj.getString("email"), jsonobj.getString("weburl"), jsonobj.getString("state"), jsonobj.getString("city"), jsonobj.getString("address"), jsonobj.getString("admin_thumb"), jsonobj.getString("group_thumb"), jsonobj.getString("admin_pic"), jsonobj.getString("group_pic"), jsonobj.getString("title"), jsonobj.getString("comment"), jsonobj.getString("date"), jsonobj.getString("follow")));
//                        db.insertgroup(jsonobj.getInt("id"), jsonobj.getString("admin_name"), jsonobj.getString("group_name"), jsonobj.getString("about"), jsonobj.getString("alt_mobile"), jsonobj.getString("category"), jsonobj.getString("subcategory"), jsonobj.getString("email"), jsonobj.getString("weburl"), jsonobj.getString("state"), jsonobj.getString("city"), jsonobj.getString("address"), jsonobj.getString("date"),jsonobj.getString("admin_thumb"), jsonobj.getString("group_thumb"), jsonobj.getString("title"), jsonobj.getString("comment"), jsonobj.getString("follow"), jsonobj.getString("profile_name"));
//                    }
//
//                    db.close();
//                    int si=db.getAllGroups().size();
//                    Log.e("All Group Size", si + "");
//
////                        JSONArray jr1 = res.getJSONArray("name1");
////
////                        for(int i=0;i<data.size();i++)
////                        {
////                            Log.e("checkname1",jr1.length()+"");
////
////                            ArrayList<String> list=new ArrayList<String>();
////
////                            for (int j=0;j<jr1.length();j++)
////                            {
////                                JSONObject jsonobj = jr1.getJSONObject(j);
////                                String gname=jsonobj.getString("group_name");
////                                String comm=jsonobj.getString("comment");
////                                String uname=jsonobj.getString("username");
////
////                                if (data.get(i).getGroup_name().equalsIgnoreCase(gname))
////                                    list.add(uname+" >> "+comm);
////
////                                if(j==jr1.length()-1)
////                                    listoflist.add(list);
////
////                            }
////
////                            if(jr1.length()<1)
////                                listoflist.add(list);
////
////
////                        }
//
////                    for (int i=0;i<listoflist.size();i++)
////                    {
////
////                        ArrayList<String> list=new ArrayList<String>();
////
////                        list=listoflist.get(i);
////                        for (int j=0;j<list.size();j++)
////                            Log.e("commenttttt",list.get(j));
////
////                    }
//
//                    adapter1=new groupadapter(data,width);
//                    lv.setAdapter(adapter1);
//                } catch (JSONException e) {
//                    // TODO Auto-generated catch block
//                    Log.e("catch exp= ", e.toString());
//                    e.printStackTrace();
//                    dialog.dismiss();
//                }
//
//            }
//
//        }
//                , new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError arg0) {
//
////                Log.e("error", arg0.toString());
////                dialog.dismiss();
//            }
//        });
//        int socketTimeout = 50000;
//        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        request.setRetryPolicy(policy);
//        queue.add(request);
//    }

    public void categorygroupvolley()
    {
        lv=(ListView)findViewById(R.id.listView2);
        data2=new ArrayList<get_allgroup>();

        final ProgressDialog dialog = new ProgressDialog(Group.this);
        dialog.setMessage("Loading..Please wait.");
        dialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        RequestQueue queue = Volley.newRequestQueue(Group.this);
        Log.e("url= ", "jjava.lang.String[]iii");
        ccategory=ccategory.replace(" ","%20");
        subcategory=subcategory.replace(" ","%20");
        String url = HiHelloConstant.url+"search_catsubcat.php?category="+ccategory+"&subcat="+subcategory;
        Log.e("url= ", url);

        JsonObjectRequest request = new JsonObjectRequest( url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject res) {
                dialog.dismiss();
                try {
                    JSONArray jr = res.getJSONArray("name");

//                    Log.e("response=", res.getJSONArray("data").toString());
                    int len = jr.length();

//                    Log.e("length=", String.valueOf(len));

                    for (int i = 0; i < jr.length(); i++) {
                        JSONObject jsonobj = jr.getJSONObject(i);           //String nom, String prenom, String email, String age, String deport, String actuel
                        data2.add(new get_allgroup(jsonobj.getInt("id"), jsonobj.getString("admin_name"), jsonobj.getString("group_name"), jsonobj.getString("about"), jsonobj.getString("alt_mobile"), jsonobj.getString("category"), jsonobj.getString("subcategory"), jsonobj.getString("email"), jsonobj.getString("weburl"), jsonobj.getString("state"), jsonobj.getString("city"), jsonobj.getString("address"), jsonobj.getString("admin_thumb"), jsonobj.getString("group_thumb"), jsonobj.getString("admin_pic"), jsonobj.getString("group_pic"), jsonobj.getString("title"), jsonobj.getString("comment"), jsonobj.getString("date"), jsonobj.getString("follow"), jsonobj.getString("profile_name")));
//                        db.insertgroup(jsonobj.getInt("id"), jsonobj.getString("admin_name"), jsonobj.getString("group_name"), jsonobj.getString("about"), jsonobj.getString("alt_mobile"), jsonobj.getString("category"), jsonobj.getString("subcategory"), jsonobj.getString("email"), jsonobj.getString("weburl"), jsonobj.getString("state"), jsonobj.getString("city"), jsonobj.getString("address"), jsonobj.getString("date"),jsonobj.getString("admin_thumb"), jsonobj.getString("group_thumb"), jsonobj.getString("title"), jsonobj.getString("comment"), jsonobj.getString("follow"), jsonobj.getString("profile_name"));
                    }

                    adapter1=new groupadapter(data2,width);
                    lv.setAdapter(adapter1);
                    if(adapter1!=null) {
                        adapter1.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Log.e("catch exp= ", e.toString());
                    e.printStackTrace();
                    dialog.dismiss();
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




    public void commentvolley()
    {

        data1=new ArrayList<get_comment>();

        final ProgressDialog dialog = new ProgressDialog(getApplicationContext());
        dialog.setMessage("Loading..Please wait.");
//	     dialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        dialog.setCanceledOnTouchOutside(false);
//        dialog.show();

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        android.util.Log.e("url= ", "jjava.lang.String[]iii");
        String url = HiHelloConstant.url+"fetch_comment.php?";
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
                        data1.add(new get_comment(jsonobj.getInt("id"), jsonobj.getString("group_name"), jsonobj.getString("username"), jsonobj.getString("comment"), jsonobj.getString("pic_url"), jsonobj.getString("date")));
//                        Toast.makeText(getActivity(), jsonobj.getString("subcat") + "", Toast.LENGTH_LONG).show();
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

    public void statecitygroupvolley()
    {
        lv=(ListView)findViewById(R.id.listView2);
        data3=new ArrayList<get_allgroup>();
        listoflist=new ArrayList<ArrayList<String>>();

        final ProgressDialog dialog = new ProgressDialog(Group.this);
        dialog.setMessage("Loading..Please wait.");
        dialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        RequestQueue queue = Volley.newRequestQueue(Group.this);
        Log.e("url= ", "jjava.lang.String[]iii");
        statename=statename.replace(" ","%20");
        cityname=cityname.replace(" ","%20");
        String url = HiHelloConstant.url+"search_statecity.php?state="+statename+"&city="+cityname;
        Log.e("url= ", url);

        JsonObjectRequest request = new JsonObjectRequest( url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject res) {
                dialog.dismiss();
                try {
                    JSONArray jr = res.getJSONArray("name");

//                    Log.e("response=", res.getJSONArray("data").toString());
                    int len = jr.length();

//                    Log.e("length=", String.valueOf(len));

                    for (int i = 0; i < jr.length(); i++) {
                        JSONObject jsonobj = jr.getJSONObject(i);           //String nom, String prenom, String email, String age, String deport, String actuel
                        data3.add(new get_allgroup(jsonobj.getInt("id"), jsonobj.getString("admin_name"), jsonobj.getString("group_name"), jsonobj.getString("about"), jsonobj.getString("alt_mobile"), jsonobj.getString("category"), jsonobj.getString("subcategory"), jsonobj.getString("email"), jsonobj.getString("weburl"), jsonobj.getString("state"), jsonobj.getString("city"), jsonobj.getString("address"), jsonobj.getString("admin_thumb"), jsonobj.getString("group_thumb"), jsonobj.getString("admin_pic"), jsonobj.getString("group_pic"), jsonobj.getString("title"), jsonobj.getString("comment"), jsonobj.getString("date"), jsonobj.getString("follow"), jsonobj.getString("profile_name")));
//                        db.insertgroup(jsonobj.getInt("id"), jsonobj.getString("admin_name"), jsonobj.getString("group_name"), jsonobj.getString("about"), jsonobj.getString("alt_mobile"), jsonobj.getString("category"), jsonobj.getString("subcategory"), jsonobj.getString("email"), jsonobj.getString("weburl"), jsonobj.getString("state"), jsonobj.getString("city"), jsonobj.getString("address"), jsonobj.getString("date"),jsonobj.getString("admin_thumb"), jsonobj.getString("group_thumb"), jsonobj.getString("title"), jsonobj.getString("comment"), jsonobj.getString("follow"), jsonobj.getString("profile_name"));
                    }
                    adapter1=new groupadapter(data3,width);
                    lv.setAdapter(adapter1);
                    adapter1.notifyDataSetChanged();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Log.e("catch exp= ", e.toString());
                    e.printStackTrace();
                    dialog.dismiss();
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


    public void topgroupvolley()
    {
        lv=(ListView)findViewById(R.id.listView2);
        data4=new ArrayList<get_allgroup>();
        listoflist=new ArrayList<ArrayList<String>>();

        final ProgressDialog dialog = new ProgressDialog(Group.this);
        dialog.setMessage("Loading..Please wait.");
        dialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        RequestQueue queue = Volley.newRequestQueue(Group.this);
        Log.e("url= ", "jjava.lang.String[]iii");
        ccategory=ccategory.replace(" ","%20");
        subcategory=subcategory.replace(" ","%20");
        String url = HiHelloConstant.url+"top_groups.php";
        Log.e("url= ", url);

        JsonObjectRequest request = new JsonObjectRequest( url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject res) {
                dialog.dismiss();
                try {
                    JSONArray jr = res.getJSONArray("name");

//                    Log.e("response=", res.getJSONArray("data").toString());
                    int len = jr.length();

//                    Log.e("length=", String.valueOf(len));

                    for (int i = 0; i < jr.length(); i++) {
                        JSONObject jsonobj = jr.getJSONObject(i);           //String nom, String prenom, String email, String age, String deport, String actuel
                        data4.add(new get_allgroup(jsonobj.getInt("id"), jsonobj.getString("admin_name"), jsonobj.getString("group_name"), jsonobj.getString("about"), jsonobj.getString("alt_mobile"), jsonobj.getString("category"), jsonobj.getString("subcategory"), jsonobj.getString("email"), jsonobj.getString("weburl"), jsonobj.getString("state"), jsonobj.getString("city"), jsonobj.getString("address"), jsonobj.getString("admin_thumb"), jsonobj.getString("group_thumb"), jsonobj.getString("admin_pic"), jsonobj.getString("group_pic"), jsonobj.getString("title"), jsonobj.getString("comment"), jsonobj.getString("date"), jsonobj.getString("follow"), jsonobj.getString("profile_name")));
//                        db.insertgroup(jsonobj.getInt("id"), jsonobj.getString("admin_name"), jsonobj.getString("group_name"), jsonobj.getString("about"), jsonobj.getString("alt_mobile"), jsonobj.getString("category"), jsonobj.getString("subcategory"), jsonobj.getString("email"), jsonobj.getString("weburl"), jsonobj.getString("state"), jsonobj.getString("city"), jsonobj.getString("address"), jsonobj.getString("date"),jsonobj.getString("admin_thumb"), jsonobj.getString("group_thumb"), jsonobj.getString("title"), jsonobj.getString("comment"), jsonobj.getString("follow"), jsonobj.getString("profile_name"));
                    }

                    adapter1=new groupadapter(data4,width);
                    lv.setAdapter(adapter1);
                    adapter1.notifyDataSetChanged();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Log.e("catch exp= ", e.toString());
                    e.printStackTrace();
                    dialog.dismiss();
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



        public void subcategoryfun()
        {
            final Dialog dialog = new Dialog(Group.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.selectcategory);

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            Window window = dialog.getWindow();
            lp.copyFrom(window.getAttributes());

            //This makes the dialog take up the full width
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);

            heading = (TextView) dialog.findViewById(R.id.textView32);
            heading.setText(HiHelloConstant.catename);
            lv = (ListView) dialog.findViewById(R.id.listView3);
            subadapter = new Subcategoryadapter(subcate);
            close = (ImageView) dialog.findViewById(R.id.close);
            lv.setAdapter(subadapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    edit_cate = pref_cate.edit();
                    edit_cate.putString("subcategory", subcate.get(position).getSubcategory());
                    edit_cate.commit();

                    HiHelloConstant.subcatename = pref_cate.getString("subcategory", "");
//                            HiHelloConstant.subcatename=subcate.get(position).getSubcategory();
                    subcategory=HiHelloConstant.subcatename;
                    categorygroupvolley();
                    dialog.dismiss();
                }
            });
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    public void cityfun()
    {
        if(idd!=0) {
            final Dialog dialog = new Dialog(Group.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.selectcategory);

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            Window window = dialog.getWindow();
            lp.copyFrom(window.getAttributes());

            //This makes the dialog take up the full width
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);

            heading = (TextView) dialog.findViewById(R.id.textView32);
            heading.setText(statename);
            lv = (ListView) dialog.findViewById(R.id.listView3);
            cityadapter = new Cityadapter(citydata);
            close = (ImageView) dialog.findViewById(R.id.close);
            lv.setAdapter(cityadapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    cityname = citydata.get(position).getCity();
//                    city.setText(cityname);
                    statecitygroupvolley();
                    dialog.dismiss();
                }
            });
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
        else
        {
            Toast.makeText(Group.this,"Please select state",Toast.LENGTH_SHORT).show();
        }
    }

//    public void getgroup()
//    {
//        groupdata=new ArrayList<get_set_groups>();
//        groupdata=db.getAllGroups();
//        adapter2 = new Localgroupadapter(groupdata,width);
//        lv.setAdapter(adapter2);
//        int d=db.getAllGroups().size();
//        com.hihello.app.common.Log.e("sizzzeeeeee",d+"");
//
//    }
    public void groupvolley()
    {

        data=new ArrayList<get_allgroup>();

        final ProgressDialog dialog = new ProgressDialog(Group.this);
        dialog.setMessage("Loading..Please wait.");
        dialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        dialog.setCanceledOnTouchOutside(false);
//        dialog.show();

        RequestQueue queue = Volley.newRequestQueue(Group.this);
        Log.e("url= ", "jjava.lang.String[]iii");

        String url = HiHelloConstant.url+"fetch.php?";
        Log.e("url= ", url);

        JsonObjectRequest request = new JsonObjectRequest( url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject res) {
//                dialog.dismiss();
                try {
                    JSONArray jr = res.getJSONArray("name");

//                    Log.e("response=", res.getJSONArray("data").toString());
                    int len = jr.length();

//                    Log.e("length=", String.valueOf(len));

                    for (int i = 0; i < jr.length(); i++) {
                        JSONObject jsonobj = jr.getJSONObject(i);
                        data.add(new get_allgroup(jsonobj.getInt("id"), jsonobj.getString("admin_name"), jsonobj.getString("group_name"), jsonobj.getString("about"), jsonobj.getString("alt_mobile"), jsonobj.getString("category"), jsonobj.getString("subcategory"), jsonobj.getString("email"), jsonobj.getString("weburl"), jsonobj.getString("state"), jsonobj.getString("city"), jsonobj.getString("address"), jsonobj.getString("admin_thumb"), jsonobj.getString("group_thumb"), jsonobj.getString("admin_pic"), jsonobj.getString("group_pic"), jsonobj.getString("title"), jsonobj.getString("comment"), jsonobj.getString("date"), jsonobj.getString("follow"), jsonobj.getString("profile_name")));
//                        db.insertgroup(jsonobj.getInt("id"), jsonobj.getString("admin_name"), jsonobj.getString("group_name"), jsonobj.getString("about"), jsonobj.getString("alt_mobile"), jsonobj.getString("category"), jsonobj.getString("subcategory"), jsonobj.getString("email"), jsonobj.getString("weburl"), jsonobj.getString("state"), jsonobj.getString("city"), jsonobj.getString("address"), jsonobj.getString("date"),jsonobj.getString("admin_pic"), jsonobj.getString("group_pic"), jsonobj.getString("title"), jsonobj.getString("comment"), jsonobj.getString("follow"), jsonobj.getString("profile_name"),jsonobj.getString("admin_thumb"), jsonobj.getString("group_thumb"));
                        Log.e("title",jsonobj.getString("title"));
                    }
                    adapter1=new groupadapter(data,width);
                    lv.setAdapter(adapter1);
                    total_list_items=data.size();
                    Log.e("data length",total_list_items+"");
//                    db.close();
//                    int si=db.getAllGroups().size();
//                    Log.e("All Group Size", si + "");

//                    JSONArray jr1 = res.getJSONArray("name1");
//
//                    for(int i=0;i<data.size();i++)
//                    {
//                        Log.e("checkname1",jr1.length()+"");
//
//                        ArrayList<String> list=new ArrayList<String>();
//
//                        for (int j=0;j<jr1.length();j++)
//                        {
//                            JSONObject jsonobj = jr1.getJSONObject(j);
//                            String gname=jsonobj.getString("group_name");
//                            String comm=jsonobj.getString("comment");
//                            String uname=jsonobj.getString("username");
//
//                            if (data.get(i).getGroup_name().equalsIgnoreCase(gname))
//                                list.add(uname+" >> "+comm);
//
//                            if(j==jr1.length()-1)
//                                listoflist.add(list);
//
//                        }
//
//                        if(jr1.length()<1)
//                            listoflist.add(list);
//
//
//                    }
//
//                    for (int i=0;i<listoflist.size();i++)
//                    {
//
//                        ArrayList<String> list=new ArrayList<String>();
//
//                        list=listoflist.get(i);
//                        for (int j=0;j<list.size();j++)
//                            Log.e("commenttttt",list.get(j));
//
//                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Log.e("catch exp= ", e.toString());
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

    private void Btnfooter()
    {
        int val = total_list_items%number_of_items_per_pages;
        val = val==0?0:1;
        noOfBtns=total_list_items/number_of_items_per_pages+val;

        LinearLayout ll = (LinearLayout)findViewById(R.id.btnLay);

        btns = new Button[noOfBtns];

        for(int i=0;i<noOfBtns;i++)
        {
            btns[i] =   new Button(this);
            btns[i].setBackgroundColor(getResources().getColor(android.R.color.transparent));
            btns[i].setText(""+(i+1));

            LinearLayout.LayoutParams lp = new     LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            ll.addView(btns[i], lp);

            final int j = i;
            btns[j].setOnClickListener(new View.OnClickListener() {

                public void onClick(View v)
                {
                    loadList(j);
                    CheckBtnBackGroud(j);
                }
            });
        }

    }
    private void CheckBtnBackGroud(int index)
    {
        for(int i=0;i<noOfBtns;i++)
        {
            btns[i].setBackgroundColor(getResources().getColor(android.R.color.transparent));
            if(i==index)
            {
                btns[i].setTextColor(getResources().getColor(android.R.color.white));
            }
            else
            {
                btns[i].setTextColor(getResources().getColor(android.R.color.black));
            }
        }

    }
    ArrayAdapter<get_allgroup> sd;
    private void loadList(int number)
    {
        ArrayList<get_allgroup> sort = new ArrayList<get_allgroup>();

        int start = number * number_of_items_per_pages;
        for(int i=start;i<(start)+number_of_items_per_pages;i++)
        {
            if(i<data.size())
            {
                sort.add(data.get(i));
            }
            else
            {
                break;
            }
        }
        sd = new ArrayAdapter<get_allgroup>(this,
                android.R.layout.simple_list_item_1,
                sort);
//        lv.setAdapter(sd);
    }


}
