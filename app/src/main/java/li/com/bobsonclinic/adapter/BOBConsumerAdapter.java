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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import li.com.bobsonclinic.R;
import li.com.bobsonclinic.data.BOBConsumerList;


public class BOBConsumerAdapter extends RecyclerView.Adapter<BOBConsumerAdapter.ViewHolder> {

    private final String TAG = BOBConsumerAdapter.class.getSimpleName();
    private Context mContext;
    private LayoutInflater myInflater;
    public static final int IS_NORMAL = 1;
    private List<BOBConsumerList> mList;
    private FunctionMenuListener mListener;

    public BOBConsumerAdapter(final Context context) {
        super();
        this.mContext = context;
        this.myInflater = LayoutInflater.from(context);
    }

    public void setList(List<BOBConsumerList> mMenuList) {
        this.mList = mMenuList;
        this.notifyDataSetChanged();
    }

    public void setMenuListener(FunctionMenuListener listener) {
        this.mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = this.myInflater.inflate(R.layout.fragment_consumer_list, parent, false);
        return new ViewHolder(view, IS_NORMAL);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if(mList.get(position).getSchedule_time() != null && !mList.get(position).getSchedule_time().equals(""))
        {
            ((ViewHolder) holder).date.setText(mList.get(position).getSchedule_time());
        } else  {
            ((ViewHolder) holder).date.setText(mList.get(position).getVas_time());
        }

        ((ViewHolder) holder).item.setText(mList.get(position).getTitle());
        ((ViewHolder) holder).fee.setText(mList.get(position).getFee());
        ((ViewHolder) holder).status.setText("");
        ((ViewHolder) holder).line.setVisibility(View.GONE);
        if(mList.get(position).getStatus().equals("bill")){
            ((ViewHolder) holder).status.setText("未結清");
            ((ViewHolder) holder).status.setTextColor(mContext.getResources().getColor(R.color.white));
            ((ViewHolder) holder).status.setBackgroundResource(R.drawable.conumer_button_bg);
        } else if(mList.get(position).getStatus().equals("deleted")){
            ((ViewHolder) holder).status.setText("已刪除");
            ((ViewHolder) holder).line.setVisibility(View.VISIBLE);
            ((ViewHolder) holder).status.setTextColor(mContext.getResources().getColor(R.color.text_dark));
            ((ViewHolder) holder).status.setBackgroundResource(R.drawable.conumer_button_bg_white);
        } else if (mList.get(position).getStatus().equals("confirmed")){
            ((ViewHolder) holder).status.setText("已確認");
            ((ViewHolder) holder).status.setTextColor(mContext.getResources().getColor(R.color.text_dark));
            ((ViewHolder) holder).status.setBackgroundResource(R.drawable.conumer_grey_button_bg);
        } else if (mList.get(position).getStatus().equals("paid")){
            ((ViewHolder) holder).status.setText("已結清");
            ((ViewHolder) holder).status.setTextColor(mContext.getResources().getColor(R.color.text_dark));
            ((ViewHolder) holder).status.setBackgroundResource(R.drawable.conumer_grey_button_bg);
        }
        Log.d(TAG, "getStatus: " + mList.get(position).getStatus());
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }




    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView date;
        TextView item;
        TextView fee;
        TextView status;
        ImageView line;
        View rootView;

        public int viewType;

        public ViewHolder(final View itemView, int viewtype) {
            super(itemView);
            viewType = viewtype;
            rootView = itemView;
            itemView.setTag(itemView);
            date = (TextView) itemView.findViewById(R.id.consumer_date);
            item = (TextView) itemView.findViewById(R.id.consumer_item);
            fee = (TextView) itemView.findViewById(R.id.consumer_fee);
            status = (TextView) itemView.findViewById(R.id.consumer_status);
            line = (ImageView) itemView.findViewById(R.id.delete_line);
        }
    }

    public interface FunctionMenuListener{
        public void onClick(Fragment fragment);
    }

}