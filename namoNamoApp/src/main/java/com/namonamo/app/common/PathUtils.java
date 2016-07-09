package com.namonamo.app.common;

import android.os.Environment;

public class PathUtils {
	public static String getUploadImageDir() {
		return Environment.getExternalStorageDirectory()
				+ "/NamoNamo/Upload/NamoNamo Image/";
	}

	public static String getUploadAudioDir() {
		return Environment.getExternalStorageDirectory()
				+ "/NamoNamo/Upload/NamoNamo Audio/";
	}

	public static String getUploadVideoDir() {
		return Environment.getExternalStorageDirectory()
				+ "/NamoNamo/Upload/NamoNamo Video/";
	}

	public static String getProfilePicDir() {
		return Environment.getExternalStorageDirectory()
				+ "/NamoNamo/ProfilePic/";
	}

	public static String getImageDir() {
		return Environment.getExternalStorageDirectory()
				+ "/NamoNamo/Media/NamoNamo Image/";
	}

	public static String getAudioDir() {
		return Environment.getExternalStorageDirectory()
				+ "/NamoNamo/Media/NamoNamo Audio/";
	}

	public static String getVideoDir() {
		return Environment.getExternalStorageDirectory()
				+ "/NamoNamo/Media/NamoNamo Video/";
	}
}
