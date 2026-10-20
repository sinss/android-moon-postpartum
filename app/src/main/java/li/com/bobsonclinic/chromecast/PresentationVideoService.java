package li.com.bobsonclinic.chromecast;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.android.gms.cast.CastPresentation;
//import com.google.android.gms.cast.CastRemoteDisplayLocalService;

import org.videolan.libvlc.IVLCVout;
import org.videolan.libvlc.MediaPlayer;

import li.com.bobsonclinic.R;
import li.com.bobsonclinic.logic.VideoLogic;


public class PresentationVideoService {
//  private DetailPresentation castPresentation;
//
//  private String videopath;
//
//  private static TextureView videoView;

//  @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
//  @Override
//  public void onCreatePresentation(Display display) {
//    dismissPresentation();
//    castPresentation = new DetailPresentation(this, display, videopath);
//
//    try {
//      castPresentation.show();
//    } catch (WindowManager.InvalidDisplayException ex) {
//      dismissPresentation();
//    }
//  }

//  @Override
//  public void onDismissPresentation() {
//    dismissPresentation();
//  }
//
//  private void dismissPresentation() {
//    if (castPresentation != null) {
//      castPresentation.dismiss();
//      castPresentation = null;
//    }
//  }
//
//  public void setVideoOut(String videopath) {
//    this.videopath = videopath;
//    if (castPresentation != null) {
//      castPresentation.updateAdDetail(videopath);
//    }
//  }


//  public void releaseVideo() {
//    if (castPresentation != null) {
//      castPresentation.releaseVideo();
//    }
//  }
//
//
//  public class DetailPresentation {
//
//    //private TextureView videoView;
//    protected ProgressBar mProgressBar;
//    private SurfaceTexture mSurface;
//    private IVLCVout vOut;
//    private Handler mHandler = new Handler();
//    private FrameLayout rootV;
//    private String videopath;
//    private MediaPlayer mMediaPlayer;
//    private boolean isTimeChanged = false;
//    private boolean isPositionChanged = false;
//    private boolean isRelease = false;
//
//    public DetailPresentation(Context context, Display display, String videopath) {
//      super(context, display);
//      this.videopath = videopath;
//      //this.vOut = VideoLogic.getInstance().getVLCVout(videopath);
//
//      mMediaPlayer = VideoLogic.getInstance().createMedia(videopath);
//      this.vOut = mMediaPlayer.getVLCVout();
//      mMediaPlayer.setEventListener(new MediaPlayer.EventListener() {
//        @Override
//        public void onEvent(MediaPlayer.Event event) {
//          if(!isTimeChanged || !isPositionChanged){
//
//            if(event.type == MediaPlayer.Event.TimeChanged){
//              isTimeChanged = true;
//            }else if(event.type == MediaPlayer.Event.PositionChanged){
//              isPositionChanged = true;
//            }
//
//            if(isTimeChanged && isPositionChanged){
//              mProgressBar.setVisibility(View.GONE);
//            }
//          }
//
//          if(event.type == MediaPlayer.Event.EndReached){
//            //Log.e("Zack", "DetailPresentation EndReached");
//          } else if(event.type == MediaPlayer.Event.ESDeleted){
//            //Log.e("Zack", "DetailPresentation ESDeleted");
//          } else if(event.type == MediaPlayer.Event.EncounteredError){
//            //Log.e("Zack", "DetailPresentation EncounteredError");
//          } else if(event.type == MediaPlayer.Event.Paused){
//            //Log.e("Zack", "DetailPresentation Paused");
//          } else if(event.type == MediaPlayer.Event.Playing){
//            //Log.e("Zack", "DetailPresentation Playing");
//          }
//        }
//      });
//
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//      super.onCreate(savedInstanceState);
//      setContentView(R.layout.presentation_video_detail);
//
////      if(videoView == null){
////        videoView = new TextureView(getContext());
////      }
//      rootV = (FrameLayout)findViewById(R.id.root_layout);
////      rootV.addView(videoView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
//
//      videoView = (TextureView)findViewById(R.id.video_textureV);
//      mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
//
//      if(VideoLogic.getInstance().getMediaPlayer(videopath) == null){
//        findViewById(R.id.stop_txtV).setVisibility(View.VISIBLE);
//        videoView.setVisibility(View.INVISIBLE);
//        return;
//      }
//
//      initVideo();
//    }
//
//    public void updateAdDetail(String videopath) {
//      this.videopath = videopath;
//    }
//
//    @Override
//    protected void onStop() {
//      super.onStop();
//      try{
//        if(!isRelease){
//          mMediaPlayer.release();
//          VideoLogic.getInstance().getMediaPlayer(videopath).play();
//        }
//      }catch (Exception e){
//        e.printStackTrace();
//      }
//    }
//
//    @Override
//    public void onDetachedFromWindow() {
//      super.onDetachedFromWindow();
//    }
//
//    private void initVideo(){
//
//      videoView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
//        @Override
//        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
//          mSurface = surface;
//          mMediaPlayer.play();
//          play();
//        }
//
//        @Override
//        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {}
//
//        @Override
//        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
//          return false;
//        }
//
//        @Override
//        public void onSurfaceTextureUpdated(SurfaceTexture surface) {}
//      });
//    }
//
//    private void play(){
//      try{
//        if(mSurface != null && vOut != null){
//          try{
//            VideoLogic.getInstance().getMediaPlayer(videopath).pause();
//          }catch (Exception e){
//            e.printStackTrace();
//          }
//
//          if(vOut.areViewsAttached()) {
//            vOut.detachViews();
//          }
//          vOut.setVideoSurface(mSurface);
//          vOut.attachViews();
////          videoView.postDelayed(new Runnable() {
////            @Override
////            public void run() {
////              try{
////                VideoLogic.getInstance().getMediaPlayer(videopath).play();
////                mProgressBar.setVisibility(View.GONE);
////              }catch (Exception e){
////                e.printStackTrace();
////              }
////            }
////          }, 2000);
//
//        }
//      }catch (Exception e){
//        e.printStackTrace();
//      }
//    }
//
//    public void releaseVideo(){
//      isRelease = true;
//      try{
//        mMediaPlayer.release();
//        videoView.post(new Runnable() {
//          @Override
//          public void run() {
//            findViewById(R.id.stop_txtV).setVisibility(View.VISIBLE);
//            videoView.setVisibility(View.INVISIBLE);
//          }
//        });
//      }catch (Exception e){
//        e.printStackTrace();
//      }
//    }
//
//  }
}