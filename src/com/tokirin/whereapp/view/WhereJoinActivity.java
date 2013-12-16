package com.tokirin.whereapp.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.tokirin.whereapp.R;
import com.tokirin.whereapp.lib.Global;
import com.tokirin.whereapp.lib.WhereHttpRequest;
import com.tokirin.whereapp.lib.WhereHttpRequest.Callback;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class WhereJoinActivity extends Activity {

	EditText joinEmail;
	EditText joinPwd;
	EditText joinPwd_Confirm;
	Button residenceBtn;
	Button submitBtn;
	Button favorBtn1;
	Button favorBtn2;
	Button favorBtn3;
	RelativeLayout checkLayout;
	CheckBox check1;
	CheckBox check2;
	CheckBox check3;
	CheckBox check4;
	
	Context mContext;
	HashSet<Integer> category = new HashSet<Integer>();
	HashMap<String,String> residence = new HashMap<String, String>();
	ArrayList<HashMap<String,String>> location = new ArrayList<HashMap<String,String>>();
	
	final int RESIDENCE = 1;
	final int FAVORITE1 = 2;
	final int FAVORITE2 = 3;
	final int FAVORITE3 = 4;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_join);
		mContext = this.getBaseContext();
		
		joinEmail = (EditText)findViewById(R.id.join_email);
		joinPwd = (EditText)findViewById(R.id.join_pwd);
		joinPwd_Confirm = (EditText)findViewById(R.id.join_pwd_confirm);
		residenceBtn = (Button)findViewById(R.id.join_residence);
		submitBtn = (Button)findViewById(R.id.join_submit);
		checkLayout = (RelativeLayout)findViewById(R.id.join_check_layout);
		check1 = (CheckBox)findViewById(R.id.join_check1);
		check2 = (CheckBox)findViewById(R.id.join_check2);
		check3 = (CheckBox)findViewById(R.id.join_check3);
		check4 = (CheckBox)findViewById(R.id.join_check4);
		favorBtn1 = (Button)findViewById(R.id.join_favor1);
		favorBtn2 = (Button)findViewById(R.id.join_favor2);
		favorBtn3 = (Button)findViewById(R.id.join_favor3);
		
		OnCheckedChangeListener checkCallback = new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
				switch(buttonView.getId()){
					case R.id.join_check1:
						if(buttonView.isChecked()) category.add(1);
						else category.remove(1);
						break;
					case R.id.join_check2:
						if(buttonView.isChecked()) category.add(2);
						else category.remove(2);
						break;
					case R.id.join_check3:
						if(buttonView.isChecked()) category.add(3);
						else category.remove(3);
						break;
					case R.id.join_check4:
						if(buttonView.isChecked()) category.add(4);
						else category.remove(4);
						break;
					}
				}
			};
		
		OnClickListener favorCallback = new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch(v.getId()){
				case R.id.join_favor1 :
					Intent intent = new Intent(mContext,WhereLocationActivity.class);
					startActivityForResult(intent,FAVORITE1);
					break;
					
				case R.id.join_favor2 :

					Intent intent2 = new Intent(mContext,WhereLocationActivity.class);
					startActivityForResult(intent2,FAVORITE2);
					break;
					
				case R.id.join_favor3 :

					Intent intent3 = new Intent(mContext,WhereLocationActivity.class);
					startActivityForResult(intent3,FAVORITE3);
					break;
				}
				
			}
		};
		check1.setOnCheckedChangeListener(checkCallback);
		check2.setOnCheckedChangeListener(checkCallback);
		check3.setOnCheckedChangeListener(checkCallback);
		check4.setOnCheckedChangeListener(checkCallback);
		favorBtn1.setOnClickListener(favorCallback);
		favorBtn2.setOnClickListener(favorCallback);
		favorBtn3.setOnClickListener(favorCallback);
		
		submitBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				JSONObject info = new JSONObject();
				String email = joinEmail.getText().toString();
				String pwd = joinPwd.getText().toString();
				String pwd_confirm = joinPwd_Confirm.getText().toString();
				
				JSONArray arrCategory = new JSONArray();
				for(int i : category){
					arrCategory.put(i);
				}
				
				JSONArray arrFavorite = new JSONArray();
				for(HashMap<String,String> loc : location){
					try {
						arrFavorite.put(new JSONObject(new Gson().toJson(loc)));
					} catch (JSONException e) {}
				}
				
				if(!pwd.equals(pwd_confirm)){
					Toast.makeText(mContext, "비밀번호를 다시 확인해주십시오", 500).show();
				}else{
					try {
						info.put("id", email);
						info.put("password",pwd);
						info.put("residence",new JSONObject(new Gson().toJson(residence)));
						info.put("category", arrCategory);
						info.put("favorite", arrFavorite);
						info.put("mobileKey", Global.mobileKey);
						info.put("rad", Global.rad);
						WhereHttpRequest req = new WhereHttpRequest("POST_JSON",Global.appHost + ":" + Global.appPort + "/join",info);
						
						req.addObserver(new Callback(){
							@Override
							public void onSuccess(HttpResponse response) {
								Toast.makeText(mContext, "회원가입이 성공적으로 완료되었습니다", 500).show();
								finish();
							}
							@Override
							public void onFailure(HttpResponse response) {
								Toast.makeText(mContext, "회원가입이 실패하였습니다 \n Error Code : " 
										+ response.getStatusLine().getStatusCode(), 500).show();
								finish();
								Log.d("Join", ""+response.getStatusLine().getStatusCode());
							}
						});
						
						req.execute();
						
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				
			}
		});
	
		residenceBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext,WhereLocationActivity.class);
				startActivityForResult(intent,RESIDENCE);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode){
			case RESIDENCE :
				if(resultCode == RESULT_OK){
					double lat = data.getDoubleExtra("latitude", 300.00);
					double lng = data.getDoubleExtra("longitude", 300.00);
					
					residence.put("latitude", Double.toString(lat));
					residence.put("longitude", Double.toString(lng));
					residenceBtn.setText("위치가 설정되었습니다");
				}else{}
				break;
			case FAVORITE1 :
				if(resultCode == RESULT_OK){
					double lat = data.getDoubleExtra("latitude", 300.00);
					double lng = data.getDoubleExtra("longitude", 300.00);
					
					
					HashMap<String,String> temp = new HashMap();
					temp.put("latitude", Double.toString(lat));
					temp.put("longitude", Double.toString(lng));
					location.add(temp);
					favorBtn1.setText("위치가 설정되었습니다");
				}else{}
				break;
			case FAVORITE2 :
				if(resultCode == RESULT_OK){
					double lat = data.getDoubleExtra("latitude", 300.00);
					double lng = data.getDoubleExtra("longitude", 300.00);
					HashMap<String,String> temp = new HashMap();
					temp.put("latitude", Double.toString(lat));
					temp.put("longitude", Double.toString(lng));
					location.add(temp);
					favorBtn2.setText("위치가 설정되었습니다");
				}else{}
				break;
			case FAVORITE3 :
				if(resultCode == RESULT_OK){
					double lat = data.getDoubleExtra("latitude", 300.00);
					double lng = data.getDoubleExtra("longitude", 300.00);
					HashMap<String,String> temp = new HashMap();
					temp.put("latitude", Double.toString(lat));
					temp.put("longitude", Double.toString(lng));
					location.add(temp);
					favorBtn3.setText("위치가 설정되었습니다");
				}else{}
				break;
					
		}
	}

	
}
