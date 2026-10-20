package li.com.bobsonclinic.logic;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.io.IOException;

import li.com.bobsonclinic.R;
import li.com.bobsonclinic.manager.BOBSystemManager;


public class BOBPushRegisterLogic {
	private final static String TAG = BOBPushRegisterLogic.class.getSimpleName();

	private static BOBPushRegisterLogic cursorLogic = null;
	//GCM ============ Start
	private FirebaseMessaging fcm;
	private String token;
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	Activity mActivity;
	//GCM ============ End

	public static synchronized BOBPushRegisterLogic getInstance() {
		if (cursorLogic == null)
			cursorLogic = new BOBPushRegisterLogic();
		return cursorLogic;
	}


	//GCM Start ====================================================================================================

	/**
	 * 進行GCM Token 是否已取得及是否需要更新之檢查
	 */
	public void doGCMRegister(Activity activity) {
		Log.i(TAG, "進行GCM Token 是否已取得及是否需要更新之檢查");
		this.mActivity = activity;
		if (checkPlayServices(activity)) {
			fcm = FirebaseMessaging.getInstance();
			token = getRegistrationId(activity.getApplicationContext());

			if (token.isEmpty()) {
				Log.i(TAG, "本地無已儲存之GCM token或需要更新GCM Token(registration ID), 開始向 GCM server 取 token");
				registerInBackground(activity);
			} else {
				//post token to sever
				//BOBCommunicationLogic.getInstance().postDeviceToken(BOBystemManager.getInstance().getCurrentUserId(), UniSystemManager.getInstance().getCurrentUserToken(), token);
				Log.i(TAG, "已註冊之 GCM token (registration ID): " + token);
			}

		}
	}

	/**
	 * Check the device to make sure it has the Google Play Services APK. If it doesn't, display a dialog that allows
	 * users to download the APK from the Google Play Store or enable it in the device's system settings.
	 */
	public boolean checkPlayServices(Activity activity) {
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, activity, PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Log.i(TAG, "This device is not supported.");
//                finish();
			}
			return false;
		}
		return true;
	}

	/**
	 * Gets the current registration ID for application on GCM service.
	 * <p/>
	 * If result is empty, the app needs to register.
	 *
	 * @return registration ID, or empty string if there is no existing
	 * registration ID.
	 */
	public String getRegistrationId(Context context) {
		String registrationId = BOBPreferenceLogic.getInstance().getGCMToken();
		if (registrationId.isEmpty()) {
			Log.i(TAG, "Registration not found.");
			return "";
		}
		// Check if app was updated; if so, it must clear the registration ID
		// since the existing regID is not guaranteed to work with the new
		// app version.
//        int registeredVersion = UniPreferenceLogic.getInstance().getAppVersion();
//        int currentVersion = getAppVersion(context);
//        UniPreferenceLogic.getInstance().saveAPPVersion(currentVersion);
//        if (registeredVersion != currentVersion) {
//            UniLogUtil.i(TAG, "App version changed.");
//            return "";
//        }
		return registrationId;
	}

	/**
	 * Registers the application with GCM servers asynchronously.
	 * <p/>
	 * Stores the registration ID and app versionCode in the application's
	 * shared preferences.
	 */
	public void registerInBackground(final Activity activity) {
		String msg = "";
		if (fcm == null) {
			fcm = FirebaseMessaging.getInstance();
		}
		FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
			@Override
			public void onComplete(@NonNull Task<String> task) {
				try {
					token = task.getResult();
					String msg = "Device registered, registration ID=" + token;
					Log.d(TAG, "FCM registerInBackground .." + msg);
					//通知後端取得的gcm registration ID
					CommunicationLogic.getInstance().updateDeviceToken(BOBSystemManager.getInstance().getUserToken(), token);
					BOBPreferenceLogic.getInstance().saveGCMToken(token);

					Log.d(TAG, msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});



	}

	//GCM End ========================================================================================================

}
