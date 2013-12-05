package com.tokirin.whereapp.view;

import java.util.HashMap;

import com.tokirin.whereapp.R;
import com.tokirin.whereapp.R.array;
import com.tokirin.whereapp.R.id;
import com.tokirin.whereapp.R.layout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

@SuppressLint("ValidFragment")
public class WhereQuestionActivity extends Fragment {


	Context mContext;
	ArrayAdapter<CharSequence> categoryAdapter;
	
	int categoryNum = 9999; //default value 9999
	String categoryName = null;
	
	public WhereQuestionActivity(Context context) {
		mContext = context;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.activity_question, container, false);
		
		ListView qaList = (ListView)v.findViewById(R.id.qa_list);
		Button sendButton = (Button)v.findViewById(R.id.search_submit);
		Spinner categorySpinner = (Spinner)v.findViewById(R.id.search_spinner);
		
		categorySpinner.setPrompt("카테고리");
		categoryAdapter = ArrayAdapter.createFromResource(mContext,	R.array.search_category, android.R.layout.simple_spinner_item);
		categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		categorySpinner.setAdapter(categoryAdapter);
		categorySpinner.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View v,
					int pos, long id) {
				categoryNum = pos;
				categoryName = (String) categoryAdapter.getItem(pos);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
			
			
		});
		
		sendButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				//TODO:HttpRequest, POST, response check
			}
		});
		
		return v;
	}

}
