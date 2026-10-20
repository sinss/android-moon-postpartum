package li.com.bobsonclinic.logic;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;

import org.videolan.libvlc.IVLCVout;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;
import org.videolan.libvlc.util.AndroidUtil;

import java.util.ArrayList;
import java.util.HashMap;

import li.com.bobsonclinic.BOBApplication;
import li.com.bobsonclinic.data.realm.Photo;
import li.com.bobsonclinic.manager.BOBSystemManager;
import li.com.bobsonclinic.request.BOBUploadPhotoRequest;
import li.com.bobsonclinic.request.base.BOBAPI;
import zk.android.networking.Request;
import zk.android.networking.RequestError;
import zk.android.networking.RequestManager;

/**
 * Created by Zack on 7/23/16.
 */
public class VideoLogic {

    private static VideoLogic mLogic = null;

    public static synchronized VideoLogic getInstance() {
        if (mLogic == null) {
            mLogic = new VideoLogic();
        }
        return mLogic;
    }

    private Request.RequestListener reqListener = new Request.RequestListener() {

        @Override
        public void onStart(Request request) {

        }

        @Override
        public void onEnd(Request request) {

        }

        @Override
        public void onFail(Request request, RequestError requestError) {
        }

        @Override
        public void onSuccess(Request request, Object o) {
            if (request instanceof BOBUploadPhotoRequest) {
                Log.w("UPLOAD", "Upload success");
                String photoId = ((BOBUploadPhotoRequest) request).getPhotoId();
                Photo.markAsUploaded(photoId);
            }
        }
    };

    protected static HashMap<String, MediaPlayer> mMediaPlayerList = new HashMap<>();
    protected static LibVLC sLibVLC;
    protected static MediaPlayer mMediaPlayer;

    public interface MediaEventsListener{
        void startPlay();
    }

    private final RequestManager reqMgr;

    private VideoLogic(){
        reqMgr = new RequestManager();
    }


    public MediaPlayer createMedia(Context content, String path){
        ArrayList<String> options = new ArrayList<>();
        options.add("--gain=0");
        options.add(":network-caching=100");
        //options.add(":clock-jitter=0");
       // options.add("--clock-synchro=0");
        //options.add("--vout=android-display");  // Add this line!
        //options.add("--vvv");
        //options.add(":fullscreen");
        LibVLC sLibVLC = new LibVLC(content);
        MediaPlayer sMediaPlayer;
        if(getMediaPlayer(path) != null){
            try{
                getMediaPlayer(path).release();
            }catch (Exception e){
                e.printStackTrace();
            }
            setMediaPlayer(path, null);
        }

        if(getMediaPlayer(path) == null){
            sMediaPlayer = new MediaPlayer(sLibVLC);
            setMediaPlayer(path, sMediaPlayer);
            Media media = new Media(sLibVLC, AndroidUtil.LocationToUri(path));
            //sMediaPlayer.setAspectRatio("4:3");
            //sMediaPlayer.setScale(1.0f);
            sMediaPlayer.setMedia(media);
            Log.d("tets","getMediaPlayer "+path+",media "+media + ", sMediaPlayer: "+sMediaPlayer);
        }else{
            sMediaPlayer = getMediaPlayer(path);
        }
        return sMediaPlayer;
    }

    public MediaPlayer createMedia(String path){
        ArrayList<String> options = new ArrayList<>();
        options.add("--gain=0");
        LibVLC sLibVLC = new LibVLC(BOBApplication.getAppContext());
        MediaPlayer sMediaPlayer = new MediaPlayer(sLibVLC);
        Media media = new Media(sLibVLC, AndroidUtil.LocationToUri(path));
        sMediaPlayer.setMedia(media);
        return sMediaPlayer;
    }

    public void setMediaEventsListener(String path){

    }

    public MediaPlayer getMediaPlayer(String path){
        return mMediaPlayerList.get(path);
    }

    private void setMediaPlayer(String path, MediaPlayer mMediaPlayer){
        mMediaPlayerList.put(path, mMediaPlayer);
    }

    public void pause(String path){
        try{
            if(mMediaPlayerList.containsKey(path)){
                mMediaPlayerList.get(path).pause();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void play(String path){
        try{
            if(mMediaPlayerList.containsKey(path)){
                mMediaPlayerList.get(path).play();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void release(String path){
        try{
            if(mMediaPlayerList.containsKey(path)){
                mMediaPlayerList.remove(path).release();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public LibVLC getLibVLC(){
        return sLibVLC;
    }

    public IVLCVout getVLCVout(String path){
        try{
            IVLCVout Vout = mMediaPlayerList.get(path).getVLCVout();
            return Vout;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void uploadPhoto(String babyId, String photoPath, String photoId){
        long id = Request.generateRequestId(BOBUploadPhotoRequest.class, false);
        String token = BOBSystemManager.getInstance().getUserToken();
        String url =  BOBAPI.postBabyFileURL();
        BOBUploadPhotoRequest request = new BOBUploadPhotoRequest(id, url, token, babyId, photoPath, photoId);
        request.setRequestListener(reqListener);
        reqMgr.addRequest(request);
    }

}
