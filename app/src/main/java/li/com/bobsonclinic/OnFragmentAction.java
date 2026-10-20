package li.com.bobsonclinic;

/**
 * Agricola
 * Created by Milla.Wang on 2016/1/5.
 * Copyright © 2016 bobsonclinic. All rights reserved.
 */

import android.app.Activity;
import androidx.fragment.app.Fragment;

public interface OnFragmentAction {
	public void addFragment(Fragment fragment, int... args);
	

	public Activity getActivity();

}
