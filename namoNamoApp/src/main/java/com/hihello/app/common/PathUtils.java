package com.hihello.app.common;

import android.os.Environment;

public class PathUtils {
	public static String getUploadImageDir() {
		return Environment.getExternalStorageDirectory()
				+ "/HiHello/Upload/HiHello Image/";
	}

	public static String getUploadAudioDir() {
		return Environment.getExternalStorageDirectory()
				+ "/HiHello/Upload/HiHello Audio/";
	}

	public static String getUploadVideoDir() {
		return Environment.getExternalStorageDirectory()
				+ "/HiHello/Upload/HiHello Video/";
	}

	public static String getProfilePicDir() {
		return Environment.getExternalStorageDirectory()
				+ "/HiHello/ProfilePic/";
	}

	public static String getImageDir() {
		return Environment.getExternalStorageDirectory()
				+ "/HiHello/Media/HiHello Image/";
	}

	public static String getAudioDir() {
		return Environment.getExternalStorageDirectory()
				+ "/HiHello/Media/HiHello Audio/";
	}

	public static String getVideoDir() {
		return Environment.getExternalStorageDirectory()
				+ "/HiHello/Media/HiHello Video/";
	}
}
