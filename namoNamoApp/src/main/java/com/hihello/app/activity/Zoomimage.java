package com.hihello.app.activity;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.hihello.app.R;
import com.hihello.app.constant.HiHelloConstant;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by rohan on 2/18/2016.
 */
public class Zoomimage extends AppCompatActivity
{
    ImageView image;
    RelativeLayout image_layout;
    String pic,pich;
    Bitmap mBitmap=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zoomimage);

        image_layout=(RelativeLayout)findViewById(R.id.relativeLayout3);
        //image=(ImageView)findViewById(R.id.imageView12);

        Intent in=getIntent();
        pic=in.getStringExtra("pic");
        pich=in.getStringExtra("pich");
        Display display = getWindowManager().getDefaultDisplay();



        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.leftback);
        getSupportActionBar().setTitle("");

      final int w=display.getWidth();
        final int h=display.getHeight();
    image=new ImageView(Zoomimage.this);
        Picasso.with(Zoomimage.this)
                .load(HiHelloConstant.url+pich)
                .error(R.drawable.backheaderimage)
                .into(image, new Callback() {
                    @Override
                    public void onSuccess() {
//                        Log.e("success", Constants.URL+"bookimage/"+pic);
                        Bitmap bmp = ((BitmapDrawable) image.getDrawable()).getBitmap();
                        TouchImageView img = new TouchImageView(Zoomimage.this);
                        // img.setImageResource(R.drawable.img1);
                        img.setImageBitmap(bmp);
                        img.setMaxZoom(5f);
                        img.setMinimumHeight(h);
                        img.setMinimumWidth(w);

//                        image_layout.removeAllViews();
                        image_layout.addView(img);

                    }

                    @Override
                    public void onError() {
                        Log.e("success", "error");
                    }
                });
    }

    public void savefun(String pathimage)
    {
        savebitmap(pathimage);
    }


    public void savebitmap(String filename) {

        HiHelloConstant.imgseries=HiHelloConstant.imgseries+1;
        String series=String.valueOf(HiHelloConstant.imgseries);
        File direct = new File(Environment.getExternalStorageDirectory()
                + "/HiHello Profile");

        if (!direct.exists()) {
            direct.mkdirs();
        }

        DownloadManager mgr = (DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE);

        Uri downloadUri = Uri.parse(filename);
        DownloadManager.Request request = new DownloadManager.Request(
                downloadUri);

        request.setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_WIFI
                        | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false).setTitle("HiHello Gallery")
                .setDescription("Something useful. No, really.")
                .setDestinationInExternalPublicDir("/HiHello", series+".jpg");

        mgr.enqueue(request);
        Toast.makeText(Zoomimage.this,"Done",Toast.LENGTH_SHORT).show();

    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.zoomimage_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.save:
                savefun(HiHelloConstant.url+ pich);
                break;
            case R.id.share:
                URL url = null;
                try {
                    url = new URL(HiHelloConstant.url+pich);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    mBitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Bitmap icon = mBitmap;
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/jpeg");

                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "title");
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        values);


                OutputStream outstream;
                try {
                    outstream = getContentResolver().openOutputStream(uri);
                    icon.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
                    outstream.close();
                } catch (Exception e) {
                    System.err.println(e.toString());
                }

                share.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(share, "Share Image"));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
