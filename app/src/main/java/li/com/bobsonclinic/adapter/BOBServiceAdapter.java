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
import android.widget.TextView;

import java.util.List;

import li.com.bobsonclinic.BOBApplication;
import li.com.bobsonclinic.R;
import li.com.bobsonclinic.data.BOBVasList;
import li.com.bobsonclinic.request.base.BOBAPI;
import li.com.bobsonclinic.util.Picasso;


public class BOBServiceAdapter extends RecyclerView.Adapter<BOBServiceAdapter.ViewHolder> {

    private final String TAG = BOBServiceAdapter.class.getSimpleName();
    private Context mContext;
    private LayoutInflater myInflater;
    public static final int IS_NORMAL = 1;
    private List<BOBVasList> mList;
    private vasFunctioListener mListener;

    public BOBServiceAdapter(final Context context) {
        super();
        this.mContext = context;
        this.myInflater = LayoutInflater.from(context);
    }

    public void setList(List<BOBVasList> mMenuList) {
        this.mList = mMenuList;
        this.notifyDataSetChanged();
    }

    public void setListener(vasFunctioListener listener) {
        this.mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = this.myInflater.inflate(R.layout.fragment_add_service_list, parent, false);
        return new ViewHolder(view, IS_NORMAL);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,final int position) {

        ((ViewHolder) holder).title1.setText(mList.get(position).getTitle());
        Picasso.getInstance().load(BOBAPI.getAuthDomainURL() + mList.get(position).getImg()).fit().centerCrop().placeholder(R.drawable.placeholder_c).into(((ViewHolder) holder).cover1);
        Log.d("Milla", "Image: "+ BOBAPI.getAuthDomainURL() + mList.get(position).getImg());
        ((ViewHolder) holder).fee1.setText(mList.get(position).getFee());
        ((ViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onClick(mList.get(position).getId());
                }
            }
        });

        if (mList.get(position).getFee().equals("0")) {
            ((ViewHolder) holder).fee1.setText(BOBApplication.getInstance().getString(R.string.free));
        } else {
            ((ViewHolder) holder).fee1.setText(mList.get(position).getFee() + BOBApplication.getInstance().getString(R.string.co));
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView cover1;
        TextView title1;
        TextView fee1;
        View layout1;
        View rootView;

        public int viewType;

        public ViewHolder(final View itemView, int viewtype) {
            super(itemView);
            viewType = viewtype;
            rootView = itemView;
            itemView.setTag(itemView);
            cover1 = (ImageView) itemView.findViewById(R.id.cover1);
            title1 = (TextView) itemView.findViewById(R.id.title1);
            fee1 = (TextView) itemView.findViewById(R.id.fee1);
            //layout1 =  itemView.findViewById(R.id.layoyt1);
        }
    }

    public interface vasFunctioListener {
        public void onClick(String id);
    }

}