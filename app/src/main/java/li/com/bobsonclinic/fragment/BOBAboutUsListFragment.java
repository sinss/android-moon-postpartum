package li.com.bobsonclinic.fragment;

/**
 * Agricola
 * <p>
 * Created by Milla.Wang on 2016/1/5.
 * Copyright © 2016 bobsonclinic. All rights reserved.
 */

import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Map;

import li.com.bobsonclinic.R;
import li.com.bobsonclinic.activity.MainActivity;
import li.com.bobsonclinic.adapter.ExpandableListAdapter;
import li.com.bobsonclinic.data.BOBUIRightList;
import li.com.bobsonclinic.fragment.base.BaseFragment;
import li.com.bobsonclinic.manager.BOBDataManager;
import li.com.bobsonclinic.provider.BOBProvider;


public class BOBAboutUsListFragment extends BaseFragment implements View.OnClickListener {

    private ViewGroup rootView;
    private RecyclerView recyclerview;
    ExpandableListAdapter adapter;
    protected ContentObserver infoObserver;
    protected Handler mHandler;
    LinearLayoutManager layoutmanager;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_list, null);
        recyclerview = rootView.findViewById(R.id.recyclerview);
        progressBar = rootView.findViewById(R.id.progress);
        //Log.d("Description", "Description :" + description);
        layoutmanager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerview.setLayoutManager(layoutmanager);
        recyclerview.setHasFixedSize(true);


        adapter = new ExpandableListAdapter(getActivity());
        recyclerview.setAdapter(adapter);


        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        infoObserver = new ContentObserver(mHandler) {
            @Override
            public void onChange(boolean selfChange) {
                super.onChange(selfChange);
                loadInfo();
            }
        };
        initHandler();
        ((MainActivity) getActivity()).postAboutUSIntroList();
        return rootView;
    }

    private void initHandler() {
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 0: {
                        setData();
                    }
                    break;
                }
                return true;
            }
        });
    }

    private void loadInfo() {

        Message msg = mHandler.obtainMessage();
        msg.what = 0;
        msg.obj = null;
        mHandler.sendMessageDelayed(msg, 500);
    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().getContentResolver().registerContentObserver(BOBProvider.URI_AboutUs, true, infoObserver);
    }

    @Override
    public void onPause() {

        super.onPause();

        getActivity().getContentResolver().unregisterContentObserver(infoObserver);
    }


    public void setData() {
        final Map<String, BOBUIRightList> header = BOBDataManager.getInstance().getAboutUsData();
        List<ExpandableListAdapter.Item> data = new ArrayList<>();
        int i = 0;
        try {
            for (Map.Entry<String, BOBUIRightList> entry : header.entrySet()) {
                String key = entry.getKey();
                Log.d("Milla", "test BOBAboutUsListFragment: " + key);
                //if(i == 0 && mTitle.getText().toString().length() == 0){
                if (adapter != null) {
                    adapter.setKey(key);
                }

                data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, key, header.get(key).getTitle(), header.get(key).getSubData().size()));
                final Map<String, BOBUIRightList> child = header.get(key).getSubData();
                for (Map.Entry<String, BOBUIRightList> entryChild : child.entrySet()) {
                    String ckey = entryChild.getKey();
                    data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, key, ckey, child.get(ckey).getTitle()));
                }
                i++;
            }
            adapter.setListener(new ExpandableListAdapter.LisClickListener() {
                @Override
                public void headerClick(String key) {
                    ((MainActivity) getActivity()).addFragment(BOBWebViewContentFragment.newInstance(header.get(key).getTitle(), header.get(key).getContentUrl()));
                }

                @Override
                public void childClick(String key, String childKey) {
                    if (header.get(key) != null && header.get(key).getSubData() != null && header.get(key).getSubData().get(childKey) != null) {
                        ((MainActivity) getActivity()).addFragment(BOBWebViewContentFragment.newInstance(header.get(key).getSubData().get(childKey).getTitle(), header.get(key).getSubData().get(childKey).getContentUrl()));
                    }
                }

                @Override
                public void headerExpand(int pos) {
                    Log.d("Milla", "headerExpand: " + pos + "，" + recyclerview);
                    if (recyclerview != null) {
                        layoutmanager.scrollToPositionWithOffset(pos, 0);
                        //recyclerview.scrollToPosition(pos+1);
                    }
                }
            });

            adapter.setList(data);
        } catch (ConcurrentModificationException e) {

        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }


}
