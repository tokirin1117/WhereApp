package com.tokirin.whereapp.adapter;

import java.util.ArrayList;
import java.util.List;

import com.tokirin.whereapp.model.Item;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ChatListAdapter extends ArrayAdapter<Item> {

	private ArrayList<Item> items;
	private LayoutInflater inflater;

	
	public enum rowType{
		RECEIVER,SENDER,MEMBER
	}
	public ChatListAdapter(Context context,LayoutInflater inflater,ArrayList<Item> items) {
		super(context, 0, items);
		this.items = items;
		this.inflater = inflater;
	}
	
	
	@Override
	public int getItemViewType(int position) {
        return items.get(position).getViewType();
	}


	@Override
	public int getViewTypeCount() {
		return rowType.values().length;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        return items.get(position).getView(inflater, convertView);
        
	}

}
