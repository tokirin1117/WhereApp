package com.tokirin.whereapp.view;

import com.tokirin.whereapp.R;
import com.tokirin.whereapp.model.Info;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

@SuppressLint("ValidFragment")
public class WhereSettingActivity extends Fragment {

	

	Context mContext;
	Info user;
	public WhereSettingActivity(Context context,Info user) {
		mContext = context;
		this.user = user;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.activity_setting, container, false);
		return v;
	}

}
