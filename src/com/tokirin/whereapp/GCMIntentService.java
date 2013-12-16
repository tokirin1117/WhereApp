package com.tokirin.whereapp;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.gson.Gson;
import com.tokirin.whereapp.lib.Global;
import com.tokirin.whereapp.model.Chat;
import com.tokirin.whereapp.view.WhereChatActivity;
import com.tokirin.whereapp.view.WhereMainActivity;

public class GCMIntentService extends GCMBaseIntentService {
    private static final String tag = "GCMIntentService";
    private static final String PROJECT_ID = "573108739764";
    public GCMIntentService(){ this(PROJECT_ID); }
   
    public GCMIntentService(String project_id) { super(project_id); }
	 
	@Override
	protected void onError(Context arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		 String event = intent.getStringExtra("event");
		 String channel = intent.getStringExtra("channel");
		 String data = intent.getStringExtra("data");
		 data = data.replace("\\\"","");
		 Log.e("getmessage", "event:"+event);
		 Log.e("getmessage", "channel:"+channel);
		 Log.e("getmessage", "data:"+ data);
		 
		 if(Global.isLogin){
			 if(event.equals("pusher_internal:member_added")){
				 try {
					 JSONObject json = new JSONObject(data);
					 generateNotification(context, json.getString("user_id") + "님이 입장하셨습니다",channel);
				} catch (JSONException e) {
					Log.e("getmessage",e.toString());
				}	 
			 }else if(event.equals("client-test")){
				 Chat chat = new Gson().fromJson(data, Chat.class);
				 generateNotification(context, chat.id + " : " + chat.content,channel);
			 }else if(event.equals("WhereQuestionPush")){
				 generateNotification(context,data + " 질문이 도착했어요!", channel);
			 }
		 }
		
	}

	@Override
	protected void onRegistered(Context arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) {
		// TODO Auto-generated method stub

	}
	private static void generateNotification(Context context, String message,String channel) {
		 int icon = R.drawable.ic_launcher;
		 long when = System.currentTimeMillis();
		 long[] vibrate = {0,300};
		 NotificationManager notificationManager = (NotificationManager) context
		   .getSystemService(Context.NOTIFICATION_SERVICE);
		 Notification notification = new Notification(icon, message, when);
		 notification.vibrate = vibrate;
		 String title = context.getString(R.string.app_name);
		 Intent notificationIntent = new Intent(context, WhereChatActivity.class);
		 notificationIntent.putExtra("id", Global.id);
		 notificationIntent.putExtra("channel","permanent-" + channel);
		 notificationIntent.putExtra("mobileKey",Global.mobileKey);
		 notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
		   | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		 PendingIntent intent = PendingIntent.getActivity(context, 0,
		   notificationIntent, 0);
		 notification.setLatestEventInfo(context, title, message, intent);
		 notification.flags |= Notification.FLAG_AUTO_CANCEL;
		 notificationManager.notify(Notification.DEFAULT_VIBRATE, notification);
		}
}
