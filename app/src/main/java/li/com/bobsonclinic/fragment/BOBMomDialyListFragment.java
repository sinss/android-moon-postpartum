package li.com.bobsonclinic.fragment;

/**
 * Agricola
 * <p/>
 * Created by Milla.Wang on 2016/1/5.
 * Copyright © 2016 bobsonclinic. All rights reserved.
 */

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import li.com.bobsonclinic.data.server.BOBMomBreathResponse;
import li.com.bobsonclinic.data.server.BOBMomPressureResponse;
import li.com.bobsonclinic.data.server.BOBMomPulseResponse;
import li.com.bobsonclinic.data.server.BOBMomTemperatureResponse;
import li.com.bobsonclinic.data.server.BOBMomWeightResponse;
import li.com.bobsonclinic.fragment.base.BaseFragment;
import li.com.bobsonclinic.fragment.chart.ChartFragment;
import li.com.bobsonclinic.manager.BOBDataManager;


public class BOBMomDialyListFragment extends BaseFragment implements View.OnClickListener {


    private ViewGroup rootView;
    private RecyclerView recyclerview;
    private TextView mTitle;
    ExpandableListAdapter adapter;
    LinearLayoutManager layoutmanager;
    List<ExpandableListAdapter.Item> data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_list, null);
        recyclerview = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        mTitle = (TextView) rootView.findViewById(R.id.title);
        layoutmanager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerview.setLayoutManager(layoutmanager);
        recyclerview.setHasFixedSize(true);
        //recyclerview.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));


        final Map<String, String> selects = BOBDataManager.getInstance().getMomeyMap();
        data = new ArrayList<>();
        int i = 0;
        for (Map.Entry<String, String> entry : selects.entrySet()) {
            String key = entry.getKey();

            data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, key, entry.getValue(), 0));
            i++;

        }


        adapter = new ExpandableListAdapter(getActivity(),data);
        adapter.setListener(new ExpandableListAdapter.LisClickListener() {
            @Override
            public void headerClick(String key) {
                //mTitle.setText(selects.get(key));
                if (key.equals("1")) { //检查总表
                    ((MainActivity)getActivity()).postExplain();
                    goExamFragment(selects.get(key));
                } else if (key.equals("2")) { //每日体温丶脉博丶血压
                    goTempertureFragment(selects.get(key));
                } else if (key.equals("3")) { //体温
                    ((MainActivity)getActivity()).postMomTemperature();
                    goChartFragment(new BOBMomTemperatureResponse(),selects.get(key));
                } else if (key.equals("4")) { //血压
                    ((MainActivity)getActivity()).postMomPressure();
                    goChartFragment(new BOBMomPressureResponse(),selects.get(key));
                } else if (key.equals("5")) { //呼吸
                    ((MainActivity)getActivity()).postMomBreath();
                    goChartFragment(new BOBMomBreathResponse(),selects.get(key));
                } else if (key.equals("6")) { //脉搏
                    ((MainActivity)getActivity()).postMomPulse();
                    goChartFragment(new BOBMomPulseResponse(),selects.get(key));
                }else if (key.equals("")) { //體重 ??
                    ((MainActivity)getActivity()).postMomWeight();
                    goChartFragment(new BOBMomWeightResponse(),selects.get(key));
                }
            }

            @Override
            public void headerExpand(int pos) {
                adapter.notifyItemChanged(pos);
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

        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity)getActivity()).dismissLoading();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //reqId = CommunicationLogic.getInstance().postMomPressure(BOBSystemManager.getInstance().getUserToken());
    }

    private void goExamFragment(String title){
        BOBMomExamFragment fragment = new BOBMomExamFragment();
        fragment.setTitle(title);
        ((MainActivity) getActivity()).addFragment(fragment);
    }

    private void goTempertureFragment(String title){
        BOBMomTemPulPreFragment fragment = new BOBMomTemPulPreFragment();
        fragment.setTitle(title);
        ((MainActivity) getActivity()).addFragment(fragment);
    }

    private void goChartFragment(Object obj, String title){
        ChartFragment fragment = new ChartFragment();
        fragment.setChartData(obj);
        fragment.setTitle(title);
        ((MainActivity) getActivity()).addFragment(fragment);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }


}
