package li.com.bobsonclinic.logic;


/**
 * Agricola
 * Created by Milla.Wang on 2016/1/5.
 * Copyright © 2016 bobsonclinic. All rights reserved.
 */

import android.util.Log;

import li.com.bobsonclinic.data.server.base.BOBRequestListener;
import li.com.bobsonclinic.provider.BusNotifyProvider;
import li.com.bobsonclinic.provider.event.RequestEvent;
import li.com.bobsonclinic.request.BOBActivityRequest;
import li.com.bobsonclinic.request.BOBBabiesDataRequest;
import li.com.bobsonclinic.request.BOBBabyBreathRequest;
import li.com.bobsonclinic.request.BOBBabyCameraDataRequest;
import li.com.bobsonclinic.request.BOBBabyExamDataRequest;
import li.com.bobsonclinic.request.BOBBabyFileUploadRequest;
import li.com.bobsonclinic.request.BOBBabyJaundiceIndexRequest;
import li.com.bobsonclinic.request.BOBBabyMilkRequest;
import li.com.bobsonclinic.request.BOBBabyPulseRequest;
import li.com.bobsonclinic.request.BOBBabySwimRequest;
import li.com.bobsonclinic.request.BOBBabyTemperatureRequest;
import li.com.bobsonclinic.request.BOBBabyWeightRequest;
import li.com.bobsonclinic.request.BOBBugReportRequest;
import li.com.bobsonclinic.request.BOBCheckActivityRequest;
import li.com.bobsonclinic.request.BOBConsumerListRequest;
import li.com.bobsonclinic.request.BOBExplainRequest;
import li.com.bobsonclinic.request.BOBIntroListRequest;
import li.com.bobsonclinic.request.BOBIntroRequest;
import li.com.bobsonclinic.request.BOBLoginRequest;
import li.com.bobsonclinic.request.BOBMomBreathRequest;
import li.com.bobsonclinic.request.BOBMomDataRequest;
import li.com.bobsonclinic.request.BOBMomExamDataRequest;
import li.com.bobsonclinic.request.BOBMomExamTemPulRequest;
import li.com.bobsonclinic.request.BOBMomPressureRequest;
import li.com.bobsonclinic.request.BOBMomPulseRequest;
import li.com.bobsonclinic.request.BOBMomTemperatureRequest;
import li.com.bobsonclinic.request.BOBMomWeightRequest;
import li.com.bobsonclinic.request.BOBMonthActivitiesRequest;
import li.com.bobsonclinic.request.BOBNotifcationRequest;
import li.com.bobsonclinic.request.BOBResetPasswordRequest;
import li.com.bobsonclinic.request.BOBSubIntroListRequest;
import li.com.bobsonclinic.request.BOBSubIntroRequest;
import li.com.bobsonclinic.request.BOBTodayScheduleRequest;
import li.com.bobsonclinic.request.BOBUncheckActivityRequest;
import li.com.bobsonclinic.request.BOBUpdateDeviceTokenRequest;
import li.com.bobsonclinic.request.BOBUpdateStreamRequest;
import li.com.bobsonclinic.request.BOBVasCheckRequest;
import li.com.bobsonclinic.request.BOBVasListRequest;
import li.com.bobsonclinic.request.BOBVasRequest;
import li.com.bobsonclinic.request.BOBVasUnCheckRequest;
import li.com.bobsonclinic.request.base.BOBAPI;
import li.com.bobsonclinic.request.base.BOBRequest;
import zk.android.networking.Request;
import zk.android.networking.RequestError;
import zk.android.networking.RequestManager;

public class CommunicationLogic {

    private static final String TAG = CommunicationLogic.class.getSimpleName();
    private static CommunicationLogic mLogic = null;

    public static synchronized CommunicationLogic getInstance() {
        if (mLogic == null) {
            mLogic = new CommunicationLogic();
        }
        return mLogic;
    }

    private final static int RETRY_MAX = 3;

    private boolean IS_REQUEST = true;      //test for close request.
    private final RequestManager reqMgr;
    private final RequestManager retryMgr;

    //private final Handler handler;

    private CommunicationLogic() {
        reqMgr = new RequestManager(2);
        retryMgr = new RequestManager(1);
        //Looper.prepare();
       // handler = new Handler();
        //Looper.loop();
        BusNotifyProvider.getInstance().register(this);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        BusNotifyProvider.getInstance().unregister(this);
    }

    public void stopAllRequests() {
        reqMgr.stopRequests();
    }

    public void stopRequest(long id) {
        reqMgr.removeRequest(id);
    }

    private Request.RequestListener reqListener = new Request.RequestListener() {
        @Override
        public void onStart(Request request) {
            RequestEvent.Event event = RequestEvent.Event.START;
            BusNotifyProvider.getInstance().post(new RequestEvent(request.getClass(), request.getId(), event));
        }

        @Override
        public void onEnd(Request request) {
            RequestEvent.Event event = RequestEvent.Event.END;
            BusNotifyProvider.getInstance().post(new RequestEvent(request.getClass(), request.getId(), event));
        }

        @Override
        public void onFail(Request request, RequestError requestError) {
            RequestEvent.Event event = RequestEvent.Event.ERROR;
            RequestEvent reqEvent =  new RequestEvent(request.getClass(), request.getId(), event);

            if(requestError != null){
                if(requestError.getErrorType() == RequestError.ErrorType.TIMEOUT){
                    final BOBRequest bobRequest = (BOBRequest)request;

                }
                reqEvent.setData(requestError);
                reqEvent.setResponseCode(requestError.getResponseCode());
            } else {
                requestError = new RequestError(RequestError.ErrorType.EXCEPTION);
                requestError.setErrorCode("Error");
                reqEvent.setData(requestError);
            }
//            Log.d("onFail", "request.getClass(): "+request.getClass() +",reqEvent: " + reqEvent.getData()+", "+reqEvent.getRequestEvent());
            BusNotifyProvider.getInstance().post(reqEvent);

            if(request instanceof BOBRequest){
                BOBRequestListener l = ((BOBRequest) request).getBOBListener();
                if(l != null){
                    l.onResult(false, requestError);
                }
            }
        }

        @Override
        public void onSuccess(Request request, Object o) {
            RequestEvent.Event event = RequestEvent.Event.SUCCEED;
            if(o !=null){
                Log.d("reqListener" ,"reqListener onSuccess "+o);
            }
            RequestEvent requestEvent = new RequestEvent(request.getClass(), request.getId(), event);
            requestEvent.setData(o);
            BusNotifyProvider.getInstance().post(requestEvent);

            if(request instanceof BOBRequest){
                BOBRequestListener l = ((BOBRequest) request).getBOBListener();
                if(l != null){
                    l.onResult(true, o);
                }
            }
        }
    };


    public void removeAllRetryRequest(){
        retryMgr.stopRequests();
    }

    /**
     * User Login
     * @param userName, password, postId
     * @return id
     */
    public long postUserLogin(String userName, String password, String token) {
        if(IS_REQUEST) {

            String url = String.format(BOBAPI.getPostLoginURL());
            long id = Request.generateRequestId(BOBLoginRequest.class, true);
            long priority = 0;

            BOBLoginRequest request = new BOBLoginRequest(id, url, userName, password, token);
            request.setRequestListener(reqListener);
            reqMgr.addRequest(request, priority);

            return id;
        }
        return -1;
    }

    /**
     * AboutUS or Education
     * @param token, category
     * @return id
     */
    public long postIntroList(String token, String category) {
        if(IS_REQUEST) {

            String url = String.format(BOBAPI.getIntroListURL());
            long id = Request.generateRequestId(BOBIntroListRequest.class, false);
            long priority = 0;

            BOBIntroListRequest request = new BOBIntroListRequest(id, url, token, category);
            request.setRequestListener(reqListener);
            reqMgr.addRequest(request, priority);

            return id;
        }
        return -1;
    }

    /**
     * AboutUS or Education
     * @param token, introId
     * @return id
     */
    public long postIntro(String category, String token, String introId) {
        if(IS_REQUEST) {

            String url = String.format(BOBAPI.getIntroURL());
            long id = Request.generateRequestId(BOBIntroRequest.class, false);
            long priority = 0;

            BOBIntroRequest request = new BOBIntroRequest(id, url, token, introId, category);
            request.setRequestListener(reqListener);
            reqMgr.addRequest(request, priority);

            return id;
        }
        return -1;
    }

    /**
     * AboutUS or Education Sub intro
     * @param token, introId
     * @return id
     */
    public long postSubIntroList(String token, String introId, String category) {
        if(IS_REQUEST) {

            String url = String.format(BOBAPI.getSubIntroListURL());
            long id = Request.generateRequestId(BOBSubIntroListRequest.class, false);
            long priority = 0;

            BOBSubIntroListRequest request = new BOBSubIntroListRequest(id, url, token, introId, category);
            request.setRequestListener(reqListener);
            reqMgr.addRequest(request, priority);

            return id;
        }
        return -1;
    }

    /**
     * AboutUS or Education Sub intro
     * @param token, subintroId
     * @return id
     */
    public long postSubIntro(String token, String subintroId, String introId, String categoryId) {
        if(IS_REQUEST) {

            String url = String.format(BOBAPI.getSubIntroURL());
            long id = Request.generateRequestId(BOBSubIntroRequest.class, false);
            long priority = 0;

            BOBSubIntroRequest request = new BOBSubIntroRequest(id, url, token, subintroId, introId, categoryId);
            request.setRequestListener(reqListener);
            reqMgr.addRequest(request, priority);

            return id;
        }
        return -1;
    }

    /**
     * babies-data
     * @param token, category
     * @return id
     */
    public long postBabiesData(String token) {
        if(IS_REQUEST) {

            String url = String.format(BOBAPI.getBabyDataURL());
            long id = Request.generateRequestId(BOBBabiesDataRequest.class, true);
            long priority = 0;

            BOBBabiesDataRequest request = new BOBBabiesDataRequest(id, url, token);
            request.setRequestListener(reqListener);
            reqMgr.addRequest(request, priority);

            return id;
        }
        return -1;
    }

    /**
     * Momey-data
     * @param token
     * @return id
     */
    public long postMomData(String token) {
        if(IS_REQUEST) {

            String url = String.format(BOBAPI.getMomDataURL());
            long id = Request.generateRequestId(BOBMomDataRequest.class, true);
            long priority = 0;

            BOBMomDataRequest request = new BOBMomDataRequest(id, url, token);
            request.setRequestListener(reqListener);
            reqMgr.addRequest(request, priority);

            return id;
        }
        return -1;
    }

    /**
     * mom-exams-data
     * @param token, babyId
     * @return id
     */
    public long postMomExamData(String token, String babyId) {
        if(IS_REQUEST) {

            String url = String.format(BOBAPI.getMomExamDataURL());
            long id = Request.generateRequestId(BOBMomExamDataRequest.class, true);
            long priority = 0;

            BOBMomExamDataRequest request = new BOBMomExamDataRequest(id, url, token, babyId);
            request.setRequestListener(reqListener);
            reqMgr.addRequest(request, priority);

            return id;
        }
        return -1;
    }

    /**
     * baby-exams-data
     * @param token, babyId
     * @return id
     */
    public long postBabyExamData(String token, String babyId) {
        if(IS_REQUEST) {

            String url = String.format(BOBAPI.getBabyExamDataURL());
            long id = Request.generateRequestId(BOBBabyExamDataRequest.class, true);
            long priority = 0;

            BOBBabyExamDataRequest request = new BOBBabyExamDataRequest(id, url, token, babyId);
            request.setRequestListener(reqListener);
            reqMgr.addRequest(request, priority);

            return id;
        }
        return -1;
    }

    /**
     * baby-camera-data
     * @param token, babyId, meidaType, filefield
     * @return id
     */
    public long postCameraData(String token, String babyId) {
        if(IS_REQUEST) {

            String url = String.format(BOBAPI.getBabyCameraDataURL());
            long id = Request.generateRequestId(BOBBabyExamDataRequest.class, true);
            long priority = 0;

            BOBBabyCameraDataRequest request = new BOBBabyCameraDataRequest(id, url, token, babyId);
            request.setRequestListener(reqListener);
            reqMgr.addRequest(request, priority);

            return id;
        }
        return -1;
    }

    public long postCameraData(String token, String babyId, Request.RequestListener l) {
        if(IS_REQUEST) {

            String url = String.format(BOBAPI.getBabyCameraDataURL());
            long id = Request.generateRequestId(BOBBabyExamDataRequest.class, true);
            long priority = 0;

            BOBBabyCameraDataRequest request = new BOBBabyCameraDataRequest(id, url, token, babyId);
            request.setRequestListener(l);
            reqMgr.addRequest(request, priority);

            return id;
        }
        return -1;
    }

    /**
     * post-baby-file
     * @param token, babyId, meidaType, filefield
     * @return id
     */
    public long postBabyFile(String token, String babyId, String meidaType, String filefield) {
        if(IS_REQUEST) {

            String url = String.format(BOBAPI.getBabyCameraDataURL());
            long id = Request.generateRequestId(BOBBabyExamDataRequest.class, true);
            long priority = 0;

            BOBBabyFileUploadRequest request = new BOBBabyFileUploadRequest(id, url, token, babyId, meidaType, filefield);
            request.setRequestListener(reqListener);
            reqMgr.addRequest(request, priority);

            return id;
        }
        return -1;
    }

    /**
     * today-schedule
     * @param token
     * @return id
     */
    public long postTodaySchedule(String token) {
        if(IS_REQUEST) {

            String url = String.format(BOBAPI.getTodayScheduleURL());
            long id = Request.generateRequestId(BOBTodayScheduleRequest.class, true);
            long priority = 0;

            BOBTodayScheduleRequest request = new BOBTodayScheduleRequest(id, url, token);
            request.setRequestListener(reqListener);
            reqMgr.addRequest(request, priority);

            return id;
        }
        return -1;
    }

    /**
     * notifications
     * @param token
     * @return id
     */
    public long postNotifications(String token) {
        if(IS_REQUEST) {

            String url = String.format(BOBAPI.getNotifcationURL());
            long id = Request.generateRequestId(BOBNotifcationRequest.class, true);
            long priority = 0;

            BOBNotifcationRequest request = new BOBNotifcationRequest(id, url, token);
            request.setRequestListener(reqListener);
            reqMgr.addRequest(request, priority);

            return id;
        }
        return -1;
    }

    /**
     * mom-exam-tem-pul-pre
     * @param token
     * @return id
     */
    public long postMomExamTemPulPre(String token) {
        if(IS_REQUEST) {

            String url = String.format(BOBAPI.getMomExamTemPulURL());
            long id = Request.generateRequestId(BOBMomExamTemPulRequest.class, true);
            long priority = 0;

            BOBMomExamTemPulRequest request = new BOBMomExamTemPulRequest(id, url, token);
            request.setRequestListener(reqListener);
            reqMgr.addRequest(request, priority);

            return id;
        }
        return -1;
    }

    /**
     * mom-weight
     * @param token
     * @return id
     */
    public long postMomWeight(String token) {
        if(IS_REQUEST) {

            String url = String.format(BOBAPI.getMomWeightURL());
            long id = Request.generateRequestId(BOBMomWeightRequest.class, true);
            long priority = 0;

            BOBMomWeightRequest request = new BOBMomWeightRequest(id, url, token);
            request.setRequestListener(reqListener);
            reqMgr.addRequest(request, priority);

            return id;
        }
        return -1;
    }

    /**
     * mom-breath
     * @param token
     * @return id
     */
    public long postMomBreath(String token) {
        if(IS_REQUEST) {

            String url = String.format(BOBAPI.getMomBreathURL());
            long id = Request.generateRequestId(BOBMomBreathRequest.class, true);
            long priority = 0;

            BOBMomBreathRequest request = new BOBMomBreathRequest(id, url, token);
            request.setRequestListener(reqListener);
            reqMgr.addRequest(request, priority);

            return id;
        }
        return -1;
    }

    /**
     * mom-temperature
     * @param token
     * @return id
     */
    public long postMomTemperature(String token) {
        if(IS_REQUEST) {

            String url = String.format(BOBAPI.getMomTemperatureURL());
            long id = Request.generateRequestId(BOBMomTemperatureRequest.class, true);
            long priority = 0;

            BOBMomTemperatureRequest request = new BOBMomTemperatureRequest(id, url, token);
            request.setRequestListener(reqListener);
            reqMgr.addRequest(request, priority);

            return id;
        }
        return -1;
    }

    /**
     * mom-pulse
     * @param token
     * @return id
     */
    public long postMomPulse(String token) {
        if(IS_REQUEST) {

            String url = String.format(BOBAPI.getMomPulseURL());
            long id = Request.generateRequestId(BOBMomPulseRequest.class, true);
            long priority = 0;

            BOBMomPulseRequest request = new BOBMomPulseRequest(id, url, token);
            request.setRequestListener(reqListener);
            reqMgr.addRequest(request, priority);

            return id;
        }
        return -1;
    }

    /**
     * mom-pressure
     * @param token
     * @return id
     */
    public long postMomPressure(String token) {
        if(IS_REQUEST) {

            String url = String.format(BOBAPI.getMomPressureURL());
            long id = Request.generateRequestId(BOBMomPressureRequest.class, true);
            long priority = 0;

            BOBMomPressureRequest request = new BOBMomPressureRequest(id, url, token);
            request.setRequestListener(reqListener);
            reqMgr.addRequest(request, priority);

            return id;
        }
        return -1;
    }

    /**
     * baby-temperature
     * @param token
     * @return id
     */
    public long postBabyTemperature(String token, String babyId) {
        if(IS_REQUEST) {

            String url = String.format(BOBAPI.getBabyTemperatureURL());
            long id = Request.generateRequestId(BOBBabyTemperatureRequest.class, true);
            long priority = 0;

            BOBBabyTemperatureRequest request = new BOBBabyTemperatureRequest(id, url, token,babyId);
            request.setRequestListener(reqListener);
            reqMgr.addRequest(request, priority);

            return id;
        }
        return -1;
    }

    /**
     * baby-weight
     * @param token
     * @return id
     */
    public long postBabyWeight(String token, String babyId) {
        if(IS_REQUEST) {

            String url = String.format(BOBAPI.getBabyWeightURL());
            long id = Request.generateRequestId(BOBBabyWeightRequest.class, true);
            long priority = 0;

            BOBBabyWeightRequest request = new BOBBabyWeightRequest(id, url, token, babyId);
            request.setRequestListener(reqListener);
            reqMgr.addRequest(request, priority);

            return id;
        }
        return -1;
    }

    /**
     * baby-jaundice-index
     * @param token
     * @return id
     */
    public long postBabyJaundiceIndex(String token, String babyId) {
        if(IS_REQUEST) {

            String url = String.format(BOBAPI.getBabyJundiceIndexURL());
            long id = Request.generateRequestId(BOBBabyJaundiceIndexRequest.class, true);
            long priority = 0;

            BOBBabyJaundiceIndexRequest request = new BOBBabyJaundiceIndexRequest(id, url, token, babyId);
            request.setRequestListener(reqListener);
            reqMgr.addRequest(request, priority);


            return id;
        }
        return -1;
    }

    /**
     * baby-milk
     * @param token
     * @return id
     */
    public long postBabyMilk(String token, String babyId) {
        if(IS_REQUEST) {

            String url = String.format(BOBAPI.getBabyMilkURL());
            long id = Request.generateRequestId(BOBBabyMilkRequest.class, true);
            long priority = 0;

            BOBBabyMilkRequest request = new BOBBabyMilkRequest(id, url, token, babyId);
            request.setRequestListener(reqListener);
            reqMgr.addRequest(request, priority);

            return id;
        }
        return -1;
    }

    /**
     * baby-pulse
     * @param token
     * @return id
     */
    public long postBabyPulse(String token, String babyId) {
        if(IS_REQUEST) {

            String url = String.format(BOBAPI.getBabyPulseURL());
            long id = Request.generateRequestId(BOBBabyPulseRequest.class, true);
            long priority = 0;

            BOBBabyPulseRequest request = new BOBBabyPulseRequest(id, url, token,  babyId);
            request.setRequestListener(reqListener);
            reqMgr.addRequest(request, priority);

            return id;
        }
        return -1;
    }

    /**
     * baby-breath
     * @param token
     * @return id
     */
    public long postBabyBreath(String token, String babyId) {
        if(IS_REQUEST) {

            String url = String.format(BOBAPI.getBabyBreathURL());
            long id = Request.generateRequestId(BOBBabyBreathRequest.class, true);
            long priority = 0;

            BOBBabyBreathRequest request = new BOBBabyBreathRequest(id, url, token,  babyId);
            request.setRequestListener(reqListener);
            reqMgr.addRequest(request, priority);

            return id;
        }
        return -1;
    }

    /**
     * baby-swim
     * @param token
     * @return id
     */
    public long postBabySwim(String token, String babyId) {
        if(IS_REQUEST) {

            String url = String.format(BOBAPI.getBabySwimURL());
            long id = Request.generateRequestId(BOBBabySwimRequest.class, true);
            long priority = 0;

            BOBBabySwimRequest request = new BOBBabySwimRequest(id, url, token, babyId);
            request.setRequestListener(reqListener);
            reqMgr.addRequest(request, priority);

            return id;
        }
        return -1;
    }

    /**
     * explain
     * @param token
     * @return id
     */
    public long postExplain(String token, String exId) {
        if(IS_REQUEST) {

            String url = String.format(BOBAPI.getExplainURL());
            long id = Request.generateRequestId(BOBExplainRequest.class, false);
            long priority = 0;

            BOBExplainRequest request = new BOBExplainRequest(id, url, token, exId);
            request.setRequestListener(reqListener);
            reqMgr.addRequest(request, priority);

            return id;
        }
        return -1;
    }

    /**
     * month-activities
     * @param token
     * @return id
     */
    public long postMonthActivities(String token, String month) {
        if(IS_REQUEST) {

            String url = String.format(BOBAPI.getMonthActivitiesURL());
            long id = Request.generateRequestId(BOBMonthActivitiesRequest.class, true);
            long priority = 0;

            BOBMonthActivitiesRequest request = new BOBMonthActivitiesRequest(id, url, token, month);
            request.setRequestListener(reqListener);
            reqMgr.addRequest(request, priority);

            return id;
        }
        return -1;
    }

    /**
     * all month-activities
     * @param token
     * @return id
     */
    public long postMonthActivities(String token) {
        if(IS_REQUEST) {

            String url = String.format(BOBAPI.getMonthActivitiesURL());
            long id = Request.generateRequestId(BOBMonthActivitiesRequest.class, true);
            long priority = 0;

            BOBMonthActivitiesRequest request = new BOBMonthActivitiesRequest(id, url, token);
            request.setRequestListener(reqListener);
            reqMgr.addRequest(request, priority);

            return id;
        }
        return -1;
    }

    /**
     * activity
     * @param token
     * @return id
     */
    public long postActivity(String token, String actId) {
        if(IS_REQUEST) {

            String url = String.format(BOBAPI.getActivityURL());
            long id = Request.generateRequestId(BOBActivityRequest.class, true);
            long priority = 0;

            BOBActivityRequest request = new BOBActivityRequest(id, url, token, actId);
            request.setRequestListener(reqListener);
            reqMgr.addRequest(request, priority);

            return id;
        }
        return -1;
    }

    /**
     * check-activity
     * @param token
     * @return id
     */
    public long postCheckActivity(String token, String actId) {
        if(IS_REQUEST) {

            String url = String.format(BOBAPI.getCheckActivityURL());
            long id = Request.generateRequestId(BOBCheckActivityRequest.class, true);
            long priority = 0;

            BOBCheckActivityRequest request = new BOBCheckActivityRequest(id, url, token, actId);
            request.setRequestListener(reqListener);
            reqMgr.addRequest(request, priority);

            return id;
        }
        return -1;
    }

    /**
     * uncheck-activity
     * @param token
     * @return id
     */
    public long postUncheckActivity(String token, String actId) {
        if(IS_REQUEST) {

            String url = String.format(BOBAPI.getUncheckActivityURL());
            long id = Request.generateRequestId(BOBUncheckActivityRequest.class, true);
            long priority = 0;

            BOBUncheckActivityRequest request = new BOBUncheckActivityRequest(id, url, token, actId);
            request.setRequestListener(reqListener);
            reqMgr.addRequest(request, priority);

            return id;
        }
        return -1;
    }

    /**
     * vas-list
     * @param token
     * @return id
     */
    public long postVasList(String token) {
        if(IS_REQUEST) {

            String url = String.format(BOBAPI.getVasListURL());
            long id = Request.generateRequestId(BOBVasListRequest.class, true);
            long priority = 0;

            BOBVasListRequest request = new BOBVasListRequest(id, url, token);
            request.setRequestListener(reqListener);
            reqMgr.addRequest(request, priority);

            return id;
        }
        return -1;
    }

       /**
     * vas
     * @param token
     * @return id
     */
    public long postVas(String token, String vasid) {
        if(IS_REQUEST) {

            String url = String.format(BOBAPI.getVasURL());
            long id = Request.generateRequestId(BOBVasRequest.class, true);
            long priority = 0;

            BOBVasRequest request = new BOBVasRequest(id, url, token, vasid);
            request.setRequestListener(reqListener);
            reqMgr.addRequest(request, priority);

            return id;
        }
        return -1;
    }

    /**
     * check-vas
     * @param token
     * @return id
     */
    public long postVasCheck(String token, String vasId) {
        if(IS_REQUEST) {

            String url = String.format(BOBAPI.getCheckVasURL());
            long id = Request.generateRequestId(BOBVasCheckRequest.class, true);
            long priority = 0;

            BOBVasCheckRequest request = new BOBVasCheckRequest(id, url, token, vasId);
            request.setRequestListener(reqListener);
            reqMgr.addRequest(request, priority);

            return id;
        }
        return -1;
    }

    /**
     * check-vas
     * @param token
     * @return id
     */
    public long postVasUnCheck(String token, String vasId) {
        if(IS_REQUEST) {

            String url = String.format(BOBAPI.getUnCheckVasURL());
            long id = Request.generateRequestId(BOBVasUnCheckRequest.class, true);
            long priority = 0;

            BOBVasUnCheckRequest request = new BOBVasUnCheckRequest(id, url, token, vasId);
            request.setRequestListener(reqListener);
            reqMgr.addRequest(request, priority);

            return id;
        }
        return -1;
    }

    /**
     * get-consume-list
     * @param token
     * @return id
     */
    public long postConsumeList(String token) {
        if(IS_REQUEST) {

            String url = String.format(BOBAPI.getConsumeListURL());
            long id = Request.generateRequestId(BOBConsumerListRequest.class, true);
            long priority = 0;

            BOBConsumerListRequest request = new BOBConsumerListRequest(id, url, token);
            request.setRequestListener(reqListener);
            reqMgr.addRequest(request, priority);

            return id;
        }
        return -1;
    }

    /**
     * update device token
     * @param token
     * @return id
     */
    public long updateDeviceToken(String token, String deviceToken) {
        if(IS_REQUEST) {

            String url = String.format(BOBAPI.getUpdateDeviceTokenURL());
            long id = Request.generateRequestId(BOBUpdateDeviceTokenRequest.class, true);
            long priority = 0;

            BOBUpdateDeviceTokenRequest request = new BOBUpdateDeviceTokenRequest(id, url, token, deviceToken);
            request.setRequestListener(reqListener);
            reqMgr.addRequest(request, priority);

            return id;
        }
        return -1;
    }



    public long postResetPassword(String token, String password, BOBRequestListener listener) {
        if(IS_REQUEST) {
            String url = BOBAPI.getResetPasswordURL();
            long id = Request.generateRequestId(BOBResetPasswordRequest.class, true);
            long priority = 0;
            BOBResetPasswordRequest request = new BOBResetPasswordRequest(id, url, token, password);
            request.setRequestListener(reqListener);
            request.setBOBListener(listener);
            reqMgr.addRequest(request, priority);
            return id;
        }
        return -1;
    }

    public long postUpdateStream(String token, String camera_id, BOBRequestListener listener) {
        if(IS_REQUEST) {
            String url = BOBAPI.getUpdateStreamURL();
            long id = Request.generateRequestId(BOBUpdateStreamRequest.class, true);
            long priority = 0;
            BOBUpdateStreamRequest request = new BOBUpdateStreamRequest(id, url, token, camera_id);
            request.setRequestListener(reqListener);
            request.setBOBListener(listener);
            reqMgr.addRequest(request, priority);
            return id;
        }
        return -1;
    }

    public long postBugReport(String room,String email,boolean limit,String content, BOBRequestListener listener) {
        if(IS_REQUEST) {
            String url = BOBAPI.getBugReportURL();
            long id = Request.generateRequestId(BOBBugReportRequest.class, true);
            long priority = 0;
            BOBBugReportRequest request = new BOBBugReportRequest(id, url, room, email, limit, content);
            request.setRequestListener(reqListener);
            request.setBOBListener(listener);
            reqMgr.addRequest(request, priority);
            return id;
        }
        return -1;
    }



}
