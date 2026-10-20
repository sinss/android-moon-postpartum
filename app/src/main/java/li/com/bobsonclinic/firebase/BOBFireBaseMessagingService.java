package li.com.bobsonclinic.firebase;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by leo.chang on 03/10/2017.
 */

public class BOBFireBaseMessagingService extends FirebaseMessagingService {
    public static final int FCM_REQUEST_CODE = 9001;
    public static final int REMOTE_NOTIFICATION_ID = 995;
    private static final String TAG = FirebaseMessagingService.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onMessageReceived(com.google.firebase.messaging.RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
//        if (remoteMessage.getData().get("default") != null) {
//            try {
//                JSONObject json = new JSONObject(remoteMessage.getData());
//                JSONObject def = new JSONObject(json.getString("default"));
//                String message = def.getString("message");
//                MessageAction action = new MessageAction(0, "", 0, 3, message);
//                Log.w("Receive Message", message);
//                sendNotification(message, action);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        } else {
//            Map<String, String> json = remoteMessage.getData();
//            String activityId = json.get("activity_id");
//            String message = remoteMessage.getNotification().getBody();
//            String vcode = json.get("vcode");
//            String itemId = json.get("item_id");
//            String hospitalId = json.get("hospital_id");
//            MessageAction action = new MessageAction(Integer.valueOf(activityId), vcode, Integer.valueOf(itemId), Integer.valueOf(hospitalId), message);
//            sendNotification(message, action);
//        }
    }

//    private void sendNotification(String msg, MessageAction action) {
//        //for test
//        int badgeCount = 1;
//        //boolean success = ShortcutBadger.applyCount(this, badgeCount);
//
//        //設定點選推播要啟動的 Activity
////        Intent intent = new Intent(this, BOBSplashActivity.class);
////        intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
////        intent.putExtra(MessageAction.TAG, action);
////        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
////        stackBuilder.addParentStack(BOBSplashActivity.class);
////        stackBuilder.addNextIntent(intent);
////        PendingIntent contentIntent = PendingIntent.getActivity(this, FCM_REQUEST_CODE, intent, PendingIntent.FLAG_CANCEL_CURRENT);//設定新通知
////        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
////        mBuilder.setSmallIcon(R.drawable.icon);
////        mBuilder.setTicker(msg);
////        mBuilder.setWhen(new Date().getTime() + 1000);
////        mBuilder.setContentTitle(msg);
////        mBuilder.setPriority(Notification.PRIORITY_MAX);
////        mBuilder.setAutoCancel(false);
////        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
////        mBuilder.setContentIntent(contentIntent);
////        Notification notificaiton = mBuilder.build();
////        notificaiton.flags = PendingIntent.FLAG_CANCEL_CURRENT;
//
//        Intent intent = new Intent(this, BOBCompanyActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//        Bundle bundle = new Bundle();
////        bundle.putParcelable(MessageAction.TAG, action);
////		intent.putExtra(MessageAction.TAG, action);
//        bundle.putString("key", "TEST");
//        intent.putExtra("key", "TEST");
//        intent.putExtras(bundle);
//
////		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
////		stackBuilder.addParentStack(BOBSplashActivity.class);
////		stackBuilder.addNextIntent(intent);
////		PendingIntent contentIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
////        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);
//		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);//設定新通知
//        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
//        bigText.setBigContentTitle("榜生婦產聯盟");
//        bigText.bigText(msg);
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
//        builder.setStyle(bigText)
//                .setDefaults(Notification.DEFAULT_SOUND)
//                .setSmallIcon(R.drawable.icon)
//                .setPriority(Notification.PRIORITY_MAX)
//                .setSound(defaultSoundUri)
//                .setAutoCancel(true)
//                .setContentIntent(contentIntent);
//        //builder.setFullScreenIntent(contentIntent, true);
//        Notification notificaiton = builder.build();
//        notificaiton.flags = PendingIntent.FLAG_UPDATE_CURRENT;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            setupChannels();
//        }
//        //顯示通知
//        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        mNotificationManager.notify(0, notificaiton);
//
//        //同時送一筆Local broadcast message
//        Intent broardcastIntent = new Intent(FCM_MESSAGE_TAG);
//        broardcastIntent.putExtra(MessageAction.TAG, action);
//        LocalBroadcastManager.getInstance(this).sendBroadcast(broardcastIntent);
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    private void setupChannels(){
//        CharSequence adminChannelName = "BOBSON";
//        String adminChannelDescription = "榜生婦產聯盟";
//
//        NotificationChannel adminChannel;
//        adminChannel = new NotificationChannel("tw.sw16.bobson.mobile", adminChannelName, NotificationManager.IMPORTANCE_DEFAULT);
//        adminChannel.setDescription(adminChannelDescription);
//        adminChannel.enableLights(true);
//        adminChannel.setLightColor(Color.RED);
//        adminChannel.enableVibration(true);
//        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        mNotificationManager.createNotificationChannel(adminChannel);
//    }
//
//    private int getNotificationIcon(NotificationCompat.Builder notificationBuilder) {
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            //int color = 0x008000;
//            //notificationBuilder.setColor(color);
//            return R.drawable.icon;
//
//        } else {
//            return R.drawable.icon;
//        }
//    }
}
