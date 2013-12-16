package com.tokirin.whereapp.model;

import com.tokirin.whereapp.R;
import com.tokirin.whereapp.adapter.ChatListAdapter.rowType;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class Receiver implements Item{

	Chat chat;
	public Receiver(Chat chat){
		this.chat = chat;
	}
	
	@Override
	public int getViewType() {
		return rowType.RECEIVER.ordinal();
	}

	@Override
	public View getView(LayoutInflater inflater, View convertView) {
		Chatholder holder = new Chatholder();
		if(convertView == null){
			convertView = inflater.inflate(R.layout.row_receiver, null);

			holder.qa_time = (TextView)convertView.findViewById(R.id.receiver_timestamp);
			holder.qa_id = (TextView)convertView.findViewById(R.id.receiver_id);
			holder.qa_content = (TextView)convertView.findViewById(R.id.receiver_text);
			convertView.setTag(holder);
			
		}else{
			holder = (Chatholder)convertView.getTag();
		}

		holder.qa_time.setText(chat.time);
		holder.qa_id.setText(chat.id);
		holder.qa_content.setText(chat.content);
		
		return convertView;
	}


	private class Chatholder{
		
		TextView qa_time,qa_id,qa_content;
		
	}
	
}
