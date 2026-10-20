package li.com.bobsonclinic.adapter;

import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import li.com.bobsonclinic.R;
import li.com.bobsonclinic.data.BOBBabies;


public class BOBBabyPagerAdapter extends PagerAdapter {

    private List<BOBBabies> mList = new ArrayList<>();
    private BabyListener mListener;

    public BOBBabyPagerAdapter(final Context context) {
    }

    public void setListenet(BabyListener listener){
        this.mListener = listener;
    }

    @Override
    public Object instantiateItem(ViewGroup container,final int position) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.fragment_home_baby_page, container, false);

        TextView birthday = (TextView) view.findViewById(R.id.birthday);
        TextView whoChild = (TextView) view.findViewById(R.id.who_child);
        ImageView right = (ImageView) view.findViewById(R.id.right);
        ImageView left = (ImageView) view.findViewById(R.id.left);

        whoChild.setText(mList.get(position).getName());
        birthday.setText(mList.get(position).getBirth_date());

        if(mList != null && mList.size() > 1){
            if(position == 0){
                right.setVisibility(View.GONE);
            } else {
                right.setVisibility(View.VISIBLE);
            }
            if(position < mList.size() - 1){
                left.setVisibility(View.VISIBLE);
            } else {
                left.setVisibility(View.GONE);
            }
        }
        container.addView(view);

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onClick(position - 1);
                }
            }
        });

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Milla","position: "+position );
                if(mListener != null){
                    mListener.onClick(position + 1);
                }
            }
        });

        return view;
    }

    public void setList(List<BOBBabies> list){
        this.mList = list;
        this.notifyDataSetChanged();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public interface BabyListener{
        public void onClick(int pos);
    }

}
