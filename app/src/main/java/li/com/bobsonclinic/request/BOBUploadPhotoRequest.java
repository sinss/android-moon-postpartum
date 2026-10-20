package li.com.bobsonclinic.request;

import android.util.Log;

import org.apache.http.NameValuePair;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

import li.com.bobsonclinic.manager.BOBDataManager;
import li.com.bobsonclinic.request.base.BOBRequest;
import zk.android.networking.RequestError;

/**
 * Created by Zack on 7/23/16.
 */
public class BOBUploadPhotoRequest extends BOBRequest {

    private String photoId;
    public String getPhotoId() {
        return photoId;
    }

    public final String RequestName = BOBUploadPhotoRequest.class.getSimpleName();

    public BOBUploadPhotoRequest(long id, String url, String token, String baby_id, String path, String photoId) {
        super(id, url);
        this.photoId = photoId;
        setRequestMethod(RequestType.POST);

        addEntity("token", token);
        addEntity("baby_id", baby_id);
        addEntity("media_type", "photo");

        if (path != null){
            File f = new File(path);
            if(f.exists()){
                addFileEntity("filefield", path);
            }
        }

        //Log.d("BOBRequest", RequestName + " baby_id = " + baby_id+", id="+ BOBDataManager.getInstance().getBabyId());
    }

    @Override
    protected String getFileEntityName(NameValuePair fileValue) {
        //Log.e("zack", "filename = "+System.currentTimeMillis()+".png");
        return System.currentTimeMillis()+".png";
    }

    @Override
    protected void onRequestFinished(InputStream inputStream) {
        super.onRequestFinished(inputStream);
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            Log.d("BOBRequest", RequestName + " onRequestFinished.." + response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onRequestFail(RequestError error) throws Exception {
        super.onRequestFail(error);
    }


}
