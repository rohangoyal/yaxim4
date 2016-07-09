package com.hihello.app.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
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

import com.hihello.androidclient.chat.XMPPChatServiceAdapter;
import com.hihello.app.R;

import com.hihello.app.adapter.Messageadapter;
import com.hihello.app.adapter.Messagereciveradapter;
import com.hihello.app.constant.HiHelloConstant;
import com.hihello.app.constant.HiHelloNewConstant;
import com.hihello.app.constant.NetworkConnection;
import com.hihello.app.db.BlockUserDBService;
import com.hihello.app.db.DatabaseHandler;
import com.hihello.app.db.HiHelloContact;
import com.hihello.app.db.RecentChatDBService;
import com.hihello.app.getset.get_message;
import com.hihello.app.getset.get_messages;
import com.hihello.app.getset.get_set_groups;
import com.hihello.group.Group;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import github.ankushsachdeva.emojicon.EmojiconGridView;
import github.ankushsachdeva.emojicon.EmojiconsPopup;
import github.ankushsachdeva.emojicon.emoji.Emojicon;

/**
 * Created by rohan on 6/9/2016.
 */
public class Group_admin extends AppCompatActivity
{
    private static final int DELAY_NEWMSG = 400;
    private static final int CHAT_MSG_LOADER = 0;

    private static final int REQUEST_PICK_CONTACT = 100;
    private static final int REQUEST_AUDIO = 101;
    private static final int REQUEST_VIDEO = 102;
    private static final int REQUEST_LOCATION = 103;
    private static final int REQUEST_IMAGE = 104;
    private static final int REQUEST_CAMERA = 105;
    private static final int REQUEST_EDIT_CONTACT = 106;
    private static final int REQUEST_PICK_GALLERY = 107;
    private static final int REQUEST_AUDIO_RECORD = 108;
    String pic_url="",mob_no="";
    protected boolean needs_to_bind_unbind = false;
    private HiHelloContact hiHelloContact;

    ArrayList<get_message> msgdata;
    private ProgressDialog progress;

    protected boolean dataToShareComplete;


    private Uri mPhotoPath;
    private Uri mVideoPath;
    NetworkConnection nw;
    DatabaseHandler db;
    SharedPreferences pref_name;
    ImageView mSendButton,mRecordAudio,mCaptureImage,smile;
    Toolbar toolbar;
    CircularImageView userpic;
    TextView group_name;
    String user_name,admin_msg,role="";
    AQuery aquery;
    EditText mChatInput;
    ListView lv;
    ImageView img_emoji;
    EmojiconsPopup popup;
    Messagereciveradapter adaptor1;
    Messageadapter adaptor;
    ArrayList<get_messages> messages;
//    ArrayList<String> messages1;
    SharedPreferences pref;

    String name_pich,admin,title,admin_pich,groupname,about,cate,subcate,altno,location,email,weburl,city,locstate,date,group_pic,admin_pic,name,follow,group_id,profile_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_admin);
        nw=new NetworkConnection(getApplicationContext());
        db = new DatabaseHandler(this);
        pref_name=getSharedPreferences("pref_name", 1);
        user_name=pref_name.getString("username", "");
        pref =getSharedPreferences("MyPref", 1);
        mob_no=pref.getString("contact", "");

        mSendButton=(ImageView)findViewById(R.id.img_send_chat);
        mRecordAudio=(ImageView)findViewById(R.id.img_record_audio);
        mCaptureImage=(ImageView)findViewById(R.id.img_capture_image);
        smile=(ImageView)findViewById(R.id.img_emoji);
        mChatInput=(EditText)findViewById(R.id.Chat_UserInput);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//		getSupportActionBar().setHomeAsUpIndicator(R.drawable.leftback);

        userpic=(CircularImageView)toolbar.findViewById(R.id.conversation_contact_photo);
        group_name=(TextView)toolbar.findViewById(R.id.action_bar_title_1);
//        lastseen=(TextView)toolbar.findViewById(R.id.action_bar_title_2);


        userpicvolley();




        img_emoji = (ImageView)findViewById(R.id.img_emoji);
        img_emoji.setOnClickListener(emojiClick);
        lv=(ListView)findViewById(R.id.list);
        setUpEmojiKeyboard();
//        msgdata=new ArrayList<get_message>();
//        msgdata=db.getAllMessage();
//        int i=msgdata.size();
//        Log.e("message size", msgdata.size() + "");
//        if(i>0) {
//            adapter = new Messageadapter(msgdata);
//            lv.setAdapter(adapter);
//            Log.e("message size", msgdata.size() + "");
//        }

        Intent in=getIntent();
        admin=in.getStringExtra("admin");
//        Log.e("admin", admin);
        groupname=in.getStringExtra("group");
//        Log.e("groupname", groupname);
        about=in.getStringExtra("about");
//        Log.e("about", about);
        cate=in.getStringExtra("cate");
//        Log.e("cate", cate);
        subcate=in.getStringExtra("subcate");
//        Log.e("subcate", subcate);
        altno=in.getStringExtra("alt_mob");
//        Log.e("altno", altno);
        location=in.getStringExtra("address");
//        Log.e("location", location);
        email=in.getStringExtra("email");
//        Log.e("email", email);
        weburl=in.getStringExtra("weburl");
//        Log.e("weburlll", weburl);
        city=in.getStringExtra("city");
//        Log.e("city", city);
        locstate=in.getStringExtra("state");
//        Log.e("locstate", locstate);
        date=in.getStringExtra("date");
//        Log.e("date", date);
        group_pic=in.getStringExtra("group_pic");
//        Log.e("group_pic", group_pic);
        admin_pic=in.getStringExtra("admin_pic");
//        Log.e("admin_pic", admin_pic+"iuoui");
        follow=in.getStringExtra("follow");
//        Log.e("follow", follow);
        group_id=in.getStringExtra("group_id");
//        Log.e("group_id", group_id);
        title=in.getStringExtra("title");
//        Log.e("title", title);
        admin_pich=in.getStringExtra("admin_pich");
//        Log.e("admin_pich", admin_pich);

        profile_name=in.getStringExtra("profile_name");
//        Log.e("profile_name", profile_name);
        name_pich=in.getStringExtra("name_pich");
        role=in.getStringExtra("role");
//        Log.e("name_pich", name_pich);
//        Toast.makeText(getApplicationContext(),city+","+locstate+","+date+","+profile_name,Toast.LENGTH_LONG).show();
        group_name.setText(groupname);

        aquery = new AQuery(this, userpic);
        aquery.id(userpic).image(HiHelloConstant.url+admin_pic, true, true, 300, R.drawable.backheaderimage);

        userpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(Group_admin.this, Group_view.class);
                in.putExtra("group_id", group_id);
                in.putExtra("admin", admin);
                in.putExtra("group", groupname);
                in.putExtra("about", about);
                in.putExtra("alt_mob", altno);
                in.putExtra("cate", cate);
                in.putExtra("subcate", subcate);
                in.putExtra("email", email);
                in.putExtra("weburl", weburl);
                in.putExtra("state", locstate);
                in.putExtra("city", city);
                in.putExtra("address", location);
                in.putExtra("admin_pic", admin_pic);
                in.putExtra("group_pic", group_pic);
                in.putExtra("date", date);
                in.putExtra("follow", follow);
                in.putExtra("profile_name", profile_name);
                in.putExtra("title",title);
                in.putExtra("admin_pich",admin_pich);
                in.putExtra("name_pich",name_pich);
                startActivity(in);
                finish();
            }
        });

        messages=db.getGroupMessage(groupname);

        Log.e("messages",messages.size()+"");
//        if(user_name.equals(profile_name))
//        {

            adaptor=new Messageadapter(role,messages);
            lv.setAdapter(adaptor);
//        }
//        else
//        {
//            adaptor=new Messageadapter(messages);
//            lv.setAdapter(adaptor);
//            messages=db.getJoinerMessage(groupname);
//            adaptor=new Messageadapter(messages);
//            lv.setAdapter(adaptor);
//        }


        mChatInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mSendButton.setVisibility(View.GONE);
                mCaptureImage.setVisibility(View.GONE);
                mRecordAudio.setVisibility(View.GONE);
                if (mChatInput.getText().length() >0) {
                    mSendButton.setEnabled(true);
                    mSendButton.setVisibility(View.VISIBLE);
                } else {
                    mCaptureImage.setVisibility(View.VISIBLE);
                    mRecordAudio.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mChatInput.getText().toString().length()>0) {
                    if (user_name.equals(profile_name)) {
                        admin_msg = mChatInput.getText().toString();
                    Toast.makeText(Group_admin.this, admin_msg, Toast.LENGTH_SHORT).show();
                        msgvolley();
                    } else {
                        admin_msg = mChatInput.getText().toString();
                        joinervolley();
                    }
                }
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();


        if(user_name.equals(profile_name))
        {
            Log.e("user_name_admin",user_name+" "+name_pich);
            inflater.inflate(R.menu.groupadmin_menu, menu);
        }
        else
        {
            Log.e("user_name_follow",user_name+" "+name_pich);
            inflater.inflate(R.menu.groupfollwer_menu, menu);
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(user_name.equals(profile_name))
        {
            Intent intent = null;
            switch (item.getItemId()) {
                case R.id.send_media:
                    break;

                case R.id.send_contact:
                    intent = new Intent(Intent.ACTION_PICK,
                            ContactsContract.Contacts.CONTENT_URI);
                    startActivityForResult(intent, REQUEST_PICK_CONTACT);
                    break;
                case R.id.send_picture:
                    intent = new Intent(Intent.ACTION_GET_CONTENT, null);
                    intent.setType("image/*, video/*");
                    intent.putExtra("return-data", true);
                    startActivityForResult(intent, REQUEST_PICK_GALLERY);
                    break;
//            case R.id.send_photo:
//                imageCapture();
//                break;
                case R.id.send_audio:
                    intent = new Intent(Intent.ACTION_GET_CONTENT, null);
                    intent.setType("audio/*");
                    intent.putExtra("return-data", true);
                    startActivityForResult(intent, REQUEST_AUDIO);
                    break;
                case R.id.send_location:
                    intent = new Intent(Group_admin.this, LocationShare.class);
                    startActivityForResult(intent, REQUEST_LOCATION);
                    break;
                case R.id.send_video:
                    intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                    mVideoPath = getOutputMediaFileUri();
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mVideoPath);
                    intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
                    startActivityForResult(intent, REQUEST_VIDEO);
                    break;

                case R.id.query:
				Intent in=new Intent(Group_admin.this,joinerquery.class);
                    in.putExtra("user_name",profile_name);
                    in.putExtra("group_name",groupname);
				startActivity(in);
                    break;
//            case R.id.call_contact: {
//                String phone;
//                if (hiHelloContact != null) {
//                    phone = hiHelloContact.getMobile_number();
//                } else {
//                    phone = mUserScreenName;
//                }
//                String uri = "tel:" + phone;
//                intent = new Intent(Intent.ACTION_DIAL);
//                intent.setData(Uri.parse(uri));
//                startActivity(intent);
//            }
//            break;
//
//            case R.id.block:
//
//                if (isBlocked)
//                    doUnBlockContact();
//                else
//                    doBlockContact();
//
//                finish();
//
//                break;

//            case R.id.archive:
//
//                RecentChatDBService.removeRecentChat(
//                        ChatWindow.this,
//                        mWithJabberID);
//                finish();
//
//                break;

                case R.id.delete:
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(Group_admin.this);

                    // Setting Dialog Title
                    alertDialog.setTitle("Confirm Delete...");

                    // Setting Dialog Message
                    alertDialog.setMessage("Are you sure you want delete this community group and all data will be delete?");

                    // Setting Icon to Dialog
                    alertDialog.setIcon(R.drawable.dialog_delete);

                    // Setting Positive "Yes" Button
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int which) {

                            // Write your code here to invoke YES event
//                            Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();

                            if(nw.isConnectingToInternet()==true) {
                                deletevolley();
                            }
                            else
                                Toast.makeText(getApplicationContext(), "Check your network connection", Toast.LENGTH_LONG).show();
                        }
                    });

                    // Setting Negative "NO" Button
                    alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to invoke NO event
//                            Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();
                    break;
                case R.id.email:
                    break;
                case R.id.clear:

                    AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(Group_admin.this);

                    // Setting Dialog Title
                    alertDialog1.setTitle("Confirm Delete...");

                    // Setting Dialog Message
                    alertDialog1.setMessage("Are you sure you want to clear messages in this chat ?");

                    // Setting Icon to Dialog
                    alertDialog1.setIcon(R.drawable.dialog_delete);

                    // Setting Positive "Yes" Button
                    alertDialog1.setPositiveButton("CLEAR", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int which) {

                            // Write your code here to invoke YES event
//                            Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();

                            db.deleteGroupChat(groupname);
                            adaptor.notifyDataSetChanged();
                            adaptor=new Messageadapter("admin",messages);
                            lv.setAdapter(adaptor);
                            dialog.cancel();
                        }
                    });

                    // Setting Negative "NO" Button
                    alertDialog1.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to invoke NO event
//                            Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }
                    });

                    // Showing Alert Message
                    alertDialog1.show();
                    break;

//            case R.id.clear:
//
//                removeChatHistory(mWithJabberID);
//                RecentChatDBService.removeRecentChat(ChatWindow.this,mWithJabberID);
//
//                mChatAdapter = new ChatWindowAdapter(null, PROJECTION_FROM,
//                        PROJECTION_TO, mWithJabberID, mUserScreenName);
//                mListView.setAdapter(mChatAdapter);
//                break;

                case android.R.id.home:
                 finish();
                    break;
                case R.id.edit_group:
                    Intent in1=new Intent(Group_admin.this,Edit_group.class);
                    in1.putExtra("group_id", group_id);
                    in1.putExtra("admin", admin);
                    in1.putExtra("group", groupname);
                    in1.putExtra("about", about);
                    in1.putExtra("alt_mob", altno);
                    in1.putExtra("cate", cate);
                    in1.putExtra("subcate", subcate);
                    in1.putExtra("email", email);
                    in1.putExtra("weburl", weburl);
                    in1.putExtra("state", locstate);
                    in1.putExtra("city", city);
                    in1.putExtra("address", location);
                    in1.putExtra("admin_pic", admin_pic);
                    in1.putExtra("group_pic", group_pic);
                    in1.putExtra("date", date);
                    in1.putExtra("follow", follow);
                    in1.putExtra("profile_name", profile_name);
                    in1.putExtra("title",title);
                    in1.putExtra("admin_pich",admin_pich);
                    in1.putExtra("class","group_admin");
                    startActivity(in1);
                    break;
//            default:
//                return super.onOptionsItemSelected(item);
            }

        }
        else
        {
            switch (item.getItemId()) {
                case android.R.id.home:
                   finish();
                    break;
                case R.id.clear:

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(Group_admin.this);

                    // Setting Dialog Title
                    alertDialog.setTitle("Confirm Delete...");

                    // Setting Dialog Message
                    alertDialog.setMessage("Are you sure you want to clear messages in this chat ?");

                    // Setting Icon to Dialog
                    alertDialog.setIcon(R.drawable.dialog_delete);

                    // Setting Positive "Yes" Button
                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int which) {

                            // Write your code here to invoke YES event
//                            Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();

                            db.deleteJoinerChat(groupname);
                            adaptor.notifyDataSetChanged();
                            adaptor=new Messageadapter("joiner",messages);
                            lv.setAdapter(adaptor1);
                            dialog.cancel();
                        }
                    });

                    // Setting Negative "NO" Button
                    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to invoke NO event
//                            Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();
                    break;
                case R.id.exit:
                    if(nw.isConnectingToInternet()==true) {
                        exitvolley();
                    }
                    else
                        Toast.makeText(getApplicationContext(), "Check your network connection", Toast.LENGTH_LONG).show();
                  break;
            }
        }

        return true;
    }
    //    private void imageCapture() {
//        mPhotoPath = captureImage(REQUEST_CAMERA);
//    }
    @Override
    public void onBackPressed() {
        Intent in=new Intent(Group_admin.this,HomeMain_activity.class);
        startActivity(in);
        finish();
    }


//    private void setUserInput() {
//        Intent i = getIntent();
//        mChatInput = (EditText) findViewById(R.id.Chat_UserInput);
//        mChatInput.addTextChangedListener(this);
//        if (i.hasExtra(INTENT_EXTRA_MESSAGE)) {
//            mChatInput.setText(i.getExtras().getString(INTENT_EXTRA_MESSAGE));
//        }
//    }

    public void exitvolley()
    {
        final ProgressDialog dialog = new ProgressDialog(Group_admin.this);
        dialog.setMessage("Loading..Please wait.");
        dialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        RequestQueue queue= Volley.newRequestQueue(Group_admin.this);
        groupname=groupname.replace(" ","%20");
        String url1= HiHelloConstant.url+"exit_group.php?group="+groupname+"&mobile="+mob_no;
        android.util.Log.e("url= ", url1);

        JsonObjectRequest request=new JsonObjectRequest(url1, null,new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject res)
            {
                    dialog.dismiss();
                try {
                   if(res.getString("scalar").equals("success"))
                   {
                       groupname=groupname.replace("%20"," ");
                       db.deleteGroup(groupname);
                       db.close();
                       Intent in=new Intent(Group_admin.this,HomeMain_activity.class);
                       startActivity(in);
                   }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    dialog.dismiss();
                    android.util.Log.e("catch exp= ", e.toString());
                    e.printStackTrace();
                }

            }

        }
                ,new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                dialog.dismiss();
                android.util.Log.e("error", arg0.toString());
            }
        });

        queue.add(request);
    }






public void joinervolley()
{
    final ProgressDialog pd = new ProgressDialog(Group_admin.this);
    pd.setCancelable(true);
    pd.setMessage("Loading Please Wait");
    pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);

//        pd.show();
    RequestQueue queue= Volley.newRequestQueue(Group_admin.this);
    String url;

    url= HiHelloConstant.url + "joiner.php?group_name="+groupname+"&joiner_pic="+pic_url+"&message="+admin_msg+"&joiner_name="+user_name;
    url=url.replace(" ","%20");
    Log.e("bookurl", url);
    JsonObjectRequest jsonreq=new JsonObjectRequest(url, null,new Response.Listener<JSONObject>() {

        @Override
        public void onResponse(JSONObject res) {
            // TODO Auto-generated method stub

//                pd.dismiss();

            Log.e("loginres", res.toString());

            try {

                String joiner_name=res.getString("joiner_name");
                String messg=res.getString("message");
                String g_name=res.getString("groupname");
                String joiner_pic=res.getString("joiner_pic");
                String type=res.getString("Type");
                Log.e("total message",joiner_name+" "+messg+" "+g_name+" "+type);

                messages.add(new get_messages(mChatInput.getText().toString(),type));
//                adaptor.notifyDataSetChanged();
                db.insertgroupchat("", g_name, messg,type,joiner_name,joiner_pic);
//                db.insertjoinerchat(g_name,joiner_name, messg,joiner_pic,type);
                mChatInput.setText("");


            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("exceptionlogin",e.toString());
//                    pd.dismiss();
            }
        }
    }, new Response.ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError e) {
            // TODO Auto-generated method stub

//                pd.dismiss();
            Toast.makeText(getApplicationContext(),"Server Error", Toast.LENGTH_LONG).show();
            Log.e("error", e.toString());
        }
    });
    queue.add(jsonreq);
}
    public void msgvolley()
    {
        final ProgressDialog pd = new ProgressDialog(Group_admin.this);
        pd.setCancelable(true);
        pd.setMessage("Loading Please Wait");
        pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);

//        pd.show();
        RequestQueue queue= Volley.newRequestQueue(Group_admin.this);
        String url;

        url= HiHelloConstant.url + "message.php?group_id="+group_id+"&admin_name="+profile_name+"&message="+admin_msg;
        url=url.replace(" ","%20");
        Log.e("bookurl", url);
        JsonObjectRequest jsonreq=new JsonObjectRequest(url, null,new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject res) {
                // TODO Auto-generated method stub

//                pd.dismiss();

                Log.e("loginres", res.toString());

                try {

                    String g_id=res.getString("groupid");
                    String messg=res.getString("message");
                    String g_name=res.getString("groupname");
                    String type=res.getString("Type");
                    Log.e("total message",g_id+" "+messg+" "+g_name+" "+type);

                    messages.add(new get_messages(mChatInput.getText().toString(),type));
//                    messages.add(mChatInput.getText().toString());
                    adaptor.notifyDataSetChanged();
                    db.insertgroupchat(g_id, g_name, messg,type,"","");
                    mChatInput.setText("");


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("exceptionlogin",e.toString());
//                    pd.dismiss();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError e) {
                // TODO Auto-generated method stub

//                pd.dismiss();
                Toast.makeText(getApplicationContext(),"Server Error", Toast.LENGTH_LONG).show();
                Log.e("error", e.toString());
            }
        });
        queue.add(jsonreq);
    }
    private View.OnClickListener emojiClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            if (!popup.isShowing()) {

                // If keyboard is visible, simply show the emoji popup
                if (popup.isKeyBoardOpen()) {
                    popup.showAtBottom();
                    img_emoji.setImageResource(R.drawable.ic_action_keyboard);
                }

                // else, open the text keyboard first and immediately after that
                // show the emoji popup
                else {
                    mChatInput.setFocusableInTouchMode(true);
                    // mChatInput.requestFocus();
                    popup.showAtBottomPending();
                    final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.showSoftInput(mChatInput,
                            InputMethodManager.SHOW_IMPLICIT);
                    img_emoji.setImageResource(R.drawable.ic_action_keyboard);
                }
            }

            // If popup is showing, simply dismiss it to show the undelying text
            // keyboard
            else {
                popup.dismiss();
            }
        }
    };

    private void setUpEmojiKeyboard() {
        popup = new EmojiconsPopup(findViewById(R.id.root_view), this);
        popup.setSizeForSoftKeyboard();
        popup.setOnEmojiconClickedListener(new EmojiconGridView.OnEmojiconClickedListener() {

            @Override
            public void onEmojiconClicked(Emojicon emojicon) {
                mChatInput.append(emojicon.getEmoji());
            }
        });

        // Set on backspace click listener
        popup.setOnEmojiconBackspaceClickedListener(new EmojiconsPopup.OnEmojiconBackspaceClickedListener() {

            @Override
            public void onEmojiconBackspaceClicked(View v) {
                KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0,
                        0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
                mChatInput.dispatchKeyEvent(event);
            }
        });
        // If the emoji popup is dismissed, change emojiButton to smiley icon
        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                img_emoji.setImageResource(R.drawable.smiley);
            }
        });

        // If the text keyboard closes, also dismiss the emoji popup
        popup.setOnSoftKeyboardOpenCloseListener(new EmojiconsPopup.OnSoftKeyboardOpenCloseListener() {

            @Override
            public void onKeyboardOpen(int keyBoardHeight) {

            }

            @Override
            public void onKeyboardClose() {
                if (popup.isShowing())
                    popup.dismiss();
            }
        });

        img_emoji = (ImageView) findViewById(R.id.img_emoji);
        img_emoji.setOnClickListener(emojiClick);

    }







    private void setSendButton() {
        mSendButton = (ImageView) findViewById(R.id.img_send_chat);
        mCaptureImage = (ImageView) findViewById(R.id.img_capture_image);
        mRecordAudio = (ImageView) findViewById(R.id.img_record_audio);

//        View.OnClickListener onSend = getOnSetListener();
//        mSendButton.setOnClickListener(onSend);
//        mCaptureImage.setOnClickListener(onCapture);
//        mRecordAudio.setOnClickListener(onRecorder);
//        mSendButton.setEnabled(false);

    }
    private static Uri getOutputMediaFileUri() {
        return Uri.fromFile(getOutputMediaFile());
    }

    private static File getOutputMediaFile() {
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "MyCameraVideo");
        mediaStorageDir.mkdirs();
        Date date = new Date();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date
                .getTime());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "VID_" + timeStamp + ".mp4");
        return mediaFile;
    }


    public void deletevolley()
    {

        final ProgressDialog pd = new ProgressDialog(Group_admin.this);
        pd.setCancelable(true);
        pd.setMessage("Loading Please Wait");
        pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);

        pd.show();

        RequestQueue queue= Volley.newRequestQueue(Group_admin.this);
        String url;

//        http://delainetechnologies.com/fructus/login.php?username=surenderkhowal91@gmail.com
        url= HiHelloConstant.url+"delete_group.php?group="+groupname;
        //       url="http://delainetechnologies.com/fructus/user.php?name="+st_name+"&email="+st_email+"&mobile="+st_phone+"&address="+st_add+"&dob="+st_dob;
        url=url.replace(" ", "%20");
        android.util.Log.e("name", url);
        JsonObjectRequest jsonreq=new JsonObjectRequest( url, null,new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject res) {
                // TODO Auto-generated method stub

//   dialog.dismiss();
                android.util.Log.e("loginres", res.toString());


                try {

//                    String g_id=res.getString("groupid");
                    String messg=res.getString("message");
                    String g_name=res.getString("groupname");
                    android.util.Log.e("total message",messg+" "+g_name);
                    Toast.makeText(Group_admin.this,messg+" "+g_name, Toast.LENGTH_SHORT).show();
//                    JSONArray jr = res.getJSONArray("name");
//                    JSONObject jsonobj = jr.getJSONObject(0);
                    if(g_name.equals(groupname))
                    {
                        pd.dismiss();
                        db.deleteGroup(groupname);
                        Toast.makeText(getApplicationContext(), "Successfully Delete", Toast.LENGTH_LONG).show();
                        Intent in=new Intent(Group_admin.this,Group.class);
                        startActivity(in);

                    }



                    else
                    {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), "Not done", Toast.LENGTH_LONG).show();

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
                if(e.toString().equals("com.android.volley.ParseError: org.json.JSONException: Value null of type org.json.JSONObject$1 cannot be converted to JSONObject"))
                {
                    Toast.makeText(Group_admin.this,"Server Error",Toast.LENGTH_LONG).show();
                }
                android.util.Log.e("error", e.toString());
            }
        });
        int socketTimeout = 50000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonreq.setRetryPolicy(policy);
        queue.add(jsonreq);
    }
    public void userpicvolley()
    {

        final ProgressDialog dialog = new ProgressDialog(getApplicationContext());
        dialog.setMessage("Loading..Please wait.");
//	     dialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        dialog.setCanceledOnTouchOutside(false);
//        dialog.show();

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        android.util.Log.e("url= ", "jjava.lang.String[]iii");
        mob_no="91"+mob_no;
        String url = HiHelloConstant.url+"profile_pic.php?mobile="+mob_no;
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
//                        data1.add(new get_comment(jsonobj.getInt("id"), jsonobj.getString("group_name"), jsonobj.getString("username"), jsonobj.getString("comment")));
//                        Toast.makeText(getActivity(), jsonobj.getString("subcat") + "", Toast.LENGTH_LONG).show();
                        pic_url=jsonobj.getString("pic_url");
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
}
