package li.com.bobsonclinic.fragment;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.videolan.libvlc.IVLCVout;
import org.videolan.libvlc.MediaPlayer;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import li.com.bobsonclinic.BOBApplication;
import li.com.bobsonclinic.R;
import li.com.bobsonclinic.activity.MainActivity;
import li.com.bobsonclinic.adapter.BOBBabysAdapter;
import li.com.bobsonclinic.data.BOBBabies;
import li.com.bobsonclinic.data.BOBBabyCamera;
import li.com.bobsonclinic.data.realm.Photo;
import li.com.bobsonclinic.data.server.base.BOBRequestListener;
import li.com.bobsonclinic.data.server.base.BOBResponse;
import li.com.bobsonclinic.fragment.base.BaseFragment;
import li.com.bobsonclinic.logic.BOBFolderLogic;
import li.com.bobsonclinic.logic.CommunicationLogic;
import li.com.bobsonclinic.logic.VideoLogic;
import li.com.bobsonclinic.manager.BOBDataManager;
import li.com.bobsonclinic.manager.BOBSystemManager;
import li.com.bobsonclinic.provider.BOBProvider;
import li.com.bobsonclinic.util.Config;
import zk.android.networking.Request;
import zk.android.networking.RequestError;

import static androidx.constraintlayout.widget.ConstraintSet.WRAP_CONTENT;
import static li.com.bobsonclinic.data.realm.Photo.PHOTO_EXTENSION;
import static li.com.bobsonclinic.data.realm.Photo.PHOTO_TYPE;

/**
 * Created by Zack on 6/23/16.
 */
public class BOBBabyPlayVideoFragment extends BaseFragment implements View.OnClickListener {



    private final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 1;

    protected ViewGroup rootView;
    protected TextureView mVideoTextureV;
    protected SurfaceTexture mSurface;
    protected String videopath = null;//videopath = "http://storage.googleapis.com/bobson/alison/7-24_zH2tKtaD3IhOvlaB.stream.mp4v";//"rtsp://211.75.138.248:554/user=admin_password=tlJwpbo6_channel=1_stream=0.sdp?real_stream";

    protected ProgressBar mProgressBar;
    protected BOBBabyCamera mCameraData;
    protected ServiceConnection connc;
    protected View mRetry, mToolBarLayout;
    protected View mTakePhoto;
    //protected ImageButton mExtend;

    protected String momId = "";
    protected String babyId = "";
    protected String mBabyVideoFolder;
    protected String mBabyAlbumFolder;
    protected ContentObserver infoObserver, babyObserver;
    protected Handler mHandler;
    MediaPlayer sMediaPlayer = null;
    protected boolean isPlaying = false;
    protected int isOpeningSecond = 0;

    //protected boolean isExtend = false;

    protected Timer keepVideoAliveTimer;

    private Handler handler = new Handler();

    private boolean isTimeChanged = false;
    private boolean isPositionChanged = false;
    private boolean isRetrying = false;
    private TextView babySelect;
    private BOBBabysAdapter babysAdapter;
    private TextView title, videoType;
    private boolean isTesting = false;//測試影片
    private View  babySelectView;
    private BottomSheetDialog mBottomReportDialog;

    private Button reportClose, reportBug;
    private EditText reportEmail;
    private EditText reportRoom;
    private Switch reportFullSwitch;
    private EditText reportContent;
    private ProgressBar reportProgress;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_baby_play_video, null);
        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        findView();
        initHandler();
        infoObserver = new ContentObserver(mHandler) {
            @Override
            public void onChange(boolean selfChange) {
                super.onChange(selfChange);
                loadInfo();
            }
        };

        babyObserver = new ContentObserver(mHandler) {
            @Override
            public void onChange(boolean selfChange) {
                super.onChange(selfChange);
                loadBabyInfo();
            }
        };

    }

    private void initHandler() {
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 0: {
                        setData();
                    }
                    break;
                    case 1: {
                        setBabyPager();
                    }
                    break;
                }
                return true;
            }
        });
    }

    private void loadInfo() {

        Message msg = mHandler.obtainMessage();
        msg.what = 0;
        msg.obj = null;
        mHandler.sendMessageDelayed(msg, 500);
    }

    private void loadBabyInfo() {

        Message msg = mHandler.obtainMessage();
        msg.what = 1;
        msg.obj = null;
        mHandler.sendMessageDelayed(msg, 500);
    }


    public void setData() {
        if (!isAdded()) {
            return;
        }
        mCameraData = BOBDataManager.getInstance().getCamera();
        Log.d("dd", "setData " + mCameraData.getLocal_ip_port());

        init();
    }

    private void findView() {
        WindowManager wm = getActivity().getWindowManager();
        int height = wm.getDefaultDisplay().getHeight();

        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        mVideoTextureV = rootView.findViewById(R.id.video_textureV);
        babySelect = rootView.findViewById(R.id.baby_select);
        babySelectView = rootView.findViewById(R.id.baby_select_layout);
        mRetry = rootView.findViewById(R.id.retry_btn_imgV);
        mTakePhoto = rootView.findViewById(R.id.snapshot_btn_imgV);
        //mExtend = (ImageButton) rootView.findViewById(R.id.extend_btn_imgV);
        title = rootView.findViewById(R.id.title);
        videoType = rootView.findViewById(R.id.video_type);
        mToolBarLayout = rootView.findViewById(R.id.toolbar);
        mBottomReportDialog = new BottomSheetDialog(getActivity());
        mToolBarLayout.setVisibility(View.VISIBLE);

        View reportView = LayoutInflater.from(getActivity()).inflate(R.layout.custom_report, null, false);
        mBottomReportDialog.setContentView(reportView);

        BottomSheetBehavior mBehavior = BottomSheetBehavior.from((View) reportView.getParent());
        //设置点击dialog外部不消失
        mBottomReportDialog.setCanceledOnTouchOutside(false);
        reportClose = reportView.findViewById(R.id.report_close);
        reportRoom = reportView.findViewById(R.id.room_number);
        reportEmail = reportView.findViewById(R.id.email_address);
        reportContent = reportView.findViewById(R.id.report_content);
        reportFullSwitch = reportView.findViewById(R.id.full_switch);
        reportBug = reportView.findViewById(R.id.report_button);
        reportProgress = reportView.findViewById(R.id.report_progress);
        setReporFieldtListener();
        mTakePhoto.setOnClickListener(this);
        //mExtend.setOnClickListener(this);
        mRetry.setOnClickListener(this);
        reportBug.setOnClickListener(this);
        reportClose.setOnClickListener(this);
    }

    private void setReporFieldtListener() {
        reportBug.setEnabled(false);
        reportRoom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkReportField();
            }
        });

        reportEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkReportField();
            }
        });

        reportContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkReportField();
            }
        });
    }

    public boolean checkReportField() {
        if (reportRoom.getText() == null || reportRoom.getText().toString().isEmpty()) {
            reportBug.setEnabled(false);
            return false;
        }
        if (reportEmail.getText() == null || reportEmail.getText().toString().isEmpty()) {
            reportBug.setEnabled(false);
            return false;
        }
        if (reportContent.getText() == null || reportContent.getText().toString().isEmpty()) {
            reportBug.setEnabled(false);
            return false;
        }
        reportBug.setEnabled(true);
        return true;
    }



    protected void init() {
        Log.d("onResume", "Video init " + ((MainActivity) getActivity()).getCurrentTab());
        //mVideoTextureV.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);

        if (((MainActivity) getActivity()).getCurrentTab() != MainActivity.TAB_MEDIA) {
            return;
        }
        if (babysAdapter == null) {
            babysAdapter = new BOBBabysAdapter(getActivity());
            babysAdapter.setListener(babyListener);
        }
        momId = BOBDataManager.getInstance().getMomeyData().getId();
        babyId = BOBDataManager.getInstance().getBabyId();
        babySelectView.setOnClickListener(babySelectClick);
        mBabyVideoFolder = BOBFolderLogic.getInstance().getBabyVideoFolder(momId, babyId);
        mBabyAlbumFolder = BOBFolderLogic.getInstance().getBabyPhotoFolder(momId, babyId);
        releaseMedia();
        if (isTesting) {
            initPlay();
        } else {
            if (mCameraData == null || mCameraData.getLocal_ip_port().isEmpty()) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mVideoTextureV.setVisibility(View.INVISIBLE);
                        mProgressBar.setVisibility(View.GONE);
                        videoType.setText(getString(R.string.video_fail));
                        //Toast.makeText(getActivity(), getString(R.string.video_fail), Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                initPlay();
            }
        }
    }

    protected void initPlay() {
        countTimer();
        if (BOBDataManager.getInstance().getSelectBabyName() != null) {
            babySelect.setText(BOBDataManager.getInstance().getSelectBabyName());
        }
        String path = getVideoPath();
        if (videopath == null || (path != null && !videopath.equals(path))) {
            videopath = getVideoPath();
            Log.d("setBabyPager", "videopath " + videopath + "，mSurface: " + mSurface);
            if (videopath != null && !videopath.isEmpty()) {
                VideoLogic.getInstance().createMedia(getContext(), videopath);
                if (mSurface != null)
                    play(mSurface);
            } else {
                getCameraDataFail();
            }
        }

        if (BOBDataManager.getInstance().getSelectBabyName() != null) {
            babySelect.setText(BOBDataManager.getInstance().getSelectBabyName());
        }
    }

    protected String getVideoPath() {
        if (isTesting) {
            return "http://storage.googleapis.com/bobson/alison/7-24_zH2tKtaD3IhOvlaB.stream.mp4";
        }
        if (mCameraData == null) {
            return null;
        }
        String videopath = mCameraData.getLocal_ip_port();
        return videopath;
    }

    protected synchronized void startKeepVideoAlive() {
        if (keepVideoAliveTimer != null) {
            return;
        }

        keepVideoAliveTimer = new Timer();
        keepVideoAliveTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                sendKeepVideoAlive();
            }
        }, 0, 60 * 1000L);
    }

    private boolean isUpdating = false;

    protected synchronized void sendKeepVideoAlive() {
        if (mCameraData == null || isUpdating) {
            return;
        }

        isUpdating = true;

        CommunicationLogic.getInstance().postUpdateStream(
                BOBSystemManager.getInstance().getUserToken(), mCameraData.getId(),
                new BOBRequestListener() {
                    @Override
                    public void onResult(boolean success, Object object) {
                        if (success) {
                            if (object instanceof BOBResponse) {
                                String status = ((BOBResponse) object).getStatus();
                                if ("700".equals(status)) {
                                    //The Video Stream Can't Be Played now.
                                    releaseVideo();
                                } else if ("701".equals(status)) {
                                    //The Video Stream Is Playing
                                }
                            }
                        }
                        isUpdating = false;
                    }
                });
    }

    protected void retryVideo() {
        Log.d("retryVideo", "retryVideo....");
        isPlaying = false;
        isTimeChanged = false;
        isPositionChanged = false;
        mProgressBar.setVisibility(View.VISIBLE);
        VideoLogic.getInstance().release(videopath);
        videopath = null;
        initPlay();
    }

    private synchronized void releaseVideo() {

        if (VideoLogic.getInstance().getMediaPlayer(videopath) == null) {
            return;
        }
        isPlaying = false;
        if (keepVideoAliveTimer != null) {
            keepVideoAliveTimer.cancel();
            keepVideoAliveTimer = null;
        }
        VideoLogic.getInstance().release(videopath);
        handler.post(new Runnable() {
            @Override
            public void run() {
                mVideoTextureV.setVisibility(View.INVISIBLE);
                //Toast.makeText(getActivity(), "影像已經停止傳送!", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void setInfo(BOBBabyCamera cameraData) {
        mCameraData = cameraData;
    }

    public void getCameraDataFail() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                videoType.setText("");
                mVideoTextureV.setVisibility(View.INVISIBLE);
                isRetrying = false;
                showErrorMsg(getString(R.string.error_baby_no_setting_camera));
            }
        });
    }

    public void setBabyPager() {
        if (!isAdded())
            return;
        countTimer();
        final List<BOBBabies> list = BOBDataManager.getInstance().getBabyLst();
        Log.d("setBabyPager", "setBabyPager list " + list.size());
        if (list != null) {
            if (list.size() > 0) {
                if (babysAdapter == null) {
                    babysAdapter = new BOBBabysAdapter(getActivity());
                    babysAdapter.setListener(babyListener);
                }
                babysAdapter.setList(list);
                Log.d("setBabyPager", "setBabyPager babyID " + list.get(0).getId());
                BOBDataManager.getInstance().setBabyId(list.get(0).getId());
                BOBDataManager.getInstance().setSelectBabyName(list.get(0).getName());
                if (title != null)
                    title.setText(list.get(0).getName());
                if (babySelect != null) {
                    babySelect.setText(list.get(0).getName());
                }


                ((MainActivity) getActivity()).postBabyCarmeraData();
            } else {
                mProgressBar.setVisibility(View.VISIBLE);
                showErrorMsg(getString(R.string.error_baby_no_setting));
            }
            Log.d("setBabyPager", "setBabyPager " + BOBDataManager.getInstance().getBabyId());
        } else {
            mProgressBar.setVisibility(View.VISIBLE);
            showErrorMsg(getString(R.string.error_baby_no_setting));
        }
    }

    android.app.AlertDialog dialog;
    private void showErrorMsg(String msg){
        dialog = new android.app.AlertDialog.Builder(getContext())
                .setTitle(getString(R.string.warring))
                .setMessage(msg)
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setNegativeButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();
        dialog.show();
    }

    CountDownTimer countDownTimer;
    private void countTimer(){
        if(countDownTimer != null){
            return;
        }
        countDownTimer = new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                if (!isPlaying) {
                    isOpeningSecond++;
                    if (isOpeningSecond == 20) {

                        showErrorMsg(getString(R.string.error_no_wify));
                    }
                }
            }

            public void onFinish() {
                if (!isPlaying && isOpeningSecond == 30) {
                    if (mBottomReportDialog != null)
                        mBottomReportDialog.show();
                }
                countDownTimer = null;
            }
        }.start();
    }

    public void releaseMedia() {
        Log.d("releaseMedia", "releaseMedia " + sMediaPlayer);

/*
        Canvas canvas = mVideoTextureV.lockCanvas();
        // Clear surface by drawing on it
        canvas.drawColor(Color.BLACK, PorterDuff.Mode.CLEAR);
        mVideoTextureV.unlockCanvasAndPost(canvas);
        */
        //closeVideo();
        if (sMediaPlayer != null) {
            sMediaPlayer.release();
            sMediaPlayer = null;

        }
        videopath = null;
        isPlaying = false;
        isTimeChanged = false;
        isPositionChanged = false;

        if (!isTesting && mVideoTextureV != null) {
            mVideoTextureV.setVisibility(View.INVISIBLE);
        }

    }


    @Override
    public void onClick(View v) {

        if (v == mTakePhoto) {
            if (isPlaying) {
                takeSnapShot();
            } else {
                Toast.makeText(getContext(), "影像尚未連線", Toast.LENGTH_LONG).show();
            }
        } else if (v == reportBug) {
            reportBug.setEnabled(false);
            reportProgress.setVisibility(View.VISIBLE);
            CommunicationLogic.getInstance().postBugReport(reportRoom.getText().toString(),
                    reportEmail.getText().toString(), reportFullSwitch.isChecked(), reportContent.getText().toString(), new BOBRequestListener() {
                        @Override
                        public void onResult(final boolean success,final Object object) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    reportBug.setEnabled(true);
                                    reportProgress.setVisibility(View.GONE);
                                }
                            });
                            if (success) {
                                if (object instanceof BOBResponse) {
                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getContext(), ((BOBResponse) object).getMessage(), Toast.LENGTH_LONG).show();
                                            mBottomReportDialog.dismiss();
                                        }
                                    });
                                }
                            }

                        }
                    });
        } else if (v == reportClose) {
            mBottomReportDialog.dismiss();

        } else if (v == mRetry) {
            Log.d("mRetry", "mRetry 1");
            if (isRetrying) {
                return;
            }

            isRetrying = true;
            releaseVideo();
            countTimer();
            if (BOBSystemManager.getInstance().checkNetWorkState(getActivity())) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBar.setVisibility(View.VISIBLE);
                    }
                });
                CommunicationLogic.getInstance().postCameraData(BOBSystemManager.getInstance().getUserToken(),
                        BOBDataManager.getInstance().getBabyId(), new Request.RequestListener() {
                            @Override
                            public void onStart(Request request) {
                                Log.d("mRetry", "mRetry 2");
                            }

                            @Override
                            public void onEnd(Request request) {
                                isRetrying = false;
                            }

                            @Override
                            public void onFail(Request request, RequestError requestError) {
                                getCameraDataFail();
                            }

                            @Override
                            public void onSuccess(Request request, Object o) {
                                mCameraData = BOBDataManager.getInstance().getCamera();
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        retryVideo();
                                    }
                                });
                            }
                        });
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == VideoPlayerActivity.CLOSE_EXTEND_REQUEST &&
//                resultCode == VideoPlayerActivity.CLOSE_EXTEND_RESULT){
//            reAttachView();
//            isExtend = false;
//        }
    }



    protected void initVideo() {
        Log.d("initVideo", "mVideoTextureV ....　" + mVideoTextureV);
        mVideoTextureV.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                Log.d("initVideo", "mVideoTextureV onSurfaceTextureAvailable　" + width + ", " + height);
                mSurface = surface;
                play(mSurface);
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {
//                Log.d("initVideo", "mVideoTextureV onSurfaceTextureUpdated　"+surface);
//                if(mSurface != surface) {
//                    mSurface = surface;
//                    play(mSurface);
//                }
            }
        });
    }


    private void play(SurfaceTexture surfaceTexture) {
        Log.d("initVideo", "mVideoTextureV play " + videopath + "，" + surfaceTexture);
        if (videopath != null && !videopath.isEmpty()) {
            try {
                IVLCVout vOut = VideoLogic.getInstance().getVLCVout(videopath);
                if (vOut != null) {

                    DisplayMetrics displayMetrics = new DisplayMetrics();
                    getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

                    ViewGroup.LayoutParams videoParams = mVideoTextureV.getLayoutParams();
                    videoParams.width = displayMetrics.widthPixels;
                    videoParams.height = displayMetrics.heightPixels;

                    // Set up video output
                    vOut.setVideoView(mVideoTextureV);
                    if(((MainActivity)getActivity()).isPortrait())
                        vOut.setWindowSize(videoParams.width,videoParams.height);
                    else
                        vOut.setWindowSize(videoParams.height,videoParams.width);

                    Log.d("isPortrait", "isPortrait "+((MainActivity)getActivity()).isPortrait()+ ", "+videoParams.width);


                    if (vOut.areViewsAttached()) {
                        vOut.detachViews();
                    }
                    //vOut.setVideoSurface(surfaceTexture);
                    vOut.attachViews();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            sMediaPlayer = VideoLogic.getInstance().getMediaPlayer(videopath);
            //Log.d("initVideo", "mVideoTextureV sMediaPlayer " + sMediaPlayer);
            if (sMediaPlayer != null && !sMediaPlayer.isPlaying()) {
                sMediaPlayer.setEventListener(new MediaPlayer.EventListener() {
                    @Override
                    public void onEvent(MediaPlayer.Event event) {
                        Log.e("zack", "MediaPlayer type=" + event.type);
                        setVideoTypeText(event.type);

                        if (!isTimeChanged || !isPositionChanged) {

                            if (event.type == MediaPlayer.Event.TimeChanged) {
                                isTimeChanged = true;

                            } else if (event.type == MediaPlayer.Event.PositionChanged) {
                                isPositionChanged = true;
                            }
                        }

                        if (isTimeChanged && isPositionChanged) {
                            mProgressBar.setVisibility(View.GONE);
                            mVideoTextureV.setVisibility(View.VISIBLE);
                            isPlaying = true;
                            isRetrying = false;
                            startKeepVideoAlive();
                        }

                        if (event.type == MediaPlayer.Event.EndReached) {
                            //can't receive
                            //releaseVideo();
                            retryVideo();
                            //Log.e("Zack", "EndReached");
                        } else if (event.type == MediaPlayer.Event.ESDeleted) {
                            //Log.e("Zack", "ESDeleted");
                        } else if (event.type == MediaPlayer.Event.EncounteredError) {
                            //Log.e("Zack", "EncounteredError");
                        } else if (event.type == MediaPlayer.Event.Paused || event.type == MediaPlayer.Event.Stopped) {
                            Log.e("Zack", "MediaPlayer Paused");
                            isPlaying = false;
                            isTimeChanged = false;
                            isPositionChanged = false;
                            //retryVideo();
                        } else if (event.type == MediaPlayer.Event.Playing) {
                            Log.e("Zack", "MediaPlayer Playing");
                        } else if (event.type == MediaPlayer.Event.Opening) {
                            Log.e("Zack", "MediaPlayer Opening");
                        }

                    }
                });
                sMediaPlayer.play();
            }
        }
    }

    private void setVideoTypeText(final int type) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (type == MediaPlayer.Event.TimeChanged || type == MediaPlayer.Event.Playing) {
                    mProgressBar.setVisibility(View.GONE);
                    videoType.setText(getString(R.string.playing));
                } else if (type == MediaPlayer.Event.Opening) {
                    mProgressBar.setVisibility(View.VISIBLE);
                    videoType.setText(getString(R.string.connecting));
                } else if (type == MediaPlayer.Event.EncounteredError) {
                    mProgressBar.setVisibility(View.VISIBLE);
                    showErrorMsg(getString(R.string.error_camera_close));
                }

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        initVideo();
        if (isTesting) {
            init();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        VideoLogic.getInstance().release(videopath);
        if (keepVideoAliveTimer != null) {
            keepVideoAliveTimer.cancel();
            keepVideoAliveTimer = null;
        }

        try {
            releaseVideo();
            ((MainActivity) getActivity()).closeChromecast();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private Bitmap captureView(TextureView view) {
        int w = view.getWidth();
        int h = view.getHeight();
        if (w <= 0 || h <= 0) {
            throw new RuntimeException("Impossible to snapshot the view: view is invalid");
        }

        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Bitmap childBitmapBuffer;
        Canvas c = new Canvas(bitmap);
        view.draw(c);
        childBitmapBuffer = view.getBitmap(view.getWidth(), view.getHeight());
        c.drawBitmap(childBitmapBuffer, view.getLeft() + view.getPaddingLeft(), view.getTop() + view.getPaddingTop(), null);

        if (bitmap == null) {
            throw new RuntimeException("Impossible to snapshot the view");
        }
        return bitmap;
    }


    private void takeSnapShot() {
        if (!checkPermission()) {
            return;
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mProgressBar.setVisibility(View.VISIBLE);
            }
        });
        Bitmap textureViewBitmap = mVideoTextureV.getBitmap();
        textureViewBitmap = Bitmap.createBitmap(textureViewBitmap, 0, 0, textureViewBitmap.getWidth(), textureViewBitmap.getHeight(), mVideoTextureV.getTransform(null), true);


        //Bitmap textureViewBitmap = mVideoTextureV.getBitmap(300, 240);
        //final Bitmap bit = Bitmap.createBitmap( textureViewBitmap, 0, 0, textureViewBitmap.getWidth(), textureViewBitmap.getHeight(), mVideoTextureV.getTransform( null ), true );

        Log.d("dd", "takeSnapShot textureViewBitmap " + textureViewBitmap.getWidth());

        File filedir = new File(mBabyAlbumFolder);
        Date createdAt = new Date();
        String photoId = String.valueOf(System.currentTimeMillis() / 1000);
        String fileName = photoId + "." + PHOTO_EXTENSION;
        String path = filedir.getAbsolutePath() + '/' + fileName;


        File imagePath = new File(path);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(imagePath);
            Log.d("dd", "imagePath: " + path);
            textureViewBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            Toast.makeText(getContext(), "影像儲存成功", Toast.LENGTH_SHORT).show();
            //儲存到資料庫
            Photo photo = Photo.build(photoId, fileName, false, PHOTO_TYPE, createdAt, momId, babyId);
            photo.save();
            VideoLogic.getInstance().uploadPhoto(babyId, path, photoId);
            getContext().sendBroadcast(new Intent(Config.BROADCAST_VIDEO));
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mProgressBar.setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {
        } finally {
            try {
                fos.close();
                textureViewBitmap.recycle();
            } catch (Exception e) {
            }
        }
    }

    protected void closeVideo() {
        isPlaying = false;

        //mProgressBar.setVisibility(View.GONE);
        mVideoTextureV.setVisibility(View.INVISIBLE);

        try {
            if (connc != null) {
                getActivity().unbindService(connc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        VideoLogic.getInstance().release(videopath);
    }

    protected static Rect locateView(View v) {
        int[] loc_int = new int[2];
        try {
            v.getLocationOnScreen(loc_int);
        } catch (Exception npe) {
            return null;
        }
        Rect location = new Rect();
        location.left = loc_int[0];
        location.top = loc_int[1];
        location.right = location.left + v.getWidth();
        location.bottom = location.top + v.getHeight();
        return location;
    }

    protected boolean checkPermission() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
            return true;
        }

        boolean albumAgreed = (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);

        if (!albumAgreed) {
            askPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        return albumAgreed;
    }

    protected void askPermissions(String permissionStr) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            ArrayList<String> mPermission = new ArrayList<String>();

            if (ActivityCompat.checkSelfPermission(getActivity(), permissionStr) != PackageManager.PERMISSION_GRANTED) {
                mPermission.add(permissionStr);
            }

            String[] permission = new String[mPermission.size()];
            for (int i = 0; i < mPermission.size(); i++) {
                permission[i] = mPermission.get(i);
            }

            if (permission.length != 0) {
                ActivityCompat.requestPermissions(getActivity(), permission, WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
            }
        }
    }

    PopupWindow popWindow;
    View.OnClickListener babySelectClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d("dds", "babySelectClick");
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

            View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_babys_layout, null, false);
            popWindow = new PopupWindow(contentView,
                    WRAP_CONTENT, WRAP_CONTENT, true);
            popWindow.setAnimationStyle(R.anim.fade_in);  //设置加载动画
            popWindow.setTouchable(true);
            popWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.popwindow_gray_bg));


            RecyclerView recyclerView = contentView.findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(babysAdapter);

            popWindow.setContentView(contentView);
            //设置popupWindow显示的位置，参数依次是参照View，x轴的偏移量，y轴的偏移量
            popWindow.showAsDropDown(v);
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        doNext(requestCode, grantResults);
    }

    private BOBBabysAdapter.bListener babyListener = new BOBBabysAdapter.bListener() {
        @Override
        public void onClick(final BOBBabies babies) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBar.setVisibility(View.VISIBLE);
                        }
                    });

                    BOBDataManager.getInstance().setBabyId(babies.getId());
                    BOBDataManager.getInstance().setSelectBabyName(babies.getName());
                    babySelect.setText(babies.getName());
                    if (popWindow != null)
                        popWindow.dismiss();
                    //重新用babyid抓carmer資料才能重新連線
                    ((MainActivity) getActivity()).postBabyCarmeraData();
                }

            });
        }
    };

    private void doNext(int requestCode, int[] grantResults) {

        boolean albumAgreed = (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!albumAgreed) {
            Toast.makeText(getContext(), "您不同意使用相簿權限,將無法啟動儲存照片功能！", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isAdded())
            return;


        BOBSystemManager.getInstance().checkNetWorkState(getActivity());
        IntentFilter counterActionFilter = new IntentFilter();
        getActivity().registerReceiver(myReceiver, counterActionFilter);
        getActivity().getContentResolver().registerContentObserver(BOBProvider.URI_Babies, true, babyObserver);
        getActivity().getContentResolver().registerContentObserver(BOBProvider.URI_Camera, true, infoObserver);
        if (BOBDataManager.getInstance().getBabyLst() != null && BOBDataManager.getInstance().getBabyLst().size() > 0) {
            setBabyPager();
            return;
        }


        Log.d("onResume", "Video init onResume " + ((MainActivity) getActivity()).getCurrentTab() + ",getCamera " + BOBDataManager.getInstance().getCamera() + ", " + BOBDataManager.getInstance().getBabyLst());

    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(myReceiver);
        try {
            VideoLogic.getInstance().pause(videopath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        getActivity().getContentResolver().unregisterContentObserver(infoObserver);
        getActivity().getContentResolver().unregisterContentObserver(babyObserver);

        if (sMediaPlayer != null) {
            sMediaPlayer.release();
        }
    }

    public BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (connc != null) {
                getActivity().unbindService(connc);
            }

        }
    };

}
