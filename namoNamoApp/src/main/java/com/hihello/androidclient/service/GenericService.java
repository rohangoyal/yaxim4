package com.hihello.androidclient.service;

import java.util.HashMap;
import java.util.Map;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Vibrator;

import com.hihello.androidclient.chat.ChatItem;
import com.hihello.androidclient.chat.ChatWindow;
import com.hihello.androidclient.data.YaximConfiguration;
import com.hihello.androidclient.util.LogConstants;
import com.hihello.app.R;
import com.hihello.app.activity.RecentChats;
import com.hihello.app.activity.UIApplication;
import com.hihello.app.common.Log;
import com.hihello.app.db.HiHelloContact;
import com.hihello.app.db.hiHelloContactDBService;

public abstract class GenericService extends Service {

	private static final String TAG = "yaxim.Service";
	private static final String APP_NAME = "yaxim";
	private static final int MAX_TICKER_MSG_LEN = 45;

	private NotificationManager mNotificationMGR;
	private Notification mNotification;
	private Vibrator mVibrator;
	private Intent mNotificationIntent;
	protected WakeLock mWakeLock;
	// private int mNotificationCounter = 0;

	private Map<String, Integer> notificationCount = new HashMap<String, Integer>(
			2);
	private Map<String, Integer> notificationId = new HashMap<String, Integer>(
			2);
	protected static int SERVICE_NOTIFICATION = 1;
	private int lastNotificationId = 2;

	protected YaximConfiguration mConfig;

	@Override
	public void onCreate() {
		Log.i(TAG, "called onCreate()");
		super.onCreate();
		mConfig = UIApplication.getConfig(this);
		mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		mWakeLock = ((PowerManager) getSystemService(Context.POWER_SERVICE))
				.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, APP_NAME);
		addNotificationMGR();
	}

	@Override
	public void onDestroy() {
		Log.i(TAG, "called onDestroy()");
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(TAG, "called onStartCommand()");
		return START_STICKY;
	}

	private void addNotificationMGR() {
		mNotificationMGR = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	}

	private MediaPlayer m;

	public void playBeep() {
		try {
			if (m != null) {
				m.stop();
				m.reset();
			} else
				m = new MediaPlayer();
			AssetFileDescriptor descriptor = getAssets().openFd(
					"namonamo_ringtone.mp3");
			m.setDataSource(descriptor.getFileDescriptor(),
					descriptor.getStartOffset(), descriptor.getLength());
			descriptor.close();
			m.prepare();

			m.setVolume(1f, 1f);
			m.setLooping(false);
			m.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void notifyClient(String fromJid, String fromUserName,
			String message, boolean showNotification,
			boolean silent_notification, boolean is_error) {
		if (showNotification) {
//			if (is_error)
//				shortToastNotify(getString(R.string.notification_error) + " "
//						+ message);
			// only play sound and return
			try {
				if (!silent_notification
						&& !Uri.EMPTY.equals(mConfig.notifySound)) {
					if (mConfig.notifySound.toString().equalsIgnoreCase("NAMONAMO")) {
						playBeep();
					}
					// RingtoneManager.getRingtone(getApplicationContext(),
					// mConfig.notifySound).play();
				}
			} catch (NullPointerException e) {
				// ignore NPE when ringtone was not found
			}
		} else {
			return;
		}
		mWakeLock.acquire();

		// Override silence when notification is created initially
		// if there is no open notification for that JID, and we get a "silent"
		// one (i.e. caused by an incoming carbon message), we still
		// ring/vibrate,
		// but only once. As long as the user ignores the notifications, no more
		// sounds are made. When the user opens the chat window, the counter is
		// reset and a new sound can be made.
		if (silent_notification && !notificationCount.containsKey(fromJid)) {
			silent_notification = false;
		}

		setNotification(fromJid, fromUserName, message, is_error);
		setLEDNotification();
//		if (showNotification && !silent_notification) {
//			playBeep();
//		}

		int notifyId = 0;
		if (notificationId.containsKey(fromJid)) {
			notifyId = notificationId.get(fromJid);
		} else {
			lastNotificationId++;
			notifyId = lastNotificationId;
			notificationId.put(fromJid, Integer.valueOf(notifyId));
		}

		// If vibration is set to "system default", add the vibration flag to
		// the
		// notification and let the system decide.
		if (!silent_notification && "SYSTEM".equals(mConfig.vibraNotify)) {
			mNotification.defaults |= Notification.DEFAULT_VIBRATE;
		}
		mNotificationMGR.notify(0, mNotification);

		// If vibration is forced, vibrate now.
		if (!silent_notification && "ALWAYS".equals(mConfig.vibraNotify)) {
			mVibrator.vibrate(400);
		}
		mWakeLock.release();
	}

	private void setNotification(String fromJid, String fromUserId,
			String message, boolean is_error) {
		ChatItem item = new ChatItem();
		item.setMessage(message);
		int mNotificationCounter = 0;
		if (notificationCount.containsKey(fromJid)) {
			mNotificationCounter = notificationCount.get(fromJid);
		}
		mNotificationCounter++;

		String pic="";
		notificationCount.put(fromJid, mNotificationCounter);
		String author;
		if (fromJid.equalsIgnoreCase(fromUserId)) {
			HiHelloContact contact = hiHelloContactDBService
					.fetchContactByJID(getApplicationContext(), fromJid);
			if (contact != null) {
				fromUserId = contact.getDisplayName();

				pic=contact.getPic_url();

			}
			else
				fromUserId = item.getMobileNo();

		}
		if (null == fromUserId || fromUserId.length() == 0) {
			author = fromJid;
		} else {
			author = fromUserId;
		}

		String title = getString(R.string.notification_message, author);
		String ticker;
		if (is_error) {
			title = getString(R.string.notification_error);
			ticker = title;
			message = author + ": " + message;
		} else if (mConfig.ticker) {
			int newline = message.indexOf('\n');
			int limit = 0;
			String messageSummary = message;
			if (newline >= 0)
				limit = newline;
			if (limit > MAX_TICKER_MSG_LEN
					|| message.length() > MAX_TICKER_MSG_LEN)
				limit = MAX_TICKER_MSG_LEN;
			if (limit > 0)
				messageSummary = message.substring(0, limit) + " [...]";
			ticker = title;// + "\n" + messageSummary;
		} else
			ticker = getString(R.string.notification_anonymous_message);
		Uri userNameUri = Uri.parse(fromJid);

		if (item.getChatType() == ChatItem.CHAT_TYPE_TEXT)
			message = item.getChatText();
		else
			message = item.getChatTypeStr();
		if (notificationCount.size() > 1) {
			int size = 0;
			for (String key : notificationCount.keySet()) {
				size = size + notificationCount.get(key);
			}
			message = size + " messages from " + notificationCount.size()
					+ " contacts";
			author = "Hii Hello";
			mNotificationIntent = new Intent(this, RecentChats.class);

		} else {
			if (mNotificationCounter > 1)
				message = mNotificationCounter + " messages";
			mNotificationIntent = new Intent(this, ChatWindow.class);
			mNotificationIntent.putExtra("from","push");
			mNotificationIntent.putExtra("name",fromUserId);
			mNotificationIntent.putExtra("pic",pic);


		}
		mNotificationIntent.setData(userNameUri);
		mNotificationIntent.putExtra(ChatWindow.INTENT_EXTRA_USERNAME,
				fromUserId);
		mNotificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
				mNotificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		Notification.Builder builder = new Notification.Builder(
				getApplicationContext());
		builder.setTicker(ticker);

		builder.setSmallIcon(R.mipmap.ic_launcher);
		builder.setWhen(System.currentTimeMillis());
		builder.setContentIntent(pendingIntent);
		builder.setContentText(message);
		builder.setContentTitle(author);
		builder.setAutoCancel(true);
		builder.setDefaults(0);
		if (mNotificationCounter > 1) {
			builder.setNumber(mNotificationCounter);
		}
		mNotification = builder.build();
//		mNotification.setLatestEventInfo(this, author, message, pendingIntent);



		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		notificationManager.notify(0, mNotification);
	}

	private void setLEDNotification() {
		if (mConfig.isLEDNotify) {
			mNotification.ledARGB = Color.MAGENTA;
			mNotification.ledOnMS = 300;
			mNotification.ledOffMS = 1000;
			mNotification.flags |= Notification.FLAG_SHOW_LIGHTS;
		}
	}

	protected void shortToastNotify(String msg) {
//		Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
//		toast.show();
	}

	protected void shortToastNotify(Throwable e) {
		e.printStackTrace();
		while (e.getCause() != null)
			e = e.getCause();
		shortToastNotify(e.getMessage());
	}

	public void resetNotificationCounter(String userJid) {
		notificationCount.remove(userJid);
	}

	protected void logError(String data) {
		if (LogConstants.LOG_ERROR) {
			Log.e(TAG, data);
		}
	}

	protected void logInfo(String data) {
		if (LogConstants.LOG_INFO) {
			Log.i(TAG, data);
		}
	}

	public void clearNotification(String Jid) {
		mNotificationMGR.cancel(0);
		int notifyId = 0;
		if (notificationId.containsKey(Jid)) {
			notifyId = notificationId.get(Jid);
			mNotificationMGR.cancel(0);
		}
	}

}
