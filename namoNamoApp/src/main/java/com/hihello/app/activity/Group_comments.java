package com.hihello.app.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.hihello.app.R;
import com.hihello.app.adapter.Commentadapter;
import com.hihello.app.constant.HiHelloConstant;
import com.hihello.app.constant.NetworkConnection;
import com.hihello.app.getset.get_comment;
import com.hihello.group.Group;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import github.ankushsachdeva.emojicon.EmojiconGridView;
import github.ankushsachdeva.emojicon.EmojiconsPopup;
import github.ankushsachdeva.emojicon.emoji.Emojicon;

/**
 * Created by rohan on 6/4/2016.
 */
public class Group_comments extends AppCompatActivity
{
    ArrayList<get_comment> data1;
    NetworkConnection nw;
    ListView lv;
    private EditText mChatInput = null;
    String user_name,group_name,comm;
    ImageView send,img_emoji;
    private EmojiconsPopup popup;
    String mob_no;
    public static String pic_url;
    Commentadapter commadapter;
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_comments);

        pref =getSharedPreferences("MyPref", 1);
        mob_no=pref.getString("contact", "");
        userpicvolley();
        lv=(ListView)findViewById(R.id.list);
        mChatInput=(EditText)findViewById(R.id.Chat_UserInput);
        nw=new NetworkConnection(getApplicationContext());
        send=(ImageView)findViewById(R.id.img_send_chat);
        img_emoji = (ImageView) findViewById(R.id.img_emoji);
        img_emoji.setOnClickListener(emojiClick);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.leftback);
        getSupportActionBar().setTitle("Group Comments");
        setUpEmojiKeyboard();
        Intent in=getIntent();
        user_name=in.getStringExtra("user_name");
        group_name=in.getStringExtra("group_name");

        if(nw.isConnectingToInternet()==true) {
            commentvolley();
        }
        else
            Toast.makeText(getApplicationContext(), "Check your network connection", Toast.LENGTH_LONG).show();


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comm=mChatInput.getText().toString();
                mChatInput.setText("");
                if(nw.isConnectingToInternet()==true) {
                    submitvolley();
                        }
                        else
                            Toast.makeText(getApplicationContext(), "Check your network connection", Toast.LENGTH_LONG).show();
            }
        });



    }

    public void submitvolley()
    {

        final ProgressDialog pd = new ProgressDialog(Group_comments.this);
        pd.setCancelable(true);
        pd.setMessage("Loading Please Wait");
        pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);

        pd.show();

        RequestQueue queue= Volley.newRequestQueue(Group_comments.this);
        String url;

//        http://delainetechnologies.com/fructus/login.php?username=surenderkhowal91@gmail.com
        url= HiHelloConstant.url+"comment.php?group_name="+group_name+"&username="+user_name+"&comment="+comm+"&pic_url="+pic_url;
        //       url="http://delainetechnologies.com/fructus/user.php?name="+st_name+"&email="+st_email+"&mobile="+st_phone+"&address="+st_add+"&dob="+st_dob;
        url=url.replace(" ", "%20");
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
                        Toast.makeText(getApplicationContext(), "Successfully Inserted", Toast.LENGTH_LONG).show();
                        commentvolley();
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
                android.util.Log.e("error", e.toString());
            }
        });
        int socketTimeout = 50000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonreq.setRetryPolicy(policy);
        queue.add(jsonreq);
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
        group_name=group_name.replace(" ","%20");
        String url = HiHelloConstant.url+"fetch_comment.php?group="+group_name;
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
                        data1.add(new get_comment(jsonobj.getInt("id"), jsonobj.getString("group_name"), jsonobj.getString("username"), jsonobj.getString("comment"),jsonobj.getString("pic_url"),jsonobj.getString("date")));
//                        Toast.makeText(getActivity(), jsonobj.getString("subcat") + "", Toast.LENGTH_LONG).show();
                    }

                    if(data1.size()>0) {
                        commadapter = new Commentadapter(getApplicationContext(),data1);
                        lv.setAdapter(commadapter);
                    }
                    else
                    {
                        Toast.makeText(Group_comments.this,"Comments are not here.Please add comments.",Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                Intent in1=new Intent(Group_comments.this,Group.class);
                startActivity(in1);
                finish();
                break;
            case R.id.home:
                Intent in=new Intent(Group_comments.this,Group.class);
                startActivity(in);
                finish();
                break;

        }

        return (super.onOptionsItemSelected(menuItem));
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
