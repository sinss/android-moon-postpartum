package li.com.bobsonclinic.logic;

/**
 * Agricola
 * Created by Milla.Wang on 2016/1/5.
 * Copyright © 2016 bobsonclinic. All rights reserved.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import li.com.bobsonclinic.BOBApplication;


public class BOBPreferenceLogic {

    private static BOBPreferenceLogic prefMgr = null;
    private static final String TAG = BOBPreferenceLogic.class.getSimpleName();
    public static synchronized BOBPreferenceLogic getInstance(){
        if(prefMgr == null)
            prefMgr = new BOBPreferenceLogic();
        return prefMgr;
    }

    private String mAccount = "guest";

    protected static final String PREF_HEADER = "li.com.bobsonclinic.pad";
    private static final String PREF_USER_TOKEN = "member_user_token";
    private static final String PREF_USER_REGISTRATION_ID = "member_user_regstration_id";
    private static final String PREF_USER_PASSWORD = "member_user_password";
    private static final String PREF_USER_VIDEO_SOURCE = "member_user_video_source";


    /**
     * GCM儲存regid的key
     */
    public static final String PROPERTY_REG_ID = "property_reg_id";

    /**
     * GCM儲存app version的key
     */
    public static final String PROPERTY_APP_VERSION = "property_app_version";


    protected Context mContext;
    protected SharedPreferences mPreference;

    private BOBPreferenceLogic(){
        mContext = BOBApplication.getAppContext();
        mPreference = mContext.getSharedPreferences(PREF_HEADER, Context.MODE_MULTI_PROCESS);
    }

    /**
     * @author Milla_Wang
     * @since 2016.01.19
     */
    public void logoutData() {
        mPreference.edit().putString(PREF_USER_TOKEN, "")
                .putString(PREF_USER_VIDEO_SOURCE, "")
                .commit();
    }

    /**
     * 取得本地儲存之GCM token
     * @author Milla_Wang
     * @since 2016.01.19
     * @return
     */
    public String getGCMToken() {
        SharedPreferences pref = BOBApplication.getAppContext().getSharedPreferences(PREF_HEADER, Context.MODE_PRIVATE);
        return pref.getString(PROPERTY_REG_ID, "");
    }

    /**
     * 儲存GCM Token
     * @author Milla_Wang
     * @since 2016.01.19
     * @param token
     */
    public void saveGCMToken(String token) {
        if (!"".equals(token)) {
            Log.i(TAG, "儲存GCM Token : " + token);
            SharedPreferences pref = BOBApplication.getAppContext().getSharedPreferences(PREF_HEADER, Context.MODE_PRIVATE);
            pref.edit().putString(PROPERTY_REG_ID, token).commit();
        }
    }

    /**
     * @author Milla_Wang
     */
    public void setUserRgistrationId(String id) {
        mPreference.edit().putString(PREF_USER_REGISTRATION_ID, id).commit();
    }

    /**
     * @author Milla_Wang
     */
    public String getUserRgistrationId() {
        return mPreference.getString(PREF_USER_REGISTRATION_ID, "");
    }

    /**
     * @author Milla_Wang
     */
    public void setUserToken(String token) {
        mPreference.edit().putString(PREF_USER_TOKEN, token).commit();
    }

    /**
     * @author Milla_Wang
     */
    public String getUserToken() {
        return mPreference.getString(PREF_USER_TOKEN, "");
    }

    /**
     * @author Milla_Wang
     */
    public void setUserPassword(String password) {
        mPreference.edit().putString(PREF_USER_PASSWORD, password).commit();
    }

    /**
     * @author Milla_Wang
     */
    public String getUserPassword() {
        return mPreference.getString(PREF_USER_PASSWORD, "");
    }

    /**
     * @author Milla_Wang
     */
    public void setUserRTSP(String url) {
        mPreference.edit().putString(PREF_USER_VIDEO_SOURCE, url).commit();
    }

    /**
     * @author Milla_Wang
     */
    public String getUserRTSP() {
        return mPreference.getString(PREF_USER_VIDEO_SOURCE, "");
    }
}
