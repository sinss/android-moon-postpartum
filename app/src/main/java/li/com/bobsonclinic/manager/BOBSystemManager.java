package li.com.bobsonclinic.manager;

/**
 * Agricola
 * Created by Milla.Wang on 2016/1/5.
 * Copyright © 2016 bobsonclinic. All rights reserved.
 */

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import li.com.bobsonclinic.BOBApplication;
import li.com.bobsonclinic.R;
import li.com.bobsonclinic.logic.BOBPreferenceLogic;
import li.com.bobsonclinic.viewer.CustomAlertDialogBuilder;

public class BOBSystemManager {
	private static BOBSystemManager systemMgr = null;
	private Context mContext;
	private TelephonyManager mTelephonyManager;
	private String mServiceEmail = "";
	private static boolean DEBUG_MODE_ON =  false;


	public static synchronized BOBSystemManager getInstance(){
		if(systemMgr == null)
			systemMgr = new BOBSystemManager();
		return systemMgr;
	} 
	
	private BOBSystemManager(){
		mContext = BOBApplication.getAppContext();
		mTelephonyManager = (TelephonyManager) mContext
				.getSystemService(Context.TELEPHONY_SERVICE);
	}

	public void setDebugOn(boolean isOn){
		DEBUG_MODE_ON = isOn;
	}

	public boolean isDebugMode(){
		return DEBUG_MODE_ON;
	}


	public boolean isLogin(){
		String token =  BOBPreferenceLogic.getInstance().getUserToken();
		if(token == null || token.equals("")){
			return false;
		}
		return true;
	}

//	public String getCurrentUserId(){
//		return BOBPreferenceLogic.getInstance().getUserRgistrationId();
//	}

	public String getUserToken(){
		return BOBPreferenceLogic.getInstance().getUserToken();
	}


	public int getCurrentVersionCode() {
		PackageInfo pinfo = null;
		try {
			pinfo = BOBApplication
					.getAppContext()
					.getPackageManager()
					.getPackageInfo(BOBApplication.getAppContext().getPackageName(), 0);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		int currentVersionCode = pinfo.versionCode;
		return currentVersionCode;
	}

	public String getCurrentVersionName() {
		PackageInfo pinfo = null;
		try {
			pinfo = BOBApplication
					.getAppContext()
					.getPackageManager()
					.getPackageInfo(BOBApplication.getAppContext().getPackageName(), 0);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		if(pinfo != null)
			return pinfo.versionName;
		return "";
	}
	
	/**
	 * 取得目前網路狀態
	 * 
	 * @return <tt>true</tt> If network is connected <br>
	 *         <tt>false</tt> if network is <b>NOT</b> connected
	 */
	public boolean checkNetWorkState() {
		return BOBApplication.getInstance().getNetworkConnectivity();
	}
	

	/**
	 * 取得目前網路狀態 and 顯示提示訊息
	 * 
	 * @return <tt>true</tt> If network is connected <br>
	 *         <tt>false</tt> if network is <b>NOT</b> connected
	 */
	public  boolean checkNetWorkState(Context context) {

		if (!BOBApplication.getInstance().getNetworkConnectivity()) {
			showNetworkDialog(context);
            return false;
		}

		return BOBApplication.getInstance().getNetworkConnectivity();
	}

	private Dialog netWorkdialog;
	public void showNetworkDialog(Context context){
		if(netWorkdialog == null) {
			netWorkdialog = new CustomAlertDialogBuilder(context, R.style.customAlertDialog, R.layout.custom_alert_wifi);
			netWorkdialog.setCancelable(true);
			netWorkdialog.show();
		} else {
			netWorkdialog.show();
		}
	}
	
	public String getManufacturer(){
		return Build.MANUFACTURER;
	}
	
	/**
	 * 改用螢幕邊長 > 600
	 * 
	 * @return
	 */
	public boolean isPad() {
		if (findWindowResolution() >= 600)
			return true;

		return false;
	}
	
	/**
	 * 螢幕邊長 > 768
	 * 
	 * @return
	 */
	public boolean isPad768() {
		if (findWindowResolution() >= 768)
			return true;

		return false;
	}
	
	public String getDeviceParam() {
		if (isPad())
			return "gpad";
		else
			return "gphone";
	}
	

	/**
	 * 請給我螢幕小邊的值，如320x480給我320即可。
	 * 
	 * @return
	 */
	public int findWindowResolution() {
		if (BOBApplication.getAppContext() != null) {
			// 取得螢幕解析度
			DisplayMetrics dm = new DisplayMetrics();
			WindowManager manager = (WindowManager) BOBApplication
					.getAppContext().getSystemService(Context.WINDOW_SERVICE);
			manager.getDefaultDisplay().getMetrics(dm);

			int vWidth = (int) (dm.widthPixels / dm.density);
			int vHeight = (int) (dm.heightPixels / dm.density);

			return (vWidth < vHeight) ? vWidth : vHeight;
		}
		return 400;
	}

	/**
	 * 請給我螢幕高的值。
	 *
	 * @return
	 */
	public int findWindowResolutionHeight(Context context) {
			// 取得螢幕解析度
			DisplayMetrics dm = new DisplayMetrics();
			WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
			manager.getDefaultDisplay().getMetrics(dm);

			int vHeight = (int) (dm.heightPixels / dm.density);

			return vHeight;
	}

	/**
	 * 請給我螢幕寬的值
	 *
	 */
	public int findWindowResolutionWidth(){
		// 取得螢幕解析度
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager manager = (WindowManager) BOBApplication
				.getAppContext().getSystemService(Context.WINDOW_SERVICE);
		manager.getDefaultDisplay().getMetrics(dm);

		int vWidth = (int) (dm.widthPixels / dm.density);

		return vWidth;
	}

	/**
	 * 請給我螢幕寬的值
	 *
	 */
	public int findWindowWidthPixes(){
		// 取得螢幕解析度
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager manager = (WindowManager) BOBApplication
				.getAppContext().getSystemService(Context.WINDOW_SERVICE);
		manager.getDefaultDisplay().getMetrics(dm);

		return dm.widthPixels;
	}

	/**
	 * 請給我螢幕寬的值
	 *
	 */
	public int findWindowHightPixes(){
		// 取得螢幕解析度
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager manager = (WindowManager) BOBApplication
				.getAppContext().getSystemService(Context.WINDOW_SERVICE);
		manager.getDefaultDisplay().getMetrics(dm);

		return dm.heightPixels;
	}

	/**
	 * convert dp to pixel
	 * @param dp
	 * @return pixel
	 */
	public  int convertDpToPixel(float dp, Context context) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		int px = (int) (dp * (metrics.densityDpi / 160f));
		return px;
	}

	/**
	 * convert dp to pixel
	 * @param dp
	 * @return pixel
	 */
	public int convertDpToPixel(float dp) {
		Context context = BOBApplication.getAppContext();
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		int px = (int) (dp * (metrics.densityDpi / 160f));
		return px;
	}

	/**
	 * get OS Version
	 * @return version
	 */
	public String getOSVersion() {
		String version = Build.VERSION.RELEASE;
		version = version.replace(" ", "_");
		return version;
	}

	/**
	 * get Model
	 * @return vendor
	 */
	public String getModel() {
		String vendor = Build.MODEL;
		vendor = vendor.replace(" ", "_");
		return vendor;
	}

	/**
	 * get Device
	 * @return DEVICE
	 */
	public String getDevice() {
		return Build.DEVICE;
	}

	/**
	 * get Brand
	 * @return Brand
	 */
	public String getBrand() {
		String brand = Build.BRAND;
		brand = brand.replace(" ", "_");
		return brand;
	}

	/**
	 * get UuId
	 * @return UuId
	 */
	public String getIMEI() {
		String imeiID = mTelephonyManager.getDeviceId();

		if (imeiID == null || imeiID.trim().equals("") || imeiID.length() < 2) {
			imeiID = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
		}

		if (imeiID == null || imeiID.trim().equals("") || imeiID.length() < 2){
			String model = getModel();
			if(model != null){
				int hashCode = model.hashCode();
				if(hashCode > 0){
					imeiID = "nop"+hashCode;
				}else{
					imeiID = "non"+hashCode;
				}
			}
		}

		if(imeiID == null || imeiID.trim().equals("") || imeiID.length() < 2){
			imeiID = "no"+"IMEI".hashCode();
		}

		if (imeiID == null || imeiID.equals("")){
			throw new RuntimeException("what!?");
		}

		return imeiID;
	}

	/**
	 * 将String转换成InputStream
	 *
	 * @param in
	 * @return
	 * @throws Exception
	 */
	public static InputStream StringTOInputStream(String in) throws Exception {

		ByteArrayInputStream is = new ByteArrayInputStream(in.getBytes());
		return is;
	}

	/**
	 * 将InputStrea轉換成某種字符编碼的String
	 *
	 * @param in
	 * @return
	 * @throws Exception
	 */
	public static String InputStreamTOString(InputStream in) throws Exception {

		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] data = new byte[1024];
		int count = -1;
		while ((count = in.read(data, 0, 1024)) != -1)
			outStream.write(data, 0, count);

		data = null;
		return new String(outStream.toByteArray());
	}

	public static void InputstreamToFile(InputStream inputStream, File file) {
		try {

			// write the inputStream to a FileOutputStream
			OutputStream out = new FileOutputStream(file);

			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = inputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}

			inputStream.close();
			out.flush();
			out.close();
		} catch (IOException e) {
			Log.d("LogSystem", e.getMessage());
		}
	}


	/**
	 * Uses static final constants to detect if the device's platform version is Lollipop or
	 * later.
	 */
	public static boolean hasJellyBean() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
	}

	/**
	 * Uses static final constants to detect if the device's platform version is Lollipop or
	 * later.
	 */
	public static boolean hasLollipop() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
	}

}
