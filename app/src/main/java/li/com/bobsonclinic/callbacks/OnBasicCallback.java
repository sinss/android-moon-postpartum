package li.com.bobsonclinic.callbacks;

/**
 * Created by leo.chang on 07/03/2017.
 */

public interface OnBasicCallback {
    void onSuccess();
    void onFailure(Exception e);
}
