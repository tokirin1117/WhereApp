package com.tokirin.whereapp.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tokirin.whereapp.R;
import com.tokirin.whereapp.adapter.QAListAdapter;
import com.tokirin.whereapp.lib.Global;
import com.tokirin.whereapp.lib.WhereHttpRequest;
import com.tokirin.whereapp.lib.WhereHttpRequest.Callback;
import com.tokirin.whereapp.model.Location;
import com.tokirin.whereapp.model.QA;
import com.tokirin.whereapp.model.Info;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class WhereQuestionActivity extends Fragment {


	Context mContext;
	ArrayAdapter<CharSequence> categoryAdapter;
	
	int categoryNum = 9999; //default value 9999
	String categoryName = null;
	
	Button sendBtn;
	Button locationBtn;
	Location questionLoc;
	
	Spinner categorySpinner;
	EditText question;
	ListView qaList;
	QAListAdapter qaAdapter;
	ArrayList<QA> qaSet ;
	Info user;
	
	final int LOCATION = 1;
	
	public WhereQuestionActivity(Context context,Info user) {
		mContext = context;
		this.user= user;
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.activity_question, container, false);
		
		qaList = (ListView)v.findViewById(R.id.qa_list);
		sendBtn = (Button)v.findViewById(R.id.search_submit);
		question = (EditText)v.findViewById(R.id.search_query);
		locationBtn = (Button)v.findViewById(R.id.search_location);
		categorySpinner = (Spinner)v.findViewById(R.id.search_spinner);
		
		qaSet = new ArrayList<QA>();

		setSpinner();
		setSendButton();
		setListItemClick();
		setLocationButton();
		initList();
		
		return v;
	}
	
	public void setSpinner(){
		
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
		
	}
	
	public void setSendButton(){
		sendBtn.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {

				Date now = new Date();
				SimpleDateFormat fm = new SimpleDateFormat("yy-MM-dd a hh:mm:ss");
				String time = fm.format(now);
				JSONObject loc = new JSONObject();
				try {
					loc.put("latitude", "300.0");
					loc.put("longitude", "300.0");
				} catch (JSONException e1) {}
				
				JSONObject json = new JSONObject();
				try {
					json.put("query", question.getText().toString());
					if(questionLoc!=null){
						json.put("loc", new JSONObject(new Gson().toJson(questionLoc)));
					}else{
						json.put("loc",loc);						
					}
					json.put("category", categoryNum);
					json.put("rad", Global.rad);
					json.put("time", time);
				} catch (JSONException e) {}
				
				final WhereHttpRequest req = new WhereHttpRequest("POST_JSON",Global.appHost + ":" + Global.appPort + "/"+user.id+"/query",json);
				
				
				req.addObserver(new Callback(){

					@Override
					public void onSuccess(HttpResponse response) {
						Log.d("Query",req.responseBody);
						Toast.makeText(mContext,req.responseBody,Toast.LENGTH_SHORT).show();
						QA qa = new Gson().fromJson(req.responseBody,QA.class);
						qaSet.add(qa);
						qaAdapter.notifyDataSetChanged();
						
						Intent intent = new Intent(mContext,WhereChatActivity.class);
						intent.putExtra("id", qa.owner);
						intent.putExtra("channel", "permanent-"+qa.hash);
						intent.putExtra("mobileKey", user.mobileKey);
						startActivity(intent);
						
					}

					@Override
					public void onFailure(HttpResponse response) {
						
						Log.d("Query","Query Failed");
						
					}
					
				});
				
				req.execute();
				
			}
		});
	}

	public void setLocationButton(){

		locationBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext,WhereLocationActivity.class);
				startActivityForResult(intent,LOCATION);
			}
		});
	}
	
	public void initList(){
		qaAdapter = new QAListAdapter(mContext,R.layout.row_qa,qaSet);
		qaList.setAdapter(qaAdapter);
		qaAdapter.notifyDataSetChanged();
		Log.d("Question","Initialize ArrayList");
	}

	public void setListItemClick(){
		qaList.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> adapter, View v, int position,
					long arg3) {
				QA qa = qaSet.get(position);
				Intent intent = new Intent(mContext,WhereChatActivity.class);
				intent.putExtra("id", qa.owner);
				intent.putExtra("channel", "permanent-"+qa.hash);						
				startActivity(intent);
			}
		});
		
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode){
			case LOCATION :
				if(resultCode == Activity.RESULT_OK){
					double lat = data.getDoubleExtra("latitude", 300.00);
					double lng = data.getDoubleExtra("longitude", 300.00);
					Location loc = new Location();
					loc.latitude =Double.toString(lat);
					loc.longitude =Double.toString(lng);
					this.questionLoc = loc;
				}else{
					Toast.makeText(mContext, "위치정보가 제대로 전달되지 않았습니다. 다시시도해주세요", Toast.LENGTH_SHORT).show();
				}
				break;
		
		}
	}

	
	
}
