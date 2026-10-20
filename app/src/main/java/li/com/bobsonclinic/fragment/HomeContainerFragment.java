package li.com.bobsonclinic.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;

import li.com.bobsonclinic.R;
import li.com.bobsonclinic.logic.CommunicationLogic;
import li.com.bobsonclinic.manager.BOBSystemManager;


public class HomeContainerFragment extends BaseContainerFragment {

    // -------------------------------------------
    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        if (findChildFragment(HomePagerFragment.class) == null) {
            HomePagerFragment fragment = new HomePagerFragment();
            loadRootFragment(R.id.base_container, fragment);
        }
    }
    // -------------------------------------------


    @Override
    public void onResume() {
        super.onResume();
        CommunicationLogic.getInstance().postNotifications(BOBSystemManager.getInstance().getUserToken());
        CommunicationLogic.getInstance().postTodaySchedule(BOBSystemManager.getInstance().getUserToken());

        Log.d("onResume", "HomeContainerFragment onResume");
    }
}
