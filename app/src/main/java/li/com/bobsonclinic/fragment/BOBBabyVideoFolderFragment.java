package li.com.bobsonclinic.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import li.com.bobsonclinic.R;
import li.com.bobsonclinic.adapter.BOBBabyVideoFolderAdapter;
import li.com.bobsonclinic.data.BOBBabyCamera;
import li.com.bobsonclinic.data.local.AlbumData;
import li.com.bobsonclinic.data.realm.Photo;
import li.com.bobsonclinic.fragment.base.BaseFragment;
import li.com.bobsonclinic.logic.BOBFolderLogic;
import li.com.bobsonclinic.manager.BOBDataManager;

/**
 * Created by Zack on 6/23/16.
 */
public class BOBBabyVideoFolderFragment extends BaseFragment implements
        BOBBabyVideoFolderAdapter.BabyVideoFolderAdapterListener, View.OnClickListener {

    private static final SimpleDateFormat sdf_date = new SimpleDateFormat("yyyy/MM/dd");

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
    private String mBabyVideoFolder;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_baby_album, null);
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
        init();
    }

    private void findView() {
        recyclerV = (RecyclerView)getView().findViewById(R.id.recyclerV);
        left_btn = (ImageView)getView().findViewById(R.id.left_btn);
        right_btn = (ImageView)getView().findViewById(R.id.right_btn);
    }

    private void init() {
        momId = BOBDataManager.getInstance().getMomeyData().getId();
        babyId = mCamera.getBaby_id(); //BOBDataManager.getInstance().getBabyId();
        mBabyVideoFolder = BOBFolderLogic.getInstance().getBabyVideoFolder(momId, babyId);

        GridLayoutManager lLayout = new GridLayoutManager(getActivity(), 3);
        recyclerV.setHasFixedSize(true);
        recyclerV.setLayoutManager(lLayout);

        left_btn.setOnClickListener(this);
        right_btn.setOnClickListener(this);

        refreshAdapter();
        //initAlbum();

//        ArrayList<AlbumData> data = new ArrayList<>();
//        data.add(new AlbumData("http://student.kkes.tc.edu.tw/~calc/pic/03jpg/pic/b001.jpg", System.currentTimeMillis()));
//        data.add(new AlbumData("http://student.kkes.tc.edu.tw/~calc/pic/03jpg/pic/b002.jpg", System.currentTimeMillis()));
//        data.add(new AlbumData("http://student.kkes.tc.edu.tw/~calc/pic/03jpg/pic/b003.jpg", System.currentTimeMillis()));
//        data.add(new AlbumData("http://student.kkes.tc.edu.tw/~calc/pic/03jpg/pic/b004.jpg", System.currentTimeMillis()));
//        data.add(new AlbumData("http://student.kkes.tc.edu.tw/~calc/pic/03jpg/pic/b005.jpg", System.currentTimeMillis()));
//        data.add(new AlbumData("http://student.kkes.tc.edu.tw/~calc/pic/03jpg/pic/b006.jpg", System.currentTimeMillis()));
//        data.add(new AlbumData("http://student.kkes.tc.edu.tw/~calc/pic/03jpg/pic/b007.jpg", System.currentTimeMillis()));
//        data.add(new AlbumData("http://student.kkes.tc.edu.tw/~calc/pic/03jpg/pic/b008.jpg", System.currentTimeMillis()));
//        data.add(new AlbumData("http://student.kkes.tc.edu.tw/~calc/pic/03jpg/pic/b009.jpg", System.currentTimeMillis()));
//        data.add(new AlbumData("http://student.kkes.tc.edu.tw/~calc/pic/03jpg/pic/b010.jpg", System.currentTimeMillis()));
    }

    public void setInfo(BOBBabyCamera cameraData){
        mCamera = cameraData;
    }

    private void initAlbum(){

        files = new TreeMap<>();
        keys = new ArrayList<>();

        File albumPath = new File(mBabyVideoFolder);

        File[] albumFiles = albumPath.listFiles();

        ArrayList<AlbumData> list;
        for(int i=0; i<albumFiles.length; i++){
            File file = albumFiles[i];

            if(file.isDirectory()){
                continue;
            }

            String fileNameWithOutExt = file.getName().replaceFirst("[.][^.]+$", "");

            AlbumData album = new AlbumData(file.getAbsolutePath(), Long.valueOf(fileNameWithOutExt));
            String dateStr = sdf_date.format(album.time);

            if(files.containsKey(dateStr)){
                list = files.get(dateStr);
                list.add(album);
            }else{
                list = new ArrayList<>();
                list.add(album);
                files.put(dateStr, list);
            }
        }

        Iterator<String> its = files.keySet().iterator();
        while(its.hasNext()){
            keys.add(its.next());
        }

//        for(int i=0; i<keys.size(); i++){
//            String key = keys.get(i);
//            Log.e("BOB", "key="+key+":"+new Gson().toJson(files.get(key)));
//        }

        refreshAdapter();
    }

    private void refreshAdapter(){
        List<Photo> photos = Photo.fetchAllPhotos();
        BOBBabyVideoFolderAdapter adapter = new BOBBabyVideoFolderAdapter(getContext(), photos, this);
        recyclerV.setAdapter(adapter);
    }

    @Override
    public void onClickAdapterItem(PressType type, String path) {
        if(type == PressType.VIDEO){
            Intent i = new Intent();
            i.setAction(Intent.ACTION_VIEW);
            i.setDataAndType(Uri.parse("file://"+path), "video/mp4");
            startActivity(i);
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
}
