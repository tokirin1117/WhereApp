package com.tokirin.whereapp.model;

import com.tokirin.whereapp.R;
import com.tokirin.whereapp.adapter.ChatListAdapter.rowType;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class Member implements Item{

	String id;
	boolean code;
	public Member(String id,boolean code){
		this.id = id;
		this.code = code;
	}
	@Override
	public int getViewType() {
		return rowType.MEMBER.ordinal();
	}
	@Override
	public View getView(LayoutInflater inflater, View convertView) {
		if(convertView == null){
			convertView = inflater.inflate(R.layout.row_member_added, null);
		}

		TextView header = (TextView)convertView.findViewById(R.id.header);
		if(code){
			header.setText(id + " 님이 입장하셨습니다.");
		}else{
			header.setText(id + " 님이 퇴장하셨습니다.");
		}
		return convertView;
		
	}



}
