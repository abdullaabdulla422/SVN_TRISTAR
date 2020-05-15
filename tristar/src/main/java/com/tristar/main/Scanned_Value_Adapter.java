package com.tristar.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Scanned_Value_Adapter extends BaseAdapter {

	ArrayList<String> stringArrayList = new ArrayList<>();
	Context mContext;
	private LayoutInflater inflater;

	public Scanned_Value_Adapter(ArrayList<String> stringArrayList, Context mContext) {
		this.stringArrayList = stringArrayList;
		this.mContext = mContext;
		inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return stringArrayList.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("SetTextI18n")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;

			if (view == null)
				view = inflater.inflate(R.layout.scanned_wo_child,null);
				TextView textView = (TextView) view.findViewById(R.id.scanned_wo_item);
				if (stringArrayList == null || stringArrayList.size() == 0){
					textView.setText(mContext.getResources().getString(R.string.No_Scanned_work));
				}else {
					textView.setText(stringArrayList.get(position));

				}


		return view;
	}
}
