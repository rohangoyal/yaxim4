package com.hihello.app.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hihello.app.R;
import com.hihello.app.adapter.countrylistadapter;
import com.hihello.app.constant.HiHelloConstant;


public class VerificationPhoneNo extends BaseActivity {

	ArrayAdapter<String> adapter1;
	Hashtable<String, String> countryISDCodes;
	ArrayList<String> country_list = new ArrayList<String>();
	ArrayList<String> country_code = new ArrayList<String>();
	ArrayList<String> country_lc = new ArrayList<String>();
	private EditText text_contact_no;
	private TextView btn_ok,countryspinner;
	int code = 0;
	String countryname,countrycode;
	countrylistadapter adapter;
	private EditText et_country_code;

	public TextView code1,ok;
	public String phone1;
	SharedPreferences pref;
	SharedPreferences.Editor editor;
	String contact;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.phoneregister);
		Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle("Phone Verification");
		text_contact_no = (EditText) findViewById(R.id.txt_first_name);
		et_country_code = (EditText) findViewById(R.id.et_country_code);
		btn_ok = (TextView) findViewById(R.id.btn_ok);
//		getActionBar().hide();
		text_contact_no.requestFocus();
//		btn_ok.setOnClickListener(ok_Click);
		countryspinner = (TextView) findViewById(R.id.countrySpinner);
		countryISDCodes = HiHelloConstant.getAllCountryCode();
		country_list.addAll(countryISDCodes.keySet());
		Collections.sort(country_list);


		for(int i=0;i<country_list.size();i++)
		{
		country_code.add(countryISDCodes.get(country_list.get(i)));
		Log.e("countrycode=",country_list.get(i)+" "+countryISDCodes.get(country_list.get(i)));
		
		}

		for(int i=0;i<country_list.size();i++)
		{
		country_lc.add(country_code.get(i)+"      "+country_list.get(i));	
		}

		btn_ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				contact=text_contact_no.getText().toString();
				Log.e("Contact",contact);
				pref =getSharedPreferences("MyPref", 1);
				editor = pref.edit();
				editor.putString("contact", contact);
				editor.commit();
//				Toast.makeText(getApplicationContext(),contact,Toast.LENGTH_SHORT).show();
				dialog1();
			}
		});
		
		countryspinner.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final Dialog dialog=new Dialog(VerificationPhoneNo.this);
				  dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);				
				   Rect displayRectangle = new Rect();
				   Window window = dialog.getWindow();
				   window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

				   // inflate and adjust layout
				   LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				   View layout1 = inflater.inflate(R.layout.countrylist, null);
				   layout1.setMinimumWidth((int) (displayRectangle.width() * 1.0f));
				   layout1.setMinimumHeight((int) (displayRectangle.height() * 1.0f));
				    dialog.setContentView(layout1);
				    
					  dialog.setCancelable(true);
				    ListView countrylist=(ListView)dialog.findViewById(R.id.countrylistid);
				    dialog.show();

					ImageView search=(ImageView)dialog.findViewById(R.id.nextarrowid);

					final EditText searchtext=(EditText)dialog.findViewById(R.id.searchtxt);
					searchtext.setVisibility(View.GONE);

					final TextView hheading=(TextView)dialog.findViewById(R.id.registerid);
					 adapter1 = new ArrayAdapter<String>(VerificationPhoneNo.this, R.layout.countrylistitem,R.id.textView1,country_lc);
					
					 countrylist.setAdapter(adapter1);
					 countrylist.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,View view, int position, long id) {
							// TODO Auto-generated method stub
							String str=(String)adapter1.getItem(position);
							Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
							dialog.cancel();
							String[] str1=str.split("    ");
							
							String code=str1[0];
							String countr=str1[1];
							Toast.makeText(getApplicationContext(), countr, Toast.LENGTH_LONG).show();
							et_country_code.setText(code);
							countryspinner.setText(countr);							
							//String str2=str.substring(start, end)
						}
					});
					 search.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							searchtext.setVisibility(View.VISIBLE);
							hheading.setVisibility(View.GONE);
							
						}
					});
					searchtext.addTextChangedListener(new TextWatcher() {
						
						@Override
						public void onTextChanged(CharSequence s, int start, int before, int count) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void beforeTextChanged(CharSequence s, int start, int count,
								int after) {
							// TODO Auto-generated method stub
							adapter1.getFilter().filter(s);
						}
						
						@Override
						public void afterTextChanged(Editable s) {
							// TODO Auto-generated method stub
							
						}
					});
			}
		});
		
	}

	public void dialog1() {


		final Dialog dialog=new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCancelable(true);
		dialog.setContentView(R.layout.stuffdownload);
//		dialog.getWindow().setLayout(450, 350);

		dialog.show();
		 //Effects.getInstance().init(this);
		TextView edit=(TextView)dialog.findViewById(R.id.editid);
		TextView ok=(TextView)dialog.findViewById(R.id.okid);
		TextView phone=(TextView)dialog.findViewById(R.id.textView2);

		phone.setText(contact);
		phone1=phone.toString();
		edit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(contact.length()==10 )
				{
					String ccode=et_country_code.getText().toString();
					Log.e("countrycode",ccode);
				Intent in=new Intent(getApplicationContext(),SmsVarifecation.class);
					in.putExtra("phone1", contact);
					finish();
					in.putExtra("ccode",ccode);
					startActivity(in);
				}
				else
				{
					Toast.makeText(getApplicationContext(), "Please Enter 10 Digit Mobile Number", Toast.LENGTH_LONG).show();
				}
			}
		});

	}

}
