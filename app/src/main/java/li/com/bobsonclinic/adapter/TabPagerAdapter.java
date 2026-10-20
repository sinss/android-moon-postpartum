package li.com.bobsonclinic.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import android.view.ViewGroup;


import java.util.List;

import li.com.bobsonclinic.viewer.TabPage;

public class TabPagerAdapter extends FragmentPagerAdapter {

    // -------------------------------------------
    private List<TabPage> tabPageBeans;
    private FragmentManager fm;

    // -------------------------------------------
    public TabPagerAdapter(FragmentManager fm, List<TabPage> tabPageBeans) {
        super(fm);
        this.fm = fm;
        this.tabPageBeans = tabPageBeans;
    }

    // -------------------------------------------
    @Override
    public Fragment getItem(int position) {
        return tabPageBeans.get(position).getFragment();
    }

    // -------------------------------------------
    @Override
    public int getCount() {
        return tabPageBeans.size();
    }

    // -------------------------------------------
    @Override
    public CharSequence getPageTitle(int position) {
        return tabPageBeans.get(position).getName();
    }

    // -------------------------------------------
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        this.fm.beginTransaction().show(fragment).commit();
        return fragment;
    }

    // -------------------------------------------
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        Fragment fragment = tabPageBeans.get(position).getFragment();
        fm.beginTransaction().hide(fragment).commit();
    }

    // -------------------------------------------
    public void addTabData(List<TabPage> tabPages) {
        this.tabPageBeans = tabPages;
        notifyDataSetChanged();
    }

    // -------------------------------------------
}
