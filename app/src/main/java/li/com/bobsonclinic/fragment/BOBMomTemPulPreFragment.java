package li.com.bobsonclinic.fragment;

/**
 * Agricola
 *
 * Created by Milla.Wang on 2016/1/5.
 * Copyright © 2016 bobsonclinic. All rights reserved.
 */

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import li.com.bobsonclinic.R;
import li.com.bobsonclinic.activity.MainActivity;
import li.com.bobsonclinic.adapter.BOBMomTemPulPreAdapter;
import li.com.bobsonclinic.data.BOBMomTemPulPre;
import li.com.bobsonclinic.fragment.base.BaseFragment;
import li.com.bobsonclinic.manager.BOBDataManager;
import li.com.bobsonclinic.viewer.DividerItemDecoration;


public class BOBMomTemPulPreFragment extends BaseFragment implements View.OnClickListener {

	private ViewGroup rootView;
	private RecyclerView recyclerview;
	BOBMomTemPulPreAdapter adapter;
	private String title = "";

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		rootView = (ViewGroup) inflater.inflate(R.layout.fragment_mom_tempterure_blood, null);
		((MainActivity)getActivity()).postMomExamTemPul();
		recyclerview = (RecyclerView) rootView.findViewById(R.id.recyclerview);
		recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
		recyclerview.addItemDecoration(new DividerItemDecoration(getActivity(), false, false));
		adapter = new BOBMomTemPulPreAdapter(getActivity());
		recyclerview.setAdapter(adapter);
		setData();

		TextView titleTxv = rootView.findViewById(R.id.title);
		titleTxv.setText(title);

		rootView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});

		return rootView;
	}

	public void setData(){
		List<BOBMomTemPulPre> list = BOBDataManager.getInstance().getMomTemPulPreList();
		if(adapter != null){
			adapter.setList(list);
		}
	}



	@Override
	public void onClick(View v) {

	}


}
