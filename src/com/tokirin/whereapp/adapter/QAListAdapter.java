package com.tokirin.whereapp.adapter;

import java.util.ArrayList;
import java.util.List;


import com.tokirin.whereapp.R;
import com.tokirin.whereapp.model.QA;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class QAListAdapter extends ArrayAdapter<QA> {

	private Context context;
	private int resource;
	private ArrayList<QA> qaArrayList;
	private LayoutInflater layoutInflator;

	public QAListAdapter(Context context, int layoutResource,
			ArrayList<QA> objects) {
		super(context, layoutResource, objects);
		this.context = context;
		this.resource = layoutResource;
		this.qaArrayList = objects;
		this.layoutInflator = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		QA myQA = qaArrayList.get(position);
		if(myQA==null){
			Log.d("Question","QA is null");
		}
		QAholder holder = new QAholder();
		
		if(convertView == null){
			convertView = layoutInflator.inflate(resource, null);
			
			holder.qa_time = (TextView)convertView.findViewById(R.id.qa_timestamp);
			holder.qa_question = (TextView)convertView.findViewById(R.id.qa_question);
			holder.qa_answer = (TextView)convertView.findViewById(R.id.qa_answer);
			convertView.setTag(holder);
			Log.d("Question","convertView is null");
		}else{
			holder = (QAholder)convertView.getTag();
		}
		
		holder.qa_time.setText(myQA.qa_date);
		Log.d("Question",myQA.qa_date);
		holder.qa_question.setText(myQA.qa_question);
		Log.d("Question",myQA.qa_question);
		holder.qa_answer.setText(myQA.qa_answer);
		Log.d("Question",myQA.qa_answer);
		
		return convertView;
	}

	private class QAholder{
		
		TextView qa_time,qa_question,qa_answer;
	}
	
}
