package com.zoolife.app.firebase.services;

import android.app.LauncherActivity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.zoolife.app.R;
import com.zoolife.app.Session;
import com.zoolife.app.activity.AppBaseActivity;
import com.zoolife.app.activity.ChatActivity;
import com.zoolife.app.activity.MainActivity;
import com.zoolife.app.activity.SplashActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class MessagingService extends FirebaseMessagingService {

    private static final String TAG = MessagingService.class.getSimpleName();
    NotificationCompat.Builder notificationBuilder;
    private final int NOTIFICATION_ID = 200258;

    private Session session = AppBaseActivity.session;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.i("notificationreced","notificationrecevied ---- "+remoteMessage.getData());
        boolean sent = false;
        if(remoteMessage!=null){
            Log.d(TAG, "From: " + remoteMessage.getFrom()+"");
            Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody()+"");

            Intent intent = new Intent(this, LauncherActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            if (remoteMessage.getData().size() > 0 && remoteMessage.getNotification() != null) {
                Log.d(TAG, "Notification Message Body: " + remoteMessage.getData()+"");

                if (remoteMessage.getData().containsKey("data")) {
                    try {
                        JSONObject data = new JSONObject(remoteMessage.getData().get("data"));
                        String identifier = data.getString("identifier");

                        if (identifier.equals("NC")) { // New Chat
                            String remoteGroup = remoteMessage.getData().get("group_id");
                            boolean isChat = session.isChat();
                            String chatGroupId = session.getChatGroupId();
                            if (!(isChat && chatGroupId.equals(remoteGroup))) {
                                Intent chatIntent = new Intent(this, ChatActivity.class);
                                chatIntent.putExtra(ChatActivity.FROM_NOTIFICATION, true);
                                chatIntent.putExtra(ChatActivity.GROUP_ID, data.getString("group_id"));
                                chatIntent.putExtra(ChatActivity.USER_EMAIL, data.getString("user_email"));
//                                chatIntent.putExtra(ChatActivity.USER_ID, data.getString("user_id"));
//                                return;
                                sendNotification(remoteMessage, chatIntent);
                                sent = true;
                            }
                        }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if(!sent) {
            Intent chatIntent = new Intent(this, SplashActivity.class);
            sendNotification(remoteMessage, chatIntent);
        }
        SharedPreferences preferences = getApplicationContext().getSharedPreferences(MYPREF, Context.MODE_PRIVATE);
        preferences.edit().putInt("notificationCount",(preferences.getInt("notificationCount",0)+1)).apply();
        Log.i("notificationreced",preferences.getInt("notificationCount",0)+"   ------ ");
        try {
            if(MainActivity.mainActivity != null)
            {
                MainActivity.mainActivity.SetNotificationBadge();
            }
        }
        catch (Exception ex)
        {

        }
    }

    private void sendNotification(RemoteMessage remoteMessage, Intent intent) {
        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_ONE_SHOT);
//        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String NOTIFICATION_CHANNEL_ID=createNotificationChannel();
            notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.drawable.app_logo)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);
            NotificationManager notificationManager = (NotificationManager)
                    getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify((int) System.currentTimeMillis(), notificationBuilder.build());

        } else {
            notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.app_logo)
                    .setContentTitle(title)
                    .setContentText(body)
//                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager)
                    getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify((int) System.currentTimeMillis(), notificationBuilder.build());



        }


    }
    String MYPREF = "MyPref";

    @RequiresApi(Build.VERSION_CODES.O)
    private String createNotificationChannel(){
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build();

        String channelId = "ZooLife_255";
        String channelName = "ZooLife_Channel";
        NotificationChannel channel = new NotificationChannel(channelId,
                channelName, NotificationManager.IMPORTANCE_HIGH);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        channel.setShowBadge(false);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
//        channel.setSound(null, null);
        channel.setSound(sound, audioAttributes);
        mNotificationManager.createNotificationChannel(channel);
        return channelId;
    }
}
