package li.com.bobsonclinic.fragment;

/**
 * Agricola
 *
 * Created by Milla.Wang on 2016/1/5.
 * Copyright © 2016 bobsonclinic. All rights reserved.
 */

import android.content.Intent;
import android.database.ContentObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import li.com.bobsonclinic.BOBApplication;
import li.com.bobsonclinic.R;
import li.com.bobsonclinic.fragment.base.BaseFragment;
import li.com.bobsonclinic.provider.BOBProvider;


public class BOBWebViewContentFragment extends BaseFragment implements View.OnClickListener {

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

	public static BOBWebViewContentFragment newInstance(String title, String url){
		BOBWebViewContentFragment f =new BOBWebViewContentFragment();
		f.setTitle(title);
		f.setUrl(url);
		return f;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		rootView = (ViewGroup) inflater.inflate(R.layout.fragment_content, null);
		mWebView = (WebView) rootView.findViewById(R.id.content);
		mTitle  = (TextView) rootView.findViewById(R.id.title);
		progressBar = (ProgressBar) rootView.findViewById(R.id.progress);
		//Log.d("Description", "Description :" + description);
		WebSettings s = mWebView.getSettings();
		s.setDefaultTextEncodingName("utf-8");

		s.setJavaScriptEnabled(true);
		// 設置可以支持縮放
		s.setSupportZoom(true);
		// 設置出現縮放工具
		s.setBuiltInZoomControls(true);
		//擴大比例的縮放
		s.setUseWideViewPort(true);
        //自適應屏幕
		s.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
		s.setLoadWithOverviewMode(true);

		mWebView.setWebChromeClient(new MyChrome());
		mWebView.setWebViewClient(new Browser_home());

		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setAllowFileAccess(true);
//		webSettings.setAppCacheEnabled(true);
		mWebView.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");

		layoutmanager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

		Log.d("dd", "Web Content");

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

		setData();

		return rootView;
	}

	private class MyWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {

			if (url.endsWith(".mp4")) {
				Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
				view.getContext().startActivity(intent);
				return true;
			} else {
				view.loadUrl(url);
				return true;
			}
		}

		@Override
		public void onPageFinished(final WebView view, final String url) {
			progressBar.setVisibility(View.GONE);

		}

		@Override
		public void onScaleChanged(WebView view, float oldScale, float newScale) {
		}
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
		Log.d("url","setDataurl "+url);
		mWebView.loadUrl(url);

		mWebView.setInitialScale(30);
	}


	@Override
	public void onClick(View v) {
		switch(v.getId()){

		}
	}

	class Browser_home extends WebViewClient {

		Browser_home() {
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);

		}

		@Override
		public void onPageFinished(WebView view, String url) {
			setTitle(view.getTitle());
			progressBar.setVisibility(View.GONE);
			view.loadUrl("javascript:window.HTMLOUT.processHTML('<html>'+document.getElementsByTagName('iframe')[0].getAttribute('src')+'</html>');");
			super.onPageFinished(view, url);

		}
	}

	private class MyChrome extends WebChromeClient {

		private View mCustomView;
		private WebChromeClient.CustomViewCallback mCustomViewCallback;
		protected FrameLayout mFullscreenContainer;
		private int mOriginalOrientation;
		private int mOriginalSystemUiVisibility;

		MyChrome() {}

		public Bitmap getDefaultVideoPoster()
		{
			if (mCustomView == null) {
				return null;
			}
			return BitmapFactory.decodeResource(BOBApplication.getAppContext().getResources(), 2130837573);
		}

		public void onHideCustomView()
		{
			((FrameLayout)getActivity().getWindow().getDecorView()).removeView(this.mCustomView);
			this.mCustomView = null;
			getActivity().getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
			getActivity().setRequestedOrientation(this.mOriginalOrientation);
			this.mCustomViewCallback.onCustomViewHidden();
			this.mCustomViewCallback = null;
		}

		public void onShowCustomView(View paramView, WebChromeClient.CustomViewCallback paramCustomViewCallback)
		{
			if (this.mCustomView != null)
			{
				onHideCustomView();
				return;
			}
			this.mCustomView = paramView;
			this.mOriginalSystemUiVisibility = getActivity().getWindow().getDecorView().getSystemUiVisibility();
			this.mOriginalOrientation = getActivity().getRequestedOrientation();
			this.mCustomViewCallback = paramCustomViewCallback;
			((FrameLayout)getActivity().getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
			getActivity().getWindow().getDecorView().setSystemUiVisibility(3846);
		}
	}

	class MyJavaScriptInterface
	{
		@JavascriptInterface
		public void processHTML(String html)
		{
			Log.d("processHTML", "processHTML IFRAME text: " + html);

		}
	}

}
