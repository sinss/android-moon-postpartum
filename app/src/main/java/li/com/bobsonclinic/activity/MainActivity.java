package li.com.bobsonclinic.activity;

import static com.google.gson.internal.$Gson$Types.arrayOf;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.core.view.MenuItemCompat;
import androidx.mediarouter.app.MediaRouteActionProvider;
import androidx.mediarouter.media.MediaRouteSelector;
import androidx.mediarouter.media.MediaRouter;

import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.OrientationEventListener;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.android.gms.cast.CastDevice;
//import com.google.android.gms.cast.CastMediaControlIntent;
//import com.google.android.gms.cast.CastRemoteDisplayLocalService;
import com.google.android.gms.common.api.Status;
import com.squareup.otto.Subscribe;

import org.videolan.libvlc.IVLCVout;

import java.util.List;
import java.util.concurrent.ExecutionException;

import li.com.bobsonclinic.OnFragmentAction;
import li.com.bobsonclinic.R;
import li.com.bobsonclinic.chromecast.PresentationVideoService;
import li.com.bobsonclinic.data.BOBActivity;
import li.com.bobsonclinic.data.BOBBabyExam;
import li.com.bobsonclinic.data.BOBBabySwim;
import li.com.bobsonclinic.data.BOBMomData;
import li.com.bobsonclinic.data.BOBMomExam;
import li.com.bobsonclinic.data.BOBMomTemPulPre;
import li.com.bobsonclinic.data.BOBMonthActivity;
import li.com.bobsonclinic.data.BOBVas;
import li.com.bobsonclinic.data.BOBVasList;
import li.com.bobsonclinic.data.realm.Photo;
import li.com.bobsonclinic.data.server.BOBBabyBreathResponse;
import li.com.bobsonclinic.data.server.BOBBabyJaundiceResponse;
import li.com.bobsonclinic.data.server.BOBBabyMilkResponse;
import li.com.bobsonclinic.data.server.BOBBabyPulseResponse;
import li.com.bobsonclinic.data.server.BOBBabyTemperatureResponse;
import li.com.bobsonclinic.data.server.BOBBabyWeightResponse;
import li.com.bobsonclinic.data.server.BOBLogin;
import li.com.bobsonclinic.data.server.BOBMomBreathResponse;
import li.com.bobsonclinic.data.server.BOBMomPressureResponse;
import li.com.bobsonclinic.data.server.BOBMomPulseResponse;
import li.com.bobsonclinic.data.server.BOBMomTemperatureResponse;
import li.com.bobsonclinic.data.server.BOBMomWeightResponse;
import li.com.bobsonclinic.data.server.base.BOBRequestListener;
import li.com.bobsonclinic.fragment.AboutContainerFragment;
import li.com.bobsonclinic.fragment.BOBAboutUsFragment;
import li.com.bobsonclinic.fragment.BOBAddServiceDetailFragment;
import li.com.bobsonclinic.fragment.BOBAddServiceFragment;
import li.com.bobsonclinic.fragment.BOBBabyExamFragment;
import li.com.bobsonclinic.fragment.BOBBabyPlayVideoFragment;
import li.com.bobsonclinic.fragment.BOBBabyVideoFragment;
import li.com.bobsonclinic.fragment.BOBConsumerFragment;
import li.com.bobsonclinic.fragment.BOBEduInfoFragment;
import li.com.bobsonclinic.fragment.BOBHomePageFragment;
import li.com.bobsonclinic.fragment.BOBLoginFragment;
import li.com.bobsonclinic.fragment.BOBMomDialyFragment;
import li.com.bobsonclinic.fragment.BOBMomExamFragment;
import li.com.bobsonclinic.fragment.BOBMomTemPulPreFragment;
import li.com.bobsonclinic.fragment.BOBMonthClassDetailFragment;
import li.com.bobsonclinic.fragment.BOBMonthClassFragment;
import li.com.bobsonclinic.fragment.BOBMyBabyFragment;
import li.com.bobsonclinic.fragment.BOBWebViewContentFragment;
import li.com.bobsonclinic.fragment.HomeContainerFragment;
import li.com.bobsonclinic.fragment.MyContainerFragment;
import li.com.bobsonclinic.fragment.ServiceContainerFragment;
import li.com.bobsonclinic.fragment.chart.ChartFragment;
import li.com.bobsonclinic.fragment.dialog.base.BaseDialogFragment;
import li.com.bobsonclinic.fragment.dialog.base.DialogPickerDialogListener;
import li.com.bobsonclinic.logic.BOBDialogLogic;
import li.com.bobsonclinic.logic.BOBPreferenceLogic;
import li.com.bobsonclinic.logic.CommunicationLogic;
import li.com.bobsonclinic.logic.VideoLogic;
import li.com.bobsonclinic.manager.BOBDataManager;
import li.com.bobsonclinic.manager.BOBSystemManager;
import li.com.bobsonclinic.provider.BusNotifyProvider;
import li.com.bobsonclinic.provider.event.RequestEvent;
import li.com.bobsonclinic.util.SimpleOrientationListener;
import li.com.bobsonclinic.util.VersionChecker;
import li.com.bobsonclinic.viewer.CustomAlertDialogBuilder;
import me.yokeyword.fragmentation.SupportFragment;
import zk.android.networking.RequestError;

import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;

public class MainActivity extends BaseActivity implements OnFragmentAction, View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener {
    protected Fragment mAddContent, mAddTabContent;
    public static final String ADD_CONTENT_TAG = "add_content_frame";
    public static final String ADD_TAB_TAG = "add_tab_frame";
    public static final String LAYOUT_DID_EXPAND_TAG = "LAYOUT_DID_EXPAND_TAG";
    public static final String LAYOUT_DID_COLLAPSE_TAG = "LAYOUT_DID_COLLAPSE_TAG";
    private Handler mHandler;
    private Fragment mHomeFragment;
    Dialog mShowDialog;
    TextView momName;
    TextView inDate;
    TextView outDate;
    Dialog mShowLoading;
    RelativeLayout titleLayout;
    LinearLayout bottomLayout;
    BottomNavigationView navigation;

    //VIDEO ============ Start
    private RelativeLayout video_layout;
    private TextureView videoView;
    private ImageView extend_btn;
    private SurfaceTexture mSurface;
    //VIDEO ============ End

    //Chromecast ============ Start
    protected static final String INTENT_EXTRA_CAST_DEVICE = "CastDevice";
    private MediaRouter mediaRouter;
    private MediaRouteSelector mediaRouteSelector;
    //    private CastDevice castDevice;
    private boolean open = true;
    //Chromecast ============ End

    //Request ============ Start
    private long mLoginId;
    private long mMomDataId;
    private long mMomBreathId;
    private long mMomExamDataId;
    private long mMomTemperatureId;
    private long mMomExamTemPulId;
    private long mMomWeightId;
    private long mMomPulseId;
    private long mMomPressureId;
    private long mBabyDataId;
    private long mBabyBreathId;
    private long mBabyCarmeraDataId;
    private long mBabyExamDataId;
    private long mBabyJaundiceIndexId;
    private long mBabyMilkId;
    private long mBabyPulseId;
    private long mBabySwimId;
    private long mBabyTemperatureId;
    private long mBabyWeightId;
    private long mAboutUsId;
    private long mEducationId;
    private long mConsumerListId;
    private long mVasListId;
    private long mVasId;
    private long mVasCheckId;
    private long mVasUnCheckId;
    private long mMonthActivityId;
    private long mMonthActivityByMId;
    private long mActivityId;
    private long mActivityCheckId;
    private long mActivityUnCheckId;
    private long mTodayScheduleId;
    private long mMessageId;
    private long mExplainId1;
    private long mExplainId2;
    private long mExplainId3;
    private long mExplainId4;
    private long mExplainId5;
    private long mExplainId6;
    private long mExplainId7;
    //Request ============ End

    public static final int HANDLER_MESSAGE_LOGIN_SUCCESS = 0;
    public static final int HANDLER_MESSAGE_ABOUTUS_SUCCESS = 1;
    public static final int HANDLER_MESSAGE_EDUCATIONID_SUCCESS = 2;
    public static final int HANDLER_MESSAGE_MOM_DATA_SUCCESS = 3;
    public static final int HANDLER_MESSAGE_MOM_BREATH_SUCCESS = 4;
    public static final int HANDLER_MESSAGE_MOM_EXAM_SUCCESS = 5;
    public static final int HANDLER_MESSAGE_MOM_TEMPERATURE_SUCCESS = 6;
    public static final int HANDLER_MESSAGE_MOM_WEIGHT_SUCCESS = 7;
    public static final int HANDLER_MESSAGE_MOM_Pulse_SUCCESS = 8;
    public static final int HANDLER_MESSAGE_MOM_Pressure_SUCCESS = 9;
    public static final int HANDLER_MESSAGE_BABY_DATA_SUCCESS = 10;
    public static final int HANDLER_MESSAGE_BABY_BREATH_SUCCESS = 11;
    public static final int HANDLER_MESSAGE_BABY_CARMERA_DATA_SUCCESS = 12;
    public static final int HANDLER_MESSAGE_BABY_EXAM_DATA_SUCCESS = 13;
    public static final int HANDLER_MESSAGE_BABY_JAUNDICE_SUCCESS = 14;
    public static final int HANDLER_MESSAGE_BABY_MILK_SUCCESS = 15;
    public static final int HANDLER_MESSAGE_BABY_PULSE_SUCCESS = 16;
    public static final int HANDLER_MESSAGE_BABY_SWIM_SUCCESS = 17;
    public static final int HANDLER_MESSAGE_BABY_TEMPERATUREID_SUCCESS = 18;
    public static final int HANDLER_MESSAGE_BABY_WEIGHT_SUCCESS = 19;
    public static final int HANDLER_MESSAGE_MOM_EXAM_TEM_PUL_SUCCESS = 20;
    public static final int HANDLER_MESSAGE_COMSUMER_LIST_SUCCESS = 21;
    public static final int HANDLER_MESSAGE_VAS_LIST_SUCCESS = 22;
    public static final int HANDLER_MESSAGE_VAS_SUCCESS = 23;
    public static final int HANDLER_MESSAGE_MONTH_LIST_SUCCESS = 24;
    public static final int HANDLER_MESSAGE_VAS_CHECK_SUCCESS = 25;
    public static final int HANDLER_MESSAGE_VAS_UNCHECK_SUCCESS = 26;
    public static final int HANDLER_MESSAGE_ACTIVITY_CHECK_SUCCESS = 27;
    public static final int HANDLER_MESSAGE_ACTIVITY_UNCHECK_SUCCESS = 28;
    public static final int HANDLER_MESSAGE_ACTIVITY_SUCCESS = 29;
    public static final int HANDLER_MESSAGE_CONSUMER_SUCCESS = 30;
    public static final int HANDLER_MESSAGE_TODAY_SCHEDULE_SUCCESS = 31;
    public static final int HANDLER_MESSAGE_MESSAGE_SUCCESS = 32;
    public static final int HANDLER_MESSAGE_MONTH_LIST_BY_M_SUCCESS = 33;
    public static final int HANDLER_MESSAGE_EXPLAIN_SUCCESS = 34;
    public static final int HANDLER_MESSAGE_BABY_CARMERA_DATA_FAIL = 35;

    public static final int TAB_HOME = 0;
    public static final int TAB_SERVICE = 1;
    public static final int TAB_MEDIA = 2;
    public static final int TAB_MY = 3;
    public static final int TAB_ABOUT = 4;
    private int selectTab = TAB_HOME;
    private SparseArray<SupportFragment> tabStack = new SparseArray<>();
    private BaseDialogFragment dialog;
    private InputMethodManager inputMethodManager = null;
    private Handler handler = new Handler();
    private boolean isPortrait = true;
    protected SimpleOrientationListener mOrientationListener;

    public boolean isPortrait() {
        return isPortrait;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Check Network
        BOBSystemManager.getInstance().checkNetWorkState(getActivity());
        //Check Bluetooth
        BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        //Log.d("Milla", "bluetooth :" + mBtAdapter.isEnabled());
        if (mBtAdapter == null || !mBtAdapter.isEnabled()) {
            //showDialog("提醒您藍芽設定未開啟", true);
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            //TODO: 提示啟動藍芽
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 999);
            } else {
                startActivityForResult(enableIntent, 0);
            }

        }

        BusNotifyProvider.getInstance().register(this);
        initHandler();
        BOBDataManager.getInstance().setData();
        navigation = findViewById(R.id.navigation);

        setTabStack();
        checkVersion(true);
        initOrientationListener();
        AppCenter.start(getApplication(), "ca897a4b-606f-4394-82d8-42d73279e298",
                Analytics.class, Crashes.class);

        if (!BOBSystemManager.getInstance().isLogin()) {
            addFragment(new BOBLoginFragment());
            return;
        }


        navigation.setSelectedItemId(R.id.nav_media);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.hide(tabStack.get(TAB_HOME));
        ft.hide(tabStack.get(TAB_SERVICE));
        ft.hide(tabStack.get(TAB_MEDIA));
        ft.hide(tabStack.get(TAB_MY));
        ft.hide(tabStack.get(TAB_HOME));
        ft.commit();


    }



    // -------------------------------------------
    private void setTabStack() {
        if (getSupportFragmentManager() != null && getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        }
        if (findFragment(HomeContainerFragment.class) == null || tabStack.get(TAB_HOME) == null) {
            tabStack.put(TAB_HOME, new HomeContainerFragment());
            tabStack.put(TAB_SERVICE, new ServiceContainerFragment());
            tabStack.put(TAB_MEDIA, new BOBBabyPlayVideoFragment());
            tabStack.put(TAB_MY, new MyContainerFragment());
            tabStack.put(TAB_ABOUT, new AboutContainerFragment());

            loadMultipleRootFragment(R.id.tab_layout, TAB_HOME
                    , tabStack.get(TAB_HOME)
                    , tabStack.get(TAB_SERVICE)
                    , tabStack.get(TAB_MEDIA)
                    , tabStack.get(TAB_MY)
                    , tabStack.get(TAB_ABOUT));
        } else {
            tabStack.put(TAB_HOME, findFragment(tabStack.get(TAB_HOME).getClass()));
            tabStack.put(TAB_SERVICE, findFragment(tabStack.get(TAB_SERVICE).getClass()));
            tabStack.put(TAB_MEDIA, findFragment(tabStack.get(TAB_MEDIA).getClass()));
            tabStack.put(TAB_MY, findFragment(tabStack.get(TAB_MY).getClass()));
            tabStack.put(TAB_ABOUT, findFragment(tabStack.get(TAB_ABOUT).getClass()));
        }


        navigation.setOnNavigationItemSelectedListener(this);

    }


    @Override
    public void onResume() {
        super.onResume();
        setupReceiver();
        getHomeData();
    }

    @Override
    public void onPause() {
        super.onPause();
        removeReceiver();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (isRemoteDisplaying()) {
//                CastRemoteDisplayLocalService.stopService();
            }
            if (mediaRouter != null) {
//                mediaRouter.removeCallback(mMediaRouterCallback);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            if (mOrientationListener != null) {
                mOrientationListener.disable();
                mOrientationListener = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // -------------------------------------------
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (getSupportFragmentManager() != null && getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        }

        switch (item.getItemId()) {
            case R.id.nav_home:
                changeTab(TAB_HOME);
                break;
            case R.id.nav_service:
                changeTab(TAB_SERVICE);
                break;
            case R.id.nav_media:
                changeTab(TAB_MEDIA);
                break;
            case R.id.nav_my:
                changeTab(TAB_MY);
                //replaceTabFragment(new MyContainerFragment());
                break;
            case R.id.nav_about:
                changeTab(TAB_ABOUT);
                break;
        }

        return true;
    }

    // -------------------------------------------
    private void changeTab(int tabId) {
        showHideFragment(tabStack.get(tabId), tabStack.get(selectTab));
        selectTab = tabId;
        if (tabStack.get(tabId) != null) {
            tabStack.get(tabId).onResume();
        }
    }

    public int getCurrentTab() {
        return this.selectTab;
    }


    private void checkVersion(boolean shouldShowDialog) {

        VersionChecker checker = new VersionChecker();
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            int currentVersion = Integer.valueOf(pInfo.versionCode);
            int latestVersion = currentVersion;
            String v = checker.execute().get();
            if (v != null) latestVersion = Integer.valueOf(v);
            if (latestVersion > currentVersion) {
                //should show dialog
                if (shouldShowDialog) {
                    showUpdateDialog();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {

        }

    }

    private void showUpdateDialog() {
        android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(this)
                .setTitle(getString(R.string.new_version_update_title))
                .setMessage(getString(R.string.new_version_update_message))
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setNegativeButton(getString(R.string.go_to_update), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        gotoGooglaPlayUpdate();
                    }
                }).create();
        dialog.show();
    }

    private void gotoGooglaPlayUpdate() {
        //Show dialog
        final String appPackageName = getApplicationContext().getPackageName(); // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    public void getHomeData() {
        Log.d("getHomeData","getHomeData");
        getMomData();
        postBabyData();
        postMessage();
    }

    //Get Data from Server
    public void getMomData() {
        mMomDataId = CommunicationLogic.getInstance().postMomData(BOBSystemManager.getInstance().getUserToken());
    }

    private void updateMomData(BOBMomData data) {
        momName.setText(data.getName());
        inDate.setText(data.getCheck_in_date());
        outDate.setText(data.getCheck_out_date());
    }

    private void initHandler() {

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Fragment fragment = getSupportFragmentManager().findFragmentByTag(ADD_CONTENT_TAG);
                Fragment mainfragment = getSupportFragmentManager().findFragmentById(R.id.tab_layout);
                super.handleMessage(msg);
                int key = msg.what;
                switch (key) {

                    case HANDLER_MESSAGE_LOGIN_SUCCESS:
                        dismissLoading();
                        try {
                            RequestError error = (RequestError) msg.obj;
                            if (error != null && error.getErrorCode() != null && error.getErrorCode().equals("601")) {
                                showErrorAccountDialog(true);
                                break;
                            }
                            if (error != null && error.getErrorCode() != null && error.getErrorCode().equals("602")) {
                                showDialog(error.getErrorMsg(), true);
                                break;
                            }
                        } catch (ClassCastException e) {

                        }

                        try {
                            BOBLogin login = (BOBLogin) msg.obj;
                            getHomeData();
                            goHomePage();
                            postMessage();
                        } catch (ClassCastException e) {

                        }
                        break;
                    case HANDLER_MESSAGE_ABOUTUS_SUCCESS:
                        break;
                    case HANDLER_MESSAGE_EDUCATIONID_SUCCESS:
                        break;
                    case HANDLER_MESSAGE_CONSUMER_SUCCESS:

                        tokenFailProcess(msg.obj);
                        break;
                    case HANDLER_MESSAGE_MOM_DATA_SUCCESS:
                        try {
                            //Handle not upload photos
                            Photo.handleNotUploadedPhotos();
                        } catch (ClassCastException e) {
                        }
                        tokenFailProcess(msg.obj);
                        break;
                    case HANDLER_MESSAGE_MOM_BREATH_SUCCESS:
                        try {
                            BOBMomBreathResponse res = (BOBMomBreathResponse) msg.obj;
                            if (fragment instanceof ChartFragment) {
                                ((ChartFragment) fragment).setChartData(res);
                            }
                        } catch (ClassCastException e) {
                        }
                        tokenFailProcess(msg.obj);
                        break;
                    case HANDLER_MESSAGE_MOM_EXAM_SUCCESS:
                        try {
                            List<BOBMomExam> momExam = (List<BOBMomExam>) msg.obj;
                            if (fragment instanceof BOBMomExamFragment) {
                                ((BOBMomExamFragment) fragment).setData();
                            }
                        } catch (ClassCastException e) {
                        }
                        tokenFailProcess(msg.obj);

                        break;
                    case HANDLER_MESSAGE_MOM_TEMPERATURE_SUCCESS:
                        try {
                            BOBMomTemperatureResponse res = (BOBMomTemperatureResponse) msg.obj;
                            if (fragment instanceof ChartFragment) {
                                ((ChartFragment) fragment).setChartData(res);
                            }
                        } catch (ClassCastException e) {
                        }
                        tokenFailProcess(msg.obj);

                        break;
                    case HANDLER_MESSAGE_MOM_WEIGHT_SUCCESS:
                        try {
                            BOBMomWeightResponse res = (BOBMomWeightResponse) msg.obj;
                            if (fragment instanceof ChartFragment) {
                                ((ChartFragment) fragment).setChartData(res);
                            }
                        } catch (ClassCastException e) {
                        }
                        tokenFailProcess(msg.obj);

                        break;
                    case HANDLER_MESSAGE_MOM_Pulse_SUCCESS:
                        try {
                            BOBMomPulseResponse res = (BOBMomPulseResponse) msg.obj;
                            if (fragment instanceof ChartFragment) {
                                ((ChartFragment) fragment).setChartData(res);
                            }
                        } catch (ClassCastException e) {
                        }
                        tokenFailProcess(msg.obj);
                        break;
                    case HANDLER_MESSAGE_MOM_Pressure_SUCCESS:
                        try {
                            BOBMomPressureResponse res = (BOBMomPressureResponse) msg.obj;
                            if (fragment instanceof ChartFragment) {
                                ((ChartFragment) fragment).setChartData(res);
                            }
                        } catch (ClassCastException e) {
                        }
                        tokenFailProcess(msg.obj);

                        break;
                    case HANDLER_MESSAGE_MOM_EXAM_TEM_PUL_SUCCESS:
                        try {

                            List<BOBMomTemPulPre> momList = (List<BOBMomTemPulPre>) msg.obj;
                            if (fragment instanceof BOBMomTemPulPreFragment) {
                                ((BOBMomTemPulPreFragment) fragment).setData();
                            }
                        } catch (ClassCastException e) {
                        }
                        tokenFailProcess(msg.obj);

                        break;
                    case HANDLER_MESSAGE_BABY_DATA_SUCCESS:
                        if (mainfragment instanceof BOBHomePageFragment) {
                            ((BOBHomePageFragment) mainfragment).setBabyPager();
                        }
                        tokenFailProcess(msg.obj);

                        break;
                    case HANDLER_MESSAGE_BABY_BREATH_SUCCESS:
                        try {
                            BOBBabyBreathResponse res = (BOBBabyBreathResponse) msg.obj;
                            if (fragment instanceof ChartFragment) {
                                ((ChartFragment) fragment).setChartData(res);
                            }
                        } catch (ClassCastException e) {
                        }
                        tokenFailProcess(msg.obj);

                        break;
                    case HANDLER_MESSAGE_BABY_CARMERA_DATA_SUCCESS:

                        tokenFailProcess(msg.obj);
                        break;
                    case HANDLER_MESSAGE_BABY_CARMERA_DATA_FAIL:
                        if(tabStack.get(selectTab) instanceof BOBBabyPlayVideoFragment){
                            ( (BOBBabyPlayVideoFragment)tabStack.get(selectTab)).getCameraDataFail();
                        }
                        break;
                    case HANDLER_MESSAGE_BABY_EXAM_DATA_SUCCESS:
                        try {
                            List<BOBBabyExam> res = (List<BOBBabyExam>) msg.obj;
                            if (fragment instanceof BOBBabyExamFragment) {
                                ((BOBBabyExamFragment) fragment).setData();
                            }
                        } catch (ClassCastException e) {
                        }

                        tokenFailProcess(msg.obj);
                        break;

                    case HANDLER_MESSAGE_BABY_JAUNDICE_SUCCESS:
                        try {
                            BOBBabyJaundiceResponse res = (BOBBabyJaundiceResponse) msg.obj;
                            if (fragment instanceof ChartFragment) {
                                ((ChartFragment) fragment).setChartData(res);
                            }
                        } catch (ClassCastException e) {
                        }
                        tokenFailProcess(msg.obj);
                        break;

                    case HANDLER_MESSAGE_BABY_MILK_SUCCESS:
                        try {
                            BOBBabyMilkResponse res = (BOBBabyMilkResponse) msg.obj;
                            if (fragment instanceof ChartFragment) {
                                ((ChartFragment) fragment).setChartData(res);
                            }
                        } catch (ClassCastException e) {
                        }
                        tokenFailProcess(msg.obj);
                        break;
                    case HANDLER_MESSAGE_BABY_PULSE_SUCCESS:
                        try {
                            BOBBabyPulseResponse res = (BOBBabyPulseResponse) msg.obj;
                            if (fragment instanceof ChartFragment) {
                                ((ChartFragment) fragment).setChartData(res);
                            }
                        } catch (ClassCastException e) {
                        }
                        tokenFailProcess(msg.obj);
                        break;
                    case HANDLER_MESSAGE_BABY_SWIM_SUCCESS:
                        try {
                            BOBBabySwim res = (BOBBabySwim) msg.obj;
                            if (fragment instanceof BOBBabyVideoFragment) {
//                                ((BOBBabyVideoFragment)fragment).setChartData(res);
                            }
                        } catch (ClassCastException e) {
                        }
                        tokenFailProcess(msg.obj);
                        break;
                    case HANDLER_MESSAGE_BABY_TEMPERATUREID_SUCCESS:
                        try {
                            BOBBabyTemperatureResponse res = (BOBBabyTemperatureResponse) msg.obj;
                            if (fragment instanceof ChartFragment) {
                                ((ChartFragment) fragment).setChartData(res);
                            }
                        } catch (ClassCastException e) {
                        }
                        tokenFailProcess(msg.obj);
                        break;
                    case HANDLER_MESSAGE_BABY_WEIGHT_SUCCESS:
                        try {
                            BOBBabyWeightResponse res = (BOBBabyWeightResponse) msg.obj;
                            if (fragment instanceof ChartFragment) {
                                ((ChartFragment) fragment).setChartData(res);
                            }
                        } catch (ClassCastException e) {
                        }
                        tokenFailProcess(msg.obj);
                        break;
                    case HANDLER_MESSAGE_COMSUMER_LIST_SUCCESS:

                        break;
                    case HANDLER_MESSAGE_VAS_LIST_SUCCESS:
                        if (fragment instanceof BOBAddServiceFragment) {
                            try {
                                List<BOBVasList> List = (List<BOBVasList>) msg.obj;
                                ((BOBAddServiceFragment) fragment).loadInfo();
                            } catch (ClassCastException e) {
                            }
                        }
                        break;
                    case HANDLER_MESSAGE_VAS_SUCCESS:
                        if (fragment instanceof BOBAddServiceDetailFragment) {
                            try {
                                BOBVas vas = (BOBVas) msg.obj;
                                ((BOBAddServiceDetailFragment) fragment).setData(vas);
                            } catch (ClassCastException e) {
                            }

                        }
                        break;
                    case HANDLER_MESSAGE_VAS_CHECK_SUCCESS:
                        if (fragment instanceof BOBAddServiceDetailFragment) {
                            ((BOBAddServiceDetailFragment) fragment).setCheck(true);
                        }
                        break;
                    case HANDLER_MESSAGE_VAS_UNCHECK_SUCCESS:
                        if (fragment instanceof BOBAddServiceDetailFragment) {
                            ((BOBAddServiceDetailFragment) fragment).setCheck(false);
                        }
                        break;
                    case HANDLER_MESSAGE_MONTH_LIST_SUCCESS:
                        if (fragment instanceof BOBMonthClassFragment) {
                            try {
                                List<BOBMonthActivity> List = (List<BOBMonthActivity>) msg.obj;
                                ((BOBMonthClassFragment) fragment).loadInfo();
                            } catch (ClassCastException e) {
                            }
                        }
                        break;
                    case HANDLER_MESSAGE_MONTH_LIST_BY_M_SUCCESS:
                        if (fragment instanceof BOBMonthClassFragment) {
                            ((BOBMonthClassFragment) fragment).loadInfo();
                        }
                        break;
                    case HANDLER_MESSAGE_ACTIVITY_SUCCESS:

                        if (fragment instanceof BOBMonthClassDetailFragment) {
                            try {
                                BOBActivity act = (BOBActivity) msg.obj;
                                ((BOBMonthClassDetailFragment) fragment).setData(act);
                            } catch (ClassCastException e) {
                            }

                        }
                        break;
                    case HANDLER_MESSAGE_ACTIVITY_CHECK_SUCCESS:
                        if (fragment instanceof BOBMonthClassDetailFragment) {
                            ((BOBMonthClassDetailFragment) fragment).setCheck(true);
                        }
                        tokenFailProcess(msg.obj);
                        break;
                    case HANDLER_MESSAGE_ACTIVITY_UNCHECK_SUCCESS:
                        if (fragment instanceof BOBMonthClassDetailFragment) {
                            ((BOBMonthClassDetailFragment) fragment).setCheck(false);
                        }
                        tokenFailProcess(msg.obj);
                        break;
                    case HANDLER_MESSAGE_TODAY_SCHEDULE_SUCCESS:

                        tokenFailProcess(msg.obj);
                        break;
                    case HANDLER_MESSAGE_MESSAGE_SUCCESS:
                        tokenFailProcess(msg.obj);
                        break;
                    case HANDLER_MESSAGE_EXPLAIN_SUCCESS:

                        break;
                }
            }
        };
    }

    public void showLoading() {
        if (mShowLoading != null && !mShowLoading.isShowing()) {
            mShowLoading.show();
            return;
        }

        mShowLoading = new CustomAlertDialogBuilder(getActivity(), R.style.customAlertDialog, R.layout.custom_alert_loading);
        mShowLoading.setCancelable(false);
        mShowLoading.show();
    }

    public void showLogining() {
        if (mShowLoading != null && !mShowLoading.isShowing()) {
            mShowLoading.show();
            return;
        }

        mShowLoading = new CustomAlertDialogBuilder(getActivity(), R.style.customAlertDialog, R.layout.custom_alert_login);
        mShowLoading.setCancelable(false);
        mShowLoading.show();
    }

    public void dismissLoading() {
        if (mShowLoading != null && mShowLoading.isShowing()) {
            mShowLoading.dismiss();
        }
    }

    //關於我們
    public void goAboutUs() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(ADD_CONTENT_TAG);
        if (fragment instanceof BOBAboutUsFragment) {
            return;
        }
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        postAboutUSIntroList();
        addFragment(new BOBAboutUsFragment(), R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
    }

    //媽咪日記
    public void goMomeyDialy() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(ADD_CONTENT_TAG);
        if (fragment instanceof BOBMomDialyFragment) {
            return;
        }
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        addFragment(new BOBMomDialyFragment(), R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
    }

    //寶寶影像
    public void goBabyImage() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(ADD_CONTENT_TAG);
        if (fragment instanceof BOBBabyVideoFragment) {
            return;
        }
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        postBabyCarmeraData();
        addFragment(new BOBBabyVideoFragment(), R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
    }

    //我的寶寶
    public void goMyBaby() {

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(ADD_CONTENT_TAG);
        if (fragment instanceof BOBMyBabyFragment) {
            return;
        }
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        addFragment(new BOBMyBabyFragment(), R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
    }

    //衛教資訊
    public void goTeachInfo() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(ADD_CONTENT_TAG);
        if (fragment instanceof BOBEduInfoFragment) {
            return;
        }
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        postEducationIntroList();
        addFragment(new BOBEduInfoFragment(), R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
    }

    //本月課程
    public void goMonthClasses() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(ADD_CONTENT_TAG);
        if (fragment instanceof BOBMonthClassFragment) {
            return;
        }
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        addFragment(new BOBMonthClassFragment(), R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
    }

    //加值服務內頁
    public void goAddServiceDetail(String vasId) {
        addFragment(BOBAddServiceDetailFragment.createFragment(vasId), R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
    }

    //加值服務
    public void goAddService() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(ADD_CONTENT_TAG);
        if (fragment instanceof BOBAddServiceFragment) {
            return;
        }
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        postVasList();
        addFragment(new BOBAddServiceFragment(), R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
    }


    //消費明細
    public void goConsumerDetail() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(ADD_CONTENT_TAG);
        if (fragment instanceof BOBConsumerFragment) {
            return;
        }
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        postConsumerList();
        addFragment(new BOBConsumerFragment(), R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
    }

    //回首頁
    public void goHomePage() {
        getSupportFragmentManager().popBackStack();

    }

    //登入頁
    public void goLoginPage() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        getSupportFragmentManager().beginTransaction().add(R.id.content_layout, new BOBLoginFragment()).addToBackStack(null).commitAllowingStateLoss();
    }


    //本月課程內頁
    public void goMonthClassDetail(String actId) {
        addFragment(BOBMonthClassDetailFragment.createFragment(actId), R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
    }

    //登出
    public void logout() {
        if(tabStack.get(2) != null && tabStack.get(2) instanceof BOBBabyPlayVideoFragment){
            ((BOBBabyPlayVideoFragment)tabStack.get(2)).releaseMedia();
        }
        BOBDataManager.getInstance().cleanData();
        BOBPreferenceLogic.getInstance().logoutData();
        goLoginPage();

    }

    public void postLogin(String account, String password) {
        mLoginId = CommunicationLogic.getInstance().postUserLogin(account, password, BOBPreferenceLogic.getInstance().getGCMToken());
    }

    public void postBabyData() {
        mBabyDataId = CommunicationLogic.getInstance().postBabiesData(BOBSystemManager.getInstance().getUserToken());
    }

    public void postMomExam() {
        mMomExamDataId = CommunicationLogic.getInstance().postMomExamData(BOBSystemManager.getInstance().getUserToken(), BOBDataManager.getInstance().getBabyId());
    }

    public Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentByTag(ADD_CONTENT_TAG);
    }

    //For test
    public void postBabyData(String babyId) {
        mBabyBreathId = CommunicationLogic.getInstance().postBabyBreath(BOBSystemManager.getInstance().getUserToken(), BOBDataManager.getInstance().getBabyId());
        mMomExamDataId = CommunicationLogic.getInstance().postMomExamData(BOBSystemManager.getInstance().getUserToken(), BOBDataManager.getInstance().getBabyId());
        mBabyExamDataId = CommunicationLogic.getInstance().postBabyExamData(BOBSystemManager.getInstance().getUserToken(), BOBDataManager.getInstance().getBabyId());
        mBabyJaundiceIndexId = CommunicationLogic.getInstance().postBabyJaundiceIndex(BOBSystemManager.getInstance().getUserToken(), BOBDataManager.getInstance().getBabyId());
        mBabyMilkId = CommunicationLogic.getInstance().postBabyMilk(BOBSystemManager.getInstance().getUserToken(), BOBDataManager.getInstance().getBabyId());
        mBabyPulseId = CommunicationLogic.getInstance().postBabyPulse(BOBSystemManager.getInstance().getUserToken(), BOBDataManager.getInstance().getBabyId());
        mBabySwimId = CommunicationLogic.getInstance().postBabySwim(BOBSystemManager.getInstance().getUserToken(), BOBDataManager.getInstance().getBabyId());
        mBabyTemperatureId = CommunicationLogic.getInstance().postBabyTemperature(BOBSystemManager.getInstance().getUserToken(), BOBDataManager.getInstance().getBabyId());
        mBabyWeightId = CommunicationLogic.getInstance().postBabyWeight(BOBSystemManager.getInstance().getUserToken(), BOBDataManager.getInstance().getBabyId());
    }

    public void postBabyBreath() {
        mBabyBreathId = CommunicationLogic.getInstance().postBabyBreath(BOBSystemManager.getInstance().getUserToken(), BOBDataManager.getInstance().getBabyId());
    }

    public void postBabyExam() {
        mBabyExamDataId = CommunicationLogic.getInstance().postBabyExamData(BOBSystemManager.getInstance().getUserToken(), BOBDataManager.getInstance().getBabyId());
    }

    public void postBabyJaundice() {
        mBabyJaundiceIndexId = CommunicationLogic.getInstance().postBabyJaundiceIndex(BOBSystemManager.getInstance().getUserToken(), BOBDataManager.getInstance().getBabyId());
    }

    public void postBabyMilk() {
        mBabyMilkId = CommunicationLogic.getInstance().postBabyMilk(BOBSystemManager.getInstance().getUserToken(), BOBDataManager.getInstance().getBabyId());
    }

    public void postBabyPulse() {
        mBabyPulseId = CommunicationLogic.getInstance().postBabyPulse(BOBSystemManager.getInstance().getUserToken(), BOBDataManager.getInstance().getBabyId());
    }

    public void postBabyTemperature() {
        mBabyTemperatureId = CommunicationLogic.getInstance().postBabyTemperature(BOBSystemManager.getInstance().getUserToken(), BOBDataManager.getInstance().getBabyId());
    }

    public void postBabySwim() {
        mBabySwimId = CommunicationLogic.getInstance().postBabySwim(BOBSystemManager.getInstance().getUserToken(), BOBDataManager.getInstance().getBabyId());
    }

    public void postBabyWeight() {
        mBabyWeightId = CommunicationLogic.getInstance().postBabyWeight(BOBSystemManager.getInstance().getUserToken(), BOBDataManager.getInstance().getBabyId());
    }

    public void postBabyCarmeraData() {
        if (BOBSystemManager.getInstance().checkNetWorkState(getActivity())) {
            mBabyCarmeraDataId = CommunicationLogic.getInstance().postCameraData(BOBSystemManager.getInstance().getUserToken(), BOBDataManager.getInstance().getBabyId());
        }
    }


    public void postTodaySchedule() {
        mTodayScheduleId = CommunicationLogic.getInstance().postTodaySchedule(BOBSystemManager.getInstance().getUserToken());
    }

    public void postMessage() {
        mMessageId = CommunicationLogic.getInstance().postNotifications(BOBSystemManager.getInstance().getUserToken());
    }

    public void postAboutUSIntroList() {
        if (BOBSystemManager.getInstance().checkNetWorkState(getActivity())) {
            mAboutUsId = CommunicationLogic.getInstance().postIntroList(BOBSystemManager.getInstance().getUserToken(), BOBDataManager.INTRO_ABOUT_US);
        }
    }

    public void postEducationIntroList() {
        if (BOBSystemManager.getInstance().checkNetWorkState(getActivity())) {
            mEducationId = CommunicationLogic.getInstance().postIntroList(BOBSystemManager.getInstance().getUserToken(), BOBDataManager.INTRO_EDU);
        }
    }

    public void postConsumerList() {
        if (BOBSystemManager.getInstance().checkNetWorkState(getActivity())) {
            mConsumerListId = CommunicationLogic.getInstance().postConsumeList(BOBSystemManager.getInstance().getUserToken());
        }
    }

    public void postVasList() {
        if (BOBSystemManager.getInstance().checkNetWorkState(getActivity())) {
            mVasListId = CommunicationLogic.getInstance().postVasList(BOBSystemManager.getInstance().getUserToken());
        }
    }

    public void postVas(String vasId) {
        if (BOBSystemManager.getInstance().checkNetWorkState(getActivity())) {
            mVasId = CommunicationLogic.getInstance().postVas(BOBSystemManager.getInstance().getUserToken(), vasId);
        }
    }

    public void postCheckVas(String vasId) {
        if (BOBSystemManager.getInstance().checkNetWorkState(getActivity())) {
            mVasCheckId = CommunicationLogic.getInstance().postVasCheck(BOBSystemManager.getInstance().getUserToken(), vasId);
        }
    }

    public void postUnCheckVas(String vasId) {
        if (BOBSystemManager.getInstance().checkNetWorkState(getActivity())) {
            mVasUnCheckId = CommunicationLogic.getInstance().postVasUnCheck(BOBSystemManager.getInstance().getUserToken(), vasId);
        }
    }

    public void postMonthActivity() {
        if (BOBSystemManager.getInstance().checkNetWorkState(getActivity())) {
            mMonthActivityId = CommunicationLogic.getInstance().postMonthActivities(BOBSystemManager.getInstance().getUserToken());
        }
    }

    public void postMonthActivity(String month) {
        if (BOBSystemManager.getInstance().checkNetWorkState(getActivity())) {
            mMonthActivityByMId = CommunicationLogic.getInstance().postMonthActivities(BOBSystemManager.getInstance().getUserToken(), month);
        }
    }

    public void postActivity(String id) {
        if (BOBSystemManager.getInstance().checkNetWorkState(getActivity())) {
            mActivityId = CommunicationLogic.getInstance().postActivity(BOBSystemManager.getInstance().getUserToken(), id);
        }
    }

    public void postCheckActivity(String actId) {
        if (BOBSystemManager.getInstance().checkNetWorkState(getActivity())) {
            mActivityCheckId = CommunicationLogic.getInstance().postCheckActivity(BOBSystemManager.getInstance().getUserToken(), actId);
        }
    }

    public void postUnCheckActivity(String actId) {
        if (BOBSystemManager.getInstance().checkNetWorkState(getActivity())) {
            mActivityUnCheckId = CommunicationLogic.getInstance().postUncheckActivity(BOBSystemManager.getInstance().getUserToken(), actId);
        }
    }

    public void postMomBreath() {
        mMomBreathId = CommunicationLogic.getInstance().postMomBreath(BOBSystemManager.getInstance().getUserToken());
    }

    public void postMomTemperature() {
        mMomTemperatureId = CommunicationLogic.getInstance().postMomTemperature(BOBSystemManager.getInstance().getUserToken());
    }

    public void postMomExamTemPul() {
        mMomExamTemPulId = CommunicationLogic.getInstance().postMomExamTemPulPre(BOBSystemManager.getInstance().getUserToken());
    }

    public void postMomWeight() {
        mMomWeightId = CommunicationLogic.getInstance().postMomWeight(BOBSystemManager.getInstance().getUserToken());
    }

    public void postMomPulse() {
        mMomPulseId = CommunicationLogic.getInstance().postMomPulse(BOBSystemManager.getInstance().getUserToken());
    }

    public void postMomPressure() {
        mMomPressureId = CommunicationLogic.getInstance().postMomPressure(BOBSystemManager.getInstance().getUserToken());
    }

    public void postExplain() {
        mExplainId1 = CommunicationLogic.getInstance().postExplain(BOBSystemManager.getInstance().getUserToken(), "1");
        mExplainId2 = CommunicationLogic.getInstance().postExplain(BOBSystemManager.getInstance().getUserToken(), "2");
        mExplainId3 = CommunicationLogic.getInstance().postExplain(BOBSystemManager.getInstance().getUserToken(), "3");
        mExplainId4 = CommunicationLogic.getInstance().postExplain(BOBSystemManager.getInstance().getUserToken(), "4");
        mExplainId5 = CommunicationLogic.getInstance().postExplain(BOBSystemManager.getInstance().getUserToken(), "5");
        mExplainId6 = CommunicationLogic.getInstance().postExplain(BOBSystemManager.getInstance().getUserToken(), "6");
        mExplainId7 = CommunicationLogic.getInstance().postExplain(BOBSystemManager.getInstance().getUserToken(), "7");
        CommunicationLogic.getInstance().postExplain(BOBSystemManager.getInstance().getUserToken(), "8");
        CommunicationLogic.getInstance().postExplain(BOBSystemManager.getInstance().getUserToken(), "9");
        CommunicationLogic.getInstance().postExplain(BOBSystemManager.getInstance().getUserToken(), "10");
        CommunicationLogic.getInstance().postExplain(BOBSystemManager.getInstance().getUserToken(), "11");
        CommunicationLogic.getInstance().postExplain(BOBSystemManager.getInstance().getUserToken(), "12");
        CommunicationLogic.getInstance().postExplain(BOBSystemManager.getInstance().getUserToken(), "13");
        CommunicationLogic.getInstance().postExplain(BOBSystemManager.getInstance().getUserToken(), "14");
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        final Fragment fragment = getSupportFragmentManager().findFragmentByTag(ADD_CONTENT_TAG); //getChildFragmentManager().findFragmentByTag(TAG + id);
        // 檢查是否為Back按鍵以及fragment backstack已經沒有頁面
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && fragment == null) {

            Dialog customButton = new CustomAlertDialogBuilder(MainActivity.this, R.style.customAlertDialog, getString(R.string.isexist_app), getString(R.string.cancel), getString(R.string.determine), new CustomAlertDialogBuilder.CustomAlertDialogListener() {

                @Override
                public void onLeftBtnOnClick() {

                }

                @Override
                public void onRightBtnOnClick() {
                    finish();
                    // 殺掉目前的task
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
            });
            customButton.setCancelable(true);
            customButton.show();

        } else if (fragment != null) {
            if(fragment instanceof BOBLoginFragment && !BOBSystemManager.getInstance().isLogin()){
                return true;
            }
            getSupportFragmentManager().popBackStack();
            return true;

        }

        return super.onKeyDown(keyCode, event);
    }

    @Subscribe
    public void onRequestStatusChanged(RequestEvent event) {
        RequestEvent.Event requestEvent = event.getRequestEvent();
        long requestId = event.getRequestId();
        switch (requestEvent) {
            case START:
                break;
            case SUCCEED:
                Log.d("Milla", " requestId: " + requestId);
                if (requestId == mLoginId) {
                    sendActivityMessage(HANDLER_MESSAGE_LOGIN_SUCCESS, 0, 0, event.getData());
                } else if (requestId == mMomDataId) {
                    sendActivityMessage(HANDLER_MESSAGE_MOM_DATA_SUCCESS, 0, 0, event.getData());
                } else if (requestId == mMomBreathId) {
                    sendActivityMessage(HANDLER_MESSAGE_MOM_BREATH_SUCCESS, 0, 0, event.getData());
                } else if (requestId == mMomExamDataId) {
                    sendActivityMessage(HANDLER_MESSAGE_MOM_EXAM_SUCCESS, 0, 0, event.getData());
                } else if (requestId == mMomTemperatureId) {
                    sendActivityMessage(HANDLER_MESSAGE_MOM_TEMPERATURE_SUCCESS, 0, 0, event.getData());
                } else if (requestId == mMomExamTemPulId) {
                    sendActivityMessage(HANDLER_MESSAGE_MOM_EXAM_TEM_PUL_SUCCESS, 0, 0, event.getData());
                } else if (requestId == mMomWeightId) {
                    sendActivityMessage(HANDLER_MESSAGE_MOM_WEIGHT_SUCCESS, 0, 0, event.getData());
                } else if (requestId == mMomPulseId) {
                    sendActivityMessage(HANDLER_MESSAGE_MOM_Pulse_SUCCESS, 0, 0, event.getData());
                } else if (requestId == mMomPressureId) {
                    sendActivityMessage(HANDLER_MESSAGE_MOM_Pressure_SUCCESS, 0, 0, event.getData());
                } else if (requestId == mBabyDataId) {
                    sendActivityMessage(HANDLER_MESSAGE_BABY_DATA_SUCCESS, 0, 0, event.getData());
                } else if (requestId == mBabyBreathId) {
                    sendActivityMessage(HANDLER_MESSAGE_BABY_BREATH_SUCCESS, 0, 0, event.getData());
                } else if (requestId == mBabyCarmeraDataId) {
                    sendActivityMessage(HANDLER_MESSAGE_BABY_CARMERA_DATA_SUCCESS, 0, 0, event.getData());
                } else if (requestId == mBabyExamDataId) {
                    sendActivityMessage(HANDLER_MESSAGE_BABY_EXAM_DATA_SUCCESS, 0, 0, event.getData());
                } else if (requestId == mBabyJaundiceIndexId) {
                    sendActivityMessage(HANDLER_MESSAGE_BABY_JAUNDICE_SUCCESS, 0, 0, event.getData());
                } else if (requestId == mBabyMilkId) {
                    sendActivityMessage(HANDLER_MESSAGE_BABY_MILK_SUCCESS, 0, 0, event.getData());
                } else if (requestId == mBabyPulseId) {
                    sendActivityMessage(HANDLER_MESSAGE_BABY_PULSE_SUCCESS, 0, 0, event.getData());
                } else if (requestId == mBabySwimId) {
                    sendActivityMessage(HANDLER_MESSAGE_BABY_SWIM_SUCCESS, 0, 0, event.getData());
                } else if (requestId == mBabyTemperatureId) {
                    sendActivityMessage(HANDLER_MESSAGE_BABY_TEMPERATUREID_SUCCESS, 0, 0, event.getData());
                } else if (requestId == mBabyWeightId) {
                    sendActivityMessage(HANDLER_MESSAGE_BABY_WEIGHT_SUCCESS, 0, 0, event.getData());
                } else if (requestId == mAboutUsId) {
                    sendActivityMessage(HANDLER_MESSAGE_ABOUTUS_SUCCESS, 0, 0, event.getData());
                } else if (requestId == mEducationId) {
                    sendActivityMessage(HANDLER_MESSAGE_EDUCATIONID_SUCCESS, 0, 0, event.getData());
                } else if (requestId == mVasListId) {
                    sendActivityMessage(HANDLER_MESSAGE_VAS_LIST_SUCCESS, 0, 0, event.getData());
                } else if (requestId == mVasId) {
                    sendActivityMessage(HANDLER_MESSAGE_VAS_SUCCESS, 0, 0, event.getData());
                } else if (requestId == mVasCheckId) {
                    sendActivityMessage(HANDLER_MESSAGE_VAS_CHECK_SUCCESS, 0, 0, event.getData());
                } else if (requestId == mVasUnCheckId) {
                    sendActivityMessage(HANDLER_MESSAGE_VAS_UNCHECK_SUCCESS, 0, 0, event.getData());
                } else if (requestId == mActivityCheckId) {
                    sendActivityMessage(HANDLER_MESSAGE_ACTIVITY_CHECK_SUCCESS, 0, 0, event.getData());
                } else if (requestId == mActivityUnCheckId) {
                    sendActivityMessage(HANDLER_MESSAGE_ACTIVITY_UNCHECK_SUCCESS, 0, 0, event.getData());
                } else if (requestId == mActivityId) {
                    sendActivityMessage(HANDLER_MESSAGE_ACTIVITY_SUCCESS, 0, 0, event.getData());
                } else if (requestId == mMonthActivityId) {
                    sendActivityMessage(HANDLER_MESSAGE_MONTH_LIST_SUCCESS, 0, 0, event.getData());
                } else if (requestId == mConsumerListId) {
                    sendActivityMessage(HANDLER_MESSAGE_CONSUMER_SUCCESS, 0, 0, event.getData());
                } else if (requestId == mMessageId) {
                    sendActivityMessage(HANDLER_MESSAGE_MESSAGE_SUCCESS, 0, 0, event.getData());
                } else if (requestId == mTodayScheduleId) {
                    sendActivityMessage(HANDLER_MESSAGE_TODAY_SCHEDULE_SUCCESS, 0, 0, event.getData());
                } else if (requestId == mMonthActivityByMId) {
                    sendActivityMessage(HANDLER_MESSAGE_MONTH_LIST_BY_M_SUCCESS, 0, 0, event.getData());
                } else if (requestId == mExplainId1 || requestId == mExplainId2 || requestId == mExplainId3 || requestId == mExplainId4 ||
                        requestId == mExplainId5 || requestId == mExplainId6 || requestId == mExplainId7) {
                    sendActivityMessage(HANDLER_MESSAGE_EXPLAIN_SUCCESS, 0, 0, event.getData());
                } else {
                    try {
                        String data = (String) event.getData();
                        if ("AboutUS".equals(data)) {
                            sendActivityMessage(HANDLER_MESSAGE_ABOUTUS_SUCCESS, 0, 0, event.getData());
                        } else if ("Education".equals(data)) {
                            sendActivityMessage(HANDLER_MESSAGE_EDUCATIONID_SUCCESS, 0, 0, event.getData());
                        }
                    } catch (ClassCastException e) {
                    }
                }

                break;
            case END:
                //dismissLoading();
                break;

            case ERROR:
                if (requestId == mBabyCarmeraDataId) {
                    sendActivityMessage(HANDLER_MESSAGE_BABY_CARMERA_DATA_FAIL, 0, 0, event.getData());
                }

                break;
            case TIMEOUT:

                break;
        }
    }

    public void tokenFailProcess(Object obj) {

        Fragment hfragment = getSupportFragmentManager().findFragmentById(R.id.content_layout);
        try {
            RequestError error = (RequestError) obj;
            if (error != null && error.getErrorCode() != null && error.getErrorCode().equals("602")) {
                if (hfragment instanceof BOBLoginFragment) {
                    return;
                }
                showDialog(getString(R.string.token_fail), true);
                goLoginPage();
            }
        } catch (ClassCastException e) {

        }
    }

    /**
     * Show Message Dialog
     *
     * @param msg
     * @param cancelable
     */
    public void showDialog(String msg, boolean cancelable) {
        if (mShowDialog != null && mShowDialog.isShowing())
            return;

        mShowDialog = new CustomAlertDialogBuilder(MainActivity.this, R.style.customAlertDialog, msg, null);
        mShowDialog.setCancelable(cancelable);
        mShowDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mShowDialog = null;
            }
        });
        mShowDialog.show();
    }

    public void showErrorAccountDialog(boolean cancelable) {
        if (mShowDialog != null && mShowDialog.isShowing())
            return;

        mShowDialog = new CustomAlertDialogBuilder(MainActivity.this, R.style.customAlertDialog, R.layout.custom_alert_account);
        mShowDialog.setCancelable(cancelable);
        mShowDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mShowDialog = null;
            }
        });
        mShowDialog.show();
    }

    private void sendActivityMessage(int what, int arg1, int arg2, Object obj) {
        Message msg = mHandler.obtainMessage();
        msg.what = what;
        msg.arg1 = arg1;
        msg.arg2 = arg2;
        msg.obj = obj;

        mHandler.sendMessage(msg);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logout:
                logout();
                break;
            case R.id.extend_btn_imgV:
                closeVideoView();
                break;
            case R.id.title_modified_password:
                popupResetPassword();
                break;
        }
    }

    public void addFragment(Fragment fragment, int... args) {
        if (fragment == null)
            return;

        String newClassName = fragment.getClass().getSimpleName();

        mAddContent = getSupportFragmentManager().findFragmentByTag(ADD_CONTENT_TAG);
        String beforeClassName = "";
        if (mAddContent != null)
            beforeClassName = mAddContent.getClass().getSimpleName();

        if (newClassName.equals(beforeClassName)) {
            //此目前Fragment與要add的Fragment一樣, 所以先pop原有的
            return;
        }

        mAddContent = fragment;

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        switch (args.length) {
            case 2:
                ft.setCustomAnimations(args[0], args[1]);
                break;
            case 4:
                ft.setCustomAnimations(args[0], args[1], args[2], args[3]);
                break;
        }
        ft.add(R.id.content_layout, fragment, ADD_CONTENT_TAG);
        ft.addToBackStack(null);

        ft.commitAllowingStateLoss();
        if (!fragment.isAdded()) {
            getSupportFragmentManager().executePendingTransactions();
        }
    }

    @Override
    public Activity getActivity() {
        return this;
    }


    public interface VideoListener {
        public void onVideoClose();
    }

    private IVLCVout vOut;
    private String videopath;
    private VideoListener mVideoListener;

    public void extendVideoView(String videopath, Rect location, VideoListener l) {
        this.mVideoListener = l;
        this.videopath = videopath;
        this.vOut = VideoLogic.getInstance().getVLCVout(videopath);

//        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)videoView.getLayoutParams();
//        params.width = location.width();
//        params.height = location.height();
//        params.leftMargin = location.left;
//        params.topMargin = location.top;
//        videoView.setLayoutParams(params);
//
//        float ratioX = video_layout.getWidth() / (location.width()*1.0f);
//        float ratioY = video_layout.getHeight() / (location.height()*1.0f);
//
        hideSystemUI();
//
//        Animation scaleAnim = new ScaleAnimation(1f, ratioX, 1f, ratioY, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        scaleAnim.setDuration(500);
//        scaleAnim.setFillAfter(true);
//        videoView.startAnimation(scaleAnim);

        video_layout.setVisibility(View.VISIBLE);

        if (mSurface != null && vOut != null) {
            //VideoLogic.getInstance().getMediaPlayer(videopath).pause();
            if (vOut.areViewsAttached()) {
                vOut.detachViews();
            }
            vOut.setVideoSurface(mSurface);
            vOut.attachViews();
        }
    }

    public void videoReload() {
        Log.e("Zack", "videoReload");
        this.vOut = VideoLogic.getInstance().getVLCVout(videopath);
        reAttachView();
    }

    public void closeVideoView() {
        extend_btn.setEnabled(false);
//        try{
//            Animation scaleAnim = new ScaleAnimation(1f, 0f, 1f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//            scaleAnim.setDuration(500);
//            video_layout.startAnimation(scaleAnim);
//
        video_layout.setVisibility(View.INVISIBLE);
        if (mVideoListener != null) {
            mVideoListener.onVideoClose();
        }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        extend_btn.setEnabled(true);

        showSystemUI();
    }

    private void initVideo() {
        videoView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                mSurface = surface;
                if (mSurface != null && vOut != null) {
                    if (vOut.areViewsAttached()) {
                        vOut.detachViews();
                    }
                    vOut.setVideoSurface(mSurface);
                    vOut.attachViews();
                }
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
            }
        });
    }

    private void reAttachView() {
        try {
            if (mSurface != null && vOut != null) {
                //VideoLogic.getInstance().getMediaPlayer(videopath).pause();
                if (vOut.areViewsAttached()) {
                    vOut.detachViews();
                }
                vOut.setVideoSurface(mSurface);
                vOut.attachViews();

//                videoView.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        try{
//                            VideoLogic.getInstance().getMediaPlayer(videopath).play();
//                        }catch (Exception e){
//                            e.printStackTrace();
//                        }
//                    }
//                }, 2000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void releaseVideo() {
//        try {
//            if (mService != null) {
//                mService.releaseVideo();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }


    //Chromecast --Start
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_actions, menu);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && mediaRouteSelector != null) {
            MenuItem mediaRouteMenuItem = menu.findItem(R.id.action_cast);
            if (MenuItemCompat.getActionProvider(
                    mediaRouteMenuItem) instanceof MediaRouteActionProvider) {
                MediaRouteActionProvider mediaRouteActionProvider =
                        (MediaRouteActionProvider) MenuItemCompat.getActionProvider(mediaRouteMenuItem);
                mediaRouteActionProvider.setRouteSelector(mediaRouteSelector);
            }
        }
        return true;
    }

    private void setupMediaRouter() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            mediaRouter = MediaRouter.getInstance(getApplicationContext());
//            mediaRouteSelector = new MediaRouteSelector.Builder().addControlCategory(
//                    CastMediaControlIntent.categoryForCast(getString(R.string.app_cast_id))).build();
//            if (isRemoteDisplaying()) {
//                this.castDevice = CastDevice.getFromBundle(mediaRouter.getSelectedRoute().getExtras());
//            } else {
//                Bundle extras = getIntent().getExtras();
//                if (extras != null) {
//                    castDevice = extras.getParcelable(INTENT_EXTRA_CAST_DEVICE);
//                }
//            }
//            mediaRouter.addCallback(mediaRouteSelector, mMediaRouterCallback, MediaRouter.CALLBACK_FLAG_REQUEST_DISCOVERY);
//        }
    }

    private boolean isRemoteDisplaying() {
        return false;
    }

//    private final MediaRouter.Callback mMediaRouterCallback = new MediaRouter.Callback() {
//        @Override
//        public void onRouteSelected(MediaRouter router, MediaRouter.RouteInfo info) {
//
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//
//            castDevice = CastDevice.getFromBundle(info.getExtras());
//            Toast.makeText(getApplicationContext(), getString(R.string.cast_connected_to) + info.getName(), Toast.LENGTH_LONG).show();
//            startCastService(castDevice);
//            //VideoLogic.getInstance().pause(videopath);
//
//            videoView.post(new Runnable() {
//                @Override
//                public void run() {
//                    videoView.setVisibility(View.INVISIBLE);
//                }
//            });
//        }
//
//        @Override
//        public void onRouteUnselected(MediaRouter router, MediaRouter.RouteInfo info) {
//            if (isRemoteDisplaying()) {
//                CastRemoteDisplayLocalService.stopService();
//            }
//            reAttachView();
//            castDevice = null;
//
//            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//            videoView.post(new Runnable() {
//                @Override
//                public void run() {
//                    videoView.setVisibility(View.VISIBLE);
//                }
//            });
//
//            //VideoLogic.getInstance().play(videopath);
//        }
//    };

    private PresentationVideoService mService;

//    private void startCastService(CastDevice castDevice) {
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        PendingIntent notificationPendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
//
//        CastRemoteDisplayLocalService.NotificationSettings settings =
//                new CastRemoteDisplayLocalService.NotificationSettings.Builder().setNotificationPendingIntent(
//                        notificationPendingIntent).build();
//
//        CastRemoteDisplayLocalService.startService(this, PresentationVideoService.class,
//                getString(R.string.app_cast_id), castDevice, settings,
//                new CastRemoteDisplayLocalService.Callbacks() {
//                    @Override
//                    public void onServiceCreated(CastRemoteDisplayLocalService castRemoteDisplayLocalService) {
//                        ((PresentationVideoService) castRemoteDisplayLocalService).setVideoOut(videopath);
//                        mService = (PresentationVideoService) castRemoteDisplayLocalService;
//                    }
//
//                    @Override
//                    public void onRemoteDisplaySessionStarted(CastRemoteDisplayLocalService castRemoteDisplayLocalService) {
//
//                    }
//
//                    @Override
//                    public void onRemoteDisplaySessionError(Status status) {
//                        initError();
//                    }
//
//                    @Override
//                    public void onRemoteDisplaySessionEnded(CastRemoteDisplayLocalService castRemoteDisplayLocalService) {
//
//                    }
//
//                    @Override
//                    public void onRemoteDisplayMuteStateChanged(boolean isMuted) {
//
//                    }
//
//                });
//    }

    private void initError() {
        Toast toast = Toast.makeText(getApplicationContext(), R.string.toast_connection_error, Toast.LENGTH_SHORT);
        if (mediaRouter != null) {
            mediaRouter.selectRoute(mediaRouter.getDefaultRoute());
        }
        toast.show();
    }

    public void closeChromecast() {
        try {
            if (isRemoteDisplaying()) {
//                CastRemoteDisplayLocalService.stopService();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //Chromecast --End
    public void disableVideoItem(){
        //Menu menuNav=navigation.getMenu();
        //MenuItem media_item = menuNav.findItem(R.id.nav_media);
        //TODO
        //media_item.setEnabled(false);
    }


    private void hideSystemUI() {
        /*
        int flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            flags |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                    | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                    | View.SYSTEM_UI_FLAG_IMMERSIVE;
        }else{
            flags |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        }
        getWindow().getDecorView().setSystemUiVisibility(flags);
        getSupportActionBar().hide();
        */
    }

    private void showSystemUI() {
        /*
        int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        getWindow().getDecorView().setSystemUiVisibility(flags);

        getSupportActionBar().hide();
        */
    }

    private void showActionSystemUI() {
        /*
        int flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            flags |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                    | View.SYSTEM_UI_FLAG_IMMERSIVE;
        }else{
            flags |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
        }
        getWindow().getDecorView().setSystemUiVisibility(flags);
        getSupportActionBar().show();
        */
    }


    private int getNavigationHeight() {
        Resources resources = getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }


    protected synchronized void popupResetPassword() {

        if (dialog != null && dialog.isVisible()) {
            dialog.dismiss();
            dialog = null;
            return;
        }

        dialog = BOBDialogLogic.getInstance().showResetPasswordDialog(this, true, new DialogPickerDialogListener() {
            @Override
            public void onClickDialogPicker(DialogPickerType type, Object obj) {
                if (type == DialogPickerType.OK) {
                    String password = (String) obj;
                    postResetPassword(password);
                    dialog.dismiss();
                }
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                hideKeyboard(200);
            }
        });

        showKeyboard();
    }

    private void postResetPassword(String password) {
        CommunicationLogic.getInstance().postResetPassword(BOBSystemManager.getInstance().getUserToken(),
                password, new BOBRequestListener() {
                    @Override
                    public void onResult(final boolean success, Object object) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (success) {
                                    Toast.makeText(getActivity(), "密碼置換成功!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getActivity(), "密碼置換失敗!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
    }

    public void replaceTabFragment(Fragment fragment) {

        if (fragment == null)
            return;

        String newClassName = fragment.getClass().getSimpleName();

        mAddTabContent = getSupportFragmentManager().findFragmentByTag(ADD_TAB_TAG);
        String beforeClassName = "";
        if (mAddTabContent != null)
            beforeClassName = mAddTabContent.getClass().getSimpleName();

        if (newClassName.equals(beforeClassName)) {
            //此目前Fragment與要add的Fragment一樣, 所以先pop原有的
            return;
        }

        mAddTabContent = fragment;

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();


        ft.replace(R.id.tab_layout, fragment, ADD_TAB_TAG);
        ft.addToBackStack(null);

        ft.commitAllowingStateLoss();
        if (!fragment.isAdded()) {
            getSupportFragmentManager().executePendingTransactions();
        }
    }


    public void hideKeyboard() {
        try {
            View view = getActivity().getWindow().getDecorView();
            if (inputMethodManager != null && view != null) {
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showKeyboard() {
        if (inputMethodManager != null) {
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    }

    public void hideKeyboard(long delayMillis) {
        handler.postDelayed(new Runnable() {
            public void run() {
                hideKeyboard();
            }
        }, delayMillis);
    }

    protected void initOrientationListener() {
        try {
            mOrientationListener = new SimpleOrientationListener(this) {
                private int prevOrientation = OrientationEventListener.ORIENTATION_UNKNOWN;
                private int orientationDegree = 270;

                @Override
                public void onSimpleOrientationChanged(int orientation) {
                }

                @Override
                public void onOrientationChanged(int orientation) {
                    super.onOrientationChanged(orientation);
                    Fragment fragment = getSupportFragmentManager().findFragmentByTag(ADD_CONTENT_TAG);
                    int currentOrientation = OrientationEventListener.ORIENTATION_UNKNOWN;
                    if (orientation >= 330 || orientation < 30) {
                        currentOrientation = 0;
                    } else if (orientation >= 60 && orientation < 120) {
                        currentOrientation = 90;
                    } else if (orientation >= 150 && orientation < 210) {
                        currentOrientation = 180;
                    } else if (orientation >= 240 && orientation < 300) {
                        currentOrientation = 270;
                    }
                    Log.d("dddd","currentOrientation: "+currentOrientation +", orientation:"+orientation + ", fragment "+fragment);

                    if (prevOrientation != currentOrientation && orientation != OrientationEventListener.ORIENTATION_UNKNOWN) {
                        prevOrientation = currentOrientation;
                        if (currentOrientation != OrientationEventListener.ORIENTATION_UNKNOWN) {
                            orientationDegree = currentOrientation;

                            if (orientationDegree == 270) {
                                isPortrait = false;
                                if(tabStack.get(selectTab) instanceof BOBBabyPlayVideoFragment ) {
                                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                                } else if(fragment instanceof BOBWebViewContentFragment  || fragment instanceof ChartFragment){
                                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                                }
                            } else {
                                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                                isPortrait = true;
                                if(tabStack.get(selectTab) instanceof BOBWebViewContentFragment){
                                }
                            }
                        }
                    }
                }
            };
            mOrientationListener.enable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(LAYOUT_DID_COLLAPSE_TAG);
        filter.addAction(LAYOUT_DID_EXPAND_TAG);
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        manager.registerReceiver(localReceiver, filter);

        IntentFilter mNetworkStateFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mNetworkStateReceiver, mNetworkStateFilter);
    }

    private void removeReceiver() {
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        manager.unregisterReceiver(localReceiver);

        unregisterReceiver(mNetworkStateReceiver);
    }

    BroadcastReceiver localReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(LAYOUT_DID_EXPAND_TAG)) {
                titleLayout.setVisibility(View.GONE);
                bottomLayout.setVisibility(View.GONE);
            } else if (intent.getAction().equals(LAYOUT_DID_COLLAPSE_TAG)) {
                titleLayout.setVisibility(View.VISIBLE);
                bottomLayout.setVisibility(View.VISIBLE);
            } 
        }
    };

    private final BroadcastReceiver mNetworkStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            ConnectivityManager cm = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm == null) {
            } else {
                NetworkInfo[] infos = cm.getAllNetworkInfo();
                if (infos != null) {
                    for (NetworkInfo networkInfo : infos) {
                        if(networkInfo.getState() == NetworkInfo.State.CONNECTED){
                            Log.d("mNetworkStateReceiver", "mNetworkStateReceiver BROADCAST_CONNECTED");
                            getHomeData();
                        }
                    }
                }
            }
        }
    };
}
