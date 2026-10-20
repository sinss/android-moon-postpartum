package li.com.bobsonclinic.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import li.com.bobsonclinic.R;
import li.com.bobsonclinic.activity.SplashActivity;

/**
 * This {@code IntentService} does the actual handling of the GCM message.
 * {@code BOBGcmBroadcastReceiver} (a {@code WakefulBroadcastReceiver}) holds a
 * partial wake lock for this service while the service does its work. When the
 * service is finished, it calls {@code completeWakefulIntent()} to release the
 * wake lock.
 */
public class BOBGcmIntentService extends IntentService {
	public static final int NOTIFICATION_ID = 1;
	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;

	public BOBGcmIntentService() {
		super("BOBGcmIntentService");
	}

	public static final String TAG = BOBGcmIntentService.class.getSimpleName();

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		// The getMessageType() intent parameter must be the intent you received
		// in your BroadcastReceiver.
		String messageType = gcm.getMessageType(intent);
		Log.i(TAG, "GCM onHandleIntent messageType: " + messageType);
		if (!extras.isEmpty()) { // has effect of unparcelling Bundle
			/*
			 * Filter messages based on message type. Since it is likely that
			 * GCM will be extended in the future with new message types, just
			 * ignore any message types you're not interested in, or that you
			 * don't recognize.
			 */
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
//				sendNotification("Send error: " + extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
//				sendNotification("Deleted messages on server: " + extras.toString());
				// If it's a regular GCM message, do some work.
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
				// Post notification of received message.
				Log.i(TAG, "GCM Received: " + extras.toString());
				String message = extras.getString("message", null);
				sendNotification(message);
			}
		}
		intent.putExtra("push", "push");
		// Release the wake lock provided by the WakefulBroadcastReceiver.
		BOBGcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	private void sendNotification(String msg) {
		//for test
		int badgeCount = 1;
		//boolean success = ShortcutBadger.applyCount(this, badgeCount);

		//設定點選推播要啟動的Activity
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, SplashActivity.class), 0);
		//設定新通知
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
		mBuilder.setSmallIcon(R.mipmap.ic_launcher);
		mBuilder.setTicker(msg);
		mBuilder.setContentTitle(msg);
		mBuilder.setAutoCancel(true);
		mBuilder.setContentIntent(contentIntent);
		//顯示通知
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

	}
}
