package li.com.bobsonclinic.adapter;


import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import li.com.bobsonclinic.R;
import li.com.bobsonclinic.data.BOBBabies;


public class BOBBabysAdapter extends RecyclerView.Adapter<BOBBabysAdapter.ViewHolder> {

    private final String TAG = BOBBabysAdapter.class.getSimpleName();
    private Context mContext;
    private LayoutInflater myInflater;
    public static final int IS_NORMAL = 1;
    private List<BOBBabies> mList;
    private bListener mListener;

    public BOBBabysAdapter(final Context context) {
        super();
        this.mContext = context;
        this.myInflater = LayoutInflater.from(context);
    }

    public void setList(List<BOBBabies> nameList) {
        this.mList = nameList;
        this.notifyDataSetChanged();
    }

    public void setListener(bListener listener) {
        this.mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = this.myInflater.inflate(R.layout.fragment_babys_list, parent, false);
        return new ViewHolder(view, IS_NORMAL);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,final int position) {

        holder.name.setText(mList.get(position).getName());
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null){
                    mListener.onClick(mList.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }




    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        View rootView;

        public int viewType;

        public ViewHolder(final View itemView, int viewtype) {
            super(itemView);
            viewType = viewtype;
            rootView = itemView;
            itemView.setTag(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
        }
    }

    public interface bListener{
        public void onClick(BOBBabies babies);
    }

}