package com.hihello.app.adapter;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.hihello.app.R;
import com.hihello.app.activity.CircularImageView;
import com.hihello.app.common.Log;
import com.hihello.app.constant.HiHelloConstant;
import com.hihello.app.constant.NetworkConnection;
import com.hihello.app.getset.get_createdgroup;
import com.hihello.app.getset.get_set_groups;
import com.hihello.app.getset.get_setter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class firstlistadapter extends BaseAdapter {
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	private DisplayImageOptions options;
	Context c;
	NetworkConnection nw;
	AQuery aquery;
	ArrayList<get_set_groups> info;
	TextView groupname,weburl;
	String name;


	public firstlistadapter(Context c, ArrayList<get_set_groups> info,String name) {
		this.c=c;
		this.info=info;
		this.name=name;
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.loading)
				.showImageForEmptyUri(R.drawable.backheaderimage)
				.showImageOnFail(R.drawable.backheaderimage)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.considerExifParams(true)
				.build();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return info.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View v, ViewGroup parent) {
		// TODO Auto-generated method stub
		c=parent.getContext();
		LayoutInflater li=(LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		v=li.inflate(R.layout.firsrlistitem,parent,false);
		nw=new NetworkConnection(c);
		final ViewHolder holder=new ViewHolder();
		holder.admin= (CircularImageView) v.findViewById(R.id.imageView2);
		groupname=(TextView)v.findViewById(R.id.textView1);
		weburl=(TextView)v.findViewById(R.id.textView2);
		holder.role=(TextView)v.findViewById(R.id.textView38);
		groupname.setText(info.get(position).getGroup_name());
		weburl.setText(info.get(position).getWeb_url());
//		Toast.makeText(c,info.get(position).getAdmin_pich()+","+info.get(position).getGroup_pich()+","+info.get(position).getGroup_name(),Toast.LENGTH_LONG).show();
		if(name.equals(info.get(position).getProfile_name()))
		{
			holder.role.setText("Admin");
			holder.role.setTextColor(Color.parseColor("#12C609"));
		}
		else
		{
			holder.role.setText("Joiner");
			holder.role.setTextColor(Color.parseColor("#ff0000"));
		}
//		aquery=new AQuery((Activity)c,admin);
//		File imgFile = new File(info.get(position).getProfileimage());
//		if(imgFile.exists())
//		{
//
//			admin.setImageURI(Uri.fromFile(imgFile));

//		}

		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(c));
		android.util.Log.e("userimageee",info.get(position).getTitle()+"//");
//		Toast.makeText(c,info.get(position).getTitle()+"//",Toast.LENGTH_LONG).show();
		imageLoader.getInstance().displayImage(HiHelloConstant.url+info.get(position).getGroup_pic(),
				holder.admin, options, animateFirstListener);
//		if(nw.isConnectingToInternet()==true) {
//			Log.e("admin_pic",HiHelloConstant.url+"img/"+info.get(position).getGroup_pic());
////			aquery=new AQuery((Activity)c,admin);
////			aquery.id(admin).image(HiHelloConstant.url+"img/"+info.get(position).getGroup_pic(),true, true, 300,R.drawable.backheaderimage);
//			Picasso.with(css)
//					.load(HiHelloConstant.url+"img/"+info.get(position).getAdmin_pic())
//					.error(R.drawable.backheaderimage)
//					.placeholder(R.drawable.backheaderimage)
//					.into(admin);
//
//
//		}
//		else
//		{
//			Log.e("group_pic",HiHelloConstant.url+"img/"+info.get(position).getAdmin_pic());
////			aquery=new AQuery((Activity)c,admin);
////			aquery.id(admin).image(HiHelloConstant.url+"img/"+info.get(position).getAdmin_pic(),true, true, 300,R.drawable.backheaderimage);
//			Picasso.with(c)
//					.load(HiHelloConstant.url+"img/"+info.get(position).getAdmin_pic())
//					.error(R.drawable.backheaderimage)
//					.placeholder(R.drawable.backheaderimage)
//					.into(admin);
//		}

		return v;
	}
	static class ViewHolder
	{
		CircularImageView admin;
		TextView role;
	}
	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}



}
