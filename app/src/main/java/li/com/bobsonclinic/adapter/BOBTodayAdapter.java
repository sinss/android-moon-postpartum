package li.com.bobsonclinic.adapter;
/**
 * Agricola
 * <p/>
 * Created by Milla.Wang on 2016/1/5.
 * Copyright © 2016 bobsonclinic. All rights reserved.
 */

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import li.com.bobsonclinic.R;
import li.com.bobsonclinic.data.BOBTodaySchedule;


public class BOBTodayAdapter extends RecyclerView.Adapter<BOBTodayAdapter.ViewHolder> {

    private final String TAG = BOBTodayAdapter.class.getSimpleName();
    private Context mContext;
    private LayoutInflater myInflater;
    public static final int IS_NORMAL = 1;
    private List<BOBTodaySchedule> mList;
    private FunctionListener mListener;

    public BOBTodayAdapter(final Context context) {
        super();
        this.mContext = context;
        this.myInflater = LayoutInflater.from(context);
    }

    public void setList(List<BOBTodaySchedule> mMenuList) {
        this.mList = mMenuList;
        this.notifyDataSetChanged();
    }

    public void setListener(FunctionListener listener) {
        this.mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = this.myInflater.inflate(R.layout.fragment_message_list, parent, false);
        return new ViewHolder(view, IS_NORMAL);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,final int position) {

        ((ViewHolder) holder).date.setText(mList.get(position).getSchedule_time());
        ((ViewHolder) holder).message.setText(mList.get(position).getTitle());
        ((ViewHolder) holder).rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onClick(mList.get(position).getActivity_id(), mList.get(position).getVas_id());
                }
            }
        });
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

    public interface FunctionListener{
        public void onClick(String activityId, String vasId);
    }

}