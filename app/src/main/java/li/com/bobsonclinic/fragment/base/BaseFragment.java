package li.com.bobsonclinic.fragment.base;

import li.com.bobsonclinic.logic.CommunicationLogic;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by Zack on 6/5/16.
 */
public class BaseFragment extends SupportFragment {

    @Override
    public void onDetach() {
        super.onDetach();
        CommunicationLogic.getInstance().removeAllRetryRequest();
    }
}
