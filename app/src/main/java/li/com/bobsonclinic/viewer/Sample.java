package li.com.bobsonclinic.viewer;

import java.io.Serializable;

/**
 * Created by lgvalle on 04/09/15.
 */
public class Sample implements Serializable {

    final int imageID;
    private final String name;

    public Sample(int resId, String name) {
        this.imageID = resId;
        this.name = name;
    }

    public int getImageId() {
        return imageID;
    }

    public String getName() {
        return name;
    }
}
