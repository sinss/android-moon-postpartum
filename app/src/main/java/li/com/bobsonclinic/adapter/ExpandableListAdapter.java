package li.com.bobsonclinic.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import li.com.bobsonclinic.R;

/**
 * Created by anandbose on 09/06/15.
 */
public class ExpandableListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int HEADER = 0;
    public static final int CHILD = 1;
    private int isHeaderPosClick = -1;
    private int isChangePosClick;
    private int isChildPosClick;
    Context mContext;
    String mKey;
    String mChildKey;

    private List<Item> data;
    private LisClickListener mListener;

    public ExpandableListAdapter(Context context) {
        mContext = context;
    }

    public ExpandableListAdapter(Context context, List<Item> data) {
        this.data = data;
        mContext = context;
    }

    public void setList(List<Item> data) {
        this.data = data;
        this.notifyDataSetChanged();
    }

    public void setRefresh(int prePos, int currPos){
        //this.notifyDataSetChanged();
    }

    public void setChildKey(String key){
        this.mChildKey = key;
        this.notifyDataSetChanged();
    }

    public void setKey(String key){
        this.mKey = key;
        this.notifyDataSetChanged();
    }

    public void setListener(LisClickListener listener){
        this.mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        View view = null;
        Context context = parent.getContext();
        float dp = context.getResources().getDisplayMetrics().density;
        switch (type) {
            case HEADER:
                LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.right_list_header, parent, false);
                ListHeaderViewHolder header = new ListHeaderViewHolder(view);
                return header;
            case CHILD:
                LayoutInflater inflater1 = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater1.inflate(R.layout.right_list_child, parent, false);
                ListChildViewHolder child = new ListChildViewHolder(view);
                return child;
        }
        return null;
    }

    public void onBindViewHolder(RecyclerView.ViewHolder holder,final int position) {
        final Item item = data.get(position);
        Log.d("Milla", "onBindViewHolder item : "+item.type);
        switch (item.type) {
            case HEADER:
                final ListHeaderViewHolder itemController = (ListHeaderViewHolder) holder;
                itemController.refferalItem = item;
                itemController.header_title.setText(item.text);
                itemController.mArrow.setVisibility(View.VISIBLE);
                Log.d("Milla", "onBindViewHolder isHeaderPosClick: "+isHeaderPosClick);

                if(item.childCount > 0 && item.invisibleChildren == null){
                    itemController.mArrow.setVisibility(View.GONE);
                }

                itemController.rootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (item.childCount > 0 && item.invisibleChildren == null) {
                            item.invisibleChildren = new ArrayList<Item>();
                            int count = 0;
                            int pos = data.indexOf(itemController.refferalItem);
                            while (data.size() > pos + 1 && data.get(pos + 1).type == CHILD) {
                                item.invisibleChildren.add(data.remove(pos + 1));
                                count++;
                            }
                            itemController.mArrow.setVisibility(View.VISIBLE);
                            notifyItemRangeRemoved(pos + 1, count);

                        } else if(item.childCount > 0 && item.invisibleChildren != null && item.invisibleChildren.size() > 0) {
                            int pos = data.indexOf(itemController.refferalItem);
                            int index = pos + 1;
                            for (Item i : item.invisibleChildren) {
                                data.add(index, i);
                                index++;
                            }
                            notifyItemRangeInserted(pos + 1, index - pos - 1);
                            if(mListener != null){
                                mListener.headerExpand(pos);
                            }
                            itemController.mArrow.setVisibility(View.GONE);
                            item.invisibleChildren = null;
                        } else {
                            if(mListener != null){
                                mKey = null;
                                Log.d("Milla", "onBindViewHolder isHeaderPosClick: " + isHeaderPosClick);
                                mListener.headerClick(item.key);
                                if(isHeaderPosClick == -1)
                                    isHeaderPosClick = 0;
                                notifyItemChanged(isHeaderPosClick);
                                itemController.mArrow.setVisibility(View.VISIBLE);
                                //itemController.header_title.setTextColor(mContext.getResources().getColor(R.color.theme_red_primary_dark));
                                isHeaderPosClick = position;

                            }
                        }

                    }
                });

                //if(isHeaderPosClick == position || (isHeaderPosClick == -1 && ((mKey != null &&  mKey.equals(data.get(position).key))))){
                    //itemController.mArrow.setVisibility(View.GONE);
                    //itemController.header_title.setTextColor(mContext.getResources().getColor(R.color.theme_red_primary_dark));
                //}

                break;
            case CHILD:
                final ListChildViewHolder itemChild = (ListChildViewHolder) holder;
                final TextView child = (TextView) itemChild.child_title;
                child.setText(data.get(position).text);
                itemChild.rootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mListener != null){
                            mChildKey = null;
                            if(isHeaderPosClick == -1)
                                isHeaderPosClick = 0;
                            mListener.childClick(data.get(position).key,data.get(position).childKey);
                            notifyItemChanged(isHeaderPosClick);
                            //child.setTextColor(mContext.getResources().getColor(R.color.theme_red_primary_dark));
                            isHeaderPosClick = position;
                        }
                    }
                });

                /*
                if(isHeaderPosClick == position || (isHeaderPosClick == -1 && (mChildKey != null &&  mChildKey.equals(data.get(position).childKey)))){
                    child.setTextColor(mContext.getResources().getColor(R.color.theme_red_primary_dark));
                } else {
                    child.setTextColor(mContext.getResources().getColor(R.color.text_dark));
                }
                */
                break;
        }

    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).type;
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public void collapseAll(){
        for(int i=0;i<getItemCount();i++){
            final Item item = data.get(i);
            Log.d("collapseAll", "collapseAll "+getItemViewType(i));
             if(item.type == CHILD)   {
                 notifyItemRemoved(i);
             }

        }
    }

    private static class ListHeaderViewHolder extends RecyclerView.ViewHolder {
        public TextView header_title;
        public Item refferalItem;
        public ImageView mArrow;
        View rootView;

        public ListHeaderViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            header_title = (TextView) itemView.findViewById(R.id.header_title);
            mArrow = itemView.findViewById(R.id.arrow);
        }
    }

    public static class Item {
        public int type;
        public String text;
        public String key;
        public String childKey;
        public int childCount;
        public List<Item> invisibleChildren;

        public Item() {
        }

        public Item(int type, String text) {
            this.type = type;
            this.text = text;
        }


        public Item(int type, String key, String text,int childcount) {
            this.type = type;
            this.text = text;
            this.key = key;
            this.childCount = childcount;
        }

        public Item(int type, String hkey, String ckey, String text) {
            this.type = type;
            this.text = text;
            this.key = hkey;
            this.childKey = ckey;
        }
    }

    private static class ListChildViewHolder extends RecyclerView.ViewHolder {
        public TextView child_title;
        public Item refferalItem;
        View rootView;

        public ListChildViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            child_title = (TextView) itemView.findViewById(R.id.header_title);
        }
    }

    public interface  LisClickListener{
        public void headerClick(String key);
        public void childClick(String key, String cKey);
        public void headerExpand(int pos);
    }

}
