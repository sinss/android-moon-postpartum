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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

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


public class BOBEduInfoListFragment extends BaseFragment implements View.OnClickListener {

    private ViewGroup rootView;
    private RecyclerView recyclerview;
    ExpandableListAdapter adapter;
    private ContentObserver infoObserver;
    private Handler mHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_list, null);
        recyclerview = rootView.findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerview.setHasFixedSize(true);


        adapter = new ExpandableListAdapter(getActivity());
        recyclerview.setAdapter(adapter);

        initHandler();

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

        ((MainActivity) getActivity()).postEducationIntroList();

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

        getActivity().getContentResolver().registerContentObserver(BOBProvider.URI_EduInfo, true, infoObserver);
    }

    @Override
    public void onPause() {

        super.onPause();

        getActivity().getContentResolver().unregisterContentObserver(infoObserver);
    }

    public void setData() {
        try {

            final Map<String, BOBUIRightList> header = BOBDataManager.getInstance().getEducationData();
            List<ExpandableListAdapter.Item> data = new ArrayList<>();
            int i = 0;
            for (Map.Entry<String, BOBUIRightList> entry : header.entrySet()) {
                String key = entry.getKey();
                if (adapter != null) {
                    adapter.setKey(key);
                }

                if (header.get(key) != null) {
                    data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, key, header.get(key).getTitle(), header.get(key).getSubData().size()));
                    final Map<String, BOBUIRightList> child = header.get(key).getSubData();
                    int childIndex = 0;
                    for (Map.Entry<String, BOBUIRightList> entryChild : child.entrySet()) {
                        String ckey = entryChild.getKey();
                        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, key, ckey, child.get(ckey).getTitle()));
                        //if(i == 0 && childIndex == 0){
                        if (adapter != null) {
                            adapter.setKey(key);
                        }
                        childIndex++;
                    }
                    i++;
                }
            }
            adapter.setList(data);
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
                    if (recyclerview != null) {
                        recyclerview.scrollToPosition(pos + 1);
                    }
                }
            });

        } catch (ConcurrentModificationException e) {
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }


}
