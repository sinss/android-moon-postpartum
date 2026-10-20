package li.com.bobsonclinic.fragment;

/**
 * Agricola
 * <p/>
 * Created by Milla.Wang on 2016/1/5.
 * Copyright © 2016 bobsonclinic. All rights reserved.
 */

import android.app.Activity;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import li.com.bobsonclinic.R;
import li.com.bobsonclinic.activity.MainActivity;
import li.com.bobsonclinic.adapter.BOBTodayAdapter;
import li.com.bobsonclinic.data.BOBTodaySchedule;
import li.com.bobsonclinic.fragment.base.BaseFragment;
import li.com.bobsonclinic.manager.BOBDataManager;
import li.com.bobsonclinic.provider.BOBProvider;
import li.com.bobsonclinic.viewer.RecyclerViewEmptySupport;


public class BOBTodayPlanFragment extends BaseFragment  {

    private ViewGroup rootView;
    private RecyclerViewEmptySupport mTodayRecyclerView;
    BOBTodayAdapter mTodayAdapter;

    private Handler mHandler = new Handler();
    private MainActivity activity;
    protected ContentObserver infoObserver;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof li.com.bobsonclinic.activity.MainActivity) {
            this.activity = (MainActivity) activity;
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_list, null);
        mTodayRecyclerView = rootView.findViewById(R.id.recyclerview);
        mTodayAdapter = new BOBTodayAdapter(getActivity());
        mTodayAdapter.setListener(new BOBTodayAdapter.FunctionListener() {
            @Override
            public void onClick(String activityId, String vasId) {
                if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
                if (activityId != null && !activityId.equals("")) {
                    activity.goMonthClassDetail(activityId);
                }
                if (vasId != null && !vasId.equals("")) {
                    activity.goAddServiceDetail(vasId);
                }
            }
        });
        mTodayRecyclerView.setEmptyView(rootView.findViewById(R.id.list_empty));
        mTodayRecyclerView.setAdapter(mTodayAdapter);
        mTodayRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }



    public void loadInfo(){
        if(!isAdded())
            return;
        List<BOBTodaySchedule> list = BOBDataManager.getInstance().getTodayList();
        if(list == null)
            return;
        if(mTodayAdapter != null)
            mTodayAdapter.setList(list);
    }

    @Override
    public void onResume() {
        super.onResume();

        activity.getContentResolver().registerContentObserver(BOBProvider.URI_TodayPlan, true, infoObserver);
        activity.postTodaySchedule();
    }

    @Override
    public void onPause() {

        super.onPause();

        activity.getContentResolver().unregisterContentObserver(infoObserver);
    }

}
