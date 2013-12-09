package com.tokirin.whereapp.view;

import java.util.HashMap;

import org.apache.http.HttpResponse;

import com.tokirin.whereapp.R;
import com.tokirin.whereapp.lib.WhereHttpRequest;
import com.tokirin.whereapp.lib.WhereHttpRequest.Callback;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class WhereLoginActivity extends Activity {
	
	Button loginBtn;
	Button joinBtn;
	EditText idText;
	EditText pwdText;
	Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		mContext = this.getBaseContext();
		loginBtn = (Button)findViewById(R.id.login_button);
		joinBtn = (Button)findViewById(R.id.join_button);
		idText = (EditText)findViewById(R.id.login_id);
		pwdText = (EditText)findViewById(R.id.login_pwd);
		
		joinBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext,WhereJoinActivity.class);
				startActivity(intent);
				
			}
		});
		
		loginBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.d("Login","Login Process start");
				String id = idText.getText().toString();
				String pwd = pwdText.getText().toString();
				Log.d("Login","get id,pwd");
				HashMap<String,String> params = new HashMap();
				params.put("id", id);
				params.put("pwd", pwd);
				Log.d("Login","put id,pwd");
				final WhereHttpRequest req = new WhereHttpRequest("POST","http://192.168.100.28:8920/login",params);
				req.addObserver(new Callback(){
					@Override
					public void onSuccess(HttpResponse response) {
						Log.d("Login","Login success");
						String info = req.responseContent;
						Intent intent = new Intent(mContext,WhereMainActivity.class);
						intent.putExtra("info", info);
						startActivity(intent);
					}

					@Override
					public void onFailure(HttpResponse response) {
						Toast.makeText(mContext, "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show();
					}
				});
				req.execute();
			}
		});
		
		
	}

	
}
