package li.com.bobsonclinic.fragment;

/**
 * Agricola
 *
 * Created by Milla.Wang on 2016/1/5.
 * Copyright © 2016 bobsonclinic. All rights reserved.
 */

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import li.com.bobsonclinic.R;
import li.com.bobsonclinic.activity.MainActivity;
import li.com.bobsonclinic.data.BOBActivity;
import li.com.bobsonclinic.fragment.base.BaseFragment;
import li.com.bobsonclinic.request.base.BOBAPI;
import li.com.bobsonclinic.util.Picasso;


public class BOBMonthClassDetailFragment extends BaseFragment implements View.OnClickListener {

	private ViewGroup rootView;
	private TextView title, className;
	private WebView detail;
	//private TextView dateTime;
	private TextView fee;
	//private TextView count;
	private ImageView cover;
	private Button check;
	private String mActivityId;

	public static BOBMonthClassDetailFragment createFragment(String id) {
		BOBMonthClassDetailFragment fragment = new BOBMonthClassDetailFragment();
		Bundle bundle = new Bundle();
		bundle.putString("activityId", id);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		rootView = (ViewGroup) inflater.inflate(R.layout.fragment_month_class_detail, null);
		rootView.findViewById(R.id.left_btn).setOnClickListener(this);
		rootView.findViewById(R.id.is_check).setOnClickListener(this);
		title = (TextView)rootView.findViewById(R.id.title);
		className= (TextView)rootView.findViewById(R.id.class_name);
		detail = (WebView)rootView.findViewById(R.id.class_detail);
		//dateTime  = (TextView)rootView.findViewById(R.id.date_time);
		fee = (TextView)rootView.findViewById(R.id.class_fee);
		//count = (TextView)rootView.findViewById(R.id.remaining_count);
		cover = (ImageView)rootView.findViewById(R.id.cover);
		check = rootView.findViewById(R.id.is_check);
		mActivityId = getArguments().getString("activityId");
		((MainActivity)getActivity()).postActivity(mActivityId);
		rootView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});



		return rootView;
	}

	public void setData(BOBActivity act){
		if(act == null)
			return;
		if(!isAdded())
			return;

		if(!mActivityId.equals(act.getId()))
			return;
		//dateTime.setText(act.getTime());
		title.setText(act.getTitle());
		className.setText(act.getTitle());
		//count.setText(act.getMember_count());
		detail.loadUrl(BOBAPI.getAuthDomainURL()+act.getEntry());


		if (act.getFee().equals("0")) {
			fee.setText(getString(R.string.free));
		} else {
			fee.setText(act.getFee() + getString(R.string.co));
		}

		Picasso.getInstance().load(BOBAPI.getAuthDomainURL() +act.getImg()).fit().centerCrop().placeholder(R.drawable.placeholder_c).into(cover);
		Log.d("Milla", "Activity getJoin_status: ["+act.getJoin_status()+"]");
		if(act.getJoin_status().equals("checked")){
			check.setSelected(true);
			check.setText(R.string.is_class);
		} else {
			check.setSelected(false);
			check.setText(R.string.un_class);
		}

		//  利用 DateFormat 來parse 日期的字串
		SimpleDateFormat dt = new SimpleDateFormat("yyyyy-MM-dd HH:mm:ss");
		Date date1 = null;
		try {
			date1 = dt.parse(act.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}

		int member_count = 0;
		try{
			member_count = Integer.valueOf(act.getMember_count());
		} catch (NumberFormatException e){

		}
		Log.d("Milla", "date1 : "+date1.getTime() +", "+ System.currentTimeMillis());
		//修正成已報名人數，只有已過期才不可選。
		if(date1.getTime() < System.currentTimeMillis()){
			check.setEnabled(false);
		}
	}

	public void setCheck(boolean ischeck){

		if(ischeck){
			check.setSelected(true);
			check.setText(R.string.is_class);
		} else {
			check.setSelected(false);
			check.setText(R.string.un_class);
		}

	}



	@Override
	public void onClick(View v) {
		switch(v.getId()){

			case R.id.left_btn:
				getActivity().getSupportFragmentManager().popBackStack();
				if(!(((MainActivity)getActivity()).getCurrentFragment() instanceof BOBMonthClassFragment)){
					((MainActivity)getActivity()).goMonthClasses();
				}
				break;
				/*
			case R.id.is_check:
				if(check.isSelected()){
					((MainActivity)getActivity()).postUnCheckActivity(mActivityId);
				} else {
					((MainActivity)getActivity()).postCheckActivity(mActivityId);
				}
				break;
				*/
		}
	}


}
