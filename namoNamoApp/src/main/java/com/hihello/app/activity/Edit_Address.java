package com.hihello.app.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.hihello.app.R;
import com.hihello.app.adapter.Categoryadapter;
import com.hihello.app.adapter.Cityadapter;
import com.hihello.app.adapter.Stateadapter;
import com.hihello.app.adapter.Subcategoryadapter;
import com.hihello.app.constant.HiHelloConstant;
import com.hihello.app.db.DatabaseHandler;
import com.hihello.app.getset.get_category;
import com.hihello.app.getset.get_city;
import com.hihello.app.getset.get_state;
import com.hihello.app.getset.get_subcate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by rohan on 5/9/2016.
 */
public class Edit_Address extends AppCompatActivity
{
    DatabaseHandler db;
    ArrayList<get_state> statedata;
    ArrayList<get_city> citydata;
    SharedPreferences pref;
    SharedPreferences.Editor pref_edit;
    EditText address,country,zipcode;
    TextView city,state;
    String pt="";
    int idd=0;
    TextView heading;
    ListView lv;
    ImageView close;
    public static String statename="Haryana",cityname="";
    Stateadapter adapter;
    Cityadapter cityadapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_address);
        pref=getSharedPreferences("pref",1);
        db = new DatabaseHandler(getApplicationContext());
        statedata=db.getAllState();
        Log.e("statedateee size",statedata.size()+"");
        address=(EditText)findViewById(R.id.editText);
        state=(TextView)findViewById(R.id.editText2);
        city=(TextView)findViewById(R.id.editText3);
        country=(EditText)findViewById(R.id.editText4);
        zipcode=(EditText)findViewById(R.id.editText5);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//		getSupportActionBar().setHomeAsUpIndicator(R.drawable.leftback);
        getSupportActionBar().setTitle("Edit Address");

        state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(Edit_Address.this);
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
                adapter=new Stateadapter(statedata);
                close=(ImageView)dialog.findViewById(R.id.close);
                lv.setAdapter(adapter);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        statename = statedata.get(position).getState();
                        idd=statedata.get(position).getId();
                        citydata=db.getSelectedAllCity(String.valueOf(idd));
                        Log.e("citydata size",citydata.size()+"");
                        state.setText(statename);
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

        city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                cityvolley();
                if(idd!=0) {
                    final Dialog dialog = new Dialog(Edit_Address.this);
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
                            city.setText(cityname);
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
                    Toast.makeText(Edit_Address.this,"Please select state",Toast.LENGTH_SHORT).show();
                }
            }
        });

        Intent in=getIntent();
        pt=in.getStringExtra("parameter");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.status_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                Intent i1=new Intent(Edit_Address.this,Create_group.class);
                startActivity(i1);
                finish();
                break;
            case R.id.post:
                String add = address.getText().toString();
                String City=city.getText().toString();
                String State=state.getText().toString();
                String Country=country.getText().toString();
                if (add.trim().length() == 0 & City.trim().length()== 0 & State.trim().length() == 0 & Country.trim().length() == 0 & zipcode.getText().toString().trim().length()==0) {
                    Toast.makeText(Edit_Address.this, "Text should not be blank.", Toast.LENGTH_SHORT).show();
                    break;
                }
                else
                {
                    pref_edit=pref.edit();
                    if(pt.equals("addressimg"))
                        pref_edit.putString("address",add+","+Country);
                        pref_edit.putString("state",State);
                        pref_edit.putString("city",City);
                    pref_edit.putString("zipcode",zipcode.getText().toString());

                    pref_edit.commit();
                    address.setHint("Address");
                    city.setHint("City");
                    state.setHint("State");
                    zipcode.setHint("Zip Code");
                    Intent i=new Intent(Edit_Address.this,Create_group.class);
                    startActivity(i);
                    finish();
                }
                break;
        }
        return (super.onOptionsItemSelected(menuItem));
    }
    @Override
    public void onBackPressed() {
        Intent i=new Intent(Edit_Address.this,Create_group.class);
        startActivity(i);
        finish();
    }

//    public void statevolley()
//    {
//
//        statedata=new ArrayList<get_state>();
//
//        final ProgressDialog dialog = new ProgressDialog(Edit_Address.this);
//        dialog.setMessage("Loading..Please wait.");
////	     dialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
//        dialog.setCanceledOnTouchOutside(false);
////        dialog.show();
//
//        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
//        Log.e("url= ", "jjava.lang.String[]iii");
//        String url = HiHelloConstant.url+"fetch_state.php?cid=1";
//        Log.e("url= ", url);
//
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//
//            @Override
//            public void onResponse(JSONObject res) {
//                //dataobj = new ArrayList<get_setr>();
////                dialog.dismiss();
//                try {
//                    JSONArray jr = res.getJSONArray("name");
//
////                    Log.e("response=", res.getJSONArray("data").toString());
//
//
//
//                    int len = jr.length();
//
////                    Log.e("length=", String.valueOf(len));
//
//                    for (int i = 0; i < jr.length(); i++) {
//                        JSONObject jsonobj = jr.getJSONObject(i);           //String nom, String prenom, String email, String age, String deport, String actuel
//
////                        Log.e("aarray=", jr.toString());
//                        statedata.add(new get_state(jsonobj.getInt("id"), jsonobj.getString("cid"), jsonobj.getString("state")));
////                        Toast.makeText(getActivity(), jsonobj.getString("subcat") + "", Toast.LENGTH_LONG).show();
//
//                    }
//
//
//                } catch (JSONException e) {
//                    // TODO Auto-generated catch block
//                    Log.e("catch exp= ", e.toString());
//                    e.printStackTrace();
////                    dialog.dismiss();
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

//    public void cityvolley()
//    {
//
//        citydata=new ArrayList<get_city>();
//
//        final ProgressDialog dialog = new ProgressDialog(Edit_Address.this);
//        dialog.setMessage("Loading..Please wait.");
////	     dialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
//        dialog.setCanceledOnTouchOutside(false);
////        dialog.show();
//
//        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
//        Log.e("url= ", "jjava.lang.String[]iii");
//        String url = HiHelloConstant.url+"fetch_city.php?state_id="+String.valueOf(idd);
//        Log.e("url= ", url);
//
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//
//            @Override
//            public void onResponse(JSONObject res) {
//                //dataobj = new ArrayList<get_setr>();
////                dialog.dismiss();
//                try {
//                    JSONArray jr = res.getJSONArray("name");
//
////                    Log.e("response=", res.getJSONArray("data").toString());
//
//
//
//                    int len = jr.length();
//
////                    Log.e("length=", String.valueOf(len));
//
//                    for (int i = 0; i < jr.length(); i++) {
//                        JSONObject jsonobj = jr.getJSONObject(i);           //String nom, String prenom, String email, String age, String deport, String actuel
//
////                        Log.e("aarray=", jr.toString());
//                        citydata.add(new get_city(jsonobj.getInt("id"), jsonobj.getString("state_id"), jsonobj.getString("city")));
////                        Toast.makeText(getActivity(), jsonobj.getString("subcat") + "", Toast.LENGTH_LONG).show();
//
//                    }
//                    final Dialog dialog = new Dialog(Edit_Address.this);
//                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                    dialog.setContentView(R.layout.selectcategory);
//
//                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//                    Window window = dialog.getWindow();
//                    lp.copyFrom(window.getAttributes());
//
//                    //This makes the dialog take up the full width
//                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//                    window.setAttributes(lp);
//
//
//                    lv=(ListView)dialog.findViewById(R.id.listView3);
//                    cityadapter=new Cityadapter(citydata);
//                    close=(ImageView)dialog.findViewById(R.id.close);
//                    lv.setAdapter(cityadapter);
//                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            cityname=citydata.get(position).getCity();
//                            city.setText(cityname);
//                            dialog.dismiss();
//                        }
//                    });
//                    close.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            dialog.dismiss();
//                        }
//                    });
//                    dialog.show();
//
//                } catch (JSONException e) {
//                    // TODO Auto-generated catch block
//                    Log.e("catch exp= ", e.toString());
//                    e.printStackTrace();
////                    dialog.dismiss();
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

}
