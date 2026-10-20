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
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import li.com.bobsonclinic.R;
import li.com.bobsonclinic.activity.MainActivity;
import li.com.bobsonclinic.data.BOBBabyExam;
import li.com.bobsonclinic.fragment.base.BaseFragment;
import li.com.bobsonclinic.manager.BOBDataManager;
import li.com.bobsonclinic.manager.BOBSystemManager;
import li.com.bobsonclinic.request.base.BOBAPI;
import li.com.bobsonclinic.viewer.BOBQuickPopupView;


public class BOBBabyExamFragment extends BaseFragment implements View.OnClickListener {

    private ViewGroup rootView;
    private TextView mWeight;
    private TextView mbottleMilk;
    private TextView mformulaMilk;
    private TextView mJaundice;
    private TextView mUrination;
    private TextView mDefecation;
    private TextView mHeart;
    private TextView mBreath;
    private TextView mTemperature;
    private TextView mDate, mTitle;
    private ImageView mMilkTime;
    private String title, css;
    View popupView;
    private int nowIndex;
    private BOBQuickPopupView mExpPopView;
    //	private Date mNowDate;
    private WebView mWebView;
    private PopupWindow popWindow;
    RelativeLayout.LayoutParams params;
    SimpleDateFormat odf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
    private Handler handler;
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((MainActivity) getActivity()).postBabyExam();
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_baby_exam, null);
        rootView.findViewById(R.id.right_btn).setOnClickListener(this);
        rootView.findViewById(R.id.left_btn).setOnClickListener(this);
        rootView.findViewById(R.id.weight_exp).setOnClickListener(this);
        rootView.findViewById(R.id.milk_exp).setOnClickListener(this);
        rootView.findViewById(R.id.heart_exp).setOnClickListener(this);
        rootView.findViewById(R.id.jaundice_exp).setOnClickListener(this);
        rootView.findViewById(R.id.wc_exp).setOnClickListener(this);
        rootView.findViewById(R.id.breath_exp).setOnClickListener(this);
        rootView.findViewById(R.id.temperature_exp).setOnClickListener(this);
        mWeight = (TextView) rootView.findViewById(R.id.weight);
        mbottleMilk = (TextView) rootView.findViewById(R.id.m_milk_a);
        mformulaMilk = (TextView) rootView.findViewById(R.id.m_milk_b);
        mUrination = (TextView) rootView.findViewById(R.id.small_p);
        mDefecation = (TextView) rootView.findViewById(R.id.big_p);
        mJaundice = (TextView) rootView.findViewById(R.id.jaundice);
        mHeart = (TextView) rootView.findViewById(R.id.heart);
        mBreath = (TextView) rootView.findViewById(R.id.breath);
        mTemperature = (TextView) rootView.findViewById(R.id.temperature);
        mDate = (TextView) rootView.findViewById(R.id.date);
        mMilkTime = (ImageView) rootView.findViewById(R.id.m_milk_time_img);
        mTitle = rootView.findViewById(R.id.title);
        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(66, 5, 5, 28);
        mTitle.setText(title);
        handler = new Handler();
        popupView = LayoutInflater.from(getContext()).inflate(R.layout.ui_view_popup_layout,null, false);
        return rootView;
    }

    private void clear() {
        mWeight.setText(getString(R.string.ul_ul));
        mformulaMilk.setText(getString(R.string.ul_ul));
        mbottleMilk.setText(getString(R.string.ul_ul));
        mUrination.setText(getString(R.string.ul_ul));
        mUrination.setText(getString(R.string.ul_ul));
        mDefecation.setText(getString(R.string.ul_ul));
        mBreath.setText(getString(R.string.ul_ul));
        mTemperature.setText(getString(R.string.ul_ul));
        mJaundice.setText(getString(R.string.ul_ul));
        mHeart.setText(getString(R.string.ul_ul));
        mMilkTime.setImageResource(R.drawable.check_no);
    }


    private void prevDate() {
//		if (mNowDate != null) {
//			long prevtime = mNowDate.getTime() - 1 * 24 * 60 * 60 * 1000;
//			Calendar cal = Calendar.getInstance();
//			cal.setTimeInMillis(prevtime);
//			mDate.setText(odf.format(cal.getTime()).substring(0,10));
//			setData(odf.format(cal.getTime()));
//			mNowDate = cal.getTime();
//		}
        if (BOBDataManager.getInstance().getBabyExamList() != null && nowIndex < BOBDataManager.getInstance().getBabyExamList().size()) {
            nowIndex = nowIndex + 1;
            setData(nowIndex);
        }
    }

    private void nextDate() {
//		if (mNowDate != null) {
//			long prevtime = mNowDate.getTime() + 1 * 24 * 60 * 60 * 1000;
//			Calendar cal = Calendar.getInstance();
//			cal.setTimeInMillis(prevtime);
//			mDate.setText(odf.format(cal.getTime()).substring(0,10));
//			setData(odf.format(cal.getTime()));
//			mNowDate = cal.getTime();
//
//		}
        if (nowIndex > 0) {
            nowIndex = nowIndex - 1;
            setData(nowIndex);
        }
    }

    public void setData() {
        List<BOBBabyExam> list = BOBDataManager.getInstance().getBabyExamList();
        int i = 0;
        if(list == null)
            return;
        for (BOBBabyExam exam : list) {
            if (i == 0) {
                setTextView(exam);

//				mNowDate = null;
//				try {
//					mNowDate = odf.parse(exam.getExam_datetime());
//				} catch (ParseException e) {
//					e.printStackTrace();
//				}
            }

            i++;
        }
        ;
    }

//	public void setData(String date){
//		List<BOBBabyExam> list = BOBDataManager.getInstance().getBabyExamList();
//		for(BOBBabyExam exam:list){
//			if(date.contains(exam.getExam_datetime().substring(0,10))){
//				setTextView(exam);
//			}
//		};
//	}

    public void setData(int index) {
        int i = 0;
        List<BOBBabyExam> list = BOBDataManager.getInstance().getBabyExamList();
        for (BOBBabyExam exam : list) {
            if (i == index) {
                setTextView(exam);
                break;
            }
            i++;
        }
        ;
    }

    private void setTextView(BOBBabyExam exam) {
        clear();
        if (exam == null) {
            return;
        }

        if (exam.getWeight() != null)
            mWeight.setText(exam.getWeight());

        if (exam.getFormula_milk() != null)
            mformulaMilk.setText(exam.getFormula_milk());

        if (exam.getMom_bottle_milk() != null)
            mbottleMilk.setText(exam.getMom_bottle_milk());

        if (exam.getUrination() != null)
            mUrination.setText(exam.getUrination());
        if (exam.getDefecation() != null)
            mDefecation.setText(exam.getDefecation());
        if (exam.getBreath() != null)
            mBreath.setText(exam.getBreath());

        if (exam.getPulse() != null) {
            mHeart.setText(exam.getPulse());
        }

        if (exam.getMom_milk_time() > 0) {
            mMilkTime.setImageResource(R.drawable.check_yes);
        } else {
            mMilkTime.setImageResource(R.drawable.check_no);
        }

        if (exam.getTemperature() != null) mTemperature.setText(exam.getTemperature());
        if (exam.getJaundice_index() != null) mJaundice.setText(exam.getJaundice_index());

        Date mNowDate = null;
        try {
            mNowDate = odf.parse(exam.getExam_datetime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (mNowDate != null) {
            mDate.setText(df.format(mNowDate));
        } else {
            mDate.setText(exam.getExam_datetime());
        }

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
                mWebView = popupView.findViewById(R.id.webview);
                mWebView.setWebViewClient(new WebClient());
                mWebView.getSettings().setJavaScriptEnabled(true);
                //mWebView.loadDataWithBaseURL("", "kkkk", "text/html", "UTF-8", "");
                mWebView.loadUrl(BOBAPI.getAuthDomainURL() + BOBDataManager.getInstance().getExplain().get("6"));
                mWebView.setBackgroundColor(0);
                mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                //1.构造一个PopupWindow，参数依次是加载的View，宽高
                popWindow = new PopupWindow(popupView,BOBSystemManager.getInstance().convertDpToPixel(200), BOBSystemManager.getInstance().convertDpToPixel(200), true);
                popWindow.setAnimationStyle(R.anim.fade_in);  //设置加载动画
                popWindow.setTouchable(true);
                popWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.popwindow_gray_bg));
                //设置popupWindow显示的位置，参数依次是参照View，x轴的偏移量，y轴的偏移量
                popWindow.showAsDropDown(v, Gravity.CENTER,0,0);

                break;
            case R.id.milk_exp:
                mWebView = popupView.findViewById(R.id.webview);
                mWebView.setWebViewClient(new WebClient());
                mWebView.getSettings().setJavaScriptEnabled(true);
                mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE,null);
                mWebView.loadUrl(BOBAPI.getAuthDomainURL() + BOBDataManager.getInstance().getExplain().get("9"));
                mWebView.setBackgroundColor(0);
                mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                popWindow = new PopupWindow(popupView,BOBSystemManager.getInstance().convertDpToPixel(200), BOBSystemManager.getInstance().convertDpToPixel(200), true);
                popWindow.setAnimationStyle(R.anim.fade_in);  //设置加载动画
                popWindow.setTouchable(true);
                popWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.popwindow_gray_bg));
                popWindow.showAsDropDown(v, Gravity.CENTER,0,0);
                break;
            case R.id.heart_exp:
                mWebView = popupView.findViewById(R.id.webview);
                mWebView.getSettings().setJavaScriptEnabled(true);
                mWebView.setWebViewClient(new WebClient());
                mWebView.loadUrl(BOBAPI.getAuthDomainURL() + BOBDataManager.getInstance().getExplain().get("4"));
                mWebView.setBackgroundColor(0);
                mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                popWindow = new PopupWindow(popupView,BOBSystemManager.getInstance().convertDpToPixel(200), BOBSystemManager.getInstance().convertDpToPixel(200), true);
                popWindow.setAnimationStyle(R.anim.fade_in);  //设置加载动画
                popWindow.setTouchable(true);
                popWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.popwindow_gray_bg));
                popWindow.showAsDropDown(v, Gravity.CENTER,0,0);
                break;
            case R.id.jaundice_exp:
                mWebView = popupView.findViewById(R.id.webview);
                mWebView.getSettings().setJavaScriptEnabled(true);
                mWebView.setWebViewClient(new WebClient());
                mWebView.loadUrl(BOBAPI.getAuthDomainURL() + BOBDataManager.getInstance().getExplain().get("11"));
                mWebView.setBackgroundColor(0);
                mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                //1.构造一个PopupWindow，参数依次是加载的View，宽高
                popWindow = new PopupWindow(popupView,BOBSystemManager.getInstance().convertDpToPixel(200), BOBSystemManager.getInstance().convertDpToPixel(200), true);
                popWindow.setAnimationStyle(R.anim.fade_in);  //设置加载动画
                popWindow.setTouchable(true);
                popWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.popwindow_gray_bg));
                popWindow.showAsDropDown(v, Gravity.CENTER,0,0);
                break;
            case R.id.breath_exp:
                mWebView = popupView.findViewById(R.id.webview);
                mWebView.getSettings().setJavaScriptEnabled(true);
                mWebView.setWebViewClient(new WebClient());
                mWebView.loadUrl(BOBAPI.getAuthDomainURL() + BOBDataManager.getInstance().getExplain().get("12"));
                mWebView.setBackgroundColor(0);
                mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                //1.构造一个PopupWindow，参数依次是加载的View，宽高
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
                mWebView.setWebViewClient(new WebClient());
                mWebView.loadUrl(BOBAPI.getAuthDomainURL() + BOBDataManager.getInstance().getExplain().get("7"));
                mWebView.setBackgroundColor(0);
                mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                //1.构造一个PopupWindow，参数依次是加载的View，宽高
                popWindow = new PopupWindow(popupView,
                        BOBSystemManager.getInstance().convertDpToPixel(200), BOBSystemManager.getInstance().convertDpToPixel(200), true);
                popWindow.setAnimationStyle(R.anim.fade_in);  //设置加载动画
                popWindow.setTouchable(true);
                popWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.popwindow_gray_bg));
                popWindow.showAsDropDown(v);
                break;
            case R.id.wc_exp:
                mWebView = popupView.findViewById(R.id.webview);
                mWebView.getSettings().setJavaScriptEnabled(true);
                mWebView.setWebViewClient(new WebClient());
                mWebView.loadUrl(BOBAPI.getAuthDomainURL() + BOBDataManager.getInstance().getExplain().get("10"));
                //1.构造一个PopupWindow，参数依次是加载的View，宽高
                mWebView.setBackgroundColor(0);
                mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
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
