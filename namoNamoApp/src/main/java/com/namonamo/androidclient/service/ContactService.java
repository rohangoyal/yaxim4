package com.namonamo.androidclient.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


public class ContactService extends Service {

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
	
		
		return super.onStartCommand(intent, flags, startId);
	}
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
