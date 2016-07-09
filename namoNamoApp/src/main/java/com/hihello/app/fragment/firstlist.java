package com.hihello.app.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.hihello.androidclient.chat.ChatWindow2;
import com.hihello.app.R;
import com.hihello.app.activity.AllContacts;
import com.hihello.app.activity.Creategroup;
import com.hihello.app.activity.EditStatus;
import com.hihello.app.activity.Group_admin;
import com.hihello.app.activity.Group_view;
import com.hihello.app.activity.SettingPrefs;
import com.hihello.app.adapter.Localgroupadapter;
import com.hihello.app.adapter.firstlistadapter;
import com.hihello.app.adapter.groupadapter;
import com.hihello.app.common.Log;
import com.hihello.app.constant.HiHelloConstant;
import com.hihello.app.constant.NetworkConnection;
import com.hihello.app.db.DatabaseHandler;
import com.hihello.app.getset.get_createdgroup;
import com.hihello.app.getset.get_set_groups;
import com.hihello.app.getset.get_setter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class firstlist extends Fragment {
    Context c;
    NetworkConnection nw;
    ListView list;
    firstlistadapter adp;
    ArrayList<get_set_groups> data;
    String name;
    DatabaseHandler db;
    SharedPreferences pref_name;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.firstlist, container, false);
        db = new DatabaseHandler(getActivity());
        nw=new NetworkConnection(getActivity());
        pref_name=getActivity().getSharedPreferences("pref_name", 1);
        name=pref_name.getString("username", "");
        Log.e("usernameee", name);

        list=(ListView)rootView.findViewById(R.id.listView1);

        getgroup();

//        if(nw.isConnectingToInternet()==true) {
//            volley();
//        }
//        else
//        {
////            Toast.makeText(getActivity(), "Network Problem", Toast.LENGTH_LONG).show();if(db.getAllGroups().size()>0)
//            data = new ArrayList<get_set_groups>();
//            data = db.getAllUserGroup(name);
//            adp = new firstlistadapter(getActivity(),data,name);
//            list.setAdapter(adp);
//
//        }


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(name.equals(data.get(position).getProfile_name())) {
                    Intent in = new Intent(getActivity(), Group_admin.class);
                    in.putExtra("group_id", data.get(position).getId() + "");
                    android.util.Log.e("group_id", data.get(position).getId() + "");
                    in.putExtra("admin", data.get(position).getAdmin_name());
                    android.util.Log.e("admin", data.get(position).getAdmin_name());
                    in.putExtra("group", data.get(position).getGroup_name());
                    android.util.Log.e("group", data.get(position).getGroup_name());
                    in.putExtra("about", data.get(position).getAbout());
                    android.util.Log.e("about",  data.get(position).getAbout());
                    in.putExtra("alt_mob", data.get(position).getAlt_mob());
                    android.util.Log.e("alt_mob",  data.get(position).getAlt_mob());
                    in.putExtra("cate", data.get(position).getCate());
                    android.util.Log.e("cate",  data.get(position).getCate());
                    in.putExtra("subcate", data.get(position).getSubcate());
                    android.util.Log.e("subcate",  data.get(position).getSubcate());
                    in.putExtra("email", data.get(position).getEmail());
                    android.util.Log.e("email",  data.get(position).getEmail());
                    in.putExtra("weburl", data.get(position).getWeb_url());
                    android.util.Log.e("weburl",  data.get(position).getWeb_url());
                    in.putExtra("state", data.get(position).getState());
                    android.util.Log.e("state",  data.get(position).getState());
                    in.putExtra("city", data.get(position).getCity());
                    android.util.Log.e("city",   data.get(position).getCity());
                    in.putExtra("address", data.get(position).getAddress());
                    android.util.Log.e("address",  data.get(position).getAddress());
                    in.putExtra("admin_pic", data.get(position).getAdmin_pic());
                    android.util.Log.e("admin_pic",  data.get(position).getAdmin_pic()+"");
                    in.putExtra("group_pic", data.get(position).getGroup_pic());
                    android.util.Log.e("group_pic",  data.get(position).getGroup_pic());
                    in.putExtra("date", data.get(position).getDate());
                    android.util.Log.e("date",   data.get(position).getDate());
                    in.putExtra("follow", data.get(position).getFollow());
                    android.util.Log.e("follow",data.get(position).getFollow());
                    in.putExtra("title",data.get(position).getTitle());
                    android.util.Log.e("title",data.get(position).getTitle());
                    in.putExtra("admin_pich",data.get(position).getGroup_pich());
                    android.util.Log.e("admin_pich+group_pich",data.get(position).getGroup_pich());
                    in.putExtra("name_pich",name);

//                    Toast.makeText(getActivity(),data.get(position).getTitle(),Toast.LENGTH_SHORT).show();
//                    Log.e("group_pic", data.get(position).getGroup_pic());
//                    Log.e("admin_pic", data.get(position).getAdmin_pic());
                    in.putExtra("profile_name", data.get(position).getProfile_name());
                    in.putExtra("role","admin");
                    android.util.Log.e("profile_name",data.get(position).getProfile_name());
                    startActivity(in);
                }
                else
                {
                    Intent in = new Intent(getActivity(), Group_admin.class);
//                    in.putExtra("group_id", data.get(position).getId() + "");
//                    in.putExtra("admin", data.get(position).getAdmin_name());
//                    in.putExtra("group", data.get(position).getGroup_name());
//                    in.putExtra("about", data.get(position).getAbout());
//                    in.putExtra("alt_mob", data.get(position).getAlt_mob());
//                    in.putExtra("cate", data.get(position).getCate());
//                    in.putExtra("subcate", data.get(position).getSubcate());
//                    in.putExtra("email", data.get(position).getEmail());
//                    in.putExtra("weburl", data.get(position).getWeb_url());
//                    in.putExtra("state", data.get(position).getState());
//                    in.putExtra("city", data.get(position).getCity());
//                    in.putExtra("address", data.get(position).getAddress());
//                    in.putExtra("admin_pic", data.get(position).getAdmin_pic());
//                    in.putExtra("group_pic", data.get(position).getGroup_pic());
//                    in.putExtra("title",data.get(position).getTitle());
//                    in.putExtra("name_pich",name);
////                    Toast.makeText(getActivity(),data.get(position).getTitle(),Toast.LENGTH_SHORT).show();
//                    Log.e("group_pic", data.get(position).getGroup_pic());
//                    Log.e("admin_pic",data.get(position).getAdmin_pic());
//                    in.putExtra("date", data.get(position).getDate());
//                    in.putExtra("admin_pich",data.get(position).getGroup_pich());
//                    in.putExtra("follow", data.get(position).getFollow());
//                    in.putExtra("profile_name", data.get(position).getProfile_name());
                    in.putExtra("group_id", data.get(position).getId() + "");
                    android.util.Log.e("group_id", data.get(position).getId() + "");
                    in.putExtra("admin", data.get(position).getAdmin_name());
                    android.util.Log.e("admin", data.get(position).getAdmin_name());
                    in.putExtra("group", data.get(position).getGroup_name());
                    android.util.Log.e("group", data.get(position).getGroup_name());
                    in.putExtra("about", data.get(position).getAbout());
                    android.util.Log.e("about",  data.get(position).getAbout());
                    in.putExtra("alt_mob", data.get(position).getAlt_mob());
                    android.util.Log.e("alt_mob",  data.get(position).getAlt_mob());
                    in.putExtra("cate", data.get(position).getCate());
                    android.util.Log.e("cate",  data.get(position).getCate());
                    in.putExtra("subcate", data.get(position).getSubcate());
                    android.util.Log.e("subcate",  data.get(position).getSubcate());
                    in.putExtra("email", data.get(position).getEmail());
                    android.util.Log.e("email",  data.get(position).getEmail());
                    in.putExtra("weburl", data.get(position).getWeb_url());
                    android.util.Log.e("weburl",  data.get(position).getWeb_url());
                    in.putExtra("state", data.get(position).getState());
                    android.util.Log.e("state",  data.get(position).getState());
                    in.putExtra("city", data.get(position).getCity());
                    android.util.Log.e("city",   data.get(position).getCity());
                    in.putExtra("address", data.get(position).getAddress());
                    android.util.Log.e("address",  data.get(position).getAddress());
                    in.putExtra("admin_pic", data.get(position).getAdmin_pic());
                    android.util.Log.e("admin_pic",  data.get(position).getAdmin_pic()+"");
                    in.putExtra("group_pic", data.get(position).getGroup_pic());
                    android.util.Log.e("group_pic",  data.get(position).getGroup_pic());
                    in.putExtra("date", data.get(position).getDate());
                    android.util.Log.e("date",   data.get(position).getDate());
                    in.putExtra("follow", data.get(position).getFollow());
                    android.util.Log.e("follow",data.get(position).getFollow());
                    in.putExtra("title",data.get(position).getTitle());
                    android.util.Log.e("title",data.get(position).getTitle());
                    in.putExtra("admin_pich",data.get(position).getGroup_pich());
                    android.util.Log.e("admin_pich+group_pich",data.get(position).getGroup_pich());
                    in.putExtra("name_pich",name);
                    in.putExtra("role","joiner");
//                    Toast.makeText(getActivity(),data.get(position).getTitle(),Toast.LENGTH_SHORT).show();
//                    Log.e("group_pic", data.get(position).getGroup_pic());
//                    Log.e("admin_pic", data.get(position).getAdmin_pic());
                    in.putExtra("profile_name", data.get(position).getProfile_name());
//                    android.util.Log.e("profile_name",data.get(position).getProfile_name());
                    startActivity(in);
                }
            }
        });




        return rootView;
    }
    
    public void volley()
    {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading..Please wait.");
        dialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        dialog.setCanceledOnTouchOutside(false);
//        dialog.show();


        RequestQueue queue= Volley.newRequestQueue(getActivity());

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

//                    Toast.makeText(getActivity(),"running",Toast.LENGTH_SHORT).show();
                    for(int i=0;i<jr.length();i++)
                    {
                        JSONObject jsonobj=jr.getJSONObject(i);

                        Log.e("aarray=",jr.toString());

                        data.add(new get_set_groups(jsonobj.getInt("id"), jsonobj.getString("admin_name"), jsonobj.getString("group_name"), jsonobj.getString("about"), jsonobj.getString("alt_mobile"), jsonobj.getString("category"), jsonobj.getString("subcategory"), jsonobj.getString("email"), jsonobj.getString("weburl"), jsonobj.getString("state"), jsonobj.getString("city"), jsonobj.getString("address"),jsonobj.getString("date"), jsonobj.getString("admin_thumb"), jsonobj.getString("group_thumb"), jsonobj.getString("title"), jsonobj.getString("comment"), jsonobj.getString("follow"), jsonobj.getString("profile_name"), jsonobj.getString("admin_pic"), jsonobj.getString("group_pic")));
                    }


                    adp=new firstlistadapter(c,data,name);
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
public void getgroup()
{
    try {
        data=new ArrayList<get_set_groups>();
        data=db.getAllJoinGroup();

        adp=new firstlistadapter(c,data,name);
        list.setAdapter(adp);
        adp.notifyDataSetChanged();
        int d=db.getAllJoinGroup().size();
        android.util.Log.e("sizzzeeeeee",d+"");
//        adp.notifyDataSetChanged();
    }
    catch (Exception e)
    {
        Log.e("ex",e.getMessage()+"");
    }

}
    }
