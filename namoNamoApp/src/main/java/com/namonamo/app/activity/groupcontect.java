package com.namonamo.app.activity;



import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.namonamo.app.R;

public class groupcontect extends Fragment implements OnClickListener
{
	 public static EditText mobile;
	public static EditText landline;
	public static EditText email;
	public static EditText website;
	public static EditText streetadd;
	
	public static EditText cityadd;
	
	
	TextView street,zip,city;
	ImageView pencil,plus;
	LinearLayout stret,zcode,ccity;
	Boolean b=false;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		  View view =inflater.inflate(R.layout.groupcontect,container,false);
		  street=(TextView)view.findViewById(R.id.sstreet);
		 
		  city=(TextView)view.findViewById(R.id.ccityy);
		  pencil=(ImageView)view.findViewById(R.id.pencil);
		  plus=(ImageView)view.findViewById(R.id.plus);
		  
		  mobile=(EditText)view.findViewById(R.id.mobile);
		  mobile.requestFocus();
		  landline=(EditText)view.findViewById(R.id.landline);
		  
		  email=(EditText)view.findViewById(R.id.emailadd);
		 
		  website=(EditText)view.findViewById(R.id.website);
		  
		  streetadd=(EditText)view.findViewById(R.id.street);
		  
		 
		
		  cityadd=(EditText)view.findViewById(R.id.city);
	
		  
		  stret=(LinearLayout)view.findViewById(R.id.llayout1);
		  
		  ccity=(LinearLayout)view.findViewById(R.id.llayout3);
		 
		 
		  stret.setVisibility(View.GONE);
		 
		  ccity.setVisibility(View.GONE);
		  plus.setOnClickListener(groupcontect.this);
		 
		 
		  	
	 return view;
	
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(b==false)
		{
		stret.setVisibility(View.VISIBLE);
		  
		  ccity.setVisibility(View.VISIBLE);
		  plus.setImageResource(R.drawable.arrowup);
		  b=true;
		}
		else
		{
			stret.setVisibility(View.GONE);
			 
			  ccity.setVisibility(View.GONE);
			  plus.setImageResource(R.drawable.arrowdown1);
			  b=false;
		}
		
	}
	
	
}
