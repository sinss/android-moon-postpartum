package li.com.bobsonclinic.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;

import li.com.bobsonclinic.R;


public class MyContainerFragment extends BaseContainerFragment {
    MyPagerFragment fragment;
    // -------------------------------------------
    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        if (findChildFragment(MyPagerFragment.class) == null) {
            fragment = new MyPagerFragment();
            loadRootFragment(R.id.base_container, fragment);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(fragment != null)
            fragment.onResume();
    }

    // -------------------------------------------
}
