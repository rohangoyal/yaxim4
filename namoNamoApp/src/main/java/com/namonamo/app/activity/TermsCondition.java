package com.namonamo.app.activity;

import com.namonamo.app.R;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class TermsCondition extends BaseActivity {

	private OnClickListener backClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			finish();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.terms_condition);
		findViewById(R.id.btn_back).setOnClickListener(backClick);
	}
}
