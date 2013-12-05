package com.tokirin.whereapp.view;

import com.tokirin.whereapp.R;
import com.tokirin.whereapp.R.layout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class WhereAnswerActivity extends Fragment {

	Context mContext;
	
	public WhereAnswerActivity(Context context) {
		mContext = context;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.activity_answer, container, false);
		return v;
	}

	

}
