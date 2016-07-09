package com.hihello.app.common;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.hihello.app.activity.UIApplication;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class BitmapUtil {

	public static DisplayMetrics getDefaultDisplay(Context ctx) {
		DisplayMetrics outMetrics = new DisplayMetrics();
		((WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics;
	}

	public static Bitmap getBitMapFromString(String encodedString) {
		try {
			byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
			Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
					encodeByte.length);
			return bitmap;
		} catch (Exception e) {
			e.getMessage();
			return null;
		}
	}
	 public static Bitmap getVideoThumbnail(Context context, ContentResolver cr, String testVideopath) {  
	        // final String testVideopath = "/mnt/sdcard/sidamingbu.mp4";  
		    ContentResolver testcr = context.getContentResolver();  
	        String[] projection = { MediaStore.Video.Media.DATA, MediaStore.Video.Media._ID, };  
	        String whereClause = MediaStore.Video.Media.DATA + " = '" + testVideopath + "'";  
	        Cursor cursor = testcr.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, whereClause,  
	                null, null);  
	        int _id = 0;  
	        String videoPath = "";  
	        if (cursor == null || cursor.getCount() == 0) {  
	            return null;  
	        }  
	        if (cursor.moveToFirst()) {  
	  
	            int _idColumn = cursor.getColumnIndex(MediaStore.Video.Media._ID);  
	            int _dataColumn = cursor.getColumnIndex(MediaStore.Video.Media.DATA);  
	  
	            do {  
	                _id = cursor.getInt(_idColumn);  
	                videoPath = cursor.getString(_dataColumn);  
	                System.out.println(_id + " " + videoPath);  
	            } while (cursor.moveToNext());  
	        }  
	        BitmapFactory.Options options = new BitmapFactory.Options();  
	        options.inDither = false;  
	        options.inPreferredConfig = Bitmap.Config.ARGB_8888;  
	        Bitmap bitmap = MediaStore.Video.Thumbnails.getThumbnail(cr, _id, Images.Thumbnails.MICRO_KIND,  
	                options);  
	        return bitmap;  
	    }  
	public static Bitmap getBitmapFromPath(String filePath, int maxSize,
			boolean fromCamera) {
		Bitmap bitmap = null;
		System.gc();
		File file = new File(filePath);
		int scale = 1;
		if (file.exists()) {
			try {
				BitmapFactory.Options o = new BitmapFactory.Options();
				o.inJustDecodeBounds = true;
				System.gc();
				BitmapFactory.decodeStream(new FileInputStream(file), null, o);
				if (o.outHeight > maxSize || o.outWidth > maxSize)
					scale = (int) Math.pow(
							2,
							(int) Math.round(Math.log(maxSize
									/ (double) Math
											.max(o.outHeight, o.outWidth))
									/ Math.log(0.5)));

				BitmapFactory.Options o2 = new BitmapFactory.Options();
				o2.inSampleSize = (int) scale;
				System.gc();
				bitmap = BitmapFactory.decodeStream(new FileInputStream(file),
						null, o2);
				if (fromCamera) {
					Matrix matrix = new Matrix();
					matrix.postRotate(getImageOrientation(filePath));
					Bitmap rotatedBitmap = Bitmap
							.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
									bitmap.getHeight(), matrix, true);
//					bitmap.recycle();
					bitmap = rotatedBitmap;
				}
			} catch (Exception x) {
			} catch (OutOfMemoryError x) {
			}
		}
		return bitmap;
	}

	public static String saveUploadImage(Context ctx, String filePath,
			boolean fromCamera) {

		File dir = new File(PathUtils.getUploadImageDir());
		dir.mkdirs();
		DisplayMetrics outMetrics = getDefaultDisplay(ctx);
		String fileName = UUID.randomUUID().toString() + ".png";
		File file = new File(dir, fileName);
		Bitmap out = getBitmapFromPath(filePath,
				outMetrics.heightPixels > 480 ? 480 : outMetrics.heightPixels,
				fromCamera);
		FileOutputStream fOut;
		try {
			fOut = new FileOutputStream(file);
			out.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
			fOut.flush();
			fOut.close();
			out.recycle();
		} catch (Exception e) {

		}
		return file.getPath();
	}

	public static String saveUploadVideo(Context ctx, String sourceFile)
			throws Exception {

		File dir = new File(PathUtils.getUploadVideoDir());
		dir.mkdirs();
		String[] splits = sourceFile.split("\\.");
		String fileName = UUID.randomUUID() + "." + splits[splits.length - 1];
		String destFile = PathUtils.getUploadVideoDir() + fileName;

		InputStream in = new FileInputStream(sourceFile);

		OutputStream out = new FileOutputStream(destFile);

		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		in.close();
		out.close();
		return destFile;
	}

	public static String getImageThumbnailEncodeString(int imageUri,
			ContentResolver cr) {
		Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(cr, imageUri,
				MediaStore.Images.Thumbnails.MICRO_KIND, null);
		Bitmap finalBitmap = Bitmap.createScaledBitmap(bitmap, 30, 30, false);
		bitmap.recycle();
		return BitMapToString(finalBitmap);
	}

	public static String getVideoThumbnailEncodeString(int videoUri,
			ContentResolver cr) {
		Bitmap bitmap = MediaStore.Video.Thumbnails.getThumbnail(cr, videoUri,
				MediaStore.Video.Thumbnails.MICRO_KIND, null);
//		Bitmap finalBitmap = Bitmap.createScaledBitmap(bitmap, 50, 50, false);
//		bitmap.recycle();
		return BitMapToString(bitmap);
	}

	public static String getVideoDuration(Uri uri, ContentResolver cr) {
		String duration = "";
		Cursor cursor = null;
		try {
			cursor = cr.query(uri, null, null, null, null);
			if (cursor.moveToFirst()) {
				duration = cursor
						.getString(cursor
								.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
			}
		} catch (Exception x) {

		} finally {
			cursor.close();
		}
		return duration;
	}

	public static String BitMapToString(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		byte[] b = baos.toByteArray();
		String temp = Base64.encodeToString(b, Base64.DEFAULT);
		return temp;
	}

	public static int getImageOrientation(String imagePath) {
		int rotate = 0;
		try {

			File imageFile = new File(imagePath);
			ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
			int orientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);

			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_270:
				rotate = 270;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				rotate = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_90:
				rotate = 90;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rotate;
	}

	public static void displayImage(String uri, ImageView imageView,
			DisplayImageOptions diOptions) {
		ImageLoadingListener listener = new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String arg0, View arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadingComplete(String arg0, View view, Bitmap arg2) {
			    Animation fadeInAnimation = AnimationUtils.loadAnimation(UIApplication.context, android.R.anim.fade_in);
			    view.startAnimation(fadeInAnimation);

			}
			
			@Override
			public void onLoadingCancelled(String arg0, View arg1) {
				// TODO Auto-generated method stub
				
			}
		};
		ImageLoader.getInstance().displayImage(uri, imageView, diOptions);
				
	}
}
