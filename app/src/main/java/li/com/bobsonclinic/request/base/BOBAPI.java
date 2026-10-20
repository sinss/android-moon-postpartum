package li.com.bobsonclinic.request.base;

/**
 * Agricola
 * Created by Milla.Wang on 2016/1/5.
 * Copyright © 2016 bobsonclinic. All rights reserved.
 */

public class BOBAPI {


    private static final String AUTH_TAIWAN_DOMAIN = "https://www.happymonth.cc/";
    private static final String AUTH_CHINA_DOMAIN = "http://120.24.185.83/";

    public static final String USER_LOGIN = "auth/get-token";
    public static final String BABY_EXAM_DATA = "baby-exams-data";
    public static final String BABY_CAMERA_DATA = "baby-camera-data";
    public static final String BABY_DATA = "babies-data";
    public static final String MOM_DATA = "mom-data";
    public static final String MOM_EXAM_DATA = "mom-exams-data";
    public static final String INTRO_LIST = "intro-list";
    public static final String INTRO = "intro";
    public static final String SUB_INTRO_LIST = "sub-intro-list";
    public static final String SUB_INTRO = "sub-intro";
    public static final String TODAY_SCHEDULE = "today-schedule";
    public static final String NOTIFCATION = "notifications";
    public static final String MOM_EXAM_TEM_PUL = "mom-exam-tem-pul-pre";
    public static final String MOM_WEIGHT = "mom-weight";
    public static final String MOM_BREATH = "mom-breath";
    public static final String MOM_TEMPERATURE = "mom-temperature";
    public static final String MOM_PULSE = "mom-pulse";
    public static final String MOM_PRESSURE = "mom-pressure";
    public static final String BABY_TEMPERATURE = "baby-temperature";
    public static final String BABY_WEIGHT = "baby-weight";
    public static final String BABY_JAUNDICE_INDEX = "baby-jaundice-index";
    public static final String BABY_MILK = "baby-milk";
    public static final String BABY_PULSE = "baby-pulse";
    public static final String BABY_BREATH = "baby-breath";
    public static final String BABY_SWIM = "baby-swim";
    public static final String EXPLAIN = "explain";
    public static final String MONTH_ACTIVITIES = "month-activities";
    public static final String ACTIVITY = "activity";
    public static final String UNCHECK_ACTIVITY = "uncheck-activity";
    public static final String CHECK_ACTIVITY = "check-activity";
    public static final String VAS_LIST = "vas-list";
    public static final String VAS = "vas";
    public static final String CHECK_VAS = "check-vas";
    public static final String UNCHECK_VAS = "uncheck-vas";
    public static final String GET_CONSUME_LIST = "get-consume-list";
    public static final String UPDATE_DEVICE_TOKEN = "update-device-token";
    public static final String POST_BABY_FILE = "post-baby-file";

    public static final String POST_RESET_PASSWORD = "api/reset-password";
    public static final String POST_UPDATE_STREAM = "api/update-stream";
    public static final String POST_BUG_REPORT = "api/bug_report";



    private static boolean mIsChina = false;


    public static String getAuthDomainURL(){
        if(isChina()){
            return AUTH_CHINA_DOMAIN;
        } else {
            return AUTH_TAIWAN_DOMAIN;
        }
    }
    public static String getPostLoginURL(){

        return getAuthDomainURL() + USER_LOGIN;

    }


    public static String getIntroURL(){

        return getAuthDomainURL() +INTRO;

    }

    public static String getIntroListURL(){

        return getAuthDomainURL() +INTRO_LIST;

    }

    public static String getSubIntroURL(){

        return getAuthDomainURL() +SUB_INTRO;

    }

    public static String getSubIntroListURL(){

        return getAuthDomainURL() +SUB_INTRO_LIST;

    }

    public static String getBabyExamDataURL(){

        return getAuthDomainURL() + BABY_EXAM_DATA;

    }

    public static String getBabyCameraDataURL(){

        return getAuthDomainURL() + BABY_CAMERA_DATA;

    }

    public static String getBabyDataURL(){

        return getAuthDomainURL() + BABY_DATA;

    }

    public static String getMomDataURL(){

        return getAuthDomainURL() + MOM_DATA;

    }

    public static String getMomExamDataURL(){

        return getAuthDomainURL() + MOM_EXAM_DATA;

    }

    public static String getTodayScheduleURL(){

        return getAuthDomainURL() + TODAY_SCHEDULE;

    }

    public static String getNotifcationURL(){

        return getAuthDomainURL() + NOTIFCATION;

    }

    public static String getMomExamTemPulURL(){

        return getAuthDomainURL() + MOM_EXAM_TEM_PUL;

    }

    public static String getMomWeightURL(){

        return getAuthDomainURL() + MOM_WEIGHT;

    }

    public static String getMomBreathURL(){

        return getAuthDomainURL() + MOM_BREATH;

    }

    public static String getMomTemperatureURL() {

        return getAuthDomainURL() + MOM_TEMPERATURE;
    }

    public static String getMomPulseURL() {

        return getAuthDomainURL() + MOM_PULSE;
    }

    public static String getMomPressureURL() {

        return getAuthDomainURL() + MOM_PRESSURE;
    }

    public static String getBabyTemperatureURL() {

        return getAuthDomainURL() + BABY_TEMPERATURE;
    }

    public static String getBabyWeightURL() {

        return getAuthDomainURL() + BABY_WEIGHT;
    }

    public static String getBabyJundiceIndexURL() {

        return getAuthDomainURL() + BABY_JAUNDICE_INDEX;
    }

    public static String getBabyMilkURL() {

        return getAuthDomainURL() + BABY_MILK;
    }

    public static String getBabyPulseURL() {

        return getAuthDomainURL() + BABY_PULSE;
    }

    public static String getBabyBreathURL() {

        return getAuthDomainURL() + BABY_BREATH;
    }

    public static String getBabySwimURL() {

        return getAuthDomainURL() + BABY_SWIM;
    }

    public static String getExplainURL() {

        return getAuthDomainURL() + EXPLAIN;
    }

    public static String getMonthActivitiesURL() {

        return getAuthDomainURL() + MONTH_ACTIVITIES;
    }

    public static String getActivityURL() {

        return getAuthDomainURL() + ACTIVITY;
    }

    public static String getUncheckActivityURL() {

        return getAuthDomainURL() + UNCHECK_ACTIVITY;
    }

    public static String getCheckActivityURL() {

        return getAuthDomainURL() + CHECK_ACTIVITY;
    }

    public static String getVasListURL() {

        return getAuthDomainURL() + VAS_LIST;
    }

    public static String getVasURL() {

        return getAuthDomainURL() + VAS;
    }

    public static String getCheckVasURL() {

        return getAuthDomainURL() + CHECK_VAS;
    }

    public static String getUnCheckVasURL() {

        return getAuthDomainURL() + UNCHECK_VAS;
    }

    public static String getConsumeListURL() {

        return getAuthDomainURL() + GET_CONSUME_LIST;
    }

    public static String getUpdateDeviceTokenURL() {

        return getAuthDomainURL() + UPDATE_DEVICE_TOKEN;
    }

    public static String postBabyFileURL() {

        return getAuthDomainURL() + POST_BABY_FILE;
    }

    public static String getResetPasswordURL() {
        return getAuthDomainURL() + POST_RESET_PASSWORD;
    }

    public static String getUpdateStreamURL() {
        return getAuthDomainURL() + POST_UPDATE_STREAM;
    }

    public static String getBugReportURL() {
        return getAuthDomainURL() + POST_BUG_REPORT;
    }
    public static void setChina(boolean isTest) {
        mIsChina = isTest;
    }

    public static boolean isChina() {
        return mIsChina;
    }
}
