package li.com.bobsonclinic.data;

/**
 * Created by leo on 6/23/16.
 */

public class BOBGalleryItem {
    public enum BOBGalleryType {
        Photo, Video;
    }
    private BOBGalleryType type;
    private String url;

    public BOBGalleryItem(BOBGalleryType type, String url) {
        super();
        this.type = type;
        this.url = url;
    }
}
