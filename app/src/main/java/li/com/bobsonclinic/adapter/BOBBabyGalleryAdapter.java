package li.com.bobsonclinic.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import li.com.bobsonclinic.data.BOBGalleryItem;

/**
 * Created by leo on 6/23/16.
 */
public class BOBBabyGalleryAdapter extends RecyclerView.Adapter<BOBBabyGalleryAdapter.BOBBabyGalleryViewHolder> {

    private List<BOBGalleryItem> items = new ArrayList<BOBGalleryItem>();

    @Override
    public BOBBabyGalleryAdapter.BOBBabyGalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(BOBBabyGalleryAdapter.BOBBabyGalleryViewHolder holder, int position) {
        BOBGalleryItem item = items.get(position);

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    protected class BOBBabyGalleryViewHolder extends RecyclerView.ViewHolder {
        TextView date;
        public BOBBabyGalleryViewHolder(View itemView) {
            super(itemView);

        }
    }

    public void refreshData(List<BOBGalleryItem> items) {
        items.clear();
        items.addAll(items);
        notifyDataSetChanged();
    }
}
