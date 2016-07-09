package com.namonamo.app.activity;


import com.namonamo.app.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Groupabout extends Fragment 
{
	TextView prefer,male,female;
	TextView shortnote;
	EditText shortnotetxt;
	ImageView arrow;
	LinearLayout mlayout,flayout;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		  View view =inflater.inflate(R.layout.groupabout,container,false);
		
		mlayout=(LinearLayout)view.findViewById(R.id.llayout1);
//		flayout=(LinearLayout)view.findViewById(R.id.llayout2);
		prefer=(TextView)view.findViewById(R.id.textView2);
//		male=(TextView)view.findViewById(R.id.textView3);
//		female=(TextView)view.findViewById(R.id.textView4);
		shortnote=(TextView)view.findViewById(R.id.edittext);
		shortnotetxt=(EditText)view.findViewById(R.id.editText5);
		 return view;
	}
}
