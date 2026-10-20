package li.com.bobsonclinic.viewer;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * 用來演示如何在RecyclerView裏面添加setEmptyView
 */
public class RecyclerViewEmptySupport extends RecyclerView {
    private static final String TAG = "RecyclerViewEmptySupport";
    /**
     * 當數據爲空時展示的View
     */
    private View mEmptyView;
    /**
     * 創建一個觀察者
     * *爲什麼要在onChanged裏面寫？
     * * 因爲每次notifyDataChanged的時候，系統都會調用這個觀察者的onChange函數
     * * 我們大可以在這個觀察者這裏判斷我們的邏輯，就是顯示隱藏
     */
    private AdapterDataObserver emptyObserver = new AdapterDataObserver() {
        @SuppressLint("LongLogTag")
        @Override
        public void onChanged() {
            //Log.i(TAG, "onChanged: 000");
            Adapter<?> adapter = getAdapter(); //這種寫發跟之前我們之前看到的ListView的是一樣的，判斷數據爲空否，再進行顯示或者隱藏
            if (adapter != null && mEmptyView != null) {
                if (adapter.getItemCount() == 0) {
                    mEmptyView.setVisibility(View.VISIBLE);
                    RecyclerViewEmptySupport.this.setVisibility(View.GONE);
                } else {
                    mEmptyView.setVisibility(View.GONE);
                    RecyclerViewEmptySupport.this.setVisibility(View.VISIBLE);
                }
            }
        }
    };

    public RecyclerViewEmptySupport(Context context) {
        super(context);
    }

    public RecyclerViewEmptySupport(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerViewEmptySupport(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * * @param emptyView 展示的空view
     */
    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
    }

    @SuppressLint("LongLogTag")
    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        super.setAdapter(adapter);
        //Log.i(TAG, "setAdapter: adapter::" + adapter);
        if (adapter != null) {
            //這裏用了觀察者模式，同時把這個觀察者添加進去，
            // 至於這個模式怎麼用，谷歌一下，不多講了，因爲這個涉及到了Adapter的一些原理，感興趣可以點進去看看源碼，還是受益匪淺的
            adapter.registerAdapterDataObserver(emptyObserver);
        }
        //當setAdapter的時候也調一次（實際上，經我粗略驗證，不添加貌似也可以。不行就給添上唄，多大事嘛）
        emptyObserver.onChanged();
    }
}