package li.com.bobsonclinic.fragment;

/**
 * Created by Milla.Wang on 2019/6/6.
 * Copyright © 2019 bobsonclinic. All rights reserved.
 */

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import li.com.bobsonclinic.R;
import li.com.bobsonclinic.activity.MainActivity;
import li.com.bobsonclinic.adapter.TabPagerAdapter;
import li.com.bobsonclinic.fragment.base.BaseFragment;
import li.com.bobsonclinic.viewer.TabPage;


public class HomePagerFragment extends BaseFragment implements View.OnClickListener,ViewPager.OnPageChangeListener {

    private ViewGroup rootView;
    private TabLayout tabs;
    private ViewPager viewPager;
    private TabPagerAdapter adapter;
    private List<TabPage> tabPages;
    private TextView title;
    private Button logout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_tab_home, null);
        tabs = rootView.findViewById(R.id.tabs);
        viewPager= rootView.findViewById(R.id.viewpager);
        title = rootView.findViewById(R.id.title);
        logout = rootView.findViewById(R.id.btn_logout);
        initView();
        return rootView;
    }

    private void initView() {
        tabPages = new ArrayList<>();
        //tabPages.add(new TabPage("", new BOBHomePageFragment()));
        tabPages.add(new TabPage(getString(R.string.news), new BOBNewsFragment()));
        tabPages.add(new TabPage(getString(R.string.today_schedule), new BOBTodayPlanFragment()));
        adapter = new TabPagerAdapter(getChildFragmentManager(), tabPages);
        title.setText(tabPages.get(0).getName()); //Default
        logout.setOnClickListener(this);
        viewPager.setAdapter(adapter);
        tabs.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(this);

        //tabs.getTabAt(0).setIcon(R.mipmap.ic_launcher);
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d("onResume", "HomePagerFragment onResume");
    }

    @Override
    public void onPause() {

        super.onPause();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_logout:
                ((MainActivity) getActivity()).logout();
                break;
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


