package li.com.bobsonclinic.logic;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import li.com.bobsonclinic.BOBApplication;
import li.com.bobsonclinic.data.BOBGalleryItem;

/**
 * Created by Zack on 6/19/16.
 */
public class BOBFolderLogic {

    public interface BOBFolderFetchCallback {
        void OnFinish(List<BOBGalleryItem> fetchResults);
        void OnFail();
    }

    private static BOBFolderLogic mMgr = new BOBFolderLogic();
    public static synchronized BOBFolderLogic getInstance(){
        return mMgr;
    }

    private final String mAppCachePath;
    private final String projectPath = "BOB/";
    private final String videoPath = "VIDEO/";
    private final String photoPath = "PHOTO/";
    private final String rtspPath = "RTSP/";

    private final String type_video = "mp4";
    private final String type_photo = "png";

    private BOBFolderLogic(){
        Context context = BOBApplication.getAppContext();
        mAppCachePath = context.getExternalCacheDir().getParentFile().getAbsolutePath() + "/";
    }

    public void createDir(String dir){
        File dirf = new File(dir);
        if(!dirf.exists()) {
            dirf.mkdirs();
        }
    }

    public final String getBabyPhotoFolder(String momId, String babyId){
        String path = mAppCachePath + projectPath + momId + "/" + babyId + "/" + photoPath;
        createDir(path);
        return path;
    }

    public String getBabyVideoFolder(String momId, String babyId){
        String path = mAppCachePath + projectPath + momId + "/" + babyId + "/" + videoPath;
        createDir(path);
        return path;
    }

    public String getProjectPath() {
        return mAppCachePath + projectPath;
    }

    public String getRtspTempFolder(){
        String path = mAppCachePath + projectPath + rtspPath;
        createDir(path);
        return path;
    }

    public void fetchPhotosWithCurrentBaby(String momId, String babyId, BOBFolderFetchCallback callback) {
        String path = getBabyPhotoFolder(momId, babyId);
        fetchFilesInDirectory(path, callback);
    }

    public void fetchVideosWithCurrentBaby(String momId, String babyId, BOBFolderFetchCallback callback) {
        String path = getBabyVideoFolder(momId, babyId);
        fetchFilesInDirectory(path, callback);
    }

    private void fetchFilesInDirectory(final String directory, final BOBFolderFetchCallback callback) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<BOBGalleryItem> items = new ArrayList<BOBGalleryItem>();
                File f = new File(directory);
                File[] files = f.listFiles();
                Log.d("Files", "Size: "+ files.length);
                for (File file : files)
                {
                    String fileName = file.getName();
                    String ext = fileName.substring(fileName.lastIndexOf("."));
                    String url = file.getPath();
                    BOBGalleryItem.BOBGalleryType type = BOBGalleryItem.BOBGalleryType.Photo;
                    if (ext.equals(type_video)) {
                        type = BOBGalleryItem.BOBGalleryType.Video;
                    }

                    BOBGalleryItem item = new BOBGalleryItem(type, url);

                    items.add(item);

                    Log.d("Files", "FileName:" + file.getName());
                }
                callback.OnFinish(items);
            }
        });
        thread.start();
    }
}
