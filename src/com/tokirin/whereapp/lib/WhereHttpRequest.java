package com.tokirin.whereapp.lib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.http.HttpResponse;


import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class WhereHttpRequest extends AsyncTask<Void, Void, HttpResponse> {

	public String method;
	public String url;
	public HashMap<String,String> params;
	public JSONObject jsonBody;
	public String responseContent;
	public interface Callback {
        void onSuccess(HttpResponse response);
        void onFailure(HttpResponse response);
    }

    private Set<Callback> callbacks = new HashSet<Callback>();

	public void addObserver(Callback cb) {
    	callbacks.add(cb);
    } 
	
	public WhereHttpRequest(String method, String url){
		this.method = method;
		this.url = url;
		
	}

	public WhereHttpRequest(String method, String url, JSONObject body){
		this.method = method;
		this.url = url;
		this.jsonBody = body;
	}
	
	public WhereHttpRequest(String method, String url, HashMap<String,String> params){
		this.method = method;
		this.url = url;
		this.params = params;
	}
	
	@Override
	protected HttpResponse doInBackground(Void ... arg0) {
		if(method.equals("POST_JSON")){
			HttpResponse response = postWithJson();
			return response;
		}else if(method.equals("POST")){
			Log.d("Login","POST REQUEST start");
			HttpResponse response = post();
			return response;
		}
		
		return null;
	}

	@Override
	protected void onPostExecute(HttpResponse result) {
		if(result==null){
			Log.d("Where","Fuckinggggg");
		}else{
			if (result.getStatusLine().getStatusCode() == 200) {
				for (Callback cb: callbacks) {
					cb.onSuccess(result);
				}
			} else {
				for (final Callback cb: callbacks) {
					cb.onFailure(result);
				}
			}
		}
	}
	
	public HttpResponse postWithJson(){
		try{
			HttpClient client =  new DefaultHttpClient();
			HttpPost mPost = new HttpPost(url);
			mPost.addHeader(new BasicHeader("Content-Type","application/json"));
			mPost.setEntity(new StringEntity(jsonBody.toString()));
			HttpResponse response = client.execute(mPost);
			responseContent = response.getStatusLine().getReasonPhrase();
			return response;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
	}

	public HttpResponse post(){
		try{
			HttpClient client =  new DefaultHttpClient();
			HttpPost mPost = new HttpPost(url);
			
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			
            for(String key: params.keySet()){
            	Log.d("Login",key + " : " + params.get(key));
    			nameValuePairs.add(new BasicNameValuePair(key, params.get(key)));
            }
            
            mPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf-8"));
            
			HttpResponse response = client.execute(mPost);
			responseContent = response.getStatusLine().getReasonPhrase();
			Log.d("Login",responseContent);
			return response;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
	}

	public static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
}
