package li.com.bobsonclinic.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Request;
import com.squareup.picasso.RequestHandler;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import li.com.bobsonclinic.R;
import li.com.bobsonclinic.data.realm.Photo;

/**
 * Created by Zack on 6/23/16.
 */
public class BOBBabyVideoFolderAdapter extends RecyclerView.Adapter<BOBBabyVideoFolderAdapter.BabyRecordVideoHolder> {

    private static final SimpleDateFormat sdf_date = new SimpleDateFormat("yyyy/MM/dd");

    public interface BabyVideoFolderAdapterListener {
        enum PressType{VIDEO};
        void onClickAdapterItem(PressType type, String path);
    }

    private class HandleOnClick implements View.OnClickListener{

        private int position;
        private BabyVideoFolderAdapterListener.PressType type;

        public HandleOnClick(int position, BabyVideoFolderAdapterListener.PressType type){
            this.position = position;
            this.type = type;
        }

        @Override
        public void onClick(View v) {
            if(mListener == null){
                return;
            }

            mListener.onClickAdapterItem(type, mList.get(position).getPhotoPath());
        }
    }

    public class VideoRequestHandler extends RequestHandler {

        public String SCHEME_VIEDEO = "video";

        @Override
        public boolean canHandleRequest(Request data) {
            String scheme = data.uri.getScheme();
            return (SCHEME_VIEDEO.equals(scheme));
        }

        @Override
        public Result load(Request data, int arg1) throws IOException {
            Bitmap bm = ThumbnailUtils.createVideoThumbnail(data.uri.getPath(), MediaStore.Images.Thumbnails.MINI_KIND);
            return new Result(bm, com.squareup.picasso.Picasso.LoadedFrom.DISK);
        }
    }

    protected BabyVideoFolderAdapterListener mListener;
    protected List<Photo> mList;

    protected com.squareup.picasso.Picasso picassoInstance;
    protected VideoRequestHandler videoRequestHandler;

    public BOBBabyVideoFolderAdapter(Context context, List<Photo> list, BabyVideoFolderAdapterListener l){
        this.mList = list;
        this.mListener = l;
        if(list == null){
            mList = new ArrayList<>();
        }

        videoRequestHandler = new VideoRequestHandler();
        picassoInstance = new com.squareup.picasso.Picasso.Builder(context).addRequestHandler(videoRequestHandler).build();
    }

    public class BabyRecordVideoHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;
        private TextView date_txtV;
        public BabyRecordVideoHolder(View view) {
            super(view);
            mImageView = (ImageView) view.findViewById(R.id.imageView);
            date_txtV = (TextView) view.findViewById(R.id.date_txtV);
        }
    }

    @Override
    public BOBBabyVideoFolderAdapter.BabyRecordVideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BabyRecordVideoHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.ui_item_baby_album, parent, false));
    }

    @Override
    public void onBindViewHolder(BabyRecordVideoHolder holder, int position) {
        Photo data = mList.get(position);

        String path = data.getPhotoPath();
        holder.mImageView.setImageBitmap(null);
        picassoInstance.load(videoRequestHandler.SCHEME_VIEDEO+":"+path).fit().into(holder.mImageView);

        holder.date_txtV.setText(sdf_date.format(data.getCreatedAt()));
        holder.mImageView.setOnClickListener(new HandleOnClick(position, BabyVideoFolderAdapterListener.PressType.VIDEO));
        //Picasso.getInstance().load(path).fit().into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
