package li.com.bobsonclinic.fragment;

/**
 * Agricola
 * <p>
 * Created by Milla.Wang on 2016/1/5.
 * Copyright © 2016 bobsonclinic. All rights reserved.
 */

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import androidx.core.content.ContextCompat;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import li.com.bobsonclinic.R;
import li.com.bobsonclinic.activity.MainActivity;
import li.com.bobsonclinic.data.BOBMomExam;
import li.com.bobsonclinic.fragment.base.BaseFragment;
import li.com.bobsonclinic.manager.BOBDataManager;
import li.com.bobsonclinic.manager.BOBSystemManager;
import li.com.bobsonclinic.request.base.BOBAPI;
import li.com.bobsonclinic.viewer.BOBQuickPopupView;


public class BOBMomExamFragment extends BaseFragment implements View.OnClickListener {

    private ViewGroup rootView;
    private TextView mWeight;
    private TextView mMomMilk;
    private TextView mHeart;
    private TextView mSystolcBlood;
    private TextView mDaistolcBlood;
    private TextView mBreath;
    private TextView mTemperature;
    private TextView mDate;
    private int nowIndex;
    private BOBQuickPopupView mExpPopView;
    private WebView mWebView;
    private PopupWindow popWindow;
    RelativeLayout.LayoutParams params;
    private Handler handler;
    //	private Date mNowDate;
//	SimpleDateFormat odf = new SimpleDateFormat("yyyy-MM-dd");
    private String title = "";
    private String css = "";
    View popupView;
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((MainActivity) getActivity()).postMomExam();
        ((MainActivity) getActivity()).postExplain();
        handler = new Handler();
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_mom_exam, null);
        rootView.findViewById(R.id.right_btn).setOnClickListener(this);
        rootView.findViewById(R.id.left_btn).setOnClickListener(this);
        rootView.findViewById(R.id.weight_exp).setOnClickListener(this);
        rootView.findViewById(R.id.milk_exp).setOnClickListener(this);
        rootView.findViewById(R.id.heart_exp).setOnClickListener(this);
        rootView.findViewById(R.id.blood_exp).setOnClickListener(this);
        rootView.findViewById(R.id.breath_exp).setOnClickListener(this);
        rootView.findViewById(R.id.temperature_exp).setOnClickListener(this);
        mWeight = (TextView) rootView.findViewById(R.id.weight);
        mMomMilk = (TextView) rootView.findViewById(R.id.milk);
        mHeart = (TextView) rootView.findViewById(R.id.heart);
        mSystolcBlood = (TextView) rootView.findViewById(R.id.systolc_blood);
        mDaistolcBlood = (TextView) rootView.findViewById(R.id.diastolc_blood);
        mBreath = (TextView) rootView.findViewById(R.id.breath);
        mTemperature = (TextView) rootView.findViewById(R.id.temperature);
        mDate = (TextView) rootView.findViewById(R.id.date);
        clear();
        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        setData();
        params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(5, 5, 5, 20);

        TextView titleTxv = rootView.findViewById(R.id.title);
        titleTxv.setText(title);
        popupView = LayoutInflater.from(getContext()).inflate(R.layout.ui_view_popup_layout,null, false);
        return rootView;
    }

    public void setData() {
        Map<Integer, BOBMomExam> mMomExamMap = BOBDataManager.getInstance().getMomExamMap();
        if (mMomExamMap.size() > 0) {
            setTextView(mMomExamMap.get(0));
        }
        nowIndex = 0;
//		LinkedList<BOBMomExam> list = BOBDataManager.getInstance().getMomExamList();
//		int i = 0;
//        for(BOBMomExam exam:list){
//			if(i == 0){
//				nowIndex = i;
//				setTextView(exam);
//
//				break;
//			}
//
//			i++;
//		};
    }

    private void setTextView(BOBMomExam exam) {
        clear();
        if (exam == null) {
            return;
        }
        if (exam.getWeight() != null) mWeight.setText(exam.getWeight());
        if (exam.getBreast_milk() != null) mMomMilk.setText(exam.getBreast_milk());
        if (exam.getPulse() != null) mHeart.setText(exam.getPulse());
        if (exam.getSystolic_pressure() != null) mSystolcBlood.setText(exam.getSystolic_pressure());
        if (exam.getDiastolic_pressure() != null)
            mDaistolcBlood.setText(exam.getDiastolic_pressure());
        if (exam.getBreath() != null) mBreath.setText(exam.getBreath());
        if (exam.getTemperature() != null) mTemperature.setText(exam.getTemperature());
        if (exam.getExam_date() != null) mDate.setText(exam.getExam_date());
    }

    private void clear() {
        mWeight.setText(getString(R.string.ul_ul));
        mMomMilk.setText(getString(R.string.ul_ul));
        mHeart.setText(getString(R.string.ul_ul));
        mSystolcBlood.setText(getString(R.string.ul_ul));
        mDaistolcBlood.setText(getString(R.string.ul_ul));
        mBreath.setText(getString(R.string.ul_ul));
        mTemperature.setText(getString(R.string.ul_ul));
    }

    private void prevDate() {
//		if (mNowDate != null) {
//			long prevtime = mNowDate.getTime() - 1 * 24 * 60 * 60 * 1000;
//			Calendar cal = Calendar.getInstance();
//			cal.setTimeInMillis(prevtime);
//			mDate.setText(odf.format(cal.getTime()));
//			setData(odf.format(cal.getTime()));
//			mNowDate = cal.getTime();
//		}
        if (nowIndex < BOBDataManager.getInstance().getMomExamMap().size()) {
            nowIndex = nowIndex + 1;
            setData(nowIndex);
        }
    }

    private void nextDate() {
//		if (mNowDate != null) {
//			long prevtime = mNowDate.getTime() + 1 * 24 * 60 * 60 * 1000;
//			Calendar cal = Calendar.getInstance();
//			cal.setTimeInMillis(prevtime);
//			mDate.setText(odf.format(cal.getTime()));
//			setData(odf.format(cal.getTime()));
//			mNowDate = cal.getTime();
//
//		}
        if (nowIndex > 0) {
            nowIndex = nowIndex - 1;
            setData(nowIndex);
        }
    }

    public void setData(String date) {
        List<BOBMomExam> list = BOBDataManager.getInstance().getMomExamList();
        for (BOBMomExam exam : list) {
            if (date.contains(exam.getExam_date())) {
                setTextView(exam);
            }

        }
        ;
    }

    public void setData(int index) {

        Map<Integer, BOBMomExam> mMomExamMap = BOBDataManager.getInstance().getMomExamMap();
        if (mMomExamMap.size() > index) {
            setTextView(mMomExamMap.get(index));
        }
//		List<BOBMomExam> list = BOBDataManager.getInstance().getMomExamList();
//		int i = 0;
//		for(BOBMomExam exam:list){
//
//			if(i == index){
//				setTextView(exam);
//				break;
//			}
//			i++;
//		};
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.right_btn: //next day
                nextDate();
                break;
            case R.id.left_btn:  //prev day
                prevDate();
                break;
            case R.id.weight_exp:

                break;
            case R.id.milk_exp:
                mWebView = popupView.findViewById(R.id.webview);
                mWebView.loadUrl(BOBAPI.getAuthDomainURL() + BOBDataManager.getInstance().getExplain().get("1"));
                // Enable Javascript
                mWebView.getSettings().setJavaScriptEnabled(true);
                // Add a WebViewClient
                mWebView.setWebViewClient(new WebClient());
                popWindow = new PopupWindow(popupView,
                        BOBSystemManager.getInstance().convertDpToPixel(200), BOBSystemManager.getInstance().convertDpToPixel(200), true);
                popWindow.setAnimationStyle(R.anim.fade_in);  //设置加载动画
                popWindow.setTouchable(true);
                popWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.popwindow_gray_bg));
                popWindow.showAsDropDown(v, Gravity.CENTER,0,0);

                break;
            case R.id.heart_exp:
                mWebView = popupView.findViewById(R.id.webview);
                mWebView.getSettings().setJavaScriptEnabled(true);
                mWebView.loadUrl(BOBAPI.getAuthDomainURL() + BOBDataManager.getInstance().getExplain().get("4"));
                //1.构造一个PopupWindow，参数依次是加载的View，宽高
                mWebView.setBackgroundColor(0);
                mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                mWebView.setWebViewClient(new WebClient());
                popWindow = new PopupWindow(popupView,
                        BOBSystemManager.getInstance().convertDpToPixel(200), BOBSystemManager.getInstance().convertDpToPixel(200), true);
                popWindow.setAnimationStyle(R.anim.fade_in);  //设置加载动画
                popWindow.setTouchable(true);
                popWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.popwindow_gray_bg));
                popWindow.showAsDropDown(v, Gravity.CENTER,0,0);
                break;
            case R.id.blood_exp:
                mWebView = popupView.findViewById(R.id.webview);
                mWebView.getSettings().setJavaScriptEnabled(true);
                mWebView.loadUrl(BOBAPI.getAuthDomainURL() + BOBDataManager.getInstance().getExplain().get("13"));
                //1.构造一个PopupWindow，参数依次是加载的View，宽高
                mWebView.setBackgroundColor(0);
                mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                mWebView.setWebViewClient(new WebClient());
                popWindow = new PopupWindow(popupView,
                        BOBSystemManager.getInstance().convertDpToPixel(200), BOBSystemManager.getInstance().convertDpToPixel(200), true);
                popWindow.setAnimationStyle(R.anim.fade_in);  //设置加载动画
                popWindow.setTouchable(true);
                popWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.popwindow_gray_bg));
                popWindow.showAsDropDown(v, Gravity.CENTER,0,0);
                break;
            case R.id.breath_exp:
                mWebView = popupView.findViewById(R.id.webview);
                mWebView.getSettings().setJavaScriptEnabled(true);
                mWebView.loadUrl(BOBAPI.getAuthDomainURL() + BOBDataManager.getInstance().getExplain().get("3"));
                //1.构造一个PopupWindow，参数依次是加载的View，宽高
                mWebView.setBackgroundColor(0);
                mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                mWebView.setWebViewClient(new WebClient());
                popWindow = new PopupWindow(popupView,
                        BOBSystemManager.getInstance().convertDpToPixel(200), BOBSystemManager.getInstance().convertDpToPixel(200), true);
                popWindow.setAnimationStyle(R.anim.fade_in);  //设置加载动画
                popWindow.setTouchable(true);
                popWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.popwindow_gray_bg));
                popWindow.showAsDropDown(v, Gravity.CENTER,0,0);
                break;
            case R.id.temperature_exp:
                mWebView = popupView.findViewById(R.id.webview);
                mWebView.getSettings().setJavaScriptEnabled(true);
                mWebView.loadUrl(BOBAPI.getAuthDomainURL() + BOBDataManager.getInstance().getExplain().get("5"));
                //1.构造一个PopupWindow，参数依次是加载的View，宽高
                mWebView.setBackgroundColor(0);
                mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                mWebView.setWebViewClient(new WebClient());
                popWindow = new PopupWindow(popupView,
                        BOBSystemManager.getInstance().convertDpToPixel(200), BOBSystemManager.getInstance().convertDpToPixel(200), true);
                popWindow.setAnimationStyle(R.anim.fade_in);  //设置加载动画
                popWindow.setTouchable(true);
                popWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.popwindow_gray_bg));
                popWindow.showAsDropDown(v, Gravity.CENTER,0,0);
                break;
        }
    }

    public class WebClient extends WebViewClient {


        public WebClient(){
        }

        @Override
        public void onPageFinished(WebView view, String url)
        {
            injectCSS();
            super.onPageFinished(view, url);
        }
    }
    // Inject CSS method: read style.css from assets folder
// Append stylesheet to document head
    private void injectCSS() {

        @SuppressLint("ResourceType")
        String mBackColor = "#" + Integer.toHexString(ContextCompat.getColor(getActivity(), R.color.transparent) & 0x00ffffff);//"#F6F3F1";//getActivity().getResources().getString(R.color.backgroundColor);
        String mFontSize = "16";
        @SuppressLint("ResourceType")
        String fontColor = "#" + Integer.toHexString(ContextCompat.getColor(getActivity(), R.color.colorPrimary) & 0x00ffffff);//getActivity().getResources().getString(R.color.textColorPrimary);

        css = "body {" +
                "background-color:"+mBackColor+" !important;" +
                "font-size: " + mFontSize + "px !important;" +
                "color: " + fontColor + " !important;" +
                "font-family:sans-serif;" +
                "align-content:center;" +
                "font-weight:normal;" +
                "word-break:break-all;" +
                "}";

        try {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    mWebView.loadUrl("javascript: " +
                            "var c = document.createElement('style');" +
                            "c.setAttribute('type','text/css');" +
                            "c.innerHTML = '"+css+"';"+
                            "document.body.appendChild(c); " ); //重新套用CSS,需重新取得文章大小去計算總頁面數量
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
