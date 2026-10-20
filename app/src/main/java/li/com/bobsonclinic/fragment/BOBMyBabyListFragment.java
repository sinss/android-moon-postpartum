package li.com.bobsonclinic.fragment;

/**
 * Agricola
 *
 * Created by Milla.Wang on 2016/1/5.
 * Copyright © 2016 bobsonclinic. All rights reserved.
 */

import android.os.Bundle;
import androidx.fragment.app.Fragment;
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
import li.com.bobsonclinic.data.server.BOBBabyBreathResponse;
import li.com.bobsonclinic.data.server.BOBBabyJaundiceResponse;
import li.com.bobsonclinic.data.server.BOBBabyMilkResponse;
import li.com.bobsonclinic.data.server.BOBBabyPulseResponse;
import li.com.bobsonclinic.data.server.BOBBabyTemperatureResponse;
import li.com.bobsonclinic.data.server.BOBBabyWeightResponse;
import li.com.bobsonclinic.fragment.base.BaseFragment;
import li.com.bobsonclinic.fragment.chart.ChartFragment;
import li.com.bobsonclinic.manager.BOBDataManager;


public class BOBMyBabyListFragment extends BaseFragment implements View.OnClickListener {

	private ViewGroup rootView;
	private RecyclerView recyclerview;
	private TextView mTitle;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		rootView = (ViewGroup) inflater.inflate(R.layout.fragment_list, null);
		recyclerview = (RecyclerView) rootView.findViewById(R.id.recyclerview);
		mTitle  = (TextView) rootView.findViewById(R.id.title);

		recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
		recyclerview.setHasFixedSize(true);

		final Map<String, String> selects = BOBDataManager.getInstance().getMyBabyMap();
		List<ExpandableListAdapter.Item> data = new ArrayList<>();
		int i =0;
		for(Map.Entry<String, String> entry : selects.entrySet()) {
			String key = entry.getKey();

			data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, key, entry.getValue(), 0));
			i++;

		}


		ExpandableListAdapter adapter = new ExpandableListAdapter(getActivity(),data);
		adapter.setListener(new ExpandableListAdapter.LisClickListener() {
			@Override
			public void headerClick(String key) {
				//mTitle.setText(selects.get(key));
				if (key.equals("1")) { //检查总表
					((MainActivity)getActivity()).postBabyExam();
					goExplaionFragment(selects.get(key));
				} else if (key.equals("2")) { //黃疸
					((MainActivity)getActivity()).postBabyJaundice();
					goChartFragment(new BOBBabyJaundiceResponse(),selects.get(key));
				} else if (key.equals("3")) { //体温
					((MainActivity)getActivity()).postBabyTemperature();
					goChartFragment(new BOBBabyTemperatureResponse(),selects.get(key));
				} else if (key.equals("4")) { //体重
					((MainActivity)getActivity()).postBabyWeight();
					goChartFragment(new BOBBabyWeightResponse(),selects.get(key));
				} else if (key.equals("5")) { //奶量
					((MainActivity)getActivity()).postBabyMilk();
					goChartFragment(new BOBBabyMilkResponse(),selects.get(key));
				} else if (key.equals("6")) { //宝宝脉搏
					((MainActivity)getActivity()).postBabyPulse();
					goChartFragment(new BOBBabyPulseResponse(),selects.get(key));
				} else if (key.equals("7")) { //宝宝呼吸
					((MainActivity)getActivity()).postBabyBreath();
					goChartFragment(new BOBBabyBreathResponse(),selects.get(key));
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
			adapter.setKey("1"); //預設第一筆
		}
		recyclerview.setAdapter(adapter);
		//mTitle.setText(selects.get("1"));
		((MainActivity)getActivity()).postBabyExam();
		rootView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});

		return rootView;
	}

	private void goExplaionFragment(String title){
		BOBBabyExamFragment fragment = new BOBBabyExamFragment();
		fragment.setTitle(title);
		((MainActivity) getActivity()).addFragment(fragment);
		/*
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.baby_fragment, new BOBBabyExamFragment());
		transaction.commitAllowingStateLoss();
		*/
	}

	private void goChartFragment(Object obj, String title){
		/*
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		ChartFragment fragment = new ChartFragment();
		fragment.setChartData(obj);
		transaction.replace(R.id.baby_fragment, fragment);
		transaction.commitAllowingStateLoss();
		*/
		ChartFragment fragment = new ChartFragment();
		fragment.setChartData(obj);
		fragment.setTitle(title);
		((MainActivity) getActivity()).addFragment(fragment);
	}

	public void setChartData(Object obj){
		if(!isAdded())
			return;
		if(obj == null)
			return;
		Fragment fm = getFragmentManager().findFragmentById(R.id.baby_fragment);
		if(fm instanceof ChartFragment){
			((ChartFragment)fm).setChartData(obj);
		}
	}

	public void refreshExamData(){

		Fragment fm = getFragmentManager().findFragmentById(R.id.baby_fragment);
		if(fm instanceof  BOBBabyExamFragment){
			((BOBBabyExamFragment)fm).setData();
		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){

		}
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	@Override
	public void onPause() {
		super.onPause();
	}
}
