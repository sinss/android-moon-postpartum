package li.com.bobsonclinic.fragment;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import li.com.bobsonclinic.R;
import li.com.bobsonclinic.activity.PhotoViewActivity;
import li.com.bobsonclinic.adapter.BOBBabyAlbumAdapter;
import li.com.bobsonclinic.data.BOBBabyCamera;
import li.com.bobsonclinic.data.local.AlbumData;
import li.com.bobsonclinic.data.realm.Photo;
import li.com.bobsonclinic.fragment.base.BaseFragment;
import li.com.bobsonclinic.logic.BOBFolderLogic;
import li.com.bobsonclinic.manager.BOBDataManager;
import li.com.bobsonclinic.provider.BOBProvider;
import li.com.bobsonclinic.util.Config;

import static li.com.bobsonclinic.activity.PhotoViewActivity.TAG_PHOTO_PATH;

/**
 * Created by Zack on 6/23/16.
 */
public class BOBBabyAlbumFragment extends BaseFragment implements BOBBabyAlbumAdapter.BOBBabyAlbumAdapterListener, View.OnClickListener {

    private static final SimpleDateFormat sdf_date = new SimpleDateFormat("yyyy/MM/dd");
    private final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 1;
    private ViewGroup rootView;
    private RecyclerView recyclerV;
    private ImageView left_btn;
    private ImageView right_btn;

    private TreeMap<String, ArrayList<AlbumData>> files;
    private ArrayList<String> keys;
    private int index = 0;

    private BOBBabyCamera mCamera;
    private String momId = "";
    private String babyId = "";
    private String mBabyAlbumFolder;

    protected ContentObserver infoObserver;
    protected Handler mHandler;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_baby_album, null);
        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        checkPermission();
        mHandler = new Handler();
        infoObserver = new ContentObserver(mHandler) {
            @Override
            public void onChange(boolean selfChange)
            {
                super.onChange(selfChange);
                //setData();
            }
        };

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        findView();
        init();

    }

    private void findView() {
        recyclerV = (RecyclerView)getView().findViewById(R.id.recyclerV);
        left_btn = (ImageView)getView().findViewById(R.id.left_btn);
        right_btn = (ImageView)getView().findViewById(R.id.right_btn);
    }

    private void init() {
        momId = BOBDataManager.getInstance().getMomeyData().getId();
        babyId = BOBDataManager.getInstance().getBabyId();
        mBabyAlbumFolder = BOBFolderLogic.getInstance().getBabyPhotoFolder(momId, babyId);
        GridLayoutManager lLayout = new GridLayoutManager(getActivity(), 3);
        recyclerV.setHasFixedSize(true);
        recyclerV.setLayoutManager(lLayout);

        left_btn.setOnClickListener(this);
        right_btn.setOnClickListener(this);
        refreshAdapter();
    }

    public void setInfo(BOBBabyCamera cameraData){
        mCamera = cameraData;
    }

    public void refreshAdapter(){

        if(babyId != null && BOBDataManager.getInstance().getBabyId() != null && !babyId.equals( BOBDataManager.getInstance().getBabyId())){
            momId = BOBDataManager.getInstance().getMomeyData().getId();
            babyId = BOBDataManager.getInstance().getBabyId();
            mBabyAlbumFolder = BOBFolderLogic.getInstance().getBabyPhotoFolder(momId, babyId);
        }

        if(recyclerV != null) {
            List<Photo> photos = Photo.fetchAllPhotos();
            BOBBabyAlbumAdapter adapter = new BOBBabyAlbumAdapter(photos, this);
            recyclerV.setAdapter(adapter);
        }
    }


    @Override
    public void onClickAdapterItem(PressType type, String path) {
        if(type == PressType.IMAGE){
            Intent intent = new Intent();
//            intent.setAction(Intent.ACTION_VIEW);
//            File cameraPhoto = new File(path);
//            //
//            Uri photoUri = FileProvider.getUriForFile(
//                    getContext(),
//                    BuildConfig.APPLICATION_ID + ".provider",
//                    cameraPhoto);
//            //Uri.parse(path)
//            intent.setDataAndType(photoUri, "image/*");
            //startActivity(intent);

            Bundle bundle = new Bundle();
            bundle.putString(TAG_PHOTO_PATH, path);
            intent.setClass(getContext(), PhotoViewActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }


    @Override
    public void onClick(View v) {
        int i = index;
        if(v == left_btn){
            i--;
            if(i < 0){
                i = 0;
            }
        }else if(v == right_btn){
            i++;
            if(i >= keys.size()-1){
                i = keys.size()-1;
            }
        }
        if(i != index){
            refreshAdapter();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setupReceiver();

        getActivity().getContentResolver().registerContentObserver(BOBProvider.URI_Camera, true, infoObserver);
    }

    @Override
    public void onPause() {
        super.onPause();
        getContext().unregisterReceiver(receiver);
        getActivity().getContentResolver().unregisterContentObserver(infoObserver);
    }

    public void setupReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Config.BROADCAST_VIDEO);
        getActivity().registerReceiver(receiver, filter);
    }

    protected boolean checkPermission() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
            return true;
        }

        boolean albumAgreed = (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);

        if (!albumAgreed) {
            askPermissions(Manifest.permission.READ_EXTERNAL_STORAGE);
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
                ActivityCompat.requestPermissions(getActivity(), permission, READ_EXTERNAL_STORAGE_REQUEST_CODE);
            }
        }
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(Config.BROADCAST_VIDEO)) {
                refreshAdapter();
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case READ_EXTERNAL_STORAGE_REQUEST_CODE: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    refreshAdapter();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
