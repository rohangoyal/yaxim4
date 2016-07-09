package com.hihello.app.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.hihello.app.R;
import com.hihello.app.activity.Group_comments;
import com.hihello.app.activity.Group_view;
import com.hihello.app.activity.Zoomimage;
import com.hihello.app.common.Log;
import com.hihello.app.constant.HiHelloConstant;
import com.hihello.app.constant.NetworkConnection;
import com.hihello.app.getset.get_allgroup;
import com.hihello.app.getset.get_set_groups;
import com.hihello.group.Group;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by rohan on 6/6/2016.
 */
public class Localgroupadapter extends BaseAdapter {
    View v;
    NetworkConnection nw;
    SharedPreferences pref_name;
    String comm,group,name;
    Context context;
    int width;
    ArrayList<get_set_groups> data;
    ArrayList<ArrayList<String>> listoflist;

    public Localgroupadapter(ArrayList<get_set_groups> data, int width) {
        this.data=data;
        this.width=width;
    }

//    public Localgroupadapter(ArrayList<get_allgroup> data,ArrayList<ArrayList<String>> listoflist,int width) {
//        this.data=data;
//        this.listoflist=listoflist;
//        this.width=width;
//    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        context=parent.getContext();
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        v=inflater.inflate(R.layout.localgroupadapter, parent, false);

//        Toast.makeText(context,data.get(position).getProfile_name()+","+data.get(position).getAlt_mob()+","+data.get(position).getGroup_pic(),Toast.LENGTH_LONG).show();
        Log.e("admin_name", data.get(position).getAdmin_name());
        Log.e("group_name",data.get(position).getGroup_name());
        Log.e("admin_pic",data.get(position).getAdmin_pic());
        Log.e("title",data.get(position).getTitle());
        Log.e("follower",data.get(position).getFollow());
        Log.e("Comment",data.get(position).getComment());
        Log.e("cate",data.get(position).getCate());
        Log.e("sub_cate",data.get(position).getSubcate());
        Log.e("about",data.get(position).getAbout());
        Log.e("email",data.get(position).getEmail());
        Log.e("weburl",data.get(position).getWeb_url());
        Log.e("state",data.get(position).getState());
        Log.e("city",data.get(position).getCity());
        Log.e("address",data.get(position).getAddress());
        Log.e("date",data.get(position).getDate());
        Log.e("alt_number",data.get(position).getAlt_mob());
        Log.e("group_pic",data.get(position).getGroup_pic());
        Log.e("profile_name",data.get(position).getProfile_name());
        Log.e("////////////////","////////////////////////////");


        pref_name=context.getSharedPreferences("pref_name", 1);
        name=pref_name.getString("username", "");
        Log.e("usernameee", name);
        nw=new NetworkConnection(context);
        final ViewHolder holder=new ViewHolder();
        holder.group_pic=(ImageView)v.findViewById(R.id.imageView11);
        holder.share=(LinearLayout)v.findViewById(R.id.sharelayout);
        holder.count=(TextView)v.findViewById(R.id.commentcount);
        holder.commentlayout=(LinearLayout)v.findViewById(R.id.llayout6);
        holder.title=(TextView)v.findViewById(R.id.title);
        holder.showcomment=(TextView)v.findViewById(R.id.commentshowid);
        holder.comment=(ImageView)v.findViewById(R.id.imageView12);
        holder.follower=(TextView)v.findViewById(R.id.textView11);
//        holder.txtcomment=(EditText)v.findViewById(R.id.commenttxt);
//        holder.submitcomment=(TextView)v.findViewById(R.id.commentsubmit);
        holder.shareicon=(ImageView)v.findViewById(R.id.imageView13);
        holder.admin_pic=(CircleImageView)v.findViewById(R.id.imageView1);
        holder.admin=(TextView)v.findViewById(R.id.textView6);
        holder.group_name=(TextView)v.findViewById(R.id.textView7);
        holder.admin.setText(data.get(position).getAdmin_name());
        holder.group_name.setText(data.get(position).getGroup_name());
        holder.title.setText(data.get(position).getAbout());
        holder.follower.setText(data.get(position).getProfile_name());



        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(width,width);
        holder.group_pic.setLayoutParams(parms);

        ArrayList<String> clist=new ArrayList<String>();
//        clist=listoflist.get(position);
//        for (int i=0;i<clist.size();i++)
//        if(clist.size()>0)
//        holder.showcomment.setText(clist.get(0));
//
//        blink(holder.showcomment, clist);

        holder.count.setText(data.get(position).getFollow());

        Picasso.with(context)
                .load(HiHelloConstant.url+data.get(position).getTitle())
                .into(holder.admin_pic);
        Picasso.with(context)
                .load(HiHelloConstant.url+data.get(position).getGroup_pic())
                .into(holder.group_pic);

        holder.shareicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context,"hello hiii",Toast.LENGTH_SHORT).show();
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("text/plain");
                    share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                    share.putExtra(Intent.EXTRA_SUBJECT, "Hi Hello");
                    share.putExtra(Intent.EXTRA_TEXT,
                            "Hi I am Join " + data.get(position).getGroup_name() + " Group and I am Using Hi Hello App.Download Hi Hello App And Get Free Recharge Every Time" + " " + "play.google.com/store/apps/details?id=com.hihello.app");
                    context.startActivity(Intent.createChooser(share, "Share link!"));

            }
        });

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context,"hello",Toast.LENGTH_SHORT).show();
                if(nw.isConnectingToInternet()==true) {
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("text/plain");
                    share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                    share.putExtra(Intent.EXTRA_SUBJECT, "Hi Hello");
                    share.putExtra(Intent.EXTRA_TEXT,
                            "Hi I am Join " + data.get(position).getGroup_name() + " Group and I am Using Hi Hello App.Download Hi Hello App And Get Free Recharge Every Time" + " " + "play.google.com/store/apps/details?id=com.hihello.app");
                    context.startActivity(Intent.createChooser(share, "Share link!"));
                }
                else
                    Toast.makeText(context, "Network Problem", Toast.LENGTH_LONG).show();





//                Intent share = new Intent(Intent.ACTION_SEND);
//                share.setType("text/plain");
//                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
//                share.putExtra(Intent.EXTRA_SUBJECT, "Hi Hello");
//                share.putExtra(Intent.EXTRA_TEXT,
//                        "Hi I am Join " + data.get(position).getGroup_name() + " Group and I am Using Hi Hello App.Download Hi Hello App And Get Free Recharge Every Time" + " " + "play.google.com/store/apps/details?id=com.hihello.app");
//                context.startActivity(Intent.createChooser(share, "Share link!"));
            }
        });
        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in=new Intent(context, Group_comments.class);
                in.putExtra("group_name",group=holder.group_name.getText().toString());
                in.putExtra("user_name",name);
                context.startActivity(in);



//                holder.commentlayout.setVisibility(View.VISIBLE);
//                final Dialog dialog = new Dialog(context);
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//
//                dialog.setContentView(R.layout.comment_dialog);
//                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//                Window window = dialog.getWindow();
//                lp.copyFrom(window.getAttributes());
//
//                //This makes the dialog take up the full width
//                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//                window.setAttributes(lp);
//
//                holder.txtcomment=(EditText)dialog.findViewById(R.id.commenttxt);
//                holder.submitcomment = (TextView) dialog.findViewById(R.id.commentsubmit);
//                holder.cancelcomment = (TextView) dialog.findViewById(R.id.commentcancel);
//
//
//                holder.submitcomment.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        comm=holder.txtcomment.getText().toString();
//                        group=holder.group_name.getText().toString();
//                        if(nw.isConnectingToInternet()==true) {
//                            submitvolley();
//                        }
//                        else
//                            Toast.makeText(context, "Network Problem", Toast.LENGTH_LONG).show();
//
//
//                        dialog.dismiss();
//                    }
//                });
//
//                holder.cancelcomment.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        dialog.dismiss();
//                    }
//                });
//
//
//                dialog.show();
            }
        });
//        holder.submitcomment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                comm=holder.txtcomment.getText().toString();
//                group=holder.group_name.getText().toString();
//                submitvolley();
//
//                holder.commentlayout.setVisibility(View.GONE);
//
//            }
//        });

        holder.admin_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(context, Group_view.class);
                in.putExtra("group_id",data.get(position).getId());
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
                in.putExtra("profile_name",data.get(position).getProfile_name());
                context.startActivity(in);
                ((Activity)context).finish();
            }
        });

        holder.group_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(context,Zoomimage.class);
                in.putExtra("pic",data.get(position).getGroup_pic());
                context.startActivity(in);
            }
        });

        return v;
    }
    static class ViewHolder
    {
        ImageView group_pic,comment,shareicon;
        CircleImageView admin_pic;
        LinearLayout share,commentlayout;
        TextView title,submitcomment,admin,group_name,count,showcomment,cancelcomment,follower;
        EditText txtcomment;
    }

    public void submitvolley()
    {

        final ProgressDialog pd = new ProgressDialog(context);
        pd.setCancelable(true);
        pd.setMessage("Loading Please Wait");
        pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);

        pd.show();

        RequestQueue queue= Volley.newRequestQueue(context);
        String url;

//        http://delainetechnologies.com/fructus/login.php?username=surenderkhowal91@gmail.com
        url= HiHelloConstant.url+"comment.php?group_name="+group+"&username="+name+"&comment="+comm;
        //       url="http://delainetechnologies.com/fructus/user.php?name="+st_name+"&email="+st_email+"&mobile="+st_phone+"&address="+st_add+"&dob="+st_dob;
        url=url.replace(" ","%20");
        android.util.Log.e("name", url);
        JsonObjectRequest jsonreq=new JsonObjectRequest( url, null,new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject arg0) {
                // TODO Auto-generated method stub

//   dialog.dismiss();
                android.util.Log.e("res", arg0.toString());


                try{
                    JSONArray jr = arg0.getJSONArray("name");
                    JSONObject jsonobj = jr.getJSONObject(0);
                    if(jsonobj.getString("scalar").equals("Inserted")==true)
                    {
                        pd.dismiss();
                        Toast.makeText(context, "Successfully Inserted", Toast.LENGTH_LONG).show();
                        Intent in=new Intent(context, Group.class);
                        context.startActivity(in);


                    }



                    else
                    {
                        pd.dismiss();
                        Toast.makeText(context, "Not done", Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    pd.dismiss();
                    android.util.Log.e("hi", e.getMessage());
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError e) {
                // TODO Auto-generated method stub

                pd.dismiss();
                android.util.Log.e("error", e.toString());
            }
        });
        queue.add(jsonreq);
    }


//    int i=0;
//    private void blink(final TextView txt, final ArrayList<String> clist){
//
//
//      ObjectAnimator  textColorAnim = ObjectAnimator.ofInt(txt, "textColor", Color.BLACK, Color.TRANSPARENT);
//
//        if(i<clist.size()) {
//
//            String CurrentString = clist.get(i);
//            String[] separated = CurrentString.split(">>");
//            separated[0]=separated[0].trim();
//            separated[1]=separated[1].trim();
//
//
//            String text = "<font color=#000000>"+separated[0]+"</font>"+" >> "+"<font color=#1E90FF>"+separated[1]+"</font>";
//
//
//
//            txt.setText(Html.fromHtml(text));
//
//            textColorAnim.setDuration(2500);
//            textColorAnim.setEvaluator(new ArgbEvaluator());
//            textColorAnim.setStartDelay(200);
//            textColorAnim.setRepeatCount(ValueAnimator.INFINITE);
//            textColorAnim.setRepeatMode(ValueAnimator.REVERSE);
//            textColorAnim.start();
//            i++;
//
//            final Handler handler = new Handler();
//
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    int timeToBlink = 1000;    //in milissegunds
//
//
//                    try {
//                        Thread.sleep(2500);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            //TextView txt = (TextView) findViewById(R.id.usage);
////                        if(txt.getVisibility() == View.VISIBLE){
////                            txt.setVisibility(View.INVISIBLE);
////                           // blink(txt, clist);
////                        }else{
////
////                            if(i<clist.size())
////                            {
////                                txt.setVisibility(View.VISIBLE);
////                                txt.setText(clist.get(i));
////                                i++;
////
////
////                            }
////                            else if(clist.size()>0)
////                            i=0;
////                        }
//                            blink(txt, clist);
//                            Log.e("blinkcalled",txt.getText().toString());
//                        }
//                    });
//                }
//            }).start();
//
//
//        }
//        else {
//            i = 0;
//            if(clist.size()>1)
//                blink(txt,clist);
//            else
//            {
//                textColorAnim.setDuration(2500);
//                textColorAnim.setEvaluator(new ArgbEvaluator());
//                textColorAnim.setStartDelay(500);
//                textColorAnim.setRepeatCount(ValueAnimator.INFINITE);
//                textColorAnim.setRepeatMode(ValueAnimator.REVERSE);
//                textColorAnim.start();
//            }
//        }
//
//
//    }
}
