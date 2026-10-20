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

import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import li.com.bobsonclinic.R;
import li.com.bobsonclinic.activity.MainActivity;
import li.com.bobsonclinic.adapter.BOBConsumerAdapter;
import li.com.bobsonclinic.data.BOBConsumerList;
import li.com.bobsonclinic.fragment.base.BaseFragment;
import li.com.bobsonclinic.manager.BOBDataManager;
import li.com.bobsonclinic.provider.BOBProvider;
import li.com.bobsonclinic.viewer.RecyclerViewEmptySupport;


public class BOBConsumerFragment extends BaseFragment implements View.OnClickListener {

	private ViewGroup rootView;
	private BOBConsumerAdapter mAdapter;
	private RecyclerViewEmptySupport mRecyclerView;
	private LinearLayoutManager mLayoutManager;
	protected ContentObserver infoObserver;
	protected Handler mHandler;
	private MainActivity activity;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof MainActivity) {
			this.activity = (MainActivity) activity;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		rootView = (ViewGroup) inflater.inflate(R.layout.fragment_list, null);

		mRecyclerView = rootView.findViewById(R.id.recyclerview);
		mRecyclerView.setEmptyView(rootView.findViewById(R.id.list_empty));
		mLayoutManager = new LinearLayoutManager(getActivity());
		mAdapter = new BOBConsumerAdapter(getActivity());
		mRecyclerView.setAdapter(mAdapter);
		mRecyclerView.setLayoutManager(mLayoutManager);
		//mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), false, true));
		rootView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
		loadInfo();
		mHandler = new Handler();
		infoObserver = new ContentObserver(mHandler)
		{
			@Override
			public void onChange(boolean selfChange)
			{
				super.onChange(selfChange);
				loadInfo();
			}
		};

		return rootView;
	}

	public void loadInfo(){
		List<BOBConsumerList> mConsumerList = BOBDataManager.getInstance().getConsumerList();
		mAdapter.setList(mConsumerList);
	}


	@Override
	public void onClick(View v) {
		switch(v.getId()){

		}
	}

	@Override
	public void onResume() {
		super.onResume();

		activity.getContentResolver().registerContentObserver(BOBProvider.URI_Consumer, true, infoObserver);
		activity.postConsumerList();
	}

	@Override
	public void onPause() {

		super.onPause();

		activity.getContentResolver().unregisterContentObserver(infoObserver);
	}
}
