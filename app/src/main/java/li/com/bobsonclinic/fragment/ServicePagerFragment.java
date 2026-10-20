package li.com.bobsonclinic.fragment;

/**
 * Created by Milla.Wang on 2019/6/6.
 * Copyright © 2019 bobsonclinic. All rights reserved.
 */

import android.app.Activity;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import li.com.bobsonclinic.R;
import li.com.bobsonclinic.activity.MainActivity;
import li.com.bobsonclinic.adapter.TabPagerAdapter;
import li.com.bobsonclinic.fragment.base.BaseFragment;
import li.com.bobsonclinic.viewer.TabPage;


public class ServicePagerFragment extends BaseFragment implements View.OnClickListener, ViewPager.OnPageChangeListener  {

    private View rootView;
    private TabLayout tabs;
    private ViewPager viewPager;
    private TabPagerAdapter adapter;
    private List<TabPage> tabPages;
    private TextView title;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof MainActivity) {
            _mActivity = (MainActivity) activity;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((MainActivity)getActivity()).showLoading();
        rootView =  super.onCreateView(inflater, container, savedInstanceState);
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_tab_service, container, false);
        }
        tabs = rootView.findViewById(R.id.tabs);
        viewPager= rootView.findViewById(R.id.viewpager);
        title = rootView.findViewById(R.id.title);
        initView();
        ((MainActivity)getActivity()).dismissLoading();
        return rootView;
    }

    private void initView() {
        tabPages = new ArrayList<>();
        tabPages.add(new TabPage(getString(R.string.monthclass), new BOBMonthClassFragment()));
        tabPages.add(new TabPage(getString(R.string.addservice), new BOBAddServiceFragment()));
        tabPages.add(new TabPage(getString(R.string.consoumerdetail), new BOBConsumerFragment()));
        adapter = new TabPagerAdapter(getChildFragmentManager(), tabPages);
        viewPager.setAdapter(adapter);
        tabs.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(this);
        title.setText(tabPages.get(0).getName()); //Default
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {

        super.onPause();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        title.setText(tabPages.get(i).getName()); //Default
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}


