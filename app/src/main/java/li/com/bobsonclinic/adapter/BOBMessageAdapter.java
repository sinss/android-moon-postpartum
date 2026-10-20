package li.com.bobsonclinic.adapter;
/**
 * Agricola
 * <p/>
 * Created by Milla.Wang on 2016/1/5.
 * Copyright © 2016 bobsonclinic. All rights reserved.
 */

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import li.com.bobsonclinic.R;
import li.com.bobsonclinic.data.BOBNotifaction;


public class BOBMessageAdapter extends RecyclerView.Adapter<BOBMessageAdapter.ViewHolder> {

    private final String TAG = BOBMessageAdapter.class.getSimpleName();
    private Context mContext;
    private LayoutInflater myInflater;
    public static final int IS_NORMAL = 1;
    private List<BOBNotifaction> mList;
    private FunctionMenuListener mListener;

    public BOBMessageAdapter(final Context context) {
        super();
        this.mContext = context;
        this.myInflater = LayoutInflater.from(context);
    }

    public void setList(List<BOBNotifaction> mMenuList) {
        this.mList = mMenuList;
        this.notifyDataSetChanged();
    }

    public void setMenuListener(FunctionMenuListener listener) {
        this.mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = this.myInflater.inflate(R.layout.fragment_message_list, parent, false);
        return new ViewHolder(view, IS_NORMAL);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ((ViewHolder) holder).date.setText(mList.get(position).getPush_at());
        ((ViewHolder) holder).message.setText(mList.get(position).getMessage());
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }




    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView date;
        TextView message;
        View rootView;

        public int viewType;

        public ViewHolder(final View itemView, int viewtype) {
            super(itemView);
            viewType = viewtype;
            rootView = itemView;
            itemView.setTag(itemView);
            date = (TextView) itemView.findViewById(R.id.date);
            message = (TextView) itemView.findViewById(R.id.message);
        }
    }

    public interface FunctionMenuListener{
        public void onClick(Fragment fragment);
    }

}