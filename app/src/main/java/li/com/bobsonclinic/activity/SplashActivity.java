package li.com.bobsonclinic.activity;

/**
 * Agricola
 *
 * Created by Milla.Wang on 2016/1/5.
 * Copyright © 2016 bobsonclinic. All rights reserved.
 */

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.fragment.app.FragmentActivity;

import li.com.bobsonclinic.R;
import li.com.bobsonclinic.logic.BOBPushRegisterLogic;

public class SplashActivity extends FragmentActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();

    private static int SPLASH_TIME = 1000;
    private int previousVersionCode = -1;
    private boolean isLogin = false;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //GCM
        BOBPushRegisterLogic.getInstance().doGCMRegister(SplashActivity.this);
        setContentView(R.layout.activity_splash);
        startApp();

    }

    private void startApp(){
        startMainActivity(SPLASH_TIME);
    }

    private void startMainActivity(long delayTime){

        final Handler handler = new Handler();
        getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                init();
            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                executeStartApp();
            }
        }, delayTime);
    }

    private void init() {

    }

    private void initHandler() {

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                int key = msg.what;
                switch (key) {
                    case 0:

                        break;
                }
            }
        };
    }

    private void sendActivityMessage(int what, int arg1, int arg2, Object obj) {
        Message msg = mHandler.obtainMessage();
        msg.what = what;
        msg.arg1 = arg1;
        msg.arg2 = arg2;
        msg.obj = obj;

        mHandler.sendMessage(msg);
    }

    private synchronized void executeStartApp(){


            Intent intent = new Intent();
            intent.setClass(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:
                switch (requestCode) {
                    case 0:
                        startMainActivity(SPLASH_TIME);
                        break;
                    case 1:
                        Intent intent = new Intent();
                        intent.setClass(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                }
                break;
            case RESULT_CANCELED:
                finish();
                break;
        }
    }


}
