package li.com.bobsonclinic.fragment;

/**
 * Agricola
 * <p/>
 * Created by Milla.Wang on 2016/1/5.
 * Copyright © 2016 bobsonclinic. All rights reserved.
 */

import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.Nullable;
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
import li.com.bobsonclinic.activity.MainPadActivity;
import li.com.bobsonclinic.adapter.ExpandableListAdapter;
import li.com.bobsonclinic.data.server.BOBMomBreathResponse;
import li.com.bobsonclinic.data.server.BOBMomPressureResponse;
import li.com.bobsonclinic.data.server.BOBMomPulseResponse;
import li.com.bobsonclinic.data.server.BOBMomTemperatureResponse;
import li.com.bobsonclinic.data.server.BOBMomWeightResponse;
import li.com.bobsonclinic.fragment.base.BaseFragment;
import li.com.bobsonclinic.fragment.chart.ChartFragment;
import li.com.bobsonclinic.manager.BOBDataManager;


public class BOBMomDialyFragment extends BaseFragment implements View.OnClickListener {

    private long reqId = -1;

    private ViewGroup rootView;
    private RecyclerView recyclerview;
    private TextView mTitle;
    private TextView mListTitle;
    ExpandableListAdapter adapter;
    LinearLayoutManager layoutmanager;
    private Parcelable recyclerViewState;
    List<ExpandableListAdapter.Item> data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_mom_dialy, null);
        recyclerview = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        mTitle = (TextView) rootView.findViewById(R.id.title);
        mListTitle = (TextView) rootView.findViewById(R.id.list_title);
        layoutmanager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerview.setLayoutManager(layoutmanager);
        recyclerview.setHasFixedSize(true);
        //recyclerview.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));

        mListTitle.setText(getString(R.string.momeydialy));

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
                mTitle.setText(selects.get(key));
                if (key.equals("1")) { //检查总表
                    ((MainPadActivity)getActivity()).postExplain();
                    goExamFragment();
                } else if (key.equals("2")) { //每日体温丶脉博丶血压
                    goTempertureFragment();
                } else if (key.equals("3")) { //体温
                    ((MainPadActivity)getActivity()).postMomTemperature();
                    goChartFragment(new BOBMomTemperatureResponse());
                } else if (key.equals("4")) { //血压
                    //TODO input BOBMomPressureResponse
                    ((MainPadActivity)getActivity()).postMomPressure();
                    goChartFragment(new BOBMomPressureResponse());
                } else if (key.equals("5")) { //呼吸
                    ((MainPadActivity)getActivity()).postMomBreath();
                    goChartFragment(new BOBMomBreathResponse());
                } else if (key.equals("6")) { //脉搏
                    ((MainPadActivity)getActivity()).postMomPulse();
                    goChartFragment(new BOBMomPulseResponse());
                }else if (key.equals("")) { //體重 ??
                    ((MainPadActivity)getActivity()).postMomWeight();
                    goChartFragment(new BOBMomWeightResponse());
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
        mTitle.setText(selects.get("1"));
        goExamFragment();

        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //reqId = CommunicationLogic.getInstance().postMomPressure(BOBSystemManager.getInstance().getUserToken());
    }

    private void goExamFragment(){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.mom_fragment, new BOBMomExamFragment());
        transaction.commitAllowingStateLoss();
    }

    private void goTempertureFragment(){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.mom_fragment, new BOBMomTemPulPreFragment());
        transaction.commitAllowingStateLoss();
    }

    private void goChartFragment(Object obj){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        ChartFragment fragment = new ChartFragment();
        fragment.setChartData(obj);
        transaction.replace(R.id.mom_fragment, fragment);
        transaction.commitAllowingStateLoss();
    }


    public void setChartData(Object obj){
        if(!isAdded())
            return;
        if(obj == null)
            return;

        Fragment fm = getFragmentManager().findFragmentById(R.id.mom_fragment);
        if(fm instanceof  ChartFragment){
            ((ChartFragment)fm).setChartData(obj);
        }
    }

    public void refreshExamData(){

        Fragment fm = getFragmentManager().findFragmentById(R.id.mom_fragment);
        if(fm instanceof  BOBMomExamFragment){
            ((BOBMomExamFragment)fm).setData();
        }
    }

    public void refreshTemPulPreData(){
        if(!isAdded())
            return;

        Fragment fm = getFragmentManager().findFragmentById(R.id.mom_fragment);
        if(fm instanceof  BOBMomTemPulPreFragment){
            ((BOBMomTemPulPreFragment)fm).setData();
        }
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }


}
