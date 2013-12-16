package com.tokirin.whereapp.view;

import java.util.ArrayList;

import org.apache.http.HttpResponse;

import com.google.gson.Gson;
import com.tokirin.whereapp.R;
import com.tokirin.whereapp.adapter.QAListAdapter;
import com.tokirin.whereapp.lib.Global;
import com.tokirin.whereapp.lib.WhereHttpRequest;
import com.tokirin.whereapp.lib.WhereHttpRequest.Callback;
import com.tokirin.whereapp.model.Info;
import com.tokirin.whereapp.model.QA;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("ValidFragment")
public class WhereAnswerActivity extends Fragment {

	Context mContext;
	Info user;
	ListView answerList;

	QAListAdapter qaAdapter;
	ArrayList<QA> qaSet;
	QA[] qaArray;
	Button refresh;
	public WhereAnswerActivity(Context context,Info user) {
		mContext = context;
		this.user = user;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.activity_answer, container, false);
		answerList = (ListView)v.findViewById(R.id.answer_list);
		refresh = (Button)v.findViewById(R.id.answer_refresh);
		refresh.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				qaSet = new ArrayList<QA>();
				qaAdapter = new QAListAdapter(mContext,R.layout.row_qa,qaSet);
				answerList.setAdapter(qaAdapter);
				qaAdapter.notifyDataSetChanged();
				getAnswerList();
			}
		});
		
		qaSet = new ArrayList<QA>();
		qaAdapter = new QAListAdapter(mContext,R.layout.row_qa,qaSet);
		answerList.setAdapter(qaAdapter);
		qaAdapter.notifyDataSetChanged();
		getAnswerList();
		setListItemClick();
		
		
		return v;
	}
	
	public void getAnswerList(){
		final WhereHttpRequest req = new WhereHttpRequest("GET",Global.appHost + ":" + Global.appPort + "/getRooms");
		req.addObserver(new Callback(){
			@Override
			public void onSuccess(HttpResponse response) {
				String arrayString = req.responseBody;
				qaArray = new Gson().fromJson(arrayString, QA[].class);
				for(QA qa: qaArray){
					qaSet.add(qa);
				}
				qaAdapter.notifyDataSetChanged();
			}

			@Override
			public void onFailure(HttpResponse response) {
				
			}
		});
		req.execute();
		
	}


	public void setListItemClick(){
		answerList.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> adapter, View v, int position,
					long arg3) {
				QA qa = qaSet.get(position);
				Intent intent = new Intent(mContext,WhereChatActivity.class);
				intent.putExtra("id", user.id);
				intent.putExtra("channel", "permanent-"+qa.hash);	
				intent.putExtra("mobileKey", user.mobileKey);
				startActivity(intent);
			}
		});
		
	}

}
