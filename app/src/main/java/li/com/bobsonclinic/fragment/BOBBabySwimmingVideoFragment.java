package li.com.bobsonclinic.fragment;

import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import li.com.bobsonclinic.data.BOBBabySwim;
import li.com.bobsonclinic.manager.BOBDataManager;
import li.com.bobsonclinic.provider.BOBProvider;

/**
 * Created by Zack on 6/23/16.
 */
public class BOBBabySwimmingVideoFragment extends BOBBabyPlayVideoFragment{

    protected SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    protected ContentObserver infoObserver;
    protected Handler mHandler;

    private class Timer implements Runnable{
        @Override
        public void run() {
            closeSwimmingVideo();
            timer = null;
        }
    }

    protected BOBBabySwim swimmingData;
    protected Timer timer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        infoObserver = new ContentObserver(mHandler) {
            @Override
            public void onChange(boolean selfChange) {
                super.onChange(selfChange);
                loadInfo();
            }
        };
        initHandler();

        return rootView;
    }

    @Override
    protected void init() {
        //super.init();
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    protected String getVideoPath(){
        return videopath;
    }

    public void setData(){
        swimmingData = BOBDataManager.getInstance().getBabySwim();
    }

    private void initTimer() {
        if (swimmingData != null) {
            String dateStr = swimmingData.getStop_at();
            try {
                Date date = df.parse(dateStr);
                if (date.getTime() >= System.currentTimeMillis()) {
                    videopath = swimmingData.getRtsp_uri();
                    init();
                    startTimer((date.getTime() - System.currentTimeMillis()));
                } else {
                    closeSwimmingVideo();
                }
            } catch (Exception e) {
                e.printStackTrace();
                videopath = null;
            }
        }else{
            closeSwimmingVideo();
        }
    }


    public void initHandler() {
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 0: {
                        setData();
                    }
                    break;
                }
                return true;
            }
        });
    }

    private void loadInfo(){
        Message msg = mHandler.obtainMessage();
        msg.what = 0;
        msg.obj = null;
        mHandler.sendMessageDelayed(msg, 500);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getContentResolver().registerContentObserver(BOBProvider.URI_BabySwim, true, infoObserver);
    }

    @Override
    public void onStart() {
        initTimer();
        super.onStart();
    }

    @Override
    public void onPause() {
        getActivity().getContentResolver().unregisterContentObserver(infoObserver);
        super.onPause();
        if(timer != null){
            mHandler.removeCallbacks(timer);
            timer = null;
        }
    }

    private synchronized void startTimer(long delaySec){
        if(timer != null){
            return;
        }
        timer = new Timer();
        mHandler.postDelayed(timer, delaySec);
    }

    private void closeSwimmingVideo(){
        timer = null;
        closeVideo();
        Toast.makeText(getActivity(), "游泳課程已結束!!", Toast.LENGTH_SHORT).show();
    }

}
