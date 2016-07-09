package com.hihello.app.activity;

import android.app.Application;
import android.content.Context;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;
import android.support.multidex.MultiDex;

import com.hihello.androidclient.data.YaximConfiguration;
import com.hihello.app.R;
import com.hihello.app.common.ContactImageDownloader;
import com.hihello.app.constant.HiHelloConstant;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions.Builder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.utils.L;

import de.duenndns.ssl.MemorizingTrustManager;

public class UIApplication extends Application {

	// identity name and type, see:
	// http://xmpp.org/registrar/disco-categories.html
	public static final String XMPP_IDENTITY_NAME = "yaxim";
	public static final String XMPP_IDENTITY_TYPE = "phone";
	public static DisplayImageOptions diOptions;
	public static DisplayImageOptions diRoundOptions;

	// MTM is needed globally for both the backend (connect)
	// and the frontend (display dialog)
	public MemorizingTrustManager mMTM;

	private YaximConfiguration mConfig;
	public static Context context;

	public static UIApplication getApp(Context ctx) {
		return (UIApplication) ctx.getApplicationContext();
	}

	public static YaximConfiguration getConfig(Context ctx) {
		return getApp(ctx).mConfig;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		context = getApplicationContext();
		mMTM = new MemorizingTrustManager(this);
		mConfig = new YaximConfiguration(
				PreferenceManager.getDefaultSharedPreferences(this));
		HiHelloConstant.DEVICE_ID = Secure.getString(getContentResolver(),
				Secure.ANDROID_ID);

		Builder builder = new DisplayImageOptions.Builder().cacheInMemory()
				.cacheOnDisc().showImageForEmptyUri(R.drawable.defaultuser)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT);
		diOptions = builder.build();
		builder.displayer(new RoundedBitmapDisplayer(5));
		diRoundOptions = builder.build();
		initImageLoader(getApplicationContext());
	}

	public static DisplayImageOptions getRoundedBO(int radius) {
		Builder builder = new DisplayImageOptions.Builder().cacheInMemory()
				.cacheOnDisc().imageScaleType(ImageScaleType.IN_SAMPLE_INT);
		builder.displayer(new RoundedBitmapDisplayer(radius));
		return builder.build();

	}

	public static void initImageLoader(Context ctx) {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				ctx).threadPoolSize(3)

		.threadPriority(Thread.NORM_PRIORITY - 2)
				.memoryCacheExtraOptions(320, 320)
				.imageDownloader(new ContactImageDownloader(ctx))
				// default = device screen dimensions
				.memoryCache(new LruMemoryCache(25 * 1024 * 1024))
				.memoryCacheSize(25 * 1024 * 1024)
				/** Adding a directory path to cache the images */
				// .denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator()).build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
		L.disableLogging();
		L.writeLogs(false);
	}

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}
}
