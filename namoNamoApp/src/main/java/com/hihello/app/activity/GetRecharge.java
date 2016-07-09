package com.hihello.app.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hihello.app.R;
import com.hihello.app.apicall.GetRechargeApiCall;
import com.hihello.app.apicall.GetUserRewardApiCall;
import com.hihello.app.constant.HiHelloSharedPrefrence;
import com.hihello.app.constant.HiHelloConstant;
import com.hihello.app.content.RewardItem;
import com.hihello.app.service.Servicable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GetRecharge extends ActionBarActivity {


    int amt=0;
    String operators[]={"Aircel",
            "Airtel",
            "BSNL",
            "Idea",
            "MTNL",
            "MTS",
            "Reliance CDMA",
            "Reliance GSM",
            "Tata DOCOMO",
            "Tata Indicom",
            "Telenor",
            "Videocon",
            "Vodafone"};

    ArrayList<RewardItem> rewards = new ArrayList<RewardItem>();

    String type[]={"Andaman and Nicobar Islands",
            "Andhra Pradesh",
            "Arunachal Pradesh",
            "Assam",
            "Bihar",
            "Chandigarh",
            "Chhattisgarh",
            "Dadra and Nagar Haveli",
            "Daman and Diu",
            "National Capital Territory of Delhi",
            "Goa",
            "Gujarat",
            "Haryana",
            "Himachal Pradesh",
            "Jammu and Kashmir",
            "Jharkhand",
            "Karnataka",
            "Kerala",
            "Lakshadweep",
            "Madhya Pradesh",
            "Maharashtra",
            "Manipur",
            "Meghalaya",
            "Mizoram",
            "Nagaland",
            "Odisha",
            "Puducherry",
            "Punjab",
            "Rajasthan",
            "Sikkim",
            "Tamil Nadu",
            "Telangana",
            "Tripura",
            "Uttar Pradesh",
            "Uttarakhand",
            "West Bengal"};
ArrayAdapter adapter1,adapter2;
    TextView recharge_spinner,recharge_type;
    EditText amount,no;
    Button Submit;

    RadioGroup radio;


    private int totalReward;
    private Servicable.ServiceListener listener = new Servicable.ServiceListener() {

        @Override
        public void onComplete(Servicable<?> service) {
            TextView txt_message = (TextView) findViewById(R.id.txt_message);



            String response= ((GetUserRewardApiCall) service).getStringResponce();
            if(response!=null) {

                Log.e("response", response);

                try {
                    JSONObject data = new JSONObject(response);
                    String reward = data.getString("reward");

                    totalReward = Integer.parseInt(reward);

                    totalReward = totalReward / 10;

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressBar.setVisibility(View.GONE);

            }
        else
            {
                Toast.makeText(GetRecharge.this,"Please Check Network Connection",Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }







//				HiHelloNewConstant.reward=totalReward;
//				TextView txt_reward = (TextView) findViewById(R.id.txt_rewards);
//				txt_reward.setText(totalReward + "");
                text_reward.setText("Rs." + totalReward);

                if(totalReward>=50.0)
                {
                    recharge_spinner.setEnabled(true);
                    recharge_type.setEnabled(true);
                    amount.setEnabled(true);
                    Submit.setEnabled(true);
                    no.setEnabled(true);
                }



        }
    };

    private Servicable.ServiceListener listener1 = new Servicable.ServiceListener() {

        @Override
        public void onComplete(Servicable<?> service) {


                String response= ((GetRechargeApiCall) service).getStringResponce();
            Log.e("aaa", ((GetRechargeApiCall) service).getStringResponce());

            try {
                JSONObject js=new JSONObject(response);

                Toast.makeText(getApplicationContext(),js.getString("message"), Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressBar.setVisibility(View.GONE);

        }
    };
    private ProgressBar progressBar;
    private TextView text_reward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_recharge);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        text_reward=(TextView)toolbar.findViewById(R.id.txt_reward);
        radio=(RadioGroup)findViewById(R.id.group);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.leftback);

        recharge_spinner=(TextView)findViewById(R.id.spinner);

        recharge_spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(GetRecharge.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                Rect displayRectangle = new Rect();
                Window window = dialog.getWindow();
                window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

                // inflate and adjust layout
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View layout1 = inflater.inflate(R.layout.countrylist1, null);
                layout1.setMinimumWidth((int) (displayRectangle.width() * 1.0f));
                layout1.setMinimumHeight((int) (displayRectangle.height() * 1.0f));
                dialog.setContentView(layout1);

                dialog.setCancelable(true);
                ListView countrylist = (ListView) dialog.findViewById(R.id.countrylistid);
                dialog.show();

                ImageView search = (ImageView) dialog.findViewById(R.id.nextarrowid);




                adapter1 = new ArrayAdapter<String>(getApplicationContext(), R.layout.countrylistitem, R.id.textView1, operators);

                countrylist.setAdapter(adapter1);
                countrylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // TODO Auto-generated method stub
                        String str = (String) adapter1.getItem(position);
                        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
                        dialog.cancel();

                        recharge_spinner.setText(str);

                    }
                });


            }
        });

        fetchMyRewards();
        recharge_type=(TextView)findViewById(R.id.spinner1);
                recharge_type.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Dialog dialog=new Dialog(GetRecharge.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        Rect displayRectangle = new Rect();
                        Window window = dialog.getWindow();
                        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

                        // inflate and adjust layout
                        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View layout1 = inflater.inflate(R.layout.countrylist1, null);
                        layout1.setMinimumWidth((int) (displayRectangle.width() * 1.0f));
                        layout1.setMinimumHeight((int) (displayRectangle.height() * 1.0f));
                        dialog.setContentView(layout1);

                        dialog.setCancelable(true);
                        ListView countrylist=(ListView)dialog.findViewById(R.id.countrylistid);
                        dialog.show();

                        ImageView search=(ImageView)dialog.findViewById(R.id.nextarrowid);


                        final TextView hheading=(TextView)dialog.findViewById(R.id.registerid);
                        adapter2 = new ArrayAdapter<String>(getApplicationContext(), R.layout.countrylistitem,R.id.textView1,type);

                        countrylist.setAdapter(adapter2);
                        countrylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent,View view, int position, long id) {
                                // TODO Auto-generated method stub
                                String str=(String)adapter2.getItem(position);
                                Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
                                dialog.cancel();

                                recharge_type.setText(str);

                            }
                        });

                    }
                });

        amount=(EditText)findViewById(R.id.editText7);
        no=(EditText)findViewById(R.id.editText6);
        Submit=(Button)findViewById(R.id.textView23);

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(amount.getText().toString().length()>0) {
                   amt = Integer.parseInt(amount.getText().toString());
                }

                if(amount.length()<=0 || no.getText().length()!=10  )
                {
                    Toast.makeText(getApplicationContext(),"Please Fill All Fields",Toast.LENGTH_LONG).show();


                }


                else
                    if(Integer.parseInt(amount.getText().toString())>totalReward)
                    {
                        Toast.makeText(getApplicationContext(),"Please Enter Amount Less Then Your Wallet Available",Toast.LENGTH_LONG).show();
                    }

                 else
                        if(amt>100)
                {
                    Toast.makeText(getApplicationContext(),"Sorry,You can't recharge more than 100 Rs.",Toast.LENGTH_LONG).show();
                }

//                else
//                    if(recharge_spinner.getSelectedItemPosition()==0)
//                    {
//                        Toast.makeText(getApplicationContext(),"Please Select Your Operator",Toast.LENGTH_LONG).show();
//                    }
//
//                    else
//                    if(recharge_type.getSelectedItemPosition()==0)
//                    {
//                        Toast.makeText(getApplicationContext(),"Please Select Your Type",Toast.LENGTH_LONG).show();
//                    }
                else
                {
                    SharedPreferences info1 = getSharedPreferences("userinfo",
                            Context.MODE_PRIVATE);
                    progressBar = (ProgressBar) findViewById(R.id.progressBar);

                    progressBar.setVisibility(View.VISIBLE);
                    String name=info1.getString("USER_NAME", "");

                    RadioButton selected=(RadioButton)findViewById(radio.getCheckedRadioButtonId());

                    int am=Integer.parseInt(amount.getText().toString())*10;
                    GetRechargeApiCall getrc=new GetRechargeApiCall(no.getText().toString(),name,recharge_spinner.getText().toString(),String.valueOf(am), HiHelloSharedPrefrence.getUserId(getApplicationContext()),selected.getText().toString(),(String)recharge_type.getText().toString() );


                    getrc.runAsync(listener1);
                    finish();
                    Intent in=new Intent(GetRecharge.this, GetRecharge.class);
                        startActivity(in);

                }

            }
        });

        ArrayAdapter rech=new ArrayAdapter(getApplicationContext(),R.layout.spin_item,operators);
        ArrayAdapter rec_type=new ArrayAdapter(getApplicationContext(),R.layout.spin_item,type);

//        recharge_spinner.setAdapter(rech);
//        recharge_type.setAdapter(rec_type);

        setSupportActionBar(toolbar);

        recharge_spinner.setEnabled(false);
        recharge_type.setEnabled(false);
        amount.setEnabled(false);
        Submit.setEnabled(false);
        no.setEnabled(false);

    }
    private void fetchMyRewards() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        GetUserRewardApiCall api = new GetUserRewardApiCall(
                HiHelloSharedPrefrence.getUserId(getApplicationContext()));
        api.runAsync(listener);

    }
    public void send()
    {



//String text="mobile_no="+no.getText().toString()+"&operator_name="+(String)recharge_spinner.getText().toString()+"&circle="+(String)recharge_type.getText().toString()+"&amount="+amount.getText().toString()+"&mobile_number_type=Prepaid&user_name="+name+"nameuser_id=1";





        String url="http://geminibusiness.in/admin/index.php/api/other/rechargeRequest";
        Log.e("text", url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("text", response);

                        Toast.makeText(getApplicationContext(),"your recharge request has been successfully submitted. please wait for some time.",Toast.LENGTH_LONG).show();
                        amount.setText("");
                        no.setText("");
//                        Intent in=new Intent(GetRecharge.this, GetRecharge.class);
//                        startActivity(in);


                    }
                }

                ,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {



                    }


                }


        )



        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

               // String text="mobile_no="+no.getText().toString()+"&operator_name="+(String)recharge_spinner.getText().toString()+"&circle="+(String)recharge_type.getText().toString()+"&amount="+amount.getText().toString()+"&mobile_number_type=Prepaid&user_name="+name+"nameuser_id=1";

                SharedPreferences info1 = getSharedPreferences("userinfo",
                        Context.MODE_PRIVATE);
                String name=info1.getString("USER_NAME", "");




                Map<String,String> params = new HashMap<String, String>();
                params.put("mobile_no",no.getText().toString());
                params.put("operator_name",recharge_spinner.getText().toString());
                params.put("circle",(String)recharge_type.getText().toString() );
                params.put("amount",amount.getText().toString());
                params.put("user_name",name);
                params.put("user_id","11");
                return params;

            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("X-APPKEY", HiHelloConstant.X_APPKEY);
                params.put("X-DEVICE-ID", HiHelloConstant.DEVICE_ID);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.recharge ,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

if(item.getItemId()==	 android.R.id.home)
        finish();
        else {
//    Intent ins = new Intent(getApplicationContext(), MyWallet.class);
//    startActivity(ins);
}
        return super.onOptionsItemSelected(item);
    }
}
