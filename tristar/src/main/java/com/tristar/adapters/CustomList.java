package com.tristar.adapters;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tristar.main.R;
import com.tristar.object.Tristar;

@SuppressWarnings("ALL")
public class CustomList extends BaseAdapter implements OnClickListener {
	private Activity activity;
	private ArrayList<?> list;
	private static LayoutInflater inflater = null;
	public Resources res;
	Tristar object = null;

	public CustomList(Activity a, ArrayList<?> list) {
		activity = a;
		this.list = list;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		object = new Tristar();
	}

	public int getCount() {
		return list.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public static class ViewHolder {

		public TextView code;
		public TextView title;

	}

	@SuppressLint({"InflateParams", "SetTextI18n"})
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		ViewHolder holder;

		if (convertView == null) { 

			vi = inflater.inflate(R.layout.list_courtservice_items, null);

			holder = new ViewHolder();
			holder.code = (TextView) vi.findViewById(R.id.txt_udno);
			holder.title = (TextView) vi.findViewById(R.id.txt_addresslist);

			vi.setTag(holder);
		} else
			holder = (ViewHolder) vi.getTag();

		if (getCount() <= 0) {
			holder.code.setText("No Data");
			holder.title.setText("No Data");

		} else {
			object = (Tristar) list.get(position);
			holder.code.setText(object.getCode());
			holder.title.setText(object.getTitle());
		}
		return vi;
	}

	@Override
	public void onClick(DialogInterface arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("unused")
	private class OnItemClickListener implements OnClickListener {
		private int mPosition;

		@Override
		public void onClick(DialogInterface arg0, int arg1) {

		}
	}

}
