package com.hihello.androidclient.service;

import java.util.Date;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicBoolean;

import org.json.JSONObject;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

import com.hihello.androidclient.IXMPPRosterCallback;
import com.hihello.androidclient.MainWindow;
import com.hihello.androidclient.data.RosterProvider;
import com.hihello.androidclient.exceptions.YaximXMPPException;
import com.hihello.androidclient.util.ConnectionState;
import com.hihello.androidclient.util.StatusMode;
import com.hihello.app.R;
import com.hihello.app.db.HiHelloContact;
import com.hihello.app.db.hiHelloContactDBService;
import com.hihello.app.db.RecentChat;
import com.hihello.app.db.RecentChatDBService;

public class XMPPService extends GenericService {

	private AtomicBoolean mConnectionDemanded = new AtomicBoolean(false); // should
																			// we
																			// try
																			// to
																			// reconnect?
	private static final int RECONNECT_AFTER = 5;
	private static final int RECONNECT_MAXIMUM = 10 * 60;
	private static final String RECONNECT_ALARM = "com.namonamo.androidclient.RECONNECT_ALARM";
	private int mReconnectTimeout = RECONNECT_AFTER;
	private String mReconnectInfo = "";
	private Intent mAlarmIntent = new Intent(RECONNECT_ALARM);
	private PendingIntent mPAlarmIntent;
	private BroadcastReceiver mAlarmReceiver = new ReconnectAlarmReceiver();

	private ServiceNotification mServiceNotification = null;

	private Smackable mSmackable;
	private boolean create_account = false;
	private IXMPPRosterService.Stub mService2RosterConnection;
	private IXMPPChatService.Stub mServiceChatConnection;

	private RemoteCallbackList<IXMPPRosterCallback> mRosterCallbacks = new RemoteCallbackList<IXMPPRosterCallback>();
	private HashSet<String> mIsBoundTo = new HashSet<String>();
	private Handler mMainHandler = new Handler();

	@Override
	public IBinder onBind(Intent intent) {
		userStartedWatching();

		String chatPartner = intent.getDataString();
		if ((chatPartner != null)) {
			resetNotificationCounter(chatPartner);
			mIsBoundTo.add(chatPartner);
			return mServiceChatConnection;
		}
		return mService2RosterConnection;
	}

	@Override
	public void onRebind(Intent intent) {
		userStartedWatching();
		String chatPartner = intent.getDataString();
		if ((chatPartner != null)) {
			mIsBoundTo.add(chatPartner);
			resetNotificationCounter(chatPartner);
		}
	}

	@Override
	public boolean onUnbind(Intent intent) {
		String chatPartner = intent.getDataString();
		if ((chatPartner != null)) {
			mIsBoundTo.remove(chatPartner);
		}
		userStoppedWatching();

		return true;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		createServiceRosterStub();
		createServiceChatStub();

		mPAlarmIntent = PendingIntent.getBroadcast(this, 0, mAlarmIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		registerReceiver(mAlarmReceiver, new IntentFilter(RECONNECT_ALARM));

		YaximBroadcastReceiver.initNetworkStatus(getApplicationContext());

		if (mConfig.autoConnect && mConfig.jid_configured) {
			/*
			 * start our own service so it remains in background even when
			 * unbound
			 */
			Intent xmppServiceIntent = new Intent(this, XMPPService.class);
			startService(xmppServiceIntent);
		}

		mServiceNotification = ServiceNotification.getInstance();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		((AlarmManager) getSystemService(Context.ALARM_SERVICE))
				.cancel(mPAlarmIntent);
		mRosterCallbacks.kill();
		if (mSmackable != null) {
			manualDisconnect();
			mSmackable.unRegisterCallback();
		}
		unregisterReceiver(mAlarmReceiver);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		logInfo("onStartCommand(), mConnectionDemanded="
				+ mConnectionDemanded.get());
		if (intent != null) {
			create_account = intent.getBooleanExtra("create_account", false);

			if ("disconnect".equals(intent.getAction())) {
				failConnection(getString(R.string.conn_no_network));
				return START_STICKY;
			} else if ("reconnect".equals(intent.getAction())) {
				// TODO: integrate the following steps into one "RECONNECT"
				failConnection(getString(R.string.conn_no_network));
				// reset reconnection timeout
				mReconnectTimeout = RECONNECT_AFTER;
				doConnect();
				return START_STICKY;
			} else if ("ping".equals(intent.getAction())) {
				if (mSmackable != null) {
					mSmackable.sendServerPing();
					return START_STICKY;
				}
				// if not yet connected, fall through to doConnect()
			} else if ("sync".equals(intent.getAction())) {
				if (mSmackable != null) {
					((SmackableImp) mSmackable).sendOfflineMessages();
					return START_STICKY;
				}
			}
		}

		mConnectionDemanded.set(mConfig.autoConnect);
		doConnect();
		return START_STICKY;
	}

	private void createServiceChatStub() {
		mServiceChatConnection = new IXMPPChatService.Stub() {

			public void sendMessage(String user, String message)
					throws RemoteException {
				if (mSmackable != null)
					mSmackable.sendMessage(user, message);
				else
					SmackableImp.sendOfflineMessage(getContentResolver(), user,
							message);
			}

			public void sendPingMessage(String user) throws RemoteException {
				if (mSmackable != null)
					mSmackable.sendPingMessage(user);
			}

			public boolean isAuthenticated() throws RemoteException {
				if (mSmackable != null) {
					return mSmackable.isAuthenticated();
				}

				return false;
			}

			public void clearNotifications(String Jid) throws RemoteException {
				clearNotification(Jid);
			}

			@Override
			public void saveMessage(String user, String message)
					throws RemoteException {
				SmackableImp.sendPendingMessage(getContentResolver(), user,
						message);
			}

			@Override
			public void setCurrentJID(String user) throws RemoteException {
				if (mSmackable != null) {
					mSmackable.setCurrentJID(user);
					if (user != null && user.length() > 0)
						sendPingMessage(user);
				}
			}
		};
	}

	private void createServiceRosterStub() {
		mService2RosterConnection = new IXMPPRosterService.Stub() {

			public void registerRosterCallback(IXMPPRosterCallback callback)
					throws RemoteException {
				if (callback != null)
					mRosterCallbacks.register(callback);
			}

			public void unregisterRosterCallback(IXMPPRosterCallback callback)
					throws RemoteException {
				if (callback != null)
					mRosterCallbacks.unregister(callback);
			}

			public int getConnectionState() throws RemoteException {
				if (mSmackable != null) {
					return mSmackable.getConnectionState().ordinal();
				} else {
					return ConnectionState.OFFLINE.ordinal();
				}
			}

			public String getConnectionStateString() throws RemoteException {
				return XMPPService.this.getConnectionStateString();
			}

			public void setStatusFromConfig() throws RemoteException {
				if (mSmackable != null) { // this should always be true, but
											// stil...
					mSmackable.setStatusFromConfig();
					updateServiceNotification();
				}
			}

			public void addRosterItem(String user, String alias, String group)
					throws RemoteException {
				try {
					mSmackable.addRosterItem(user, alias, group);
				} catch (YaximXMPPException e) {
					shortToastNotify(e);
				}
			}

			public void addRosterGroup(String group) throws RemoteException {
				mSmackable.addRosterGroup(group);
			}

			public void removeRosterItem(String user) throws RemoteException {
				try {
					mSmackable.removeRosterItem(user);
				} catch (YaximXMPPException e) {
					shortToastNotify(e);
				}
			}

			public void moveRosterItemToGroup(String user, String group)
					throws RemoteException {
				try {
					mSmackable.moveRosterItemToGroup(user, group);
				} catch (YaximXMPPException e) {
					shortToastNotify(e);
				}
			}

			public void renameRosterItem(String user, String newName)
					throws RemoteException {
				try {
					mSmackable.renameRosterItem(user, newName);
				} catch (YaximXMPPException e) {
					shortToastNotify(e);
				}
			}

			public void renameRosterGroup(String group, String newGroup)
					throws RemoteException {
				mSmackable.renameRosterGroup(group, newGroup);
			}

			@Override
			public String changePassword(String newPassword)
					throws RemoteException {
				return mSmackable.changePassword(newPassword);
			}

			public void disconnect() throws RemoteException {
				manualDisconnect();
			}

			public void connect() throws RemoteException {
				mConnectionDemanded.set(true);
				mReconnectTimeout = RECONNECT_AFTER;
				doConnect();
			}

			public void sendPresenceRequest(String jid, String type)
					throws RemoteException {
				mSmackable.sendPresenceRequest(jid, type);
			}

			@Override
			public boolean hasRosterItem(String jid) throws RemoteException {

				try {
					return mSmackable.hasRosterItem(jid);
				} catch (Exception e) {
					shortToastNotify(e);
				}
				return false;
			}

		};
	}

	private String getConnectionStateString() {
		StringBuilder sb = new StringBuilder();
		sb.append(mReconnectInfo);
		if (mSmackable != null && mSmackable.getLastError() != null) {
			sb.append("\n");
			sb.append(mSmackable.getLastError());
		}
		return sb.toString();
	}

	public String getStatusTitle(ConnectionState cs) {
		if (cs != ConnectionState.ONLINE)
			return mReconnectInfo;
		String status = getString(StatusMode.fromString(mConfig.statusMode)
				.getTextId());

		if (mConfig.statusMessage.length() > 0) {
			status = status + " (" + mConfig.statusMessage + ")";
		}

		return status;
	}

	private void updateServiceNotification() {
		ConnectionState cs = ConnectionState.OFFLINE;
		if (mSmackable != null) {
			cs = mSmackable.getConnectionState();
		}

		// HACK to trigger show-offline when XEP-0198 reconnect is going on
		getContentResolver().notifyChange(RosterProvider.CONTENT_URI, null);
		getContentResolver().notifyChange(RosterProvider.GROUPS_URI, null);
		// end-of-HACK

		broadcastConnectionState(cs);

		// do not show notification if not a foreground service
		if (!mConfig.foregroundService)
			return;

		if (cs == ConnectionState.OFFLINE) {
			mServiceNotification.hideNotification(this, SERVICE_NOTIFICATION);
			return;
		}
		Notification n = new Notification(R.drawable.ic_offline, null,
				System.currentTimeMillis());
		n.flags = Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR
				| Notification.FLAG_ONLY_ALERT_ONCE;

		Intent notificationIntent = new Intent(this, MainWindow.class);
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		n.contentIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		if (cs == ConnectionState.ONLINE)
			n.icon = R.drawable.ic_online;

		String title = getString(R.string.conn_title, mConfig.jabberID);
		String message = getStatusTitle(cs);
//		n.setLatestEventInfo(this, title, message, n.contentIntent);
//
//		mServiceNotification.showNotification(this, SERVICE_NOTIFICATION, n);
	}

	private void doConnect() {
		mReconnectInfo = getString(R.string.conn_connecting);
		updateServiceNotification();
		if (mSmackable == null) {
			createAdapter();
		}

		mSmackable.requestConnectionState(ConnectionState.ONLINE,
				create_account);
	}

	private void broadcastConnectionState(ConnectionState cs) {
		final int broadCastItems = mRosterCallbacks.beginBroadcast();

		for (int i = 0; i < broadCastItems; i++) {
			try {
				mRosterCallbacks.getBroadcastItem(i).connectionStateChanged(
						cs.ordinal());
			} catch (RemoteException e) {
				logError("caught RemoteException: " + e.getMessage());
			}
		}
		mRosterCallbacks.finishBroadcast();
	}

	private NetworkInfo getNetworkInfo() {
		Context ctx = getApplicationContext();
		ConnectivityManager connMgr = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		return connMgr.getActiveNetworkInfo();
	}

	private boolean networkConnected() {
		NetworkInfo info = getNetworkInfo();

		return info != null && info.isConnected();
	}

	private boolean networkConnectedOrConnecting() {
		NetworkInfo info = getNetworkInfo();

		return info != null && info.isConnectedOrConnecting();
	}

	// call this when Android tells us to shut down
	private void failConnection(String reason) {
		logInfo("failConnection: " + reason);
		mReconnectInfo = reason;
		updateServiceNotification();
		if (mSmackable != null)
			mSmackable.requestConnectionState(ConnectionState.DISCONNECTED);
	}

	// called from Smackable when connection broke down
	private void connectionFailed(String reason) {
		logInfo("connectionFailed: " + reason);
		// TODO: error message from downstream?
		// mLastConnectionError = reason;
		if (!networkConnected()) {
			mReconnectInfo = getString(R.string.conn_no_network);
			mSmackable
					.requestConnectionState(ConnectionState.RECONNECT_NETWORK);

		} else if (mConnectionDemanded.get()) {
			mReconnectInfo = getString(R.string.conn_reconnect,
					mReconnectTimeout);
			mSmackable
					.requestConnectionState(ConnectionState.RECONNECT_DELAYED);
			logInfo("connectionFailed(): registering reconnect in "
					+ mReconnectTimeout + "s");
			((AlarmManager) getSystemService(Context.ALARM_SERVICE)).set(
					AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
							+ mReconnectTimeout * 1000, mPAlarmIntent);
			mReconnectTimeout = mReconnectTimeout * 2;
			if (mReconnectTimeout > RECONNECT_MAXIMUM)
				mReconnectTimeout = RECONNECT_MAXIMUM;
		} else {
			connectionClosed();
		}

	}

	private void connectionClosed() {
		logInfo("connectionClosed.");
		mReconnectInfo = "";
		mServiceNotification.hideNotification(this, SERVICE_NOTIFICATION);
	}

	public void manualDisconnect() {
		mConnectionDemanded.set(false);
		mReconnectInfo = getString(R.string.conn_disconnecting);
		performDisconnect();
	}

	public void performDisconnect() {
		if (mSmackable != null) {
			// this is non-blocking
			mSmackable.requestConnectionState(ConnectionState.OFFLINE);
		}
	}

	private void createAdapter() {
		System.setProperty("smack.debugEnabled", "" + mConfig.smackdebug);
		try {
			mSmackable = new SmackableImp(mConfig, getContentResolver(), this);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		mSmackable.registerCallback(new XMPPServiceCallback() {
			public void newMessage(String from, String message,
					boolean silent_notification) {
				logInfo("notification: " + from);
				updateRecentHistory(from, message);
				notifyClient(from, mSmackable.getNameForJID(from), message,
						!mIsBoundTo.contains(from), silent_notification, false);
			}

			private void updateRecentHistory(String jid, String dataMessage) {
				String message = dataMessage;
				String contactNo = "";
				try {
					JSONObject json = new JSONObject(dataMessage);
					contactNo = json.getString("mobileNo");
					message = json.getString("message");
				} catch (Exception x) {
					x.getMessage();
				}
				RecentChat recentChat = RecentChatDBService.fetchRecentChat(
						getApplicationContext(), jid);
				if (recentChat == null)
					recentChat = new RecentChat();

				recentChat.updateUnreadChat();
				recentChat.setFromMe(false);
				recentChat.setDate(new Date());
				recentChat.setMessage(dataMessage);
				recentChat.setMobileNo(contactNo);
				recentChat.setUserJID(jid);

				HiHelloContact contact = hiHelloContactDBService
						.fetchContactByJID(getApplicationContext(), jid);
				if (contact != null) {
					recentChat.setProfilePic(contact.getPic_url());
					recentChat.setDisplayName(contact.getDisplayName());
				}

				RecentChatDBService.saveRecentChat(getApplicationContext(),
						recentChat);
				Intent intent = new Intent();
				intent.setAction(RecentChatDBService.RECENT_DB_CHANGE_ACTION);
				sendBroadcast(intent);
			}

			public void messageError(final String from, final String error,
					final boolean silent_notification) {
				logInfo("error notification: " + from);
				mMainHandler.post(new Runnable() {
					public void run() {
						// work around Toast fallback for errors
						notifyClient(from, mSmackable.getNameForJID(from),
								error, !mIsBoundTo.contains(from),
								silent_notification, true);
					}
				});
			}

			public void rosterChanged() {
			}

			public void connectionStateChanged() {
				// TODO: OFFLINE is sometimes caused by XMPPConnection calling
				// connectionClosed() callback on an error, need to catch that?
				switch (mSmackable.getConnectionState()) {
				// case OFFLINE:
				case DISCONNECTED:
					connectionFailed(getString(R.string.conn_disconnected));
					break;
				case ONLINE:
					mReconnectTimeout = RECONNECT_AFTER;
				default:
					updateServiceNotification();
				}
			}
		});
	}

	private class ReconnectAlarmReceiver extends BroadcastReceiver {
		public void onReceive(Context ctx, Intent i) {
			logInfo("Alarm received.");
			if (!mConnectionDemanded.get()) {
				return;
			}
			if (mSmackable != null
					&& mSmackable.getConnectionState() == ConnectionState.ONLINE) {
				logError("Reconnect attempt aborted: we are connected again!");
				return;
			}
			doConnect();
		}
	}

	private int number_of_eyes = 0;

	private void userStartedWatching() {
		number_of_eyes += 1;
		logInfo("userStartedWatching: " + number_of_eyes);
		if (mSmackable != null)
			mSmackable.setUserWatching(true);
	}

	private void userStoppedWatching() {
		number_of_eyes -= 1;
		logInfo("userStoppedWatching: " + number_of_eyes);
		// delay deactivation by 3s, in case we happen to be immediately
		// re-bound
		mMainHandler.postDelayed(new Runnable() {
			public void run() {
				if (mSmackable != null && number_of_eyes == 0)
					mSmackable.setUserWatching(false);
			}
		}, 3000);
	}
}
