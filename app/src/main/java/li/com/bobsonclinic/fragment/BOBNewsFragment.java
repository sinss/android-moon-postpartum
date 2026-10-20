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
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import li.com.bobsonclinic.R;
import li.com.bobsonclinic.activity.MainActivity;
import li.com.bobsonclinic.adapter.BOBMessageAdapter;
import li.com.bobsonclinic.data.BOBNotifaction;
import li.com.bobsonclinic.fragment.base.BaseFragment;
import li.com.bobsonclinic.manager.BOBDataManager;
import li.com.bobsonclinic.provider.BOBProvider;
import li.com.bobsonclinic.viewer.RecyclerViewEmptySupport;


public class BOBNewsFragment extends BaseFragment  {

    private ViewGroup rootView;
    private RecyclerViewEmptySupport mMessageRecyclerView;
    BOBMessageAdapter mMessageAdapter;
    private Handler mHandler = new Handler();
    private MainActivity activity;
    protected ContentObserver infoObserver;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof MainActivity) {
            this.activity = (MainActivity) activity;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_list, null);
        mMessageRecyclerView = rootView.findViewById(R.id.recyclerview);
        mMessageAdapter = new BOBMessageAdapter(getActivity());
        mMessageRecyclerView.setAdapter(mMessageAdapter);
        mMessageRecyclerView.setEmptyView(rootView.findViewById(R.id.list_empty));
        mMessageRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
        Log.d("onResume", "BOBNewsFragment loadInfo");
        List<BOBNotifaction> list = BOBDataManager.getInstance().getMessageList();
        if(list == null)
            return;
        if(!isAdded())
            return;
        if(mMessageAdapter != null)
            mMessageAdapter.setList(list);
    }

    @Override
    public void onResume() {
        super.onResume();
        activity.getContentResolver().registerContentObserver(BOBProvider.URI_News, true, infoObserver);
        activity.postMessage();
        Log.d("onResume", "BOBNewsFragment onResume");
    }

    @Override
    public void onPause() {

        super.onPause();

        activity.getContentResolver().unregisterContentObserver(infoObserver);
    }

}
