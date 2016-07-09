package com.namonamo.app.constant;

import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Class is used to save and get the data to the preference store
 * 
 * 
 */
public class NamoNamoSharedPrefrence {
	/** preference file name */
	private static final String PREF_USER_INFO = "userinfo";
	private static final String PREF_URLMAP_INFO = "url_map";

	/** Preference storage keys */

	private interface PREF_KEYS {
		final String MOBILE_NO = "MOBILE_NO";
		final String IS_REGISTERED = "IS_REGISTERED";
		final String PROFILE_IMAGE = "PROFILE_IMAGE";
		final String USER_NAME = "USER_NAME";
		final String STATUS = "STATUS";
		final String CHAT_INIT = "CHAT_INIT";
		final String USER_ID = "userId";
		final String USER_JID = "jid";
		final String USER_PASSWORD = "password";

		final String SHOW_LAST_SEEN = "show_last_seen";
		final String SHOW_PROFILE = "show_profile";
		final String SHOW_STATUS = "show_status";
		final String DAILY_MESSAGE_DATE = "DAILY_MESSAGE_DATE";
		final String VERSION_CODE = "VERSION_CODE";
		final String FORCE_UPGRADE = "FORCE_UPGRADE";
		final String COUNTRY_CODE = "COUNTRY_CODE";
		final String FB_LIKE = "FB_LIKE";
		final String RATE_US = "RATE_US";

	}

	/**
	 * 
	 * @param mcontext
	 * @param userId
	 */
	public static void saveMobileNo(Context mcontext, String mobile_no) {
		SharedPreferences info = mcontext.getSharedPreferences(PREF_USER_INFO,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = info.edit();
		editor.putString(PREF_KEYS.MOBILE_NO, mobile_no);
		editor.commit();

	}

	public static void saveCountryCode(Context mcontext, String countryCode) {
		SharedPreferences info = mcontext.getSharedPreferences(PREF_USER_INFO,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = info.edit();
		editor.putString(PREF_KEYS.COUNTRY_CODE, countryCode);
		editor.commit();

	}

	public static void setMobileNo(Context mcontext, String mobile_no) {
		SharedPreferences info = mcontext.getSharedPreferences(PREF_USER_INFO,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = info.edit();
		editor.putString(PREF_KEYS.MOBILE_NO, mobile_no);
		editor.commit();
	}

	/**
	 * 
	 * @param ctx
	 * @return
	 */
	public static String getMobileNo(Context ctx) {
		SharedPreferences info = ctx.getSharedPreferences(PREF_USER_INFO,
				Context.MODE_PRIVATE);
		return info.getString(PREF_KEYS.MOBILE_NO, "");
	}

	public static String getCountryCode(Context ctx) {
		SharedPreferences info = ctx.getSharedPreferences(PREF_USER_INFO,
				Context.MODE_PRIVATE);
		return info.getString(PREF_KEYS.COUNTRY_CODE, "+91");
	}

	public static boolean isRegistered(Context ctx) {
		SharedPreferences info = ctx.getSharedPreferences(PREF_USER_INFO,
				Context.MODE_PRIVATE);
		return info.getBoolean(PREF_KEYS.IS_REGISTERED, false);
	}

	public static void setRegistered(Context ctx, boolean status) {
		SharedPreferences info = ctx.getSharedPreferences(PREF_USER_INFO,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = info.edit();
		editor.putBoolean(PREF_KEYS.IS_REGISTERED, status);
		editor.commit();
	}

	public static void setProfileImageUrl(Context ctx, String url) {
		SharedPreferences info = ctx.getSharedPreferences(PREF_USER_INFO,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = info.edit();
		editor.putString(PREF_KEYS.PROFILE_IMAGE, url);
		editor.commit();
	}

	public static void setUserName(Context ctx, String name) {
		SharedPreferences info = ctx.getSharedPreferences(PREF_USER_INFO,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = info.edit();
		editor.putString(PREF_KEYS.USER_NAME, name);
		editor.commit();

	}

	public static void setStatus(Context ctx, String status) {
		SharedPreferences info = ctx.getSharedPreferences(PREF_USER_INFO,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = info.edit();
		editor.putString(PREF_KEYS.STATUS, status);
		editor.commit();

	}

	public static String getUserName(Context ctx) {
		SharedPreferences info = ctx.getSharedPreferences(PREF_USER_INFO,
				Context.MODE_PRIVATE);
		return info.getString(PREF_KEYS.USER_NAME, "");
	}

	public static String getStatus(Context ctx) {
		SharedPreferences info = ctx.getSharedPreferences(PREF_USER_INFO,
				Context.MODE_PRIVATE);
		return info.getString(PREF_KEYS.STATUS, "");
	}
	public static String getProfileImageUrl(Context ctx) {
		SharedPreferences info = ctx.getSharedPreferences(PREF_USER_INFO,
				Context.MODE_PRIVATE);
		return info.getString(PREF_KEYS.PROFILE_IMAGE, "");
	}
	public static String common(Context ctx) {
		SharedPreferences info = ctx.getSharedPreferences(PREF_USER_INFO,
				Context.MODE_PRIVATE);
		return info.getString(PREF_KEYS.PROFILE_IMAGE, "");
	}

	public static void setChatPageInit(Context ctx, boolean value) {
		SharedPreferences info = ctx.getSharedPreferences(PREF_USER_INFO,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = info.edit();
		editor.putBoolean(PREF_KEYS.CHAT_INIT, value);
		editor.commit();
	}

	public static boolean getChatPageInit(Context ctx) {
		SharedPreferences info = ctx.getSharedPreferences(PREF_USER_INFO,
				Context.MODE_PRIVATE);
		return info.getBoolean(PREF_KEYS.CHAT_INIT, false);
	}

	public static void setLocalPath(Context ctx, String localPath,
			String serverPath) {
		SharedPreferences info = ctx.getSharedPreferences(PREF_URLMAP_INFO,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = info.edit();
		editor.putString(serverPath, localPath);
		editor.commit();
	}

	public static String getLocalPath(Context ctx, String serverPath) {
		SharedPreferences info = ctx.getSharedPreferences(PREF_URLMAP_INFO,
				Context.MODE_PRIVATE);
		return info.getString(serverPath, "");
	}

	public static String getUserId(Context ctx) {
		SharedPreferences info = ctx.getSharedPreferences(PREF_URLMAP_INFO,
				Context.MODE_PRIVATE);
		return info.getString(PREF_KEYS.USER_ID, "");
	}

	public static void setUserId(Context mcontext, String value) {
		SharedPreferences info = mcontext.getSharedPreferences(
				PREF_URLMAP_INFO, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = info.edit();
		editor.putString(PREF_KEYS.USER_ID, value);
		editor.commit();
	}

	public static boolean getShowLastSeen(Context ctx) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(ctx);
		return sharedPreferences.getBoolean(PREF_KEYS.SHOW_LAST_SEEN, true);
	}

	public static void setShowLastSeen(Context ctx, boolean value) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(ctx);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putBoolean(PREF_KEYS.SHOW_LAST_SEEN, value);
		editor.commit();
	}

	public static boolean getShowProfile(Context ctx) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(ctx);
		return sharedPreferences.getBoolean(PREF_KEYS.SHOW_PROFILE, true);
	}

	public static void setShowProfile(Context ctx, boolean value) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(ctx);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putBoolean(PREF_KEYS.SHOW_PROFILE, value);
		editor.commit();
	}

	public static boolean getShowStatus(Context ctx) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(ctx);
		return sharedPreferences.getBoolean(PREF_KEYS.SHOW_STATUS, true);
	}

	public static void setShowStatus(Context ctx, boolean value) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(ctx);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putBoolean(PREF_KEYS.SHOW_STATUS, value);
		editor.commit();
	}

	public static void setRewardPoint(Context mcontext, int value,
			String rewardType) {
		SharedPreferences info = mcontext.getSharedPreferences(
				PREF_URLMAP_INFO, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = info.edit();
		editor.putInt(rewardType, value);
		editor.commit();
	}

	public static void removeRewardPoint(Context mcontext, int reward,
			String rewardType) {
		reward = getRewardPoint(mcontext, rewardType) - reward;
		setRewardPoint(mcontext, reward, rewardType);
	}

	public static void addRewardPoint(Context mcontext, int reward,
			String rewardType) {
		reward = reward + getRewardPoint(mcontext, rewardType);
		setRewardPoint(mcontext, reward, rewardType);
	}

	public static int getRewardPoint(Context mcontext, String rewardType) {
		SharedPreferences info = mcontext.getSharedPreferences(
				PREF_URLMAP_INFO, Context.MODE_PRIVATE);
		return info.getInt(rewardType, 0);
	}

	public static void setDailyMessageLastTime(Context mcontext, long time) {
		SharedPreferences info = mcontext.getSharedPreferences(
				PREF_URLMAP_INFO, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = info.edit();
		editor.putLong(PREF_KEYS.DAILY_MESSAGE_DATE, time);
		editor.commit();
	}

	public static long getDailyMessageLastTime(Context mcontext) {
		SharedPreferences info = mcontext.getSharedPreferences(
				PREF_URLMAP_INFO, Context.MODE_PRIVATE);
		return info.getLong(PREF_KEYS.DAILY_MESSAGE_DATE,
				new Date().getTime() / 1000);
	}

	public static String getUserJID(Context ctx) {
		SharedPreferences info = ctx.getSharedPreferences(PREF_URLMAP_INFO,
				Context.MODE_PRIVATE);
		return info.getString(PREF_KEYS.USER_JID, "");
	}

	public static String getUserPassword(Context ctx) {
		SharedPreferences info = ctx.getSharedPreferences(PREF_URLMAP_INFO,
				Context.MODE_PRIVATE);
		return info.getString(PREF_KEYS.USER_PASSWORD, "");
	}

	public static void setUserJID(Context mcontext, String jid) {
		SharedPreferences info = mcontext.getSharedPreferences(PREF_USER_INFO,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = info.edit();
		editor.putString(PREF_KEYS.USER_JID, jid);
		editor.commit();
	}

	public static void setUserPassword(Context mcontext, String password) {
		SharedPreferences info = mcontext.getSharedPreferences(PREF_USER_INFO,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = info.edit();
		editor.putString(PREF_KEYS.USER_PASSWORD, password);
		editor.commit();
	}

	public static int getVersionCode(Context ctx) {
		SharedPreferences info = ctx.getSharedPreferences(PREF_USER_INFO,
				Context.MODE_PRIVATE);
		return info.getInt(PREF_KEYS.VERSION_CODE, 0);
	}

	public static void setVersionCode(Context mcontext, int value) {
		SharedPreferences info = mcontext.getSharedPreferences(PREF_USER_INFO,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = info.edit();
		editor.putInt(PREF_KEYS.VERSION_CODE, value);
		editor.commit();
	}

	public static boolean isForceUpgrade(Context ctx) {
		SharedPreferences info = ctx.getSharedPreferences(PREF_USER_INFO,
				Context.MODE_PRIVATE);
		return info.getBoolean(PREF_KEYS.FORCE_UPGRADE, false);
	}

	public static void setForceUpgrade(Context ctx, boolean value) {
		SharedPreferences info = ctx.getSharedPreferences(PREF_USER_INFO,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = info.edit();
		editor.putBoolean(PREF_KEYS.FORCE_UPGRADE, value);
		editor.commit();
	}

	public static void setFacebookLike(Context ctx, boolean value) {
		SharedPreferences info = ctx.getSharedPreferences(PREF_USER_INFO,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = info.edit();
		editor.putBoolean(PREF_KEYS.FB_LIKE, value);
		editor.commit();
	}

	public static boolean isFBLike(Context ctx) {
		SharedPreferences info = ctx.getSharedPreferences(PREF_USER_INFO,
				Context.MODE_PRIVATE);
		return info.getBoolean(PREF_KEYS.FB_LIKE, false);
	}

	public static void setRateUs(Context ctx, boolean value) {
		SharedPreferences info = ctx.getSharedPreferences(PREF_USER_INFO,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = info.edit();
		editor.putBoolean(PREF_KEYS.RATE_US, value);
		editor.commit();
	}

	public static boolean isRateUs(Context ctx) {
		SharedPreferences info = ctx.getSharedPreferences(PREF_USER_INFO,
				Context.MODE_PRIVATE);
		return info.getBoolean(PREF_KEYS.RATE_US, false);
	}
}
