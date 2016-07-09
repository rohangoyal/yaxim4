package com.hihello.app.common;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;

import com.hihello.app.constant.HiHelloSharedPrefrence;

public class HiHelloDownloadManager {

	public static String downloadAudioFile(Context ctx, String fileUrl) {
		String filePath = PathUtils.getAudioDir();
		return downloadMediaFile(ctx, fileUrl, filePath);	}

	public static String downloadVideoFile(Context ctx, String fileUrl) {
		String filePath = PathUtils.getVideoDir();
		return downloadMediaFile(ctx, fileUrl, filePath);

	}

	public static String downloadImageFile(Context ctx, String fileUrl) {
		String filePath = PathUtils.getImageDir();
		// "/sdcard/NamoNamo/Media/Image/";
		return downloadMediaFile(ctx, fileUrl, filePath);
	}

	private static String downloadMediaFile(Context ctx, String fileUrl,
			String filePath) {
		String path = filePath
				+ fileUrl.substring(fileUrl.lastIndexOf("/") + 1);

		try {
			File file = new File(filePath);
			file.mkdirs();
			int count;
			URL url = new URL(fileUrl);
			URLConnection connection = url.openConnection();
			connection.connect();
			// int lenghtOfFile = connection.getContentLength();
			InputStream input = new BufferedInputStream(url.openStream());
			OutputStream output = new FileOutputStream(path);
			byte data[] = new byte[1024];
			long total = 0;
			while ((count = input.read(data)) != -1) {
				total += count;
				output.write(data, 0, count);
			}
			output.flush();
			output.close();
			input.close();
			HiHelloSharedPrefrence.setLocalPath(ctx, path, fileUrl);
		} catch (Exception e) {
		}
		return path;
	}
}
