

package li.com.bobsonclinic.callbacks;

import androidx.annotation.Nullable;

import java.util.List;

/**
 * Created by Leo on 08/03/2017.
 */

public interface OnFetchCallback<E> {
    void onSuccess(List<E> items);
    void onFailure(@Nullable Exception exception);
}
