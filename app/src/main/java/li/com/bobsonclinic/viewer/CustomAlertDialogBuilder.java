package li.com.bobsonclinic.viewer;

/**
 * Agricola
 *
 * Created by Milla.Wang on 2016/1/5.
 * Copyright © 2016 bobsonclinic. All rights reserved.
 */

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import li.com.bobsonclinic.R;


public class CustomAlertDialogBuilder extends Dialog {

	private class CustomCategoriesView extends LinearLayout {

		private Context mCxt;
		protected Button mLeftBtn;
		protected Button mRightBtn;
		protected TextView mContentTv;
		protected String mContent;
		protected CustomAlertDialogListener mCusListener;
		protected String mRightBtnName;
		protected String mLeftBtnName;
		protected int mImageNameId;
		AnimationDrawable animDrawable;
		protected ProgressBar mProgressbar;


		public CustomCategoriesView(Context context, int layoutId) {
			super(context);
			mCxt = context;
			initLayout(layoutId);
		}



		public CustomCategoriesView(Context context, String text, String leftBtnName, String rightBtnName,  int
				imageName, boolean isShowProgress, CustomAlertDialogListener listener) {
			super(context);
			mCxt = context;
			mContent = text;
			mCusListener = listener;
			mLeftBtnName = leftBtnName;
			mRightBtnName = rightBtnName;
			mImageNameId = imageName;
			init();

			if(isShowProgress)
				mProgressbar.setVisibility(View.VISIBLE);
		}


		private void initLayout(int layoutId){
			LayoutInflater li = LayoutInflater.from(getContext());
			li.inflate(layoutId, this);

		}

		private void init() {
			LayoutInflater li = LayoutInflater.from(getContext());
			li.inflate(R.layout.custom_alert_dialog, this);
			mContentTv =  (TextView) findViewById(R.id.custom_alert_title);
			mLeftBtn = (Button) findViewById(R.id.custom_button_cancel);
			mRightBtn = (Button) findViewById(R.id.custom_button_finish);
			mProgressbar = (ProgressBar)findViewById(R.id.progressbar);
			mContentTv.setText(mContent);
			mLeftBtn.setText(mLeftBtnName);
			mRightBtn.setText(mRightBtnName);

			mRightBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View paramView) {
					if(mCusListener != null)
						mCusListener.onRightBtnOnClick();

					dismiss();
				}
			});

			mLeftBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View paramView) {
					if(mCusListener != null)
						mCusListener.onLeftBtnOnClick();


					dismiss();
				}
			});

			if(mLeftBtnName == null || "".equals(mLeftBtnName)) mLeftBtn.setVisibility(View.GONE);
			if(mRightBtnName == null || "".equals(mRightBtnName)) mRightBtn.setVisibility(View.GONE);
		}

		class Starter implements Runnable {
			public void run() {
				animDrawable.start();        //animation.start必須寫在runnable裡面才會有效果, 這是官方網站沒提及的。
			}
		}


	}

	protected Context mContext;
	protected CustomCategoriesView mView;
	protected OnDismissListener osDissmissListener;
	protected String mContent;
	protected String rightBtnName;
	protected String leftBtnName;
	protected boolean mIsShowProgress;
	protected CustomAlertDialogListener mListener;
	protected int mImageResId;
	protected int mLayoutId = 0;

	public CustomAlertDialogBuilder(Context context, int theme, String content, String leftBtnName, String rightBtnName,
									CustomAlertDialogListener listener) {
		super(context, theme);
		mContext = context;
		mContent = content;
		this.leftBtnName = leftBtnName;
		this.rightBtnName = rightBtnName;
		mListener = listener;
		setCancelable(false);
	}

	public CustomAlertDialogBuilder(Context context, int theme, String text, CustomAlertDialogListener
			listener) {
		super(context, theme);
		mContext = context;
		mContent = text;
		this.leftBtnName = "";
		this.rightBtnName = "";
		mListener=listener;
	}

	public CustomAlertDialogBuilder(Context context, int theme, String text, boolean isShowProgress) {
		super(context, theme);
		mContext = context;
		mContent = text;
		mIsShowProgress = isShowProgress;
	}

	public CustomAlertDialogBuilder(Context context, int theme, int layoutId) {
		super(context, theme);
		mContext = context;
		mLayoutId = layoutId;
	}


	@Override
	public void setOnDismissListener(OnDismissListener listener) {
		osDissmissListener = listener;
		super.setOnDismissListener(listener);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		if(mLayoutId > 0) {
			mView = new CustomCategoriesView(mContext, mLayoutId);
		} else
			mView = new CustomCategoriesView(mContext, mContent, leftBtnName, rightBtnName,mImageResId, mIsShowProgress, mListener);
		setContentView(mView);
	}

	public interface CustomAlertDialogListener {
		void onLeftBtnOnClick();
		void onRightBtnOnClick();
	}


}
