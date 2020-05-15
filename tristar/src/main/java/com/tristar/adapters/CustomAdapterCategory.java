package com.tristar.adapters;
import java.util.ArrayList;

import com.tristar.main.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

@SuppressWarnings("ALL")
public class CustomAdapterCategory extends BaseAdapter implements OnClickListener {

	private Activity activity;
	private ArrayList<?> data;
	private static LayoutInflater inflater = null;
	public Resources res;
	int i = 0;

 public CustomAdapterCategory(Activity a, ArrayList<?> d) {
		activity = a;
		data = d;
		
      
  		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	} 

	public int getCount() {

		if (data.size() <= 0)
			return 1;
		return data.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public static class ViewHolder {

		public TextView text;
		public TextView textWide;

	}

	@SuppressLint({"InflateParams", "SetTextI18n"})
	public View getView(int position, View convertView, ViewGroup parent) {

		View vi = convertView;
		ViewHolder holder;

		if (convertView == null) {

			vi = inflater.inflate(R.layout.textcategory, null);

			holder = new ViewHolder();
			holder.text = (TextView) vi.findViewById(R.id.txt_Category);
			vi.setTag(holder);
		} else
			holder = (ViewHolder) vi.getTag();

		if (data.size() <= 0) {
			holder.text.setText("No Data");

		} else {
		}
		return vi;
	}

	@Override
	public void onClick(View v) {
		
	}
}