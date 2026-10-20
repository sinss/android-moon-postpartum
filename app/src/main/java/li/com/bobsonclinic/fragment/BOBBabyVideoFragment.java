package li.com.bobsonclinic.fragment;

/**
 * Agricola
 *
 * Created by Milla.Wang on 2016/1/5.
 * Copyright © 2016 bobsonclinic. All rights reserved.
 */

import android.app.Activity;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import li.com.bobsonclinic.R;
import li.com.bobsonclinic.activity.MainActivity;
import li.com.bobsonclinic.adapter.ExpandableListAdapter;
import li.com.bobsonclinic.data.BOBBabyCamera;
import li.com.bobsonclinic.fragment.base.BaseFragment;
import li.com.bobsonclinic.manager.BOBDataManager;
import li.com.bobsonclinic.provider.BOBProvider;


public class BOBBabyVideoFragment extends BaseFragment implements View.OnClickListener {

	protected ContentObserver infoObserver;
	protected Handler mHandler;

	private ViewGroup rootView;
	private RecyclerView recyclerview;
	private TextView mTitle;
	private TextView mListTitle;
	private MainActivity activity;
	private BOBBabyCamera mCamera;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof MainActivity) {
			this.activity = (MainActivity) activity;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		rootView = (ViewGroup) inflater.inflate(R.layout.fragment_baby_video, null);
		recyclerview = (RecyclerView) rootView.findViewById(R.id.recyclerview);
		mTitle  = (TextView) rootView.findViewById(R.id.title);
		mListTitle  = (TextView) rootView.findViewById(R.id.list_title);

		infoObserver = new ContentObserver(mHandler) {
			@Override
			public void onChange(boolean selfChange)
			{
				super.onChange(selfChange);
				loadInfo();
			}
		};
		initHandler();

		recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
		recyclerview.setHasFixedSize(true);
		mListTitle.setText(getString(R.string.babyimg));

		final Map<String, String> selects = BOBDataManager.getInstance().getBabyVideoMap();
		List<ExpandableListAdapter.Item> data = new ArrayList<>();
		int i =0;
		for(Map.Entry<String, String> entry : selects.entrySet()) {
			String key = entry.getKey();

			data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, key, selects.get(key), 0));
			i++;

		}

		ExpandableListAdapter adapter = new ExpandableListAdapter(getActivity(),data);
		adapter.setListener(new ExpandableListAdapter.LisClickListener() {
			@Override
			public void headerClick(String key) {
				mTitle.setText(selects.get(key));
				if (key.equals("1")) { //宝宝视讯
					goToPlayVideoFragment();
				} else if (key.equals("2")) { //宝宝影片
					goToRecordVideoFragment();
				} else if (key.equals("3")) { //宝宝照片
					goToAlbumFragment();
				} else if (key.equals("4")) { //宝宝游泳
					activity.postBabySwim();
					goToSwimmingVideoFragment();
				}
			}

			@Override
			public void headerExpand(int pos) {

			}

			@Override
			public void childClick(String key, String cKey) {

			}
		});
		if(adapter != null){
			adapter.setKey("1");
		}
		recyclerview.setAdapter(adapter);

		rootView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});

		activity.postBabySwim();

		mTitle.setText(selects.get("1"));

		return rootView;
	}

	private void goToAlbumFragment(){
		Fragment f = getChildFragmentManager().findFragmentById(R.id.content_layout);
		if(f instanceof BOBBabyAlbumFragment){
			return;
		}

		FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
		BOBBabyAlbumFragment fragment = new BOBBabyAlbumFragment();
		fragment.setInfo(mCamera);
		transaction.replace(R.id.content_layout, fragment);
		transaction.commitAllowingStateLoss();
	}

	private void goToRecordVideoFragment(){
		Fragment f = getChildFragmentManager().findFragmentById(R.id.content_layout);
		if(f instanceof BOBBabyVideoFolderFragment){
			return;
		}

		FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
		BOBBabyVideoFolderFragment fragment = new BOBBabyVideoFolderFragment();
		fragment.setInfo(mCamera);
		transaction.replace(R.id.content_layout, fragment);
		transaction.commitAllowingStateLoss();
	}


	private void goToPlayVideoFragment(){
		Fragment f = getChildFragmentManager().findFragmentById(R.id.content_layout);
		if(f instanceof BOBBabyPlayVideoFragment && !(f instanceof BOBBabySwimmingVideoFragment)){
			return;
		}
		FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
		BOBBabyPlayVideoFragment fragment = new BOBBabyPlayVideoFragment();
		fragment.setInfo(mCamera);
		transaction.replace(R.id.content_layout, fragment);
		transaction.commitAllowingStateLoss();
	}

	private void goToSwimmingVideoFragment(){
		Fragment f = getChildFragmentManager().findFragmentById(R.id.content_layout);
		if(f instanceof BOBBabySwimmingVideoFragment){
			return;
		}
		FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
		BOBBabySwimmingVideoFragment fragment = new BOBBabySwimmingVideoFragment();
		fragment.setInfo(mCamera);
		transaction.replace(R.id.content_layout, fragment);
		transaction.commitAllowingStateLoss();
	}


	private void initHandler() {
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


	public void setData(){
		if(!isAdded()) {
			return;
		}
		mCamera = BOBDataManager.getInstance().getCamera();

		goToPlayVideoFragment();
	}

	@Override
	public void onClick(View v) {
	}

	@Override
	public void onResume() {
		super.onResume();
		getActivity().getContentResolver().registerContentObserver(BOBProvider.URI_Camera, true, infoObserver);
	}

	@Override
	public void onPause() {
		super.onPause();
		getActivity().getContentResolver().unregisterContentObserver(infoObserver);
	}


}
