package com.hihello.app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hihello.app.R;
import com.hihello.app.content.HiHelloMenuItem;

import java.util.List;

public class MenuAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<HiHelloMenuItem> items;

	public MenuAdapter(LayoutInflater inflater, List<HiHelloMenuItem> items) {
		this.inflater = inflater;
		this.items = items;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int index, View arg1, ViewGroup arg2) {
		View view = arg1;
		view = inflater.inflate(R.layout.drawer_item, null);
		TextView txt_menu = (TextView) view.findViewById(R.id.txt_menu);
		txt_menu.setText(items.get(index).getMenuText());

		ImageView img_menu = (ImageView) view.findViewById(R.id.img_menu);
		img_menu.setImageResource(items.get(index).getMenu_image());
		ImageView img_divider = (ImageView) view.findViewById(R.id.img_divider);
		switch (index) {
		case 2:
		case 7:
			img_divider.setVisibility(View.VISIBLE);
			break;
		}
		return view;
	}

}
