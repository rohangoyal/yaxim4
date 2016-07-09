package com.hihello.app.activity;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.hihello.app.R;
import com.hihello.app.adapter.Cityadapter;
import com.hihello.app.adapter.groupadapter;
import com.hihello.app.constant.HiHelloConstant;
import com.hihello.app.db.DatabaseHandler;
import com.hihello.app.getset.get_allgroup;
import com.hihello.app.getset.get_category;
import com.hihello.app.getset.get_city;
import com.hihello.app.getset.get_state;
import com.hihello.app.getset.get_subcate;
import com.natasa.progressviews.ArcProgressBar;
import com.natasa.progressviews.CircleProgressBar;
import com.natasa.progressviews.CircleSegmentBar;
import com.natasa.progressviews.LineProgressBar;
import com.natasa.progressviews.utils.OnProgressViewListener;
import com.natasa.progressviews.utils.ProgressLineOrientation;
import com.natasa.progressviews.utils.ProgressShape;
import com.natasa.progressviews.utils.ProgressStartPoint;
import com.natasa.progressviews.utils.ProgressViewFactory;
import com.natasa.progressviews.utils.ShapeType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

public class DemoActivity extends AppCompatActivity {
    DatabaseHandler db;
    ArrayList<get_state> statedata;
    ArrayList<get_city> citydata;
    private static final String DATABASE_NAME = "hihello.db";
    private CircleProgressBar circleProgressBar;
    private CircleProgressBar circleProgressBar1;
    private CircleSegmentBar segmentBar;
    private LineProgressBar lineProgressbar;
    private Runnable mTimer;
    protected int progress;
    private Handler mHandler;
    private LineProgressBar lineProgressbar1;
    private ArcProgressBar arc_progressbar;
    private int[] colors = {Color.GRAY, Color.CYAN, Color.BLUE};
    TextView line1,line2,cont;
    ArrayList<get_category> cate;
    ArrayList<get_subcate> subcate;
    ArrayList<get_allgroup> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_waiting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        db = new DatabaseHandler(this);
        getSupportActionBar().setTitle("Welcome to Hi Hello");
        mHandler = new Handler();

        line1=(TextView)findViewById(R.id.textView27);
        line2=(TextView)findViewById(R.id.textView28);
        cont=(TextView)findViewById(R.id.textView30);
        categoryvolley();
        subcategoryvolley();
        statevolley();
        cityvolley();
        groupvolley();

//        initSegmentProgressBar();


//        initLineProgressBar();

        circleProgressBar1 = (CircleProgressBar) findViewById(R.id.circle_progress1);
//        circleProgressBar = (CircleProgressBar) findViewById(R.id.circle_progress);
//
//
//        arc_progressbar = (ArcProgressBar) findViewById(R.id.arc_progressbar);
//        arc_progressbar.setLinearGradientProgress(true, colors);
        circleProgressBar1.setStartPositionInDegrees(ProgressStartPoint.LEFT);
//        circleProgressBar.setRoundEdgeProgress(true);
//        circleProgressBar.setTextSize(52);
        circleProgressBar1.setLinearGradientProgress(true);
//        circleProgressBar.setStartPositionInDegrees(90);
        //you can set listener for progress in every ProgressView
//        circleProgressBar.setOnProgressViewListener(new OnProgressViewListener() {
//            @Override
//            public void onFinish() {
//                //do something on progress finish
//                circleProgressBar.setText("done!");
//                // circleProgressBar.resetProgressBar();
//            }
//
//            @Override
//            public void onProgressUpdate(float progress) {
//                circleProgressBar.setText("" + (int) progress);
//
//            }
//        });
        // addArcProgress();
//        addCircleView();

        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in=new Intent(DemoActivity.this,MyProfile.class);
                startActivity(in);
                finish();
            }
        });
    }

    private void initLineProgressBar() {
//        lineProgressbar = (LineProgressBar) findViewById(R.id.line_progressbar);
//        lineProgressbar1 = (LineProgressBar) findViewById(R.id.line_progressbar1);
        lineProgressbar1.setLineOrientation(ProgressLineOrientation.VERTICAL);
        lineProgressbar1.setLinearGradientProgress(true);
        lineProgressbar1.setRoundEdgeProgress(true);

    }

    private void initSegmentProgressBar() {
//        segmentBar = (CircleSegmentBar) findViewById(R.id.segment_bar);
        //you can set for every ProgressView width, progress background width, progress bar line width
        segmentBar.setCircleViewPadding(2);
        segmentBar.setWidth(250);
        segmentBar.setWidthProgressBackground(25);
        segmentBar.setWidthProgressBarLine(25);
        //you can set start position for progress
        segmentBar.setStartPositionInDegrees(ProgressStartPoint.BOTTOM);

        //you can set linear gradient with default colors or to set yours colors, or sweep gradient
        segmentBar.setLinearGradientProgress(true);
    }

    //examples adding progressview programatically
//    private void addArcProgress() {
//        ProgressViewFactory pv = new ProgressViewFactory(this);
//        ProgressShape progres = pv.getShape(ShapeType.ARC);
//        ((ViewGroup) findViewById(R.id.rlMain)).addView((View) progres);
//        ((ArcProgressBar) progres).setProgressIndeterminateAnimation(2000);
//    }

//    private void addCircleView() {
//        CircleProgressBar cpbar = new CircleProgressBar(this);
//        ((ViewGroup) findViewById(R.id.rlContainer)).addView(cpbar);
//        cpbar.setProgress(65);
//        cpbar.setWidth(200);
//        cpbar.setText("Loading...");
//        cpbar.setTextSize(30);
//        cpbar.setBackgroundColor(Color.LTGRAY);
//        cpbar.setProgressColor(Color.RED);
//
//    }


    @Override
    protected void onResume() {
        super.onResume();
        setTimer();

    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mTimer);

    }

    private void setTimer() {
        mTimer = new Runnable() {
            @Override
            public void run() {
                progress += 1;
                if (progress <= 100)
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
//                            circleProgressBar.setProgress(progress);

                            circleProgressBar1.setProgress(progress);
                            circleProgressBar1.setText("" + progress, Color.DKGRAY);

//                            lineProgressbar.setProgress(progress);
//                            lineProgressbar1.setProgress(progress);
//
//                            segmentBar.setProgress((float) progress);
//                            segmentBar.setText("" + progress, 30, Color.GRAY);
//
//                            arc_progressbar.setProgress(progress);
                        }
                    });
                else
                {
                    line1.setVisibility(View.INVISIBLE);
                    cont.setVisibility(View.VISIBLE);
                    line2.setText("Congratulations");
                }

                mHandler.postDelayed(this, 500);
            }

        };

        mHandler.postDelayed(mTimer, 500);

    }



    public void categoryvolley()
    {

        cate=new ArrayList<get_category>();

        final ProgressDialog dialog = new ProgressDialog(DemoActivity.this);
        dialog.setMessage("Loading..Please wait.");
//	     dialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        dialog.setCanceledOnTouchOutside(false);
//        dialog.show();

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        Log.e("url= ", "jjava.lang.String[]iii");
        String url = HiHelloConstant.url+"fetch_category.php";
        Log.e("url= ", url);

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
                        cate.add(new get_category(jsonobj.getInt("id"), jsonobj.getString("category")));
//                        Toast.makeText(getActivity(), jsonobj.getString("subcat") + "", Toast.LENGTH_LONG).show();
                        int id=jsonobj.getInt("id");
                        String category=jsonobj.getString("category").trim();


                        db.insertcategory(id, category);

                    }
                    db.close();
                    db.getAllCategory();
                    int i=db.getAllCategory().size();
                    Log.e("category size", i + "");

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

    public void subcategoryvolley()
    {

        subcate=new ArrayList<get_subcate>();

        final ProgressDialog dialog = new ProgressDialog(DemoActivity.this);
        dialog.setMessage("Loading..Please wait.");
//	     dialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        dialog.setCanceledOnTouchOutside(false);
//        dialog.show();

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        Log.e("url= ", "jjava.lang.String[]iii");
        String url = HiHelloConstant.url+"fetch_subcategory.php";
        Log.e("url= ", url);

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
                        subcate.add(new get_subcate(jsonobj.getInt("id"), jsonobj.getString("cat_id"), jsonobj.getString("subcategory")));
//                        Toast.makeText(getActivity(), jsonobj.getString("subcat") + "", Toast.LENGTH_LONG).show();
                        int id=jsonobj.getInt("id");
                        String category_id=jsonobj.getString("cat_id").trim();
                        String subcategory=jsonobj.getString("subcategory").trim();


                        db.insertsubcategory(id, category_id, subcategory);

                    }
                    db.close();
                    db.getAllSubCategory();
                    int i=db.getAllSubCategory().size();
                    Log.e("subcategory size", i + "");

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
    public void statevolley()
    {

        statedata=new ArrayList<get_state>();

        final ProgressDialog dialog = new ProgressDialog(DemoActivity.this);
        dialog.setMessage("Loading..Please wait.");
//	     dialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        dialog.setCanceledOnTouchOutside(false);
//        dialog.show();

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        Log.e("url= ", "jjava.lang.String[]iii");
        String url = HiHelloConstant.url+"fetch_state.php?";
        Log.e("url= ", url);

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
                        statedata.add(new get_state(jsonobj.getInt("id"), jsonobj.getString("cid"), jsonobj.getString("state")));
//                        Toast.makeText(getActivity(), jsonobj.getString("subcat") + "", Toast.LENGTH_LONG).show();
                        int id=jsonobj.getInt("id");
                        String cid=jsonobj.getString("cid").trim();
                        String state=jsonobj.getString("state").trim();


                        db.insertstate(id, cid, state);
                    }
                    db.close();
                    db.getAllState();
                    int i=db.getAllState().size();
                    Log.e("state size", i + "");


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

    public void cityvolley()
    {

        citydata=new ArrayList<get_city>();

        final ProgressDialog dialog = new ProgressDialog(DemoActivity.this);
        dialog.setMessage("Loading..Please wait.");
//	     dialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        dialog.setCanceledOnTouchOutside(false);
//        dialog.show();

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        Log.e("url= ", "jjava.lang.String[]iii");
        String url = HiHelloConstant.url+"fetch_city.php?";
        Log.e("url= ", url);

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
                        citydata.add(new get_city(jsonobj.getInt("id"), jsonobj.getString("state_id"), jsonobj.getString("city")));
//                        Toast.makeText(getActivity(), jsonobj.getString("subcat") + "", Toast.LENGTH_LONG).show();
                        int id=jsonobj.getInt("id");
                        String state_id=jsonobj.getString("state_id").trim();
                        String city=jsonobj.getString("city").trim();


                        db.insertcity(id, state_id, city);
                    }
                    db.close();
                    db.getAllCity();
                    int i=db.getAllCity().size();
                    Log.e("city size", i + "");


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







    public void groupvolley()
    {

        data=new ArrayList<get_allgroup>();

        final ProgressDialog dialog = new ProgressDialog(DemoActivity.this);
        dialog.setMessage("Loading..Please wait.");
        dialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        dialog.setCanceledOnTouchOutside(false);
//        dialog.show();

        RequestQueue queue = Volley.newRequestQueue(DemoActivity.this);
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
                        db.insertgroup(jsonobj.getInt("id"), jsonobj.getString("admin_name"), jsonobj.getString("group_name"), jsonobj.getString("about"), jsonobj.getString("alt_mobile"), jsonobj.getString("category"), jsonobj.getString("subcategory"), jsonobj.getString("email"), jsonobj.getString("weburl"), jsonobj.getString("state"), jsonobj.getString("city"), jsonobj.getString("address"), jsonobj.getString("date"),jsonobj.getString("admin_pic"), jsonobj.getString("group_pic"), jsonobj.getString("title"), jsonobj.getString("comment"), jsonobj.getString("follow"), jsonobj.getString("profile_name"),jsonobj.getString("admin_thumb"), jsonobj.getString("group_thumb"));
                        Log.e("title",jsonobj.getString("title"));
                    }

                    db.close();
                    int si=db.getAllGroups().size();
                    Log.e("All Group Size", si + "");

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


}
