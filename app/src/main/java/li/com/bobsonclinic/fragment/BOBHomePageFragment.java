package li.com.bobsonclinic.fragment;

/**
 * Agricola
 * <p/>
 * Created by Milla.Wang on 2016/1/5.
 * Copyright © 2016 bobsonclinic. All rights reserved.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.viewpager.widget.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import li.com.bobsonclinic.R;
import li.com.bobsonclinic.activity.MainActivity;
import li.com.bobsonclinic.adapter.BOBBabyPagerAdapter;
import li.com.bobsonclinic.data.BOBBabies;
import li.com.bobsonclinic.data.BOBMomData;
import li.com.bobsonclinic.data.server.base.BOBRequestListener;
import li.com.bobsonclinic.fragment.base.BaseFragment;
import li.com.bobsonclinic.fragment.dialog.base.BaseDialogFragment;
import li.com.bobsonclinic.fragment.dialog.base.DialogPickerDialogListener;
import li.com.bobsonclinic.logic.BOBDialogLogic;
import li.com.bobsonclinic.logic.CommunicationLogic;
import li.com.bobsonclinic.manager.BOBDataManager;
import li.com.bobsonclinic.manager.BOBSystemManager;
import li.com.bobsonclinic.provider.BOBProvider;


public class BOBHomePageFragment extends BaseFragment implements View.OnClickListener {

    private ViewGroup rootView;
    TextView momName;
    TextView inDate;
    TextView outDate;
    private ViewPager babyViewPager;
    BOBBabyPagerAdapter mBabyAdapter;

    private BaseDialogFragment dialog;
    private float SCREEN_DENSITY = 0.0f;
    private InputMethodManager inputMethodManager = null;
    private Handler handler = new Handler();
    private ContentObserver infoObserver, babyObserver;
    private Handler mHandler;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        SCREEN_DENSITY = metrics.density;
        initHandler();
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_homepage, null);

        rootView.findViewById(R.id.title_modified_password).setOnClickListener(this);

        mBabyAdapter = new  BOBBabyPagerAdapter(getActivity());
        babyViewPager = (ViewPager)rootView.findViewById(R.id.baby_view_pager);
        momName = (TextView) rootView.findViewById(R.id.mom_name);
        inDate = (TextView) rootView.findViewById(R.id.in_date);
        outDate = (TextView) rootView.findViewById(R.id.out_date);

        mBabyAdapter.setListenet(new BOBBabyPagerAdapter.BabyListener() {
            @Override
            public void onClick(int pos) {
                if(babyViewPager != null && (pos >=0 || pos< mBabyAdapter.getCount())) {
                    babyViewPager.setCurrentItem(pos, true);
                }
            }
        });
        babyViewPager.setAdapter(mBabyAdapter);
        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        babyViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                List<BOBBabies> list = BOBDataManager.getInstance().getBabyLst();
                BOBDataManager.getInstance().setBabyId(list.get(position).getId());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        infoObserver = new ContentObserver(mHandler)
        {
            @Override
            public void onChange(boolean selfChange)
            {
                super.onChange(selfChange);
                loadInfo();
            }
        };
        babyObserver = new ContentObserver(mHandler)
        {
            @Override
            public void onChange(boolean selfChange)
            {
                super.onChange(selfChange);
                loadInfo();
            }
        };

        ((MainActivity)getActivity()).getMomData();
        ((MainActivity)getActivity()).postBabyData();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadInfo();
        getActivity().getContentResolver().registerContentObserver(BOBProvider.URI_Mather, true, infoObserver);
        getActivity().getContentResolver().registerContentObserver(BOBProvider.URI_Babies, true, babyObserver);
    }

    @Override
    public void onPause() {

        super.onPause();

        getActivity().getContentResolver().unregisterContentObserver(infoObserver);
        getActivity().getContentResolver().unregisterContentObserver(babyObserver);
    }

    private void initHandler(){
        mHandler = new Handler(new Handler.Callback()
        {
            @Override
            public boolean handleMessage(Message msg)
            {
                switch(msg.what)
                {
                    case 0:
                    {
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


    public void setData() {
        ((MainActivity)getActivity()).dismissLoading();
        setMomData();
        setBabyPager();
    }

    public void setMomData() {
        if(!isAdded())
            return;
        BOBMomData momeyData = BOBDataManager.getInstance().getMomeyData();
        if(momeyData != null) {
            momName.setText(momeyData.getName());
            inDate.setText(momeyData.getCheck_in_date());
            outDate.setText(momeyData.getCheck_out_date());
        }
    }

    public void setBabyPager(){
        if(!isAdded())
            return;
        List<BOBBabies> list = BOBDataManager.getInstance().getBabyLst();
        if(list != null) {
            if (mBabyAdapter != null)
                mBabyAdapter.setList(list);
            if (list.size() > 0)
                BOBDataManager.getInstance().setBabyId(list.get(0).getId());

            Log.d("setBabyPager", "setBabyPager " + BOBDataManager.getInstance().getBabyId());
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_modified_password:
                popupResetPassword();
                break;

        }
    }

    protected synchronized void popupResetPassword(){

        if(dialog != null && dialog.isVisible()){
            dialog.dismiss();
            dialog = null;
            return;
        }

        dialog = BOBDialogLogic.getInstance().showResetPasswordDialog(getActivity(), true, new DialogPickerDialogListener() {
            @Override
            public void onClickDialogPicker(DialogPickerType type, Object obj) {
                if(type == DialogPickerType.OK){
                    String password = (String)obj;
                    postResetPassword(password);
                    dialog.dismiss();
                }
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                hideKeyboard(200);
            }
        });

        showKeyboard();
    }

    private void postResetPassword(String password){
        CommunicationLogic.getInstance().postResetPassword(BOBSystemManager.getInstance().getUserToken(),
                password, new BOBRequestListener() {
                    @Override
                    public void onResult(final boolean success, Object object) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if(success){
                                    Toast.makeText(getActivity(), "密碼置換成功!", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(getActivity(), "密碼置換失敗!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
    }

    public int dpToPixel(int x) {
        return (int) (SCREEN_DENSITY * (float)x);
    }


    public void hideKeyboard() {
        try {
            View view = getActivity().getWindow().getDecorView();
            if (inputMethodManager != null && view != null) {
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showKeyboard() {
        if(inputMethodManager != null){
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    }

    public void hideKeyboard(long delayMillis) {
        handler.postDelayed(new Runnable() {
            public void run() {
                hideKeyboard();
            }
        }, delayMillis);
    }
}
