package com.namonamo.app.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.namonamo.app.R;
import com.namonamo.app.activity.Creategroup;
import com.namonamo.app.activity.EditStatus;
import com.namonamo.app.activity.SettingPrefs;
import com.namonamo.app.adapter.firstlistadapter;
import com.namonamo.app.common.Log;
import com.namonamo.app.getset.get_setter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class firstlist extends Fragment {
    Context c;
    ListView list;
    firstlistadapter adp;
    ArrayList<get_setter> dataobj;
String wallimage,profileimage,contact, adminname,groupname,email,phone, landline, website, address,state,category, followers, viewers;
@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {
    setHasOptionsMenu(true);
    // Inflate the layout for this fragment
    View rootView = inflater.inflate(R.layout.firstlist, container, false);
//    list=(ListView)rootView.findViewById(R.id.listView1);
//		adp=new firstlistadapter();
    RequestQueue queue= Volley.newRequestQueue(getActivity());

    String url1="http://delainetechnologies.com/gyanProject/webservices/fetch.php";
    Log.e("url= ", url1);

    JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET,url1, null,new Response.Listener<JSONObject>() {

        @Override
        public void onResponse(JSONObject res)
        {
            dataobj=new ArrayList<get_setter>();

            try {
                JSONArray jr=res.getJSONArray("name");

                Log.e("response=",res.getJSONArray("name").toString());

                //Log.e("vacancy= ",jobarray.toString());
                //categoryarray=new ArrayList<String>();

                int len=jr.length();

                Log.e("length=",String.valueOf(len));

                for(int i=0;i<jr.length();i++)
                {
                    JSONObject jsonobj=jr.getJSONObject(i);

                    Log.e("aarray=",jr.toString());

                    dataobj.add(new get_setter(jsonobj.getString("wall_image"),jsonobj.getString("profile_image"), jsonobj.getString("app_contact"),jsonobj.getString("admin_name"),jsonobj.getString("group_name"),jsonobj.getString("email"),jsonobj.getString("phone"),jsonobj.getString("landline"),jsonobj.getString("url"),jsonobj.getString("address"),jsonobj.getString("state"),jsonobj.getString("category"),jsonobj.getString("followers"),jsonobj.getString("viewers")));
                }


                adp=new firstlistadapter(c,dataobj);

                adp.notifyDataSetChanged();
//                list.setAdapter(adp);

//                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

//                    @Override
//                    public void onItemClick(AdapterView<?> arg0, View view,int position, long arg3) {
//                        // TODO Auto-generated method stub
//                        //Integer i=view.getId();
//
//                        //Toast.makeText(getActivity(), Integer.toString(i),200).show();
//
//                        Intent in =new Intent(getActivity(),groupprofile.class);
//                        in.putExtra("wallimage",dataobj.get(position).getWallimage());
//                        in.putExtra("profileimage",dataobj.get(position).getProfileimage());
//                        in.putExtra("contact",dataobj.get(position).getContact()); // form fees
//                        in.putExtra("adminname",dataobj.get(position).getAdminname());// website
//                        in.putExtra("groupimage",dataobj.get(position).getGroupname());
//                        in.putExtra("email",dataobj.get(position).getEmail());
//                        in.putExtra("phone",dataobj.get(position).getPhone());
//                        in.putExtra("landline",dataobj.get(position).getLandline());
//                        in.putExtra("website",dataobj.get(position).getWebsite()); // form fees
//                        in.putExtra("address",dataobj.get(position).getAddress());// website
//                        in.putExtra("state",dataobj.get(position).getState());
//                        in.putExtra("category",dataobj.get(position).getCategory());
//                        in.putExtra("followers",dataobj.get(position).getFollowers());
//                        in.putExtra("viewers",dataobj.get(position).getViewers());
//
////							String g=dataobj.get(position).getComment();
//                        Log.e("hsgsgshgd", dataobj.get(position).getWallimage());
//
//                        startActivity(in);
//
//
//
//
//
//
//                    }
//                });
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                Log.e("catch exp= ", e.toString());
                e.printStackTrace();
            }

        }

    }
            ,new Response.ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError arg0) {

            Log.e("error", arg0.toString());
        }
    });

    queue.add(request);

//    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
////            Intent in =new Intent(getActivity(),AllContacts.class);
////            startActivity(in);
//        }
//    });
    return rootView;
}


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.recentgroup_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                break;
            case R.id.search:
                break;
            case R.id.groupChat:
                Intent in=new Intent(getActivity(), Creategroup.class);
                startActivity(in);
                break;
            case  R.id.action_newgroup:
                Intent in1 =new Intent(getActivity(),Creategroup.class);
                startActivity(in1);
                break;
            case  R.id.action_status:
                Intent in2 =new Intent(getActivity(),EditStatus.class);
                startActivity(in2);
                break;
            case  R.id.action_settings:
                Intent in3 =new Intent(getActivity(),SettingPrefs.class);
                startActivity(in3);
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
