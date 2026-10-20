package li.com.bobsonclinic.callbacks;

import androidx.annotation.Nullable;

/**
 * Created by leo.chang on 07/03/2017.
 */

public interface OnSingleFetchCallback<T> {
    void onSuccess(T item);
    void onFailure(@Nullable Exception exception);
}
