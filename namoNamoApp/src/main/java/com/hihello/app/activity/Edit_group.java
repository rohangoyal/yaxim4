package com.hihello.app.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidquery.AQuery;
import com.hihello.app.R;
import com.hihello.app.adapter.Categoryadapter;
import com.hihello.app.adapter.Subcategoryadapter;
import com.hihello.app.constant.HiHelloConstant;
import com.hihello.app.db.DatabaseHandler;
import com.hihello.app.getset.get_category;
import com.hihello.app.getset.get_set_groups;
import com.hihello.app.getset.get_subcate;
import com.hihello.group.Group;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by delaine on 6/25/2016.
 */
public class Edit_group extends AppCompatActivity
{
    DatabaseHandler db;
    Uri selectedImage;
    int idd = 0;
    AQuery aquery;
    Bitmap photo;

    ArrayList<get_set_groups> data1;
    Categoryadapter adapter;
    Subcategoryadapter subadapter;
    ListView lv;
    ImageView close;

    public static final int REQUEST_CODE_GALLERY = 0x1;
    public static final int REQUEST_CODE_TAKE_PICTURE = 0x2;
    public static final int REQUEST_CODE_CROP_IMAGE = 0x3;

    private final static int ACTIVITY_PICK_IMAGE = 0;
    private final static int ACTIVITY_TAKE_PHOTO = 1;

    public static final int TAKE_PICTURE = 1;

    TextView Skip;
    private static int RESULT_LOAD_IMAGE = 2;
    private File imgFile;
    final int PIC_CROP = 4044;
    private Bitmap thePic;
    private String path, UploadFileName = "";

    public static String str = "true";
    ArrayList<get_category> catee;
    ArrayList<get_subcate> subcatee;
    CircleImageView round_image;
    SharedPreferences pref_name;
    SharedPreferences pref, pref_cate;
    SharedPreferences.Editor pref_edit, edit_cate;
    LinearLayout l_state, l_city, l_admin, l_group, l_about, l_alter, l_category, l_subcate, l_email, l_web, l_address, l_title;
    ImageView adminimg, groupimg, aboutimg, alternateimg, maincateimg, subcatimg, emailimg, websiteimg, stateimg, cityimg, next, addressimg, edit_wall, wall_image, camera, titleimg;
    String zipcode,admintxt, grouptxt, abouttxt, alternatetxt, maincatetxt, subcatetxt, emailtxt, websitetxt, statetxt, citytxt, addresstxt, titletxt;
    EditText  adminid, aboutid, alternatephoneid, maincateid, subcateid, emailid, websiteid, stateid, cityid, addressid, txtCamera, txtGallary, titleid;
    TextView submit,heading,groupid;
    Button btnCancelImage;
    String img1 = "", img2 = "",namee;
    String classname,admin,groupname,about,cate,subcate,altno,location,email,weburl,city,locstate,date,group_pic,admin_pic,name,follow,group_id,profile_name,admin_pich,title_id;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_group);

        pref = getSharedPreferences("pref", 1);
        pref_cate = getSharedPreferences("pref_cate", 1);
        pref_name=getSharedPreferences("pref_name", 1);
        name=pref_name.getString("username", "");
        com.hihello.app.common.Log.e("usernameee", name);
        db = new DatabaseHandler(getApplicationContext());
        catee = db.getAllCategory();
        Log.e("cateeee size", catee.size() + "");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_group);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.leftback);

        adminimg = (ImageView) findViewById(R.id.imageView3);
        groupimg = (ImageView) findViewById(R.id.imageView4);
        aboutimg = (ImageView) findViewById(R.id.imageView5);
        alternateimg = (ImageView) findViewById(R.id.imageView7);
        maincateimg = (ImageView) findViewById(R.id.imageView6);
        subcatimg = (ImageView) findViewById(R.id.imageView8);
        emailimg = (ImageView) findViewById(R.id.imageView9);
        websiteimg = (ImageView) findViewById(R.id.imageView10);
        stateimg = (ImageView) findViewById(R.id.imageView11);
        cityimg = (ImageView) findViewById(R.id.imageView12);
        addressimg = (ImageView) findViewById(R.id.imageView13);
        edit_wall = (ImageView) findViewById(R.id.imageView14);
        wall_image = (ImageView) findViewById(R.id.imageView);
        round_image = (CircleImageView) findViewById(R.id.imageView2);
        camera = (ImageView) findViewById(R.id.camera);
        titleimg = (ImageView) findViewById(R.id.titleimg);

        l_state = (LinearLayout) findViewById(R.id.linearLayout9);
        l_city = (LinearLayout) findViewById(R.id.linearLayout10);
        l_admin = (LinearLayout) findViewById(R.id.linearLayout);
        l_group = (LinearLayout) findViewById(R.id.linearLayout2);
        l_about = (LinearLayout) findViewById(R.id.linearLayout3);
        l_alter = (LinearLayout) findViewById(R.id.linearLayout4);
        l_category = (LinearLayout) findViewById(R.id.linearLayout5);
        l_subcate = (LinearLayout) findViewById(R.id.linearLayout6);
        l_email = (LinearLayout) findViewById(R.id.linearLayout7);
        l_web = (LinearLayout) findViewById(R.id.linearLayout8);
        l_address = (LinearLayout) findViewById(R.id.linearLayout11);
        l_title = (LinearLayout) findViewById(R.id.linearLayout13);

        adminid = (EditText) findViewById(R.id.adminid);
        groupid = (TextView) findViewById(R.id.groupid);
        aboutid = (EditText) findViewById(R.id.aboutusid);
        alternatephoneid = (EditText) findViewById(R.id.alternatephoneid);
        maincateid = (EditText) findViewById(R.id.maincateid);
        subcateid = (EditText) findViewById(R.id.subcategoryid);
        emailid = (EditText) findViewById(R.id.emailid);
        websiteid = (EditText) findViewById(R.id.websiteid);
        stateid = (EditText) findViewById(R.id.stateid);
        cityid = (EditText) findViewById(R.id.cityid);
        addressid = (EditText) findViewById(R.id.addressid);
        next = (ImageView) findViewById(R.id.next);
        submit = (TextView) findViewById(R.id.submitid);
        titleid = (EditText) findViewById(R.id.titleid);


        if (HiHelloConstant.pic1 != null & HiHelloConstant.pic2 != null) {
            wall_image.setImageBitmap(HiHelloConstant.pic1);
            round_image.setImageBitmap(HiHelloConstant.pic2);
            img1=HiHelloConstant.imgg1;
            img2=HiHelloConstant.imgg2;
            Log.e("imgg1",img1);
            Log.e("imgg2",img2);
        }

        Intent in=getIntent();
        admin=in.getStringExtra("admin");
        groupname=in.getStringExtra("group");
        about=in.getStringExtra("about");
        cate=in.getStringExtra("cate");
        subcate=in.getStringExtra("subcate");
        altno=in.getStringExtra("alt_mob");
        location=in.getStringExtra("address");
        email=in.getStringExtra("email");
        weburl=in.getStringExtra("weburl");
        Log.e("weburlll",weburl);
        city=in.getStringExtra("city");
        locstate=in.getStringExtra("state");
        date=in.getStringExtra("date");
        group_pic=in.getStringExtra("group_pic");
        admin_pic=in.getStringExtra("admin_pic");
        admin_pich=in.getStringExtra("admin_pich");
        follow=in.getStringExtra("follow");
        group_id=in.getStringExtra("group_id");
//        Toast.makeText(Edit_group.this,group_id+"",Toast.LENGTH_LONG).show();
        profile_name=in.getStringExtra("profile_name");
        title_id=in.getStringExtra("title");
        classname=in.getStringExtra("class");

//        if(classname.equals("group_admin"))
//        {
            adminid.setText(admin);
            groupid.setText(groupname);
            aboutid.setText(about);
            alternatephoneid.setText(altno);
            emailid.setText(email);
            websiteid.setText(weburl);
            l_state.setVisibility(View.VISIBLE);
            stateid.setText(locstate);
            l_city.setVisibility(View.VISIBLE);
            cityid.setText(city);
            addressid.setText(location);
            titleid.setText(title_id);
            maincateid.setText(cate);
            subcateid.setText(subcate);
            Log.e("admin_pic",HiHelloConstant.url + admin_pic);
            Log.e("group_pic",HiHelloConstant.url + group_pic);
            aquery=new AQuery(this,round_image);
            aquery.id(round_image).image(HiHelloConstant.url + admin_pic,true, true, 300,R.drawable.backheaderimage);
            aquery=new AQuery(this,wall_image);
            aquery.id(wall_image).image(HiHelloConstant.url + group_pic,true, true, 300,R.drawable.backheaderimage);
//        }

//        else {
//            admintxt = pref.getString("admin", "");
//            grouptxt = pref.getString("group", "");
//            abouttxt = pref.getString("about", "");
//            alternatetxt = pref.getString("alternatephone", "");
//            maincatetxt = pref.getString("main", "");
//            subcatetxt = pref.getString("sub", "");
//            emailtxt = pref.getString("email", "");
//            websitetxt = pref.getString("website", "");
//            statetxt = pref.getString("state", "");
//            citytxt = pref.getString("city", "");
//            zipcode = pref.getString("zipcode", "");
//            addresstxt = pref.getString("address", "");
//            titletxt = pref.getString("title", "");
//
////        abouttxt = abouttxt.replace(" ", "%20");
//
//            if (admintxt.length() > 0)
//                adminid.setText(admintxt);
//
//            if (grouptxt.length() > 0)
//                groupid.setText(grouptxt);
//
//            if (abouttxt.length() > 0)
//                aboutid.setText(abouttxt);
//
//            if (alternatetxt.length() > 0)
//                alternatephoneid.setText(alternatetxt);
//
////        if (maincatetxt.length() > 0)
////            maincateid.setText(maincatetxt);
//
////        if (subcatetxt.length() > 0)
////            subcateid.setText(subcatetxt);
//
//            if (emailtxt.length() > 0) {
//                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
//
//// onClick of button perform this simplest code.
//                if (emailtxt.matches(emailPattern)) {
//                    emailid.setText(emailtxt);
//                } else {
//                    Toast.makeText(getApplicationContext(), "Please enter valid email address", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//
//            if (websitetxt.length() > 0) {
//                Pattern p = Patterns.WEB_URL;
//                Matcher m = p.matcher(websitetxt);
//                if (m.matches())
//                    websiteid.setText(websitetxt);
//                else
//                    Toast.makeText(Edit_group.this, "Please enter valid website link", Toast.LENGTH_SHORT).show();
//            }
//
//
//            if (statetxt.length() > 0) {
//                l_state.setVisibility(View.VISIBLE);
//                stateid.setText(statetxt);
//            }
//
//            if (citytxt.length() > 0) {
//                l_city.setVisibility(View.VISIBLE);
//                cityid.setText(citytxt);
//            }
//
//            if (addresstxt.length() > 0)
//                addressid.setText(addresstxt);
//
//            if (titletxt.length() > 0)
//                titleid.setText(titletxt);
//
//
//            round_image.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Upload_Image();
//                    str = "false";
//                }
//            });
//
//
//            HiHelloConstant.catename = pref_cate.getString("category", "");
//            if (HiHelloConstant.catename.length() > 0) {
//                maincateid.setText(HiHelloConstant.catename);
//            }
//            HiHelloConstant.subcatename = pref_cate.getString("subcategory", "");
//            if (HiHelloConstant.subcatename.length() > 0) {
//                subcateid.setText(HiHelloConstant.subcatename);
//            }
//
//        }
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adminid.getText().toString().length()>0 & groupid.getText().toString().length()>0 & maincateid.getText().toString().length()>0 & subcateid.getText().toString().length()>0 & titleid.getText().toString().length()>0 & stateid.getText().toString().length()>0 & cityid.getText().toString().length()>0 & addressid.getText().toString().length()>0)
                {
                    citytxt=cityid.getText().toString();
                    citytxt=citytxt.replace(" ","%20");
                    alternatetxt=alternatephoneid.getText().toString();
                    alternatetxt=alternatetxt.replace(" ","%20");
                    statetxt=stateid.getText().toString();
                    statetxt=statetxt.replace(" ","%20");
                    websitetxt=websiteid.getText().toString();
                    websitetxt=websitetxt.replace(" ","%20");
                    emailtxt=emailid.getText().toString();
                    emailtxt=emailtxt.replace(" ","%20");
                    admintxt=adminid.getText().toString();
                    admintxt=admintxt.replace(" ","%20");
                    grouptxt=groupid.getText().toString();
                    grouptxt=grouptxt.replace(" ","%20");
                    titletxt=titleid.getText().toString();
                    titletxt = titletxt.replace(" ", "%20");
                    abouttxt=aboutid.getText().toString();
                    abouttxt = abouttxt.replace(" ", "%20");
                    maincatetxt = maincateid.getText().toString();
                    maincatetxt = maincatetxt.replace(" ", "%20");
                    subcatetxt = subcateid.getText().toString();
                    subcatetxt = subcatetxt.replace(" ", "%20");
                    addresstxt=addressid.getText().toString();
                    addresstxt=addresstxt.replace(" ","%20");
                    if (img1.equals("") & img2.equals("")) {
                        groupsvolley();
                    }
                    else if(img1.equals("") & img2 !=(""))
                    {
                        secondvolley();
                    }
                    else if(img1!=("") & img2.equals(""))
                    {
                        firstvolley();
                    }
                    else
                    {
                        volley();
                    }
//                uploadFile(uploadFileName);
                    pref_edit = pref.edit();
                    pref_edit.clear();
                    pref_edit.commit();
                    HiHelloConstant.catename = "";
                    HiHelloConstant.subcatename = "";
                    titleid.setHint("Title");
                    adminid.setHint("Admin Name");
                    groupid.setHint("Group/Org/Community");
                    aboutid.setHint("About us");
                    alternatephoneid.setHint("Alternate Mobile No");
                    maincateid.setHint("Select Main Category Type");
                    subcateid.setHint("Select Sub Category Type");
                    emailid.setHint("E-Mail Id");
                    websiteid.setHint("Website Url");
                    addressid.setHint("Address");
                    HiHelloConstant.pic1 = null;
                    HiHelloConstant.pic2 = null;
                    Toast.makeText(Edit_group.this, "Your community is update successfully. Please wait 5-24 hours.", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(Edit_group.this, Group.class);
                    startActivity(in);
                    finish();
//                    }
                }
                else
                {
                    Toast.makeText(Edit_group.this,"Please fill all fields",Toast.LENGTH_SHORT).show();
                }
            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adminid.getText().toString().length()>0 & groupid.getText().toString().length()>0 & maincateid.getText().toString().length()>0 & subcateid.getText().toString().length()>0 & titleid.getText().toString().length()>0 & stateid.getText().toString().length()>0 & cityid.getText().toString().length()>0 & addressid.getText().toString().length()>0)
                {
                    citytxt=cityid.getText().toString();
                    citytxt=citytxt.replace(" ","%20");
                    alternatetxt=alternatephoneid.getText().toString();
                    alternatetxt=alternatetxt.replace(" ","%20");
                    statetxt=stateid.getText().toString();
                    statetxt=statetxt.replace(" ","%20");
                    websitetxt=websiteid.getText().toString();
                    websitetxt=websitetxt.replace(" ","%20");
                    emailtxt=emailid.getText().toString();
                    emailtxt=emailtxt.replace(" ","%20");
                    admintxt=adminid.getText().toString();
                    admintxt=admintxt.replace(" ","%20");
                    grouptxt=groupid.getText().toString();
                    grouptxt=grouptxt.replace(" ","%20");
                    titletxt=titleid.getText().toString();
                    titletxt = titletxt.replace(" ", "%20");
                    abouttxt=aboutid.getText().toString();
                    abouttxt = abouttxt.replace(" ", "%20");
                    maincatetxt = maincateid.getText().toString();
                    maincatetxt = maincatetxt.replace(" ", "%20");
                    subcatetxt = subcateid.getText().toString();
                    subcatetxt = subcatetxt.replace(" ", "%20");
                    addresstxt=addresstxt+","+zipcode;
                    addresstxt=addresstxt.replace(" ","%20");
                    if (img1.equals("") & img2.equals("")) {
                        groupsvolley();
                    }
                    else if(img1.equals("") & img2 !=(""))
                    {
                        secondvolley();
                    }
                    else if(img1!=("") & img2.equals(""))
                    {
                        firstvolley();
                    }
                    else
                    {
                        volley();
                    }
                    pref_edit = pref.edit();
                    pref_edit.clear();
                    pref_edit.commit();
                    HiHelloConstant.catename = "";
                    HiHelloConstant.subcatename = "";
                    titleid.setHint("Title");
                    adminid.setHint("Admin Name");
                    groupid.setHint("Group/Org/Community");
                    aboutid.setHint("About us");
                    alternatephoneid.setHint("Alternate Mobile No");
                    maincateid.setHint("Select Main Category Type");
                    subcateid.setHint("Select Sub Category Type");
                    emailid.setHint("E-Mail Id");
                    websiteid.setHint("Website Url");
                    addressid.setHint("Address");
                    HiHelloConstant.pic1 = null;
                    HiHelloConstant.pic2 = null;
                    Toast.makeText(Edit_group.this, "Your community is update successfully. Please wait 5-24 hours.", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(Edit_group.this, HomeMain_activity.class);
                    startActivity(in);
//                    }
                }
                else
                {
                    Toast.makeText(Edit_group.this,"Please fill all fields",Toast.LENGTH_SHORT).show();
                }
            }
        });

        edit_wall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //loadingPopupForImg();
                Upload_Image();
                str = "true";
            }
        });


//        l_admin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent in = new Intent(Edit_group.this, Edit_groupname.class);
//                in.putExtra("parameter", "adminimg");
//                startActivity(in);
//                finish();
//            }
//        });
//
//        l_group.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent in = new Intent(Edit_group.this, Edit_groupname.class);
//                in.putExtra("parameter", "groupimg");
//                startActivity(in);
//                finish();
//            }
//        });
//
//        l_about.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent in = new Intent(Edit_group.this, Edit_groupname.class);
//                in.putExtra("parameter", "aboutimg");
//                startActivity(in);
//                finish();
//            }
//        });
//
//        l_alter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent in = new Intent(Edit_group.this, Edit_groupname.class);
//                in.putExtra("parameter", "alternateimg");
//                startActivity(in);
//                finish();
//            }
//        });

        l_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent in = new Intent(Create_group.this, Edit_groupname.class);
//                in.putExtra("parameter", "maincateimg");
//                startActivity(in);
                final Dialog dialog = new Dialog(Edit_group.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.selectcategory);

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                Window window = dialog.getWindow();
                lp.copyFrom(window.getAttributes());

                //This makes the dialog take up the full width
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                window.setAttributes(lp);


                lv = (ListView) dialog.findViewById(R.id.listView3);
                adapter = new Categoryadapter(catee);
                close = (ImageView) dialog.findViewById(R.id.close);
                lv.setAdapter(adapter);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        edit_cate = pref_cate.edit();
                        edit_cate.putString("category", catee.get(position).getCategory());
                        edit_cate.commit();

                        HiHelloConstant.catename = pref_cate.getString("category", "");

                        idd = catee.get(position).getId();
                        maincateid.setText(HiHelloConstant.catename);
                        subcatee = db.getSelectedAllSubCategory(String.valueOf(idd));
                        Log.e("subcateeee size", subcatee.size() + "");
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

        l_subcate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent in = new Intent(Create_group.this, Edit_groupname.class);
//                in.putExtra("parameter", "subcatimg");
//                startActivity(in);
//                subcategoryvolley();
                final Dialog dialog = new Dialog(Edit_group.this);
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
                subadapter = new Subcategoryadapter(subcatee);
                close = (ImageView) dialog.findViewById(R.id.close);
                lv.setAdapter(subadapter);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        edit_cate = pref_cate.edit();
                        edit_cate.putString("subcategory", subcatee.get(position).getSubcategory());
                        edit_cate.commit();

                        HiHelloConstant.subcatename = pref_cate.getString("subcategory", "");
//                            HiHelloConstant.subcatename=subcate.get(position).getSubcategory();
                        subcateid.setText(HiHelloConstant.subcatename);
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

//        l_email.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent in = new Intent(Edit_group.this, Edit_groupname.class);
//                in.putExtra("parameter", "emailimg");
//                startActivity(in);
//                finish();
//            }
//        });
//
//        l_web.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent in = new Intent(Edit_group.this, Edit_groupname.class);
//                in.putExtra("parameter", "websiteimg");
//                startActivity(in);
//                finish();
//            }
//        });
//
//        l_address.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent in = new Intent(Edit_group.this, Edit_Address.class);
//                in.putExtra("parameter", "addressimg");
//                startActivity(in);
//                finish();
//            }
//        });
//        l_title.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent in = new Intent(Edit_group.this, Edit_groupname.class);
//                in.putExtra("parameter", "titleimg");
//                startActivity(in);
//                finish();
//            }
//        });
//
//        titleimg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent in = new Intent(Edit_group.this, Edit_groupname.class);
//                in.putExtra("parameter", "titleimg");
//                startActivity(in);
//                finish();
//            }
//        });
//        adminimg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent in = new Intent(Edit_group.this, Edit_groupname.class);
//                in.putExtra("parameter", "adminimg");
//                startActivity(in);
//                finish();
//            }
//        });
//        groupimg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent in = new Intent(Edit_group.this, Edit_groupname.class);
//                in.putExtra("parameter", "groupimg");
//                startActivity(in);
//                finish();
//            }
//        });
//        aboutimg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent in = new Intent(Edit_group.this, Edit_groupname.class);
//                in.putExtra("parameter", "aboutimg");
//                startActivity(in);
//                finish();
//            }
//        });
//        alternateimg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent in = new Intent(Edit_group.this, Edit_groupname.class);
//                in.putExtra("parameter", "alternateimg");
//                startActivity(in);
//                finish();
//            }
//        });
        maincateimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(Edit_group.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.selectcategory);

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                Window window = dialog.getWindow();
                lp.copyFrom(window.getAttributes());

                //This makes the dialog take up the full width
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                window.setAttributes(lp);


                lv = (ListView) dialog.findViewById(R.id.listView3);
                adapter = new Categoryadapter(catee);
                close = (ImageView) dialog.findViewById(R.id.close);
                lv.setAdapter(adapter);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        edit_cate = pref_cate.edit();
                        edit_cate.putString("category", catee.get(position).getCategory());
                        edit_cate.commit();

                        HiHelloConstant.catename = pref_cate.getString("category", "");
                        idd = catee.get(position).getId();
                        maincateid.setText(HiHelloConstant.catename);
                        subcatee = db.getSelectedAllSubCategory(String.valueOf(idd));
                        Log.e("subcateeee size", subcatee.size() + "");
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
        subcatimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                subcategoryvolley();
                final Dialog dialog = new Dialog(Edit_group.this);
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
                subadapter = new Subcategoryadapter(subcatee);
                close = (ImageView) dialog.findViewById(R.id.close);
                lv.setAdapter(subadapter);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        edit_cate = pref_cate.edit();
                        edit_cate.putString("subcategory", subcatee.get(position).getSubcategory());
                        edit_cate.commit();

                        HiHelloConstant.subcatename = pref_cate.getString("subcategory", "");
//                            HiHelloConstant.subcatename=subcate.get(position).getSubcategory();
                        subcateid.setText(HiHelloConstant.subcatename);
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
//        emailimg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent in = new Intent(Edit_group.this, Edit_groupname.class);
//                in.putExtra("parameter", "emailimg");
//                startActivity(in);
//                finish();
//            }
//        });
//        websiteimg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent in = new Intent(Edit_group.this, Edit_groupname.class);
//                in.putExtra("parameter", "websiteimg");
//                startActivity(in);
//                finish();
//            }
//        });
//
//        addressimg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent in = new Intent(Edit_group.this, Edit_Address.class);
//                in.putExtra("parameter", "addressimg");
//                startActivity(in);
//                finish();
//            }
//        });

        wall_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //loadingPopupForImg();

                Upload_Image();
                str = "true";
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // loadingPopupForImg();
                Upload_Image();
                str = "false";
            }
        });


    }


    public  void Upload_Image()
    {

        final CharSequence[] items = {"Take Photo",
                "Choose from Gallery"
                ,"Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(Edit_group.this);
        builder.setTitle("Add Photo!");

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which) {


                    case 0:
                        Uri outputFileUri = Uri.fromFile(getTempFile(getApplicationContext()));
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

                        startActivityForResult(cameraIntent, ACTIVITY_TAKE_PHOTO);
                        break;

                    case 1:

                        SharedPreferences press =
                                getSharedPreferences("galclick",
                                        Context.MODE_PRIVATE);
                        final SharedPreferences.Editor e2 = press.edit();
                        e2.putString("values", "p1");
                        e2.commit();


                        Intent i = new Intent(
                                Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                        startActivityForResult(i, RESULT_LOAD_IMAGE);

                        break;


                    case 2:
                        dialog.dismiss();
                }


                dialog.dismiss();


            }
        });



        builder.show();


    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
//          Log.e("codeee", resultCode + "," + requestCode + "," + data);
//        Toast.makeText(Create_group.this,"hiiihello",Toast.LENGTH_LONG).show();
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
                && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            String url = data.getData().toString();
            Bitmap bitmap = null;
            InputStream is = null;
            if (url.startsWith("content://com.google.android.apps.photos.content")) {

                Uri myUri = Uri.parse(url);
                Log.e("url", myUri.getAuthority());
                picturePath = getImageUrlWithAuthority(getApplicationContext(),
                        myUri);




                Uri ur = Uri.parse(picturePath);
                String path = getRealPathFromURI(ur);
//                compressImage(ur);
                imgFile = new File(path);
                Log.e("url", picturePath);



            }

            else

                imgFile = new File(picturePath);

            try {
                // call the standard crop action intent (the user device may not
                // support it)
                Intent cropIntent = new Intent("com.android.camera.action.CROP");
                // indicate image type and Uri
                cropIntent.setDataAndType(Uri.fromFile(imgFile), "image/*");
                // set crop properties
                cropIntent.putExtra("crop", "true");
                // indicate aspect of desired crop
                cropIntent.putExtra("aspectX", 1);
                cropIntent.putExtra("aspectY", 1);
                // indicate output X and Y
                cropIntent.putExtra("outputX",500);
                cropIntent.putExtra("outputY",500);
                // retrieve data on return
                cropIntent.putExtra("return-data", true);

                Uri mCropImagedUri = Uri
                        .fromFile(getTempFile(getApplicationContext()));
                cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCropImagedUri);
                startActivityForResult(cropIntent, PIC_CROP);
            }
            // respond to users whose devices do not support the crop action
            catch (ActivityNotFoundException anfe) {
                // display an error message
                String errorMessage = "Whoops - your device doesn't support the crop action!";
                Toast toast = Toast.makeText(this, errorMessage,
                        Toast.LENGTH_SHORT);
                toast.show();
            }
        }

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ACTIVITY_PICK_IMAGE:

                    break;
                case ACTIVITY_TAKE_PHOTO:

                    try {

                        Log.e("inside", "crop");
                        // call the standard crop action intent (the user device may
                        // not support it)
                        Intent cropIntent = new Intent(
                                "com.android.camera.action.CROP");
                        // indicate image type and Uri
                        cropIntent.setDataAndType(
                                Uri.fromFile(getTempFile(getApplicationContext())),
                                "image/*");
                        // set crop properties
                        cropIntent.putExtra("crop", "true");
                        // indicate aspect of desired crop
                        cropIntent.putExtra("aspectX", 1);
                        cropIntent.putExtra("aspectY", 1);

                        cropIntent.putExtra("scale", true);

                        cropIntent.putExtra("outputX",500);
                        cropIntent.putExtra("outputY",500);

                        cropIntent
                                .putExtra(MediaStore.EXTRA_OUTPUT, Uri
                                        .fromFile(getFilePath(
                                                getApplicationContext(),
                                                "image.jpeg")));

                        // retrieve data on return
                        cropIntent.putExtra("return-data", true);

                        // start the activity - we handle returning in
                        // onActivityResult
                        startActivityForResult(cropIntent, 1221);
                    }
                    // respond to users whose devices do not support the crop action
                    catch (Exception anfe) {
                        // display an error message
                        String errorMessage = "Whoops - your device doesn't support the crop action!";
                        Toast toast = Toast.makeText(this, errorMessage,
                                Toast.LENGTH_SHORT);
                        toast.show();
                    }

                    break;
                default:
                    break;
            }
        }

        if (requestCode == 1221 && resultCode == RESULT_OK && null != data) {
            Bundle extras = data.getExtras();

            Log.e("Aaaa", data.toString());
            Uri selectedImage = data.getData();
            if (selectedImage == null) {
                thePic = extras.getParcelable("data");
            } else {

                Log.e("image", selectedImage.getPath());




                try {
                    thePic = MediaStore.Images.Media.getBitmap(
                            this.getContentResolver(), selectedImage);


                    if(thePic!=null)
                    {

                    }

                    if(str.equals("true"))
                    {
                        wall_image.setImageBitmap(thePic);
                        HiHelloConstant.pic1=thePic;
                    }
                    else
                    {
                        round_image.setImageBitmap(thePic);
                        HiHelloConstant.pic2=thePic;
//                        round_image.setDrawingCacheEnabled(true);
//                        Bitmap b = round_image.getDrawingCache();
//                        int w = b.getWidth();
//                        int h = b.getHeight();
//                        Log.e("width and height",w+"   "+h);
                    }

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    thePic.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream .toByteArray();

                    if(str.equals("true"))
                    {
                        img1 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        HiHelloConstant.imgg1=img1;
                        Log.e("base64",img1);
                    }
                    else
                    {
                        img2 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        HiHelloConstant.imgg2=img2;
//                        compressImage(img2);
                        Log.e("base64",img2);
                    }


//                    img1 = Base64.encodeToString(byteArray, Base64.DEFAULT);
//                    HiHelloConstant.imgg1=img1;
//                    Log.e("base64",img1);



                    // Log.e("base64",base64(thePic));
                    //new UploadPdf().execute(base64(),cno,"Image"+timeStamp + ".pdf",pref.getString("name2",""),myFile.getTotalSpace()+"","0","0","pdf",shared.getString("username",""),pref.getString("id",""),shared.getString("name1",""),"0");
                } catch (Exception e) {

                    Log.e("ex","e",e);

                    e.printStackTrace();
                }

            }

            File f = getFilePath(getApplicationContext(),
                    "image.jpeg");



            // final Bitmap thePic = extras.getParcelable("dat");




        }

        if (requestCode == PIC_CROP && resultCode == RESULT_OK && null != data) {
            Bundle extras = data.getExtras();




            Uri selectedImage = data.getData();

            Log.e("image", "IO");
            if (selectedImage == null) {
                thePic = extras.getParcelable("data");


                path=getFilePath(getApplicationContext(), "image.jpg").getAbsolutePath();

            }

            else {

                Log.e("image", selectedImage.getPath());
                try {
                    thePic = MediaStore.Images.Media.getBitmap(
                            this.getContentResolver(), selectedImage);


                    path= selectedImage.getPath();


                    if(thePic!=null)
                    {

                    }

                    if(str.equals("true"))
                    {
                        wall_image.setImageBitmap(thePic);
                        HiHelloConstant.pic1=thePic;
                    }
                    else
                    {
                        round_image.setImageBitmap(thePic);
                        HiHelloConstant.pic2=thePic;
                    }
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    thePic.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream .toByteArray();

                    if(str.equals("true"))
                    {
                        img1 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        HiHelloConstant.imgg1=img1;
                        Log.e("base64",img1);
                    }
                    else
                    {
                        img2 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        HiHelloConstant.imgg2=img2;
//                        compressImage(img2);
                        Log.e("base64",img2);
                    }

//                    Log.e("base64",img1);
                    //new UploadPdf().execute(base64(),cno,"Image"+timeStamp + ".pdf",username,myFile.getTotalSpace()+"","0","0","pdf",shared.getString("username",""),pref.getString("id",""),shared.getString("name1",""),"0");



                } catch (Exception e) {


                    Log.e("ui","e",e);


                }
            }

            // final Bitmap thePic = extras.getParcelable("dat");


        }




    }

    public static File getFilePath(Context context,String filename) {
        File path = new File(Environment.getExternalStorageDirectory(),
                "HiHello");
        if (!path.exists()) {
            path.mkdir();
        }
        return new File(path, filename);
    }



    private File savebitmap(String filename) {
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        OutputStream outStream = null;

        File file =  new File(extStorageDirectory, filename + ".png");
        if (file.exists()) {
            file.delete();
            file = new File(extStorageDirectory, filename + ".png");
            Log.e("file exist", "" + file + ",Bitmap= " + filename);
        }
        try {
            // make a new bitmap from your file
            Bitmap bitmap = BitmapFactory.decodeFile(file.getName());

            outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (Exception e) {

            Log.e("ae","e",e);
            e.printStackTrace();
        }
        Log.e("file", "" + file);
        return file;

    }

    private File getTempFile(Context context) {
        String fileName = "image.jpg";
        File path = new File(Environment.getExternalStorageDirectory(),
                "HiHello");
        if (!path.exists()) {
            path.mkdir();
        }
        return new File(path, fileName);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public static Uri writeToTempImageAndGetPathUri(Context inContext,
                                                    Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(
                inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    public static String getImageUrlWithAuthority(Context context, Uri uri) {
        InputStream is = null;
        if (uri.getAuthority() != null) {
            try {
                is = context.getContentResolver().openInputStream(uri);
                Bitmap bmp = BitmapFactory.decodeStream(is);
                return writeToTempImageAndGetPathUri(context, bmp).toString();
            } catch (Exception e) {
                Log.e("e", "e", e);

            } finally {
                try {
                    is.close();
                } catch (IOException e) {


                }
            }
        }
        return null;
    }
    public void firstvolley() {
        final ProgressDialog pd = new ProgressDialog(Edit_group.this);
        pd.setCancelable(true);
        pd.setMessage("Loading Please Wait");
        pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);

        pd.show();


        RequestQueue queue = Volley.newRequestQueue(Edit_group.this);
        String url;
//        titletxt = titletxt.replace(" ", "%20");
//        abouttxt = abouttxt.replace(" ", "%20");
//        maincatetxt = maincateid.getText().toString();
//        maincatetxt = maincatetxt.replace(" ", "%20");
//        subcatetxt = subcateid.getText().toString();
//        subcatetxt = subcatetxt.replace(" ", "%20");
//        addresstxt=addresstxt+","+zipcode;
//        addresstxt=addresstxt.replace(" ","%20");
        url = HiHelloConstant.url + "first_pic_groupupdate.php?admin_name=" + admintxt + "&group=" + grouptxt + "&about=" + abouttxt + "&alt_mobile=" + alternatetxt + "&category=" + maincatetxt + "&subcategory=" + subcatetxt + "&email=" + emailtxt + "&weburl=" + websitetxt + "&state=" + statetxt + "&city=" + citytxt + "&address=" + addresstxt + "&title=" + titletxt+"&profile_name="+name;
        url = url.replace(" ", "%20");
        Log.e("name", url);

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                Log.e("response", s);
                try {
                    joinvolley();
                }

                catch (Exception e)
                {
                    Log.e("e","e",e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("img1", img1);
//                params.put("img2", img2);
                Log.e("img11111", img1);
//                Log.e("img222222", img2);


                return params;
            }

        };

        int socketTimeout = 50000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        queue.add(request);
    }
    public void secondvolley() {
        final ProgressDialog pd = new ProgressDialog(Edit_group.this);
        pd.setCancelable(true);
        pd.setMessage("Loading Please Wait");
        pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);

        pd.show();


        RequestQueue queue = Volley.newRequestQueue(Edit_group.this);
        String url;
//        titletxt = titletxt.replace(" ", "%20");
//        abouttxt = abouttxt.replace(" ", "%20");
//        maincatetxt = maincateid.getText().toString();
//        maincatetxt = maincatetxt.replace(" ", "%20");
//        subcatetxt = subcateid.getText().toString();
//        subcatetxt = subcatetxt.replace(" ", "%20");
//        addresstxt=addresstxt+","+zipcode;
//        addresstxt=addresstxt.replace(" ","%20");
        url = HiHelloConstant.url + "second_pic_groupupdate.php?admin_name=" + admintxt + "&group=" + grouptxt + "&about=" + abouttxt + "&alt_mobile=" + alternatetxt + "&category=" + maincatetxt + "&subcategory=" + subcatetxt + "&email=" + emailtxt + "&weburl=" + websitetxt + "&state=" + statetxt + "&city=" + citytxt + "&address=" + addresstxt + "&title=" + titletxt+"&profile_name="+name;
        url = url.replace(" ", "%20");
        Log.e("name", url);

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                Log.e("response", s);
                try {
                    joinvolley();
                }

                catch (Exception e)
                {
                    Log.e("e","e",e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("img1", img1);
                params.put("img2", img2);
//                Log.e("img11111", img1);
                Log.e("img222222", img2);


                return params;
            }

        };

        int socketTimeout = 50000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        queue.add(request);
    }
    public void groupsvolley() {
        final ProgressDialog pd = new ProgressDialog(Edit_group.this);
        pd.setCancelable(true);
        pd.setMessage("Loading Please Wait");
        pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);

        pd.show();


        RequestQueue queue = Volley.newRequestQueue(Edit_group.this);
        String url;
//        titletxt = titletxt.replace(" ", "%20");
//        abouttxt = abouttxt.replace(" ", "%20");
//        maincatetxt = maincateid.getText().toString();
//        maincatetxt = maincatetxt.replace(" ", "%20");
//        subcatetxt = subcateid.getText().toString();
//        subcatetxt = subcatetxt.replace(" ", "%20");
//        addresstxt=addresstxt+","+zipcode;
//        addresstxt=addresstxt.replace(" ","%20");
        url = HiHelloConstant.url + "update_groups.php?admin_name=" + admintxt + "&group=" + grouptxt + "&about=" + abouttxt + "&alt_mobile=" + alternatetxt + "&category=" + maincatetxt + "&subcategory=" + subcatetxt + "&email=" + emailtxt + "&weburl=" + websitetxt + "&state=" + statetxt + "&city=" + citytxt + "&address=" + addresstxt + "&title=" + titletxt+"&profile_name="+name;
        url = url.replace(" ", "%20");
        Log.e("name", url);

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                Log.e("response", s);
                try {
                    joinvolley();
                }

                catch (Exception e)
                {
                    Log.e("e","e",e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        //{
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("img1", img1);
////                params.put("img2", img2);
//                Log.e("img11111", img1);
////                Log.e("img222222", img2);
//
//
//                return params;
//            }

//        };

        int socketTimeout = 50000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        queue.add(request);
    }
    public void volley() {
        final ProgressDialog pd = new ProgressDialog(Edit_group.this);
        pd.setCancelable(true);
        pd.setMessage("Loading Please Wait");
        pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);

        pd.show();


        RequestQueue queue = Volley.newRequestQueue(Edit_group.this);
        String url;

        url = HiHelloConstant.url + "update_group.php?admin_name=" + admintxt + "&group=" + grouptxt + "&about=" + abouttxt + "&alt_mobile=" + alternatetxt + "&category=" + maincatetxt + "&subcategory=" + subcatetxt + "&email=" + emailtxt + "&weburl=" + websitetxt + "&state=" + statetxt + "&city=" + citytxt + "&address=" + addresstxt + "&title=" + titletxt+"&profile_name="+name;
        url = url.replace(" ", "%20");
        Log.e("name", url);

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                Log.e("response", s);
                try {
                    joinvolley();
                }

                catch (Exception e)
                {
                    Log.e("e","e",e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("img1", img1);
                params.put("img2", img2);
                Log.e("img11111", img1);
                Log.e("img222222", img2);


                return params;
            }

        };

        int socketTimeout = 50000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        queue.add(request);
    }
    public void joinvolley()
    {
        final ProgressDialog dialog = new ProgressDialog(Edit_group.this);
        dialog.setMessage("Loading..Please wait.");
        dialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        dialog.setCanceledOnTouchOutside(false);
//        dialog.show();


        RequestQueue queue= Volley.newRequestQueue(Edit_group.this);

        String url1= HiHelloConstant.url+"fetch_joined.php?profile_name="+name;
        android.util.Log.e("url= ", url1);

        JsonObjectRequest request=new JsonObjectRequest(url1, null,new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject res)
            {
                data1=new ArrayList<get_set_groups>();

                try {
                    JSONArray jr=res.getJSONArray("name");
//                    dialog.dismiss();
                    android.util.Log.e("response=",res.getJSONArray("name").toString());

                    int len=jr.length();

                    android.util.Log.e("length=",String.valueOf(len));

//                    Toast.makeText(getActivity(),"running",Toast.LENGTH_SHORT).show();
                    for(int i=0;i<jr.length();i++)
                    {
                        JSONObject jsonobj=jr.getJSONObject(i);

                        android.util.Log.e("aarray=",jr.toString());

                        data1.add(new get_set_groups(jsonobj.getInt("id"), jsonobj.getString("admin_name"), jsonobj.getString("group_name"), jsonobj.getString("about"), jsonobj.getString("alt_mobile"), jsonobj.getString("category"), jsonobj.getString("subcategory"), jsonobj.getString("email"), jsonobj.getString("weburl"), jsonobj.getString("state"), jsonobj.getString("city"), jsonobj.getString("address"),jsonobj.getString("date"), jsonobj.getString("admin_thumb"), jsonobj.getString("group_thumb"), jsonobj.getString("title"), jsonobj.getString("comment"), jsonobj.getString("follow"), jsonobj.getString("profile_name"), jsonobj.getString("admin_pic"), jsonobj.getString("group_pic")));
                        db.insertjoingroup(jsonobj.getInt("id"), jsonobj.getString("admin_name"), jsonobj.getString("group_name"), jsonobj.getString("about"), jsonobj.getString("alt_mobile"), jsonobj.getString("category"), jsonobj.getString("subcategory"), jsonobj.getString("email"), jsonobj.getString("weburl"), jsonobj.getString("state"), jsonobj.getString("city"), jsonobj.getString("address"), jsonobj.getString("date"),jsonobj.getString("admin_pic"), jsonobj.getString("group_pic"), jsonobj.getString("title"), jsonobj.getString("comment"), jsonobj.getString("follow"), jsonobj.getString("profile_name"),jsonobj.getString("admin_thumb"), jsonobj.getString("group_thumb"));
//						id integer primary key,admin_name text ,group_name text,about text,alt_mobile text,category text,subcategory text,email text,weburl text,state text,city text,address text,date,text,admin_pic text,group_pic text,title text,comment text,follow text,profile_name text,admin_pich text,group_pich text
                    }


                    int si=db.getAllJoinGroup().size();
                    android.util.Log.e("All Join Group Size", si + "  "+name);
                    db.close();

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
//                    dialog.dismiss();
                    android.util.Log.e("catch exp= ", e.toString());
                    e.printStackTrace();
                }

            }

        }
                ,new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
//                dialog.dismiss();
                android.util.Log.e("error", arg0.toString());
            }
        });

        queue.add(request);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
//                Intent in=new Intent(Edit_group.this,HomeMain_activity.class);
//                startActivity(in);
                finish();
                break;

        }
        return (super.onOptionsItemSelected(menuItem));
    }

}
