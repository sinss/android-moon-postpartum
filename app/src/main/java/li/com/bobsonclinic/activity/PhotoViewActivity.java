package li.com.bobsonclinic.activity;

import android.app.Activity;
import android.os.Bundle;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import li.com.bobsonclinic.R;

public class PhotoViewActivity extends Activity {

    public static final String TAG_PHOTO_PATH = "TAG_PHOTO_PATH";
    SubsamplingScaleImageView imageView;

    String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);
        if (getIntent().getExtras() != null) {
            imagePath = getIntent().getExtras().getString(TAG_PHOTO_PATH);
        }
        setupUI();
    }

    private void setupUI() {
        imageView = (SubsamplingScaleImageView)findViewById(R.id.imageView);
        try {
            imageView.setImage(ImageSource.uri(imagePath));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
