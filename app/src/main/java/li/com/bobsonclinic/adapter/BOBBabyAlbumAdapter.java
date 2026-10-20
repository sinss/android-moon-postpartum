package li.com.bobsonclinic.adapter;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import li.com.bobsonclinic.R;
import li.com.bobsonclinic.data.realm.Photo;
import li.com.bobsonclinic.util.Picasso;

/**
 * Created by Zack on 6/23/16.
 */
public class BOBBabyAlbumAdapter extends RecyclerView.Adapter<BOBBabyAlbumAdapter.BabyAlbumHolder> {

    private static final SimpleDateFormat sdf_date = new SimpleDateFormat("yyyy/MM/dd");

    public interface BOBBabyAlbumAdapterListener{
        enum PressType{IMAGE};
        void onClickAdapterItem(PressType type, String path);
    }

    private class HandleOnClick implements View.OnClickListener{

        private int position;
        private BOBBabyAlbumAdapterListener.PressType type;

        public HandleOnClick(int position, BOBBabyAlbumAdapterListener.PressType type){
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

    protected BOBBabyAlbumAdapterListener mListener;
    protected List<Photo> mList;

    public BOBBabyAlbumAdapter(List<Photo> list, BOBBabyAlbumAdapterListener l){
        this.mList = list;
        this.mListener = l;
        if(list == null){
            mList = new ArrayList<>();
        }
    }

    public class BabyAlbumHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;
        private TextView date_txtV;
        public BabyAlbumHolder(View view) {
            super(view);
            mImageView = (ImageView) view.findViewById(R.id.imageView);
            date_txtV = (TextView) view.findViewById(R.id.date_txtV);
        }
    }

    @Override
    public BOBBabyAlbumAdapter.BabyAlbumHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BabyAlbumHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.ui_item_baby_album, parent, false));
    }

    @Override
    public void onBindViewHolder(BabyAlbumHolder holder, int position) {
        Photo data = mList.get(position);
        String path = data.getPhotoPath();
        holder.date_txtV.setText(sdf_date.format(data.getCreatedAt()));
        holder.mImageView.setOnClickListener(new HandleOnClick(position, BOBBabyAlbumAdapterListener.PressType.IMAGE));
        Picasso.getInstance().load(path).fit().centerCrop().into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
