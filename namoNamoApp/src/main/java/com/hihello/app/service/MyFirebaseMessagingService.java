package com.hihello.app.service;

import com.google.firebase.messaging.FirebaseMessagingService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


import com.google.firebase.messaging.RemoteMessage;
import com.hihello.app.activity.HomeMain_activity;
import com.hihello.app.db.DatabaseHandler;

/**
 * Created by rohan on 6/15/2016.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    DatabaseHandler db;

    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        String result="false",joiner_name="",option="";
        Log.e("received","push");
        String groupname=remoteMessage.getData().get("groupname");
        String id=remoteMessage.getData().get("groupid");
        String message=remoteMessage.getData().get("message");
        result=remoteMessage.getData().get("Result");
        joiner_name=remoteMessage.getData().get("joiner_name");
        option=remoteMessage.getData().get("option");
        String joiner_pic=remoteMessage.getData().get("joiner_pic");
        String type=remoteMessage.getData().get("Type");
        Log.e("result",message+"jiuuio");

//        Log.e("result",message+"jiuuio");
        if(("delete").equals(option))
        {
            Log.e("click","click");
            db = new DatabaseHandler(this);
                db.deleteGroup(groupname);
            Log.e("deletegroup",groupname);
        }
//        else if("true".equals(result))
//        {
//            Log.e("push sent","push sent");
//            db = new DatabaseHandler(this);
//            db.insertjoinerchat(groupname,joiner_name,message,joiner_pic,type);
//        }
        else {
            db = new DatabaseHandler(this);
            db.insertgroupchat(id, groupname, message,type,joiner_name,joiner_pic);
        }

      sendNotification(groupname,message);
        Log.e(TAG, "From: " + remoteMessage.getData());
        //Log.e(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
    }
    // [END receive_message]


    private void sendNotification(String Groupname,String message) {
        Intent intent = new Intent(this,HomeMain_activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.btn_plus)
                .setContentTitle(Groupname)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
