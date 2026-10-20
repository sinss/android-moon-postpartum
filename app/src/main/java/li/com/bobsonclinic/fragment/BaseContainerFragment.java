package li.com.bobsonclinic.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import li.com.bobsonclinic.R;


public abstract class BaseContainerFragment extends ContainerFragment {
    private ViewGroup rootView;

    // -------------------------------------------
    @Override
    public boolean onBackPressedSupport() {
        if (getChildFragmentManager().getBackStackEntryCount() > 1) {
            popChild();
        }
        return true;
    }

    // -------------------------------------------
    @Override
    public View setView(LayoutInflater inflater, ViewGroup container) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_base_container, null);
        return rootView;
    }

    // -------------------------------------------
}
