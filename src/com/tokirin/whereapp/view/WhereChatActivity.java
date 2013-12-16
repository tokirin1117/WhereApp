package com.tokirin.whereapp.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.PresenceChannel;
import com.pusher.client.channel.PresenceChannelEventListener;
import com.pusher.client.channel.User;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionStateChange;
import com.pusher.client.util.HttpAuthorizer;
import com.tokirin.whereapp.R;
import com.tokirin.whereapp.adapter.ChatListAdapter;
import com.tokirin.whereapp.lib.Global;
import com.tokirin.whereapp.model.Chat;
import com.tokirin.whereapp.model.Item;
import com.tokirin.whereapp.model.Member;
import com.tokirin.whereapp.model.Receiver;
import com.tokirin.whereapp.model.Sender;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class WhereChatActivity extends Activity {
	ArrayList<Item> chatArrayList;
	ListView chatList;
	ChatListAdapter myAdapter;
	Context context;
	Pusher pusher;
	PresenceChannel channel;
	EditText contentText;
	PusherOptions opt;
	boolean isPaused = false;
	String currentId;
	String channelName;
	Button exitBtn;
	Button sendBtn;
	String mobileKey;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		context = this.getBaseContext();
		Intent intent = this.getIntent();
		currentId = intent.getStringExtra("id");
		channelName = intent.getStringExtra("channel");
		mobileKey = intent.getStringExtra("mobileKey");
		
		chatArrayList = new ArrayList<Item>();
		chatList = (ListView)findViewById(R.id.chatList);
		LayoutInflater inflater = LayoutInflater.from(this);
		myAdapter = new ChatListAdapter(this,inflater,chatArrayList);
		chatList.setAdapter(myAdapter);
		
		opt = new PusherOptions();
		opt.setHost(Global.wsHost);
		opt.setWsPort(Global.socket_port);
		opt.setEncrypted(false);

		initPusher();
		
		sendBtn = (Button)findViewById(R.id.send_btn);
		exitBtn = (Button)findViewById(R.id.chat_exit);
		
		contentText = (EditText)findViewById(R.id.content);
		
		sendBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				Date now = new Date();
				SimpleDateFormat fm = new SimpleDateFormat("yy-MM-dd a hh:mm:ss");
				JSONObject event = new JSONObject();
				try {
					event.put("\"content\"", "\""+contentText.getText().toString()+"\"");
					event.put("\"id\"", "\"" + currentId + "\"");
					event.put("\"time\"", "\"" + fm.format(now) + "\"");
				} catch (JSONException e) {}
				
				channel.trigger("client-test", event.toString());
				contentText.setText("");
				
			}
		});
		
		exitBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pusher.unsubscribe(channelName);
				pusher.disconnect();
				finish();
			}
		});
		
	}
	
	public void initPusher(){
		
		HttpAuthorizer auth = new HttpAuthorizer("http://j4.link-to-rink.com/pushertok/pusher_auth.php");
		HashMap<String,String> params = new HashMap<String, String>();
		int a = new Random().nextInt(100);
		params.put("user_id", currentId);
		params.put("user_name", "notdefined");
		params.put("mobile_key", mobileKey);
		auth.setQueryStringParameters(params);

		opt.setAuthorizer(auth);

		pusher = new Pusher("a080e81530d15631ff70",opt);

		pusher.connect(new ConnectionEventListener() {
		    @Override
		    public void onConnectionStateChange(ConnectionStateChange change) {
		       Log.d("Test", "State changed to " + change.getCurrentState() +
		                           " from " + change.getPreviousState());
		    }
		    @Override
		    public void onError(String message, String code, Exception e) {
		       Log.d("Test","message : " + message);
		    }
		});
		
		channel = pusher.subscribePresence(channelName,new PresenceChannelEventListener(){

			@Override
			public void onAuthenticationFailure(String msg, Exception arg1) {
				Log.d("Test","Auth Failed");
			}

			@Override
			public void onSubscriptionSucceeded(String msg) {
				Log.d("Test",msg + " : Subscription Succeeded!");
			}

			@Override
			public void onEvent(String channelName, String eventName, String data) {
				String msg = String.format("Event received: [%s] [%s] [%s]", channelName, eventName, data);
		        Log.d("Test", msg);
		        Chat chat = new Gson().fromJson(data, Chat.class);
		        new ApplyEventTask(chat).execute();
			}

			@Override
			public void onUsersInformationReceived(String msg, Set<User> users) {
				Log.d("Test",msg + " : User information Received!");
				int i=1;
				for(User user : users){
					Log.d("Test","" + (i++));
					Log.d("Test","id : " + user.getId() + "&& info : " + user.getInfo());
				}
			}

			@Override
			public void userSubscribed(String msg, User user) {
				Log.d("Test",msg + " : Member Added " + "<"+user.getId()+">");
				new ApplyEventTask(user.getId(),true).execute();
			}

			@Override
			public void userUnsubscribed(String msg, User user) {
				Log.d("Test",msg + " : Member Removed");
				new ApplyEventTask(user.getId(),false).execute();
			}
			
			
		},"client-test");
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		if(isPaused==true){
			isPaused=false;
			initPusher();
		}
		super.onResume();
	}


	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//pusher.unsubscribe(channelName);
    	pusher.disconnect();
		super.onBackPressed();
		
	}
	

    @Override
	protected void onPause() {
		// TODO Auto-generated method stub
    	isPaused = true;
    	pusher.disconnect();
		super.onPause();
	}


	class ApplyEventTask extends AsyncTask<Void, Void, Void> {

		Chat chat = null;
		String id = null;
		Boolean code = false;
		
		public ApplyEventTask(Chat chat){
			this.chat = chat;
		}

		public ApplyEventTask(String id, boolean code){
			this.id = id;
			this.code = code;
		}
		
        @Override
        protected void onPostExecute(Void result) {
        	myAdapter.notifyDataSetChanged();
            super.onPostExecute(result);
        }

		@Override
		protected Void doInBackground(Void... params) {
			if(chat!=null){
				if(chat.id.equals(currentId)){
					chatArrayList.add(new Receiver(chat));
				}else{
					chatArrayList.add(new Sender(chat));
				}
			}else if(id!=null){
				chatArrayList.add(new Member(this.id,code));
			}
			return null;
		}

    }
	

}
