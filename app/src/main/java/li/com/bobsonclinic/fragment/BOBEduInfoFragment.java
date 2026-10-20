package li.com.bobsonclinic.fragment;

/**
 * Agricola
 *
 * Created by Milla.Wang on 2016/1/5.
 * Copyright © 2016 bobsonclinic. All rights reserved.
 */

import android.content.Intent;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import li.com.bobsonclinic.R;
import li.com.bobsonclinic.adapter.ExpandableListAdapter;
import li.com.bobsonclinic.data.BOBUIRightList;
import li.com.bobsonclinic.fragment.base.BaseFragment;
import li.com.bobsonclinic.manager.BOBDataManager;
import li.com.bobsonclinic.provider.BOBProvider;

import static li.com.bobsonclinic.activity.MainPadActivity.LAYOUT_DID_COLLAPSE_TAG;
import static li.com.bobsonclinic.activity.MainPadActivity.LAYOUT_DID_EXPAND_TAG;


public class BOBEduInfoFragment extends BaseFragment implements View.OnClickListener {

	private ViewGroup rootView;
	private RecyclerView recyclerview;
	private WebView mWebView;
	private TextView mTitle;
	private TextView mListTitle;
	ExpandableListAdapter adapter;
	private ContentObserver infoObserver;
	private Handler mHandler;
	private ProgressBar progressBar;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		rootView = (ViewGroup) inflater.inflate(R.layout.fragment_edu_info, null);
		recyclerview = rootView.findViewById(R.id.recyclerview);
		mWebView = rootView.findViewById(R.id.content);
		mTitle  = rootView.findViewById(R.id.title);
		mListTitle  = rootView.findViewById(R.id.list_title);
		progressBar = rootView.findViewById(R.id.progress);
		recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
		recyclerview.setHasFixedSize(true);
		mListTitle.setText(getString(R.string.teachinfo));

		final ImageButton expandButton = rootView.findViewById(R.id.btn_expand);
		expandButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ViewGroup menuLayout = rootView.findViewById(R.id.menu_layout);
				if (menuLayout.getVisibility() == View.GONE) {
					menuLayout.setVisibility(View.VISIBLE);
					expandButton.setImageResource(R.drawable.icon_expand);
					Intent intent = new Intent(LAYOUT_DID_COLLAPSE_TAG);
					LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
				} else if (menuLayout.getVisibility() == View.VISIBLE) {
					menuLayout.setVisibility(View.GONE);
					expandButton.setImageResource(R.drawable.icon_collapse);
					Intent intent = new Intent(LAYOUT_DID_EXPAND_TAG);
					LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
				}
			}
		});

		adapter = new ExpandableListAdapter(getActivity());
		recyclerview.setAdapter(adapter);


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
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setLoadWithOverviewMode(true);
		mWebView.getSettings().setUseWideViewPort(true);

		mWebView.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageFinished(WebView view, String url) {
				progressBar.setVisibility(View.GONE);
			}
		});


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

		getActivity().getContentResolver().registerContentObserver(BOBProvider.URI_EduInfo, true, infoObserver);
	}

	@Override
	public void onPause() {

		super.onPause();

		getActivity().getContentResolver().unregisterContentObserver(infoObserver);
	}

	public void setData(){
		final Map<String, BOBUIRightList> header = BOBDataManager.getInstance().getEducationData();
		List<ExpandableListAdapter.Item> data = new ArrayList<>();
		int i =0;
		for(Map.Entry<String, BOBUIRightList> entry : header.entrySet()) {
			String key = entry.getKey();
			if(mTitle.getText() != null && mTitle.getText().toString().length() == 0 && header.get(key).getContentUrl() != null && !header.get(key).getContentUrl().equals("") && header.get(key).getSubData().size() == 0){
				mTitle.setText(header.get(key).getTitle());
				progressBar.setVisibility(View.VISIBLE);
				mWebView.loadUrl(header.get(key).getContentUrl());
				if(adapter != null){
					adapter.setKey(key);
				}
			}

			data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, key, header.get(key).getTitle(), header.get(key).getSubData().size()));
			final Map<String, BOBUIRightList> child = header.get(key).getSubData();
			int childIndex =0;
			for(Map.Entry<String, BOBUIRightList> entryChild : child.entrySet()){
				String ckey = entryChild.getKey();
				data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, key, ckey, child.get(ckey).getTitle()));
				//if(i == 0 && childIndex == 0){
				if( mTitle.getText().toString().length() == 0 && child.get(ckey).getContentUrl() != null && !child.get(ckey).getContentUrl().equals("")) {
					mTitle.setText(child.get(ckey).getTitle());
					progressBar.setVisibility(View.VISIBLE);
					mWebView.clearCache(true);
					mWebView.clearHistory();
					mWebView.loadUrl(child.get(ckey).getContentUrl());
					if(adapter != null){
						adapter.setChildKey(ckey);
					}
				}
				childIndex++;
			}
			i++;
		}
		adapter.setList(data);
		adapter.setListener(new ExpandableListAdapter.LisClickListener() {
			@Override
			public void headerClick(String key) {
				progressBar.setVisibility(View.VISIBLE);
				mWebView.clearCache(true);
				mWebView.clearHistory();
				mWebView.loadUrl(header.get(key).getContentUrl());
				mTitle.setText(header.get(key).getTitle());
			}

			@Override
			public void childClick(String key, String childKey) {
				if(header.get(key) !=null && header.get(key).getSubData() != null &&  header.get(key).getSubData().get(childKey) != null) {
					progressBar.setVisibility(View.VISIBLE);
					mWebView.clearCache(true);
					mWebView.clearHistory();
					mWebView.loadUrl(header.get(key).getSubData().get(childKey).getContentUrl());
					mTitle.setText(header.get(key).getSubData().get(childKey).getTitle());
				}
			}

			@Override
			public void headerExpand(int pos) {
				if(recyclerview != null){
					recyclerview.scrollToPosition(pos + 1);
				}
			}
		});
	}


	@Override
	public void onClick(View v) {
		switch(v.getId()){

		}
	}


}
