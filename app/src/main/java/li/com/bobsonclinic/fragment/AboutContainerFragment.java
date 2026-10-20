package li.com.bobsonclinic.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;

import li.com.bobsonclinic.R;


public class AboutContainerFragment extends BaseContainerFragment {

    // -------------------------------------------
    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        if (findChildFragment(AboutPagerFragment.class) == null) {
            AboutPagerFragment fragment = new AboutPagerFragment();
            loadRootFragment(R.id.base_container, fragment);
        }
    }
    // -------------------------------------------
}
