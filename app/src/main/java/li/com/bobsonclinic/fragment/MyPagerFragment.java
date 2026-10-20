package li.com.bobsonclinic.fragment;

/**
 * Created by Milla.Wang on 2019/6/6.
 * Copyright © 2019 bobsonclinic. All rights reserved.
 */

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
import li.com.bobsonclinic.adapter.TabPagerAdapter;
import li.com.bobsonclinic.fragment.base.BaseFragment;
import li.com.bobsonclinic.viewer.TabPage;


public class MyPagerFragment extends BaseFragment implements ViewPager.OnPageChangeListener {

    private View rootView;
    private TabLayout tabs;
    private ViewPager viewPager;
    private TabPagerAdapter adapter;
    private List<TabPage> tabPages;
    private TextView title;
    private int currentTab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView =  super.onCreateView(inflater, container, savedInstanceState);
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_tab_my, container, false);
        }
        tabs = rootView.findViewById(R.id.tabs);
        title = rootView.findViewById(R.id.title);
        viewPager= rootView.findViewById(R.id.viewpager);
        initView();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(tabPages.get(currentTab).getFragment() instanceof BOBBabyAlbumFragment){
            ( (BOBBabyAlbumFragment)tabPages.get(currentTab).getFragment()).refreshAdapter();
        }
        //((MainActivity)getActivity()).showLoading();

    }

    private void initView() {
        tabPages = new ArrayList<>();
        tabPages.add(new TabPage(getString(R.string.momeydialy), new BOBMomDialyListFragment()));
        tabPages.add(new TabPage(getString(R.string.mybaby), new BOBMyBabyListFragment()));
        tabPages.add(new TabPage(getString(R.string.baby_photo), new BOBBabyAlbumFragment()));
        adapter = new TabPagerAdapter(getChildFragmentManager(), tabPages);
        viewPager.setAdapter(adapter);
        tabs.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(this);
        title.setText(tabPages.get(0).getName()); //Default
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        currentTab = i;
        title.setText(tabPages.get(i).getName());
        if(tabPages.get(i).getFragment() instanceof BOBBabyAlbumFragment){
            ( (BOBBabyAlbumFragment)tabPages.get(i).getFragment()).refreshAdapter();
        }

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}


