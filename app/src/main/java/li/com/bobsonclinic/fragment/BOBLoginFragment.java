package li.com.bobsonclinic.fragment;

/**
 * Agricola
 *
 * Created by Milla.Wang on 2016/1/5.
 * Copyright © 2016 bobsonclinic. All rights reserved.
 */

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import li.com.bobsonclinic.BuildConfig;
import li.com.bobsonclinic.R;
import li.com.bobsonclinic.activity.MainActivity;
import li.com.bobsonclinic.activity.MainPadActivity;
import li.com.bobsonclinic.fragment.base.BaseFragment;
import li.com.bobsonclinic.logic.BOBPreferenceLogic;
import li.com.bobsonclinic.manager.BOBSystemManager;


public class BOBLoginFragment extends BaseFragment implements View.OnClickListener {

	private ViewGroup rootView;
	private TextView mRegistrationID;
	private TextView mPassword;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		rootView = (ViewGroup) inflater.inflate(R.layout.fragment_login, null);
		rootView.findViewById(R.id.submit).setOnClickListener(this);


		rootView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});

		mRegistrationID = (TextView)rootView.findViewById(R.id.name_id);
		mPassword = (TextView)rootView.findViewById(R.id.password);

		mRegistrationID.setText(BOBPreferenceLogic.getInstance().getUserRgistrationId());
		mPassword.setText(BOBPreferenceLogic.getInstance().getUserPassword());
		if (BuildConfig.DEBUG) {
			mRegistrationID.setText("M122510168");
			mPassword.setText("19920214");
		}
		mPassword.setOnKeyListener(new View.OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER) {
					getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
					if(BOBSystemManager.getInstance().checkNetWorkState(getActivity())) {
						if (checkData()) {
							((MainActivity) getActivity()).postLogin(mRegistrationID.getText().toString(), mPassword.getText().toString());
						}
					}
					return false;
				}
				return false;
			}
		});
		return rootView;
	}

	private boolean checkData(){
		if(mRegistrationID.getText().toString().equals("")){
			((MainActivity)getActivity()).showDialog("請填入身份證字號", true);
			return false;
		} else if(mPassword.getText().toString().equals("")){
			((MainActivity)getActivity()).showDialog("請填入密碼", true);
			return false;
		}
		return true;
	}


	@Override
	public void onClick(View v) {
		switch(v.getId()){

			case R.id.submit:
				Log.d("BOB", "Login submit");
				((MainActivity) getActivity()).hideKeyboard();
				if(BOBSystemManager.getInstance().checkNetWorkState(getActivity())) {
					if (checkData()) {
						((MainActivity) getActivity()).showLogining();
						((MainActivity) getActivity()).postLogin(mRegistrationID.getText().toString(), mPassword.getText().toString());
					}
				}
				break;

		}
	}


}
