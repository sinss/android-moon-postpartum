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
import li.com.bobsonclinic.data.BOBMomTemPulPre;


public class BOBMomTemPulPreAdapter extends RecyclerView.Adapter<BOBMomTemPulPreAdapter.ViewHolder> {

    private final String TAG = BOBMomTemPulPreAdapter.class.getSimpleName();
    private Context mContext;
    private LayoutInflater myInflater;
    public static final int IS_NORMAL = 1;
    private List<BOBMomTemPulPre> mList;
    private classFunctioListener mListener;

    public BOBMomTemPulPreAdapter(final Context context) {
        super();
        this.mContext = context;
        this.myInflater = LayoutInflater.from(context);
    }

    public void setList(List<BOBMomTemPulPre> mMenuList) {
        this.mList = mMenuList;
        this.notifyDataSetChanged();
    }

    public void setListener(classFunctioListener listener) {
        this.mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = this.myInflater.inflate(R.layout.fragment_mom_tempterure_blood_list, parent, false);
        return new ViewHolder(view, IS_NORMAL);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,final int position) {
        ((ViewHolder) holder).date.setText(mList.get(position).getExam_date());
        ((ViewHolder) holder).pulse.setText(mList.get(position).getPulse());
        ((ViewHolder) holder).pressure.setText(mList.get(position).getSystolic_pressure()+"/"+mList.get(position).getDiastolic_pressure());
        ((ViewHolder) holder).temperture.setText(mList.get(position).getTemperature());
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView date;
        TextView temperture;
        TextView pulse;
        TextView pressure;
        View rootView;

        public int viewType;

        public ViewHolder(final View itemView, int viewtype) {
            super(itemView);
            viewType = viewtype;
            rootView = itemView;
            itemView.setTag(itemView);
            date = (TextView) itemView.findViewById(R.id.date);
            temperture = (TextView) itemView.findViewById(R.id.tempterure);
            pulse = (TextView) itemView.findViewById(R.id.pulse);
            pressure = (TextView) itemView.findViewById(R.id.pressure);
        }
    }

    public interface classFunctioListener {
        public void onClick(String id);
    }

}