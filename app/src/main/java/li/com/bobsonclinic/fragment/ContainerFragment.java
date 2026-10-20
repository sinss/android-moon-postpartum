package li.com.bobsonclinic.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import me.yokeyword.fragmentation.SupportFragment;

/**
 * @author Milla
 * @create 2019/6/6
 */
public abstract class ContainerFragment extends SupportFragment {

    // -------------------------------------------
    private View view;

    // -------------------------------------------
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // -------------------------------------------
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = setView(inflater, container);
        } else {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        }
        return view;
    }

    // -------------------------------------------
    public abstract View setView(LayoutInflater inflater, ViewGroup container);

    // -------------------------------------------
}
