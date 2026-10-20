package li.com.bobsonclinic.fragment;

/**
 * Agricola
 *
 * Created by Milla.Wang on 2016/1/5.
 * Copyright © 2016 bobsonclinic. All rights reserved.
 */

import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import li.com.bobsonclinic.R;
import li.com.bobsonclinic.fragment.base.BaseFragment;
import li.com.bobsonclinic.provider.BOBProvider;


public class BOBAboutUsFragment extends BaseFragment implements View.OnClickListener {

	private ViewGroup rootView;
	private WebView mWebView;
	private TextView mTitle;
	protected ContentObserver infoObserver;
	protected Handler mHandler;
	LinearLayoutManager layoutmanager;
	ProgressBar progressBar;
	private String title = "", url = "";

	public void setTitle(String title) {
		this.title = title;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public static BOBAboutUsFragment newInstance(String title, String url){
		BOBAboutUsFragment f =new BOBAboutUsFragment();
		f.setTitle(title);
		f.setUrl(url);
		return f;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		rootView = (ViewGroup) inflater.inflate(R.layout.fragment_aboutus, null);
		mWebView = (WebView) rootView.findViewById(R.id.content);
		mTitle  = (TextView) rootView.findViewById(R.id.title);
		progressBar = (ProgressBar) rootView.findViewById(R.id.progress);
		//Log.d("Description", "Description :" + description);
		WebSettings settings = mWebView.getSettings();
		settings.setDefaultTextEncodingName("utf-8");
		settings.setJavaScriptEnabled(true);
		layoutmanager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);


		mWebView.getSettings().setLoadWithOverviewMode(true);
		mWebView.getSettings().setUseWideViewPort(true);


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
		initHandler();

		mWebView.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageFinished(WebView view, String url) {
				progressBar.setVisibility(View.GONE);
			}
		});
		setData();

		return rootView;
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

		getActivity().getContentResolver().registerContentObserver(BOBProvider.URI_AboutUs, true, infoObserver);
	}

	@Override
	public void onPause() {

		super.onPause();

		getActivity().getContentResolver().unregisterContentObserver(infoObserver);
	}


	public void setData(){
		progressBar.setVisibility(View.VISIBLE);
		mTitle.setText(title);
		mWebView.clearCache(true);
		mWebView.clearHistory();
		mWebView.loadUrl(url);
	}


	@Override
	public void onClick(View v) {
		switch(v.getId()){

		}
	}


}
