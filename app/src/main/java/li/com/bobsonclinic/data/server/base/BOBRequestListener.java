package li.com.bobsonclinic.data.server.base;

/**
 * Created by Zack on 8/9/16.
 */
public interface BOBRequestListener {

    public void onResult(boolean success, Object object);
}
