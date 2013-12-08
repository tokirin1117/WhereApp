package com.tokirin.whereapp.view;

import com.tokirin.whereapp.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
		
		loginBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext,WhereMainActivity.class);
				intent.putExtra("id", idText.getText().toString());
				intent.putExtra("pwd", pwdText.getText().toString());
				startActivity(intent);
			}
		});
		
		
	}

	
}
