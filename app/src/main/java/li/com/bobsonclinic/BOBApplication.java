package li.com.bobsonclinic;

/**
 * Agricola
 *
 * Created by Milla.Wang on 2016/1/5.
 * Copyright © 2016 bobsonclinic. All rights reserved.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

//import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.ExecutionException;

import io.realm.Realm;
//import io.salyangoz.updateme.UpdateMe;
//import io.salyangoz.updateme.listener.OnPositiveButtonClickListener;
import io.realm.RealmConfiguration;
import li.com.bobsonclinic.data.realm.Photo;
import li.com.bobsonclinic.util.Config;
import li.com.bobsonclinic.util.VersionChecker;


public class BOBApplication extends android.app.Application {
	private static BOBApplication appInstance;
	private static Context context;
	private static Handler mHandler;
	
	private boolean isNetworkConnectionOK;

	@Override
	public void onCreate() {
		super.onCreate();
		BOBApplication.context = getApplicationContext();
 		mHandler = new Handler();

		if (appInstance == null) {
			appInstance = this;
		}
		Realm.init(this);

		// Flurry start
		//FlurryAgent.setLogEnabled(false); // configure Flurry
		//FlurryAgent.init(this, getString(R.string.flurry_key));// init Flurry
		// Flurry end

		IntentFilter mNetworkStateFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(mNetworkStateReceiver, mNetworkStateFilter);

		Photo.importAllSavedPhotos();
		Photo.clearUselessPhotos();
	}

	public static BOBApplication getInstance() {
		return appInstance;
	}

	public static Context getAppContext() {
		return BOBApplication.context;
	}


	public boolean getNetworkConnectivity() {


		return isNetworkConnectionOK;
	}

	private final BroadcastReceiver mNetworkStateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			isNetworkConnectionOK =  false;

			ConnectivityManager cm = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
			if (cm == null) {
				isNetworkConnectionOK = false;
				Log.d("mNetworkStateReceiver", "mNetworkStateReceiver dis");
				context.sendBroadcast(new Intent(Config.BROADCAST_DISCONNECTED));
			} else {
				NetworkInfo[] infos = cm.getAllNetworkInfo();
				if (infos != null) {
					for (NetworkInfo networkInfo : infos) {
						if(networkInfo.getState() == NetworkInfo.State.CONNECTED){
							isNetworkConnectionOK =  true;
							Log.d("mNetworkStateReceiver", "mNetworkStateReceiver conn");
							sendBroadcast(new Intent(Config.BROADCAST_CONNECTED));
						}
					}
				}

			}
		}
	};
	
	public static String joinStackTrace(Throwable e) {
		StringWriter writer = null;
		try {
			writer = new StringWriter();
			joinStackTrace(e, writer);
			return writer.toString();
		} finally {
			if (writer != null)
				try {
					writer.close();
				} catch (IOException e1) {
					// ignore
				}
		}
	}
	
	public static void joinStackTrace(Throwable e, StringWriter writer) {
		PrintWriter printer = null;
		try {
			printer = new PrintWriter(writer);

			while (e != null) {

				printer.println(e);
				StackTraceElement[] trace = e.getStackTrace();
				for (int i = 0; i < trace.length; i++)
					printer.println("\tat " + trace[i]);

				e = e.getCause();
				if (e != null)
					printer.println("Caused by:\r\n");
			}
		} finally {
			if (printer != null)
				printer.close();
		}
	}
	
	public static void showToastMsg(final String message, final boolean isLong){
		if(mHandler != null){
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					if(context != null)
						Toast.makeText(context, message, isLong?Toast.LENGTH_LONG:Toast.LENGTH_SHORT).show();
				}
			});
		}
	}
}
