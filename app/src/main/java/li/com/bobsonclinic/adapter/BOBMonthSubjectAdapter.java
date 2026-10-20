package li.com.bobsonclinic.adapter;
/**
 * Agricola
 * <p/>
 * Created by Milla.Wang on 2016/1/5.
 * Copyright © 2016 bobsonclinic. All rights reserved.
 */

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import li.com.bobsonclinic.R;
import li.com.bobsonclinic.data.BOBMonthActivity;
import li.com.bobsonclinic.request.base.BOBAPI;
import li.com.bobsonclinic.util.Picasso;


public class BOBMonthSubjectAdapter extends RecyclerView.Adapter<BOBMonthSubjectAdapter.ViewHolder> {

    private final String TAG = BOBMonthSubjectAdapter.class.getSimpleName();
    private Context mContext;
    private LayoutInflater myInflater;
    public static final int IS_NORMAL = 1;
    private List<BOBMonthActivity> mList;
    private classFunctioListener mListener;

    public BOBMonthSubjectAdapter(final Context context) {
        super();
        this.mContext = context;
        this.myInflater = LayoutInflater.from(context);
    }

    public void setList(List<BOBMonthActivity> mMenuList) {
        this.mList = mMenuList;
        this.notifyDataSetChanged();
    }

    public void setListener(classFunctioListener listener) {
        this.mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = this.myInflater.inflate(R.layout.fragment_month_subject_list, parent, false);
        return new ViewHolder(view, IS_NORMAL);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,final int position) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //  利用 DateFormat 來parse 日期的字串
        SimpleDateFormat dt = new SimpleDateFormat("yyyyy-MM-dd HH:mm:ss");
        Date date1 = null;
        Date date2 = null;
        final BOBMonthActivity item = mList.get(position);
        try {
            date1 = dt.parse(item.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(date1 != null)  ((ViewHolder) holder).time1.setText(sdf.format(date1));
        String imageName = item.getImg();
        holder.cover1.setVisibility(View.VISIBLE);
        if (imageName == null || imageName.isEmpty()) {
            //holder.cover1.setVisibility(View.GONE);
        } else {
            Picasso.getInstance().load(BOBAPI.getAuthDomainURL() + item.getImg()).fit().centerCrop().placeholder(R.drawable.placeholder_c).into(((ViewHolder) holder).cover1);
        }
        Log.d("dd","image: "+BOBAPI.getAuthDomainURL() + item.getImg());

        ((ViewHolder) holder).title1.setText(item.getTitle());
        ((ViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onClick(item.getId());
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView cover1;
        TextView time1;
        TextView title1;
        RelativeLayout layout1;
        View rootView;

        public int viewType;

        public ViewHolder(final View itemView, int viewtype) {
            super(itemView);
            viewType = viewtype;
            rootView = itemView;
            itemView.setTag(itemView);
            cover1 = (ImageView) itemView.findViewById(R.id.cover);
            time1 = (TextView) itemView.findViewById(R.id.time);
            title1 = (TextView) itemView.findViewById(R.id.title);
            //layout1 = (RelativeLayout) itemView.findViewById(R.id.layoyt1);
        }
    }

    public interface classFunctioListener {
        public void onClick(String id);
    }

}