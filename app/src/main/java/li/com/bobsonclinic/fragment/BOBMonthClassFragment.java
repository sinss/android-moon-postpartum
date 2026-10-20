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
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import li.com.bobsonclinic.R;
import li.com.bobsonclinic.activity.MainActivity;
import li.com.bobsonclinic.adapter.BOBMonthSubjectAdapter;
import li.com.bobsonclinic.data.BOBMonthActivity;
import li.com.bobsonclinic.fragment.base.BaseFragment;
import li.com.bobsonclinic.manager.BOBDataManager;
import li.com.bobsonclinic.provider.BOBProvider;
import li.com.bobsonclinic.viewer.RecyclerViewEmptySupport;


public class BOBMonthClassFragment extends BaseFragment implements View.OnClickListener {

	private ViewGroup rootView;
	private RecyclerViewEmptySupport recyclerview;
	private BOBMonthSubjectAdapter mAdapter;
	private int thisMonth = 0;
	private int currentMonth;
	Calendar mCal;
	TextView date;
	private final static String ID_KEY = "activityId";
	protected ContentObserver infoObserver;
	protected Handler mHandler;

	public static BOBMonthClassFragment createFragment(String id) {
		BOBMonthClassFragment fragment = new BOBMonthClassFragment();
		Bundle bundle = new Bundle();
		bundle.putString(ID_KEY, id);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		rootView = (ViewGroup) inflater.inflate(R.layout.fragment_month_class, null);
		recyclerview = rootView.findViewById(R.id.recyclerview);
		rootView.findViewById(R.id.left_btn).setOnClickListener(this);
		rootView.findViewById(R.id.right_btn).setOnClickListener(this);
		recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 1));
		recyclerview.setEmptyView(rootView.findViewById(R.id.list_empty));
		mAdapter = new BOBMonthSubjectAdapter(getActivity());
		mAdapter.setListener(new BOBMonthSubjectAdapter.classFunctioListener() {
			@Override
			public void onClick(String id) {
				((MainActivity) getActivity()).goMonthClassDetail(id);
			}
		});
		recyclerview.setAdapter(mAdapter);
		mCal=Calendar.getInstance();
		int month = mCal.get(Calendar.MONTH);
		currentMonth = month;
		date = (TextView)rootView.findViewById(R.id.date);
		date.setText(mCal.get(Calendar.YEAR) + "/" + (currentMonth + 1) + getActivity().getString(R.string.m_class));


		rootView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});

		if(getArguments() != null && getArguments().getString(ID_KEY) != null){
			((MainActivity) getActivity()).goMonthClassDetail(getArguments().getString(ID_KEY));
		}
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

	@Override
	public void onResume() {
		super.onResume();

		getActivity().getContentResolver().registerContentObserver(BOBProvider.URI_MonthClass, true, infoObserver);
		((MainActivity)getActivity()).postMonthActivity();
	}

	@Override
	public void onPause() {

		super.onPause();

		getActivity().getContentResolver().unregisterContentObserver(infoObserver);
	}
/*
	public void loadInfo(){
		List<BOBMonthActivity> list = BOBDataManager.getInstance().getMonthList();

		if(list == null){
			return;
		}


		if(mAdapter != null) {
			mAdapter.setList(list);
		}
	}
*/
	public void loadInfo(){
		List<BOBMonthActivity> list = null;
		if(thisMonth == 0){
			list = BOBDataManager.getInstance().getMonthList();
		} else {
			date.setText(mCal.get(Calendar.YEAR) + "/" + (currentMonth + thisMonth + 1) + getActivity().getString(R.string.m_class));
			list = BOBDataManager.getInstance().getMonthClassMap().get(String.valueOf(thisMonth));
		}

		if(list == null || list.size() < 1){
			//mNoData.setVisibility(View.VISIBLE);
		}

		if(mAdapter != null) {
			mAdapter.setList(list);
		}
	}

	public void setDateView(){
		//mNoData.setVisibility(View.GONE);

		date.setText(mCal.get(Calendar.YEAR)+"/"+(currentMonth+thisMonth+1)+ getActivity().getString(R.string.m_class));

	}


	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.left_btn:
				thisMonth--;
				setDateView();
				((MainActivity)getActivity()).postMonthActivity(String.valueOf(thisMonth));
				if((currentMonth + thisMonth) < 1){
					rootView.findViewById(R.id.left_btn).setVisibility(View.GONE);
				}
				if((currentMonth + thisMonth) < 11){
					rootView.findViewById(R.id.right_btn).setVisibility(View.VISIBLE);
				}

				break;
			case R.id.right_btn:
				thisMonth++;
				setDateView();
				((MainActivity)getActivity()).postMonthActivity(String.valueOf(thisMonth));
				if((currentMonth + thisMonth) >= 1){
					rootView.findViewById(R.id.left_btn).setVisibility(View.VISIBLE);
				}
				if((currentMonth + thisMonth) >= 11){
					rootView.findViewById(R.id.right_btn).setVisibility(View.GONE);
				}
				break;
		}
	}


}
