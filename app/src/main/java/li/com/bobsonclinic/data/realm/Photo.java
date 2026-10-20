package li.com.bobsonclinic.data.realm;

import androidx.annotation.Nullable;
import androidx.core.util.LogWriter;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.annotations.PrimaryKey;
import li.com.bobsonclinic.logic.BOBFolderLogic;
import li.com.bobsonclinic.logic.VideoLogic;
import li.com.bobsonclinic.manager.BOBDataManager;
import li.com.bobsonclinic.manager.RealmManager;
import li.com.bobsonclinic.util.DateHelper;

/**
 * Created by leo.chang on 21/08/2017.
 */

public class Photo extends RealmObject {
    public static final String PHOTO_TYPE = "photo";
    public static final String VIDEO_TYPE = "video";
    public static final String PHOTO_FOLDER = "PHOTO";
    public static final String VIDEO_FOLDER = "VIDEO";
    public static final String PHOTO_EXTENSION = "png";

    @PrimaryKey
    String id;
    String fileName;
    String originalFilePath;
    boolean isUploaded = false;
    String type;
    Date createdAt;
    String userId;
    String babyId;

    public String getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public String getOriginalFilePath() {
        return originalFilePath;
    }

    public String getPhotoPath() {
        String path = BOBFolderLogic.getInstance().getProjectPath();
        return String.format("file://%s/%s/%s/%s/%s", path, userId, babyId, PHOTO_FOLDER, fileName);
    }

    public String getUploadPath() {
        String path = BOBFolderLogic.getInstance().getProjectPath();
        return String.format("%s/%s/%s/%s/%s", path, userId, babyId, PHOTO_FOLDER, fileName);
    }


    public boolean isUploaded() {
        return isUploaded;
    }

    public String getBabyId() {
        return babyId;
    }

    public void setUploaded(final boolean value) {
        Runnable executor = new Runnable() {
            @Override
            public void run() {
                isUploaded = value;
            }
        };
        RealmManager.shared().realmTransaction(executor);
    }


    public String getType() {
        return type;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getUserId() {
        return userId;
    }

    public Photo() {
        super();
    }

    public Photo(String id, String fileName, boolean isUploaded, String type, Date createdAt, String userId, String babyId) {
        this();
        this.id = id;
        this.fileName = fileName;
        this.originalFilePath = fileName;
        this.isUploaded = isUploaded;
        this.type = type;
        this.createdAt = createdAt;
        this.userId = userId;
        this.babyId = babyId;
    }

    public static Photo build(String id, String fileName, boolean isUploaded, String type, Date createdAt, String userId, String babyId) {
        return new Photo(id, fileName, isUploaded, type, createdAt, userId, babyId);
    }

    public void save() {
        RealmManager.shared().save(this, true, null);
    }

    public static void saveAll(List<Photo> photos) {
        RealmManager.shared().save(photos, true, null);
    }

    public void delete() {
        RealmManager.shared().delete(this, Photo.class, null);
    }

    public static void deleteBy(String id) {
        Photo photo = fetchBy(id);
        photo.delete();
    }

    public static @Nullable Photo fetchBy(String id) {
        Realm db = RealmManager.shared().getRealm();
        return db.where(Photo.class).equalTo("id", id).findFirst();
    }

    public static void deleteUselessPhotos() {
        Date date = DateHelper.shared().addDays(new Date(), -60);
        Realm db = RealmManager.shared().getRealm();
        final RealmResults<Photo> result = db.where(Photo.class).lessThan("createdAt", date).findAll();
        db.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                result.deleteAllFromRealm();
            }
        });
    }

    public static List<Photo> fetchNotUploadedPhotos() {
        String userId = BOBDataManager.getInstance().getMomeyData().getId();
        Realm db = RealmManager.shared().getRealm();
        RealmResults<Photo> result = db.where(Photo.class).equalTo("isUploaded", false).equalTo("userId", userId).findAll();
        return new ArrayList<>(result);
    }

    public static List<Photo> fetchUselessPhotos() {
        Date date = DateHelper.shared().addDays(new Date(), -60);
        Realm db = RealmManager.shared().getRealm();
        RealmResults<Photo> result = db.where(Photo.class).lessThan("createdAt", date).findAll();
        return new ArrayList<>(result);
    }

    public static List<Photo> fetchAllPhotos() {
        String userId = BOBDataManager.getInstance().getMomeyData().getId();
        Realm db = RealmManager.shared().getRealm();
        RealmResults<Photo> result = db.where(Photo.class).equalTo("type", PHOTO_TYPE).equalTo("userId", userId).findAll().sort("createdAt", Sort.DESCENDING);
        return new ArrayList<>(result);
    }

    public static void markAsUploaded(String id) {
        Photo photo = fetchBy(id);
        if (photo == null) {
            return;
        }
        photo.setUploaded(true);
    }

    public static void importAllSavedPhotos() {
        List<Photo> photos = new ArrayList<>();
        String rootPath = BOBFolderLogic.getInstance().getProjectPath();
        File root = new File(rootPath);

        if(root == null)
            return;

        try {
            for (File momFolder : root.listFiles()) {
                if (!momFolder.isDirectory()) {
                    continue;
                }
                String userId = momFolder.getName();
                for (File babyFolder: momFolder.listFiles()) {
                    if (!babyFolder.isDirectory()) {
                        continue;
                    }
                    String babyId = babyFolder.getName();
                    for (File folder : babyFolder.listFiles()) {
                        if (!folder.isDirectory()) {
                            continue;
                        }
                        if (folder.getName().equals(PHOTO_FOLDER)) {
                            for (File photo : folder.listFiles()) {
                                String fileName = photo.getName();
                                String ext = MimeTypeMap.getFileExtensionFromUrl(fileName);
                                if (ext.equals(PHOTO_EXTENSION)) {
                                    //正確的圖片格式
                                    String suffix = "." + PHOTO_EXTENSION;
                                    long timeStamp = Long.valueOf(fileName.replace(suffix, ""));
                                    String id = String.valueOf(timeStamp);
                                    Date date = new Date(timeStamp);
                                    //Create Photo item to database
                                    Photo exist = Photo.fetchBy(id);
                                    if (exist == null) {
                                        Photo item = Photo.build(id, fileName, true, PHOTO_TYPE, date, userId, babyId);
                                        photos.add(item);
                                    }
                                }
                            }
                        } else if (folder.getName().equals(VIDEO_FOLDER)) {
                            for (File video : folder.listFiles()) {
                                Log.w("VIDEO", video.getAbsolutePath());
                            }
                        }
                    }
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        Photo.saveAll(photos);
    }

    public static void clearUselessPhotos() {
        List<Photo> photos = Photo.fetchUselessPhotos();
        for (Photo photo : photos) {
            String path = photo.getUploadPath();
            File file = new File(path);
            if (file != null && file.exists()) {
                if (file.delete()) {
                    Log.e("DELETE", "delete file success");
                }
            }
        }
        deleteUselessPhotos();
    }

    public static void handleNotUploadedPhotos() {
        List<Photo> photos = Photo.fetchNotUploadedPhotos();
        for (Photo photo : photos) {
            String path = photo.getUploadPath();
            VideoLogic.getInstance().uploadPhoto(photo.getBabyId(), path, photo.getId());
        }
    }
}
