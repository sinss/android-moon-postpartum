package li.com.bobsonclinic.activity;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;


import me.yokeyword.fragmentation.SupportActivity;

/**
 * @author Milla
 * @create 2019/6/6
 */
public class BaseActivity extends SupportActivity {

    // -------------------------------------------
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // -------------------------------------------
    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    // -------------------------------------------
}
