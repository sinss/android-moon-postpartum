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

import java.util.List;

import li.com.bobsonclinic.R;
import li.com.bobsonclinic.activity.MainActivity;
import li.com.bobsonclinic.adapter.BOBServiceAdapter;
import li.com.bobsonclinic.data.BOBVasList;
import li.com.bobsonclinic.fragment.base.BaseFragment;
import li.com.bobsonclinic.manager.BOBDataManager;
import li.com.bobsonclinic.provider.BOBProvider;
import li.com.bobsonclinic.viewer.RecyclerViewEmptySupport;


public class BOBAddServiceFragment extends BaseFragment implements View.OnClickListener {

	private ViewGroup rootView;
	private RecyclerViewEmptySupport recyclerview;
	private BOBServiceAdapter adapter;
	private final static String ID_KEY = "vasid";
	protected ContentObserver infoObserver;
	protected Handler mHandler;

	public static BOBAddServiceFragment createFragment(String id) {
		BOBAddServiceFragment fragment = new BOBAddServiceFragment();
		Bundle bundle = new Bundle();
		bundle.putString(ID_KEY, id);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		rootView = (ViewGroup) inflater.inflate(R.layout.fragment_list, null);
		recyclerview = rootView.findViewById(R.id.recyclerview);
		recyclerview.setEmptyView(rootView.findViewById(R.id.list_empty));
		recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 1));
		adapter = new BOBServiceAdapter(getActivity());
		recyclerview.setAdapter(adapter);
		adapter.setListener(new BOBServiceAdapter.vasFunctioListener() {
			@Override
			public void onClick(String id) {
				((MainActivity) getActivity()).goAddServiceDetail(id);
			}
		});

		rootView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});

		if(getArguments() != null && getArguments().getString(ID_KEY) != null){
			((MainActivity) getActivity()).goAddServiceDetail(getArguments().getString(ID_KEY));
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

	public void loadInfo(){
		if(!isAdded()){
			return;
		}

		List<BOBVasList> list = BOBDataManager.getInstance().getVasList();

		if(adapter != null) {
			adapter.setList(list);
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

		getActivity().getContentResolver().registerContentObserver(BOBProvider.URI_AddService, true, infoObserver);
		((MainActivity) getActivity()).postVasList();
	}

	@Override
	public void onPause() {

		super.onPause();

		getActivity().getContentResolver().unregisterContentObserver(infoObserver);
	}

}
